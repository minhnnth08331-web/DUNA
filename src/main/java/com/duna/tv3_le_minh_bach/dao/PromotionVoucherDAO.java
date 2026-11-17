package com.duna.tv3_le_minh_bach.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M09 - Voucher, Khuyen mai & Flash Sale (UC191 - UC210).
 * Tac gia: Le Minh Bach - TH08442
 */
public class PromotionVoucherDAO extends BaseDAO {

    // UC191: Xem danh sach voucher kem trang thai hieu luc va luot dung
    public List<Map<String, Object>> uc191_listVouchers(String keyword) {
        String sql = "SELECT voucher_id, voucher_code, voucher_name, discount_type, discount_value, "
                + "min_order_value, used_count, usage_limit, start_date, end_date, is_active "
                + "FROM vouchers WHERE voucher_code LIKE ? OR voucher_name LIKE ? ORDER BY end_date DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC192: Them moi voucher
    public int uc192_createVoucher(String code, String name, String type, double value,
                                   double maxDiscount, double minOrder, int usageLimit, int perUser,
                                   String startDate, String endDate) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM vouchers WHERE voucher_code = ?) "
                + "INSERT INTO vouchers (voucher_code, voucher_name, discount_type, discount_value, max_discount_amount, "
                + "min_order_value, usage_limit, per_user_limit, start_date, end_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, type, value, maxDiscount, minOrder, usageLimit, perUser, startDate, endDate);
    }

    // UC193: Cap nhat dieu kien ap dung va thoi gian hieu luc voucher
    public int uc193_updateVoucher(int voucherId, double minOrder, String endDate, int usageLimit) {
        return executeUpdate("UPDATE vouchers SET min_order_value = ?, end_date = ?, usage_limit = ? WHERE voucher_id = ?",
                minOrder, endDate, usageLimit, voucherId);
    }

    // UC194: Bat tat trang thai voucher
    public int uc194_toggleVoucher(int voucherId) {
        return executeUpdate("UPDATE vouchers SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END WHERE voucher_id = ?",
                voucherId);
    }

    // UC195: Xoa voucher chua co luot su dung
    public int uc195_deleteVoucherIfUnused(int voucherId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM voucher_usages WHERE voucher_id = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM orders WHERE voucher_id = ?) DELETE FROM vouchers WHERE voucher_id = ?";
        return executeUpdate(sql, voucherId, voucherId, voucherId);
    }

    // UC196: Kiem tra ma voucher hop le khi thanh toan
    public Map<String, Object> uc196_validateVoucher(String voucherCode, double orderAmount, int userId) {
        String sql = "SELECT TOP 1 v.voucher_id, v.voucher_code, v.discount_type, v.discount_value, v.max_discount_amount "
                + "FROM vouchers v WHERE v.voucher_code = ? AND v.is_active = 1 "
                + "AND GETDATE() BETWEEN v.start_date AND v.end_date AND ? >= v.min_order_value "
                + "AND (v.usage_limit IS NULL OR v.used_count < v.usage_limit) "
                + "AND (SELECT COUNT(*) FROM voucher_usages vu WHERE vu.voucher_id = v.voucher_id AND vu.user_id = ?) < v.per_user_limit";
        return queryFirstRow(sql, voucherCode, orderAmount, userId);
    }

    // UC197: Ghi nhan luot su dung voucher khi dat hang thanh cong
    public int uc197_recordVoucherUsage(int voucherId, int userId, long orderId, double discountAmount) {
        executeUpdate("INSERT INTO voucher_usages (voucher_id, user_id, order_id, discount_amount) VALUES (?, ?, ?, ?)",
                voucherId, userId, orderId, discountAmount);
        return executeUpdate("UPDATE vouchers SET used_count = used_count + 1 WHERE voucher_id = ?", voucherId);
    }

    // UC198: Thong ke hieu qua voucher theo so luot dung va tong tien giam
    public List<Map<String, Object>> uc198_getVoucherAnalytics() {
        String sql = "SELECT v.voucher_code, v.voucher_name, COUNT(vu.usage_id) AS total_uses, "
                + "ISNULL(SUM(vu.discount_amount), 0) AS total_discount, ISNULL(SUM(o.total_amount), 0) AS revenue_with_voucher "
                + "FROM vouchers v LEFT JOIN voucher_usages vu ON v.voucher_id = vu.voucher_id "
                + "LEFT JOIN orders o ON vu.order_id = o.order_id "
                + "GROUP BY v.voucher_code, v.voucher_name ORDER BY total_uses DESC";
        return queryRows(sql);
    }

    // UC199: Xem danh sach chuong trinh khuyen mai dang dien ra
    public List<Map<String, Object>> uc199_listActivePromotions(String keyword) {
        String sql = "SELECT promotion_id, promotion_name, promo_type, discount_value, start_date, end_date, is_active "
                + "FROM promotions WHERE promotion_name LIKE ? ORDER BY start_date DESC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC200: Them moi chuong trinh khuyen mai
    public int uc200_createPromotion(String name, String promoType, double discountValue,
                                     String startDate, String endDate, String bannerUrl) {
        return executeUpdate("INSERT INTO promotions (promotion_name, promo_type, discount_value, start_date, end_date, banner_url) "
                + "VALUES (?, ?, ?, ?, ?, ?)", name, promoType, discountValue, startDate, endDate, bannerUrl);
    }

    // UC201: Gan san pham vao chuong trinh khuyen mai
    public int uc201_assignProductToPromotion(int promotionId, int productId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id = ? AND product_id = ?) "
                + "INSERT INTO promotion_products (promotion_id, product_id) VALUES (?, ?)";
        return executeUpdate(sql, promotionId, productId, promotionId, productId);
    }

    // UC202: Go san pham khoi chuong trinh khuyen mai
    public int uc202_removeProductFromPromotion(int promotionId, int productId) {
        return executeUpdate("DELETE FROM promotion_products WHERE promotion_id = ? AND product_id = ?", promotionId, productId);
    }

    // UC203: Tinh gia ban sau khuyen mai cua san pham
    public Map<String, Object> uc203_getProductPromoPrice(int productId) {
        String sql = "SELECT TOP 1 p.product_id, p.product_name, p.base_price, "
                + "CASE WHEN pr.promo_type = 'PERCENT' THEN p.base_price * (100 - pr.discount_value) / 100 "
                + "WHEN pr.promo_type = 'FIXED' THEN p.base_price - pr.discount_value ELSE p.base_price END AS final_price "
                + "FROM products p LEFT JOIN promotion_products pp ON p.product_id = pp.product_id "
                + "LEFT JOIN promotions pr ON pp.promotion_id = pr.promotion_id AND pr.is_active = 1 AND GETDATE() BETWEEN pr.start_date AND pr.end_date "
                + "WHERE p.product_id = ?";
        return queryFirstRow(sql, productId);
    }

    // UC204: Ket thuc som chuong trinh khuyen mai
    public int uc204_endPromotionEarly(int promotionId) {
        return executeUpdate("UPDATE promotions SET end_date = GETDATE(), is_active = 0 WHERE promotion_id = ?", promotionId);
    }

    // UC205: Xem danh sach cac dot flash sale
    public List<Map<String, Object>> uc205_listFlashSales(String keyword) {
        String sql = "SELECT flash_sale_id, title, start_time, end_time, status FROM flash_sales "
                + "WHERE title LIKE ? ORDER BY start_time DESC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC206: Tao moi dot flash sale theo khung gio vang
    public int uc206_createFlashSale(String title, String startTime, String endTime) {
        return executeUpdate("INSERT INTO flash_sales (title, start_time, end_time) VALUES (?, ?, ?)", title, startTime, endTime);
    }

    // UC207: Them bien the san pham vao flash sale
    public int uc207_addFlashSaleItem(int flashSaleId, int variantId, double salePrice, int qtyLimit) {
        return executeUpdate("INSERT INTO flash_sale_items (flash_sale_id, variant_id, sale_price, quantity_limit) VALUES (?, ?, ?, ?)",
                flashSaleId, variantId, salePrice, qtyLimit);
    }

    // UC208: Tu dong cap nhat trang thai flash sale theo khung gio
    public int uc208_syncFlashSaleStatuses() {
        int a = executeUpdate("UPDATE flash_sales SET status = 'RUNNING' WHERE status = 'SCHEDULED' AND GETDATE() >= start_time AND GETDATE() < end_time");
        int b = executeUpdate("UPDATE flash_sales SET status = 'ENDED' WHERE status = 'RUNNING' AND GETDATE() >= end_time");
        return a + b;
    }

    // UC209: Ghi nhan ban hang flash sale va kiem tra gioi han so luong
    public int uc209_incrementFlashSaleSold(int flashItemId) {
        return executeUpdate("UPDATE flash_sale_items SET sold_quantity = sold_quantity + 1 WHERE flash_item_id = ? AND sold_quantity < quantity_limit",
                flashItemId);
    }

    // UC210: Xem san pham flash sale dang dien ra kem phan tram giam
    public List<Map<String, Object>> uc210_listRunningFlashSaleItems() {
        String sql = "SELECT p.product_name, c.color_name, s.size_code, f.sale_price, ISNULL(pv.price, p.base_price) AS orig_price, "
                + "(f.quantity_limit - f.sold_quantity) AS remaining "
                + "FROM flash_sales fs JOIN flash_sale_items f ON fs.flash_sale_id = f.flash_sale_id "
                + "JOIN product_variants pv ON f.variant_id = pv.variant_id "
                + "JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "WHERE fs.status = 'RUNNING'";
        return queryRows(sql);
    }
}
