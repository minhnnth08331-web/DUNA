package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ToggleEmployeeStatusServlet")
public class ToggleEmployeeStatusServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("mode", "edit");

        String id = request.getParameter("idNhanVien");
        String currentStatus = request.getParameter("currentStatus");
        String lyDo = request.getParameter("lyDoThayDoi");

        int newStatus = 1;

        if ("1".equals(currentStatus)) {
            newStatus = 2; // Hành động: KHÓA
            
            HttpSession session = request.getSession();
            Integer loggedInAdminId = (Integer) session.getAttribute("nhanVienID");
            if (loggedInAdminId != null && loggedInAdminId.toString().equals(id)) {
                request.setAttribute("message", "L\u1ed2I: B\u1ea1n kh\u00f4ng th\u1ec3 t\u1ef1 kh\u00f3a t\u00e0i kho\u1ea3n c\u1ee7a ch\u00ednh m\u00ecnh!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }
            if (lyDo == null || lyDo.trim().isEmpty()) {
                request.setAttribute("message", "L\u1ed2I: B\u1eaft bu\u1ed9c ph\u1ea3i nh\u1eadp L\u00fd do khi kh\u00f3a t\u00e0i kho\u1ea3n!");
                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                return;
            }
        } else {
            newStatus = 1; // Hành động: MỞ KHÓA
            lyDo = ""; // Mở khóa thì xóa trắng lý do cũ
        }

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            conn.setAutoCommit(false);

            if (newStatus == 2) {
                try (PreparedStatement psChk = conn.prepareStatement("SELECT n.MaNhanVien, n.HoVaTen, tk.Role_ID, tk.Username FROM NhanVien n LEFT JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID WHERE n.ID_NhanVien = ?")) {
                    psChk.setString(1, id);
                    try (ResultSet rsChk = psChk.executeQuery()) {
                        if (rsChk.next()) {
                            int rId = rsChk.getInt("Role_ID");
                            String uName = rsChk.getString("Username");
                            String maNV = rsChk.getString("MaNhanVien");
                            String hoTen = rsChk.getString("HoVaTen");
                            if (rId == 1 || "admin".equalsIgnoreCase(uName) || (maNV != null && maNV.trim().toUpperCase().startsWith("QL")) || (hoTen != null && hoTen.contains("Admin"))) {
                                request.setAttribute("message", "L\u1ed2I: Kh\u00f4ng th\u1ec3 kh\u00f3a t\u00e0i kho\u1ea3n Qu\u1ea3n tr\u1ecb v\u00e0 Qu\u1ea3n l\u00fd (Admin)!");
                                request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
                                return;
                            }
                        }
                    }
                }
            }

            String sqlNV = "UPDATE NhanVien SET TrangThaiNV_ID = ?, NgayCapNhat = GETDATE(), LyDoKhoa = ? WHERE ID_NhanVien = ?";
            PreparedStatement psNV = conn.prepareStatement(sqlNV);
            psNV.setInt(1, newStatus);
            psNV.setString(2, lyDo);
            psNV.setString(3, id);
            psNV.executeUpdate();


            String sqlTK = "UPDATE TaiKhoan SET TrangThaiTK_ID = ? WHERE NhanVienID = ?";
            PreparedStatement psTK = conn.prepareStatement(sqlTK);
            psTK.setInt(1, newStatus);
            psTK.setInt(2, Integer.parseInt(id));
            psTK.executeUpdate();

            conn.commit();
            request.setAttribute("message", newStatus == 2 ? " \u0110\u00e3 kh\u00f3a t\u00e0i kho\u1ea3n nh\u00e2n vi\u00ean!" : "M\u1edf kh\u00f3a t\u00e0i kho\u1ea3n th\u00e0nh c\u00f4ng!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("EmployeeDetailServlet?id=" + id).forward(request, response);
    }
}