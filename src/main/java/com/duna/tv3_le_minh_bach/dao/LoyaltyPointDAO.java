package com.duna.tv3_le_minh_bach.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M10 - Tai khoan tich diem, Giao dich diem & Danh sach qua (UC211 - UC218).
 * Tac gia: Le Minh Bach - TH08442
 */
public class LoyaltyPointDAO extends BaseDAO {

    // UC211: Tao tai khoan tich diem cho khach hang moi
    public int uc211_createLoyaltyAccount(int customerId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM loyalty_accounts WHERE customer_id = ?) INSERT INTO loyalty_accounts (customer_id) VALUES (?)";
        return executeUpdate(sql, customerId, customerId);
    }

    // UC212: Xem so du diem va hang thanh vien cua khach hang
    public Map<String, Object> uc212_getLoyaltyBalance(int customerId) {
        String sql = "SELECT c.customer_id, c.full_name, la.account_id, la.points_balance, la.lifetime_points, t.tier_name "
                + "FROM loyalty_accounts la JOIN customers c ON la.customer_id = c.customer_id "
                + "LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id WHERE c.customer_id = ?";
        return queryFirstRow(sql, customerId);
    }

    // UC213: Cong diem thuong khi don hang hoan tat
    public int uc213_earnPoints(int accountId, long orderId, int points, String description) {
        executeUpdate("INSERT INTO loyalty_transactions (account_id, order_id, points, txn_type, description, expires_at) "
                + "VALUES (?, ?, ?, 'EARN', ?, DATEADD(MONTH, 12, GETDATE()))", accountId, orderId, points, description);
        return executeUpdate("UPDATE loyalty_accounts SET points_balance = points_balance + ?, lifetime_points = lifetime_points + ?, "
                + "updated_at = GETDATE() WHERE account_id = ?", points, points, accountId);
    }

    // UC214: Tru diem khi khach dung diem de thanh toan don hang
    public int uc214_redeemPointsForOrder(int accountId, long orderId, int points, String description) {
        int updated = executeUpdate("UPDATE loyalty_accounts SET points_balance = points_balance - ?, updated_at = GETDATE() "
                + "WHERE account_id = ? AND points_balance >= ?", points, accountId, points);
        if (updated > 0) {
            executeUpdate("INSERT INTO loyalty_transactions (account_id, order_id, points, txn_type, description) "
                    + "VALUES (?, ?, ?, 'REDEEM', ?)", accountId, orderId, -points, description);
        }
        return updated;
    }

    // UC215: Dieu chinh diem thu cong kem ly do
    public int uc215_adjustPoints(int accountId, int deltaPoints, String reason) {
        executeUpdate("UPDATE loyalty_accounts SET points_balance = points_balance + ?, updated_at = GETDATE() WHERE account_id = ?",
                deltaPoints, accountId);
        return executeUpdate("INSERT INTO loyalty_transactions (account_id, points, txn_type, description) VALUES (?, ?, 'ADJUST', ?)",
                accountId, deltaPoints, reason);
    }

    // UC216: Xem lich su giao dich diem thuong cua khach hang
    public List<Map<String, Object>> uc216_listLoyaltyTransactions(int customerId, String txnTypeFilter) {
        String sql = "SELECT TOP 100 lt.loyalty_txn_id, lt.txn_type, lt.points, lt.description, lt.expires_at, lt.created_at "
                + "FROM loyalty_transactions lt JOIN loyalty_accounts la ON lt.account_id = la.account_id "
                + "WHERE (? <= 0 OR la.customer_id = ?) AND lt.txn_type LIKE ? ORDER BY lt.created_at DESC";
        return queryRows(sql, customerId, customerId, likePattern(txnTypeFilter));
    }

    // UC217: Tu dong het han diem thuong qua 12 thang (khong tru lap va dong bo points_balance)
    public int uc217_expireOldPoints() {
        List<Map<String, Object>> due = queryRows(
                "SELECT la.account_id, la.points_balance, "
                        + "ISNULL(SUM(CASE WHEN lt.txn_type = 'EARN' AND lt.expires_at < GETDATE() THEN lt.points ELSE 0 END), 0) AS overdue_earned, "
                        + "ISNULL(SUM(CASE WHEN lt.txn_type = 'EXPIRE' THEN ABS(lt.points) ELSE 0 END), 0) AS already_expired "
                        + "FROM loyalty_accounts la "
                        + "JOIN loyalty_transactions lt ON la.account_id = lt.account_id "
                        + "GROUP BY la.account_id, la.points_balance");
        int affected = 0;
        for (Map<String, Object> row : due) {
            int accId = ((Number) row.get("account_id")).intValue();
            int balance = ((Number) row.get("points_balance")).intValue();
            int overdueEarned = ((Number) row.get("overdue_earned")).intValue();
            int alreadyExpired = ((Number) row.get("already_expired")).intValue();
            int toExpire = Math.min(Math.max(0, balance), Math.max(0, overdueEarned - alreadyExpired));
            if (toExpire > 0) {
                executeUpdate("INSERT INTO loyalty_transactions (account_id, points, txn_type, description) "
                        + "VALUES (?, ?, 'EXPIRE', N'Điểm hết hạn sau 12 tháng')", accId, -toExpire);
                affected += executeUpdate("UPDATE loyalty_accounts SET points_balance = CASE WHEN points_balance >= ? THEN points_balance - ? ELSE 0 END, "
                        + "updated_at = GETDATE() WHERE account_id = ?", toExpire, toExpire, accId);
            }
        }
        return affected;
    }

    // UC218: Xem danh sach qua doi diem dang ap dung
    public List<Map<String, Object>> uc218_listRewards(String keyword) {
        String sql = "SELECT r.reward_id, r.reward_name, r.points_required, r.stock_quantity, v.voucher_code, r.is_active "
                + "FROM loyalty_rewards r LEFT JOIN vouchers v ON r.voucher_id = v.voucher_id "
                + "WHERE r.is_active = 1 AND r.reward_name LIKE ? ORDER BY r.points_required ASC";
        return queryRows(sql, likePattern(keyword));
    }
}
