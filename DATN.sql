-- ======================================================================
-- HỆ THỐNG QUẢN LÝ BÁN HÀNG THỜI TRANG NAM DUNA (DUNA MEN'S WEAR) - V2
-- TẬP LỆNH KHỞI TẠO CƠ SỞ DỮ LIỆU CHUẨN VÀ SEED DỮ LIỆU THỜI TRANG DUNA
-- ======================================================================

USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QuanLyBanHang')
BEGIN
    ALTER DATABASE QuanLyBanHang SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyBanHang;
END
GO

CREATE DATABASE QuanLyBanHang;
GO

USE QuanLyBanHang;
GO

-- ======================================================================
-- 1. TẠO CÁC BẢNG DANH MỤC TRẠNG THÁI VÀ PHÂN QUYỀN
-- ======================================================================

CREATE TABLE Role (
    Role_ID INT IDENTITY(1,1) PRIMARY KEY,
    Quyen NVARCHAR(50) NOT NULL
);

CREATE TABLE TrangThaiTaiKhoan (
    TrangThaiTK_ID INT IDENTITY(1,1) PRIMARY KEY,
    TenTrangThai NVARCHAR(50) NOT NULL
);

CREATE TABLE TrangThaiNhanVien (
    TrangThaiNV_ID INT IDENTITY(1,1) PRIMARY KEY,
    TenTrangThai NVARCHAR(50) NOT NULL
);

CREATE TABLE TrangThaiDonHang (
    TrangThaiDH_ID INT IDENTITY(1,1) PRIMARY KEY,
    TenTrangThai NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE DanhMuc (
    ID_DanhMuc INT IDENTITY(1,1) PRIMARY KEY,
    TenDanhMuc NVARCHAR(100) NOT NULL,
    TyLeVat INT NOT NULL DEFAULT 8 CHECK (TyLeVat BETWEEN 0 AND 100)
);

-- ======================================================================
-- 2. TẠO BẢNG KHÁCH HÀNG (TÍCH LŨY CHI TIÊU VNĐ TRONG 24 THÁNG)
-- ======================================================================

CREATE TABLE KhachHang (
    ID_KhachHang INT IDENTITY(1,1) PRIMARY KEY,
    MaKH VARCHAR(20) UNIQUE,
    HoTen NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(20) NOT NULL,
    Email VARCHAR(100) NULL,
    GioiTinh NVARCHAR(10) DEFAULT N'Nam',
    NgaySinh DATE NULL,
    HangKhachHang NVARCHAR(50) DEFAULT N'Thường',
    DiaChi NVARCHAR(255) NULL,
    TrangThai VARCHAR(20) DEFAULT 'active',
    NgayBatDauChuKy DATETIME DEFAULT GETDATE(), -- Dùng xét chu kỳ hạng 24 tháng
    NgayTao DATETIME DEFAULT GETDATE(),
    NgayCapNhat DATETIME DEFAULT GETDATE()
);

-- ======================================================================
-- 3. TẠO BẢNG NHÂN VIÊN VÀ TÀI KHOẢN
-- ======================================================================

CREATE TABLE NhanVien (
    ID_NhanVien INT IDENTITY(1,1) PRIMARY KEY,
    MaNhanVien VARCHAR(50) NULL,
    TrangThaiNV_ID INT,
    HoVaTen NVARCHAR(100) NOT NULL,
    MaSoCCCD VARCHAR(20) UNIQUE,
    NgayCapCCCD DATE,
    NgayHetHanCCCD DATE,
    NoiCapCCCD NVARCHAR(255),
    NgaySinh DATE,
    GioiTinh BIT, 
    QuocTich NVARCHAR(50) DEFAULT N'Việt Nam',
    QueQuan NVARCHAR(255),
    NoiThuongTru NVARCHAR(255),
    DiaChiHienTai NVARCHAR(255),
    SoDienThoai VARCHAR(15),
    Email VARCHAR(100),
    LyDoKhoa NVARCHAR(MAX) NULL,
    NgayTao DATETIME DEFAULT GETDATE(),
    NgayCapNhat DATETIME DEFAULT GETDATE()
);

CREATE TABLE TaiKhoan (
    TaiKhoanID INT IDENTITY(1,1) PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Role_ID INT,
    TrangThaiTK_ID INT,
    NhanVienID INT UNIQUE NULL
);

-- ======================================================================
-- 4. TẠO BẢNG SẢN PHẨM & BIẾN THỂ THỜI TRANG NAM DUNA
-- ======================================================================

CREATE TABLE SanPham (
    ID_SanPham INT IDENTITY(1,1) PRIMARY KEY,
    MaSP VARCHAR(20) UNIQUE NULL,
    TenSP NVARCHAR(255) NOT NULL,
    ID_DanhMuc INT,
    ThuongHieu NVARCHAR(100) DEFAULT N'DUNA',
    ChatLieu NVARCHAR(150) NULL,
    FormDang NVARCHAR(50) NULL,
    HinhAnh NVARCHAR(255) NULL,
    GiaNhap DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (GiaNhap >= 0), -- Giá vốn nhập hàng
    GiaBan DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (GiaBan >= 0),
    SoLuong INT NOT NULL DEFAULT 0 CHECK (SoLuong >= 0),
    TongSoLuong INT NOT NULL DEFAULT 0 CHECK (TongSoLuong >= 0),
    TyLeVat INT NOT NULL DEFAULT 8 CHECK (TyLeVat BETWEEN 0 AND 100),
    TrangThai NVARCHAR(50) NOT NULL DEFAULT N'Còn hàng', -- Còn hàng, Hết hàng, Ngừng bán (Soft Delete)
    MoTa NVARCHAR(MAX) NULL,
    NgayTao DATETIME DEFAULT GETDATE(),
    NgayCapNhat DATETIME DEFAULT GETDATE()
);

CREATE TABLE BienTheSanPham (
    ID_BienThe INT IDENTITY(1,1) PRIMARY KEY,
    MaBienThe VARCHAR(20) UNIQUE NOT NULL,
    ID_SanPham INT NOT NULL,
    MauSac NVARCHAR(50) NOT NULL,
    TrongLuong VARCHAR(20) NOT NULL, -- Đóng vai trò là Size áo (S/M/L/XL/XXL) hoặc Size quần (28-34)
    TrongLuongGram DECIMAL(5,2) NULL,
    TrongLuongU VARCHAR(10) NULL,
    CoCan VARCHAR(10) NULL,
    HinhAnhBienThe NVARCHAR(255) NULL,
    GiaNhap DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (GiaNhap >= 0),
    GiaBan DECIMAL(18,2) NOT NULL CHECK (GiaBan >= 0),
    SoLuong INT NOT NULL DEFAULT 0 CHECK (SoLuong >= 0),
    Barcode VARCHAR(50) UNIQUE NULL,
    CONSTRAINT UQ_SanPham_Mau_Size UNIQUE (ID_SanPham, MauSac, TrongLuong)
);

CREATE TABLE ThongSoKyThuat (
    ID_ThongSo INT IDENTITY(1,1) PRIMARY KEY,
    ID_SanPham INT NOT NULL,
    DoCung NVARCHAR(30) NULL,
    DiemCanBang NVARCHAR(30) NULL,
    DiemCanBangMM INT NULL,
    VatLieuKhung NVARCHAR(150) NULL,
    VatLieuThan NVARCHAR(150) NULL,
    DoDai VARCHAR(20) NULL,
    LucCang VARCHAR(30) NULL,
    LoiChoi NVARCHAR(100) NULL,
    DoiTuong NVARCHAR(100) NULL,
    BaoHanh NVARCHAR(100) NULL,
    CongNghe NVARCHAR(500) NULL
);

-- ======================================================================
-- 5. TẠO BẢNG VOUCHERS (TIỀN TỐ THƯƠNG HIỆU DUNA)
-- ======================================================================

CREATE TABLE Vouchers (
    ID_Voucher     INT           IDENTITY(1,1) PRIMARY KEY,
    MaVoucher      NVARCHAR(20)  NOT NULL UNIQUE, -- VD: DUNA10, DUNA50K, VIP100K
    TenChuongTrinh NVARCHAR(100) DEFAULT N'Voucher ưu đãi thời trang nam DUNA',
    LoaiVoucher    NVARCHAR(20)  NOT NULL DEFAULT N'Thường' CHECK (LoaiVoucher IN (N'Thường', N'VIP')),
    GiamGia        BIGINT        NOT NULL CHECK (GiamGia >= 0), -- <= 100: %, > 100: VNĐ
    DonToiThieu    BIGINT        NOT NULL DEFAULT 0 CHECK (DonToiThieu >= 0),
    SoLuong        INT           NOT NULL DEFAULT 0 CHECK (SoLuong >= 0),
    DaSuDung       INT           NOT NULL DEFAULT 0 CHECK (DaSuDung >= 0),
    DonHangSuDung  NVARCHAR(100) NULL,
    TrangThai      INT           NOT NULL DEFAULT 1,
    NgayTao        DATETIME      DEFAULT GETDATE()
);

-- ======================================================================
-- 6. TẠO BẢNG ĐƠN HÀNG VÀ CHI TIẾT ĐƠN HÀNG
-- ======================================================================

CREATE TABLE DonHang (
    ID_DonHang INT IDENTITY(1,1) PRIMARY KEY,
    MaDonHang VARCHAR(20) UNIQUE NOT NULL,
    ID_KhachHang INT NULL,
    ID_Voucher INT NULL,
    ID_NhanVien INT NULL,
    TenNhanVien NVARCHAR(100) DEFAULT N'Admin',
    TongTien DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (TongTien >= 0),
    TienThueVAT DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (TienThueVAT >= 0),
    PhuongThucThanhToan NVARCHAR(50) NOT NULL DEFAULT N'Tiền mặt',
    TrangThaiDH_ID INT NOT NULL,
    TenNguoiNhan NVARCHAR(100) NULL,
    SoDienThoaiNhan VARCHAR(15) NULL,
    DiaChiGiaoHang NVARCHAR(255) NULL,
    GhiChu NVARCHAR(MAX) NULL,
    NgayTao DATETIME NOT NULL DEFAULT GETDATE(),
    NgayThanhToan DATETIME NULL
);

CREATE TABLE ChiTietDonHang (
    ID_ChiTiet INT IDENTITY(1,1) PRIMARY KEY,
    ID_DonHang INT NOT NULL,
    ID_BienThe INT NULL,
    ID_SanPham INT NULL,
    TenSP NVARCHAR(200) NULL,
    SoLuong INT NOT NULL DEFAULT 1 CHECK (SoLuong > 0),
    GiaNhapLucBan DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK (GiaNhapLucBan >= 0), -- Giá vốn tại thời điểm bán
    DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0),
    ThanhTien DECIMAL(18,2) NOT NULL CHECK (ThanhTien >= 0)
);
GO

-- ======================================================================
-- 7. KHÓA NGOẠI
-- ======================================================================

ALTER TABLE TaiKhoan ADD CONSTRAINT FK_TaiKhoan_Role FOREIGN KEY (Role_ID) REFERENCES Role(Role_ID);
ALTER TABLE TaiKhoan ADD CONSTRAINT FK_TaiKhoan_TrangThaiTK FOREIGN KEY (TrangThaiTK_ID) REFERENCES TrangThaiTaiKhoan(TrangThaiTK_ID);
ALTER TABLE TaiKhoan ADD CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (NhanVienID) REFERENCES NhanVien(ID_NhanVien);

ALTER TABLE NhanVien ADD CONSTRAINT FK_NhanVien_TrangThaiNV FOREIGN KEY (TrangThaiNV_ID) REFERENCES TrangThaiNhanVien(TrangThaiNV_ID);

ALTER TABLE SanPham ADD CONSTRAINT FK_SanPham_DanhMuc FOREIGN KEY (ID_DanhMuc) REFERENCES DanhMuc(ID_DanhMuc);

ALTER TABLE BienTheSanPham ADD CONSTRAINT FK_BienThe_SanPham FOREIGN KEY (ID_SanPham) REFERENCES SanPham(ID_SanPham) ON DELETE CASCADE;
ALTER TABLE ThongSoKyThuat ADD CONSTRAINT FK_ThongSo_SanPham FOREIGN KEY (ID_SanPham) REFERENCES SanPham(ID_SanPham) ON DELETE CASCADE;

ALTER TABLE DonHang ADD CONSTRAINT FK_DonHang_KhachHang FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID_KhachHang);
ALTER TABLE DonHang ADD CONSTRAINT FK_DonHang_Vouchers FOREIGN KEY (ID_Voucher) REFERENCES Vouchers(ID_Voucher);
ALTER TABLE DonHang ADD CONSTRAINT FK_DonHang_NhanVien FOREIGN KEY (ID_NhanVien) REFERENCES NhanVien(ID_NhanVien);
ALTER TABLE DonHang ADD CONSTRAINT FK_DonHang_TrangThaiDH FOREIGN KEY (TrangThaiDH_ID) REFERENCES TrangThaiDonHang(TrangThaiDH_ID);

ALTER TABLE ChiTietDonHang ADD CONSTRAINT FK_CTDH_DonHang FOREIGN KEY (ID_DonHang) REFERENCES DonHang(ID_DonHang) ON DELETE CASCADE;
ALTER TABLE ChiTietDonHang ADD CONSTRAINT FK_CTDH_BienThe FOREIGN KEY (ID_BienThe) REFERENCES BienTheSanPham(ID_BienThe);
ALTER TABLE ChiTietDonHang ADD CONSTRAINT FK_CTDH_SanPham FOREIGN KEY (ID_SanPham) REFERENCES SanPham(ID_SanPham);
GO

-- ======================================================================
-- 8. SEED DỮ LIỆU CHUẨN THỜI TRANG NAM DUNA
-- ======================================================================

-- 8.1. Vai trò & Trạng thái
INSERT INTO Role (Quyen) VALUES (N'MANAGER'), (N'EMPLOYEE');
INSERT INTO TrangThaiTaiKhoan (TenTrangThai) VALUES (N'Hoạt động'), (N'Bị khóa');
INSERT INTO TrangThaiNhanVien (TenTrangThai) VALUES (N'Hoạt động'), (N'Nghỉ việc');
INSERT INTO TrangThaiDonHang (TenTrangThai) VALUES (N'Đã thanh toán'), (N'Chưa thanh toán'), (N'Đã hủy');

-- 8.2. Danh mục thời trang nam DUNA
INSERT INTO DanhMuc (TenDanhMuc, TyLeVat) VALUES 
(N'Áo Sơ Mi Nam', 8),
(N'Áo Polo Nam', 8),
(N'Quần Âu Nam', 8),
(N'Quần Khaki Nam', 8),
(N'Áo Blazer & Suit', 8),
(N'Phụ Kiện Nam', 8);

-- 8.3. Nhân Viên
INSERT INTO NhanVien (MaNhanVien, HoVaTen, MaSoCCCD, NgayCapCCCD, NgayHetHanCCCD, NoiCapCCCD, TrangThaiNV_ID, GioiTinh, Email, NgaySinh, QueQuan, NoiThuongTru, DiaChiHienTai, SoDienThoai, QuocTich) 
VALUES 
('QL001', N'Admin Quản Lý', '001090000001', '2020-10-10', '2035-04-30', N'Cục Cảnh sát QLHC về TTXH', 1, 1, 'admin@duna.vn', '1990-01-01', N'Hà Nội', N'Ba Đình, Hà Nội', N'Số 88 Phố Bùi Thị Xuân, Hai Bà Trưng, Hà Nội', '0988888888', N'Việt Nam'),
('NV001', N'Triệu Lân Việt', '123456789012', '2021-05-15', '2037-08-19', N'Cục Cảnh sát QLHC về TTXH', 1, 1, 'viet.trieu@duna.vn', '1999-03-13', N'Hà Nội', N'Long Biên, Hà Nội', N'Số 45 Ngõ 12, Phạm Hùng, Nam Từ Liêm, Hà Nội', '0987654321', N'Việt Nam'),
('NV002', N'Nguyễn Danh Khánh', '123456789013', '2022-03-20', '2039-11-25', N'Cục Cảnh sát QLHC về TTXH', 1, 1, 'khanh.nguyen@duna.vn', '1998-10-20', N'Hải Phòng', N'Lê Chân, Hải Phòng', N'Tòa Golden West, Lê Văn Thiêm, Thanh Xuân, Hà Nội', '0912345678', N'Việt Nam'),
('NV003', N'Nguyễn Ngọc Minh', '001099123456', '2021-08-15', '2039-08-15', N'Cục Cảnh sát QLHC về TTXH', 1, 1, 'minh.nguyen@duna.vn', '1999-06-12', N'Hà Nội', N'Đống Đa, Hà Nội', N'Số 15 Ngõ 102 Trường Chinh, Đống Đa, Hà Nội', '0981234567', N'Việt Nam'),
('NV004', N'Trịnh Minh Đức', '036098654321', '2022-04-10', '2038-04-10', N'Cục Cảnh sát QLHC về TTXH', 1, 1, 'duc.trinh@duna.vn', '1998-11-20', N'Nam Định', N'TP. Nam Định', N'Số 28 Trần Thái Tông, Cầu Giấy, Hà Nội', '0934567890', N'Việt Nam');

-- 8.4. Tài Khoản
INSERT INTO TaiKhoan (Username, Password, Role_ID, TrangThaiTK_ID, NhanVienID)
VALUES 
('admin', '123', 1, 1, 1),
('nv01', '123', 2, 1, 2),
('nv02', '123', 2, 1, 3),
('nv03', '123', 2, 1, 4),
('nv04', '123', 2, 1, 5);

-- 8.5. Khách Hàng (Kèm mốc chu kỳ 24 tháng)
INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao) VALUES 
('KH-00001', N'Nguyễn Hoàng Anh', '03887410395', 'hoanganh@gmail.com', N'Nam', '1995-05-10', N'VIP', N'123 Trương Định, Hà Nội', 'active', DATEADD(MONTH, -6, GETDATE()), '2024-01-10'),
('KH-00002', N'Trần Quốc Bảo', '03355339383', 'quocbao@gmail.com', N'Nam', '1992-03-15', N'Vàng', N'45 Lý Thường Kiệt, Hà Nội', 'active', DATEADD(MONTH, -4, GETDATE()), '2024-02-15'),
('KH-00003', N'Phạm Minh Tuấn', '03889671195', 'tuanpm@gmail.com', N'Nam', '1998-07-22', N'Bạc', N'78 Đinh Tiên Hoàng, TP.HCM', 'active', DATEADD(MONTH, -2, GETDATE()), '2024-03-01'),
('KH-00004', N'Đỗ Viết Hùng', '03552033203', 'hungdv@gmail.com', N'Nam', '1990-11-08', N'Thường', N'12 Nguyễn Huệ, Đà Nẵng', 'active', GETDATE(), '2024-04-20'),
('KH-00005', N'Lê Quang Hải', '0912345678', 'hai.le@gmail.com', N'Nam', '1993-08-14', N'VIP', N'99 Hoàng Diệu, Hà Nội', 'active', DATEADD(MONTH, -8, GETDATE()), '2023-12-01');

-- 8.6. Sản Phẩm Thời Trang Nam DUNA (Kèm Giá vốn, Chất liệu, Form dáng)
INSERT INTO SanPham (MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao) VALUES
('SP001', N'Áo Sơ Mi Nam Oxford Button-Down DUNA', 1, N'DUNA', N'100% Cotton Oxford dệt đôi cao cấp', N'Slimfit tôn dáng', N'img/sp-somi-oxford.jpg', 210000, 450000, 75, 75, 8, N'Còn hàng', N'Áo sơ mi dài tay dệt sợi đôi chống nhăn tự nhiên, thấm hút mồ hôi tối đa, lịch lãm công sở.', '2026-01-01'),
('SP002', N'Áo Polo Nam DUNA Pima Cotton Bo Dệt', 2, N'DUNA', N'95% Pima Cotton, 5% Spandex co giãn', N'Regular Fit chuẩn mực', N'img/sp-polo-pima.jpg', 175000, 380000, 85, 85, 8, N'Còn hàng', N'Sợi Pima siêu dài mềm mịn gấp 2 lần cotton thường, bo cổ dệt tinh tế không bai gião.', '2026-01-02'),
('SP003', N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA', 3, N'DUNA', N'Poly Rayon Spandex xử lý nano', N'Slimfit ống đứng', N'img/sp-quan-au.jpg', 240000, 520000, 60, 60, 8, N'Còn hàng', N'Thiết kế cạp thông minh tăng giảm 3cm, đường ly dập vĩnh viễn, đứng dáng cả ngày.', '2026-01-03'),
('SP004', N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend', 5, N'DUNA', N'70% Wool, 30% Silk Blend lót lụa', N'Tailored Fit chuẩn may đo', N'img/sp-blazer-italy.jpg', 680000, 1450000, 30, 30, 8, N'Còn hàng', N'Dáng áo ve xếch Ý hiện đại, đệm vai mỏng tự nhiên, thích hợp dự tiệc và sự kiện trang trọng.', '2026-01-04'),
('SP005', N'Quần Khaki Nam DUNA Classic Co Giãn Nhẹ', 4, N'DUNA', N'98% Cotton Khaki chải kỹ, 2% Spandex', N'Straight Regular', N'img/sp-quan-khaki.jpg', 190000, 420000, 65, 65, 8, N'Còn hàng', N'Vải khaki dệt chéo dày dặn bền màu, giặt máy thoải mái không xù lông.', '2026-01-05'),
('SP006', N'Thắt Lưng Da Bò Ý Khóa Tự Động DUNA Milano', 6, N'DUNA', N'Da bò Full-grain thuộc thảo mộc', N'Bản 3.5cm chuẩn âu phục', N'img/sp-that-lung.jpg', 140000, 350000, 45, 45, 8, N'Còn hàng', N'Mặt khóa hợp kim mạ titan chống trầy, dây da nguyên tấm không bong tróc.', '2026-01-06');

-- 8.7. Biến Thể Sản Phẩm (Màu sắc × Size S/M/L/XL / 29-32)
INSERT INTO BienTheSanPham (MaBienThe, ID_SanPham, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode) VALUES
-- SP001: Áo Sơ Mi Oxford (Size 38, 39, 40, 41)
('BT001', 1, N'Trắng', N'38 (S)', 210000, 450000, 20, 'SP001-WHT-38'),
('BT002', 1, N'Trắng', N'39 (M)', 210000, 450000, 25, 'SP001-WHT-39'),
('BT003', 1, N'Trắng', N'40 (L)', 210000, 450000, 20, 'SP001-WHT-40'),
('BT004', 1, N'Xanh Baby', N'39 (M)', 210000, 450000, 10, 'SP001-BLU-39'),

-- SP002: Áo Polo Pima (Size M, L, XL)
('BT005', 2, N'Đen', N'M', 175000, 380000, 30, 'SP002-BLK-M'),
('BT006', 2, N'Đen', N'L', 175000, 380000, 25, 'SP002-BLK-L'),
('BT007', 2, N'Xanh Navy', N'M', 175000, 380000, 15, 'SP002-NVY-M'),
('BT008', 2, N'Xanh Navy', N'L', 175000, 380000, 15, 'SP002-NVY-L'),

-- SP003: Quần Âu Slimfit (Size 29, 30, 31, 32)
('BT009', 3, N'Đen Than', N'29', 240000, 520000, 15, 'SP003-BLK-29'),
('BT010', 3, N'Đen Than', N'30', 240000, 520000, 20, 'SP003-BLK-30'),
('BT011', 3, N'Đen Than', N'31', 240000, 520000, 15, 'SP003-BLK-31'),
('BT012', 3, N'Xám Ghi', N'30', 240000, 520000, 10, 'SP003-GRY-30'),

-- SP004: Áo Blazer Ý (Size 48, 50)
('BT013', 4, N'Xanh Midnight Navy', N'48 (M)', 680000, 1450000, 15, 'SP004-NVY-48'),
('BT014', 4, N'Xanh Midnight Navy', N'50 (L)', 680000, 1450000, 15, 'SP004-NVY-50'),

-- SP005: Quần Khaki (Size 30, 31)
('BT015', 5, N'Vàng Khaki Cát', N'30', 190000, 420000, 35, 'SP005-KHK-30'),
('BT016', 5, N'Vàng Khaki Cát', N'31', 190000, 420000, 30, 'SP005-KHK-31'),

-- SP006: Thắt Lưng Da Bò
('BT017', 6, N'Đen Khóa Bạc', N'Freesize', 140000, 350000, 25, 'SP006-BLK-FS'),
('BT018', 6, N'Nâu Cà Phê', N'Freesize', 140000, 350000, 20, 'SP006-BRN-FS');

-- 8.8. Vouchers Ưu Đãi Thương Hiệu DUNA
INSERT INTO Vouchers (MaVoucher, TenChuongTrinh, LoaiVoucher, GiamGia, DonToiThieu, SoLuong, DaSuDung, TrangThai) VALUES
(N'DUNA10', N'Giảm 10% đơn hàng thời trang nam DUNA', N'Thường', 10, 500000, 100, 12, 1),
(N'DUNA50K', N'Giảm trực tiếp 50.000 VNĐ cho đơn từ 600K', N'Thường', 50000, 600000, 50, 8, 1),
(N'DUNA100K', N'Giảm trực tiếp 100.000 VNĐ cho đơn từ 1.2 Triệu', N'Thường', 100000, 1200000, 30, 5, 1),
(N'VIP15P', N'Đặc quyền khách VIP: Giảm 15% tổng hóa đơn', N'VIP', 15, 800000, 50, 3, 1);

-- 8.9. Đơn Hàng Mẫu Bán Lẻ Quần Áo DUNA
INSERT INTO DonHang (MaDonHang, ID_KhachHang, ID_NhanVien, TongTien, TienThueVAT, PhuongThucThanhToan, TrangThaiDH_ID, TenNhanVien, TenNguoiNhan, SoDienThoaiNhan, DiaChiGiaoHang, GhiChu, NgayTao, NgayThanhToan) VALUES
('HD-001', 1, 2, 970000, 71852, N'Tiền mặt', 1, N'Triệu Lân Việt', N'Nguyễn Hoàng Anh', '03887410395', N'123 Trương Định, Hà Nội', N'Bán hàng tại quầy | Khách đưa: 1.000.000 VNĐ | Tiền thừa: 30.000 VNĐ | VAT (8%): 71.852 VNĐ', '2026-07-01 09:15:22', '2026-07-01 09:20:00'),
('HD-002', 2, 3, 1450000, 107407, N'Chuyển khoản', 1, N'Nguyễn Danh Khánh', N'Trần Quốc Bảo', '03355339383', N'45 Lý Thường Kiệt, Hà Nội', N'Bán hàng tại quầy | Quét mã QR VietQR | VAT (8%): 107.407 VNĐ', '2026-07-01 11:42:10', '2026-07-01 11:45:00'),
('HD-003', 3, 4, 520000, 38519, N'Tiền mặt', 1, N'Nguyễn Ngọc Minh', N'Phạm Minh Tuấn', '03889671195', N'78 Đinh Tiên Hoàng, TP.HCM', N'Bán hàng tại quầy | Khách đưa: 520.000 VNĐ | Tiền thừa: 0 VNĐ | VAT (8%): 38.519 VNĐ', '2026-07-02 10:20:15', '2026-07-02 10:22:00'),
('HD-004', 1, 2, 1970000, 145926, N'Chuyển khoản', 1, N'Triệu Lân Việt', N'Nguyễn Hoàng Anh', '03887410395', N'123 Trương Định, Hà Nội', N'Bán hàng tại quầy | Áp voucher VIP15P | VAT (8%): 145.926 VNĐ', '2026-07-02 14:35:45', '2026-07-02 14:40:00'),
('HD-005', 4, 5, 800000, 59259, N'Tiền mặt', 2, N'Trịnh Minh Đức', N'Đỗ Viết Hùng', '03552033203', N'12 Nguyễn Huệ, Đà Nẵng', N'Hóa đơn chờ - Bán hàng tại quầy', '2026-07-03 08:50:30', NULL);

-- 8.10. Chi Tiết Đơn Hàng (Kèm GiaNhapLucBan để tính Lợi Nhuận Gộp)
INSERT INTO ChiTietDonHang (ID_DonHang, ID_BienThe, ID_SanPham, TenSP, SoLuong, GiaNhapLucBan, DonGia, ThanhTien) VALUES
-- HD-001: 1 Áo Sơ Mi Oxford (450K, vốn 210K) + 1 Quần Âu (520K, vốn 240K) = 970K, vốn 450K -> Lợi nhuận gộp = 520K
(1, 2, 1, N'Áo Sơ Mi Nam Oxford Button-Down DUNA - Trắng - Size 39 (M)', 1, 210000, 450000, 450000),
(1, 10, 3, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 30', 1, 240000, 520000, 520000),

-- HD-002: 1 Áo Blazer Ý (1.450K, vốn 680K) = 1.450K, vốn 680K -> Lợi nhuận gộp = 770K
(2, 13, 4, N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend - Xanh Midnight Navy - Size 48 (M)', 1, 680000, 1450000, 1450000),

-- HD-003: 1 Quần Âu Slimfit (520K, vốn 240K)
(3, 11, 3, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 31', 1, 240000, 520000, 520000),

-- HD-004: 1 Áo Blazer (1.450K, vốn 680K) + 1 Quần Âu (520K, vốn 240K) = 1.970K
(4, 14, 4, N'Áo Blazer Nam Phong Cách Ý DUNA Wool Blend - Xanh Midnight Navy - Size 50 (L)', 1, 680000, 1450000, 1450000),
(4, 10, 3, N'Quần Âu Nam Slimfit Co Giãn Chống Nhăn DUNA - Đen Than - Size 30', 1, 240000, 520000, 520000),

-- HD-005 (Hóa đơn chờ): 1 Áo Polo (380K, vốn 175K) + 1 Quần Khaki (420K, vốn 190K) = 800K
(5, 5, 2, N'Áo Polo Nam DUNA Pima Cotton Bo Dệt - Đen - Size M', 1, 175000, 380000, 380000),
(5, 15, 5, N'Quần Khaki Nam DUNA Classic Co Giãn Nhẹ - Vàng Khaki Cát - Size 30', 1, 190000, 420000, 420000);
GO

-- 8.11. Đồng Bộ Hạng Khách Hàng Theo Tổng Chi Tiêu VNĐ Trong 24 Tháng
UPDATE k
SET k.HangKhachHang = CASE 
    WHEN ISNULL(O.TongChiTieu, 0) >= 10000000 THEN N'VIP'
    WHEN ISNULL(O.TongChiTieu, 0) >= 5000000  THEN N'Vàng'
    WHEN ISNULL(O.TongChiTieu, 0) >= 2000000  THEN N'Bạc'
    ELSE N'Thường'
END
FROM KhachHang k
LEFT JOIN (
    SELECT dh.ID_KhachHang, SUM(dh.TongTien) AS TongChiTieu
    FROM DonHang dh
    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID
    WHERE ttdh.TenTrangThai = N'Đã thanh toán'
      AND dh.NgayTao >= DATEADD(MONTH, -24, GETDATE())
    GROUP BY dh.ID_KhachHang
) O ON k.ID_KhachHang = O.ID_KhachHang;
GO
