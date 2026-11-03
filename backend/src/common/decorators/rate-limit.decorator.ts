import { SetMetadata } from '@nestjs/common';

export interface RateLimitOptions {
  limit: number;
  windowSeconds: number;
  keyPrefix?: string;
  byIp?: boolean;
  byBodyField?: string;
}

export const RATE_LIMIT_KEY = 'RATE_LIMIT_OPTIONS';
export const RateLimit = (options: RateLimitOptions) => SetMetadata(RATE_LIMIT_KEY, options);
