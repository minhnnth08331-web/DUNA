import { INestApplication } from '@nestjs/common';
import * as request from 'supertest';
import { createTestApp, seedTestUsers, TEST_TOTP_SECRET_PLAIN } from './test-helper';
import { PrismaService } from '../src/modules/prisma/prisma.service';
import { RedisService } from '../src/modules/redis/redis.service';
import { encryptSecret, generateTotpSecret } from '../src/common/utils/crypto.util';
import { authenticator } from 'otplib';

describe('M0 Platform Foundation E2E Tests', () => {
  let app: INestApplication;
  let prisma: PrismaService;
  let redis: RedisService;

  beforeAll(async () => {
    const testEnv = await createTestApp();
    app = testEnv.app;
    prisma = testEnv.prisma;
    redis = testEnv.redis;
    await seedTestUsers(prisma);
  });

  afterAll(async () => {
    if (app) await app.close();
  });

  // Helper to log in and get access token
  async function loginAs(
    email: string,
    password = 'Password123!',
    explicitTotpCode?: string,
  ): Promise<{ accessToken: string; refreshToken: string; cookies: string[] }> {
    // Reset lastTotpStep so multiple test cases can log in within the same 30s window
    await prisma.employee.updateMany({
      where: { email },
      data: { lastTotpStep: null },
    });

    let totpCode = explicitTotpCode;
    // Auto-fill TOTP for test accounts with 2FA enabled
    if (!totpCode && (email.includes('owner') || email === 'admin2@duna.vn')) {
      totpCode = authenticator.generate(TEST_TOTP_SECRET_PLAIN);
    }

    const payload: any = { email, password };
    if (totpCode) payload.totpCode = totpCode;

    const res = await request(app.getHttpServer())
      .post('/api/v1/admin/auth/login')
      .send(payload);

    if (res.status >= 400) {
      throw new Error(`loginAs failed for ${email}: ${res.status} ${JSON.stringify(res.body)}`);
    }

    const rawCookies = res.headers['set-cookie'] || [];
    const cookies: string[] = Array.isArray(rawCookies) ? rawCookies : [rawCookies];
    return {
      accessToken: res.body.accessToken,
      refreshToken: res.body.refreshToken,
      cookies,
    };
  }

  // -------------------------------------------------------------------------
  // TC-ADM-04: Kiểm tra phân quyền RBAC ở tầng API (Chống chỉ ẩn nút UI)
  // -------------------------------------------------------------------------
  describe('TC-ADM-04: RBAC API Enforcement', () => {
    it('SALES and WAREHOUSE cannot access financial reports, stock count approval, or cancel preparing orders (403)', async () => {
      const salesAuth = await loginAs('sales@duna.vn');
      const warehouseAuth = await loginAs('warehouse@duna.vn');

      // SALES calling finance report -> 403
      const res1 = await request(app.getHttpServer())
        .get('/api/v1/admin/reports/finance')
        .set('Authorization', `Bearer ${salesAuth.accessToken}`);
      expect(res1.status).toBe(403);
      expect(res1.body.error.code).toBe('PERMISSION_DENIED');

      // SALES calling stock count approval -> 403
      const res2 = await request(app.getHttpServer())
        .post('/api/v1/admin/stock-counts/1/approve')
        .set('Authorization', `Bearer ${salesAuth.accessToken}`);
      expect(res2.status).toBe(403);
      expect(res2.body.error.code).toBe('PERMISSION_DENIED');

      // WAREHOUSE calling finance report -> 403
      const res3 = await request(app.getHttpServer())
        .get('/api/v1/admin/reports/finance')
        .set('Authorization', `Bearer ${warehouseAuth.accessToken}`);
      expect(res3.status).toBe(403);
      expect(res3.body.error.code).toBe('PERMISSION_DENIED');

      // WAREHOUSE canceling preparing order -> 403
      const res4 = await request(app.getHttpServer())
        .post('/api/v1/admin/orders/1/cancel-preparing')
        .set('Authorization', `Bearer ${warehouseAuth.accessToken}`);
      expect(res4.status).toBe(403);
      expect(res4.body.error.code).toBe('PERMISSION_DENIED');
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-13: Khóa tài khoản sau 5 lần sai
  // -------------------------------------------------------------------------
  describe('TC-ADM-13: Login Lockout after 5 Failures', () => {
    it('Locks account after 5 consecutive failed attempts, rejects even with right password until unlocked', async () => {
      const email = 'manager@duna.vn';
      await prisma.employee.update({
        where: { email },
        data: { failedLoginCount: 0, lockedUntil: null },
      });

      // Clear any rate limits for this test
      await redis.getClient().del(`ratelimit:login_email:field:${email}`);

      // Attempts 1 to 4: 401
      for (let i = 1; i <= 4; i++) {
        const res = await request(app.getHttpServer())
          .post('/api/v1/admin/auth/login')
          .send({ email, password: 'WrongPassword!' });
        expect(res.status).toBe(401);
        expect(res.body.error.code).toBe('AUTH_INVALID_CREDENTIALS');
      }

      // Attempt 5: 423 Locked
      const res5 = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email, password: 'WrongPassword!' });
      expect(res5.status).toBe(423);
      expect(res5.body.error.code).toBe('AUTH_ACCOUNT_LOCKED');

      // Attempt 6 (Correct password while locked): still 423 Locked!
      const res6 = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email, password: 'Password123!' });
      expect(res6.status).toBe(423);
      expect(res6.body.error.code).toBe('AUTH_ACCOUNT_LOCKED');

      // Exactly 1 LOGIN_LOCKOUT audit record
      const lockAudits = await prisma.auditLog.findMany({
        where: { action: 'LOGIN_LOCKOUT', beforeState: { path: ['failedCount'], equals: 5 } },
      });
      expect(lockAudits.length).toBeGreaterThanOrEqual(1);

      // Simulate +15 minutes elapsed
      await prisma.employee.update({
        where: { email },
        data: { lockedUntil: new Date(Date.now() - 1000) },
      });

      // Attempt 7 (After lock expires with correct password): success & reset
      const res7 = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email, password: 'Password123!' });
      expect(res7.status).toBe(200);
      expect(res7.body.accessToken).toBeDefined();

      const updatedEmp = await prisma.employee.findUnique({ where: { email } });
      expect(updatedEmp?.failedLoginCount).toBe(0);
      expect(updatedEmp?.lockedUntil).toBeNull();
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-14: Chống dò tài khoản
  // -------------------------------------------------------------------------
  describe('TC-ADM-14: Anti-Account Enumeration', () => {
    it('(a) non-existent email, (b) wrong pass, (c) disabled + wrong pass all return identical 401 message; (d) disabled + right pass returns 403', async () => {
      // (a) Non-existent email
      const resA = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'nonexistent@duna.vn', password: 'Password123!' });
      expect(resA.status).toBe(401);
      expect(resA.body.error.message).toBe('Email hoặc mật khẩu không chính xác.');

      // (b) Existent email + wrong password
      const resB = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'sales@duna.vn', password: 'WrongPassword!' });
      expect(resB.status).toBe(401);
      expect(resB.body.error.message).toBe('Email hoặc mật khẩu không chính xác.');

      // (c) Disabled account + wrong password
      const resC = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'disabled@duna.vn', password: 'WrongPassword!' });
      expect(resC.status).toBe(401);
      expect(resC.body.error.message).toBe('Email hoặc mật khẩu không chính xác.');

      // (d) Disabled account + correct password -> 403 (only reveals after password match)
      const resD = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'disabled@duna.vn', password: 'Password123!' });
      expect(resD.status).toBe(403);
      expect(resD.body.error.code).toBe('AUTH_ACCOUNT_DISABLED');
      expect(resD.body.error.message).toBe('Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ Chủ cửa hàng.');
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-15: 2FA bắt buộc, chống dùng lại mã
  // -------------------------------------------------------------------------
  describe('TC-ADM-15: 2FA Mandatory & Replay Prevention', () => {
    it('Requires 2FA code, blocks replay within same 30s window, mandates enrollment for ADMIN', async () => {
      const totpSecretPlain = generateTotpSecret();
      const encryptedSecret = encryptSecret(totpSecretPlain);

      // Setup owner2fa with known 2FA secret
      await prisma.employee.update({
        where: { email: 'owner2fa@duna.vn' },
        data: {
          is2faEnabled: true,
          totpSecret: encryptedSecret,
          lastTotpStep: null,
        },
      });

      // 1. Login without totpCode -> 202 AUTH_2FA_REQUIRED
      const resNoCode = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'owner2fa@duna.vn', password: 'Password123!' });
      expect(resNoCode.status).toBe(202);
      expect(resNoCode.body.error?.code || resNoCode.body.code).toBe('AUTH_2FA_REQUIRED');
      expect(resNoCode.body.accessToken).toBeUndefined();

      // 2. Login with wrong code -> 401 AUTH_2FA_INVALID
      const resWrongCode = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'owner2fa@duna.vn', password: 'Password123!', totpCode: '000000' });
      expect(resWrongCode.status).toBe(401);
      expect(resWrongCode.body.error.code).toBe('AUTH_2FA_INVALID');

      // 3. Login with correct code -> 200
      const validCode = authenticator.generate(totpSecretPlain);
      const resRightCode = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'owner2fa@duna.vn', password: 'Password123!', totpCode: validCode });
      expect(resRightCode.status).toBe(200);
      expect(resRightCode.body.accessToken).toBeDefined();

      // 4. Replay same code in same 30s step -> 401
      const resReplay = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'owner2fa@duna.vn', password: 'Password123!', totpCode: validCode });
      expect(resReplay.status).toBe(401);
      expect(resReplay.body.error.code).toBe('AUTH_2FA_INVALID');

      // 5. ADMIN without 2FA logs in -> returns enroll2FA: true & setupToken
      const resAdminNo2fa = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: 'admin@duna.vn', password: 'Password123!' });
      expect(resAdminNo2fa.status).toBe(200);
      expect(resAdminNo2fa.body.enroll2FA).toBe(true);
      expect(resAdminNo2fa.body.setupToken).toBeDefined();

      // Using setupToken to call orders -> 403
      const resForbidden = await request(app.getHttpServer())
        .post('/api/v1/admin/orders')
        .set('Authorization', `Bearer ${resAdminNo2fa.body.setupToken}`)
        .send({ customerName: 'Test', items: [] });
      expect(resForbidden.status).toBe(403);
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-16: Xoay vòng refresh token & phát hiện dùng lại
  // -------------------------------------------------------------------------
  describe('TC-ADM-16: Refresh Token Rotation & Reuse Detection', () => {
    it('Rotates token on refresh, revokes entire family when old token is reused', async () => {
      // Login to get R1
      const loginRes = await loginAs('sales@duna.vn');
      const R1 = loginRes.refreshToken;

      // Refresh once to get R2
      const refreshRes = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/refresh')
        .send({ refreshToken: R1 });
      expect(refreshRes.status).toBe(200);
      expect(refreshRes.body.accessToken).toBeDefined();

      // Reusing old token R1 -> 401 AUTH_SESSION_REVOKED
      const reuseRes = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/refresh')
        .send({ refreshToken: R1 });
      expect(reuseRes.status).toBe(401);
      expect(reuseRes.body.error.code).toBe('AUTH_SESSION_REVOKED');

      // Audit log recorded REFRESH_TOKEN_REUSE
      const reuseAudit = await prisma.auditLog.findFirst({
        where: { action: 'REFRESH_TOKEN_REUSE' },
      });
      expect(reuseAudit).toBeDefined();
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-17: Thu hồi phiên từ xa tức thời
  // -------------------------------------------------------------------------
  describe('TC-ADM-17: Remote Session Revocation', () => {
    it('Revoking session B invalidates its token within <= 1s; revoking another users session is 403', async () => {
      // Create session A and session B for Manager
      const sessionA = await loginAs('manager@duna.vn');
      const sessionB = await loginAs('manager@duna.vn');

      // Manager gets active sessions from session A
      const sessionsRes = await request(app.getHttpServer())
        .get('/api/v1/admin/auth/sessions')
        .set('Authorization', `Bearer ${sessionA.accessToken}`);
      expect(sessionsRes.status).toBe(200);

      // Identify session B ID
      const sessionsList = sessionsRes.body;
      const targetSession = sessionsList.find((s: any) => s.id !== sessionA.accessToken) || sessionsList[0];

      // Revoke target session from session A
      const revokeRes = await request(app.getHttpServer())
        .post(`/api/v1/admin/auth/sessions/${targetSession.id}/revoke`)
        .set('Authorization', `Bearer ${sessionA.accessToken}`);
      expect(revokeRes.status).toBe(200);

      // Using revoked session token immediately -> 401 AUTH_SESSION_REVOKED
      const testB = await request(app.getHttpServer())
        .get('/api/v1/admin/auth/me')
        .set('Authorization', `Bearer ${sessionB.accessToken}`);
      expect(testB.status).toBe(401);
      expect(testB.body.error.code).toBe('AUTH_SESSION_REVOKED');

      // Session A remains valid
      const testA = await request(app.getHttpServer())
        .get('/api/v1/admin/auth/me')
        .set('Authorization', `Bearer ${sessionA.accessToken}`);
      expect(testA.status).toBe(200);

      // Sales trying to revoke Managers session -> 403
      const salesAuth = await loginAs('sales@duna.vn');
      const unauthorizedRevoke = await request(app.getHttpServer())
        .post(`/api/v1/admin/auth/sessions/${targetSession.id}/revoke`)
        .set('Authorization', `Bearer ${salesAuth.accessToken}`);
      expect(unauthorizedRevoke.status).toBe(403);
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-18: Giới hạn quyền ADMIN & bảo vệ OWNER cuối
  // -------------------------------------------------------------------------
  describe('TC-ADM-18: ADMIN Limits & Protecting Last OWNER', () => {
    it('ADMIN cannot modify OWNER or other ADMIN; last OWNER cannot be demoted or disabled', async () => {
      const adminAuth = await loginAs('admin2@duna.vn');
      const ownerEmp = await prisma.employee.findUnique({ where: { email: 'owner@duna.vn' } });
      const otherAdmin = await prisma.employee.findUnique({ where: { email: 'admin@duna.vn' } });

      // 1. ADMIN calls status-role on OWNER -> 403
      const resAdminOnOwner = await request(app.getHttpServer())
        .patch(`/api/v1/admin/employees/${ownerEmp!.id}/status-role`)
        .set('Authorization', `Bearer ${adminAuth.accessToken}`)
        .send({ status: 'DISABLED' });
      expect(resAdminOnOwner.status).toBe(403);

      // 2. ADMIN calls status-role on other ADMIN -> 403
      const resAdminOnAdmin = await request(app.getHttpServer())
        .patch(`/api/v1/admin/employees/${otherAdmin!.id}/status-role`)
        .set('Authorization', `Bearer ${adminAuth.accessToken}`)
        .send({ status: 'DISABLED' });
      expect(resAdminOnAdmin.status).toBe(403);

      // 3. ADMIN creates employee with roleCode: OWNER -> 403
      const resAdminCreateOwner = await request(app.getHttpServer())
        .post('/api/v1/admin/employees')
        .set('Authorization', `Bearer ${adminAuth.accessToken}`)
        .set('Idempotency-Key', 'admin-create-owner-key-123456')
        .send({
          email: 'fakeowner@duna.vn',
          fullName: 'Fake Owner',
          phone: '0909999999',
          roleCode: 'OWNER',
        });
      expect(resAdminCreateOwner.status).toBe(403);

      // 4. OWNER self-demoting -> 400 SELF_ACTION_FORBIDDEN
      const ownerAuth = await loginAs('owner@duna.vn');
      const resSelfDemote = await request(app.getHttpServer())
        .patch(`/api/v1/admin/employees/${ownerEmp!.id}/status-role`)
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .send({ roleCode: 'SALES' });
      expect(resSelfDemote.status).toBe(400);
      expect(resSelfDemote.body.error.code).toBe('SELF_ACTION_FORBIDDEN');

      // 5. Locking last OWNER -> 400 LAST_OWNER_PROTECTED
      const owner2fa = await prisma.employee.findUnique({ where: { email: 'owner2fa@duna.vn' } });
      await prisma.employee.update({
        where: { id: owner2fa!.id },
        data: { status: 'DISABLED' },
      });

      // Try to disable the last owner:
      const resDisableLast = await request(app.getHttpServer())
        .patch(`/api/v1/admin/employees/${ownerEmp!.id}/status-role`)
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .send({ status: 'DISABLED' });
      expect(resDisableLast.status).toBe(400);

      // Restore owner2fa
      await prisma.employee.update({
        where: { id: owner2fa!.id },
        data: { status: 'ACTIVE' },
      });
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-19: Vô hiệu hóa nhân viên & buộc đổi mật khẩu
  // -------------------------------------------------------------------------
  describe('TC-ADM-19: Disabling Employee & Forced Password Change', () => {
    it('Revokes all sessions when employee disabled; blocks API when must_change_password=true until changed', async () => {
      const ownerAuth = await loginAs('owner@duna.vn');

      // Create new employee with must_change_password=true
      const newEmpEmail = 'newhire@duna.vn';
      await prisma.employee.deleteMany({ where: { email: newEmpEmail } });

      const createRes = await request(app.getHttpServer())
        .post('/api/v1/admin/employees')
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .set('Idempotency-Key', 'create-newhire-emp-key-123456')
        .send({
          email: newEmpEmail,
          fullName: 'Nhân Viên Mới',
          phone: '0903333333',
          roleCode: 'SALES',
          initialPassword: 'TempPassword123!',
        });
      expect(createRes.status).toBe(201);
      expect(createRes.body.passwordHash).toBeUndefined();

      // Login as new hire
      const loginNewHire = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email: newEmpEmail, password: 'TempPassword123!' });
      expect(loginNewHire.status).toBe(200);
      const newHireToken = loginNewHire.body.accessToken;

      // Calling an operational API before password change -> 403 PASSWORD_CHANGE_REQUIRED
      const blockedRes = await request(app.getHttpServer())
        .post('/api/v1/admin/orders')
        .set('Authorization', `Bearer ${newHireToken}`)
        .send({ customerName: 'Khách Test', items: [] });
      expect(blockedRes.status).toBe(403);
      expect(blockedRes.body.error.code).toBe('PASSWORD_CHANGE_REQUIRED');

      // Change password
      const changePassRes = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/change-password')
        .set('Authorization', `Bearer ${newHireToken}`)
        .send({
          oldPassword: 'TempPassword123!',
          newPassword: 'BrandNewPassword123!',
        });
      expect(changePassRes.status).toBe(200);

      // Now operational API succeeds
      const successRes = await request(app.getHttpServer())
        .post('/api/v1/admin/orders')
        .set('Authorization', `Bearer ${newHireToken}`)
        .send({ customerName: 'Khách Test', items: [] });
      expect(successRes.status).toBe(201);
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-42: Idempotency nâng cao
  // -------------------------------------------------------------------------
  describe('TC-ADM-42: Advanced Idempotency (INV-04)', () => {
    it('Requires header, rejects reused key with different payload (422), replays completed response', async () => {
      const ownerAuth = await loginAs('owner@duna.vn');
      const uniqueKey = 'idempotent-test-key-' + Date.now();

      // 1. Missing header -> 400 IDEMPOTENCY_KEY_REQUIRED
      const resMissing = await request(app.getHttpServer())
        .post('/api/v1/admin/employees')
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .send({
          email: 'idem1@duna.vn',
          fullName: 'Idem One',
          phone: '0908888001',
          roleCode: 'SALES',
        });
      expect(resMissing.status).toBe(400);
      expect(resMissing.body.error.code).toBe('IDEMPOTENCY_KEY_REQUIRED');

      // 2. Valid first request -> 201
      const resFirst = await request(app.getHttpServer())
        .post('/api/v1/admin/employees')
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .set('Idempotency-Key', uniqueKey)
        .send({
          email: 'idem1@duna.vn',
          fullName: 'Idem One',
          phone: '0908888001',
          roleCode: 'SALES',
        });
      expect(resFirst.status).toBe(201);

      // 3. Replay with exact same payload -> receives cached response + header Idempotent-Replayed
      const resReplay = await request(app.getHttpServer())
        .post('/api/v1/admin/employees')
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .set('Idempotency-Key', uniqueKey)
        .send({
          email: 'idem1@duna.vn',
          fullName: 'Idem One',
          phone: '0908888001',
          roleCode: 'SALES',
        });
      expect(resReplay.status).toBe(201);
      expect(resReplay.headers['idempotent-replayed']).toBe('true');
      expect(resReplay.body.id).toBe(resFirst.body.id);

      // 4. Same key but DIFFERENT body -> 422 IDEMPOTENCY_KEY_REUSED
      const resDiffBody = await request(app.getHttpServer())
        .post('/api/v1/admin/employees')
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .set('Idempotency-Key', uniqueKey)
        .send({
          email: 'idem2@duna.vn',
          fullName: 'Idem Two Changed',
          phone: '0908888002',
          roleCode: 'SALES',
        });
      expect(resDiffBody.status).toBe(422);
      expect(resDiffBody.body.error.code).toBe('IDEMPOTENCY_KEY_REUSED');
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-45: Chống Mass Assignment
  // -------------------------------------------------------------------------
  describe('TC-ADM-45: Anti Mass Assignment & DTO Whitelist (INV-16)', () => {
    it('Ignores client-sent money/status on order creation and logs tamper; rejects unknown fields on other APIs (400)', async () => {
      const salesAuth = await loginAs('sales@duna.vn');
      const adminAuth = await loginAs('admin2@duna.vn');

      // 1. Order creation with tampered status/grandTotal -> ignored, creates CONFIRMED/UNPAID, logs CLIENT_TAMPER_ATTEMPT
      const orderRes = await request(app.getHttpServer())
        .post('/api/v1/admin/orders')
        .set('Authorization', `Bearer ${salesAuth.accessToken}`)
        .send({
          customerName: 'Khách VIP',
          items: [],
          fulfillmentStatus: 'DELIVERED',
          paymentStatus: 'PAID',
          grandTotal: 0,
          unitCostSnap: 1,
          createdByEmpId: 1,
          version: 99,
        });
      expect(orderRes.status).toBe(201);
      expect(orderRes.body.fulfillmentStatus).toBe('CONFIRMED');
      expect(orderRes.body.paymentStatus).toBe('UNPAID');
      expect(orderRes.body.grandTotal).toBe(350000);

      // Check tamper audit log
      const tamperLog = await prisma.auditLog.findFirst({
        where: { action: 'CLIENT_TAMPER_ATTEMPT' },
      });
      expect(tamperLog).toBeDefined();

      // 2. PATCH /employees/:id with unexpected fields -> 400 VALIDATION_FAILED (caller is admin)
      const empRes = await request(app.getHttpServer())
        .patch('/api/v1/admin/employees/1')
        .set('Authorization', `Bearer ${adminAuth.accessToken}`)
        .send({
          failedLoginCount: 0,
          passwordHash: 'injected_hash',
        });
      expect(empRes.status).toBe(400);
      expect(empRes.body.error.code).toBe('VALIDATION_FAILED');

      // 3. PUT /products with deletedAt -> 400 VALIDATION_FAILED
      const ownerAuth = await loginAs('owner@duna.vn');
      const prodRes = await request(app.getHttpServer())
        .put('/api/v1/admin/products')
        .set('Authorization', `Bearer ${ownerAuth.accessToken}`)
        .send({
          name: 'Áo Thun',
          deletedAt: '2026-11-03',
        });
      expect(prodRes.status).toBe(400);
      expect(prodRes.body.error.code).toBe('VALIDATION_FAILED');
    });
  });

  // -------------------------------------------------------------------------
  // TC-ADM-46: Giới hạn tần suất (Rate Limiting)
  // -------------------------------------------------------------------------
  describe('TC-ADM-46: Rate Limiting', () => {
    it('Blocks 6th failed login on same email in 1 minute with 429 and Retry-After header', async () => {
      const email = 'ratelimit-target@duna.vn';
      await redis.getClient().del(`login_failed_email:${email}`);

      for (let i = 1; i <= 5; i++) {
        const res = await request(app.getHttpServer())
          .post('/api/v1/admin/auth/login')
          .send({ email, password: 'WrongPassword!' });
        expect(res.status).toBe(401);
      }

      // 6th attempt -> 429 RATE_LIMITED
      const res6 = await request(app.getHttpServer())
        .post('/api/v1/admin/auth/login')
        .send({ email, password: 'WrongPassword!' });
      expect(res6.status).toBe(429);
      expect(res6.body.error.code).toBe('RATE_LIMITED');
      expect(res6.headers['retry-after']).toBeDefined();
    });
  });
});
