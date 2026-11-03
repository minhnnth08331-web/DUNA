import * as path from 'path';
import * as dotenv from 'dotenv';
dotenv.config({ path: path.resolve(__dirname, '../.env') });

import { Test, TestingModule } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import * as cookieParser from 'cookie-parser';
import * as crypto from 'crypto';
import { AppModule } from '../src/app.module';
import { PrismaService } from '../src/modules/prisma/prisma.service';
import { RedisService } from '../src/modules/redis/redis.service';
import { GlobalExceptionFilter } from '../src/common/filters/global-exception.filter';
import { TransformInterceptor } from '../src/common/interceptors/transform.interceptor';
import { CustomValidationPipe } from '../src/common/pipes/custom-validation.pipe';
import { hashPassword, encryptSecret } from '../src/common/utils/crypto.util';

export const TEST_TOTP_SECRET_PLAIN = 'JBSWY3DPEHPK3PXP';

export async function createTestApp(): Promise<{
  app: INestApplication;
  prisma: PrismaService;
  redis: RedisService;
}> {
  const moduleFixture: TestingModule = await Test.createTestingModule({
    imports: [AppModule],
  }).compile();

  const app = moduleFixture.createNestApplication();
  app.setGlobalPrefix('api/v1/admin');
  app.use(cookieParser());
  app.use((req: any, res: any, next: () => void) => {
    const requestId = req.headers['x-request-id'] || crypto.randomUUID();
    req.requestId = requestId;
    res.setHeader('X-Request-Id', requestId);
    next();
  });
  app.useGlobalPipes(new CustomValidationPipe());
  app.useGlobalFilters(new GlobalExceptionFilter());
  app.useGlobalInterceptors(new TransformInterceptor());

  await app.init();

  const prisma = app.get(PrismaService);
  const redis = app.get(RedisService);

  return { app, prisma, redis };
}

export async function seedTestUsers(prisma: PrismaService) {
  const defaultPassword = 'Password123!';
  const passwordHash = await hashPassword(defaultPassword);
  const encryptedTotp = encryptSecret(TEST_TOTP_SECRET_PLAIN);

  const roles = await prisma.role.findMany();
  const roleMap = new Map(roles.map(r => [r.code, r.id]));

  const users = [
    // OWNER with 2FA enabled
    { email: 'owner@duna.vn', roleCode: 'OWNER', fullName: 'Chủ Cửa Hàng', phone: '0901000001', is2faEnabled: true, totpSecret: encryptedTotp, mustChangePassword: false },
    // Second OWNER with 2FA enabled
    { email: 'owner2fa@duna.vn', roleCode: 'OWNER', fullName: 'Chủ Cửa Hàng 2FA', phone: '0901000002', is2faEnabled: true, totpSecret: encryptedTotp, mustChangePassword: false },
    // ADMIN without 2FA (for enrollment testing TC-ADM-15)
    { email: 'admin@duna.vn', roleCode: 'ADMIN', fullName: 'Quản Trị Viên Chưa 2FA', phone: '0901000003', is2faEnabled: false, totpSecret: null, mustChangePassword: false },
    // ADMIN with 2FA enabled (for operations testing TC-ADM-18)
    { email: 'admin2@duna.vn', roleCode: 'ADMIN', fullName: 'Quản Trị Viên 2FA', phone: '0901000004', is2faEnabled: true, totpSecret: encryptedTotp, mustChangePassword: false },
    // MANAGER
    { email: 'manager@duna.vn', roleCode: 'MANAGER', fullName: 'Quản Lý', phone: '0901000005', is2faEnabled: false, totpSecret: null, mustChangePassword: false },
    // SALES
    { email: 'sales@duna.vn', roleCode: 'SALES', fullName: 'Nhân Viên Sales', phone: '0901000006', is2faEnabled: false, totpSecret: null, mustChangePassword: false },
    // WAREHOUSE
    { email: 'warehouse@duna.vn', roleCode: 'WAREHOUSE', fullName: 'Nhân Viên Kho', phone: '0901000007', is2faEnabled: false, totpSecret: null, mustChangePassword: false },
    // ACCOUNTANT
    { email: 'accountant@duna.vn', roleCode: 'ACCOUNTANT', fullName: 'Kế Toán', phone: '0901000008', is2faEnabled: false, totpSecret: null, mustChangePassword: false },
    // DISABLED
    { email: 'disabled@duna.vn', roleCode: 'SALES', fullName: 'Nhân Viên Bị Khóa', phone: '0901000009', status: 'DISABLED', is2faEnabled: false, totpSecret: null, mustChangePassword: false },
  ];

  for (let i = 0; i < users.length; i++) {
    const u = users[i];
    const roleId = roleMap.get(u.roleCode)!;
    const employeeCode = `TEST-EMP-${String(i + 1).padStart(3, '0')}`;

    await prisma.employee.upsert({
      where: { email: u.email },
      update: {
        roleId,
        status: u.status || 'ACTIVE',
        is2faEnabled: u.is2faEnabled,
        totpSecret: u.totpSecret,
        mustChangePassword: u.mustChangePassword,
        failedLoginCount: 0,
        lockedUntil: null,
      },
      create: {
        employeeCode,
        email: u.email,
        passwordHash,
        fullName: u.fullName,
        phone: u.phone,
        roleId,
        status: u.status || 'ACTIVE',
        is2faEnabled: u.is2faEnabled,
        totpSecret: u.totpSecret,
        mustChangePassword: u.mustChangePassword,
      },
    });
  }
}
