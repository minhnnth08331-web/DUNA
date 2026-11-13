package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EmployeeDetailServlet")
public class EmployeeDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("1".equals(request.getParameter("ajaxVerifyAdmin"))) {
            response.setContentType("application/json;charset=UTF-8");
            HttpSession currentSession = request.getSession(false);
            if (currentSession == null || !"MANAGER".equals(currentSession.getAttribute("role"))) {
                response.getWriter().write("{\"success\":false,\"message\":\"Truy cập bị từ chối! Yêu cầu quyền Quản lý.\"}");
                return;
            }
            String adminPass = request.getParameter("adminPass");
            String targetId  = request.getParameter("targetId");
            if (adminPass == null || adminPass.trim().isEmpty() || targetId == null || targetId.trim().isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng nhập đầy đủ thông tin xác thực!\"}");
                return;
            }
            try (Connection conn = new connectService().myConnection()) {
                // Kiểm tra mật khẩu admin (Role_ID = 1 hoặc Username = 'admin')
                PreparedStatement psAdm = conn.prepareStatement("SELECT TaiKhoanID FROM TaiKhoan WHERE (Username='admin' OR Role_ID=1) AND Password=? AND TrangThaiTK_ID=1");
                psAdm.setString(1, adminPass);
                ResultSet rsAdm = psAdm.executeQuery();
                if (rsAdm.next()) {
                    // Mật khẩu đúng -> Lấy mật khẩu của nhân viên cần xem (bao gồm cả Admin và Nhân viên thường)
                    PreparedStatement psEmp = conn.prepareStatement("SELECT t.Password FROM NhanVien n INNER JOIN TaiKhoan t ON n.ID_NhanVien = t.NhanVienID WHERE n.ID_NhanVien = ?");
                    psEmp.setString(1, targetId);
                    ResultSet rsEmp = psEmp.executeQuery();
                    String empPass = "Chưa đặt mật khẩu";
                    if (rsEmp.next() && rsEmp.getString("Password") != null) {
                        empPass = rsEmp.getString("Password");
                    }
                    response.getWriter().write("{\"success\":true,\"password\":\"" + empPass.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
                } else {
                    response.getWriter().write("{\"success\":false,\"message\":\"Mật khẩu Admin không chính xác!\"}");
                }
            } catch (Exception ex) {
                response.getWriter().write("{\"success\":false,\"message\":\"Lỗi hệ thống: " + ex.getMessage().replace("\"", "\\\"") + "\"}");
            }
            return;
        }

        HttpSession session = request.getSession(false);
        String role = (session != null && session.getAttribute("role") != null) ? (String) session.getAttribute("role") : "";
        Integer loggedInEmpId = (session != null && session.getAttribute("nhanVienID") != null) ? (Integer) session.getAttribute("nhanVienID") : null;
        boolean isManager = "MANAGER".equals(role) || (session != null && session.getAttribute("roleID") != null && "1".equals(session.getAttribute("roleID").toString()));

        String id = request.getParameter("id");
        int targetId = 0;
        try {
            if (id != null && !id.trim().isEmpty()) targetId = Integer.parseInt(id.trim());
        } catch (NumberFormatException nfe) { targetId = 0; }

        if (targetId <= 0) {
            if (loggedInEmpId != null) {
                targetId = loggedInEmpId;
                id = String.valueOf(targetId);
            } else {
                response.sendRedirect(request.getContextPath() + "/EmployeeManagementServlet");
                return;
            }
        }

        // Chống leo quyền IDOR: EMPLOYEE chỉ được xem thông tin chính mình, không được xem nhân viên khác hoặc Admin
        if (!isManager && (loggedInEmpId == null || loggedInEmpId != targetId)) {
            response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=unauthorized");
            return;
        }

        Map<String, String> nv = new HashMap<>();
        connectService db = new connectService();

        try (Connection conn = db.myConnection()) {
            try (Statement st = conn.createStatement()) {
                try {
                    st.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'NgayHetHanCCCD' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD NgayHetHanCCCD DATE");
                    st.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'DiaChiHienTai' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD DiaChiHienTai NVARCHAR(255)");
                } catch (Exception exCol) {}
            } catch (Exception e) {}

            String sql = "SELECT n.*, t.Username, t.Password, t.Role_ID, tt.TenTrangThai FROM NhanVien n " +
                    "INNER JOIN TaiKhoan t ON n.ID_NhanVien = t.NhanVienID " +
                    "INNER JOIN TrangThaiNhanVien tt ON n.TrangThaiNV_ID = tt.TrangThaiNV_ID WHERE n.ID_NhanVien = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, targetId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nv.put("id", String.valueOf(targetId));
                String username = rs.getString("Username") != null ? rs.getString("Username") : "";
                String hoVaTen = rs.getString("HoVaTen") != null ? rs.getString("HoVaTen") : "";
                int roleID = 0;
                try { roleID = rs.getInt("Role_ID"); } catch (Exception ignored) {}
                nv.put("hoVaTen", hoVaTen);
                nv.put("username", username);
                nv.put("password", "••••••••"); // Bảo mật: Không bao giờ lộ plaintext password trong HTML
                
                String maNV = null;
                try { maNV = rs.getString("MaNhanVien"); } catch(Exception exCol) {}
                boolean isAdmin = (roleID != 2) && ((roleID == 1) || "admin".equalsIgnoreCase(username) || hoVaTen.contains("Admin") || (roleID == 0 && maNV != null && maNV.trim().toUpperCase().startsWith("QL")));
                if (maNV == null || maNV.trim().isEmpty() || maNV.contains("-")) {
                    try {
                        maNV = isAdmin ? "QL001" : String.format("NV%03d", targetId);
                    } catch (Exception e) {
                        maNV = isAdmin ? "QL001" : "NV001";
                    }
                }
                nv.put("maNV", maNV);
                nv.put("isAdmin", isAdmin ? "true" : "false");
                nv.put("cccd", rs.getString("MaSoCCCD"));
                nv.put("ngayCapCccd", rs.getString("NgayCapCCCD"));
                nv.put("ngayHetHanCccd", rs.getString("NgayHetHanCCCD") != null ? rs.getString("NgayHetHanCCCD") : "");
                nv.put("noiCapCccd", rs.getString("NoiCapCCCD"));
                nv.put("ngaySinh", rs.getString("NgaySinh"));
                nv.put("email", rs.getString("Email"));
                nv.put("soDienThoai", rs.getString("SoDienThoai"));
                nv.put("queQuan", rs.getString("QueQuan"));
                nv.put("noiThuongTru", rs.getString("NoiThuongTru"));
                nv.put("diaChiHienTai", rs.getString("DiaChiHienTai") != null ? rs.getString("DiaChiHienTai") : "");
                nv.put("trangThai", rs.getString("TenTrangThai"));
                String ntStr = "";
                try {
                    java.sql.Timestamp ts = rs.getTimestamp("NgayTao");
                    if (ts != null) ntStr = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ts);
                } catch (Exception exDate) {
                    ntStr = rs.getString("NgayTao") != null ? rs.getString("NgayTao").replace(".000", "").replace(".0", "") : "";
                }
                nv.put("ngayTao", ntStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String mode = request.getParameter("mode");
        if (!isManager && "edit".equals(mode)) {
            mode = "view"; // EMPLOYEE không có quyền sửa thông tin
        }
        if (mode == null || !mode.equals("edit")) {
            mode = (String) request.getAttribute("mode");
            if (mode == null || !mode.equals("edit")) {
                mode = "view";
            }
        }
        request.setAttribute("mode", mode);
        request.setAttribute("nv", nv);
        request.getRequestDispatcher("employee-detail.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }
}