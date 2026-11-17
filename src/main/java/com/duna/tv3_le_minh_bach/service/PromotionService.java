package com.duna.tv3_le_minh_bach.service;

import com.duna.tv3_le_minh_bach.dao.PromotionVoucherDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M09 - Voucher, Khuyen mai & Flash Sale (UC191 - UC210).
 * Tac gia: Le Minh Bach - TH08442
 */
public class PromotionService {
    private final PromotionVoucherDAO promoDAO = new PromotionVoucherDAO();

    public List<Map<String, Object>> listVouchers(String kw) {
        return promoDAO.uc191_listVouchers(kw);
    }

    public int createVoucher(String code, String name, String type, double val, double maxD, double minOrd, int limit, int perUser, String start, String end) {
        return promoDAO.uc192_createVoucher(code.toUpperCase(), name, type, val, maxD, minOrd, limit, perUser, start, end);
    }

    public int updateVoucher(int id, double minOrd, String end, int limit) {
        return promoDAO.uc193_updateVoucher(id, minOrd, end, limit);
    }

    public int toggleVoucher(int id) {
        return promoDAO.uc194_toggleVoucher(id);
    }

    public int deleteVoucher(int id) {
        return promoDAO.uc195_deleteVoucherIfUnused(id);
    }

    public Map<String, Object> validateVoucher(String code, double orderAmount, int userId) {
        return promoDAO.uc196_validateVoucher(code.toUpperCase(), orderAmount, userId);
    }

    public int recordVoucherUsage(int voucherId, int userId, long orderId, double discount) {
        return promoDAO.uc197_recordVoucherUsage(voucherId, userId, orderId, discount);
    }

    public List<Map<String, Object>> getVoucherAnalytics() {
        return promoDAO.uc198_getVoucherAnalytics();
    }

    public List<Map<String, Object>> listPromotions(String kw) {
        return promoDAO.uc199_listActivePromotions(kw);
    }

    public int createPromotion(String name, String type, double val, String start, String end, String banner) {
        return promoDAO.uc200_createPromotion(name, type, val, start, end, banner);
    }

    public int assignProduct(int promoId, int productId) {
        return promoDAO.uc201_assignProductToPromotion(promoId, productId);
    }

    public int removeProduct(int promoId, int productId) {
        return promoDAO.uc202_removeProductFromPromotion(promoId, productId);
    }

    public Map<String, Object> getPromoPrice(int productId) {
        return promoDAO.uc203_getProductPromoPrice(productId);
    }

    public int endPromotionEarly(int promoId) {
        return promoDAO.uc204_endPromotionEarly(promoId);
    }

    public List<Map<String, Object>> listFlashSales(String kw) {
        return promoDAO.uc205_listFlashSales(kw);
    }

    public int createFlashSale(String title, String start, String end) {
        return promoDAO.uc206_createFlashSale(title, start, end);
    }

    public int addFlashSaleItem(int fsId, int varId, double price, int limit) {
        return promoDAO.uc207_addFlashSaleItem(fsId, varId, price, limit);
    }

    public int syncFlashSaleStatuses() {
        return promoDAO.uc208_syncFlashSaleStatuses();
    }

    public int incrementFlashSaleSold(int flashItemId) {
        return promoDAO.uc209_incrementFlashSaleSold(flashItemId);
    }

    public List<Map<String, Object>> listRunningFlashItems() {
        return promoDAO.uc210_listRunningFlashSaleItems();
    }
}
