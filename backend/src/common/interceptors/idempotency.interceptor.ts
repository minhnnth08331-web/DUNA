import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  HttpStatus,
} from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { Observable, from, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import * as crypto from 'crypto';
import { Response } from 'express';
import { PrismaService } from '../../modules/prisma/prisma.service';
import { REQUIRE_IDEMPOTENCY_KEY } from '../decorators/idempotency.decorator';
import { BusinessException } from '../filters/global-exception.filter';
import { ERROR_CODES, ERROR_MESSAGES } from '../constants/error-codes';

function canonicalJson(obj: any): string {
  if (obj === null || obj === undefined) return '';
  if (typeof obj !== 'object') return JSON.stringify(obj);
  if (Array.isArray(obj)) {
    return '[' + obj.map(canonicalJson).join(',') + ']';
  }
  const keys = Object.keys(obj).sort();
  return '{' + keys.map(k => JSON.stringify(k) + ':' + canonicalJson(obj[k])).join(',') + '}';
}

@Injectable()
export class IdempotencyInterceptor implements NestInterceptor {
  constructor(
    private readonly reflector: Reflector,
    private readonly prisma: PrismaService,
  ) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const isRequired = this.reflector.getAllAndOverride<boolean>(REQUIRE_IDEMPOTENCY_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);

    if (!isRequired) {
      return next.handle();
    }

    const http = context.switchToHttp();
    const req = http.getRequest();
    const res = http.getResponse<Response>();

    const key = req.headers['idempotency-key'] as string;
    if (!key || typeof key !== 'string' || key.trim().length < 16 || key.trim().length > 100) {
      throw new BusinessException(
        ERROR_CODES.IDEMPOTENCY_KEY_REQUIRED,
        ERROR_MESSAGES.IDEMPOTENCY_KEY_REQUIRED,
        HttpStatus.BAD_REQUEST,
      );
    }

    const trimmedKey = key.trim();
    const endpoint = req.route?.path || req.path;
    const employeeId = BigInt(req.user?.id || req.user?.sub || 0);

    const bodyHash = crypto.createHash('sha256').update(canonicalJson(req.body || {})).digest('hex');
    const expiresAt = new Date(Date.now() + 24 * 60 * 60 * 1000);

    return from(this.checkOrAcquire(employeeId, endpoint, trimmedKey, bodyHash, expiresAt)).pipe(
      switchMap(existingRecord => {
        if (existingRecord) {
          // Replay cached result
          res.setHeader('Idempotent-Replayed', 'true');
          res.status(existingRecord.responseStatus || 200);
          return from(Promise.resolve(existingRecord.responseBody));
        }

        // Newly acquired: proceed to handler
        return next.handle().pipe(
          switchMap(data => {
            const status = res.statusCode || 200;
            return from(
              this.completeKey(employeeId, endpoint, trimmedKey, status, data).then(() => data),
            );
          }),
          catchError(err => {
            const status = err instanceof BusinessException ? err.getStatus() : err.status || 500;
            if (status >= 500) {
              // Delete key on 5xx so client can retry
              return from(
                this.deleteKey(employeeId, endpoint, trimmedKey).then(() => {
                  throw err;
                }),
              );
            } else {
              // Business client errors (4xx) are saved as completed responses
              const errBody = err.response || {
                error: { code: err.code || 'ERROR', message: err.message },
              };
              return from(
                this.completeKey(employeeId, endpoint, trimmedKey, status, errBody).then(() => {
                  throw err;
                }),
              );
            }
          }),
        );
      }),
    );
  }

  private async checkOrAcquire(
    employeeId: bigint,
    endpoint: string,
    key: string,
    requestHash: string,
    expiresAt: Date,
  ): Promise<any | null> {
    const existing = await this.prisma.idempotencyKey.findUnique({
      where: {
        employeeId_endpoint_key: {
          employeeId,
          endpoint,
          key,
        },
      },
    });

    if (existing) {
      if (existing.requestHash !== requestHash) {
        throw new BusinessException(
          ERROR_CODES.IDEMPOTENCY_KEY_REUSED,
          ERROR_MESSAGES.IDEMPOTENCY_KEY_REUSED,
          HttpStatus.UNPROCESSABLE_ENTITY,
        );
      }

      if (existing.status === 'IN_PROGRESS') {
        throw new BusinessException(
          ERROR_CODES.IDEMPOTENCY_IN_PROGRESS,
          ERROR_MESSAGES.IDEMPOTENCY_IN_PROGRESS,
          HttpStatus.CONFLICT,
        );
      }

      return existing;
    }

    try {
      await this.prisma.idempotencyKey.create({
        data: {
          employeeId,
          endpoint,
          key,
          requestHash,
          status: 'IN_PROGRESS',
          expiresAt,
        },
      });
      return null;
    } catch (createErr: any) {
      // Race condition insert conflict
      const raceExisting = await this.prisma.idempotencyKey.findUnique({
        where: {
          employeeId_endpoint_key: {
            employeeId,
            endpoint,
            key,
          },
        },
      });

      if (raceExisting) {
        if (raceExisting.requestHash !== requestHash) {
          throw new BusinessException(
            ERROR_CODES.IDEMPOTENCY_KEY_REUSED,
            ERROR_MESSAGES.IDEMPOTENCY_KEY_REUSED,
            HttpStatus.UNPROCESSABLE_ENTITY,
          );
        }
        if (raceExisting.status === 'IN_PROGRESS') {
          throw new BusinessException(
            ERROR_CODES.IDEMPOTENCY_IN_PROGRESS,
            ERROR_MESSAGES.IDEMPOTENCY_IN_PROGRESS,
            HttpStatus.CONFLICT,
          );
        }
        return raceExisting;
      }
      throw createErr;
    }
  }

  private async completeKey(
    employeeId: bigint,
    endpoint: string,
    key: string,
    status: number,
    body: any,
  ) {
    try {
      await this.prisma.idempotencyKey.update({
        where: {
          employeeId_endpoint_key: {
            employeeId,
            endpoint,
            key,
          },
        },
        data: {
          status: 'COMPLETED',
          responseStatus: status,
          responseBody: body !== undefined ? body : null,
        },
      });
    } catch {}
  }

  private async deleteKey(employeeId: bigint, endpoint: string, key: string) {
    try {
      await this.prisma.idempotencyKey.delete({
        where: {
          employeeId_endpoint_key: {
            employeeId,
            endpoint,
            key,
          },
        },
      });
    } catch {}
  }
}
