import { Injectable, HttpStatus } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { RedisService } from '../redis/redis.service';
import { BusinessException } from '../../common/filters/global-exception.filter';
import { ERROR_CODES } from '../../common/constants/error-codes';

const SENSITIVE_SETTING_KEYS = new Set([
  'price_includes_vat',
  'default_tax_rate_pct',
  'stock_count_approve_value_limit',
  'customer_group_thresholds',
  'accounting_lock_date',
]);

@Injectable()
export class SettingsService {
  constructor(
    private readonly prisma: PrismaService,
    private readonly redis: RedisService,
  ) {}

  async getAllSettings() {
    const cached = await this.redis.getClient().get('system:settings:all');
    if (cached) {
      return JSON.parse(cached);
    }

    const settings = await this.prisma.systemSetting.findMany({
      orderBy: { key: 'asc' },
    });

    await this.redis.getClient().set('system:settings:all', JSON.stringify(settings), 'EX', 3600);
    return settings;
  }

  async getSetting(key: string) {
    const cached = await this.redis.getClient().get(`system:settings:${key}`);
    if (cached) {
      return JSON.parse(cached);
    }

    const setting = await this.prisma.systemSetting.findUnique({
      where: { key },
    });

    if (setting) {
      await this.redis.getClient().set(`system:settings:${key}`, JSON.stringify(setting), 'EX', 3600);
    }
    return setting;
  }

  async updateSettings(
    updates: Array<{ key: string; value: any }>,
    userRole: string,
    empId: bigint,
  ) {
    const results = [];

    for (const item of updates) {
      if (SENSITIVE_SETTING_KEYS.has(item.key) && userRole !== 'OWNER') {
        throw new BusinessException(
          ERROR_CODES.PERMISSION_DENIED,
          `Khóa cấu hình ${item.key} chỉ Chủ cửa hàng (OWNER) mới được phép thay đổi.`,
          HttpStatus.FORBIDDEN,
        );
      }

      const updated = await this.prisma.systemSetting.update({
        where: { key: item.key },
        data: {
          value: item.value,
          updatedBy: empId,
        },
      });

      results.push(updated);
      await this.redis.getClient().del(`system:settings:${item.key}`);
    }

    await this.redis.getClient().del('system:settings:all');
    return results;
  }
}
