# NHẬT KÝ & LỊCH SỬ CHỈNH SỬA HỆ THỐNG DUNA MEN'S WEAR

> **Dự án:** DUNA Men's Wear POS Web Application  
> **Môi trường:** Apache Tomcat 7.0.47 | Java 8 (`class file version 52.0`) | Microsoft SQL Server  
> **Quy tắc bắt buộc:** Mọi thay đổi mã nguồn, tính năng, vá lỗi hoặc thiết kế lại giao diện bắt buộc phải được ghi nhận đầy đủ vào tài liệu này theo trình tự thời gian.

---

## 📅 CẬP NHẬT GẦN NHẤT: 13/11/2026

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

## 📌 HƯỚNG DẪN DÀNH CHO CÁC LẦN CHỈNH SỬA TIẾP THEO
Mỗi khi có yêu cầu chỉnh sửa hoặc thêm tính năng mới, AI Assistant và Lập trình viên bắt buộc phải:
1. Ghi rõ **Thời gian**, **Mục đích thay đổi** và **Danh sách file bị tác động**.
2. Kiểm tra tuân thủ các quy tắc trong `AGENTS.md` (Java 8 bytecode 52.0, bọc try-catch chống crash 500, không lưu plaintext password).
3. Cập nhật chi tiết kết quả vào file `LICH_SU_CHINH_SUA.md` này trước khi bàn giao cho người dùng.
