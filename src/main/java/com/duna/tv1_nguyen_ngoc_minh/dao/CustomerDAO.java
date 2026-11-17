package com.duna.tv1_nguyen_ngoc_minh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M02 - Khach hang, Hang thanh vien, Nhom & So do (UC028 - UC043).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class CustomerDAO extends BaseDAO {

    // UC028: Xem danh sach khach hang kem hang thanh vien
    public List<Map<String, Object>> uc028_listCustomersPaged(String keyword, int offset, int limit) {
        String sql = "SELECT c.customer_id, c.customer_code, c.full_name, c.phone, c.email, "
                + "t.tier_name, c.total_spent, c.total_orders, c.is_active "
                + "FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "WHERE c.full_name LIKE ? OR c.customer_code LIKE ? "
                + "ORDER BY c.customer_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, Math.max(0, offset), Math.max(1, limit));
    }

    // UC029: Tim kiem khach hang theo ma, so dien thoai hoac ho ten
    public List<Map<String, Object>> uc029_searchCustomers(String keyword) {
        String sql = "SELECT c.customer_id, c.customer_code, c.full_name, c.phone, c.email, t.tier_name, c.total_spent, c.total_orders "
                + "FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "WHERE c.customer_code LIKE ? OR c.phone LIKE ? OR c.full_name LIKE ? ORDER BY c.customer_id DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, pat);
    }

    // UC030: Xem ho so chi tiet khach hang va tong chi tieu tich luy
    public Map<String, Object> uc030_getCustomerDetail(int customerId) {
        String sql = "SELECT c.*, t.tier_name, t.discount_percent, g.group_name, ISNULL(la.points_balance, 0) AS points_balance "
                + "FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "LEFT JOIN customer_groups g ON c.group_id = g.group_id "
                + "LEFT JOIN loyalty_accounts la ON c.customer_id = la.customer_id WHERE c.customer_id = ?";
        return queryFirstRow(sql, customerId);
    }

    // UC031: Them moi khach hang vang lai mua tai quay (dong bo 1:1 tai khoan users + vi diem)
    public int uc031_addWalkInCustomer(String code, String fullName, String phone, String gender) {
        if (queryInt("SELECT COUNT(*) FROM customers WHERE phone = ?", phone) > 0) {
            return 0;
        }
        String genEmail = (code == null ? "kh" : code.trim().toLowerCase()) + "@duna-member.vn";
        String defaultHash = "a54126ab084dee02e0d353876d89bb04347688ca817ccaa2fcf52713f0cc6a61";
        Integer existingUserId = queryOne("SELECT TOP 1 user_id FROM users WHERE email = ? OR (phone IS NOT NULL AND phone = ?)",
                rs -> rs.getInt(1), genEmail, phone);
        long uid = existingUserId != null ? existingUserId.longValue() : executeInsertGetId(
                "INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified) "
                        + "SELECT ?, NULLIF(?, ''), ?, ?, role_id, 'ACTIVE', 1 FROM roles WHERE role_code = 'CUSTOMER'",
                genEmail, phone == null ? "" : phone, defaultHash, fullName);
        String sql = "INSERT INTO customers (user_id, customer_code, full_name, phone, email, gender, tier_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, (SELECT TOP 1 tier_id FROM membership_tiers ORDER BY min_spend ASC))";
        long newCustId = executeInsertGetId(sql, uid > 0 ? uid : null, code, fullName, phone, genEmail, gender == null ? "MALE" : gender);
        if (newCustId > 0) {
            executeUpdate("INSERT INTO loyalty_accounts (customer_id, points_balance, lifetime_points) VALUES (?, 0, 0)", newCustId);
            return 1;
        }
        return 0;
    }

    // UC032: Cap nhat thong tin lien he va ngay sinh khach hang (dong bo sang users)
    public int uc032_updateCustomer(int customerId, String phone, String email, String birthDate) {
        String sql = "UPDATE customers SET phone = ?, email = ?, birth_date = NULLIF(?, ''), updated_at = GETDATE() WHERE customer_id = ?";
        int res = executeUpdate(sql, phone, email, birthDate == null ? "" : birthDate, customerId);
        if (res > 0) {
            executeUpdate("UPDATE users SET phone = NULLIF(?, ''), email = CASE WHEN ? <> '' THEN ? ELSE email END, updated_at = GETDATE() "
                    + "WHERE user_id = (SELECT user_id FROM customers WHERE customer_id = ?)",
                    phone == null ? "" : phone, email == null ? "" : email, email == null ? "" : email, customerId);
        }
        return res;
    }

    // UC033: Vo hieu hoa ho so khach hang vi pham chinh sach (dong bo sang users)
    public int uc033_toggleCustomerActive(int customerId, boolean active) {
        int res = executeUpdate("UPDATE customers SET is_active = ?, updated_at = GETDATE() WHERE customer_id = ?",
                active ? 1 : 0, customerId);
        if (res > 0) {
            executeUpdate("UPDATE users SET status = ?, updated_at = GETDATE() WHERE user_id = (SELECT user_id FROM customers WHERE customer_id = ?)",
                    active ? "ACTIVE" : "LOCKED", customerId);
        }
        return res;
    }

    // UC034: Xoa ho so khach hang chua phat sinh don hang
    public int uc034_deleteCustomerIfNoOrders(int customerId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM orders WHERE customer_id = ?) DELETE FROM customers WHERE customer_id = ?";
        return executeUpdate(sql, customerId, customerId);
    }

    // UC035: Xem danh sach hang thanh vien va quyen loi uu dai
    public List<Map<String, Object>> uc035_listMembershipTiers(String keyword) {
        String sql = "SELECT tier_id, tier_name, min_spend, discount_percent, point_multiplier, benefits "
                + "FROM membership_tiers WHERE tier_name LIKE ? ORDER BY min_spend ASC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC036: Them moi hang thanh vien
    public int uc036_addMembershipTier(String tierName, double minSpend, double discountPercent, double pointMultiplier, String benefits) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM membership_tiers WHERE tier_name = ?) "
                + "INSERT INTO membership_tiers (tier_name, min_spend, discount_percent, point_multiplier, benefits) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql, tierName, tierName, minSpend, discountPercent, pointMultiplier, benefits);
    }

    // UC037: Cap nhat nguong chi tieu va muc giam gia cua hang thanh vien
    public int uc037_updateMembershipTier(int tierId, double minSpend, double discountPercent) {
        String sql = "UPDATE membership_tiers SET min_spend = ?, discount_percent = ? WHERE tier_id = ?";
        return executeUpdate(sql, minSpend, discountPercent, tierId);
    }

    // UC038: Tu dong nang hang thanh vien theo tong chi tieu tich luy
    public int uc038_autoUpgradeAllCustomerTiers() {
        String sql = "UPDATE customers SET tier_id = ("
                + "SELECT TOP 1 t.tier_id FROM membership_tiers t WHERE t.min_spend <= customers.total_spent ORDER BY t.min_spend DESC"
                + ") WHERE is_active = 1";
        return executeUpdate(sql);
    }

    // UC039: Xem danh sach nhom khach hang cham soc
    public List<Map<String, Object>> uc039_listCustomerGroups(String keyword) {
        String sql = "SELECT g.group_id, g.group_name, g.description, COUNT(c.customer_id) AS total_customers "
                + "FROM customer_groups g LEFT JOIN customers c ON g.group_id = c.group_id "
                + "WHERE g.group_name LIKE ? GROUP BY g.group_id, g.group_name, g.description";
        return queryRows(sql, likePattern(keyword));
    }

    // UC040: Them moi nhom khach hang
    public int uc040_addCustomerGroup(String groupName, String description) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM customer_groups WHERE group_name = ?) "
                + "INSERT INTO customer_groups (group_name, description) VALUES (?, ?)";
        return executeUpdate(sql, groupName, groupName, description);
    }

    // UC041: Gan khach hang vao nhom cham soc
    public int uc041_assignCustomerToGroup(int customerId, int groupId) {
        String sql = "UPDATE customers SET group_id = ?, updated_at = GETDATE() WHERE customer_id = ?";
        return executeUpdate(sql, groupId, customerId);
    }

    // UC042: Luu so do co the cua khach hang phuc vu tu van size
    public int uc042_saveMeasurement(int customerId, double heightCm, double weightKg,
                                     double chestCm, double waistCm, double hipCm, double shoulderCm) {
        String sql = "INSERT INTO customer_measurements (customer_id, height_cm, weight_kg, chest_cm, waist_cm, hip_cm, shoulder_cm) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, customerId, heightCm, weightKg, chestCm, waistCm, hipCm, shoulderCm);
    }

    // UC043: Xem so do gan nhat cua khach hang de goi y size
    public Map<String, Object> uc043_getLatestMeasurement(int customerId) {
        String sql = "SELECT TOP 1 height_cm, weight_kg, chest_cm, waist_cm, hip_cm, shoulder_cm, measured_at "
                + "FROM customer_measurements WHERE customer_id = ? ORDER BY measured_at DESC";
        return queryFirstRow(sql, customerId);
    }

    // --- Adapter cho giao dien quan-ly-khach-hang.jsp (DUNA_MensWear 100 bang) ---
    public List<Map<String, Object>> listClassicCustomers(String search, String rank, String status, int offset, int limit) {
        String kw = likePattern(search);
        String r = rank == null ? "" : rank.trim();
        String st = status == null ? "" : status.trim();
        String sql = "SELECT c.customer_id AS id, ISNULL(c.user_id, 0) AS userId, c.customer_code AS maKH, c.full_name AS hoTen, "
                + "ISNULL(c.phone, '') AS sdt, ISNULL(c.email, '') AS email, "
                + "CASE WHEN c.gender = 'FEMALE' THEN N'Nữ' ELSE N'Nam' END AS gioiTinh, "
                + "ISNULL(CONVERT(VARCHAR(10), c.birth_date, 120), '') AS ngaySinh, "
                + "ISNULL((SELECT TOP 1 ua.address_line FROM user_addresses ua WHERE ua.user_id = c.user_id ORDER BY ua.is_default DESC), N'Tại cửa hàng DUNA') AS diaChi, "
                + "ISNULL(t.tier_name, N'Thành Viên') AS hang, "
                + "CAST(ISNULL(t.discount_percent, 0) AS INT) AS discountPct, "
                + "ISNULL(la.points_balance, 0) AS pointsBalance, "
                + "CAST(c.total_spent AS BIGINT) AS tongChiTieu, "
                + "FORMAT(c.total_spent, 'N0') + N' đ' AS tongChiTieuFormatted, "
                + "ISNULL(c.total_orders, 0) AS orderCount, "
                + "ISNULL((SELECT SUM(oi.quantity) FROM orders o JOIN order_items oi ON oi.order_id = o.order_id "
                + "        WHERE o.customer_id = c.customer_id AND o.status IN ('COMPLETED', 'DELIVERED')), 0) AS tongSP, "
                + "CONVERT(VARCHAR(10), DATEADD(MONTH, 3, c.created_at), 103) AS hanReset, "
                + "DATEDIFF(DAY, GETDATE(), DATEADD(MONTH, 3, DATEADD(YEAR, 1, c.created_at))) AS daysLeft, "
                + "CASE WHEN c.is_active = 1 THEN 'active' ELSE 'locked' END AS trangThai "
                + "FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "LEFT JOIN loyalty_accounts la ON la.customer_id = c.customer_id "
                + "WHERE (c.customer_code LIKE ? OR c.full_name LIKE ? OR ISNULL(c.phone, '') LIKE ? OR ISNULL(c.email, '') LIKE ?) "
                + "AND (? = '' OR ? = 'ALL' OR t.tier_name LIKE ?) "
                + "AND (? = '' OR ? = 'ALL' OR ((? = '1' OR ? = 'active') AND c.is_active = 1) OR ((? = '0' OR ? = 'locked') AND c.is_active = 0)) "
                + "ORDER BY c.customer_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String rPat = r.isEmpty() ? "" : "%" + r + "%";
        return queryRows(sql, kw, kw, kw, kw, r, r, rPat, st, st, st, st, st, st, Math.max(0, offset), Math.max(1, limit));
    }

    public int countClassicCustomers(String search, String rank, String status) {
        String kw = likePattern(search);
        String r = rank == null ? "" : rank.trim();
        String st = status == null ? "" : status.trim();
        String rPat = r.isEmpty() ? "" : "%" + r + "%";
        String sql = "SELECT COUNT(*) FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "WHERE (c.customer_code LIKE ? OR c.full_name LIKE ? OR ISNULL(c.phone, '') LIKE ? OR ISNULL(c.email, '') LIKE ?) "
                + "AND (? = '' OR ? = 'ALL' OR t.tier_name LIKE ?) "
                + "AND (? = '' OR ? = 'ALL' OR ((? = '1' OR ? = 'active') AND c.is_active = 1) OR ((? = '0' OR ? = 'locked') AND c.is_active = 0))";
        return queryInt(sql, kw, kw, kw, kw, r, r, rPat, st, st, st, st, st, st);
    }

    public int updateClassicCustomerFull(int id, String hoTen, String sdt, String email, String gioiTinh, String birthDate) {
        String g = "Nữ".equalsIgnoreCase(gioiTinh) || "FEMALE".equalsIgnoreCase(gioiTinh) ? "FEMALE" : "MALE";
        int updated = executeUpdate("UPDATE customers SET full_name = ?, phone = ?, email = ?, gender = ?, birth_date = NULLIF(?, ''), updated_at = GETDATE() WHERE customer_id = ?",
                hoTen, sdt, email, g, birthDate == null ? "" : birthDate, id);
        if (updated > 0) {
            executeUpdate("UPDATE users SET full_name = ?, phone = NULLIF(?, ''), email = CASE WHEN ? <> '' THEN ? ELSE email END, updated_at = GETDATE() "
                    + "WHERE user_id = (SELECT user_id FROM customers WHERE customer_id = ?)",
                    hoTen, sdt == null ? "" : sdt, email == null ? "" : email, email == null ? "" : email, id);
        }
        return updated;
    }
}
