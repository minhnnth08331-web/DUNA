package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AddEmployeeServlet")
public class AddEmployeeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("add-employee.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String roleID = "2"; // Cố định Role_ID = 2 (Nhân viên bán hàng), không cho phép tạo thêm tài khoản Quản lý (Role 1)
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String hoVaTen = request.getParameter("hoVaTen");
        String gioiTinh = request.getParameter("gioiTinh");
        String ngaySinh = request.getParameter("ngaySinh");
        String sdt = request.getParameter("soDienThoai");
        String email = request.getParameter("email");
        String cccd = request.getParameter("cccd");
        String ngayCapCccd = request.getParameter("ngayCapCccd");
        String ngayHetHanCccd = request.getParameter("ngayHetHanCccd");
        String noiCapCccd = request.getParameter("noiCapCccd");
        String queQuan = request.getParameter("queQuan");
        String noiThuongTru = request.getParameter("noiThuongTru");
        String diaChiHienTai = request.getParameter("diaChiHienTai");

        // Validate bắt buộc nhập tất cả các thông tin, không được để trống
        if (username == null || username.trim().isEmpty() ||
            roleID == null || roleID.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            hoVaTen == null || hoVaTen.trim().isEmpty() ||
            gioiTinh == null || gioiTinh.trim().isEmpty() ||
            ngaySinh == null || ngaySinh.trim().isEmpty() ||
            sdt == null || sdt.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            cccd == null || cccd.trim().isEmpty() ||
            ngayCapCccd == null || ngayCapCccd.trim().isEmpty() ||
            ngayHetHanCccd == null || ngayHetHanCccd.trim().isEmpty() ||
            noiCapCccd == null || noiCapCccd.trim().isEmpty() ||
            queQuan == null || queQuan.trim().isEmpty() ||
            noiThuongTru == null || noiThuongTru.trim().isEmpty() ||
            diaChiHienTai == null || diaChiHienTai.trim().isEmpty()) {
            
            request.setAttribute("message", "LỖI: Bắt buộc phải nhập đầy đủ tất cả các trường thông tin nhân viên, không được để trống bất kỳ thông tin nào!");
            request.getRequestDispatcher("add-employee.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("message", "LỖI: Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("add-employee.jsp").forward(request, response);
            return;
        }

        String cleanSdt = sdt.trim().replaceAll("[\\s\\-]", "");
        if (!cleanSdt.matches("0\\d{9}|[1-9]\\d{8}")) {
            request.setAttribute("message", "LỖI: Số điện thoại hợp lệ là 10 số (bắt đầu bằng 0) hoặc 9 số (sau +84)!");
            request.getRequestDispatcher("add-employee.jsp").forward(request, response);
            return;
        }
        if (!cleanSdt.startsWith("0")) {
            sdt = "0" + cleanSdt;
        } else {
            sdt = cleanSdt;
        }

        if (!cccd.trim().matches("\\d{12}")) {
            request.setAttribute("message", "LỖI: Mã số CCCD phải bao gồm chính xác 12 chữ số!");
            request.getRequestDispatcher("add-employee.jsp").forward(request, response);
            return;
        }
        
        try {
            java.time.LocalDate dobDate = java.time.LocalDate.parse(ngaySinh.trim());
            java.time.LocalDate issueDate = java.time.LocalDate.parse(ngayCapCccd.trim());
            java.time.LocalDate expDate = java.time.LocalDate.parse(ngayHetHanCccd.trim());

            if (issueDate.isBefore(dobDate)) {
                request.setAttribute("message", "LỖI: Ngày cấp CCCD không thể sớm hơn Ngày sinh!");
                request.getRequestDispatcher("add-employee.jsp").forward(request, response);
                return;
            }
            if (!expDate.isAfter(issueDate)) {
                request.setAttribute("message", "LỖI: Ngày hết hạn CCCD phải sau Ngày cấp CCCD!");
                request.getRequestDispatcher("add-employee.jsp").forward(request, response);
                return;
            }
        } catch (Exception ex) {
            request.setAttribute("message", "LỖI: Định dạng ngày tháng không hợp lệ!");
            request.getRequestDispatcher("add-employee.jsp").forward(request, response);
            return;
        }

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {


            PreparedStatement psCheckUser = conn.prepareStatement("SELECT TaiKhoanID FROM TaiKhoan WHERE Username = ?");
            psCheckUser.setString(1, username);
            if (psCheckUser.executeQuery().next()) {
                request.setAttribute("message", "L\u1ed2I: Username '" + username + "' \u0111\u00e3 c\u00f3 ng\u01b0\u1eddi s\u1eed d\u1ee5ng!");
                request.getRequestDispatcher("add-employee.jsp").forward(request, response);
                return;
            }


            PreparedStatement psCheckCCCD = conn.prepareStatement("SELECT ID_NhanVien FROM NhanVien WHERE MaSoCCCD = ?");
            psCheckCCCD.setString(1, cccd);
            if (psCheckCCCD.executeQuery().next()) {
                request.setAttribute("message", "L\u1ed2I: M\u00e3 s\u1ed1 CCCD '" + cccd + "' \u0111\u00e3 t\u1ed3n t\u1ea1i trong h\u1ec7 th\u1ed1ng!");
                request.getRequestDispatcher("add-employee.jsp").forward(request, response);
                return;
            }


            String sqlCheckContact = "SELECT ID_NhanVien FROM NhanVien WHERE (SoDienThoai = ? AND LEN(SoDienThoai) > 0) OR (Email = ? AND LEN(Email) > 0)";
            PreparedStatement psCheckContact = conn.prepareStatement(sqlCheckContact);
            psCheckContact.setString(1, sdt != null ? sdt : "");
            psCheckContact.setString(2, email != null ? email : "");
            if (psCheckContact.executeQuery().next()) {
                request.setAttribute("message", "L\u1ed2I: S\u1ed1 \u0111i\u1ec7n tho\u1ea1i ho\u1eb7c Email \u0111\u00e3 \u0111\u01b0\u1ee3c s\u1eed d\u1ee5ng b\u1edfi ng\u01b0\u1eddi kh\u00e1c!");
                request.getRequestDispatcher("add-employee.jsp").forward(request, response);
                return;
            }

            conn.setAutoCommit(false);


            try (Statement stCol = conn.createStatement()) {
                stCol.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'NgayHetHanCCCD' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD NgayHetHanCCCD DATE");
                stCol.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'DiaChiHienTai' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD DiaChiHienTai NVARCHAR(255)");
            } catch (Exception ex) {}

            String sqlNV = "INSERT INTO NhanVien (HoVaTen, MaSoCCCD, NgayCapCCCD, NgayHetHanCCCD, NoiCapCCCD, TrangThaiNV_ID, GioiTinh, NgaySinh, SoDienThoai, Email, QueQuan, NoiThuongTru, DiaChiHienTai) VALUES (?, ?, ?, ?, ?, 1, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psNV = conn.prepareStatement(sqlNV, Statement.RETURN_GENERATED_KEYS);
            psNV.setString(1, hoVaTen.trim());
            psNV.setString(2, cccd.trim());
            psNV.setString(3, ngayCapCccd.trim());
            psNV.setString(4, ngayHetHanCccd.trim());
            psNV.setString(5, noiCapCccd.trim());
            psNV.setInt(6, Integer.parseInt(gioiTinh.trim()));
            psNV.setString(7, ngaySinh.trim());
            psNV.setString(8, sdt.trim());
            psNV.setString(9, email.trim());
            psNV.setString(10, queQuan.trim());
            psNV.setString(11, noiThuongTru.trim());
            psNV.setString(12, diaChiHienTai.trim());

            psNV.executeUpdate();

            ResultSet rs = psNV.getGeneratedKeys();
            int idNhanVien = 0;
            if (rs.next()) idNhanVien = rs.getInt(1);


            String sqlTK = "INSERT INTO TaiKhoan (Username, Password, Role_ID, TrangThaiTK_ID, NhanVienID) VALUES (?, ?, ?, 1, ?)";
            PreparedStatement psTK = conn.prepareStatement(sqlTK);
            psTK.setString(1, username.trim());
            psTK.setString(2, password);
            psTK.setString(3, roleID.trim());
            psTK.setInt(4, idNhanVien);
            psTK.executeUpdate();

            // Cập nhật mã nhân viên (MaNhanVien) tuần tự chuẩn
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'MaNhanVien' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD MaNhanVien VARCHAR(50)");
                String updateManagers = "WITH ManagerCTE AS (" +
                        "SELECT n.ID_NhanVien, ROW_NUMBER() OVER (ORDER BY CASE WHEN n.HoVaTen = N'Admin' OR tk.Username = 'admin' THEN 0 ELSE 1 END ASC, n.ID_NhanVien ASC) AS rn " +
                        "FROM NhanVien n LEFT JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID " +
                        "WHERE (tk.Role_ID = 1 OR tk.Username = 'admin' OR n.HoVaTen LIKE '%Admin%' OR (tk.Role_ID IS NULL AND n.MaNhanVien LIKE 'QL%')) AND ISNULL(tk.Role_ID, 0) != 2" +
                        ") UPDATE n SET n.MaNhanVien = 'QL' + RIGHT('000' + CAST(m.rn AS VARCHAR), 3) " +
                        "FROM NhanVien n JOIN ManagerCTE m ON n.ID_NhanVien = m.ID_NhanVien";
                st.executeUpdate(updateManagers);

                String updateEmployees = "WITH EmployeeCTE AS (" +
                        "SELECT n.ID_NhanVien, ROW_NUMBER() OVER (ORDER BY n.ID_NhanVien ASC) AS rn " +
                        "FROM NhanVien n WHERE n.ID_NhanVien NOT IN (" +
                        "SELECT n2.ID_NhanVien FROM NhanVien n2 LEFT JOIN TaiKhoan tk2 ON n2.ID_NhanVien = tk2.NhanVienID " +
                        "WHERE (tk2.Role_ID = 1 OR tk2.Username = 'admin' OR n2.HoVaTen LIKE '%Admin%' OR (tk2.Role_ID IS NULL AND n2.MaNhanVien LIKE 'QL%')) AND ISNULL(tk2.Role_ID, 0) != 2" +
                        ")) UPDATE n SET n.MaNhanVien = 'NV' + RIGHT('000' + CAST(e.rn AS VARCHAR), 3) " +
                        "FROM NhanVien n JOIN EmployeeCTE e ON n.ID_NhanVien = e.ID_NhanVien";
                st.executeUpdate(updateEmployees);
            } catch(Exception exMa) {}

            conn.commit();
            request.setAttribute("message", "Th\u00eam nh\u00e2n vi\u00ean m\u1edbi th\u00e0nh c\u00f4ng v\u1edbi \u0111\u1ea7y \u0111\u1ee7 th\u00f4ng tin!");
        } catch (Exception e) {
            request.setAttribute("message", "L\u1ed7i h\u1ec7 th\u1ed1ng: " + e.getMessage());
        }
        request.getRequestDispatcher("add-employee.jsp").forward(request, response);
    }
}