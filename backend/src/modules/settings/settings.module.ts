import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { SettingsService } from './settings.service';
import { SettingsController } from './settings.controller';

@Module({
  imports: [JwtModule.register({})],
  controllers: [SettingsController],
  providers: [SettingsService],
  exports: [SettingsService],
})
export class SettingsModule {}
