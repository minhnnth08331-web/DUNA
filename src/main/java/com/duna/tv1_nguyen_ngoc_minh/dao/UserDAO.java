package com.duna.tv1_nguyen_ngoc_minh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M01 - Tai khoan nguoi dung, Xac thuc, Dia chi & Nhat ky (UC011 - UC027).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class UserDAO extends BaseDAO {

    // UC011: Xem danh sach tai khoan nguoi dung phan trang
    public List<Map<String, Object>> uc011_listUsersPaged(String keyword, int offset, int limit) {
        String sql = "SELECT u.user_id, ISNULL(e.employee_code, ISNULL(c.customer_code, '-')) AS linked_code, "
                + "e.employee_id AS linked_emp_id, c.customer_id AS linked_cust_id, "
                + "u.email, u.phone, u.full_name, r.role_code, ISNULL(mt.tier_name, r.role_name) AS linked_info, "
                + "u.status, u.last_login_at "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "LEFT JOIN customers c ON c.user_id = u.user_id "
                + "LEFT JOIN membership_tiers mt ON c.tier_id = mt.tier_id "
                + "LEFT JOIN employees e ON e.user_id = u.user_id "
                + "WHERE u.full_name LIKE ? OR u.email LIKE ? OR ISNULL(c.customer_code, '') LIKE ? OR ISNULL(e.employee_code, '') LIKE ? "
                + "ORDER BY u.user_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, pat, pat, Math.max(0, offset), Math.max(1, limit));
    }

    // UC012: Tim kiem tai khoan theo email, so dien thoai hoac ho ten
    public List<Map<String, Object>> uc012_searchUsers(String keyword) {
        String sql = "SELECT u.user_id, ISNULL(e.employee_code, ISNULL(c.customer_code, '-')) AS linked_code, "
                + "e.employee_id AS linked_emp_id, c.customer_id AS linked_cust_id, "
                + "u.email, u.phone, u.full_name, r.role_code, ISNULL(mt.tier_name, r.role_name) AS linked_info, u.status "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "LEFT JOIN customers c ON c.user_id = u.user_id "
                + "LEFT JOIN membership_tiers mt ON c.tier_id = mt.tier_id "
                + "LEFT JOIN employees e ON e.user_id = u.user_id "
                + "WHERE u.email LIKE ? OR ISNULL(u.phone, '') LIKE ? OR u.full_name LIKE ? OR ISNULL(c.customer_code, '') LIKE ? "
                + "ORDER BY u.user_id DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, pat, pat);
    }

    // UC013: Loc tai khoan theo vai tro va trang thai hoat dong
    public List<Map<String, Object>> uc013_filterUsersByRoleAndStatus(String roleCode, String status, String keyword) {
        return uc013_filterUsersByRoleAndStatusPaged(roleCode, status, keyword, 0, 200);
    }

    public List<Map<String, Object>> uc013_filterUsersByRoleAndStatusPaged(String roleCode, String status, String keyword, int offset, int limit) {
        String sql = "SELECT u.user_id, ISNULL(e.employee_code, ISNULL(c.customer_code, '-')) AS linked_code, "
                + "e.employee_id AS linked_emp_id, c.customer_id AS linked_cust_id, "
                + "u.email, u.phone, u.full_name, r.role_code, ISNULL(mt.tier_name, r.role_name) AS linked_info, "
                + "u.status, CONVERT(VARCHAR(16), u.last_login_at, 120) AS last_login_at "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "LEFT JOIN customers c ON c.user_id = u.user_id "
                + "LEFT JOIN membership_tiers mt ON c.tier_id = mt.tier_id "
                + "LEFT JOIN employees e ON e.user_id = u.user_id "
                + "WHERE (? = '' OR r.role_code = ?) AND (? = '' OR u.status = ?) "
                + "AND (u.full_name LIKE ? OR u.email LIKE ? OR ISNULL(u.phone, '') LIKE ? OR ISNULL(c.customer_code, '') LIKE ? OR ISNULL(e.employee_code, '') LIKE ?) "
                + "ORDER BY u.user_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String r = roleCode == null ? "" : roleCode.trim();
        String s = status == null ? "" : status.trim();
        String pat = likePattern(keyword);
        return queryRows(sql, r, r, s, s, pat, pat, pat, pat, pat, Math.max(0, offset), Math.max(1, limit));
    }

    public int countUsersFiltered(String roleCode, String status, String keyword) {
        String sql = "SELECT COUNT(*) FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "LEFT JOIN customers c ON c.user_id = u.user_id "
                + "LEFT JOIN employees e ON e.user_id = u.user_id "
                + "WHERE (? = '' OR r.role_code = ?) AND (? = '' OR u.status = ?) "
                + "AND (u.full_name LIKE ? OR u.email LIKE ? OR ISNULL(u.phone, '') LIKE ? OR ISNULL(c.customer_code, '') LIKE ? OR ISNULL(e.employee_code, '') LIKE ?)";
        String r = roleCode == null ? "" : roleCode.trim();
        String s = status == null ? "" : status.trim();
        String pat = likePattern(keyword);
        return queryInt(sql, r, r, s, s, pat, pat, pat, pat, pat);
    }

    public Map<String, Object> getUserStats() {
        return queryFirstRow(
                "SELECT COUNT(*) AS totalUsers, "
                        + "SUM(CASE WHEN r.role_code IN ('ADMIN','MANAGER') THEN 1 ELSE 0 END) AS adminMgrCount, "
                        + "SUM(CASE WHEN r.role_code IN ('STAFF','EMPLOYEE') THEN 1 ELSE 0 END) AS staffCount, "
                        + "SUM(CASE WHEN r.role_code = 'CUSTOMER' THEN 1 ELSE 0 END) AS customerCount, "
                        + "SUM(CASE WHEN u.status <> 'ACTIVE' THEN 1 ELSE 0 END) AS lockedCount "
                        + "FROM users u JOIN roles r ON u.role_id = r.role_id");
    }

    // UC014: Dang ky tai khoan khach hang moi va tu tao ho so khach hang
    public long uc014_registerCustomerUser(String email, String phone, String passwordHash, String fullName) {
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ? OR (phone IS NOT NULL AND phone = ?)";
        if (queryInt(checkSql, email, phone) > 0) {
            return -1L;
        }
        String insertUserSql = "INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified) "
                + "SELECT ?, ?, ?, ?, role_id, 'ACTIVE', 1 FROM roles WHERE role_code = 'CUSTOMER'";
        long newUserId = executeInsertGetId(insertUserSql, email, phone, passwordHash, fullName);
        if (newUserId > 0) {
            String custCode = String.format("KH%06d", newUserId);
            String insertCustSql = "INSERT INTO customers (user_id, customer_code, full_name, phone, email, gender, tier_id) "
                    + "VALUES (?, ?, ?, ?, ?, 'MALE', (SELECT TOP 1 tier_id FROM membership_tiers ORDER BY min_spend ASC))";
            executeUpdate(insertCustSql, newUserId, custCode, fullName, phone, email);
        }
        return newUserId;
    }

    // UC015: Dang nhap bang email/so dien thoai va mat khau, ghi nhan lich su dang nhap
    public Map<String, Object> uc015_findActiveUserForLogin(String loginId, String passwordHash) {
        String sql = "SELECT TOP 1 u.user_id, u.email, u.phone, u.full_name, u.avatar_url, u.status, r.role_id, r.role_code, r.role_name "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE (u.email = ? OR u.phone = ?) AND u.password_hash = ? AND u.status = 'ACTIVE'";
        return queryFirstRow(sql, loginId, loginId, passwordHash);
    }

    // UC015 (tiep): Cap nhat thoi gian dang nhap cuoi va ghi lich su
    public void uc015_recordLoginAttempt(Integer userId, String emailAttempt, String ip, String deviceInfo,
                                         boolean success, String failReason) {
        if (success && userId != null) {
            executeUpdate("UPDATE users SET last_login_at = GETDATE() WHERE user_id = ?", userId);
        }
        String logSql = "INSERT INTO login_histories (user_id, email_attempt, ip_address, device_info, is_success, fail_reason) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        executeUpdate(logSql, userId, emailAttempt, ip, deviceInfo, success ? 1 : 0, failReason);
    }

    // UC016: Tu dong khoa tai khoan khi dang nhap sai qua 5 lan lien tiep
    public boolean uc016_lockIfTooManyFails(String emailAttempt) {
        String countSql = "SELECT COUNT(*) FROM login_histories "
                + "WHERE email_attempt = ? AND is_success = 0 AND login_at >= DATEADD(MINUTE, -15, GETDATE())";
        int fails = queryInt(countSql, emailAttempt);
        if (fails >= 5) {
            executeUpdate("UPDATE users SET status = 'LOCKED', updated_at = GETDATE() WHERE email = ? OR phone = ?",
                    emailAttempt, emailAttempt);
            return true;
        }
        return false;
    }

    // UC017: Cap nhat ho so ca nhan va anh dai dien
    public int uc017_updateProfile(int userId, String fullName, String phone, String avatarUrl) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, avatar_url = ?, updated_at = GETDATE() WHERE user_id = ?";
        return executeUpdate(sql, fullName, phone, avatarUrl, userId);
    }

    // UC018: Doi mat khau tai khoan sau khi xac thuc mat khau cu
    public int uc018_changePassword(int userId, String oldHash, String newHash) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = GETDATE() WHERE user_id = ? AND password_hash = ?";
        return executeUpdate(sql, newHash, userId, oldHash);
    }

    // UC019: Yeu cau quen mat khau va tao token dat lai
    public int uc019_createPasswordResetToken(String email, String tokenHash) {
        Map<String, Object> user = queryFirstRow("SELECT user_id FROM users WHERE email = ?", email);
        if (user == null) {
            return 0;
        }
        int userId = ((Number) user.get("user_id")).intValue();
        String sql = "INSERT INTO password_reset_tokens (user_id, token_hash, expires_at) "
                + "VALUES (?, ?, DATEADD(MINUTE, 30, GETDATE()))";
        return executeUpdate(sql, userId, tokenHash);
    }

    // UC020: Dat lai mat khau bang token con han su dung
    public boolean uc020_resetPasswordByToken(String tokenHash, String newHash) {
        Map<String, Object> tok = queryFirstRow(
                "SELECT token_id, user_id FROM password_reset_tokens WHERE token_hash = ? AND used_at IS NULL AND expires_at > GETDATE()",
                tokenHash);
        if (tok == null) {
            return false;
        }
        int userId = ((Number) tok.get("user_id")).intValue();
        executeUpdate("UPDATE users SET password_hash = ?, updated_at = GETDATE() WHERE user_id = ?", newHash, userId);
        executeUpdate("UPDATE password_reset_tokens SET used_at = GETDATE() WHERE token_hash = ?", tokenHash);
        return true;
    }

    // UC021: Khoa hoac mo khoa tai khoan nguoi dung (dong bo sang customers & employees)
    public int uc021_toggleUserStatus(int userId) {
        String sql = "UPDATE users SET status = CASE WHEN status = 'ACTIVE' THEN 'LOCKED' ELSE 'ACTIVE' END, "
                + "updated_at = GETDATE() WHERE user_id = ?";
        int res = executeUpdate(sql, userId);
        if (res > 0) {
            executeUpdate("UPDATE customers SET is_active = CASE WHEN (SELECT status FROM users WHERE user_id = ?) = 'ACTIVE' THEN 1 ELSE 0 END, "
                    + "updated_at = GETDATE() WHERE user_id = ?", userId, userId);
            executeUpdate("UPDATE employees SET status = CASE WHEN (SELECT status FROM users WHERE user_id = ?) = 'ACTIVE' THEN 'WORKING' ELSE 'RESIGNED' END, "
                    + "updated_at = GETDATE() WHERE user_id = ?", userId, userId);
        }
        return res;
    }

    // UC015-UC016: Xem lich su dang nhap & canh bao sai mat khau
    public List<Map<String, Object>> uc015_listLoginHistories(String keyword, boolean onlyFailed) {
        String sql = "SELECT TOP 100 lh.login_id, lh.user_id, ISNULL(u.full_name, N'Khách / Chưa xác định') AS full_name, "
                + "lh.email_attempt, lh.ip_address, LEFT(ISNULL(lh.device_info, ''), 60) AS device_info, "
                + "CASE WHEN lh.is_success = 1 THEN N'Thành công' ELSE N'Thất bại' END AS status, "
                + "ISNULL(lh.fail_reason, N'-') AS fail_reason, CONVERT(VARCHAR(19), lh.login_at, 120) AS created_at "
                + "FROM login_histories lh LEFT JOIN users u ON lh.user_id = u.user_id "
                + "WHERE (lh.email_attempt LIKE ? OR ISNULL(u.full_name, '') LIKE ?) "
                + (onlyFailed ? "AND lh.is_success = 0 " : "")
                + "ORDER BY lh.login_id DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC017 (Admin/Self): Cap nhat ho so tai khoan va dong bo sang customers / employees
    public int uc017_updateProfileAndSync(int userId, String fullName, String phone, String email) {
        int res = executeUpdate(
                "UPDATE users SET full_name = ?, phone = NULLIF(?, ''), "
                        + "email = CASE WHEN ? <> '' THEN ? ELSE email END, updated_at = GETDATE() WHERE user_id = ?",
                fullName, phone == null ? "" : phone, email == null ? "" : email, email == null ? "" : email, userId);
        if (res > 0) {
            executeUpdate("UPDATE customers SET full_name = ?, phone = NULLIF(?, ''), "
                    + "email = CASE WHEN ? <> '' THEN ? ELSE email END, updated_at = GETDATE() WHERE user_id = ?",
                    fullName, phone == null ? "" : phone, email == null ? "" : email, email == null ? "" : email, userId);
            executeUpdate("UPDATE employees SET full_name = ?, phone = NULLIF(?, ''), "
                    + "email = CASE WHEN ? <> '' THEN ? ELSE email END, updated_at = GETDATE() WHERE user_id = ?",
                    fullName, phone == null ? "" : phone, email == null ? "" : email, email == null ? "" : email, userId);
        }
        return res;
    }

    public int adminResetPassword(int userId, String newPasswordHash, int actorUserId) {
        int res = executeUpdate("UPDATE users SET password_hash = ?, updated_at = GETDATE() WHERE user_id = ?",
                newPasswordHash, userId);
        if (res > 0) {
            executeUpdate("INSERT INTO audit_logs (user_id, action, entity_name, entity_id, new_value) "
                    + "VALUES (?, 'RESET_PASSWORD', 'users', ?, N'Quản trị đặt lại mật khẩu')",
                    actorUserId > 0 ? actorUserId : null, String.valueOf(userId));
        }
        return res;
    }

    // UC022: Xoa tai khoan chua phat sinh don hang
    public int uc022_deleteUserIfUnused(int userId) {
        int usedInOrders = queryInt(
                "SELECT (SELECT COUNT(*) FROM orders WHERE user_id = ? OR handled_by = ?) "
                        + "+ (SELECT COUNT(*) FROM orders o JOIN customers c ON o.customer_id = c.customer_id WHERE c.user_id = ?) "
                        + "+ (SELECT COUNT(*) FROM employees WHERE user_id = ?)",
                userId, userId, userId, userId);
        if (usedInOrders > 0) {
            return 0;
        }
        executeUpdate("DELETE la FROM loyalty_accounts la JOIN customers c ON la.customer_id = c.customer_id WHERE c.user_id = ?", userId);
        executeUpdate("DELETE FROM customers WHERE user_id = ?", userId);
        executeUpdate("DELETE FROM user_addresses WHERE user_id = ?", userId);
        executeUpdate("DELETE FROM login_histories WHERE user_id = ?", userId);
        executeUpdate("DELETE FROM password_reset_tokens WHERE user_id = ?", userId);
        executeUpdate("UPDATE audit_logs SET user_id = NULL WHERE user_id = ?", userId);
        return executeUpdate("DELETE FROM users WHERE user_id = ?", userId);
    }

    // UC023: Xem so dia chi nhan hang cua nguoi dung (userId <= 0 xem tat ca gan day)
    public List<Map<String, Object>> uc023_listAddresses(int userId) {
        String sql = "SELECT TOP 100 a.address_id, a.user_id, u.full_name, a.receiver_name, a.receiver_phone, a.address_line, "
                + "w.ward_name, p.province_name, CASE WHEN a.is_default = 1 THEN N'Mặc định' ELSE N'Phụ' END AS is_default "
                + "FROM user_addresses a LEFT JOIN users u ON a.user_id = u.user_id "
                + "LEFT JOIN wards w ON a.ward_id = w.ward_id "
                + "LEFT JOIN provinces p ON a.province_id = p.province_id "
                + (userId > 0 ? "WHERE a.user_id = ? " : "")
                + "ORDER BY a.is_default DESC, a.address_id DESC";
        return userId > 0 ? queryRows(sql, userId) : queryRows(sql);
    }

    public List<Map<String, Object>> listAllProvinces() {
        return queryRows("SELECT province_id, province_name FROM provinces ORDER BY province_name");
    }

    public List<Map<String, Object>> listWardsByProvince(int provinceId) {
        if (provinceId > 0) {
            return queryRows("SELECT ward_id, province_id, ward_name FROM wards WHERE province_id = ? ORDER BY ward_name", provinceId);
        }
        return queryRows("SELECT TOP 200 ward_id, province_id, ward_name FROM wards ORDER BY province_id, ward_name");
    }

    // UC024: Them moi dia chi nhan hang theo tinh va xa/phuong
    public int uc024_addAddress(int userId, String receiverName, String receiverPhone,
                              Integer provinceId, Integer wardId, String addressLine, boolean isDefault) {
        if (isDefault) {
            executeUpdate("UPDATE user_addresses SET is_default = 0 WHERE user_id = ?", userId);
        }
        String sql = "INSERT INTO user_addresses (user_id, receiver_name, receiver_phone, province_id, ward_id, address_line, is_default) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, userId, receiverName, receiverPhone, provinceId, wardId, addressLine, isDefault ? 1 : 0);
    }

    // UC025: Cap nhat dia chi va dat dia chi nhan hang mac dinh
    public int uc025_updateAndSetDefaultAddress(int userId, int addressId, String addressLine) {
        executeUpdate("UPDATE user_addresses SET is_default = 0 WHERE user_id = ?", userId);
        String sql = "UPDATE user_addresses SET is_default = 1, "
                + "address_line = CASE WHEN ? <> '' THEN ? ELSE address_line END WHERE address_id = ? AND user_id = ?";
        String line = addressLine == null ? "" : addressLine.trim();
        return executeUpdate(sql, line, line, addressId, userId);
    }

    // UC026: Xoa dia chi nhan hang khoi so dia chi
    public int uc026_deleteAddress(int addressId) {
        String sql = "DELETE FROM user_addresses WHERE address_id = ? AND is_default = 0";
        return executeUpdate(sql, addressId);
    }

    // UC027: Xem nhat ky thao tac he thong theo nguoi dung va khoang thoi gian
    public List<Map<String, Object>> uc027_listAuditLogs(String fromDate, String toDate, String actionKeyword) {
        String fd = (fromDate == null || fromDate.trim().isEmpty()) ? "2026-01-01" : fromDate.trim();
        String td = (toDate == null || toDate.trim().isEmpty()) ? "2027-12-31" : toDate.trim();
        String sql = "SELECT TOP 150 a.audit_id, ISNULL(u.full_name, N'Hệ thống') AS full_name, "
                + "a.action, a.entity_name, a.entity_id, LEFT(ISNULL(a.new_value, ''), 80) AS new_value, "
                + "ISNULL(a.ip_address, '-') AS ip_address, CONVERT(VARCHAR(19), a.created_at, 120) AS created_at "
                + "FROM audit_logs a LEFT JOIN users u ON a.user_id = u.user_id "
                + "WHERE a.created_at >= CAST(? AS DATE) AND a.created_at < DATEADD(DAY, 1, CAST(? AS DATE)) "
                + "AND (a.action LIKE ? OR ISNULL(u.full_name, '') LIKE ? OR a.entity_name LIKE ?) "
                + "ORDER BY a.audit_id DESC";
        String pat = likePattern(actionKeyword);
        return queryRows(sql, fd, td, pat, pat, pat);
    }

    public Integer findEmployeeIdByUserId(int userId) {
        return queryOne("SELECT TOP 1 employee_id FROM employees WHERE user_id = ?", rs -> rs.getInt(1), userId);
    }
}
