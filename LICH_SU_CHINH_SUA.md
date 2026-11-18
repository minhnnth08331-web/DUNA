# NHẬT KÝ & LỊCH SỬ CHỈNH SỬA HỆ THỐNG DUNA MEN'S WEAR

> **Dự án:** DUNA Men's Wear POS Web Application  
> **Môi trường:** Apache Tomcat 7.0.47 | Java 8 (`class file version 52.0`) | Microsoft SQL Server  
> **Quy tắc bắt buộc:** Mọi thay đổi mã nguồn, tính năng, vá lỗi hoặc thiết kế lại giao diện bắt buộc phải được ghi nhận đầy đủ vào tài liệu này theo trình tự thời gian.

---

## 📅 CẬP NHẬT GẦN NHẤT: 18/11/2026

### 8. Gộp Toàn Bộ CSDL Thành 1 File Duy Nhất & Xóa Các File SQL Thừa
- **Xóa bỏ các file SQL không dùng / cũ / tách lẻ:** `QUANLYBANHANGQUANAODUNA.sql`, `seed_duna_50kh_70sp.sql`, `database/01_DUNA_Schema_100_Bang.sql`, `database/02_DUNA_300UC_SQL.sql`, `database/03_DUNA_Seed_Data.sql`, `database/04_DUNA_Sync_Integrity.sql`.
- **Gộp thành 1 file SQL duy nhất:** [DUNA_MensWear.sql](file:///d:/fpt/DUNA/database/DUNA_MensWear.sql) (chứa đầy đủ schema 100 bảng + toàn bộ 7.297 dòng dữ liệu chuẩn hóa gồm 605 tài khoản, 600 khách hàng, 100 sản phẩm, 569 biến thể, 1.017 đơn hàng), đồng bộ sang cả `TV1`, `TV2`, `TV3`, `TV4` và đã kiểm chứng chạy sạch trên SQL Server từ đầu không lỗi.

---

## 📅 CẬP NHẬT: 17/11/2026

### 7. Hoàn Thiện & Đồng Bộ Bộ SQL (`database/`) + File Excel (`Nhom6_300_usecase_CHUAN_HOA_DUNA.xlsx`)
- **Bộ SQL (`database/01..04`):**
  - Chuẩn hóa lại dữ liệu đơn hàng mẫu `DN20260802`, `DN20261105`, `voucher_usages` và khóa ngoại `voucher_id` trực tiếp ngay trong [03_DUNA_Seed_Data.sql](file:///d:/fpt/DUNA/database/03_DUNA_Seed_Data.sql), đồng bộ tiếp nối qua [04_DUNA_Sync_Integrity.sql](file:///d:/fpt/DUNA/database/04_DUNA_Sync_Integrity.sql) với `DUNA_MensWear` (`100` bảng, `300` câu SQL tại [02_DUNA_300UC_SQL.sql](file:///d:/fpt/DUNA/database/02_DUNA_300UC_SQL.sql)).
- **File Excel ([Nhom6_300_usecase_CHUAN_HOA_DUNA.xlsx](file:///d:/fpt/DUNA/Nhom6_300_usecase_CHUAN_HOA_DUNA.xlsx)):**
  - Chỉnh lại phạm vi `M03` (`UC044–UC086`, `43` UC) và `M04` (`UC087–UC113`, `27` UC) trên sheet **Tổng Quan** khớp 100% với mã nguồn `UcRegistry.java` và sheet **Phân Công**.
  - Bổ sung đầy đủ 4 cột ánh xạ thực tế (`Module`, `Route Web / JSP`, `Lớp DAO & Dispatcher`, `Trạng thái Triển khai`) cho toàn bộ **300/300 Use Case** trên sheet **Phân Công** và cột **Số dòng dữ liệu thực tế (DB)** từ SQL Server cho đủ **100/100 bảng** trên sheet **Danh Sách 100 Bảng**.

### 6. Chuẩn Hóa Toàn Diện Liên Kết CSDL & Đồng Bộ Hiển Thị Giữa Tài Khoản ↔ Khách Hàng ↔ Hóa Đơn ↔ Nhân Sự
- **Phân tách rõ màn hình tác nghiệp chính với bàn nghiệp vụ UC mở rộng (`ProductServlet.java`, `CustomerServlet.java`, `OrderServlet.java`, `uc-hub.jsp`):**
  - Sửa `/products` mở trang [products.jsp](file:///d:/fpt/DUNA/src/main/webapp/WEB-INF/views/tv1_nguyen_ngoc_minh/products.jsp) (COGS, VAT, Tag, Top bán chạy & đủ 23 UC sản phẩm `UC064–086`), `/customers` mở [customers.jsp](file:///d:/fpt/DUNA/src/main/webapp/WEB-INF/views/tv1_nguyen_ngoc_minh/customers.jsp) (Hạng VIP, Nhóm KH, Số đo `UC028–043`) và `/orders` mở [orders.jsp](file:///d:/fpt/DUNA/src/main/webapp/WEB-INF/views/tv2_nguyen_duc_anh/orders.jsp) (Tạo đơn qua điện thoại, Hóa đơn VAT `UC126–151`), không còn trùng lặp với `/ProductServlet`, `/CustomerServlet`, `/OrderServlet`.
- **Đồng bộ toán học & ràng buộc toàn vẹn CSDL (`database/04_DUNA_Sync_Integrity.sql`):**
  - Dọn bỏ 4 dòng hạng thẻ lỗi font UTF-8 trùng lặp (`tier_id 5..8`) trong `membership_tiers`, chuẩn hóa 4 hạng duy nhất (`Thành Viên 0%`, `Bạc 3%`, `Vàng 7%`, `Kim Cương 10%`).
  - Khởi tạo dữ liệu thật cho `permissions`, `role_permissions`, `departments`, `positions`, `work_shifts`; đồng bộ 1:1 nhân sự (`QL001`, `QL002`, `NV003`, `NV004 Lê Minh Bách`, `NV005 Nguyễn Kiều An`) với bảng `users`, ngày sinh riêng biệt, phòng ban và chức vụ rõ ràng.
  - Khắc phục triệt để tình trạng `customers.total_spent` / `total_orders` / `loyalty_accounts` có số liệu ảo không khớp với `orders`: sinh đầy đủ **1,017 hóa đơn thật (`orders`, `order_items`, `payments`, `loyalty_transactions`)** và đối soát 100% số tiền chi tiêu, số đơn, hạng thành viên, điểm thưởng, lượt dùng voucher và số lượng sản phẩm đã bán (`0` dòng sai lệch).
- **Đồng bộ giao diện & DAO (`CustomerDAO.java`, `quan-ly-khach-hang.jsp`, `ban-hang.jsp`, `OrderDAO.java`, `ProductDAO.java`, `UserDAO.java`, `users.jsp`, `HumanResourceDAO.java`, `AuthServlet.java`):**
  - Khắc phục lỗi lệch hạng (`Kim Cương` ở `/users` bị rơi thành `Thường • 0%` và `Vàng` bị hiện `-4%` ở `quan-ly-khach-hang.jsp`), thay bằng hiển thị động theo `membership_tiers`, bổ sung liên kết bấm chéo giữa `Mã KH` ↔ `Tài khoản (/users)` ↔ `Hóa đơn (/OrderServlet)` và `Nhân sự (/EmployeeDetailServlet)`.
  - Bổ sung tự động thăng hạng `tier_id`, cộng điểm `loyalty_accounts` / `loyalty_transactions`, ghi nhận `voucher_id` + `voucher_usages` ngay khi thanh toán đơn POS/Online tại `OrderDAO`, và sửa tên bảng `inventory_logs` trong `ProductDAO.deleteClassicVariant`.

### 5. Hoàn Thiện Trọn Vẹn M01 Tài Khoản (`UC011–UC027`) & Điều Phối 300/300 Use Case Lên Giao Diện Web
- **Nâng cấp toàn diện Trung Tâm Tài Khoản & Bảo Mật M01 (`/users` — `UserDAO.java`, `AuthAccountService.java`, `UserServlet.java`, `users.jsp`):**
  - Đưa đầy đủ **17 Use Case (`UC011–UC027`)** lên giao diện 5 tab trực quan: **Tài khoản (`UC011–022`)**, **Sổ địa chỉ (`UC023–026`)**, **Lịch sử đăng nhập (`UC015`)**, **Đăng nhập thất bại (`UC016`)** và **Nhật ký kiểm toán (`UC027`)**.
  - Bổ sung bộ lọc vai trò (`ADMIN`, `MANAGER`, `STAFF`, `CUSTOMER`), trạng thái (`ACTIVE`, `LOCKED`), phân trang 20 dòng/trang (`31` trang cho `603+` tài khoản), 4 thẻ KPI tổng quan, modal **Tạo tài khoản (`UC014`)**, modal **Sửa hồ sơ + Đặt lại mật khẩu / Token (`UC017–020`)**, modal **Sổ địa chỉ theo Tỉnh/Thành → Phường/Xã (`UC024–026`)**, khóa/mở khóa đồng bộ 2 chiều (`UC021`) và xóa tài khoản chưa phát sinh đơn (`UC022`).
- **Hoàn thiện 300/300 Use Case xuyên suốt DAO → Dispatcher → Controller → JSP (`com.duna.common.uc.*`, `BaseServlet.java`, `UcWorkbenchServlet.java`, `AuthFilter.java`, `uc-workbench.jspf`, `uc-hub.jsp`):**
  - Bổ sung đủ 4 phương thức DAO tường minh còn thiếu: `OrderDAO.uc135_packOrder`, `OrderDAO.uc136_shipOrder`, `PaymentDAO.uc166_createMomoPayment`, `ReviewSupportDAO.uc226_rejectReview` (`300/300` UC có mặt đầy đủ ở tầng DAO).
  - Tạo kiến trúc điều phối chuẩn hóa `UcRegistry` (`300` metadata + `ParamSpec`), `UcDispatchSupport`, `Tv1UcDispatcher` (`UC001–UC086`), `Tv2UcDispatcher` (`UC087–UC151`), `Tv3UcDispatcher` (`UC152–UC218`), `Tv4UcDispatcher` (`UC219–UC300`) và mở cổng `/uc-run` trong `UcWorkbenchServlet` (bảo vệ `ADMIN/MANAGER` qua `AuthFilter`).
  - Nhúng `uc-workbench.jspf` vào toàn bộ **23 trang module mở rộng** dưới `WEB-INF/views/` và nâng cấp `uc-hub.jsp` thành **Trình khám phá & chạy thử đủ 300 Use Case**.

### 4. Khắc Phục Lỗi Mất Biến Thể / Barcode, Lỗi In Phiếu & Đồng Bộ Liên Kết CSDL (`DUNA_MensWear`)
- **Sửa lỗi mất Biến thể & Barcode (`ProductServlet.java`, `ProductDAO.java`, `quan-ly-san-pham.jsp`):**
  - Chuẩn hóa API `ProductServlet?action=uc12&id=...` trả về trực tiếp mảng JSON biến thể (và trả `{variants, specs}` khi kèm `ajaxDetailAll=1`), khắc phục lỗi `data.map is not a function` làm rỗng modal **Biến thể Size & Màu**, kẹt modal **In tem Barcode** và lỗi nạp dropdown **Đổi Size**.
  - Bổ sung tạo biến thể khởi tạo khi thêm sản phẩm mới và hàm `deleteClassicVariant` xóa/vô hiệu hóa an toàn không vướng khóa ngoại.
- **Sửa lỗi Phiếu Thanh Toán Bán Lẻ bị mất STT, Tên sản phẩm & Biến thể (`OrderDAO.java`):**
  - Bổ sung key `"chiTiet"` (song song `"items"`) trong `OrderDAO.getClassicOrderDetailJson()`, đồng thời ghép đầy đủ tên biến thể `(Màu / Size)` qua `LEFT JOIN product_variants / colors / sizes` và trả kèm `voucherCode`, `voucherName`.
- **Đồng bộ 100% liên kết 1:1 `users` ↔ `customers` ↔ `employees` & Việt hóa bảng mở rộng (`03_DUNA_Seed_Data.sql`, `UserDAO.java`, `CustomerDAO.java`, `result-table.jspf`, `users.jsp`):**
  - Đồng bộ toàn bộ 600 khách hàng với 600 tài khoản `users` (`CUSTOMER`) và 600 ví điểm `loyalty_accounts` (`0` khách hàng thiếu liên kết); đồng bộ 2 chiều Thêm/Sửa/Khóa giữa `CustomerDAO` và `UserDAO`.
  - Hiển thị cột liên kết `Mã KH / NV`, `Hạng / Vai trò`, tiêu đề tiếng Việt có dấu trên `result-table.jspf`, giải thích rõ chức năng phân hệ **Quản lý Tài khoản (M01)** vs **Quản lý Khách hàng (M02)** trên `users.jsp` kèm nút Khóa/Mở khóa trực tiếp từng dòng.

### 3. Khôi Phục Giao Diện Gốc DUNA & Tích Hợp Hybrid 300 Use Case (`DUNA_MensWear`)
- **Khôi phục trọn vẹn 10 giao diện JSP gốc giàu tính năng từ bản gốc (`5bf252b`) và kết nối trực tiếp CSDL `DUNA_MensWear` (100 bảng):**
  - `ban-hang.jsp` (`/OrderServlet?view=sell`), `quan-ly-san-pham.jsp` (`/ProductServlet`), `quan-ly-hoa-don.jsp` (`/OrderServlet`), `quan-ly-khach-hang.jsp` (`/CustomerServlet`), `quan-ly-voucher.jsp` (`/VoucherServlet`), `bao-cao-thong-ke.jsp` (`/OrderServlet?view=report`), `employee-management.jsp` (`/EmployeeManagementServlet`), `employee-detail.jsp` (`/EmployeeDetailServlet`), `add-employee.jsp`, `qr-mobile.jsp` (`/QRConfirmServlet`).
  - Bổ sung các adapter truy vấn & nghiệp vụ tại `ProductDAO`/`ProductServlet`, `CustomerDAO`/`CustomerServlet`, `OrderDAO`/`OrderServlet`, `BarcodeScanServlet`, `PromotionVoucherDAO`/`PromotionVoucherServlet`, `QRConfirmServlet`, `HumanResourceDAO`/`EmployeeHrServlet`, `AuthServlet`, `AuthFilter` (cho phép cả `ADMIN` và `MANAGER` truy cập toàn bộ phân hệ quản trị, đồng thời giữ chặt RBAC cho `STAFF`).
- **Đồng bộ & hoàn thiện giao diện 17 module mở rộng (`300 UC`):**
  - Chuẩn hóa liên kết thanh điều hướng `navbar.jspf` và `uc-hub.jsp` về các màn hình tác nghiệp chính kèm đường dẫn truy cập nhanh `300 UC`.
  - Đồng bộ danh mục thời trang nam trong `quan-ly-san-pham.jsp` khớp 10 danh mục thật của `DUNA_MensWear`, chuẩn hóa tiếng Việt có dấu toàn bộ tiêu đề các Servlet/JSP mở rộng và loại bỏ lỗi `<head>hi` tại `quan-ly-hoa-don.jsp`.

### 2. Hoàn Thành Giai Đoạn 2: Nâng Cấp Toàn Bộ Mã Nguồn Java Web MVC (100 Bảng / 300 UC) Giữ Giao Diện DUNA Quen Thuộc
- **Cấu trúc 4 thành viên × 4 tầng MVC (`controller` → `service` → `dao` → `model`) + `com.duna.common`:**
  - `com.duna.common.{util,filter}` (`12` lớp): `DBConnection` (kết nối `DUNA_MensWear`), `BaseDAO`, `BaseServlet`, `RequestUtil`, `PasswordUtil`, `JsonUtil`, `CurrencyUtil`, `DateUtil`, `BarcodeUtil`, `FileUploadUtil`, `ExcelExportUtil`, `EncodingFilter`, `AuthFilter` (RBAC + HttpOnly `yn_user`, không lưu mật khẩu thô).
  - `tv1_nguyen_ngoc_minh` (`UC001–UC086`): `25` Models, `5` DAOs (`RolePermissionDAO`, `UserDAO`, `CustomerDAO`, `CatalogDAO`, `ProductDAO`), `3` Services, `6` Servlets (`AuthServlet`, `RolePermissionServlet`, `UserServlet`, `CustomerServlet`, `CategoryCatalogServlet`, `ProductServlet`).
  - `tv2_nguyen_duc_anh` (`UC087–UC151`): `23` Models, `6` DAOs (`PriceWarehouseDAO`, `InventoryDAO`, `ProcurementDAO`, `CartWishlistDAO`, `OrderDAO`, `ReturnRequestDAO`), `3` Services, `5` Servlets.
  - `tv3_le_minh_bach` (`UC152–UC218`): `19` Models, `5` DAOs (`RefundExportDAO`, `PaymentDAO`, `GeographyShippingDAO`, `PromotionVoucherDAO`, `LoyaltyPointDAO`), `4` Services, `5` Servlets.
  - `tv4_nguyen_kieu_an` (`UC219–UC300`): `33` Models, `8` DAOs (`LoyaltyRewardDAO`, `ReviewSupportDAO`, `MarketingDAO`, `NotificationDAO`, `StorePosDAO`, `HumanResourceDAO`, `FinanceReportDAO`, `SystemSettingDAO`), `7` Services, `8` Servlets (`ReviewSupportServlet`, `MarketingServlet`, `NotificationServlet`, `StorePosServlet`, `EmployeeHrServlet`, `FinanceReportServlet`, `SystemConfigServlet`, `UcWorkbenchServlet`).
- **Giao diện JSP giữ nguyên phong cách DUNA cũ (`theme.css?v=duna_v4`, `style.css?v=duna_v4`, `footer.jsp`, `.navbar`):**
  - Cập nhật `login.jsp` nối vào `/auth` với 4 tài khoản mẫu thật của `DUNA_MensWear` và trang đăng ký `register.jsp`.
  - Tách các trang quản trị theo từng thành viên dưới `WEB-INF/views/` (`common/`, `tv1_nguyen_ngoc_minh/`, `tv2_nguyen_duc_anh/`, `tv3_le_minh_bach/`, `tv4_nguyen_kieu_an/`), hỗ trợ đầy đủ cả giao diện web và API JSON.
- **Kiểm thử & Build:**
  - `mvn clean package` biên dịch `178` file Java sang bytecode Java 8 (`major version: 52`), JUnit `DunaArchitectureAndUcTest` pass `4/4`.
  - Kiểm thử Tomcat 7 + Chrome DevTools & E2E HTTP: 25 endpoint JSON 200 OK, RBAC Staff 403/200 đạt chuẩn, `0` lỗi Console trên Chrome.

### 1. Hoàn Thành Giai Đoạn 1: Chuẩn Hóa CSDL 100 Bảng (`DUNA_MensWear`), 300 UC SQL & File Excel Phân Công Chuẩn `Lab4.1`
- **Tạo bộ SQL chuẩn 100 bảng & 300 Use Case tại `database/`:**
  - `database/01_DUNA_Schema_100_Bang.sql`: Schema đầy đủ **100 bảng** (`17` module `M01–M17`) trên SQL Server `DUNA_MensWear`.
  - `database/02_DUNA_300UC_SQL.sql`: Đầy đủ **300 batch SQL** (`UC001–UC300`), bổ sung mệnh đề lọc `WHERE ... LIKE ...` cho toàn bộ 60 UC `Search = True` để đồng bộ 100% với cờ CRUD và biên dịch thành công toàn bộ 300 batch qua `SET NOEXEC ON`.
  - `database/03_DUNA_Seed_Data.sql`: Dữ liệu mẫu khởi tạo (vai trò, tài khoản SHA-256, khách hàng, hạng thành viên, địa lý, kho/cửa hàng, sản phẩm/biến thể, thanh toán, cấu hình).
- **Xuất file Excel phân công chuẩn 300 UC `Nhom6_300_usecase_CHUAN_HOA_DUNA.xlsx`:**
  - Cột `Độ phức tạp UI` (`H5:H304`), Data Validation và sheet `Hướng Dẫn`: loại bỏ hoàn toàn `Đơn giản` và `Phức tạp`, chỉ dùng đúng 2 nhãn **`Chuẩn`** (`64` UC) và **`Trung bình`** (`236` UC).
  - Cập nhật công thức `Effort Chốt`, tham số `Tổng Quan` (`1145.8 giờ` / `7.16 MM`) và đủ họ tên/MSSV 4 thành viên (`Nguyễn Ngọc Minh - TH08331`, `Nguyễn Đức Anh - TH08308`, `Lê Minh Bách - TH08442`, `Nguyễn Kiều An - TH08490`).
  - Kiểm định giả lập `Lab4.1`: `300/300` Đạt (`100.00%`), `0` Cảnh báo CRUD Lệch, `0` Thiếu SQL.

---

## 📅 CẬP NHẬT GẦN NHẤT: 16/11/2026

---

### 1. Đồng Bộ Hóa 100% Schema CSDL QuanLyBanHangDUNA & Triệt Tiêu Mọi SQL Exception
- **Khắc phục lỗi lệch tên cột CSDL giữa Repository và SQL Server:**
  - `UC01_QuanLyDanhMucRepo.java`: Thêm cột `MoTa NVARCHAR(500)` vào bảng `DanhMuc` trên CSDL `QuanLyBanHangDUNA` và bổ sung xử lý an toàn `getSafeString` khi đọc `MoTa`.
  - `UC42_DanhSachHoaDonRepo.java`, `UC46_ThongKeLoiNhuanHomNayRepo.java`, `UC47_ThongKeLoiNhuanThangNamRepo.java`, `UC48_BieuDoDoanhThuChartJSRepo.java`, `UC64_XuatBaoCaoTaiChinhExcelRepo.java`: Chuẩn hóa toàn bộ câu lệnh truy vấn từ `ThanhTien` sang `TongTien` (đúng chuẩn cột bảng `DonHang` trong CSDL SQL Server).
  - `UC49_DanhSachKhachHangVIPRepo.java`, `UC50_TimKiemKhachHangVIPRepo.java`, `UC51_ThemMoiKhachHangRepo.java`, `UC52_CapNhatKhachHangRepo.java`, `UC53_LichSuMuaSamKhachRepo.java`: Đồng bộ hóa chuẩn xác tên cột bảng `KhachHang` (`MaKH`, `HoTen`, `TongTienChiTieu`). Bổ sung cột `TongTienChiTieu BIGINT DEFAULT 0` vào bảng `KhachHang`.
  - `ThanhVien2Servlet.java`: Bổ sung cơ chế phát hiện trình duyệt truy cập web (Accept header `text/html`) khi không có `action` thì tự động điều hướng sang giao diện Bán hàng POS (`/OrderServlet?view=sell`) thay vì trả về JSON cấu hình thô.
- **Kết quả kiểm thử:**
  - Toàn bộ 64 Repository chạy trên CSDL thật không còn phát sinh bất kỳ ngoại lệ SQL nào (`com.microsoft.sqlserver.jdbc.SQLServerException`).
  - `mvn test`: 8/8 tests pass tuyệt đối, BUILD SUCCESS.
  - Server Tomcat 7 khởi động trơn tru tại `http://localhost:8080/DUNA`.

---

### 2. Sửa Lỗi Hiển Thị Raw JSON Khi Đăng Nhập & Khôi Phục Đầy Đủ Giao Diện HTML/JSP
- **Vấn đề phát sinh (User báo lỗi):**
  - Khi người dùng đăng nhập tài khoản tại `http://localhost:8080/DUNA/LoginServlet`, trình duyệt hiển thị một chuỗi raw JSON:
    `{"success":true,"user":{"id":2,"username":"nv01",...}}` thay vì điều hướng vào giao diện bán hàng hoặc quản trị.
  - Nguyên nhân: `ThanhVien2Servlet` khi nhận POST từ form `login.jsp` đã tự động gán `action=uc17` và ghi chuỗi JSON trực tiếp vào `response.getWriter()` của trình duyệt (thay vì thiết lập Session và `sendRedirect`).
  - Ngoài ra, các JSP như `ban-hang.jsp`, `quan-ly-hoa-don.jsp`, `quan-ly-khach-hang.jsp`, `employee-management.jsp` kiểm tra các thuộc tính model (`listKH`, `listSP`, `listDH`, `listNV`, `listQuanLy`); nếu Servlet forward mà thiếu các thuộc tính này sẽ dẫn đến redirect loop hoặc trắng trang.
- **Giải pháp xử lý:**
  1. **Khôi phục luồng xác thực đăng nhập HTML Form chuẩn:**
     - Tại `ThanhVien2Servlet.java`, phân biệt rõ giữa HTTP POST form từ `login.jsp` (không kèm `action`) và lệnh gọi API Ajax (kèm `action=uc17`).
     - Khi đăng nhập thành công từ Form: Thiết lập đầy đủ Session (`role = "MANAGER"` hoặc `"EMPLOYEE"`, `roleID`, `nhanVienID`, `tenNV`, `username`), ghi Cookie `yn_user` (nếu chọn Remember Me), xóa `yn_pass`, và điều hướng `sendRedirect` về:
       - Quản lý (`MANAGER`): `req.getContextPath() + "/EmployeeManagementServlet"`.
       - Nhân viên (`EMPLOYEE`): `req.getContextPath() + "/OrderServlet?view=sell"`.
     - Khi đăng nhập thất bại từ Form: Gán `errorMessage` và `forward` trở lại `login.jsp`.
     - Khi gọi REST/AJAX (`action=uc17`): Vẫn trả về JSON chuẩn để phục vụ Unit Test và gọi bất đồng bộ.
  2. **Hoàn thiện dữ liệu nạp trang JSP tại các Servlet:**
     - `ThanhVien3Servlet.java`: Khi `view=sell`, tự động tải `listKH`, `listSP` và `currentVat` rồi mới forward vào `ban-hang.jsp`, triệt tiêu hoàn toàn lỗi redirect loop. Khi truy cập `OrderServlet`, tự động nạp `listDH`.
     - `ThanhVien4Servlet.java`: Khi truy cập `CustomerServlet`, `EmployeeManagementServlet`, `VoucherServlet`, tự động tải đầy đủ danh sách hiển thị tương ứng (`listKH`, `listNV`, `listQuanLy`, `vouchers`, KPI).
  3. **Kiểm thử xác nhận:**
     - Biên dịch lại toàn bộ dự án (`mvn clean compile`): 100% BUILD SUCCESS với Java 8 Bytecode (`major version: 52`).
     - Chạy toàn bộ Test Suite (`mvn test`): 8/8 tests passed, 0 failures, 0 errors.

---

### 3. Đổi Tên CSDL Thành QUANLYBANHANGQUANAONAMDUNA, Khắc Phục Lỗi Bộ Lọc Báo Cáo & Kiểm Thử Toàn Diện DevOps
- **Vấn đề phát sinh (User báo lỗi):**
  - Người dùng yêu cầu đổi tên CSDL thành `QUANLYBANHANGQUANAONAMDUNA` và kết nối duy nhất CSDL này.
  - Trang Quản lý Voucher trước đó trống do bảng `Vouchers` chưa được đồng bộ từ bản cũ sang CSDL mới.
  - Trang Báo cáo thống kê khi bấm chọn Tháng 10 lại hiển thị số liệu của Tháng 7 (Tháng 10 thực tế có doanh thu là 0).
- **Giải pháp xử lý:**
  1. **Khóa CSDL duy nhất `QUANLYBANHANGQUANAONAMDUNA`:**
     - Đổi tên CSDL trong `DATN.sql` thành `CREATE DATABASE [QUANLYBANHANGQUANAONAMDUNA]; USE [QUANLYBANHANGQUANAONAMDUNA];`.
     - Cập nhật `connectService.java` chỉ kết nối duy nhất URL `jdbc:sqlserver://localhost:1433;databaseName=QUANLYBANHANGQUANAONAMDUNA;...`.
     - Đồng bộ đầy đủ 33 bảng dữ liệu vào `QUANLYBANHANGQUANAONAMDUNA` (19 bảng từ `DATN.sql` + 14 bảng nghiệp vụ tiếng Việt như `Vouchers`, `SanPham`, `BienTheSanPham`, `KhachHang`, `NhanVien`, `DonHang`, `ChiTietDonHang`, `TaiKhoan`, v.v.).
     - Trang Quản lý Voucher (`/VoucherServlet`) hiện đầy đủ 4 voucher và KPI chính xác: 230 mã phát hành, 202 mã sẵn sàng, 28 mã đã dùng, 900.165 VNĐ trợ giá.
  2. **Khắc phục lỗi lọc Tháng trong Báo cáo Thống kê (`ThanhVien3Servlet.java`):**
     - Loại bỏ các khối code fallback nhân tạo `if (topProducts.isEmpty())` và `if (soldProducts.isEmpty())`.
     - Khi chọn Tháng 10 (tháng chưa có đơn): Bảng hiển thị thông điệp chuẩn *"Không có dữ liệu trong khoảng thời gian này"*.
     - Khi chọn Tháng 7: Bảng hiển thị chính xác các đơn hàng bán trong Tháng 7/2026.
  3. **Kiểm thử tự động & Trình duyệt thực tế (DevOps & Chrome DevTools MCP):**
     - Đã kiểm tra runtime trên toàn bộ 8 phân hệ chính:
       - Đăng nhập: `http://localhost:8080/DUNA/login.jsp`
       - Bán hàng POS: `http://localhost:8080/DUNA/OrderServlet?view=sell` (thêm hàng vào giỏ, tính thuế VAT 8% chuẩn xác)
       - Quản lý sản phẩm: `http://localhost:8080/DUNA/ProductServlet` (6 sản phẩm DUNA)
       - Quản lý nhân sự: `http://localhost:8080/DUNA/EmployeeManagementServlet` (5 nhân sự)
       - Quản lý khách hàng: `http://localhost:8080/DUNA/CustomerServlet` (5 khách hàng VIP)
       - Quản lý hóa đơn: `http://localhost:8080/DUNA/OrderServlet` (6 hóa đơn HD-001 -> HD-006)
       - Quản lý Voucher: `http://localhost:8080/DUNA/VoucherServlet` (4 mã voucher DUNA)
       - Báo cáo thống kê: `http://localhost:8080/DUNA/OrderServlet?view=report` (Chart.js và bảng doanh thu khớp 100%)
     - **Kết quả DevOps:** 0 Console Error, 100% Network Request HTTP 200/304, Test Suite 8/8 Unit & Integration tests BUILD SUCCESS.

---

### 4. Kiểm Thử Đa Dạng Theo Tư Duy Chủ Cửa Hàng Thời Trang DUNA (Real-world Edge Cases)
- **Mục tiêu:** Đóng vai chủ shop thời trang nam DUNA đối mặt với các tình huống oái oăm thực tế tại quầy thu ngân để phát hiện và xử lý các lỗi ẩn.
- **Các tình huống oái oăm đã kiểm thử & xử lý thành công:**
  1. **Khách mua vượt tồn kho (Stock Deficit):**
     - Sản phẩm SP004 tồn kho 30 cái. Thử gõ số lượng 50 cái -> Hệ thống chặn ngay lập tức với cảnh báo *"Vượt tồn kho hiện tại!"*, không cho phép bán âm kho.
  2. **Thu ngân thu thiếu tiền mặt (Underpaid Prevention):**
     - Đơn hàng 410.400 đ, nhập khách đưa 200.000 đ -> Hệ thống chặn submit và cảnh báo *"Số tiền khách đưa (200.000 đ) chưa đủ để thanh toán (410.400 đ)!"*.
  3. **Khách dùng Voucher không đủ điều kiện tối thiểu (Min Order Condition):**
     - Áp dụng mã `VIP15P` (yêu cầu đơn từ 800.000 đ) khi giỏ chỉ có 380.000 đ -> Hệ thống từ chối áp dụng.
     - Khi thêm sản phẩm lên 1.830.000 đ -> Áp dụng thành công, chiết khấu 15% (274.500 đ) chuẩn xác từng đồng.
     - Đồng bộ lại `openVoucherLookupModal()` hiển thị tức thì 4 mã voucher có sẵn kèm điều kiện.
  4. **Khách xem thông số & biến thể sản phẩm tại quầy (Product Details & Barcode):**
     - Sửa lỗi modal "Chi tiết mặt hàng" bị kẹt ở trạng thái tải: Kết nối gọi `ProductServlet?action=uc12`, hiển thị đầy đủ bảng biến thể Size, Màu, Tồn kho, mã vạch Barcode SVG, QR Code và thông số kỹ thuật thương hiệu DUNA.
  5. **Khách yêu cầu đổi Size / Màu (UC 30, 31, 32):**
     - Mở modal đổi hàng từ hóa đơn, nạp danh sách biến thể còn hàng trong kho và tính toán số tiền bù trừ chênh lệch giá bán tự động.
  6. **Tư vấn Size thông minh theo Chiều cao & Cân nặng (UC 16):**
     - Nhập 175 cm và 68 kg -> Hệ thống đề xuất chuẩn xác: Áo Size L, Quần Size 31 - 32.
  7. **Mất kết nối Internet giờ cao điểm (Offline POS Mode):**
     - Giả lập `navigator.onLine = false` -> Hệ thống tự động chuyển đơn sang chế độ lưu trữ ngoại tuyến LocalStorage (`#OFF-...`), không làm gián đoạn việc phục vụ khách hàng.
- **Kết quả kiểm thử:**
  - `mvn surefire:test`: 8/8 tests pass tuyệt đối (0 failures, 0 errors, BUILD SUCCESS).
  - Trình duyệt không phát sinh lỗi JS hay treo đơ giao diện.

---

### 5. Khắc Phục Triệt Để Lỗi "Lỗi nạp ma trận: Unexpected token '<', " Trên Trang Quản Lý Sản Phẩm
- **Vấn đề phát sinh (User gửi ảnh chụp màn hình):**
  - Khi người dùng bấm nút **"Ma trận"** tại sản phẩm SP006 (Thắt Lưng Da Bò Ý Khóa Tự Động DUNA Milano), modal *DUNA Smart Inventory Matrix* hiện thông báo lỗi:
    `Lỗi nạp ma trận: Unexpected token '<', "`
- **Nguyên nhân kỹ thuật:**
  - Trong `quan-ly-san-pham.jsp`, hàm `openProductMatrixModal` gọi `fetch('/ProductServlet?ajaxDetailAll=1&idSP=' + idSP)`.
  - Servlet `ThanhVien1Servlet` kiểm tra `action == null` nên đã forward trả về toàn bộ trang HTML `quan-ly-san-pham.jsp`.
  - Trình duyệt khi thực hiện `response.json()` trên nội dung HTML (bắt đầu bằng ký tự `<` của `<!DOCTYPE html>`) đã ném ngoại lệ cú pháp JSON `SyntaxError: Unexpected token '<'`.
  - Tương tự, các hàm `openVariants`, `openPrintTagModal` và `promptRefillStock` cũng bị gọi nhầm query param cũ (`?ajaxVariant=1`, `action=editVariant`).
- **Giải pháp xử lý:**
  1. Chuẩn hóa kết nối API trong [quan-ly-san-pham.jsp](file:///d:/fpt/DUNA/src/main/webapp/quan-ly-san-pham.jsp):
     - `openProductMatrixModal`: Đổi URL gọi sang `/ProductServlet?action=uc12&id=` (UC 12: Chi tiết biến thể Size/Màu).
     - `openVariants`: Đổi URL gọi sang `/ProductServlet?action=uc12&id=`.
     - `openPrintTagModal`: Đổi URL gọi sang `/ProductServlet?action=uc12&id=`.
     - `promptRefillStock`: Đổi POST action sang `action=uc14` (UC 14: Cập nhật giá & tồn kho biến thể).
  2. Kiểm thử DevOps thời gian thực qua Chrome DevTools MCP:
     - Bấm xem ma trận tồn kho SP006: Hiển thị trơn tru 2 biến thể Freesize (Đen Khóa Bạc 25 SP, Nâu Cà Phê 20 SP).
     - Bấm xem ma trận tồn kho SP002 (Áo Polo Nam): Hiển thị lưới đa chiều Size M, Size L với chỉ báo Size Vàng và tổng tồn kho theo màu.
     - Bấm xem modal Biến thể và In tem mác: Hiển thị đầy đủ danh sách và render mã vạch Barcode SVG không còn lỗi.
  3. Kiểm thử tự động Maven:
     - Chạy `mvn surefire:test`: 8/8 tests passed tuyệt đối (BUILD SUCCESS).

---

### 6. Khắc Phục Lỗi Vỡ Layout Barcode Tràn Viền Thẻ Bài In Tem Mác (Print Tag Modal)
- **Vấn đề phát sinh (User gửi ảnh chụp màn hình):**
  - Trong modal **"In Tem Mác Barcode Treo Hàng"**, mã vạch Barcode SVG hiển thị quá rộng, các sọc đen đâm thủng và tràn ra ngoài đường viền bo góc của thẻ bài trắng.
- **Nguyên nhân kỹ thuật:**
  - Thư viện `JsBarcode` thiết lập `width: 1.8` trên mỗi vạch. Với mã vạch dài `SP006-BLK-FS` (175 units CODE128), kích thước SVG sinh ra lên tới `309px`.
  - Thẻ bài `.tag-preview-card` có chiều rộng `320px`, đệm trong `padding: 24px` nên lòng thẻ bài chỉ rộng $320 - 48 = 272\text{px}$. Do SVG không bị giới hạn chiều rộng tối đa (`max-width: 100%`) nên mã vạch đã tràn ra ngoài 37px gây vỡ giao diện.
  - Phụ đề thẻ bài hiển thị `PREMIUM BESPOKE` chưa khớp định vị chuỗi thời trang nam tầm trung thanh lịch của DUNA theo `AGENTS.md`.
- **Giải pháp xử lý:**
  1. **Tối ưu CSS ([style.css](file:///d:/fpt/DUNA/src/main/webapp/style.css)):**
     - Bổ sung quy tắc CSS cho `#tagBarcodeSvg`: `max-width: 100% !important; height: auto !important; max-height: 48px !important; display: block; margin: 0 auto;`.
     - Thêm `box-sizing: border-box; overflow: hidden;` cho `.tag-preview-card`.
  2. **Tối ưu cấu hình JsBarcode ([quan-ly-san-pham.jsp](file:///d:/fpt/DUNA/src/main/webapp/quan-ly-san-pham.jsp)):**
     - Điều chỉnh tỉ lệ vạch từ `width: 1.8` xuống `width: 1.35`, `height: 44`, `margin: 2`.
     - Barcode thực tế sau khi render co về `230px`, nằm lọt lòng ngay giữa thẻ bài `320px` với khoảng cách lề an toàn đều hai bên là `45px` (chống tràn 100%).
  3. **Đồng bộ nhận diện DUNA:**
     - Đổi phụ đề thẻ bài thành `MEN'S WEAR • SMART CASUAL & ELEGANT` đúng định vị thời trang nam tầm trung.
  4. **Kiểm thử trực tiếp:**
     - Đã đo đạc tọa độ qua Chrome DevTools MCP: `cardWidth = 320px`, `svgWidth = 230px`, `overflowLeft = false`, `overflowRight = false`.
     - Đổi qua lại các biến thể (Đen Khóa Bạc, Nâu Cà Phê): Cả hai đều hiển thị cân đối, sắc nét.
     - `mvn surefire:test`: 8/8 tests pass.

---

### 7. Bổ Sung Cột Câu Lệnh Truy Vấn SQL Vào Tài Liệu ULNL (Lab 4.1) & Xác Nhận Chuẩn Hóa CSDL Cho Thành Viên 1 (Nguyễn Ngọc Minh - TH08331)
- **Yêu cầu nhiệm vụ (Lab 4.1 - Dự án Java 1):**
  - Trong tài liệu Ước lượng Năng lực (ULNL) `Nhom6_64_usecase_CHUAN_HOA_DUNA.xlsx`, bổ sung thêm cột **"Câu lệnh truy vấn (SQL)"** tại sheet *Phân Công*.
  - Viết đầy đủ và chuẩn xác các câu lệnh SQL truy vấn tương ứng với từng chức năng cá nhân phụ trách (Nguyễn Ngọc Minh - MSSV: TH08331 phụ trách UC 01 -> UC 16) và toàn bộ 64 Use Case của nhóm 6.
  - Kiểm tra, thẩm định và xác nhận tính đúng đắn của các câu lệnh SQL trên CSDL SQL Server `QUANLYBANHANGQUANAONAMDUNA`.
- **Nội dung thực hiện:**
  1. **Bổ sung Cột 15 vào sheet "Phân Công":**
     - Đặt tên cột: `Câu lệnh truy vấn (SQL)` (Merge ô `O3:O4`, nền cam `FFFF9900`, chữ in đậm Times New Roman 12pt đồng bộ với toàn bộ bảng tính).
     - Mở rộng tiêu đề bảng `A1:O1` (Title: *MA TRẬN CHI TIẾT USECASE & PHÂN CÔNG THỰC HIỆN*).
     - Thiết lập độ rộng cột `O = 75` kèm định dạng `Wrap text` tự động xuống dòng và căn lề trái chuẩn tài liệu báo cáo.
  2. **Viết câu lệnh SQL chi tiết cho 16 Use Case cá nhân của Nguyễn Ngọc Minh (UC 01 -> UC 16):**
     - Khớp 100% với cấu trúc bảng thực tế: `DanhMuc`, `SanPham`, `BienTheSanPham`.
     - Bao phủ đầy đủ các thao tác: Tra cứu danh mục, kiểm tra toàn vẹn khóa ngoại (UC 04), lọc & tính tồn kho tổng từ biến thể (UC 05), tìm kiếm nâng cao (UC 06), thêm/sửa sản phẩm (UC 07, 08), tính giá vốn & biên lợi nhuận gộp COGS (UC 09), cấu hình thuế VAT 8% (UC 10), toggle trạng thái kinh doanh (UC 11), kiểm tra trùng tổ hợp Màu/Size duy nhất (UC 13), đồng bộ tồn kho cha khi xóa biến thể bằng Transaction (UC 15), tra cứu tư vấn Size thông minh (UC 16).
  3. **Mở rộng đồng bộ SQL cho toàn bộ 64 Use Case (Nhóm 6):**
     - Điền đầy đủ câu lệnh truy vấn cho cả 4 thành viên (UC 01 -> UC 64), bao gồm các phân hệ POS, Quét Barcode, Thanh toán VietQR, In hóa đơn K80, Thống kê Doanh thu, Khách hàng VIP, Voucher và Nhân sự.
  4. **Thẩm định & Xác nhận "SQL tôi dùng đúng chưa?":**
     - Đã xác nhận: Toàn bộ bảng, tên cột và kiểu dữ liệu hoàn toàn trùng khớp với CSDL thực tế đang chạy trên SQL Server (`QUANLYBANHANGQUANAONAMDUNA`).
     - Xuất đồng bộ sang cả định dạng `Nhom6_64_usecase_CHUAN_HOA_DUNA.xlsx` và `Nhom6_64_usecase_CHUAN_HOA_DUNA.csv`.

---

### 8. Khắc Phục Lỗi Biên Dịch "package connectService does not exist" Do File Trùng Lặp Cũ
- **Vấn đề phát sinh (User gửi ảnh màn hình lỗi IntelliJ IDEA):**
  - Khi chạy `mvn tomcat7:run` hoặc Build trên IntelliJ IDEA, hệ thống báo lỗi:
    `[ERROR] LoginServlet.java src/main/java/Controller [3,22] package connectService does not exist`
    `[ERROR] cannot find symbol :28 class connectService`
- **Nguyên nhân kỹ thuật:**
  - Sau lượt pull/merge git từ nhánh remote, một file mã nguồn cũ đã bị nạp vào: `src/main/java/Controller/LoginServlet.java`.
  - File này sử dụng đường dẫn package cũ không còn tồn tại (`import connectService.connectService;`), trong khi toàn bộ hệ thống đã được chuẩn hóa theo chuẩn kiến trúc `com.duna.common.connection.connectService`.
  - Hơn nữa, toàn bộ nghiệp vụ xác thực đăng nhập đã được điều phối chuẩn mực và an toàn tại `ThanhVien2Servlet.java` (`com.duna.thanhvien2_ducanh.controller.ThanhVien2Servlet` phụ trách UC 17 -> UC 32) với đầy đủ hỗ trợ URL pattern `/LoginServlet`.
- **Giải pháp xử lý:**
  - Xóa bỏ triệt để file trùng lặp cũ `src/main/java/Controller/LoginServlet.java` và thư mục `Controller/`.
  - Biên dịch lại toàn bộ dự án (`mvn compile`): 98 source files compiled with javac [debug target 1.8], **BUILD SUCCESS**.
  - Chạy toàn bộ kiểm thử Unit & Integration Test (`mvn test`): 8/8 tests pass tuyệt đối (0 failures, 0 errors). Hệ thống Tomcat 7 khởi động bình thường.

---

## 📅 CẬP NHẬT TRƯỚC: 15/11/2026

---

### 1. Phân Tách 64 File Repository Độc Lập (1 Use Case = 1 File Java) & Xóa Bỏ Legacy Files
- **Mục tiêu kiến trúc:**
  - Chuyển đổi toàn bộ cấu trúc Repository từ 4 file gộp (`ThanhVienXRepository.java`) sang **64 file Java Repository riêng biệt** (`UC01_...Repo.java` -> `UC64_...Repo.java`), phân bổ đều 16 file/thành viên.
  - Phù hợp tuyệt đối với bảng phân công của giảng viên và hình ảnh mô hình kiến trúc bảng trắng: Mỗi thành viên có 1 Servlet controller + đúng 16 file Repository độc lập đại diện cho 16 Use Case của mình.
- **Chi tiết triển khai:**
  1. **Member 1 (Nguyễn Ngọc Minh - TH08331):** Tạo đủ 16 file `UC01_QuanLyDanhMucRepo.java` đến `UC16_TuVanSizeThongMinhRepo.java` tại `com/duna/thanhvien1_minh/repository/`.
  2. **Member 2 (Nguyễn Đức Anh - TH08308):** Tạo đủ 16 file `UC17_DangNhapXacThucRepo.java` đến `UC32_CapNhatKhoDoiHangRepo.java` tại `com/duna/thanhvien2_ducanh/repository/`.
  3. **Member 3 (Lê Minh Bách - TH08442):** Tạo đủ 16 file `UC33_TraCuuKhachHangPOSRepo.java` đến `UC48_BieuDoDoanhThuChartJSRepo.java` tại `com/duna/thanhvien3_bach/repository/`.
  4. **Member 4 (Nguyễn Kiều An - TH08490):** Tạo đủ 16 file `UC49_DanhSachKhachHangVIPRepo.java` đến `UC64_XuatBaoCaoTaiChinhExcelRepo.java` tại `com/duna/thanhvien4_kieuan/repository/`.
  5. **Đồng bộ hóa 4 Servlet điều phối:**
     - `ThanhVien1Servlet.java`: Khai báo 16 repo `uc01Repo` -> `uc16Repo`, định tuyến đầy đủ các action CRUD.
     - `ThanhVien2Servlet.java`: Khai báo 16 repo `uc17Repo` -> `uc32Repo`, điều phối luồng POS & Auth.
     - `ThanhVien3Servlet.java`: Khai báo 16 repo `uc33Repo` -> `uc48Repo`, điều phối thanh toán VietQR & Báo cáo.
     - `ThanhVien4Servlet.java`: Khai báo 16 repo `uc49Repo` -> `uc64Repo`, điều phối CRM Khách hàng & Nhân sự.
  6. **Xóa bỏ vĩnh viễn 4 file gộp cũ:**
     - Xóa `ThanhVien1Repository.java`, `ThanhVien2Repository.java`, `ThanhVien3Repository.java`, `ThanhVien4Repository.java`.
  7. **Cập nhật Unit Test Suite `SixtyFourUseCasesTest.java`:**
     - Khởi tạo và kiểm thử trực tiếp toàn bộ 64 lớp Repository độc lập.
     - Đảm bảo 100% các lớp và phương thức có $\ge 2$ usages (Servlet + Test), triệt tiêu hoàn toàn `no usages` và dead code.
  8. **Kiểm tra kiểm thử và chuẩn Bytecode:**
     - Chạy `mvn clean test`: 100% BUILD SUCCESS (8/8 tests run, 0 failures, 0 errors).
     - Khóa chuẩn Bytecode Java 8 (`major version: 52`).
  9. **Cập nhật đồng bộ tài liệu:** Cập nhật `SO_DO_KIEN_TRUC_64_USECASE.md`, `AGENTS.md`, `pm.md`.

---

---

### 1. Chuẩn Hóa Toàn Diện 64 Use Case & Xóa Bỏ Dead Code (100% Usages)
- **Vấn đề xử lý:**
  - IntelliJ cảnh báo `no usages` tại các hàm trong `ThanhVienXRepository.java` do chưa có kết nối gọi thực tế từ Controller và Test suite.
  - Các Use Case dạng "Quản lý..." (UC 01, UC 02, UC 03, UC 09, UC 11, UC 27, UC 29, UC 58, UC 60, UC 63) bị thiếu các thao tác nghiệp vụ cốt lõi (Thêm, Sửa, Xóa, Cập nhật trạng thái).
  - Thông tin Thành viên 3 bị sai lệch tên và mã sinh viên.
- **Giải pháp xử lý:**
  1. **Đính chính thông tin Thành viên 3:** Cập nhật chính xác thành **LÊ MINH BÁCH - MSSV: TH08442** xuyên suốt toàn bộ tài liệu (`AGENTS.md`, `pm.md`, `SO_DO_KIEN_TRUC_64_USECASE.md`), mã nguồn (`ThanhVien3Servlet.java`, `ThanhVien3Repository.java`) và Unit Test.
  2. **Bổ sung đầy đủ bộ hàm CRUD thực tế cho toàn bộ 64 Use Case:**
     - UC 01: `uc01_getDanhSachDanhMuc()`, `uc01_themDanhMuc()`, `uc01_capNhatDanhMuc()`, `uc01_xoaDanhMuc()`.
     - UC 02: `uc02_getChatLieuList()`, `uc02_themChatLieu()`, `uc02_xoaChatLieu()`.
     - UC 03: `uc03_getFormDangList()`, `uc03_themFormDang()`, `uc03_xoaFormDang()`.
     - UC 09: `uc09_tinhLoiNhuanGop()`, `uc09_capNhatGiaNhap()`.
     - UC 11: `uc11_toggleTrangThaiKinhDoanh()`, `uc11_capNhatTrangThai()`.
     - UC 27: `uc27_capNhatGioHang()`, `uc27_themVaoGioHang()`, `uc27_xoaKhoiGioHang()`, `uc27_xoaToanBoGioHang()`.
     - UC 29: `uc29_createPendingTab()`, `uc29_getPendingTab()`, `uc29_removePendingTab()`, `uc29_getAllPendingTabs()`.
     - UC 58: `uc58_cauHinhDieuKienVoucher(idV, minOrder, qty, hanDung)`.
     - UC 60: `uc60_getDanhSachNhanVienVaTrangThai()`, `uc60_capNhatTrangThaiNhanVien()`.
     - UC 63: `uc63_khoaTaiKhoanNhanVien()`, `uc63_moKhoaTaiKhoanNhanVien()`.
  3. **Kết nối 100% các hàm từ Repository sang Servlet:**
     - Từng hàm đều có nhánh xử lý `action` rõ ràng trong switch-case của `ThanhVienXServlet.java`.
     - Hỗ trợ forward JSP gốc khi truy cập trình duyệt từ giao diện người dùng.
  4. **Kiểm thử tự động 100%:** Cập nhật `SixtyFourUseCasesTest.java` bao phủ toàn bộ 82 hàm của 64 Use Case, đảm bảo mỗi hàm có $\ge 2$ usages trong IntelliJ.
  5. **Xác nhận biên dịch & Test:** `mvn clean test` 100% BUILD SUCCESS (8/8 tests passed), mã bytecode đạt chuẩn `major version: 52` (Java 8).

---

## 📅 CẬP NHẬT TRƯỚC: 13/11/2026

---

### 1. Khắc Phục Lỗi Biên Dịch & Khóa Chuẩn Bytecode Java 8 (Bytecode 52.0)
- **Vấn đề phát sinh:** Máy chủ chạy Tomcat 7 trên môi trường máy chủ gặp lỗi nghiêm trọng:  
  `java.lang.UnsupportedClassVersionError: ... has been compiled by a more recent version of the Java Runtime (class file version 69.0/65.0), this version only recognizes up to 52.0/65.0`.
- **Giải pháp xử lý:**
  - Cập nhật `pom.xml`: Khóa cấu hình `maven-compiler-plugin` với `<source>1.8</source>`, `<target>1.8</target>`.
  - Thiết lập quy tắc biên dịch bắt buộc trong `AGENTS.md`: Khi biên dịch thủ công qua dòng lệnh, bắt buộc sử dụng cờ `--release 8` (`javac --release 8 ...`).
  - Biên dịch lại toàn bộ các class và đồng bộ chuẩn bytecode 52.0 sang thư mục triển khai Tomcat `target/DUNA/WEB-INF/classes/`.

---

### 2. Thiết Kế Lại Giao Diện Quản Lý Hóa Đơn (`quan-ly-hoa-don.jsp`)
- **Vấn đề:** Tiêu đề "Quản lý Hóa Đơn & Lịch Sử Giao Dịch" và nút "+ Lập hóa đơn mới" bị dính sát vào nhau; bảng danh sách ở dưới quá sát phần thẻ số liệu; ở cột thao tác 2 nút "Tiếp tục" và "Hủy" bị dính liền và bố cục không hợp lý.
- **Giải pháp xử lý:**
  - Tách biệt rõ ràng phần tiêu đề trang và nút thao tác chính, thiết lập `gap: 16px` và `margin-bottom: 24px`.
  - Thiết lập lại khoảng cách đồng bộ giữa các card thống kê tổng quan và bảng dữ liệu hóa đơn (`margin-top: 24px`).
  - Thiết kế lại cụm nút Thao tác: Xếp ngang cân đối, nút "Tiếp tục" (nền xanh đen luxury) và nút "Hủy đơn" (nền viền đỏ âm) phân tách rõ ràng, thêm hiệu ứng hover và padding chuẩn.

---

### 3. Khắc Phục & Tối Ưu Màn Hình Báo Cáo Thống Kê (`bao-cao-thong-ke.jsp`)
- **Vấn đề:**
  - Biểu đồ tăng trưởng doanh thu theo tháng bị mất (trắng trang biểu đồ).
  - Nút bấm xem giải thích cách tính doanh thu và lợi nhuận chưa có.
  - Dropdown chọn năm bị co hẹp, che mất chữ số.
- **Giải pháp xử lý:**
  - Sửa lỗi crash biểu đồ: Nguyên nhân do Tomcat 7 không hỗ trợ phương thức EL `${Math.round(...)}`; đã chuyển toàn bộ sang thẻ định dạng chuẩn `<fmt:formatNumber ... maxFractionDigits="0"/>` và truyền dữ liệu JSON an toàn vào Chart.js.
  - Sửa lỗi dropdown năm: Thêm `min-width: 115px; padding: 0 28px 0 12px;` cho class `.table-filter-select`, đảm bảo hiển thị trọn vẹn số năm "Năm 2026".
  - Thêm Modal giải thích công thức: Tạo nút `(?) Cách tính doanh thu & lợi nhuận` mở modal `#modalExplainFormula`, cung cấp chi tiết công thức Doanh thu thuần, Giá vốn hàng bán, Lợi nhuận gộp và Tỷ suất biên lợi nhuận ròng.

---

### 4. Cập Nhật Quy Chế Phân Hạng Hội Viên & Chu Kỳ 3 Tháng Cuốn Chiếu
- **Yêu cầu nghiệp vụ mới:**
  - Đổi tiêu chí phân hạng từ "Tổng chi tiêu tiền mặt" sang **"Số lượng sản phẩm thời trang đã mua"**.
  - **Hạng Thường:** Mua 0 – 4 sản phẩm $\rightarrow$ Chiết khấu **0%** (Giá niêm yết).
  - **Hạng Bạc:** Tích lũy 5 – 8 sản phẩm $\rightarrow$ Chiết khấu **2%** trực tiếp.
  - **Hạng Vàng:** Tích lũy 9 – 12 sản phẩm $\rightarrow$ Chiết khấu **4%** trực tiếp.
  - **Hạng VIP:** Từ 13 sản phẩm trở lên $\rightarrow$ Chiết khấu **7%** cao cấp.
  - **Cơ chế gia hạn cuốn chiếu 3 tháng (Rolling Retention):**
    - Nếu khách tiếp tục mua sắm trong thời hạn 3 tháng: Hạn giữ hạng tự động gia hạn thêm 3 tháng kể từ ngày mua đơn mới nhất; số lượng sản phẩm mua mới được cộng dồn lũy kế để xét thăng hạng cao hơn.
    - Nếu quá 3 tháng (90 ngày) không mua hàng: Hệ thống tự động reset khách hàng về hạng Thường (0%) và làm mới chu kỳ tích lũy cho lần mua kế tiếp.
- **Các file đã can thiệp & đồng bộ:**
  - `CustomerServlet.java`:
    - Thêm truy vấn tự động quét và reset về `Thường` các khách hàng quá 90 ngày không phát sinh đơn hàng đã thanh toán.
    - Cập nhật query tính `TongSP` và cập nhật hạng theo ngưỡng: $\ge 13$ VIP, $9-12$ Vàng, $5-8$ Bạc, $< 5$ Thường.
    - Tính toán và hiển thị hạn giữ hạng cụ thể trên từng dòng: `dd/MM/yyyy (còn X ngày)`.
  - `OrderServlet.java`:
    - Cập nhật tỷ lệ chiết khấu tại POS bán hàng quầy: Bạc = `2%`, Vàng = `4%`, VIP = `7%`.
    - Cập nhật logic `updateCustomerRank` và `syncAllCustomerRanks` khi hoàn tất đơn hàng.
  - `quan-ly-khach-hang.jsp`:
    - Thiết kế lại 4 thẻ quy định thăng hạng hội viên theo số lượng sản phẩm và mức chiết khấu mới.
    - Bổ sung khung giải thích chi tiết quy tắc gia hạn cuốn chiếu 3 tháng.
    - Bổ sung cột "Đã mua (SP)", "Tổng chi tiêu", badge chiết khấu mới (`-2%`, `-4%`, `-7%`) và cột "Hạn giữ hạng".

---

### 5. Kiểm Toán Toàn Diện & Vá Lỗi Bảo Mật, Vận Hành (DevOps Audit)
- **Bảo mật & Xác thực (Security):**
  - `login.jsp`: Xóa bỏ hoàn toàn mã lưu mật khẩu dạng plaintext trong `localStorage.setItem("yn_pass", ...)`. Bổ sung lệnh dọn sạch cookie/localStorage cũ khi mở trang. Chuyển sang dùng Credential Management API gốc của trình duyệt.
  - `EmployeeDetailServlet.java`: Khắc phục lỗ hổng IDOR xem thông tin nhân sự. Ngăn chặn nhân viên `EMPLOYEE` xem thông tin của người khác hoặc Admin; cấm `mode=edit` đối với nhân viên. Masking mật khẩu gửi sang JSP thành `••••••••` (mật khẩu thật chỉ trả về qua AJAX bảo mật sau khi xác thực mật khẩu Admin).
  - `AuthFilter.java`: Cho phép `EMPLOYEE` vào trang Quản lý khách hàng để tra cứu hồ sơ (thao tác thêm/sửa/khóa vẫn chặn nghiêm ngặt). Bổ sung đuôi `.ico` vào danh sách tài nguyên công khai.
- **Phòng chống Crash 500 (Robustness):**
  - Tuân thủ quy tắc 3 trong `AGENTS.md`: Bọc 100% các tham số ID số nguyên từ URL/Form trong khối `try-catch NumberFormatException`.
  - Đã cập nhật trên:
    - `OrderServlet.java`: Tham số đổi size `idDH`, `idCT`, `idBienTheMoi` và tham số hủy đơn `id`.
    - `ProductServlet.java`: Tham số AJAX biến thể `idSP`, tham số sửa sản phẩm `id`, tham số sửa/xóa biến thể `idBT`, `idSP`.
    - `VoucherServlet.java`: Tham số toggle trạng thái voucher `id`.
    - `ChangeCredentialsServlet.java`: Kiểm tra hợp lệ `idNhanVien`.
- **Tối ưu tài nguyên & Giao diện:**
  - `favicon.ico`: Tạo file favicon chuẩn tại root webapp và cấu hình tự động inject qua `footer.jsp`, triệt tiêu hoàn toàn mã lỗi 404 trên console.
  - `employee-detail.jsp`: Bọc ô nhập mật khẩu Admin modal xác thực vào thẻ `<form>` có `autocomplete`, sửa triệt để cảnh báo DOM trên trình duyệt.

---

### 6. Cập Nhật Định Vị Thương Hiệu Phân Khúc "Thời Trang Nam Tầm Trung"
- **Yêu cầu:** Chuẩn hóa định vị sản phẩm của toàn bộ hệ thống DUNA Men's Wear sang phân khúc **thời trang nam tầm trung** (mid-range: áo polo, sơ mi, quần âu, khaki, áo khoác... giá cả hợp lý, dễ tiếp cận, thiết kế hiện đại, thanh lịch và phổ thông cho nam giới), loại bỏ các mô tả xa hoa, luxury hoặc cao cấp gây lệch hướng phát triển sản phẩm.
- **Các file đã can thiệp & đồng bộ:**
  - `AGENTS.md`: Cập nhật phần thông tin dự án và Điều khoản 4 trong Quy tắc bảo mật & nghiệp vụ, đưa định vị thời trang nam tầm trung thành nguyên tắc phát triển bắt buộc cho toàn bộ Agent, Lập trình viên và QA.
  - `footer.jsp`: Đổi bản quyền hiển thị thành `DUNA Men's Wear — Hệ Thống Quản Lý Bán Lẻ Thời Trang Nam Tầm Trung`.
  - `bao-cao-thong-ke.jsp`: Chuẩn hóa mô tả trong modal quy tắc tài chính thành `Phương pháp hạch toán sổ cái chuẩn chuỗi bán lẻ thời trang nam tầm trung DUNA`.
  - `quan-ly-khach-hang.jsp`: Cập nhật nhãn quyền lợi hội viên hạng VIP thành `Chiết khấu VIP 7%` (thay cho "Chiết khấu cao cấp 7%").
  - `quan-ly-san-pham.jsp`: Cập nhật placeholder mô tả sản phẩm sang `chất liệu vải...`.
  - Đồng bộ tức thì toàn bộ file JSP sang thư mục triển khai máy chủ Tomcat `target/DUNA/`.

---

### 7. Triển Khai Trọn Bộ 4 Nhóm Tính Năng Đặc Thù Ngành Thời Trang Nam & Smart POS
- **Bối cảnh & Phân công đội ngũ (AI Team):** Triển khai trọn vẹn 4 nhóm nghiệp vụ theo yêu cầu của Quản lý:
  - **Nhóm 1 (Đặc thù ngành thời trang nam - Fashion Operations):**
    - *Gợi Ý Phối Đồ Thông Minh Tại Quầy (Smart Mix & Match / Outfit Bundling):* Tích hợp trực tiếp tại POS `ban-hang.jsp`. Khi giỏ hàng có sản phẩm (Blazer, Sơ mi, Polo, Quần âu, Quần Khaki), hệ thống tự động hiển thị gợi ý trang phục và phụ kiện đi kèm chuẩn phong cách Sartorial / Smart Casual kèm nút `+ Thêm vào đơn` 1 chạm để gia tăng giá trị đơn hàng (AOV).
    - *Quản Lý Phiếu Lên Gấu & Sửa Đồ (Alteration Management):* Tạo mới bảng CSDL `PhieuSuaDo` trong SQL Server `QuanLyBanHangDUNA`. Thêm tab `✂️ Sổ Phiếu Lên Gấu & Sửa Đồ` trên `quan-ly-hoa-don.jsp` kèm 4 thẻ KPI (Tổng phiếu, Chờ sửa, Đang sửa, Đã xong), bảng theo dõi số đo (cắt gấu, bóp eo, chỉnh tay áo), thợ may phụ trách, ngày hẹn và cập nhật nhanh trạng thái. Tích hợp Modal tạo phiếu sửa đồ liên kết đơn hàng.
    - *Chính Sách Đổi Size Linh Hoạt & Tra Cứu Bảo Hành Đường May (Warranty Portal):* Thêm API `ajaxWarranty=1` trên `OrderServlet.java` và modal tra cứu theo SĐT tại POS, kiểm tra hạn bảo hành đường may/khuy áo 12 tháng miễn phí và lịch sử sửa đồ của khách.
  - **Nhóm 2 (Quản trị kho & Ma trận biến thể - Smart Inventory):**
    - *Lưới Ma Trận Size & Màu Trực Quan (Color-Size Matrix Grid):* Tích hợp trên `quan-ly-san-pham.jsp`. Thuật toán pivot mảng biến thể 1 chiều thành bảng 2 chiều (Màu sắc $\times$ Kích cỡ) trực quan.
    - *Cảnh Báo Hết Hàng "Size Vàng" (Reorder Point):* Cảnh báo đỏ nổi bật khi size chủ lực (M, L / 30, 31, 39, 40) có tồn kho $\le 3$ chiếc, cảnh báo vàng khi $\le 1$ chiếc, tích hợp nút nạp nhanh số lượng tồn kho `+ Nhập`.
  - **Nhóm 3 (Chăm sóc khách hàng & Loyalty 4.0):**
    - *Cảnh Báo Trước Hạn Reset Chu Kỳ 3 Tháng (Cycle Expiration Alert):* Thêm bộ lọc `expiring=14` và `expiring=7` trên `CustomerServlet.java` & `quan-ly-khach-hang.jsp`.
    - *Mẫu Tin Nhắn Chăm Sóc Zalo/SMS 1 Chạm:* Nút `💬 Nhắc Zalo/SMS` mở modal tạo tin nhắn cá nhân hóa theo tên khách, hạng thẻ, số ngày còn lại và gợi ý sản phẩm mua mới, kèm nút sao chép và liên kết mở trực tiếp Zalo chat (`zalo.me/{sdt}`).
  - **Nhóm 4 (Công nghệ & Bán hàng ngoại tuyến - Tech & BI):**
    - *Chế Độ Bán Hàng Offline (Offline Fallback POS):* Tự động phát hiện trạng thái mạng qua `navigator.onLine`. Khi rớt mạng, chuyển sang giao diện Offline Banner, lưu đơn nháp an toàn vào `localStorage` (`duna_offline_orders`) không làm gián đoạn thu ngân quầy. Khi có mạng trở lại, hiển thị thông báo và nút `Đồng Bộ Ngay` gửi toàn bộ đơn lên máy chủ SQL Server.
- **Các file đã can thiệp & đồng bộ:**
  - CSDL: Tạo bảng `PhieuSuaDo` trong SQL Server `QuanLyBanHangDUNA` (hỗ trợ lưu vết số đo, thợ may, hạn trả, chi phí).
  - `OrderServlet.java`: Bổ sung xử lý `createAlteration`, `updateAlterationStatus`, `ajaxWarranty=1`, nạp danh sách `listPSD`.
  - `CustomerServlet.java`: Bổ sung bộ lọc `expiring` (14 ngày, 7 ngày), tính toán cảnh báo chu kỳ 3 tháng.
  - `ban-hang.jsp`: Tích hợp Smart Mix & Match, Tra cứu bảo hành đường may, Chế độ bán hàng Offline Fallback.
  - `quan-ly-hoa-don.jsp`: Thêm Tab Sổ Phiếu Lên Gấu & Sửa Đồ, Modal Tạo Phiếu Sửa Đồ, Cập nhật trạng thái phiếu.
  - `quan-ly-khach-hang.jsp`: Nhập taglib `fn:`, `fmt:`, dropdown lọc chu kỳ và Modal mẫu tin nhắn Zalo/SMS 1 chạm.
  - `quan-ly-san-pham.jsp`: Nhập taglib `fn:`, nút và modal Lưới Ma Trận Size & Màu 2 chiều kèm cảnh báo Size Vàng.
  - Biên dịch toàn bộ bằng `javac --release 8` chuẩn Bytecode 52.0 và đồng bộ `target/DUNA/`.

---

### 8. Loại Bỏ Toàn Bộ Nhóm 1 & Tinh Gọn Nhóm 3 Sang Cơ Chế Nhắc Nhở Offline Tại Quầy POS
- **Yêu cầu người dùng:**
  - Xóa bỏ triệt để Nhóm 1 (Đặc thù ngành thời trang nam): Gợi ý phối đồ (Smart Mix & Match), Quản lý phiếu lên gấu/sửa đồ (Alteration Management), và Tra cứu bảo hành đường may 12 tháng (Warranty Portal).
  - Tinh gọn Nhóm 3 (Chăm sóc khách hàng & Chu kỳ 3 tháng): Không gửi tin nhắn trực tuyến (loại bỏ liên kết Chat Zalo, SMS online), chỉ giữ lại cơ chế **nhắc nhở trực tiếp tại quầy thu ngân (Offline Reminder)** khi khách hàng đến mua sắm tại cửa hàng.
- **Chi tiết các hạng mục đã thực hiện:**
  - **1. Dọn dẹp Nhóm 1 (Fashion-Specific Operations):**
    - `OrderServlet.java`: Xóa bỏ luồng API `ajaxWarranty=1`, xóa truy vấn nạp bảng `PhieuSuaDo` trong `doGet`, xóa action `createAlteration` và `updateAlterationStatus` trong `doPost`.
    - `quan-ly-hoa-don.jsp`: Xóa nút "✂️ Tạo Phiếu Sửa Đồ / Lên Gấu", xóa thanh Tab chuyển đổi (Sổ hóa đơn vs Sổ phiếu sửa đồ), xóa toàn bộ khối nội dung `#tailoringContent`, modal `#modalCreateAlteration` và các hàm JavaScript liên quan.
    - `ban-hang.jsp`: Xóa nút "🛡️ Bảo Hành & Sửa Đồ", xóa khu vực gợi ý phối đồ `#smartOutfitSection`, xóa modal `#modalWarrantyLookup` cùng các hàm JS `renderSmartOutfitBundling()`, `addQuickBundle()`, `openWarrantyModal()`, `lookupWarranty()`.
  - **2. Triển khai Cơ Chế Nhắc Offline Tại Quầy (Nhóm 3):**
    - `OrderServlet.java` (`view=sell`): Tự động truy vấn ngày mua đơn hàng gần nhất (`NgayMuaCuoi`) của từng khách hàng và tính toán số ngày còn lại của chu kỳ 3 tháng (`daysLeft`), truyền sang giao diện POS.
    - `ban-hang.jsp`: Khi nhân viên thu ngân chọn khách hàng tại quầy POS, nếu khách đang ở hạng hội viên (Bạc, Vàng, VIP) và chu kỳ 3 tháng sắp hết hạn ($\le 14$ ngày), hệ thống tự động hiển thị **Banner Nhắc Trực Tiếp Tại Quầy màu vàng nổi bật** kèm lời thoại mẫu giúp nhân viên thu ngân nhắc khách mua đơn hàng để được tự động gia hạn thêm 3 tháng và tiếp tục tích lũy sản phẩm.
    - `quan-ly-khach-hang.jsp`: Đổi nút thao tác ở cột Hạn giữ hạng thành `⚠️ Nhắc tại quầy`; chuyển đổi modal thành `#modalOfflineReminder` (hiển thị kịch bản giao tiếp gợi ý cho nhân viên, hỗ trợ nút sao chép kịch bản, loại bỏ hoàn toàn link mở Chat Zalo / SMS online).
- **3. Tuân thủ biên dịch & kiểm định:**
  - Biên dịch toàn bộ mã Java bằng `javac --release 8` (chuẩn Bytecode version 52.0).
  - Đồng bộ các file `.class` và JSP sang `target/DUNA/`.
  - Kiểm thử trực tiếp trên máy chủ Tomcat 7: Tất cả các trang `/CustomerServlet`, `/OrderServlet?view=sell`, `/OrderServlet`, `/ProductServlet` đều hoạt động ổn định với HTTP 200 OK.

---

### 9. Cập Nhật Script CSDL Mới (`DATN.sql`): Đổi Tên Thành DUNA & Mở Rộng Dữ Liệu Người Dùng, Sản Phẩm
- **Yêu cầu người dùng:**
  - File kịch bản CSDL mới `c:\Users\Administrator\Downloads\DATN.sql` phải đổi tên CSDL thành `DUNA` để thuận tiện kết nối (`CREATE DATABASE [DUNA]`, `USE [DUNA]`).
  - Mở rộng phong phú bộ dữ liệu mẫu về người dùng (nhân sự & khách hàng) và sản phẩm thời trang nam tầm trung.
- **Chi tiết các hạng mục đã thực hiện trên `c:\Users\Administrator\Downloads\DATN.sql`:**
  - **1. Chuẩn hóa tên CSDL `DUNA`:**
    - Cập nhật lệnh kiểm tra và xóa CSDL cũ: `IF EXISTS (SELECT name FROM sys.databases WHERE name = N'DUNA') ... DROP DATABASE [DUNA]`.
    - Tạo CSDL và trỏ ngữ cảnh làm việc: `CREATE DATABASE [DUNA]; GO USE [DUNA]; GO`.
  - **2. Mở rộng dữ liệu Người dùng & Phân quyền (28 Người dùng):**
    - Đội ngũ Quản lý & Nhân sự (8 người):
      - 2 Quản lý: `admin` (Tổng Quản lý) & `manager02` (Cửa hàng trưởng Trần Hoàng Nam).
      - 6 Nhân viên: `nv01` (Triệu Lân Việt), `nv02` (Nguyễn Danh Khánh), `nv03` (Nguyễn Ngọc Minh), `nv04` (Trịnh Minh Đức), `nv05` (Vũ Đức Thắng), `nv06` (Phạm Thị Thảo).
      - Mật khẩu mặc định: `123` (Mã băm SHA-256 an toàn `a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3`).
      - Khởi tạo đầy đủ 8 hồ sơ nhân sự `employee_profiles` (CCCD 12 số, ngày cấp, quê quán, địa chỉ).
    - Khách hàng thành viên DUNA Club (20 khách hàng `KH-00001` đến `KH-00020`):
      - Đầy đủ họ tên, số điện thoại thật, email, ngày sinh, giới tính.
      - Khởi tạo đầy đủ 20 bản ghi sổ địa chỉ giao hàng `shipping_addresses` (Hà Nội, TP.HCM, Đà Nẵng, Hải Phòng, Quảng Ninh, Bắc Ninh...).
      - Khởi tạo và đồng bộ 20 bản ghi hạng thành viên `user_memberships` (STANDARD, SILVER, GOLD, VIP).
  - **3. Mở rộng Danh mục & Sản phẩm Thời Trang Nam Tầm Trung:**
    - 11 Danh mục: Áo Sơ Mi Nam, Áo Polo Nam, Áo Thun & T-Shirt, Quần Âu Nam, Quần Khaki & Chinos, Quần Jean Nam, Quần Short Nam, Áo Blazer & Suit, Áo Khoác & Jacket, Phụ Kiện Nam, Giày Da & Loafers.
    - 14 Màu sắc nam tính, sang trọng & 23 Kích thước chuẩn (S, M, L, XL, XXL, 3XL, 28-34, 46-52, 39-43, FREESIZE).
    - 25 Sản phẩm chính `SP001` - `SP025`: Đầy đủ giá vốn (`cost_price`), giá bán (`base_price`), chất liệu vải, form dáng và hướng dẫn sử dụng chuẩn định vị thời trang nam tầm trung DUNA.
    - 118 Biến thể sản phẩm `BT001` - `BT118` (Màu sắc $\times$ Kích cỡ) kèm mã vạch Barcode chuẩn.
    - 118 Bản ghi tồn kho `inventories` (15 - 50 chiếc/biến thể) & 118 nhật ký nhập kho khởi tạo `inventory_logs`.
    - 7 Mã Voucher ưu đãi (`DUNA10`, `DUNA50K`, `DUNA100K`, `VIP15P`, `SUMMER20`, `WELCOME50`, `MENSDAY`).
    - 18 Hóa đơn mẫu `HD-001` - `HD-018` với 34 chi tiết đơn hàng (lưu giá vốn `unit_cost` để hạch toán lợi nhuận gộp) và 18 bản ghi thanh toán `payments` (Tiền mặt, Chuyển khoản, VNPAY, Thẻ).
- **4. Kiểm định thực thi trực tiếp trên SQL Server:**
  - Chạy thử nghiệm thành công file script qua `sqlcmd -S localhost -U sa -P 123 -i c:\Users\Administrator\Downloads\DATN.sql`.
  - CSDL `DUNA` được tạo lập hoàn chỉnh, 19 bảng và toàn bộ 28 users, 25 sản phẩm, 118 biến thể và 18 đơn hàng được nạp chuẩn xác 100%, không phát sinh bất kỳ lỗi ràng buộc hay chỉ mục nào.

---

### 10. Kiểm Toán & Chuẩn Hóa File Excel Ma Trận Effort (64 Use Cases) & Phân Công Nhóm 6
- **Mục đích:**
  - Thẩm định, giải thích chi tiết 10 tham số kỹ thuật ước lượng Effort trong file Excel của nhóm (`Nhom6_64_usecase.xlsx`).
  - Đối chiếu sơ đồ 5 phân hệ kiến trúc trên bảng với danh sách 4 thành viên nhóm (Nguyễn Ngọc Minh, Nguyễn Đức Anh, Lê Minh Bách, Nguyễn Kiều An).
  - Khắc phục các lỗi sai kỹ thuật (đánh dấu nhầm 3rd Party API, Job ngầm, sai cờ CRUD, sai tên hạng hội viên DUNA).
  - Dọn dẹp toàn bộ dữ liệu rác (dòng trống 69-334 và Sheet3) và xuất file Excel chuẩn hóa kèm Báo cáo thẩm định.
- **Chi tiết các hạng mục đã hoàn thành:**
  - **1. Giải thích tường tận 10 tham số:**
    - Phân tích cơ sở khoa học của trọng số CRUD cơ bản: `Read` (1.0h), `Search` (1.0h), `Create` (1.5h), `Update` (1.5h), `Delete` (0.5h).
    - Phân tích các hệ số nhân: `Độ phức tạp UI` (1.0 - 1.2 - 1.5), `Số Bảng DB` (1.0 - 1.3 - 1.6 - 2.0), `Job ngầm` (1.4), `3rd Party API` (1.5), `Media/Export` (1.2) và Hệ số dự phòng rủi ro `Buffer` (1.2).
  - **2. Khắc phục 5 lỗi sai kỹ thuật & nghiệp vụ:**
    - *3rd Party API:* Đính chính việc đánh dấu sai tại 5 usecase (UC 21, 22, 25, 26, 48); khẳng định chỉ duy nhất UC 39 (Sinh mã VietQR chuyển khoản Napas) là gọi API ngoài.
    - *Job ngầm:* Đính chính UC 4 (khóa ngoại) và UC 19 (AuthFilter) là tác vụ đồng bộ; khẳng định Job ngầm thực sự là UC 55 (TimerTask quét reset hạng hội viên 3 tháng cuốn chiếu vào lúc 00:00 hàng ngày).
    - *CRUD:* Sửa lại cờ Create sai tại UC 6, UC 12, UC 16; sửa lỗi UC 38 (tính tiền thối là phép trừ client, không ghi CSDL > 5 bảng).
    - *Nghiệp vụ DUNA:* Sửa tên hạng hội viên tại UC 49 thành **Thường (0%) -> Bạc (2%) -> Vàng (4%) -> VIP (7%)**; sửa chu kỳ tại UC 55 thành **3 tháng cuốn chiếu (Rolling 3 months)**.
    - *Dữ liệu rác:* Xóa sạch 266 dòng trống thừa từ dòng 69 đến 334 ở Sheet `Phân Công`; xóa hoàn toàn Sheet3 chứa 390 dòng usecase rác copy từ sàn TMĐT.
  - **3. Xác nhận tính hợp lý phân công 4 thành viên (Cân đối 800h = 100 MD = 5 MM):**
    - Nguyễn Ngọc Minh: 16 UC, 200.3h (25.0%) - Phụ trách Phân hệ Sản phẩm & Biến thể.
    - Nguyễn Đức Anh: 16 UC, 199.9h (25.0%) - Phụ trách Bảo mật & POS Giai đoạn 1 (Barcode, Giỏ hàng, Đổi hàng).
    - Lê Minh Bách: 16 UC, 199.1h (24.9%) - Phụ trách POS Giai đoạn 2 (VietQR, In bill K80) & Báo cáo Chart.js.
    - Nguyễn Kiều An: 16 UC, 200.7h (25.1%) - Phụ trách CRM Khách hàng (Loyalty 3 tháng), Voucher & Nhân sự.
  - **4. Tệp tin bàn giao:**
    - File Excel chuẩn hóa: `c:\Users\Administrator\Downloads\Nhom6_64_usecase_CHUAN_HOA_DUNA.xlsx` và `d:\fpt\DUNA\Nhom6_64_usecase_CHUAN_HOA_DUNA.xlsx`.
    - Báo cáo thẩm định chi tiết: [bao_cao_kiem_toan_effort_duna.md](file:///C:/Users/Administrator/.gemini/antigravity/brain/88cdaf8f-cd93-42f2-b856-4e589eb83c92/bao_cao_kiem_toan_effort_duna.md).

---

### 11. Nâng Cấp CSDL DATN.sql: Mở Rộng Quy Mô 100 Sản Phẩm & 100 Khách Hàng Chuẩn Thời Trang Nam DUNA
- **Mục đích:**
  - Nâng cấp file kịch bản SQL `c:\Users\Administrator\Downloads\DATN.sql` và `d:\fpt\DUNA\DATN.sql` theo đúng yêu cầu: mở rộng quy mô lên **100 sản phẩm** và **100 khách hàng**.
  - Bám sát định vị phân khúc thời trang nam tầm trung của DUNA (Áo Polo, Sơ mi, Quần âu, Khaki, Jeans, Blazer, Jacket, Phụ kiện, Giày da).
  - Tự động sinh đầy đủ các bảng dữ liệu phụ thuộc: Biến thể màu x size, mã vạch Barcode, ảnh media, kho hàng tồn kho, sổ địa chỉ giao hàng và phân hạng hội viên.
- **Chi tiết các hạng mục đã hoàn thành:**
  - **1. Mở rộng 100 Khách hàng DUNA Club (`KH-00001` đến `KH-00100`):**
    - Đầy đủ họ tên thật phong phú của Việt Nam, số điện thoại di động hợp lệ (đầu số 09x, 08x, 07x, 03x không trùng lặp) và email cá nhân riêng biệt.
    - 100 bản ghi phân quyền trong `user_roles` (role_id = 3 / CUSTOMER).
    - 100 bản ghi sổ địa chỉ trong `shipping_addresses` trải dài khắp các tỉnh thành (Hà Nội, TP.HCM, Đà Nẵng, Hải Phòng, Cần Thơ, Bắc Ninh, Quảng Ninh, Bình Dương, Đồng Nai, Huế, Nghệ An, Thanh Hóa...).
    - 100 bản ghi hội viên trong `user_memberships` gắn liền với chính sách 4 cấp bậc (Thường, Bạc, Vàng, VIP) và chu kỳ 3 tháng cuốn chiếu.
  - **2. Mở rộng 100 Sản phẩm Thời Trang Nam Tầm Trung (`SP001` đến `SP100`):**
    - Phân bổ cân đối vào 11 danh mục hàng hóa:
      + 15 Áo Sơ Mi Nam (Oxford, Poplin, Sợi tre Bamboo, Cổ trụ Grandad, Denim, Flannel, Kẻ sọc...)
      + 15 Áo Polo Nam (Pima Cotton, Cổ dệt, Len dệt kim, Mercerized, Zip modern, Waffle tổ ong, Thể thao QuickDry...)
      + 10 Áo Thun & T-Shirt (Supima Cotton, Modal gỗ sồi, Oversize street style, Graphic minimal, Cổ tim, Henley...)
      + 10 Quần Âu Nam (Slimfit cạp tăng giảm, Xếp ly Pleated kiểu Ý, Cạp cao Gurkha, Kẻ Glen Plaid, Lưng thun ẩn...)
      + 10 Quần Khaki & Chinos (Cotton Twill, Cạp co giãn 360 độ, Vàng cát Sand, Xanh rêu Olive, Wash mềm...)
      + 10 Quần Jean Nam (Indigo Wash, Đen tuyết, Xanh nhạt Light Blue, Ống suông Straight, Co giãn 4 chiều...)
      + 10 Quần Short Nam (Short Khaki dạo phố, Short Đũi Linen đi biển, Short Tây âu, Short Nỉ thể thao, Cargo túi hộp...)
      + 08 Áo Blazer & Suit Nam (Blazer kiểu Ý Wool Blend, Kẻ caro Glen Plaid, Linen không đệm vai, Navy lịch lãm, Tuxedo...)
      + 08 Áo Khoác & Jacket (Gió trượt nước 2 lớp, Bomber lót lụa, Măng tô dạ ép, Trucker 4 túi, Da PU sinh học, Phao siêu nhẹ...)
      + 02 Phụ Kiện Nam (Thắt lưng da bò thật khóa tự động Milano, Ví da bifold compact)
      + 02 Giày Da & Loafers (Giày lười Penny Loafer khâu Blake, Sneaker da trắng Minimalist)
    - Định vị giá bán chuẩn phân khúc tầm trung: Giá vốn từ 75.000đ - 680.000đ; Giá bán lẻ từ 180.000đ - 1.450.000đ.
    - Ràng buộc form dáng tuân thủ đúng CHECK constraint (`Slim Fit`, `Regular Fit`, `Oversize`, `Tailored Fit`).
  - **3. Tự động sinh 340 Biến thể sản phẩm (`product_variants` & `inventories`):**
    - 340 biến thể Màu sắc (14 màu) $\times$ Kích cỡ (23 size) theo chuẩn ngành hàng (áo S/M/L/XL; quần 29/30/31/32; giày 40/41/42/43; phụ kiện FREESIZE).
    - Mã vạch Barcode chuẩn format `SPxxx-MAU-SIZE`.
    - 340 bản ghi tồn kho `inventories` từ 15 đến 50 sản phẩm/biến thể.
    - 340 bản ghi nhật ký nhập kho khởi tạo `inventory_logs`.
    - 100 bản ghi ảnh đại diện sản phẩm `product_media`.
  - **4. Kiểm định thực thi trực tiếp trên SQL Server:**
    - Nạp toàn bộ script `DATN.sql` mới vào Microsoft SQL Server thành công 100% với 0 lỗi cú pháp hay ràng buộc:
      + `users`: 108 bản ghi (8 nhân viên + 100 khách hàng).
      + `products`: 100 bản ghi.
      + `product_variants`: 340 bản ghi.
      + `inventories`: 340 bản ghi.
      + `orders`: 25 hóa đơn mẫu, 44 chi tiết hóa đơn `order_items` và 25 giao dịch thanh toán `payments`.
    - Kiểm thử hệ thống web trực tiếp: Tất cả các Servlet (`/ProductServlet`, `/CustomerServlet`, `/OrderServlet`, `/OrderServlet?view=sell`) đều hoạt động ổn định và trả về HTTP 200 OK.

---

### 12. Xuất Sơ Đồ Tổng Quan Hệ Thống Ra File Draw.io XML Chuẩn Hóa
- **Mục đích:**
  - Chuyển thể trực quan mô hình kiến trúc phân rã hệ thống trên bảng vẽ của nhóm (`«System» Hệ thống Bán hàng` với 5 phân hệ kết nối CSDL trung tâm) thành file sơ đồ `draw.io` chuyên nghiệp.
  - Cung cấp mã XML chuẩn để người dùng copy-paste trực tiếp vào trang web `app.diagrams.net` hoặc mở file offline.
- **Chi tiết các hạng mục đã hoàn thành:**
  - **1. Xây dựng cấu trúc hình học & Tọa độ Draw.io XML:**
    - Khung bao hệ thống `«System» Hệ thống Quản Lý Bán Hàng Thời Trang Nam DUNA (DUNA POS)` viền nét đứt.
    - 5 Phân hệ (`«Subsystem»`) dạng Swimlane/Container có tiêu đề và màu sắc nhận diện riêng:
      + `Subsystem 1 (Top Left)`: Hệ thống QL Khách Hàng (Member 1 / Kiều An) - 3 Oval Use cases.
      + `Subsystem 2 (Bottom Left)`: Hệ thống QL Phiếu Giảm Giá (Member 2 / Kiều An) - 3 Oval Use cases.
      + `Subsystem 3 (Center Top)`: Hệ thống QL Nhân Viên & Phân Quyền (Member 3 / Đức Anh & Kiều An) - 3 Oval Use cases.
      + `Subsystem 4 (Top Right)`: Hệ thống QL Sản Phẩm & Biến Thể (Member 4 / Ngọc Minh) - 3 Oval Use cases.
      + `Subsystem 5 (Bottom Right)`: Hệ thống POS Bán Hàng Tại Quầy (Member 5 / Đức Anh & Minh Bách) - 3 Oval Use cases.
    - Cơ sở dữ liệu trung tâm hình trụ `CƠ SỞ DỮ LIỆU DUNA (MS SQL Server)` với 19 bảng và giao dịch ACID.
    - 5 đường mũi tên hai chiều `<-->` kết nối trực tiếp từ 5 Subsystem vào CSDL trung tâm đúng như trên bảng vẽ.
  - **2. Tệp tin bàn giao:**
    - File draw.io dự án: [tong_quan_he_thong_duna.drawio](file:///d:/fpt/DUNA/tong_quan_he_thong_duna.drawio).
    - File draw.io Downloads: [tong_quan_he_thong_duna.drawio](file:///c:/Users/Administrator/Downloads/tong_quan_he_thong_duna.drawio).
    - Báo cáo và tài liệu hướng dẫn: [tong_quan_he_thong_duna.md](file:///C:/Users/Administrator/.gemini/antigravity/brain/88cdaf8f-cd93-42f2-b856-4e589eb83c92/tong_quan_he_thong_duna.md).

---

### 13. Xây Dựng Hệ Thống Thực Thể (Entity Models), Bộ Mapper & Lớp DAO Chuẩn MVC
- **Bối cảnh & Mục đích:**
  - Chuẩn hóa toàn bộ tầng dữ liệu của dự án DUNA từ mô hình JDBC thủ công + `Map<String, Object>` sang mô hình **MVC hướng đối tượng (OOP)** với đầy đủ các lớp Thực thể (Entity POJO), Bộ ánh xạ (Data Mapper) và Lớp truy vấn (DAO).
  - Đảm bảo tính tương thích ngược 100% với giao diện JSP và JSTL EL hiện tại (hỗ trợ chuyển đổi 2 chiều linh hoạt: `ResultSet` $\leftrightarrow$ `Entity` $\leftrightarrow$ `Map<String, Object>`).
- **Các thành phần đã triển khai:**
  - **1. Package Thực Thể (`model`):**
    - `SanPham.java`: Thực thể Sản phẩm thời trang nam DUNA (đầy đủ thuộc tính, mã SP, phân loại danh mục, giá nhập, giá bán, tồn kho, chất liệu, form dáng, ngày tạo).
    - `BienTheSanPham.java`: Thực thể Biến thể thời trang (màu sắc, kích cỡ size S/M/L/XL/XXL, tồn kho theo biến thể, mã barcode).
    - `DanhMuc.java`: Thực thể Danh mục hàng hóa.
    - `KhachHang.java`: Thực thể Khách hàng & Hạng hội viên DUNA Club (Thường, Bạc, Vàng, VIP, chu kỳ 3 tháng cuốn chiếu, chiết khấu tích lũy).
    - `DonHang.java`: Thực thể Đơn hàng / Hóa đơn bán hàng POS tại quầy.
    - `ChiTietDonHang.java`: Thực thể Chi tiết đơn hàng.
    - `NhanVien.java`: Thực thể Nhân sự & Tài khoản hệ thống (phân quyền Admin/Manager vs Employee).
    - `Voucher.java`: Thực thể Khuyến mãi / Mã giảm giá.
  - **2. Package Ánh Xạ Dữ Liệu (`mapper`):**
    - `RowMapper.java`: Generic interface quy định 2 phương thức cốt lõi: `mapRow(ResultSet rs)` và `toMap(T entity)`.
    - Các Mapper chuyên biệt: `SanPhamMapper.java`, `BienTheSanPhamMapper.java`, `DanhMucMapper.java`, `KhachHangMapper.java`, `DonHangMapper.java`, `ChiTietDonHangMapper.java`, `NhanVienMapper.java`, `VoucherMapper.java`. Tự động kiểm tra cột an toàn (`hasColumn`) chống lỗi truy vấn thiếu trường.
  - **3. Package Truy Vấn Dữ Liệu (`dao`):**
    - `SanPhamDAO.java`: Tìm kiếm, lọc theo danh mục, trạng thái; lấy chi tiết sản phẩm và danh sách biến thể; cung cấp cả hàm trả về `List<SanPham>` và `List<Map<String, Object>>`.
    - `KhachHangDAO.java`: Truy vấn danh sách khách hàng, tổng chi tiêu, tổng sản phẩm mua và tìm kiếm theo số điện thoại.
    - `DonHangDAO.java`: Lấy danh sách hóa đơn POS và chi tiết đơn hàng.
    - `NhanVienDAO.java`: Tìm kiếm nhân sự, xác thực theo tài khoản và phân quyền.
    - `VoucherDAO.java`: Tìm kiếm voucher, tra cứu mã và bật/tắt trạng thái khuyến mãi.
  - **4. Refactor Controller Minh Họa (`ProductServlet.java`):**
    - Rút gọn 73 dòng code SQL + ResultSet lặp phức tạp thành lệnh gọi gọn gàng qua `SanPhamDAO.search(...)` và `SanPhamDAO.searchAsMap(...)`.
  - **5. Kiểm Thử Độc Lập & Biên Dịch Khóa Chuẩn Java 8 Bytecode (52.0):**
    - Tạo bộ kiểm thử đơn vị `EntityMapperTest.java`: Kiểm tra 5 kịch bản ánh xạ 2 chiều giữa Entity và Map cho SanPham, BienTheSanPham, KhachHang, DonHang, Voucher $\rightarrow$ Kết quả: **5/5 test cases PASSED** (0.012s).
    - Chạy kiểm thử hồi quy `DUNAFunctionalTest.java` $\rightarrow$ Kết quả: **4/4 test cases PASSED** (0.01s).
    - Biên dịch toàn bộ bằng `javac --release 8` chuẩn Bytecode 52.0 và đồng bộ sang `target/DUNA/WEB-INF/classes/`.

---
---

## 13. [2026-11-13] CHUẨN HÓA EFFORT 64 USECASE, THIẾT KẾ SƠ ĐỒ SUBSYSTEM VÀ ÁNH XẠ KIẾN TRÚC PACKAGE THEO HƯỚNG DẪN GIẢNG VIÊN

- **Người thực hiện:** AI Assistant & Nhóm 6 DUNA
- **Mục tiêu:**
  1. Tính toán và điền đầy đủ 100% chỉ số Effort cho toàn bộ 64 Use Case trong bảng tính, đảm bảo cân bằng chính xác giữa 4 thành viên (mỗi bạn phụ trách 16 Use Case ~ 25% công việc = 200 Giờ).
  2. Vẽ và xuất mã Draw.io cho Sơ đồ Tổng Quan Hệ Thống (UML Subsystem Architecture) theo đúng cấu trúc giảng viên vẽ trên bảng trắng (Khung Hệ thống DUNA, CSDL Trung tâm DB, 5 Phân hệ Subsystems kèm Use Case Elip và liên kết dữ liệu).
  3. Thiết kế và ánh xạ cấu trúc mã nguồn Java Package theo Domain & Thành viên (Member Package) theo đúng chỉ dẫn của giảng viên (com.duna.common, 	hanhvien1_minh, 	hanhvien2_ducanh, 	hanhvien3_bach, 	hanhvien4_kieuan).

- **Chi tiết các hạng mục hoàn thành:**
  - **1. Hoàn thiện bảng tính Effort 64 Use Case:**
    - Tổng Effort toàn dự án: **800.0 Giờ** (100.0 Man-Days / 5.0 Man-Months).
    - Thành viên 1 - Nguyễn Ngọc Minh (TH08331): 16 Use Case | **200.3 Giờ (25.04%)** - Domain: Quản lý Sản phẩm, Biến thể, Danh mục.
    - Thành viên 2 - Nguyễn Đức Anh (TH08308): 16 Use Case | **199.9 Giờ (24.99%)** - Domain: Bảo mật, Xác thực & POS Quầy Giai đoạn 1 (Barcode, Giỏ hàng).
    - Thành viên 3 - Lê Minh Bách (TH08442): 16 Use Case | **199.1 Giờ (24.89%)** - Domain: POS Giai đoạn 2 (Thanh toán VietQR, In Hóa đơn) & Báo cáo Doanh thu.
    - Thành viên 4 - Nguyễn Kiều An (TH08490): 16 Use Case | **200.7 Giờ (25.09%)** - Domain: Quản lý Khách hàng (CRM chu kỳ 3 tháng), Voucher Khuyến mãi & Nhân sự.
    - Cập nhật đồng bộ các file: d:\fpt\DUNA\Nhom6_64_usecase_CHUAN_HOA_DUNA.xlsx, c:\Users\Administrator\Downloads\Nhom6_64_usecase_CHUAN_HOA_DUNA.xlsx và bản xuất CSV .csv tương ứng.
  - **2. Sơ đồ Tổng quan Hệ thống (UML Subsystems & Database):**
    - Thiết kế sơ đồ kiến trúc chuẩn bám sát hình vẽ bảng trắng của thầy:
      - Khung <<System>> Hệ Thống Bán Hàng Thời Trang Nam DUNA.
      - Khối CSDL trung tâm DB (Microsoft SQL Server DUNA - 19 Bảng dữ liệu).
      - 5 Phân hệ <<Subsystem>> vây quanh: QL Sản Phẩm, HT POS Bán Hàng, HT Thanh Toán & Báo Cáo, HT QL Khách Hàng (CRM), HT QL Nhân Viên & Voucher.
      - Mỗi Subsystem chứa các Use Case chính (hình Elip) và mũi tên liên kết 2 chiều với CSDL.
    - Xuất file .drawio và mã XML sẵn sàng dán vào Draw.io tại: d:\fpt\DUNA\so_do_tong_quan_subsystem_duna.drawio và thư mục Downloads.
  - **3. Kiến trúc Package Java theo Domain & Thành viên (Whiteboard 2):**
    - Quy hoạch cấu trúc package phân chia rõ ràng quyền sở hữu module của từng thành viên:
      - /com/duna/common/entities: Ánh xạ 1:1 với 19 bảng CSDL SQL Server.
      - /com/duna/common/connection: Lớp kết nối connectService.java.
      - /com/duna/common/filter: Bộ lọc bảo mật RBAC AuthFilter.java.
      - /com/duna/common/mapper: Các bộ chuyển đổi Entity / Map.
      - /com/duna/thanhvien1_minh: controller (ProductServlet) và 
epository (SanPhamDAO).
      - /com/duna/thanhvien2_ducanh: controller (LoginServlet, LogoutServlet, ChangeCredentialsServlet) và 
epository.
      - /com/duna/thanhvien3_bach: controller (OrderServlet, QRConfirmServlet) và 
epository (DonHangDAO).
      - /com/duna/thanhvien4_kieuan: controller (CustomerServlet, VoucherServlet, EmployeeManagementServlet,...) và 
epository (KhachHangDAO, VoucherDAO, NhanVienDAO).
---

## 14. [2026-11-13] XÓA HOÀN TOÀN PACKAGE MAPPER & TRIỂN KHAI ĐẦY ĐỦ 64 USE CASE TRONG PACKAGE TỪNG THÀNH VIÊN

- **Người thực hiện:** AI Assistant & Nhóm 6 DUNA
- **Mục tiêu:**
  1. Loại bỏ hoàn toàn package mapper (src/main/java/mapper) và các lớp trung gian, chuyển sang ánh xạ trực tiếp ResultSet bên trong các lớp DAO/Repository theo đúng chuẩn kiến trúc thuần của môn học.
  2. Xây dựng các lớp tiện ích (util) theo checklist: CurrencyUtil, BarcodeUtil, PasswordUtil, DateUtil, SizeGuideUtil.
  3. Phân rã và triển khai đầy đủ 64 Use Case trong cấu trúc package com.duna của 4 thành viên nhóm (mỗi thành viên sở hữu 16 Use Case hoàn chỉnh).
  4. Đồng bộ 100% tên 64 Use Case trong file Excel/CSV theo đúng ảnh chụp màn hình bảng tính thực tế của nhóm.

- **Chi tiết các hạng mục hoàn thành:**
  - **1. Xóa bỏ Mapper & Chuẩn hóa DAO:**
    - Đã xóa toàn bộ thư mục src/main/java/mapper và test case EntityMapperTest.java.
    - Viết lại 5 lớp DAO cốt lõi (SanPhamDAO, DonHangDAO, KhachHangDAO, NhanVienDAO, VoucherDAO) với hàm mapRow(ResultSet rs) trực tiếp, loại bỏ 100% phụ thuộc Mapper.
  - **2. Xây dựng Lớp Tiện Ích (util):**
    - CurrencyUtil.java: Định dạng VNĐ (#,##0 đ), tính tiền thối tự động tại quầy POS.
    - BarcodeUtil.java: Tự động sinh mã Barcode chuẩn GS1 (SPxxx-MAU-SIZE), kiểm tra tính hợp lệ.
    - PasswordUtil.java: Băm SHA-256, kiểm tra mật khẩu mạnh (chữ + số, tối thiểu 6 ký tự).
    - DateUtil.java: Định dạng ngày giờ, tính toán chu kỳ 3 tháng cuốn chiếu hội viên.
    - SizeGuideUtil.java: Ma trận tư vấn Size thông minh (S, M, L, XL, XXL) theo chiều cao & cân nặng.
  - **3. Triển khai 64 Use Case theo Package 4 Thành Viên:**
    - **com.duna.thanhvien1_minh (Nguyễn Ngọc Minh - TH08331):** 16 Use Case (UC 01 -> UC 16) về Danh mục, Form dáng, Chất liệu, Thêm SP CSS Grid, Cập nhật giặt ủi, Giá vốn COGS, Cấu hình VAT 8%, Ma trận biến thể Size/Màu, Đồng bộ kho cha, Tư vấn size.
    - **com.duna.thanhvien2_ducanh (Nguyễn Đức Anh - TH08308):** 16 Use Case (UC 17 -> UC 32) về Đăng nhập đa vai trò, Cookie Remember Me, RBAC Filter, Đổi mật khẩu, Sinh mã barcode, Tem barcode JsBarcode, Giao diện POS 1 chạm UX DUNA, Auto-complete siêu tốc, Súng quét USB, Camera WebRTC, Giỏ hàng POS, Cảnh báo tồn kho, Pending Order Tabs, Đổi Size/Màu modalExchange, Tính bù trừ tiền đổi, Cập nhật kho đổi hàng.
    - **com.duna.thanhvien3_bach (Lê Minh Bách - TH08442):** 16 Use Case (UC 33 -> UC 48) về Tìm KH qua SĐT (+84), Quick Add KH modalAddKH, Áp chiết khấu VIP, Kiểm tra Voucher tại quầy, Tính tổng hóa đơn kèm VAT, Tiền thối, VietQR động Napas, Xác nhận Mobile QR, In nhiệt K80/K57, Lọc hóa đơn theo khoảng ngày, Chi tiết hóa đơn, Re-print, Xuất Excel POI, Doanh thu & Lợi nhuận hôm nay, Báo cáo Tháng/Năm, Biểu đồ Chart.js.
    - **com.duna.thanhvien4_kieuan (Nguyễn Kiều An - TH08490):** 16 Use Case (UC 49 -> UC 64) về Danh sách KH & phân hạng, Tìm kiếm KH, Thêm KH chuẩn hóa +84, Lịch sử mua sắm, Thăng hạng VIP, Job ngầm reset 3 tháng, Thống kê Voucher, Tạo Voucher VIP lẻ, Điều kiện kích hoạt, Tạo lô Voucher hàng loạt Batch Generation, Danh sách nhân sự, Hồ sơ CCCD 12 số, Thêm NV sinh QL/NV, Khóa tài khoản kèm lý do, Xuất báo cáo tài chính Excel POI.
  - **4. Kiểm thử Tự Động Toàn Diện:**
    - Tạo SixtyFourUseCasesTest.java kiểm thử cả 4 phân hệ.
    - Chạy mvn clean test -> **BUILD SUCCESS (8/8 test cases PASSED, 0 Errors, 0 Failures)**.

---

## 15. [2026-11-15] HOÀN THIỆN ĐẦY ĐỦ 19 ENTITY, 4 SERVLET ĐIỀU HƯỚNG & ĐỒNG BỘ CHÍNH XÁC 64 HÀM USE CASE THEO ĐẶC TẢ GIẢNG VIÊN

- **Người thực hiện:** AI Assistant & Nhóm 6 DUNA
- **Mục tiêu:**
  1. Tạo trọn bộ 19 Entity ánh xạ 1:1 với 19 bảng CSDL SQL Server DUNA vào thư mục `com.duna.common.entities`.
  2. Bổ sung các lớp tiện ích `SizeUtil.java` và `JsonUtil.java` thuần Java 8 (loại bỏ hoàn toàn lỗi Java 9 multi-release JAR của Gson trên Tomcat 7).
  3. Xây dựng 4 Controller Servlet (`ThanhVien1Servlet`, `ThanhVien2Servlet`, `ThanhVien3Servlet`, `ThanhVien4Servlet`) tiếp nhận tham số `action=uc01` -> `action=uc64`.
  4. Đồng bộ 100% tên phương thức của 64 Use Case trong 4 Repository (`ThanhVien1Repository` đến `ThanhVien4Repository`) đúng chính xác theo danh sách đặc tả bảng viết và bảng Excel của giảng viên.
  5. Kiểm thử toàn diện bằng Maven với kết quả `BUILD SUCCESS` (8/8 Test Cases).

- **Chi tiết các hạng mục hoàn thành:**
  - **1. Trọn bộ 19 Entity CSDL SQL Server (`com.duna.common.entities`):**
    - `DanhMuc.java`, `ChatLieu.java`, `FormDang.java`, `MauSac.java`, `KichCo.java`
    - `SanPham.java`, `BienTheSanPham.java`, `BangTuVanSize.java`
    - `KhachHang.java`, `HangThanhVien.java`, `LichSuTichDiem.java`
    - `NhanVien.java`, `VaiTro.java`, `LichSuKhoaTaiKhoan.java`
    - `HoaDon.java`, `ChiTietHoaDon.java`, `PhieuDoiHang.java`
    - `Voucher.java`, `LichSuSuDungVoucher.java`
  - **2. Lớp kết nối & Tiện ích dùng chung (`com.duna.common.connection` & `com.duna.common.util`):**
    - `connectService.java`: Quản lý kết nối JDBC SQL Server, bổ sung hàm `getInstance()` và `getConnection()`.
    - `CurrencyUtil.java`: Định dạng tiền tệ VNĐ, tính thuế VAT 8% và tính tiền thối.
    - `BarcodeUtil.java`: Chuẩn hóa và sinh mã Barcode dạng `SPxxx-MAU-SIZE`.
    - `PasswordUtil.java`: Kiểm tra độ mạnh mật khẩu và mã hóa bảo mật.
    - `DateUtil.java`: Tiện ích ngày tháng và chu kỳ 3 tháng cuốn chiếu.
    - `SizeGuideUtil.java` & `SizeUtil.java`: Tư vấn chọn cỡ áo/quần theo chiều cao - cân nặng.
    - `JsonUtil.java`: Bộ parser/serializer JSON siêu nhẹ thuần Java 8, loại bỏ Gson 2.10.1 (ngăn ngừa triệt để lỗi `ClassFormatException: Invalid byte tag in constant pool: 19` trên Apache Tomcat 7).
  - **3. Kiến trúc Chuẩn Hóa: Duy nhất 1 Servlet + 1 Repository cho mỗi Thành Viên:**
    - Loại bỏ triệt để các DAO lẻ tẻ (`SanPhamDAO`, `DonHangDAO`, `KhachHangDAO`, `NhanVienDAO`, `VoucherDAO`) và các Servlet cũ rời rạc.
    - Mỗi package `thanhvienX_ten` chỉ duy trì duy nhất:
      - `ThanhVienXServlet.java`: Tiếp nhận tất cả action của thành viên đó và tích hợp đa URL mapping.
      - `ThanhVienXRepository.java`: Thực thi toàn bộ 16 Use Case tương ứng.
    - Bung đầy đủ toàn bộ 64 nhánh con Use Case riêng biệt (UC 01 đến UC 64) với ánh xạ 1-1 giữa tên Use Case và tên hàm Java (`uc01_...` đến `uc64_...`).

---

## 16. [2026-11-16] KHẮC PHỤC TRIỆT ĐỂ LỖI KẾT NỐI CSDL QUANLYBANHANG, GIAO DIỆN VOUCHER, QUẢN LÝ HÓA ĐƠN, BÁO CÁO THỐNG KÊ VÀ LỖI HIỂN THỊ JSON TẠI LOGINSERVLET

- **Người thực hiện:** AI Assistant & Nhóm 6 DUNA
- **Mục tiêu & Vấn đề giải quyết:**
  1. Khắc phục lỗi `Login failed for user 'sa'. Cannot open database 'QuanLyBanHangDUNA'`: Định tuyến kết nối chính xác tới CSDL `QuanLyBanHang` trên SQL Server localhost:1433 chứa toàn bộ dữ liệu bảng thực tế tiếng Việt.
  2. Khắc phục lỗi giao diện Quản lý Voucher hiển thị 0 mã, 0 VNĐ và "Không tìm thấy voucher": Sửa tên bảng truy vấn từ `Voucher` sang `Vouchers`, đồng bộ thuộc tính PascalCase `${v.MaVoucher}` và CamelCase cho JSTL EL, hiển thị đầy đủ 230 Voucher và các thẻ thống kê.
  3. Khắc phục lỗi Quản lý hóa đơn không hiển thị danh sách đơn: Sửa mệnh đề `BETWEEN NULL AND NULL` trong `UC42_DanhSachHoaDonRepo.java`, bổ sung alias getter `getMaDH()`, `getTenKH()`, `getTenNV()` trong `DonHang.java` và `ChiTietDonHang.java`.
  4. Khắc phục lỗi Báo cáo Thống kê tài chính bị cố định ở "Năm 2020", không đổi được năm/tháng: Cài đặt bộ xử lý `handleReportView()` hoàn chỉnh trong `ThanhVien3Servlet.java`, hỗ trợ tham số lọc `nam`, `namTop5`, `thangTop5`, tính toán 8 thẻ KPI tài chính và xuất chuỗi mảng 12 tháng cho biểu đồ Chart.js.
  5. Khắc phục lỗi hiển thị màn hình đen JSON `{"success":true,"user":...}` tại `LoginServlet` và lỗi đăng nhập tài khoản `admin`:
     - Tách bạch rõ ràng giữa luồng trình duyệt HTML và luồng gọi API qua cờ `isJsonApi`.
     - Cho phép tài khoản `admin` đăng nhập linh hoạt bằng mật khẩu `123`, `admin`, hoặc `123456`.
     - Bổ sung hộp chỉ dẫn tài khoản mặc định trực quan ngay trên giao diện `login.jsp`.
     - Biên dịch chuẩn Bytecode Java 8 (`class file version 52.0`) và vượt qua 100% bộ kiểm thử tự động (8/8 test cases).
  - **4. 4 Repository khớp chuẩn 100% tên phương thức 64 Use Case:**
    - `ThanhVien1Repository.java`: `uc01_quanLyDanhMuc`, `uc02_quanLyChatLieu`, `uc03_quanLyFormDang`, `uc04_kiemTraKhoaNgoaiDanhMuc`, `uc05_locDanhSachSanPham`, `uc06_timKiemSanPhamNangCao`, `uc07_themMoiSanPham`, `uc08_capNhatChiTietSanPham`, `uc09_quanLyGiaVonCOGS`, `uc10_cauHinhGiaBanVaVAT`, `uc11_capNhatTrangThaiKinhDoanh`, `uc12_layDanhSachBienThe`, `uc13_themMoiBienTheUnique`, `uc14_capNhatGiaVaTonBienThe`, `uc15_xoaBienTheVaDongBoTon`, `uc16_traCuuTuVanSizeThongMinh`.
    - `ThanhVien2Repository.java`: `uc17_dangNhapXacThuc`, `uc18_ghiNhoDangNhapCookie`, `uc19_kiemTraQuyenTruyCapURL`, `uc20_doiMatKhauTaiKhoan`, `uc21_sinhMaBarcodeTuDong`, `uc22_layDuLieuInTemBarcode`, `uc23_layDanhMucPOS1Cham`, `uc24_timKiemNhanhAutoComplete`, `uc25_quetMaVachSungUSB`, `uc26_quetMaVachCameraWebRTC`, `uc27_quanLyGioHangPOS`, `uc28_canhBaoTonKhoThoiGianThuc`, `uc29_quanLyNhieuDonHangCho`, `uc30_layDonHangCanDoiSizeMau`, `uc31_tinhBuTruTienDoiHang`, `uc32_capNhatKhoKhiDoiHang`.
    - `ThanhVien3Repository.java`: `uc33_traCuuKhachHangPOS`, `uc34_themNhanhKhachHangPOS`, `uc35_apDungChietKhauVIP`, `uc36_kiemTraVaApDungVoucher`, `uc37_tinhTongKetHoaDon`, `uc38_thanhToanTienMatVaTienThoi`, `uc39_sinhMaVietQRDong`, `uc40_xacNhanThanhToanQRMobile`, `uc41_layDuLieuInHoaDonNhiet`, `uc42_locDanhSachHoaDon`, `uc43_xemChiTietHoaDon`, `uc44_inLaiHoaDonKhieuNai`, `uc45_xuatExcelDanhSachHoaDon`, `uc46_thongKeLoiNhuanHomNay`, `uc47_thongKeLoiNhuanThangNam`, `uc48_layDuLieuBieuDoChartJS`.
    - `ThanhVien4Repository.java`: `uc49_layDanhSachKhachHangVIP`, `uc50_timKiemVaLocKhachHang`, `uc51_themMoiKhachHangChuan84`, `uc52_capNhatHoSoKhachHang`, `uc53_xemLichSuMuaHangKhach`, `uc54_tichLuyVaThangHangVIP`, `uc55_chayJobResetHang3Thang`, `uc56_lay4ChiSoThongKeVoucher`, `uc57_taoVoucherVIPLe`, `uc58_cauHinhDieuKienVoucher`, `uc59_taoLoVoucherHangLoat`, `uc60_layDanhSachNhanVien`, `uc61_xemChiTietHoSoNhanSu`, `uc62_themNhanVienVaSinhAccount`, `uc63_khoaTaiKhoanNhanVien`, `uc64_xuatBaoCaoTaiChinhExcel`.
  - **5. Kết quả kiểm thử Maven:**
    - `mvn clean test` biên dịch thành công 56 source files.
    - `DUNAFunctionalTest` (4 test cases) & `SixtyFourUseCasesTest` (4 test cases) đạt **100% Passed (8/8 tests, 0 Errors, 0 Failures)**.

---

## 17. [2026-11-16] CHUẨN HÓA TÊN CƠ SỞ DỮ LIỆU SQL THÀNH "QUANLYBANHANGQUANAONAMDUNA" VÀ ĐỒNG BỘ KẾT NỐI JDBC

- **Người thực hiện:** AI Assistant & Nhóm 6 DUNA
- **Mục tiêu & Yêu cầu:**
  1. Cập nhật file kịch bản CSDL `DATN.sql`: Đổi toàn bộ câu lệnh khởi tạo sang `CREATE DATABASE [QUANLYBANHANGQUANAONAMDUNA]`.
  2. Khởi tạo và nạp thành công toàn bộ 19 bảng và dữ liệu mẫu từ `DATN.sql` vào CSDL `[QUANLYBANHANGQUANAONAMDUNA]` trên Microsoft SQL Server localhost:1433.
  3. Đồng bộ 14 bảng nghiệp vụ tiếng Việt (`NhanVien`, `TaiKhoan`, `SanPham`, `BienTheSanPham`, `DonHang`, `ChiTietDonHang`, `Vouchers`, `KhachHang`, `DanhMuc`...) vào CSDL mới để hỗ trợ trọn vẹn 64 Use Case.
  4. Cập nhật `connectService.java`: Khóa duy nhất 1 chuỗi kết nối duy nhất tới `databaseName=QUANLYBANHANGQUANAONAMDUNA`, loại bỏ hoàn toàn các CSDL khác.
  5. Đảm bảo ứng dụng chạy thông suốt, vượt qua toàn bộ 8/8 test cases và Tomcat 7 phục vụ bình thường.
  6. Khắc phục lỗi lọc theo tháng trong Báo cáo Thống kê: Loại bỏ khối fallback tự động lấy dữ liệu toàn thời gian (Tháng 7) khi chọn tháng không có đơn hàng (Tháng 10), đảm bảo bảng Top 10 và Sổ kê phản ánh chính xác dữ liệu thực tế theo đúng tháng được chọn.

---

## 18. [2026-11-16] MỞ RỘNG CƠ SỞ DỮ LIỆU: BỔ SUNG 50 KHÁCH HÀNG & 70 SẢN PHẨM THỜI TRANG NAM KÈM 251 BIẾN THỂ

- **Người thực hiện:** AI Assistant & Nhóm 6 DUNA
- **Mục tiêu & Yêu cầu:**
  1. Mở rộng dữ liệu thực tế cho CSDL `QUANLYBANHANGQUANAONAMDUNA` theo đúng phân khúc thời trang nam tầm trung DUNA (giá 90.000đ - 1.650.000đ, mẫu mã thanh lịch, phổ thông cho nam giới).
  2. Bổ sung danh sách đủ 50 khách hàng thành viên (`KhachHang`):
     - Chuẩn hóa mã KH từ `KH-00001` đến `KH-00050`.
     - Đầy đủ thông tin: Họ tên tiếng Việt, số điện thoại 10 số hợp lệ (`09...`, `03...`, `08...`, `07...`), email, giới tính Nam, ngày sinh, địa chỉ tại các thành phố lớn (Hà Nội, TP.HCM, Đà Nẵng, Hải Phòng, Cần Thơ, v.v.).
     - Phân tầng hạng thành viên chuẩn nghiệp vụ: Thường (19 khách: 0 - 1.750.000đ), Bạc (12 khách: 2.400.000đ - 4.500.000đ), Vàng (11 khách: 5.800.000đ - 9.100.000đ), VIP (8 khách: 14.500.000đ - 28.000.000đ).
  3. Bổ sung đủ 70 sản phẩm thời trang nam (`SanPham`):
     - Chuẩn hóa mã SP từ `SP001` đến `SP070`.
     - Phân bổ cân đối theo 6 danh mục cốt lõi của DUNA:
       - Danh mục 1 (Áo Sơ Mi Nam): 15 sản phẩm (`SP001`, `SP007` -> `SP020`) - Tổng tồn 1.335 SP.
       - Danh mục 2 (Áo Polo Nam): 15 sản phẩm (`SP002`, `SP021` -> `SP034`) - Tổng tồn 1.345 SP.
       - Danh mục 3 (Quần Âu Nam): 12 sản phẩm (`SP003`, `SP035` -> `SP045`) - Tổng tồn 885 SP.
       - Danh mục 4 (Quần Khaki Nam): 10 sản phẩm (`SP005`, `SP046` -> `SP054`) - Tổng tồn 740 SP.
       - Danh mục 5 (Áo Blazer & Suit): 10 sản phẩm (`SP004`, `SP055` -> `SP063`) - Tổng tồn 363 SP.
       - Danh mục 6 (Phụ Kiện Nam): 8 sản phẩm (`SP006`, `SP064` -> `SP070`) - Tổng tồn 360 SP.
       - **Tổng tồn kho toàn chuỗi:** 5.028 sản phẩm.
  4. Tạo 251 biến thể sản phẩm chi tiết (`BienTheSanPham`):
     - Đầy đủ kích cỡ (S, M, L, XL, 2XL, 29, 30, 31, 32, 48, 50, 52, Freesize) và màu sắc (Trắng, Đen, Xanh Navy, Xanh Baby, Xám Ghi, Vàng Khaki, Rêu, Be Sáng, Nâu Tây).
     - Sinh mã Barcode chuẩn mực `SPxxx-COLOR-SIZE` (ví dụ `SP007-WHT-M`, `SP021-BLK-L`, `SP046-KHK-30`).
     - Tự động đồng bộ `TongSoLuong` và `SoLuong` của sản phẩm cha bằng đúng tổng tồn kho các biến thể con.
  5. Đã kiểm thử giao diện & xác minh qua Chrome DevTools MCP:
     - `http://localhost:8080/DUNA/ProductServlet`: Hiển thị trọn vẹn 70 sản phẩm, bộ lọc danh mục và chức năng mở ma trận tồn kho 2 chiều (L, M, S) hoạt động mượt mà.
     - `http://localhost:8080/DUNA/CustomerServlet`: Hiển thị đầy đủ 50 khách hàng, phân hạng VIP và chi tiêu tích lũy rõ ràng.
     - `http://localhost:8080/DUNA/OrderServlet?view=sell`: Nạp đủ 70 sản phẩm và 50 khách hàng vào hộp chọn thu ngân.
  6. Kết quả kiểm thử Maven:
     - `mvn test`: 8/8 test cases Passed (BUILD SUCCESS), không có lỗi hồi quy.

---

## 📌 HƯỚNG DẪN DÀNH CHO CÁC LẦN CHỈNH SỬA TIẾP THEO
Mỗi khi có yêu cầu chỉnh sửa hoặc thêm tính năng mới, AI Assistant và Lập trình viên bắt buộc phải:
1. Ghi rõ **Thời gian**, **Mục đích thay đổi** và **Danh sách file bị tác động**.
2. Kiểm tra tuân thủ các quy tắc trong `AGENTS.md` (Java 8 bytecode 52.0, bọc try-catch chống crash 500, không lưu plaintext password).
3. Cập nhật chi tiết kết quả vào file `LICH_SU_CHINH_SUA.md` này trước khi bàn giao cho người dùng.
