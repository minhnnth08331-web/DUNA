# AGENTS.md - Quy Tắc Dự Án DUNA (Project Directives & Constraints)

> **Dự án:** DUNA Men's Wear POS Web Application  
> **Định vị:** Thời trang nam tầm trung (phân khúc giá hợp lý, phổ thông, thanh lịch & hiện đại)  
> **Công nghệ:** Java Servlet (`javax.servlet-api:4.0.1`), JSP/JSTL, Apache Tomcat 7 (`tomcat7-maven-plugin:2.2`), Microsoft SQL Server (`QUANLYBANHANGQUANAONAMDUNA`).
> **Thành viên & Phân bổ 64 Use Case:**  
> - Member 1: Nguyễn Ngọc Minh - TH08331 (UC 01 -> UC 16)  
> - Member 2: Nguyễn Đức Anh - TH08308 (UC 17 -> UC 32)  
> - Member 3: Lê Minh Bách - TH08442 (UC 33 -> UC 48)  
> - Member 4: Nguyễn Kiều An - TH08490 (UC 49 -> UC 64)  

---

## ⚠️ QUY TẮC BIÊN DỊCH & MÔI TRƯỜNG BUILD (CRITICAL)

1. **Khóa Chuẩn Bytecode Java 8 (Class file version 52.0):**
   - Môi trường chạy máy chủ là **Tomcat 7 trên Java 21 / Java 8**.
   - **CẤM** biên dịch mã Java mà không kèm cờ `--release 8` (tránh lỗi `UnsupportedClassVersionError: class file version 69.0/65.0`).
   - Cấu hình trong `pom.xml` luôn phải có `maven-compiler-plugin` với `<source>1.8</source>` và `<target>1.8</target>`.
   - Khi chạy lệnh biên dịch thủ công qua dòng lệnh: Bắt buộc dùng `javac --release 8 ...`.

2. **Khởi Động Ứng Dụng:**
   ```bash
   mvn tomcat7:run
   ```
   Ứng dụng khởi chạy tại: `http://localhost:8080/DUNA`

---

## 🔒 QUY TẮC BẢO MẬT & NGHIỆP VỤ (SECURITY & BUSINESS RULES)

1. **Xác thực & Mật khẩu:**
   - **CẤM** lưu mật khẩu thô (plaintext password) vào Cookie của client.
   - Chỉ lưu username vào `yn_user` kèm cờ `HttpOnly = true`.
2. **Phân quyền (RBAC) & Filter:**
   - Mọi Servlet quản trị bắt buộc phải được bảo vệ trong `AuthFilter.java` để ngăn chặn nhân viên `EMPLOYEE` leo quyền.
3. **Phòng chống Crash 500:**
   - Mọi tham số ID từ URL hoặc Form (`request.getParameter("id")`) phải được kiểm tra `null`, rỗng và bọc trong khối `try-catch NumberFormatException`.
4. **Nhất quán thương hiệu DUNA (Phân khúc thời trang nam tầm trung):**
   - DUNA là thương hiệu chuỗi cửa hàng thời trang nam **tầm trung** (mid-range: áo polo, sơ mi, quần âu, khaki, áo khoác... với phân khúc giá hợp lý, chất lượng vải tốt, thiết kế hiện đại, thanh lịch và phổ thông cho nam giới).
   - Tuyệt đối không định vị nhầm lẫn sang hàng xa xỉ/luxury cao cấp hay sao chép code từ các ngành hàng khác.
5. **Ghi Nhận Lịch Sử Chỉnh Sửa:**
   - Bất cứ khi nào hoàn thành một lượt sửa lỗi, cập nhật logic hay thay đổi giao diện, bắt buộc phải cập nhật chi tiết nội dung thay đổi vào file `LICH_SU_CHINH_SUA.md` tại thư mục gốc.

---

## 🌳 SƠ ĐỒ KIẾN TRÚC CHUẨN XÁC 64 USE CASE (KHỚP 100% CODE THẬT)

```text
src/main/java/com/duna/
│
├── common/                                                 # [TIỆN ÍCH & THỰC THỂ DÙNG CHUNG]
│   ├── connection/
│   │   └── connectService.java                             # Kết nối JDBC SQL Server (Singleton/Pool)
│   ├── entities/                                           # 19 Entity ánh xạ 1:1 CSDL SQL Server DUNA
│   │   ├── BangTuVanSize.java, BienTheSanPham.java, ChatLieu.java, ChiTietDonHang.java
│   │   ├── ChiTietHoaDon.java, DanhMuc.java, DonHang.java, FormDang.java
│   │   ├── HangThanhVien.java, HoaDon.java, KhachHang.java, KichCo.java
│   │   ├── LichSuKhoaTaiKhoan.java, LichSuSuDungVoucher.java, LichSuTichDiem.java
│   │   ├── MauSac.java, NhanVien.java, PhieuDoiHang.java, SanPham.java
│   │   └── VaiTro.java, Voucher.java
│   ├── filter/
│   │   └── AuthFilter.java                                 # Bộ lọc bảo mật phân quyền RBAC
│   └── util/                                               # 6 Lớp tiện ích chuẩn Java 8
│       ├── BarcodeUtil.java                                # Chuẩn hóa chuỗi Barcode SPxxx-MAU-SIZE
│       ├── CurrencyUtil.java                               # Định dạng VNĐ, tính thuế VAT 8%, tiền thối
│       ├── DateUtil.java                                   # Chu kỳ 3 tháng cuốn chiếu, định dạng ngày
│       ├── JsonUtil.java                                   # Bộ serializer JSON thuần Java 8 (chống lỗi Tomcat 7)
│       ├── PasswordUtil.java                               # Kiểm tra độ mạnh mật khẩu & băm SHA-256
│       └── SizeUtil.java (SizeGuideUtil.java)              # Ma trận tư vấn Size theo Chiều cao - Cân nặng
│
├── thanhvien1_minh/                                        # [MEMBER 1: NGUYỄN NGỌC MINH - TH08331]
│   ├── controller/
│   │   └── ThanhVien1Servlet.java                          # Controller điều phối UC 01 -> UC 16
│   └── repository/                                         # ĐỦ 16 REPOSITORY CHO 16 USE CASE
│       ├── UC01_QuanLyDanhMucRepo.java                     # UC 01: Quản lý Danh mục ➔ getDanhSachDanhMuc(), themDanhMuc(), capNhatDanhMuc(), xoaDanhMuc()
│       ├── UC02_QuanLyChatLieuRepo.java                    # UC 02: Quản lý Chất liệu ➔ getChatLieuList(), themChatLieu(), xoaChatLieu()
│       ├── UC03_QuanLyFormDangRepo.java                    # UC 03: Quản lý Form dáng ➔ getFormDangList(), themFormDang(), xoaFormDang()
│       ├── UC04_KiemTraKhoaNgoaiDanhMucRepo.java           # UC 04: Ràng buộc khóa ngoại Danh mục ➔ checkDanhMucForeignKey()
│       ├── UC05_DanhSachSanPhamRepo.java                   # UC 05: Danh sách & Lọc sản phẩm ➔ getDanhSachSanPham()
│       ├── UC06_TimKiemSanPhamNangCaoRepo.java             # UC 06: Tìm kiếm nâng cao ➔ searchAdvanced()
│       ├── UC07_ThemMoiSanPhamRepo.java                    # UC 07: Thêm mới sản phẩm ➔ themSanPham()
│       ├── UC08_CapNhatSanPhamRepo.java                    # UC 08: Cập nhật thông tin & ảnh ➔ capNhatSanPham()
│       ├── UC09_QuanLyGiaVonCOGSRepo.java                  # UC 09: Quản lý Giá vốn COGS ➔ tinhLoiNhuanGop(), capNhatGiaNhap()
│       ├── UC10_CauHinhGiaBanVATRepo.java                  # UC 10: Cấu hình Giá bán & VAT 8% ➔ cauHinhGiaNiemYetVAT()
│       ├── UC11_TrangThaiKinhDoanhRepo.java                # UC 11: Trạng thái kinh doanh ➔ toggleTrangThaiKinhDoanh(), capNhatTrangThai()
│       ├── UC12_DanhSachBienTheRepo.java                   # UC 12: Chi tiết biến thể Size/Màu ➔ getVariantsByProductId()
│       ├── UC13_ThemBienTheUniqueRepo.java                 # UC 13: Thêm biến thể duy nhất (Unique) ➔ themBienTheUnique()
│       ├── UC14_CapNhatGiaTonBienTheRepo.java              # UC 14: Cập nhật giá & tồn kho biến thể ➔ capNhatGiaVaTonKhoBienThe()
│       ├── UC15_XoaBienTheDongBoKhoRepo.java               # UC 15: Xóa biến thể đồng bộ kho ➔ xoaBienTheVaDongBoKho()
│       └── UC16_TuVanSizeThongMinhRepo.java                # UC 16: Tư vấn Size thông minh ➔ tuVanSize()
│
├── thanhvien2_ducanh/                                      # [MEMBER 2: NGUYỄN ĐỨC ANH - TH08308]
│   ├── controller/
│   │   └── ThanhVien2Servlet.java                          # Controller điều phối UC 17 -> UC 32
│   └── repository/                                         # ĐỦ 16 REPOSITORY CHO 16 USE CASE
│       ├── UC17_DangNhapXacThucRepo.java                   # UC 17: Đăng nhập & Xác thực ➔ authenticate()
│       ├── UC18_GhiNhoCookieRepo.java                      # UC 18: Ghi nhớ Cookie an toàn ➔ isValidRememberMeToken()
│       ├── UC19_PhanQuyenRBACRepo.java                     # UC 19: Bộ lọc phân quyền RBAC ➔ checkRoleAccess()
│       ├── UC20_DoiMatKhauRepo.java                        # UC 20: Đổi mật khẩu tài khoản ➔ doiMatKhau()
│       ├── UC21_SinhMaBarcodeRepo.java                     # UC 21: Tự động sinh mã Barcode ➔ generateBarcode()
│       ├── UC22_InTemBarcodeRepo.java                      # UC 22: Thẻ bài & In tem barcode ➔ renderBarcodeLabel()
│       ├── UC23_BanHangPOS1ChamRepo.java                   # UC 23: Giao diện POS 1 chạm ➔ getPOSLayoutConfig()
│       ├── UC24_TimKiemAutoCompleteRepo.java               # UC 24: Auto-complete sản phẩm ➔ searchAutoComplete()
│       ├── UC25_QuetBarcodeUSBRepo.java                    # UC 25: Súng quét Barcode USB ➔ findByBarcodeUSB()
│       ├── UC26_QuetBarcodeCameraRepo.java                 # UC 26: Quét Barcode Camera WebRTC ➔ handleWebRTCScan()
│       ├── UC27_QuanLyGioHangPOSRepo.java                  # UC 27: Quản lý giỏ hàng POS ➔ themVaoGioHang(), capNhatGioHang(), xoaKhoiGioHang(), xoaToanBoGioHang()
│       ├── UC28_CanhBaoTonKhoRepo.java                     # UC 28: Cảnh báo tồn kho thời gian thực ➔ checkInventoryRealtime()
│       ├── UC29_QuanLyDonHangChoRepo.java                  # UC 29: Quản lý đơn hàng chờ (Pending Tabs) ➔ createPendingTab(), getPendingTab(), removePendingTab(), getAllPendingTabs()
│       ├── UC30_TiepNhanDoiSizeMauRepo.java                # UC 30: Mở giao diện đổi Size/Màu ➔ openExchangeModal()
│       ├── UC31_TinhBuTruTienDoiHangRepo.java              # UC 31: Tính bù trừ tiền đổi hàng ➔ tinhTienBuTruDoiHang()
│       └── UC32_CapNhatKhoDoiHangRepo.java                 # UC 32: Cập nhật kho đổi hàng ➔ capNhatKhoKhiDoiHang()
│
├── thanhvien3_bach/                                        # [MEMBER 3: LÊ MINH BÁCH - TH08442]
│   ├── controller/
│   │   └── ThanhVien3Servlet.java                          # Controller điều phối UC 33 -> UC 48
│   └── repository/                                         # ĐỦ 16 REPOSITORY CHO 16 USE CASE
│       ├── UC33_TraCuuKhachHangPOSRepo.java                # UC 33: Tra cứu khách hàng theo SĐT ➔ searchCustomerByPhone()
│       ├── UC34_ThemNhanhKhachHangPOSRepo.java             # UC 34: Thêm nhanh khách hàng tại quầy ➔ quickAddCustomer()
│       ├── UC35_ApDungChietKhauVIPRepo.java                # UC 35: Chiết khấu hạng VIP vào giỏ hàng ➔ tinhGiamGiaHangVIP()
│       ├── UC36_ApDungVoucherPOSRepo.java                  # UC 36: Kiểm tra & áp dụng Voucher ➔ kiemTraVaApDungVoucher()
│       ├── UC37_TinhTongHoaDonRepo.java                    # UC 37: Tính toán tổng hóa đơn (+VAT) ➔ tinhToanTongHoaDon()
│       ├── UC38_ThanhToanTienMatRepo.java                  # UC 38: Tính tiền thối tự động ➔ tinhTienThoi()
│       ├── UC39_SinhMaVietQRDongRepo.java                  # UC 39: Sinh mã VietQR Napas 247 động ➔ sinhMaVietQR()
│       ├── UC40_XacNhanThanhToanQRRepo.java                # UC 40: Xác nhận chuyển khoản tức thì ➔ xacNhanChuyenKhoanQRTucThi()
│       ├── UC41_InHoaDonNhietK80Repo.java                  # UC 41: In hóa đơn nhiệt K80/K57 ➔ taoNoiDungHoaDonK80()
│       ├── UC42_DanhSachHoaDonRepo.java                    # UC 42: Danh sách đơn hàng theo ngày ➔ getDonHangTheoNgay()
│       ├── UC43_XemChiTietHoaDonRepo.java                  # UC 43: Xem chi tiết hóa đơn ➔ getChiTietHoaDon()
│       ├── UC44_InLaiHoaDonRepo.java                       # UC 44: In lại hóa đơn bán lẻ (Re-print) ➔ reprintHoaDon()
│       ├── UC45_XuatExcelHoaDonRepo.java                   # UC 45: Xuất hóa đơn ra Excel POI ➔ exportOrdersData()
│       ├── UC46_ThongKeLoiNhuanHomNayRepo.java             # UC 46: Doanh thu & lợi nhuận hôm nay ➔ getDoanhThuHomNay()
│       ├── UC47_ThongKeLoiNhuanThangNamRepo.java           # UC 47: Doanh thu theo tháng/năm ➔ getDoanhThuThangNam()
│       └── UC48_BieuDoDoanhThuChartJSRepo.java             # UC 48: Biểu đồ doanh thu Chart.js ➔ getChartData()
│
└── thanhvien4_kieuan/                                      # [MEMBER 4: NGUYỄN KIỀU AN - TH08490]
    ├── controller/
    │   └── ThanhVien4Servlet.java                          # Controller điều phối UC 49 -> UC 64
    └── repository/                                         # ĐỦ 16 REPOSITORY CHO 16 USE CASE
        ├── UC49_DanhSachKhachHangVIPRepo.java              # UC 49: Danh sách & phân hạng khách hàng ➔ getDanhSachKhachHangVaHang()
        ├── UC50_TimKiemKhachHangVIPRepo.java               # UC 50: Tìm kiếm & lọc khách hàng VIP ➔ searchKhachHang()
        ├── UC51_ThemMoiKhachHangRepo.java                  # UC 51: Thêm khách hàng chuẩn hóa (+84) ➔ themKhachHangChuanHoa()
        ├── UC52_CapNhatKhachHangRepo.java                  # UC 52: Cập nhật thông tin khách hàng ➔ capNhatThongTinKhachHang()
        ├── UC53_LichSuMuaSamKhachRepo.java                 # UC 53: Lịch sử mua sắm tích lũy ➔ getLichSuMuaSam()
        ├── UC54_TichLuyThangHangVIPRepo.java               # UC 54: Tích lũy chi tiêu & thăng hạng VIP ➔ tichLuyChiTieuVaThangHang()
        ├── UC55_ResetHang3ThangJobRepo.java                # UC 55: Job reset hạng 3 tháng cuốn chiếu ➔ jobResetHang3ThangCuonChieu()
        ├── UC56_ThongKe4ChiSoVoucherRepo.java              # UC 56: Bảng 4 chỉ số thống kê Voucher ➔ getThongKeVoucher()
        ├── UC57_TaoVoucherVIPLeRepo.java                   # UC 57: Tạo Voucher VIP lẻ tự sinh mã ➔ taoVoucherVIPLe()
        ├── UC58_CauHinhDieuKienVoucherRepo.java            # UC 58: Cấu hình điều kiện Voucher ➔ cauHinhDieuKienVoucher()
        ├── UC59_TaoLoVoucherHangLoatRepo.java              # UC 59: Tạo lô Voucher tự động ➔ generateBatchVouchers()
        ├── UC60_DanhSachNhanVienRepo.java                  # UC 60: Danh sách & trạng thái nhân viên ➔ getDanhSachNhanVienVaTrangThai(), capNhatTrangThaiNhanVien()
        ├── UC61_XemChiTietHoSoNhanSuRepo.java              # UC 61: Xem chi tiết CCCD & hồ sơ nhân sự ➔ getChiTietHoSoCCCD()
        ├── UC62_ThemNhanVienTuSinhAccountRepo.java         # UC 62: Thêm nhân viên kiểm tra CCCD ➔ themNhanVienKiemTraCCCD()
        ├── UC63_KhoaTaiKhoanNhanVienRepo.java              # UC 63: Khóa tài khoản kèm lý do & mở khóa ➔ khoaTaiKhoanNhanVien(), moKhoaTaiKhoanNhanVien()
        └── UC64_XuatBaoCaoTaiChinhExcelRepo.java           # UC 64: Báo cáo tài chính ra file Excel POI ➔ getFinancialReportData()
```
