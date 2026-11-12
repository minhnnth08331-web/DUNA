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

## 📌 HƯỚNG DẪN DÀNH CHO CÁC LẦN CHỈNH SỬA TIẾP THEO
Mỗi khi có yêu cầu chỉnh sửa hoặc thêm tính năng mới, AI Assistant và Lập trình viên bắt buộc phải:
1. Ghi rõ **Thời gian**, **Mục đích thay đổi** và **Danh sách file bị tác động**.
2. Kiểm tra tuân thủ các quy tắc trong `AGENTS.md` (Java 8 bytecode 52.0, bọc try-catch chống crash 500, không lưu plaintext password).
3. Cập nhật chi tiết kết quả vào file `LICH_SU_CHINH_SUA.md` này trước khi bàn giao cho người dùng.
