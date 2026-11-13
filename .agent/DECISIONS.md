# Nhật Ký Quyết Định Kỹ Thuật (Architecture Decision Records - ADR)
*Lưu trữ bởi Thư ký dự án (Claude Mem / Secretary Persona)*

---

## [ADR-001] Khóa Chuẩn Bytecode Java 8 (Bytecode Version 52.0)
- **Ngày ghi nhận:** 13/11/2026
- **Bối cảnh:** Khi khởi động `mvn tomcat7:run` trên môi trường Java 21, Tomcat 7 bị crash do lỗi `UnsupportedClassVersionError: Controller/AuthFilter has been compiled by a more recent version of the Java Runtime (class file version 69.0), this version of the Java Runtime only recognizes class file versions up to 65.0`.
- **Nguyên nhân:** Lệnh biên dịch trước đó sử dụng trình biên dịch Java 25 (version 69.0), không tương thích với JVM của Tomcat.
- **Quyết định:** 
  1. Khóa cứng cấu hình `maven-compiler-plugin` trong `pom.xml` ở `<source>1.8</source>` và `<target>1.8</target>`.
  2. Mọi lệnh biên dịch thủ công bắt buộc truyền cờ `--release 8`.
  3. Cả 15 Servlet/Controller đều được recompile về bytecode 52.0.

---

## [ADR-002] Vá Lỗ Hổng Mật Khẩu Cookie & Phân Quyền AuthFilter
- **Ngày ghi nhận:** 13/11/2026
- **Bối cảnh:** QA phát hiện lưu mật khẩu thô trong cookie `yn_pass` 30 ngày và `AuthFilter.java` bỏ sót các servlet quản trị (`ToggleEmployeeStatusServlet`, `AddEmployeeServlet`, `ChangeCredentialsServlet`).
- **Quyết định:**
  1. Xóa hoàn toàn cookie `yn_pass`. Chỉ lưu `yn_user` kèm `HttpOnly`.
  2. Mở rộng bộ lọc `AuthFilter` chặn triệt để nhân viên `EMPLOYEE` gửi request vào các servlet quản lý và admin-dashboard.

---

## [ADR-003] Bọc An Toàn Cho Các Tham Số Integer ID
- **Ngày ghi nhận:** 13/11/2026
- **Bối cảnh:** `OrderServlet.java` dòng 67 parse ID trực tiếp không bọc try-catch gây sập HTTP 500 khi ID rỗng.
- **Quyết định:** Mọi truy vấn ID từ `HttpServletRequest` phải kiểm tra `null/empty` và bọc trong `try-catch NumberFormatException`.
