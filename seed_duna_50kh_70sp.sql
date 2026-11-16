-- ============================================================================
-- SEED DATA: 50 KHÁCH HÀNG & 70 SẢN PHẨM THỜI TRANG NAM DUNA
-- CƠ SỞ DỮ LIỆU: QUANLYBANHANGQUANAONAMDUNA
-- ============================================================================
USE [QUANLYBANHANGQUANAONAMDUNA];
GO

-- 1. CẬP NHẬT 6 KHÁCH HÀNG BAN ĐẦU (ĐẢM BẢO CHUẨN THÔNG TIN)
UPDATE KhachHang SET MaKH = 'KH-00001', HoTen = N'Nguyễn Hoàng Anh', SoDienThoai = '0388741039', Email = 'hoanganh.duna@gmail.com', GioiTinh = N'Nam', NgaySinh = '1995-05-10', HangKhachHang = N'Bạc', DiaChi = N'123 Trương Định, Hai Bà Trưng, Hà Nội', TrangThai = 'active', TongTienChiTieu = 3500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 1;
UPDATE KhachHang SET MaKH = 'KH-00002', HoTen = N'Trần Quốc Bảo', SoDienThoai = '0335533938', Email = 'quocbao.tran@gmail.com', GioiTinh = N'Nam', NgaySinh = '1992-03-15', HangKhachHang = N'Thường', DiaChi = N'45 Lý Thường Kiệt, Hoàn Kiếm, Hà Nội', TrangThai = 'active', TongTienChiTieu = 1200000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 2;
UPDATE KhachHang SET MaKH = 'KH-00003', HoTen = N'Phạm Minh Tuấn', SoDienThoai = '0388967119', Email = 'tuanpm.invest@gmail.com', GioiTinh = N'Nam', NgaySinh = '1998-07-22', HangKhachHang = N'Thường', DiaChi = N'78 Đinh Tiên Hoàng, Quận 1, TP.HCM', TrangThai = 'active', TongTienChiTieu = 850000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 3;
UPDATE KhachHang SET MaKH = 'KH-00004', HoTen = N'Đỗ Viết Hùng', SoDienThoai = '0355203320', Email = 'hungdv.fpt@gmail.com', GioiTinh = N'Nam', NgaySinh = '1990-11-08', HangKhachHang = N'Thường', DiaChi = N'12 Nguyễn Huệ, Quận Hải Châu, Đà Nẵng', TrangThai = 'active', TongTienChiTieu = 600000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 4;
UPDATE KhachHang SET MaKH = 'KH-00005', HoTen = N'Lê Quang Hải', SoDienThoai = '0912345678', Email = 'hai.lequang@gmail.com', GioiTinh = N'Nam', NgaySinh = '1993-08-14', HangKhachHang = N'Thường', DiaChi = N'99 Hoàng Diệu, Ba Đình, Hà Nội', TrangThai = 'active', TongTienChiTieu = 1500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 5;
UPDATE KhachHang SET MaKH = 'KH-00006', HoTen = N'Khách lẻ vãng lai', SoDienThoai = '0987654321', Email = 'khachle@duna.vn', GioiTinh = N'Nam', NgaySinh = '1995-01-01', HangKhachHang = N'Thường', DiaChi = N'Mua trực tiếp tại showroom DUNA', TrangThai = 'active', TongTienChiTieu = 0, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 6;
GO

-- 2. THÊM 44 KHÁCH HÀNG MỚI (TỔNG CỘNG 50 KHÁCH HÀNG)
SET IDENTITY_INSERT KhachHang ON;
GO
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 7)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (7, 'KH-00007', N'Hoàng Mạnh Cường', '0987112233', 'cuonghm.tech@gmail.com', N'Nam', '1994-02-18', N'Vàng', N'24 Láng Hạ, Đống Đa, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 6800000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00007', HoTen = N'Hoàng Mạnh Cường', SoDienThoai = '0987112233', Email = 'cuonghm.tech@gmail.com', GioiTinh = N'Nam', NgaySinh = '1994-02-18', HangKhachHang = N'Vàng', DiaChi = N'24 Láng Hạ, Đống Đa, Hà Nội', TrangThai = 'active', TongTienChiTieu = 6800000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 7;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 8)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (8, 'KH-00008', N'Bùi Thế Anh', '0976223344', 'theanh.bui@gmail.com', N'Nam', '1997-09-05', N'Bạc', N'56 Nguyễn Trãi, Thanh Xuân, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 3200000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00008', HoTen = N'Bùi Thế Anh', SoDienThoai = '0976223344', Email = 'theanh.bui@gmail.com', GioiTinh = N'Nam', NgaySinh = '1997-09-05', HangKhachHang = N'Bạc', DiaChi = N'56 Nguyễn Trãi, Thanh Xuân, Hà Nội', TrangThai = 'active', TongTienChiTieu = 3200000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 8;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 9)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (9, 'KH-00009', N'Đặng Minh Quân', '0903334455', 'minhquan.dang@gmail.com', N'Nam', '1996-12-30', N'VIP', N'88 Lê Lợi, Bến Nghé, Quận 1, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 14500000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00009', HoTen = N'Đặng Minh Quân', SoDienThoai = '0903334455', Email = 'minhquan.dang@gmail.com', GioiTinh = N'Nam', NgaySinh = '1996-12-30', HangKhachHang = N'VIP', DiaChi = N'88 Lê Lợi, Bến Nghé, Quận 1, TP.HCM', TrangThai = 'active', TongTienChiTieu = 14500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 9;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 10)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (10, 'KH-00010', N'Phan Văn Đức', '0918445566', 'ducpv.arch@gmail.com', N'Nam', '1991-04-25', N'Thường', N'102 Bạch Đằng, Hải Châu, Đà Nẵng', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1450000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00010', HoTen = N'Phan Văn Đức', SoDienThoai = '0918445566', Email = 'ducpv.arch@gmail.com', GioiTinh = N'Nam', NgaySinh = '1991-04-25', HangKhachHang = N'Thường', DiaChi = N'102 Bạch Đằng, Hải Châu, Đà Nẵng', TrangThai = 'active', TongTienChiTieu = 1450000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 10;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 11)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (11, 'KH-00011', N'Vũ Thành Long', '0935556677', 'longvt.media@gmail.com', N'Nam', '1995-10-12', N'Vàng', N'15 Trần Phú, Ngô Quyền, Hải Phòng', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 8200000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00011', HoTen = N'Vũ Thành Long', SoDienThoai = '0935556677', Email = 'longvt.media@gmail.com', GioiTinh = N'Nam', NgaySinh = '1995-10-12', HangKhachHang = N'Vàng', DiaChi = N'15 Trần Phú, Ngô Quyền, Hải Phòng', TrangThai = 'active', TongTienChiTieu = 8200000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 11;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 12)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (12, 'KH-00012', N'Dương Đình Trọng', '0947667788', 'trongdd.duna@gmail.com', N'Nam', '1998-01-20', N'Thường', N'36 Hòa Bình, Ninh Kiều, Cần Thơ', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 950000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00012', HoTen = N'Dương Đình Trọng', SoDienThoai = '0947667788', Email = 'trongdd.duna@gmail.com', GioiTinh = N'Nam', NgaySinh = '1998-01-20', HangKhachHang = N'Thường', DiaChi = N'36 Hòa Bình, Ninh Kiều, Cần Thơ', TrangThai = 'active', TongTienChiTieu = 950000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 12;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 13)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (13, 'KH-00013', N'Mai Xuân Tiến', '0968778899', 'tienmx.lawyer@gmail.com', N'Nam', '1989-06-14', N'Bạc', N'210 Xã Đàn, Đống Đa, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 4100000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00013', HoTen = N'Mai Xuân Tiến', SoDienThoai = '0968778899', Email = 'tienmx.lawyer@gmail.com', GioiTinh = N'Nam', NgaySinh = '1989-06-14', HangKhachHang = N'Bạc', DiaChi = N'210 Xã Đàn, Đống Đa, Hà Nội', TrangThai = 'active', TongTienChiTieu = 4100000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 13;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 14)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (14, 'KH-00014', N'Nguyễn Tiến Dũng', '0979889900', 'tiendung.nguyen@gmail.com', N'Nam', '1993-03-08', N'VIP', N'150 Nguyễn Thị Minh Khai, Quận 3, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 18200000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00014', HoTen = N'Nguyễn Tiến Dũng', SoDienThoai = '0979889900', Email = 'tiendung.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1993-03-08', HangKhachHang = N'VIP', DiaChi = N'150 Nguyễn Thị Minh Khai, Quận 3, TP.HCM', TrangThai = 'active', TongTienChiTieu = 18200000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 14;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 15)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (15, 'KH-00015', N'Trịnh Công Minh', '0981990011', 'congminh.trinh@gmail.com', N'Nam', '1990-08-27', N'Thường', N'72 Lý Thái Tổ, TP. Bắc Ninh', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1100000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00015', HoTen = N'Trịnh Công Minh', SoDienThoai = '0981990011', Email = 'congminh.trinh@gmail.com', GioiTinh = N'Nam', NgaySinh = '1990-08-27', HangKhachHang = N'Thường', DiaChi = N'72 Lý Thái Tổ, TP. Bắc Ninh', TrangThai = 'active', TongTienChiTieu = 1100000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 15;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 16)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (16, 'KH-00016', N'Lương Gia Huy', '0902112244', 'giahuy.luong@gmail.com', N'Nam', '1999-05-19', N'Bạc', N'65 Phan Châu Trinh, Hải Châu, Đà Nẵng', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 2800000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00016', HoTen = N'Lương Gia Huy', SoDienThoai = '0902112244', Email = 'giahuy.luong@gmail.com', GioiTinh = N'Nam', NgaySinh = '1999-05-19', HangKhachHang = N'Bạc', DiaChi = N'65 Phan Châu Trinh, Hải Châu, Đà Nẵng', TrangThai = 'active', TongTienChiTieu = 2800000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 16;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 17)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (17, 'KH-00017', N'Hà Đức Chinh', '0913223355', 'ducchinh.ha@gmail.com', N'Nam', '1997-09-22', N'Vàng', N'89 Lê Thanh Nghị, TP. Hải Dương', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 7500000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00017', HoTen = N'Hà Đức Chinh', SoDienThoai = '0913223355', Email = 'ducchinh.ha@gmail.com', GioiTinh = N'Nam', NgaySinh = '1997-09-22', HangKhachHang = N'Vàng', DiaChi = N'89 Lê Thanh Nghị, TP. Hải Dương', TrangThai = 'active', TongTienChiTieu = 7500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 17;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 18)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (18, 'KH-00018', N'Đoàn Văn Hậu', '0934334466', 'vanhau.doan@gmail.com', N'Nam', '1999-04-19', N'VIP', N'25 Liễu Giai, Ba Đình, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 22000000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00018', HoTen = N'Đoàn Văn Hậu', SoDienThoai = '0934334466', Email = 'vanhau.doan@gmail.com', GioiTinh = N'Nam', NgaySinh = '1999-04-19', HangKhachHang = N'VIP', DiaChi = N'25 Liễu Giai, Ba Đình, Hà Nội', TrangThai = 'active', TongTienChiTieu = 22000000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 18;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 19)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (19, 'KH-00019', N'Quế Ngọc Hải', '0945445577', 'ngochai.que@gmail.com', N'Nam', '1993-05-15', N'Vàng', N'42 Quang Trung, TP. Vinh, Nghệ An', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 9100000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00019', HoTen = N'Quế Ngọc Hải', SoDienThoai = '0945445577', Email = 'ngochai.que@gmail.com', GioiTinh = N'Nam', NgaySinh = '1993-05-15', HangKhachHang = N'Vàng', DiaChi = N'42 Quang Trung, TP. Vinh, Nghệ An', TrangThai = 'active', TongTienChiTieu = 9100000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 19;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 20)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (20, 'KH-00020', N'Đỗ Duy Mạnh', '0966556688', 'duymanh.do@gmail.com', N'Nam', '1996-09-29', N'Bạc', N'18 Cầu Giấy, Cầu Giấy, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 3900000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00020', HoTen = N'Đỗ Duy Mạnh', SoDienThoai = '0966556688', Email = 'duymanh.do@gmail.com', GioiTinh = N'Nam', NgaySinh = '1996-09-29', HangKhachHang = N'Bạc', DiaChi = N'18 Cầu Giấy, Cầu Giấy, Hà Nội', TrangThai = 'active', TongTienChiTieu = 3900000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 20;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 21)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (21, 'KH-00021', N'Nguyễn Hồng Duy', '0977667799', 'hongduy.nguyen@gmail.com', N'Nam', '1996-06-13', N'Thường', N'15 Cách Mạng Tháng 8, Đồng Xoài, Bình Phước', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1600000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00021', HoTen = N'Nguyễn Hồng Duy', SoDienThoai = '0977667799', Email = 'hongduy.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1996-06-13', HangKhachHang = N'Thường', DiaChi = N'15 Cách Mạng Tháng 8, Đồng Xoài, Bình Phước', TrangThai = 'active', TongTienChiTieu = 1600000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 21;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 22)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (22, 'KH-00022', N'Vũ Văn Thanh', '0988778800', 'vanthanh.vu@gmail.com', N'Nam', '1996-04-14', N'Vàng', N'68 Nguyễn Lương Bằng, TP. Hải Dương', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 6400000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00022', HoTen = N'Vũ Văn Thanh', SoDienThoai = '0988778800', Email = 'vanthanh.vu@gmail.com', GioiTinh = N'Nam', NgaySinh = '1996-04-14', HangKhachHang = N'Vàng', DiaChi = N'68 Nguyễn Lương Bằng, TP. Hải Dương', TrangThai = 'active', TongTienChiTieu = 6400000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 22;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 23)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (23, 'KH-00023', N'Nguyễn Tuấn Anh', '0909889911', 'tuananh.nguyen@gmail.com', N'Nam', '1995-05-16', N'VIP', N'77 Trần Hưng Đạo, TP. Thái Bình', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 16800000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00023', HoTen = N'Nguyễn Tuấn Anh', SoDienThoai = '0909889911', Email = 'tuananh.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1995-05-16', HangKhachHang = N'VIP', DiaChi = N'77 Trần Hưng Đạo, TP. Thái Bình', TrangThai = 'active', TongTienChiTieu = 16800000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 23;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 24)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (24, 'KH-00024', N'Lương Xuân Trường', '0919990022', 'xuantruong.luong@gmail.com', N'Nam', '1995-04-28', N'Bạc', N'12 Bình Thuận, TP. Tuyên Quang', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 4500000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00024', HoTen = N'Lương Xuân Trường', SoDienThoai = '0919990022', Email = 'xuantruong.luong@gmail.com', GioiTinh = N'Nam', NgaySinh = '1995-04-28', HangKhachHang = N'Bạc', DiaChi = N'12 Bình Thuận, TP. Tuyên Quang', TrangThai = 'active', TongTienChiTieu = 4500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 24;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 25)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (25, 'KH-00025', N'Nguyễn Công Phượng', '0931001133', 'congphuong.nguyen@gmail.com', N'Nam', '1995-01-21', N'VIP', N'83 Lê Nin, TP. Vinh, Nghệ An', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 25400000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00025', HoTen = N'Nguyễn Công Phượng', SoDienThoai = '0931001133', Email = 'congphuong.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1995-01-21', HangKhachHang = N'VIP', DiaChi = N'83 Lê Nin, TP. Vinh, Nghệ An', TrangThai = 'active', TongTienChiTieu = 25400000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 25;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 26)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (26, 'KH-00026', N'Trần Đình Trọng', '0942112244', 'dinhtrong.tran@gmail.com', N'Nam', '1997-04-25', N'Thường', N'93 Đa Tốn, Gia Lâm, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 800000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00026', HoTen = N'Trần Đình Trọng', SoDienThoai = '0942112244', Email = 'dinhtrong.tran@gmail.com', GioiTinh = N'Nam', NgaySinh = '1997-04-25', HangKhachHang = N'Thường', DiaChi = N'93 Đa Tốn, Gia Lâm, Hà Nội', TrangThai = 'active', TongTienChiTieu = 800000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 26;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 27)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (27, 'KH-00027', N'Phạm Đức Huy', '0963223355', 'duchuy.pham@gmail.com', N'Nam', '1995-01-20', N'Thường', N'31 Nguyễn Trãi, Thanh Miện, Hải Dương', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1300000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00027', HoTen = N'Phạm Đức Huy', SoDienThoai = '0963223355', Email = 'duchuy.pham@gmail.com', GioiTinh = N'Nam', NgaySinh = '1995-01-20', HangKhachHang = N'Thường', DiaChi = N'31 Nguyễn Trãi, Thanh Miện, Hải Dương', TrangThai = 'active', TongTienChiTieu = 1300000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 27;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 28)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (28, 'KH-00028', N'Nguyễn Huy Hùng', '0974334466', 'huyhung.nguyen@gmail.com', N'Nam', '1992-03-02', N'Bạc', N'54 Tây Sơn, Đống Đa, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 2400000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00028', HoTen = N'Nguyễn Huy Hùng', SoDienThoai = '0974334466', Email = 'huyhung.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1992-03-02', HangKhachHang = N'Bạc', DiaChi = N'54 Tây Sơn, Đống Đa, Hà Nội', TrangThai = 'active', TongTienChiTieu = 2400000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 28;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 29)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (29, 'KH-00029', N'Nguyễn Anh Đức', '0985445577', 'anhduc.nguyen@gmail.com', N'Nam', '1985-10-24', N'Vàng', N'102 Đại Lộ Bình Dương, Thủ Dầu Một', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 8900000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00029', HoTen = N'Nguyễn Anh Đức', SoDienThoai = '0985445577', Email = 'anhduc.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1985-10-24', HangKhachHang = N'Vàng', DiaChi = N'102 Đại Lộ Bình Dương, Thủ Dầu Một', TrangThai = 'active', TongTienChiTieu = 8900000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 29;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 30)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (30, 'KH-00030', N'Phan Văn Tài Em', '0906556688', 'taiem.phan@gmail.com', N'Nam', '1982-04-23', N'Thường', N'48 Hùng Vương, TP. Tân An, Long An', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1750000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00030', HoTen = N'Phan Văn Tài Em', SoDienThoai = '0906556688', Email = 'taiem.phan@gmail.com', GioiTinh = N'Nam', NgaySinh = '1982-04-23', HangKhachHang = N'Thường', DiaChi = N'48 Hùng Vương, TP. Tân An, Long An', TrangThai = 'active', TongTienChiTieu = 1750000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 30;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 31)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (31, 'KH-00031', N'Lê Công Vinh', '0917667799', 'congvinh.le@gmail.com', N'Nam', '1985-12-10', N'VIP', N'90 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 28000000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00031', HoTen = N'Lê Công Vinh', SoDienThoai = '0917667799', Email = 'congvinh.le@gmail.com', GioiTinh = N'Nam', NgaySinh = '1985-12-10', HangKhachHang = N'VIP', DiaChi = N'90 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM', TrangThai = 'active', TongTienChiTieu = 28000000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 31;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 32)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (32, 'KH-00032', N'Huỳnh Kesley', '0938778800', 'kesley.huynh@gmail.com', N'Nam', '1981-11-27', N'Bạc', N'34 Yersin, TP. Thủ Dầu Một, Bình Dương', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 3600000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00032', HoTen = N'Huỳnh Kesley', SoDienThoai = '0938778800', Email = 'kesley.huynh@gmail.com', GioiTinh = N'Nam', NgaySinh = '1981-11-27', HangKhachHang = N'Bạc', DiaChi = N'34 Yersin, TP. Thủ Dầu Một, Bình Dương', TrangThai = 'active', TongTienChiTieu = 3600000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 32;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 33)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (33, 'KH-00033', N'Nguyễn Minh Phương', '0949889911', 'minhphuong.nguyen@gmail.com', N'Nam', '1980-07-05', N'Vàng', N'61 Đồng Khởi, TP. Biên Hòa, Đồng Nai', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 7100000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00033', HoTen = N'Nguyễn Minh Phương', SoDienThoai = '0949889911', Email = 'minhphuong.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1980-07-05', HangKhachHang = N'Vàng', DiaChi = N'61 Đồng Khởi, TP. Biên Hòa, Đồng Nai', TrangThai = 'active', TongTienChiTieu = 7100000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 33;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 34)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (34, 'KH-00034', N'Vũ Như Thành', '0961990022', 'nhuthanh.vu@gmail.com', N'Nam', '1981-08-28', N'Thường', N'19 Hàng Sắt, TP. Nam Định', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1250000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00034', HoTen = N'Vũ Như Thành', SoDienThoai = '0961990022', Email = 'nhuthanh.vu@gmail.com', GioiTinh = N'Nam', NgaySinh = '1981-08-28', HangKhachHang = N'Thường', DiaChi = N'19 Hàng Sắt, TP. Nam Định', TrangThai = 'active', TongTienChiTieu = 1250000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 34;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 35)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (35, 'KH-00035', N'Dương Hồng Sơn', '0972001133', 'hongson.duong@gmail.com', N'Nam', '1982-11-20', N'Bạc', N'74 Đặng Thái Thân, TP. Vinh, Nghệ An', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 4300000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00035', HoTen = N'Dương Hồng Sơn', SoDienThoai = '0972001133', Email = 'hongson.duong@gmail.com', GioiTinh = N'Nam', NgaySinh = '1982-11-20', HangKhachHang = N'Bạc', DiaChi = N'74 Đặng Thái Thân, TP. Vinh, Nghệ An', TrangThai = 'active', TongTienChiTieu = 4300000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 35;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 36)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (36, 'KH-00036', N'Nguyễn Việt Thắng', '0983112244', 'vietthang.nguyen@gmail.com', N'Nam', '1981-09-13', N'Vàng', N'82 Trương Định, TP. Tân An, Long An', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 5800000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00036', HoTen = N'Nguyễn Việt Thắng', SoDienThoai = '0983112244', Email = 'vietthang.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1981-09-13', HangKhachHang = N'Vàng', DiaChi = N'82 Trương Định, TP. Tân An, Long An', TrangThai = 'active', TongTienChiTieu = 5800000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 36;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 37)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (37, 'KH-00037', N'Thạch Bảo Khanh', '0904223355', 'baokhanh.thach@gmail.com', N'Nam', '1979-04-25', N'Thường', N'11 Hàng Bông, Hoàn Kiếm, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 900000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00037', HoTen = N'Thạch Bảo Khanh', SoDienThoai = '0904223355', Email = 'baokhanh.thach@gmail.com', GioiTinh = N'Nam', NgaySinh = '1979-04-25', HangKhachHang = N'Thường', DiaChi = N'11 Hàng Bông, Hoàn Kiếm, Hà Nội', TrangThai = 'active', TongTienChiTieu = 900000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 37;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 38)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (38, 'KH-00038', N'Đặng Phương Nam', '0915334466', 'phuongnam.dang@gmail.com', N'Nam', '1976-12-15', N'Bạc', N'45 Mạc Thị Bưởi, TP. Nam Định', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 3100000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00038', HoTen = N'Đặng Phương Nam', SoDienThoai = '0915334466', Email = 'phuongnam.dang@gmail.com', GioiTinh = N'Nam', NgaySinh = '1976-12-15', HangKhachHang = N'Bạc', DiaChi = N'45 Mạc Thị Bưởi, TP. Nam Định', TrangThai = 'active', TongTienChiTieu = 3100000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 38;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 39)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (39, 'KH-00039', N'Nguyễn Hồng Sơn', '0936445577', 'hongson.nguyen@gmail.com', N'Nam', '1970-10-09', N'VIP', N'88 Phố Huế, Hai Bà Trưng, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 19500000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00039', HoTen = N'Nguyễn Hồng Sơn', SoDienThoai = '0936445577', Email = 'hongson.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1970-10-09', HangKhachHang = N'VIP', DiaChi = N'88 Phố Huế, Hai Bà Trưng, Hà Nội', TrangThai = 'active', TongTienChiTieu = 19500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 39;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 40)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (40, 'KH-00040', N'Trương Việt Hoàng', '0947556688', 'viethoang.truong@gmail.com', N'Nam', '1975-12-09', N'Vàng', N'29 Lạch Tray, Ngô Quyền, Hải Phòng', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 6200000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00040', HoTen = N'Trương Việt Hoàng', SoDienThoai = '0947556688', Email = 'viethoang.truong@gmail.com', GioiTinh = N'Nam', NgaySinh = '1975-12-09', HangKhachHang = N'Vàng', DiaChi = N'29 Lạch Tray, Ngô Quyền, Hải Phòng', TrangThai = 'active', TongTienChiTieu = 6200000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 40;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 41)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (41, 'KH-00041', N'Trần Công Minh', '0968667799', 'congminh.tran@gmail.com', N'Nam', '1970-09-01', N'Thường', N'14 Lý Thường Kiệt, TP. Cao Lãnh, Đồng Tháp', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1400000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00041', HoTen = N'Trần Công Minh', SoDienThoai = '0968667799', Email = 'congminh.tran@gmail.com', GioiTinh = N'Nam', NgaySinh = '1970-09-01', HangKhachHang = N'Thường', DiaChi = N'14 Lý Thường Kiệt, TP. Cao Lãnh, Đồng Tháp', TrangThai = 'active', TongTienChiTieu = 1400000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 41;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 42)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (42, 'KH-00042', N'Lê Huỳnh Đức', '0979778800', 'huynhduc.le@gmail.com', N'Nam', '1972-04-20', N'VIP', N'120 Nguyễn Văn Linh, Thanh Khê, Đà Nẵng', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 21000000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00042', HoTen = N'Lê Huỳnh Đức', SoDienThoai = '0979778800', Email = 'huynhduc.le@gmail.com', GioiTinh = N'Nam', NgaySinh = '1972-04-20', HangKhachHang = N'VIP', DiaChi = N'120 Nguyễn Văn Linh, Thanh Khê, Đà Nẵng', TrangThai = 'active', TongTienChiTieu = 21000000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 42;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 43)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (43, 'KH-00043', N'Văn Sỹ Hùng', '0980889911', 'syhung.van@gmail.com', N'Nam', '1970-08-14', N'Thường', N'38 Lê Mao, TP. Vinh, Nghệ An', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 750000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00043', HoTen = N'Văn Sỹ Hùng', SoDienThoai = '0980889911', Email = 'syhung.van@gmail.com', GioiTinh = N'Nam', NgaySinh = '1970-08-14', HangKhachHang = N'Thường', DiaChi = N'38 Lê Mao, TP. Vinh, Nghệ An', TrangThai = 'active', TongTienChiTieu = 750000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 43;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 44)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (44, 'KH-00044', N'Nguyễn Hữu Đang', '0901990022', 'huudang.nguyen@gmail.com', N'Nam', '1969-03-05', N'Bạc', N'52 Trần Phú, TP. Nha Trang, Khánh Hòa', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 2900000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00044', HoTen = N'Nguyễn Hữu Đang', SoDienThoai = '0901990022', Email = 'huudang.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1969-03-05', HangKhachHang = N'Bạc', DiaChi = N'52 Trần Phú, TP. Nha Trang, Khánh Hòa', TrangThai = 'active', TongTienChiTieu = 2900000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 44;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 45)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (45, 'KH-00045', N'Trần Minh Chiến', '0912001133', 'minhchien.tran@gmail.com', N'Nam', '1974-10-27', N'Vàng', N'180 Ba Tháng Hai, Quận 10, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 8500000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00045', HoTen = N'Trần Minh Chiến', SoDienThoai = '0912001133', Email = 'minhchien.tran@gmail.com', GioiTinh = N'Nam', NgaySinh = '1974-10-27', HangKhachHang = N'Vàng', DiaChi = N'180 Ba Tháng Hai, Quận 10, TP.HCM', TrangThai = 'active', TongTienChiTieu = 8500000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 45;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 46)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (46, 'KH-00046', N'Đỗ Khải', '0933112244', 'dokhai.fpt@gmail.com', N'Nam', '1974-04-01', N'Thường', N'95 Nguyễn Trãi, Quận 5, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1150000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00046', HoTen = N'Đỗ Khải', SoDienThoai = '0933112244', Email = 'dokhai.fpt@gmail.com', GioiTinh = N'Nam', NgaySinh = '1974-04-01', HangKhachHang = N'Thường', DiaChi = N'95 Nguyễn Trãi, Quận 5, TP.HCM', TrangThai = 'active', TongTienChiTieu = 1150000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 46;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 47)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (47, 'KH-00047', N'Nguyễn Đức Thắng', '0944223355', 'ducthang.nguyen@gmail.com', N'Nam', '1976-05-28', N'Bạc', N'76 Đội Cấn, Ba Đình, Hà Nội', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 3700000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00047', HoTen = N'Nguyễn Đức Thắng', SoDienThoai = '0944223355', Email = 'ducthang.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1976-05-28', HangKhachHang = N'Bạc', DiaChi = N'76 Đội Cấn, Ba Đình, Hà Nội', TrangThai = 'active', TongTienChiTieu = 3700000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 47;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 48)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (48, 'KH-00048', N'Triệu Quang Hà', '0965334466', 'quangha.trieu@gmail.com', N'Nam', '1975-09-03', N'Thường', N'60 Quang Trung, TP. Thanh Hóa', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1650000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00048', HoTen = N'Triệu Quang Hà', SoDienThoai = '0965334466', Email = 'quangha.trieu@gmail.com', GioiTinh = N'Nam', NgaySinh = '1975-09-03', HangKhachHang = N'Thường', DiaChi = N'60 Quang Trung, TP. Thanh Hóa', TrangThai = 'active', TongTienChiTieu = 1650000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 48;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 49)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (49, 'KH-00049', N'Võ Hoàng Bửu', '0976445577', 'hoangbuu.vo@gmail.com', N'Nam', '1968-07-10', N'Vàng', N'230 Hai Bà Trưng, Quận 1, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 5900000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00049', HoTen = N'Võ Hoàng Bửu', SoDienThoai = '0976445577', Email = 'hoangbuu.vo@gmail.com', GioiTinh = N'Nam', NgaySinh = '1968-07-10', HangKhachHang = N'Vàng', DiaChi = N'230 Hai Bà Trưng, Quận 1, TP.HCM', TrangThai = 'active', TongTienChiTieu = 5900000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 49;
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = 50)
    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES (50, 'KH-00050', N'Nguyễn Liêm Thanh', '0987556688', 'liemthanh.nguyen@gmail.com', N'Nam', '1972-03-25', N'Thường', N'41 Võ Văn Tần, Quận 3, TP.HCM', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), 1050000);
ELSE
    UPDATE KhachHang SET MaKH = 'KH-00050', HoTen = N'Nguyễn Liêm Thanh', SoDienThoai = '0987556688', Email = 'liemthanh.nguyen@gmail.com', GioiTinh = N'Nam', NgaySinh = '1972-03-25', HangKhachHang = N'Thường', DiaChi = N'41 Võ Văn Tần, Quận 3, TP.HCM', TrangThai = 'active', TongTienChiTieu = 1050000, NgayCapNhat = GETDATE() WHERE ID_KhachHang = 50;
SET IDENTITY_INSERT KhachHang OFF;
GO

-- 3. THÊM 64 SẢN PHẨM MỚI (TỔNG CỘNG 70 SẢN PHẨM: SP001 -> SP070)
SET IDENTITY_INSERT SanPham ON;
GO
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 7)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (7, 'SP007', N'Áo Sơ Mi Nam Poplin Trắng Cổ Điển DUNA', 1, N'DUNA', N'100% Poplin Cotton dệt mịn', N'Regular Fit', N'img/sp-somi-poplin.jpg', 195000, 420000, 0, 0, 8, N'Còn hàng', N'Vải poplin dệt mịn màng, bề mặt bóng nhẹ sang trọng, dễ ủi và luôn phẳng phiu.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP007', TenSP = N'Áo Sơ Mi Nam Poplin Trắng Cổ Điển DUNA', ID_DanhMuc = 1, ChatLieu = N'100% Poplin Cotton dệt mịn', FormDang = N'Regular Fit', HinhAnh = N'img/sp-somi-poplin.jpg', GiaNhap = 195000, GiaBan = 420000, TrangThai = N'Còn hàng', MoTa = N'Vải poplin dệt mịn màng, bề mặt bóng nhẹ sang trọng, dễ ủi và luôn phẳng phiu.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 7;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 8)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (8, 'SP008', N'Áo Sơ Mi Nam Kẻ Sọc Xanh Slimfit DUNA', 1, N'DUNA', N'60% Cotton, 40% Bamboo kháng khuẩn', N'Slim Fit', N'img/sp-somi-kesoc.jpg', 215000, 460000, 0, 0, 8, N'Còn hàng', N'Họa tiết sọc kẻ thanh lịch tạo cảm giác tôn dáng và cao ráo, chất liệu sợi tre mát lạnh.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP008', TenSP = N'Áo Sơ Mi Nam Kẻ Sọc Xanh Slimfit DUNA', ID_DanhMuc = 1, ChatLieu = N'60% Cotton, 40% Bamboo kháng khuẩn', FormDang = N'Slim Fit', HinhAnh = N'img/sp-somi-kesoc.jpg', GiaNhap = 215000, GiaBan = 460000, TrangThai = N'Còn hàng', MoTa = N'Họa tiết sọc kẻ thanh lịch tạo cảm giác tôn dáng và cao ráo, chất liệu sợi tre mát lạnh.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 8;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 9)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (9, 'SP009', N'Áo Sơ Mi Nam Sợi Tre Bamboo DUNA Trắng', 1, N'DUNA', N'50% Bamboo, 50% Spun Poly mềm mại', N'Slim Fit', N'img/sp-somi-bamboo.jpg', 225000, 480000, 0, 0, 8, N'Còn hàng', N'Sợi tre tự nhiên kháng khuẩn, hút ẩm vượt trội và chống tia cực tím hiệu quả.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP009', TenSP = N'Áo Sơ Mi Nam Sợi Tre Bamboo DUNA Trắng', ID_DanhMuc = 1, ChatLieu = N'50% Bamboo, 50% Spun Poly mềm mại', FormDang = N'Slim Fit', HinhAnh = N'img/sp-somi-bamboo.jpg', GiaNhap = 225000, GiaBan = 480000, TrangThai = N'Còn hàng', MoTa = N'Sợi tre tự nhiên kháng khuẩn, hút ẩm vượt trội và chống tia cực tím hiệu quả.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 9;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 10)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (10, 'SP010', N'Áo Sơ Mi Nam Cổ Trụ Grandad DUNA Linen', 1, N'DUNA', N'100% Linen Pháp dệt thưa mát mẻ', N'Relaxed Fit', N'img/sp-somi-cotru.jpg', 240000, 520000, 0, 0, 8, N'Còn hàng', N'Cổ trụ phong cách tối giản thanh lịch, chất đũi tự nhiên mát lịm ngày hè.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP010', TenSP = N'Áo Sơ Mi Nam Cổ Trụ Grandad DUNA Linen', ID_DanhMuc = 1, ChatLieu = N'100% Linen Pháp dệt thưa mát mẻ', FormDang = N'Relaxed Fit', HinhAnh = N'img/sp-somi-cotru.jpg', GiaNhap = 240000, GiaBan = 520000, TrangThai = N'Còn hàng', MoTa = N'Cổ trụ phong cách tối giản thanh lịch, chất đũi tự nhiên mát lịm ngày hè.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 10;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 11)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (11, 'SP011', N'Áo Sơ Mi Denim Nam DUNA Vintage Indigo', 1, N'DUNA', N'100% Cotton Denim wash mềm', N'Regular Fit', N'img/sp-somi-denim.jpg', 230000, 490000, 0, 0, 8, N'Còn hàng', N'Vải denim wash xanh chàm bụi bặm nhưng vẫn giữ được nét lịch thiệp khi phối quần âu/khaki.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP011', TenSP = N'Áo Sơ Mi Denim Nam DUNA Vintage Indigo', ID_DanhMuc = 1, ChatLieu = N'100% Cotton Denim wash mềm', FormDang = N'Regular Fit', HinhAnh = N'img/sp-somi-denim.jpg', GiaNhap = 230000, GiaBan = 490000, TrangThai = N'Còn hàng', MoTa = N'Vải denim wash xanh chàm bụi bặm nhưng vẫn giữ được nét lịch thiệp khi phối quần âu/khaki.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 11;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 12)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (12, 'SP012', N'Áo Sơ Mi Nam Ngắn Tay Họa Tiết DUNA Summer', 1, N'DUNA', N'100% Cotton Rayon thoáng mát', N'Regular Fit', N'img/sp-somi-ngantay-hoatiet.jpg', 185000, 390000, 0, 0, 8, N'Còn hàng', N'Sơ mi cộc tay họa tiết hoa văn chìm tinh tế, thích hợp cho chuyến du lịch hoặc dạo phố cuối tuần.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP012', TenSP = N'Áo Sơ Mi Nam Ngắn Tay Họa Tiết DUNA Summer', ID_DanhMuc = 1, ChatLieu = N'100% Cotton Rayon thoáng mát', FormDang = N'Regular Fit', HinhAnh = N'img/sp-somi-ngantay-hoatiet.jpg', GiaNhap = 185000, GiaBan = 390000, TrangThai = N'Còn hàng', MoTa = N'Sơ mi cộc tay họa tiết hoa văn chìm tinh tế, thích hợp cho chuyến du lịch hoặc dạo phố cuối tuần.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 12;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 13)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (13, 'SP013', N'Áo Sơ Mi Nam Flannel Kẻ Caro DUNA Warm', 1, N'DUNA', N'100% Flannel Cotton chải lông ấm áp', N'Regular Fit', N'img/sp-somi-flannel.jpg', 220000, 470000, 0, 0, 8, N'Còn hàng', N'Sơ mi flannel dày dặn mùa thu đông, giữ nhiệt tốt, phối khoác ngoài áo thun cực kỳ năng động.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP013', TenSP = N'Áo Sơ Mi Nam Flannel Kẻ Caro DUNA Warm', ID_DanhMuc = 1, ChatLieu = N'100% Flannel Cotton chải lông ấm áp', FormDang = N'Regular Fit', HinhAnh = N'img/sp-somi-flannel.jpg', GiaNhap = 220000, GiaBan = 470000, TrangThai = N'Còn hàng', MoTa = N'Sơ mi flannel dày dặn mùa thu đông, giữ nhiệt tốt, phối khoác ngoài áo thun cực kỳ năng động.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 13;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 14)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (14, 'SP014', N'Áo Sơ Mi Nam Cổ Bẻ Spread Collar DUNA Silk', 1, N'DUNA', N'85% Cotton, 15% Silk lụa tự nhiên', N'Tailored Fit', N'img/sp-somi-silk.jpg', 260000, 560000, 0, 0, 8, N'Còn hàng', N'Dòng sơ mi may đo cao cấp cổ bẻ rộng chuyên thắt cà vạt cho các cuộc họp và sự kiện quan trọng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP014', TenSP = N'Áo Sơ Mi Nam Cổ Bẻ Spread Collar DUNA Silk', ID_DanhMuc = 1, ChatLieu = N'85% Cotton, 15% Silk lụa tự nhiên', FormDang = N'Tailored Fit', HinhAnh = N'img/sp-somi-silk.jpg', GiaNhap = 260000, GiaBan = 560000, TrangThai = N'Còn hàng', MoTa = N'Dòng sơ mi may đo cao cấp cổ bẻ rộng chuyên thắt cà vạt cho các cuộc họp và sự kiện quan trọng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 14;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 15)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (15, 'SP015', N'Áo Sơ Mi Nam Xanh Baby Slimfit DUNA', 1, N'DUNA', N'100% Compact Cotton chống nhăn', N'Slim Fit', N'img/sp-somi-xanhbaby.jpg', 205000, 440000, 0, 0, 8, N'Còn hàng', N'Gam màu xanh baby nhã nhặn, tôn sáng làn da nam giới, form ôm nhẹ hiện đại.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP015', TenSP = N'Áo Sơ Mi Nam Xanh Baby Slimfit DUNA', ID_DanhMuc = 1, ChatLieu = N'100% Compact Cotton chống nhăn', FormDang = N'Slim Fit', HinhAnh = N'img/sp-somi-xanhbaby.jpg', GiaNhap = 205000, GiaBan = 440000, TrangThai = N'Còn hàng', MoTa = N'Gam màu xanh baby nhã nhặn, tôn sáng làn da nam giới, form ôm nhẹ hiện đại.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 15;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 16)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (16, 'SP016', N'Áo Sơ Mi Nam Kẻ Windowpane DUNA Smart', 1, N'DUNA', N'100% Cotton dệt ô cửa sổ mảnh', N'Slim Fit', N'img/sp-somi-windowpane.jpg', 210000, 450000, 0, 0, 8, N'Còn hàng', N'Họa tiết kẻ ô mảnh thời thượng, phong thái đĩnh đạc cho quý ông hiện đại.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP016', TenSP = N'Áo Sơ Mi Nam Kẻ Windowpane DUNA Smart', ID_DanhMuc = 1, ChatLieu = N'100% Cotton dệt ô cửa sổ mảnh', FormDang = N'Slim Fit', HinhAnh = N'img/sp-somi-windowpane.jpg', GiaNhap = 210000, GiaBan = 450000, TrangThai = N'Còn hàng', MoTa = N'Họa tiết kẻ ô mảnh thời thượng, phong thái đĩnh đạc cho quý ông hiện đại.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 16;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 17)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (17, 'SP017', N'Áo Sơ Mi Nam Trắng Cổ Vát Cutaway DUNA', 1, N'DUNA', N'100% Cotton Ai Cập sợi dài', N'Tailored Fit', N'img/sp-somi-cutaway.jpg', 235000, 500000, 0, 0, 8, N'Còn hàng', N'Thiết kế cổ vát góc rộng sang trọng, điểm nhấn tinh tế khi mặc kèm suit jacket.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP017', TenSP = N'Áo Sơ Mi Nam Trắng Cổ Vát Cutaway DUNA', ID_DanhMuc = 1, ChatLieu = N'100% Cotton Ai Cập sợi dài', FormDang = N'Tailored Fit', HinhAnh = N'img/sp-somi-cutaway.jpg', GiaNhap = 235000, GiaBan = 500000, TrangThai = N'Còn hàng', MoTa = N'Thiết kế cổ vát góc rộng sang trọng, điểm nhấn tinh tế khi mặc kèm suit jacket.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 17;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 18)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (18, 'SP018', N'Áo Sơ Mi Nam Dài Tay Màu Xám Khói DUNA', 1, N'DUNA', N'65% Cotton, 35% Polyester chống nhăn', N'Regular Fit', N'img/sp-somi-xamkhoi.jpg', 200000, 430000, 0, 0, 8, N'Còn hàng', N'Tông màu xám khói trung tính thanh lịch, dễ phối cùng mọi loại quần âu và khaki.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP018', TenSP = N'Áo Sơ Mi Nam Dài Tay Màu Xám Khói DUNA', ID_DanhMuc = 1, ChatLieu = N'65% Cotton, 35% Polyester chống nhăn', FormDang = N'Regular Fit', HinhAnh = N'img/sp-somi-xamkhoi.jpg', GiaNhap = 200000, GiaBan = 430000, TrangThai = N'Còn hàng', MoTa = N'Tông màu xám khói trung tính thanh lịch, dễ phối cùng mọi loại quần âu và khaki.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 18;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 19)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (19, 'SP019', N'Áo Sơ Mi Nam Màu Đen Tuyết DUNA Night', 1, N'DUNA', N'100% Cotton chải kỹ nhuộm hoạt tính', N'Slim Fit', N'img/sp-somi-dentuyet.jpg', 215000, 460000, 0, 0, 8, N'Còn hàng', N'Sắc đen tuyền huyền bí, không phai màu sau nhiều lần giặt, tôn dáng nam tính.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP019', TenSP = N'Áo Sơ Mi Nam Màu Đen Tuyết DUNA Night', ID_DanhMuc = 1, ChatLieu = N'100% Cotton chải kỹ nhuộm hoạt tính', FormDang = N'Slim Fit', HinhAnh = N'img/sp-somi-dentuyet.jpg', GiaNhap = 215000, GiaBan = 460000, TrangThai = N'Còn hàng', MoTa = N'Sắc đen tuyền huyền bí, không phai màu sau nhiều lần giặt, tôn dáng nam tính.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 19;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 20)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (20, 'SP020', N'Áo Sơ Mi Nam Ngắn Tay Trắng DUNA Casual', 1, N'DUNA', N'100% Cotton Poplin xử lý chống nhăn', N'Regular Fit', N'img/sp-somi-ngantay-trang.jpg', 175000, 380000, 0, 0, 8, N'Còn hàng', N'Áo sơ mi cộc tay công sở mát nhẹ ngày hè, tiện dụng thoải mái vận động.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP020', TenSP = N'Áo Sơ Mi Nam Ngắn Tay Trắng DUNA Casual', ID_DanhMuc = 1, ChatLieu = N'100% Cotton Poplin xử lý chống nhăn', FormDang = N'Regular Fit', HinhAnh = N'img/sp-somi-ngantay-trang.jpg', GiaNhap = 175000, GiaBan = 380000, TrangThai = N'Còn hàng', MoTa = N'Áo sơ mi cộc tay công sở mát nhẹ ngày hè, tiện dụng thoải mái vận động.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 20;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 21)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (21, 'SP021', N'Áo Polo Nam DUNA CVC Cá Sấu Co Giãn 4 Chiều', 2, N'DUNA', N'65% Cotton, 35% Poly cá sấu 4 chiều', N'Regular Fit', N'img/sp-polo-cvc.jpg', 160000, 350000, 0, 0, 8, N'Còn hàng', N'Vải mắt chim cá sấu thoáng khí, thấm hút mồ hôi, co giãn 4 chiều vận động thoải mái.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP021', TenSP = N'Áo Polo Nam DUNA CVC Cá Sấu Co Giãn 4 Chiều', ID_DanhMuc = 2, ChatLieu = N'65% Cotton, 35% Poly cá sấu 4 chiều', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-cvc.jpg', GiaNhap = 160000, GiaBan = 350000, TrangThai = N'Còn hàng', MoTa = N'Vải mắt chim cá sấu thoáng khí, thấm hút mồ hôi, co giãn 4 chiều vận động thoải mái.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 21;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 22)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (22, 'SP022', N'Áo Polo Nam DUNA Phối Bo Cổ Viền Màu', 2, N'DUNA', N'95% Cotton compact, 5% Spandex', N'Slim Fit', N'img/sp-polo-bo-vien.jpg', 170000, 370000, 0, 0, 8, N'Còn hàng', N'Điểm nhấn đường viền tương phản ở cổ và tay áo, phong cách thể thao trẻ trung.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP022', TenSP = N'Áo Polo Nam DUNA Phối Bo Cổ Viền Màu', ID_DanhMuc = 2, ChatLieu = N'95% Cotton compact, 5% Spandex', FormDang = N'Slim Fit', HinhAnh = N'img/sp-polo-bo-vien.jpg', GiaNhap = 170000, GiaBan = 370000, TrangThai = N'Còn hàng', MoTa = N'Điểm nhấn đường viền tương phản ở cổ và tay áo, phong cách thể thao trẻ trung.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 22;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 23)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (23, 'SP023', N'Áo Polo Nam Coolmax DUNA Thoáng Khí Kháng Khuẩn', 2, N'DUNA', N'100% Sợi Coolmax công nghệ làm mát', N'Regular Fit', N'img/sp-polo-coolmax.jpg', 190000, 420000, 0, 0, 8, N'Còn hàng', N'Công nghệ sợi rãnh thoát nhiệt cực nhanh, giữ cơ thể luôn khô ráo trong những ngày nắng nóng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP023', TenSP = N'Áo Polo Nam Coolmax DUNA Thoáng Khí Kháng Khuẩn', ID_DanhMuc = 2, ChatLieu = N'100% Sợi Coolmax công nghệ làm mát', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-coolmax.jpg', GiaNhap = 190000, GiaBan = 420000, TrangThai = N'Còn hàng', MoTa = N'Công nghệ sợi rãnh thoát nhiệt cực nhanh, giữ cơ thể luôn khô ráo trong những ngày nắng nóng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 23;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 24)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (24, 'SP024', N'Áo Polo Nam DUNA Cổ Khóa Kéo Zip Polo Hiện Đại', 2, N'DUNA', N'95% Pima Cotton, 5% Elastane', N'Slim Fit', N'img/sp-polo-zip.jpg', 205000, 450000, 0, 0, 8, N'Còn hàng', N'Khóa kéo kim loại sáng bóng thay cho khuy cài truyền thống, thiết kế phá cách hiện đại.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP024', TenSP = N'Áo Polo Nam DUNA Cổ Khóa Kéo Zip Polo Hiện Đại', ID_DanhMuc = 2, ChatLieu = N'95% Pima Cotton, 5% Elastane', FormDang = N'Slim Fit', HinhAnh = N'img/sp-polo-zip.jpg', GiaNhap = 205000, GiaBan = 450000, TrangThai = N'Còn hàng', MoTa = N'Khóa kéo kim loại sáng bóng thay cho khuy cài truyền thống, thiết kế phá cách hiện đại.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 24;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 25)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (25, 'SP025', N'Áo Polo Nam DUNA Dệt Tổ Ong Waffle Knit', 2, N'DUNA', N'100% Cotton dệt vân tổ ong 3D', N'Regular Fit', N'img/sp-polo-waffle.jpg', 180000, 390000, 0, 0, 8, N'Còn hàng', N'Cấu trúc dệt tổ ong thoáng khí và có chiều sâu bề mặt vải, mang lại vẻ ngoài lịch lãm.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP025', TenSP = N'Áo Polo Nam DUNA Dệt Tổ Ong Waffle Knit', ID_DanhMuc = 2, ChatLieu = N'100% Cotton dệt vân tổ ong 3D', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-waffle.jpg', GiaNhap = 180000, GiaBan = 390000, TrangThai = N'Còn hàng', MoTa = N'Cấu trúc dệt tổ ong thoáng khí và có chiều sâu bề mặt vải, mang lại vẻ ngoài lịch lãm.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 25;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 26)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (26, 'SP026', N'Áo Polo Nam DUNA Kẻ Ngang Thủy Thủ Hè', 2, N'DUNA', N'100% Cotton chải kỹ dệt sọc yarn-dyed', N'Regular Fit', N'img/sp-polo-sockey.jpg', 175000, 380000, 0, 0, 8, N'Còn hàng', N'Sọc kẻ ngang phong cách nautical trẻ trung, năng động cho các dịp đi chơi cuối tuần.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP026', TenSP = N'Áo Polo Nam DUNA Kẻ Ngang Thủy Thủ Hè', ID_DanhMuc = 2, ChatLieu = N'100% Cotton chải kỹ dệt sọc yarn-dyed', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-sockey.jpg', GiaNhap = 175000, GiaBan = 380000, TrangThai = N'Còn hàng', MoTa = N'Sọc kẻ ngang phong cách nautical trẻ trung, năng động cho các dịp đi chơi cuối tuần.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 26;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 27)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (27, 'SP027', N'Áo Polo Nam DUNA Mercerized Cotton Ánh Kim', 2, N'DUNA', N'100% Mercerized Cotton cao cấp', N'Tailored Fit', N'img/sp-polo-mercerized.jpg', 220000, 480000, 0, 0, 8, N'Còn hàng', N'Sợi cotton được kiềm hóa bóng mượt như lụa, độ bền màu tuyệt đối và không xù lông.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP027', TenSP = N'Áo Polo Nam DUNA Mercerized Cotton Ánh Kim', ID_DanhMuc = 2, ChatLieu = N'100% Mercerized Cotton cao cấp', FormDang = N'Tailored Fit', HinhAnh = N'img/sp-polo-mercerized.jpg', GiaNhap = 220000, GiaBan = 480000, TrangThai = N'Còn hàng', MoTa = N'Sợi cotton được kiềm hóa bóng mượt như lụa, độ bền màu tuyệt đối và không xù lông.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 27;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 28)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (28, 'SP028', N'Áo Polo Nam DUNA Cổ Trụ Tàu Trẻ Trung', 2, N'DUNA', N'95% Cotton, 5% Spandex', N'Slim Fit', N'img/sp-polo-cotru.jpg', 165000, 360000, 0, 0, 8, N'Còn hàng', N'Cổ trụ tàu gọn gàng cá tính, dễ phối đồ dạo phố và đi làm thường ngày.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP028', TenSP = N'Áo Polo Nam DUNA Cổ Trụ Tàu Trẻ Trung', ID_DanhMuc = 2, ChatLieu = N'95% Cotton, 5% Spandex', FormDang = N'Slim Fit', HinhAnh = N'img/sp-polo-cotru.jpg', GiaNhap = 165000, GiaBan = 360000, TrangThai = N'Còn hàng', MoTa = N'Cổ trụ tàu gọn gàng cá tính, dễ phối đồ dạo phố và đi làm thường ngày.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 28;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 29)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (29, 'SP029', N'Áo Polo Nam DUNA Thể Thao Quick-Dry Golf', 2, N'DUNA', N'88% Polyester, 12% Spandex Quick-Dry', N'Athletic Fit', N'img/sp-polo-golf.jpg', 195000, 430000, 0, 0, 8, N'Còn hàng', N'Vải thể thao chuyên dụng chơi golf, co giãn tối đa và chống nhăn tuyệt đối.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP029', TenSP = N'Áo Polo Nam DUNA Thể Thao Quick-Dry Golf', ID_DanhMuc = 2, ChatLieu = N'88% Polyester, 12% Spandex Quick-Dry', FormDang = N'Athletic Fit', HinhAnh = N'img/sp-polo-golf.jpg', GiaNhap = 195000, GiaBan = 430000, TrangThai = N'Còn hàng', MoTa = N'Vải thể thao chuyên dụng chơi golf, co giãn tối đa và chống nhăn tuyệt đối.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 29;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 30)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (30, 'SP030', N'Áo Polo Nam DUNA Màu Xanh Rêu Olive Vintage', 2, N'DUNA', N'95% Cotton chải mềm, 5% Spandex', N'Regular Fit', N'img/sp-polo-olive.jpg', 170000, 370000, 0, 0, 8, N'Còn hàng', N'Sắc xanh rêu phong trần, nam tính, dễ kết hợp với quần khaki màu cát hoặc quần âu đen.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP030', TenSP = N'Áo Polo Nam DUNA Màu Xanh Rêu Olive Vintage', ID_DanhMuc = 2, ChatLieu = N'95% Cotton chải mềm, 5% Spandex', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-olive.jpg', GiaNhap = 170000, GiaBan = 370000, TrangThai = N'Còn hàng', MoTa = N'Sắc xanh rêu phong trần, nam tính, dễ kết hợp với quần khaki màu cát hoặc quần âu đen.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 30;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 31)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (31, 'SP031', N'Áo Polo Nam DUNA Màu Nâu Be Phong Cách Hàn Quốc', 2, N'DUNA', N'95% Cotton Organic, 5% Spandex', N'Relaxed Fit', N'img/sp-polo-naube.jpg', 175000, 380000, 0, 0, 8, N'Còn hàng', N'Gam màu nâu be ấm áp phong cách Hàn Quốc, phom rộng rãi phóng khoáng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP031', TenSP = N'Áo Polo Nam DUNA Màu Nâu Be Phong Cách Hàn Quốc', ID_DanhMuc = 2, ChatLieu = N'95% Cotton Organic, 5% Spandex', FormDang = N'Relaxed Fit', HinhAnh = N'img/sp-polo-naube.jpg', GiaNhap = 175000, GiaBan = 380000, TrangThai = N'Còn hàng', MoTa = N'Gam màu nâu be ấm áp phong cách Hàn Quốc, phom rộng rãi phóng khoáng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 31;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 32)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (32, 'SP032', N'Áo Polo Nam DUNA Đen Trơn Minimalist Form Chuẩn', 2, N'DUNA', N'100% Pima Cotton dệt mịn', N'Slim Fit', N'img/sp-polo-den.jpg', 165000, 360000, 0, 0, 8, N'Còn hàng', N'Thiết kế đen tuyền cơ bản không bao giờ lỗi mốt, item bắt buộc trong tủ đồ phái mạnh.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP032', TenSP = N'Áo Polo Nam DUNA Đen Trơn Minimalist Form Chuẩn', ID_DanhMuc = 2, ChatLieu = N'100% Pima Cotton dệt mịn', FormDang = N'Slim Fit', HinhAnh = N'img/sp-polo-den.jpg', GiaNhap = 165000, GiaBan = 360000, TrangThai = N'Còn hàng', MoTa = N'Thiết kế đen tuyền cơ bản không bao giờ lỗi mốt, item bắt buộc trong tủ đồ phái mạnh.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 32;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 33)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (33, 'SP033', N'Áo Polo Nam DUNA Dệt Kim Dài Tay Thu Đông', 2, N'DUNA', N'80% Cotton, 20% Wool mỏng nhẹ', N'Regular Fit', N'img/sp-polo-daitay.jpg', 210000, 460000, 0, 0, 8, N'Còn hàng', N'Polo tay dài len dệt kim mỏng ấm áp thời tiết giao mùa, lịch sự khi đi làm.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP033', TenSP = N'Áo Polo Nam DUNA Dệt Kim Dài Tay Thu Đông', ID_DanhMuc = 2, ChatLieu = N'80% Cotton, 20% Wool mỏng nhẹ', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-daitay.jpg', GiaNhap = 210000, GiaBan = 460000, TrangThai = N'Còn hàng', MoTa = N'Polo tay dài len dệt kim mỏng ấm áp thời tiết giao mùa, lịch sự khi đi làm.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 33;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 34)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (34, 'SP034', N'Áo Polo Nam DUNA Vải Modal Siêu Mềm Mịn', 2, N'DUNA', N'50% Modal gỗ sồi, 50% Cotton', N'Regular Fit', N'img/sp-polo-modal.jpg', 185000, 410000, 0, 0, 8, N'Còn hàng', N'Sợi modal chiết xuất từ gỗ sồi tự nhiên cho bề mặt êm dịu vượt trội, làm mát tức thì.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP034', TenSP = N'Áo Polo Nam DUNA Vải Modal Siêu Mềm Mịn', ID_DanhMuc = 2, ChatLieu = N'50% Modal gỗ sồi, 50% Cotton', FormDang = N'Regular Fit', HinhAnh = N'img/sp-polo-modal.jpg', GiaNhap = 185000, GiaBan = 410000, TrangThai = N'Còn hàng', MoTa = N'Sợi modal chiết xuất từ gỗ sồi tự nhiên cho bề mặt êm dịu vượt trội, làm mát tức thì.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 34;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 35)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (35, 'SP035', N'Quần Âu Nam DUNA Regular Cạp Tăng Giảm Thông Minh', 3, N'DUNA', N'70% Polyester, 28% Rayon, 2% Spandex', N'Regular Fit', N'img/sp-quan-au-captang.jpg', 250000, 550000, 0, 0, 8, N'Còn hàng', N'Cạp quần có nút trượt thông minh co giãn linh hoạt 3cm, cực kỳ thoải mái khi ngồi làm việc lâu.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP035', TenSP = N'Quần Âu Nam DUNA Regular Cạp Tăng Giảm Thông Minh', ID_DanhMuc = 3, ChatLieu = N'70% Polyester, 28% Rayon, 2% Spandex', FormDang = N'Regular Fit', HinhAnh = N'img/sp-quan-au-captang.jpg', GiaNhap = 250000, GiaBan = 550000, TrangThai = N'Còn hàng', MoTa = N'Cạp quần có nút trượt thông minh co giãn linh hoạt 3cm, cực kỳ thoải mái khi ngồi làm việc lâu.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 35;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 36)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (36, 'SP036', N'Quần Âu Nam Xếp Ly 1 Ly Hàn Quốc DUNA Classic', 3, N'DUNA', N'Polyester Rayon chải sợi kỹ', N'Relaxed Tapered', N'img/sp-quan-au-xepli.jpg', 265000, 580000, 0, 0, 8, N'Còn hàng', N'Đường xếp ly 1 ly tinh xảo phong cách retro thanh lịch, tôn dáng đĩnh đạc.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP036', TenSP = N'Quần Âu Nam Xếp Ly 1 Ly Hàn Quốc DUNA Classic', ID_DanhMuc = 3, ChatLieu = N'Polyester Rayon chải sợi kỹ', FormDang = N'Relaxed Tapered', HinhAnh = N'img/sp-quan-au-xepli.jpg', GiaNhap = 265000, GiaBan = 580000, TrangThai = N'Còn hàng', MoTa = N'Đường xếp ly 1 ly tinh xảo phong cách retro thanh lịch, tôn dáng đĩnh đạc.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 36;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 37)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (37, 'SP037', N'Quần Âu Nam Vải Tuytsi Cao Cấp Đứng Dáng DUNA', 3, N'DUNA', N'Vải Tuytsi biên vàng chống nhăn', N'Slim Fit', N'img/sp-quan-au-tuytsi.jpg', 280000, 620000, 0, 0, 8, N'Còn hàng', N'Chất liệu tuytsi cao cấp mặt vải đanh chắc, nếp ly vĩnh viễn không cần ủi lại sau khi giặt.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP037', TenSP = N'Quần Âu Nam Vải Tuytsi Cao Cấp Đứng Dáng DUNA', ID_DanhMuc = 3, ChatLieu = N'Vải Tuytsi biên vàng chống nhăn', FormDang = N'Slim Fit', HinhAnh = N'img/sp-quan-au-tuytsi.jpg', GiaNhap = 280000, GiaBan = 620000, TrangThai = N'Còn hàng', MoTa = N'Chất liệu tuytsi cao cấp mặt vải đanh chắc, nếp ly vĩnh viễn không cần ủi lại sau khi giặt.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 37;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 38)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (38, 'SP038', N'Quần Âu Nam Màu Xanh Navy Sọc Chìm DUNA Smart', 3, N'DUNA', N'Poly Rayon Spandex vân sọc nhỏ', N'Slim Fit', N'img/sp-quan-au-navysoc.jpg', 255000, 560000, 0, 0, 8, N'Còn hàng', N'Họa tiết sọc chìm tôn chiều dài đôi chân, màu xanh navy chuẩn công sở chuyên nghiệp.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP038', TenSP = N'Quần Âu Nam Màu Xanh Navy Sọc Chìm DUNA Smart', ID_DanhMuc = 3, ChatLieu = N'Poly Rayon Spandex vân sọc nhỏ', FormDang = N'Slim Fit', HinhAnh = N'img/sp-quan-au-navysoc.jpg', GiaNhap = 255000, GiaBan = 560000, TrangThai = N'Còn hàng', MoTa = N'Họa tiết sọc chìm tôn chiều dài đôi chân, màu xanh navy chuẩn công sở chuyên nghiệp.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 38;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 39)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (39, 'SP039', N'Quần Âu Nam DUNA Màu Xám Khói Ống Đứng Lịch Lãm', 3, N'DUNA', N'Poly Viscose pha Spandex co giãn', N'Straight Fit', N'img/sp-quan-au-xamkhoi.jpg', 240000, 530000, 0, 0, 8, N'Còn hàng', N'Ống đứng suông nhẹ giấu khuyết điểm chân, màu xám khói dễ phối cùng sơ mi và polo.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP039', TenSP = N'Quần Âu Nam DUNA Màu Xám Khói Ống Đứng Lịch Lãm', ID_DanhMuc = 3, ChatLieu = N'Poly Viscose pha Spandex co giãn', FormDang = N'Straight Fit', HinhAnh = N'img/sp-quan-au-xamkhoi.jpg', GiaNhap = 240000, GiaBan = 530000, TrangThai = N'Còn hàng', MoTa = N'Ống đứng suông nhẹ giấu khuyết điểm chân, màu xám khói dễ phối cùng sơ mi và polo.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 39;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 40)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (40, 'SP040', N'Quần Âu Nam DUNA Co Giãn 4 Chiều Công Sở', 3, N'DUNA', N'80% Poly, 20% Spandex siêu đàn hồi', N'Slim Fit', N'img/sp-quan-au-cogian4c.jpg', 220000, 490000, 0, 0, 8, N'Còn hàng', N'Độ đàn hồi cực cao, đứng lên ngồi xuống thoải mái không hề bị gò bó.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP040', TenSP = N'Quần Âu Nam DUNA Co Giãn 4 Chiều Công Sở', ID_DanhMuc = 3, ChatLieu = N'80% Poly, 20% Spandex siêu đàn hồi', FormDang = N'Slim Fit', HinhAnh = N'img/sp-quan-au-cogian4c.jpg', GiaNhap = 220000, GiaBan = 490000, TrangThai = N'Còn hàng', MoTa = N'Độ đàn hồi cực cao, đứng lên ngồi xuống thoải mái không hề bị gò bó.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 40;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 41)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (41, 'SP041', N'Quần Âu Nam DUNA Wool Blend Giữ Phom Chuẩn Ý', 3, N'DUNA', N'50% Wool, 50% Poly dệt thoi cao cấp', N'Tailored Fit', N'img/sp-quan-au-wool.jpg', 310000, 680000, 0, 0, 8, N'Còn hàng', N'Pha len tự nhiên tạo độ bóng và rủ vải sang trọng, phù hợp dự tiệc và lễ tân ngoại giao.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP041', TenSP = N'Quần Âu Nam DUNA Wool Blend Giữ Phom Chuẩn Ý', ID_DanhMuc = 3, ChatLieu = N'50% Wool, 50% Poly dệt thoi cao cấp', FormDang = N'Tailored Fit', HinhAnh = N'img/sp-quan-au-wool.jpg', GiaNhap = 310000, GiaBan = 680000, TrangThai = N'Còn hàng', MoTa = N'Pha len tự nhiên tạo độ bóng và rủ vải sang trọng, phù hợp dự tiệc và lễ tân ngoại giao.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 41;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 42)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (42, 'SP042', N'Quần Âu Nam DUNA Màu Nâu Tây Dáng Tapered', 3, N'DUNA', N'Poly Rayon Spandex bề mặt mịn', N'Tapered Fit', N'img/sp-quan-au-nautay.jpg', 245000, 540000, 0, 0, 8, N'Còn hàng', N'Tông màu nâu tây thời thượng, phần đùi thoải mái và ôm thon dần xuống cổ chân.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP042', TenSP = N'Quần Âu Nam DUNA Màu Nâu Tây Dáng Tapered', ID_DanhMuc = 3, ChatLieu = N'Poly Rayon Spandex bề mặt mịn', FormDang = N'Tapered Fit', HinhAnh = N'img/sp-quan-au-nautay.jpg', GiaNhap = 245000, GiaBan = 540000, TrangThai = N'Còn hàng', MoTa = N'Tông màu nâu tây thời thượng, phần đùi thoải mái và ôm thon dần xuống cổ chân.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 42;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 43)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (43, 'SP043', N'Quần Âu Nam DUNA Cạp Chun Ẩn Thoải Mái Cả Ngày', 3, N'DUNA', N'Cotton pha Poly Spandex co giãn', N'Regular Fit', N'img/sp-quan-au-capchun.jpg', 230000, 510000, 0, 0, 8, N'Còn hàng', N'Chun co giãn 2 bên sườn được giấu khéo léo bên trong cạp, vừa vặn mà lịch thiệp.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP043', TenSP = N'Quần Âu Nam DUNA Cạp Chun Ẩn Thoải Mái Cả Ngày', ID_DanhMuc = 3, ChatLieu = N'Cotton pha Poly Spandex co giãn', FormDang = N'Regular Fit', HinhAnh = N'img/sp-quan-au-capchun.jpg', GiaNhap = 230000, GiaBan = 510000, TrangThai = N'Còn hàng', MoTa = N'Chun co giãn 2 bên sườn được giấu khéo léo bên trong cạp, vừa vặn mà lịch thiệp.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 43;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 44)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (44, 'SP044', N'Quần Âu Nam DUNA Dáng Cropped Trên Mắt Cá', 3, N'DUNA', N'Poly Rayon Spandex xước nhẹ', N'Cropped Slim', N'img/sp-quan-au-cropped.jpg', 225000, 500000, 0, 0, 8, N'Còn hàng', N'Chiều dài lửng trên mắt cá chân hiện đại, phối cực đẹp cùng giày lười Loafer hoặc Sneaker trắng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP044', TenSP = N'Quần Âu Nam DUNA Dáng Cropped Trên Mắt Cá', ID_DanhMuc = 3, ChatLieu = N'Poly Rayon Spandex xước nhẹ', FormDang = N'Cropped Slim', HinhAnh = N'img/sp-quan-au-cropped.jpg', GiaNhap = 225000, GiaBan = 500000, TrangThai = N'Còn hàng', MoTa = N'Chiều dài lửng trên mắt cá chân hiện đại, phối cực đẹp cùng giày lười Loafer hoặc Sneaker trắng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 44;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 45)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (45, 'SP045', N'Quần Âu Nam DUNA Màu Trắng Kem Dự Tiệc Sang Trọng', 3, N'DUNA', N'Tuytsi lót chống lộ dày dặn', N'Slim Fit', N'img/sp-quan-au-trangkem.jpg', 295000, 650000, 0, 0, 8, N'Còn hàng', N'Màu trắng kem quý phái với lớp lót trong tinh tế, nổi bật trong các buổi tiệc tối và dạ hội.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP045', TenSP = N'Quần Âu Nam DUNA Màu Trắng Kem Dự Tiệc Sang Trọng', ID_DanhMuc = 3, ChatLieu = N'Tuytsi lót chống lộ dày dặn', FormDang = N'Slim Fit', HinhAnh = N'img/sp-quan-au-trangkem.jpg', GiaNhap = 295000, GiaBan = 650000, TrangThai = N'Còn hàng', MoTa = N'Màu trắng kem quý phái với lớp lót trong tinh tế, nổi bật trong các buổi tiệc tối và dạ hội.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 45;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 46)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (46, 'SP046', N'Quần Khaki Chinos Nam DUNA Slimfit Túi Xéo', 4, N'DUNA', N'98% Cotton Khaki chải kỹ, 2% Spandex', N'Slim Fit', N'img/sp-khaki-chinos.jpg', 205000, 450000, 0, 0, 8, N'Còn hàng', N'Thiết kế túi xéo Chinos kinh điển, vải khaki chải kỹ bề mặt mịn không bám bụi.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP046', TenSP = N'Quần Khaki Chinos Nam DUNA Slimfit Túi Xéo', ID_DanhMuc = 4, ChatLieu = N'98% Cotton Khaki chải kỹ, 2% Spandex', FormDang = N'Slim Fit', HinhAnh = N'img/sp-khaki-chinos.jpg', GiaNhap = 205000, GiaBan = 450000, TrangThai = N'Còn hàng', MoTa = N'Thiết kế túi xéo Chinos kinh điển, vải khaki chải kỹ bề mặt mịn không bám bụi.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 46;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 47)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (47, 'SP047', N'Quần Khaki Nam DUNA Màu Vàng Be Dạo Phố Trẻ Trung', 4, N'DUNA', N'100% Cotton twill dệt chéo', N'Regular Fit', N'img/sp-khaki-vangbe.jpg', 195000, 430000, 0, 0, 8, N'Còn hàng', N'Tông màu be sáng năng động trẻ trung, dễ phối cùng áo thun và áo polo.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP047', TenSP = N'Quần Khaki Nam DUNA Màu Vàng Be Dạo Phố Trẻ Trung', ID_DanhMuc = 4, ChatLieu = N'100% Cotton twill dệt chéo', FormDang = N'Regular Fit', HinhAnh = N'img/sp-khaki-vangbe.jpg', GiaNhap = 195000, GiaBan = 430000, TrangThai = N'Còn hàng', MoTa = N'Tông màu be sáng năng động trẻ trung, dễ phối cùng áo thun và áo polo.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 47;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 48)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (48, 'SP048', N'Quần Khaki Nam DUNA Ống Suông Vải Dệt Chéo Dày Dặn', 4, N'DUNA', N'Cotton dệt chéo mật độ cao', N'Straight Fit', N'img/sp-khaki-ongsuong.jpg', 200000, 440000, 0, 0, 8, N'Còn hàng', N'Ống suông rộng rãi phóng khoáng, chất vải dày dặn giữ form bền bỉ theo năm tháng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP048', TenSP = N'Quần Khaki Nam DUNA Ống Suông Vải Dệt Chéo Dày Dặn', ID_DanhMuc = 4, ChatLieu = N'Cotton dệt chéo mật độ cao', FormDang = N'Straight Fit', HinhAnh = N'img/sp-khaki-ongsuong.jpg', GiaNhap = 200000, GiaBan = 440000, TrangThai = N'Còn hàng', MoTa = N'Ống suông rộng rãi phóng khoáng, chất vải dày dặn giữ form bền bỉ theo năm tháng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 48;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 49)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (49, 'SP049', N'Quần Khaki Nam DUNA Màu Xanh Rêu Quân Đội Cá Tính', 4, N'DUNA', N'98% Cotton, 2% Spandex co giãn', N'Slim Fit', N'img/sp-khaki-xanhreu.jpg', 210000, 460000, 0, 0, 8, N'Còn hàng', N'Sắc xanh rêu lính bụi bặm nam tính, phù hợp cho chàng trai yêu phong cách street casual.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP049', TenSP = N'Quần Khaki Nam DUNA Màu Xanh Rêu Quân Đội Cá Tính', ID_DanhMuc = 4, ChatLieu = N'98% Cotton, 2% Spandex co giãn', FormDang = N'Slim Fit', HinhAnh = N'img/sp-khaki-xanhreu.jpg', GiaNhap = 210000, GiaBan = 460000, TrangThai = N'Còn hàng', MoTa = N'Sắc xanh rêu lính bụi bặm nam tính, phù hợp cho chàng trai yêu phong cách street casual.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 49;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 50)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (50, 'SP050', N'Quần Khaki Nam DUNA Cạp Co Giãn Chống Nhăn Mềm Mại', 4, N'DUNA', N'Cotton Spandex wash enzyme mềm', N'Regular Fit', N'img/sp-khaki-capchun.jpg', 215000, 470000, 0, 0, 8, N'Còn hàng', N'Được xử lý giặt enzyme sinh học cho sợi vải siêu êm mềm ngay từ lần chạm đầu tiên.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP050', TenSP = N'Quần Khaki Nam DUNA Cạp Co Giãn Chống Nhăn Mềm Mại', ID_DanhMuc = 4, ChatLieu = N'Cotton Spandex wash enzyme mềm', FormDang = N'Regular Fit', HinhAnh = N'img/sp-khaki-capchun.jpg', GiaNhap = 215000, GiaBan = 470000, TrangThai = N'Còn hàng', MoTa = N'Được xử lý giặt enzyme sinh học cho sợi vải siêu êm mềm ngay từ lần chạm đầu tiên.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 50;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 51)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (51, 'SP051', N'Quần Khaki Nam DUNA Màu Đen Tuyền Bền Màu Giặt Máy', 4, N'DUNA', N'Cotton pha Poly Spandex nhuộm bền màu', N'Slim Fit', N'img/sp-khaki-den.jpg', 195000, 430000, 0, 0, 8, N'Còn hàng', N'Công nghệ nhuộm lưu huỳnh giữ màu đen tuyền lâu phai, giặt máy thoải mái không lo bạc màu.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP051', TenSP = N'Quần Khaki Nam DUNA Màu Đen Tuyền Bền Màu Giặt Máy', ID_DanhMuc = 4, ChatLieu = N'Cotton pha Poly Spandex nhuộm bền màu', FormDang = N'Slim Fit', HinhAnh = N'img/sp-khaki-den.jpg', GiaNhap = 195000, GiaBan = 430000, TrangThai = N'Còn hàng', MoTa = N'Công nghệ nhuộm lưu huỳnh giữ màu đen tuyền lâu phai, giặt máy thoải mái không lo bạc màu.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 51;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 52)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (52, 'SP052', N'Quần Khaki Nam DUNA Màu Xám Tro Phối Đồ Hiện Đại', 4, N'DUNA', N'98% Cotton chải, 2% Elastane', N'Slim Fit', N'img/sp-khaki-xamtro.jpg', 205000, 450000, 0, 0, 8, N'Còn hàng', N'Màu xám tro thanh lịch trung tính, phù hợp cả đi làm lẫn đi cà phê gặp gỡ bạn bè.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP052', TenSP = N'Quần Khaki Nam DUNA Màu Xám Tro Phối Đồ Hiện Đại', ID_DanhMuc = 4, ChatLieu = N'98% Cotton chải, 2% Elastane', FormDang = N'Slim Fit', HinhAnh = N'img/sp-khaki-xamtro.jpg', GiaNhap = 205000, GiaBan = 450000, TrangThai = N'Còn hàng', MoTa = N'Màu xám tro thanh lịch trung tính, phù hợp cả đi làm lẫn đi cà phê gặp gỡ bạn bè.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 52;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 53)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (53, 'SP053', N'Quần Khaki Chinos Nam DUNA Túi Hộp Nhẹ Phượt Dạo', 4, N'DUNA', N'Cotton Canvas mỏng nhẹ', N'Relaxed Fit', N'img/sp-khaki-tuihop.jpg', 220000, 480000, 0, 0, 8, N'Còn hàng', N'Túi hộp sườn tiện lợi để điện thoại ví tiền, phong cách dã ngoại năng động.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP053', TenSP = N'Quần Khaki Chinos Nam DUNA Túi Hộp Nhẹ Phượt Dạo', ID_DanhMuc = 4, ChatLieu = N'Cotton Canvas mỏng nhẹ', FormDang = N'Relaxed Fit', HinhAnh = N'img/sp-khaki-tuihop.jpg', GiaNhap = 220000, GiaBan = 480000, TrangThai = N'Còn hàng', MoTa = N'Túi hộp sườn tiện lợi để điện thoại ví tiền, phong cách dã ngoại năng động.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 53;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 54)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (54, 'SP054', N'Quần Khaki Nam DUNA Vải Sợi Bông Tự Nhiên Thoáng Khí', 4, N'DUNA', N'100% Bông tự nhiên trồng sinh thái', N'Regular Fit', N'img/sp-khaki-soibong.jpg', 210000, 460000, 0, 0, 8, N'Còn hàng', N'Thân thiện với làn da, thoáng khí và thấm mồ hôi tối đa trong ngày dài hoạt động.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP054', TenSP = N'Quần Khaki Nam DUNA Vải Sợi Bông Tự Nhiên Thoáng Khí', ID_DanhMuc = 4, ChatLieu = N'100% Bông tự nhiên trồng sinh thái', FormDang = N'Regular Fit', HinhAnh = N'img/sp-khaki-soibong.jpg', GiaNhap = 210000, GiaBan = 460000, TrangThai = N'Còn hàng', MoTa = N'Thân thiện với làn da, thoáng khí và thấm mồ hôi tối đa trong ngày dài hoạt động.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 54;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 55)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (55, 'SP055', N'Áo Blazer Nam DUNA Vải Linen Thoáng Mát Mùa Hè', 5, N'DUNA', N'100% Linen dệt thoáng khí, không lót đệm', N'Unstructured Fit', N'img/sp-blazer-linen.jpg', 530000, 1150000, 0, 0, 8, N'Còn hàng', N'Áo khoác blazer đũi không đệm vai, siêu nhẹ và mát mẻ khi diện cùng áo thun mùa hè.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP055', TenSP = N'Áo Blazer Nam DUNA Vải Linen Thoáng Mát Mùa Hè', ID_DanhMuc = 5, ChatLieu = N'100% Linen dệt thoáng khí, không lót đệm', FormDang = N'Unstructured Fit', HinhAnh = N'img/sp-blazer-linen.jpg', GiaNhap = 530000, GiaBan = 1150000, TrangThai = N'Còn hàng', MoTa = N'Áo khoác blazer đũi không đệm vai, siêu nhẹ và mát mẻ khi diện cùng áo thun mùa hè.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 55;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 56)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (56, 'SP056', N'Áo Blazer Nam Kẻ Ô Glen Plaid Phong Cách Anh Quốc DUNA', 5, N'DUNA', N'70% Wool pha Poly dệt kẻ ô', N'Tailored Fit', N'img/sp-blazer-glenplaid.jpg', 720000, 1550000, 0, 0, 8, N'Còn hàng', N'Họa tiết kẻ Glen Plaid phong cách quý tộc Anh Quốc, ve áo cổ điển tinh tế.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP056', TenSP = N'Áo Blazer Nam Kẻ Ô Glen Plaid Phong Cách Anh Quốc DUNA', ID_DanhMuc = 5, ChatLieu = N'70% Wool pha Poly dệt kẻ ô', FormDang = N'Tailored Fit', HinhAnh = N'img/sp-blazer-glenplaid.jpg', GiaNhap = 720000, GiaBan = 1550000, TrangThai = N'Còn hàng', MoTa = N'Họa tiết kẻ Glen Plaid phong cách quý tộc Anh Quốc, ve áo cổ điển tinh tế.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 56;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 57)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (57, 'SP057', N'Áo Blazer Nam Màu Xanh Navy Đệm Vai Mỏng Tự Nhiên DUNA', 5, N'DUNA', N'Poly Rayon lót lụa cao cấp', N'Modern Fit', N'img/sp-blazer-navymong.jpg', 620000, 1350000, 0, 0, 8, N'Còn hàng', N'Màu xanh navy chuẩn mực, đệm vai mỏng tạo đường nét bờ vai tự nhiên vững chãi.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP057', TenSP = N'Áo Blazer Nam Màu Xanh Navy Đệm Vai Mỏng Tự Nhiên DUNA', ID_DanhMuc = 5, ChatLieu = N'Poly Rayon lót lụa cao cấp', FormDang = N'Modern Fit', HinhAnh = N'img/sp-blazer-navymong.jpg', GiaNhap = 620000, GiaBan = 1350000, TrangThai = N'Còn hàng', MoTa = N'Màu xanh navy chuẩn mực, đệm vai mỏng tạo đường nét bờ vai tự nhiên vững chãi.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 57;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 58)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (58, 'SP058', N'Áo Blazer Nam Màu Đen Ve Nhọn Single-Breasted DUNA', 5, N'DUNA', N'Wool Poly pha Spandex chống nhăn', N'Slim Tailored', N'img/sp-blazer-denvenhon.jpg', 650000, 1400000, 0, 0, 8, N'Còn hàng', N'Ve nhọn quyền lực, khuy sừng sang trọng, hoàn hảo cho các buổi ký kết hợp đồng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP058', TenSP = N'Áo Blazer Nam Màu Đen Ve Nhọn Single-Breasted DUNA', ID_DanhMuc = 5, ChatLieu = N'Wool Poly pha Spandex chống nhăn', FormDang = N'Slim Tailored', HinhAnh = N'img/sp-blazer-denvenhon.jpg', GiaNhap = 650000, GiaBan = 1400000, TrangThai = N'Còn hàng', MoTa = N'Ve nhọn quyền lực, khuy sừng sang trọng, hoàn hảo cho các buổi ký kết hợp đồng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 58;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 59)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (59, 'SP059', N'Áo Blazer Nam Casual Dệt Tổ Ong Không Nhăn DUNA', 5, N'DUNA', N'Jersey Knit dệt kim co giãn 4 chiều', N'Casual Fit', N'img/sp-blazer-toong.jpg', 450000, 980000, 0, 0, 8, N'Còn hàng', N'Chất liệu dệt kim không nhăn gập gọn bỏ balo công tác thoải mái, phong cách trẻ trung.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP059', TenSP = N'Áo Blazer Nam Casual Dệt Tổ Ong Không Nhăn DUNA', ID_DanhMuc = 5, ChatLieu = N'Jersey Knit dệt kim co giãn 4 chiều', FormDang = N'Casual Fit', HinhAnh = N'img/sp-blazer-toong.jpg', GiaNhap = 450000, GiaBan = 980000, TrangThai = N'Còn hàng', MoTa = N'Chất liệu dệt kim không nhăn gập gọn bỏ balo công tác thoải mái, phong cách trẻ trung.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 59;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 60)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (60, 'SP060', N'Áo Blazer Nam Màu Nâu Camel Dạ Ép Mùa Đông DUNA', 5, N'DUNA', N'Dạ lông cừu ép mịn giữ ấm', N'Classic Fit', N'img/sp-blazer-nau-camel.jpg', 780000, 1650000, 0, 0, 8, N'Còn hàng', N'Sắc nâu lạc đà Camel ấm áp thượng lưu, chất dạ ép giữ nhiệt tuyệt vời.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP060', TenSP = N'Áo Blazer Nam Màu Nâu Camel Dạ Ép Mùa Đông DUNA', ID_DanhMuc = 5, ChatLieu = N'Dạ lông cừu ép mịn giữ ấm', FormDang = N'Classic Fit', HinhAnh = N'img/sp-blazer-nau-camel.jpg', GiaNhap = 780000, GiaBan = 1650000, TrangThai = N'Còn hàng', MoTa = N'Sắc nâu lạc đà Camel ấm áp thượng lưu, chất dạ ép giữ nhiệt tuyệt vời.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 60;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 61)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (61, 'SP061', N'Áo Khoác Suit Nam DUNA 2 Hàng Khuy Double-Breasted', 5, N'DUNA', N'Wool Blend chải kỹ bóng nhẹ', N'Tailored Fit', N'img/sp-suit-double.jpg', 740000, 1580000, 0, 0, 8, N'Còn hàng', N'Kiểu dáng 6 khuy cài đúp phong cách Ý cổ điển, khẳng định đẳng cấp thủ lĩnh.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP061', TenSP = N'Áo Khoác Suit Nam DUNA 2 Hàng Khuy Double-Breasted', ID_DanhMuc = 5, ChatLieu = N'Wool Blend chải kỹ bóng nhẹ', FormDang = N'Tailored Fit', HinhAnh = N'img/sp-suit-double.jpg', GiaNhap = 740000, GiaBan = 1580000, TrangThai = N'Còn hàng', MoTa = N'Kiểu dáng 6 khuy cài đúp phong cách Ý cổ điển, khẳng định đẳng cấp thủ lĩnh.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 61;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 62)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (62, 'SP062', N'Áo Blazer Nam Màu Xám Ghi Họa Tiết Houndstooth DUNA', 5, N'DUNA', N'Poly Wool dệt họa tiết nanh sói nhỏ', N'Modern Fit', N'img/sp-blazer-houndstooth.jpg', 690000, 1480000, 0, 0, 8, N'Còn hàng', N'Họa tiết nanh sói Houndstooth cổ điển bất hủ, tạo điểm nhấn cá tính thanh lịch.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP062', TenSP = N'Áo Blazer Nam Màu Xám Ghi Họa Tiết Houndstooth DUNA', ID_DanhMuc = 5, ChatLieu = N'Poly Wool dệt họa tiết nanh sói nhỏ', FormDang = N'Modern Fit', HinhAnh = N'img/sp-blazer-houndstooth.jpg', GiaNhap = 690000, GiaBan = 1480000, TrangThai = N'Còn hàng', MoTa = N'Họa tiết nanh sói Houndstooth cổ điển bất hủ, tạo điểm nhấn cá tính thanh lịch.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 62;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 63)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (63, 'SP063', N'Áo Blazer Nam DUNA Dáng Suông Phong Cách Minimalist', 5, N'DUNA', N'Cotton Rayon phủ bóng nhẹ', N'Loose Relaxed', N'img/sp-blazer-minimalist.jpg', 580000, 1250000, 0, 0, 8, N'Còn hàng', N'Phom suông hiện đại xu hướng thời trang Hàn Quốc, kết hợp tuyệt vời với quần âu suông.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP063', TenSP = N'Áo Blazer Nam DUNA Dáng Suông Phong Cách Minimalist', ID_DanhMuc = 5, ChatLieu = N'Cotton Rayon phủ bóng nhẹ', FormDang = N'Loose Relaxed', HinhAnh = N'img/sp-blazer-minimalist.jpg', GiaNhap = 580000, GiaBan = 1250000, TrangThai = N'Còn hàng', MoTa = N'Phom suông hiện đại xu hướng thời trang Hàn Quốc, kết hợp tuyệt vời với quần âu suông.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 63;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 64)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (64, 'SP064', N'Ví Da Nam Bifold DUNA Da Bò Thật Sáp Ngựa Điên', 6, N'DUNA', N'100% Da bò sáp Crazy Horse cao cấp', N'Bản gập đôi Bifold', N'img/sp-vi-da.jpg', 175000, 390000, 0, 0, 8, N'Còn hàng', N'Chất da sáp càng dùng càng bóng đẹp, nhiều ngăn đựng thẻ ATM và tiền mặt gọn gàng.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP064', TenSP = N'Ví Da Nam Bifold DUNA Da Bò Thật Sáp Ngựa Điên', ID_DanhMuc = 6, ChatLieu = N'100% Da bò sáp Crazy Horse cao cấp', FormDang = N'Bản gập đôi Bifold', HinhAnh = N'img/sp-vi-da.jpg', GiaNhap = 175000, GiaBan = 390000, TrangThai = N'Còn hàng', MoTa = N'Chất da sáp càng dùng càng bóng đẹp, nhiều ngăn đựng thẻ ATM và tiền mặt gọn gàng.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 64;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 65)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (65, 'SP065', N'Cà Vạt Lụa Nam DUNA Bản 7cm Họa Tiết Kẻ Chìm', 6, N'DUNA', N'100% Lụa tơ tằm Mulberry dệt jacquard', N'Bản 7cm chuẩn hiện đại', N'img/sp-cavat-lua.jpg', 95000, 220000, 0, 0, 8, N'Còn hàng', N'Lụa dệt bóng mờ sang trọng, độ rủ chuẩn mực, không nhăn gãy khi thắt nút.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP065', TenSP = N'Cà Vạt Lụa Nam DUNA Bản 7cm Họa Tiết Kẻ Chìm', ID_DanhMuc = 6, ChatLieu = N'100% Lụa tơ tằm Mulberry dệt jacquard', FormDang = N'Bản 7cm chuẩn hiện đại', HinhAnh = N'img/sp-cavat-lua.jpg', GiaNhap = 95000, GiaBan = 220000, TrangThai = N'Còn hàng', MoTa = N'Lụa dệt bóng mờ sang trọng, độ rủ chuẩn mực, không nhăn gãy khi thắt nút.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 65;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 66)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (66, 'SP066', N'Nơ Đeo Cổ Tuxedo Nam Lụa DUNA Lịch Lãm', 6, N'DUNA', N'Lụa bóng cao cấp may hai lớp', N'Dây tăng giảm cổ', N'img/sp-no-tuxedo.jpg', 65000, 150000, 0, 0, 8, N'Còn hàng', N'Phụ kiện trang trọng cho tiệc cưới và sự kiện thảm đỏ, dây đeo tùy chỉnh tiện lợi.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP066', TenSP = N'Nơ Đeo Cổ Tuxedo Nam Lụa DUNA Lịch Lãm', ID_DanhMuc = 6, ChatLieu = N'Lụa bóng cao cấp may hai lớp', FormDang = N'Dây tăng giảm cổ', HinhAnh = N'img/sp-no-tuxedo.jpg', GiaNhap = 65000, GiaBan = 150000, TrangThai = N'Còn hàng', MoTa = N'Phụ kiện trang trọng cho tiệc cưới và sự kiện thảm đỏ, dây đeo tùy chỉnh tiện lợi.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 66;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 67)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (67, 'SP067', N'Set 5 Đôi Tất Khử Mùi Nano Bạc DUNA Cotton', 6, N'DUNA', N'85% Cotton chải, 15% Sợi Nano bạc', N'Cổ trung qua mắt cá', N'img/sp-set-tat.jpg', 50000, 120000, 0, 0, 8, N'Còn hàng', N'Phủ hạt nano bạc kháng khuẩn khử mùi hôi chân hiệu quả, thun dệt êm ái không hằn da.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP067', TenSP = N'Set 5 Đôi Tất Khử Mùi Nano Bạc DUNA Cotton', ID_DanhMuc = 6, ChatLieu = N'85% Cotton chải, 15% Sợi Nano bạc', FormDang = N'Cổ trung qua mắt cá', HinhAnh = N'img/sp-set-tat.jpg', GiaNhap = 50000, GiaBan = 120000, TrangThai = N'Còn hàng', MoTa = N'Phủ hạt nano bạc kháng khuẩn khử mùi hôi chân hiệu quả, thun dệt êm ái không hằn da.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 67;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 68)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (68, 'SP068', N'Kẹp Cà Vạt Kim Loại Mạ Bạc Cao Cấp DUNA', 6, N'DUNA', N'Hợp kim mạ Rhodium chống oxy hóa', N'Chiều dài 5.5cm', N'img/sp-kep-cavat.jpg', 45000, 110000, 0, 0, 8, N'Còn hàng', N'Thiết kế tối giản mạ bạc sáng bóng, lò xo kẹp chắc chắn giữ cà vạt ngay ngắn.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP068', TenSP = N'Kẹp Cà Vạt Kim Loại Mạ Bạc Cao Cấp DUNA', ID_DanhMuc = 6, ChatLieu = N'Hợp kim mạ Rhodium chống oxy hóa', FormDang = N'Chiều dài 5.5cm', HinhAnh = N'img/sp-kep-cavat.jpg', GiaNhap = 45000, GiaBan = 110000, TrangThai = N'Còn hàng', MoTa = N'Thiết kế tối giản mạ bạc sáng bóng, lò xo kẹp chắc chắn giữ cà vạt ngay ngắn.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 68;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 69)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (69, 'SP069', N'Khuy Cài Măng Sét Áo Sơ Mi DUNA Cufflinks Tinh Tế', 6, N'DUNA', N'Đá mã não đen bọc viền mạ bạc', N'Khuy xoay tiêu chuẩn', N'img/sp-cufflinks.jpg', 80000, 180000, 0, 0, 8, N'Còn hàng', N'Điểm nhấn quý ông ở cổ tay áo sơ mi, đá đen sang trọng thu hút ánh nhìn.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP069', TenSP = N'Khuy Cài Măng Sét Áo Sơ Mi DUNA Cufflinks Tinh Tế', ID_DanhMuc = 6, ChatLieu = N'Đá mã não đen bọc viền mạ bạc', FormDang = N'Khuy xoay tiêu chuẩn', HinhAnh = N'img/sp-cufflinks.jpg', GiaNhap = 80000, GiaBan = 180000, TrangThai = N'Còn hàng', MoTa = N'Điểm nhấn quý ông ở cổ tay áo sơ mi, đá đen sang trọng thu hút ánh nhìn.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 69;
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = 70)
    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES (70, 'SP070', N'Túi Đeo Chéo Da Nam DUNA Compact Đi Làm Dạo Phố', 6, N'DUNA', N'Da PU vân hạt cao cấp chống nước', N'Kích thước 26x18x7cm', N'img/sp-tui-cheo.jpg', 310000, 650000, 0, 0, 8, N'Còn hàng', N'Thiết kế nhỏ gọn chứa vừa iPad mini, ví tiền, chìa khóa; chống thấm nước đi mưa nhẹ.', GETDATE(), GETDATE());
ELSE
    UPDATE SanPham SET MaSP = 'SP070', TenSP = N'Túi Đeo Chéo Da Nam DUNA Compact Đi Làm Dạo Phố', ID_DanhMuc = 6, ChatLieu = N'Da PU vân hạt cao cấp chống nước', FormDang = N'Kích thước 26x18x7cm', HinhAnh = N'img/sp-tui-cheo.jpg', GiaNhap = 310000, GiaBan = 650000, TrangThai = N'Còn hàng', MoTa = N'Thiết kế nhỏ gọn chứa vừa iPad mini, ví tiền, chìa khóa; chống thấm nước đi mưa nhẹ.', NgayCapNhat = GETDATE() WHERE ID_SanPham = 70;
SET IDENTITY_INSERT SanPham OFF;
GO

-- 4. THÊM BIẾN THỂ (SIZE / MÀU / TỒN KHO / BARCODE) CHO SẢN PHẨM TỪ SP007 ĐẾN SP070
SET IDENTITY_INSERT BienTheSanPham ON;
GO
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 19)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (19, 'BT019', 7, N'Trắng', 'S', 250, 'g', '1', NULL, 195000, 420000, 20, 'SP007-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT019', ID_SanPham = 7, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 195000, GiaBan = 420000, SoLuong = 20, Barcode = 'SP007-WHT-S' WHERE ID_BienThe = 19;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 20)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (20, 'BT020', 7, N'Trắng', 'M', 250, 'g', '1', NULL, 195000, 420000, 30, 'SP007-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT020', ID_SanPham = 7, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 195000, GiaBan = 420000, SoLuong = 30, Barcode = 'SP007-WHT-M' WHERE ID_BienThe = 20;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 21)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (21, 'BT021', 7, N'Trắng', 'L', 250, 'g', '1', NULL, 195000, 420000, 25, 'SP007-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT021', ID_SanPham = 7, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 195000, GiaBan = 420000, SoLuong = 25, Barcode = 'SP007-WHT-L' WHERE ID_BienThe = 21;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 22)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (22, 'BT022', 7, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 195000, 420000, 15, 'SP007-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT022', ID_SanPham = 7, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 195000, GiaBan = 420000, SoLuong = 15, Barcode = 'SP007-BLU-M' WHERE ID_BienThe = 22;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 23)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (23, 'BT023', 8, N'Trắng', 'S', 250, 'g', '1', NULL, 215000, 460000, 20, 'SP008-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT023', ID_SanPham = 8, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 215000, GiaBan = 460000, SoLuong = 20, Barcode = 'SP008-WHT-S' WHERE ID_BienThe = 23;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 24)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (24, 'BT024', 8, N'Trắng', 'M', 250, 'g', '1', NULL, 215000, 460000, 30, 'SP008-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT024', ID_SanPham = 8, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 215000, GiaBan = 460000, SoLuong = 30, Barcode = 'SP008-WHT-M' WHERE ID_BienThe = 24;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 25)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (25, 'BT025', 8, N'Trắng', 'L', 250, 'g', '1', NULL, 215000, 460000, 25, 'SP008-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT025', ID_SanPham = 8, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 215000, GiaBan = 460000, SoLuong = 25, Barcode = 'SP008-WHT-L' WHERE ID_BienThe = 25;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 26)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (26, 'BT026', 8, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 215000, 460000, 15, 'SP008-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT026', ID_SanPham = 8, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 215000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP008-BLU-M' WHERE ID_BienThe = 26;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 27)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (27, 'BT027', 9, N'Trắng', 'S', 250, 'g', '1', NULL, 225000, 480000, 20, 'SP009-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT027', ID_SanPham = 9, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 225000, GiaBan = 480000, SoLuong = 20, Barcode = 'SP009-WHT-S' WHERE ID_BienThe = 27;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 28)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (28, 'BT028', 9, N'Trắng', 'M', 250, 'g', '1', NULL, 225000, 480000, 30, 'SP009-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT028', ID_SanPham = 9, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 225000, GiaBan = 480000, SoLuong = 30, Barcode = 'SP009-WHT-M' WHERE ID_BienThe = 28;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 29)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (29, 'BT029', 9, N'Trắng', 'L', 250, 'g', '1', NULL, 225000, 480000, 25, 'SP009-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT029', ID_SanPham = 9, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 225000, GiaBan = 480000, SoLuong = 25, Barcode = 'SP009-WHT-L' WHERE ID_BienThe = 29;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 30)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (30, 'BT030', 9, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 225000, 480000, 15, 'SP009-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT030', ID_SanPham = 9, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 225000, GiaBan = 480000, SoLuong = 15, Barcode = 'SP009-BLU-M' WHERE ID_BienThe = 30;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 31)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (31, 'BT031', 10, N'Trắng', 'S', 250, 'g', '1', NULL, 240000, 520000, 20, 'SP010-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT031', ID_SanPham = 10, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 240000, GiaBan = 520000, SoLuong = 20, Barcode = 'SP010-WHT-S' WHERE ID_BienThe = 31;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 32)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (32, 'BT032', 10, N'Trắng', 'M', 250, 'g', '1', NULL, 240000, 520000, 30, 'SP010-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT032', ID_SanPham = 10, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 240000, GiaBan = 520000, SoLuong = 30, Barcode = 'SP010-WHT-M' WHERE ID_BienThe = 32;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 33)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (33, 'BT033', 10, N'Trắng', 'L', 250, 'g', '1', NULL, 240000, 520000, 25, 'SP010-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT033', ID_SanPham = 10, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 240000, GiaBan = 520000, SoLuong = 25, Barcode = 'SP010-WHT-L' WHERE ID_BienThe = 33;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 34)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (34, 'BT034', 10, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 240000, 520000, 15, 'SP010-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT034', ID_SanPham = 10, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 240000, GiaBan = 520000, SoLuong = 15, Barcode = 'SP010-BLU-M' WHERE ID_BienThe = 34;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 35)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (35, 'BT035', 11, N'Trắng', 'S', 250, 'g', '1', NULL, 230000, 490000, 20, 'SP011-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT035', ID_SanPham = 11, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 230000, GiaBan = 490000, SoLuong = 20, Barcode = 'SP011-WHT-S' WHERE ID_BienThe = 35;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 36)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (36, 'BT036', 11, N'Trắng', 'M', 250, 'g', '1', NULL, 230000, 490000, 30, 'SP011-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT036', ID_SanPham = 11, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 230000, GiaBan = 490000, SoLuong = 30, Barcode = 'SP011-WHT-M' WHERE ID_BienThe = 36;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 37)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (37, 'BT037', 11, N'Trắng', 'L', 250, 'g', '1', NULL, 230000, 490000, 25, 'SP011-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT037', ID_SanPham = 11, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 230000, GiaBan = 490000, SoLuong = 25, Barcode = 'SP011-WHT-L' WHERE ID_BienThe = 37;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 38)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (38, 'BT038', 11, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 230000, 490000, 15, 'SP011-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT038', ID_SanPham = 11, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 230000, GiaBan = 490000, SoLuong = 15, Barcode = 'SP011-BLU-M' WHERE ID_BienThe = 38;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 39)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (39, 'BT039', 12, N'Trắng', 'S', 250, 'g', '1', NULL, 185000, 390000, 20, 'SP012-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT039', ID_SanPham = 12, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 185000, GiaBan = 390000, SoLuong = 20, Barcode = 'SP012-WHT-S' WHERE ID_BienThe = 39;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 40)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (40, 'BT040', 12, N'Trắng', 'M', 250, 'g', '1', NULL, 185000, 390000, 30, 'SP012-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT040', ID_SanPham = 12, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 185000, GiaBan = 390000, SoLuong = 30, Barcode = 'SP012-WHT-M' WHERE ID_BienThe = 40;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 41)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (41, 'BT041', 12, N'Trắng', 'L', 250, 'g', '1', NULL, 185000, 390000, 25, 'SP012-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT041', ID_SanPham = 12, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 185000, GiaBan = 390000, SoLuong = 25, Barcode = 'SP012-WHT-L' WHERE ID_BienThe = 41;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 42)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (42, 'BT042', 12, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 185000, 390000, 15, 'SP012-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT042', ID_SanPham = 12, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 185000, GiaBan = 390000, SoLuong = 15, Barcode = 'SP012-BLU-M' WHERE ID_BienThe = 42;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 43)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (43, 'BT043', 13, N'Trắng', 'S', 250, 'g', '1', NULL, 220000, 470000, 20, 'SP013-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT043', ID_SanPham = 13, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 220000, GiaBan = 470000, SoLuong = 20, Barcode = 'SP013-WHT-S' WHERE ID_BienThe = 43;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 44)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (44, 'BT044', 13, N'Trắng', 'M', 250, 'g', '1', NULL, 220000, 470000, 30, 'SP013-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT044', ID_SanPham = 13, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 220000, GiaBan = 470000, SoLuong = 30, Barcode = 'SP013-WHT-M' WHERE ID_BienThe = 44;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 45)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (45, 'BT045', 13, N'Trắng', 'L', 250, 'g', '1', NULL, 220000, 470000, 25, 'SP013-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT045', ID_SanPham = 13, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 220000, GiaBan = 470000, SoLuong = 25, Barcode = 'SP013-WHT-L' WHERE ID_BienThe = 45;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 46)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (46, 'BT046', 13, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 220000, 470000, 15, 'SP013-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT046', ID_SanPham = 13, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 220000, GiaBan = 470000, SoLuong = 15, Barcode = 'SP013-BLU-M' WHERE ID_BienThe = 46;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 47)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (47, 'BT047', 14, N'Trắng', 'S', 250, 'g', '1', NULL, 260000, 560000, 20, 'SP014-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT047', ID_SanPham = 14, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 260000, GiaBan = 560000, SoLuong = 20, Barcode = 'SP014-WHT-S' WHERE ID_BienThe = 47;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 48)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (48, 'BT048', 14, N'Trắng', 'M', 250, 'g', '1', NULL, 260000, 560000, 30, 'SP014-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT048', ID_SanPham = 14, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 260000, GiaBan = 560000, SoLuong = 30, Barcode = 'SP014-WHT-M' WHERE ID_BienThe = 48;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 49)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (49, 'BT049', 14, N'Trắng', 'L', 250, 'g', '1', NULL, 260000, 560000, 25, 'SP014-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT049', ID_SanPham = 14, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 260000, GiaBan = 560000, SoLuong = 25, Barcode = 'SP014-WHT-L' WHERE ID_BienThe = 49;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 50)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (50, 'BT050', 14, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 260000, 560000, 15, 'SP014-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT050', ID_SanPham = 14, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 260000, GiaBan = 560000, SoLuong = 15, Barcode = 'SP014-BLU-M' WHERE ID_BienThe = 50;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 51)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (51, 'BT051', 15, N'Trắng', 'S', 250, 'g', '1', NULL, 205000, 440000, 20, 'SP015-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT051', ID_SanPham = 15, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 205000, GiaBan = 440000, SoLuong = 20, Barcode = 'SP015-WHT-S' WHERE ID_BienThe = 51;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 52)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (52, 'BT052', 15, N'Trắng', 'M', 250, 'g', '1', NULL, 205000, 440000, 30, 'SP015-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT052', ID_SanPham = 15, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 205000, GiaBan = 440000, SoLuong = 30, Barcode = 'SP015-WHT-M' WHERE ID_BienThe = 52;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 53)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (53, 'BT053', 15, N'Trắng', 'L', 250, 'g', '1', NULL, 205000, 440000, 25, 'SP015-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT053', ID_SanPham = 15, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 205000, GiaBan = 440000, SoLuong = 25, Barcode = 'SP015-WHT-L' WHERE ID_BienThe = 53;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 54)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (54, 'BT054', 15, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 205000, 440000, 15, 'SP015-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT054', ID_SanPham = 15, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 205000, GiaBan = 440000, SoLuong = 15, Barcode = 'SP015-BLU-M' WHERE ID_BienThe = 54;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 55)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (55, 'BT055', 16, N'Trắng', 'S', 250, 'g', '1', NULL, 210000, 450000, 20, 'SP016-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT055', ID_SanPham = 16, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 210000, GiaBan = 450000, SoLuong = 20, Barcode = 'SP016-WHT-S' WHERE ID_BienThe = 55;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 56)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (56, 'BT056', 16, N'Trắng', 'M', 250, 'g', '1', NULL, 210000, 450000, 30, 'SP016-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT056', ID_SanPham = 16, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 210000, GiaBan = 450000, SoLuong = 30, Barcode = 'SP016-WHT-M' WHERE ID_BienThe = 56;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 57)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (57, 'BT057', 16, N'Trắng', 'L', 250, 'g', '1', NULL, 210000, 450000, 25, 'SP016-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT057', ID_SanPham = 16, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 210000, GiaBan = 450000, SoLuong = 25, Barcode = 'SP016-WHT-L' WHERE ID_BienThe = 57;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 58)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (58, 'BT058', 16, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 210000, 450000, 15, 'SP016-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT058', ID_SanPham = 16, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 210000, GiaBan = 450000, SoLuong = 15, Barcode = 'SP016-BLU-M' WHERE ID_BienThe = 58;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 59)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (59, 'BT059', 17, N'Trắng', 'S', 250, 'g', '1', NULL, 235000, 500000, 20, 'SP017-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT059', ID_SanPham = 17, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 235000, GiaBan = 500000, SoLuong = 20, Barcode = 'SP017-WHT-S' WHERE ID_BienThe = 59;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 60)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (60, 'BT060', 17, N'Trắng', 'M', 250, 'g', '1', NULL, 235000, 500000, 30, 'SP017-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT060', ID_SanPham = 17, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 235000, GiaBan = 500000, SoLuong = 30, Barcode = 'SP017-WHT-M' WHERE ID_BienThe = 60;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 61)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (61, 'BT061', 17, N'Trắng', 'L', 250, 'g', '1', NULL, 235000, 500000, 25, 'SP017-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT061', ID_SanPham = 17, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 235000, GiaBan = 500000, SoLuong = 25, Barcode = 'SP017-WHT-L' WHERE ID_BienThe = 61;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 62)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (62, 'BT062', 17, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 235000, 500000, 15, 'SP017-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT062', ID_SanPham = 17, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 235000, GiaBan = 500000, SoLuong = 15, Barcode = 'SP017-BLU-M' WHERE ID_BienThe = 62;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 63)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (63, 'BT063', 18, N'Trắng', 'S', 250, 'g', '1', NULL, 200000, 430000, 20, 'SP018-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT063', ID_SanPham = 18, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 200000, GiaBan = 430000, SoLuong = 20, Barcode = 'SP018-WHT-S' WHERE ID_BienThe = 63;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 64)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (64, 'BT064', 18, N'Trắng', 'M', 250, 'g', '1', NULL, 200000, 430000, 30, 'SP018-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT064', ID_SanPham = 18, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 200000, GiaBan = 430000, SoLuong = 30, Barcode = 'SP018-WHT-M' WHERE ID_BienThe = 64;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 65)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (65, 'BT065', 18, N'Trắng', 'L', 250, 'g', '1', NULL, 200000, 430000, 25, 'SP018-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT065', ID_SanPham = 18, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 200000, GiaBan = 430000, SoLuong = 25, Barcode = 'SP018-WHT-L' WHERE ID_BienThe = 65;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 66)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (66, 'BT066', 18, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 200000, 430000, 15, 'SP018-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT066', ID_SanPham = 18, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 200000, GiaBan = 430000, SoLuong = 15, Barcode = 'SP018-BLU-M' WHERE ID_BienThe = 66;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 67)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (67, 'BT067', 19, N'Trắng', 'S', 250, 'g', '1', NULL, 215000, 460000, 20, 'SP019-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT067', ID_SanPham = 19, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 215000, GiaBan = 460000, SoLuong = 20, Barcode = 'SP019-WHT-S' WHERE ID_BienThe = 67;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 68)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (68, 'BT068', 19, N'Trắng', 'M', 250, 'g', '1', NULL, 215000, 460000, 30, 'SP019-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT068', ID_SanPham = 19, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 215000, GiaBan = 460000, SoLuong = 30, Barcode = 'SP019-WHT-M' WHERE ID_BienThe = 68;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 69)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (69, 'BT069', 19, N'Trắng', 'L', 250, 'g', '1', NULL, 215000, 460000, 25, 'SP019-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT069', ID_SanPham = 19, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 215000, GiaBan = 460000, SoLuong = 25, Barcode = 'SP019-WHT-L' WHERE ID_BienThe = 69;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 70)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (70, 'BT070', 19, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 215000, 460000, 15, 'SP019-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT070', ID_SanPham = 19, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 215000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP019-BLU-M' WHERE ID_BienThe = 70;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 71)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (71, 'BT071', 20, N'Trắng', 'S', 250, 'g', '1', NULL, 175000, 380000, 20, 'SP020-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT071', ID_SanPham = 20, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 175000, GiaBan = 380000, SoLuong = 20, Barcode = 'SP020-WHT-S' WHERE ID_BienThe = 71;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 72)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (72, 'BT072', 20, N'Trắng', 'M', 250, 'g', '1', NULL, 175000, 380000, 30, 'SP020-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT072', ID_SanPham = 20, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 175000, GiaBan = 380000, SoLuong = 30, Barcode = 'SP020-WHT-M' WHERE ID_BienThe = 72;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 73)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (73, 'BT073', 20, N'Trắng', 'L', 250, 'g', '1', NULL, 175000, 380000, 25, 'SP020-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT073', ID_SanPham = 20, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 175000, GiaBan = 380000, SoLuong = 25, Barcode = 'SP020-WHT-L' WHERE ID_BienThe = 73;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 74)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (74, 'BT074', 20, N'Xanh Baby', 'M', 250, 'g', '1', NULL, 175000, 380000, 15, 'SP020-BLU-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT074', ID_SanPham = 20, MauSac = N'Xanh Baby', TrongLuong = 'M', GiaNhap = 175000, GiaBan = 380000, SoLuong = 15, Barcode = 'SP020-BLU-M' WHERE ID_BienThe = 74;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 75)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (75, 'BT075', 21, N'Trắng', 'S', 250, 'g', '1', NULL, 160000, 350000, 20, 'SP021-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT075', ID_SanPham = 21, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 160000, GiaBan = 350000, SoLuong = 20, Barcode = 'SP021-WHT-S' WHERE ID_BienThe = 75;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 76)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (76, 'BT076', 21, N'Trắng', 'M', 250, 'g', '1', NULL, 160000, 350000, 30, 'SP021-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT076', ID_SanPham = 21, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 160000, GiaBan = 350000, SoLuong = 30, Barcode = 'SP021-WHT-M' WHERE ID_BienThe = 76;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 77)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (77, 'BT077', 21, N'Trắng', 'L', 250, 'g', '1', NULL, 160000, 350000, 25, 'SP021-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT077', ID_SanPham = 21, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 160000, GiaBan = 350000, SoLuong = 25, Barcode = 'SP021-WHT-L' WHERE ID_BienThe = 77;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 78)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (78, 'BT078', 21, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 160000, 350000, 15, 'SP021-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT078', ID_SanPham = 21, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 160000, GiaBan = 350000, SoLuong = 15, Barcode = 'SP021-NVY-M' WHERE ID_BienThe = 78;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 79)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (79, 'BT079', 22, N'Trắng', 'S', 250, 'g', '1', NULL, 170000, 370000, 20, 'SP022-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT079', ID_SanPham = 22, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 170000, GiaBan = 370000, SoLuong = 20, Barcode = 'SP022-WHT-S' WHERE ID_BienThe = 79;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 80)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (80, 'BT080', 22, N'Trắng', 'M', 250, 'g', '1', NULL, 170000, 370000, 30, 'SP022-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT080', ID_SanPham = 22, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 170000, GiaBan = 370000, SoLuong = 30, Barcode = 'SP022-WHT-M' WHERE ID_BienThe = 80;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 81)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (81, 'BT081', 22, N'Trắng', 'L', 250, 'g', '1', NULL, 170000, 370000, 25, 'SP022-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT081', ID_SanPham = 22, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 170000, GiaBan = 370000, SoLuong = 25, Barcode = 'SP022-WHT-L' WHERE ID_BienThe = 81;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 82)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (82, 'BT082', 22, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 170000, 370000, 15, 'SP022-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT082', ID_SanPham = 22, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 170000, GiaBan = 370000, SoLuong = 15, Barcode = 'SP022-NVY-M' WHERE ID_BienThe = 82;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 83)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (83, 'BT083', 23, N'Trắng', 'S', 250, 'g', '1', NULL, 190000, 420000, 20, 'SP023-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT083', ID_SanPham = 23, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 190000, GiaBan = 420000, SoLuong = 20, Barcode = 'SP023-WHT-S' WHERE ID_BienThe = 83;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 84)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (84, 'BT084', 23, N'Trắng', 'M', 250, 'g', '1', NULL, 190000, 420000, 30, 'SP023-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT084', ID_SanPham = 23, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 190000, GiaBan = 420000, SoLuong = 30, Barcode = 'SP023-WHT-M' WHERE ID_BienThe = 84;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 85)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (85, 'BT085', 23, N'Trắng', 'L', 250, 'g', '1', NULL, 190000, 420000, 25, 'SP023-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT085', ID_SanPham = 23, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 190000, GiaBan = 420000, SoLuong = 25, Barcode = 'SP023-WHT-L' WHERE ID_BienThe = 85;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 86)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (86, 'BT086', 23, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 190000, 420000, 15, 'SP023-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT086', ID_SanPham = 23, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 190000, GiaBan = 420000, SoLuong = 15, Barcode = 'SP023-NVY-M' WHERE ID_BienThe = 86;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 87)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (87, 'BT087', 24, N'Trắng', 'S', 250, 'g', '1', NULL, 205000, 450000, 20, 'SP024-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT087', ID_SanPham = 24, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 205000, GiaBan = 450000, SoLuong = 20, Barcode = 'SP024-WHT-S' WHERE ID_BienThe = 87;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 88)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (88, 'BT088', 24, N'Trắng', 'M', 250, 'g', '1', NULL, 205000, 450000, 30, 'SP024-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT088', ID_SanPham = 24, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 205000, GiaBan = 450000, SoLuong = 30, Barcode = 'SP024-WHT-M' WHERE ID_BienThe = 88;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 89)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (89, 'BT089', 24, N'Trắng', 'L', 250, 'g', '1', NULL, 205000, 450000, 25, 'SP024-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT089', ID_SanPham = 24, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 205000, GiaBan = 450000, SoLuong = 25, Barcode = 'SP024-WHT-L' WHERE ID_BienThe = 89;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 90)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (90, 'BT090', 24, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 205000, 450000, 15, 'SP024-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT090', ID_SanPham = 24, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 205000, GiaBan = 450000, SoLuong = 15, Barcode = 'SP024-NVY-M' WHERE ID_BienThe = 90;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 91)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (91, 'BT091', 25, N'Trắng', 'S', 250, 'g', '1', NULL, 180000, 390000, 20, 'SP025-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT091', ID_SanPham = 25, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 180000, GiaBan = 390000, SoLuong = 20, Barcode = 'SP025-WHT-S' WHERE ID_BienThe = 91;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 92)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (92, 'BT092', 25, N'Trắng', 'M', 250, 'g', '1', NULL, 180000, 390000, 30, 'SP025-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT092', ID_SanPham = 25, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 180000, GiaBan = 390000, SoLuong = 30, Barcode = 'SP025-WHT-M' WHERE ID_BienThe = 92;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 93)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (93, 'BT093', 25, N'Trắng', 'L', 250, 'g', '1', NULL, 180000, 390000, 25, 'SP025-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT093', ID_SanPham = 25, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 180000, GiaBan = 390000, SoLuong = 25, Barcode = 'SP025-WHT-L' WHERE ID_BienThe = 93;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 94)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (94, 'BT094', 25, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 180000, 390000, 15, 'SP025-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT094', ID_SanPham = 25, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 180000, GiaBan = 390000, SoLuong = 15, Barcode = 'SP025-NVY-M' WHERE ID_BienThe = 94;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 95)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (95, 'BT095', 26, N'Trắng', 'S', 250, 'g', '1', NULL, 175000, 380000, 20, 'SP026-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT095', ID_SanPham = 26, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 175000, GiaBan = 380000, SoLuong = 20, Barcode = 'SP026-WHT-S' WHERE ID_BienThe = 95;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 96)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (96, 'BT096', 26, N'Trắng', 'M', 250, 'g', '1', NULL, 175000, 380000, 30, 'SP026-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT096', ID_SanPham = 26, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 175000, GiaBan = 380000, SoLuong = 30, Barcode = 'SP026-WHT-M' WHERE ID_BienThe = 96;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 97)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (97, 'BT097', 26, N'Trắng', 'L', 250, 'g', '1', NULL, 175000, 380000, 25, 'SP026-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT097', ID_SanPham = 26, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 175000, GiaBan = 380000, SoLuong = 25, Barcode = 'SP026-WHT-L' WHERE ID_BienThe = 97;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 98)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (98, 'BT098', 26, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 175000, 380000, 15, 'SP026-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT098', ID_SanPham = 26, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 175000, GiaBan = 380000, SoLuong = 15, Barcode = 'SP026-NVY-M' WHERE ID_BienThe = 98;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 99)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (99, 'BT099', 27, N'Trắng', 'S', 250, 'g', '1', NULL, 220000, 480000, 20, 'SP027-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT099', ID_SanPham = 27, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 220000, GiaBan = 480000, SoLuong = 20, Barcode = 'SP027-WHT-S' WHERE ID_BienThe = 99;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 100)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (100, 'BT100', 27, N'Trắng', 'M', 250, 'g', '1', NULL, 220000, 480000, 30, 'SP027-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT100', ID_SanPham = 27, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 220000, GiaBan = 480000, SoLuong = 30, Barcode = 'SP027-WHT-M' WHERE ID_BienThe = 100;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 101)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (101, 'BT101', 27, N'Trắng', 'L', 250, 'g', '1', NULL, 220000, 480000, 25, 'SP027-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT101', ID_SanPham = 27, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 220000, GiaBan = 480000, SoLuong = 25, Barcode = 'SP027-WHT-L' WHERE ID_BienThe = 101;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 102)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (102, 'BT102', 27, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 220000, 480000, 15, 'SP027-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT102', ID_SanPham = 27, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 220000, GiaBan = 480000, SoLuong = 15, Barcode = 'SP027-NVY-M' WHERE ID_BienThe = 102;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 103)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (103, 'BT103', 28, N'Trắng', 'S', 250, 'g', '1', NULL, 165000, 360000, 20, 'SP028-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT103', ID_SanPham = 28, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 165000, GiaBan = 360000, SoLuong = 20, Barcode = 'SP028-WHT-S' WHERE ID_BienThe = 103;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 104)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (104, 'BT104', 28, N'Trắng', 'M', 250, 'g', '1', NULL, 165000, 360000, 30, 'SP028-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT104', ID_SanPham = 28, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 165000, GiaBan = 360000, SoLuong = 30, Barcode = 'SP028-WHT-M' WHERE ID_BienThe = 104;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 105)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (105, 'BT105', 28, N'Trắng', 'L', 250, 'g', '1', NULL, 165000, 360000, 25, 'SP028-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT105', ID_SanPham = 28, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 165000, GiaBan = 360000, SoLuong = 25, Barcode = 'SP028-WHT-L' WHERE ID_BienThe = 105;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 106)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (106, 'BT106', 28, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 165000, 360000, 15, 'SP028-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT106', ID_SanPham = 28, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 165000, GiaBan = 360000, SoLuong = 15, Barcode = 'SP028-NVY-M' WHERE ID_BienThe = 106;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 107)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (107, 'BT107', 29, N'Trắng', 'S', 250, 'g', '1', NULL, 195000, 430000, 20, 'SP029-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT107', ID_SanPham = 29, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 195000, GiaBan = 430000, SoLuong = 20, Barcode = 'SP029-WHT-S' WHERE ID_BienThe = 107;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 108)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (108, 'BT108', 29, N'Trắng', 'M', 250, 'g', '1', NULL, 195000, 430000, 30, 'SP029-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT108', ID_SanPham = 29, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 195000, GiaBan = 430000, SoLuong = 30, Barcode = 'SP029-WHT-M' WHERE ID_BienThe = 108;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 109)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (109, 'BT109', 29, N'Trắng', 'L', 250, 'g', '1', NULL, 195000, 430000, 25, 'SP029-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT109', ID_SanPham = 29, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 195000, GiaBan = 430000, SoLuong = 25, Barcode = 'SP029-WHT-L' WHERE ID_BienThe = 109;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 110)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (110, 'BT110', 29, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 195000, 430000, 15, 'SP029-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT110', ID_SanPham = 29, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 195000, GiaBan = 430000, SoLuong = 15, Barcode = 'SP029-NVY-M' WHERE ID_BienThe = 110;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 111)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (111, 'BT111', 30, N'Trắng', 'S', 250, 'g', '1', NULL, 170000, 370000, 20, 'SP030-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT111', ID_SanPham = 30, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 170000, GiaBan = 370000, SoLuong = 20, Barcode = 'SP030-WHT-S' WHERE ID_BienThe = 111;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 112)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (112, 'BT112', 30, N'Trắng', 'M', 250, 'g', '1', NULL, 170000, 370000, 30, 'SP030-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT112', ID_SanPham = 30, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 170000, GiaBan = 370000, SoLuong = 30, Barcode = 'SP030-WHT-M' WHERE ID_BienThe = 112;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 113)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (113, 'BT113', 30, N'Trắng', 'L', 250, 'g', '1', NULL, 170000, 370000, 25, 'SP030-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT113', ID_SanPham = 30, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 170000, GiaBan = 370000, SoLuong = 25, Barcode = 'SP030-WHT-L' WHERE ID_BienThe = 113;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 114)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (114, 'BT114', 30, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 170000, 370000, 15, 'SP030-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT114', ID_SanPham = 30, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 170000, GiaBan = 370000, SoLuong = 15, Barcode = 'SP030-NVY-M' WHERE ID_BienThe = 114;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 115)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (115, 'BT115', 31, N'Trắng', 'S', 250, 'g', '1', NULL, 175000, 380000, 20, 'SP031-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT115', ID_SanPham = 31, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 175000, GiaBan = 380000, SoLuong = 20, Barcode = 'SP031-WHT-S' WHERE ID_BienThe = 115;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 116)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (116, 'BT116', 31, N'Trắng', 'M', 250, 'g', '1', NULL, 175000, 380000, 30, 'SP031-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT116', ID_SanPham = 31, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 175000, GiaBan = 380000, SoLuong = 30, Barcode = 'SP031-WHT-M' WHERE ID_BienThe = 116;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 117)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (117, 'BT117', 31, N'Trắng', 'L', 250, 'g', '1', NULL, 175000, 380000, 25, 'SP031-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT117', ID_SanPham = 31, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 175000, GiaBan = 380000, SoLuong = 25, Barcode = 'SP031-WHT-L' WHERE ID_BienThe = 117;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 118)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (118, 'BT118', 31, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 175000, 380000, 15, 'SP031-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT118', ID_SanPham = 31, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 175000, GiaBan = 380000, SoLuong = 15, Barcode = 'SP031-NVY-M' WHERE ID_BienThe = 118;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 119)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (119, 'BT119', 32, N'Trắng', 'S', 250, 'g', '1', NULL, 165000, 360000, 20, 'SP032-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT119', ID_SanPham = 32, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 165000, GiaBan = 360000, SoLuong = 20, Barcode = 'SP032-WHT-S' WHERE ID_BienThe = 119;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 120)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (120, 'BT120', 32, N'Trắng', 'M', 250, 'g', '1', NULL, 165000, 360000, 30, 'SP032-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT120', ID_SanPham = 32, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 165000, GiaBan = 360000, SoLuong = 30, Barcode = 'SP032-WHT-M' WHERE ID_BienThe = 120;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 121)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (121, 'BT121', 32, N'Trắng', 'L', 250, 'g', '1', NULL, 165000, 360000, 25, 'SP032-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT121', ID_SanPham = 32, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 165000, GiaBan = 360000, SoLuong = 25, Barcode = 'SP032-WHT-L' WHERE ID_BienThe = 121;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 122)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (122, 'BT122', 32, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 165000, 360000, 15, 'SP032-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT122', ID_SanPham = 32, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 165000, GiaBan = 360000, SoLuong = 15, Barcode = 'SP032-NVY-M' WHERE ID_BienThe = 122;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 123)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (123, 'BT123', 33, N'Trắng', 'S', 250, 'g', '1', NULL, 210000, 460000, 20, 'SP033-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT123', ID_SanPham = 33, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 210000, GiaBan = 460000, SoLuong = 20, Barcode = 'SP033-WHT-S' WHERE ID_BienThe = 123;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 124)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (124, 'BT124', 33, N'Trắng', 'M', 250, 'g', '1', NULL, 210000, 460000, 30, 'SP033-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT124', ID_SanPham = 33, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 210000, GiaBan = 460000, SoLuong = 30, Barcode = 'SP033-WHT-M' WHERE ID_BienThe = 124;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 125)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (125, 'BT125', 33, N'Trắng', 'L', 250, 'g', '1', NULL, 210000, 460000, 25, 'SP033-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT125', ID_SanPham = 33, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 210000, GiaBan = 460000, SoLuong = 25, Barcode = 'SP033-WHT-L' WHERE ID_BienThe = 125;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 126)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (126, 'BT126', 33, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 210000, 460000, 15, 'SP033-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT126', ID_SanPham = 33, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 210000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP033-NVY-M' WHERE ID_BienThe = 126;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 127)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (127, 'BT127', 34, N'Trắng', 'S', 250, 'g', '1', NULL, 185000, 410000, 20, 'SP034-WHT-S');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT127', ID_SanPham = 34, MauSac = N'Trắng', TrongLuong = 'S', GiaNhap = 185000, GiaBan = 410000, SoLuong = 20, Barcode = 'SP034-WHT-S' WHERE ID_BienThe = 127;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 128)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (128, 'BT128', 34, N'Trắng', 'M', 250, 'g', '1', NULL, 185000, 410000, 30, 'SP034-WHT-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT128', ID_SanPham = 34, MauSac = N'Trắng', TrongLuong = 'M', GiaNhap = 185000, GiaBan = 410000, SoLuong = 30, Barcode = 'SP034-WHT-M' WHERE ID_BienThe = 128;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 129)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (129, 'BT129', 34, N'Trắng', 'L', 250, 'g', '1', NULL, 185000, 410000, 25, 'SP034-WHT-L');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT129', ID_SanPham = 34, MauSac = N'Trắng', TrongLuong = 'L', GiaNhap = 185000, GiaBan = 410000, SoLuong = 25, Barcode = 'SP034-WHT-L' WHERE ID_BienThe = 129;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 130)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (130, 'BT130', 34, N'Xanh Navy', 'M', 250, 'g', '1', NULL, 185000, 410000, 15, 'SP034-NVY-M');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT130', ID_SanPham = 34, MauSac = N'Xanh Navy', TrongLuong = 'M', GiaNhap = 185000, GiaBan = 410000, SoLuong = 15, Barcode = 'SP034-NVY-M' WHERE ID_BienThe = 130;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 131)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (131, 'BT131', 35, N'Đen Than', '29', 250, 'g', '1', NULL, 250000, 550000, 15, 'SP035-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT131', ID_SanPham = 35, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 250000, GiaBan = 550000, SoLuong = 15, Barcode = 'SP035-BLK-29' WHERE ID_BienThe = 131;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 132)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (132, 'BT132', 35, N'Đen Than', '30', 250, 'g', '1', NULL, 250000, 550000, 25, 'SP035-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT132', ID_SanPham = 35, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 250000, GiaBan = 550000, SoLuong = 25, Barcode = 'SP035-BLK-30' WHERE ID_BienThe = 132;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 133)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (133, 'BT133', 35, N'Đen Than', '31', 250, 'g', '1', NULL, 250000, 550000, 20, 'SP035-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT133', ID_SanPham = 35, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 250000, GiaBan = 550000, SoLuong = 20, Barcode = 'SP035-BLK-31' WHERE ID_BienThe = 133;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 134)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (134, 'BT134', 35, N'Xám Ghi', '30', 250, 'g', '1', NULL, 250000, 550000, 15, 'SP035-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT134', ID_SanPham = 35, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 250000, GiaBan = 550000, SoLuong = 15, Barcode = 'SP035-GRY-30' WHERE ID_BienThe = 134;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 135)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (135, 'BT135', 36, N'Đen Than', '29', 250, 'g', '1', NULL, 265000, 580000, 15, 'SP036-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT135', ID_SanPham = 36, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 265000, GiaBan = 580000, SoLuong = 15, Barcode = 'SP036-BLK-29' WHERE ID_BienThe = 135;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 136)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (136, 'BT136', 36, N'Đen Than', '30', 250, 'g', '1', NULL, 265000, 580000, 25, 'SP036-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT136', ID_SanPham = 36, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 265000, GiaBan = 580000, SoLuong = 25, Barcode = 'SP036-BLK-30' WHERE ID_BienThe = 136;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 137)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (137, 'BT137', 36, N'Đen Than', '31', 250, 'g', '1', NULL, 265000, 580000, 20, 'SP036-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT137', ID_SanPham = 36, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 265000, GiaBan = 580000, SoLuong = 20, Barcode = 'SP036-BLK-31' WHERE ID_BienThe = 137;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 138)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (138, 'BT138', 36, N'Xám Ghi', '30', 250, 'g', '1', NULL, 265000, 580000, 15, 'SP036-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT138', ID_SanPham = 36, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 265000, GiaBan = 580000, SoLuong = 15, Barcode = 'SP036-GRY-30' WHERE ID_BienThe = 138;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 139)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (139, 'BT139', 37, N'Đen Than', '29', 250, 'g', '1', NULL, 280000, 620000, 15, 'SP037-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT139', ID_SanPham = 37, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 280000, GiaBan = 620000, SoLuong = 15, Barcode = 'SP037-BLK-29' WHERE ID_BienThe = 139;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 140)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (140, 'BT140', 37, N'Đen Than', '30', 250, 'g', '1', NULL, 280000, 620000, 25, 'SP037-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT140', ID_SanPham = 37, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 280000, GiaBan = 620000, SoLuong = 25, Barcode = 'SP037-BLK-30' WHERE ID_BienThe = 140;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 141)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (141, 'BT141', 37, N'Đen Than', '31', 250, 'g', '1', NULL, 280000, 620000, 20, 'SP037-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT141', ID_SanPham = 37, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 280000, GiaBan = 620000, SoLuong = 20, Barcode = 'SP037-BLK-31' WHERE ID_BienThe = 141;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 142)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (142, 'BT142', 37, N'Xám Ghi', '30', 250, 'g', '1', NULL, 280000, 620000, 15, 'SP037-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT142', ID_SanPham = 37, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 280000, GiaBan = 620000, SoLuong = 15, Barcode = 'SP037-GRY-30' WHERE ID_BienThe = 142;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 143)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (143, 'BT143', 38, N'Đen Than', '29', 250, 'g', '1', NULL, 255000, 560000, 15, 'SP038-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT143', ID_SanPham = 38, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 255000, GiaBan = 560000, SoLuong = 15, Barcode = 'SP038-BLK-29' WHERE ID_BienThe = 143;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 144)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (144, 'BT144', 38, N'Đen Than', '30', 250, 'g', '1', NULL, 255000, 560000, 25, 'SP038-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT144', ID_SanPham = 38, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 255000, GiaBan = 560000, SoLuong = 25, Barcode = 'SP038-BLK-30' WHERE ID_BienThe = 144;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 145)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (145, 'BT145', 38, N'Đen Than', '31', 250, 'g', '1', NULL, 255000, 560000, 20, 'SP038-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT145', ID_SanPham = 38, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 255000, GiaBan = 560000, SoLuong = 20, Barcode = 'SP038-BLK-31' WHERE ID_BienThe = 145;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 146)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (146, 'BT146', 38, N'Xám Ghi', '30', 250, 'g', '1', NULL, 255000, 560000, 15, 'SP038-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT146', ID_SanPham = 38, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 255000, GiaBan = 560000, SoLuong = 15, Barcode = 'SP038-GRY-30' WHERE ID_BienThe = 146;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 147)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (147, 'BT147', 39, N'Đen Than', '29', 250, 'g', '1', NULL, 240000, 530000, 15, 'SP039-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT147', ID_SanPham = 39, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 240000, GiaBan = 530000, SoLuong = 15, Barcode = 'SP039-BLK-29' WHERE ID_BienThe = 147;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 148)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (148, 'BT148', 39, N'Đen Than', '30', 250, 'g', '1', NULL, 240000, 530000, 25, 'SP039-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT148', ID_SanPham = 39, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 240000, GiaBan = 530000, SoLuong = 25, Barcode = 'SP039-BLK-30' WHERE ID_BienThe = 148;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 149)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (149, 'BT149', 39, N'Đen Than', '31', 250, 'g', '1', NULL, 240000, 530000, 20, 'SP039-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT149', ID_SanPham = 39, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 240000, GiaBan = 530000, SoLuong = 20, Barcode = 'SP039-BLK-31' WHERE ID_BienThe = 149;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 150)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (150, 'BT150', 39, N'Xám Ghi', '30', 250, 'g', '1', NULL, 240000, 530000, 15, 'SP039-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT150', ID_SanPham = 39, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 240000, GiaBan = 530000, SoLuong = 15, Barcode = 'SP039-GRY-30' WHERE ID_BienThe = 150;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 151)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (151, 'BT151', 40, N'Đen Than', '29', 250, 'g', '1', NULL, 220000, 490000, 15, 'SP040-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT151', ID_SanPham = 40, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 220000, GiaBan = 490000, SoLuong = 15, Barcode = 'SP040-BLK-29' WHERE ID_BienThe = 151;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 152)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (152, 'BT152', 40, N'Đen Than', '30', 250, 'g', '1', NULL, 220000, 490000, 25, 'SP040-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT152', ID_SanPham = 40, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 220000, GiaBan = 490000, SoLuong = 25, Barcode = 'SP040-BLK-30' WHERE ID_BienThe = 152;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 153)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (153, 'BT153', 40, N'Đen Than', '31', 250, 'g', '1', NULL, 220000, 490000, 20, 'SP040-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT153', ID_SanPham = 40, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 220000, GiaBan = 490000, SoLuong = 20, Barcode = 'SP040-BLK-31' WHERE ID_BienThe = 153;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 154)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (154, 'BT154', 40, N'Xám Ghi', '30', 250, 'g', '1', NULL, 220000, 490000, 15, 'SP040-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT154', ID_SanPham = 40, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 220000, GiaBan = 490000, SoLuong = 15, Barcode = 'SP040-GRY-30' WHERE ID_BienThe = 154;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 155)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (155, 'BT155', 41, N'Đen Than', '29', 250, 'g', '1', NULL, 310000, 680000, 15, 'SP041-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT155', ID_SanPham = 41, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 310000, GiaBan = 680000, SoLuong = 15, Barcode = 'SP041-BLK-29' WHERE ID_BienThe = 155;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 156)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (156, 'BT156', 41, N'Đen Than', '30', 250, 'g', '1', NULL, 310000, 680000, 25, 'SP041-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT156', ID_SanPham = 41, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 310000, GiaBan = 680000, SoLuong = 25, Barcode = 'SP041-BLK-30' WHERE ID_BienThe = 156;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 157)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (157, 'BT157', 41, N'Đen Than', '31', 250, 'g', '1', NULL, 310000, 680000, 20, 'SP041-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT157', ID_SanPham = 41, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 310000, GiaBan = 680000, SoLuong = 20, Barcode = 'SP041-BLK-31' WHERE ID_BienThe = 157;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 158)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (158, 'BT158', 41, N'Xám Ghi', '30', 250, 'g', '1', NULL, 310000, 680000, 15, 'SP041-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT158', ID_SanPham = 41, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 310000, GiaBan = 680000, SoLuong = 15, Barcode = 'SP041-GRY-30' WHERE ID_BienThe = 158;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 159)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (159, 'BT159', 42, N'Đen Than', '29', 250, 'g', '1', NULL, 245000, 540000, 15, 'SP042-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT159', ID_SanPham = 42, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 245000, GiaBan = 540000, SoLuong = 15, Barcode = 'SP042-BLK-29' WHERE ID_BienThe = 159;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 160)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (160, 'BT160', 42, N'Đen Than', '30', 250, 'g', '1', NULL, 245000, 540000, 25, 'SP042-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT160', ID_SanPham = 42, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 245000, GiaBan = 540000, SoLuong = 25, Barcode = 'SP042-BLK-30' WHERE ID_BienThe = 160;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 161)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (161, 'BT161', 42, N'Đen Than', '31', 250, 'g', '1', NULL, 245000, 540000, 20, 'SP042-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT161', ID_SanPham = 42, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 245000, GiaBan = 540000, SoLuong = 20, Barcode = 'SP042-BLK-31' WHERE ID_BienThe = 161;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 162)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (162, 'BT162', 42, N'Xám Ghi', '30', 250, 'g', '1', NULL, 245000, 540000, 15, 'SP042-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT162', ID_SanPham = 42, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 245000, GiaBan = 540000, SoLuong = 15, Barcode = 'SP042-GRY-30' WHERE ID_BienThe = 162;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 163)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (163, 'BT163', 43, N'Đen Than', '29', 250, 'g', '1', NULL, 230000, 510000, 15, 'SP043-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT163', ID_SanPham = 43, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 230000, GiaBan = 510000, SoLuong = 15, Barcode = 'SP043-BLK-29' WHERE ID_BienThe = 163;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 164)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (164, 'BT164', 43, N'Đen Than', '30', 250, 'g', '1', NULL, 230000, 510000, 25, 'SP043-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT164', ID_SanPham = 43, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 230000, GiaBan = 510000, SoLuong = 25, Barcode = 'SP043-BLK-30' WHERE ID_BienThe = 164;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 165)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (165, 'BT165', 43, N'Đen Than', '31', 250, 'g', '1', NULL, 230000, 510000, 20, 'SP043-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT165', ID_SanPham = 43, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 230000, GiaBan = 510000, SoLuong = 20, Barcode = 'SP043-BLK-31' WHERE ID_BienThe = 165;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 166)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (166, 'BT166', 43, N'Xám Ghi', '30', 250, 'g', '1', NULL, 230000, 510000, 15, 'SP043-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT166', ID_SanPham = 43, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 230000, GiaBan = 510000, SoLuong = 15, Barcode = 'SP043-GRY-30' WHERE ID_BienThe = 166;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 167)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (167, 'BT167', 44, N'Đen Than', '29', 250, 'g', '1', NULL, 225000, 500000, 15, 'SP044-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT167', ID_SanPham = 44, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 225000, GiaBan = 500000, SoLuong = 15, Barcode = 'SP044-BLK-29' WHERE ID_BienThe = 167;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 168)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (168, 'BT168', 44, N'Đen Than', '30', 250, 'g', '1', NULL, 225000, 500000, 25, 'SP044-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT168', ID_SanPham = 44, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 225000, GiaBan = 500000, SoLuong = 25, Barcode = 'SP044-BLK-30' WHERE ID_BienThe = 168;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 169)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (169, 'BT169', 44, N'Đen Than', '31', 250, 'g', '1', NULL, 225000, 500000, 20, 'SP044-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT169', ID_SanPham = 44, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 225000, GiaBan = 500000, SoLuong = 20, Barcode = 'SP044-BLK-31' WHERE ID_BienThe = 169;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 170)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (170, 'BT170', 44, N'Xám Ghi', '30', 250, 'g', '1', NULL, 225000, 500000, 15, 'SP044-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT170', ID_SanPham = 44, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 225000, GiaBan = 500000, SoLuong = 15, Barcode = 'SP044-GRY-30' WHERE ID_BienThe = 170;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 171)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (171, 'BT171', 45, N'Đen Than', '29', 250, 'g', '1', NULL, 295000, 650000, 15, 'SP045-BLK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT171', ID_SanPham = 45, MauSac = N'Đen Than', TrongLuong = '29', GiaNhap = 295000, GiaBan = 650000, SoLuong = 15, Barcode = 'SP045-BLK-29' WHERE ID_BienThe = 171;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 172)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (172, 'BT172', 45, N'Đen Than', '30', 250, 'g', '1', NULL, 295000, 650000, 25, 'SP045-BLK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT172', ID_SanPham = 45, MauSac = N'Đen Than', TrongLuong = '30', GiaNhap = 295000, GiaBan = 650000, SoLuong = 25, Barcode = 'SP045-BLK-30' WHERE ID_BienThe = 172;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 173)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (173, 'BT173', 45, N'Đen Than', '31', 250, 'g', '1', NULL, 295000, 650000, 20, 'SP045-BLK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT173', ID_SanPham = 45, MauSac = N'Đen Than', TrongLuong = '31', GiaNhap = 295000, GiaBan = 650000, SoLuong = 20, Barcode = 'SP045-BLK-31' WHERE ID_BienThe = 173;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 174)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (174, 'BT174', 45, N'Xám Ghi', '30', 250, 'g', '1', NULL, 295000, 650000, 15, 'SP045-GRY-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT174', ID_SanPham = 45, MauSac = N'Xám Ghi', TrongLuong = '30', GiaNhap = 295000, GiaBan = 650000, SoLuong = 15, Barcode = 'SP045-GRY-30' WHERE ID_BienThe = 174;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 175)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (175, 'BT175', 46, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 205000, 450000, 15, 'SP046-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT175', ID_SanPham = 46, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 205000, GiaBan = 450000, SoLuong = 15, Barcode = 'SP046-KHK-29' WHERE ID_BienThe = 175;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 176)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (176, 'BT176', 46, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 205000, 450000, 25, 'SP046-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT176', ID_SanPham = 46, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 205000, GiaBan = 450000, SoLuong = 25, Barcode = 'SP046-KHK-30' WHERE ID_BienThe = 176;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 177)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (177, 'BT177', 46, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 205000, 450000, 20, 'SP046-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT177', ID_SanPham = 46, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 205000, GiaBan = 450000, SoLuong = 20, Barcode = 'SP046-KHK-31' WHERE ID_BienThe = 177;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 178)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (178, 'BT178', 46, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 205000, 450000, 15, 'SP046-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT178', ID_SanPham = 46, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 205000, GiaBan = 450000, SoLuong = 15, Barcode = 'SP046-OLV-30' WHERE ID_BienThe = 178;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 179)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (179, 'BT179', 47, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 195000, 430000, 15, 'SP047-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT179', ID_SanPham = 47, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 195000, GiaBan = 430000, SoLuong = 15, Barcode = 'SP047-KHK-29' WHERE ID_BienThe = 179;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 180)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (180, 'BT180', 47, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 195000, 430000, 25, 'SP047-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT180', ID_SanPham = 47, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 195000, GiaBan = 430000, SoLuong = 25, Barcode = 'SP047-KHK-30' WHERE ID_BienThe = 180;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 181)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (181, 'BT181', 47, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 195000, 430000, 20, 'SP047-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT181', ID_SanPham = 47, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 195000, GiaBan = 430000, SoLuong = 20, Barcode = 'SP047-KHK-31' WHERE ID_BienThe = 181;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 182)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (182, 'BT182', 47, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 195000, 430000, 15, 'SP047-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT182', ID_SanPham = 47, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 195000, GiaBan = 430000, SoLuong = 15, Barcode = 'SP047-OLV-30' WHERE ID_BienThe = 182;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 183)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (183, 'BT183', 48, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 200000, 440000, 15, 'SP048-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT183', ID_SanPham = 48, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 200000, GiaBan = 440000, SoLuong = 15, Barcode = 'SP048-KHK-29' WHERE ID_BienThe = 183;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 184)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (184, 'BT184', 48, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 200000, 440000, 25, 'SP048-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT184', ID_SanPham = 48, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 200000, GiaBan = 440000, SoLuong = 25, Barcode = 'SP048-KHK-30' WHERE ID_BienThe = 184;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 185)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (185, 'BT185', 48, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 200000, 440000, 20, 'SP048-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT185', ID_SanPham = 48, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 200000, GiaBan = 440000, SoLuong = 20, Barcode = 'SP048-KHK-31' WHERE ID_BienThe = 185;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 186)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (186, 'BT186', 48, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 200000, 440000, 15, 'SP048-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT186', ID_SanPham = 48, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 200000, GiaBan = 440000, SoLuong = 15, Barcode = 'SP048-OLV-30' WHERE ID_BienThe = 186;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 187)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (187, 'BT187', 49, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 210000, 460000, 15, 'SP049-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT187', ID_SanPham = 49, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 210000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP049-KHK-29' WHERE ID_BienThe = 187;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 188)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (188, 'BT188', 49, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 210000, 460000, 25, 'SP049-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT188', ID_SanPham = 49, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 210000, GiaBan = 460000, SoLuong = 25, Barcode = 'SP049-KHK-30' WHERE ID_BienThe = 188;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 189)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (189, 'BT189', 49, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 210000, 460000, 20, 'SP049-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT189', ID_SanPham = 49, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 210000, GiaBan = 460000, SoLuong = 20, Barcode = 'SP049-KHK-31' WHERE ID_BienThe = 189;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 190)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (190, 'BT190', 49, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 210000, 460000, 15, 'SP049-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT190', ID_SanPham = 49, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 210000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP049-OLV-30' WHERE ID_BienThe = 190;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 191)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (191, 'BT191', 50, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 215000, 470000, 15, 'SP050-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT191', ID_SanPham = 50, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 215000, GiaBan = 470000, SoLuong = 15, Barcode = 'SP050-KHK-29' WHERE ID_BienThe = 191;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 192)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (192, 'BT192', 50, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 215000, 470000, 25, 'SP050-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT192', ID_SanPham = 50, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 215000, GiaBan = 470000, SoLuong = 25, Barcode = 'SP050-KHK-30' WHERE ID_BienThe = 192;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 193)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (193, 'BT193', 50, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 215000, 470000, 20, 'SP050-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT193', ID_SanPham = 50, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 215000, GiaBan = 470000, SoLuong = 20, Barcode = 'SP050-KHK-31' WHERE ID_BienThe = 193;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 194)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (194, 'BT194', 50, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 215000, 470000, 15, 'SP050-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT194', ID_SanPham = 50, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 215000, GiaBan = 470000, SoLuong = 15, Barcode = 'SP050-OLV-30' WHERE ID_BienThe = 194;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 195)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (195, 'BT195', 51, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 195000, 430000, 15, 'SP051-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT195', ID_SanPham = 51, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 195000, GiaBan = 430000, SoLuong = 15, Barcode = 'SP051-KHK-29' WHERE ID_BienThe = 195;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 196)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (196, 'BT196', 51, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 195000, 430000, 25, 'SP051-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT196', ID_SanPham = 51, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 195000, GiaBan = 430000, SoLuong = 25, Barcode = 'SP051-KHK-30' WHERE ID_BienThe = 196;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 197)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (197, 'BT197', 51, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 195000, 430000, 20, 'SP051-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT197', ID_SanPham = 51, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 195000, GiaBan = 430000, SoLuong = 20, Barcode = 'SP051-KHK-31' WHERE ID_BienThe = 197;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 198)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (198, 'BT198', 51, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 195000, 430000, 15, 'SP051-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT198', ID_SanPham = 51, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 195000, GiaBan = 430000, SoLuong = 15, Barcode = 'SP051-OLV-30' WHERE ID_BienThe = 198;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 199)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (199, 'BT199', 52, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 205000, 450000, 15, 'SP052-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT199', ID_SanPham = 52, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 205000, GiaBan = 450000, SoLuong = 15, Barcode = 'SP052-KHK-29' WHERE ID_BienThe = 199;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 200)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (200, 'BT200', 52, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 205000, 450000, 25, 'SP052-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT200', ID_SanPham = 52, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 205000, GiaBan = 450000, SoLuong = 25, Barcode = 'SP052-KHK-30' WHERE ID_BienThe = 200;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 201)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (201, 'BT201', 52, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 205000, 450000, 20, 'SP052-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT201', ID_SanPham = 52, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 205000, GiaBan = 450000, SoLuong = 20, Barcode = 'SP052-KHK-31' WHERE ID_BienThe = 201;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 202)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (202, 'BT202', 52, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 205000, 450000, 15, 'SP052-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT202', ID_SanPham = 52, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 205000, GiaBan = 450000, SoLuong = 15, Barcode = 'SP052-OLV-30' WHERE ID_BienThe = 202;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 203)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (203, 'BT203', 53, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 220000, 480000, 15, 'SP053-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT203', ID_SanPham = 53, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 220000, GiaBan = 480000, SoLuong = 15, Barcode = 'SP053-KHK-29' WHERE ID_BienThe = 203;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 204)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (204, 'BT204', 53, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 220000, 480000, 25, 'SP053-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT204', ID_SanPham = 53, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 220000, GiaBan = 480000, SoLuong = 25, Barcode = 'SP053-KHK-30' WHERE ID_BienThe = 204;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 205)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (205, 'BT205', 53, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 220000, 480000, 20, 'SP053-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT205', ID_SanPham = 53, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 220000, GiaBan = 480000, SoLuong = 20, Barcode = 'SP053-KHK-31' WHERE ID_BienThe = 205;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 206)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (206, 'BT206', 53, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 220000, 480000, 15, 'SP053-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT206', ID_SanPham = 53, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 220000, GiaBan = 480000, SoLuong = 15, Barcode = 'SP053-OLV-30' WHERE ID_BienThe = 206;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 207)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (207, 'BT207', 54, N'Vàng Khaki Cát', '29', 250, 'g', '1', NULL, 210000, 460000, 15, 'SP054-KHK-29');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT207', ID_SanPham = 54, MauSac = N'Vàng Khaki Cát', TrongLuong = '29', GiaNhap = 210000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP054-KHK-29' WHERE ID_BienThe = 207;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 208)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (208, 'BT208', 54, N'Vàng Khaki Cát', '30', 250, 'g', '1', NULL, 210000, 460000, 25, 'SP054-KHK-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT208', ID_SanPham = 54, MauSac = N'Vàng Khaki Cát', TrongLuong = '30', GiaNhap = 210000, GiaBan = 460000, SoLuong = 25, Barcode = 'SP054-KHK-30' WHERE ID_BienThe = 208;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 209)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (209, 'BT209', 54, N'Vàng Khaki Cát', '31', 250, 'g', '1', NULL, 210000, 460000, 20, 'SP054-KHK-31');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT209', ID_SanPham = 54, MauSac = N'Vàng Khaki Cát', TrongLuong = '31', GiaNhap = 210000, GiaBan = 460000, SoLuong = 20, Barcode = 'SP054-KHK-31' WHERE ID_BienThe = 209;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 210)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (210, 'BT210', 54, N'Xanh Rêu', '30', 250, 'g', '1', NULL, 210000, 460000, 15, 'SP054-OLV-30');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT210', ID_SanPham = 54, MauSac = N'Xanh Rêu', TrongLuong = '30', GiaNhap = 210000, GiaBan = 460000, SoLuong = 15, Barcode = 'SP054-OLV-30' WHERE ID_BienThe = 210;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 211)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (211, 'BT211', 55, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 530000, 1150000, 12, 'SP055-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT211', ID_SanPham = 55, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 530000, GiaBan = 1150000, SoLuong = 12, Barcode = 'SP055-NVY-48' WHERE ID_BienThe = 211;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 212)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (212, 'BT212', 55, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 530000, 1150000, 15, 'SP055-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT212', ID_SanPham = 55, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 530000, GiaBan = 1150000, SoLuong = 15, Barcode = 'SP055-NVY-50' WHERE ID_BienThe = 212;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 213)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (213, 'BT213', 55, N'Đen', '50 (L)', 250, 'g', '1', NULL, 530000, 1150000, 10, 'SP055-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT213', ID_SanPham = 55, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 530000, GiaBan = 1150000, SoLuong = 10, Barcode = 'SP055-BLK-50' WHERE ID_BienThe = 213;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 214)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (214, 'BT214', 56, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 720000, 1550000, 12, 'SP056-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT214', ID_SanPham = 56, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 720000, GiaBan = 1550000, SoLuong = 12, Barcode = 'SP056-NVY-48' WHERE ID_BienThe = 214;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 215)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (215, 'BT215', 56, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 720000, 1550000, 15, 'SP056-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT215', ID_SanPham = 56, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 720000, GiaBan = 1550000, SoLuong = 15, Barcode = 'SP056-NVY-50' WHERE ID_BienThe = 215;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 216)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (216, 'BT216', 56, N'Đen', '50 (L)', 250, 'g', '1', NULL, 720000, 1550000, 10, 'SP056-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT216', ID_SanPham = 56, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 720000, GiaBan = 1550000, SoLuong = 10, Barcode = 'SP056-BLK-50' WHERE ID_BienThe = 216;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 217)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (217, 'BT217', 57, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 620000, 1350000, 12, 'SP057-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT217', ID_SanPham = 57, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 620000, GiaBan = 1350000, SoLuong = 12, Barcode = 'SP057-NVY-48' WHERE ID_BienThe = 217;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 218)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (218, 'BT218', 57, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 620000, 1350000, 15, 'SP057-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT218', ID_SanPham = 57, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 620000, GiaBan = 1350000, SoLuong = 15, Barcode = 'SP057-NVY-50' WHERE ID_BienThe = 218;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 219)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (219, 'BT219', 57, N'Đen', '50 (L)', 250, 'g', '1', NULL, 620000, 1350000, 10, 'SP057-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT219', ID_SanPham = 57, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 620000, GiaBan = 1350000, SoLuong = 10, Barcode = 'SP057-BLK-50' WHERE ID_BienThe = 219;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 220)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (220, 'BT220', 58, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 650000, 1400000, 12, 'SP058-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT220', ID_SanPham = 58, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 650000, GiaBan = 1400000, SoLuong = 12, Barcode = 'SP058-NVY-48' WHERE ID_BienThe = 220;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 221)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (221, 'BT221', 58, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 650000, 1400000, 15, 'SP058-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT221', ID_SanPham = 58, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 650000, GiaBan = 1400000, SoLuong = 15, Barcode = 'SP058-NVY-50' WHERE ID_BienThe = 221;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 222)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (222, 'BT222', 58, N'Đen', '50 (L)', 250, 'g', '1', NULL, 650000, 1400000, 10, 'SP058-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT222', ID_SanPham = 58, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 650000, GiaBan = 1400000, SoLuong = 10, Barcode = 'SP058-BLK-50' WHERE ID_BienThe = 222;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 223)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (223, 'BT223', 59, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 450000, 980000, 12, 'SP059-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT223', ID_SanPham = 59, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 450000, GiaBan = 980000, SoLuong = 12, Barcode = 'SP059-NVY-48' WHERE ID_BienThe = 223;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 224)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (224, 'BT224', 59, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 450000, 980000, 15, 'SP059-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT224', ID_SanPham = 59, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 450000, GiaBan = 980000, SoLuong = 15, Barcode = 'SP059-NVY-50' WHERE ID_BienThe = 224;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 225)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (225, 'BT225', 59, N'Đen', '50 (L)', 250, 'g', '1', NULL, 450000, 980000, 10, 'SP059-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT225', ID_SanPham = 59, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 450000, GiaBan = 980000, SoLuong = 10, Barcode = 'SP059-BLK-50' WHERE ID_BienThe = 225;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 226)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (226, 'BT226', 60, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 780000, 1650000, 12, 'SP060-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT226', ID_SanPham = 60, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 780000, GiaBan = 1650000, SoLuong = 12, Barcode = 'SP060-NVY-48' WHERE ID_BienThe = 226;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 227)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (227, 'BT227', 60, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 780000, 1650000, 15, 'SP060-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT227', ID_SanPham = 60, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 780000, GiaBan = 1650000, SoLuong = 15, Barcode = 'SP060-NVY-50' WHERE ID_BienThe = 227;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 228)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (228, 'BT228', 60, N'Đen', '50 (L)', 250, 'g', '1', NULL, 780000, 1650000, 10, 'SP060-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT228', ID_SanPham = 60, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 780000, GiaBan = 1650000, SoLuong = 10, Barcode = 'SP060-BLK-50' WHERE ID_BienThe = 228;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 229)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (229, 'BT229', 61, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 740000, 1580000, 12, 'SP061-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT229', ID_SanPham = 61, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 740000, GiaBan = 1580000, SoLuong = 12, Barcode = 'SP061-NVY-48' WHERE ID_BienThe = 229;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 230)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (230, 'BT230', 61, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 740000, 1580000, 15, 'SP061-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT230', ID_SanPham = 61, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 740000, GiaBan = 1580000, SoLuong = 15, Barcode = 'SP061-NVY-50' WHERE ID_BienThe = 230;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 231)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (231, 'BT231', 61, N'Đen', '50 (L)', 250, 'g', '1', NULL, 740000, 1580000, 10, 'SP061-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT231', ID_SanPham = 61, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 740000, GiaBan = 1580000, SoLuong = 10, Barcode = 'SP061-BLK-50' WHERE ID_BienThe = 231;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 232)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (232, 'BT232', 62, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 690000, 1480000, 12, 'SP062-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT232', ID_SanPham = 62, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 690000, GiaBan = 1480000, SoLuong = 12, Barcode = 'SP062-NVY-48' WHERE ID_BienThe = 232;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 233)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (233, 'BT233', 62, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 690000, 1480000, 15, 'SP062-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT233', ID_SanPham = 62, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 690000, GiaBan = 1480000, SoLuong = 15, Barcode = 'SP062-NVY-50' WHERE ID_BienThe = 233;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 234)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (234, 'BT234', 62, N'Đen', '50 (L)', 250, 'g', '1', NULL, 690000, 1480000, 10, 'SP062-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT234', ID_SanPham = 62, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 690000, GiaBan = 1480000, SoLuong = 10, Barcode = 'SP062-BLK-50' WHERE ID_BienThe = 234;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 235)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (235, 'BT235', 63, N'Xanh Midnight Navy', '48 (M)', 250, 'g', '1', NULL, 580000, 1250000, 12, 'SP063-NVY-48');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT235', ID_SanPham = 63, MauSac = N'Xanh Midnight Navy', TrongLuong = '48 (M)', GiaNhap = 580000, GiaBan = 1250000, SoLuong = 12, Barcode = 'SP063-NVY-48' WHERE ID_BienThe = 235;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 236)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (236, 'BT236', 63, N'Xanh Midnight Navy', '50 (L)', 250, 'g', '1', NULL, 580000, 1250000, 15, 'SP063-NVY-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT236', ID_SanPham = 63, MauSac = N'Xanh Midnight Navy', TrongLuong = '50 (L)', GiaNhap = 580000, GiaBan = 1250000, SoLuong = 15, Barcode = 'SP063-NVY-50' WHERE ID_BienThe = 236;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 237)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (237, 'BT237', 63, N'Đen', '50 (L)', 250, 'g', '1', NULL, 580000, 1250000, 10, 'SP063-BLK-50');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT237', ID_SanPham = 63, MauSac = N'Đen', TrongLuong = '50 (L)', GiaNhap = 580000, GiaBan = 1250000, SoLuong = 10, Barcode = 'SP063-BLK-50' WHERE ID_BienThe = 237;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 238)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (238, 'BT238', 64, N'Đen', 'Freesize', 250, 'g', '1', NULL, 175000, 390000, 25, 'SP064-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT238', ID_SanPham = 64, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 175000, GiaBan = 390000, SoLuong = 25, Barcode = 'SP064-BLK-FS' WHERE ID_BienThe = 238;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 239)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (239, 'BT239', 64, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 175000, 390000, 20, 'SP064-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT239', ID_SanPham = 64, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 175000, GiaBan = 390000, SoLuong = 20, Barcode = 'SP064-BRN-FS' WHERE ID_BienThe = 239;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 240)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (240, 'BT240', 65, N'Đen', 'Freesize', 250, 'g', '1', NULL, 95000, 220000, 25, 'SP065-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT240', ID_SanPham = 65, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 95000, GiaBan = 220000, SoLuong = 25, Barcode = 'SP065-BLK-FS' WHERE ID_BienThe = 240;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 241)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (241, 'BT241', 65, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 95000, 220000, 20, 'SP065-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT241', ID_SanPham = 65, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 95000, GiaBan = 220000, SoLuong = 20, Barcode = 'SP065-BRN-FS' WHERE ID_BienThe = 241;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 242)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (242, 'BT242', 66, N'Đen', 'Freesize', 250, 'g', '1', NULL, 65000, 150000, 25, 'SP066-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT242', ID_SanPham = 66, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 65000, GiaBan = 150000, SoLuong = 25, Barcode = 'SP066-BLK-FS' WHERE ID_BienThe = 242;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 243)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (243, 'BT243', 66, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 65000, 150000, 20, 'SP066-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT243', ID_SanPham = 66, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 65000, GiaBan = 150000, SoLuong = 20, Barcode = 'SP066-BRN-FS' WHERE ID_BienThe = 243;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 244)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (244, 'BT244', 67, N'Đen', 'Freesize', 250, 'g', '1', NULL, 50000, 120000, 25, 'SP067-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT244', ID_SanPham = 67, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 50000, GiaBan = 120000, SoLuong = 25, Barcode = 'SP067-BLK-FS' WHERE ID_BienThe = 244;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 245)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (245, 'BT245', 67, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 50000, 120000, 20, 'SP067-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT245', ID_SanPham = 67, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 50000, GiaBan = 120000, SoLuong = 20, Barcode = 'SP067-BRN-FS' WHERE ID_BienThe = 245;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 246)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (246, 'BT246', 68, N'Đen', 'Freesize', 250, 'g', '1', NULL, 45000, 110000, 25, 'SP068-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT246', ID_SanPham = 68, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 45000, GiaBan = 110000, SoLuong = 25, Barcode = 'SP068-BLK-FS' WHERE ID_BienThe = 246;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 247)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (247, 'BT247', 68, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 45000, 110000, 20, 'SP068-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT247', ID_SanPham = 68, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 45000, GiaBan = 110000, SoLuong = 20, Barcode = 'SP068-BRN-FS' WHERE ID_BienThe = 247;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 248)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (248, 'BT248', 69, N'Đen', 'Freesize', 250, 'g', '1', NULL, 80000, 180000, 25, 'SP069-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT248', ID_SanPham = 69, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 80000, GiaBan = 180000, SoLuong = 25, Barcode = 'SP069-BLK-FS' WHERE ID_BienThe = 248;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 249)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (249, 'BT249', 69, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 80000, 180000, 20, 'SP069-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT249', ID_SanPham = 69, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 80000, GiaBan = 180000, SoLuong = 20, Barcode = 'SP069-BRN-FS' WHERE ID_BienThe = 249;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 250)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (250, 'BT250', 70, N'Đen', 'Freesize', 250, 'g', '1', NULL, 310000, 650000, 25, 'SP070-BLK-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT250', ID_SanPham = 70, MauSac = N'Đen', TrongLuong = 'Freesize', GiaNhap = 310000, GiaBan = 650000, SoLuong = 25, Barcode = 'SP070-BLK-FS' WHERE ID_BienThe = 250;
IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = 251)
    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (251, 'BT251', 70, N'Nâu Cà Phê', 'Freesize', 250, 'g', '1', NULL, 310000, 650000, 20, 'SP070-BRN-FS');
ELSE
    UPDATE BienTheSanPham SET MaBienThe = 'BT251', ID_SanPham = 70, MauSac = N'Nâu Cà Phê', TrongLuong = 'Freesize', GiaNhap = 310000, GiaBan = 650000, SoLuong = 20, Barcode = 'SP070-BRN-FS' WHERE ID_BienThe = 251;
SET IDENTITY_INSERT BienTheSanPham OFF;
GO

-- 5. ĐỒNG BỘ TỔNG TỒN KHO CHO TOÀN BỘ SẢN PHẨM SANPHAM
UPDATE SanPham SET
    TongSoLuong = ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE BienTheSanPham.ID_SanPham = SanPham.ID_SanPham), 50),
    SoLuong = ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE BienTheSanPham.ID_SanPham = SanPham.ID_SanPham), 50);
GO
