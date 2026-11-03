import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import Redis from 'ioredis';

@Injectable()
export class RedisService implements OnModuleInit, OnModuleDestroy {
  private client: Redis;

  onModuleInit() {
    this.client = new Redis({
      host: process.env.REDIS_HOST || 'localhost',
      port: Number(process.env.REDIS_PORT) || 6379,
      maxRetriesPerRequest: 3,
    });
  }

  async onModuleDestroy() {
    await this.client.quit();
  }

  getClient(): Redis {
    return this.client;
  }

  // Session Blacklist
  async blacklistSession(sessionId: string, ttlSeconds = 86400): Promise<void> {
    await this.client.set(`blacklist:session:${sessionId}`, '1', 'EX', ttlSeconds);
  }

  async isSessionBlacklisted(sessionId: string): Promise<boolean> {
    const val = await this.client.get(`blacklist:session:${sessionId}`);
    return val === '1';
  }

  // Role Permissions & permVersion
  async getRolePermVersion(roleCode: string): Promise<number> {
    const ver = await this.client.get(`rbac:perm_version:${roleCode}`);
    return ver ? parseInt(ver, 10) : 1;
  }

  async incrementRolePermVersion(roleCode: string): Promise<number> {
    return this.client.incr(`rbac:perm_version:${roleCode}`);
  }

  async getRolePermissions(roleCode: string, permVersion: number): Promise<string[] | null> {
    const cached = await this.client.get(`rbac:perms:${roleCode}:v${permVersion}`);
    if (!cached) return null;
    return JSON.parse(cached);
  }

  async cacheRolePermissions(roleCode: string, permVersion: number, perms: string[]): Promise<void> {
    await this.client.set(`rbac:perms:${roleCode}:v${permVersion}`, JSON.stringify(perms), 'EX', 86400);
  }

  async invalidateRolePermissions(roleCode: string): Promise<void> {
    await this.incrementRolePermVersion(roleCode);
  }

  // Rate Limiting (Sliding Window via Redis Sorted Set)
  async checkRateLimit(
    key: string,
    limit: number,
    windowSeconds: number,
  ): Promise<{ allowed: boolean; remaining: number; resetAfter: number }> {
    const now = Date.now();
    const clearBefore = now - windowSeconds * 1000;
    const redisKey = `ratelimit:${key}`;

    const multi = this.client.multi();
    multi.zremrangebyscore(redisKey, 0, clearBefore);
    multi.zadd(redisKey, now, `${now}-${Math.random()}`);
    multi.zcard(redisKey);
    multi.expire(redisKey, windowSeconds);

    const results = await multi.exec();
    const count = (results?.[2]?.[1] as number) || 0;

    const allowed = count <= limit;
    const remaining = Math.max(0, limit - count);
    const resetAfter = windowSeconds;

    return { allowed, remaining, resetAfter };
  }
}
