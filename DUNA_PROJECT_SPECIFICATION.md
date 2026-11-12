# TÀI LIỆU ĐẶC TẢ KỸ THUẬT & TOÀN BỘ CHỨC NĂNG DỰ ÁN DUNA
**Hệ thống Quản lý Bán lẻ Thời trang Nam Cao cấp DUNA (DUNA Men's Wear)**
*Tài liệu nguồn chuẩn xác (Single Source of Truth) được trích xuất trực tiếp từ mã nguồn Java, Cơ sở dữ liệu SQL Server và Bảng phân công nỗ lực (Effort Matrix) của dự án.*

---

## 1. TỔNG QUAN HỆ THỐNG & CÔNG NGHỆ (TECH STACK)

### 1.1. Kiến trúc & Công nghệ Thực tế
* **Mô hình kiến trúc:** MVC (Model - View - Controller) thuần trên nền Java Web.
* **Backend:** Java Servlet (`javax.servlet`), Java Filter (`AuthFilter` kiểm soát URL & RBAC), JDBC Connection (`connectService`).
* **Database:** Microsoft SQL Server (Database: `QuanLyBanHang`, file tạo bảng: `DATN.sql`).
* **Frontend:** JSP (JavaServer Pages), JSTL (`c:choose`, `c:when`, `c:otherwise`, `c:forEach`), HTML5, CSS Grid 2 cột, Flexbox (`theme.css`, `style.css`), Vanilla JavaScript.
* **Thư viện & Công cụ Tích hợp Ngoại vi:**
  * `Apache POI`: Xuất danh sách hóa đơn, báo cáo tài chính ra file Excel `.xlsx`.
  * `Chart.js`: Biểu đồ trực quan so sánh Doanh thu thuần, Tổng giá vốn & Lợi nhuận gộp theo ngày/tháng/năm.
  * `JsBarcode Engine`: Tự động sinh và hiển thị mã vạch định danh biến thể (`SPxxx-MAU-SIZE`), in tem mác thẻ bài.
  * `WebRTC API`: Quét mã vạch sản phẩm trực tiếp từ Camera điện thoại / webcam tại quầy thu ngân.
  * `Barcode Scanner USB`: Bắt sự kiện bàn phím nhanh từ súng quét barcode chuyên dụng.
  * `VietQR Dynamic API`: Sinh mã VietQR chuyển khoản động hiển thị chính xác số tiền và nội dung đơn hàng.

---

## 2. PHÂN QUYỀN HỆ THỐNG (RBAC) & ĐIỀU HƯỚNG GIAO DIỆN (NAVBAR)

Hệ thống được kiểm soát phân quyền chặt chẽ bằng `AuthFilter.java` và session đăng nhập (`LoginServlet.java`).

### 2.1. Quản lý Cửa hàng (Role: `MANAGER` / Admin)
Trên thanh điều hướng (Navbar) của Quản lý hiển thị chính xác **7 menu chức năng**:
1. `Quản lý sản phẩm` (`/ProductServlet` -> `quan-ly-san-pham.jsp`)
2. `Quản lý nhân sự` (`/EmployeeManagementServlet` -> `employee-management.jsp`, `add-employee.jsp`)
3. `Báo cáo thống kê` (`/OrderServlet?view=report` -> `bao-cao-thong-ke.jsp`)
4. `Quản lý khách hàng` (`/CustomerServlet` -> `quan-ly-khach-hang.jsp`)
5. `Bán hàng` (`/OrderServlet?view=sell` -> `ban-hang.jsp`)
6. `Quản lý hóa đơn` (`/OrderServlet` -> `quan-ly-hoa-don.jsp`)
7. `Quản lý Voucher` (`/VoucherServlet` -> `quan-ly-voucher.jsp`)
* *Khu vực tài khoản phải:* `Xin chào [Tên Quản lý]` (`EmployeeDetailServlet?mode=view`), `Đăng xuất` (`LogoutServlet`).

### 2.2. Nhân viên Thu ngân (Role: `EMPLOYEE` / Staff)
Trên thanh điều hướng (Navbar) của Nhân viên **chỉ hiển thị đúng 3 menu chức năng**:
1. `Thông tin cá nhân` (`EmployeeDetailServlet?id={nhanVienID}&mode=view`)
2. `Bán hàng` (`/OrderServlet?view=sell` -> `ban-hang.jsp`)
3. `Quản lý hóa đơn` (`/OrderServlet` -> `quan-ly-hoa-don.jsp`)
* *Khu vực tài khoản phải:* `Xin chào [Tên Nhân viên]`, `Đăng xuất` (`LogoutServlet`).
* *Ràng buộc AuthFilter:* Nếu Nhân viên cố tình truy cập vào các URL của Quản lý (`/ProductServlet`, `/EmployeeManagementServlet`, `/OrderServlet?view=report`, `/CustomerServlet`, `/VoucherServlet`, `add-employee.jsp`), `AuthFilter` sẽ tự động chặn và chuyển hướng về `/OrderServlet?view=sell`.

---

## 3. CƠ SỞ DỮ LIỆU DỰ ÁN (CÁC BẢNG TRONG `DATN.sql`)

1. **`Role`**: Quản lý vai trò (`1: MANAGER`, `2: EMPLOYEE`).
2. **`TrangThaiTaiKhoan`**: Trạng thái tài khoản (`1: Hoạt động`, `2: Khóa`).
3. **`TrangThaiNhanVien`**: Trạng thái làm việc của nhân sự (`Đang làm việc`, `Đã nghỉ việc`).
4. **`TrangThaiDonHang`**: Trạng thái đơn hàng (`Đã thanh toán`, `Chờ thanh toán / Đơn chờ`, `Đã hủy`).
5. **`DanhMuc`**: Danh mục thời trang (`ID_DanhMuc`, `TenDanhMuc`, `TyLeVat` mặc định 8%).
6. **`SanPham`**: Sản phẩm thời trang (`ID_SanPham`, `MaSP`, `TenSP`, `ID_DanhMuc`, `ThuongHieu`, `ChatLieu`, `FormDang`, `HinhAnh`, `GiaNhap`, `GiaBan`, `SoLuong`, `TongSoLuong`, `TyLeVat`, `TrangThai`, `MoTa`).
7. **`BienTheSanPham`**: Biến thể chi tiết (`ID_BienThe`, `MaBienThe`, `ID_SanPham`, `MauSac`, `TrongLuong` [Size S/M/L/XL...], `GiaNhap`, `GiaBan`, `SoLuong`, `Barcode`, Unique constraint `(ID_SanPham, MauSac, TrongLuong)`).
8. **`KhachHang`**: Hồ sơ khách hàng (`ID_KhachHang`, `MaKH`, `HoTen`, `SoDienThoai`, `Email`, `GioiTinh`, `NgaySinh`, `HangKhachHang`, `DiaChi`, `TrangThai`, `NgayBatDauChuKy` [chu kỳ 24 tháng]).
   * *Hạng thành viên:* `Thường` (< 2tr), `Bạc` (2tr - < 5tr), `Vàng` (5tr - < 10tr), `VIP / Kim Cương` (>= 10tr).
9. **`NhanVien`**: Nhân sự (`ID_NhanVien`, `MaNhanVien`, `HoVaTen`, `MaSoCCCD` [12 số], `NgaySinh`, `QueQuan`, `DiaChiHienTai`, `SoDienThoai`, `Email`, `LyDoKhoa`).
10. **`TaiKhoan`**: Tài khoản người dùng (`TaiKhoanID`, `Username`, `Password`, `Role_ID`, `TrangThaiTK_ID`, `NhanVienID`).
11. **`Vouchers`**: Quản lý khuyến mãi (`ID_Voucher`, `MaVoucher`, `TenChuongTrinh`, `LoaiVoucher`, `GiamGia`, `DonToiThieu`, `SoLuong`, `DaSuDung`, `DonHangSuDung`, `TrangThai`).
12. **`DonHang`**: Đơn hàng bán lẻ (`ID_DonHang`, `MaDonHang`, `ID_KhachHang`, `ID_Voucher`, `ID_NhanVien`, `TenNhanVien`, `TongTien`, `TienThueVAT`, `PhuongThucThanhToan`, `TrangThaiDH_ID`, `GhiChu`, `NgayTao`, `NgayThanhToan`).
13. **`ChiTietDonHang`**: Mặt hàng trong đơn (`ID_ChiTiet`, `ID_DonHang`, `ID_BienThe`, `ID_SanPham`, `TenSP`, `SoLuong`, `GiaNhapLucBan` [COGS], `DonGia`, `ThanhTien`).

---

## 4. MA TRẬN 64 USE CASE CHÍNH XÁC CỦA DỰ ÁN DUNA

*Trích xuất 100% nguyên văn từ mã nguồn thực tế và file `BANG_PHAN_CONG_EFFORT_DUNA_CHUAN_5MM.xlsx`.*
*Quy chuẩn Độ phức tạp UI: Chỉ áp dụng 2 mức theo chuẩn học thuật gồm **Chuẩn** (Form/Table cơ bản) và **Trung bình** (Responsive 2 cột/Modal/Tabs/Chart/WebRTC/CSS Grid), tuyệt đối không có mức Phức tạp.*
*Bảng đầy đủ các cột CRUD chi tiết: Read, Search, Create, Update, Delete, Độ phức tạp UI, Số Bảng DB, Job ngầm, 3rd Party API, Media/Export, Người Phụ Trách, Effort (Giờ).*

| STT | Tên Use Case / Chức năng | Read | Search | Create | Update | Delete | Độ phức tạp UI | Số Bảng DB | Job ngầm | 3rd Party API | Media/Export | Người Phụ Trách | Effort (Giờ) | Servlet / File Thực Thi |
|:---:|:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---|:---:|:---|
| **1** | Quản lý Danh mục thời trang nam (Áo sơ mi, Polo, Quần âu, Blazer) | True | True | True | True | True | Trung bình | 4-5 Bảng | False | False | False | Nguyễn Ngọc Minh | 12.7 | `ProductServlet.java`, `quan-ly-san-pham.jsp` |
| **2** | Quản lý Thuộc tính Chất liệu vải may mặc (Cotton, Kaki, Denim) | True | True | True | True | True | Chuẩn | 2-3 Bảng | False | False | False | Nguyễn Ngọc Minh | 8.6 | `ProductServlet.java`, `quan-ly-san-pham.jsp` |
| **3** | Quản lý Thuộc tính Form dáng trang phục (Slimfit, Regular, Oversize) | True | True | True | True | True | Trung bình | 2-3 Bảng | False | False | False | Nguyễn Ngọc Minh | 10.3 | `ProductServlet.java`, `quan-ly-san-pham.jsp` |
| **4** | Kiểm tra ràng buộc toàn vẹn khóa ngoại khi thao tác Danh mục | True | True | False | True | True | Chuẩn | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 10.8 | `ProductServlet.java`, SQL Foreign Keys constraint |
| **5** | Danh sách sản phẩm thời trang & Bộ lọc danh mục, trạng thái | True | True | True | True | False | Trung bình | 4-5 Bảng | False | False | True | Nguyễn Ngọc Minh | 13.8 | `ProductServlet.java`, `quan-ly-san-pham.jsp` |
| **6** | Tìm kiếm nâng cao sản phẩm theo Mã SP, Tên, Chất liệu, Form dáng | True | True | True | False | False | Chuẩn | 4-5 Bảng | False | False | False | Nguyễn Ngọc Minh | 6.7 | `ProductServlet.java` (multi-field search query) |
| **7** | Thêm mới sản phẩm thời trang nam (Giao diện CSS Grid 2 cột) | True | True | True | True | False | Trung bình | 4-5 Bảng | False | False | True | Nguyễn Ngọc Minh | 13.8 | `ProductServlet.java?action=add` (CSS Grid 2 cột) |
| **8** | Cập nhật thông tin chi tiết, hình ảnh & Mô tả giặt ủi sản phẩm | True | True | False | True | False | Trung bình | 4-5 Bảng | False | False | True | Nguyễn Ngọc Minh | 9.7 | `ProductServlet.java?action=edit`, upload ảnh & mô tả |
| **9** | Quản lý Giá vốn nhập hàng (COGS) để tính toán biên lợi nhuận gộp | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 16.1 | `ProductServlet.java`, tính biên lợi nhuận gộp từ COGS |
| **10** | Cấu hình Giá niêm yết bán lẻ & Thiết lập tỷ lệ thuế VAT 8% | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 16.1 | `OrderServlet.java?action=updateVAT`, tính lại thuế VAT 8% |
| **11** | Quản lý trạng thái kinh doanh sản phẩm (Còn hàng / Ngừng bán) | True | True | False | True | True | Chuẩn | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 10.8 | `ProductServlet.java?action=delete` (Soft delete status) |
| **12** | Xem chi tiết danh sách Biến thể Màu sắc & Kích cỡ (modalVariants) | True | True | True | False | False | Trung bình | 4-5 Bảng | False | False | True | Nguyễn Ngọc Minh | 9.7 | `ProductServlet.java?ajaxDetailAll=1`, `modalVariants` |
| **13** | Thêm mới biến thể Size/Màu kèm ràng buộc duy nhất (Unique DB) | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 16.1 | `ProductServlet.java?action=addVariant`, ràng buộc Unique |
| **14** | Cập nhật Giá bán riêng & Số lượng tồn kho theo từng biến thể | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 16.1 | `ProductServlet.java?action=editVariant`, cập nhật tổng kho |
| **15** | Xóa biến thể sản phẩm & Tự động đồng bộ số lượng tồn kho cha | True | True | False | True | True | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 12.9 | `ProductServlet.java?action=deleteVariant`, tự đồng bộ tồn kho |
| **16** | Bảng tra cứu tư vấn Size thông minh theo Chiều cao - Cân nặng | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Ngọc Minh | 16.1 | `ban-hang.jsp` (modal Tư vấn Size thông minh) |
| **17** | Đăng nhập hệ thống & Xác thực tài khoản đa vai trò (LoginServlet) | True | False | True | True | False | Chuẩn | 2-3 Bảng | False | False | False | Nguyễn Đức Anh | 6.2 | `LoginServlet.java`, `login.jsp` |
| **18** | Ghi nhớ trạng thái phiên đăng nhập qua Cookie an toàn (Remember Me) | True | False | True | True | False | Chuẩn | 2-3 Bảng | False | False | False | Nguyễn Đức Anh | 6.2 | `LoginServlet.java` (Cookie an toàn `yn_user`, `yn_pass`) |
| **19** | Bộ lọc kiểm soát quyền truy cập URL theo vai trò (AuthFilter RBAC) | True | True | False | True | False | Chuẩn | 4-5 Bảng | True | False | False | Nguyễn Đức Anh | 9.4 | `AuthFilter.java` (RBAC URL Filter tự động chặn & redirect) |
| **20** | Đổi mật khẩu tài khoản người dùng đang đăng nhập (ChangeCredentials) | True | False | False | True | False | Chuẩn | 2-3 Bảng | False | False | False | Nguyễn Đức Anh | 3.9 | `ChangeCredentialsServlet.java`, đổi mật khẩu an toàn |
| **21** | Tự động sinh mã Barcode định danh biến thể (SPxxx-MAU-SIZE) | True | True | True | True | False | Chuẩn | 4-5 Bảng | True | True | False | Nguyễn Đức Anh | 20.2 | `ProductServlet.java`, thuật toán auto-generate Barcode |
| **22** | Xem trước thẻ bài & In tem mác Barcode DUNA (JsBarcode Engine) | True | True | True | False | False | Trung bình | 2-3 Bảng | False | True | True | Nguyễn Đức Anh | 11.8 | `quan-ly-san-pham.jsp`, `JsBarcode Engine` in tem thẻ bài |
| **23** | Giao diện bán hàng POS 1 chạm (Thiết kế 2 cột chuẩn UX DUNA) | True | True | True | True | True | Trung bình | > 5 Bảng | False | False | False | Nguyễn Đức Anh | 15.8 | `OrderServlet?view=sell`, `ban-hang.jsp` (POS UX 2 cột) |
| **24** | Tìm kiếm nhanh sản phẩm tại quầy bằng Auto-complete siêu tốc | True | True | False | False | False | Trung bình | 4-5 Bảng | False | False | False | Nguyễn Đức Anh | 4.6 | `ban-hang.jsp` (AJAX dropdown tìm kiếm sản phẩm siêu tốc) |
| **25** | Tích hợp súng bắn mã vạch Barcode USB quét hàng siêu tốc tại quầy | True | True | True | True | False | Chuẩn | > 5 Bảng | False | True | False | Nguyễn Đức Anh | 18.0 | `ban-hang.jsp` (Hardware keyboard listener súng quét Barcode USB) |
| **26** | Quét mã vạch sản phẩm trực tiếp qua Camera WebRTC tại quầy thu ngân | True | True | True | True | False | Trung bình | 4-5 Bảng | False | True | False | Nguyễn Đức Anh | 17.3 | `ban-hang.jsp` (WebRTC Camera barcode scanning stream) |
| **27** | Quản lý giỏ hàng POS (Thêm món, chỉnh số lượng, xóa món) | True | True | True | True | True | Trung bình | > 5 Bảng | False | False | False | Nguyễn Đức Anh | 15.8 | `ban-hang.jsp` (Reactive Cart state, chỉnh số lượng/xóa món) |
| **28** | Kiểm tra & Cảnh báo tồn kho thời gian thực khi đưa hàng vào giỏ | True | True | False | True | False | Chuẩn | > 5 Bảng | True | False | False | Nguyễn Đức Anh | 11.8 | `ban-hang.jsp` (Async stock validation check thời gian thực) |
| **29** | Quản lý nhiều đơn hàng chờ cùng lúc tại quầy (Pending Order Tabs) | True | True | True | True | True | Trung bình | > 5 Bảng | True | False | False | Nguyễn Đức Anh | 22.2 | `OrderServlet?action=create&invoiceType=hold` (Hàng đợi đơn chờ) |
| **30** | Tiếp nhận & Mở giao diện Đổi Size / Đổi Màu sản phẩm (modalExchange) | True | True | False | False | False | Trung bình | > 5 Bảng | True | False | False | Nguyễn Đức Anh | 8.1 | `ban-hang.jsp` (`modalExchange` tiếp nhận đổi size/màu) |
| **31** | Tự động tính bù trừ tiền chênh lệch khi khách đổi Size/Mẫu khác | True | True | False | True | False | Chuẩn | > 5 Bảng | True | False | False | Nguyễn Đức Anh | 11.8 | `OrderServlet?action=exchangeItem` (Tự tính bù trừ tiền chênh) |
| **32** | Cập nhật kho hàng tự động khi đổi hàng (Cộng kho cũ, Trừ kho mới) | True | True | True | True | False | Chuẩn | > 5 Bảng | True | False | False | Nguyễn Đức Anh | 16.8 | `OrderServlet.java` (Atomic transaction update 2 chiều kho hàng) |
| **33** | Tra cứu khách hàng thành viên bằng SĐT (+84) tại màn hình POS | True | True | True | False | False | Trung bình | 4-5 Bảng | False | False | False | Lê Minh Bách | 8.1 | `ban-hang.jsp` (Tra cứu SĐT chuẩn hóa +84 tại POS) |
| **34** | Thêm nhanh khách hàng mới trực tiếp tại quầy thu ngân (modalAddKH) | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Lê Minh Bách | 16.1 | `OrderServlet?action=addCustomer`, `modalAddKH` tại quầy |
| **35** | Tự động áp dụng chiết khấu hạng VIP vào giỏ hàng POS | True | True | False | True | False | Chuẩn | > 5 Bảng | True | False | False | Lê Minh Bách | 11.8 | `ban-hang.jsp` (Tự động áp dụng chiết khấu hạng thành viên VIP) |
| **36** | Tra cứu, kiểm tra điều kiện & Áp dụng mã Voucher tại quầy | True | True | True | True | False | Trung bình | > 5 Bảng | True | False | False | Lê Minh Bách | 20.2 | `ban-hang.jsp` (Tra cứu, kiểm tra điều kiện & áp dụng Voucher) |
| **37** | Tự động tính toán tổng hóa đơn (Tiền hàng, giảm VIP, Voucher, VAT) | True | True | False | True | False | Trung bình | > 5 Bảng | True | False | False | Lê Minh Bách | 14.1 | `ban-hang.jsp` (Reactive formula tính tổng tiền, thuế, giảm trừ) |
| **38** | Tính tiền thối tự động khi khách thanh toán bằng Tiền mặt | True | False | True | True | False | Chuẩn | > 5 Bảng | False | False | False | Lê Minh Bách | 9.6 | `ban-hang.jsp` (Thuật toán tính tiền thối khi khách trả tiền mặt) |
| **39** | Sinh mã VietQR thanh toán động hiển thị chính xác số tiền đơn | True | True | True | True | False | Trung bình | 4-5 Bảng | False | True | True | Lê Minh Bách | 20.7 | `ban-hang.jsp`, VietQR dynamic API sinh mã QR động kèm số tiền |
| **40** | Xác nhận thanh toán Chuyển khoản QR tức thì trên Mobile (QR-Mobile) | True | True | False | True | False | Trung bình | > 5 Bảng | True | True | False | Lê Minh Bách | 21.2 | `QRConfirmServlet.java`, `qr-mobile.jsp` (Xác thực QR tức thì) |
| **41** | In hóa đơn thanh toán nhiệt khổ K80/K57 tại quầy thu ngân | True | True | True | False | False | Trung bình | > 5 Bảng | False | False | True | Lê Minh Bách | 12.1 | `ban-hang.jsp` (Template in nhiệt K80/K57 tại quầy thu ngân) |
| **42** | Danh sách toàn bộ hóa đơn bán hàng & Lọc theo khoảng ngày | True | True | True | False | False | Trung bình | > 5 Bảng | False | False | False | Lê Minh Bách | 10.1 | `OrderServlet`, `quan-ly-hoa-don.jsp` (Bộ lọc ngày/trạng thái) |
| **43** | Xem chi tiết hóa đơn (Từng món, Size/Màu, Giá bán, Chiết khấu, VAT) | True | True | False | False | False | Trung bình | > 5 Bảng | False | False | False | Lê Minh Bách | 5.8 | `OrderServlet?ajaxDetail=1` (Modal chi tiết đa bảng đơn hàng) |
| **44** | In lại hóa đơn bán hàng cho khách khi có khiếu nại (Re-print) | True | True | False | False | False | Chuẩn | > 5 Bảng | False | False | True | Lê Minh Bách | 5.8 | `quan-ly-hoa-don.jsp` (In lại hóa đơn bán lẻ cho khách khiếu nại) |
| **45** | Xuất dữ liệu danh sách hóa đơn bán hàng ra file Excel (POI) | True | True | False | False | False | Trung bình | > 5 Bảng | False | False | True | Lê Minh Bách | 6.9 | `OrderServlet?export=excel` (Apache POI xuất danh sách hóa đơn) |
| **46** | Thống kê Doanh thu thuần, Tổng giá vốn & Lợi nhuận gộp Hôm nay | True | True | False | False | False | Chuẩn | > 5 Bảng | True | False | False | Lê Minh Bách | 6.7 | `OrderServlet?view=report` (Thống kê doanh thu, giá vốn, lãi hôm nay) |
| **47** | Thống kê Doanh thu, Tổng giá vốn & Lợi nhuận gộp theo Tháng/Năm | True | True | True | False | False | Chuẩn | > 5 Bảng | True | False | False | Lê Minh Bách | 11.8 | `OrderServlet?view=report` (Thống kê tài chính đa chiều tháng/năm) |
| **48** | Biểu đồ trực quan so sánh Doanh thu & Lợi nhuận qua Chart.js | True | True | True | False | False | Trung bình | > 5 Bảng | False | True | True | Lê Minh Bách | 18.1 | `bao-cao-thong-ke.jsp`, `Chart.js` (Biểu đồ doanh thu & lợi nhuận) |
| **49** | Xem danh sách khách hàng & Phân hạng thành viên (Đồng -> KC) | True | True | True | False | False | Trung bình | 4-5 Bảng | False | False | False | Nguyễn Kiều An | 8.1 | `CustomerServlet.java`, `quan-ly-khach-hang.jsp` (Phân hạng VIP) |
| **50** | Tìm kiếm khách hàng theo Tên, Số điện thoại và Lọc theo Hạng VIP | True | True | False | False | False | Trung bình | 4-5 Bảng | False | False | False | Nguyễn Kiều An | 4.6 | `CustomerServlet.java` (Tìm kiếm SĐT, tên và lọc hạng thành viên) |
| **51** | Thêm mới hồ sơ khách hàng chuẩn hóa đầu số (+84) | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 16.1 | `CustomerServlet?action=add` (Chuẩn hóa SĐT E.164, tạo chu kỳ) |
| **52** | Cập nhật thông tin khách hàng, ngày sinh và địa chỉ liên lạc | True | True | False | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 11.3 | `CustomerServlet?action=edit` (Cập nhật thông tin khách hàng) |
| **53** | Xem lịch sử mua sắm chi tiết & Tổng chi tiêu tích lũy của khách | True | True | True | False | False | Trung bình | > 5 Bảng | False | False | False | Nguyễn Kiều An | 10.1 | `quan-ly-khach-hang.jsp` (Modal lịch sử mua sắm chi tiết & chi tiêu) |
| **54** | Tự động tích lũy chi tiêu và thăng hạng thành viên VIP | True | True | False | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 11.3 | `CustomerServlet.java` (Job tự động tích lũy chi tiêu & nâng hạng VIP) |
| **55** | Chính sách chu kỳ Reset hạng khách hàng sau 2 năm (Countdown) | True | True | False | True | False | Chuẩn | > 5 Bảng | True | False | False | Nguyễn Kiều An | 11.8 | `CustomerServlet.java` (Job countdown chu kỳ 24 tháng reset hạng) |
| **56** | Bảng tổng quan 4 chỉ số thống kê Voucher (Phát hành, Còn lại, Đã dùng) | True | True | True | False | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 11.3 | `VoucherServlet.java`, `quan-ly-voucher.jsp` (4 chỉ số KPI voucher) |
| **57** | Tạo Voucher VIP lẻ với mã ngẫu nhiên tự sinh (Giảm % hoặc Tiền mặt) | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 16.1 | `VoucherServlet?action=addCustom` (Tạo voucher lẻ mã tự sinh ngẫu nhiên) |
| **58** | Cấu hình điều kiện kích hoạt Voucher (Đơn tối thiểu, Hạn dùng, Lượt dùng) | True | True | False | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 11.3 | `VoucherServlet.java` (Cấu hình điều kiện đơn tối thiểu, hạn dùng) |
| **59** | Tạo lô Voucher tự động hàng loạt (Batch Generation) cho sự kiện | True | True | True | False | False | Trung bình | > 5 Bảng | True | False | True | Nguyễn Kiều An | 16.9 | `VoucherServlet?action=generateBatch` (Tự sinh lô voucher chống trùng) |
| **60** | Danh sách nhân viên bán hàng & Quản lý kèm trạng thái hoạt động | True | True | True | False | False | Trung bình | > 5 Bảng | False | False | False | Nguyễn Kiều An | 10.1 | `EmployeeManagementServlet.java` (Danh sách nhân sự đa bảng) |
| **61** | Xem chi tiết hồ sơ nhân sự đầy đủ (CCCD 12 số, Quê quán, Địa chỉ) | True | True | True | False | False | Trung bình | > 5 Bảng | False | False | False | Nguyễn Kiều An | 10.1 | `EmployeeDetailServlet.java` (Hồ sơ nhân sự CCCD 12 số & lịch sử ca) |
| **62** | Thêm mới nhân viên, kiểm tra trùng CCCD & Tự sinh tài khoản (QLxxx/NVxxx) | True | True | True | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 16.1 | `AddEmployeeServlet.java`, `add-employee.jsp` (Tự sinh mã QL/NV) |
| **63** | Khóa tài khoản nhân viên tức thời & Bắt buộc nhập Lý do khóa | True | True | False | True | False | Trung bình | 4-5 Bảng | True | False | False | Nguyễn Kiều An | 11.3 | `ToggleEmployeeStatusServlet.java` (Khóa tài khoản kèm lý do) |
| **64** | Xuất toàn bộ báo cáo tài chính & Biểu đồ ra file Excel (POI) | True | True | True | True | False | Trung bình | > 5 Bảng | True | False | True | Nguyễn Kiều An | 24.2 | `OrderServlet?view=report&export=excel` (Apache POI báo cáo tài chính) |

## 5. TỔNG HỢP NỖ LỰC & PHÂN CÔNG THÀNH VIÊN (CHUẨN 5.00000 MM)

### 5.1. Bảng Tổng Hợp Phân Bổ Nhân Lực (Effort KPI)

| STT | Họ tên Thành viên | Mã Sinh Viên | Phạm vi Use Case | Số lượng Use Case | Số ca UI Trung bình | Số ca UI Chuẩn | 2-3 Bảng | 4-5 Bảng | > 5 Bảng | Job ngầm | 3rd API | Media/Export | Tổng Giờ Effort | Tỷ lệ (%) |
|:---:|:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| 1 | **Nguyễn Ngọc Minh** | TH08331 | STT 1 - 16 (Sản phẩm, Danh mục, Biến thể, Giá) | 16 | 12 | 4 | 2 | 14 | 0 | 8 | 0 | 4 | **200.3** | 25.04% |
| 2 | **Nguyễn Đức Anh** | TH08308 | STT 17 - 32 (Xác thực, Mã vạch, Bán hàng POS, Đổi hàng) | 16 | 7 | 9 | 4 | 4 | 8 | 7 | 4 | 1 | **199.9** | 24.99% |
| 3 | **Lê Minh Bách** | TH08442 | STT 33 - 48 (Thanh toán, Hóa đơn, Báo cáo & Biểu đồ) | 16 | 11 | 5 | 0 | 3 | 13 | 7 | 3 | 5 | **199.1** | 24.89% |
| 4 | **Nguyễn Kiều An** | TH08490 | STT 49 - 64 (Khách hàng CRM, Voucher, Nhân sự, Xuất Excel) | 16 | 15 | 1 | 0 | 10 | 6 | 11 | 0 | 2 | **200.7** | 25.09% |
| **TỔNG** | **4 Thành viên** | | **Toàn bộ hệ thống DUNA** | **64** | **45** | **19** | **6** | **31** | **27** | **33** | **7** | **12** | **800.0** | **100.00%** |

### 5.2. Chỉ Số Tổng Quan Quy Đổi Chuẩn Học Thuật

* **Tổng nỗ lực dự án (Total Effort):** **800.0 Giờ**
* **Tổng ngày công (Total Man-Day - MD):** 800.0 / 8 = **100.00 MD** (Tiêu chuẩn 8 giờ làm việc / ngày)
* **Tổng tháng công (Total Man-Month - MM):** 100.00 / 20 = **5.00000 MM** (Tiêu chuẩn 20 ngày làm việc / tháng $\rightarrow$ Chuẩn xác **5.00000 MM**)
* **Mức độ cân bằng nỗ lực:** Mỗi thành viên đảm nhiệm đúng 16 Use Case, nỗ lực dao động từ **199.1h đến 200.7h** (Tỷ lệ phân bổ đều xấp xỉ **25.0%**, chênh lệch dưới 0.2%).

## 6. NGUYÊN TẮC BẮT BUỘC KHI LÀM VIỆC VỚI DỰ ÁN DUNA

1. **Tuyệt đối không bịa đặt chức năng:** Mọi sơ đồ (Use Case, Sequence, Activity, Class), tài liệu phân tích, đặc tả kiểm thử (Test Case) chỉ được lấy duy nhất từ 64 chức năng được liệt kê ở Mục 4 và các bảng cơ sở dữ liệu ở Mục 3.
2. **Tuân thủ đúng thanh điều hướng (Navbar) theo Role:**
   * Quản lý có 7 menu: *Sản phẩm, Nhân sự, Báo cáo thống kê, Khách hàng, Bán hàng, Hóa đơn, Voucher* (+ Thông tin cá nhân).
   * Nhân viên chỉ có 3 menu: *Thông tin cá nhân, Bán hàng, Quản lý hóa đơn*.
3. **Mọi câu trả lời tiếp theo phải đối chiếu trực tiếp vào file này (`d:\fpt\DUNA\DUNA_PROJECT_SPECIFICATION.md`).**
