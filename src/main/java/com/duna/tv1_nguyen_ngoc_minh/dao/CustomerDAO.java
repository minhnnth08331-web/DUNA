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

    // UC031: Them moi khach hang vang lai mua tai quay
    public int uc031_addWalkInCustomer(String code, String fullName, String phone, String gender) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM customers WHERE phone = ?) "
                + "INSERT INTO customers (customer_code, full_name, phone, gender, tier_id) "
                + "VALUES (?, ?, ?, ?, (SELECT TOP 1 tier_id FROM membership_tiers ORDER BY min_spend ASC))";
        return executeUpdate(sql, phone, code, fullName, phone, gender == null ? "MALE" : gender);
    }

    // UC032: Cap nhat thong tin lien he va ngay sinh khach hang
    public int uc032_updateCustomer(int customerId, String phone, String email, String birthDate) {
        String sql = "UPDATE customers SET phone = ?, email = ?, birth_date = NULLIF(?, ''), updated_at = GETDATE() WHERE customer_id = ?";
        return executeUpdate(sql, phone, email, birthDate == null ? "" : birthDate, customerId);
    }

    // UC033: Vo hieu hoa ho so khach hang vi pham chinh sach
    public int uc033_toggleCustomerActive(int customerId, boolean active) {
        String sql = "UPDATE customers SET is_active = ?, updated_at = GETDATE() WHERE customer_id = ?";
        return executeUpdate(sql, active ? 1 : 0, customerId);
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
}
