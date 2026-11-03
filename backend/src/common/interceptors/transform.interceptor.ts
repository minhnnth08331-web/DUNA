import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Response } from 'express';

function serializeBigInt(data: any): any {
  if (data === null || data === undefined) return data;
  if (typeof data === 'bigint') return data.toString();
  if (Array.isArray(data)) return data.map(serializeBigInt);
  if (typeof data === 'object' && !(data instanceof Date)) {
    const res: Record<string, any> = {};
    for (const [k, v] of Object.entries(data)) {
      res[k] = serializeBigInt(v);
    }
    return res;
  }
  return data;
}

@Injectable()
export class TransformInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const ctx = context.switchToHttp();
    const request = ctx.getRequest();
    const response = ctx.getResponse<Response>();

    const requestId =
      (request.headers['x-request-id'] as string) ||
      request.requestId ||
      'req-' + Date.now();

    response.setHeader('X-Request-Id', requestId);

    return next.handle().pipe(map(data => serializeBigInt(data)));
  }
}
