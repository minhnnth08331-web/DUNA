package com.duna.common.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Bo loc bao mat phan quyen RBAC (UC010, UC015, UC019).
 * Tuyet doi khong luu mat khau tho trong Cookie; chi dung HttpSession va HttpOnly username.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Set<String> ADMIN_ONLY_PATHS = new HashSet<>(Arrays.asList(
            "/roles", "/users", "/hr", "/EmployeeManagementServlet", "/AddEmployeeServlet",
            "/UpdateEmployeeServlet", "/ToggleEmployeeStatusServlet", "/finance",
            "/system", "/marketing", "/notifications", "/procurement", "/categories",
            "/promotions", "/vouchers", "/VoucherServlet", "/uc-run"
    ));

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String ctx = request.getContextPath();
        String uri = request.getRequestURI();
        String path = uri.startsWith(ctx) ? uri.substring(ctx.length()) : uri;

        if (isPublicResource(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        String role = (session != null && session.getAttribute("role") != null)
                ? String.valueOf(session.getAttribute("role")).toUpperCase()
                : null;

        if (role == null || role.isEmpty()) {
            if (isJsonRequest(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"ok\":false,\"message\":\"Vui long dang nhap he thong DUNA\"}");
                return;
            }
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        boolean isAdminOrManager = "ADMIN".equals(role) || "MANAGER".equals(role);
        boolean isStaff = "STAFF".equals(role) || "EMPLOYEE".equals(role);

        if (!isAdminOrManager && isRestrictedForNonAdmin(path, request, isStaff)) {
            if (isJsonRequest(request)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"ok\":false,\"message\":\"Tu choi truy cap: Tai khoan khong du quyen quan tri (RBAC)\"}");
                return;
            }
            String fallback = isStaff ? "/OrderServlet?view=sell&error=access_denied" : "/products?error=access_denied";
            response.sendRedirect(ctx + fallback);
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isPublicResource(String path) {
        if (path.isEmpty() || "/".equals(path) || "/index.jsp".equals(path) || "/login.jsp".equals(path)) {
            return true;
        }
        if ("/auth".equals(path) || "/LoginServlet".equals(path) || "/LogoutServlet".equals(path)
                || "/QRConfirmServlet".equals(path) || "/qr-mobile.jsp".equals(path)) {
            return true;
        }
        return path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png")
                || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".ico")
                || path.endsWith(".svg") || path.startsWith("/img/");
    }

    private boolean isRestrictedForNonAdmin(String path, HttpServletRequest request, boolean isStaff) {
        if (ADMIN_ONLY_PATHS.contains(path)) {
            return true;
        }
        if ("/OrderServlet".equals(path) && "report".equalsIgnoreCase(request.getParameter("view"))) {
            return true;
        }
        if ("/ProductServlet".equals(path)) {
            String act = request.getParameter("action");
            boolean allowedVariantRead = "GET".equalsIgnoreCase(request.getMethod())
                    && ("uc12".equalsIgnoreCase(act) || "uc16".equalsIgnoreCase(act) || "1".equals(request.getParameter("ajaxDetailAll")));
            if (!allowedVariantRead) {
                return true;
            }
        }
        if (("/products".equals(path) || "/variants".equals(path)) && "POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (!isStaff && ("/pos".equals(path) || "/stores".equals(path) || "/inventory".equals(path)
                || "/warehouses".equals(path) || "/customers".equals(path) || "/CustomerServlet".equals(path))) {
            return true;
        }
        return false;
    }

    private boolean isJsonRequest(HttpServletRequest req) {
        return "json".equalsIgnoreCase(req.getParameter("format"))
                || "1".equals(req.getParameter("ajax"))
                || (req.getHeader("Accept") != null && req.getHeader("Accept").contains("application/json"));
    }

    @Override
    public void destroy() {
    }
}
