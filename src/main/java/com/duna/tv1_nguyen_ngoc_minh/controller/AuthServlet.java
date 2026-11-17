package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.service.AuthAccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Controller M01 - Dang nhap, Dang ky, Quen/Doi mat khau & Dang xuat (UC014 - UC020).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/auth", "/LoginServlet", "/LogoutServlet", "/ChangeCredentialsServlet"})
public class AuthServlet extends BaseServlet {
    private final AuthAccountService authService = new AuthAccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String action = RequestUtil.getString(req, "action", "login");
        if ("/LogoutServlet".equals(servletPath) || "logout".equalsIgnoreCase(action)) {
            HttpSession s = req.getSession(false);
            if (s != null) {
                s.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login.jsp?logout=1");
            return;
        }
        if ("register".equalsIgnoreCase(action)) {
            forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/register.jsp");
            return;
        }
        forwardView(req, resp, "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = RequestUtil.getString(req, "action", "login");
        try {
            if ("register".equalsIgnoreCase(action)) {
                handleRegister(req, resp);
                return;
            }
            if ("changePassword".equalsIgnoreCase(action)) {
                handleChangePassword(req, resp);
                return;
            }
            handleLogin(req, resp);
        } catch (Exception e) {
            if (wantsJson(req)) {
                writeErrJson(resp, 500, "Loi xu ly xac thuc: " + e.getMessage());
            } else {
                req.setAttribute("errorMessage", "Loi ket noi CSDL: " + e.getMessage());
                forwardView(req, resp, "/login.jsp");
            }
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginId = RequestUtil.getString(req, "username", RequestUtil.getString(req, "email", ""));
        String password = RequestUtil.getString(req, "password", "");
        String remember = RequestUtil.getString(req, "rememberMe", "");

        Map<String, Object> user = authService.login(loginId, password, req.getRemoteAddr(), req.getHeader("User-Agent"));
        if (user == null) {
            if (wantsJson(req)) {
                writeErrJson(resp, 401, "Sai tai khoan hoac mat khau, hoac tai khoan dang bi khoa.");
            } else {
                req.setAttribute("errorMessage", "Sai email/so dien thoai hoac mat khau (hoac tai khoan bi khoa).");
                forwardView(req, resp, "/login.jsp");
            }
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("userId", user.get("user_id"));
        session.setAttribute("email", user.get("email"));
        session.setAttribute("fullName", user.get("full_name"));
        session.setAttribute("tenNV", user.get("full_name"));
        String role = String.valueOf(user.get("role_code")).toUpperCase();
        session.setAttribute("role", role);

        // Bao mat: Chi luu ten dang nhap vao Cookie HttpOnly, tuyet doi khong luu mat khau tho
        Cookie userCookie = new Cookie("yn_user", "true".equalsIgnoreCase(remember) || "on".equalsIgnoreCase(remember) ? loginId : "");
        userCookie.setHttpOnly(true);
        userCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        userCookie.setMaxAge(("true".equalsIgnoreCase(remember) || "on".equalsIgnoreCase(remember)) ? 7 * 86400 : 0);
        resp.addCookie(userCookie);

        if (wantsJson(req)) {
            writeOkJson(resp, "UC015", "Dang nhap thanh cong", user);
            return;
        }
        if ("STAFF".equals(role) || "EMPLOYEE".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/pos");
        } else {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = RequestUtil.getString(req, "fullName", "");
        String email = RequestUtil.getString(req, "email", "");
        String phone = RequestUtil.getString(req, "phone", "");
        String password = RequestUtil.getString(req, "password", "");
        long id = authService.registerCustomer(email, phone, password, fullName);
        if (wantsJson(req)) {
            if (id > 0) {
                writeOkJson(resp, "UC014", "Dang ky thanh cong", id);
            } else {
                writeErrJson(resp, 400, id == -2L ? "Mat khau yeu (>= 6 ky tu, co chu & so)" : "Email hoac SDT da ton tai");
            }
            return;
        }
        if (id > 0) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=1");
        } else {
            req.setAttribute("errorMessage", id == -2L ? "Mat khau phai >= 6 ky tu, gom ca chu va so." : "Email hoac so dien thoai da duoc su dung.");
            forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/register.jsp");
        }
    }

    private void handleChangePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = getSessionUserId(req, 0);
        String oldPw = RequestUtil.getString(req, "oldPassword", "");
        String newPw = RequestUtil.getString(req, "newPassword", "");
        int res = authService.changePassword(userId, oldPw, newPw);
        if (wantsJson(req)) {
            if (res > 0) writeOkJson(resp, "UC018", "Doi mat khau thanh cong", res);
            else writeErrJson(resp, 400, "Mat khau cu khong dung hoac mat khau moi chua du manh");
        } else {
            resp.sendRedirect(req.getContextPath() + "/users?msg=" + (res > 0 ? "pw_changed" : "pw_error"));
        }
    }
}
