package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.PasswordUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.dao.HumanResourceDAO;
import com.duna.tv4_nguyen_kieu_an.service.HumanResourceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller M15 - Quan ly Nhan su, Phong ban, Ca lam & Cham cong (UC267 - UC278) & Giao dien Ho so Nhan su.
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "EmployeeHrServlet", urlPatterns = {
        "/hr", "/EmployeeManagementServlet", "/EmployeeDetailServlet",
        "/AddEmployeeServlet", "/UpdateEmployeeServlet", "/ToggleEmployeeStatusServlet"
})
public class EmployeeHrServlet extends BaseServlet {
    private final HumanResourceService hrService = new HumanResourceService();
    private final HumanResourceDAO hrDAO = new HumanResourceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String servletPath = req.getServletPath();
        String uc = req.getParameter("uc");

        if ("/EmployeeDetailServlet".equals(servletPath)) {
            javax.servlet.http.HttpSession session = req.getSession(false);
            String role = (session != null && session.getAttribute("role") != null)
                    ? String.valueOf(session.getAttribute("role")).toUpperCase() : "";
            boolean isAdminOrManager = "ADMIN".equals(role) || "MANAGER".equals(role);
            if ("1".equals(req.getParameter("ajaxVerifyAdmin"))) {
                if (!isAdminOrManager) {
                    writeErrJson(resp, HttpServletResponse.SC_FORBIDDEN, "Khong du quyen quan tri");
                    return;
                }
                handleVerifyAdminPassword(req, resp);
                return;
            }
            int ownEmpId = (session != null && session.getAttribute("nhanVienID") instanceof Number)
                    ? ((Number) session.getAttribute("nhanVienID")).intValue() : getSessionUserId(req, 1);
            int id = RequestUtil.getInt(req, "id", ownEmpId);
            String mode = RequestUtil.getString(req, "mode", "view");
            if (!isAdminOrManager) {
                id = ownEmpId;
                mode = "view";
            }
            Map<String, Object> nv = hrDAO.getClassicEmployeeDetail(id);
            if (nv == null) {
                resp.sendRedirect(req.getContextPath() + "/EmployeeManagementServlet?error=not_found");
                return;
            }
            if ("1".equals(req.getParameter("credSuccess"))) {
                req.setAttribute("credMessage", "Đã cập nhật mật khẩu đăng nhập mới cho nhân viên.");
            } else if ("1".equals(req.getParameter("credError"))) {
                req.setAttribute("credMessage", "LỖI: Mật khẩu mới phải tối thiểu 6 ký tự (gồm chữ và số) và khớp xác nhận.");
            } else if ("1".equals(req.getParameter("saved"))) {
                req.setAttribute("message", "Đã lưu cập nhật hồ sơ nhân sự thành công.");
            } else if ("1".equals(req.getParameter("phoneError"))) {
                req.setAttribute("message", "LỖI: Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số (03, 05, 07, 08, 09).");
            } else if ("1".equals(req.getParameter("dupError"))) {
                req.setAttribute("message", "LỖI: Email hoặc Số điện thoại đã thuộc về tài khoản khác.");
            } else if ("1".equals(req.getParameter("lockError"))) {
                req.setAttribute("message", "LỖI: Vui lòng nhập lý do bắt buộc khi khóa tài khoản nhân viên.");
            }
            req.setAttribute("nv", nv);
            req.setAttribute("mode", "edit".equalsIgnoreCase(mode) ? "edit" : "view");
            forwardView(req, resp, "/employee-detail.jsp");
            return;
        }

        if ("/EmployeeManagementServlet".equals(servletPath) && (uc == null || uc.isEmpty()) && !wantsJson(req)) {
            handleClassicEmployeeList(req, resp);
            return;
        }

        String resolvedUc = (uc == null || uc.isEmpty()) ? "UC267" : uc;
        String keyword = RequestUtil.getString(req, "keyword", "");
        List<Map<String, Object>> rows;
        if ("UC271".equalsIgnoreCase(resolvedUc)) {
            rows = hrService.listDepartmentsAndPositions(keyword);
        } else if ("UC275".equalsIgnoreCase(resolvedUc)) {
            rows = hrService.getWeeklySchedule(RequestUtil.getInt(req, "storeId", 0), "2026-01-01", "2027-12-31");
        } else if ("UC278".equalsIgnoreCase(resolvedUc)) {
            rows = hrService.monthlyAttendance("2026-01-01", "2027-12-31");
        } else {
            rows = hrService.listEmployees(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, resolvedUc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, resolvedUc, "Quản lý Nhân sự & Chấm công (" + resolvedUc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/hr.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String servletPath = req.getServletPath();
        String action = RequestUtil.getString(req, "action", "");
        int actorUserId = getSessionUserId(req, 1);

        if ("/EmployeeManagementServlet".equals(servletPath)) {
            handleClassicEmployeeList(req, resp);
            return;
        }

        if ("/AddEmployeeServlet".equals(servletPath)) {
            String username = RequestUtil.getString(req, "username", "");
            String password = RequestUtil.getString(req, "password", "");
            String confirm = RequestUtil.getString(req, "confirmPassword", "");
            if (!password.equals(confirm) || !PasswordUtil.isStrongPassword(password)) {
                req.setAttribute("message", "LỖI: Mật khẩu phải từ 6 ký tự trở lên (có cả chữ và số) và khớp nhau.");
                forwardView(req, resp, "/add-employee.jsp");
                return;
            }
            String fullName = RequestUtil.getString(req, "hoVaTen", "");
            String rawPhone = RequestUtil.getString(req, "soDienThoai", "");
            String phone = RequestUtil.normalizePhone(rawPhone);
            if (!RequestUtil.isValidVnPhone(phone)) {
                req.setAttribute("message", "LỖI: Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số (đầu số 03, 05, 07, 08, 09).");
                forwardView(req, resp, "/add-employee.jsp");
                return;
            }
            String email = RequestUtil.getString(req, "email", "");
            String dob = RequestUtil.getSafeDate(req, "ngaySinh", "");
            int createdId = hrDAO.addClassicEmployee(
                    username, PasswordUtil.hashPassword(password), fullName, phone, email, dob,
                    RequestUtil.getString(req, "cccd", ""),
                    RequestUtil.getSafeDate(req, "ngayCapCccd", ""),
                    RequestUtil.getSafeDate(req, "ngayHetHanCccd", ""),
                    RequestUtil.getString(req, "noiCapCccd", ""),
                    RequestUtil.getString(req, "queQuan", ""),
                    RequestUtil.getString(req, "noiThuongTru", ""),
                    RequestUtil.getString(req, "diaChiHienTai", ""),
                    actorUserId
            );
            if (createdId <= 0) {
                req.setAttribute("message", "LỖI: Email hoặc Số điện thoại đã tồn tại trong hệ thống.");
                forwardView(req, resp, "/add-employee.jsp");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/EmployeeDetailServlet?id=" + createdId + "&mode=view&saved=1");
            return;
        }

        if ("/UpdateEmployeeServlet".equals(servletPath)) {
            int id = RequestUtil.getInt(req, "idNhanVien", 0);
            String fullName = RequestUtil.getString(req, "hoVaTen", "");
            String rawPhone = RequestUtil.getString(req, "soDienThoai", "");
            String phone = RequestUtil.normalizePhone(rawPhone);
            if (!rawPhone.isEmpty() && !RequestUtil.isValidVnPhone(phone)) {
                resp.sendRedirect(req.getContextPath() + "/EmployeeDetailServlet?id=" + id + "&mode=edit&phoneError=1");
                return;
            }
            String email = RequestUtil.getString(req, "email", "");
            String dob = RequestUtil.getSafeDate(req, "ngaySinh", "");
            int res = hrDAO.updateClassicEmployee(
                    id, fullName, phone, email, dob,
                    RequestUtil.getString(req, "cccd", ""),
                    RequestUtil.getSafeDate(req, "ngayCapCccd", ""),
                    RequestUtil.getSafeDate(req, "ngayHetHanCccd", ""),
                    RequestUtil.getString(req, "noiCapCccd", ""),
                    RequestUtil.getString(req, "queQuan", ""),
                    RequestUtil.getString(req, "noiThuongTru", ""),
                    RequestUtil.getString(req, "diaChiHienTai", ""),
                    RequestUtil.getString(req, "lyDoThayDoi", ""),
                    actorUserId
            );
            if (res == -1) {
                resp.sendRedirect(req.getContextPath() + "/EmployeeDetailServlet?id=" + id + "&mode=edit&dupError=1");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/EmployeeDetailServlet?id=" + id + "&mode=view&saved=1");
            return;
        }

        if ("/ToggleEmployeeStatusServlet".equals(servletPath)) {
            int id = RequestUtil.getInt(req, "idNhanVien", 0);
            boolean currentlyActive = "1".equals(RequestUtil.getString(req, "currentStatus", "1"));
            String reason = RequestUtil.getString(req, "lyDoThayDoi", "");
            if (currentlyActive && reason.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/EmployeeDetailServlet?id=" + id + "&mode=edit&lockError=1");
                return;
            }
            hrDAO.toggleClassicEmployeeStatus(id, currentlyActive, reason, actorUserId);
            resp.sendRedirect(req.getContextPath() + "/EmployeeDetailServlet?id=" + id + "&mode=view&saved=1");
            return;
        }

        int affected = 0;
        if ("addEmployee".equals(action)) {
            affected = hrService.addEmployee(RequestUtil.getString(req, "employeeCode", ""),
                    null, RequestUtil.getString(req, "fullName", ""),
                    RequestUtil.getString(req, "phone", ""), RequestUtil.getString(req, "email", ""),
                    RequestUtil.getSafeDate(req, "birthDate", ""), RequestUtil.getSafeDate(req, "hireDate", "2026-11-17"),
                    null, null, RequestUtil.getInt(req, "storeId", 1));
        } else if ("resign".equals(action)) {
            affected = hrService.resignEmployee(RequestUtil.getInt(req, "employeeId", 0));
        } else if ("addPosition".equals(action)) {
            affected = hrService.addPosition(RequestUtil.getString(req, "positionName", ""),
                    RequestUtil.getDouble(req, "baseSalary", 8000000));
        } else if ("checkIn".equals(action)) {
            affected = hrService.checkIn(RequestUtil.getInt(req, "employeeId", 1),
                    RequestUtil.getSafeDate(req, "workDate", "2026-11-17"));
        } else if ("checkOut".equals(action)) {
            affected = hrService.checkOut(RequestUtil.getInt(req, "employeeId", 1),
                    RequestUtil.getSafeDate(req, "workDate", "2026-11-17"));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/hr?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }

    private void handleClassicEmployeeList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String searchQuery = RequestUtil.getString(req, "searchQuery", "");
        String statusFilter = RequestUtil.getString(req, "statusFilter", "");
        List<Map<String, Object>> listQuanLy = hrDAO.listClassicEmployees(searchQuery, statusFilter, "MANAGER");
        List<Map<String, Object>> allStaff = hrDAO.listClassicEmployees(searchQuery, statusFilter, "STAFF");
        int pageSize = 15;
        int totalPages = Math.max(1, (int) Math.ceil(allStaff.size() / (double) pageSize));
        int currentPage = Math.min(Math.max(1, RequestUtil.getInt(req, "page", 1)), totalPages);
        int fromIdx = (currentPage - 1) * pageSize;
        int toIdx = Math.min(fromIdx + pageSize, allStaff.size());
        List<Map<String, Object>> pagedStaff = fromIdx < allStaff.size() ? allStaff.subList(fromIdx, toIdx) : allStaff;

        req.setAttribute("listQuanLy", listQuanLy);
        req.setAttribute("listNV", pagedStaff);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        forwardView(req, resp, "/employee-management.jsp");
    }

    private void handleVerifyAdminPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int currentUserId = getSessionUserId(req, 1);
        String adminPass = RequestUtil.getString(req, "adminPass", "");
        Map<String, Object> out = new LinkedHashMap<>();
        boolean ok = !adminPass.isEmpty() && hrDAO.verifyUserPassword(currentUserId, PasswordUtil.hashPassword(adminPass));
        out.put("success", ok);
        if (ok) {
            out.put("password", "(Đã mã hóa SHA-256 an toàn — dùng Đổi mật khẩu để đặt mới)");
        } else {
            out.put("message", "Mật khẩu xác thực tài khoản quản trị không chính xác.");
        }
        writeJson(resp, HttpServletResponse.SC_OK, out);
    }
}
