package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.service.AuthAccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Controller M01 - Quan ly Tai khoan nguoi dung, Bao mat, Dia chi & Audit Log (UC011 - UC027).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends BaseServlet {
    private final AuthAccountService authService = new AuthAccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("wards".equalsIgnoreCase(req.getParameter("ajax"))) {
            int pid = RequestUtil.getInt(req, "provinceId", 1);
            writeJson(resp, HttpServletResponse.SC_OK, authService.listWardsByProvince(pid));
            return;
        }

        String uc = RequestUtil.getString(req, "uc", "UC011").toUpperCase();
        String keyword = RequestUtil.getString(req, "keyword", "");
        String roleCode = RequestUtil.getString(req, "roleCode", "");
        String status = RequestUtil.getString(req, "status", "");
        String fromDate = RequestUtil.getSafeDate(req, "fromDate", "2026-01-01");
        String toDate = RequestUtil.getSafeDate(req, "toDate", "2027-12-31");
        int userId = RequestUtil.getInt(req, "userId", 0);
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));
        int pageSize = 20;

        List<Map<String, Object>> rows;
        int totalPages = 1;
        if ("UC015".equals(uc) || "UC016".equals(uc)) {
            boolean onlyFailed = "UC016".equals(uc) || "1".equals(req.getParameter("onlyFailed"));
            rows = authService.listLoginHistories(keyword, onlyFailed);
        } else if ("UC023".equals(uc)) {
            rows = authService.listAddresses(userId);
        } else if ("UC027".equals(uc)) {
            rows = authService.listAuditLogs(fromDate, toDate, keyword);
        } else {
            int total = authService.countUsersFiltered(roleCode, status, keyword);
            totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));
            page = Math.min(page, totalPages);
            rows = authService.filterUsersPaged(roleCode, status, keyword, (page - 1) * pageSize, pageSize);
        }

        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }

        req.setAttribute("userStats", authService.getUserStats());
        req.setAttribute("provinces", authService.listProvinces());
        req.setAttribute("wards", authService.listWardsByProvince(1));
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("roleCode", roleCode);
        req.setAttribute("statusFilter", status);
        req.setAttribute("keyword", keyword);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);
        req.setAttribute("selectedUserId", userId);
        setResultRows(req, uc, "Quản lý Tài khoản, Bảo mật & Nhật ký (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/users.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String action = RequestUtil.getString(req, "action", "");
        String returnUc = RequestUtil.getString(req, "returnUc", "UC011");
        int actorId = getSessionUserId(req, 1);
        int affected = 0;
        String extra = "";

        if ("register".equals(action)) {
            long id = authService.registerCustomer(
                    RequestUtil.getString(req, "email", ""),
                    RequestUtil.getString(req, "phone", ""),
                    RequestUtil.getString(req, "password", "Duna123"),
                    RequestUtil.getString(req, "fullName", ""));
            affected = id > 0 ? 1 : 0;
            extra = id == -2L ? "&msg=weak_password" : (id <= 0 ? "&msg=duplicate_user" : "&msg=created");
        } else if ("updateProfile".equals(action)) {
            int uid = RequestUtil.getInt(req, "userId", 0);
            affected = authService.updateProfileAndSync(uid,
                    RequestUtil.getString(req, "fullName", ""),
                    RequestUtil.getString(req, "phone", ""),
                    RequestUtil.getString(req, "email", ""));
            String newPw = RequestUtil.getString(req, "newPassword", "");
            if (!newPw.trim().isEmpty()) {
                int pwRes = authService.adminResetPassword(uid, newPw.trim(), actorId);
                if (pwRes < 0) extra = "&msg=weak_password";
            }
        } else if ("adminResetPassword".equals(action)) {
            int uid = RequestUtil.getInt(req, "userId", 0);
            int res = authService.adminResetPassword(uid, RequestUtil.getString(req, "newPassword", ""), actorId);
            affected = res > 0 ? 1 : 0;
            if (res < 0) extra = "&msg=weak_password";
        } else if ("createResetToken".equals(action)) {
            String tok = authService.createResetToken(RequestUtil.getString(req, "email", ""));
            affected = tok != null ? 1 : 0;
            if (tok != null) {
                extra = "&msg=token&token=" + URLEncoder.encode(tok, "UTF-8");
            }
        } else if ("toggleStatus".equals(action)) {
            affected = authService.toggleUserStatus(RequestUtil.getInt(req, "userId", 0));
        } else if ("deleteUser".equals(action)) {
            affected = authService.deleteUser(RequestUtil.getInt(req, "userId", 0));
            if (affected == 0) extra = "&msg=has_orders";
        } else if ("addAddress".equals(action)) {
            int uid = RequestUtil.getInt(req, "userId", actorId);
            if (uid <= 0) uid = actorId;
            int provId = RequestUtil.getInt(req, "provinceId", 0);
            int wardId = RequestUtil.getInt(req, "wardId", 0);
            affected = authService.addAddress(uid,
                    RequestUtil.getString(req, "receiverName", ""),
                    RequestUtil.getString(req, "receiverPhone", ""),
                    provId > 0 ? provId : null,
                    wardId > 0 ? wardId : null,
                    RequestUtil.getString(req, "addressLine", ""),
                    "1".equals(req.getParameter("isDefault")) || "on".equals(req.getParameter("isDefault")));
            returnUc = "UC023&userId=" + uid;
        } else if ("setDefaultAddress".equals(action)) {
            int uid = RequestUtil.getInt(req, "userId", actorId);
            affected = authService.setDefaultAddress(uid,
                    RequestUtil.getInt(req, "addressId", 0),
                    RequestUtil.getString(req, "addressLine", ""));
            returnUc = "UC023&userId=" + uid;
        } else if ("deleteAddress".equals(action)) {
            int uid = RequestUtil.getInt(req, "userId", 0);
            affected = authService.deleteAddress(RequestUtil.getInt(req, "addressId", 0));
            returnUc = uid > 0 ? ("UC023&userId=" + uid) : "UC023";
        }

        if (wantsJson(req)) {
            writeOkJson(resp, action, "Da cap nhat", affected);
            return;
        }
        String suffix = extra.isEmpty() ? ("&msg=" + (affected > 0 ? "ok" : "skip")) : extra;
        resp.sendRedirect(req.getContextPath() + "/users?uc=" + returnUc + suffix);
    }
}
