import { Controller, Get, Post, Put, Body, Param, UseGuards } from '@nestjs/common';
import { RequirePermissions, CurrentUser } from '../../common/decorators/rbac.decorator';
import { RbacGuard } from '../../common/guards/rbac.guard';
import { AuditService } from '../audit/audit.service';
import { IsNotEmpty, IsNumber, IsOptional, IsString, IsArray } from 'class-validator';

export class CreateOrderDto {
  @IsString()
  @IsNotEmpty()
  customerName: string;

  @IsArray()
  items: any[];

  // Tamper fields that clients might try to inject
  @IsOptional()
  fulfillmentStatus?: string;

  @IsOptional()
  paymentStatus?: string;

  @IsOptional()
  grandTotal?: number;

  @IsOptional()
  unitCostSnap?: number;

  @IsOptional()
  createdByEmpId?: number;

  @IsOptional()
  version?: number;
}

export class UpdateProductDto {
  @IsString()
  @IsNotEmpty()
  name: string;

  @IsOptional()
  @IsNumber()
  price?: number;
}

@Controller()
@UseGuards(RbacGuard)
export class TestEndpointsController {
  constructor(private readonly auditService: AuditService) {}

  @Get('reports/finance')
  @RequirePermissions('report.finance.read')
  async getFinanceReport() {
    return { data: 'Báo cáo tài chính' };
  }

  @Post('stock-counts/:id/approve')
  @RequirePermissions('stockcount.approve')
  async approveStockCount(@Param('id') id: string) {
    return { message: 'Đã duyệt kiểm kê' };
  }

  @Post('orders/:id/cancel-preparing')
  @RequirePermissions('order.transition.cancel_late')
  async cancelPreparingOrder(@Param('id') id: string) {
    return { message: 'Đã hủy đơn đang chuẩn bị' };
  }

  @Post('orders')
  @RequirePermissions('order.create')
  async createOrder(@Body() body: CreateOrderDto, @CurrentUser('id') empId: string) {
    // Check if client attempted to tamper with server-computed fields
    const hasTamper =
      body.fulfillmentStatus !== undefined ||
      body.paymentStatus !== undefined ||
      body.grandTotal !== undefined ||
      body.unitCostSnap !== undefined ||
      body.createdByEmpId !== undefined ||
      body.version !== undefined;

    if (hasTamper) {
      await this.auditService.log({
        actorEmpId: BigInt(empId),
        action: 'CLIENT_TAMPER_ATTEMPT',
        entityType: 'ORDER',
        beforeState: {
          attemptedFields: {
            fulfillmentStatus: body.fulfillmentStatus,
            paymentStatus: body.paymentStatus,
            grandTotal: body.grandTotal,
            unitCostSnap: body.unitCostSnap,
            createdByEmpId: body.createdByEmpId,
            version: body.version,
          },
        },
      });
    }

    // Server enforces the state: CONFIRMED / UNPAID
    return {
      id: 'ord-123456',
      fulfillmentStatus: 'CONFIRMED',
      paymentStatus: 'UNPAID',
      grandTotal: 350000,
      customerName: body.customerName,
    };
  }

  @Put('products')
  @RequirePermissions('product.write')
  async updateProduct(@Body() body: UpdateProductDto) {
    return { message: 'Cập nhật sản phẩm', data: body };
  }
}
