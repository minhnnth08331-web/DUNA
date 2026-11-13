# AGENTS.md - Quy Tắc Dự Án DUNA (Project Directives & Constraints)

> **Dự án:** DUNA Men's Wear POS Web Application  
> **Định vị:** Thời trang nam tầm trung (phân khúc giá hợp lý, phổ thông, thanh lịch & hiện đại)  
> **Công nghệ:** Java Servlet (`javax.servlet-api:4.0.1`), JSP/JSTL, Apache Tomcat 7 (`tomcat7-maven-plugin:2.2`), Microsoft SQL Server (`QuanLyBanHangDUNA`).

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
   - **CẤM** lưu mật khẩu thô (plaintext password) vào Cookie của client (như cookie `yn_pass` trước đây).
   - Chỉ lưu username vào `yn_user` kèm cờ `HttpOnly = true`.
2. **Phân quyền (RBAC) & Filter:**
   - Mọi Servlet quản trị mới (`Add*`, `Update*`, `Toggle*`, `Change*`, `/ProductServlet`, `/EmployeeManagementServlet`, `/VoucherServlet`) bắt buộc phải được bảo vệ trong `AuthFilter.java` để ngăn chặn nhân viên `EMPLOYEE` leo quyền.
3. **Phòng chống Crash 500:**
   - Mọi tham số ID từ URL hoặc Form (`request.getParameter("id")`) phải được kiểm tra `null`, rỗng và bọc trong khối `try-catch NumberFormatException`.
4. **Nhất quán thương hiệu DUNA (Phân khúc thời trang nam tầm trung):**
   - DUNA là thương hiệu chuỗi cửa hàng thời trang nam **tầm trung** (mid-range: áo polo, sơ mi, quần âu, khaki, áo khoác... với phân khúc giá hợp lý, chất lượng vải tốt, thiết kế hiện đại, thanh lịch và phổ thông cho nam giới).
   - Tuyệt đối không định vị nhầm lẫn sang hàng xa xỉ/luxury cao cấp hay sao chép code từ các ngành hàng khác (như "vợt & phụ kiện cầu lông").
   - Mọi đề xuất tính năng, giao diện, bảng giá, phân loại sản phẩm và nội dung hiển thị phải luôn bám sát định vị phân khúc thời trang nam tầm trung này.
5. **Ghi Nhận Lịch Sử Chỉnh Sửa:**
   - Bất cứ khi nào hoàn thành một lượt sửa lỗi, cập nhật logic hay thay đổi giao diện, bắt buộc phải cập nhật chi tiết nội dung thay đổi vào file `LICH_SU_CHINH_SUA.md` tại thư mục gốc.
