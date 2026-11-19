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

    // UC196: Kiem tra ma voucher hop le khi thanh toan (chi tiet tung ly do, dong bo 100% voi Quan ly Voucher)
    public Map<String, Object> validateVoucherDetailed(String voucherCode, double orderAmount, Integer customerUserId) {
        Map<String, Object> res = new java.util.LinkedHashMap<>();
        String code = voucherCode == null ? "" : voucherCode.trim().toUpperCase();
        if (code.isEmpty()) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("message", "Vui lòng nhập mã voucher!");
            return res;
        }
        String sql = "SELECT TOP 1 v.voucher_id, v.voucher_code, v.voucher_name, v.discount_type, "
                + "v.discount_value, ISNULL(v.max_discount_amount, 0) AS max_discount_amount, "
                + "ISNULL(v.min_order_value, 0) AS min_order_value, v.usage_limit, v.per_user_limit, "
                + "v.used_count, v.is_active, "
                + "CASE WHEN GETDATE() < v.start_date THEN 1 ELSE 0 END AS is_not_started, "
                + "CASE WHEN GETDATE() > v.end_date THEN 1 ELSE 0 END AS is_expired "
                + "FROM vouchers v WHERE UPPER(LTRIM(RTRIM(v.voucher_code))) = ?";
        Map<String, Object> v = queryFirstRow(sql, code);
        if (v == null) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("message", "Mã voucher \"" + code + "\" không tồn tại trong hệ thống!");
            return res;
        }

        int vid = ((Number) v.get("voucher_id")).intValue();
        String actualCode = String.valueOf(v.get("voucher_code"));
        String vName = v.get("voucher_name") != null ? String.valueOf(v.get("voucher_name")) : actualCode;
        boolean active = Boolean.TRUE.equals(v.get("is_active"))
                || (v.get("is_active") instanceof Number && ((Number) v.get("is_active")).intValue() == 1);
        int notStarted = v.get("is_not_started") instanceof Number ? ((Number) v.get("is_not_started")).intValue() : 0;
        int expired = v.get("is_expired") instanceof Number ? ((Number) v.get("is_expired")).intValue() : 0;
        int usedCount = v.get("used_count") instanceof Number ? ((Number) v.get("used_count")).intValue() : 0;
        Integer usageLimit = v.get("usage_limit") instanceof Number ? ((Number) v.get("usage_limit")).intValue() : null;
        Integer perUserLimit = v.get("per_user_limit") instanceof Number ? ((Number) v.get("per_user_limit")).intValue() : null;
        double minOrder = v.get("min_order_value") instanceof Number ? ((Number) v.get("min_order_value")).doubleValue() : 0;
        double val = v.get("discount_value") instanceof Number ? ((Number) v.get("discount_value")).doubleValue() : 0;
        double maxD = v.get("max_discount_amount") instanceof Number ? ((Number) v.get("max_discount_amount")).doubleValue() : 0;
        String type = v.get("discount_type") != null ? String.valueOf(v.get("discount_type")) : "FIXED";

        if (!active) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("message", "Mã voucher \"" + actualCode + "\" đang bị khóa / tạm dừng!");
            return res;
        }
        if (notStarted == 1) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("message", "Mã voucher \"" + actualCode + "\" chưa đến thời gian áp dụng!");
            return res;
        }
        if (expired == 1) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("message", "Mã voucher \"" + actualCode + "\" đã hết hạn sử dụng!");
            return res;
        }
        if (usageLimit != null && usedCount >= usageLimit) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("message", "Mã voucher \"" + actualCode + "\" đã hết lượt sử dụng (" + usedCount + "/" + usageLimit + ")!");
            return res;
        }
        if (orderAmount < minOrder) {
            res.put("valid", Boolean.FALSE);
            res.put("amount", 0L);
            res.put("discountVoucher", 0L);
            res.put("minOrder", Math.round(minOrder));
            res.put("message", "Đơn hàng chưa đạt giá trị tối thiểu " + formatVnd(minOrder) + " đ để áp dụng mã \"" + actualCode + "\"!");
            return res;
        }
        if (customerUserId != null && customerUserId > 0 && perUserLimit != null && perUserLimit > 0) {
            int customerUsed = queryInt("SELECT COUNT(*) FROM voucher_usages WHERE voucher_id = ? AND user_id = ?", vid, customerUserId);
            if (customerUsed >= perUserLimit) {
                res.put("valid", Boolean.FALSE);
                res.put("amount", 0L);
                res.put("discountVoucher", 0L);
                res.put("message", "Khách hàng này đã dùng hết lượt mã \"" + actualCode + "\" (tối đa " + perUserLimit + " lượt/khách)!");
                return res;
            }
        }

        double rawCalc = "PERCENT".equalsIgnoreCase(type) ? (orderAmount * val / 100.0) : val;
        if ("PERCENT".equalsIgnoreCase(type) && maxD > 0 && rawCalc > maxD) {
            rawCalc = maxD;
        }
        if (rawCalc > orderAmount) {
            rawCalc = orderAmount;
        }
        long finalDiscount = Math.max(0L, Math.round(rawCalc));

        res.put("valid", Boolean.TRUE);
        res.put("voucherId", vid);
        res.put("code", actualCode);
        res.put("voucherName", vName);
        res.put("discountType", type);
        res.put("discountValue", Math.round(val));
        res.put("maxDiscount", Math.round(maxD));
        res.put("minOrder", Math.round(minOrder));
        res.put("amount", finalDiscount);
        res.put("discountVoucher", finalDiscount);
        res.put("message", "Áp dụng thành công mã " + actualCode + " (Giảm " + formatVnd(finalDiscount) + " đ)");
        return res;
    }

    private String formatVnd(double amount) {
        return String.format(java.util.Locale.GERMAN, "%,d", Math.round(amount));
    }

    // UC196: Kiem tra ma voucher hop le khi thanh toan
    public Map<String, Object> uc196_validateVoucher(String voucherCode, double orderAmount, int userId) {
        String sql = "SELECT TOP 1 v.voucher_id, v.voucher_code, v.voucher_name, v.discount_type, v.discount_value, "
                + "ISNULL(v.max_discount_amount, 0) AS max_discount_amount, ISNULL(v.min_order_value, 0) AS min_order_value "
                + "FROM vouchers v WHERE UPPER(LTRIM(RTRIM(v.voucher_code))) = UPPER(?) AND v.is_active = 1 "
                + "AND GETDATE() BETWEEN v.start_date AND v.end_date AND ? >= v.min_order_value "
                + "AND (v.usage_limit IS NULL OR v.used_count < v.usage_limit) "
                + "AND (? <= 0 OR v.per_user_limit IS NULL OR (SELECT COUNT(*) FROM voucher_usages vu WHERE vu.voucher_id = v.voucher_id AND vu.user_id = ?) < v.per_user_limit)";
        return queryFirstRow(sql, voucherCode == null ? "" : voucherCode.trim(), orderAmount, userId, userId);
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

    // ==================== CLASSIC UI ADAPTERS (quan-ly-voucher.jsp & ban-hang.jsp) ====================

    public List<Map<String, Object>> listClassicVouchers(String search, String giamGia, String trangThai) {
        String s = search == null ? "" : search.trim();
        String g = giamGia == null ? "" : giamGia.trim();
        String st = trangThai == null ? "" : trangThai.trim();

        StringBuilder sql = new StringBuilder(
                "SELECT TOP 300 v.voucher_id AS ID_Voucher, v.voucher_code AS MaVoucher, "
                + "v.voucher_name AS TenChuongTrinh, "
                + "v.discount_type AS LoaiGiam, "
                + "CAST(v.discount_value AS BIGINT) AS GiamGia, "
                + "CAST(ISNULL(v.max_discount_amount, 0) AS BIGINT) AS GiamToiDa, "
                + "CAST(ISNULL(v.min_order_value, 0) AS BIGINT) AS DonToiThieu, "
                + "ISNULL(v.usage_limit, 0) AS SoLuong, "
                + "v.used_count AS DaSuDung, "
                + "CASE WHEN v.usage_limit IS NULL THEN 999 WHEN v.usage_limit > v.used_count THEN v.usage_limit - v.used_count ELSE 0 END AS ConLai, "
                + "CAST(v.is_active AS INT) AS TrangThai, "
                + "CASE WHEN v.is_active = 0 THEN 'locked' "
                + "     WHEN GETDATE() < v.start_date OR GETDATE() > v.end_date THEN 'expired' "
                + "     WHEN v.usage_limit IS NOT NULL AND v.used_count >= v.usage_limit THEN 'used' "
                + "     ELSE 'available' END AS TrangThaiCode, "
                + "CONVERT(VARCHAR(10), v.start_date, 103) AS NgayBatDau, "
                + "CONVERT(VARCHAR(10), v.end_date, 103) AS NgayKetThuc, "
                + "(SELECT TOP 1 o.order_code FROM voucher_usages vu JOIN orders o ON vu.order_id = o.order_id "
                + " WHERE vu.voucher_id = v.voucher_id ORDER BY vu.used_at DESC) AS DonHangSuDung "
                + "FROM vouchers v WHERE 1=1 "
        );
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (!s.isEmpty()) {
            sql.append("AND (v.voucher_code LIKE ? OR v.voucher_name LIKE ? OR EXISTS (SELECT 1 FROM voucher_usages vu JOIN orders o ON vu.order_id = o.order_id WHERE vu.voucher_id = v.voucher_id AND o.order_code LIKE ?)) ");
            String pat = likePattern(s);
            params.add(pat);
            params.add(pat);
            params.add(pat);
        }
        if (!g.isEmpty() && !"ALL".equalsIgnoreCase(g)) {
            try {
                double gv = Double.parseDouble(g);
                if (gv <= 100) {
                    sql.append("AND v.discount_type = 'PERCENT' AND v.discount_value = ? ");
                } else {
                    sql.append("AND v.discount_type = 'FIXED' AND v.discount_value = ? ");
                }
                params.add(gv);
            } catch (NumberFormatException ignored) {
            }
        }
        if ("available".equalsIgnoreCase(st)) {
            sql.append("AND v.is_active = 1 AND GETDATE() BETWEEN v.start_date AND v.end_date AND (v.usage_limit IS NULL OR v.used_count < v.usage_limit) ");
        } else if ("used".equalsIgnoreCase(st)) {
            sql.append("AND v.usage_limit IS NOT NULL AND v.used_count >= v.usage_limit ");
        } else if ("expired".equalsIgnoreCase(st)) {
            sql.append("AND v.is_active = 1 AND (GETDATE() < v.start_date OR GETDATE() > v.end_date) ");
        } else if ("locked".equalsIgnoreCase(st)) {
            sql.append("AND v.is_active = 0 ");
        }
        sql.append("ORDER BY v.voucher_id DESC");
        return queryRows(sql.toString(), params.toArray());
    }

    public Map<String, Object> getClassicVoucherStats() {
        Map<String, Object> stats = new java.util.LinkedHashMap<>();
        stats.put("stat_total", queryInt("SELECT COUNT(*) FROM vouchers"));
        stats.put("stat_available", queryInt(
                "SELECT COUNT(*) FROM vouchers WHERE is_active = 1 AND GETDATE() BETWEEN start_date AND end_date AND (usage_limit IS NULL OR used_count < usage_limit)"));
        stats.put("stat_used", queryInt("SELECT ISNULL(SUM(used_count), 0) FROM vouchers"));
        stats.put("stat_discount", queryDouble("SELECT ISNULL(SUM(discount_amount), 0) FROM voucher_usages"));
        return stats;
    }

    public int createClassicVipVoucher(String code, String tenChuongTrinh, double giamGia, double donToiThieu, int soLuong) {
        return createClassicVipVoucher(code, tenChuongTrinh, giamGia, donToiThieu, 0, soLuong);
    }

    public int createClassicVipVoucher(String code, String tenChuongTrinh, double giamGia, double donToiThieu, double giamToiDa, int soLuong) {
        String cleanCode = (code == null ? "" : code.trim().toUpperCase().replaceAll("[^A-Z0-9]", ""));
        if (!cleanCode.startsWith("VIP")) {
            cleanCode = "VIP" + cleanCode;
        }
        String type = giamGia <= 100 ? "PERCENT" : "FIXED";
        double maxDiscount;
        if ("PERCENT".equals(type)) {
            maxDiscount = giamToiDa > 0 ? giamToiDa : Math.max(donToiThieu * 0.5, 100000);
        } else {
            maxDiscount = giamGia;
        }
        String sql = "IF NOT EXISTS (SELECT 1 FROM vouchers WHERE voucher_code = ?) "
                + "INSERT INTO vouchers (voucher_code, voucher_name, discount_type, discount_value, max_discount_amount, "
                + "min_order_value, usage_limit, per_user_limit, start_date, end_date, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 1, DATEADD(MINUTE, -1, GETDATE()), DATEADD(YEAR, 1, GETDATE()), 1)";
        return executeUpdate(sql, cleanCode, cleanCode,
                (tenChuongTrinh == null || tenChuongTrinh.trim().isEmpty()) ? "Voucher VIP DUNA" : tenChuongTrinh.trim(),
                type, giamGia, maxDiscount, donToiThieu, Math.max(1, soLuong));
    }

    public int generateClassicBatchVouchers(String prefix, String campaignName, double amount, int qty) {
        return generateClassicBatchVouchers(prefix, campaignName, amount, 0, 0, qty);
    }

    public int generateClassicBatchVouchers(String prefix, String campaignName, double amount, double minOrderInput, double maxDiscountInput, int qty) {
        String cleanPrefix = (prefix == null || prefix.trim().isEmpty()) ? "DUNA" : prefix.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
        String name = (campaignName == null || campaignName.trim().isEmpty()) ? "Lô voucher DUNA" : campaignName.trim();
        String type = amount <= 100 ? "PERCENT" : "FIXED";
        double minOrder = minOrderInput > 0 ? minOrderInput : (amount <= 100 ? amount * 150000 : amount * 5);
        double maxDiscount = "PERCENT".equals(type)
                ? (maxDiscountInput > 0 ? maxDiscountInput : Math.max(minOrder * 0.3, 100000))
                : amount;
        int safeQty = Math.min(Math.max(1, qty), 500);
        int inserted = 0;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        java.util.Random rnd = new java.util.Random();
        String sql = "IF NOT EXISTS (SELECT 1 FROM vouchers WHERE voucher_code = ?) "
                + "INSERT INTO vouchers (voucher_code, voucher_name, discount_type, discount_value, max_discount_amount, "
                + "min_order_value, usage_limit, per_user_limit, start_date, end_date, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, 1, 1, DATEADD(MINUTE, -1, GETDATE()), DATEADD(YEAR, 1, GETDATE()), 1)";
        for (int i = 0; i < safeQty; i++) {
            StringBuilder suffix = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                suffix.append(chars.charAt(rnd.nextInt(chars.length())));
            }
            String code = cleanPrefix + suffix;
            inserted += executeUpdate(sql, code, code, name, type, amount, maxDiscount, minOrder);
        }
        return inserted;
    }
}
