package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.service.HumanResourceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M15 - Quan ly Nhan su, Phong ban, Ca lam & Cham cong (UC267 - UC278).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "EmployeeHrServlet", urlPatterns = {
        "/hr", "/EmployeeManagementServlet", "/EmployeeDetailServlet",
        "/AddEmployeeServlet", "/UpdateEmployeeServlet", "/ToggleEmployeeStatusServlet"
})
public class EmployeeHrServlet extends BaseServlet {
    private final HumanResourceService hrService = new HumanResourceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC267");
        String keyword = RequestUtil.getString(req, "keyword", "");
        List<Map<String, Object>> rows;
        if ("UC271".equalsIgnoreCase(uc)) {
            rows = hrService.listDepartmentsAndPositions(keyword);
        } else if ("UC275".equalsIgnoreCase(uc)) {
            rows = hrService.getWeeklySchedule(RequestUtil.getInt(req, "storeId", 0), "2026-01-01", "2027-12-31");
        } else if ("UC278".equalsIgnoreCase(uc)) {
            rows = hrService.monthlyAttendance("2026-01-01", "2027-12-31");
        } else {
            rows = hrService.listEmployees(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Quan ly Nhan su & Cham cong (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/hr.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("addEmployee".equals(action) || "/AddEmployeeServlet".equals(req.getServletPath())) {
            affected = hrService.addEmployee(RequestUtil.getString(req, "employeeCode", ""),
                    null, RequestUtil.getString(req, "fullName", ""),
                    RequestUtil.getString(req, "phone", ""), RequestUtil.getString(req, "email", ""),
                    RequestUtil.getString(req, "birthDate", ""), RequestUtil.getString(req, "hireDate", "2026-11-17"),
                    null, null, RequestUtil.getInt(req, "storeId", 1));
        } else if ("resign".equals(action) || "/ToggleEmployeeStatusServlet".equals(req.getServletPath())) {
            affected = hrService.resignEmployee(RequestUtil.getInt(req, "employeeId", 0));
        } else if ("addPosition".equals(action)) {
            affected = hrService.addPosition(RequestUtil.getString(req, "positionName", ""),
                    RequestUtil.getDouble(req, "baseSalary", 8000000));
        } else if ("checkIn".equals(action)) {
            affected = hrService.checkIn(RequestUtil.getInt(req, "employeeId", 1),
                    RequestUtil.getString(req, "workDate", "2026-11-17"));
        } else if ("checkOut".equals(action)) {
            affected = hrService.checkOut(RequestUtil.getInt(req, "employeeId", 1),
                    RequestUtil.getString(req, "workDate", "2026-11-17"));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/hr?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
