import { Injectable, HttpStatus } from '@nestjs/common';
import * as crypto from 'crypto';
import { PrismaService } from '../prisma/prisma.service';
import { RedisService } from '../redis/redis.service';
import { AuditService } from '../audit/audit.service';
import { hashPassword } from '../../common/utils/crypto.util';
import { BusinessException } from '../../common/filters/global-exception.filter';
import { ERROR_CODES, ERROR_MESSAGES } from '../../common/constants/error-codes';
import { CreateEmployeeDto, UpdateEmployeeDto, UpdateStatusRoleDto } from './dto/employee.dto';

@Injectable()
export class EmployeeService {
  constructor(
    private readonly prisma: PrismaService,
    private readonly redis: RedisService,
    private readonly auditService: AuditService,
  ) {}

  async findAll(query: {
    q?: string;
    roleCode?: string;
    status?: string;
    page?: number;
    pageSize?: number;
  }) {
    const page = Math.max(1, query.page ? Number(query.page) : 1);
    const pageSize = Math.min(100, Math.max(1, query.pageSize ? Number(query.pageSize) : 20));
    const skip = (page - 1) * pageSize;

    const where: any = {};
    if (query.status) where.status = query.status;
    if (query.roleCode) where.role = { code: query.roleCode };
    if (query.q) {
      where.OR = [
        { fullName: { contains: query.q, mode: 'insensitive' } },
        { email: { contains: query.q, mode: 'insensitive' } },
        { employeeCode: { contains: query.q, mode: 'insensitive' } },
        { phone: { contains: query.q } },
      ];
    }

    const [total, items] = await Promise.all([
      this.prisma.employee.count({ where }),
      this.prisma.employee.findMany({
        where,
        skip,
        take: pageSize,
        orderBy: { id: 'desc' },
        select: {
          id: true,
          employeeCode: true,
          email: true,
          fullName: true,
          phone: true,
          status: true,
          is2faEnabled: true,
          roleId: true,
          role: { select: { code: true, name: true } },
          createdAt: true,
          lastLoginAt: true,
        },
      }),
    ]);

    return {
      data: items,
      meta: { page, pageSize, total },
    };
  }

  async findById(id: bigint) {
    const employee = await this.prisma.employee.findUnique({
      where: { id },
      select: {
        id: true,
        employeeCode: true,
        email: true,
        fullName: true,
        phone: true,
        status: true,
        is2faEnabled: true,
        roleId: true,
        role: { select: { code: true, name: true } },
        createdAt: true,
        lastLoginAt: true,
      },
    });

    if (!employee) {
      throw new BusinessException(ERROR_CODES.NOT_FOUND, 'Nhân viên không tồn tại.', HttpStatus.NOT_FOUND);
    }
    return employee;
  }

  async create(dto: CreateEmployeeDto, actor: { id: string; role: string }) {
    // Rule: ADMIN cannot create OWNER
    if (actor.role === 'ADMIN' && dto.roleCode === 'OWNER') {
      throw new BusinessException(
        ERROR_CODES.PERMISSION_DENIED,
        'Quản trị viên không có quyền tạo tài khoản Chủ cửa hàng.',
        HttpStatus.FORBIDDEN,
      );
    }

    const role = await this.prisma.role.findUnique({ where: { code: dto.roleCode } });
    if (!role) {
      throw new BusinessException(ERROR_CODES.VALIDATION_FAILED, 'Vai trò không hợp lệ.', HttpStatus.BAD_REQUEST);
    }

    const existingEmail = await this.prisma.employee.findUnique({ where: { email: dto.email.toLowerCase().trim() } });
    if (existingEmail) {
      throw new BusinessException(
        ERROR_CODES.CONFLICT_DUPLICATE,
        'Email đã tồn tại trên hệ thống.',
        HttpStatus.CONFLICT,
      );
    }

    const existingPhone = await this.prisma.employee.findUnique({ where: { phone: dto.phone.trim() } });
    if (existingPhone) {
      throw new BusinessException(
        ERROR_CODES.CONFLICT_DUPLICATE,
        'Số điện thoại đã tồn tại trên hệ thống.',
        HttpStatus.CONFLICT,
      );
    }

    const initialPassword = dto.initialPassword || crypto.randomBytes(6).toString('hex') + 'A1!';
    const passwordHash = await hashPassword(initialPassword);

    // Generate code EMP-XXXX
    const count = await this.prisma.employee.count();
    const employeeCode = `EMP-${String(count + 1).padStart(4, '0')}`;

    const newEmp = await this.prisma.employee.create({
      data: {
        employeeCode,
        email: dto.email.toLowerCase().trim(),
        passwordHash,
        fullName: dto.fullName.trim(),
        phone: dto.phone.trim(),
        roleId: role.id,
        status: 'ACTIVE',
        is2faEnabled: false,
        mustChangePassword: true,
      },
      include: { role: true },
    });

    await this.auditService.log({
      actorEmpId: BigInt(actor.id),
      action: 'EMPLOYEE_CREATED',
      entityType: 'EMPLOYEE',
      entityId: newEmp.id.toString(),
      afterState: {
        employeeCode: newEmp.employeeCode,
        email: newEmp.email,
        fullName: newEmp.fullName,
        role: newEmp.role.code,
      },
    });

    return {
      id: newEmp.id.toString(),
      employeeCode: newEmp.employeeCode,
      email: newEmp.email,
      fullName: newEmp.fullName,
      phone: newEmp.phone,
      role: newEmp.role.code,
      status: newEmp.status,
      mustChangePassword: newEmp.mustChangePassword,
    };
  }

  async update(id: bigint, dto: UpdateEmployeeDto, actor: { id: string; role: string }) {
    const target = await this.prisma.employee.findUnique({
      where: { id },
      include: { role: true },
    });

    if (!target) {
      throw new BusinessException(ERROR_CODES.NOT_FOUND, 'Nhân viên không tồn tại.', HttpStatus.NOT_FOUND);
    }

    // Rule: ADMIN cannot modify OWNER or other ADMIN
    if (actor.role === 'ADMIN') {
      if (target.role.code === 'OWNER' || (target.role.code === 'ADMIN' && target.id !== BigInt(actor.id))) {
        throw new BusinessException(
          ERROR_CODES.PERMISSION_DENIED,
          'Quản trị viên không có quyền thay đổi tài khoản Chủ cửa hàng hoặc Quản trị viên khác.',
          HttpStatus.FORBIDDEN,
        );
      }
    }

    const data: any = {};
    if (dto.fullName) data.fullName = dto.fullName.trim();
    if (dto.phone) {
      const existingPhone = await this.prisma.employee.findFirst({
        where: { phone: dto.phone.trim(), id: { not: id } },
      });
      if (existingPhone) {
        throw new BusinessException(ERROR_CODES.CONFLICT_DUPLICATE, 'Số điện thoại đã tồn tại.', HttpStatus.CONFLICT);
      }
      data.phone = dto.phone.trim();
    }

    let emailChanged = false;
    if (dto.email) {
      const emailNorm = dto.email.toLowerCase().trim();
      if (emailNorm !== target.email) {
        const existingEmail = await this.prisma.employee.findFirst({
          where: { email: emailNorm, id: { not: id } },
        });
        if (existingEmail) {
          throw new BusinessException(ERROR_CODES.CONFLICT_DUPLICATE, 'Email đã tồn tại.', HttpStatus.CONFLICT);
        }
        data.email = emailNorm;
        emailChanged = true;
      }
    }

    const updated = await this.prisma.employee.update({
      where: { id },
      data,
      include: { role: true },
    });

    // If email changed, revoke all sessions
    if (emailChanged) {
      await this.revokeAllEmployeeSessions(id);
    }

    await this.auditService.log({
      actorEmpId: BigInt(actor.id),
      action: 'EMPLOYEE_UPDATED',
      entityType: 'EMPLOYEE',
      entityId: id.toString(),
      afterState: data,
    });

    return {
      id: updated.id.toString(),
      employeeCode: updated.employeeCode,
      email: updated.email,
      fullName: updated.fullName,
      phone: updated.phone,
      role: updated.role.code,
      status: updated.status,
    };
  }

  async updateStatusRole(
    id: bigint,
    dto: UpdateStatusRoleDto,
    actor: { id: string; role: string },
  ) {
    const actorId = BigInt(actor.id);
    const target = await this.prisma.employee.findUnique({
      where: { id },
      include: { role: true },
    });

    if (!target) {
      throw new BusinessException(ERROR_CODES.NOT_FOUND, 'Nhân viên không tồn tại.', HttpStatus.NOT_FOUND);
    }

    // Rule 1: SELF_ACTION_FORBIDDEN (cannot change own role or status)
    if (target.id === actorId) {
      throw new BusinessException(
        ERROR_CODES.SELF_ACTION_FORBIDDEN,
        ERROR_MESSAGES.SELF_ACTION_FORBIDDEN,
        HttpStatus.BAD_REQUEST,
      );
    }

    // Rule 2: ADMIN cannot touch OWNER or other ADMIN
    if (actor.role === 'ADMIN') {
      if (target.role.code === 'OWNER' || target.role.code === 'ADMIN') {
        throw new BusinessException(
          ERROR_CODES.PERMISSION_DENIED,
          'Quản trị viên không có quyền thay đổi tài khoản Chủ cửa hàng hoặc Quản trị viên khác.',
          HttpStatus.FORBIDDEN,
        );
      }
      if (dto.roleCode === 'OWNER') {
        throw new BusinessException(
          ERROR_CODES.PERMISSION_DENIED,
          'Quản trị viên không có quyền nâng quyền lên Chủ cửa hàng.',
          HttpStatus.FORBIDDEN,
        );
      }
    }

    // Rule 3: LAST_OWNER_PROTECTED
    const isTargetOwner = target.role.code === 'OWNER';
    const isDemotingOrDisablingOwner =
      isTargetOwner &&
      ((dto.roleCode && dto.roleCode !== 'OWNER') ||
        (dto.status && dto.status !== 'ACTIVE'));

    if (isDemotingOrDisablingOwner) {
      const activeOwnersCount = await this.prisma.employee.count({
        where: {
          role: { code: 'OWNER' },
          status: 'ACTIVE',
        },
      });

      if (activeOwnersCount <= 1) {
        throw new BusinessException(
          ERROR_CODES.LAST_OWNER_PROTECTED,
          ERROR_MESSAGES.LAST_OWNER_PROTECTED,
          HttpStatus.BAD_REQUEST,
        );
      }
    }

    const data: any = {};
    let roleChanged = false;

    if (dto.roleCode && dto.roleCode !== target.role.code) {
      const newRole = await this.prisma.role.findUnique({ where: { code: dto.roleCode } });
      if (!newRole) {
        throw new BusinessException(ERROR_CODES.VALIDATION_FAILED, 'Vai trò không hợp lệ.', HttpStatus.BAD_REQUEST);
      }
      data.roleId = newRole.id;
      roleChanged = true;
    }

    if (dto.status && dto.status !== target.status) {
      data.status = dto.status;
      if (dto.reason) data.disabledReason = dto.reason;
    }

    const updated = await this.prisma.employee.update({
      where: { id },
      data,
      include: { role: true },
    });

    if (roleChanged) {
      await this.redis.invalidateRolePermissions(target.role.code);
      await this.revokeAllEmployeeSessions(id);
    }

    if (dto.status === 'DISABLED' || dto.status === 'LOCKED') {
      await this.revokeAllEmployeeSessions(id);
    }

    await this.auditService.log({
      actorEmpId: actorId,
      action: roleChanged ? 'EMPLOYEE_ROLE_CHANGED' : 'EMPLOYEE_STATUS_CHANGED',
      entityType: 'EMPLOYEE',
      entityId: id.toString(),
      afterState: { role: updated.role.code, status: updated.status, reason: dto.reason },
    });

    return {
      id: updated.id.toString(),
      employeeCode: updated.employeeCode,
      email: updated.email,
      fullName: updated.fullName,
      role: updated.role.code,
      status: updated.status,
    };
  }

  async unlock(id: bigint, reason: string, actorId: bigint) {
    const employee = await this.prisma.employee.findUnique({ where: { id } });
    if (!employee) {
      throw new BusinessException(ERROR_CODES.NOT_FOUND, 'Nhân viên không tồn tại.', HttpStatus.NOT_FOUND);
    }

    await this.prisma.employee.update({
      where: { id },
      data: {
        failedLoginCount: 0,
        lockedUntil: null,
      },
    });

    await this.auditService.log({
      actorEmpId: actorId,
      action: 'EMPLOYEE_UNLOCKED',
      entityType: 'EMPLOYEE',
      entityId: id.toString(),
      afterState: { reason },
    });

    return { message: 'Đã mở khóa tài khoản thành công.' };
  }

  async reset2fa(id: bigint, reason: string, actor: { id: string; role: string }) {
    const target = await this.prisma.employee.findUnique({
      where: { id },
      include: { role: true },
    });

    if (!target) {
      throw new BusinessException(ERROR_CODES.NOT_FOUND, 'Nhân viên không tồn tại.', HttpStatus.NOT_FOUND);
    }

    if (actor.role === 'ADMIN' && (target.role.code === 'OWNER' || target.role.code === 'ADMIN')) {
      throw new BusinessException(
        ERROR_CODES.PERMISSION_DENIED,
        'Quản trị viên không có quyền reset 2FA của Chủ cửa hàng hoặc Quản trị viên khác.',
        HttpStatus.FORBIDDEN,
      );
    }

    // Check last owner with 2FA protection
    if (target.role.code === 'OWNER') {
      const activeOwnersWith2fa = await this.prisma.employee.count({
        where: {
          role: { code: 'OWNER' },
          status: 'ACTIVE',
          is2faEnabled: true,
        },
      });

      if (activeOwnersWith2fa <= 1) {
        throw new BusinessException(
          ERROR_CODES.LAST_OWNER_PROTECTED,
          ERROR_MESSAGES.LAST_OWNER_PROTECTED,
          HttpStatus.BAD_REQUEST,
        );
      }
    }

    await this.prisma.employee.update({
      where: { id },
      data: {
        is2faEnabled: false,
        totpSecret: null,
      },
    });

    await this.prisma.employeeRecoveryCode.deleteMany({ where: { employeeId: id } });
    await this.revokeAllEmployeeSessions(id);

    await this.auditService.log({
      actorEmpId: BigInt(actor.id),
      action: '2FA_RESET',
      entityType: 'EMPLOYEE',
      entityId: id.toString(),
      afterState: { reason },
    });

    return { message: 'Đã đặt lại 2FA thành công.' };
  }

  private async revokeAllEmployeeSessions(employeeId: bigint) {
    const sessions = await this.prisma.employeeSession.findMany({
      where: { employeeId, revokedAt: null },
    });

    await this.prisma.employeeSession.updateMany({
      where: { employeeId, revokedAt: null },
      data: { revokedAt: new Date(), revokeReason: 'EMPLOYEE_DISABLED' },
    });

    for (const s of sessions) {
      await this.redis.blacklistSession(s.id);
    }
  }
}
