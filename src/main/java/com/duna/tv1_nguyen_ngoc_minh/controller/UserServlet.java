package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.service.AuthAccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M01 - Quan ly Tai khoan nguoi dung, Dia chi & Audit Log (UC011 - UC027).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends BaseServlet {
    private final AuthAccountService authService = new AuthAccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC011");
        String keyword = RequestUtil.getString(req, "keyword", "");
        String roleCode = RequestUtil.getString(req, "roleCode", "");
        String status = RequestUtil.getString(req, "status", "");
        int userId = RequestUtil.getInt(req, "userId", getSessionUserId(req, 1));
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));

        List<Map<String, Object>> rows;
        if ("UC012".equalsIgnoreCase(uc)) {
            rows = authService.searchUsers(keyword);
        } else if ("UC013".equalsIgnoreCase(uc)) {
            rows = authService.filterUsers(roleCode, status, keyword);
        } else if ("UC023".equalsIgnoreCase(uc)) {
            rows = authService.listAddresses(userId);
        } else if ("UC027".equalsIgnoreCase(uc)) {
            rows = authService.listAuditLogs("2026-01-01", "2027-12-31", keyword);
        } else {
            rows = authService.listUsers(keyword, (page - 1) * 20, 20);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Quan ly Tai khoan & Nhat ky (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/users.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("toggleStatus".equals(action)) {
            affected = authService.toggleUserStatus(RequestUtil.getInt(req, "userId", 0));
        } else if ("deleteUser".equals(action)) {
            affected = authService.deleteUser(RequestUtil.getInt(req, "userId", 0));
        } else if ("addAddress".equals(action)) {
            affected = authService.addAddress(
                    RequestUtil.getInt(req, "userId", getSessionUserId(req, 1)),
                    RequestUtil.getString(req, "receiverName", ""),
                    RequestUtil.getString(req, "receiverPhone", ""),
                    null, null,
                    RequestUtil.getString(req, "addressLine", ""),
                    "1".equals(req.getParameter("isDefault")));
        } else if ("deleteAddress".equals(action)) {
            affected = authService.deleteAddress(RequestUtil.getInt(req, "addressId", 0));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Da cap nhat", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/users?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
