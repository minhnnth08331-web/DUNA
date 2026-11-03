import { Controller, Get, Param, Query, UseGuards } from '@nestjs/common';
import { AuditService } from './audit.service';
import { RequirePermissions } from '../../common/decorators/rbac.decorator';
import { RbacGuard } from '../../common/guards/rbac.guard';

@Controller('audit-logs')
@UseGuards(RbacGuard)
export class AuditController {
  constructor(private readonly auditService: AuditService) {}

  @Get()
  @RequirePermissions('audit.read')
  async listLogs(
    @Query('actorEmpId') actorEmpId?: string,
    @Query('action') action?: string,
    @Query('entityType') entityType?: string,
    @Query('entityId') entityId?: string,
    @Query('requestId') requestId?: string,
    @Query('limit') limit?: number,
    @Query('cursor') cursor?: string,
  ) {
    return this.auditService.findLogs({
      actorEmpId,
      action,
      entityType,
      entityId,
      requestId,
      limit,
      cursor,
    });
  }

  @Get(':id')
  @RequirePermissions('audit.read')
  async getLog(@Param('id') id: string) {
    return this.auditService.findById(BigInt(id));
  }
}
