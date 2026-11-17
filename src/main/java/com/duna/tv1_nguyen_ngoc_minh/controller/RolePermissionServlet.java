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
 * Controller M01 - Quan ly Vai tro & Ma tran Phan quyen (UC001 - UC010).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "RolePermissionServlet", urlPatterns = {"/roles"})
public class RolePermissionServlet extends BaseServlet {
    private final AuthAccountService authService = new AuthAccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC001");
        String keyword = RequestUtil.getString(req, "keyword", "");
        int roleId = RequestUtil.getInt(req, "roleId", 1);
        List<Map<String, Object>> rows;
        if ("UC005".equalsIgnoreCase(uc)) {
            rows = authService.listPermissions(keyword);
        } else if ("UC009".equalsIgnoreCase(uc)) {
            rows = authService.getRoleMatrix(roleId);
        } else {
            rows = authService.listRoles(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("roles", authService.listRoles(""));
        req.setAttribute("permissions", authService.listPermissions(""));
        setResultRows(req, uc, "Phân quyền hệ thống (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/roles.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("createRole".equals(action)) {
            affected = authService.createRole(RequestUtil.getString(req, "roleCode", ""),
                    RequestUtil.getString(req, "roleName", ""), RequestUtil.getString(req, "description", ""));
        } else if ("updateRole".equals(action)) {
            affected = authService.updateRole(RequestUtil.getInt(req, "roleId", 0),
                    RequestUtil.getString(req, "roleName", ""), RequestUtil.getString(req, "description", ""));
        } else if ("deleteRole".equals(action)) {
            affected = authService.deleteRole(RequestUtil.getInt(req, "roleId", 0));
        } else if ("createPermission".equals(action)) {
            affected = authService.createPermission(RequestUtil.getString(req, "permissionCode", ""),
                    RequestUtil.getString(req, "permissionName", ""), RequestUtil.getString(req, "moduleName", ""));
        } else if ("grant".equals(action)) {
            affected = authService.grantPermission(RequestUtil.getInt(req, "roleId", 0),
                    RequestUtil.getInt(req, "permissionId", 0));
        } else if ("revoke".equals(action)) {
            affected = authService.revokePermission(RequestUtil.getInt(req, "roleId", 0),
                    RequestUtil.getInt(req, "permissionId", 0));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Da xu ly", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/roles?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
