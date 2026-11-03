import { Module } from '@nestjs/common';
import { PrismaModule } from './modules/prisma/prisma.module';
import { RedisModule } from './modules/redis/redis.module';
import { AuditModule } from './modules/audit/audit.module';
import { AuthModule } from './modules/auth/auth.module';
import { EmployeeModule } from './modules/employees/employee.module';
import { SettingsModule } from './modules/settings/settings.module';
import { TestSupportModule } from './modules/test-support/test-support.module';

@Module({
  imports: [
    PrismaModule,
    RedisModule,
    AuditModule,
    AuthModule,
    EmployeeModule,
    SettingsModule,
    TestSupportModule,
  ],
})
export class AppModule {}
