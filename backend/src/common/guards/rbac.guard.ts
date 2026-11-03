import {
  Injectable,
  CanActivate,
  ExecutionContext,
  HttpStatus,
} from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { JwtService } from '@nestjs/jwt';
import { PrismaService } from '../../modules/prisma/prisma.service';
import { RedisService } from '../../modules/redis/redis.service';
import {
  REQUIRE_PERMISSIONS_KEY,
  IS_PUBLIC_KEY,
  IS_SELF_SERVICE_KEY,
} from '../decorators/rbac.decorator';
import { BusinessException } from '../filters/global-exception.filter';
import { ERROR_CODES, ERROR_MESSAGES } from '../constants/error-codes';

@Injectable()
export class RbacGuard implements CanActivate {
  constructor(
    private readonly reflector: Reflector,
    private readonly jwtService: JwtService,
    private readonly prisma: PrismaService,
    private readonly redis: RedisService,
  ) {}

  async canActivate(context: ExecutionContext): Promise<boolean> {
    const isPublic = this.reflector.getAllAndOverride<boolean>(IS_PUBLIC_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);

    if (isPublic) {
      return true;
    }

    const isSelfService = this.reflector.getAllAndOverride<boolean>(
      IS_SELF_SERVICE_KEY,
      [context.getHandler(), context.getClass()],
    );

    const requiredPermissions = this.reflector.getAllAndOverride<string[]>(
      REQUIRE_PERMISSIONS_KEY,
      [context.getHandler(), context.getClass()],
    );

    // INV-08: Deny-by-default - If endpoint does not define permissions, is not self-service and is not public, reject
    if (!isSelfService && (!requiredPermissions || requiredPermissions.length === 0)) {
      throw new BusinessException(
        ERROR_CODES.PERMISSION_DENIED,
        ERROR_MESSAGES.PERMISSION_DENIED,
        HttpStatus.FORBIDDEN,
      );
    }

    const request = context.switchToHttp().getRequest();
    const authHeader = request.headers.authorization;

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      throw new BusinessException(
        ERROR_CODES.AUTH_TOKEN_EXPIRED,
        'Yêu cầu xác thực với Bearer token.',
        HttpStatus.UNAUTHORIZED,
      );
    }

    const token = authHeader.split(' ')[1];
    let payload: any;
    try {
      payload = this.jwtService.verify(token, {
        secret: process.env.JWT_ACCESS_SECRET || 'duna_access_secret_key_32bytes_min_dev_2026',
      });
    } catch (err: any) {
      throw new BusinessException(
        ERROR_CODES.AUTH_TOKEN_EXPIRED,
        ERROR_MESSAGES.AUTH_TOKEN_EXPIRED,
        HttpStatus.UNAUTHORIZED,
      );
    }

    // Token scope check: setupToken (scope: setup_2fa) cannot access business routes (TC-ADM-15)
    if (payload.scope && payload.scope !== 'full') {
      throw new BusinessException(
        ERROR_CODES.PERMISSION_DENIED,
        'Token chỉ dùng cho bước thiết lập xác thực 2 lớp.',
        HttpStatus.FORBIDDEN,
      );
    }

    // Check Redis blacklist for sid
    const isRevoked = await this.redis.isSessionBlacklisted(payload.sid);
    if (isRevoked) {
      throw new BusinessException(
        ERROR_CODES.AUTH_SESSION_REVOKED,
        ERROR_MESSAGES.AUTH_SESSION_REVOKED,
        HttpStatus.UNAUTHORIZED,
      );
    }

    // Check session in DB if still valid
    const session = await this.prisma.employeeSession.findUnique({
      where: { id: payload.sid },
      include: { employee: { include: { role: true } } },
    });

    if (!session || session.revokedAt) {
      throw new BusinessException(
        ERROR_CODES.AUTH_SESSION_REVOKED,
        ERROR_MESSAGES.AUTH_SESSION_REVOKED,
        HttpStatus.UNAUTHORIZED,
      );
    }

    const employee = session.employee;
    if (employee.status !== 'ACTIVE') {
      throw new BusinessException(
        ERROR_CODES.AUTH_ACCOUNT_DISABLED,
        ERROR_MESSAGES.AUTH_ACCOUNT_DISABLED,
        HttpStatus.FORBIDDEN,
      );
    }

    // Attach user to request
    request.user = {
      id: employee.id.toString(),
      sub: employee.id.toString(),
      sid: session.id,
      role: employee.role.code,
      roleId: employee.roleId,
      status: employee.status,
      mustChangePassword: employee.mustChangePassword,
    };

    // Mandatory password change check
    const path = request.route?.path || request.path;
    const isChangePasswordRoute =
      path.endsWith('/auth/change-password') || path === '/auth/change-password';

    if (employee.mustChangePassword && !isChangePasswordRoute) {
      throw new BusinessException(
        ERROR_CODES.PASSWORD_CHANGE_REQUIRED,
        ERROR_MESSAGES.PASSWORD_CHANGE_REQUIRED,
        HttpStatus.FORBIDDEN,
      );
    }

    // If self-service route, authenticated employee is allowed (§9.5)
    if (isSelfService) {
      return true;
    }

    // Check permissions
    const roleCode = employee.role.code;
    const currentVersion = await this.redis.getRolePermVersion(roleCode);
    let rolePerms = await this.redis.getRolePermissions(roleCode, currentVersion);

    if (!rolePerms) {
      const rpList = await this.prisma.rolePermission.findMany({
        where: { roleId: employee.roleId },
        include: { permission: true },
      });
      rolePerms = rpList.map(rp => rp.permission.code);
      await this.redis.cacheRolePermissions(roleCode, currentVersion, rolePerms);
    }

    const hasAll = requiredPermissions.every(p => rolePerms!.includes(p));
    if (!hasAll) {
      throw new BusinessException(
        ERROR_CODES.PERMISSION_DENIED,
        ERROR_MESSAGES.PERMISSION_DENIED,
        HttpStatus.FORBIDDEN,
      );
    }

    return true;
  }
}
