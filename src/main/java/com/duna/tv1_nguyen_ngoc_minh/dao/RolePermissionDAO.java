package com.duna.tv1_nguyen_ngoc_minh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M01 - Vai tro & Quyen han (UC001 - UC010).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class RolePermissionDAO extends BaseDAO {

    // UC001: Xem danh sach vai tro he thong
    public List<Map<String, Object>> uc001_listRoles(String keyword) {
        String sql = "SELECT role_id, role_code, role_name, description, is_active "
                + "FROM roles WHERE role_name LIKE ? OR role_code LIKE ? ORDER BY role_id ASC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC002: Them moi vai tro va ma vai tro duy nhat
    public int uc002_createRole(String roleCode, String roleName, String description) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM roles WHERE role_code = ?) "
                + "INSERT INTO roles (role_code, role_name, description) VALUES (?, ?, ?)";
        return executeUpdate(sql, roleCode, roleCode, roleName, description);
    }

    // UC003: Cap nhat ten hien thi va mo ta vai tro
    public int uc003_updateRole(int roleId, String roleName, String description) {
        String sql = "UPDATE roles SET role_name = ?, description = ? WHERE role_id = ?";
        return executeUpdate(sql, roleName, description, roleId);
    }

    // UC004: Xoa vai tro va kiem tra tai khoan dang su dung
    public int uc004_deleteRoleIfUnused(int roleId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM users WHERE role_id = ?) "
                + "DELETE FROM roles WHERE role_id = ?";
        return executeUpdate(sql, roleId, roleId);
    }

    // UC005: Xem danh sach quyen han theo module
    public List<Map<String, Object>> uc005_listPermissions(String keyword) {
        String sql = "SELECT permission_id, permission_code, permission_name, module_name "
                + "FROM permissions WHERE permission_name LIKE ? OR permission_code LIKE ? "
                + "ORDER BY module_name, permission_code";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC006: Them moi quyen han truy cap chuc nang
    public int uc006_createPermission(String code, String name, String moduleName) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM permissions WHERE permission_code = ?) "
                + "INSERT INTO permissions (permission_code, permission_name, module_name) VALUES (?, ?, ?)";
        return executeUpdate(sql, code, code, name, moduleName);
    }

    // UC007: Gan quyen han cho vai tro
    public int uc007_grantPermissionToRole(int roleId, int permissionId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM role_permissions WHERE role_id = ? AND permission_id = ?) "
                + "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";
        return executeUpdate(sql, roleId, permissionId, roleId, permissionId);
    }

    // UC008: Thu hoi quyen han khoi vai tro
    public int uc008_revokePermissionFromRole(int roleId, int permissionId) {
        String sql = "DELETE FROM role_permissions WHERE role_id = ? AND permission_id = ?";
        return executeUpdate(sql, roleId, permissionId);
    }

    // UC009: Xem ma tran phan quyen chi tiet cua mot vai tro
    public List<Map<String, Object>> uc009_getRolePermissionMatrix(int roleId) {
        String sql = "SELECT r.role_id, r.role_name, p.permission_id, p.module_name, p.permission_code, p.permission_name "
                + "FROM roles r JOIN role_permissions rp ON r.role_id = rp.role_id "
                + "JOIN permissions p ON rp.permission_id = p.permission_id "
                + "WHERE r.role_id = ? ORDER BY p.module_name";
        return queryRows(sql, roleId);
    }

    // UC010: Kiem tra quyen cua nguoi dung khi truy cap chuc nang
    public boolean uc010_hasPermission(int userId, String permissionCode) {
        String sql = "SELECT CASE WHEN EXISTS ("
                + "SELECT 1 FROM users u "
                + "JOIN role_permissions rp ON u.role_id = rp.role_id "
                + "JOIN permissions p ON rp.permission_id = p.permission_id "
                + "WHERE u.user_id = ? AND p.permission_code = ? AND u.status = 'ACTIVE'"
                + ") THEN 1 ELSE 0 END AS has_permission";
        return queryInt(sql, userId, permissionCode) == 1;
    }
}
