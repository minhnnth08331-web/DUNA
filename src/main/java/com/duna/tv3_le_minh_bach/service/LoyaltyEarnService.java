package com.duna.tv3_le_minh_bach.service;

import com.duna.tv3_le_minh_bach.dao.LoyaltyPointDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M10 - Tich diem thuong & Danh sach qua (UC211 - UC218).
 * Tac gia: Le Minh Bach - TH08442
 */
public class LoyaltyEarnService {
    private final LoyaltyPointDAO loyaltyDAO = new LoyaltyPointDAO();

    public int createAccount(int customerId) {
        return loyaltyDAO.uc211_createLoyaltyAccount(customerId);
    }

    public Map<String, Object> getBalance(int customerId) {
        return loyaltyDAO.uc212_getLoyaltyBalance(customerId);
    }

    public int earnPoints(int accountId, long orderId, int points, String desc) {
        return loyaltyDAO.uc213_earnPoints(accountId, orderId, points, desc);
    }

    public int redeemPoints(int accountId, long orderId, int points, String desc) {
        return loyaltyDAO.uc214_redeemPointsForOrder(accountId, orderId, points, desc);
    }

    public int adjustPoints(int accountId, int delta, String reason) {
        return loyaltyDAO.uc215_adjustPoints(accountId, delta, reason);
    }

    public List<Map<String, Object>> listTransactions(int customerId, String typeFilter) {
        return loyaltyDAO.uc216_listLoyaltyTransactions(customerId, typeFilter);
    }

    public int expireOldPoints() {
        return loyaltyDAO.uc217_expireOldPoints();
    }

    public List<Map<String, Object>> listRewards(String kw) {
        return loyaltyDAO.uc218_listRewards(kw);
    }
}
