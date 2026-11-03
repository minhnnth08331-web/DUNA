import { Controller, Get, Patch, Body, UseGuards } from '@nestjs/common';
import { SettingsService } from './settings.service';
import { RequirePermissions, CurrentUser } from '../../common/decorators/rbac.decorator';
import { RbacGuard } from '../../common/guards/rbac.guard';

@Controller('settings')
@UseGuards(RbacGuard)
export class SettingsController {
  constructor(private readonly settingsService: SettingsService) {}

  @Get()
  @RequirePermissions('settings.read')
  async getSettings() {
    return this.settingsService.getAllSettings();
  }

  @Patch()
  @RequirePermissions('settings.write')
  async updateSettings(
    @Body() body: { settings: Array<{ key: string; value: any }> },
    @CurrentUser('role') role: string,
    @CurrentUser('id') id: string,
  ) {
    return this.settingsService.updateSettings(
      body.settings || [],
      role,
      BigInt(id),
    );
  }
}
