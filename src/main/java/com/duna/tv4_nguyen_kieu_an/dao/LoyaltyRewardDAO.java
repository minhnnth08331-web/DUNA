package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

/**
 * DAO phan he M10 - Them qua doi diem & Thuc hien doi qua (UC219 - UC220).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class LoyaltyRewardDAO extends BaseDAO {

    // UC219: Them moi qua doi diem gan voi voucher
    public int uc219_addReward(String rewardName, int pointsRequired, Integer voucherId, int stockQuantity) {
        String sql = "INSERT INTO loyalty_rewards (reward_name, points_required, voucher_id, stock_quantity) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, rewardName, pointsRequired, voucherId, stockQuantity);
    }

    // UC220: Khach hang doi qua bang diem thuong
    public int uc220_redeemReward(int accountId, int rewardId, int pointsCost) {
        int updated = executeUpdate(
                "UPDATE loyalty_accounts SET points_balance = points_balance - ?, updated_at = GETDATE() "
                        + "WHERE account_id = ? AND points_balance >= ?",
                pointsCost, accountId, pointsCost);
        if (updated > 0) {
            executeUpdate("UPDATE loyalty_rewards SET stock_quantity = stock_quantity - 1 WHERE reward_id = ? AND stock_quantity > 0",
                    rewardId);
            executeUpdate("INSERT INTO reward_redemptions (account_id, reward_id, points_used) VALUES (?, ?, ?)",
                    accountId, rewardId, pointsCost);
        }
        return updated;
    }
}
