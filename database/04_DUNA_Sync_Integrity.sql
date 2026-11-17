SET NOCOUNT ON;
SET XACT_ABORT ON;

-- 1. Don dep duplicate mojibake trong membership_tiers va chuan hoa 4 hang
UPDATE customers SET tier_id = 1 WHERE tier_id = 5;
UPDATE customers SET tier_id = 2 WHERE tier_id = 6;
UPDATE customers SET tier_id = 3 WHERE tier_id = 7;
UPDATE customers SET tier_id = 4 WHERE tier_id = 8;
DELETE FROM membership_tiers WHERE tier_id > 4;

UPDATE membership_tiers SET tier_name = N'Thành Viên', min_spend = 0, discount_percent = 0.00, point_multiplier = 1.00 WHERE tier_id = 1;
UPDATE membership_tiers SET tier_name = N'Bạc',        min_spend = 3000000, discount_percent = 3.00, point_multiplier = 1.25 WHERE tier_id = 2;
UPDATE membership_tiers SET tier_name = N'Vàng',       min_spend = 10000000, discount_percent = 7.00, point_multiplier = 1.50 WHERE tier_id = 3;
UPDATE membership_tiers SET tier_name = N'Kim Cương',  min_spend = 20000000, discount_percent = 10.00, point_multiplier = 2.00 WHERE tier_id = 4;

-- 2. Bo sung quyen han (permissions & role_permissions) neu chua co
IF NOT EXISTS (SELECT 1 FROM permissions)
BEGIN
    INSERT INTO permissions (permission_code, permission_name, module_name) VALUES
    ('USER_VIEW', N'Xem & quản lý tài khoản', 'M01'),
    ('USER_LOCK', N'Khóa / mở khóa tài khoản', 'M01'),
    ('CUSTOMER_MANAGE', N'Quản lý khách hàng & hạng VIP', 'M02'),
    ('PRODUCT_MANAGE', N'Quản lý sản phẩm & biến thể', 'M03'),
    ('INVENTORY_MANAGE', N'Quản lý kho & nhập hàng', 'M04'),
    ('POS_SELL', N'Bán hàng tại quầy POS', 'M14'),
    ('ORDER_MANAGE', N'Quản lý hóa đơn & đổi trả', 'M06'),
    ('VOUCHER_MANAGE', N'Quản lý voucher & khuyến mãi', 'M09'),
    ('HR_MANAGE', N'Quản lý nhân sự & chấm công', 'M15'),
    ('FINANCE_VIEW', N'Xem báo cáo tài chính & lợi nhuận', 'M16'),
    ('SYSTEM_CONFIG', N'Cấu hình hệ thống & Job ngầm', 'M17');

    INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.role_id, p.permission_id FROM roles r CROSS JOIN permissions p WHERE r.role_code = 'ADMIN';

    INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.role_id, p.permission_id FROM roles r CROSS JOIN permissions p
    WHERE r.role_code = 'MANAGER' AND p.permission_code <> 'SYSTEM_CONFIG';

    INSERT INTO role_permissions (role_id, permission_id)
    SELECT r.role_id, p.permission_id FROM roles r CROSS JOIN permissions p
    WHERE r.role_code = 'STAFF' AND p.permission_code IN ('POS_SELL', 'ORDER_MANAGE', 'CUSTOMER_MANAGE');
END;

-- 3. Seed Phong ban, Chuc vu, Ca lam viec va dong bo 1:1 Nhan su <-> Tai khoan
IF NOT EXISTS (SELECT 1 FROM departments)
BEGIN
    INSERT INTO departments (department_name, description) VALUES
    (N'Ban Giám Đốc', N'Điều hành toàn hệ thống DUNA Men''s Wear'),
    (N'Vận Hành Cửa Hàng & POS', N'Tư vấn bán hàng, thu ngân tại quầy'),
    (N'Kho & Kiểm Kê', N'Quản lý tồn kho, nhập PO và mã vạch'),
    (N'Chăm Sóc Khách Hàng', N'Hỗ trợ hội viên DUNA Club & đổi trả');
END;

IF NOT EXISTS (SELECT 1 FROM positions)
BEGIN
    INSERT INTO positions (position_name, base_salary) VALUES
    (N'Giám Đốc Điều Hành', 25000000),
    (N'Quản Lý Cửa Hàng', 15000000),
    (N'Nhân Viên Bán Hàng POS', 8500000),
    (N'Chuyên Viên Kho & CSKH', 9000000);
END;

IF NOT EXISTS (SELECT 1 FROM work_shifts)
BEGIN
    INSERT INTO work_shifts (shift_name, start_time, end_time) VALUES
    (N'Ca Sáng', '08:30', '13:30'),
    (N'Ca Chiều', '13:30', '18:30'),
    (N'Ca Tối', '18:00', '22:00');
END;

DECLARE @deptMgmt INT = (SELECT TOP 1 department_id FROM departments ORDER BY department_id);
DECLARE @deptSales INT = ISNULL((SELECT TOP 1 department_id FROM departments WHERE department_name LIKE N'%POS%'), @deptMgmt);
DECLARE @deptCs INT = ISNULL((SELECT TOP 1 department_id FROM departments WHERE department_name LIKE N'%Chăm Sóc%'), @deptMgmt);
DECLARE @posAdmin INT = (SELECT TOP 1 position_id FROM positions ORDER BY position_id);
DECLARE @posMgr INT = ISNULL((SELECT TOP 1 position_id FROM positions WHERE position_name LIKE N'%Quản Lý%'), @posAdmin);
DECLARE @posStaff INT = ISNULL((SELECT TOP 1 position_id FROM positions WHERE position_name LIKE N'%Bán Hàng%'), @posAdmin);
DECLARE @posCs INT = ISNULL((SELECT TOP 1 position_id FROM positions WHERE position_name LIKE N'%Kho%'), @posStaff);

UPDATE employees SET birth_date = '1992-05-14', department_id = @deptMgmt, position_id = @posAdmin WHERE employee_code = 'QL001';
UPDATE employees SET birth_date = '1996-09-20', department_id = @deptSales, position_id = @posMgr  WHERE employee_code = 'QL002';
UPDATE employees SET birth_date = '2001-03-11', department_id = @deptSales, position_id = @posStaff WHERE employee_code = 'NV003';

-- Dong bo them 2 thanh vien con lai cua nhom 6 vao users + employees (Staff123)
DECLARE @staffHash VARCHAR(64) = (SELECT TOP 1 password_hash FROM users WHERE email = 'staff@duna.vn');
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'bach@duna.vn')
BEGIN
    INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified)
    SELECT 'bach@duna.vn', '0918442004', @staffHash, N'Lê Minh Bách', role_id, 'ACTIVE', 1 FROM roles WHERE role_code = 'STAFF';
END;
IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'NV004')
BEGIN
    INSERT INTO employees (employee_code, user_id, full_name, phone, email, birth_date, hire_date, department_id, position_id, store_id, status)
    SELECT 'NV004', user_id, N'Lê Minh Bách', '0918442004', 'bach@duna.vn', '2002-07-08', '2026-06-01', @deptSales, @posStaff, 1, 'WORKING'
    FROM users WHERE email = 'bach@duna.vn';
END;

IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'an@duna.vn')
BEGIN
    INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified)
    SELECT 'an@duna.vn', '0918490005', @staffHash, N'Nguyễn Kiều An', role_id, 'ACTIVE', 1 FROM roles WHERE role_code = 'STAFF';
END;
IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'NV005')
BEGIN
    INSERT INTO employees (employee_code, user_id, full_name, phone, email, birth_date, hire_date, department_id, position_id, store_id, status)
    SELECT 'NV005', user_id, N'Nguyễn Kiều An', '0918490005', 'an@duna.vn', '2003-11-25', '2026-06-15', @deptCs, @posCs, 1, 'WORKING'
    FROM users WHERE email = 'an@duna.vn';
END;

-- 4. Chinh xac hoa 6 don hang mau ban dau (orders 7..12)
UPDATE o SET o.voucher_id = vu.voucher_id
FROM orders o
JOIN voucher_usages vu ON vu.order_id = o.order_id
WHERE o.voucher_id IS NULL;

UPDATE vu SET vu.user_id = c.user_id
FROM voucher_usages vu
JOIN orders o ON o.order_id = vu.order_id
JOIN customers c ON c.customer_id = o.customer_id
WHERE c.user_id IS NOT NULL;

UPDATE orders SET subtotal = 1110000, discount_amount = 168000, tax_amount = 75360, total_amount = 1017360 WHERE order_id = 8;
UPDATE payments SET amount = 1017360 WHERE order_id = 8;

IF (SELECT ISNULL(SUM(line_total), 0) FROM order_items WHERE order_id = 11) = 700000
BEGIN
    INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total)
    VALUES (11, 1, N'Áo Sơ Mi Oxford Trắng DUNA (Trắng / M)', 'SP001-WHT-M', 2, 390000, 780000);
END;

-- 5. Tao hoa don that (orders + order_items + payments) cho cac khach hang co lich su chi tieu nhung chua co don hang nao
DECLARE @variants TABLE (
    rn INT IDENTITY(1,1) PRIMARY KEY,
    variant_id INT,
    product_name NVARCHAR(250),
    sku VARCHAR(60),
    unit_price DECIMAL(18,0)
);
INSERT INTO @variants (variant_id, product_name, sku, unit_price)
SELECT pv.variant_id,
       p.product_name + N' (' + c.color_name + N' / ' + s.size_code + N')',
       pv.sku,
       ISNULL(pv.price, p.base_price)
FROM product_variants pv
JOIN products p ON pv.product_id = p.product_id
JOIN colors c ON pv.color_id = c.color_id
JOIN sizes s ON pv.size_id = s.size_id
WHERE pv.is_active = 1 AND ISNULL(pv.price, p.base_price) >= 150000
ORDER BY pv.variant_id;

DECLARE @maxVar INT = (SELECT COUNT(*) FROM @variants);
DECLARE @storeId INT = (SELECT TOP 1 store_id FROM stores ORDER BY store_id);

IF @maxVar > 0
BEGIN
    DECLARE @cid INT, @uid INT, @cname NVARCHAR(150), @cphone VARCHAR(20), @targetSpent DECIMAL(18,0);
    DECLARE @orderCount INT, @k INT, @vIdx INT, @vid INT, @vName NVARCHAR(250), @vSku VARCHAR(60), @vPrice DECIMAL(18,0);
    DECLARE @qty INT, @sub DECIMAL(18,0), @tax DECIMAL(18,0), @tot DECIMAL(18,0), @newOid BIGINT;
    DECLARE @ordDate DATETIME2, @ocode VARCHAR(30), @handlerUid INT, @payMethodId INT;

    DECLARE cur CURSOR LOCAL FAST_FORWARD FOR
        SELECT c.customer_id, c.user_id, c.full_name, ISNULL(c.phone, '0900000000'), c.total_spent
        FROM customers c
        WHERE c.total_spent > 0
          AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.customer_id)
        ORDER BY c.customer_id;

    OPEN cur;
    FETCH NEXT FROM cur INTO @cid, @uid, @cname, @cphone, @targetSpent;
    WHILE @@FETCH_STATUS = 0
    BEGIN
        SET @orderCount = CASE
            WHEN @targetSpent >= 20000000 THEN 4
            WHEN @targetSpent >= 10000000 THEN 3
            WHEN @targetSpent >= 3000000  THEN 2
            ELSE 1
        END;
        SET @k = 1;
        WHILE @k <= @orderCount
        BEGIN
            SET @vIdx = ((@cid * 7 + @k * 13) % @maxVar) + 1;
            SELECT @vid = variant_id, @vName = product_name, @vSku = sku, @vPrice = unit_price FROM @variants WHERE rn = @vIdx;

            -- Tinh so luong sao cho tong cac don cua khach khop dung phan khuc chi tieu
            DECLARE @perOrderTarget DECIMAL(18,0) = @targetSpent / @orderCount;
            SET @qty = CASE
                WHEN @perOrderTarget / NULLIF(@vPrice * 1.08, 0) >= 1 THEN CAST(ROUND(@perOrderTarget / (@vPrice * 1.08), 0) AS INT)
                ELSE 1
            END;
            IF @qty < 1 SET @qty = 1;
            IF @qty > 8 SET @qty = 8;

            SET @sub = @vPrice * @qty;
            SET @tax = ROUND(@sub * 0.08, 0);
            SET @tot = @sub + @tax;

            SET @ordDate = DATEADD(DAY, -((@cid % 260) + @k * 7), '2026-11-16 14:30:00');
            SET @ocode = 'DN' + FORMAT(@ordDate, 'yyMMdd') + '-' + RIGHT('000' + CAST(@cid AS VARCHAR(6)), 3) + CAST(@k AS VARCHAR(2));
            SET @handlerUid = CASE WHEN (@cid + @k) % 3 = 0 THEN 2 ELSE 3 END;
            SET @payMethodId = CASE WHEN (@cid + @k) % 2 = 0 THEN 2 ELSE 1 END;

            INSERT INTO orders (order_code, customer_id, store_id, channel, status, payment_status, handled_by,
                                receiver_name, receiver_phone, shipping_address, subtotal, discount_amount, tax_amount, total_amount, note, ordered_at, created_at)
            VALUES (@ocode, @cid, @storeId, CASE WHEN @k % 2 = 1 THEN 'POS' ELSE 'WEB' END, 'COMPLETED', 'PAID', @handlerUid,
                    @cname, @cphone, N'Tại cửa hàng DUNA', @sub, 0, @tax, @tot, N'Đơn hàng đồng bộ lịch sử mua sắm DUNA', @ordDate, @ordDate);
            SET @newOid = SCOPE_IDENTITY();

            INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total)
            VALUES (@newOid, @vid, @vName, @vSku, @qty, @vPrice, @sub);

            INSERT INTO payments (order_id, method_id, amount, status, paid_at, created_at)
            VALUES (@newOid, @payMethodId, @tot, 'SUCCESS', @ordDate, @ordDate);

            SET @k = @k + 1;
        END;
        FETCH NEXT FROM cur INTO @cid, @uid, @cname, @cphone, @targetSpent;
    END;
    CLOSE cur;
    DEALLOCATE cur;
END;

-- 6. DONG BO NGUOC 100% CHI SO TU DON HANG THAT (SINGLE SOURCE OF TRUTH)
UPDATE c
SET c.total_spent = ISNULL(agg.sum_spent, 0),
    c.total_orders = ISNULL(agg.cnt_orders, 0),
    c.updated_at = GETDATE()
FROM customers c
OUTER APPLY (
    SELECT SUM(o.total_amount) AS sum_spent, COUNT(*) AS cnt_orders
    FROM orders o
    WHERE o.customer_id = c.customer_id AND o.status IN ('COMPLETED', 'DELIVERED')
) agg;

UPDATE c
SET c.tier_id = (SELECT TOP 1 t.tier_id FROM membership_tiers t WHERE t.min_spend <= c.total_spent ORDER BY t.min_spend DESC)
FROM customers c;

-- Dong bo vi diem va ghi nhan giao dich diem thuong gan order_id that
UPDATE la
SET la.points_balance = CAST(FLOOR(c.total_spent / 10000.0) AS INT),
    la.lifetime_points = CAST(FLOOR(c.total_spent / 10000.0) AS INT),
    la.updated_at = GETDATE()
FROM loyalty_accounts la
JOIN customers c ON la.customer_id = c.customer_id;

INSERT INTO loyalty_transactions (account_id, order_id, points, txn_type, description, created_at)
SELECT la.account_id, o.order_id, CAST(FLOOR(o.total_amount / 10000.0) AS INT), 'EARN',
       N'Tích điểm đơn hàng ' + o.order_code, o.ordered_at
FROM orders o
JOIN loyalty_accounts la ON la.customer_id = o.customer_id
WHERE o.status IN ('COMPLETED', 'DELIVERED')
  AND CAST(FLOOR(o.total_amount / 10000.0) AS INT) > 0
  AND NOT EXISTS (SELECT 1 FROM loyalty_transactions lt WHERE lt.order_id = o.order_id);

-- Dong bo luot dung voucher & so luong da ban san pham tu order_items that
UPDATE v
SET v.used_count = (SELECT COUNT(*) FROM voucher_usages vu WHERE vu.voucher_id = v.voucher_id)
FROM vouchers v;

UPDATE p
SET p.sold_count = ISNULL(s.qty_sum, 0)
FROM products p
OUTER APPLY (
    SELECT SUM(oi.quantity) AS qty_sum
    FROM order_items oi
    JOIN orders o ON oi.order_id = o.order_id
    JOIN product_variants pv ON oi.variant_id = pv.variant_id
    WHERE pv.product_id = p.product_id AND o.status IN ('COMPLETED', 'DELIVERED')
) s;
