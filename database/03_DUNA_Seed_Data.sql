-- ============================================================================
-- DUNA MEN'S WEAR - DU LIEU MAU KHOI TAO (SEED DATA CHO 100 BANG)
-- Chay sau khi da chay database/01_DUNA_Schema_100_Bang.sql
-- ============================================================================

USE [DUNA_MensWear];
GO
SET NOCOUNT ON;
GO

-- 1. Vai tro (roles: ADMIN, MANAGER, STAFF, CUSTOMER)
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'ADMIN')
    INSERT INTO roles (role_code, role_name, description) VALUES ('ADMIN', N'Quản trị viên', N'Toàn quyền quản trị hệ thống DUNA');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'MANAGER')
    INSERT INTO roles (role_code, role_name, description) VALUES ('MANAGER', N'Quản lý cửa hàng', N'Quản lý bán hàng, kho và nhân sự');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'STAFF')
    INSERT INTO roles (role_code, role_name, description) VALUES ('STAFF', N'Nhân viên bán hàng', N'Bán hàng POS, xử lý đơn hàng và CSKH');
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'CUSTOMER')
    INSERT INTO roles (role_code, role_name, description) VALUES ('CUSTOMER', N'Khách hàng', N'Khách mua sắm trực tuyến và tại cửa hàng');
GO

-- 2. Tai khoan mau (mat khau bam SHA-256: Admin123, Manager123, Staff123, Khach123)
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@duna.vn')
    INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified)
    SELECT 'admin@duna.vn', '0900000001', '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', N'Quản Trị Viên DUNA', role_id, 'ACTIVE', 1
    FROM roles WHERE role_code = 'ADMIN';

IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'manager@duna.vn')
    INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified)
    SELECT 'manager@duna.vn', '0900000002', '61b3ad24336c20515573dc7d875cb9295e8462cec645c3604ac58f1e241e20e0', N'Nguyễn Ngọc Minh (Quản lý)', role_id, 'ACTIVE', 1
    FROM roles WHERE role_code = 'MANAGER';

IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'staff@duna.vn')
    INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified)
    SELECT 'staff@duna.vn', '0900000003', '2f005e42a17da46ec51ba6f11d725e60788931a1dadd33d9cb85084fb32bb166', N'Nhân Viên Bán Hàng DUNA', role_id, 'ACTIVE', 1
    FROM roles WHERE role_code = 'STAFF';

IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'khach01@gmail.com')
    INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified)
    SELECT 'khach01@gmail.com', '0912345678', 'a54126ab084dee02e0d353876d89bb04347688ca817ccaa2fcf52713f0cc6a61', N'Trần Văn Nam', role_id, 'ACTIVE', 1
    FROM roles WHERE role_code = 'CUSTOMER';
GO

-- 3. Hang thanh vien & Khach hang mau
IF NOT EXISTS (SELECT 1 FROM membership_tiers WHERE tier_name = N'Thành Viên')
    INSERT INTO membership_tiers (tier_name, min_spend, discount_percent, point_multiplier, benefits) VALUES
    (N'Thành Viên', 0, 0, 1.00, N'Tích điểm mọi đơn hàng'),
    (N'Bạc', 3000000, 3.00, 1.25, N'Giảm 3% toàn bộ đơn hàng'),
    (N'Vàng', 10000000, 7.00, 1.50, N'Giảm 7%, ưu đãi sinh nhật'),
    (N'Kim Cương', 20000000, 10.00, 2.00, N'Giảm 10%, miễn phí vận chuyển toàn quốc');
GO

IF NOT EXISTS (SELECT 1 FROM customers WHERE customer_code = 'KH000001')
    INSERT INTO customers (user_id, customer_code, full_name, phone, email, gender, tier_id)
    SELECT u.user_id, 'KH000001', N'Trần Văn Nam', '0912345678', 'khach01@gmail.com', 'MALE', 1
    FROM users u WHERE u.email = 'khach01@gmail.com';
GO

-- 4. Dia ly, Kho & Cua hang
IF NOT EXISTS (SELECT 1 FROM shipping_zones WHERE zone_name = N'Nội thành')
    INSERT INTO shipping_zones (zone_name, description) VALUES (N'Nội thành', N'Hà Nội và TP. Hồ Chí Minh');

IF NOT EXISTS (SELECT 1 FROM provinces WHERE province_code = '01')
    INSERT INTO provinces (province_code, province_name, zone_id)
    SELECT '01', N'Hà Nội', zone_id FROM shipping_zones WHERE zone_name = N'Nội thành';

IF NOT EXISTS (SELECT 1 FROM wards WHERE ward_code = '00001')
    INSERT INTO wards (province_id, ward_code, ward_name, ward_type)
    SELECT province_id, '00001', N'Phường Láng', 'WARD' FROM provinces WHERE province_code = '01';

IF NOT EXISTS (SELECT 1 FROM warehouses WHERE warehouse_code = 'KHO-HN01')
    INSERT INTO warehouses (warehouse_code, warehouse_name, address, phone)
    VALUES ('KHO-HN01', N'Kho Tổng Hà Nội', N'Số 100 Cầu Giấy, Hà Nội', '0243111222');

IF NOT EXISTS (SELECT 1 FROM stores WHERE store_code = 'CH-HN01')
    INSERT INTO stores (store_code, store_name, address, province_id, phone, open_time, close_time, warehouse_id)
    SELECT 'CH-HN01', N'DUNA Cầu Giấy', N'Số 100 Cầu Giấy, Hà Nội', p.province_id, '0243111222', '08:30', '21:30', w.warehouse_id
    FROM provinces p, warehouses w WHERE p.province_code = '01' AND w.warehouse_code = 'KHO-HN01';
GO

-- 5. Danh muc, Thuong hieu, Mau sac, Kich co & San pham mau
IF NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'ao-so-mi-nam')
    INSERT INTO categories (category_name, slug, description, display_order) VALUES
    (N'Áo Sơ Mi Nam', 'ao-so-mi-nam', N'Sơ mi Oxford, Bamboo thanh lịch', 1),
    (N'Áo Polo Nam', 'ao-polo-nam', N'Áo Polo cotton thoáng mát', 2),
    (N'Quần Âu & Khaki', 'quan-au-khaki', N'Quần âu, quần kaki co giãn công sở', 3);

IF NOT EXISTS (SELECT 1 FROM brands WHERE brand_name = N'DUNA Basic')
    INSERT INTO brands (brand_name, description) VALUES (N'DUNA Basic', N'Thời trang nam tầm trung hiện đại');

IF NOT EXISTS (SELECT 1 FROM colors WHERE color_name = N'Trắng')
    INSERT INTO colors (color_name, hex_code, color_group) VALUES
    (N'Trắng', '#FFFFFF', 'WHITE'),
    (N'Xanh Navy', '#0F1E36', 'BLUE'),
    (N'Đen', '#141B26', 'BLACK');

IF NOT EXISTS (SELECT 1 FROM sizes WHERE size_code = 'M' AND category_type = 'SHIRT')
    INSERT INTO sizes (size_code, category_type, display_order) VALUES
    ('S', 'SHIRT', 1), ('M', 'SHIRT', 2), ('L', 'SHIRT', 3), ('XL', 'SHIRT', 4);

IF NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'SP001')
    INSERT INTO products (product_code, product_name, slug, category_id, brand_id, base_price, cost_price, material_info, form_fit, description, care_instructions)
    SELECT 'SP001', N'Áo Sơ Mi Oxford Trắng DUNA', 'ao-so-mi-oxford-trang', c.category_id, b.brand_id, 390000, 190000,
           N'100% Cotton Oxford', 'SLIM_FIT', N'Áo sơ mi nam phom Slim Fit dễ phối đồ công sở', N'Giặt máy nhẹ, ủi nhiệt độ vừa'
    FROM categories c, brands b WHERE c.slug = 'ao-so-mi-nam' AND b.brand_name = N'DUNA Basic';

IF NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'SP002')
    INSERT INTO products (product_code, product_name, slug, category_id, brand_id, base_price, cost_price, material_info, form_fit, description, care_instructions)
    SELECT 'SP002', N'Áo Polo Pima Cotton Navy', 'ao-polo-pima-cotton-navy', c.category_id, b.brand_id, 350000, 170000,
           N'95% Cotton, 5% Spandex', 'REGULAR_FIT', N'Áo polo nam cổ bẻ thoáng mát', N'Không dùng chất tẩy mạnh'
    FROM categories c, brands b WHERE c.slug = 'ao-polo-nam' AND b.brand_name = N'DUNA Basic';

IF NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SP001-WHT-M')
    INSERT INTO product_variants (product_id, color_id, size_id, sku, barcode, price)
    SELECT p.product_id, c.color_id, s.size_id, 'SP001-WHT-M', '893600100102', 390000
    FROM products p, colors c, sizes s
    WHERE p.product_code = 'SP001' AND c.color_name = N'Trắng' AND s.size_code = 'M' AND s.category_type = 'SHIRT';

IF NOT EXISTS (SELECT 1 FROM inventories i JOIN product_variants pv ON i.variant_id = pv.variant_id WHERE pv.sku = 'SP001-WHT-M')
    INSERT INTO inventories (variant_id, warehouse_id, stock_quantity, reserved_quantity, low_stock_threshold)
    SELECT pv.variant_id, w.warehouse_id, 50, 0, 5
    FROM product_variants pv, warehouses w WHERE pv.sku = 'SP001-WHT-M' AND w.warehouse_code = 'KHO-HN01';
GO

-- 6. Phuong thuc thanh toan & Cau hinh he thong
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_code = 'COD')
    INSERT INTO payment_methods (method_code, method_name, is_online, display_order) VALUES
    ('COD', N'Thanh toán khi nhận hàng (COD)', 0, 1),
    ('VNPAY', N'Cổng thanh toán VNPay', 1, 2),
    ('MOMO', N'Ví điện tử MoMo', 1, 3);

IF NOT EXISTS (SELECT 1 FROM system_settings WHERE setting_key = 'VAT_RATE')
    INSERT INTO system_settings (setting_key, setting_value, description) VALUES
    ('VAT_RATE', '8', N'Thuế suất VAT mặc định (%)'),
    ('FREE_SHIP_THRESHOLD', '500000', N'Ngưỡng đơn hàng được miễn phí vận chuyển (VNĐ)');
GO
