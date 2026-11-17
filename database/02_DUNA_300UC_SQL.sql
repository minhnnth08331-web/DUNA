-- ============================================================================
-- DUNA MEN'S WEAR - 300 USE CASE VÀ CÂU LỆNH SQL TƯƠNG ỨNG
-- Hệ CSDL: Microsoft SQL Server (T-SQL) - 100 bảng, xem file DUNA_Schema_100_Bang.sql
-- Quy ước: mỗi UC là một batch độc lập (kết thúc bằng GO), giá trị ID/tham số là dữ liệu minh họa.
-- Câu lệnh Thêm/Sửa/Xóa bắt đầu bằng SELECT hoặc điều kiện IF NOT EXISTS để kiểm tra trước khi ghi.
-- Cần có dữ liệu mẫu trong các bảng trước khi chạy các UC cập nhật / xóa.
-- ============================================================================

USE [DUNA_MensWear];
GO

-- ############################################################################
-- M01 - TÀI KHOẢN & PHÂN QUYỀN
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC001]: Xem danh sách vai trò hệ thống (Admin, Quản lý, Nhân viên, Khách hàng)
-- ----------------------------------------------------------------------------
SELECT role_id, role_code, role_name, description, is_active FROM roles WHERE role_name LIKE N'%%' ORDER BY role_id ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC002]: Thêm mới vai trò và mã vai trò duy nhất
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = 'WAREHOUSE') INSERT INTO roles (role_code, role_name, description) VALUES ('WAREHOUSE', N'Nhân viên kho', N'Quản lý nhập, xuất và kiểm kê kho');
GO

-- ----------------------------------------------------------------------------
-- [UC003]: Cập nhật tên hiển thị và mô tả vai trò
-- ----------------------------------------------------------------------------
SELECT * FROM roles WHERE role_id = 2; UPDATE roles SET role_name = N'Quản lý cửa hàng', description = N'Quản lý bán hàng và nhân sự cửa hàng' WHERE role_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC004]: Xóa vai trò và kiểm tra tài khoản đang sử dụng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM users WHERE role_id = 6) DELETE FROM roles WHERE role_id = 6;
GO

-- ----------------------------------------------------------------------------
-- [UC005]: Xem danh sách quyền hạn theo từng module chức năng
-- ----------------------------------------------------------------------------
SELECT permission_id, permission_code, permission_name, module_name FROM permissions WHERE permission_name LIKE N'%%' ORDER BY module_name, permission_code;
GO

-- ----------------------------------------------------------------------------
-- [UC006]: Thêm mới quyền hạn truy cập chức năng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM permissions WHERE permission_code = 'ORDER_APPROVE') INSERT INTO permissions (permission_code, permission_name, module_name) VALUES ('ORDER_APPROVE', N'Duyệt đơn hàng', N'Đơn hàng');
GO

-- ----------------------------------------------------------------------------
-- [UC007]: Gán quyền hạn cho vai trò
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM role_permissions WHERE role_id = 3 AND permission_id = 10) INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 10);
GO

-- ----------------------------------------------------------------------------
-- [UC008]: Thu hồi quyền hạn khỏi vai trò
-- ----------------------------------------------------------------------------
SELECT * FROM role_permissions WHERE role_id = 3; DELETE FROM role_permissions WHERE role_id = 3 AND permission_id = 10;
GO

-- ----------------------------------------------------------------------------
-- [UC009]: Xem ma trận phân quyền chi tiết của một vai trò
-- ----------------------------------------------------------------------------
SELECT r.role_name, p.module_name, p.permission_code, p.permission_name FROM roles r JOIN role_permissions rp ON r.role_id = rp.role_id JOIN permissions p ON rp.permission_id = p.permission_id WHERE r.role_id = 2 ORDER BY p.module_name;
GO

-- ----------------------------------------------------------------------------
-- [UC010]: Kiểm tra quyền của người dùng khi truy cập chức năng
-- ----------------------------------------------------------------------------
SELECT CASE WHEN EXISTS (SELECT 1 FROM users u JOIN role_permissions rp ON u.role_id = rp.role_id JOIN permissions p ON rp.permission_id = p.permission_id WHERE u.user_id = 5 AND p.permission_code = 'ORDER_APPROVE' AND u.status = 'ACTIVE') THEN 1 ELSE 0 END AS has_permission;
GO

-- ----------------------------------------------------------------------------
-- [UC011]: Xem danh sách tài khoản người dùng phân trang
-- ----------------------------------------------------------------------------
SELECT u.user_id, u.email, u.phone, u.full_name, r.role_name, u.status, u.last_login_at FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.full_name LIKE N'%%' ORDER BY u.user_id DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;
GO

-- ----------------------------------------------------------------------------
-- [UC012]: Tìm kiếm tài khoản theo email, số điện thoại hoặc họ tên
-- ----------------------------------------------------------------------------
SELECT user_id, email, phone, full_name, status FROM users WHERE email LIKE '%@gmail.com' OR phone LIKE '%0912%' OR full_name LIKE N'%Nguyễn%';
GO

-- ----------------------------------------------------------------------------
-- [UC013]: Lọc tài khoản theo vai trò và trạng thái hoạt động
-- ----------------------------------------------------------------------------
SELECT u.user_id, u.email, u.full_name, r.role_code, u.status FROM users u JOIN roles r ON u.role_id = r.role_id WHERE r.role_code = 'STAFF' AND u.status = 'ACTIVE' AND u.full_name LIKE N'%%';
GO

-- ----------------------------------------------------------------------------
-- [UC014]: Đăng ký tài khoản khách hàng mới và tự tạo hồ sơ khách hàng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'khach01@gmail.com') BEGIN INSERT INTO users (email, phone, password_hash, full_name, role_id) VALUES ('khach01@gmail.com', '0912345678', '$2a$10$hashDemo', N'Trần Văn Nam', 4); INSERT INTO customers (user_id, customer_code, full_name, phone, email) VALUES (SCOPE_IDENTITY(), 'KH000101', N'Trần Văn Nam', '0912345678', 'khach01@gmail.com'); END
GO

-- ----------------------------------------------------------------------------
-- [UC015]: Đăng nhập bằng email và mật khẩu, ghi nhận lịch sử đăng nhập
-- ----------------------------------------------------------------------------
SELECT user_id, full_name, role_id FROM users WHERE email = 'khach01@gmail.com' AND password_hash = '$2a$10$hashDemo' AND status = 'ACTIVE'; UPDATE users SET last_login_at = GETDATE() WHERE email = 'khach01@gmail.com'; INSERT INTO login_histories (user_id, email_attempt, ip_address, device_info, is_success) VALUES (101, 'khach01@gmail.com', '113.160.10.5', N'Chrome / Windows 11', 1);
GO

-- ----------------------------------------------------------------------------
-- [UC016]: Tự động khóa tài khoản khi đăng nhập sai quá 5 lần liên tiếp
-- ----------------------------------------------------------------------------
SELECT COUNT(*) AS fail_count FROM login_histories WHERE email_attempt = 'khach01@gmail.com' AND is_success = 0 AND login_at >= DATEADD(MINUTE, -15, GETDATE()); UPDATE users SET status = 'LOCKED' WHERE email = 'khach01@gmail.com' AND (SELECT COUNT(*) FROM login_histories WHERE email_attempt = 'khach01@gmail.com' AND is_success = 0 AND login_at >= DATEADD(MINUTE, -15, GETDATE())) >= 5;
GO

-- ----------------------------------------------------------------------------
-- [UC017]: Cập nhật hồ sơ cá nhân và ảnh đại diện
-- ----------------------------------------------------------------------------
SELECT * FROM users WHERE user_id = 101; UPDATE users SET full_name = N'Trần Văn Nam', phone = '0987654321', avatar_url = '/uploads/avatar/u101.jpg', updated_at = GETDATE() WHERE user_id = 101;
GO

-- ----------------------------------------------------------------------------
-- [UC018]: Đổi mật khẩu tài khoản sau khi xác thực mật khẩu cũ
-- ----------------------------------------------------------------------------
SELECT user_id FROM users WHERE user_id = 101 AND password_hash = '$2a$10$hashOld'; UPDATE users SET password_hash = '$2a$10$hashNew', updated_at = GETDATE() WHERE user_id = 101 AND password_hash = '$2a$10$hashOld';
GO

-- ----------------------------------------------------------------------------
-- [UC019]: Yêu cầu quên mật khẩu và tạo token đặt lại gửi qua email
-- ----------------------------------------------------------------------------
SELECT user_id FROM users WHERE email = 'khach01@gmail.com'; INSERT INTO password_reset_tokens (user_id, token_hash, expires_at) VALUES (101, 'a1b2c3d4e5f6tokenhash', DATEADD(MINUTE, 30, GETDATE()));
GO

-- ----------------------------------------------------------------------------
-- [UC020]: Đặt lại mật khẩu bằng token còn hạn sử dụng
-- ----------------------------------------------------------------------------
SELECT * FROM password_reset_tokens WHERE token_hash = 'a1b2c3d4e5f6tokenhash' AND used_at IS NULL AND expires_at > GETDATE(); UPDATE users SET password_hash = '$2a$10$hashReset' WHERE user_id = 101; UPDATE password_reset_tokens SET used_at = GETDATE() WHERE token_hash = 'a1b2c3d4e5f6tokenhash';
GO

-- ----------------------------------------------------------------------------
-- [UC021]: Khóa hoặc mở khóa tài khoản người dùng
-- ----------------------------------------------------------------------------
SELECT user_id, status FROM users WHERE user_id = 101; UPDATE users SET status = CASE WHEN status = 'ACTIVE' THEN 'LOCKED' ELSE 'ACTIVE' END, updated_at = GETDATE() WHERE user_id = 101;
GO

-- ----------------------------------------------------------------------------
-- [UC022]: Xóa tài khoản chưa phát sinh đơn hàng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM orders WHERE user_id = 150) AND NOT EXISTS (SELECT 1 FROM customers WHERE user_id = 150) DELETE FROM users WHERE user_id = 150;
GO

-- ----------------------------------------------------------------------------
-- [UC023]: Xem sổ địa chỉ nhận hàng của người dùng
-- ----------------------------------------------------------------------------
SELECT a.address_id, a.receiver_name, a.receiver_phone, a.address_line, w.ward_name, p.province_name, a.is_default FROM user_addresses a LEFT JOIN wards w ON a.ward_id = w.ward_id LEFT JOIN provinces p ON a.province_id = p.province_id WHERE a.user_id = 101 ORDER BY a.is_default DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC024]: Thêm mới địa chỉ nhận hàng theo tỉnh và xã/phường
-- ----------------------------------------------------------------------------
SELECT * FROM user_addresses WHERE user_id = 101; INSERT INTO user_addresses (user_id, receiver_name, receiver_phone, province_id, ward_id, address_line, is_default) VALUES (101, N'Trần Văn Nam', '0987654321', 1, 15, N'Số 25 ngõ 12 đường Láng', 0);
GO

-- ----------------------------------------------------------------------------
-- [UC025]: Cập nhật địa chỉ và đặt địa chỉ nhận hàng mặc định
-- ----------------------------------------------------------------------------
SELECT * FROM user_addresses WHERE user_id = 101; UPDATE user_addresses SET is_default = 0 WHERE user_id = 101; UPDATE user_addresses SET is_default = 1, address_line = N'Số 30 phố Huế' WHERE address_id = 7;
GO

-- ----------------------------------------------------------------------------
-- [UC026]: Xóa địa chỉ nhận hàng khỏi sổ địa chỉ
-- ----------------------------------------------------------------------------
SELECT * FROM user_addresses WHERE address_id = 9; DELETE FROM user_addresses WHERE address_id = 9 AND is_default = 0;
GO

-- ----------------------------------------------------------------------------
-- [UC027]: Xem nhật ký thao tác hệ thống theo người dùng và khoảng thời gian
-- ----------------------------------------------------------------------------
SELECT a.audit_id, u.full_name, a.action, a.entity_name, a.entity_id, a.ip_address, a.created_at FROM audit_logs a LEFT JOIN users u ON a.user_id = u.user_id WHERE a.created_at BETWEEN '2026-11-01' AND '2026-11-30' AND a.action LIKE '%%' ORDER BY a.audit_id DESC;
GO


-- ############################################################################
-- M02 - KHÁCH HÀNG
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC028]: Xem danh sách khách hàng kèm hạng thành viên
-- ----------------------------------------------------------------------------
SELECT c.customer_id, c.customer_code, c.full_name, c.phone, t.tier_name, c.total_spent, c.total_orders FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id WHERE c.full_name LIKE N'%%' ORDER BY c.customer_id DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;
GO

-- ----------------------------------------------------------------------------
-- [UC029]: Tìm kiếm khách hàng theo mã, số điện thoại hoặc họ tên
-- ----------------------------------------------------------------------------
SELECT customer_id, customer_code, full_name, phone, email FROM customers WHERE customer_code LIKE 'KH0001%' OR phone LIKE '%0912%' OR full_name LIKE N'%Nam%';
GO

-- ----------------------------------------------------------------------------
-- [UC030]: Xem hồ sơ chi tiết khách hàng và tổng chi tiêu tích lũy
-- ----------------------------------------------------------------------------
SELECT c.*, t.tier_name, g.group_name, la.points_balance FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id LEFT JOIN customer_groups g ON c.group_id = g.group_id LEFT JOIN loyalty_accounts la ON c.customer_id = la.customer_id WHERE c.customer_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC031]: Thêm mới khách hàng vãng lai mua tại quầy
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM customers WHERE phone = '0933111222') INSERT INTO customers (customer_code, full_name, phone, gender) VALUES ('KH000102', N'Lê Quang Huy', '0933111222', 'MALE');
GO

-- ----------------------------------------------------------------------------
-- [UC032]: Cập nhật thông tin liên hệ và ngày sinh khách hàng
-- ----------------------------------------------------------------------------
SELECT * FROM customers WHERE customer_id = 1; UPDATE customers SET phone = '0933999888', email = 'huy.le@gmail.com', birth_date = '1995-08-20', updated_at = GETDATE() WHERE customer_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC033]: Vô hiệu hóa hồ sơ khách hàng vi phạm chính sách
-- ----------------------------------------------------------------------------
SELECT customer_id, is_active FROM customers WHERE customer_id = 8; UPDATE customers SET is_active = 0, updated_at = GETDATE() WHERE customer_id = 8;
GO

-- ----------------------------------------------------------------------------
-- [UC034]: Xóa hồ sơ khách hàng chưa phát sinh đơn hàng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM orders WHERE customer_id = 99) DELETE FROM customers WHERE customer_id = 99;
GO

-- ----------------------------------------------------------------------------
-- [UC035]: Xem danh sách hạng thành viên và quyền lợi ưu đãi
-- ----------------------------------------------------------------------------
SELECT tier_id, tier_name, min_spend, discount_percent, point_multiplier, benefits FROM membership_tiers WHERE tier_name LIKE N'%%' ORDER BY min_spend ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC036]: Thêm mới hạng thành viên (Bạc, Vàng, Kim cương)
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM membership_tiers WHERE tier_name = N'Kim Cương') INSERT INTO membership_tiers (tier_name, min_spend, discount_percent, point_multiplier, benefits) VALUES (N'Kim Cương', 20000000, 10.00, 2.00, N'Giảm 10%, freeship toàn quốc, quà sinh nhật');
GO

-- ----------------------------------------------------------------------------
-- [UC037]: Cập nhật ngưỡng chi tiêu và mức giảm giá của hạng thành viên
-- ----------------------------------------------------------------------------
SELECT * FROM membership_tiers WHERE tier_id = 2; UPDATE membership_tiers SET min_spend = 5000000, discount_percent = 5.00 WHERE tier_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC038]: Tự động nâng hạng thành viên theo tổng chi tiêu tích lũy
-- ----------------------------------------------------------------------------
SELECT c.customer_id, c.total_spent, (SELECT TOP 1 t.tier_id FROM membership_tiers t WHERE t.min_spend <= c.total_spent ORDER BY t.min_spend DESC) AS new_tier FROM customers c; UPDATE customers SET tier_id = (SELECT TOP 1 t.tier_id FROM membership_tiers t WHERE t.min_spend <= customers.total_spent ORDER BY t.min_spend DESC) WHERE is_active = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC039]: Xem danh sách nhóm khách hàng chăm sóc
-- ----------------------------------------------------------------------------
SELECT g.group_id, g.group_name, g.description, COUNT(c.customer_id) AS total_customers FROM customer_groups g LEFT JOIN customers c ON g.group_id = c.group_id WHERE g.group_name LIKE N'%%' GROUP BY g.group_id, g.group_name, g.description;
GO

-- ----------------------------------------------------------------------------
-- [UC040]: Thêm mới nhóm khách hàng (Khách VIP, Khách sỉ, Khách lâu không mua)
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM customer_groups WHERE group_name = N'Khách sỉ') INSERT INTO customer_groups (group_name, description) VALUES (N'Khách sỉ', N'Khách mua số lượng lớn theo bảng giá sỉ');
GO

-- ----------------------------------------------------------------------------
-- [UC041]: Gán khách hàng vào nhóm chăm sóc
-- ----------------------------------------------------------------------------
SELECT * FROM customers WHERE customer_id = 1; UPDATE customers SET group_id = 2 WHERE customer_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC042]: Lưu số đo cơ thể của khách hàng phục vụ tư vấn size
-- ----------------------------------------------------------------------------
SELECT * FROM customer_measurements WHERE customer_id = 1; INSERT INTO customer_measurements (customer_id, height_cm, weight_kg, chest_cm, waist_cm, hip_cm, shoulder_cm) VALUES (1, 172.0, 68.0, 94.0, 80.0, 96.0, 45.0);
GO

-- ----------------------------------------------------------------------------
-- [UC043]: Xem số đo gần nhất của khách hàng để gợi ý size
-- ----------------------------------------------------------------------------
SELECT TOP 1 height_cm, weight_kg, chest_cm, waist_cm, hip_cm, shoulder_cm, measured_at FROM customer_measurements WHERE customer_id = 1 ORDER BY measured_at DESC;
GO


-- ############################################################################
-- M03 - SẢN PHẨM & DANH MỤC
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC044]: Xem cây danh mục sản phẩm đa cấp (cha - con)
-- ----------------------------------------------------------------------------
SELECT c.category_id, c.category_name, c.slug, p.category_name AS parent_name, c.display_order, c.is_active FROM categories c LEFT JOIN categories p ON c.parent_id = p.category_id ORDER BY ISNULL(c.parent_id, c.category_id), c.parent_id, c.display_order;
GO

-- ----------------------------------------------------------------------------
-- [UC045]: Thêm mới danh mục sản phẩm kèm slug chuẩn SEO
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'ao-khoac-nam') INSERT INTO categories (category_name, slug, parent_id, description, display_order) VALUES (N'Áo Khoác Nam', 'ao-khoac-nam', NULL, N'Áo khoác bomber, jacket, blazer', 5);
GO

-- ----------------------------------------------------------------------------
-- [UC046]: Cập nhật tên, mô tả và ảnh đại diện danh mục
-- ----------------------------------------------------------------------------
SELECT * FROM categories WHERE category_id = 2; UPDATE categories SET category_name = N'Áo Polo Nam Cao Cấp', description = N'Polo cotton Pima thoáng mát', image_url = '/uploads/cat/polo.jpg' WHERE category_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC047]: Xóa danh mục và kiểm tra sản phẩm, danh mục con đang trực thuộc
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM products WHERE category_id = 9) AND NOT EXISTS (SELECT 1 FROM categories WHERE parent_id = 9) DELETE FROM categories WHERE category_id = 9;
GO

-- ----------------------------------------------------------------------------
-- [UC048]: Xem danh sách bộ sưu tập thời trang theo mùa
-- ----------------------------------------------------------------------------
SELECT collection_id, collection_name, season, start_date, end_date, is_active FROM collections WHERE collection_name LIKE N'%%' ORDER BY start_date DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC049]: Thêm mới bộ sưu tập Xuân Hè / Thu Đông kèm banner
-- ----------------------------------------------------------------------------
SELECT * FROM collections; INSERT INTO collections (collection_name, season, banner_image_url, description, start_date, end_date) VALUES (N'DUNA Urban Gentleman 2027', 'SPRING_SUMMER', '/assets/ss2027.jpg', N'Bộ sưu tập Xuân Hè lịch lãm', '2027-02-01', '2027-06-30');
GO

-- ----------------------------------------------------------------------------
-- [UC050]: Cập nhật thời gian áp dụng và banner bộ sưu tập
-- ----------------------------------------------------------------------------
SELECT * FROM collections WHERE collection_id = 1; UPDATE collections SET end_date = '2026-12-31', banner_image_url = '/assets/fw2026_v2.jpg' WHERE collection_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC051]: Xóa bộ sưu tập và gỡ liên kết sản phẩm thuộc bộ sưu tập
-- ----------------------------------------------------------------------------
SELECT COUNT(*) AS total_products FROM products WHERE collection_id = 4; UPDATE products SET collection_id = NULL WHERE collection_id = 4; DELETE FROM collections WHERE collection_id = 4;
GO

-- ----------------------------------------------------------------------------
-- [UC052]: Xem danh sách thương hiệu kèm số lượng sản phẩm
-- ----------------------------------------------------------------------------
SELECT b.brand_id, b.brand_name, COUNT(p.product_id) AS total_products FROM brands b LEFT JOIN products p ON b.brand_id = p.brand_id WHERE b.brand_name LIKE N'%%' GROUP BY b.brand_id, b.brand_name ORDER BY total_products DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC053]: Thêm mới thương hiệu hoặc dòng sản phẩm
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM brands WHERE brand_name = N'DUNA Basic') INSERT INTO brands (brand_name, logo_url, description) VALUES (N'DUNA Basic', '/assets/logo_basic.png', N'Dòng sản phẩm cơ bản giá tốt');
GO

-- ----------------------------------------------------------------------------
-- [UC054]: Xem bảng màu sắc chuẩn kèm mã hiển thị hex
-- ----------------------------------------------------------------------------
SELECT color_id, color_name, hex_code, color_group FROM colors ORDER BY color_group, color_name;
GO

-- ----------------------------------------------------------------------------
-- [UC055]: Thêm mới màu sắc và mã hex trực quan
-- ----------------------------------------------------------------------------
SELECT * FROM colors; INSERT INTO colors (color_name, hex_code, color_group) VALUES (N'Xanh Navy Đậm', '#0A192F', 'BLUE');
GO

-- ----------------------------------------------------------------------------
-- [UC056]: Xóa màu sắc và kiểm tra biến thể đang sử dụng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM product_variants WHERE color_id = 14) DELETE FROM colors WHERE color_id = 14;
GO

-- ----------------------------------------------------------------------------
-- [UC057]: Xem danh sách kích cỡ theo loại trang phục (áo, quần, giày)
-- ----------------------------------------------------------------------------
SELECT size_id, size_code, category_type, display_order FROM sizes WHERE size_code LIKE '%%' ORDER BY category_type, display_order ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC058]: Thêm mới kích cỡ (S, M, L, XL, 2XL, 29, 30, 31)
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM sizes WHERE size_code = '33' AND category_type = 'PANTS') INSERT INTO sizes (size_code, category_type, display_order) VALUES ('33', 'PANTS', 6);
GO

-- ----------------------------------------------------------------------------
-- [UC059]: Xóa kích cỡ và kiểm tra ràng buộc với biến thể sản phẩm
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM product_variants WHERE size_id = 20) DELETE FROM sizes WHERE size_id = 20;
GO

-- ----------------------------------------------------------------------------
-- [UC060]: Xem bảng thông số size chuẩn theo danh mục
-- ----------------------------------------------------------------------------
SELECT c.category_name, s.size_code, d.min_height_cm, d.max_height_cm, d.min_weight_kg, d.max_weight_kg, d.min_chest_cm, d.max_chest_cm FROM size_chart_details d JOIN sizes s ON d.size_id = s.size_id JOIN categories c ON d.category_id = c.category_id WHERE d.category_id = 1 ORDER BY s.display_order;
GO

-- ----------------------------------------------------------------------------
-- [UC061]: Cập nhật khoảng chiều cao, cân nặng, vòng ngực trong bảng size
-- ----------------------------------------------------------------------------
SELECT * FROM size_chart_details WHERE chart_detail_id = 3; UPDATE size_chart_details SET min_height_cm = 165.0, max_height_cm = 172.0, min_chest_cm = 88.0, max_chest_cm = 94.0 WHERE chart_detail_id = 3;
GO

-- ----------------------------------------------------------------------------
-- [UC062]: Tư vấn size tự động theo chiều cao và cân nặng khách nhập
-- ----------------------------------------------------------------------------
SELECT s.size_code, r.form_fit FROM size_recommendation_rules r JOIN sizes s ON r.recommended_size_id = s.size_id WHERE r.category_id = 1 AND r.form_fit = 'SLIM_FIT' AND 172 BETWEEN r.height_min AND r.height_max AND 68 BETWEEN r.weight_min AND r.weight_max;
GO

-- ----------------------------------------------------------------------------
-- [UC063]: Thêm mới quy tắc tư vấn size cho form dáng Slim fit / Oversize
-- ----------------------------------------------------------------------------
SELECT * FROM size_recommendation_rules; INSERT INTO size_recommendation_rules (category_id, form_fit, height_min, height_max, weight_min, weight_max, recommended_size_id) VALUES (1, 'OVERSIZE', 165.0, 175.0, 60.0, 72.0, 2);
GO

-- ----------------------------------------------------------------------------
-- [UC064]: Xem danh sách sản phẩm phân trang và sắp xếp mới nhất
-- ----------------------------------------------------------------------------
SELECT p.product_id, p.product_code, p.product_name, c.category_name, p.base_price, p.cost_price, p.is_active FROM products p JOIN categories c ON p.category_id = c.category_id WHERE p.product_name LIKE N'%%' ORDER BY p.product_id DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;
GO

-- ----------------------------------------------------------------------------
-- [UC065]: Tìm kiếm sản phẩm theo mã sản phẩm hoặc tên
-- ----------------------------------------------------------------------------
SELECT product_id, product_code, product_name, base_price FROM products WHERE product_code LIKE '%SP001%' OR product_name LIKE N'%Sơ mi%';
GO

-- ----------------------------------------------------------------------------
-- [UC066]: Lọc sản phẩm theo danh mục, bộ sưu tập và khoảng giá
-- ----------------------------------------------------------------------------
SELECT p.product_id, p.product_name, p.base_price, c.category_name FROM products p JOIN categories c ON p.category_id = c.category_id WHERE p.category_id = 1 AND p.collection_id = 1 AND p.base_price BETWEEN 200000 AND 600000 AND p.is_active = 1 AND p.product_name LIKE N'%%';
GO

-- ----------------------------------------------------------------------------
-- [UC067]: Xem chi tiết sản phẩm kèm danh mục, thương hiệu và bộ sưu tập
-- ----------------------------------------------------------------------------
SELECT p.*, c.category_name, b.brand_name, col.collection_name FROM products p JOIN categories c ON p.category_id = c.category_id LEFT JOIN brands b ON p.brand_id = b.brand_id LEFT JOIN collections col ON p.collection_id = col.collection_id WHERE p.product_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC068]: Thêm mới sản phẩm thời trang nam với mã sản phẩm duy nhất
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'SP071') INSERT INTO products (product_code, product_name, slug, category_id, collection_id, brand_id, base_price, cost_price, material_info, form_fit, description, care_instructions) VALUES ('SP071', N'Áo Polo Pima Cotton Luxury', 'ao-polo-pima-cotton-luxury', 2, 1, 1, 450000, 220000, N'100% Pima Cotton', 'SLIM_FIT', N'Áo polo cao cấp thoáng mát', N'Giặt máy nhẹ, không dùng chất tẩy');
GO

-- ----------------------------------------------------------------------------
-- [UC069]: Cập nhật thông tin, chất liệu, form dáng và hướng dẫn giặt ủi
-- ----------------------------------------------------------------------------
SELECT * FROM products WHERE product_id = 2; UPDATE products SET material_info = N'95% Cotton, 5% Spandex co giãn', form_fit = 'REGULAR_FIT', care_instructions = N'Ủi ở nhiệt độ dưới 110 độ C', updated_at = GETDATE() WHERE product_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC070]: Cập nhật giá bán niêm yết và lưu lịch sử thay đổi giá
-- ----------------------------------------------------------------------------
SELECT product_id, base_price FROM products WHERE product_id = 1; INSERT INTO price_histories (product_id, old_price, new_price, changed_by, reason) SELECT product_id, base_price, 490000, 1, N'Điều chỉnh giá theo bộ sưu tập mới' FROM products WHERE product_id = 1; UPDATE products SET base_price = 490000, updated_at = GETDATE() WHERE product_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC071]: Bật tắt trạng thái kinh doanh và đánh dấu sản phẩm nổi bật
-- ----------------------------------------------------------------------------
SELECT product_id, is_active, is_featured FROM products WHERE product_id = 3; UPDATE products SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END, is_featured = 1, updated_at = GETDATE() WHERE product_id = 3;
GO

-- ----------------------------------------------------------------------------
-- [UC072]: Ngừng kinh doanh sản phẩm (xóa mềm) và ẩn toàn bộ biến thể
-- ----------------------------------------------------------------------------
SELECT product_id, is_active FROM products WHERE product_id = 10; UPDATE products SET is_active = 0, updated_at = GETDATE() WHERE product_id = 10; UPDATE product_variants SET is_active = 0 WHERE product_id = 10;
GO

-- ----------------------------------------------------------------------------
-- [UC073]: Xóa vĩnh viễn sản phẩm chưa tạo biến thể
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = 12) DELETE FROM products WHERE product_id = 12;
GO

-- ----------------------------------------------------------------------------
-- [UC074]: Xem top sản phẩm bán chạy theo số lượng đã bán
-- ----------------------------------------------------------------------------
SELECT TOP 10 product_id, product_code, product_name, sold_count, base_price FROM products WHERE is_active = 1 ORDER BY sold_count DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC075]: Tăng lượt xem sản phẩm khi khách mở trang chi tiết
-- ----------------------------------------------------------------------------
SELECT product_id, view_count FROM products WHERE product_id = 1; UPDATE products SET view_count = view_count + 1 WHERE product_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC076]: Xem danh sách biến thể màu và size kèm tồn kho
-- ----------------------------------------------------------------------------
SELECT pv.variant_id, pv.sku, pv.barcode, c.color_name, s.size_code, ISNULL(pv.price, p.base_price) AS sell_price, ISNULL(SUM(inv.stock_quantity), 0) AS stock_qty FROM product_variants pv JOIN products p ON pv.product_id = p.product_id JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = 1 AND pv.sku LIKE '%%' GROUP BY pv.variant_id, pv.sku, pv.barcode, c.color_name, s.size_code, pv.price, p.base_price;
GO

-- ----------------------------------------------------------------------------
-- [UC077]: Thêm mới biến thể sản phẩm ràng buộc duy nhất màu - size
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = 1 AND color_id = 1 AND size_id = 2) INSERT INTO product_variants (product_id, color_id, size_id, sku, barcode, price) VALUES (1, 1, 2, 'SP001-WHT-M', '893600100102', 350000);
GO

-- ----------------------------------------------------------------------------
-- [UC078]: Tự động sinh mã SKU và barcode cho biến thể mới
-- ----------------------------------------------------------------------------
SELECT p.product_code + '-' + UPPER(SUBSTRING(c.color_name, 1, 3)) + '-' + s.size_code AS generated_sku FROM products p, colors c, sizes s WHERE p.product_id = 1 AND c.color_id = 1 AND s.size_id = 2; UPDATE product_variants SET barcode = '8936' + RIGHT('00000000' + CAST(variant_id AS VARCHAR(8)), 8) WHERE barcode IS NULL;
GO

-- ----------------------------------------------------------------------------
-- [UC079]: Cập nhật giá bán riêng cho biến thể size lớn (2XL, 3XL)
-- ----------------------------------------------------------------------------
SELECT * FROM product_variants WHERE variant_id = 15; UPDATE product_variants SET price = 390000 WHERE variant_id = 15;
GO

-- ----------------------------------------------------------------------------
-- [UC080]: Xóa biến thể chưa có tồn kho và chưa phát sinh đơn hàng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM inventories WHERE variant_id = 20 AND stock_quantity > 0) AND NOT EXISTS (SELECT 1 FROM order_items WHERE variant_id = 20) BEGIN DELETE FROM inventories WHERE variant_id = 20; DELETE FROM product_variants WHERE variant_id = 20; END
GO

-- ----------------------------------------------------------------------------
-- [UC081]: Xem thư viện ảnh và video của sản phẩm theo màu sắc
-- ----------------------------------------------------------------------------
SELECT pm.media_id, pm.media_type, pm.media_url, pm.is_primary, c.color_name FROM product_media pm LEFT JOIN colors c ON pm.color_id = c.color_id WHERE pm.product_id = 1 ORDER BY pm.is_primary DESC, pm.display_order;
GO

-- ----------------------------------------------------------------------------
-- [UC082]: Tải lên ảnh sản phẩm gắn với từng màu sắc
-- ----------------------------------------------------------------------------
SELECT * FROM product_media WHERE product_id = 1; INSERT INTO product_media (product_id, color_id, media_type, media_url, is_primary, display_order) VALUES (1, 2, 'IMAGE', '/uploads/sp001_navy_angle1.jpg', 0, 2);
GO

-- ----------------------------------------------------------------------------
-- [UC083]: Đặt ảnh đại diện chính hiển thị trên website
-- ----------------------------------------------------------------------------
SELECT * FROM product_media WHERE product_id = 2; UPDATE product_media SET is_primary = 0 WHERE product_id = 2; UPDATE product_media SET is_primary = 1 WHERE media_id = 5;
GO

-- ----------------------------------------------------------------------------
-- [UC084]: Xem danh sách thẻ sản phẩm kèm số sản phẩm được gắn
-- ----------------------------------------------------------------------------
SELECT t.tag_id, t.tag_name, t.slug, COUNT(m.product_id) AS total_products FROM product_tags t LEFT JOIN product_tag_map m ON t.tag_id = m.tag_id WHERE t.tag_name LIKE N'%%' GROUP BY t.tag_id, t.tag_name, t.slug ORDER BY total_products DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC085]: Gắn thẻ cho sản phẩm (Đi làm, Dạo phố, Mùa hè)
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM product_tag_map WHERE product_id = 1 AND tag_id = 3) INSERT INTO product_tag_map (product_id, tag_id) VALUES (1, 3);
GO

-- ----------------------------------------------------------------------------
-- [UC086]: Gỡ thẻ khỏi sản phẩm
-- ----------------------------------------------------------------------------
SELECT * FROM product_tag_map WHERE product_id = 1; DELETE FROM product_tag_map WHERE product_id = 1 AND tag_id = 3;
GO

-- ----------------------------------------------------------------------------
-- [UC087]: Xem lịch sử thay đổi giá của sản phẩm
-- ----------------------------------------------------------------------------
SELECT h.price_history_id, p.product_name, h.old_price, h.new_price, u.full_name AS changed_by_name, h.reason, h.changed_at FROM price_histories h JOIN products p ON h.product_id = p.product_id LEFT JOIN users u ON h.changed_by = u.user_id WHERE h.product_id = 1 AND p.product_name LIKE N'%%' ORDER BY h.changed_at DESC;
GO


-- ############################################################################
-- M04 - KHO & NHẬP HÀNG
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC088]: Xem danh sách kho hàng kèm tổng số lượng tồn từng kho
-- ----------------------------------------------------------------------------
SELECT w.warehouse_id, w.warehouse_code, w.warehouse_name, w.address, w.is_active, ISNULL(SUM(i.stock_quantity), 0) AS total_stock FROM warehouses w LEFT JOIN inventories i ON w.warehouse_id = i.warehouse_id WHERE w.warehouse_name LIKE N'%%' GROUP BY w.warehouse_id, w.warehouse_code, w.warehouse_name, w.address, w.is_active;
GO

-- ----------------------------------------------------------------------------
-- [UC089]: Thêm mới kho hàng (kho tổng, kho cửa hàng)
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM warehouses WHERE warehouse_code = 'KHO-HN02') INSERT INTO warehouses (warehouse_code, warehouse_name, address, phone) VALUES ('KHO-HN02', N'Kho Hà Nội 02', N'Số 5 đường Phạm Hùng, Hà Nội', '0243999888');
GO

-- ----------------------------------------------------------------------------
-- [UC090]: Cập nhật thông tin hoặc ngừng hoạt động kho hàng
-- ----------------------------------------------------------------------------
SELECT * FROM warehouses WHERE warehouse_id = 2; UPDATE warehouses SET address = N'Số 8 đường Nguyễn Trãi, Hà Nội', is_active = 1 WHERE warehouse_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC091]: Xem tồn kho theo từng biến thể size màu và từng kho
-- ----------------------------------------------------------------------------
SELECT w.warehouse_name, p.product_name, c.color_name, s.size_code, inv.stock_quantity, inv.reserved_quantity, (inv.stock_quantity - inv.reserved_quantity) AS available_qty FROM inventories inv JOIN warehouses w ON inv.warehouse_id = w.warehouse_id JOIN product_variants pv ON inv.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id;
GO

-- ----------------------------------------------------------------------------
-- [UC092]: Tra cứu tồn kho theo SKU hoặc mã vạch
-- ----------------------------------------------------------------------------
SELECT pv.sku, pv.barcode, w.warehouse_name, inv.stock_quantity, inv.reserved_quantity FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id JOIN warehouses w ON inv.warehouse_id = w.warehouse_id WHERE pv.sku LIKE 'SP001%' OR pv.barcode = '893600100102';
GO

-- ----------------------------------------------------------------------------
-- [UC093]: Cảnh báo tồn kho dưới ngưỡng an toàn
-- ----------------------------------------------------------------------------
SELECT p.product_name, pv.sku, inv.stock_quantity, inv.low_stock_threshold FROM inventories inv JOIN product_variants pv ON inv.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id WHERE inv.stock_quantity <= inv.low_stock_threshold;
GO

-- ----------------------------------------------------------------------------
-- [UC094]: Cập nhật ngưỡng cảnh báo hết hàng cho biến thể
-- ----------------------------------------------------------------------------
SELECT * FROM inventories WHERE variant_id = 1; UPDATE inventories SET low_stock_threshold = 10, updated_at = GETDATE() WHERE variant_id = 1 AND warehouse_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC095]: Giữ chỗ tồn kho khi khách đặt hàng online
-- ----------------------------------------------------------------------------
SELECT stock_quantity, reserved_quantity FROM inventories WHERE variant_id = 4 AND warehouse_id = 1; UPDATE inventories SET reserved_quantity = reserved_quantity + 1, updated_at = GETDATE() WHERE variant_id = 4 AND warehouse_id = 1 AND (stock_quantity - reserved_quantity) >= 1;
GO

-- ----------------------------------------------------------------------------
-- [UC096]: Giải phóng số lượng giữ chỗ khi đơn hàng bị hủy
-- ----------------------------------------------------------------------------
SELECT * FROM inventories WHERE variant_id = 4 AND warehouse_id = 1; UPDATE inventories SET reserved_quantity = CASE WHEN reserved_quantity >= 1 THEN reserved_quantity - 1 ELSE 0 END, updated_at = GETDATE() WHERE variant_id = 4 AND warehouse_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC097]: Xem nhật ký xuất nhập tồn kho chi tiết
-- ----------------------------------------------------------------------------
SELECT l.log_id, pv.sku, w.warehouse_name, l.change_type, l.quantity_changed, l.reason, u.full_name, l.created_at FROM inventory_logs l JOIN product_variants pv ON l.variant_id = pv.variant_id LEFT JOIN warehouses w ON l.warehouse_id = w.warehouse_id LEFT JOIN users u ON l.created_by = u.user_id WHERE pv.sku LIKE '%%' ORDER BY l.log_id DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC098]: Lọc nhật ký tồn kho theo loại thay đổi và khoảng ngày
-- ----------------------------------------------------------------------------
SELECT log_id, variant_id, change_type, quantity_changed, reason, created_at FROM inventory_logs WHERE change_type IN ('SALE', 'RETURN', 'DAMAGE') AND created_at BETWEEN '2026-11-01' AND '2026-11-30' AND change_type LIKE '%%' ORDER BY created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC099]: Điều chỉnh tồn kho thủ công kèm lý do và ghi nhật ký
-- ----------------------------------------------------------------------------
SELECT * FROM inventories WHERE variant_id = 5 AND warehouse_id = 1; UPDATE inventories SET stock_quantity = 48, updated_at = GETDATE() WHERE variant_id = 5 AND warehouse_id = 1; INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reason, created_by) VALUES (5, 1, 'ADJUSTMENT', -2, N'Kiểm kê hao hụt cuối tháng', 2);
GO

-- ----------------------------------------------------------------------------
-- [UC100]: Xem danh sách nhà cung cấp và xưởng may
-- ----------------------------------------------------------------------------
SELECT supplier_id, supplier_code, supplier_name, contact_person, phone, payment_terms, is_active FROM suppliers WHERE supplier_name LIKE N'%%' ORDER BY supplier_name;
GO

-- ----------------------------------------------------------------------------
-- [UC101]: Thêm mới nhà cung cấp kèm mã số thuế
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_code = 'NCC015') INSERT INTO suppliers (supplier_code, supplier_name, contact_person, phone, email, address, tax_code, payment_terms) VALUES ('NCC015', N'Xưởng may Hoàng Gia', N'Phạm Minh Tuấn', '0988777666', 'hoanggia@gmail.com', N'Hà Đông, Hà Nội', '0109876543', N'Thanh toán 30 ngày');
GO

-- ----------------------------------------------------------------------------
-- [UC102]: Cập nhật thông tin liên hệ và điều khoản thanh toán nhà cung cấp
-- ----------------------------------------------------------------------------
SELECT * FROM suppliers WHERE supplier_id = 3; UPDATE suppliers SET phone = '0977111222', payment_terms = N'Thanh toán 45 ngày', updated_at = GETDATE() WHERE supplier_id = 3;
GO

-- ----------------------------------------------------------------------------
-- [UC103]: Ngừng hợp tác nhà cung cấp và kiểm tra đơn nhập đang mở
-- ----------------------------------------------------------------------------
UPDATE suppliers SET is_active = 0, updated_at = GETDATE() WHERE supplier_id = 7 AND NOT EXISTS (SELECT 1 FROM purchase_orders WHERE supplier_id = 7 AND status IN ('DRAFT', 'CONFIRMED', 'PARTIAL'));
GO

-- ----------------------------------------------------------------------------
-- [UC104]: Tạo đơn đặt hàng nhà cung cấp kèm chi tiết biến thể
-- ----------------------------------------------------------------------------
DECLARE @po_id INT; INSERT INTO purchase_orders (po_code, supplier_id, warehouse_id, status, expected_date, note, created_by) VALUES ('PO20261117-01', 1, 1, 'DRAFT', '2026-11-30', N'Nhập hàng bộ sưu tập Đông', 2); SET @po_id = SCOPE_IDENTITY(); INSERT INTO purchase_order_items (po_id, variant_id, quantity, unit_cost) VALUES (@po_id, 1, 100, 210000), (@po_id, 2, 80, 210000); UPDATE purchase_orders SET total_amount = (SELECT SUM(quantity * unit_cost) FROM purchase_order_items WHERE po_id = @po_id) WHERE po_id = @po_id;
GO

-- ----------------------------------------------------------------------------
-- [UC105]: Xem danh sách đơn đặt hàng nhà cung cấp theo trạng thái
-- ----------------------------------------------------------------------------
SELECT po.po_id, po.po_code, s.supplier_name, w.warehouse_name, po.status, po.total_amount, po.expected_date FROM purchase_orders po JOIN suppliers s ON po.supplier_id = s.supplier_id JOIN warehouses w ON po.warehouse_id = w.warehouse_id WHERE po.status = 'CONFIRMED' AND po.po_code LIKE '%%' ORDER BY po.po_id DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC106]: Xác nhận đơn đặt hàng gửi nhà cung cấp
-- ----------------------------------------------------------------------------
SELECT * FROM purchase_orders WHERE po_id = 1; UPDATE purchase_orders SET status = 'CONFIRMED', updated_at = GETDATE() WHERE po_id = 1 AND status = 'DRAFT';
GO

-- ----------------------------------------------------------------------------
-- [UC107]: Hủy đơn đặt hàng nhà cung cấp chưa nhận hàng
-- ----------------------------------------------------------------------------
SELECT * FROM purchase_orders WHERE po_id = 2; UPDATE purchase_orders SET status = 'CANCELLED', updated_at = GETDATE() WHERE po_id = 2 AND status IN ('DRAFT', 'CONFIRMED') AND NOT EXISTS (SELECT 1 FROM goods_receipts WHERE po_id = 2);
GO

-- ----------------------------------------------------------------------------
-- [UC108]: Lập phiếu nhập kho theo đơn đặt hàng và cộng tồn kho
-- ----------------------------------------------------------------------------
DECLARE @receipt_id INT; INSERT INTO goods_receipts (receipt_code, po_id, warehouse_id, received_by, note) VALUES ('GR20261120-01', 1, 1, 3, N'Nhập đủ theo PO'); SET @receipt_id = SCOPE_IDENTITY(); INSERT INTO goods_receipt_items (receipt_id, variant_id, quantity, unit_cost) VALUES (@receipt_id, 1, 100, 210000); UPDATE inventories SET stock_quantity = stock_quantity + 100, updated_at = GETDATE() WHERE variant_id = 1 AND warehouse_id = 1; INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id, reason, created_by) VALUES (1, 1, 'RESTOCK', 100, 'GOODS_RECEIPT', @receipt_id, N'Nhập hàng từ nhà cung cấp', 3);
GO

-- ----------------------------------------------------------------------------
-- [UC109]: Cập nhật số lượng đã nhận và trạng thái đơn đặt hàng (nhận một phần / đủ)
-- ----------------------------------------------------------------------------
SELECT po_item_id, quantity, received_quantity FROM purchase_order_items WHERE po_id = 1; UPDATE purchase_order_items SET received_quantity = received_quantity + 100 WHERE po_id = 1 AND variant_id = 1; UPDATE purchase_orders SET status = CASE WHEN (SELECT SUM(quantity - received_quantity) FROM purchase_order_items WHERE po_id = 1) <= 0 THEN 'RECEIVED' ELSE 'PARTIAL' END WHERE po_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC110]: Tạo phiếu điều chuyển hàng giữa các kho
-- ----------------------------------------------------------------------------
DECLARE @transfer_id INT; INSERT INTO stock_transfers (transfer_code, from_warehouse_id, to_warehouse_id, created_by) VALUES ('TF20261118-01', 1, 2, 2); SET @transfer_id = SCOPE_IDENTITY(); INSERT INTO stock_transfer_items (transfer_id, variant_id, quantity) VALUES (@transfer_id, 1, 20), (@transfer_id, 3, 15);
GO

-- ----------------------------------------------------------------------------
-- [UC111]: Hoàn tất điều chuyển kho và cập nhật tồn hai kho
-- ----------------------------------------------------------------------------
SELECT * FROM stock_transfer_items WHERE transfer_id = 1; UPDATE inventories SET stock_quantity = stock_quantity - 20 WHERE variant_id = 1 AND warehouse_id = 1; UPDATE inventories SET stock_quantity = stock_quantity + 20 WHERE variant_id = 1 AND warehouse_id = 2; UPDATE stock_transfers SET status = 'COMPLETED', completed_at = GETDATE() WHERE transfer_id = 1; INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id) VALUES (1, 1, 'TRANSFER_OUT', -20, 'STOCK_TRANSFER', 1), (1, 2, 'TRANSFER_IN', 20, 'STOCK_TRANSFER', 1);
GO

-- ----------------------------------------------------------------------------
-- [UC112]: Mở phiếu kiểm kê và ghi nhận số lượng đếm thực tế
-- ----------------------------------------------------------------------------
DECLARE @st_id INT; INSERT INTO stocktakes (stocktake_code, warehouse_id, created_by) VALUES ('KK20261130-01', 1, 3); SET @st_id = SCOPE_IDENTITY(); INSERT INTO stocktake_items (stocktake_id, variant_id, system_quantity, counted_quantity) SELECT @st_id, variant_id, stock_quantity, stock_quantity FROM inventories WHERE warehouse_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC113]: Chốt kiểm kê, đối chiếu chênh lệch và điều chỉnh tồn kho
-- ----------------------------------------------------------------------------
SELECT variant_id, system_quantity, counted_quantity, (counted_quantity - system_quantity) AS diff FROM stocktake_items WHERE stocktake_id = 1 AND counted_quantity <> system_quantity; UPDATE inv SET stock_quantity = si.counted_quantity, updated_at = GETDATE() FROM inventories inv JOIN stocktake_items si ON inv.variant_id = si.variant_id WHERE si.stocktake_id = 1 AND inv.warehouse_id = 1; UPDATE stocktakes SET status = 'COMPLETED', completed_at = GETDATE() WHERE stocktake_id = 1;
GO


-- ############################################################################
-- M05 - GIỎ HÀNG & YÊU THÍCH
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC114]: Xem giỏ hàng hiện tại kèm hình ảnh, màu sắc và size
-- ----------------------------------------------------------------------------
SELECT ci.cart_item_id, p.product_name, c.color_name, s.size_code, ci.quantity, ci.unit_price, (ci.quantity * ci.unit_price) AS line_total FROM cart_items ci JOIN carts ct ON ci.cart_id = ct.cart_id JOIN product_variants pv ON ci.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id WHERE ct.user_id = 101 AND ct.status = 'ACTIVE';
GO

-- ----------------------------------------------------------------------------
-- [UC115]: Thêm sản phẩm vào giỏ hàng và cộng dồn số lượng nếu đã tồn tại
-- ----------------------------------------------------------------------------
UPDATE cart_items SET quantity = quantity + 1 WHERE cart_id = 1 AND variant_id = 4; INSERT INTO cart_items (cart_id, variant_id, quantity, unit_price) SELECT 1, variant_id, 1, ISNULL(price, 350000) FROM product_variants WHERE variant_id = 4 AND NOT EXISTS (SELECT 1 FROM cart_items WHERE cart_id = 1 AND variant_id = 4);
GO

-- ----------------------------------------------------------------------------
-- [UC116]: Cập nhật số lượng sản phẩm trong giỏ có kiểm tra tồn kho
-- ----------------------------------------------------------------------------
SELECT * FROM cart_items WHERE cart_id = 1; UPDATE cart_items SET quantity = 3 WHERE cart_id = 1 AND variant_id = 4 AND EXISTS (SELECT 1 FROM inventories WHERE variant_id = 4 AND (stock_quantity - reserved_quantity) >= 3);
GO

-- ----------------------------------------------------------------------------
-- [UC117]: Xóa một sản phẩm khỏi giỏ hàng
-- ----------------------------------------------------------------------------
SELECT * FROM cart_items WHERE cart_id = 1; DELETE FROM cart_items WHERE cart_id = 1 AND variant_id = 4;
GO

-- ----------------------------------------------------------------------------
-- [UC118]: Áp dụng mã voucher vào giỏ hàng sau khi kiểm tra điều kiện
-- ----------------------------------------------------------------------------
SELECT v.voucher_id, v.discount_type, v.discount_value FROM vouchers v WHERE v.voucher_code = 'DUNA10' AND v.is_active = 1 AND GETDATE() BETWEEN v.start_date AND v.end_date AND (v.usage_limit IS NULL OR v.used_count < v.usage_limit); UPDATE carts SET voucher_id = (SELECT voucher_id FROM vouchers WHERE voucher_code = 'DUNA10'), updated_at = GETDATE() WHERE cart_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC119]: Tính tổng tiền tạm tính và số lượng sản phẩm của giỏ hàng
-- ----------------------------------------------------------------------------
SELECT SUM(quantity) AS total_items, SUM(quantity * unit_price) AS subtotal FROM cart_items WHERE cart_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC120]: Hợp nhất giỏ hàng khách vãng lai vào giỏ tài khoản sau khi đăng nhập
-- ----------------------------------------------------------------------------
SELECT cart_id FROM carts WHERE session_token = 'sess-abc123' AND status = 'ACTIVE'; UPDATE cart_items SET cart_id = 1 WHERE cart_id = 2 AND variant_id NOT IN (SELECT variant_id FROM cart_items WHERE cart_id = 1); UPDATE carts SET status = 'CONVERTED', updated_at = GETDATE() WHERE cart_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC121]: Đánh dấu giỏ hàng bị bỏ quên sau 24 giờ không hoạt động
-- ----------------------------------------------------------------------------
SELECT cart_id, user_id, updated_at FROM carts WHERE status = 'ACTIVE' AND updated_at < DATEADD(HOUR, -24, GETDATE()); UPDATE carts SET status = 'ABANDONED' WHERE status = 'ACTIVE' AND updated_at < DATEADD(HOUR, -24, GETDATE());
GO

-- ----------------------------------------------------------------------------
-- [UC122]: Lấy danh sách giỏ hàng bỏ quên để gửi email nhắc mua
-- ----------------------------------------------------------------------------
SELECT u.email, u.full_name, ct.cart_id, SUM(ci.quantity * ci.unit_price) AS cart_value FROM carts ct JOIN users u ON ct.user_id = u.user_id JOIN cart_items ci ON ct.cart_id = ci.cart_id WHERE ct.status = 'ABANDONED' GROUP BY u.email, u.full_name, ct.cart_id HAVING SUM(ci.quantity * ci.unit_price) > 300000;
GO

-- ----------------------------------------------------------------------------
-- [UC123]: Xem danh sách sản phẩm yêu thích của khách hàng
-- ----------------------------------------------------------------------------
SELECT w.wishlist_id, p.product_id, p.product_name, p.base_price, w.created_at FROM wishlists w JOIN products p ON w.product_id = p.product_id WHERE w.user_id = 101 AND p.product_name LIKE N'%%' ORDER BY w.created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC124]: Thêm sản phẩm vào danh sách yêu thích
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM wishlists WHERE user_id = 101 AND product_id = 5) INSERT INTO wishlists (user_id, product_id) VALUES (101, 5);
GO

-- ----------------------------------------------------------------------------
-- [UC125]: Bỏ sản phẩm khỏi danh sách yêu thích
-- ----------------------------------------------------------------------------
SELECT * FROM wishlists WHERE user_id = 101; DELETE FROM wishlists WHERE user_id = 101 AND product_id = 5;
GO


-- ############################################################################
-- M06 - ĐƠN HÀNG & ĐỔI TRẢ
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC126]: Xem danh sách đơn hàng phân trang dành cho quản trị
-- ----------------------------------------------------------------------------
SELECT o.order_id, o.order_code, o.receiver_name, o.channel, o.status, o.payment_status, o.total_amount, o.ordered_at FROM orders o WHERE o.order_code LIKE '%%' ORDER BY o.order_id DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;
GO

-- ----------------------------------------------------------------------------
-- [UC127]: Tìm kiếm đơn hàng theo mã đơn, số điện thoại hoặc tên người nhận
-- ----------------------------------------------------------------------------
SELECT order_id, order_code, receiver_name, receiver_phone, status, total_amount FROM orders WHERE order_code LIKE 'DN2611%' OR receiver_phone LIKE '%0912%' OR receiver_name LIKE N'%Nam%';
GO

-- ----------------------------------------------------------------------------
-- [UC128]: Lọc đơn hàng theo trạng thái, kênh bán và khoảng ngày đặt
-- ----------------------------------------------------------------------------
SELECT order_id, order_code, channel, status, payment_status, total_amount, ordered_at FROM orders WHERE status = 'PENDING' AND channel = 'WEB' AND ordered_at BETWEEN '2026-11-01' AND '2026-11-30' AND order_code LIKE '%%';
GO

-- ----------------------------------------------------------------------------
-- [UC129]: Xem chi tiết đơn hàng kèm danh sách sản phẩm đã mua
-- ----------------------------------------------------------------------------
SELECT o.order_code, o.receiver_name, o.shipping_address, o.status, o.subtotal, o.discount_amount, o.shipping_fee, o.total_amount FROM orders o WHERE o.order_id = 1; SELECT oi.product_name, oi.sku, oi.quantity, oi.unit_price, oi.line_total FROM order_items oi WHERE oi.order_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC130]: Xem lịch sử mua hàng của một khách hàng
-- ----------------------------------------------------------------------------
SELECT o.order_id, o.order_code, o.status, o.total_amount, o.ordered_at FROM orders o WHERE o.customer_id = 1 AND o.order_code LIKE '%%' ORDER BY o.ordered_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC131]: Tạo mã đơn hàng tự động theo ngày (DNyyMMdd-số thứ tự)
-- ----------------------------------------------------------------------------
SELECT 'DN' + CONVERT(VARCHAR(6), GETDATE(), 12) + '-' + RIGHT('0000' + CAST(COUNT(*) + 1 AS VARCHAR(4)), 4) AS next_order_code FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE);
GO

-- ----------------------------------------------------------------------------
-- [UC132]: Đặt hàng online từ giỏ hàng: tạo đơn, chi tiết đơn và giữ chỗ tồn kho
-- ----------------------------------------------------------------------------
DECLARE @order_id BIGINT; INSERT INTO orders (order_code, customer_id, user_id, channel, receiver_name, receiver_phone, shipping_address, province_id, ward_id, subtotal, shipping_fee, total_amount) VALUES ('DN261117-0001', 1, 101, 'WEB', N'Trần Văn Nam', '0987654321', N'Số 25 ngõ 12 đường Láng', 1, 15, 700000, 30000, 730000); SET @order_id = SCOPE_IDENTITY(); INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total) SELECT @order_id, ci.variant_id, p.product_name, pv.sku, ci.quantity, ci.unit_price, ci.quantity * ci.unit_price FROM cart_items ci JOIN product_variants pv ON ci.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id WHERE ci.cart_id = 1; UPDATE inv SET reserved_quantity = inv.reserved_quantity + ci.quantity FROM inventories inv JOIN cart_items ci ON inv.variant_id = ci.variant_id WHERE ci.cart_id = 1; UPDATE carts SET status = 'CONVERTED', updated_at = GETDATE() WHERE cart_id = 1; INSERT INTO order_status_histories (order_id, old_status, new_status, note) VALUES (@order_id, NULL, 'PENDING', N'Khách đặt hàng trên website');
GO

-- ----------------------------------------------------------------------------
-- [UC133]: Nhân viên tạo đơn hàng hộ khách qua điện thoại
-- ----------------------------------------------------------------------------
SELECT customer_id, full_name FROM customers WHERE phone = '0933111222'; INSERT INTO orders (order_code, customer_id, handled_by, channel, receiver_name, receiver_phone, shipping_address, province_id, ward_id, subtotal, shipping_fee, total_amount, note) VALUES ('DN261117-0002', 2, 3, 'PHONE', N'Lê Quang Huy', '0933111222', N'Số 10 phố Huế', 1, 20, 500000, 30000, 530000, N'Khách đặt qua hotline');
GO

-- ----------------------------------------------------------------------------
-- [UC134]: Xác nhận đơn hàng và ghi nhận lịch sử trạng thái
-- ----------------------------------------------------------------------------
SELECT order_id, status FROM orders WHERE order_id = 1; UPDATE orders SET status = 'CONFIRMED', handled_by = 3, updated_at = GETDATE() WHERE order_id = 1 AND status = 'PENDING'; INSERT INTO order_status_histories (order_id, old_status, new_status, changed_by, note) VALUES (1, 'PENDING', 'CONFIRMED', 3, N'Đã xác nhận qua điện thoại');
GO

-- ----------------------------------------------------------------------------
-- [UC135]: Chuyển đơn hàng sang trạng thái đóng gói
-- ----------------------------------------------------------------------------
SELECT order_id, status FROM orders WHERE order_id = 1; UPDATE orders SET status = 'PACKING', updated_at = GETDATE() WHERE order_id = 1 AND status = 'CONFIRMED'; INSERT INTO order_status_histories (order_id, old_status, new_status, changed_by) VALUES (1, 'CONFIRMED', 'PACKING', 3);
GO

-- ----------------------------------------------------------------------------
-- [UC136]: Chuyển đơn hàng sang trạng thái đang giao cho đơn vị vận chuyển
-- ----------------------------------------------------------------------------
SELECT order_id, status FROM orders WHERE order_id = 1; UPDATE orders SET status = 'SHIPPING', updated_at = GETDATE() WHERE order_id = 1 AND status = 'PACKING'; INSERT INTO order_status_histories (order_id, old_status, new_status, changed_by) VALUES (1, 'PACKING', 'SHIPPING', 3);
GO

-- ----------------------------------------------------------------------------
-- [UC137]: Xác nhận giao hàng thành công và cập nhật thanh toán COD
-- ----------------------------------------------------------------------------
SELECT order_id, status, payment_status FROM orders WHERE order_id = 1; UPDATE orders SET status = 'DELIVERED', payment_status = 'PAID', updated_at = GETDATE() WHERE order_id = 1 AND status = 'SHIPPING'; UPDATE payments SET status = 'SUCCESS', paid_at = GETDATE() WHERE order_id = 1 AND status = 'PENDING'; INSERT INTO order_status_histories (order_id, old_status, new_status, note) VALUES (1, 'SHIPPING', 'DELIVERED', N'Giao hàng thành công');
GO

-- ----------------------------------------------------------------------------
-- [UC138]: Hoàn tất đơn hàng, trừ tồn thực tế và cộng chi tiêu tích lũy cho khách
-- ----------------------------------------------------------------------------
SELECT order_id, customer_id, total_amount FROM orders WHERE order_id = 1 AND status = 'DELIVERED'; UPDATE orders SET status = 'COMPLETED', updated_at = GETDATE() WHERE order_id = 1; UPDATE customers SET total_spent = total_spent + (SELECT total_amount FROM orders WHERE order_id = 1), total_orders = total_orders + 1 WHERE customer_id = (SELECT customer_id FROM orders WHERE order_id = 1); UPDATE products SET sold_count = sold_count + 1 WHERE product_id IN (SELECT pv.product_id FROM order_items oi JOIN product_variants pv ON oi.variant_id = pv.variant_id WHERE oi.order_id = 1);
GO

-- ----------------------------------------------------------------------------
-- [UC139]: Hủy đơn hàng, ghi lý do và hoàn trả tồn kho giữ chỗ
-- ----------------------------------------------------------------------------
SELECT order_id, status FROM orders WHERE order_id = 2 AND status IN ('PENDING', 'CONFIRMED'); INSERT INTO order_cancellations (order_id, reason_code, reason_detail, cancelled_by) VALUES (2, 'CUSTOMER_CHANGED_MIND', N'Khách muốn đổi size khác', 3); UPDATE orders SET status = 'CANCELLED', updated_at = GETDATE() WHERE order_id = 2; UPDATE inv SET reserved_quantity = CASE WHEN inv.reserved_quantity >= oi.quantity THEN inv.reserved_quantity - oi.quantity ELSE 0 END FROM inventories inv JOIN order_items oi ON inv.variant_id = oi.variant_id WHERE oi.order_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC140]: Thêm ghi chú nội bộ cho đơn hàng
-- ----------------------------------------------------------------------------
SELECT * FROM order_notes WHERE order_id = 1; INSERT INTO order_notes (order_id, user_id, note_content, is_internal) VALUES (1, 3, N'Khách dặn gọi trước khi giao 30 phút', 1);
GO

-- ----------------------------------------------------------------------------
-- [UC141]: Xem danh sách ghi chú của đơn hàng kèm người ghi
-- ----------------------------------------------------------------------------
SELECT n.order_note_id, u.full_name, n.note_content, n.is_internal, n.created_at FROM order_notes n LEFT JOIN users u ON n.user_id = u.user_id WHERE n.order_id = 1 AND n.note_content LIKE N'%%' ORDER BY n.created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC142]: Cập nhật địa chỉ giao hàng của đơn chưa đóng gói
-- ----------------------------------------------------------------------------
SELECT order_id, status, shipping_address FROM orders WHERE order_id = 3; UPDATE orders SET receiver_name = N'Lê Quang Huy', receiver_phone = '0933111222', shipping_address = N'Số 12 đường Cầu Giấy', ward_id = 30, updated_at = GETDATE() WHERE order_id = 3 AND status IN ('PENDING', 'CONFIRMED');
GO

-- ----------------------------------------------------------------------------
-- [UC143]: Xem lịch sử thay đổi trạng thái của đơn hàng
-- ----------------------------------------------------------------------------
SELECT h.history_id, h.old_status, h.new_status, u.full_name AS changed_by_name, h.note, h.changed_at FROM order_status_histories h LEFT JOIN users u ON h.changed_by = u.user_id WHERE h.order_id = 1 AND h.new_status LIKE '%%' ORDER BY h.changed_at ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC144]: Xuất hóa đơn bán hàng cho đơn đã hoàn tất
-- ----------------------------------------------------------------------------
SELECT order_id, total_amount, tax_amount FROM orders WHERE order_id = 1 AND status = 'COMPLETED'; IF NOT EXISTS (SELECT 1 FROM invoices WHERE order_id = 1 AND status = 'ISSUED') INSERT INTO invoices (invoice_number, order_id, buyer_name, company_name, tax_code, billing_address, total_amount, tax_amount) VALUES ('HD0000101', 1, N'Trần Văn Nam', N'Công ty TNHH ABC', '0101234567', N'Hà Nội', 730000, 66364);
GO

-- ----------------------------------------------------------------------------
-- [UC145]: Tra cứu hóa đơn theo mã đơn hàng hoặc mã số thuế
-- ----------------------------------------------------------------------------
SELECT i.invoice_number, o.order_code, i.buyer_name, i.company_name, i.tax_code, i.total_amount, i.status, i.issued_at FROM invoices i JOIN orders o ON i.order_id = o.order_id WHERE (o.order_code = 'DN261117-0001' OR i.tax_code = '0101234567') AND i.invoice_number LIKE '%%';
GO

-- ----------------------------------------------------------------------------
-- [UC146]: Hủy hóa đơn đã xuất do sai thông tin
-- ----------------------------------------------------------------------------
SELECT * FROM invoices WHERE invoice_id = 5; UPDATE invoices SET status = 'CANCELLED' WHERE invoice_id = 5 AND status = 'ISSUED';
GO

-- ----------------------------------------------------------------------------
-- [UC147]: Khách tạo yêu cầu đổi trả hàng kèm sản phẩm cần đổi trả
-- ----------------------------------------------------------------------------
DECLARE @return_id INT; INSERT INTO return_requests (return_code, order_id, customer_id, request_type, reason) VALUES ('RT20261120-01', 1, 1, 'EXCHANGE', N'Áo bị chật, muốn đổi sang size L'); SET @return_id = SCOPE_IDENTITY(); INSERT INTO return_request_items (return_id, order_item_id, quantity, exchange_variant_id, condition_note) VALUES (@return_id, 1, 1, 8, N'Còn nguyên tem mác, chưa giặt');
GO

-- ----------------------------------------------------------------------------
-- [UC148]: Xem danh sách yêu cầu đổi trả đang chờ xử lý
-- ----------------------------------------------------------------------------
SELECT r.return_id, r.return_code, o.order_code, c.full_name, r.request_type, r.reason, r.status, r.created_at FROM return_requests r JOIN orders o ON r.order_id = o.order_id LEFT JOIN customers c ON r.customer_id = c.customer_id WHERE r.status = 'PENDING' AND r.return_code LIKE '%%' ORDER BY r.created_at ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC149]: Duyệt yêu cầu đổi trả hàng của khách
-- ----------------------------------------------------------------------------
SELECT * FROM return_requests WHERE return_id = 1; UPDATE return_requests SET status = 'APPROVED', handled_by = 3, updated_at = GETDATE() WHERE return_id = 1 AND status = 'PENDING';
GO

-- ----------------------------------------------------------------------------
-- [UC150]: Từ chối yêu cầu đổi trả và ghi chú lý do
-- ----------------------------------------------------------------------------
SELECT * FROM return_requests WHERE return_id = 2; UPDATE return_requests SET status = 'REJECTED', handled_by = 3, updated_at = GETDATE() WHERE return_id = 2 AND status = 'PENDING'; INSERT INTO order_notes (order_id, user_id, note_content) SELECT order_id, 3, N'Từ chối đổi trả: sản phẩm đã qua sử dụng' FROM return_requests WHERE return_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC151]: Nhận hàng trả về và nhập lại kho
-- ----------------------------------------------------------------------------
SELECT * FROM return_request_items WHERE return_id = 1; UPDATE return_requests SET status = 'RECEIVED', updated_at = GETDATE() WHERE return_id = 1 AND status = 'APPROVED'; UPDATE inventories SET stock_quantity = stock_quantity + 1, updated_at = GETDATE() WHERE variant_id = 5 AND warehouse_id = 1; INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id, reason) VALUES (5, 1, 'RETURN', 1, 'RETURN_REQUEST', 1, N'Khách trả hàng còn nguyên tem');
GO

-- ----------------------------------------------------------------------------
-- [UC152]: Tạo yêu cầu hoàn tiền sau khi nhận hàng trả
-- ----------------------------------------------------------------------------
SELECT * FROM return_requests WHERE return_id = 1 AND status = 'RECEIVED'; INSERT INTO refunds (order_id, return_id, payment_id, amount, refund_method) VALUES (1, 1, 1, 350000, 'BANK_TRANSFER');
GO

-- ----------------------------------------------------------------------------
-- [UC153]: Hoàn tất hoàn tiền và cập nhật trạng thái thanh toán đơn hàng
-- ----------------------------------------------------------------------------
SELECT * FROM refunds WHERE refund_id = 1; UPDATE refunds SET status = 'COMPLETED', processed_at = GETDATE() WHERE refund_id = 1; UPDATE orders SET payment_status = 'REFUNDED', updated_at = GETDATE() WHERE order_id = (SELECT order_id FROM refunds WHERE refund_id = 1); UPDATE return_requests SET status = 'REFUNDED', updated_at = GETDATE() WHERE return_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC154]: Xử lý đổi hàng sang size khác: trừ kho size mới và cộng kho size cũ
-- ----------------------------------------------------------------------------
SELECT ri.return_item_id, oi.variant_id AS old_variant, ri.exchange_variant_id AS new_variant, ri.quantity FROM return_request_items ri JOIN order_items oi ON ri.order_item_id = oi.order_item_id WHERE ri.return_id = 1; UPDATE inventories SET stock_quantity = stock_quantity - 1 WHERE variant_id = 8 AND warehouse_id = 1 AND stock_quantity >= 1; UPDATE inventories SET stock_quantity = stock_quantity + 1 WHERE variant_id = 5 AND warehouse_id = 1; UPDATE return_requests SET status = 'COMPLETED', updated_at = GETDATE() WHERE return_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC155]: Tự động hủy đơn hàng quá hạn chưa thanh toán online
-- ----------------------------------------------------------------------------
SELECT order_id, order_code, ordered_at FROM orders WHERE status = 'PENDING' AND payment_status = 'UNPAID' AND ordered_at < DATEADD(HOUR, -24, GETDATE()) AND order_id IN (SELECT order_id FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE m.is_online = 1); UPDATE orders SET status = 'CANCELLED', updated_at = GETDATE() WHERE status = 'PENDING' AND payment_status = 'UNPAID' AND ordered_at < DATEADD(HOUR, -24, GETDATE()) AND order_id IN (SELECT order_id FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE m.is_online = 1);
GO

-- ----------------------------------------------------------------------------
-- [UC156]: Tự động hoàn tất đơn hàng sau 7 ngày giao thành công
-- ----------------------------------------------------------------------------
SELECT order_id FROM orders WHERE status = 'DELIVERED' AND updated_at < DATEADD(DAY, -7, GETDATE()); UPDATE orders SET status = 'COMPLETED', updated_at = GETDATE() WHERE status = 'DELIVERED' AND updated_at < DATEADD(DAY, -7, GETDATE());
GO

-- ----------------------------------------------------------------------------
-- [UC157]: Thống kê số lượng và giá trị đơn hàng theo trạng thái
-- ----------------------------------------------------------------------------
SELECT status, COUNT(*) AS total_orders, SUM(total_amount) AS total_value FROM orders GROUP BY status ORDER BY total_orders DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC158]: Xuất danh sách đơn hàng ra file Excel để đối soát
-- ----------------------------------------------------------------------------
SELECT o.order_code, o.ordered_at, o.receiver_name, o.receiver_phone, o.channel, o.status, o.payment_status, o.subtotal, o.discount_amount, o.shipping_fee, o.total_amount FROM orders o WHERE o.ordered_at BETWEEN '2026-11-01' AND '2026-11-30' ORDER BY o.ordered_at;
GO


-- ############################################################################
-- M07 - THANH TOÁN
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC159]: Xem danh sách phương thức thanh toán (COD, VNPay, MoMo, chuyển khoản)
-- ----------------------------------------------------------------------------
SELECT method_id, method_code, method_name, is_online, display_order, is_active FROM payment_methods WHERE method_name LIKE N'%%' ORDER BY display_order;
GO

-- ----------------------------------------------------------------------------
-- [UC160]: Thêm mới phương thức thanh toán
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_code = 'ZALOPAY') INSERT INTO payment_methods (method_code, method_name, is_online, display_order) VALUES ('ZALOPAY', N'Ví ZaloPay', 1, 5);
GO

-- ----------------------------------------------------------------------------
-- [UC161]: Bật tắt phương thức thanh toán trên trang đặt hàng
-- ----------------------------------------------------------------------------
SELECT * FROM payment_methods WHERE method_id = 3; UPDATE payment_methods SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END WHERE method_id = 3;
GO

-- ----------------------------------------------------------------------------
-- [UC162]: Tạo bản ghi thanh toán khi khách chọn thanh toán COD
-- ----------------------------------------------------------------------------
SELECT method_id FROM payment_methods WHERE method_code = 'COD'; INSERT INTO payments (order_id, method_id, amount, status) SELECT 1, method_id, 730000, 'PENDING' FROM payment_methods WHERE method_code = 'COD';
GO

-- ----------------------------------------------------------------------------
-- [UC163]: Tạo yêu cầu thanh toán VNPay và lưu giao dịch chờ xử lý
-- ----------------------------------------------------------------------------
DECLARE @payment_id BIGINT; INSERT INTO payments (order_id, method_id, amount, status, transaction_ref) VALUES (2, 2, 530000, 'PENDING', 'DN261117-0002'); SET @payment_id = SCOPE_IDENTITY(); INSERT INTO payment_transactions (payment_id, gateway, amount, response_code) VALUES (@payment_id, 'VNPAY', 530000, NULL);
GO

-- ----------------------------------------------------------------------------
-- [UC164]: Xử lý kết quả thanh toán VNPay thành công (IPN callback)
-- ----------------------------------------------------------------------------
SELECT payment_id, order_id, status FROM payments WHERE transaction_ref = 'DN261117-0002' AND status = 'PENDING'; UPDATE payments SET status = 'SUCCESS', paid_at = GETDATE() WHERE transaction_ref = 'DN261117-0002'; UPDATE orders SET payment_status = 'PAID', status = 'CONFIRMED', updated_at = GETDATE() WHERE order_id = 2; INSERT INTO payment_transactions (payment_id, gateway, gateway_txn_no, response_code, amount, raw_response) VALUES (2, 'VNPAY', '14012345', '00', 530000, N'{"vnp_ResponseCode":"00"}');
GO

-- ----------------------------------------------------------------------------
-- [UC165]: Xử lý thanh toán thất bại hoặc hết hạn thanh toán online
-- ----------------------------------------------------------------------------
SELECT * FROM payments WHERE payment_id = 3 AND status = 'PENDING'; UPDATE payments SET status = 'FAILED' WHERE payment_id = 3; INSERT INTO payment_transactions (payment_id, gateway, response_code, amount) VALUES (3, 'VNPAY', '24', 530000);
GO

-- ----------------------------------------------------------------------------
-- [UC166]: Tạo yêu cầu thanh toán qua ví MoMo
-- ----------------------------------------------------------------------------
DECLARE @payment_id BIGINT; INSERT INTO payments (order_id, method_id, amount, status, transaction_ref) VALUES (4, 3, 890000, 'PENDING', 'DN261117-0004'); SET @payment_id = SCOPE_IDENTITY(); INSERT INTO payment_transactions (payment_id, gateway, amount) VALUES (@payment_id, 'MOMO', 890000);
GO

-- ----------------------------------------------------------------------------
-- [UC167]: Xem lịch sử các giao dịch thanh toán của một đơn hàng
-- ----------------------------------------------------------------------------
SELECT p.payment_id, m.method_name, p.amount, p.status, t.gateway, t.gateway_txn_no, t.response_code, t.created_at FROM payments p JOIN payment_methods m ON p.method_id = m.method_id LEFT JOIN payment_transactions t ON p.payment_id = t.payment_id WHERE p.order_id = 2 AND m.method_name LIKE N'%%' ORDER BY t.created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC168]: Tạo phiên đối soát COD với đơn vị vận chuyển theo kỳ
-- ----------------------------------------------------------------------------
SELECT carrier_id, SUM(cod_amount) AS total_cod, SUM(shipping_fee) AS total_fee FROM shipments WHERE carrier_id = 1 AND status = 'DELIVERED' AND delivered_at BETWEEN '2026-11-01' AND '2026-11-15' GROUP BY carrier_id; INSERT INTO cod_reconciliations (carrier_id, period_from, period_to, total_cod, total_fee, net_amount, created_by) SELECT carrier_id, '2026-11-01', '2026-11-15', SUM(cod_amount), SUM(shipping_fee), SUM(cod_amount) - SUM(shipping_fee), 4 FROM shipments WHERE carrier_id = 1 AND status = 'DELIVERED' AND delivered_at BETWEEN '2026-11-01' AND '2026-11-15' GROUP BY carrier_id;
GO

-- ----------------------------------------------------------------------------
-- [UC169]: Xác nhận hoàn tất đối soát COD và ghi nhận tiền về
-- ----------------------------------------------------------------------------
SELECT * FROM cod_reconciliations WHERE reconciliation_id = 1; UPDATE cod_reconciliations SET status = 'RECONCILED', reconciled_at = GETDATE() WHERE reconciliation_id = 1 AND status = 'PENDING';
GO

-- ----------------------------------------------------------------------------
-- [UC170]: Hoàn tiền qua cổng thanh toán cho đơn đã thanh toán online
-- ----------------------------------------------------------------------------
SELECT payment_id, amount, status FROM payments WHERE payment_id = 2 AND status = 'SUCCESS'; UPDATE payments SET status = 'REFUNDED' WHERE payment_id = 2; INSERT INTO payment_transactions (payment_id, gateway, gateway_txn_no, response_code, amount) VALUES (2, 'VNPAY', 'RF14012345', '00', 530000);
GO


-- ############################################################################
-- M08 - VẬN CHUYỂN & ĐỊA LÝ
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC171]: Xem danh sách tỉnh thành (34 đơn vị cấp tỉnh) kèm vùng giao hàng
-- ----------------------------------------------------------------------------
SELECT p.province_id, p.province_code, p.province_name, z.zone_name FROM provinces p LEFT JOIN shipping_zones z ON p.zone_id = z.zone_id WHERE p.province_name LIKE N'%%' ORDER BY p.province_name;
GO

-- ----------------------------------------------------------------------------
-- [UC172]: Thêm mới tỉnh thành phố vào danh mục địa lý
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM provinces WHERE province_code = '01') INSERT INTO provinces (province_code, province_name, zone_id) VALUES ('01', N'Hà Nội', 1);
GO

-- ----------------------------------------------------------------------------
-- [UC173]: Gán tỉnh thành vào vùng giao hàng để tính phí vận chuyển
-- ----------------------------------------------------------------------------
SELECT * FROM provinces WHERE province_id = 5; UPDATE provinces SET zone_id = 2 WHERE province_id = 5;
GO

-- ----------------------------------------------------------------------------
-- [UC174]: Xem danh sách xã, phường, đặc khu theo tỉnh thành
-- ----------------------------------------------------------------------------
SELECT w.ward_id, w.ward_code, w.ward_name, w.ward_type, p.province_name FROM wards w JOIN provinces p ON w.province_id = p.province_id WHERE w.province_id = 1 AND w.ward_name LIKE N'%%' ORDER BY w.ward_name;
GO

-- ----------------------------------------------------------------------------
-- [UC175]: Tìm kiếm xã phường theo tên gợi ý khi nhập địa chỉ
-- ----------------------------------------------------------------------------
SELECT TOP 10 w.ward_id, w.ward_name, p.province_name FROM wards w JOIN provinces p ON w.province_id = p.province_id WHERE w.ward_name LIKE N'%Láng%' ORDER BY w.ward_name;
GO

-- ----------------------------------------------------------------------------
-- [UC176]: Thêm mới xã phường đặc khu sau khi sắp xếp đơn vị hành chính
-- ----------------------------------------------------------------------------
SELECT * FROM wards WHERE province_id = 1; INSERT INTO wards (province_id, ward_code, ward_name, ward_type) VALUES (1, '00091', N'Phường Láng', 'WARD');
GO

-- ----------------------------------------------------------------------------
-- [UC177]: Xóa xã phường khi chưa được sử dụng trong địa chỉ và đơn hàng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM user_addresses WHERE ward_id = 500) AND NOT EXISTS (SELECT 1 FROM orders WHERE ward_id = 500) DELETE FROM wards WHERE ward_id = 500;
GO

-- ----------------------------------------------------------------------------
-- [UC178]: Xem danh sách vùng giao hàng kèm số tỉnh thành
-- ----------------------------------------------------------------------------
SELECT z.zone_id, z.zone_name, z.description, COUNT(p.province_id) AS total_provinces FROM shipping_zones z LEFT JOIN provinces p ON z.zone_id = p.zone_id WHERE z.zone_name LIKE N'%%' GROUP BY z.zone_id, z.zone_name, z.description;
GO

-- ----------------------------------------------------------------------------
-- [UC179]: Thêm mới vùng giao hàng (nội thành, liên tỉnh, vùng xa)
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM shipping_zones WHERE zone_name = N'Vùng xa') INSERT INTO shipping_zones (zone_name, description) VALUES (N'Vùng xa', N'Miền núi, hải đảo, phụ phí cao');
GO

-- ----------------------------------------------------------------------------
-- [UC180]: Xem danh sách đơn vị vận chuyển đang hợp tác
-- ----------------------------------------------------------------------------
SELECT carrier_id, carrier_code, carrier_name, phone, is_active FROM shipping_carriers WHERE carrier_name LIKE N'%%' ORDER BY carrier_name;
GO

-- ----------------------------------------------------------------------------
-- [UC181]: Thêm mới đơn vị vận chuyển kèm địa chỉ API tích hợp
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM shipping_carriers WHERE carrier_code = 'GHN') INSERT INTO shipping_carriers (carrier_code, carrier_name, api_endpoint, phone) VALUES ('GHN', N'Giao Hàng Nhanh', 'https://online-gateway.ghn.vn/shiip/public-api', '1900636677');
GO

-- ----------------------------------------------------------------------------
-- [UC182]: Xem danh sách dịch vụ vận chuyển theo từng đơn vị
-- ----------------------------------------------------------------------------
SELECT m.shipping_method_id, c.carrier_name, m.method_name, m.estimated_days_min, m.estimated_days_max, m.is_active FROM shipping_methods m JOIN shipping_carriers c ON m.carrier_id = c.carrier_id WHERE m.method_name LIKE N'%%' ORDER BY c.carrier_name;
GO

-- ----------------------------------------------------------------------------
-- [UC183]: Thêm mới dịch vụ vận chuyển (tiêu chuẩn, hỏa tốc)
-- ----------------------------------------------------------------------------
SELECT * FROM shipping_methods WHERE carrier_id = 1; INSERT INTO shipping_methods (carrier_id, method_name, estimated_days_min, estimated_days_max) VALUES (1, N'Giao hỏa tốc trong ngày', 0, 1);
GO

-- ----------------------------------------------------------------------------
-- [UC184]: Xem bảng phí vận chuyển theo vùng và khối lượng
-- ----------------------------------------------------------------------------
SELECT c.carrier_name, m.method_name, z.zone_name, f.min_weight_gram, f.max_weight_gram, f.base_fee, f.free_ship_threshold FROM shipping_fees f JOIN shipping_methods m ON f.shipping_method_id = m.shipping_method_id JOIN shipping_carriers c ON m.carrier_id = c.carrier_id JOIN shipping_zones z ON f.zone_id = z.zone_id ORDER BY c.carrier_name, z.zone_name, f.min_weight_gram;
GO

-- ----------------------------------------------------------------------------
-- [UC185]: Cập nhật phí vận chuyển và ngưỡng miễn phí giao hàng
-- ----------------------------------------------------------------------------
SELECT * FROM shipping_fees WHERE fee_id = 1; UPDATE shipping_fees SET base_fee = 30000, free_ship_threshold = 500000 WHERE fee_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC186]: Tính phí vận chuyển cho đơn hàng theo tỉnh nhận và khối lượng
-- ----------------------------------------------------------------------------
SELECT TOP 1 CASE WHEN 600000 >= f.free_ship_threshold THEN 0 ELSE f.base_fee END AS shipping_fee FROM shipping_fees f JOIN provinces p ON f.zone_id = p.zone_id WHERE p.province_id = 1 AND f.shipping_method_id = 1 AND 800 BETWEEN f.min_weight_gram AND ISNULL(f.max_weight_gram, 999999);
GO

-- ----------------------------------------------------------------------------
-- [UC187]: Tạo vận đơn gửi đơn vị vận chuyển cho đơn hàng
-- ----------------------------------------------------------------------------
SELECT order_id, receiver_name, shipping_address, total_amount FROM orders WHERE order_id = 1 AND status = 'PACKING'; INSERT INTO shipments (order_id, carrier_id, shipping_method_id, tracking_code, shipping_fee, cod_amount, weight_gram) VALUES (1, 1, 1, 'GHN8A2B3C4D', 30000, 730000, 800);
GO

-- ----------------------------------------------------------------------------
-- [UC188]: Cập nhật trạng thái vận đơn theo webhook của đơn vị vận chuyển
-- ----------------------------------------------------------------------------
SELECT shipment_id, status FROM shipments WHERE tracking_code = 'GHN8A2B3C4D'; UPDATE shipments SET status = 'IN_TRANSIT', shipped_at = GETDATE() WHERE tracking_code = 'GHN8A2B3C4D'; INSERT INTO shipment_tracking_events (shipment_id, event_status, event_location, event_note) SELECT shipment_id, 'IN_TRANSIT', N'Kho trung chuyển Hà Nội', N'Đơn hàng đang trên đường giao' FROM shipments WHERE tracking_code = 'GHN8A2B3C4D';
GO

-- ----------------------------------------------------------------------------
-- [UC189]: Tra cứu hành trình vận đơn theo mã vận đơn
-- ----------------------------------------------------------------------------
SELECT s.tracking_code, s.status, e.event_status, e.event_location, e.event_note, e.event_time FROM shipments s JOIN shipment_tracking_events e ON s.shipment_id = e.shipment_id WHERE s.tracking_code LIKE 'GHN8A2B3C4D%' ORDER BY e.event_time DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC190]: Xem danh sách vận đơn giao trễ hoặc giao thất bại cần xử lý
-- ----------------------------------------------------------------------------
SELECT s.shipment_id, s.tracking_code, o.order_code, c.carrier_name, s.status, s.shipped_at FROM shipments s JOIN orders o ON s.order_id = o.order_id JOIN shipping_carriers c ON s.carrier_id = c.carrier_id WHERE (s.status = 'FAILED' OR (s.status = 'IN_TRANSIT' AND s.shipped_at < DATEADD(DAY, -5, GETDATE()))) AND o.order_code LIKE '%%';
GO


-- ############################################################################
-- M09 - KHUYẾN MÃI & VOUCHER
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC191]: Xem danh sách voucher kèm trạng thái hiệu lực và lượt dùng
-- ----------------------------------------------------------------------------
SELECT voucher_id, voucher_code, voucher_name, discount_type, discount_value, min_order_value, used_count, usage_limit, start_date, end_date, is_active FROM vouchers WHERE voucher_code LIKE '%%' ORDER BY end_date DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC192]: Thêm mới voucher giảm theo phần trăm, số tiền hoặc miễn phí vận chuyển
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM vouchers WHERE voucher_code = 'DUNA10') INSERT INTO vouchers (voucher_code, voucher_name, discount_type, discount_value, max_discount_amount, min_order_value, usage_limit, per_user_limit, start_date, end_date) VALUES ('DUNA10', N'Giảm 10% đơn từ 500K', 'PERCENT', 10, 100000, 500000, 1000, 1, '2026-11-20', '2026-12-31');
GO

-- ----------------------------------------------------------------------------
-- [UC193]: Cập nhật điều kiện áp dụng và thời gian hiệu lực voucher
-- ----------------------------------------------------------------------------
SELECT * FROM vouchers WHERE voucher_id = 1; UPDATE vouchers SET min_order_value = 600000, end_date = '2027-01-15', usage_limit = 2000 WHERE voucher_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC194]: Bật tắt trạng thái voucher
-- ----------------------------------------------------------------------------
SELECT voucher_id, is_active FROM vouchers WHERE voucher_id = 2; UPDATE vouchers SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END WHERE voucher_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC195]: Xóa voucher chưa có lượt sử dụng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM voucher_usages WHERE voucher_id = 9) AND NOT EXISTS (SELECT 1 FROM orders WHERE voucher_id = 9) DELETE FROM vouchers WHERE voucher_id = 9;
GO

-- ----------------------------------------------------------------------------
-- [UC196]: Kiểm tra mã voucher hợp lệ khi khách nhập tại bước thanh toán
-- ----------------------------------------------------------------------------
SELECT v.voucher_id, v.discount_type, v.discount_value, v.max_discount_amount FROM vouchers v WHERE v.voucher_code = 'DUNA10' AND v.is_active = 1 AND GETDATE() BETWEEN v.start_date AND v.end_date AND 650000 >= v.min_order_value AND (v.usage_limit IS NULL OR v.used_count < v.usage_limit) AND (SELECT COUNT(*) FROM voucher_usages vu WHERE vu.voucher_id = v.voucher_id AND vu.user_id = 101) < v.per_user_limit;
GO

-- ----------------------------------------------------------------------------
-- [UC197]: Ghi nhận lượt sử dụng voucher khi đặt hàng thành công
-- ----------------------------------------------------------------------------
SELECT * FROM vouchers WHERE voucher_id = 1; INSERT INTO voucher_usages (voucher_id, user_id, order_id, discount_amount) VALUES (1, 101, 1, 65000); UPDATE vouchers SET used_count = used_count + 1 WHERE voucher_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC198]: Thống kê hiệu quả voucher theo số lượt dùng và tổng tiền giảm
-- ----------------------------------------------------------------------------
SELECT v.voucher_code, v.voucher_name, COUNT(vu.usage_id) AS total_uses, SUM(vu.discount_amount) AS total_discount, SUM(o.total_amount) AS revenue_with_voucher FROM vouchers v LEFT JOIN voucher_usages vu ON v.voucher_id = vu.voucher_id LEFT JOIN orders o ON vu.order_id = o.order_id GROUP BY v.voucher_code, v.voucher_name ORDER BY total_uses DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC199]: Xem danh sách chương trình khuyến mãi đang diễn ra
-- ----------------------------------------------------------------------------
SELECT promotion_id, promotion_name, promo_type, discount_value, start_date, end_date FROM promotions WHERE is_active = 1 AND GETDATE() BETWEEN start_date AND end_date AND promotion_name LIKE N'%%';
GO

-- ----------------------------------------------------------------------------
-- [UC200]: Thêm mới chương trình khuyến mãi theo sự kiện (Black Friday, 11.11)
-- ----------------------------------------------------------------------------
SELECT * FROM promotions; INSERT INTO promotions (promotion_name, promo_type, discount_value, start_date, end_date, banner_url) VALUES (N'Black Friday DUNA 2026', 'PERCENT', 30, '2026-11-27', '2026-11-30', '/assets/blackfriday.jpg');
GO

-- ----------------------------------------------------------------------------
-- [UC201]: Gán sản phẩm vào chương trình khuyến mãi
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id = 1 AND product_id = 7) INSERT INTO promotion_products (promotion_id, product_id) VALUES (1, 7);
GO

-- ----------------------------------------------------------------------------
-- [UC202]: Gỡ sản phẩm khỏi chương trình khuyến mãi
-- ----------------------------------------------------------------------------
SELECT * FROM promotion_products WHERE promotion_id = 1; DELETE FROM promotion_products WHERE promotion_id = 1 AND product_id = 7;
GO

-- ----------------------------------------------------------------------------
-- [UC203]: Tính giá bán sau khuyến mãi của sản phẩm
-- ----------------------------------------------------------------------------
SELECT p.product_id, p.product_name, p.base_price, CASE WHEN pr.promo_type = 'PERCENT' THEN p.base_price * (100 - pr.discount_value) / 100 WHEN pr.promo_type = 'FIXED' THEN p.base_price - pr.discount_value ELSE p.base_price END AS final_price FROM products p LEFT JOIN promotion_products pp ON p.product_id = pp.product_id LEFT JOIN promotions pr ON pp.promotion_id = pr.promotion_id AND pr.is_active = 1 AND GETDATE() BETWEEN pr.start_date AND pr.end_date WHERE p.product_id = 7;
GO

-- ----------------------------------------------------------------------------
-- [UC204]: Kết thúc sớm chương trình khuyến mãi
-- ----------------------------------------------------------------------------
SELECT * FROM promotions WHERE promotion_id = 2; UPDATE promotions SET end_date = GETDATE(), is_active = 0 WHERE promotion_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC205]: Xem danh sách các đợt flash sale và trạng thái
-- ----------------------------------------------------------------------------
SELECT flash_sale_id, title, start_time, end_time, status FROM flash_sales WHERE title LIKE N'%%' ORDER BY start_time DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC206]: Tạo mới đợt flash sale theo khung giờ vàng
-- ----------------------------------------------------------------------------
SELECT * FROM flash_sales; INSERT INTO flash_sales (title, start_time, end_time) VALUES (N'Flash Sale 20h - Áo sơ mi đồng giá', '2026-11-27 20:00', '2026-11-27 22:00');
GO

-- ----------------------------------------------------------------------------
-- [UC207]: Thêm biến thể sản phẩm vào flash sale kèm giá và giới hạn số lượng
-- ----------------------------------------------------------------------------
SELECT * FROM flash_sale_items WHERE flash_sale_id = 1; INSERT INTO flash_sale_items (flash_sale_id, variant_id, sale_price, quantity_limit) VALUES (1, 4, 199000, 50);
GO

-- ----------------------------------------------------------------------------
-- [UC208]: Tự động cập nhật trạng thái flash sale theo khung giờ
-- ----------------------------------------------------------------------------
SELECT flash_sale_id, status FROM flash_sales WHERE status IN ('SCHEDULED', 'RUNNING'); UPDATE flash_sales SET status = 'RUNNING' WHERE status = 'SCHEDULED' AND GETDATE() >= start_time AND GETDATE() < end_time; UPDATE flash_sales SET status = 'ENDED' WHERE status = 'RUNNING' AND GETDATE() >= end_time;
GO

-- ----------------------------------------------------------------------------
-- [UC209]: Ghi nhận bán hàng flash sale và kiểm tra giới hạn số lượng
-- ----------------------------------------------------------------------------
SELECT quantity_limit, sold_quantity FROM flash_sale_items WHERE flash_item_id = 1; UPDATE flash_sale_items SET sold_quantity = sold_quantity + 1 WHERE flash_item_id = 1 AND sold_quantity < quantity_limit;
GO

-- ----------------------------------------------------------------------------
-- [UC210]: Xem sản phẩm flash sale đang diễn ra kèm phần trăm giảm
-- ----------------------------------------------------------------------------
SELECT p.product_name, c.color_name, s.size_code, f.sale_price, pv.price, f.quantity_limit - f.sold_quantity AS remaining, CAST((pv.price - f.sale_price) * 100.0 / pv.price AS INT) AS discount_percent FROM flash_sales fs JOIN flash_sale_items f ON fs.flash_sale_id = f.flash_sale_id JOIN product_variants pv ON f.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id WHERE fs.status = 'RUNNING';
GO


-- ############################################################################
-- M10 - TÍCH ĐIỂM & ĐỔI QUÀ
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC211]: Tạo tài khoản tích điểm cho khách hàng mới
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM loyalty_accounts WHERE customer_id = 1) INSERT INTO loyalty_accounts (customer_id) VALUES (1);
GO

-- ----------------------------------------------------------------------------
-- [UC212]: Xem số dư điểm và hạng thành viên của khách hàng
-- ----------------------------------------------------------------------------
SELECT c.full_name, la.points_balance, la.lifetime_points, t.tier_name FROM loyalty_accounts la JOIN customers c ON la.customer_id = c.customer_id LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id WHERE c.customer_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC213]: Cộng điểm thưởng khi đơn hàng hoàn tất theo hệ số hạng thành viên
-- ----------------------------------------------------------------------------
SELECT o.total_amount, t.point_multiplier FROM orders o JOIN customers c ON o.customer_id = c.customer_id LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id WHERE o.order_id = 1; INSERT INTO loyalty_transactions (account_id, order_id, points, txn_type, description, expires_at) VALUES (1, 1, 73, 'EARN', N'Tích điểm đơn DN261117-0001', DATEADD(MONTH, 12, GETDATE())); UPDATE loyalty_accounts SET points_balance = points_balance + 73, lifetime_points = lifetime_points + 73, updated_at = GETDATE() WHERE account_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC214]: Trừ điểm khi khách dùng điểm để thanh toán đơn hàng
-- ----------------------------------------------------------------------------
SELECT points_balance FROM loyalty_accounts WHERE account_id = 1; UPDATE loyalty_accounts SET points_balance = points_balance - 50, updated_at = GETDATE() WHERE account_id = 1 AND points_balance >= 50; INSERT INTO loyalty_transactions (account_id, order_id, points, txn_type, description) VALUES (1, 5, -50, 'REDEEM', N'Dùng điểm thanh toán đơn hàng');
GO

-- ----------------------------------------------------------------------------
-- [UC215]: Điều chỉnh điểm thủ công kèm lý do
-- ----------------------------------------------------------------------------
SELECT * FROM loyalty_accounts WHERE account_id = 2; UPDATE loyalty_accounts SET points_balance = points_balance + 20 WHERE account_id = 2; INSERT INTO loyalty_transactions (account_id, points, txn_type, description) VALUES (2, 20, 'ADJUST', N'Tặng điểm bù do lỗi hệ thống');
GO

-- ----------------------------------------------------------------------------
-- [UC216]: Xem lịch sử giao dịch điểm thưởng của khách hàng
-- ----------------------------------------------------------------------------
SELECT lt.loyalty_txn_id, lt.txn_type, lt.points, lt.description, lt.expires_at, lt.created_at FROM loyalty_transactions lt JOIN loyalty_accounts la ON lt.account_id = la.account_id WHERE la.customer_id = 1 AND lt.txn_type LIKE '%%' ORDER BY lt.created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC217]: Tự động hết hạn điểm thưởng quá 12 tháng
-- ----------------------------------------------------------------------------
SELECT account_id, SUM(points) AS expiring_points FROM loyalty_transactions WHERE txn_type = 'EARN' AND expires_at < GETDATE() GROUP BY account_id; INSERT INTO loyalty_transactions (account_id, points, txn_type, description) SELECT account_id, -SUM(points), 'EXPIRE', N'Điểm hết hạn sau 12 tháng' FROM loyalty_transactions WHERE txn_type = 'EARN' AND expires_at < GETDATE() GROUP BY account_id;
GO

-- ----------------------------------------------------------------------------
-- [UC218]: Xem danh sách quà đổi điểm đang áp dụng
-- ----------------------------------------------------------------------------
SELECT r.reward_id, r.reward_name, r.points_required, r.stock_quantity, v.voucher_code FROM loyalty_rewards r LEFT JOIN vouchers v ON r.voucher_id = v.voucher_id WHERE r.is_active = 1 AND r.reward_name LIKE N'%%' ORDER BY r.points_required;
GO

-- ----------------------------------------------------------------------------
-- [UC219]: Thêm mới quà đổi điểm gắn với voucher
-- ----------------------------------------------------------------------------
SELECT * FROM loyalty_rewards; INSERT INTO loyalty_rewards (reward_name, points_required, voucher_id, stock_quantity) VALUES (N'Voucher giảm 50K', 200, 3, 500);
GO

-- ----------------------------------------------------------------------------
-- [UC220]: Khách hàng đổi quà bằng điểm thưởng
-- ----------------------------------------------------------------------------
SELECT la.points_balance, r.points_required FROM loyalty_accounts la, loyalty_rewards r WHERE la.account_id = 1 AND r.reward_id = 1; INSERT INTO reward_redemptions (account_id, reward_id, points_used) VALUES (1, 1, 200); UPDATE loyalty_accounts SET points_balance = points_balance - 200 WHERE account_id = 1 AND points_balance >= 200; UPDATE loyalty_rewards SET stock_quantity = stock_quantity - 1 WHERE reward_id = 1 AND stock_quantity > 0;
GO


-- ############################################################################
-- M11 - ĐÁNH GIÁ & HỖ TRỢ
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC221]: Xem đánh giá đã duyệt của sản phẩm kèm ảnh khách hàng
-- ----------------------------------------------------------------------------
SELECT r.review_id, u.full_name, r.rating, r.title, r.content, r.fit_feedback, r.created_at, rm.media_url FROM product_reviews r JOIN users u ON r.user_id = u.user_id LEFT JOIN review_media rm ON r.review_id = rm.review_id WHERE r.product_id = 1 AND r.status = 'APPROVED' ORDER BY r.created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC222]: Gửi đánh giá sản phẩm sau khi mua hàng thành công
-- ----------------------------------------------------------------------------
SELECT oi.order_item_id FROM order_items oi JOIN orders o ON oi.order_id = o.order_id WHERE o.user_id = 101 AND o.status = 'COMPLETED' AND oi.order_item_id = 1; INSERT INTO product_reviews (product_id, user_id, order_item_id, rating, title, content, fit_feedback) VALUES (1, 101, 1, 5, N'Áo đẹp, vừa vặn', N'Vải mát, form chuẩn, giao nhanh', 'TRUE_TO_SIZE');
GO

-- ----------------------------------------------------------------------------
-- [UC223]: Tải ảnh thực tế của khách lên đánh giá
-- ----------------------------------------------------------------------------
SELECT * FROM review_media WHERE review_id = 1; INSERT INTO review_media (review_id, media_url, media_type) VALUES (1, '/uploads/reviews/r1_photo1.jpg', 'IMAGE');
GO

-- ----------------------------------------------------------------------------
-- [UC224]: Xem danh sách đánh giá chờ kiểm duyệt
-- ----------------------------------------------------------------------------
SELECT r.review_id, p.product_name, u.full_name, r.rating, r.content, r.created_at FROM product_reviews r JOIN products p ON r.product_id = p.product_id JOIN users u ON r.user_id = u.user_id WHERE r.status = 'PENDING' AND p.product_name LIKE N'%%' ORDER BY r.created_at ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC225]: Duyệt đánh giá để hiển thị công khai
-- ----------------------------------------------------------------------------
SELECT * FROM product_reviews WHERE review_id = 1; UPDATE product_reviews SET status = 'APPROVED' WHERE review_id = 1 AND status = 'PENDING';
GO

-- ----------------------------------------------------------------------------
-- [UC226]: Từ chối đánh giá vi phạm nội quy
-- ----------------------------------------------------------------------------
SELECT * FROM product_reviews WHERE review_id = 2; UPDATE product_reviews SET status = 'REJECTED' WHERE review_id = 2 AND status = 'PENDING';
GO

-- ----------------------------------------------------------------------------
-- [UC227]: Shop phản hồi đánh giá của khách hàng
-- ----------------------------------------------------------------------------
SELECT * FROM review_replies WHERE review_id = 1; INSERT INTO review_replies (review_id, user_id, content) VALUES (1, 3, N'DUNA cảm ơn bạn đã tin tưởng và ủng hộ!');
GO

-- ----------------------------------------------------------------------------
-- [UC228]: Thống kê điểm đánh giá trung bình và phản hồi về size của sản phẩm
-- ----------------------------------------------------------------------------
SELECT product_id, COUNT(*) AS total_reviews, AVG(CAST(rating AS DECIMAL(3,2))) AS avg_rating, SUM(CASE WHEN fit_feedback = 'TOO_SMALL' THEN 1 ELSE 0 END) AS too_small, SUM(CASE WHEN fit_feedback = 'TRUE_TO_SIZE' THEN 1 ELSE 0 END) AS true_to_size, SUM(CASE WHEN fit_feedback = 'TOO_LARGE' THEN 1 ELSE 0 END) AS too_large FROM product_reviews WHERE status = 'APPROVED' GROUP BY product_id;
GO

-- ----------------------------------------------------------------------------
-- [UC229]: Khách hàng tạo yêu cầu hỗ trợ gắn với đơn hàng
-- ----------------------------------------------------------------------------
DECLARE @ticket_id INT; INSERT INTO support_tickets (ticket_code, customer_id, order_id, ticket_category_id, subject, priority) VALUES ('TK20261117-01', 1, 1, 2, N'Giao hàng chậm hơn dự kiến', 'NORMAL'); SET @ticket_id = SCOPE_IDENTITY(); INSERT INTO support_ticket_messages (ticket_id, sender_user_id, message_content, is_staff) VALUES (@ticket_id, 101, N'Đơn của tôi đã 5 ngày chưa nhận được hàng', 0);
GO

-- ----------------------------------------------------------------------------
-- [UC230]: Xem danh sách yêu cầu hỗ trợ theo trạng thái và mức ưu tiên
-- ----------------------------------------------------------------------------
SELECT t.ticket_id, t.ticket_code, c.full_name, tc.category_name, t.subject, t.priority, t.status, u.full_name AS assigned_name FROM support_tickets t LEFT JOIN customers c ON t.customer_id = c.customer_id LEFT JOIN ticket_categories tc ON t.ticket_category_id = tc.ticket_category_id LEFT JOIN users u ON t.assigned_to = u.user_id WHERE t.status IN ('OPEN', 'IN_PROGRESS') AND t.ticket_code LIKE '%%' ORDER BY t.priority DESC, t.created_at ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC231]: Phân công nhân viên chăm sóc xử lý yêu cầu hỗ trợ
-- ----------------------------------------------------------------------------
SELECT * FROM support_tickets WHERE ticket_id = 1; UPDATE support_tickets SET assigned_to = 3, status = 'IN_PROGRESS', updated_at = GETDATE() WHERE ticket_id = 1 AND status = 'OPEN';
GO

-- ----------------------------------------------------------------------------
-- [UC232]: Gửi tin nhắn phản hồi trong yêu cầu hỗ trợ
-- ----------------------------------------------------------------------------
SELECT * FROM support_ticket_messages WHERE ticket_id = 1; INSERT INTO support_ticket_messages (ticket_id, sender_user_id, message_content, is_staff) VALUES (1, 3, N'DUNA đã liên hệ đơn vị vận chuyển, đơn sẽ giao trong 24h', 1);
GO

-- ----------------------------------------------------------------------------
-- [UC233]: Xem toàn bộ hội thoại của một yêu cầu hỗ trợ
-- ----------------------------------------------------------------------------
SELECT m.message_id, u.full_name, m.message_content, m.is_staff, m.attachment_url, m.created_at FROM support_ticket_messages m LEFT JOIN users u ON m.sender_user_id = u.user_id WHERE m.ticket_id = 1 AND m.message_content LIKE N'%%' ORDER BY m.created_at ASC;
GO

-- ----------------------------------------------------------------------------
-- [UC234]: Đóng yêu cầu hỗ trợ sau khi giải quyết xong
-- ----------------------------------------------------------------------------
SELECT * FROM support_tickets WHERE ticket_id = 1; UPDATE support_tickets SET status = 'CLOSED', updated_at = GETDATE() WHERE ticket_id = 1 AND status IN ('IN_PROGRESS', 'RESOLVED');
GO

-- ----------------------------------------------------------------------------
-- [UC235]: Xem danh sách câu hỏi thường gặp theo nhóm
-- ----------------------------------------------------------------------------
SELECT faq_id, faq_group, question, answer FROM faqs WHERE is_active = 1 AND question LIKE N'%%' ORDER BY faq_group, display_order;
GO

-- ----------------------------------------------------------------------------
-- [UC236]: Thêm mới câu hỏi thường gặp (chính sách đổi trả, hướng dẫn chọn size)
-- ----------------------------------------------------------------------------
SELECT * FROM faqs; INSERT INTO faqs (faq_group, question, answer, display_order) VALUES (N'Đổi trả', N'Tôi có thể đổi size trong bao lâu?', N'Bạn có thể đổi size trong vòng 7 ngày kể từ khi nhận hàng, sản phẩm còn nguyên tem mác.', 1);
GO


-- ############################################################################
-- M12 - NỘI DUNG & MARKETING WEB
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC237]: Xem danh sách banner theo vị trí hiển thị trên website
-- ----------------------------------------------------------------------------
SELECT banner_id, title, banner_position, display_order, start_date, end_date, is_active FROM banners WHERE title LIKE N'%%' ORDER BY banner_position, display_order;
GO

-- ----------------------------------------------------------------------------
-- [UC238]: Thêm mới banner quảng cáo kèm ảnh và liên kết
-- ----------------------------------------------------------------------------
SELECT * FROM banners; INSERT INTO banners (title, image_url, link_url, banner_position, display_order, start_date, end_date) VALUES (N'Black Friday giảm đến 30%', '/uploads/banner/bf2026.jpg', '/collections/black-friday', 'HOME_HERO', 1, '2026-11-27', '2026-11-30');
GO

-- ----------------------------------------------------------------------------
-- [UC239]: Cập nhật thời gian hiển thị và thứ tự banner
-- ----------------------------------------------------------------------------
SELECT * FROM banners WHERE banner_id = 1; UPDATE banners SET display_order = 2, end_date = '2026-12-05' WHERE banner_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC240]: Tự động ẩn banner hết hạn hiển thị
-- ----------------------------------------------------------------------------
SELECT banner_id, title, end_date FROM banners WHERE is_active = 1 AND end_date < GETDATE(); UPDATE banners SET is_active = 0 WHERE is_active = 1 AND end_date < GETDATE();
GO

-- ----------------------------------------------------------------------------
-- [UC241]: Xem danh sách bài viết phối đồ theo chuyên mục
-- ----------------------------------------------------------------------------
SELECT p.post_id, p.title, c.category_name, p.status, p.published_at, p.view_count FROM blog_posts p LEFT JOIN blog_categories c ON p.blog_category_id = c.blog_category_id WHERE p.status = 'PUBLISHED' AND p.title LIKE N'%%' ORDER BY p.published_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC242]: Soạn bài viết mới ở trạng thái nháp
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM blog_posts WHERE slug = 'cach-phoi-do-di-lam-mua-dong') INSERT INTO blog_posts (blog_category_id, title, slug, summary, content, thumbnail_url, author_id) VALUES (1, N'Cách phối đồ đi làm mùa đông cho nam', 'cach-phoi-do-di-lam-mua-dong', N'Gợi ý phối blazer và sơ mi', N'Nội dung bài viết...', '/uploads/blog/phoi-do.jpg', 3);
GO

-- ----------------------------------------------------------------------------
-- [UC243]: Xuất bản bài viết lên website
-- ----------------------------------------------------------------------------
SELECT post_id, status FROM blog_posts WHERE post_id = 1; UPDATE blog_posts SET status = 'PUBLISHED', published_at = GETDATE(), updated_at = GETDATE() WHERE post_id = 1 AND status = 'DRAFT';
GO

-- ----------------------------------------------------------------------------
-- [UC244]: Xem bài viết theo đường dẫn và tăng lượt xem
-- ----------------------------------------------------------------------------
SELECT post_id, title, content, view_count FROM blog_posts WHERE slug = 'cach-phoi-do-di-lam-mua-dong' AND status = 'PUBLISHED'; UPDATE blog_posts SET view_count = view_count + 1 WHERE slug = 'cach-phoi-do-di-lam-mua-dong';
GO

-- ----------------------------------------------------------------------------
-- [UC245]: Thêm mới chuyên mục bài viết
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM blog_categories WHERE slug = 'meo-chon-size') INSERT INTO blog_categories (category_name, slug) VALUES (N'Mẹo chọn size', 'meo-chon-size');
GO

-- ----------------------------------------------------------------------------
-- [UC246]: Đăng ký nhận bản tin khuyến mãi qua email
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM newsletter_subscribers WHERE email = 'khach01@gmail.com') INSERT INTO newsletter_subscribers (email) VALUES ('khach01@gmail.com'); UPDATE newsletter_subscribers SET is_active = 1 WHERE email = 'khach01@gmail.com';
GO


-- ############################################################################
-- M13 - THÔNG BÁO & EMAIL
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC247]: Xem danh sách mẫu thông báo theo kênh gửi
-- ----------------------------------------------------------------------------
SELECT template_id, template_code, channel, subject, is_active FROM notification_templates WHERE channel = 'EMAIL' AND template_code LIKE '%%' ORDER BY template_code;
GO

-- ----------------------------------------------------------------------------
-- [UC248]: Thêm mới mẫu email xác nhận đơn hàng
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM notification_templates WHERE template_code = 'ORDER_CONFIRMED') INSERT INTO notification_templates (template_code, channel, subject, body) VALUES ('ORDER_CONFIRMED', 'EMAIL', N'DUNA xác nhận đơn hàng {{order_code}}', N'Xin chào {{customer_name}}, đơn hàng {{order_code}} của bạn đã được xác nhận.');
GO

-- ----------------------------------------------------------------------------
-- [UC249]: Cập nhật nội dung mẫu thông báo
-- ----------------------------------------------------------------------------
SELECT * FROM notification_templates WHERE template_id = 1; UPDATE notification_templates SET subject = N'DUNA - Đơn hàng {{order_code}} đã được giao', body = N'Đơn hàng {{order_code}} đã giao thành công. Cảm ơn bạn!' WHERE template_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC250]: Tạo thông báo trong ứng dụng khi đơn hàng đổi trạng thái
-- ----------------------------------------------------------------------------
SELECT template_id FROM notification_templates WHERE template_code = 'ORDER_CONFIRMED'; INSERT INTO notifications (user_id, template_id, title, content) SELECT o.user_id, 1, N'Đơn hàng đã được xác nhận', N'Đơn ' + o.order_code + N' đang được chuẩn bị' FROM orders o WHERE o.order_id = 1 AND o.user_id IS NOT NULL;
GO

-- ----------------------------------------------------------------------------
-- [UC251]: Xem danh sách thông báo chưa đọc của người dùng
-- ----------------------------------------------------------------------------
SELECT notification_id, title, content, created_at FROM notifications WHERE user_id = 101 AND is_read = 0 AND title LIKE N'%%' ORDER BY created_at DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC252]: Đánh dấu thông báo đã đọc
-- ----------------------------------------------------------------------------
SELECT * FROM notifications WHERE user_id = 101 AND is_read = 0; UPDATE notifications SET is_read = 1 WHERE user_id = 101 AND notification_id = 5;
GO

-- ----------------------------------------------------------------------------
-- [UC253]: Tự động xóa thông báo đã đọc cũ hơn 90 ngày
-- ----------------------------------------------------------------------------
SELECT COUNT(*) AS old_notifications FROM notifications WHERE is_read = 1 AND created_at < DATEADD(DAY, -90, GETDATE()); DELETE FROM notifications WHERE is_read = 1 AND created_at < DATEADD(DAY, -90, GETDATE());
GO

-- ----------------------------------------------------------------------------
-- [UC254]: Tạo chiến dịch email marketing gửi theo nhóm khách hàng
-- ----------------------------------------------------------------------------
SELECT COUNT(*) AS total_recipients FROM customers WHERE group_id = 2 AND email IS NOT NULL; INSERT INTO email_campaigns (campaign_name, template_id, target_group_id, total_recipients, created_by) VALUES (N'Ưu đãi Black Friday cho khách VIP', 2, 2, (SELECT COUNT(*) FROM customers WHERE group_id = 2 AND email IS NOT NULL), 4);
GO

-- ----------------------------------------------------------------------------
-- [UC255]: Lên lịch gửi chiến dịch email và cập nhật trạng thái sau khi gửi
-- ----------------------------------------------------------------------------
SELECT * FROM email_campaigns WHERE campaign_id = 1; UPDATE email_campaigns SET status = 'SCHEDULED', scheduled_at = '2026-11-27 08:00' WHERE campaign_id = 1 AND status = 'DRAFT'; UPDATE email_campaigns SET status = 'SENT', sent_at = GETDATE() WHERE campaign_id = 1 AND status = 'SCHEDULED' AND scheduled_at <= GETDATE();
GO

-- ----------------------------------------------------------------------------
-- [UC256]: Xem nhật ký gửi email và SMS thất bại để gửi lại
-- ----------------------------------------------------------------------------
SELECT message_log_id, channel, recipient, subject, error_message, created_at FROM message_logs WHERE status = 'FAILED' AND created_at >= DATEADD(DAY, -7, GETDATE()) AND recipient LIKE '%%' ORDER BY created_at DESC;
GO


-- ############################################################################
-- M14 - CỬA HÀNG & POS
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC257]: Xem danh sách cửa hàng và kho liên kết
-- ----------------------------------------------------------------------------
SELECT s.store_id, s.store_code, s.store_name, s.address, p.province_name, w.warehouse_name, s.open_time, s.close_time, s.is_active FROM stores s LEFT JOIN provinces p ON s.province_id = p.province_id LEFT JOIN warehouses w ON s.warehouse_id = w.warehouse_id WHERE s.store_name LIKE N'%%';
GO

-- ----------------------------------------------------------------------------
-- [UC258]: Thêm mới cửa hàng bán lẻ
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM stores WHERE store_code = 'CH-HN01') INSERT INTO stores (store_code, store_name, address, province_id, phone, open_time, close_time, warehouse_id) VALUES ('CH-HN01', N'DUNA Cầu Giấy', N'Số 100 Cầu Giấy, Hà Nội', 1, '0243111222', '08:30', '21:30', 2);
GO

-- ----------------------------------------------------------------------------
-- [UC259]: Cập nhật giờ mở cửa và kho xuất hàng của cửa hàng
-- ----------------------------------------------------------------------------
SELECT * FROM stores WHERE store_id = 1; UPDATE stores SET open_time = '09:00', close_time = '22:00', warehouse_id = 2 WHERE store_id = 1;
GO

-- ----------------------------------------------------------------------------
-- [UC260]: Mở ca bán hàng tại quầy và nhập tiền đầu ca
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM pos_sessions WHERE cashier_id = 6 AND status = 'OPEN') INSERT INTO pos_sessions (store_id, cashier_id, opening_cash) VALUES (1, 6, 2000000);
GO

-- ----------------------------------------------------------------------------
-- [UC261]: Tra cứu sản phẩm tại quầy bằng quét mã vạch
-- ----------------------------------------------------------------------------
SELECT pv.variant_id, p.product_name, c.color_name, s.size_code, ISNULL(pv.price, p.base_price) AS price, inv.stock_quantity FROM product_variants pv JOIN products p ON pv.product_id = p.product_id JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.barcode LIKE '893600100102%' AND inv.warehouse_id = 2;
GO

-- ----------------------------------------------------------------------------
-- [UC262]: Bán hàng tại quầy: tạo đơn POS, trừ tồn kho và ghi nhận tiền mặt
-- ----------------------------------------------------------------------------
DECLARE @order_id BIGINT; INSERT INTO orders (order_code, customer_id, store_id, channel, status, payment_status, handled_by, subtotal, total_amount) VALUES ('POS261117-0001', 2, 1, 'POS', 'COMPLETED', 'PAID', 6, 350000, 350000); SET @order_id = SCOPE_IDENTITY(); INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total) VALUES (@order_id, 1, N'Áo sơ mi Oxford trắng', 'SP001-WHT-M', 1, 350000, 350000); UPDATE inventories SET stock_quantity = stock_quantity - 1, updated_at = GETDATE() WHERE variant_id = 1 AND warehouse_id = 2; INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id) VALUES (1, 2, 'SALE', -1, 'ORDER', @order_id); INSERT INTO cash_drawer_transactions (pos_session_id, txn_type, amount, order_id) VALUES (1, 'SALE', 350000, @order_id);
GO

-- ----------------------------------------------------------------------------
-- [UC263]: Ghi nhận thu chi phát sinh trong két tiền mặt
-- ----------------------------------------------------------------------------
SELECT * FROM cash_drawer_transactions WHERE pos_session_id = 1; INSERT INTO cash_drawer_transactions (pos_session_id, txn_type, amount, note) VALUES (1, 'CASH_OUT', 150000, N'Mua văn phòng phẩm cửa hàng');
GO

-- ----------------------------------------------------------------------------
-- [UC264]: Đóng ca bán hàng và đối chiếu tiền mặt thực tế
-- ----------------------------------------------------------------------------
SELECT ps.opening_cash + ISNULL(SUM(CASE WHEN d.txn_type IN ('SALE', 'CASH_IN') THEN d.amount WHEN d.txn_type IN ('CASH_OUT', 'REFUND') THEN -d.amount ELSE 0 END), 0) AS expected_cash FROM pos_sessions ps LEFT JOIN cash_drawer_transactions d ON ps.pos_session_id = d.pos_session_id WHERE ps.pos_session_id = 1 GROUP BY ps.opening_cash; UPDATE pos_sessions SET closing_cash = 5800000, expected_cash = 5850000, status = 'CLOSED', closed_at = GETDATE() WHERE pos_session_id = 1 AND status = 'OPEN';
GO

-- ----------------------------------------------------------------------------
-- [UC265]: Báo cáo doanh thu theo ca bán hàng
-- ----------------------------------------------------------------------------
SELECT ps.pos_session_id, u.full_name AS cashier, ps.opened_at, ps.closed_at, SUM(CASE WHEN d.txn_type = 'SALE' THEN d.amount ELSE 0 END) AS total_sales, SUM(CASE WHEN d.txn_type = 'REFUND' THEN d.amount ELSE 0 END) AS total_refund FROM pos_sessions ps JOIN users u ON ps.cashier_id = u.user_id LEFT JOIN cash_drawer_transactions d ON ps.pos_session_id = d.pos_session_id WHERE ps.store_id = 1 GROUP BY ps.pos_session_id, u.full_name, ps.opened_at, ps.closed_at;
GO

-- ----------------------------------------------------------------------------
-- [UC266]: Xem lịch sử ca bán hàng theo thu ngân
-- ----------------------------------------------------------------------------
SELECT pos_session_id, store_id, opening_cash, closing_cash, expected_cash, (closing_cash - expected_cash) AS difference, status, opened_at, closed_at FROM pos_sessions WHERE cashier_id = 6 AND status LIKE '%%' ORDER BY opened_at DESC;
GO


-- ############################################################################
-- M15 - NHÂN SỰ
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC267]: Xem danh sách nhân viên theo cửa hàng và phòng ban
-- ----------------------------------------------------------------------------
SELECT e.employee_id, e.employee_code, e.full_name, d.department_name, po.position_name, s.store_name, e.status FROM employees e LEFT JOIN departments d ON e.department_id = d.department_id LEFT JOIN positions po ON e.position_id = po.position_id LEFT JOIN stores s ON e.store_id = s.store_id WHERE e.full_name LIKE N'%%' ORDER BY e.employee_code;
GO

-- ----------------------------------------------------------------------------
-- [UC268]: Thêm mới nhân viên và liên kết tài khoản đăng nhập
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'NV025') INSERT INTO employees (employee_code, user_id, full_name, phone, email, birth_date, hire_date, department_id, position_id, store_id) VALUES ('NV025', 12, N'Phạm Thu Hà', '0966555444', 'ha.pham@duna.vn', '1999-04-12', '2026-11-15', 2, 3, 1);
GO

-- ----------------------------------------------------------------------------
-- [UC269]: Cập nhật chức vụ và chuyển công tác nhân viên sang cửa hàng khác
-- ----------------------------------------------------------------------------
SELECT * FROM employees WHERE employee_id = 5; UPDATE employees SET position_id = 4, store_id = 2, updated_at = GETDATE() WHERE employee_id = 5;
GO

-- ----------------------------------------------------------------------------
-- [UC270]: Cho nhân viên nghỉ việc và khóa tài khoản đăng nhập
-- ----------------------------------------------------------------------------
SELECT employee_id, status, user_id FROM employees WHERE employee_id = 7; UPDATE employees SET status = 'RESIGNED', updated_at = GETDATE() WHERE employee_id = 7; UPDATE users SET status = 'LOCKED' WHERE user_id = (SELECT user_id FROM employees WHERE employee_id = 7);
GO

-- ----------------------------------------------------------------------------
-- [UC271]: Xem danh sách phòng ban và chức vụ kèm lương cơ bản
-- ----------------------------------------------------------------------------
SELECT d.department_name, po.position_name, po.base_salary FROM departments d CROSS JOIN positions po WHERE d.department_name LIKE N'%%' ORDER BY d.department_name, po.base_salary DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC272]: Thêm mới chức vụ kèm mức lương cơ bản
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM positions WHERE position_name = N'Nhân viên tư vấn bán hàng') INSERT INTO positions (position_name, base_salary) VALUES (N'Nhân viên tư vấn bán hàng', 8000000);
GO

-- ----------------------------------------------------------------------------
-- [UC273]: Thêm mới ca làm việc (ca sáng, ca chiều, ca tối)
-- ----------------------------------------------------------------------------
SELECT * FROM work_shifts; INSERT INTO work_shifts (shift_name, start_time, end_time) VALUES (N'Ca chiều', '13:00', '18:00');
GO

-- ----------------------------------------------------------------------------
-- [UC274]: Phân ca làm việc cho nhân viên theo ngày
-- ----------------------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM employee_shifts WHERE employee_id = 5 AND shift_id = 1 AND work_date = '2026-11-20') INSERT INTO employee_shifts (employee_id, shift_id, store_id, work_date) VALUES (5, 1, 1, '2026-11-20');
GO

-- ----------------------------------------------------------------------------
-- [UC275]: Xem lịch phân ca của cửa hàng theo tuần
-- ----------------------------------------------------------------------------
SELECT es.work_date, e.full_name, ws.shift_name, ws.start_time, ws.end_time FROM employee_shifts es JOIN employees e ON es.employee_id = e.employee_id JOIN work_shifts ws ON es.shift_id = ws.shift_id WHERE es.store_id = 1 AND es.work_date BETWEEN '2026-11-16' AND '2026-11-22' ORDER BY es.work_date, ws.start_time;
GO

-- ----------------------------------------------------------------------------
-- [UC276]: Chấm công vào ca và đánh dấu đi trễ
-- ----------------------------------------------------------------------------
SELECT * FROM attendance_records WHERE employee_id = 5 AND work_date = '2026-11-17'; INSERT INTO attendance_records (employee_id, work_date, check_in, status) SELECT 5, '2026-11-17', GETDATE(), CASE WHEN CAST(GETDATE() AS TIME) > '08:45' THEN 'LATE' ELSE 'PRESENT' END;
GO

-- ----------------------------------------------------------------------------
-- [UC277]: Chấm công ra ca và ghi nhận giờ kết thúc
-- ----------------------------------------------------------------------------
SELECT * FROM attendance_records WHERE employee_id = 5 AND work_date = '2026-11-17'; UPDATE attendance_records SET check_out = GETDATE() WHERE employee_id = 5 AND work_date = '2026-11-17' AND check_out IS NULL;
GO

-- ----------------------------------------------------------------------------
-- [UC278]: Báo cáo chấm công tháng: số ngày đi làm, đi trễ và vắng mặt
-- ----------------------------------------------------------------------------
SELECT e.employee_code, e.full_name, SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS present_days, SUM(CASE WHEN a.status = 'LATE' THEN 1 ELSE 0 END) AS late_days, SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) AS absent_days FROM employees e LEFT JOIN attendance_records a ON e.employee_id = a.employee_id AND a.work_date BETWEEN '2026-11-01' AND '2026-11-30' GROUP BY e.employee_code, e.full_name;
GO


-- ############################################################################
-- M16 - TÀI CHÍNH & BÁO CÁO
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC279]: Xem danh sách chi phí vận hành theo khoảng ngày
-- ----------------------------------------------------------------------------
SELECT e.expense_id, c.category_name, s.store_name, e.amount, e.expense_date, e.description FROM expenses e JOIN expense_categories c ON e.expense_category_id = c.expense_category_id LEFT JOIN stores s ON e.store_id = s.store_id WHERE e.expense_date BETWEEN '2026-11-01' AND '2026-11-30' AND c.category_name LIKE N'%%' ORDER BY e.expense_date DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC280]: Thêm mới loại chi phí (thuê mặt bằng, marketing, lương)
-- ----------------------------------------------------------------------------
SELECT * FROM expense_categories; INSERT INTO expense_categories (category_name) VALUES (N'Chi phí marketing');
GO

-- ----------------------------------------------------------------------------
-- [UC281]: Ghi nhận khoản chi phí phát sinh
-- ----------------------------------------------------------------------------
SELECT * FROM expenses; INSERT INTO expenses (expense_category_id, store_id, amount, expense_date, description, created_by) VALUES (1, 1, 15000000, '2026-11-05', N'Tiền thuê mặt bằng tháng 11', 4);
GO

-- ----------------------------------------------------------------------------
-- [UC282]: Cập nhật số tiền và nội dung khoản chi phí
-- ----------------------------------------------------------------------------
SELECT * FROM expenses WHERE expense_id = 3; UPDATE expenses SET amount = 16500000, description = N'Tiền thuê mặt bằng tháng 11 (đã gồm phí quản lý)' WHERE expense_id = 3;
GO

-- ----------------------------------------------------------------------------
-- [UC283]: Xóa khoản chi phí nhập sai
-- ----------------------------------------------------------------------------
SELECT * FROM expenses WHERE expense_id = 9; DELETE FROM expenses WHERE expense_id = 9;
GO

-- ----------------------------------------------------------------------------
-- [UC284]: Tự động tổng hợp doanh thu ngày hôm trước vào bảng báo cáo
-- ----------------------------------------------------------------------------
SELECT CAST(o.ordered_at AS DATE) AS summary_date, o.store_id, o.channel, COUNT(*) AS total_orders, SUM(o.total_amount) AS total_revenue FROM orders o WHERE o.status IN ('DELIVERED', 'COMPLETED') AND CAST(o.ordered_at AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) GROUP BY CAST(o.ordered_at AS DATE), o.store_id, o.channel; INSERT INTO daily_sales_summaries (summary_date, store_id, channel, total_orders, total_revenue, total_discount) SELECT CAST(o.ordered_at AS DATE), o.store_id, o.channel, COUNT(*), SUM(o.total_amount), SUM(o.discount_amount) FROM orders o WHERE o.status IN ('DELIVERED', 'COMPLETED') AND CAST(o.ordered_at AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) GROUP BY CAST(o.ordered_at AS DATE), o.store_id, o.channel;
GO

-- ----------------------------------------------------------------------------
-- [UC285]: Xem bảng tổng hợp doanh thu theo ngày và kênh bán
-- ----------------------------------------------------------------------------
SELECT summary_date, channel, total_orders, total_revenue, total_cost, total_discount, (total_revenue - total_cost) AS gross_profit FROM daily_sales_summaries WHERE summary_date BETWEEN '2026-11-01' AND '2026-11-30' ORDER BY summary_date DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC286]: Báo cáo doanh thu theo tháng trong năm
-- ----------------------------------------------------------------------------
SELECT MONTH(ordered_at) AS month_no, COUNT(*) AS total_orders, SUM(total_amount) AS revenue FROM orders WHERE status IN ('DELIVERED', 'COMPLETED') AND YEAR(ordered_at) = 2026 GROUP BY MONTH(ordered_at) ORDER BY month_no;
GO

-- ----------------------------------------------------------------------------
-- [UC287]: Báo cáo doanh thu theo danh mục sản phẩm
-- ----------------------------------------------------------------------------
SELECT c.category_name, SUM(oi.quantity) AS items_sold, SUM(oi.line_total) AS revenue FROM order_items oi JOIN orders o ON oi.order_id = o.order_id JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id JOIN categories c ON p.category_id = c.category_id WHERE o.status IN ('DELIVERED', 'COMPLETED') GROUP BY c.category_name ORDER BY revenue DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC288]: Báo cáo doanh thu theo kênh bán và theo cửa hàng
-- ----------------------------------------------------------------------------
SELECT o.channel, s.store_name, COUNT(*) AS total_orders, SUM(o.total_amount) AS revenue FROM orders o LEFT JOIN stores s ON o.store_id = s.store_id WHERE o.status IN ('DELIVERED', 'COMPLETED') GROUP BY o.channel, s.store_name ORDER BY revenue DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC289]: Top 10 sản phẩm bán chạy theo doanh thu trong kỳ
-- ----------------------------------------------------------------------------
SELECT TOP 10 p.product_code, p.product_name, SUM(oi.quantity) AS qty_sold, SUM(oi.line_total) AS revenue FROM order_items oi JOIN orders o ON oi.order_id = o.order_id JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id WHERE o.status IN ('DELIVERED', 'COMPLETED') AND o.ordered_at BETWEEN '2026-11-01' AND '2026-11-30' GROUP BY p.product_code, p.product_name ORDER BY revenue DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC290]: Báo cáo lợi nhuận gộp theo sản phẩm (doanh thu trừ giá vốn)
-- ----------------------------------------------------------------------------
SELECT p.product_code, p.product_name, SUM(oi.line_total) AS revenue, SUM(oi.quantity * p.cost_price) AS cogs, SUM(oi.line_total) - SUM(oi.quantity * p.cost_price) AS gross_profit FROM order_items oi JOIN orders o ON oi.order_id = o.order_id JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id WHERE o.status IN ('DELIVERED', 'COMPLETED') GROUP BY p.product_code, p.product_name ORDER BY gross_profit DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC291]: Báo cáo giá trị tồn kho theo từng kho hàng
-- ----------------------------------------------------------------------------
SELECT w.warehouse_name, SUM(inv.stock_quantity) AS total_qty, SUM(inv.stock_quantity * p.cost_price) AS inventory_value FROM inventories inv JOIN warehouses w ON inv.warehouse_id = w.warehouse_id JOIN product_variants pv ON inv.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id GROUP BY w.warehouse_name;
GO

-- ----------------------------------------------------------------------------
-- [UC292]: Danh sách top khách hàng VIP chi tiêu cao nhất
-- ----------------------------------------------------------------------------
SELECT TOP 10 c.customer_code, c.full_name, c.phone, t.tier_name, c.total_orders, c.total_spent FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id WHERE c.is_active = 1 ORDER BY c.total_spent DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC293]: Báo cáo tỷ lệ hủy đơn và đổi trả theo tháng
-- ----------------------------------------------------------------------------
SELECT MONTH(o.ordered_at) AS month_no, COUNT(*) AS total_orders, SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_orders, (SELECT COUNT(*) FROM return_requests r WHERE MONTH(r.created_at) = MONTH(o.ordered_at) AND YEAR(r.created_at) = 2026) AS return_requests FROM orders o WHERE YEAR(o.ordered_at) = 2026 GROUP BY MONTH(o.ordered_at) ORDER BY month_no;
GO

-- ----------------------------------------------------------------------------
-- [UC294]: Thống kê khách hàng mới theo tháng
-- ----------------------------------------------------------------------------
SELECT YEAR(created_at) AS year_no, MONTH(created_at) AS month_no, COUNT(*) AS new_customers FROM customers GROUP BY YEAR(created_at), MONTH(created_at) ORDER BY year_no DESC, month_no DESC;
GO

-- ----------------------------------------------------------------------------
-- [UC295]: Báo cáo lợi nhuận ròng tháng (doanh thu trừ giá vốn và chi phí vận hành)
-- ----------------------------------------------------------------------------
SELECT (SELECT ISNULL(SUM(total_revenue), 0) FROM daily_sales_summaries WHERE summary_date BETWEEN '2026-11-01' AND '2026-11-30') AS revenue, (SELECT ISNULL(SUM(total_cost), 0) FROM daily_sales_summaries WHERE summary_date BETWEEN '2026-11-01' AND '2026-11-30') AS cogs, (SELECT ISNULL(SUM(amount), 0) FROM expenses WHERE expense_date BETWEEN '2026-11-01' AND '2026-11-30') AS operating_expenses;
GO

-- ----------------------------------------------------------------------------
-- [UC296]: Bảng điều khiển tổng quan: đơn hôm nay, doanh thu, tồn thấp, hỗ trợ và đổi trả chờ xử lý
-- ----------------------------------------------------------------------------
SELECT (SELECT COUNT(*) FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE)) AS orders_today, (SELECT ISNULL(SUM(total_amount), 0) FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE) AND status <> 'CANCELLED') AS revenue_today, (SELECT COUNT(*) FROM inventories WHERE stock_quantity <= low_stock_threshold) AS low_stock_items, (SELECT COUNT(*) FROM support_tickets WHERE status IN ('OPEN', 'IN_PROGRESS')) AS open_tickets, (SELECT COUNT(*) FROM return_requests WHERE status = 'PENDING') AS pending_returns;
GO


-- ############################################################################
-- M17 - HỆ THỐNG
-- ############################################################################
-- ----------------------------------------------------------------------------
-- [UC297]: Xem danh sách cấu hình hệ thống
-- ----------------------------------------------------------------------------
SELECT setting_id, setting_key, setting_value, description, updated_at FROM system_settings WHERE setting_key LIKE '%%' ORDER BY setting_key;
GO

-- ----------------------------------------------------------------------------
-- [UC298]: Cập nhật giá trị cấu hình (VAT, ngưỡng miễn phí vận chuyển, thời gian giữ hàng)
-- ----------------------------------------------------------------------------
SELECT * FROM system_settings WHERE setting_key = 'VAT_RATE'; UPDATE system_settings SET setting_value = '10', updated_at = GETDATE() WHERE setting_key = 'VAT_RATE';
GO

-- ----------------------------------------------------------------------------
-- [UC299]: Xem danh sách công việc chạy ngầm và kết quả lần chạy gần nhất
-- ----------------------------------------------------------------------------
SELECT job_id, job_name, cron_expression, is_enabled, last_run_at, next_run_at, last_status FROM scheduled_jobs WHERE job_name LIKE '%%' ORDER BY job_name;
GO

-- ----------------------------------------------------------------------------
-- [UC300]: Bật tắt và đổi lịch chạy của công việc chạy ngầm
-- ----------------------------------------------------------------------------
SELECT * FROM scheduled_jobs WHERE job_name = 'AUTO_CANCEL_UNPAID_ORDERS'; UPDATE scheduled_jobs SET is_enabled = 1, cron_expression = '0 */30 * * * *' WHERE job_name = 'AUTO_CANCEL_UNPAID_ORDERS';
GO

