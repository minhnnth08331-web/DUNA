package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/UpdateEmployeeServlet")
public class UpdateEmployeeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("mode", "edit");

        String id = request.getParameter("idNhanVien");
        String lyDo = request.getParameter("lyDoThayDoi");
        String sdt = request.getParameter("soDienThoai");
        String email = request.getParameter("email");
        String maNV = request.getParameter("maNV");
        String cccd = request.getParameter("cccd");

        // --- Server-side validation: phone must contain only digits; if ngày sinh provided must be >= 18 ---
        String ns = request.getParameter("ngaySinh");
        LocalDate dob = null;

        // Validate phone: allow entering with or without leading 0 (10 digits starting with 0, or 9 digits without 0)
        if (sdt != null && !sdt.trim().isEmpty()) {
            String cleanSdt = sdt.trim().replaceAll("[\\s\\-]", "");
            if (!cleanSdt.matches("0\\d{9}|[1-9]\\d{8}")) {
                request.setAttribute("message", "LỖI: Số điện thoại hợp lệ là 10 số (bắt đầu bằng 0) hoặc 9 số (sau +84)!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }
            if (!cleanSdt.startsWith("0")) {
                sdt = "0" + cleanSdt;
            } else {
                sdt = cleanSdt;
            }
        }

        // Validate dob and age
        if (ns != null && !ns.trim().isEmpty()) {
            try {
                dob = LocalDate.parse(ns);
                int age = Period.between(dob, LocalDate.now()).getYears();
                if (age < 18) {
                    request.setAttribute("message", "LỖI: Không thể cập nhật - nhân viên phải từ 18 tuổi trở lên.");
                    request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                    return;
                }
            } catch (DateTimeParseException ex) {
                request.setAttribute("message", "LỖI: Ngày sinh không hợp lệ!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }
        }

        String ngayCapCccdStr = request.getParameter("ngayCapCccd");
        String ngayHetHanCccdStr = request.getParameter("ngayHetHanCccd");
        String noiCapCccd = request.getParameter("noiCapCccd");
        String diaChiHienTai = request.getParameter("diaChiHienTai");
        if (cccd != null && !cccd.trim().matches("\\d{12}")) {
            request.setAttribute("message", "LỖI: Mã số CCCD phải bao gồm chính xác 12 chữ số!");
            request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
            return;
        }
        if (ngayCapCccdStr == null || ngayCapCccdStr.trim().isEmpty() || ngayHetHanCccdStr == null || ngayHetHanCccdStr.trim().isEmpty() || noiCapCccd == null || noiCapCccd.trim().isEmpty() || diaChiHienTai == null || diaChiHienTai.trim().isEmpty() || (maNV != null && maNV.trim().isEmpty()) || (cccd != null && cccd.trim().isEmpty())) {
            request.setAttribute("message", "LỖI: Bắt buộc phải nhập đầy đủ Mã nhân viên, Mã số CCCD, Ngày cấp/Hết hạn CCCD, Nơi cấp CCCD và Địa chỉ hiện tại!");
            request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
            return;
        }

        LocalDate ngayCapCccd = null;
        LocalDate ngayHetHanCccd = null;
        try {
            ngayCapCccd = LocalDate.parse(ngayCapCccdStr.trim());
            ngayHetHanCccd = LocalDate.parse(ngayHetHanCccdStr.trim());
            if (dob != null && ngayCapCccd.isBefore(dob)) {
                request.setAttribute("message", "LỖI: Ngày cấp CCCD không thể sớm hơn Ngày sinh!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }
            if (!ngayHetHanCccd.isAfter(ngayCapCccd)) {
                request.setAttribute("message", "LỖI: Ngày hết hạn CCCD phải sau Ngày cấp CCCD!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }
        } catch (DateTimeParseException ex) {
            request.setAttribute("message", "LỖI: Ngày cấp hoặc Ngày hết hạn CCCD không hợp lệ!");
            request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
            return;
        }

        if (lyDo == null || lyDo.trim().isEmpty()) {
            request.setAttribute("message", "LỖI: Bắt buộc phải nhập Lý do thay đổi thông tin!");
            request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
            return;
        }

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {


            String sqlCheck = "SELECT ID_NhanVien FROM NhanVien WHERE ID_NhanVien != ? AND ( (SoDienThoai = ? AND LEN(SoDienThoai) > 0) OR (Email = ? AND LEN(Email) > 0) OR (MaNhanVien = ? AND LEN(MaNhanVien) > 0) OR (MaSoCCCD = ? AND LEN(MaSoCCCD) > 0) )";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, id);
            psCheck.setString(2, sdt != null ? sdt : "");
            psCheck.setString(3, email != null ? email : "");
            psCheck.setString(4, maNV != null ? maNV.trim() : "");
            psCheck.setString(5, cccd != null ? cccd.trim() : "");

            if (psCheck.executeQuery().next()) {
                request.setAttribute("message", "LỖI: Số điện thoại, Email, Mã nhân viên hoặc Mã số CCCD đã được sử dụng bởi người khác!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }


            String sql = "UPDATE NhanVien SET HoVaTen=?, NgaySinh=?, SoDienThoai=?, Email=?, QueQuan=?, NoiThuongTru=?, DiaChiHienTai=?, NgayCapCCCD=?, NgayHetHanCCCD=?, NoiCapCCCD=?, MaSoCCCD=COALESCE(NULLIF(?, ''), MaSoCCCD), NgayCapNhat=GETDATE() WHERE ID_NhanVien=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, request.getParameter("hoVaTen"));

            // set date properly (use parsed dob if available)
            if (dob == null) ps.setNull(2, java.sql.Types.DATE);
            else ps.setDate(2, java.sql.Date.valueOf(dob));

            ps.setString(3, sdt);
            ps.setString(4, email);
            ps.setString(5, request.getParameter("queQuan"));
            ps.setString(6, request.getParameter("noiThuongTru"));
            ps.setString(7, diaChiHienTai != null ? diaChiHienTai.trim() : "");
            
            if (ngayCapCccd == null) ps.setNull(8, java.sql.Types.DATE);
            else ps.setDate(8, java.sql.Date.valueOf(ngayCapCccd));
            
            if (ngayHetHanCccd == null) ps.setNull(9, java.sql.Types.DATE);
            else ps.setDate(9, java.sql.Date.valueOf(ngayHetHanCccd));
            
            ps.setString(10, noiCapCccd);
            ps.setString(11, cccd != null ? cccd.trim() : "");
            ps.setString(12, id);
            ps.executeUpdate();

            try (java.sql.Statement st = conn.createStatement()) {
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
            } catch (Exception ignored) {}

            request.setAttribute("message", "C\u1eadp nh\u1eadt th\u00e0nh c\u00f4ng! (Ghi nh\u1eadn l\u00fd do: " + lyDo + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
    }
}