import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

const ROLES = [
  { code: 'OWNER', name: 'Chủ cửa hàng', refundLimit: null },
  { code: 'ADMIN', name: 'Quản trị viên', refundLimit: null },
  { code: 'MANAGER', name: 'Quản lý cửa hàng', refundLimit: 1000000n },
  { code: 'SALES', name: 'Nhân viên bán hàng', refundLimit: 0n },
  { code: 'WAREHOUSE', name: 'Nhân viên kho', refundLimit: 0n },
  { code: 'ACCOUNTANT', name: 'Kế toán', refundLimit: 0n },
];

const PERMISSION_DEFINITIONS = [
  // AUDIT
  { code: 'audit.read', module: 'SYSTEM' },

  // CATEGORY
  { code: 'category.read', module: 'PRODUCT' },
  { code: 'category.write', module: 'PRODUCT' },

  // COST PRICE
  { code: 'cost_price.read', module: 'FINANCE' },

  // CUSTOMER
  { code: 'customer.anonymize', module: 'CUSTOMER' },
  { code: 'customer.merge', module: 'CUSTOMER' },
  { code: 'customer.override', module: 'CUSTOMER' },
  { code: 'customer.read', module: 'CUSTOMER' },
  { code: 'customer.write', module: 'CUSTOMER' },

  // DASHBOARD
  { code: 'dashboard.read', module: 'SYSTEM' },

  // EMPLOYEE
  { code: 'employee.read', module: 'SYSTEM' },
  { code: 'employee.security_reset', module: 'SYSTEM' },
  { code: 'employee.write', module: 'SYSTEM' },

  // EXPENSE
  { code: 'expense.delete', module: 'FINANCE' },
  { code: 'expense.read', module: 'FINANCE' },
  { code: 'expense.write', module: 'FINANCE' },

  // INVENTORY
  { code: 'inventory.adjust', module: 'INVENTORY' },
  { code: 'inventory.ledger_read', module: 'INVENTORY' },
  { code: 'inventory.read', module: 'INVENTORY' },
  { code: 'inventory.transfer_condition', module: 'INVENTORY' },

  // JOB
  { code: 'job.monitor', module: 'SYSTEM' },

  // ORDER
  { code: 'order.create', module: 'ORDER' },
  { code: 'order.edit', module: 'ORDER' },
  { code: 'order.print', module: 'ORDER' },
  { code: 'order.read', module: 'ORDER' },
  { code: 'order.transition.cancel_early', module: 'ORDER' },
  { code: 'order.transition.cancel_failed', module: 'ORDER' },
  { code: 'order.transition.cancel_late', module: 'ORDER' },
  { code: 'order.transition.confirm', module: 'ORDER' },
  { code: 'order.transition.deliver', module: 'ORDER' },
  { code: 'order.transition.prepare', module: 'ORDER' },
  { code: 'order.transition.receive_return', module: 'ORDER' },
  { code: 'order.transition.reship', module: 'ORDER' },
  { code: 'order.transition.ship', module: 'ORDER' },

  // PAYMENT
  { code: 'payment.confirm_manual', module: 'FINANCE' },
  { code: 'payment.webhook_read', module: 'FINANCE' },

  // PO
  { code: 'po.read', module: 'INVENTORY' },
  { code: 'po.receive', module: 'INVENTORY' },
  { code: 'po.write', module: 'INVENTORY' },

  // PRODUCT
  { code: 'product.delete', module: 'PRODUCT' },
  { code: 'product.export', module: 'PRODUCT' },
  { code: 'product.import', module: 'PRODUCT' },
  { code: 'product.read', module: 'PRODUCT' },
  { code: 'product.write', module: 'PRODUCT' },

  // RECONCILE
  { code: 'reconcile.accept_diff', module: 'FINANCE' },
  { code: 'reconcile.read', module: 'FINANCE' },
  { code: 'reconcile.write', module: 'FINANCE' },

  // REFUND
  { code: 'refund.approve', module: 'FINANCE' },
  { code: 'refund.complete', module: 'FINANCE' },
  { code: 'refund.request', module: 'FINANCE' },

  // REPORT
  { code: 'report.finance.export', module: 'FINANCE' },
  { code: 'report.finance.read', module: 'FINANCE' },

  // RETURN
  { code: 'return.approve', module: 'ORDER' },
  { code: 'return.create', module: 'ORDER' },
  { code: 'return.inspect', module: 'ORDER' },

  // SESSION
  { code: 'session.manage', module: 'SYSTEM' },

  // SETTINGS
  { code: 'settings.read', module: 'SYSTEM' },
  { code: 'settings.write', module: 'SYSTEM' },
  { code: 'settings.write_sensitive', module: 'SYSTEM' },

  // STOCKCOUNT
  { code: 'stockcount.approve', module: 'INVENTORY' },
  { code: 'stockcount.count', module: 'INVENTORY' },

  // SUPPLIER
  { code: 'supplier.read', module: 'INVENTORY' },
  { code: 'supplier.write', module: 'INVENTORY' },

  // VOUCHER
  { code: 'voucher.admin', module: 'VOUCHER' },
  { code: 'voucher.read', module: 'VOUCHER' },
  { code: 'voucher.write', module: 'VOUCHER' },
];

const ROLE_PERMISSIONS_MATRIX: Record<string, string[]> = {
  OWNER: [
    'audit.read', 'category.read', 'category.write', 'cost_price.read', 'customer.anonymize', 'customer.merge',
    'customer.override', 'customer.read', 'customer.write', 'dashboard.read', 'employee.read', 'employee.security_reset',
    'employee.write', 'expense.delete', 'expense.read', 'expense.write', 'inventory.adjust', 'inventory.ledger_read',
    'inventory.read', 'inventory.transfer_condition', 'job.monitor', 'order.create', 'order.edit', 'order.print',
    'order.read', 'order.transition.cancel_early', 'order.transition.cancel_failed', 'order.transition.cancel_late',
    'order.transition.confirm', 'order.transition.deliver', 'order.transition.prepare', 'order.transition.receive_return',
    'order.transition.reship', 'order.transition.ship', 'payment.confirm_manual', 'payment.webhook_read', 'po.read',
    'po.receive', 'po.write', 'product.delete', 'product.export', 'product.import', 'product.read', 'product.write',
    'reconcile.accept_diff', 'reconcile.read', 'reconcile.write', 'refund.approve', 'refund.complete', 'refund.request',
    'report.finance.export', 'report.finance.read', 'return.approve', 'return.create', 'return.inspect', 'session.manage',
    'settings.read', 'settings.write', 'settings.write_sensitive', 'stockcount.approve', 'stockcount.count', 'supplier.read',
    'supplier.write', 'voucher.admin', 'voucher.read', 'voucher.write'
  ],
  ADMIN: [
    'audit.read', 'category.read', 'category.write', 'cost_price.read', 'customer.anonymize', 'customer.merge',
    'customer.override', 'customer.read', 'customer.write', 'dashboard.read', 'employee.read', 'employee.security_reset',
    'employee.write', 'expense.read', 'inventory.adjust', 'inventory.ledger_read', 'inventory.read', 'inventory.transfer_condition',
    'job.monitor', 'order.create', 'order.edit', 'order.print', 'order.read', 'order.transition.cancel_early',
    'order.transition.cancel_failed', 'order.transition.cancel_late', 'order.transition.confirm', 'order.transition.deliver',
    'order.transition.prepare', 'order.transition.receive_return', 'order.transition.reship', 'order.transition.ship',
    'payment.confirm_manual', 'payment.webhook_read', 'po.read', 'po.receive', 'po.write', 'product.delete', 'product.export',
    'product.import', 'product.read', 'product.write', 'reconcile.accept_diff', 'reconcile.read', 'refund.approve',
    'refund.complete', 'refund.request', 'report.finance.export', 'report.finance.read', 'return.approve', 'return.create',
    'return.inspect', 'session.manage', 'settings.read', 'settings.write', 'stockcount.approve', 'stockcount.count',
    'supplier.read', 'supplier.write', 'voucher.read', 'voucher.write'
  ],
  MANAGER: [
    'audit.read', 'category.read', 'category.write', 'cost_price.read', 'customer.merge', 'customer.override',
    'customer.read', 'customer.write', 'dashboard.read', 'expense.read', 'inventory.adjust', 'inventory.ledger_read',
    'inventory.read', 'inventory.transfer_condition', 'order.create', 'order.edit', 'order.print', 'order.read',
    'order.transition.cancel_early', 'order.transition.cancel_failed', 'order.transition.cancel_late', 'order.transition.confirm',
    'order.transition.deliver', 'order.transition.prepare', 'order.transition.receive_return', 'order.transition.reship',
    'order.transition.ship', 'payment.confirm_manual', 'po.read', 'po.receive', 'po.write', 'product.delete', 'product.export',
    'product.import', 'product.read', 'product.write', 'reconcile.read', 'refund.approve', 'refund.request',
    'report.finance.export', 'report.finance.read', 'return.approve', 'return.create', 'return.inspect', 'settings.read',
    'stockcount.approve', 'stockcount.count', 'supplier.read', 'supplier.write', 'voucher.read', 'voucher.write'
  ],
  SALES: [
    'category.read', 'customer.read', 'customer.write', 'inventory.read', 'order.create', 'order.edit', 'order.print',
    'order.read', 'order.transition.cancel_early', 'order.transition.confirm', 'product.read', 'refund.request',
    'return.create', 'voucher.read'
  ],
  WAREHOUSE: [
    'category.read', 'customer.read', 'inventory.ledger_read', 'inventory.read', 'inventory.transfer_condition',
    'order.print', 'order.read', 'order.transition.deliver', 'order.transition.prepare', 'order.transition.receive_return',
    'order.transition.reship', 'order.transition.ship', 'po.read', 'po.receive', 'product.read', 'return.inspect',
    'stockcount.count', 'supplier.read'
  ],
  ACCOUNTANT: [
    'audit.read', 'category.read', 'cost_price.read', 'customer.read', 'expense.read', 'expense.write',
    'inventory.ledger_read', 'inventory.read', 'order.read', 'payment.confirm_manual', 'payment.webhook_read',
    'po.read', 'product.export', 'product.read', 'reconcile.read', 'reconcile.write', 'refund.complete',
    'report.finance.export', 'report.finance.read', 'settings.read', 'supplier.read', 'voucher.read'
  ]
};

const SYSTEM_SETTINGS = [
  { key: 'access_token_ttl_min', value: 15, valueType: 'NUMBER', description: 'Thời gian sống access token (phút)' },
  { key: 'refresh_token_ttl_days', value: 7, valueType: 'NUMBER', description: 'Thời gian sống refresh token (ngày)' },
  { key: 'session_idle_timeout_min', value: 60, valueType: 'NUMBER', description: 'Thời gian tối đa không hoạt động (phút)' },
  { key: 'max_sessions_per_employee', value: 5, valueType: 'NUMBER', description: 'Số phiên đồng thời tối đa mỗi nhân viên' },
  { key: 'login_max_failed', value: 5, valueType: 'NUMBER', description: 'Số lần đăng nhập sai tối đa trước khi khóa' },
  { key: 'login_lockout_minutes', value: 15, valueType: 'NUMBER', description: 'Thời gian khóa đăng nhập (phút)' },
  { key: 'idempotency_ttl_hours', value: 24, valueType: 'NUMBER', description: 'Thời gian lưu trữ Idempotency Key (giờ)' },
  { key: 'price_includes_vat', value: true, valueType: 'BOOLEAN', description: 'Giá niêm yết đã bao gồm VAT' },
  { key: 'default_tax_rate_pct', value: 8, valueType: 'NUMBER', description: 'Thuế suất VAT mặc định (%)' },
  { key: 'return_window_days', value: 7, valueType: 'NUMBER', description: 'Thời hạn đổi trả tính từ khi giao thành công (ngày)' },
  { key: 'return_ship_deadline_days', value: 7, valueType: 'NUMBER', description: 'Thời hạn gửi hàng sau khi duyệt đổi trả (ngày)' },
  { key: 'max_delivery_attempts', value: 3, valueType: 'NUMBER', description: 'Số lần giao hàng lại tối đa' },
  { key: 'cod_block_threshold', value: 3, valueType: 'NUMBER', description: 'Số lần giao thất bại để khóa COD' },
  { key: 'cod_max_amount', value: 0, valueType: 'NUMBER', description: 'Hạn mức COD tối đa (0 = không giới hạn)' },
  { key: 'reservation_ttl_minutes', value: 30, valueType: 'NUMBER', description: 'Thời gian giữ hàng đơn Website (phút)' },
  { key: 'telesales_prepay_hold_hours', value: 24, valueType: 'NUMBER', description: 'Thời gian giữ hàng đơn Telesales chuyển khoản (giờ)' },
  { key: 'pending_confirm_alert_hours', value: 24, valueType: 'NUMBER', description: 'Ngưỡng cảnh báo đơn chờ xác nhận (giờ)' },
  { key: 'inspecting_alert_days', value: 3, valueType: 'NUMBER', description: 'Ngưỡng cảnh báo hàng chờ kiểm định (ngày)' },
  { key: 'stock_count_expire_hours', value: 24, valueType: 'NUMBER', description: 'Thời hạn hoàn thành phiếu kiểm kê (giờ)' },
  { key: 'stock_count_approve_value_limit', value: 5000000, valueType: 'NUMBER', description: 'Ngưỡng giá trị chênh lệch kiểm kê cần ADMIN/OWNER duyệt' },
  { key: 'low_stock_default_threshold', value: 5, valueType: 'NUMBER', description: 'Ngưỡng cảnh báo sắp hết hàng mặc định' },
  { key: 'price_change_warn_pct', value: 50, valueType: 'NUMBER', description: 'Ngưỡng % đổi giá lớn cần xác nhận' },
  { key: 'customer_group_thresholds', value: { SILVER: 2000000, GOLD: 5000000, VIP: 15000000 }, valueType: 'JSON', description: 'Ngưỡng chi tiêu phân hạng khách hàng' },
  { key: 'expense_attachment_required_from', value: 1000000, valueType: 'NUMBER', description: 'Ngưỡng chi phí bắt buộc đính kèm chứng từ' },
  { key: 'accounting_lock_date', value: null, valueType: 'STRING', description: 'Ngày khóa sổ kế toán' },
  { key: 'cod_diff_tolerance', value: 0, valueType: 'NUMBER', description: 'Dung sai chênh lệch đối soát COD (VND)' },
  { key: 'cod_overdue_days', value: 14, valueType: 'NUMBER', description: 'Ngưỡng quá hạn đối soát COD (ngày)' },
  { key: 'cod_import_mappings', value: {}, valueType: 'JSON', description: 'Cấu hình ánh xạ cột đối soát COD theo hãng' },
  { key: 'export_row_limit', value: 50000, valueType: 'NUMBER', description: 'Giới hạn số dòng export tối đa' },
  { key: 'store_info', value: { name: "DUNA Men's Fashion", address: "123 Lê Lợi, Quận 1, TP.HCM", phone: "0900000000", taxId: "0100000000" }, valueType: 'JSON', description: 'Thông tin cửa hàng' },
];

export async function seed() {
  console.log('--- SEEDING DATABASE ---');

  // 1. Roles
  for (const r of ROLES) {
    await prisma.role.upsert({
      where: { code: r.code },
      update: { name: r.name, refundLimit: r.refundLimit },
      create: r,
    });
  }
  console.log(`✓ Seeded ${ROLES.length} roles`);

  // 2. Permissions
  for (const p of PERMISSION_DEFINITIONS) {
    await prisma.permission.upsert({
      where: { code: p.code },
      update: { module: p.module },
      create: p,
    });
  }
  console.log(`✓ Seeded ${PERMISSION_DEFINITIONS.length} permissions`);

  // 3. Role Permissions
  const allRoles = await prisma.role.findMany();
  const allPerms = await prisma.permission.findMany();
  const permMap = new Map(allPerms.map(p => [p.code, p.id]));

  for (const role of allRoles) {
    const permCodes = ROLE_PERMISSIONS_MATRIX[role.code] || [];
    for (const code of permCodes) {
      const permId = permMap.get(code);
      if (permId) {
        await prisma.rolePermission.upsert({
          where: {
            roleId_permissionId: {
              roleId: role.id,
              permissionId: permId,
            },
          },
          update: {},
          create: {
            roleId: role.id,
            permissionId: permId,
          },
        });
      }
    }
    console.log(`✓ Role ${role.code} granted ${permCodes.length} permissions`);
  }

  // 4. Default Warehouse Location
  await prisma.stockLocation.upsert({
    where: { code: 'WH-MAIN' },
    update: { name: 'Kho Tổng DUNA', isDefault: true },
    create: {
      code: 'WH-MAIN',
      name: 'Kho Tổng DUNA',
      isDefault: true,
    },
  });
  console.log('✓ Seeded WH-MAIN stock location');

  // 5. Shipping Rate Rule
  const existingRules = await prisma.shippingRateRule.count();
  if (existingRules === 0) {
    await prisma.shippingRateRule.create({
      data: {
        zoneCode: 'INNER_CITY',
        provinceCodes: ['79'],
        baseWeightGram: 500,
        baseFee: 25000n,
        extraFeePer500g: 5000n,
        freeShipMinSubtotal: 500000n,
        isActive: true,
      },
    });
    console.log('✓ Seeded initial shipping rate rule');
  }

  // 6. System Settings
  for (const s of SYSTEM_SETTINGS) {
    await prisma.systemSetting.upsert({
      where: { key: s.key },
      update: {
        value: s.value,
        valueType: s.valueType,
        description: s.description,
      },
      create: {
        key: s.key,
        value: s.value,
        valueType: s.valueType,
        description: s.description,
      },
    });
  }
  console.log(`✓ Seeded ${SYSTEM_SETTINGS.length} system settings`);

  console.log('--- SEED COMPLETED ---');
}

if (require.main === module) {
  seed()
    .catch(e => {
      console.error(e);
      process.exit(1);
    })
    .finally(async () => {
      await prisma.$disconnect();
    });
}

