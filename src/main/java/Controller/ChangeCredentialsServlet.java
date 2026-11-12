package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ChangeCredentialsServlet")
public class ChangeCredentialsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Only MANAGER can perform this action
        String role = (String) request.getSession().getAttribute("role");
        String idNhanVien = request.getParameter("idNhanVien");

        if (!"MANAGER".equals(role) || idNhanVien == null || idNhanVien.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/EmployeeManagementServlet");
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate
        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("credMessage", "LỖI: Mật khẩu mới không được để trống!");
            forwardToDetail(request, response, idNhanVien);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("credMessage", "LỖI: Mật khẩu xác nhận không khớp!");
            forwardToDetail(request, response, idNhanVien);
            return;
        }

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            // Chỉ cập nhật mật khẩu, KHÔNG cho phép sửa username theo quy định
            String sql = "UPDATE t SET t.Password = ? " +
                  "FROM TaiKhoan t INNER JOIN NhanVien n ON n.ID_NhanVien = t.NhanVienID " +
                  "WHERE n.ID_NhanVien = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sql);
            psUpdate.setString(1, newPassword.trim());
            psUpdate.setString(2, idNhanVien);

            int rows = psUpdate.executeUpdate();
            if (rows > 0) {
                request.setAttribute("credMessage", "✅ Đổi mật khẩu đăng nhập thành công!");
            } else {
                request.setAttribute("credMessage", "LỖI: Không tìm thấy tài khoản để cập nhật!");
            }
        } catch (Exception e) {
            request.setAttribute("credMessage", "LỖI hệ thống: " + e.getMessage());
        }

        forwardToDetail(request, response, idNhanVien);
    }

    private void forwardToDetail(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException {
        request.setAttribute("mode", "edit");
        request.getRequestDispatcher("EmployeeDetailServlet?id=" + id + "&mode=edit").forward(request, response);
    }
}
