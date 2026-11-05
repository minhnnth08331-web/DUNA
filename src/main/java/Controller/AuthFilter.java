package Controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String loginURI = request.getContextPath() + "/login.jsp";
        String loginServletURI = request.getContextPath() + "/LoginServlet";
        String requestURI = request.getRequestURI();

        boolean loggedIn = session != null && session.getAttribute("role") != null;
        boolean loginRequest = requestURI.equals(loginURI) || requestURI.equals(loginServletURI) || requestURI.endsWith(".css") || requestURI.endsWith(".js") || requestURI.endsWith(".png") || requestURI.endsWith(".jpg") || requestURI.contains("QRConfirmServlet") || requestURI.contains("qr-mobile.jsp");

        if (loggedIn || loginRequest) {
            if (loggedIn && "EMPLOYEE".equals(session.getAttribute("role"))) {
                boolean isReport = requestURI.contains("OrderServlet") && "report".equals(request.getParameter("view"));
                boolean isProductAdmin = (requestURI.contains("ProductServlet") && !"1".equals(request.getParameter("ajaxDetailAll"))) || requestURI.contains("quan-ly-san-pham");
                if (isProductAdmin || requestURI.contains("EmployeeManagementServlet") || requestURI.contains("VoucherServlet") || requestURI.contains("quan-ly-voucher") || isReport || requestURI.contains("add-employee.jsp") || requestURI.contains("employee-management.jsp") || requestURI.contains("CustomerServlet") || requestURI.contains("quan-ly-khach-hang")) {
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell");
                    return;
                }
            }
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(loginURI);
        }
    }
    @Override
    public void destroy() {}
}