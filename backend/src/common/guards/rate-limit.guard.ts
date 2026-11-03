import {
  Injectable,
  CanActivate,
  ExecutionContext,
  HttpStatus,
} from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { Response } from 'express';
import { RedisService } from '../../modules/redis/redis.service';
import { RATE_LIMIT_KEY, RateLimitOptions } from '../decorators/rate-limit.decorator';
import { BusinessException } from '../filters/global-exception.filter';
import { ERROR_CODES, ERROR_MESSAGES } from '../constants/error-codes';

@Injectable()
export class RateLimitGuard implements CanActivate {
  constructor(
    private readonly reflector: Reflector,
    private readonly redis: RedisService,
  ) {}

  async canActivate(context: ExecutionContext): Promise<boolean> {
    const options = this.reflector.getAllAndOverride<RateLimitOptions>(RATE_LIMIT_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);

    if (!options) {
      return true;
    }

    const http = context.switchToHttp();
    const req = http.getRequest();
    const res = http.getResponse<Response>();

    const ip =
      (req.headers['x-forwarded-for'] as string)?.split(',')[0]?.trim() ||
      req.socket.remoteAddress ||
      '127.0.0.1';

    let rateKey = options.keyPrefix || req.route?.path || req.path;

    if (options.byBodyField && req.body?.[options.byBodyField]) {
      const fieldVal = String(req.body[options.byBodyField]).toLowerCase().trim();
      rateKey = `${rateKey}:field:${fieldVal}`;
    } else if (options.byIp !== false) {
      rateKey = `${rateKey}:ip:${ip}`;
    }

    const result = await this.redis.checkRateLimit(
      rateKey,
      options.limit,
      options.windowSeconds,
    );

    res.setHeader('X-RateLimit-Limit', options.limit);
    res.setHeader('X-RateLimit-Remaining', result.remaining);

    if (!result.allowed) {
      res.setHeader('Retry-After', result.resetAfter);
      throw new BusinessException(
        ERROR_CODES.RATE_LIMITED,
        ERROR_MESSAGES.RATE_LIMITED,
        HttpStatus.TOO_MANY_REQUESTS,
      );
    }

    return true;
  }
}
