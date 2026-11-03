import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuditService } from '../../modules/audit/audit.service';
import { AUDIT_KEY, AuditOptions } from '../decorators/audit.decorator';

@Injectable()
export class AuditInterceptor implements NestInterceptor {
  constructor(
    private readonly reflector: Reflector,
    private readonly auditService: AuditService,
  ) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const options = this.reflector.getAllAndOverride<AuditOptions>(AUDIT_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);

    if (!options) {
      return next.handle();
    }

    const http = context.switchToHttp();
    const req = http.getRequest();

    const actorEmpId = req.user?.id ? BigInt(req.user.id) : null;
    const ipAddress =
      (req.headers['x-forwarded-for'] as string)?.split(',')[0]?.trim() ||
      req.socket.remoteAddress ||
      '127.0.0.1';
    const userAgent = req.headers['user-agent'] as string;
    const requestId =
      (req.headers['x-request-id'] as string) || req.requestId || 'req-' + Date.now();

    const beforeState = req.body ? { ...req.body } : null;

    return next.handle().pipe(
      tap(async resData => {
        try {
          const entityId = options.getEntityId
            ? options.getEntityId(req, resData)
            : req.params?.id || resData?.id?.toString() || null;

          await this.auditService.log({
            actorEmpId,
            action: options.action,
            entityType: options.entityType,
            entityId,
            beforeState,
            afterState: resData,
            ipAddress,
            userAgent,
            requestId,
          });
        } catch (err) {
          // Do not crash primary request on audit error
        }
      }),
    );
  }
}
