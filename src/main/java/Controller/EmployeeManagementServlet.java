package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EmployeeManagementServlet")
public class EmployeeManagementServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String q = request.getParameter("searchQuery");
        String status = request.getParameter("statusFilter");
        List<Map<String, String>> list = new ArrayList<>();
        connectService db = new connectService();

        try (Connection conn = db.myConnection()) {
            // Đồng bộ dữ liệu ngẫu nhiên hợp lệ cho CCCD và thông tin cá nhân cho các tài khoản hiện tại (bao gồm Admin)
            try (Statement st = conn.createStatement()) {
                try {
                    st.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'MaNhanVien' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD MaNhanVien VARCHAR(50)");
                    st.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'NgayHetHanCCCD' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD NgayHetHanCCCD DATE");
                    st.executeUpdate("IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'DiaChiHienTai' AND Object_ID = Object_ID('NhanVien')) ALTER TABLE NhanVien ADD DiaChiHienTai NVARCHAR(255)");
                } catch (Exception exCol) {}
            } catch (Exception exSync) {
                exSync.printStackTrace();
            }

            if (request.getSession().getAttribute("tenNV") != null && request.getSession().getAttribute("tenNV").toString().contains("Quản lý hệ thống")) {
                request.getSession().setAttribute("tenNV", "Admin");
            }

            String sql = "SELECT n.*, tt.TenTrangThai, tk.Username, tk.Role_ID FROM NhanVien n " +
                    "INNER JOIN TrangThaiNhanVien tt ON n.TrangThaiNV_ID = tt.TrangThaiNV_ID " +
                    "LEFT JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID " +
                    "WHERE 1=1 ";

            if (q != null && !q.trim().isEmpty()) {
                sql += "AND (n.HoVaTen LIKE ? OR n.MaNhanVien LIKE ? OR tk.Username LIKE ? OR n.SoDienThoai LIKE ? OR n.Email LIKE ?) ";
            }
            if (status != null && !status.trim().isEmpty()) sql += "AND n.TrangThaiNV_ID = ? ";

            sql += "ORDER BY CASE WHEN (tk.Role_ID = 1 OR tk.Username = 'admin' OR n.HoVaTen LIKE '%Admin%' OR (tk.Role_ID IS NULL AND n.MaNhanVien LIKE 'QL%')) AND ISNULL(tk.Role_ID, 0) != 2 THEN 0 ELSE 1 END ASC, n.MaNhanVien ASC, n.ID_NhanVien ASC ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int idx = 1;
            if (q != null && !q.trim().isEmpty()) {
                String searchPattern = "%" + q.trim() + "%";
                for (int i = 1; i <= 5; i++) {
                    ps.setString(idx++, searchPattern);
                }
            }
            if (status != null && !status.trim().isEmpty()) ps.setInt(idx++, Integer.parseInt(status));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> m = new HashMap<>();
                String id = rs.getString("ID_NhanVien");
                String username = rs.getString("Username") != null ? rs.getString("Username") : "";
                String hoVaTen = rs.getString("HoVaTen") != null ? rs.getString("HoVaTen") : "";
                int roleID = 0;
                try { roleID = rs.getInt("Role_ID"); } catch (Exception ignored) {}

                m.put("id", id);
                m.put("ten", hoVaTen);
                m.put("username", username);

                String maNV = null;
                try { maNV = rs.getString("MaNhanVien"); } catch(Exception exCol) {}
                boolean isAdmin = (roleID != 2) && ((roleID == 1) || "admin".equalsIgnoreCase(username) || hoVaTen.contains("Admin") || (roleID == 0 && maNV != null && maNV.trim().toUpperCase().startsWith("QL")));
                if (maNV == null || maNV.trim().isEmpty() || maNV.contains("-")) {
                    try {
                        maNV = isAdmin ? "QL001" : String.format("NV%03d", Integer.parseInt(id));
                    } catch (Exception e) {
                        maNV = isAdmin ? "QL001" : "NV001";
                    }
                }
                m.put("maNV", maNV);
                m.put("isAdmin", isAdmin ? "true" : "false");

                m.put("ngaySinh", rs.getString("NgaySinh") != null ? rs.getString("NgaySinh") : "Chưa cập nhật");
                m.put("trangThai", rs.getString("TenTrangThai"));

                String lyDo = rs.getString("LyDoKhoa");
                m.put("lyDo", (isAdmin || lyDo == null || lyDo.trim().isEmpty()) ? "-" : lyDo);

                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading employees: " + e.getMessage());
        }

        List<Map<String, String>> listQuanLy = new ArrayList<>();
        List<Map<String, String>> listNhanVien = new ArrayList<>();
        for (Map<String, String> emp : list) {
            if ("true".equals(emp.get("isAdmin")) || "1".equals(emp.get("roleID"))) {
                listQuanLy.add(emp);
            } else {
                listNhanVien.add(emp);
            }
        }

        // --- Logic Phân trang (10 dòng/trang cho nhân viên thường) ---
        int pageSize = 10;
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam.trim()); } catch (Exception e) { page = 1; }
        }
        if (page < 1) page = 1;
        int totalItems = listNhanVien.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (page > totalPages && totalPages > 0) page = totalPages;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);
        List<Map<String, String>> pagedList = (totalItems > 0 && fromIndex < totalItems) ? listNhanVien.subList(fromIndex, toIndex) : new ArrayList<>();

        request.setAttribute("listQuanLy", listQuanLy);
        request.setAttribute("listNV", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("employee-management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }
}