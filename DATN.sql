-- ============================================================================
-- DATABASE CREATION: HỆ THỐNG QUẢN LÝ BÁN HÀNG THỜI TRANG NAM DUNA - DUNA MEN'S WEAR (19 BẢNG)
-- Core Features: Sản phẩm - Biến thể (Màu/Size), Tồn kho, Bán hàng tại quầy (POS), Voucher & Hạng thành viên
-- Target RDBMS: SQL Server 2016+ / Azure SQL Database
-- Database Name: DUNA
-- ============================================================================

USE [master];
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'DUNA')
BEGIN
    ALTER DATABASE [DUNA] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [DUNA];
END
GO

CREATE DATABASE [DUNA];
GO

USE [DUNA];
GO

-- Bắt buộc bật để dùng Filtered Index (UNIQUE cho các cột cho phép NULL)
SET QUOTED_IDENTIFIER ON;
SET ANSI_NULLS ON;
GO

-- ============================================================================
-- PHÂN HỆ 1: NGƯỜI DÙNG, NHÂN VIÊN & PHÂN QUYỀN (USER & AUTH) - 5 BẢNG
-- ============================================================================

-- 1. Bảng Người dùng hệ thống (Khách hàng, Nhân viên, Quản lý)
CREATE TABLE [users] (
    [user_id] INT IDENTITY(1,1) PRIMARY KEY,
    [user_code] VARCHAR(20) NOT NULL UNIQUE, -- KH-00001..KH-00020 (khách hàng), QL001 / NV001..NV006 (nhân viên)
    [username] VARCHAR(50) NULL, -- Tên đăng nhập POS (chỉ nhân viên) - UNIQUE lọc NULL ở phần Index
    [password_hash] NVARCHAR(255) NULL, -- Nullable với khách mua tại quầy không có tài khoản
    [email] NVARCHAR(255) NULL, -- UNIQUE lọc NULL ở phần Index
    [phone_number] VARCHAR(20) NULL, -- UNIQUE lọc NULL ở phần Index
    [full_name] NVARCHAR(100) NOT NULL,
    [dob] DATE NULL,
    [gender] NVARCHAR(10) DEFAULT N'Nam',
    [status] NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK ([status] IN ('ACTIVE', 'INACTIVE', 'LOCKED')),
    [lock_reason] NVARCHAR(500) NULL,
    [created_at] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [updated_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- 2. Hồ sơ Nhân sự của Nhân viên (CCCD, quê quán, địa chỉ)
CREATE TABLE [employee_profiles] (
    [employee_profile_id] INT IDENTITY(1,1) PRIMARY KEY,
    [user_id] INT NOT NULL UNIQUE FOREIGN KEY REFERENCES [users]([user_id]) ON DELETE CASCADE,
    [id_card_number] VARCHAR(20) NULL, -- Số CCCD - UNIQUE lọc NULL ở phần Index
    [id_card_issued_date] DATE NULL,
    [id_card_expiry_date] DATE NULL,
    [id_card_issued_place] NVARCHAR(255) NULL,
    [nationality] NVARCHAR(50) NOT NULL DEFAULT N'Việt Nam',
    [hometown] NVARCHAR(255) NULL,
    [permanent_address] NVARCHAR(255) NULL,
    [current_address] NVARCHAR(255) NULL,
    [created_at] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [updated_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- 3. Sổ địa chỉ giao hàng của Khách hàng
CREATE TABLE [shipping_addresses] (
    [address_id] INT IDENTITY(1,1) PRIMARY KEY,
    [user_id] INT NOT NULL FOREIGN KEY REFERENCES [users]([user_id]) ON DELETE CASCADE,
    [recipient_name] NVARCHAR(100) NOT NULL,
    [phone] VARCHAR(20) NOT NULL,
    [province] NVARCHAR(100) NOT NULL,
    [district] NVARCHAR(100) NULL,
    [ward] NVARCHAR(100) NULL,
    [detailed_address] NVARCHAR(255) NOT NULL,
    [is_default] BIT NOT NULL DEFAULT 0
);

-- 4. Vai trò hệ thống (RBAC)
CREATE TABLE [roles] (
    [role_id] INT IDENTITY(1,1) PRIMARY KEY,
    [role_code] VARCHAR(50) NOT NULL UNIQUE, -- MANAGER, EMPLOYEE, CUSTOMER
    [role_name] NVARCHAR(100) NOT NULL,
    [description] NVARCHAR(255) NULL
);

-- 5. Gán Vai trò cho Người dùng
CREATE TABLE [user_roles] (
    [user_id] INT NOT NULL FOREIGN KEY REFERENCES [users]([user_id]) ON DELETE CASCADE,
    [role_id] INT NOT NULL FOREIGN KEY REFERENCES [roles]([role_id]) ON DELETE CASCADE,
    [assigned_at] DATETIME2 NOT NULL DEFAULT GETDATE(),
    PRIMARY KEY ([user_id], [role_id])
);

-- ============================================================================
-- PHÂN HỆ 2: DANH MỤC SẢN PHẨM, MÀU SẮC & KÍCH THƯỚC (CATALOG) - 4 BẢNG
-- ============================================================================

-- 6. Danh mục Sản phẩm
CREATE TABLE [categories] (
    [category_id] INT IDENTITY(1,1) PRIMARY KEY,
    [category_name] NVARCHAR(100) NOT NULL,
    [slug] VARCHAR(100) NOT NULL UNIQUE,
    [parent_id] INT NULL FOREIGN KEY REFERENCES [categories]([category_id]),
    [vat_rate] INT NOT NULL DEFAULT 8 CHECK ([vat_rate] BETWEEN 0 AND 100),
    [description] NVARCHAR(255) NULL
);

-- 7. Thư viện Màu sắc Sản phẩm
CREATE TABLE [colors] (
    [color_id] INT IDENTITY(1,1) PRIMARY KEY,
    [color_name] NVARCHAR(50) NOT NULL UNIQUE,
    [hex_code] VARCHAR(10) NOT NULL,
    [color_group] NVARCHAR(50) NULL -- Neutral, Pastel, Dark, Earth
);

-- 8. Bảng Kích thước chuẩn (Size)
CREATE TABLE [sizes] (
    [size_id] INT IDENTITY(1,1) PRIMARY KEY,
    [size_code] VARCHAR(20) NOT NULL,
    [category_type] VARCHAR(30) NOT NULL CHECK ([category_type] IN ('SHIRT', 'PANTS', 'SUIT', 'SHOES', 'ACCESSORY')),
    [display_order] INT DEFAULT 0,
    CONSTRAINT [UQ_size_type_code] UNIQUE([category_type], [size_code])
);

-- 9. Bảng Sản phẩm Chính (Master Product)
CREATE TABLE [products] (
    [product_id] INT IDENTITY(1,1) PRIMARY KEY,
    [product_code] VARCHAR(50) NOT NULL UNIQUE,
    [product_name] NVARCHAR(255) NOT NULL,
    [category_id] INT NOT NULL FOREIGN KEY REFERENCES [categories]([category_id]),
    [brand] NVARCHAR(100) NOT NULL DEFAULT N'DUNA',
    [material_info] NVARCHAR(255) NULL,
    [form_fit] NVARCHAR(50) NULL CHECK ([form_fit] IN (N'Slim Fit', N'Regular Fit', N'Oversize', N'Tailored Fit')),
    [cost_price] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([cost_price] >= 0),
    [base_price] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([base_price] >= 0),
    [sale_price] DECIMAL(18,2) NULL CHECK ([sale_price] >= 0),
    [description] NVARCHAR(MAX) NULL,
    [care_instructions] NVARCHAR(MAX) NULL,
    [status] NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK ([status] IN ('ACTIVE', 'DISCONTINUED', 'HIDDEN')),
    [created_at] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [updated_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- ============================================================================
-- PHÂN HỆ 3: BIẾN THỂ SẢN PHẨM & QUẢN LÝ TỒN KHO - 4 BẢNG
-- ============================================================================

-- 10. Biến thể sản phẩm thực tế (Product Variant = Product + Color + Size)
CREATE TABLE [product_variants] (
    [variant_id] INT IDENTITY(1,1) PRIMARY KEY,
    [product_id] INT NOT NULL FOREIGN KEY REFERENCES [products]([product_id]) ON DELETE CASCADE,
    [color_id] INT NOT NULL FOREIGN KEY REFERENCES [colors]([color_id]),
    [size_id] INT NOT NULL FOREIGN KEY REFERENCES [sizes]([size_id]),
    [sku] VARCHAR(100) NOT NULL UNIQUE,
    [barcode] VARCHAR(100) NULL,
    [additional_price] DECIMAL(18,2) NOT NULL DEFAULT 0,
    [is_active] BIT NOT NULL DEFAULT 1,
    CONSTRAINT [UQ_product_color_size] UNIQUE([product_id], [color_id], [size_id])
);

-- 11. Hình ảnh & Video sản phẩm
CREATE TABLE [product_media] (
    [media_id] INT IDENTITY(1,1) PRIMARY KEY,
    [product_id] INT NOT NULL FOREIGN KEY REFERENCES [products]([product_id]) ON DELETE CASCADE,
    [color_id] INT NULL FOREIGN KEY REFERENCES [colors]([color_id]),
    [media_type] VARCHAR(10) NOT NULL CHECK ([media_type] IN ('IMAGE', 'VIDEO')),
    [media_url] NVARCHAR(500) NOT NULL,
    [is_primary] BIT NOT NULL DEFAULT 0,
    [display_order] INT DEFAULT 0
);

-- 12. Quản lý Tồn kho Biến thể Sản phẩm
CREATE TABLE [inventories] (
    [inventory_id] INT IDENTITY(1,1) PRIMARY KEY,
    [variant_id] INT NOT NULL UNIQUE FOREIGN KEY REFERENCES [product_variants]([variant_id]) ON DELETE CASCADE,
    [stock_quantity] INT NOT NULL DEFAULT 0 CHECK ([stock_quantity] >= 0),
    [reserved_quantity] INT NOT NULL DEFAULT 0 CHECK ([reserved_quantity] >= 0),
    [low_stock_threshold] INT DEFAULT 5,
    [updated_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- 13. Nhật ký Nhập/Xuất & Kiểm kê Kho
CREATE TABLE [inventory_logs] (
    [log_id] INT IDENTITY(1,1) PRIMARY KEY,
    [variant_id] INT NOT NULL FOREIGN KEY REFERENCES [product_variants]([variant_id]),
    [change_type] VARCHAR(20) NOT NULL CHECK ([change_type] IN ('IMPORT', 'EXPORT_ORDER', 'RETURN', 'ADJUSTMENT')),
    [quantity_changed] INT NOT NULL,
    [reason] NVARCHAR(255) NULL,
    [created_at] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [created_by] INT NULL FOREIGN KEY REFERENCES [users]([user_id])
);

-- ============================================================================
-- PHÂN HỆ 4: VOUCHER, ĐƠN HÀNG & THANH TOÁN (ORDERS & PAYMENTS) - 5 BẢNG
-- ============================================================================

-- 14. Mã Giảm giá & Voucher Khuyến mãi
CREATE TABLE [coupons] (
    [coupon_id] INT IDENTITY(1,1) PRIMARY KEY,
    [code] VARCHAR(50) NOT NULL UNIQUE,
    [coupon_name] NVARCHAR(150) NOT NULL DEFAULT N'Voucher ưu đãi thời trang nam DUNA',
    [discount_type] VARCHAR(20) NOT NULL CHECK ([discount_type] IN ('PERCENT', 'FIXED_AMOUNT', 'FREE_SHIPPING')),
    [discount_value] DECIMAL(18,2) NOT NULL CHECK ([discount_value] >= 0),
    [min_order_value] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([min_order_value] >= 0),
    [max_discount_amount] DECIMAL(18,2) NULL,
    [start_date] DATETIME2 NOT NULL,
    [end_date] DATETIME2 NOT NULL,
    [usage_limit] INT NOT NULL DEFAULT 100 CHECK ([usage_limit] >= 0),
    [used_count] INT NOT NULL DEFAULT 0 CHECK ([used_count] >= 0),
    [is_vip_only] BIT NOT NULL DEFAULT 0,
    [is_active] BIT NOT NULL DEFAULT 1,
    CONSTRAINT [CK_coupons_percent_range] CHECK ([discount_type] <> 'PERCENT' OR [discount_value] <= 100),
    CONSTRAINT [CK_coupons_date_range] CHECK ([end_date] >= [start_date])
);

-- 15. Hóa đơn / Đơn đặt hàng (Master Order)
CREATE TABLE [orders] (
    [order_id] INT IDENTITY(1,1) PRIMARY KEY,
    [order_code] VARCHAR(50) NOT NULL UNIQUE,
    [user_id] INT NULL FOREIGN KEY REFERENCES [users]([user_id]),
    [staff_id] INT NULL FOREIGN KEY REFERENCES [users]([user_id]),
    [coupon_id] INT NULL FOREIGN KEY REFERENCES [coupons]([coupon_id]),
    [recipient_name] NVARCHAR(100) NULL,
    [recipient_phone] VARCHAR(20) NULL,
    [shipping_address] NVARCHAR(255) NULL,
    [shipping_fee] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([shipping_fee] >= 0),
    [subtotal_amount] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([subtotal_amount] >= 0),
    [discount_amount] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([discount_amount] >= 0),
    [vat_amount] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([vat_amount] >= 0),
    [final_amount] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([final_amount] >= 0),
    [order_status] NVARCHAR(30) NOT NULL DEFAULT 'PENDING'
        CHECK ([order_status] IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPING', 'DELIVERED', 'COMPLETED', 'CANCELLED')),
    [notes] NVARCHAR(500) NULL,
    [created_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- 16. Chi tiết Sản phẩm trong Hóa đơn
CREATE TABLE [order_items] (
    [order_item_id] INT IDENTITY(1,1) PRIMARY KEY,
    [order_id] INT NOT NULL FOREIGN KEY REFERENCES [orders]([order_id]) ON DELETE CASCADE,
    [variant_id] INT NOT NULL FOREIGN KEY REFERENCES [product_variants]([variant_id]),
    [product_name] NVARCHAR(300) NOT NULL,
    [quantity] INT NOT NULL DEFAULT 1 CHECK ([quantity] > 0),
    [unit_cost] DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK ([unit_cost] >= 0),
    [unit_price] DECIMAL(18,2) NOT NULL CHECK ([unit_price] >= 0),
    [subtotal] DECIMAL(18,2) NOT NULL CHECK ([subtotal] >= 0)
);

-- 17. Bảng Thanh toán Hóa đơn
CREATE TABLE [payments] (
    [payment_id] INT IDENTITY(1,1) PRIMARY KEY,
    [order_id] INT NOT NULL FOREIGN KEY REFERENCES [orders]([order_id]) ON DELETE CASCADE,
    [payment_method] VARCHAR(20) NOT NULL CHECK ([payment_method] IN ('CASH', 'COD', 'BANK_TRANSFER', 'VNPAY', 'ZALOPAY', 'MOMO', 'CREDIT_CARD')),
    [transaction_code] VARCHAR(100) NULL,
    [amount] DECIMAL(18,2) NOT NULL CHECK ([amount] >= 0),
    [cash_received] DECIMAL(18,2) NULL,
    [change_amount] DECIMAL(18,2) NULL,
    [payment_status] VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK ([payment_status] IN ('PENDING', 'SUCCESS', 'FAILED')),
    [paid_at] DATETIME2 NULL
);

-- 18. Lịch sử sử dụng Mã giảm giá
CREATE TABLE [user_coupon_histories] (
    [history_id] INT IDENTITY(1,1) PRIMARY KEY,
    [user_id] INT NOT NULL FOREIGN KEY REFERENCES [users]([user_id]),
    [coupon_id] INT NOT NULL FOREIGN KEY REFERENCES [coupons]([coupon_id]),
    [order_id] INT NOT NULL FOREIGN KEY REFERENCES [orders]([order_id]),
    [used_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- ============================================================================
-- PHÂN HỆ 5: HẠNG THÀNH VIÊN KHÁCH HÀNG (MEMBERSHIP) - 1 BẢNG
-- ============================================================================

-- 19. Hạng Thành viên xét theo tiêu chí tích lũy DUNA Club
CREATE TABLE [user_memberships] (
    [membership_id] INT IDENTITY(1,1) PRIMARY KEY,
    [user_id] INT NOT NULL UNIQUE FOREIGN KEY REFERENCES [users]([user_id]) ON DELETE CASCADE,
    [member_tier] NVARCHAR(30) NOT NULL DEFAULT 'STANDARD' CHECK ([member_tier] IN ('STANDARD', 'SILVER', 'GOLD', 'VIP')),
    [cycle_start_date] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [updated_at] DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================================================
-- TẠO INDEXES TỐI ƯU HIỆU NĂNG & RÀNG BUỘC UNIQUE CHO CỘT CÓ THỂ NULL
-- ============================================================================

CREATE UNIQUE INDEX UX_users_username ON [users] ([username]) WHERE [username] IS NOT NULL;
CREATE UNIQUE INDEX UX_users_email ON [users] ([email]) WHERE [email] IS NOT NULL;
CREATE UNIQUE INDEX UX_users_phone_number ON [users] ([phone_number]) WHERE [phone_number] IS NOT NULL;
CREATE UNIQUE INDEX UX_employee_profiles_id_card ON [employee_profiles] ([id_card_number]) WHERE [id_card_number] IS NOT NULL;
CREATE UNIQUE INDEX UX_product_variants_barcode ON [product_variants] ([barcode]) WHERE [barcode] IS NOT NULL;

CREATE INDEX IX_products_category_status ON [products] ([category_id], [status]);
CREATE INDEX IX_product_variants_product ON [product_variants] ([product_id]);
CREATE INDEX IX_inventories_variant ON [inventories] ([variant_id], [stock_quantity]);
CREATE INDEX IX_orders_user_status ON [orders] ([user_id], [order_status]);
CREATE INDEX IX_orders_staff ON [orders] ([staff_id]);
CREATE INDEX IX_orders_created_at ON [orders] ([created_at]);
CREATE INDEX IX_order_items_order ON [order_items] ([order_id]);
CREATE INDEX IX_order_items_variant ON [order_items] ([variant_id]);
CREATE INDEX IX_payments_order ON [payments] ([order_id]);
GO

-- ============================================================================
-- VIEW TIỆN ÍCH: TỔNG TỒN KHO THEO SẢN PHẨM
-- ============================================================================

CREATE VIEW [vw_product_stock] AS
SELECT
    p.[product_id],
    p.[product_code],
    p.[product_name],
    ISNULL(SUM(i.[stock_quantity]), 0) AS [total_stock_quantity]
FROM [products] p
LEFT JOIN [product_variants] v ON v.[product_id] = p.[product_id]
LEFT JOIN [inventories] i ON i.[variant_id] = v.[variant_id]
GROUP BY p.[product_id], p.[product_code], p.[product_name];
GO

-- ============================================================================
-- SEED DỮ LIỆU MẪU THỜI TRANG NAM DUNA (ĐÃ MỞ RỘNG NGƯỜI DÙNG & SẢN PHẨM)
-- ============================================================================

-- S1. Bảng Vai trò (Roles)
INSERT INTO [roles] ([role_code], [role_name], [description]) VALUES
('MANAGER',  N'Quản lý',            N'Toàn quyền quản trị sản phẩm, nhân viên, hóa đơn và báo cáo doanh thu DUNA'),
('EMPLOYEE', N'Nhân viên bán hàng', N'Bán hàng tại quầy POS, tư vấn size, lập và thanh toán hóa đơn'),
('CUSTOMER', N'Khách hàng',         N'Khách mua hàng thành viên DUNA Club');

-- S2. Người dùng hệ thống (Users)
-- Mật khẩu mặc định của Quản lý & Nhân viên là '123' (Mã băm SHA-256)
-- user_id 1 - 8: Đội ngũ Quản lý & Nhân sự DUNA
INSERT INTO [users] ([user_code], [username], [password_hash], [email], [phone_number], [full_name], [dob], [gender], [status]) VALUES
('QL001', 'admin',     'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'admin@duna.vn',          '0988888888', N'Admin Tổng Quản Lý',      '1990-01-01', N'Nam', 'ACTIVE'),
('QL002', 'manager02', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'hoangnam@duna.vn',      '0989999888', N'Trần Hoàng Nam (Cửa Hàng Trưởng)', '1992-05-15', N'Nam', 'ACTIVE'),
('NV001', 'nv01',      'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'viet.trieu@duna.vn',     '0987654321', N'Triệu Lân Việt (Tổ Trưởng Thu Ngân)', '1999-03-13', N'Nam', 'ACTIVE'),
('NV002', 'nv02',      'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'khanh.nguyen@duna.vn',   '0912345678', N'Nguyễn Danh Khánh',       '1998-10-20', N'Nam', 'ACTIVE'),
('NV003', 'nv03',      'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'minh.nguyen@duna.vn',    '0981234567', N'Nguyễn Ngọc Minh',        '1999-06-12', N'Nam', 'ACTIVE'),
('NV004', 'nv04',      'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'duc.trinh@duna.vn',      '0934567890', N'Trịnh Minh Đức (Kho & POS)', '1998-11-20', N'Nam', 'ACTIVE'),
('NV005', 'nv05',      'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'thang.vu@duna.vn',       '0978112233', N'Vũ Đức Thắng (Thu Ngân Quầy)', '2001-08-25', N'Nam', 'ACTIVE'),
('NV006', 'nv06',      'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'thao.pham@duna.vn',      '0965223344', N'Phạm Thị Thảo (Tư Vấn Phong Cách)', '2000-04-18', N'Nữ',  'ACTIVE');

-- user_id 9 - 28: Khách hàng thành viên DUNA Club (20 khách hàng)
INSERT INTO [users] ([user_code], [email], [phone_number], [full_name], [dob], [gender], [status], [created_at]) VALUES
('KH-00001', 'hoanganh.duna@gmail.com',  '0388741039', N'Nguyễn Hoàng Anh', '1995-05-10', N'Nam', 'ACTIVE', '2024-01-10'),
('KH-00002', 'quocbao.tran@gmail.com',   '0335533938', N'Trần Quốc Bảo',    '1992-03-15', N'Nam', 'ACTIVE', '2024-02-15'),
('KH-00003', 'tuanpm.invest@gmail.com',  '0388967119', N'Phạm Minh Tuấn',   '1998-07-22', N'Nam', 'ACTIVE', '2024-03-01'),
('KH-00004', 'hungdv.fpt@gmail.com',     '0355203320', N'Đỗ Viết Hùng',     '1990-11-08', N'Nam', 'ACTIVE', '2024-04-20'),
('KH-00005', 'hai.lequang@gmail.com',    '0912345679', N'Lê Quang Hải',     '1993-08-14', N'Nam', 'ACTIVE', '2023-12-01'),
('KH-00006', 'cuonghm.tech@gmail.com',   '0987112233', N'Hoàng Mạnh Cường', '1994-02-18', N'Nam', 'ACTIVE', '2024-05-12'),
('KH-00007', 'theanh.bui@gmail.com',     '0976223344', N'Bùi Thế Anh',      '1997-09-05', N'Nam', 'ACTIVE', '2024-06-08'),
('KH-00008', 'minhquan.dang@gmail.com',  '0903334455', N'Đặng Minh Quân',   '1996-12-30', N'Nam', 'ACTIVE', '2024-06-25'),
('KH-00009', 'ducpv.arch@gmail.com',     '0918445566', N'Phan Văn Đức',     '1991-04-25', N'Nam', 'ACTIVE', '2024-07-02'),
('KH-00010', 'longvt.media@gmail.com',   '0935556677', N'Vũ Thành Long',    '1995-10-12', N'Nam', 'ACTIVE', '2024-07-15'),
('KH-00011', 'trongdd.duna@gmail.com',   '0947667788', N'Dương Đình Trọng', '1998-01-20', N'Nam', 'ACTIVE', '2024-08-01'),
('KH-00012', 'tienmx.lawyer@gmail.com',  '0968778899', N'Mai Xuân Tiến',    '1989-06-14', N'Nam', 'ACTIVE', '2024-08-18'),
('KH-00013', 'tiendung.nguyen@gmail.com','0979889900', N'Nguyễn Tiến Dũng', '1993-03-08', N'Nam', 'ACTIVE', '2024-09-05'),
('KH-00014', 'congminh.trinh@gmail.com', '0981990011', N'Trịnh Công Minh',  '1996-05-19', N'Nam', 'ACTIVE', '2024-09-20'),
('KH-00015', 'hoangnam.le@gmail.com',    '0922113355', N'Lê Hoàng Nam',     '1992-11-27', N'Nam', 'ACTIVE', '2024-10-02'),
('KH-00016', 'duykien.dao@gmail.com',    '0933224466', N'Đào Duy Kiên',     '1997-07-03', N'Nam', 'ACTIVE', '2024-10-14'),
('KH-00017', 'quanghuy.ngo@gmail.com',   '0944335577', N'Ngô Quang Huy',    '1994-08-16', N'Nam', 'ACTIVE', '2024-10-25'),
('KH-00018', 'thanhtung.tran@gmail.com', '0955446688', N'Trần Thanh Tùng',  '1990-12-05', N'Nam', 'ACTIVE', '2024-11-01'),
('KH-00019', 'thaonp.gift@gmail.com',    '0966557799', N'Nguyễn Phương Thảo','1998-09-22', N'Nữ',  'ACTIVE', '2024-11-05'),
('KH-00020', 'giabao.luu@gmail.com',     '0977668800', N'Lưu Gia Bảo',      '1995-02-14', N'Nam', 'ACTIVE', '2024-11-08');

-- S3. Phân quyền người dùng (user_roles)
INSERT INTO [user_roles] ([user_id], [role_id]) VALUES
(1, 1), (2, 1),                           -- Quản lý
(3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2), -- Nhân viên
(9, 3), (10, 3), (11, 3), (12, 3), (13, 3), (14, 3), (15, 3), (16, 3), (17, 3), (18, 3),
(19, 3), (20, 3), (21, 3), (22, 3), (23, 3), (24, 3), (25, 3), (26, 3), (27, 3), (28, 3); -- Khách hàng

-- S4. Hồ sơ nhân sự (employee_profiles cho user_id 1 - 8)
INSERT INTO [employee_profiles] ([user_id], [id_card_number], [id_card_issued_date], [id_card_expiry_date], [id_card_issued_place], [nationality], [hometown], [permanent_address], [current_address]) VALUES
(1, '001090000001', '2020-10-10', '2035-04-30', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Hà Nội',    N'Ba Đình, Hà Nội',       N'Số 88 Phố Bùi Thị Xuân, Hai Bà Trưng, Hà Nội'),
(2, '001092000002', '2020-08-12', '2037-05-15', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Hà Nội',    N'Tây Hồ, Hà Nội',        N'Số 12 Ngõ 29 Thụy Khuê, Tây Hồ, Hà Nội'),
(3, '123456789012', '2021-05-15', '2037-08-19', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Hà Nội',    N'Long Biên, Hà Nội',     N'Số 45 Ngõ 12 Phạm Hùng, Nam Từ Liêm, Hà Nội'),
(4, '123456789013', '2022-03-20', '2039-11-25', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Hải Phòng', N'Lê Chân, Hải Phòng',    N'Tòa Golden West, Lê Văn Thiêm, Thanh Xuân, Hà Nội'),
(5, '001099123456', '2021-08-15', '2039-08-15', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Hà Nội',    N'Đống Đa, Hà Nội',       N'Số 15 Ngõ 102 Trường Chinh, Đống Đa, Hà Nội'),
(6, '036098654321', '2022-04-10', '2038-04-10', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Nam Định', N'TP. Nam Định',         N'Số 28 Trần Thái Tông, Cầu Giấy, Hà Nội'),
(7, '001201005566', '2023-01-15', '2041-08-25', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Hà Nội',    N'Hoàng Mai, Hà Nội',     N'Số 68 Tân Mai, Hoàng Mai, Hà Nội'),
(8, '001200008899', '2022-09-10', '2040-04-18', N'Cục Cảnh sát QLHC về TTXH', N'Việt Nam', N'Bắc Ninh', N'TP. Bắc Ninh',        N'Số 105 Chùa Láng, Đống Đa, Hà Nội');

-- S5. Sổ địa chỉ giao hàng khách hàng (shipping_addresses cho user_id 9 - 28)
INSERT INTO [shipping_addresses] ([user_id], [recipient_name], [phone], [province], [detailed_address], [is_default]) VALUES
(9,  N'Nguyễn Hoàng Anh', '0388741039', N'Hà Nội',          N'123 Trương Định, P. Giáp Bát, Q. Hoàng Mai',    1),
(10, N'Trần Quốc Bảo',    '0335533938', N'Hà Nội',          N'45 Lý Thường Kiệt, P. Hàng Bài, Q. Hoàn Kiếm',  1),
(11, N'Phạm Minh Tuấn',   '0388967119', N'TP. Hồ Chí Minh', N'78 Đinh Tiên Hoàng, Đa Kao, Quận 1',           1),
(12, N'Đỗ Viết Hùng',     '0355203320', N'Đà Nẵng',         N'12 Nguyễn Huệ, P. Vĩnh Trung, Q. Thanh Khê',    1),
(13, N'Lê Quang Hải',     '0912345679', N'Hà Nội',          N'99 Hoàng Diệu, P. Quán Thánh, Q. Ba Đình',      1),
(14, N'Hoàng Mạnh Cường', '0987112233', N'Hà Nội',          N'Số 25 Vũ Phạm Hàm, P. Yên Hòa, Q. Cầu Giấy',    1),
(15, N'Bùi Thế Anh',      '0976223344', N'Hà Nội',          N'Số 88 Trần Phú, P. Mộ Lao, Q. Hà Đông',         1),
(16, N'Đặng Minh Quân',   '0903334455', N'TP. Hồ Chí Minh', N'Tòa nhà Landmark 81, P. 22, Q. Bình Thạnh',    1),
(17, N'Phan Văn Đức',     '0918445566', N'Hải Phòng',       N'Số 14 Minh Khai, P. Hoàng Văn Thụ, Q. Hồng Bàng', 1),
(18, N'Vũ Thành Long',    '0935556677', N'Hà Nội',          N'Số 120 Láng Hạ, P. Thành Công, Q. Ba Đình',     1),
(19, N'Dương Đình Trọng', '0947667788', N'Bắc Ninh',        N'Số 35 Lý Thái Tổ, P. Ninh Xá, TP. Bắc Ninh',    1),
(20, N'Mai Xuân Tiến',    '0968778899', N'Hà Nội',          N'Số 50 Nam Đồng, P. Nam Đồng, Q. Đống Đa',       1),
(21, N'Nguyễn Tiến Dũng', '0979889900', N'Quảng Ninh',      N'Số 79 Lê Thánh Tông, P. Bạch Đằng, TP. Hạ Long', 1),
(22, N'Trịnh Công Minh',  '0981990011', N'Hà Nội',          N'Số 18 Nguyễn Cơ Thạch, P. Mỹ Đình 2, Nam Từ Liêm', 1),
(23, N'Lê Hoàng Nam',     '0922113355', N'TP. Hồ Chí Minh', N'Số 420 Nguyễn Thị Minh Khai, Phường 5, Quận 3', 1),
(24, N'Đào Duy Kiên',     '0933224466', N'Hà Nội',          N'Số 302 Cầu Giấy, P. Dịch Vọng, Q. Cầu Giấy',    1),
(25, N'Ngô Quang Huy',    '0944335577', N'Hà Nội',          N'Số 15 Phố Huế, P. Hàng Bài, Q. Hoàn Kiếm',      1),
(26, N'Trần Thanh Tùng',  '0955446688', N'Nam Định',        N'Số 112 Quang Trung, TP. Nam Định',              1),
(27, N'Nguyễn Phương Thảo','0966557799', N'Hà Nội',         N'Số 82 Bà Triệu, P. Hàng Bài, Q. Hoàn Kiếm',     1),
(28, N'Lưu Gia Bảo',      '0977668800', N'Đà Nẵng',         N'Số 204 Bạch Đằng, P. Phước Ninh, Q. Hải Châu',  1);

-- S6. Danh mục Thời trang Nam DUNA (11 danh mục phổ biến, VAT 8%)
INSERT INTO [categories] ([category_name], [slug], [vat_rate], [description]) VALUES
(N'Áo Sơ Mi Nam',        'ao-so-mi-nam',        8, N'Áo sơ mi Oxford, Poplin công sở thanh lịch, chống nhăn tự nhiên'),
(N'Áo Polo Nam',         'ao-polo-nam',         8, N'Áo polo Pima cotton, dệt bo cổ đứng form, trẻ trung năng động'),
(N'Áo Thun & T-Shirt',   'ao-thun-t-shirt',     8, N'Áo thun cổ tròn, cổ tim vải Supima, Modal thoáng mát mùa hè'),
(N'Quần Âu Nam',         'quan-au-nam',         8, N'Quần âu slimfit, cạp tăng giảm, nếp ly dập đứng dáng lịch sự'),
(N'Quần Khaki & Chinos', 'quan-khaki-chinos',   8, N'Quần khaki chải kỹ, chinos co giãn nhẹ chuẩn smart casual'),
(N'Quần Jean Nam',       'quan-jean-nam',       8, N'Quần jean co giãn 4 chiều, wash màu hiện đại và bền bỉ'),
(N'Quần Short Nam',      'quan-short-nam',      8, N'Quần short đũi, short khaki dạo phố và cà phê cuối tuần'),
(N'Áo Blazer & Suit',    'ao-blazer-suit',      8, N'Áo blazer phong cách Ý, ve xếch tôn dáng, nhẹ nhàng thanh lịch'),
(N'Áo Khoác & Jacket',   'ao-khoac-jacket',     8, N'Áo khoác gió trượt nước 2 lớp, bomber kaki, măng tô thu đông'),
(N'Phụ Kiện Nam',        'phu-kien-nam',        8, N'Thắt lưng da bò, ví da bifold, cà vạt lụa dệt DUNA Sartorial'),
(N'Giày Da & Loafers',   'giay-da-loafers',     8, N'Giày penny loafer da bò thật, sneaker da trắng tối giản');

-- S7. Thư viện Màu sắc (14 màu sắc nam tính, sang trọng)
INSERT INTO [colors] ([color_name], [hex_code], [color_group]) VALUES
(N'Trắng',              '#FFFFFF', N'Neutral'),  -- 1
(N'Xanh Baby',          '#89CFF0', N'Pastel'),   -- 2
(N'Đen',                '#000000', N'Dark'),     -- 3
(N'Xanh Navy',          '#000080', N'Dark'),     -- 4
(N'Đen Than',           '#2B2B2B', N'Dark'),     -- 5
(N'Xám Ghi',            '#808080', N'Neutral'),  -- 6
(N'Xanh Midnight Navy', '#191970', N'Dark'),     -- 7
(N'Vàng Khaki Cát',     '#C3B091', N'Earth'),    -- 8
(N'Nâu Cà Phê',         '#6F4E37', N'Earth'),    -- 9
(N'Rêu Phong Olive',    '#556B2F', N'Earth'),    -- 10
(N'Be Sáng Cream',      '#F5F5DC', N'Neutral'),  -- 11
(N'Xanh Cổ Vịt',        '#005662', N'Dark'),     -- 12
(N'Đỏ Mận Wine',        '#722F37', N'Dark'),     -- 13
(N'Xám Khói Smoke',     '#A9A9A9', N'Neutral');  -- 14

-- S8. Kích thước chuẩn theo phân loại hàng (23 size)
INSERT INTO [sizes] ([size_code], [category_type], [display_order]) VALUES
('S',        'SHIRT',     1),   -- 1
('M',        'SHIRT',     2),   -- 2
('L',        'SHIRT',     3),   -- 3
('XL',       'SHIRT',     4),   -- 4
('XXL',      'SHIRT',     5),   -- 5
('3XL',      'SHIRT',     6),   -- 6
('28',       'PANTS',     1),   -- 7
('29',       'PANTS',     2),   -- 8
('30',       'PANTS',     3),   -- 9
('31',       'PANTS',     4),   -- 10
('32',       'PANTS',     5),   -- 11
('33',       'PANTS',     6),   -- 12
('34',       'PANTS',     7),   -- 13
('46',       'SUIT',      1),   -- 14 (tương đương S)
('48',       'SUIT',      2),   -- 15 (tương đương M)
('50',       'SUIT',      3),   -- 16 (tương đương L)
('52',       'SUIT',      4),   -- 17 (tương đương XL)
('39',       'SHOES',     1),   -- 18
('40',       'SHOES',     2),   -- 19
('41',       'SHOES',     3),   -- 20
('42',       'SHOES',     4),   -- 21
('43',       'SHOES',     5),   -- 22
('FREESIZE', 'ACCESSORY', 1);   -- 23

-- S9. Bảng Sản phẩm Chính (25 sản phẩm thời trang nam tầm trung DUNA)
INSERT INTO [products] ([product_code], [product_name], [category_id], [brand], [material_info], [form_fit], [cost_price], [base_price], [description], [status], [created_at]) VALUES
('SP001', N'Áo Sơ Mi Nam Oxford Button-Down DUNA',        1, N'DUNA', N'100% Cotton Oxford dệt đôi cao cấp',       N'Slim Fit',     210000, 450000, N'Áo sơ mi dài tay dệt sợi đôi chống nhăn tự nhiên, thấm hút mồ hôi tối đa, lịch lãm công sở.', 'ACTIVE', '2026-01-01'),
('SP002', N'Áo Polo Nam DUNA Pima Cotton Bo Dệt',        2, N'DUNA', N'95% Pima Cotton, 5% Spandex co giãn',      N'Regular Fit',  175000, 380000, N'Sợi Pima siêu dài mềm mịn gấp 2 lần cotton thường, bo cổ dệt tinh tế không bai gião.', 'ACTIVE', '2026-01-02'),
('SP003', N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA',4, N'DUNA', N'Poly Rayon Spandex xử lý nano',          N'Slim Fit',     240000, 520000, N'Thiết kế cạp thông minh tăng giảm 3cm, đường ly dập vĩnh viễn, đứng dáng cả ngày.', 'ACTIVE', '2026-01-03'),
('SP004', N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend', 8, N'DUNA', N'70% Wool, 30% Silk Blend lót lụa',        N'Tailored Fit', 680000, 1450000,N'Dáng áo ve xếch Ý hiện đại, đệm vai mỏng tự nhiên, thích hợp dự tiệc và sự kiện trang trọng.', 'ACTIVE', '2026-01-04'),
('SP005', N'Quần Khaki Nam DUNA Classic Co Giãn Nhẹ',    5, N'DUNA', N'98% Cotton Khaki chải kỹ, 2% Spandex',     N'Regular Fit',  190000, 420000, N'Vải khaki dệt chéo dày dặn bền màu, giặt máy thoải mái không xù lông. Form straight.', 'ACTIVE', '2026-01-05'),
('SP006', N'Thắt Lưng Da Bò Ý Khóa Tự Động DUNA Milano', 10,N'DUNA', N'Da bò Full-grain thuộc thảo mộc',          NULL,            140000, 350000, N'Mặt khóa hợp kim mạ titan chống trầy, dây da nguyên tấm không bong tróc. Bản dây 3.5cm chuẩn âu phục.', 'ACTIVE', '2026-01-06'),
('SP007', N'Áo Sơ Mi Nam Poplin Trắng Cổ Điển DUNA',     1, N'DUNA', N'100% Poplin Cotton mật độ sợi cao',        N'Regular Fit',  195000, 420000, N'Vải poplin dệt mịn màng, bề mặt bóng nhẹ sang trọng, dễ ủi và luôn phẳng phiu.', 'ACTIVE', '2026-01-07'),
('SP008', N'Áo Sơ Mi Nam Kẻ Sọc Xanh Slimfit DUNA',      1, N'DUNA', N'60% Cotton, 40% Bamboo kháng khuẩn',       N'Slim Fit',     215000, 460000, N'Họa tiết sọc kẻ thanh lịch tạo cảm giác tôn dáng và cao ráo, chất liệu sợi tre mát lạnh.', 'ACTIVE', '2026-01-08'),
('SP009', N'Áo Polo DUNA Phối Bo Cổ Cản Gió Sporty',     2, N'DUNA', N'100% Cotton CVC dệt mắt chim tổ ong',      N'Regular Fit',  180000, 390000, N'Bề mặt vải thoáng khí tổ ong, viền phối cổ tinh tế mang lại vẻ khỏe khoắn nam tính.', 'ACTIVE', '2026-01-09'),
('SP010', N'Áo Polo Len Dệt Kim DUNA Knitwear Thu Đông', 2, N'DUNA', N'Fine Merino Wool Blend dệt jacquard',     N'Slim Fit',     220000, 480000, N'Dòng polo dệt kim thu đông giữ ấm nhẹ, cổ áo bẻ cổ điển sang trọng đậm chất quý ông.', 'ACTIVE', '2026-01-10'),
('SP011', N'Áo Thun Nam Cổ Tròn DUNA Supima Basic',      3, N'DUNA', N'100% Supima Cotton sợi dài Mỹ',            N'Regular Fit',  95000,  220000, N'Áo thun basic cổ tròn cao cấp, thấm mồ hôi tuyệt hảo, không nhão cổ áo sau nhiều lần giặt.', 'ACTIVE', '2026-01-11'),
('SP012', N'Áo Thun Cổ Tim Nam DUNA Modal Thoáng Khí',   3, N'DUNA', N'50% Modal gỗ sồi, 50% Cotton chải',        N'Slim Fit',     105000, 240000, N'Sợi Modal tự nhiên cực kỳ mát mịn, tôn dáng ngực và bắp tay, rất phù hợp mặc lót sơ mi hoặc mặc nhà.', 'ACTIVE', '2026-01-12'),
('SP013', N'Quần Âu Xếp Ly DUNA Pleated Classic Fit',    4, N'DUNA', N'Poly Wool Viscose co giãn 2 chiều',        N'Regular Fit',  250000, 550000, N'Quần âu xếp 1 ly trước mang âm hưởng Sartorial cổ điển, ống suông nhẹ thoải mái vận động.', 'ACTIVE', '2026-01-13'),
('SP014', N'Quần Chinos Nam Co Giãn DUNA Casual',        5, N'DUNA', N'97% Cotton Twill, 3% Elastane',            N'Slim Fit',     205000, 450000, N'Dòng chinos ống ôm vừa vặn, màu sắc trung tính dễ phối cùng áo thun, polo hoặc sơ mi.', 'ACTIVE', '2026-01-14'),
('SP015', N'Quần Jean Nam DUNA Slimfit Indigo Wash',     6, N'DUNA', N'98% Denim Cotton, 2% Spandex co giãn',     N'Slim Fit',     225000, 490000, N'Quần jean wash xanh chàm tự nhiên, co giãn tốt không gò bó, bền màu không ra nước.', 'ACTIVE', '2026-01-15'),
('SP016', N'Quần Jean Nam DUNA Đen Trơn Co Giãn 4 Chiều',6, N'DUNA', N'Cotton Spandex nhuộm đen lưu huỳnh cao cấp',N'Slim Fit',  225000, 490000, N'Gam màu đen trơn thanh lịch, khóa kéo YKK đồng thau bền bỉ, dễ phối mọi phong cách.', 'ACTIVE', '2026-01-16'),
('SP017', N'Quần Short Kaki Nam DUNA Weekend',           7, N'DUNA', N'100% Khaki Cotton chải kỹ',               N'Regular Fit',  145000, 320000, N'Quần short dài trên gối năng động, túi chéo sâu tiện lợi, phong cách dạo phố cà phê.', 'ACTIVE', '2026-01-17'),
('SP018', N'Quần Short Đũi Nam DUNA Summer Linen',       7, N'DUNA', N'70% Đũi Linen tự nhiên, 30% Cotton',       N'Regular Fit',  130000, 290000, N'Vải đũi mềm mát, cạp chun có dây rút linh hoạt, trang phục du lịch biển mùa hè lý tưởng.', 'ACTIVE', '2026-01-18'),
('SP019', N'Áo Khoác Gió Nam DUNA 2 Lớp Trượt Nước',    9, N'DUNA', N'Polyester Microfiber phủ màng Nano Water Repellent', N'Regular Fit', 270000, 590000, N'Khả năng cản gió và chống thấm mưa phùn, lớp lót lưới thoáng khí không hầm bí.', 'ACTIVE', '2026-01-19'),
('SP020', N'Áo Khoác Bomber DUNA Kaki Lót Lụa',          9, N'DUNA', N'Kaki Cotton lì trơn, lót lụa Habutai',     N'Regular Fit',  310000, 680000, N'Áo khoác bomber bo gân cổ tay thể thao, giữ ấm tốt và tạo điểm nhấn mạnh mẽ.', 'ACTIVE', '2026-01-20'),
('SP021', N'Áo Măng Tô Nam DUNA Dáng Lửng Wool Touch',   9, N'DUNA', N'Dạ ép Wool Touch bề mặt mịn màng',         N'Tailored Fit', 580000, 1250000,N'Áo măng tô cúc đôi dáng lửng vừa vặn vóc dáng người Việt, giữ ấm tối ưu mùa lạnh.', 'ACTIVE', '2026-01-21'),
('SP022', N'Cà Vạt Lụa Dệt DUNA Sartorial Bản 7cm',      10,N'DUNA', N'100% Lụa Tơ Tằm Jacquard cao cấp',         NULL,            75000,  190000, N'Bản 7cm tiêu chuẩn cho âu phục, hoa văn dệt chìm tinh tế, đứng form không bị xẹp.', 'ACTIVE', '2026-01-22'),
('SP023', N'Ví Da Bò Nam DUNA Compact Bifold',           10,N'DUNA', N'Da bò Nappa tự nhiên nguyên miếng',        NULL,            135000, 320000, N'Thiết kế gập đôi nhỏ gọn để vừa túi quần âu, ngăn chứa thẻ thông minh chống gãy cong.', 'ACTIVE', '2026-01-23'),
('SP024', N'Giày Penny Loafer Da Bò DUNA Chuẩn Form',    11,N'DUNA', N'Da bò hạt Milled Leather, đế cao su khâu Blake', NULL,      420000, 850000, N'Giày lười penny loafer thanh lịch, lót đệm êm ái chống đau chân khi đứng lâu cả ngày.', 'ACTIVE', '2026-01-24'),
('SP025', N'Giày Sneaker Da Nam DUNA Minimalist Trắng',  11,N'DUNA', N'Da bò thật nappa, đế cao su đúc nguyên khối', NULL,        330000, 690000, N'Giày sneaker da trắng tối giản, phối hoàn hảo từ quần âu, khaki cho tới jean dạo phố.', 'ACTIVE', '2026-01-25');

-- S10. Biến thể sản phẩm (Product Variants: 118 biến thể cho 25 sản phẩm)
INSERT INTO [product_variants] ([product_id], [color_id], [size_id], [sku], [barcode]) VALUES
-- SP001: Áo Sơ Mi Oxford (Trắng: S, M, L, XL | Xanh Baby: M, L)
(1, 1, 1, 'BT001', 'SP001-WHT-S'),
(1, 1, 2, 'BT002', 'SP001-WHT-M'),
(1, 1, 3, 'BT003', 'SP001-WHT-L'),
(1, 1, 4, 'BT004', 'SP001-WHT-XL'),
(1, 2, 2, 'BT005', 'SP001-BLU-M'),
(1, 2, 3, 'BT006', 'SP001-BLU-L'),

-- SP002: Áo Polo Pima (Đen: M, L, XL | Xanh Navy: M, L, XL | Trắng: M, L)
(2, 3, 2, 'BT007', 'SP002-BLK-M'),
(2, 3, 3, 'BT008', 'SP002-BLK-L'),
(2, 3, 4, 'BT009', 'SP002-BLK-XL'),
(2, 4, 2, 'BT010', 'SP002-NVY-M'),
(2, 4, 3, 'BT011', 'SP002-NVY-L'),
(2, 4, 4, 'BT012', 'SP002-NVY-XL'),
(2, 1, 2, 'BT013', 'SP002-WHT-M'),
(2, 1, 3, 'BT014', 'SP002-WHT-L'),

-- SP003: Quần Âu Slimfit (Đen Than: 29, 30, 31, 32 | Xám Ghi: 30, 31, 32)
(3, 5, 8, 'BT015', 'SP003-BLK-29'),
(3, 5, 9, 'BT016', 'SP003-BLK-30'),
(3, 5, 10,'BT017', 'SP003-BLK-31'),
(3, 5, 11,'BT018', 'SP003-BLK-32'),
(3, 6, 9, 'BT019', 'SP003-GRY-30'),
(3, 6, 10,'BT020', 'SP003-GRY-31'),
(3, 6, 11,'BT021', 'SP003-GRY-32'),

-- SP004: Áo Blazer Ý (Midnight Navy: 48, 50, 52 | Đen Than: 48, 50)
(4, 7, 15, 'BT022', 'SP004-NVY-48'),
(4, 7, 16, 'BT023', 'SP004-NVY-50'),
(4, 7, 17, 'BT024', 'SP004-NVY-52'),
(4, 5, 15, 'BT025', 'SP004-BLK-48'),
(4, 5, 16, 'BT026', 'SP004-BLK-50'),

-- SP005: Quần Khaki Classic (Vàng Khaki: 30, 31, 32 | Xanh Navy: 30, 31, 32)
(5, 8, 9,  'BT027', 'SP005-KHK-30'),
(5, 8, 10, 'BT028', 'SP005-KHK-31'),
(5, 8, 11, 'BT029', 'SP005-KHK-32'),
(5, 4, 9,  'BT030', 'SP005-NVY-30'),
(5, 4, 10, 'BT031', 'SP005-NVY-31'),
(5, 4, 11, 'BT032', 'SP005-NVY-32'),

-- SP006: Thắt Lưng Da Bò Ý (Đen / Nâu Cà Phê: Freesize)
(6, 3, 23, 'BT033', 'SP006-BLK-FS'),
(6, 9, 23, 'BT034', 'SP006-BRN-FS'),

-- SP007: Áo Sơ Mi Poplin Trắng (Trắng: S, M, L, XL)
(7, 1, 1, 'BT035', 'SP007-WHT-S'),
(7, 1, 2, 'BT036', 'SP007-WHT-M'),
(7, 1, 3, 'BT037', 'SP007-WHT-L'),
(7, 1, 4, 'BT038', 'SP007-WHT-XL'),

-- SP008: Áo Sơ Mi Kẻ Sọc Xanh (Xanh Baby: M, L, XL)
(8, 2, 2, 'BT039', 'SP008-STR-M'),
(8, 2, 3, 'BT040', 'SP008-STR-L'),
(8, 2, 4, 'BT041', 'SP008-STR-XL'),

-- SP009: Áo Polo Sporty Tổ Ong (Xanh Cổ Vịt: M, L, XL | Đen: M, L)
(9, 12, 2, 'BT042', 'SP009-TEA-M'),
(9, 12, 3, 'BT043', 'SP009-TEA-L'),
(9, 12, 4, 'BT044', 'SP009-TEA-XL'),
(9, 3, 2,  'BT045', 'SP009-BLK-M'),
(9, 3, 3,  'BT046', 'SP009-BLK-L'),

-- SP010: Áo Polo Knitwear Dệt Kim (Nâu Cà Phê: M, L | Be Sáng Cream: M, L)
(10, 9, 2,  'BT047', 'SP010-BRN-M'),
(10, 9, 3,  'BT048', 'SP010-BRN-L'),
(10, 11, 2, 'BT049', 'SP010-CRM-M'),
(10, 11, 3, 'BT050', 'SP010-CRM-L'),

-- SP011: Áo Thun Supima Basic (Trắng: S, M, L, XL | Đen: M, L, XL | Xám Khói: M, L)
(11, 1, 1,  'BT051', 'SP011-WHT-S'),
(11, 1, 2,  'BT052', 'SP011-WHT-M'),
(11, 1, 3,  'BT053', 'SP011-WHT-L'),
(11, 1, 4,  'BT054', 'SP011-WHT-XL'),
(11, 3, 2,  'BT055', 'SP011-BLK-M'),
(11, 3, 3,  'BT056', 'SP011-BLK-L'),
(11, 3, 4,  'BT057', 'SP011-BLK-XL'),
(11, 14, 2, 'BT058', 'SP011-SMK-M'),
(11, 14, 3, 'BT059', 'SP011-SMK-L'),

-- SP012: Áo Thun Modal Cổ Tim (Trắng: M, L | Xanh Navy: M, L)
(12, 1, 2, 'BT060', 'SP012-WHT-M'),
(12, 1, 3, 'BT061', 'SP012-WHT-L'),
(12, 4, 2, 'BT062', 'SP012-NVY-M'),
(12, 4, 3, 'BT063', 'SP012-NVY-L'),

-- SP013: Quần Âu Xếp Ly (Xám Ghi: 30, 31, 32 | Đen Than: 30, 31)
(13, 6, 9,  'BT064', 'SP013-GRY-30'),
(13, 6, 10, 'BT065', 'SP013-GRY-31'),
(13, 6, 11, 'BT066', 'SP013-GRY-32'),
(13, 5, 9,  'BT067', 'SP013-BLK-30'),
(13, 5, 10, 'BT068', 'SP013-BLK-31'),

-- SP014: Quần Chinos Casual (Rêu Phong: 30, 31, 32 | Vàng Khaki: 30, 31)
(14, 10, 9,  'BT069', 'SP014-OLV-30'),
(14, 10, 10, 'BT070', 'SP014-OLV-31'),
(14, 10, 11, 'BT071', 'SP014-OLV-32'),
(14, 8, 9,   'BT072', 'SP014-KHK-30'),
(14, 8, 10,  'BT073', 'SP014-KHK-31'),

-- SP015: Quần Jean Slimfit Indigo (Xanh Indigo: 29, 30, 31, 32)
(15, 7, 8,  'BT074', 'SP015-IND-29'),
(15, 7, 9,  'BT075', 'SP015-IND-30'),
(15, 7, 10, 'BT076', 'SP015-IND-31'),
(15, 7, 11, 'BT077', 'SP015-IND-32'),

-- SP016: Quần Jean Đen Trơn (Đen: 29, 30, 31, 32)
(16, 3, 8,  'BT078', 'SP016-BLK-29'),
(16, 3, 9,  'BT079', 'SP016-BLK-30'),
(16, 3, 10, 'BT080', 'SP016-BLK-31'),
(16, 3, 11, 'BT081', 'SP016-BLK-32'),

-- SP017: Quần Short Kaki Weekend (Be Sáng: 30, 31, 32 | Xanh Navy: 30, 31)
(17, 11, 9,  'BT082', 'SP017-CRM-30'),
(17, 11, 10, 'BT083', 'SP017-CRM-31'),
(17, 11, 11, 'BT084', 'SP017-CRM-32'),
(17, 4, 9,   'BT085', 'SP017-NVY-30'),
(17, 4, 10,  'BT086', 'SP017-NVY-31'),

-- SP018: Quần Short Đũi Linen (Trắng: 30, 31 | Xám Khói: 30, 31)
(18, 1, 9,   'BT087', 'SP018-WHT-30'),
(18, 1, 10,  'BT088', 'SP018-WHT-31'),
(18, 14, 9,  'BT089', 'SP018-SMK-30'),
(18, 14, 10, 'BT090', 'SP018-SMK-31'),

-- SP019: Áo Khoác Gió Trượt Nước (Đen: M, L, XL | Xanh Navy: M, L, XL)
(19, 3, 2, 'BT091', 'SP019-BLK-M'),
(19, 3, 3, 'BT092', 'SP019-BLK-L'),
(19, 3, 4, 'BT093', 'SP019-BLK-XL'),
(19, 4, 2, 'BT094', 'SP019-NVY-M'),
(19, 4, 3, 'BT095', 'SP019-NVY-L'),
(19, 4, 4, 'BT096', 'SP019-NVY-XL'),

-- SP020: Áo Bomber Kaki (Rêu Phong: M, L | Đen: M, L)
(20, 10, 2, 'BT097', 'SP020-OLV-M'),
(20, 10, 3, 'BT098', 'SP020-OLV-L'),
(20, 3, 2,  'BT099', 'SP020-BLK-M'),
(20, 3, 3,  'BT100', 'SP020-BLK-L'),

-- SP021: Áo Măng Tô Wool Touch (Nâu Cà Phê: 48, 50 | Đen Than: 48, 50)
(21, 9, 15, 'BT101', 'SP021-BRN-48'),
(21, 9, 16, 'BT102', 'SP021-BRN-50'),
(21, 5, 15, 'BT103', 'SP021-BLK-48'),
(21, 5, 16, 'BT104', 'SP021-BLK-50'),

-- SP022: Cà Vạt Lụa (Đỏ Mận / Midnight Navy: Freesize)
(22, 13, 23, 'BT105', 'SP022-WNE-FS'),
(22, 7, 23,  'BT106', 'SP022-NVY-FS'),

-- SP023: Ví Da Bò Bifold (Đen / Nâu Cà Phê: Freesize)
(23, 3, 23, 'BT107', 'SP023-BLK-FS'),
(23, 9, 23, 'BT108', 'SP023-BRN-FS'),

-- SP024: Giày Loafer Da Bò (Đen: 40, 41, 42 | Nâu: 40, 41, 42)
(24, 3, 19, 'BT109', 'SP024-BLK-40'),
(24, 3, 20, 'BT110', 'SP024-BLK-41'),
(24, 3, 21, 'BT111', 'SP024-BLK-42'),
(24, 9, 19, 'BT112', 'SP024-BRN-40'),
(24, 9, 20, 'BT113', 'SP024-BRN-41'),
(24, 9, 21, 'BT114', 'SP024-BRN-42'),

-- SP025: Giày Sneaker Da Trắng (Trắng: 39, 40, 41, 42)
(25, 1, 18, 'BT115', 'SP025-WHT-39'),
(25, 1, 19, 'BT116', 'SP025-WHT-40'),
(25, 1, 20, 'BT117', 'SP025-WHT-41'),
(25, 1, 21, 'BT118', 'SP025-WHT-42');

-- S11. Hình ảnh sản phẩm (product_media)
INSERT INTO [product_media] ([product_id], [color_id], [media_type], [media_url], [is_primary], [display_order]) VALUES
(1,  NULL, 'IMAGE', N'img/sp-somi-oxford.jpg',       1, 1),
(2,  NULL, 'IMAGE', N'img/sp-polo-pima.jpg',         1, 1),
(3,  NULL, 'IMAGE', N'img/sp-quan-au.jpg',           1, 1),
(4,  NULL, 'IMAGE', N'img/sp-blazer-italy.jpg',      1, 1),
(5,  NULL, 'IMAGE', N'img/sp-quan-khaki.jpg',        1, 1),
(6,  NULL, 'IMAGE', N'img/sp-that-lung.jpg',         1, 1),
(7,  NULL, 'IMAGE', N'img/sp-somi-poplin.jpg',       1, 1),
(8,  NULL, 'IMAGE', N'img/sp-somi-kesoc.jpg',        1, 1),
(9,  NULL, 'IMAGE', N'img/sp-polo-sporty.jpg',       1, 1),
(10, NULL, 'IMAGE', N'img/sp-polo-knitwear.jpg',     1, 1),
(11, NULL, 'IMAGE', N'img/sp-thun-supima.jpg',       1, 1),
(12, NULL, 'IMAGE', N'img/sp-thun-modal.jpg',        1, 1),
(13, NULL, 'IMAGE', N'img/sp-quan-au-xeplay.jpg',    1, 1),
(14, NULL, 'IMAGE', N'img/sp-quan-chinos.jpg',       1, 1),
(15, NULL, 'IMAGE', N'img/sp-jean-indigo.jpg',       1, 1),
(16, NULL, 'IMAGE', N'img/sp-jean-black.jpg',        1, 1),
(17, NULL, 'IMAGE', N'img/sp-short-kaki.jpg',        1, 1),
(18, NULL, 'IMAGE', N'img/sp-short-linen.jpg',       1, 1),
(19, NULL, 'IMAGE', N'img/sp-khoac-gio.jpg',         1, 1),
(20, NULL, 'IMAGE', N'img/sp-bomber-kaki.jpg',       1, 1),
(21, NULL, 'IMAGE', N'img/sp-mang-to.jpg',           1, 1),
(22, NULL, 'IMAGE', N'img/sp-cavat-lua.jpg',         1, 1),
(23, NULL, 'IMAGE', N'img/sp-vi-da-bifold.jpg',      1, 1),
(24, NULL, 'IMAGE', N'img/sp-loafer-penny.jpg',      1, 1),
(25, NULL, 'IMAGE', N'img/sp-sneaker-white.jpg',     1, 1);

-- S12. Tồn kho cho toàn bộ 118 biến thể (inventories)
INSERT INTO [inventories] ([variant_id], [stock_quantity]) VALUES
(1, 25), (2, 35), (3, 30), (4, 15), (5, 20), (6, 20),
(7, 30), (8, 35), (9, 20), (10, 25), (11, 30), (12, 15), (13, 20), (14, 25),
(15, 20), (16, 40), (17, 35), (18, 15), (19, 25), (20, 30), (21, 15),
(22, 15), (23, 20), (24, 10), (25, 15), (26, 15),
(27, 30), (28, 35), (29, 20), (30, 25), (31, 30), (32, 15),
(33, 40), (34, 30),
(35, 20), (36, 35), (37, 30), (38, 15),
(39, 25), (40, 30), (41, 15),
(42, 25), (43, 30), (44, 15), (45, 20), (46, 25),
(47, 20), (48, 25), (49, 20), (50, 25),
(51, 35), (52, 50), (53, 45), (54, 25), (55, 40), (56, 45), (57, 25), (58, 30), (59, 25),
(60, 25), (61, 30), (62, 25), (63, 30),
(64, 20), (65, 30), (66, 15), (67, 25), (68, 20),
(69, 25), (70, 35), (71, 20), (72, 25), (73, 20),
(74, 20), (75, 35), (76, 30), (77, 15),
(78, 20), (79, 35), (80, 30), (81, 15),
(82, 25), (83, 30), (84, 15), (85, 25), (86, 20),
(87, 20), (88, 25), (89, 20), (90, 25),
(91, 30), (92, 35), (93, 20), (94, 25), (95, 30), (96, 15),
(97, 20), (98, 25), (99, 20), (100, 25),
(101, 15), (102, 15), (103, 12), (104, 15),
(105, 30), (106, 25),
(107, 35), (108, 30),
(109, 15), (110, 20), (111, 15), (112, 15), (113, 20), (114, 15),
(115, 20), (116, 30), (117, 25), (118, 15);

-- S13. Nhật ký Nhập kho khởi tạo
INSERT INTO [inventory_logs] ([variant_id], [change_type], [quantity_changed], [reason], [created_by])
SELECT [variant_id], 'IMPORT', [stock_quantity], N'Nhập kho ban đầu khai trương chuỗi DUNA', 1
FROM [inventories];

-- S14. Mã Giảm giá & Voucher ưu đãi DUNA
INSERT INTO [coupons] ([code], [coupon_name], [discount_type], [discount_value], [min_order_value], [start_date], [end_date], [usage_limit], [used_count], [is_vip_only], [is_active]) VALUES
('DUNA10',    N'Giảm 10% đơn hàng thời trang nam DUNA',            'PERCENT',      10,     500000,  '2026-01-01', '2027-12-31', 200, 25, 0, 1),
('DUNA50K',   N'Giảm trực tiếp 50.000 VNĐ cho đơn từ 600K',        'FIXED_AMOUNT', 50000,  600000,  '2026-01-01', '2027-12-31', 100, 18, 0, 1),
('DUNA100K',  N'Giảm trực tiếp 100.000 VNĐ cho đơn từ 1.2 Triệu',   'FIXED_AMOUNT', 100000, 1200000, '2026-01-01', '2027-12-31', 50,  12, 0, 1),
('VIP15P',    N'Đặc quyền khách VIP: Giảm 15% tổng hóa đơn',       'PERCENT',      15,     800000,  '2026-01-01', '2027-12-31', 100, 8,  1, 1),
('SUMMER20',  N'Ưu đãi hè DUNA: Giảm 20.000 VNĐ đơn mua sắm',      'FIXED_AMOUNT', 20000,  400000,  '2026-01-01', '2027-12-31', 300, 42, 0, 1),
('WELCOME50', N'Chào mừng thành viên mới: Giảm 50.000 VNĐ',        'FIXED_AMOUNT', 50000,  500000,  '2026-01-01', '2027-12-31', 500, 65, 0, 1),
('MENSDAY',   N'Tuần lễ quý ông: Giảm 5% trực tiếp trên hóa đơn',  'PERCENT',      5,      300000,  '2026-01-01', '2027-12-31', 200, 19, 0, 1);

-- S15. Danh sách Hóa đơn bán lẻ DUNA (18 đơn hàng mẫu trải dài qua các tháng 2026)
INSERT INTO [orders] ([order_code], [user_id], [staff_id], [coupon_id], [recipient_name], [recipient_phone], [shipping_address], [subtotal_amount], [discount_amount], [vat_amount], [final_amount], [order_status], [notes], [created_at]) VALUES
('HD-001', 9,  3, NULL, N'Nguyễn Hoàng Anh', '0388741039', N'123 Trương Định, Hà Nội',    970000,  0,      71852,  970000,  'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-07-01 09:15:22'),
('HD-002', 10, 4, NULL, N'Trần Quốc Bảo',    '0335533938', N'45 Lý Thường Kiệt, Hà Nội',  1450000, 0,      107407, 1450000, 'COMPLETED', N'Bán hàng tại quầy | Quét mã QR VietQR', '2026-07-01 11:42:10'),
('HD-003', 11, 5, NULL, N'Phạm Minh Tuấn',   '0388967119', N'78 Đinh Tiên Hoàng, TP.HCM', 520000,  0,      38519,  520000,  'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-07-02 10:20:15'),
('HD-004', 9,  3, 4,    N'Nguyễn Hoàng Anh', '0388741039', N'123 Trương Định, Hà Nội',    1970000, 295500, 124037, 1674500, 'COMPLETED', N'Bán hàng tại quầy | Áp voucher VIP15P', '2026-07-02 14:35:45'),
('HD-005', 12, 6, NULL, N'Đỗ Viết Hùng',     '0355203320', N'12 Nguyễn Huệ, Đà Nẵng',    800000,  0,      59259,  800000,  'PENDING',   N'Hóa đơn chờ - Bán hàng tại quầy', '2026-07-03 08:50:30'),
('HD-006', 13, 3, 1,    N'Lê Quang Hải',     '0912345679', N'99 Hoàng Diệu, Hà Nội',      1290000, 129000, 86000,  1161000, 'COMPLETED', N'Bán hàng tại quầy | Chuyển khoản', '2026-07-10 16:15:00'),
('HD-007', 14, 4, NULL, N'Hoàng Mạnh Cường', '0987112233', N'25 Vũ Phạm Hàm, Hà Nội',     870000,  0,      64444,  870000,  'COMPLETED', N'Bán hàng tại quầy | Quẹt thẻ POS', '2026-07-15 17:30:22'),
('HD-008', 15, 5, 2,    N'Bùi Thế Anh',      '0976223344', N'88 Trần Phú, Hà Nội',        840000,  50000,  58519,  790000,  'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-07-20 10:45:10'),
('HD-009', 16, 6, NULL, N'Đặng Minh Quân',   '0903334455', N'Landmark 81, TP.HCM',        1540000, 0,      114074, 1540000, 'COMPLETED', N'Bán hàng tại quầy | VNPAY-QR', '2026-08-01 14:10:05'),
('HD-010', 17, 7, NULL, N'Phan Văn Đức',     '0918445566', N'14 Minh Khai, Hải Phòng',    910000,  0,      67407,  910000,  'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-08-05 11:20:18'),
('HD-011', 18, 8, 3,    N'Vũ Thành Long',    '0935556677', N'120 Láng Hạ, Hà Nội',        1450000, 100000, 100000, 1350000, 'COMPLETED', N'Bán hàng tại quầy | Chuyển khoản', '2026-08-12 18:05:40'),
('HD-012', 19, 3, NULL, N'Dương Đình Trọng', '0947667788', N'35 Lý Thái Tổ, Bắc Ninh',    540000,  0,      40000,  540000,  'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-08-18 09:30:15'),
('HD-013', 20, 4, NULL, N'Mai Xuân Tiến',    '0968778899', N'50 Nam Đồng, Hà Nội',        1800000, 0,      133333, 1800000, 'COMPLETED', N'Bán hàng tại quầy | Quẹt thẻ ngân hàng', '2026-08-25 15:40:50'),
('HD-014', 21, 5, NULL, N'Nguyễn Tiến Dũng', '0979889900', N'79 Lê Thánh Tông, Hạ Long',   490000,  0,      36296,  490000,  'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-09-02 10:15:20'),
('HD-015', 22, 6, NULL, N'Trịnh Công Minh',  '0981990011', N'18 Nguyễn Cơ Thạch, Hà Nội', 970000,  0,      71852,  970000,  'COMPLETED', N'Bán hàng tại quầy | Chuyển khoản', '2026-09-10 16:50:33'),
('HD-016', 23, 7, NULL, N'Lê Hoàng Nam',     '0922113355', N'420 Nguyễn Thị Minh Khai, HCM',1170000,0,      86667,  1170000, 'COMPLETED', N'Bán hàng tại quầy | VNPAY-QR', '2026-09-18 14:22:10'),
('HD-017', 27, 8, 1,    N'Nguyễn Phương Thảo','0966557799', N'82 Bà Triệu, Hà Nội',        1800000, 180000, 120000, 1620000, 'COMPLETED', N'Bán hàng tại quầy | Mua quà tặng', '2026-10-05 19:10:45'),
('HD-018', 28, 3, NULL, N'Lưu Gia Bảo',      '0977668800', N'204 Bạch Đằng, Đà Nẵng',     1070000, 0,      79259,  1070000, 'COMPLETED', N'Bán hàng tại quầy | Tiền mặt', '2026-10-20 11:35:12');

-- S16. Chi tiết hóa đơn (kèm unit_cost để tính Lợi nhuận gộp chuẩn xác)
INSERT INTO [order_items] ([order_id], [variant_id], [product_name], [quantity], [unit_cost], [unit_price], [subtotal]) VALUES
-- HD-001 (970K)
(1, 2,  N'Áo Sơ Mi Nam Oxford Button-Down DUNA - Trắng - Size M', 1, 210000, 450000, 450000),
(1, 16, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 30', 1, 240000, 520000, 520000),

-- HD-002 (1450K)
(2, 23, N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend - Xanh Midnight Navy - Size 50', 1, 680000, 1450000, 1450000),

-- HD-003 (520K)
(3, 17, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 31', 1, 240000, 520000, 520000),

-- HD-004 (1970K)
(4, 23, N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend - Xanh Midnight Navy - Size 50', 1, 680000, 1450000, 1450000),
(4, 16, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 30', 1, 240000, 520000, 520000),

-- HD-005 (800K)
(5, 7,  N'Áo Polo Nam DUNA Pima Cotton Bo Dệt - Đen - Size M', 1, 175000, 380000, 380000),
(5, 27, N'Quần Khaki Nam DUNA Classic Co Giãn Nhẹ - Vàng Khaki Cát - Size 30', 1, 190000, 420000, 420000),

-- HD-006 (1290K)
(6, 2,  N'Áo Sơ Mi Nam Oxford Button-Down DUNA - Trắng - Size M', 1, 210000, 450000, 450000),
(6, 76, N'Quần Jean Nam DUNA Slimfit Indigo Wash - Size 31', 1, 225000, 490000, 490000),
(6, 33, N'Thắt Lưng Da Bò Ý Khóa Tự Động DUNA Milano - Đen - Freesize', 1, 140000, 350000, 350000),

-- HD-007 (870K)
(7, 10, N'Áo Polo Nam DUNA Pima Cotton Bo Dệt - Xanh Navy - Size M', 1, 175000, 380000, 380000),
(7, 80, N'Quần Jean Nam DUNA Đen Trơn Co Giãn 4 Chiều - Size 31', 1, 225000, 490000, 490000),

-- HD-008 (840K)
(8, 36, N'Áo Sơ Mi Nam Poplin Trắng Cổ Điển DUNA - Size M', 1, 195000, 420000, 420000),
(8, 28, N'Quần Khaki Nam DUNA Classic Co Giãn Nhẹ - Vàng Khaki Cát - Size 31', 1, 190000, 420000, 420000),

-- HD-009 (1540K)
(9, 110,N'Giày Penny Loafer Da Bò DUNA Chuẩn Form - Đen - Size 41', 1, 420000, 850000, 850000),
(9, 92, N'Áo Khoác Gió Nam DUNA 2 Lớp Trượt Nước - Đen - Size L', 1, 270000, 590000, 590000),
(9, 105,N'Cà Vạt Lụa Dệt DUNA Sartorial Bản 7cm - Đỏ Mận', 1, 75000, 190000, 190000),

-- HD-010 (910K)
(10, 40,N'Áo Sơ Mi Nam Kẻ Sọc Xanh Slimfit DUNA - Size L', 1, 215000, 460000, 460000),
(10, 70,N'Quần Chinos Nam Co Giãn DUNA Casual - Rêu Phong - Size 31', 1, 205000, 450000, 450000),

-- HD-011 (1450K)
(11, 22,N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend - Midnight Navy - Size 48', 1, 680000, 1450000, 1450000),

-- HD-012 (540K)
(12, 52,N'Áo Thun Nam Cổ Tròn DUNA Supima Basic - Trắng - Size M', 1, 95000, 220000, 220000),
(12, 83,N'Quần Short Kaki Nam DUNA Weekend - Be Sáng - Size 31', 1, 145000, 320000, 320000),

-- HD-013 (1800K)
(13, 23,N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend - Midnight Navy - Size 50', 1, 680000, 1450000, 1450000),
(13, 34,N'Thắt Lưng Da Bò Ý Khóa Tự Động DUNA Milano - Nâu - Freesize', 1, 140000, 350000, 350000),

-- HD-014 (490K)
(14, 76,N'Quần Jean Nam DUNA Slimfit Indigo Wash - Size 31', 1, 225000, 490000, 490000),

-- HD-015 (970K)
(15, 3,  N'Áo Sơ Mi Nam Oxford Button-Down DUNA - Trắng - Size L', 1, 210000, 450000, 450000),
(15, 17, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 31', 1, 240000, 520000, 520000),

-- HD-016 (1170K)
(16, 98, N'Áo Bomber DUNA Kaki Lót Lụa - Rêu Phong - Size L', 1, 310000, 680000, 680000),
(16, 75, N'Quần Jean Nam DUNA Slimfit Indigo Wash - Size 30', 1, 225000, 490000, 490000),

-- HD-017 (1800K)
(17, 102,N'Áo Măng Tô Nam DUNA Dáng Lửng Wool Touch - Nâu Cà Phê - Size 50', 1, 580000, 1250000, 1250000),
(17, 65, N'Quần Âu Xếp Ly DUNA Pleated Classic Fit - Xám Ghi - Size 31', 1, 250000, 550000, 550000),

-- HD-018 (1070K)
(18, 116,N'Giày Sneaker Da Nam DUNA Minimalist Trắng - Size 40', 1, 330000, 690000, 690000),
(18, 7,  N'Áo Polo Nam DUNA Pima Cotton Bo Dệt - Đen - Size M', 1, 175000, 380000, 380000);

-- S17. Thanh toán hóa đơn (payments)
INSERT INTO [payments] ([order_id], [payment_method], [amount], [cash_received], [change_amount], [payment_status], [paid_at]) VALUES
(1,  'CASH',          970000,  1000000, 30000, 'SUCCESS', '2026-07-01 09:20:00'),
(2,  'BANK_TRANSFER', 1450000, NULL,    NULL,  'SUCCESS', '2026-07-01 11:45:00'),
(3,  'CASH',          520000,  520000,  0,     'SUCCESS', '2026-07-02 10:22:00'),
(4,  'BANK_TRANSFER', 1674500, NULL,    NULL,  'SUCCESS', '2026-07-02 14:40:00'),
(5,  'CASH',          800000,  NULL,    NULL,  'PENDING', NULL),
(6,  'BANK_TRANSFER', 1161000, NULL,    NULL,  'SUCCESS', '2026-07-10 16:20:00'),
(7,  'CREDIT_CARD',   870000,  NULL,    NULL,  'SUCCESS', '2026-07-15 17:35:00'),
(8,  'CASH',          790000,  800000,  10000, 'SUCCESS', '2026-07-20 10:50:00'),
(9,  'VNPAY',         1540000, NULL,    NULL,  'SUCCESS', '2026-08-01 14:15:00'),
(10, 'CASH',          910000,  1000000, 90000, 'SUCCESS', '2026-08-05 11:25:00'),
(11, 'BANK_TRANSFER', 1350000, NULL,    NULL,  'SUCCESS', '2026-08-12 18:10:00'),
(12, 'CASH',          540000,  550000,  10000, 'SUCCESS', '2026-08-18 09:35:00'),
(13, 'CREDIT_CARD',   1800000, NULL,    NULL,  'SUCCESS', '2026-08-25 15:45:00'),
(14, 'CASH',          490000,  500000,  10000, 'SUCCESS', '2026-09-02 10:20:00'),
(15, 'BANK_TRANSFER', 970000,  NULL,    NULL,  'SUCCESS', '2026-09-10 16:55:00'),
(16, 'VNPAY',         1170000, NULL,    NULL,  'SUCCESS', '2026-09-18 14:28:00'),
(17, 'BANK_TRANSFER', 1620000, NULL,    NULL,  'SUCCESS', '2026-10-05 19:15:00'),
(18, 'CASH',          1070000, 1100000, 30000, 'SUCCESS', '2026-10-20 11:40:00');

-- S18. Hạng thành viên khách hàng (user_memberships cho 20 khách hàng user_id 9 - 28)
INSERT INTO [user_memberships] ([user_id], [member_tier], [cycle_start_date]) VALUES
(9,  'VIP',      DATEADD(MONTH, -6, GETDATE())),
(10, 'GOLD',     DATEADD(MONTH, -4, GETDATE())),
(11, 'SILVER',   DATEADD(MONTH, -2, GETDATE())),
(12, 'STANDARD', GETDATE()),
(13, 'GOLD',     DATEADD(MONTH, -3, GETDATE())),
(14, 'SILVER',   DATEADD(MONTH, -5, GETDATE())),
(15, 'SILVER',   DATEADD(MONTH, -4, GETDATE())),
(16, 'GOLD',     DATEADD(MONTH, -3, GETDATE())),
(17, 'SILVER',   DATEADD(MONTH, -2, GETDATE())),
(18, 'GOLD',     DATEADD(MONTH, -2, GETDATE())),
(19, 'STANDARD', DATEADD(MONTH, -1, GETDATE())),
(20, 'GOLD',     DATEADD(MONTH, -2, GETDATE())),
(21, 'STANDARD', DATEADD(MONTH, -1, GETDATE())),
(22, 'SILVER',   DATEADD(MONTH, -2, GETDATE())),
(23, 'GOLD',     DATEADD(MONTH, -1, GETDATE())),
(24, 'STANDARD', GETDATE()),
(25, 'STANDARD', GETDATE()),
(26, 'STANDARD', GETDATE()),
(27, 'GOLD',     DATEADD(MONTH, -1, GETDATE())),
(28, 'SILVER',   DATEADD(MONTH, -1, GETDATE()));
GO

-- S19. Đồng bộ Hạng Thành Viên theo Tổng Chi Tiêu Thực Tế (Hóa đơn COMPLETED)
UPDATE m
SET m.[member_tier] = CASE
        WHEN ISNULL(o.[total_spent], 0) >= 3000000 THEN 'VIP'
        WHEN ISNULL(o.[total_spent], 0) >= 1500000 THEN 'GOLD'
        WHEN ISNULL(o.[total_spent], 0) >= 700000  THEN 'SILVER'
        ELSE 'STANDARD'
    END,
    m.[updated_at] = GETDATE()
FROM [user_memberships] m
LEFT JOIN (
    SELECT [user_id], SUM([final_amount]) AS [total_spent]
    FROM [orders]
    WHERE [order_status] = 'COMPLETED'
      AND [user_id] IS NOT NULL
    GROUP BY [user_id]
) o ON m.[user_id] = o.[user_id];
GO

PRINT N'====================================================================';
PRINT N'✅ ĐÃ KHỞI TẠO CƠ SỞ DỮ LIỆU [DUNA] VÀ NẠP DỮ LIỆU MẪU THÀNH CÔNG!';
PRINT N'   - 19 Bảng chuẩn hóa quan hệ & tối ưu Index';
PRINT N'   - 8 Tài khoản quản trị & nhân sự DUNA (Mật khẩu: 123)';
PRINT N'   - 20 Khách hàng thành viên DUNA Club đầy đủ địa chỉ giao hàng';
PRINT N'   - 11 Danh mục & 25 Sản phẩm thời trang nam tầm trung';
PRINT N'   - 118 Biến thể (Màu x Size), Tồn kho & Nhật ký nhập xuất';
PRINT N'   - 7 Mã giảm giá Voucher';
PRINT N'   - 18 Hóa đơn & Chi tiết bán lẻ kèm Lợi nhuận gộp';
PRINT N'====================================================================';
