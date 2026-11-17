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
        String sql = "SELECT u.user_id, u.email, u.phone, u.full_name, r.role_code, r.role_name, u.status, u.last_login_at "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.full_name LIKE ? OR u.email LIKE ? "
                + "ORDER BY u.user_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, Math.max(0, offset), Math.max(1, limit));
    }

    // UC012: Tim kiem tai khoan theo email, so dien thoai hoac ho ten
    public List<Map<String, Object>> uc012_searchUsers(String keyword) {
        String sql = "SELECT u.user_id, u.email, u.phone, u.full_name, r.role_code, r.role_name, u.status "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.email LIKE ? OR u.phone LIKE ? OR u.full_name LIKE ? ORDER BY u.user_id DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, pat);
    }

    // UC013: Loc tai khoan theo vai tro va trang thai hoat dong
    public List<Map<String, Object>> uc013_filterUsersByRoleAndStatus(String roleCode, String status, String keyword) {
        String sql = "SELECT u.user_id, u.email, u.phone, u.full_name, r.role_code, r.role_name, u.status "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE (? = '' OR r.role_code = ?) AND (? = '' OR u.status = ?) AND u.full_name LIKE ? "
                + "ORDER BY u.user_id DESC";
        String r = roleCode == null ? "" : roleCode.trim();
        String s = status == null ? "" : status.trim();
        return queryRows(sql, r, r, s, s, likePattern(keyword));
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

    // UC021: Khoa hoac mo khoa tai khoan nguoi dung
    public int uc021_toggleUserStatus(int userId) {
        String sql = "UPDATE users SET status = CASE WHEN status = 'ACTIVE' THEN 'LOCKED' ELSE 'ACTIVE' END, "
                + "updated_at = GETDATE() WHERE user_id = ?";
        return executeUpdate(sql, userId);
    }

    // UC022: Xoa tai khoan chua phat sinh don hang
    public int uc022_deleteUserIfUnused(int userId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM orders WHERE user_id = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM customers WHERE user_id = ?) "
                + "DELETE FROM users WHERE user_id = ?";
        return executeUpdate(sql, userId, userId, userId);
    }

    // UC023: Xem so dia chi nhan hang cua nguoi dung
    public List<Map<String, Object>> uc023_listAddresses(int userId) {
        String sql = "SELECT a.address_id, a.receiver_name, a.receiver_phone, a.address_line, "
                + "w.ward_name, p.province_name, a.is_default "
                + "FROM user_addresses a LEFT JOIN wards w ON a.ward_id = w.ward_id "
                + "LEFT JOIN provinces p ON a.province_id = p.province_id "
                + "WHERE a.user_id = ? ORDER BY a.is_default DESC, a.address_id DESC";
        return queryRows(sql, userId);
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
        String sql = "UPDATE user_addresses SET is_default = 1, address_line = ? WHERE address_id = ? AND user_id = ?";
        return executeUpdate(sql, addressLine, addressId, userId);
    }

    // UC026: Xoa dia chi nhan hang khoi so dia chi
    public int uc026_deleteAddress(int addressId) {
        String sql = "DELETE FROM user_addresses WHERE address_id = ? AND is_default = 0";
        return executeUpdate(sql, addressId);
    }

    // UC027: Xem nhat ky thao tac he thong theo nguoi dung va khoang thoi gian
    public List<Map<String, Object>> uc027_listAuditLogs(String fromDate, String toDate, String actionKeyword) {
        String sql = "SELECT TOP 100 a.audit_id, u.full_name, a.action, a.entity_name, a.entity_id, a.ip_address, a.created_at "
                + "FROM audit_logs a LEFT JOIN users u ON a.user_id = u.user_id "
                + "WHERE a.created_at BETWEEN ? AND ? AND a.action LIKE ? ORDER BY a.audit_id DESC";
        return queryRows(sql, fromDate, toDate, likePattern(actionKeyword));
    }
}
