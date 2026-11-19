package com.duna.tv1_nguyen_ngoc_minh.service;

import com.duna.common.util.PasswordUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.RolePermissionDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.UserDAO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service phan he M01 - Xac thuc, Tai khoan & Phan quyen (UC001 - UC027).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class AuthAccountService {
    private final RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
    private final UserDAO userDAO = new UserDAO();

    public List<Map<String, Object>> listRoles(String keyword) {
        return rolePermissionDAO.uc001_listRoles(keyword);
    }

    public int createRole(String code, String name, String desc) {
        return rolePermissionDAO.uc002_createRole(code.toUpperCase(), name, desc);
    }

    public int updateRole(int roleId, String name, String desc) {
        return rolePermissionDAO.uc003_updateRole(roleId, name, desc);
    }

    public int deleteRole(int roleId) {
        return rolePermissionDAO.uc004_deleteRoleIfUnused(roleId);
    }

    public List<Map<String, Object>> listPermissions(String keyword) {
        return rolePermissionDAO.uc005_listPermissions(keyword);
    }

    public int createPermission(String code, String name, String moduleName) {
        return rolePermissionDAO.uc006_createPermission(code.toUpperCase(), name, moduleName);
    }

    public int grantPermission(int roleId, int permissionId) {
        return rolePermissionDAO.uc007_grantPermissionToRole(roleId, permissionId);
    }

    public int revokePermission(int roleId, int permissionId) {
        return rolePermissionDAO.uc008_revokePermissionFromRole(roleId, permissionId);
    }

    public List<Map<String, Object>> getRoleMatrix(int roleId) {
        return rolePermissionDAO.uc009_getRolePermissionMatrix(roleId);
    }

    public boolean hasPermission(int userId, String permissionCode) {
        return rolePermissionDAO.uc010_hasPermission(userId, permissionCode);
    }

    public List<Map<String, Object>> listUsers(String keyword, int offset, int limit) {
        return userDAO.uc011_listUsersPaged(keyword, offset, limit);
    }

    public List<Map<String, Object>> searchUsers(String keyword) {
        return userDAO.uc012_searchUsers(keyword);
    }

    public List<Map<String, Object>> filterUsers(String roleCode, String status, String keyword) {
        return userDAO.uc013_filterUsersByRoleAndStatus(roleCode, status, keyword);
    }

    public List<Map<String, Object>> filterUsersPaged(String roleCode, String status, String keyword, int offset, int limit) {
        return userDAO.uc013_filterUsersByRoleAndStatusPaged(roleCode, status, keyword, offset, limit);
    }

    public int countUsersFiltered(String roleCode, String status, String keyword) {
        return userDAO.countUsersFiltered(roleCode, status, keyword);
    }

    public Map<String, Object> getUserStats() {
        return userDAO.getUserStats();
    }

    public List<Map<String, Object>> listLoginHistories(String keyword, boolean onlyFailed) {
        return userDAO.uc015_listLoginHistories(keyword, onlyFailed);
    }

    public int updateProfileAndSync(int userId, String fullName, String phone, String email) {
        return userDAO.uc017_updateProfileAndSync(userId, fullName, RequestUtil.normalizePhone(phone), email);
    }

    public int adminResetPassword(int userId, String newPassword, int actorUserId) {
        if (!PasswordUtil.isStrongPassword(newPassword)) {
            return -1;
        }
        return userDAO.adminResetPassword(userId, PasswordUtil.hashPassword(newPassword), actorUserId);
    }

    public List<Map<String, Object>> listProvinces() {
        return userDAO.listAllProvinces();
    }

    public List<Map<String, Object>> listWardsByProvince(int provinceId) {
        return userDAO.listWardsByProvince(provinceId);
    }

    public long registerCustomer(String email, String phone, String rawPassword, String fullName) {
        if (!PasswordUtil.isStrongPassword(rawPassword)) {
            return -2L;
        }
        String normPhone = RequestUtil.normalizePhone(phone);
        if (!RequestUtil.isValidVnPhone(normPhone)) {
            return -3L;
        }
        String hash = PasswordUtil.hashPassword(rawPassword);
        return userDAO.uc014_registerCustomerUser(email.trim(), normPhone, hash, fullName.trim());
    }

    public Map<String, Object> login(String loginId, String rawPassword, String ip, String userAgent) {
        String id = loginId == null ? "" : loginId.trim();
        String hash = PasswordUtil.hashPassword(rawPassword);
        Map<String, Object> user = userDAO.uc015_findActiveUserForLogin(id, hash);
        if (user != null) {
            int userId = ((Number) user.get("user_id")).intValue();
            userDAO.uc015_recordLoginAttempt(userId, id, ip, userAgent, true, null);
            return user;
        }
        userDAO.uc015_recordLoginAttempt(null, id, ip, userAgent, false, "Sai thong tin dang nhap hoac tai khoan bi khoa");
        userDAO.uc016_lockIfTooManyFails(id);
        return null;
    }

    public int updateProfile(int userId, String fullName, String phone, String avatarUrl) {
        return userDAO.uc017_updateProfile(userId, fullName, RequestUtil.normalizePhone(phone), avatarUrl);
    }

    public int changePassword(int userId, String oldPassword, String newPassword) {
        if (!PasswordUtil.isStrongPassword(newPassword)) {
            return -1;
        }
        return userDAO.uc018_changePassword(userId, PasswordUtil.hashPassword(oldPassword), PasswordUtil.hashPassword(newPassword));
    }

    public String createResetToken(String email) {
        String rawToken = UUID.randomUUID().toString().replace("-", "");
        int rows = userDAO.uc019_createPasswordResetToken(email, PasswordUtil.hashPassword(rawToken));
        return rows > 0 ? rawToken : null;
    }

    public boolean resetPassword(String rawToken, String newPassword) {
        if (!PasswordUtil.isStrongPassword(newPassword)) {
            return false;
        }
        return userDAO.uc020_resetPasswordByToken(PasswordUtil.hashPassword(rawToken), PasswordUtil.hashPassword(newPassword));
    }

    public int toggleUserStatus(int userId) {
        return userDAO.uc021_toggleUserStatus(userId);
    }

    public int deleteUser(int userId) {
        return userDAO.uc022_deleteUserIfUnused(userId);
    }

    public List<Map<String, Object>> listAddresses(int userId) {
        return userDAO.uc023_listAddresses(userId);
    }

    public int addAddress(int userId, String name, String phone, Integer provId, Integer wardId, String line, boolean def) {
        return userDAO.uc024_addAddress(userId, name, RequestUtil.normalizePhone(phone), provId, wardId, line, def);
    }

    public int setDefaultAddress(int userId, int addressId, String addressLine) {
        return userDAO.uc025_updateAndSetDefaultAddress(userId, addressId, addressLine);
    }

    public int deleteAddress(int addressId) {
        return userDAO.uc026_deleteAddress(addressId);
    }

    public List<Map<String, Object>> listAuditLogs(String fromDate, String toDate, String actionKeyword) {
        return userDAO.uc027_listAuditLogs(fromDate, toDate, actionKeyword);
    }
}
