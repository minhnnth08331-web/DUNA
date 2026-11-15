package Controller;

import connectService.connectService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        connectService db = new connectService();

        try (Connection conn = db.myConnection()) {
            if (conn == null) {
                request.setAttribute("errorMessage", "Lỗi: Không thể kết nối đến SQL Server! Hãy kiểm tra dịch vụ SQL Server hoặc tên Database DUNA.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String sql = "SELECT r.role_id, r.role_code, u.user_id, u.full_name " +
                    "FROM users u " +
                    "INNER JOIN user_roles ur ON u.user_id = ur.user_id " +
                    "INNER JOIN roles r ON ur.role_id = r.role_id " +
                    "WHERE u.username = ? AND u.password_hash = ? AND u.status = 'ACTIVE'";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, hashSHA256(pass));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("role", rs.getString("role_code"));
                session.setAttribute("roleID", rs.getInt("role_id"));
                session.setAttribute("nhanVienID", rs.getInt("user_id"));
                session.setAttribute("tenNV", rs.getString("full_name"));
                session.setAttribute("username", user);

                String rememberMe = request.getParameter("rememberMe");
                Cookie cUser = new Cookie("yn_user", "true".equals(rememberMe) ? user : "");
                cUser.setMaxAge("true".equals(rememberMe) ? 30 * 24 * 60 * 60 : 0);
                cUser.setPath("/");
                cUser.setHttpOnly(true);
                response.addCookie(cUser);

                // Bảo mật: Xóa triệt để cookie lưu mật khẩu thô yn_pass nếu tồn tại trước đây
                Cookie cPassClear = new Cookie("yn_pass", "");
                cPassClear.setMaxAge(0);
                cPassClear.setPath("/");
                response.addCookie(cPassClear);

                if ("MANAGER".equals(rs.getString("role_code"))) {
                    response.sendRedirect("EmployeeManagementServlet");
                } else {
                    response.sendRedirect("OrderServlet?view=sell");
                }
            } else {
                String errorMsg = "Sai tài khoản, mật khẩu hoặc tài khoản bị khóa!";
                request.setAttribute("errorMessage", errorMsg);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private String hashSHA256(String input) {
        if (input == null) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}