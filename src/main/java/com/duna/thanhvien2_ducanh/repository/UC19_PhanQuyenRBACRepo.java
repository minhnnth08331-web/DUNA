package com.duna.thanhvien2_ducanh.repository;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 19: Bộ lọc kiểm soát quyền truy cập URL theo vai trò (AuthFilter RBAC)
 */
public class UC19_PhanQuyenRBACRepo {

    public boolean checkRoleAccess(int roleId, String requestPath) {
        if (roleId == 1) return true;
        if (roleId == 2) {
            String p = (requestPath != null ? requestPath.toLowerCase() : "");
            return !p.contains("employeemanagementservlet") &&
                   !p.contains("addemployeeservlet") &&
                   !p.contains("toggleemployeestatus") &&
                   !p.contains("updateemployeeservlet");
        }
        return false;
    }
}
