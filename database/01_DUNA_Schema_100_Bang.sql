-- ============================================================================
-- DUNA MEN'S WEAR - CƠ SỞ DỮ LIỆU BÁN HÀNG THỜI TRANG NAM (SQL Server / T-SQL)
-- Tổng số bảng: 100 | Nhóm chức năng: 17 module
-- Địa chỉ theo mô hình hành chính 2 cấp (tỉnh/thành phố -> xã/phường/đặc khu), không còn cấp quận/huyện
-- Chạy toàn bộ file bằng SSMS hoặc sqlcmd. Bảng được sắp xếp theo thứ tự phụ thuộc khóa ngoại.
-- ============================================================================
IF DB_ID(N'DUNA_MensWear') IS NULL
    CREATE DATABASE [DUNA_MensWear] COLLATE Vietnamese_CI_AS;
GO
USE [DUNA_MensWear];
GO
-- ---- [M01 - Tài khoản & Phân quyền] roles ----
-- Vai trò phân quyền
CREATE TABLE roles (
    role_id                  INT IDENTITY(1,1) NOT NULL,
    role_code                VARCHAR(30) NOT NULL,
    role_name                NVARCHAR(100) NOT NULL,
    description              NVARCHAR(255) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_roles_is_active DEFAULT 1,
    CONSTRAINT PK_roles PRIMARY KEY (role_id),
    CONSTRAINT UQ_roles_role_code UNIQUE (role_code)
);
GO
-- ---- [M01 - Tài khoản & Phân quyền] permissions ----
-- Danh mục quyền hạn
CREATE TABLE permissions (
    permission_id            INT IDENTITY(1,1) NOT NULL,
    permission_code          VARCHAR(60) NOT NULL,
    permission_name          NVARCHAR(150) NOT NULL,
    module_name              NVARCHAR(50) NULL,
    CONSTRAINT PK_permissions PRIMARY KEY (permission_id),
    CONSTRAINT UQ_permissions_permission_code UNIQUE (permission_code)
);
GO
-- ---- [M01 - Tài khoản & Phân quyền] role_permissions ----
-- Gán quyền cho vai trò
CREATE TABLE role_permissions (
    role_permission_id       INT IDENTITY(1,1) NOT NULL,
    role_id                  INT NOT NULL,
    permission_id            INT NOT NULL,
    CONSTRAINT PK_role_permissions PRIMARY KEY (role_permission_id),
    CONSTRAINT UQ_role_permissions_role_id_permission_id UNIQUE (role_id, permission_id),
    CONSTRAINT FK_role_permissions_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    CONSTRAINT FK_role_permissions_permission_id FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);
GO
CREATE INDEX IX_role_permissions_permission_id ON role_permissions(permission_id);
GO
-- ---- [M01 - Tài khoản & Phân quyền] users ----
-- Tài khoản đăng nhập (khách hàng + nhân viên)
CREATE TABLE users (
    user_id                  INT IDENTITY(1,1) NOT NULL,
    email                    VARCHAR(150) NOT NULL,
    phone                    VARCHAR(15) NULL,
    password_hash            VARCHAR(255) NOT NULL,
    full_name                NVARCHAR(150) NOT NULL,
    avatar_url               VARCHAR(500) NULL,
    role_id                  INT NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_users_status DEFAULT 'ACTIVE',
    email_verified           BIT NOT NULL CONSTRAINT DF_users_email_verified DEFAULT 0,
    last_login_at            DATETIME2(0) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_users_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_users PRIMARY KEY (user_id),
    CONSTRAINT UQ_users_email UNIQUE (email),
    CONSTRAINT FK_users_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT CK_users_status CHECK (status IN ('ACTIVE', 'LOCKED', 'PENDING'))
);
GO
CREATE UNIQUE INDEX UX_users_phone ON users(phone) WHERE phone IS NOT NULL;
CREATE INDEX IX_users_role_id ON users(role_id);
GO
-- ---- [M08 - Vận chuyển & Địa lý] shipping_zones ----
-- Vùng giao hàng
CREATE TABLE shipping_zones (
    zone_id                  INT IDENTITY(1,1) NOT NULL,
    zone_name                NVARCHAR(100) NOT NULL,
    description              NVARCHAR(255) NULL,
    CONSTRAINT PK_shipping_zones PRIMARY KEY (zone_id)
);
GO
-- ---- [M08 - Vận chuyển & Địa lý] provinces ----
-- Tỉnh / thành phố (34 đơn vị cấp tỉnh)
CREATE TABLE provinces (
    province_id              INT IDENTITY(1,1) NOT NULL,
    province_code            VARCHAR(10) NOT NULL,
    province_name            NVARCHAR(100) NOT NULL,
    zone_id                  INT NULL,
    CONSTRAINT PK_provinces PRIMARY KEY (province_id),
    CONSTRAINT UQ_provinces_province_code UNIQUE (province_code),
    CONSTRAINT FK_provinces_zone_id FOREIGN KEY (zone_id) REFERENCES shipping_zones(zone_id)
);
GO
CREATE INDEX IX_provinces_zone_id ON provinces(zone_id);
GO
-- ---- [M08 - Vận chuyển & Địa lý] wards ----
-- Xã / phường / đặc khu (mô hình 2 cấp)
CREATE TABLE wards (
    ward_id                  INT IDENTITY(1,1) NOT NULL,
    province_id              INT NOT NULL,
    ward_code                VARCHAR(10) NOT NULL,
    ward_name                NVARCHAR(100) NOT NULL,
    ward_type                VARCHAR(20) NOT NULL CONSTRAINT DF_wards_ward_type DEFAULT 'WARD',
    CONSTRAINT PK_wards PRIMARY KEY (ward_id),
    CONSTRAINT FK_wards_province_id FOREIGN KEY (province_id) REFERENCES provinces(province_id),
    CONSTRAINT CK_wards_ward_type CHECK (ward_type IN ('WARD', 'COMMUNE', 'SPECIAL_ZONE'))
);
GO
CREATE INDEX IX_wards_province_id ON wards(province_id);
GO
-- ---- [M01 - Tài khoản & Phân quyền] user_addresses ----
-- Sổ địa chỉ nhận hàng
CREATE TABLE user_addresses (
    address_id               INT IDENTITY(1,1) NOT NULL,
    user_id                  INT NOT NULL,
    receiver_name            NVARCHAR(150) NOT NULL,
    receiver_phone           VARCHAR(15) NOT NULL,
    province_id              INT NULL,
    ward_id                  INT NULL,
    address_line             NVARCHAR(255) NOT NULL,
    is_default               BIT NOT NULL CONSTRAINT DF_user_addresses_is_default DEFAULT 0,
    CONSTRAINT PK_user_addresses PRIMARY KEY (address_id),
    CONSTRAINT FK_user_addresses_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_user_addresses_province_id FOREIGN KEY (province_id) REFERENCES provinces(province_id),
    CONSTRAINT FK_user_addresses_ward_id FOREIGN KEY (ward_id) REFERENCES wards(ward_id)
);
GO
CREATE INDEX IX_user_addresses_user_id ON user_addresses(user_id);
CREATE INDEX IX_user_addresses_province_id ON user_addresses(province_id);
CREATE INDEX IX_user_addresses_ward_id ON user_addresses(ward_id);
GO
-- ---- [M01 - Tài khoản & Phân quyền] password_reset_tokens ----
-- Token đặt lại mật khẩu
CREATE TABLE password_reset_tokens (
    token_id                 INT IDENTITY(1,1) NOT NULL,
    user_id                  INT NOT NULL,
    token_hash               VARCHAR(255) NOT NULL,
    expires_at               DATETIME2(0) NOT NULL,
    used_at                  DATETIME2(0) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_password_reset_tokens_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_password_reset_tokens PRIMARY KEY (token_id),
    CONSTRAINT FK_password_reset_tokens_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_password_reset_tokens_user_id ON password_reset_tokens(user_id);
GO
-- ---- [M01 - Tài khoản & Phân quyền] login_histories ----
-- Lịch sử đăng nhập
CREATE TABLE login_histories (
    login_id                 BIGINT IDENTITY(1,1) NOT NULL,
    user_id                  INT NULL,
    email_attempt            VARCHAR(150) NULL,
    ip_address               VARCHAR(45) NULL,
    device_info              NVARCHAR(300) NULL,
    is_success               BIT NOT NULL,
    fail_reason              NVARCHAR(200) NULL,
    login_at                 DATETIME2(0) NOT NULL CONSTRAINT DF_login_histories_login_at DEFAULT GETDATE(),
    CONSTRAINT PK_login_histories PRIMARY KEY (login_id),
    CONSTRAINT FK_login_histories_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_login_histories_user_id ON login_histories(user_id);
GO
-- ---- [M01 - Tài khoản & Phân quyền] audit_logs ----
-- Nhật ký thao tác hệ thống
CREATE TABLE audit_logs (
    audit_id                 BIGINT IDENTITY(1,1) NOT NULL,
    user_id                  INT NULL,
    action                   VARCHAR(50) NOT NULL,
    entity_name              VARCHAR(60) NOT NULL,
    entity_id                VARCHAR(40) NULL,
    old_value                NVARCHAR(MAX) NULL,
    new_value                NVARCHAR(MAX) NULL,
    ip_address               VARCHAR(45) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_audit_logs_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_audit_logs PRIMARY KEY (audit_id),
    CONSTRAINT FK_audit_logs_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_audit_logs_user_id ON audit_logs(user_id);
GO
-- ---- [M02 - Khách hàng] membership_tiers ----
-- Hạng thành viên
CREATE TABLE membership_tiers (
    tier_id                  INT IDENTITY(1,1) NOT NULL,
    tier_name                NVARCHAR(50) NOT NULL,
    min_spend                DECIMAL(18,0) NOT NULL CONSTRAINT DF_membership_tiers_min_spend DEFAULT 0,
    discount_percent         DECIMAL(5,2) NOT NULL CONSTRAINT DF_membership_tiers_discount_percent DEFAULT 0,
    point_multiplier         DECIMAL(3,2) NOT NULL CONSTRAINT DF_membership_tiers_point_multiplier DEFAULT 1,
    benefits                 NVARCHAR(500) NULL,
    CONSTRAINT PK_membership_tiers PRIMARY KEY (tier_id),
    CONSTRAINT UQ_membership_tiers_tier_name UNIQUE (tier_name)
);
GO
-- ---- [M02 - Khách hàng] customer_groups ----
-- Nhóm khách hàng
CREATE TABLE customer_groups (
    group_id                 INT IDENTITY(1,1) NOT NULL,
    group_name               NVARCHAR(100) NOT NULL,
    description              NVARCHAR(255) NULL,
    CONSTRAINT PK_customer_groups PRIMARY KEY (group_id)
);
GO
-- ---- [M02 - Khách hàng] customers ----
-- Hồ sơ khách hàng (kể cả khách vãng lai tại quầy)
CREATE TABLE customers (
    customer_id              INT IDENTITY(1,1) NOT NULL,
    user_id                  INT NULL,
    customer_code            VARCHAR(20) NOT NULL,
    full_name                NVARCHAR(150) NOT NULL,
    phone                    VARCHAR(15) NULL,
    email                    VARCHAR(150) NULL,
    gender                   VARCHAR(10) NULL,
    birth_date               DATE NULL,
    tier_id                  INT NULL,
    group_id                 INT NULL,
    total_spent              DECIMAL(18,0) NOT NULL CONSTRAINT DF_customers_total_spent DEFAULT 0,
    total_orders             INT NOT NULL CONSTRAINT DF_customers_total_orders DEFAULT 0,
    is_active                BIT NOT NULL CONSTRAINT DF_customers_is_active DEFAULT 1,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_customers_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_customers PRIMARY KEY (customer_id),
    CONSTRAINT UQ_customers_customer_code UNIQUE (customer_code),
    CONSTRAINT FK_customers_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT CK_customers_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT FK_customers_tier_id FOREIGN KEY (tier_id) REFERENCES membership_tiers(tier_id),
    CONSTRAINT FK_customers_group_id FOREIGN KEY (group_id) REFERENCES customer_groups(group_id)
);
GO
CREATE UNIQUE INDEX UX_customers_user_id ON customers(user_id) WHERE user_id IS NOT NULL;
CREATE INDEX IX_customers_user_id ON customers(user_id);
CREATE INDEX IX_customers_tier_id ON customers(tier_id);
CREATE INDEX IX_customers_group_id ON customers(group_id);
GO
-- ---- [M02 - Khách hàng] customer_measurements ----
-- Số đo cơ thể khách hàng (tư vấn size)
CREATE TABLE customer_measurements (
    measurement_id           INT IDENTITY(1,1) NOT NULL,
    customer_id              INT NOT NULL,
    height_cm                DECIMAL(5,1) NULL,
    weight_kg                DECIMAL(5,1) NULL,
    chest_cm                 DECIMAL(5,1) NULL,
    waist_cm                 DECIMAL(5,1) NULL,
    hip_cm                   DECIMAL(5,1) NULL,
    shoulder_cm              DECIMAL(5,1) NULL,
    measured_at              DATETIME2(0) NOT NULL CONSTRAINT DF_customer_measurements_measured_at DEFAULT GETDATE(),
    CONSTRAINT PK_customer_measurements PRIMARY KEY (measurement_id),
    CONSTRAINT FK_customer_measurements_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);
GO
CREATE INDEX IX_customer_measurements_customer_id ON customer_measurements(customer_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] categories ----
-- Danh mục sản phẩm (cây cha - con)
CREATE TABLE categories (
    category_id              INT IDENTITY(1,1) NOT NULL,
    category_name            NVARCHAR(150) NOT NULL,
    slug                     VARCHAR(180) NOT NULL,
    parent_id                INT NULL,
    description              NVARCHAR(500) NULL,
    image_url                VARCHAR(500) NULL,
    display_order            INT NOT NULL CONSTRAINT DF_categories_display_order DEFAULT 0,
    is_active                BIT NOT NULL CONSTRAINT DF_categories_is_active DEFAULT 1,
    CONSTRAINT PK_categories PRIMARY KEY (category_id),
    CONSTRAINT UQ_categories_slug UNIQUE (slug),
    CONSTRAINT FK_categories_parent_id FOREIGN KEY (parent_id) REFERENCES categories(category_id)
);
GO
CREATE INDEX IX_categories_parent_id ON categories(parent_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] collections ----
-- Bộ sưu tập theo mùa
CREATE TABLE collections (
    collection_id            INT IDENTITY(1,1) NOT NULL,
    collection_name          NVARCHAR(150) NOT NULL,
    season                   VARCHAR(20) NULL,
    banner_image_url         VARCHAR(500) NULL,
    description              NVARCHAR(500) NULL,
    start_date               DATE NULL,
    end_date                 DATE NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_collections_is_active DEFAULT 1,
    CONSTRAINT PK_collections PRIMARY KEY (collection_id),
    CONSTRAINT CK_collections_season CHECK (season IN ('SPRING_SUMMER', 'FALL_WINTER', 'ALL_SEASON'))
);
GO
-- ---- [M03 - Sản phẩm & Danh mục] brands ----
-- Thương hiệu
CREATE TABLE brands (
    brand_id                 INT IDENTITY(1,1) NOT NULL,
    brand_name               NVARCHAR(100) NOT NULL,
    logo_url                 VARCHAR(500) NULL,
    description              NVARCHAR(500) NULL,
    CONSTRAINT PK_brands PRIMARY KEY (brand_id),
    CONSTRAINT UQ_brands_brand_name UNIQUE (brand_name)
);
GO
-- ---- [M03 - Sản phẩm & Danh mục] colors ----
-- Bảng màu
CREATE TABLE colors (
    color_id                 INT IDENTITY(1,1) NOT NULL,
    color_name               NVARCHAR(50) NOT NULL,
    hex_code                 VARCHAR(7) NOT NULL,
    color_group              VARCHAR(20) NULL,
    CONSTRAINT PK_colors PRIMARY KEY (color_id)
);
GO
-- ---- [M03 - Sản phẩm & Danh mục] sizes ----
-- Kích cỡ
CREATE TABLE sizes (
    size_id                  INT IDENTITY(1,1) NOT NULL,
    size_code                VARCHAR(10) NOT NULL,
    category_type            VARCHAR(20) NOT NULL,
    display_order            INT NOT NULL CONSTRAINT DF_sizes_display_order DEFAULT 0,
    CONSTRAINT PK_sizes PRIMARY KEY (size_id),
    CONSTRAINT UQ_sizes_size_code_category_type UNIQUE (size_code, category_type),
    CONSTRAINT CK_sizes_category_type CHECK (category_type IN ('SHIRT', 'PANTS', 'SUIT', 'SHOES', 'ACCESSORY'))
);
GO
-- ---- [M03 - Sản phẩm & Danh mục] size_chart_details ----
-- Bảng thông số size chuẩn
CREATE TABLE size_chart_details (
    chart_detail_id          INT IDENTITY(1,1) NOT NULL,
    category_id              INT NOT NULL,
    size_id                  INT NOT NULL,
    min_height_cm            DECIMAL(5,1) NULL,
    max_height_cm            DECIMAL(5,1) NULL,
    min_weight_kg            DECIMAL(5,1) NULL,
    max_weight_kg            DECIMAL(5,1) NULL,
    min_chest_cm             DECIMAL(5,1) NULL,
    max_chest_cm             DECIMAL(5,1) NULL,
    min_waist_cm             DECIMAL(5,1) NULL,
    max_waist_cm             DECIMAL(5,1) NULL,
    CONSTRAINT PK_size_chart_details PRIMARY KEY (chart_detail_id),
    CONSTRAINT FK_size_chart_details_category_id FOREIGN KEY (category_id) REFERENCES categories(category_id),
    CONSTRAINT FK_size_chart_details_size_id FOREIGN KEY (size_id) REFERENCES sizes(size_id)
);
GO
CREATE INDEX IX_size_chart_details_category_id ON size_chart_details(category_id);
CREATE INDEX IX_size_chart_details_size_id ON size_chart_details(size_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] size_recommendation_rules ----
-- Quy tắc tư vấn size tự động
CREATE TABLE size_recommendation_rules (
    rule_id                  INT IDENTITY(1,1) NOT NULL,
    category_id              INT NOT NULL,
    form_fit                 VARCHAR(20) NOT NULL,
    height_min               DECIMAL(5,1) NOT NULL,
    height_max               DECIMAL(5,1) NOT NULL,
    weight_min               DECIMAL(5,1) NOT NULL,
    weight_max               DECIMAL(5,1) NOT NULL,
    recommended_size_id      INT NOT NULL,
    CONSTRAINT PK_size_recommendation_rules PRIMARY KEY (rule_id),
    CONSTRAINT FK_size_recommendation_rules_category_id FOREIGN KEY (category_id) REFERENCES categories(category_id),
    CONSTRAINT CK_size_recommendation_rules_form_fit CHECK (form_fit IN ('REGULAR_FIT', 'SLIM_FIT', 'OVERSIZE')),
    CONSTRAINT FK_size_recommendation_rules_recommended_size_id FOREIGN KEY (recommended_size_id) REFERENCES sizes(size_id)
);
GO
CREATE INDEX IX_size_recommendation_rules_category_id ON size_recommendation_rules(category_id);
CREATE INDEX IX_size_recommendation_rules_recommended_size_id ON size_recommendation_rules(recommended_size_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] products ----
-- Sản phẩm
CREATE TABLE products (
    product_id               INT IDENTITY(1,1) NOT NULL,
    product_code             VARCHAR(20) NOT NULL,
    product_name             NVARCHAR(200) NOT NULL,
    slug                     VARCHAR(220) NULL,
    category_id              INT NOT NULL,
    collection_id            INT NULL,
    brand_id                 INT NULL,
    base_price               DECIMAL(18,0) NOT NULL,
    cost_price               DECIMAL(18,0) NOT NULL CONSTRAINT DF_products_cost_price DEFAULT 0,
    material_info            NVARCHAR(300) NULL,
    form_fit                 VARCHAR(20) NULL,
    description              NVARCHAR(MAX) NULL,
    care_instructions        NVARCHAR(500) NULL,
    is_featured              BIT NOT NULL CONSTRAINT DF_products_is_featured DEFAULT 0,
    is_active                BIT NOT NULL CONSTRAINT DF_products_is_active DEFAULT 1,
    view_count               INT NOT NULL CONSTRAINT DF_products_view_count DEFAULT 0,
    sold_count               INT NOT NULL CONSTRAINT DF_products_sold_count DEFAULT 0,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_products_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_products PRIMARY KEY (product_id),
    CONSTRAINT UQ_products_product_code UNIQUE (product_code),
    CONSTRAINT UQ_products_slug UNIQUE (slug),
    CONSTRAINT FK_products_category_id FOREIGN KEY (category_id) REFERENCES categories(category_id),
    CONSTRAINT FK_products_collection_id FOREIGN KEY (collection_id) REFERENCES collections(collection_id),
    CONSTRAINT FK_products_brand_id FOREIGN KEY (brand_id) REFERENCES brands(brand_id),
    CONSTRAINT CK_products_form_fit CHECK (form_fit IN ('REGULAR_FIT', 'SLIM_FIT', 'OVERSIZE'))
);
GO
CREATE INDEX IX_products_category_id ON products(category_id);
CREATE INDEX IX_products_collection_id ON products(collection_id);
CREATE INDEX IX_products_brand_id ON products(brand_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] product_variants ----
-- Biến thể sản phẩm (màu x size)
CREATE TABLE product_variants (
    variant_id               INT IDENTITY(1,1) NOT NULL,
    product_id               INT NOT NULL,
    color_id                 INT NOT NULL,
    size_id                  INT NOT NULL,
    sku                      VARCHAR(50) NOT NULL,
    barcode                  VARCHAR(20) NULL,
    price                    DECIMAL(18,0) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_product_variants_is_active DEFAULT 1,
    CONSTRAINT PK_product_variants PRIMARY KEY (variant_id),
    CONSTRAINT UQ_product_variants_sku UNIQUE (sku),
    CONSTRAINT UQ_product_variants_product_id_color_id_size_id UNIQUE (product_id, color_id, size_id),
    CONSTRAINT FK_product_variants_product_id FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT FK_product_variants_color_id FOREIGN KEY (color_id) REFERENCES colors(color_id),
    CONSTRAINT FK_product_variants_size_id FOREIGN KEY (size_id) REFERENCES sizes(size_id)
);
GO
CREATE INDEX IX_product_variants_color_id ON product_variants(color_id);
CREATE INDEX IX_product_variants_size_id ON product_variants(size_id);
CREATE UNIQUE INDEX UX_product_variants_barcode ON product_variants(barcode) WHERE barcode IS NOT NULL;
GO
-- ---- [M03 - Sản phẩm & Danh mục] product_media ----
-- Ảnh / video sản phẩm
CREATE TABLE product_media (
    media_id                 INT IDENTITY(1,1) NOT NULL,
    product_id               INT NOT NULL,
    color_id                 INT NULL,
    media_type               VARCHAR(10) NOT NULL CONSTRAINT DF_product_media_media_type DEFAULT 'IMAGE',
    media_url                VARCHAR(500) NOT NULL,
    is_primary               BIT NOT NULL CONSTRAINT DF_product_media_is_primary DEFAULT 0,
    display_order            INT NOT NULL CONSTRAINT DF_product_media_display_order DEFAULT 0,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_product_media_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_product_media PRIMARY KEY (media_id),
    CONSTRAINT FK_product_media_product_id FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT FK_product_media_color_id FOREIGN KEY (color_id) REFERENCES colors(color_id),
    CONSTRAINT CK_product_media_media_type CHECK (media_type IN ('IMAGE', 'VIDEO'))
);
GO
CREATE INDEX IX_product_media_product_id ON product_media(product_id);
CREATE INDEX IX_product_media_color_id ON product_media(color_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] product_tags ----
-- Thẻ sản phẩm
CREATE TABLE product_tags (
    tag_id                   INT IDENTITY(1,1) NOT NULL,
    tag_name                 NVARCHAR(50) NOT NULL,
    slug                     VARCHAR(60) NOT NULL,
    CONSTRAINT PK_product_tags PRIMARY KEY (tag_id),
    CONSTRAINT UQ_product_tags_tag_name UNIQUE (tag_name),
    CONSTRAINT UQ_product_tags_slug UNIQUE (slug)
);
GO
-- ---- [M03 - Sản phẩm & Danh mục] product_tag_map ----
-- Gắn thẻ cho sản phẩm
CREATE TABLE product_tag_map (
    product_tag_id           INT IDENTITY(1,1) NOT NULL,
    product_id               INT NOT NULL,
    tag_id                   INT NOT NULL,
    CONSTRAINT PK_product_tag_map PRIMARY KEY (product_tag_id),
    CONSTRAINT UQ_product_tag_map_product_id_tag_id UNIQUE (product_id, tag_id),
    CONSTRAINT FK_product_tag_map_product_id FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    CONSTRAINT FK_product_tag_map_tag_id FOREIGN KEY (tag_id) REFERENCES product_tags(tag_id) ON DELETE CASCADE
);
GO
CREATE INDEX IX_product_tag_map_tag_id ON product_tag_map(tag_id);
GO
-- ---- [M03 - Sản phẩm & Danh mục] price_histories ----
-- Lịch sử thay đổi giá
CREATE TABLE price_histories (
    price_history_id         INT IDENTITY(1,1) NOT NULL,
    product_id               INT NOT NULL,
    old_price                DECIMAL(18,0) NOT NULL,
    new_price                DECIMAL(18,0) NOT NULL,
    changed_by               INT NULL,
    reason                   NVARCHAR(255) NULL,
    changed_at               DATETIME2(0) NOT NULL CONSTRAINT DF_price_histories_changed_at DEFAULT GETDATE(),
    CONSTRAINT PK_price_histories PRIMARY KEY (price_history_id),
    CONSTRAINT FK_price_histories_product_id FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT FK_price_histories_changed_by FOREIGN KEY (changed_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_price_histories_product_id ON price_histories(product_id);
CREATE INDEX IX_price_histories_changed_by ON price_histories(changed_by);
GO
-- ---- [M04 - Kho & Nhập hàng] warehouses ----
-- Kho hàng
CREATE TABLE warehouses (
    warehouse_id             INT IDENTITY(1,1) NOT NULL,
    warehouse_code           VARCHAR(20) NOT NULL,
    warehouse_name           NVARCHAR(150) NOT NULL,
    address                  NVARCHAR(255) NULL,
    phone                    VARCHAR(15) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_warehouses_is_active DEFAULT 1,
    CONSTRAINT PK_warehouses PRIMARY KEY (warehouse_id),
    CONSTRAINT UQ_warehouses_warehouse_code UNIQUE (warehouse_code)
);
GO
-- ---- [M04 - Kho & Nhập hàng] inventories ----
-- Tồn kho theo biến thể & kho
CREATE TABLE inventories (
    inventory_id             INT IDENTITY(1,1) NOT NULL,
    variant_id               INT NOT NULL,
    warehouse_id             INT NOT NULL,
    stock_quantity           INT NOT NULL CONSTRAINT DF_inventories_stock_quantity DEFAULT 0,
    reserved_quantity        INT NOT NULL CONSTRAINT DF_inventories_reserved_quantity DEFAULT 0,
    low_stock_threshold      INT NOT NULL CONSTRAINT DF_inventories_low_stock_threshold DEFAULT 5,
    updated_at               DATETIME2(0) NOT NULL CONSTRAINT DF_inventories_updated_at DEFAULT GETDATE(),
    CONSTRAINT PK_inventories PRIMARY KEY (inventory_id),
    CONSTRAINT UQ_inventories_variant_id_warehouse_id UNIQUE (variant_id, warehouse_id),
    CONSTRAINT FK_inventories_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id),
    CONSTRAINT FK_inventories_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT CK_inventories_stock_quantity_ge0 CHECK (stock_quantity >= 0),
    CONSTRAINT CK_inventories_reserved_quantity_ge0 CHECK (reserved_quantity >= 0)
);
GO
CREATE INDEX IX_inventories_warehouse_id ON inventories(warehouse_id);
GO
-- ---- [M04 - Kho & Nhập hàng] inventory_logs ----
-- Nhật ký xuất nhập tồn
CREATE TABLE inventory_logs (
    log_id                   BIGINT IDENTITY(1,1) NOT NULL,
    variant_id               INT NOT NULL,
    warehouse_id             INT NULL,
    change_type              VARCHAR(20) NOT NULL,
    quantity_changed         INT NOT NULL,
    reference_type           VARCHAR(30) NULL,
    reference_id             BIGINT NULL,
    reason                   NVARCHAR(255) NULL,
    created_by               INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_inventory_logs_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_inventory_logs PRIMARY KEY (log_id),
    CONSTRAINT FK_inventory_logs_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id),
    CONSTRAINT FK_inventory_logs_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT CK_inventory_logs_change_type CHECK (change_type IN ('RESTOCK', 'SALE', 'RETURN', 'ADJUSTMENT', 'DAMAGE', 'TRANSFER_IN', 'TRANSFER_OUT')),
    CONSTRAINT FK_inventory_logs_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_inventory_logs_variant_id ON inventory_logs(variant_id);
CREATE INDEX IX_inventory_logs_warehouse_id ON inventory_logs(warehouse_id);
CREATE INDEX IX_inventory_logs_created_by ON inventory_logs(created_by);
GO
-- ---- [M04 - Kho & Nhập hàng] suppliers ----
-- Nhà cung cấp / xưởng may
CREATE TABLE suppliers (
    supplier_id              INT IDENTITY(1,1) NOT NULL,
    supplier_code            VARCHAR(20) NOT NULL,
    supplier_name            NVARCHAR(200) NOT NULL,
    contact_person           NVARCHAR(100) NULL,
    phone                    VARCHAR(15) NULL,
    email                    VARCHAR(150) NULL,
    address                  NVARCHAR(255) NULL,
    tax_code                 VARCHAR(20) NULL,
    payment_terms            NVARCHAR(100) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_suppliers_is_active DEFAULT 1,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_suppliers_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_suppliers PRIMARY KEY (supplier_id),
    CONSTRAINT UQ_suppliers_supplier_code UNIQUE (supplier_code)
);
GO
-- ---- [M04 - Kho & Nhập hàng] purchase_orders ----
-- Đơn đặt hàng nhà cung cấp
CREATE TABLE purchase_orders (
    po_id                    INT IDENTITY(1,1) NOT NULL,
    po_code                  VARCHAR(20) NOT NULL,
    supplier_id              INT NOT NULL,
    warehouse_id             INT NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_purchase_orders_status DEFAULT 'DRAFT',
    expected_date            DATE NULL,
    total_amount             DECIMAL(18,0) NOT NULL CONSTRAINT DF_purchase_orders_total_amount DEFAULT 0,
    note                     NVARCHAR(500) NULL,
    created_by               INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_purchase_orders_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_purchase_orders PRIMARY KEY (po_id),
    CONSTRAINT UQ_purchase_orders_po_code UNIQUE (po_code),
    CONSTRAINT FK_purchase_orders_supplier_id FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id),
    CONSTRAINT FK_purchase_orders_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT CK_purchase_orders_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'PARTIAL', 'RECEIVED', 'CANCELLED')),
    CONSTRAINT FK_purchase_orders_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_purchase_orders_supplier_id ON purchase_orders(supplier_id);
CREATE INDEX IX_purchase_orders_warehouse_id ON purchase_orders(warehouse_id);
CREATE INDEX IX_purchase_orders_created_by ON purchase_orders(created_by);
GO
-- ---- [M04 - Kho & Nhập hàng] purchase_order_items ----
-- Chi tiết đơn đặt hàng NCC
CREATE TABLE purchase_order_items (
    po_item_id               INT IDENTITY(1,1) NOT NULL,
    po_id                    INT NOT NULL,
    variant_id               INT NOT NULL,
    quantity                 INT NOT NULL,
    unit_cost                DECIMAL(18,0) NOT NULL,
    received_quantity        INT NOT NULL CONSTRAINT DF_purchase_order_items_received_quantity DEFAULT 0,
    CONSTRAINT PK_purchase_order_items PRIMARY KEY (po_item_id),
    CONSTRAINT FK_purchase_order_items_po_id FOREIGN KEY (po_id) REFERENCES purchase_orders(po_id) ON DELETE CASCADE,
    CONSTRAINT FK_purchase_order_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_purchase_order_items_po_id ON purchase_order_items(po_id);
CREATE INDEX IX_purchase_order_items_variant_id ON purchase_order_items(variant_id);
GO
-- ---- [M04 - Kho & Nhập hàng] goods_receipts ----
-- Phiếu nhập kho
CREATE TABLE goods_receipts (
    receipt_id               INT IDENTITY(1,1) NOT NULL,
    receipt_code             VARCHAR(20) NOT NULL,
    po_id                    INT NULL,
    warehouse_id             INT NOT NULL,
    received_by              INT NULL,
    received_at              DATETIME2(0) NOT NULL CONSTRAINT DF_goods_receipts_received_at DEFAULT GETDATE(),
    note                     NVARCHAR(500) NULL,
    CONSTRAINT PK_goods_receipts PRIMARY KEY (receipt_id),
    CONSTRAINT UQ_goods_receipts_receipt_code UNIQUE (receipt_code),
    CONSTRAINT FK_goods_receipts_po_id FOREIGN KEY (po_id) REFERENCES purchase_orders(po_id),
    CONSTRAINT FK_goods_receipts_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT FK_goods_receipts_received_by FOREIGN KEY (received_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_goods_receipts_po_id ON goods_receipts(po_id);
CREATE INDEX IX_goods_receipts_warehouse_id ON goods_receipts(warehouse_id);
CREATE INDEX IX_goods_receipts_received_by ON goods_receipts(received_by);
GO
-- ---- [M04 - Kho & Nhập hàng] goods_receipt_items ----
-- Chi tiết phiếu nhập kho
CREATE TABLE goods_receipt_items (
    receipt_item_id          INT IDENTITY(1,1) NOT NULL,
    receipt_id               INT NOT NULL,
    variant_id               INT NOT NULL,
    quantity                 INT NOT NULL,
    unit_cost                DECIMAL(18,0) NOT NULL,
    CONSTRAINT PK_goods_receipt_items PRIMARY KEY (receipt_item_id),
    CONSTRAINT FK_goods_receipt_items_receipt_id FOREIGN KEY (receipt_id) REFERENCES goods_receipts(receipt_id) ON DELETE CASCADE,
    CONSTRAINT FK_goods_receipt_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_goods_receipt_items_receipt_id ON goods_receipt_items(receipt_id);
CREATE INDEX IX_goods_receipt_items_variant_id ON goods_receipt_items(variant_id);
GO
-- ---- [M04 - Kho & Nhập hàng] stock_transfers ----
-- Phiếu điều chuyển kho
CREATE TABLE stock_transfers (
    transfer_id              INT IDENTITY(1,1) NOT NULL,
    transfer_code            VARCHAR(20) NOT NULL,
    from_warehouse_id        INT NOT NULL,
    to_warehouse_id          INT NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_stock_transfers_status DEFAULT 'PENDING',
    created_by               INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_stock_transfers_created_at DEFAULT GETDATE(),
    completed_at             DATETIME2(0) NULL,
    CONSTRAINT PK_stock_transfers PRIMARY KEY (transfer_id),
    CONSTRAINT UQ_stock_transfers_transfer_code UNIQUE (transfer_code),
    CONSTRAINT FK_stock_transfers_from_warehouse_id FOREIGN KEY (from_warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT FK_stock_transfers_to_warehouse_id FOREIGN KEY (to_warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT CK_stock_transfers_status CHECK (status IN ('PENDING', 'IN_TRANSIT', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT FK_stock_transfers_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_stock_transfers_from_warehouse_id ON stock_transfers(from_warehouse_id);
CREATE INDEX IX_stock_transfers_to_warehouse_id ON stock_transfers(to_warehouse_id);
CREATE INDEX IX_stock_transfers_created_by ON stock_transfers(created_by);
GO
-- ---- [M04 - Kho & Nhập hàng] stock_transfer_items ----
-- Chi tiết điều chuyển kho
CREATE TABLE stock_transfer_items (
    transfer_item_id         INT IDENTITY(1,1) NOT NULL,
    transfer_id              INT NOT NULL,
    variant_id               INT NOT NULL,
    quantity                 INT NOT NULL,
    CONSTRAINT PK_stock_transfer_items PRIMARY KEY (transfer_item_id),
    CONSTRAINT FK_stock_transfer_items_transfer_id FOREIGN KEY (transfer_id) REFERENCES stock_transfers(transfer_id) ON DELETE CASCADE,
    CONSTRAINT FK_stock_transfer_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_stock_transfer_items_transfer_id ON stock_transfer_items(transfer_id);
CREATE INDEX IX_stock_transfer_items_variant_id ON stock_transfer_items(variant_id);
GO
-- ---- [M04 - Kho & Nhập hàng] stocktakes ----
-- Phiếu kiểm kê kho
CREATE TABLE stocktakes (
    stocktake_id             INT IDENTITY(1,1) NOT NULL,
    stocktake_code           VARCHAR(20) NOT NULL,
    warehouse_id             INT NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_stocktakes_status DEFAULT 'OPEN',
    created_by               INT NULL,
    started_at               DATETIME2(0) NOT NULL CONSTRAINT DF_stocktakes_started_at DEFAULT GETDATE(),
    completed_at             DATETIME2(0) NULL,
    CONSTRAINT PK_stocktakes PRIMARY KEY (stocktake_id),
    CONSTRAINT UQ_stocktakes_stocktake_code UNIQUE (stocktake_code),
    CONSTRAINT FK_stocktakes_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    CONSTRAINT CK_stocktakes_status CHECK (status IN ('OPEN', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT FK_stocktakes_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_stocktakes_warehouse_id ON stocktakes(warehouse_id);
CREATE INDEX IX_stocktakes_created_by ON stocktakes(created_by);
GO
-- ---- [M04 - Kho & Nhập hàng] stocktake_items ----
-- Chi tiết kiểm kê
CREATE TABLE stocktake_items (
    stocktake_item_id        INT IDENTITY(1,1) NOT NULL,
    stocktake_id             INT NOT NULL,
    variant_id               INT NOT NULL,
    system_quantity          INT NOT NULL,
    counted_quantity         INT NOT NULL,
    note                     NVARCHAR(255) NULL,
    CONSTRAINT PK_stocktake_items PRIMARY KEY (stocktake_item_id),
    CONSTRAINT FK_stocktake_items_stocktake_id FOREIGN KEY (stocktake_id) REFERENCES stocktakes(stocktake_id) ON DELETE CASCADE,
    CONSTRAINT FK_stocktake_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_stocktake_items_stocktake_id ON stocktake_items(stocktake_id);
CREATE INDEX IX_stocktake_items_variant_id ON stocktake_items(variant_id);
GO
-- ---- [M09 - Khuyến mãi & Voucher] vouchers ----
-- Mã giảm giá
CREATE TABLE vouchers (
    voucher_id               INT IDENTITY(1,1) NOT NULL,
    voucher_code             VARCHAR(30) NOT NULL,
    voucher_name             NVARCHAR(150) NOT NULL,
    discount_type            VARCHAR(20) NOT NULL,
    discount_value           DECIMAL(18,0) NOT NULL,
    max_discount_amount      DECIMAL(18,0) NULL,
    min_order_value          DECIMAL(18,0) NOT NULL CONSTRAINT DF_vouchers_min_order_value DEFAULT 0,
    usage_limit              INT NULL,
    per_user_limit           INT NOT NULL CONSTRAINT DF_vouchers_per_user_limit DEFAULT 1,
    used_count               INT NOT NULL CONSTRAINT DF_vouchers_used_count DEFAULT 0,
    start_date               DATETIME2(0) NOT NULL,
    end_date                 DATETIME2(0) NOT NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_vouchers_is_active DEFAULT 1,
    CONSTRAINT PK_vouchers PRIMARY KEY (voucher_id),
    CONSTRAINT UQ_vouchers_voucher_code UNIQUE (voucher_code),
    CONSTRAINT CK_vouchers_discount_type CHECK (discount_type IN ('PERCENT', 'FIXED', 'FREESHIP'))
);
GO
-- ---- [M05 - Giỏ hàng & Yêu thích] carts ----
-- Giỏ hàng
CREATE TABLE carts (
    cart_id                  INT IDENTITY(1,1) NOT NULL,
    user_id                  INT NULL,
    session_token            VARCHAR(100) NULL,
    voucher_id               INT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_carts_status DEFAULT 'ACTIVE',
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_carts_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_carts PRIMARY KEY (cart_id),
    CONSTRAINT FK_carts_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_carts_voucher_id FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id),
    CONSTRAINT CK_carts_status CHECK (status IN ('ACTIVE', 'CONVERTED', 'ABANDONED'))
);
GO
CREATE INDEX IX_carts_user_id ON carts(user_id);
CREATE INDEX IX_carts_voucher_id ON carts(voucher_id);
GO
-- ---- [M05 - Giỏ hàng & Yêu thích] cart_items ----
-- Sản phẩm trong giỏ
CREATE TABLE cart_items (
    cart_item_id             INT IDENTITY(1,1) NOT NULL,
    cart_id                  INT NOT NULL,
    variant_id               INT NOT NULL,
    quantity                 INT NOT NULL CONSTRAINT DF_cart_items_quantity DEFAULT 1,
    unit_price               DECIMAL(18,0) NOT NULL,
    added_at                 DATETIME2(0) NOT NULL CONSTRAINT DF_cart_items_added_at DEFAULT GETDATE(),
    CONSTRAINT PK_cart_items PRIMARY KEY (cart_item_id),
    CONSTRAINT UQ_cart_items_cart_id_variant_id UNIQUE (cart_id, variant_id),
    CONSTRAINT FK_cart_items_cart_id FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
    CONSTRAINT FK_cart_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_cart_items_variant_id ON cart_items(variant_id);
GO
-- ---- [M05 - Giỏ hàng & Yêu thích] wishlists ----
-- Sản phẩm yêu thích
CREATE TABLE wishlists (
    wishlist_id              INT IDENTITY(1,1) NOT NULL,
    user_id                  INT NOT NULL,
    product_id               INT NOT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_wishlists_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_wishlists PRIMARY KEY (wishlist_id),
    CONSTRAINT UQ_wishlists_user_id_product_id UNIQUE (user_id, product_id),
    CONSTRAINT FK_wishlists_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_wishlists_product_id FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO
CREATE INDEX IX_wishlists_product_id ON wishlists(product_id);
GO
-- ---- [M14 - Cửa hàng & POS] stores ----
-- Cửa hàng
CREATE TABLE stores (
    store_id                 INT IDENTITY(1,1) NOT NULL,
    store_code               VARCHAR(20) NOT NULL,
    store_name               NVARCHAR(150) NOT NULL,
    address                  NVARCHAR(255) NULL,
    province_id              INT NULL,
    phone                    VARCHAR(15) NULL,
    open_time                TIME(0) NULL,
    close_time               TIME(0) NULL,
    warehouse_id             INT NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_stores_is_active DEFAULT 1,
    CONSTRAINT PK_stores PRIMARY KEY (store_id),
    CONSTRAINT UQ_stores_store_code UNIQUE (store_code),
    CONSTRAINT FK_stores_province_id FOREIGN KEY (province_id) REFERENCES provinces(province_id),
    CONSTRAINT FK_stores_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
);
GO
CREATE INDEX IX_stores_province_id ON stores(province_id);
CREATE INDEX IX_stores_warehouse_id ON stores(warehouse_id);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] orders ----
-- Đơn hàng
CREATE TABLE orders (
    order_id                 BIGINT IDENTITY(1,1) NOT NULL,
    order_code               VARCHAR(20) NOT NULL,
    customer_id              INT NULL,
    user_id                  INT NULL,
    store_id                 INT NULL,
    channel                  VARCHAR(20) NOT NULL CONSTRAINT DF_orders_channel DEFAULT 'WEB',
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_orders_status DEFAULT 'PENDING',
    payment_status           VARCHAR(20) NOT NULL CONSTRAINT DF_orders_payment_status DEFAULT 'UNPAID',
    receiver_name            NVARCHAR(150) NULL,
    receiver_phone           VARCHAR(15) NULL,
    shipping_address         NVARCHAR(300) NULL,
    province_id              INT NULL,
    ward_id                  INT NULL,
    subtotal                 DECIMAL(18,0) NOT NULL CONSTRAINT DF_orders_subtotal DEFAULT 0,
    discount_amount          DECIMAL(18,0) NOT NULL CONSTRAINT DF_orders_discount_amount DEFAULT 0,
    shipping_fee             DECIMAL(18,0) NOT NULL CONSTRAINT DF_orders_shipping_fee DEFAULT 0,
    tax_amount               DECIMAL(18,0) NOT NULL CONSTRAINT DF_orders_tax_amount DEFAULT 0,
    total_amount             DECIMAL(18,0) NOT NULL CONSTRAINT DF_orders_total_amount DEFAULT 0,
    voucher_id               INT NULL,
    handled_by               INT NULL,
    note                     NVARCHAR(500) NULL,
    ordered_at               DATETIME2(0) NOT NULL CONSTRAINT DF_orders_ordered_at DEFAULT GETDATE(),
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_orders_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_orders PRIMARY KEY (order_id),
    CONSTRAINT UQ_orders_order_code UNIQUE (order_code),
    CONSTRAINT FK_orders_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT FK_orders_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_orders_store_id FOREIGN KEY (store_id) REFERENCES stores(store_id),
    CONSTRAINT CK_orders_channel CHECK (channel IN ('WEB', 'POS', 'APP', 'PHONE')),
    CONSTRAINT CK_orders_status CHECK (status IN ('PENDING', 'CONFIRMED', 'PACKING', 'SHIPPING', 'DELIVERED', 'COMPLETED', 'CANCELLED', 'RETURNED')),
    CONSTRAINT CK_orders_payment_status CHECK (payment_status IN ('UNPAID', 'PAID', 'PARTIAL', 'REFUNDED')),
    CONSTRAINT FK_orders_province_id FOREIGN KEY (province_id) REFERENCES provinces(province_id),
    CONSTRAINT FK_orders_ward_id FOREIGN KEY (ward_id) REFERENCES wards(ward_id),
    CONSTRAINT FK_orders_voucher_id FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id),
    CONSTRAINT FK_orders_handled_by FOREIGN KEY (handled_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_orders_customer_id ON orders(customer_id);
CREATE INDEX IX_orders_user_id ON orders(user_id);
CREATE INDEX IX_orders_store_id ON orders(store_id);
CREATE INDEX IX_orders_province_id ON orders(province_id);
CREATE INDEX IX_orders_ward_id ON orders(ward_id);
CREATE INDEX IX_orders_voucher_id ON orders(voucher_id);
CREATE INDEX IX_orders_handled_by ON orders(handled_by);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] order_items ----
-- Chi tiết đơn hàng
CREATE TABLE order_items (
    order_item_id            BIGINT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    variant_id               INT NOT NULL,
    product_name             NVARCHAR(200) NOT NULL,
    sku                      VARCHAR(50) NOT NULL,
    quantity                 INT NOT NULL,
    unit_price               DECIMAL(18,0) NOT NULL,
    discount_amount          DECIMAL(18,0) NOT NULL CONSTRAINT DF_order_items_discount_amount DEFAULT 0,
    line_total               DECIMAL(18,0) NOT NULL,
    CONSTRAINT PK_order_items PRIMARY KEY (order_item_id),
    CONSTRAINT FK_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_order_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_order_items_order_id ON order_items(order_id);
CREATE INDEX IX_order_items_variant_id ON order_items(variant_id);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] order_status_histories ----
-- Lịch sử trạng thái đơn hàng
CREATE TABLE order_status_histories (
    history_id               BIGINT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    old_status               VARCHAR(20) NULL,
    new_status               VARCHAR(20) NOT NULL,
    changed_by               INT NULL,
    note                     NVARCHAR(255) NULL,
    changed_at               DATETIME2(0) NOT NULL CONSTRAINT DF_order_status_histories_changed_at DEFAULT GETDATE(),
    CONSTRAINT PK_order_status_histories PRIMARY KEY (history_id),
    CONSTRAINT FK_order_status_histories_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_order_status_histories_changed_by FOREIGN KEY (changed_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_order_status_histories_order_id ON order_status_histories(order_id);
CREATE INDEX IX_order_status_histories_changed_by ON order_status_histories(changed_by);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] order_notes ----
-- Ghi chú nội bộ của đơn hàng
CREATE TABLE order_notes (
    order_note_id            INT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    user_id                  INT NULL,
    note_content             NVARCHAR(1000) NOT NULL,
    is_internal              BIT NOT NULL CONSTRAINT DF_order_notes_is_internal DEFAULT 1,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_order_notes_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_order_notes PRIMARY KEY (order_note_id),
    CONSTRAINT FK_order_notes_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_order_notes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_order_notes_order_id ON order_notes(order_id);
CREATE INDEX IX_order_notes_user_id ON order_notes(user_id);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] order_cancellations ----
-- Thông tin hủy đơn
CREATE TABLE order_cancellations (
    cancellation_id          INT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    reason_code              VARCHAR(30) NOT NULL,
    reason_detail            NVARCHAR(500) NULL,
    cancelled_by             INT NULL,
    cancelled_at             DATETIME2(0) NOT NULL CONSTRAINT DF_order_cancellations_cancelled_at DEFAULT GETDATE(),
    CONSTRAINT PK_order_cancellations PRIMARY KEY (cancellation_id),
    CONSTRAINT UQ_order_cancellations_order_id UNIQUE (order_id),
    CONSTRAINT FK_order_cancellations_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_order_cancellations_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_order_cancellations_order_id ON order_cancellations(order_id);
CREATE INDEX IX_order_cancellations_cancelled_by ON order_cancellations(cancelled_by);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] return_requests ----
-- Yêu cầu đổi / trả hàng
CREATE TABLE return_requests (
    return_id                INT IDENTITY(1,1) NOT NULL,
    return_code              VARCHAR(20) NOT NULL,
    order_id                 BIGINT NOT NULL,
    customer_id              INT NULL,
    request_type             VARCHAR(20) NOT NULL,
    reason                   NVARCHAR(500) NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_return_requests_status DEFAULT 'PENDING',
    handled_by               INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_return_requests_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_return_requests PRIMARY KEY (return_id),
    CONSTRAINT UQ_return_requests_return_code UNIQUE (return_code),
    CONSTRAINT FK_return_requests_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_return_requests_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT CK_return_requests_request_type CHECK (request_type IN ('RETURN', 'EXCHANGE')),
    CONSTRAINT CK_return_requests_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'RECEIVED', 'REFUNDED', 'COMPLETED')),
    CONSTRAINT FK_return_requests_handled_by FOREIGN KEY (handled_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_return_requests_order_id ON return_requests(order_id);
CREATE INDEX IX_return_requests_customer_id ON return_requests(customer_id);
CREATE INDEX IX_return_requests_handled_by ON return_requests(handled_by);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] return_request_items ----
-- Chi tiết hàng đổi / trả
CREATE TABLE return_request_items (
    return_item_id           INT IDENTITY(1,1) NOT NULL,
    return_id                INT NOT NULL,
    order_item_id            BIGINT NOT NULL,
    quantity                 INT NOT NULL,
    exchange_variant_id      INT NULL,
    condition_note           NVARCHAR(300) NULL,
    CONSTRAINT PK_return_request_items PRIMARY KEY (return_item_id),
    CONSTRAINT FK_return_request_items_return_id FOREIGN KEY (return_id) REFERENCES return_requests(return_id) ON DELETE CASCADE,
    CONSTRAINT FK_return_request_items_order_item_id FOREIGN KEY (order_item_id) REFERENCES order_items(order_item_id),
    CONSTRAINT FK_return_request_items_exchange_variant_id FOREIGN KEY (exchange_variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_return_request_items_return_id ON return_request_items(return_id);
CREATE INDEX IX_return_request_items_order_item_id ON return_request_items(order_item_id);
CREATE INDEX IX_return_request_items_exchange_variant_id ON return_request_items(exchange_variant_id);
GO
-- ---- [M07 - Thanh toán] payment_methods ----
-- Phương thức thanh toán
CREATE TABLE payment_methods (
    method_id                INT IDENTITY(1,1) NOT NULL,
    method_code              VARCHAR(20) NOT NULL,
    method_name              NVARCHAR(100) NOT NULL,
    is_online                BIT NOT NULL CONSTRAINT DF_payment_methods_is_online DEFAULT 0,
    display_order            INT NOT NULL CONSTRAINT DF_payment_methods_display_order DEFAULT 0,
    is_active                BIT NOT NULL CONSTRAINT DF_payment_methods_is_active DEFAULT 1,
    CONSTRAINT PK_payment_methods PRIMARY KEY (method_id),
    CONSTRAINT UQ_payment_methods_method_code UNIQUE (method_code)
);
GO
-- ---- [M07 - Thanh toán] payments ----
-- Thanh toán của đơn hàng
CREATE TABLE payments (
    payment_id               BIGINT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    method_id                INT NOT NULL,
    amount                   DECIMAL(18,0) NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_payments_status DEFAULT 'PENDING',
    transaction_ref          VARCHAR(60) NULL,
    paid_at                  DATETIME2(0) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_payments_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_payments PRIMARY KEY (payment_id),
    CONSTRAINT FK_payments_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_payments_method_id FOREIGN KEY (method_id) REFERENCES payment_methods(method_id),
    CONSTRAINT CK_payments_status CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED'))
);
GO
CREATE INDEX IX_payments_order_id ON payments(order_id);
CREATE INDEX IX_payments_method_id ON payments(method_id);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] refunds ----
-- Hoàn tiền
CREATE TABLE refunds (
    refund_id                INT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    return_id                INT NULL,
    payment_id               BIGINT NULL,
    amount                   DECIMAL(18,0) NOT NULL,
    refund_method            VARCHAR(20) NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_refunds_status DEFAULT 'PENDING',
    processed_at             DATETIME2(0) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_refunds_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_refunds PRIMARY KEY (refund_id),
    CONSTRAINT FK_refunds_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_refunds_return_id FOREIGN KEY (return_id) REFERENCES return_requests(return_id),
    CONSTRAINT FK_refunds_payment_id FOREIGN KEY (payment_id) REFERENCES payments(payment_id),
    CONSTRAINT CK_refunds_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED'))
);
GO
CREATE INDEX IX_refunds_order_id ON refunds(order_id);
CREATE INDEX IX_refunds_return_id ON refunds(return_id);
CREATE INDEX IX_refunds_payment_id ON refunds(payment_id);
GO
-- ---- [M06 - Đơn hàng & Đổi trả] invoices ----
-- Hóa đơn bán hàng
CREATE TABLE invoices (
    invoice_id               INT IDENTITY(1,1) NOT NULL,
    invoice_number           VARCHAR(30) NOT NULL,
    order_id                 BIGINT NOT NULL,
    buyer_name               NVARCHAR(150) NULL,
    company_name             NVARCHAR(200) NULL,
    tax_code                 VARCHAR(20) NULL,
    billing_address          NVARCHAR(300) NULL,
    total_amount             DECIMAL(18,0) NOT NULL,
    tax_amount               DECIMAL(18,0) NOT NULL CONSTRAINT DF_invoices_tax_amount DEFAULT 0,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_invoices_status DEFAULT 'ISSUED',
    issued_at                DATETIME2(0) NOT NULL CONSTRAINT DF_invoices_issued_at DEFAULT GETDATE(),
    CONSTRAINT PK_invoices PRIMARY KEY (invoice_id),
    CONSTRAINT UQ_invoices_invoice_number UNIQUE (invoice_number),
    CONSTRAINT FK_invoices_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT CK_invoices_status CHECK (status IN ('ISSUED', 'CANCELLED'))
);
GO
CREATE INDEX IX_invoices_order_id ON invoices(order_id);
GO
-- ---- [M07 - Thanh toán] payment_transactions ----
-- Giao dịch cổng thanh toán (VNPay/MoMo)
CREATE TABLE payment_transactions (
    txn_id                   BIGINT IDENTITY(1,1) NOT NULL,
    payment_id               BIGINT NOT NULL,
    gateway                  VARCHAR(20) NOT NULL,
    gateway_txn_no           VARCHAR(60) NULL,
    response_code            VARCHAR(10) NULL,
    amount                   DECIMAL(18,0) NOT NULL,
    raw_response             NVARCHAR(MAX) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_payment_transactions_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_payment_transactions PRIMARY KEY (txn_id),
    CONSTRAINT FK_payment_transactions_payment_id FOREIGN KEY (payment_id) REFERENCES payments(payment_id),
    CONSTRAINT CK_payment_transactions_gateway CHECK (gateway IN ('VNPAY', 'MOMO', 'BANK', 'COD'))
);
GO
CREATE INDEX IX_payment_transactions_payment_id ON payment_transactions(payment_id);
GO
-- ---- [M08 - Vận chuyển & Địa lý] shipping_carriers ----
-- Đơn vị vận chuyển
CREATE TABLE shipping_carriers (
    carrier_id               INT IDENTITY(1,1) NOT NULL,
    carrier_code             VARCHAR(20) NOT NULL,
    carrier_name             NVARCHAR(100) NOT NULL,
    api_endpoint             VARCHAR(300) NULL,
    phone                    VARCHAR(15) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_shipping_carriers_is_active DEFAULT 1,
    CONSTRAINT PK_shipping_carriers PRIMARY KEY (carrier_id),
    CONSTRAINT UQ_shipping_carriers_carrier_code UNIQUE (carrier_code)
);
GO
-- ---- [M07 - Thanh toán] cod_reconciliations ----
-- Đối soát COD với đơn vị vận chuyển
CREATE TABLE cod_reconciliations (
    reconciliation_id        INT IDENTITY(1,1) NOT NULL,
    carrier_id               INT NOT NULL,
    period_from              DATE NOT NULL,
    period_to                DATE NOT NULL,
    total_cod                DECIMAL(18,0) NOT NULL CONSTRAINT DF_cod_reconciliations_total_cod DEFAULT 0,
    total_fee                DECIMAL(18,0) NOT NULL CONSTRAINT DF_cod_reconciliations_total_fee DEFAULT 0,
    net_amount               DECIMAL(18,0) NOT NULL CONSTRAINT DF_cod_reconciliations_net_amount DEFAULT 0,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_cod_reconciliations_status DEFAULT 'PENDING',
    reconciled_at            DATETIME2(0) NULL,
    created_by               INT NULL,
    CONSTRAINT PK_cod_reconciliations PRIMARY KEY (reconciliation_id),
    CONSTRAINT FK_cod_reconciliations_carrier_id FOREIGN KEY (carrier_id) REFERENCES shipping_carriers(carrier_id),
    CONSTRAINT CK_cod_reconciliations_status CHECK (status IN ('PENDING', 'RECONCILED')),
    CONSTRAINT FK_cod_reconciliations_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_cod_reconciliations_carrier_id ON cod_reconciliations(carrier_id);
CREATE INDEX IX_cod_reconciliations_created_by ON cod_reconciliations(created_by);
GO
-- ---- [M08 - Vận chuyển & Địa lý] shipping_methods ----
-- Dịch vụ vận chuyển
CREATE TABLE shipping_methods (
    shipping_method_id       INT IDENTITY(1,1) NOT NULL,
    carrier_id               INT NOT NULL,
    method_name              NVARCHAR(100) NOT NULL,
    estimated_days_min       INT NULL,
    estimated_days_max       INT NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_shipping_methods_is_active DEFAULT 1,
    CONSTRAINT PK_shipping_methods PRIMARY KEY (shipping_method_id),
    CONSTRAINT FK_shipping_methods_carrier_id FOREIGN KEY (carrier_id) REFERENCES shipping_carriers(carrier_id)
);
GO
CREATE INDEX IX_shipping_methods_carrier_id ON shipping_methods(carrier_id);
GO
-- ---- [M08 - Vận chuyển & Địa lý] shipping_fees ----
-- Bảng phí vận chuyển
CREATE TABLE shipping_fees (
    fee_id                   INT IDENTITY(1,1) NOT NULL,
    shipping_method_id       INT NOT NULL,
    zone_id                  INT NOT NULL,
    min_weight_gram          INT NOT NULL CONSTRAINT DF_shipping_fees_min_weight_gram DEFAULT 0,
    max_weight_gram          INT NULL,
    base_fee                 DECIMAL(18,0) NOT NULL,
    free_ship_threshold      DECIMAL(18,0) NULL,
    CONSTRAINT PK_shipping_fees PRIMARY KEY (fee_id),
    CONSTRAINT FK_shipping_fees_shipping_method_id FOREIGN KEY (shipping_method_id) REFERENCES shipping_methods(shipping_method_id),
    CONSTRAINT FK_shipping_fees_zone_id FOREIGN KEY (zone_id) REFERENCES shipping_zones(zone_id)
);
GO
CREATE INDEX IX_shipping_fees_shipping_method_id ON shipping_fees(shipping_method_id);
CREATE INDEX IX_shipping_fees_zone_id ON shipping_fees(zone_id);
GO
-- ---- [M08 - Vận chuyển & Địa lý] shipments ----
-- Vận đơn
CREATE TABLE shipments (
    shipment_id              BIGINT IDENTITY(1,1) NOT NULL,
    order_id                 BIGINT NOT NULL,
    carrier_id               INT NOT NULL,
    shipping_method_id       INT NULL,
    tracking_code            VARCHAR(60) NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_shipments_status DEFAULT 'CREATED',
    shipping_fee             DECIMAL(18,0) NOT NULL CONSTRAINT DF_shipments_shipping_fee DEFAULT 0,
    cod_amount               DECIMAL(18,0) NOT NULL CONSTRAINT DF_shipments_cod_amount DEFAULT 0,
    weight_gram              INT NULL,
    shipped_at               DATETIME2(0) NULL,
    delivered_at             DATETIME2(0) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_shipments_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_shipments PRIMARY KEY (shipment_id),
    CONSTRAINT FK_shipments_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_shipments_carrier_id FOREIGN KEY (carrier_id) REFERENCES shipping_carriers(carrier_id),
    CONSTRAINT FK_shipments_shipping_method_id FOREIGN KEY (shipping_method_id) REFERENCES shipping_methods(shipping_method_id),
    CONSTRAINT CK_shipments_status CHECK (status IN ('CREATED', 'PICKED_UP', 'IN_TRANSIT', 'DELIVERED', 'FAILED', 'RETURNED'))
);
GO
CREATE INDEX IX_shipments_order_id ON shipments(order_id);
CREATE INDEX IX_shipments_carrier_id ON shipments(carrier_id);
CREATE INDEX IX_shipments_shipping_method_id ON shipments(shipping_method_id);
CREATE UNIQUE INDEX UX_shipments_tracking_code ON shipments(tracking_code) WHERE tracking_code IS NOT NULL;
GO
-- ---- [M08 - Vận chuyển & Địa lý] shipment_tracking_events ----
-- Hành trình vận đơn
CREATE TABLE shipment_tracking_events (
    event_id                 BIGINT IDENTITY(1,1) NOT NULL,
    shipment_id              BIGINT NOT NULL,
    event_status             VARCHAR(30) NOT NULL,
    event_location           NVARCHAR(200) NULL,
    event_note               NVARCHAR(300) NULL,
    event_time               DATETIME2(0) NOT NULL CONSTRAINT DF_shipment_tracking_events_event_time DEFAULT GETDATE(),
    CONSTRAINT PK_shipment_tracking_events PRIMARY KEY (event_id),
    CONSTRAINT FK_shipment_tracking_events_shipment_id FOREIGN KEY (shipment_id) REFERENCES shipments(shipment_id) ON DELETE CASCADE
);
GO
CREATE INDEX IX_shipment_tracking_events_shipment_id ON shipment_tracking_events(shipment_id);
GO
-- ---- [M09 - Khuyến mãi & Voucher] voucher_usages ----
-- Lịch sử dùng voucher
CREATE TABLE voucher_usages (
    usage_id                 INT IDENTITY(1,1) NOT NULL,
    voucher_id               INT NOT NULL,
    user_id                  INT NULL,
    order_id                 BIGINT NOT NULL,
    discount_amount          DECIMAL(18,0) NOT NULL,
    used_at                  DATETIME2(0) NOT NULL CONSTRAINT DF_voucher_usages_used_at DEFAULT GETDATE(),
    CONSTRAINT PK_voucher_usages PRIMARY KEY (usage_id),
    CONSTRAINT FK_voucher_usages_voucher_id FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id),
    CONSTRAINT FK_voucher_usages_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_voucher_usages_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
GO
CREATE INDEX IX_voucher_usages_voucher_id ON voucher_usages(voucher_id);
CREATE INDEX IX_voucher_usages_user_id ON voucher_usages(user_id);
CREATE INDEX IX_voucher_usages_order_id ON voucher_usages(order_id);
GO
-- ---- [M09 - Khuyến mãi & Voucher] promotions ----
-- Chương trình khuyến mãi
CREATE TABLE promotions (
    promotion_id             INT IDENTITY(1,1) NOT NULL,
    promotion_name           NVARCHAR(150) NOT NULL,
    promo_type               VARCHAR(20) NOT NULL,
    discount_value           DECIMAL(18,0) NOT NULL,
    start_date               DATETIME2(0) NOT NULL,
    end_date                 DATETIME2(0) NOT NULL,
    banner_url               VARCHAR(500) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_promotions_is_active DEFAULT 1,
    CONSTRAINT PK_promotions PRIMARY KEY (promotion_id),
    CONSTRAINT CK_promotions_promo_type CHECK (promo_type IN ('PERCENT', 'FIXED'))
);
GO
-- ---- [M09 - Khuyến mãi & Voucher] promotion_products ----
-- Sản phẩm áp dụng khuyến mãi
CREATE TABLE promotion_products (
    promotion_product_id     INT IDENTITY(1,1) NOT NULL,
    promotion_id             INT NOT NULL,
    product_id               INT NOT NULL,
    CONSTRAINT PK_promotion_products PRIMARY KEY (promotion_product_id),
    CONSTRAINT UQ_promotion_products_promotion_id_product_id UNIQUE (promotion_id, product_id),
    CONSTRAINT FK_promotion_products_promotion_id FOREIGN KEY (promotion_id) REFERENCES promotions(promotion_id) ON DELETE CASCADE,
    CONSTRAINT FK_promotion_products_product_id FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO
CREATE INDEX IX_promotion_products_product_id ON promotion_products(product_id);
GO
-- ---- [M09 - Khuyến mãi & Voucher] flash_sales ----
-- Flash sale
CREATE TABLE flash_sales (
    flash_sale_id            INT IDENTITY(1,1) NOT NULL,
    title                    NVARCHAR(150) NOT NULL,
    start_time               DATETIME2(0) NOT NULL,
    end_time                 DATETIME2(0) NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_flash_sales_status DEFAULT 'SCHEDULED',
    CONSTRAINT PK_flash_sales PRIMARY KEY (flash_sale_id),
    CONSTRAINT CK_flash_sales_status CHECK (status IN ('SCHEDULED', 'RUNNING', 'ENDED', 'CANCELLED'))
);
GO
-- ---- [M09 - Khuyến mãi & Voucher] flash_sale_items ----
-- Sản phẩm trong flash sale
CREATE TABLE flash_sale_items (
    flash_item_id            INT IDENTITY(1,1) NOT NULL,
    flash_sale_id            INT NOT NULL,
    variant_id               INT NOT NULL,
    sale_price               DECIMAL(18,0) NOT NULL,
    quantity_limit           INT NOT NULL,
    sold_quantity            INT NOT NULL CONSTRAINT DF_flash_sale_items_sold_quantity DEFAULT 0,
    CONSTRAINT PK_flash_sale_items PRIMARY KEY (flash_item_id),
    CONSTRAINT FK_flash_sale_items_flash_sale_id FOREIGN KEY (flash_sale_id) REFERENCES flash_sales(flash_sale_id) ON DELETE CASCADE,
    CONSTRAINT FK_flash_sale_items_variant_id FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
GO
CREATE INDEX IX_flash_sale_items_flash_sale_id ON flash_sale_items(flash_sale_id);
CREATE INDEX IX_flash_sale_items_variant_id ON flash_sale_items(variant_id);
GO
-- ---- [M10 - Tích điểm & Đổi quà] loyalty_accounts ----
-- Tài khoản tích điểm
CREATE TABLE loyalty_accounts (
    account_id               INT IDENTITY(1,1) NOT NULL,
    customer_id              INT NOT NULL,
    points_balance           INT NOT NULL CONSTRAINT DF_loyalty_accounts_points_balance DEFAULT 0,
    lifetime_points          INT NOT NULL CONSTRAINT DF_loyalty_accounts_lifetime_points DEFAULT 0,
    updated_at               DATETIME2(0) NOT NULL CONSTRAINT DF_loyalty_accounts_updated_at DEFAULT GETDATE(),
    CONSTRAINT PK_loyalty_accounts PRIMARY KEY (account_id),
    CONSTRAINT UQ_loyalty_accounts_customer_id UNIQUE (customer_id),
    CONSTRAINT FK_loyalty_accounts_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
GO
CREATE INDEX IX_loyalty_accounts_customer_id ON loyalty_accounts(customer_id);
GO
-- ---- [M10 - Tích điểm & Đổi quà] loyalty_transactions ----
-- Giao dịch điểm thưởng
CREATE TABLE loyalty_transactions (
    loyalty_txn_id           INT IDENTITY(1,1) NOT NULL,
    account_id               INT NOT NULL,
    order_id                 BIGINT NULL,
    points                   INT NOT NULL,
    txn_type                 VARCHAR(20) NOT NULL,
    description              NVARCHAR(255) NULL,
    expires_at               DATETIME2(0) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_loyalty_transactions_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_loyalty_transactions PRIMARY KEY (loyalty_txn_id),
    CONSTRAINT FK_loyalty_transactions_account_id FOREIGN KEY (account_id) REFERENCES loyalty_accounts(account_id),
    CONSTRAINT FK_loyalty_transactions_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT CK_loyalty_transactions_txn_type CHECK (txn_type IN ('EARN', 'REDEEM', 'EXPIRE', 'ADJUST'))
);
GO
CREATE INDEX IX_loyalty_transactions_account_id ON loyalty_transactions(account_id);
CREATE INDEX IX_loyalty_transactions_order_id ON loyalty_transactions(order_id);
GO
-- ---- [M10 - Tích điểm & Đổi quà] loyalty_rewards ----
-- Quà đổi điểm
CREATE TABLE loyalty_rewards (
    reward_id                INT IDENTITY(1,1) NOT NULL,
    reward_name              NVARCHAR(150) NOT NULL,
    points_required          INT NOT NULL,
    voucher_id               INT NULL,
    stock_quantity           INT NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_loyalty_rewards_is_active DEFAULT 1,
    CONSTRAINT PK_loyalty_rewards PRIMARY KEY (reward_id),
    CONSTRAINT FK_loyalty_rewards_voucher_id FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id)
);
GO
CREATE INDEX IX_loyalty_rewards_voucher_id ON loyalty_rewards(voucher_id);
GO
-- ---- [M10 - Tích điểm & Đổi quà] reward_redemptions ----
-- Lịch sử đổi quà
CREATE TABLE reward_redemptions (
    redemption_id            INT IDENTITY(1,1) NOT NULL,
    account_id               INT NOT NULL,
    reward_id                INT NOT NULL,
    points_used              INT NOT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_reward_redemptions_status DEFAULT 'COMPLETED',
    redeemed_at              DATETIME2(0) NOT NULL CONSTRAINT DF_reward_redemptions_redeemed_at DEFAULT GETDATE(),
    CONSTRAINT PK_reward_redemptions PRIMARY KEY (redemption_id),
    CONSTRAINT FK_reward_redemptions_account_id FOREIGN KEY (account_id) REFERENCES loyalty_accounts(account_id),
    CONSTRAINT FK_reward_redemptions_reward_id FOREIGN KEY (reward_id) REFERENCES loyalty_rewards(reward_id),
    CONSTRAINT CK_reward_redemptions_status CHECK (status IN ('COMPLETED', 'CANCELLED'))
);
GO
CREATE INDEX IX_reward_redemptions_account_id ON reward_redemptions(account_id);
CREATE INDEX IX_reward_redemptions_reward_id ON reward_redemptions(reward_id);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] product_reviews ----
-- Đánh giá sản phẩm
CREATE TABLE product_reviews (
    review_id                INT IDENTITY(1,1) NOT NULL,
    product_id               INT NOT NULL,
    user_id                  INT NOT NULL,
    order_item_id            BIGINT NULL,
    rating                   INT NOT NULL,
    title                    NVARCHAR(150) NULL,
    content                  NVARCHAR(2000) NULL,
    fit_feedback             VARCHAR(20) NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_product_reviews_status DEFAULT 'PENDING',
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_product_reviews_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_product_reviews PRIMARY KEY (review_id),
    CONSTRAINT FK_product_reviews_product_id FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT FK_product_reviews_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_product_reviews_order_item_id FOREIGN KEY (order_item_id) REFERENCES order_items(order_item_id),
    CONSTRAINT CK_product_reviews_rating_rng CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT CK_product_reviews_fit_feedback CHECK (fit_feedback IN ('TOO_SMALL', 'TRUE_TO_SIZE', 'TOO_LARGE')),
    CONSTRAINT CK_product_reviews_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'))
);
GO
CREATE INDEX IX_product_reviews_product_id ON product_reviews(product_id);
CREATE INDEX IX_product_reviews_user_id ON product_reviews(user_id);
CREATE INDEX IX_product_reviews_order_item_id ON product_reviews(order_item_id);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] review_media ----
-- Ảnh đánh giá
CREATE TABLE review_media (
    review_media_id          INT IDENTITY(1,1) NOT NULL,
    review_id                INT NOT NULL,
    media_url                VARCHAR(500) NOT NULL,
    media_type               VARCHAR(10) NOT NULL CONSTRAINT DF_review_media_media_type DEFAULT 'IMAGE',
    CONSTRAINT PK_review_media PRIMARY KEY (review_media_id),
    CONSTRAINT FK_review_media_review_id FOREIGN KEY (review_id) REFERENCES product_reviews(review_id) ON DELETE CASCADE
);
GO
CREATE INDEX IX_review_media_review_id ON review_media(review_id);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] review_replies ----
-- Phản hồi đánh giá của shop
CREATE TABLE review_replies (
    reply_id                 INT IDENTITY(1,1) NOT NULL,
    review_id                INT NOT NULL,
    user_id                  INT NOT NULL,
    content                  NVARCHAR(1000) NOT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_review_replies_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_review_replies PRIMARY KEY (reply_id),
    CONSTRAINT FK_review_replies_review_id FOREIGN KEY (review_id) REFERENCES product_reviews(review_id),
    CONSTRAINT FK_review_replies_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_review_replies_review_id ON review_replies(review_id);
CREATE INDEX IX_review_replies_user_id ON review_replies(user_id);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] ticket_categories ----
-- Phân loại yêu cầu hỗ trợ
CREATE TABLE ticket_categories (
    ticket_category_id       INT IDENTITY(1,1) NOT NULL,
    category_name            NVARCHAR(100) NOT NULL,
    CONSTRAINT PK_ticket_categories PRIMARY KEY (ticket_category_id)
);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] support_tickets ----
-- Yêu cầu hỗ trợ khách hàng
CREATE TABLE support_tickets (
    ticket_id                INT IDENTITY(1,1) NOT NULL,
    ticket_code              VARCHAR(20) NOT NULL,
    customer_id              INT NULL,
    order_id                 BIGINT NULL,
    ticket_category_id       INT NULL,
    subject                  NVARCHAR(200) NOT NULL,
    priority                 VARCHAR(10) NOT NULL CONSTRAINT DF_support_tickets_priority DEFAULT 'NORMAL',
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_support_tickets_status DEFAULT 'OPEN',
    assigned_to              INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_support_tickets_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_support_tickets PRIMARY KEY (ticket_id),
    CONSTRAINT UQ_support_tickets_ticket_code UNIQUE (ticket_code),
    CONSTRAINT FK_support_tickets_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT FK_support_tickets_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT FK_support_tickets_ticket_category_id FOREIGN KEY (ticket_category_id) REFERENCES ticket_categories(ticket_category_id),
    CONSTRAINT CK_support_tickets_priority CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    CONSTRAINT CK_support_tickets_status CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    CONSTRAINT FK_support_tickets_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_support_tickets_customer_id ON support_tickets(customer_id);
CREATE INDEX IX_support_tickets_order_id ON support_tickets(order_id);
CREATE INDEX IX_support_tickets_ticket_category_id ON support_tickets(ticket_category_id);
CREATE INDEX IX_support_tickets_assigned_to ON support_tickets(assigned_to);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] support_ticket_messages ----
-- Tin nhắn trong ticket
CREATE TABLE support_ticket_messages (
    message_id               INT IDENTITY(1,1) NOT NULL,
    ticket_id                INT NOT NULL,
    sender_user_id           INT NULL,
    message_content          NVARCHAR(2000) NOT NULL,
    is_staff                 BIT NOT NULL CONSTRAINT DF_support_ticket_messages_is_staff DEFAULT 0,
    attachment_url           VARCHAR(500) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_support_ticket_messages_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_support_ticket_messages PRIMARY KEY (message_id),
    CONSTRAINT FK_support_ticket_messages_ticket_id FOREIGN KEY (ticket_id) REFERENCES support_tickets(ticket_id) ON DELETE CASCADE,
    CONSTRAINT FK_support_ticket_messages_sender_user_id FOREIGN KEY (sender_user_id) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_support_ticket_messages_ticket_id ON support_ticket_messages(ticket_id);
CREATE INDEX IX_support_ticket_messages_sender_user_id ON support_ticket_messages(sender_user_id);
GO
-- ---- [M11 - Đánh giá & Hỗ trợ] faqs ----
-- Câu hỏi thường gặp
CREATE TABLE faqs (
    faq_id                   INT IDENTITY(1,1) NOT NULL,
    faq_group                NVARCHAR(100) NULL,
    question                 NVARCHAR(300) NOT NULL,
    answer                   NVARCHAR(MAX) NOT NULL,
    display_order            INT NOT NULL CONSTRAINT DF_faqs_display_order DEFAULT 0,
    is_active                BIT NOT NULL CONSTRAINT DF_faqs_is_active DEFAULT 1,
    CONSTRAINT PK_faqs PRIMARY KEY (faq_id)
);
GO
-- ---- [M12 - Nội dung & Marketing web] banners ----
-- Banner quảng cáo
CREATE TABLE banners (
    banner_id                INT IDENTITY(1,1) NOT NULL,
    title                    NVARCHAR(150) NOT NULL,
    image_url                VARCHAR(500) NOT NULL,
    link_url                 VARCHAR(500) NULL,
    banner_position          VARCHAR(30) NOT NULL CONSTRAINT DF_banners_banner_position DEFAULT 'HOME_HERO',
    display_order            INT NOT NULL CONSTRAINT DF_banners_display_order DEFAULT 0,
    start_date               DATETIME2(0) NULL,
    end_date                 DATETIME2(0) NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_banners_is_active DEFAULT 1,
    CONSTRAINT PK_banners PRIMARY KEY (banner_id),
    CONSTRAINT CK_banners_banner_position CHECK (banner_position IN ('HOME_HERO', 'HOME_MID', 'SIDEBAR', 'POPUP'))
);
GO
-- ---- [M12 - Nội dung & Marketing web] blog_categories ----
-- Chuyên mục bài viết
CREATE TABLE blog_categories (
    blog_category_id         INT IDENTITY(1,1) NOT NULL,
    category_name            NVARCHAR(100) NOT NULL,
    slug                     VARCHAR(120) NOT NULL,
    CONSTRAINT PK_blog_categories PRIMARY KEY (blog_category_id),
    CONSTRAINT UQ_blog_categories_slug UNIQUE (slug)
);
GO
-- ---- [M12 - Nội dung & Marketing web] blog_posts ----
-- Bài viết / tin tức phối đồ
CREATE TABLE blog_posts (
    post_id                  INT IDENTITY(1,1) NOT NULL,
    blog_category_id         INT NULL,
    title                    NVARCHAR(200) NOT NULL,
    slug                     VARCHAR(220) NOT NULL,
    summary                  NVARCHAR(500) NULL,
    content                  NVARCHAR(MAX) NULL,
    thumbnail_url            VARCHAR(500) NULL,
    author_id                INT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_blog_posts_status DEFAULT 'DRAFT',
    published_at             DATETIME2(0) NULL,
    view_count               INT NOT NULL CONSTRAINT DF_blog_posts_view_count DEFAULT 0,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_blog_posts_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_blog_posts PRIMARY KEY (post_id),
    CONSTRAINT UQ_blog_posts_slug UNIQUE (slug),
    CONSTRAINT FK_blog_posts_blog_category_id FOREIGN KEY (blog_category_id) REFERENCES blog_categories(blog_category_id),
    CONSTRAINT FK_blog_posts_author_id FOREIGN KEY (author_id) REFERENCES users(user_id),
    CONSTRAINT CK_blog_posts_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'HIDDEN'))
);
GO
CREATE INDEX IX_blog_posts_blog_category_id ON blog_posts(blog_category_id);
CREATE INDEX IX_blog_posts_author_id ON blog_posts(author_id);
GO
-- ---- [M12 - Nội dung & Marketing web] newsletter_subscribers ----
-- Đăng ký nhận bản tin
CREATE TABLE newsletter_subscribers (
    subscriber_id            INT IDENTITY(1,1) NOT NULL,
    email                    VARCHAR(150) NOT NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_newsletter_subscribers_is_active DEFAULT 1,
    subscribed_at            DATETIME2(0) NOT NULL CONSTRAINT DF_newsletter_subscribers_subscribed_at DEFAULT GETDATE(),
    CONSTRAINT PK_newsletter_subscribers PRIMARY KEY (subscriber_id),
    CONSTRAINT UQ_newsletter_subscribers_email UNIQUE (email)
);
GO
-- ---- [M13 - Thông báo & Email] notification_templates ----
-- Mẫu thông báo
CREATE TABLE notification_templates (
    template_id              INT IDENTITY(1,1) NOT NULL,
    template_code            VARCHAR(50) NOT NULL,
    channel                  VARCHAR(10) NOT NULL,
    subject                  NVARCHAR(200) NULL,
    body                     NVARCHAR(MAX) NOT NULL,
    is_active                BIT NOT NULL CONSTRAINT DF_notification_templates_is_active DEFAULT 1,
    CONSTRAINT PK_notification_templates PRIMARY KEY (template_id),
    CONSTRAINT UQ_notification_templates_template_code UNIQUE (template_code),
    CONSTRAINT CK_notification_templates_channel CHECK (channel IN ('EMAIL', 'SMS', 'PUSH', 'INAPP'))
);
GO
-- ---- [M13 - Thông báo & Email] notifications ----
-- Thông báo trong ứng dụng
CREATE TABLE notifications (
    notification_id          BIGINT IDENTITY(1,1) NOT NULL,
    user_id                  INT NOT NULL,
    template_id              INT NULL,
    title                    NVARCHAR(200) NOT NULL,
    content                  NVARCHAR(1000) NULL,
    is_read                  BIT NOT NULL CONSTRAINT DF_notifications_is_read DEFAULT 0,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_notifications_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_notifications PRIMARY KEY (notification_id),
    CONSTRAINT FK_notifications_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_notifications_template_id FOREIGN KEY (template_id) REFERENCES notification_templates(template_id)
);
GO
CREATE INDEX IX_notifications_user_id ON notifications(user_id);
CREATE INDEX IX_notifications_template_id ON notifications(template_id);
GO
-- ---- [M13 - Thông báo & Email] email_campaigns ----
-- Chiến dịch email marketing
CREATE TABLE email_campaigns (
    campaign_id              INT IDENTITY(1,1) NOT NULL,
    campaign_name            NVARCHAR(150) NOT NULL,
    template_id              INT NOT NULL,
    target_group_id          INT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_email_campaigns_status DEFAULT 'DRAFT',
    scheduled_at             DATETIME2(0) NULL,
    sent_at                  DATETIME2(0) NULL,
    total_recipients         INT NOT NULL CONSTRAINT DF_email_campaigns_total_recipients DEFAULT 0,
    created_by               INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_email_campaigns_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_email_campaigns PRIMARY KEY (campaign_id),
    CONSTRAINT FK_email_campaigns_template_id FOREIGN KEY (template_id) REFERENCES notification_templates(template_id),
    CONSTRAINT FK_email_campaigns_target_group_id FOREIGN KEY (target_group_id) REFERENCES customer_groups(group_id),
    CONSTRAINT CK_email_campaigns_status CHECK (status IN ('DRAFT', 'SCHEDULED', 'SENT', 'CANCELLED')),
    CONSTRAINT FK_email_campaigns_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_email_campaigns_template_id ON email_campaigns(template_id);
CREATE INDEX IX_email_campaigns_target_group_id ON email_campaigns(target_group_id);
CREATE INDEX IX_email_campaigns_created_by ON email_campaigns(created_by);
GO
-- ---- [M13 - Thông báo & Email] message_logs ----
-- Nhật ký gửi email / SMS
CREATE TABLE message_logs (
    message_log_id           BIGINT IDENTITY(1,1) NOT NULL,
    channel                  VARCHAR(10) NOT NULL,
    recipient                VARCHAR(150) NOT NULL,
    subject                  NVARCHAR(200) NULL,
    status                   VARCHAR(20) NOT NULL,
    error_message            NVARCHAR(500) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_message_logs_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_message_logs PRIMARY KEY (message_log_id),
    CONSTRAINT CK_message_logs_status CHECK (status IN ('SENT', 'FAILED', 'QUEUED'))
);
GO
-- ---- [M14 - Cửa hàng & POS] pos_sessions ----
-- Ca bán hàng tại quầy
CREATE TABLE pos_sessions (
    pos_session_id           INT IDENTITY(1,1) NOT NULL,
    store_id                 INT NOT NULL,
    cashier_id               INT NOT NULL,
    opening_cash             DECIMAL(18,0) NOT NULL CONSTRAINT DF_pos_sessions_opening_cash DEFAULT 0,
    closing_cash             DECIMAL(18,0) NULL,
    expected_cash            DECIMAL(18,0) NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_pos_sessions_status DEFAULT 'OPEN',
    opened_at                DATETIME2(0) NOT NULL CONSTRAINT DF_pos_sessions_opened_at DEFAULT GETDATE(),
    closed_at                DATETIME2(0) NULL,
    CONSTRAINT PK_pos_sessions PRIMARY KEY (pos_session_id),
    CONSTRAINT FK_pos_sessions_store_id FOREIGN KEY (store_id) REFERENCES stores(store_id),
    CONSTRAINT FK_pos_sessions_cashier_id FOREIGN KEY (cashier_id) REFERENCES users(user_id),
    CONSTRAINT CK_pos_sessions_status CHECK (status IN ('OPEN', 'CLOSED'))
);
GO
CREATE INDEX IX_pos_sessions_store_id ON pos_sessions(store_id);
CREATE INDEX IX_pos_sessions_cashier_id ON pos_sessions(cashier_id);
GO
-- ---- [M14 - Cửa hàng & POS] cash_drawer_transactions ----
-- Thu chi két tiền mặt
CREATE TABLE cash_drawer_transactions (
    drawer_txn_id            INT IDENTITY(1,1) NOT NULL,
    pos_session_id           INT NOT NULL,
    txn_type                 VARCHAR(20) NOT NULL,
    amount                   DECIMAL(18,0) NOT NULL,
    order_id                 BIGINT NULL,
    note                     NVARCHAR(255) NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_cash_drawer_transactions_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_cash_drawer_transactions PRIMARY KEY (drawer_txn_id),
    CONSTRAINT FK_cash_drawer_transactions_pos_session_id FOREIGN KEY (pos_session_id) REFERENCES pos_sessions(pos_session_id),
    CONSTRAINT CK_cash_drawer_transactions_txn_type CHECK (txn_type IN ('SALE', 'CASH_IN', 'CASH_OUT', 'REFUND')),
    CONSTRAINT FK_cash_drawer_transactions_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
GO
CREATE INDEX IX_cash_drawer_transactions_pos_session_id ON cash_drawer_transactions(pos_session_id);
CREATE INDEX IX_cash_drawer_transactions_order_id ON cash_drawer_transactions(order_id);
GO
-- ---- [M15 - Nhân sự] departments ----
-- Phòng ban
CREATE TABLE departments (
    department_id            INT IDENTITY(1,1) NOT NULL,
    department_name          NVARCHAR(100) NOT NULL,
    description              NVARCHAR(255) NULL,
    CONSTRAINT PK_departments PRIMARY KEY (department_id)
);
GO
-- ---- [M15 - Nhân sự] positions ----
-- Chức vụ
CREATE TABLE positions (
    position_id              INT IDENTITY(1,1) NOT NULL,
    position_name            NVARCHAR(100) NOT NULL,
    base_salary              DECIMAL(18,0) NOT NULL CONSTRAINT DF_positions_base_salary DEFAULT 0,
    CONSTRAINT PK_positions PRIMARY KEY (position_id)
);
GO
-- ---- [M15 - Nhân sự] employees ----
-- Nhân viên
CREATE TABLE employees (
    employee_id              INT IDENTITY(1,1) NOT NULL,
    employee_code            VARCHAR(20) NOT NULL,
    user_id                  INT NULL,
    full_name                NVARCHAR(150) NOT NULL,
    phone                    VARCHAR(15) NULL,
    email                    VARCHAR(150) NULL,
    birth_date               DATE NULL,
    hire_date                DATE NOT NULL,
    department_id            INT NULL,
    position_id              INT NULL,
    store_id                 INT NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_employees_status DEFAULT 'WORKING',
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_employees_created_at DEFAULT GETDATE(),
    updated_at               DATETIME2(0) NULL,
    CONSTRAINT PK_employees PRIMARY KEY (employee_id),
    CONSTRAINT UQ_employees_employee_code UNIQUE (employee_code),
    CONSTRAINT FK_employees_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_employees_department_id FOREIGN KEY (department_id) REFERENCES departments(department_id),
    CONSTRAINT FK_employees_position_id FOREIGN KEY (position_id) REFERENCES positions(position_id),
    CONSTRAINT FK_employees_store_id FOREIGN KEY (store_id) REFERENCES stores(store_id),
    CONSTRAINT CK_employees_status CHECK (status IN ('WORKING', 'ON_LEAVE', 'RESIGNED'))
);
GO
CREATE UNIQUE INDEX UX_employees_user_id ON employees(user_id) WHERE user_id IS NOT NULL;
CREATE INDEX IX_employees_user_id ON employees(user_id);
CREATE INDEX IX_employees_department_id ON employees(department_id);
CREATE INDEX IX_employees_position_id ON employees(position_id);
CREATE INDEX IX_employees_store_id ON employees(store_id);
GO
-- ---- [M15 - Nhân sự] work_shifts ----
-- Ca làm việc
CREATE TABLE work_shifts (
    shift_id                 INT IDENTITY(1,1) NOT NULL,
    shift_name               NVARCHAR(50) NOT NULL,
    start_time               TIME(0) NOT NULL,
    end_time                 TIME(0) NOT NULL,
    CONSTRAINT PK_work_shifts PRIMARY KEY (shift_id)
);
GO
-- ---- [M15 - Nhân sự] employee_shifts ----
-- Phân ca làm việc
CREATE TABLE employee_shifts (
    employee_shift_id        INT IDENTITY(1,1) NOT NULL,
    employee_id              INT NOT NULL,
    shift_id                 INT NOT NULL,
    store_id                 INT NULL,
    work_date                DATE NOT NULL,
    CONSTRAINT PK_employee_shifts PRIMARY KEY (employee_shift_id),
    CONSTRAINT UQ_employee_shifts_employee_id_shift_id_work_date UNIQUE (employee_id, shift_id, work_date),
    CONSTRAINT FK_employee_shifts_employee_id FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    CONSTRAINT FK_employee_shifts_shift_id FOREIGN KEY (shift_id) REFERENCES work_shifts(shift_id),
    CONSTRAINT FK_employee_shifts_store_id FOREIGN KEY (store_id) REFERENCES stores(store_id)
);
GO
CREATE INDEX IX_employee_shifts_shift_id ON employee_shifts(shift_id);
CREATE INDEX IX_employee_shifts_store_id ON employee_shifts(store_id);
GO
-- ---- [M15 - Nhân sự] attendance_records ----
-- Chấm công
CREATE TABLE attendance_records (
    attendance_id            INT IDENTITY(1,1) NOT NULL,
    employee_id              INT NOT NULL,
    work_date                DATE NOT NULL,
    check_in                 DATETIME2(0) NULL,
    check_out                DATETIME2(0) NULL,
    status                   VARCHAR(20) NOT NULL CONSTRAINT DF_attendance_records_status DEFAULT 'PRESENT',
    CONSTRAINT PK_attendance_records PRIMARY KEY (attendance_id),
    CONSTRAINT UQ_attendance_records_employee_id_work_date UNIQUE (employee_id, work_date),
    CONSTRAINT FK_attendance_records_employee_id FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    CONSTRAINT CK_attendance_records_status CHECK (status IN ('PRESENT', 'LATE', 'ABSENT', 'LEAVE'))
);
GO
-- ---- [M16 - Tài chính & Báo cáo] expense_categories ----
-- Loại chi phí
CREATE TABLE expense_categories (
    expense_category_id      INT IDENTITY(1,1) NOT NULL,
    category_name            NVARCHAR(100) NOT NULL,
    CONSTRAINT PK_expense_categories PRIMARY KEY (expense_category_id)
);
GO
-- ---- [M16 - Tài chính & Báo cáo] expenses ----
-- Chi phí vận hành
CREATE TABLE expenses (
    expense_id               INT IDENTITY(1,1) NOT NULL,
    expense_category_id      INT NOT NULL,
    store_id                 INT NULL,
    amount                   DECIMAL(18,0) NOT NULL,
    expense_date             DATE NOT NULL,
    description              NVARCHAR(300) NULL,
    created_by               INT NULL,
    created_at               DATETIME2(0) NOT NULL CONSTRAINT DF_expenses_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_expenses PRIMARY KEY (expense_id),
    CONSTRAINT FK_expenses_expense_category_id FOREIGN KEY (expense_category_id) REFERENCES expense_categories(expense_category_id),
    CONSTRAINT FK_expenses_store_id FOREIGN KEY (store_id) REFERENCES stores(store_id),
    CONSTRAINT FK_expenses_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO
CREATE INDEX IX_expenses_expense_category_id ON expenses(expense_category_id);
CREATE INDEX IX_expenses_store_id ON expenses(store_id);
CREATE INDEX IX_expenses_created_by ON expenses(created_by);
GO
-- ---- [M16 - Tài chính & Báo cáo] daily_sales_summaries ----
-- Tổng hợp doanh thu theo ngày
CREATE TABLE daily_sales_summaries (
    summary_id               INT IDENTITY(1,1) NOT NULL,
    summary_date             DATE NOT NULL,
    store_id                 INT NULL,
    channel                  VARCHAR(20) NOT NULL,
    total_orders             INT NOT NULL CONSTRAINT DF_daily_sales_summaries_total_orders DEFAULT 0,
    total_revenue            DECIMAL(18,0) NOT NULL CONSTRAINT DF_daily_sales_summaries_total_revenue DEFAULT 0,
    total_cost               DECIMAL(18,0) NOT NULL CONSTRAINT DF_daily_sales_summaries_total_cost DEFAULT 0,
    total_discount           DECIMAL(18,0) NOT NULL CONSTRAINT DF_daily_sales_summaries_total_discount DEFAULT 0,
    total_refund             DECIMAL(18,0) NOT NULL CONSTRAINT DF_daily_sales_summaries_total_refund DEFAULT 0,
    CONSTRAINT PK_daily_sales_summaries PRIMARY KEY (summary_id),
    CONSTRAINT UQ_daily_sales_summaries_summary_date_channel_store_id UNIQUE (summary_date, channel, store_id),
    CONSTRAINT FK_daily_sales_summaries_store_id FOREIGN KEY (store_id) REFERENCES stores(store_id)
);
GO
CREATE INDEX IX_daily_sales_summaries_store_id ON daily_sales_summaries(store_id);
GO
-- ---- [M17 - Hệ thống] system_settings ----
-- Cấu hình hệ thống
CREATE TABLE system_settings (
    setting_id               INT IDENTITY(1,1) NOT NULL,
    setting_key              VARCHAR(100) NOT NULL,
    setting_value            NVARCHAR(MAX) NULL,
    description              NVARCHAR(255) NULL,
    updated_at               DATETIME2(0) NOT NULL CONSTRAINT DF_system_settings_updated_at DEFAULT GETDATE(),
    CONSTRAINT PK_system_settings PRIMARY KEY (setting_id),
    CONSTRAINT UQ_system_settings_setting_key UNIQUE (setting_key)
);
GO
-- ---- [M17 - Hệ thống] scheduled_jobs ----
-- Công việc chạy ngầm theo lịch
CREATE TABLE scheduled_jobs (
    job_id                   INT IDENTITY(1,1) NOT NULL,
    job_name                 NVARCHAR(100) NOT NULL,
    cron_expression          VARCHAR(50) NOT NULL,
    is_enabled               BIT NOT NULL CONSTRAINT DF_scheduled_jobs_is_enabled DEFAULT 1,
    last_run_at              DATETIME2(0) NULL,
    next_run_at              DATETIME2(0) NULL,
    last_status              VARCHAR(20) NULL,
    CONSTRAINT PK_scheduled_jobs PRIMARY KEY (job_id),
    CONSTRAINT UQ_scheduled_jobs_job_name UNIQUE (job_name)
);
GO
-- ============================================================================
-- DANH SÁCH BẢNG THEO MODULE
-- M01 - Tài khoản & Phân quyền
--    roles                        Vai trò phân quyền
--    permissions                  Danh mục quyền hạn
--    role_permissions             Gán quyền cho vai trò
--    users                        Tài khoản đăng nhập (khách hàng + nhân viên)
--    user_addresses               Sổ địa chỉ nhận hàng
--    password_reset_tokens        Token đặt lại mật khẩu
--    login_histories              Lịch sử đăng nhập
--    audit_logs                   Nhật ký thao tác hệ thống
-- M02 - Khách hàng
--    membership_tiers             Hạng thành viên
--    customer_groups              Nhóm khách hàng
--    customers                    Hồ sơ khách hàng (kể cả khách vãng lai tại quầy)
--    customer_measurements        Số đo cơ thể khách hàng (tư vấn size)
-- M03 - Sản phẩm & Danh mục
--    categories                   Danh mục sản phẩm (cây cha - con)
--    collections                  Bộ sưu tập theo mùa
--    brands                       Thương hiệu
--    colors                       Bảng màu
--    sizes                        Kích cỡ
--    size_chart_details           Bảng thông số size chuẩn
--    size_recommendation_rules    Quy tắc tư vấn size tự động
--    products                     Sản phẩm
--    product_variants             Biến thể sản phẩm (màu x size)
--    product_media                Ảnh / video sản phẩm
--    product_tags                 Thẻ sản phẩm
--    product_tag_map              Gắn thẻ cho sản phẩm
--    price_histories              Lịch sử thay đổi giá
-- M04 - Kho & Nhập hàng
--    warehouses                   Kho hàng
--    inventories                  Tồn kho theo biến thể & kho
--    inventory_logs               Nhật ký xuất nhập tồn
--    suppliers                    Nhà cung cấp / xưởng may
--    purchase_orders              Đơn đặt hàng nhà cung cấp
--    purchase_order_items         Chi tiết đơn đặt hàng NCC
--    goods_receipts               Phiếu nhập kho
--    goods_receipt_items          Chi tiết phiếu nhập kho
--    stock_transfers              Phiếu điều chuyển kho
--    stock_transfer_items         Chi tiết điều chuyển kho
--    stocktakes                   Phiếu kiểm kê kho
--    stocktake_items              Chi tiết kiểm kê
-- M05 - Giỏ hàng & Yêu thích
--    carts                        Giỏ hàng
--    cart_items                   Sản phẩm trong giỏ
--    wishlists                    Sản phẩm yêu thích
-- M06 - Đơn hàng & Đổi trả
--    orders                       Đơn hàng
--    order_items                  Chi tiết đơn hàng
--    order_status_histories       Lịch sử trạng thái đơn hàng
--    order_notes                  Ghi chú nội bộ của đơn hàng
--    order_cancellations          Thông tin hủy đơn
--    return_requests              Yêu cầu đổi / trả hàng
--    return_request_items         Chi tiết hàng đổi / trả
--    refunds                      Hoàn tiền
--    invoices                     Hóa đơn bán hàng
-- M07 - Thanh toán
--    payment_methods              Phương thức thanh toán
--    payments                     Thanh toán của đơn hàng
--    payment_transactions         Giao dịch cổng thanh toán (VNPay/MoMo)
--    cod_reconciliations          Đối soát COD với đơn vị vận chuyển
-- M08 - Vận chuyển & Địa lý
--    shipping_zones               Vùng giao hàng
--    provinces                    Tỉnh / thành phố (34 đơn vị cấp tỉnh)
--    wards                        Xã / phường / đặc khu (mô hình 2 cấp)
--    shipping_carriers            Đơn vị vận chuyển
--    shipping_methods             Dịch vụ vận chuyển
--    shipping_fees                Bảng phí vận chuyển
--    shipments                    Vận đơn
--    shipment_tracking_events     Hành trình vận đơn
-- M09 - Khuyến mãi & Voucher
--    vouchers                     Mã giảm giá
--    voucher_usages               Lịch sử dùng voucher
--    promotions                   Chương trình khuyến mãi
--    promotion_products           Sản phẩm áp dụng khuyến mãi
--    flash_sales                  Flash sale
--    flash_sale_items             Sản phẩm trong flash sale
-- M10 - Tích điểm & Đổi quà
--    loyalty_accounts             Tài khoản tích điểm
--    loyalty_transactions         Giao dịch điểm thưởng
--    loyalty_rewards              Quà đổi điểm
--    reward_redemptions           Lịch sử đổi quà
-- M11 - Đánh giá & Hỗ trợ
--    product_reviews              Đánh giá sản phẩm
--    review_media                 Ảnh đánh giá
--    review_replies               Phản hồi đánh giá của shop
--    ticket_categories            Phân loại yêu cầu hỗ trợ
--    support_tickets              Yêu cầu hỗ trợ khách hàng
--    support_ticket_messages      Tin nhắn trong ticket
--    faqs                         Câu hỏi thường gặp
-- M12 - Nội dung & Marketing web
--    banners                      Banner quảng cáo
--    blog_categories              Chuyên mục bài viết
--    blog_posts                   Bài viết / tin tức phối đồ
--    newsletter_subscribers       Đăng ký nhận bản tin
-- M13 - Thông báo & Email
--    notification_templates       Mẫu thông báo
--    notifications                Thông báo trong ứng dụng
--    email_campaigns              Chiến dịch email marketing
--    message_logs                 Nhật ký gửi email / SMS
-- M14 - Cửa hàng & POS
--    stores                       Cửa hàng
--    pos_sessions                 Ca bán hàng tại quầy
--    cash_drawer_transactions     Thu chi két tiền mặt
-- M15 - Nhân sự
--    departments                  Phòng ban
--    positions                    Chức vụ
--    employees                    Nhân viên
--    work_shifts                  Ca làm việc
--    employee_shifts              Phân ca làm việc
--    attendance_records           Chấm công
-- M16 - Tài chính & Báo cáo
--    expense_categories           Loại chi phí
--    expenses                     Chi phí vận hành
--    daily_sales_summaries        Tổng hợp doanh thu theo ngày
-- M17 - Hệ thống
--    system_settings              Cấu hình hệ thống
--    scheduled_jobs               Công việc chạy ngầm theo lịch
