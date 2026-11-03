import {
  Controller,
  Get,
  Post,
  Patch,
  Body,
  Param,
  Query,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import { EmployeeService } from './employee.service';
import { RequirePermissions, CurrentUser } from '../../common/decorators/rbac.decorator';
import { RequireIdempotency } from '../../common/decorators/idempotency.decorator';
import { RbacGuard } from '../../common/guards/rbac.guard';
import { IdempotencyInterceptor } from '../../common/interceptors/idempotency.interceptor';
import { CreateEmployeeDto, UpdateEmployeeDto, UpdateStatusRoleDto } from './dto/employee.dto';

@Controller('employees')
@UseGuards(RbacGuard)
export class EmployeeController {
  constructor(private readonly employeeService: EmployeeService) {}

  @Get()
  @RequirePermissions('employee.read')
  async listEmployees(
    @Query('q') q?: string,
    @Query('roleCode') roleCode?: string,
    @Query('status') status?: string,
    @Query('page') page?: number,
    @Query('pageSize') pageSize?: number,
  ) {
    return this.employeeService.findAll({ q, roleCode, status, page, pageSize });
  }

  @Get(':id')
  @RequirePermissions('employee.read')
  async getEmployee(@Param('id') id: string) {
    return this.employeeService.findById(BigInt(id));
  }

  @Post()
  @RequirePermissions('employee.write')
  @RequireIdempotency()
  @UseInterceptors(IdempotencyInterceptor)
  async createEmployee(
    @Body() dto: CreateEmployeeDto,
    @CurrentUser() actor: any,
  ) {
    return this.employeeService.create(dto, actor);
  }

  @Patch(':id')
  @RequirePermissions('employee.write')
  async updateEmployee(
    @Param('id') id: string,
    @Body() dto: UpdateEmployeeDto,
    @CurrentUser() actor: any,
  ) {
    return this.employeeService.update(BigInt(id), dto, actor);
  }

  @Patch(':id/status-role')
  @RequirePermissions('employee.write')
  async updateStatusRole(
    @Param('id') id: string,
    @Body() dto: UpdateStatusRoleDto,
    @CurrentUser() actor: any,
  ) {
    return this.employeeService.updateStatusRole(BigInt(id), dto, actor);
  }

  @Post(':id/unlock')
  @RequirePermissions('employee.security_reset')
  async unlock(
    @Param('id') id: string,
    @Body('reason') reason: string,
    @CurrentUser('id') actorId: string,
  ) {
    return this.employeeService.unlock(BigInt(id), reason || 'Mở khóa bởi quản trị viên', BigInt(actorId));
  }

  @Post(':id/reset-2fa')
  @RequirePermissions('employee.security_reset')
  async reset2fa(
    @Param('id') id: string,
    @Body('reason') reason: string,
    @CurrentUser() actor: any,
  ) {
    return this.employeeService.reset2fa(BigInt(id), reason || 'Reset 2FA', actor);
  }
}
