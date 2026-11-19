package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;
import com.duna.common.util.JsonUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.dao.PromotionVoucherDAO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAO phan he M06 - Quan ly Don hang & Hoa don ban hang (UC126 - UC146).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class OrderDAO extends BaseDAO {

    // UC126: Xem danh sach don hang phan trang danh cho quan tri
    public List<Map<String, Object>> uc126_listOrdersPaged(String keyword, int offset, int limit) {
        String sql = "SELECT o.order_id, o.order_code, o.receiver_name, o.receiver_phone, o.channel, "
                + "o.status, o.payment_status, o.total_amount, o.ordered_at "
                + "FROM orders o WHERE o.order_code LIKE ? OR o.receiver_name LIKE ? "
                + "ORDER BY o.order_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, Math.max(0, offset), Math.max(1, limit));
    }

    // UC127: Tim kiem don hang theo ma don, so dien thoai hoac ten nguoi nhan
    public List<Map<String, Object>> uc127_searchOrders(String keyword) {
        String sql = "SELECT order_id, order_code, receiver_name, receiver_phone, channel, status, payment_status, total_amount, ordered_at "
                + "FROM orders WHERE order_code LIKE ? OR receiver_phone LIKE ? OR receiver_name LIKE ? ORDER BY order_id DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, pat);
    }

    // UC128: Loc don hang theo trang thai, kenh ban va khoang ngay dat
    public List<Map<String, Object>> uc128_filterOrders(String status, String channel, String fromDate, String toDate, String codeKeyword) {
        String sql = "SELECT order_id, order_code, channel, status, payment_status, total_amount, ordered_at "
                + "FROM orders WHERE (? = '' OR status = ?) AND (? = '' OR channel = ?) "
                + "AND ordered_at BETWEEN ? AND ? AND order_code LIKE ? ORDER BY order_id DESC";
        String st = status == null ? "" : status.trim();
        String ch = channel == null ? "" : channel.trim();
        String fd = RequestUtil.sanitizeDate(fromDate, "2000-01-01");
        String td = RequestUtil.sanitizeDate(toDate, "2099-12-31") + " 23:59:59";
        return queryRows(sql, st, st, ch, ch, fd, td, likePattern(codeKeyword));
    }

    // UC129: Xem chi tiet don hang kem danh sach san pham da mua
    public Map<String, Object> uc129_getOrderHeader(long orderId) {
        String sql = "SELECT order_id, order_code, receiver_name, receiver_phone, shipping_address, channel, "
                + "status, payment_status, subtotal, discount_amount, shipping_fee, total_amount, ordered_at "
                + "FROM orders WHERE order_id = ?";
        return queryFirstRow(sql, orderId);
    }

    // UC129 (chi tiet san pham)
    public List<Map<String, Object>> uc129_getOrderItems(long orderId) {
        String sql = "SELECT order_item_id, variant_id, product_name, sku, quantity, unit_price, line_total "
                + "FROM order_items WHERE order_id = ?";
        return queryRows(sql, orderId);
    }

    // UC130: Xem lich su mua hang cua mot khach hang
    public List<Map<String, Object>> uc130_listCustomerOrders(int customerId, String keyword) {
        String sql = "SELECT order_id, order_code, status, payment_status, total_amount, ordered_at "
                + "FROM orders WHERE customer_id = ? AND order_code LIKE ? ORDER BY ordered_at DESC";
        return queryRows(sql, customerId, likePattern(keyword));
    }

    // UC131: Tao ma don hang tu dong theo ngay (DNyyMMdd-xxxx)
    public String uc131_generateNextOrderCode() {
        String sql = "SELECT 'DN' + CONVERT(VARCHAR(6), GETDATE(), 12) + '-' "
                + "+ RIGHT('0000' + CAST(COUNT(*) + 1 AS VARCHAR(4)), 4) AS next_order_code "
                + "FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE)";
        Map<String, Object> row = queryFirstRow(sql);
        return row == null ? "DN000000-0001" : String.valueOf(row.get("next_order_code"));
    }

    // UC132: Dat hang online tu gio hang: tao don, chi tiet don va giu cho ton kho
    public long uc132_placeOrderFromCart(String orderCode, int customerId, int userId, String receiverName,
                                         String receiverPhone, String address, double subtotal, double shipFee, int cartId) {
        long orderId = executeInsertGetId(
                "INSERT INTO orders (order_code, customer_id, user_id, channel, receiver_name, receiver_phone, "
                        + "shipping_address, subtotal, shipping_fee, total_amount) "
                        + "VALUES (?, ?, ?, 'WEB', ?, ?, ?, ?, ?, ?)",
                orderCode, customerId, userId, receiverName, receiverPhone, address, subtotal, shipFee, subtotal + shipFee);
        if (orderId > 0) {
            executeUpdate(
                    "INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total) "
                            + "SELECT ?, ci.variant_id, p.product_name, pv.sku, ci.quantity, ci.unit_price, ci.quantity * ci.unit_price "
                            + "FROM cart_items ci JOIN product_variants pv ON ci.variant_id = pv.variant_id "
                            + "JOIN products p ON pv.product_id = p.product_id WHERE ci.cart_id = ?",
                    orderId, cartId);
            executeUpdate(
                    "UPDATE inv SET reserved_quantity = inv.reserved_quantity + ci.quantity "
                            + "FROM inventories inv JOIN cart_items ci ON inv.variant_id = ci.variant_id WHERE ci.cart_id = ?",
                    cartId);
            executeUpdate("UPDATE carts SET status = 'CONVERTED', updated_at = GETDATE() WHERE cart_id = ?", cartId);
            executeUpdate("INSERT INTO order_status_histories (order_id, old_status, new_status, note) VALUES (?, NULL, 'PENDING', N'Khách đặt hàng trên website')",
                    orderId);
        }
        return orderId;
    }

    // UC133: Nhan vien tao don hang ho khach qua dien thoai
    public long uc133_createPhoneOrder(String orderCode, int customerId, int staffUserId, String receiverName,
                                       String receiverPhone, String address, double subtotal, double shipFee, String note) {
        return executeInsertGetId(
                "INSERT INTO orders (order_code, customer_id, handled_by, channel, receiver_name, receiver_phone, "
                        + "shipping_address, subtotal, shipping_fee, total_amount, note) "
                        + "VALUES (?, ?, ?, 'PHONE', ?, ?, ?, ?, ?, ?, ?)",
                orderCode, customerId, staffUserId, receiverName, receiverPhone, address, subtotal, shipFee, subtotal + shipFee, note);
    }

    // UC134-UC136: Cap nhat trang thai don hang va ghi nhan lich su
    public int uc134_136_transitionOrderStatus(long orderId, String expectedOld, String newStatus, int staffUserId, String note) {
        int updated = executeUpdate("UPDATE orders SET status = ?, handled_by = ?, updated_at = GETDATE() WHERE order_id = ? AND status = ?",
                newStatus, staffUserId, orderId, expectedOld);
        if (updated > 0) {
            executeUpdate("INSERT INTO order_status_histories (order_id, old_status, new_status, changed_by, note) VALUES (?, ?, ?, ?, ?)",
                    orderId, expectedOld, newStatus, staffUserId, note);
        }
        return updated;
    }

    // UC135: Chuyen don hang sang trang thai dong goi
    public int uc135_packOrder(long orderId, int staffUserId) {
        return uc134_136_transitionOrderStatus(orderId, "CONFIRMED", "PACKING", staffUserId, "Đóng gói đơn hàng");
    }

    // UC136: Chuyen don hang sang trang thai dang giao
    public int uc136_shipOrder(long orderId, int staffUserId) {
        return uc134_136_transitionOrderStatus(orderId, "PACKING", "SHIPPING", staffUserId, "Bàn giao đơn vị vận chuyển");
    }

    // UC137: Xac nhan giao hang thanh cong va cap nhat thanh toan COD
    public int uc137_markDelivered(long orderId, int staffUserId) {
        int updated = executeUpdate("UPDATE orders SET status = 'DELIVERED', payment_status = 'PAID', updated_at = GETDATE() WHERE order_id = ? AND status = 'SHIPPING'",
                orderId);
        if (updated > 0) {
            executeUpdate("UPDATE payments SET status = 'SUCCESS', paid_at = GETDATE() WHERE order_id = ? AND status = 'PENDING'", orderId);
            executeUpdate("INSERT INTO order_status_histories (order_id, old_status, new_status, changed_by, note) VALUES (?, 'SHIPPING', 'DELIVERED', ?, N'Giao hàng thành công')",
                    orderId, staffUserId);
        }
        return updated;
    }

    // UC138: Hoan tat don hang, tru ton thuc te va cong chi tieu + nang hang + tich diem cho khach
    public int uc138_completeOrder(long orderId) {
        int updated = executeUpdate("UPDATE orders SET status = 'COMPLETED', updated_at = GETDATE() WHERE order_id = ? AND status = 'DELIVERED'", orderId);
        if (updated > 0) {
            Map<String, Object> ord = queryFirstRow("SELECT customer_id, total_amount, order_code FROM orders WHERE order_id = ?", orderId);
            if (ord != null && ord.get("customer_id") != null) {
                syncCustomerSpendTierAndLoyalty(orderId, ((Number) ord.get("customer_id")).intValue(),
                        ((Number) ord.get("total_amount")).doubleValue(), String.valueOf(ord.get("order_code")));
            }
            // Tru ton kho thuc te & giai phong giu cho theo dung so luong order_items
            Integer whId = queryOne("SELECT TOP 1 warehouse_id FROM warehouses ORDER BY warehouse_id", rs -> rs.getInt(1));
            if (whId != null) {
                executeUpdate(
                        "UPDATE inv SET "
                                + "stock_quantity = CASE WHEN inv.stock_quantity >= agg.qty THEN inv.stock_quantity - agg.qty ELSE 0 END, "
                                + "reserved_quantity = CASE WHEN inv.reserved_quantity >= agg.qty THEN inv.reserved_quantity - agg.qty ELSE 0 END, "
                                + "updated_at = GETDATE() "
                                + "FROM inventories inv "
                                + "JOIN (SELECT variant_id, SUM(quantity) AS qty FROM order_items WHERE order_id = ? GROUP BY variant_id) agg "
                                + "ON inv.variant_id = agg.variant_id WHERE inv.warehouse_id = ?",
                        orderId, whId);
            }
            // Cong dung tong so luong da ban theo tung san pham
            executeUpdate(
                    "UPDATE p SET p.sold_count = p.sold_count + agg.qty "
                            + "FROM products p "
                            + "JOIN (SELECT pv.product_id, SUM(oi.quantity) AS qty FROM order_items oi "
                            + "      JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                            + "      WHERE oi.order_id = ? GROUP BY pv.product_id) agg "
                            + "ON p.product_id = agg.product_id",
                    orderId);
        }
        return updated;
    }

    private void syncCustomerSpendTierAndLoyalty(long orderId, int customerId, double orderAmount, String orderCode) {
        executeUpdate("UPDATE customers SET total_spent = total_spent + ?, total_orders = total_orders + 1, updated_at = GETDATE() WHERE customer_id = ?",
                orderAmount, customerId);
        executeUpdate("UPDATE customers SET tier_id = (SELECT TOP 1 t.tier_id FROM membership_tiers t WHERE t.min_spend <= customers.total_spent ORDER BY t.min_spend DESC) WHERE customer_id = ?",
                customerId);
        int earned = (int) Math.floor(orderAmount / 10000.0);
        if (earned > 0) {
            executeUpdate("IF NOT EXISTS (SELECT 1 FROM loyalty_accounts WHERE customer_id = ?) INSERT INTO loyalty_accounts (customer_id, points_balance, lifetime_points) VALUES (?, 0, 0)",
                    customerId, customerId);
            executeUpdate("UPDATE loyalty_accounts SET points_balance = points_balance + ?, lifetime_points = lifetime_points + ?, updated_at = GETDATE() WHERE customer_id = ?",
                    earned, earned, customerId);
            executeUpdate("INSERT INTO loyalty_transactions (account_id, order_id, points, txn_type, description) "
                    + "SELECT account_id, ?, ?, 'EARN', ? FROM loyalty_accounts WHERE customer_id = ?",
                    orderId, earned, "Tích điểm đơn hàng " + (orderCode == null ? "#" + orderId : orderCode), customerId);
        }
    }

    // UC139: Huy don hang, ghi ly do va hoan tra ton kho giu cho
    public int uc139_cancelOrder(long orderId, String reasonCode, String reasonDetail, int userId) {
        String safeDetail = (reasonDetail == null || reasonDetail.trim().isEmpty()) ? "Hủy đơn hàng" : reasonDetail.trim();
        int updated = executeUpdate(
                "UPDATE orders SET status = 'CANCELLED', payment_status = 'UNPAID', "
                        + "note = CASE WHEN note IS NULL OR LTRIM(RTRIM(note)) = '' THEN ? ELSE LEFT(note + N' | ' + ?, 500) END, "
                        + "updated_at = GETDATE() WHERE order_id = ? AND status IN ('PENDING', 'CONFIRMED')",
                safeDetail, safeDetail, orderId);
        if (updated > 0) {
            executeUpdate("INSERT INTO order_cancellations (order_id, reason_code, reason_detail, cancelled_by) VALUES (?, ?, ?, ?)",
                    orderId, reasonCode, safeDetail, userId);
            executeUpdate("INSERT INTO order_status_histories (order_id, old_status, new_status, changed_by, note) VALUES (?, 'PENDING', 'CANCELLED', ?, ?)",
                    orderId, userId, safeDetail);
            executeUpdate("UPDATE inv SET reserved_quantity = CASE WHEN inv.reserved_quantity >= oi.quantity THEN inv.reserved_quantity - oi.quantity ELSE 0 END "
                    + "FROM inventories inv JOIN order_items oi ON inv.variant_id = oi.variant_id WHERE oi.order_id = ?",
                    orderId);
        }
        return updated;
    }

    // UC140: Them ghi chu noi bo cho don hang
    public int uc140_addOrderNote(long orderId, int userId, String content, boolean internal) {
        return executeUpdate("INSERT INTO order_notes (order_id, user_id, note_content, is_internal) VALUES (?, ?, ?, ?)",
                orderId, userId, content, internal ? 1 : 0);
    }

    // UC141: Xem danh sach ghi chu cua don hang kem nguoi ghi
    public List<Map<String, Object>> uc141_listOrderNotes(long orderId, String keyword) {
        String sql = "SELECT n.order_note_id, u.full_name, n.note_content, n.is_internal, n.created_at "
                + "FROM order_notes n LEFT JOIN users u ON n.user_id = u.user_id "
                + "WHERE n.order_id = ? AND n.note_content LIKE ? ORDER BY n.created_at DESC";
        return queryRows(sql, orderId, likePattern(keyword));
    }

    // UC142: Cap nhat dia chi giao hang cua don chua dong goi
    public int uc142_updateShippingAddress(long orderId, String receiverName, String phone, String address) {
        return executeUpdate("UPDATE orders SET receiver_name = ?, receiver_phone = ?, shipping_address = ?, updated_at = GETDATE() "
                + "WHERE order_id = ? AND status IN ('PENDING', 'CONFIRMED')", receiverName, phone, address, orderId);
    }

    // UC143: Xem lich su thay doi trang thai cua don hang
    public List<Map<String, Object>> uc143_listStatusHistory(long orderId, String statusFilter) {
        String sql = "SELECT h.history_id, h.old_status, h.new_status, u.full_name AS changed_by_name, h.note, h.changed_at "
                + "FROM order_status_histories h LEFT JOIN users u ON h.changed_by = u.user_id "
                + "WHERE h.order_id = ? AND h.new_status LIKE ? ORDER BY h.changed_at ASC";
        return queryRows(sql, orderId, likePattern(statusFilter));
    }

    // UC144: Xuat hoa don ban hang cho don da hoan tat
    public int uc144_issueInvoice(String invoiceNumber, long orderId, String buyerName, String companyName,
                                  String taxCode, String billingAddress) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM invoices WHERE order_id = ? AND status = 'ISSUED') "
                + "INSERT INTO invoices (invoice_number, order_id, buyer_name, company_name, tax_code, billing_address, total_amount, tax_amount) "
                + "SELECT ?, order_id, ?, ?, ?, ?, total_amount, tax_amount FROM orders WHERE order_id = ? AND status = 'COMPLETED'";
        return executeUpdate(sql, orderId, invoiceNumber, buyerName, companyName, taxCode, billingAddress, orderId);
    }

    // UC145: Tra cuu hoa don theo ma don hang hoac ma so thue
    public List<Map<String, Object>> uc145_findInvoices(String query) {
        String sql = "SELECT i.invoice_id, i.invoice_number, o.order_code, i.buyer_name, i.company_name, "
                + "i.tax_code, i.total_amount, i.status, i.issued_at "
                + "FROM invoices i JOIN orders o ON i.order_id = o.order_id "
                + "WHERE (o.order_code LIKE ? OR i.tax_code LIKE ? OR i.invoice_number LIKE ?) ORDER BY i.issued_at DESC";
        String pat = likePattern(query);
        return queryRows(sql, pat, pat, pat);
    }

    // UC146: Huy hoa don da xuat do sai thong tin
    public int uc146_cancelInvoice(int invoiceId) {
        return executeUpdate("UPDATE invoices SET status = 'CANCELLED' WHERE invoice_id = ? AND status = 'ISSUED'", invoiceId);
    }

    // --- Adapter cho ban-hang.jsp, quan-ly-hoa-don.jsp, bao-cao-thong-ke.jsp (DUNA_MensWear 100 bang) ---
    public List<Map<String, Object>> listPosCustomers() {
        String sql = "SELECT c.customer_id AS id, c.full_name AS ten, ISNULL(c.phone, '') AS sdt, "
                + "ISNULL(t.tier_name, N'Thành Viên') AS hang, "
                + "CAST(ISNULL(t.discount_percent, 0) AS INT) AS discountPct, "
                + "DATEDIFF(DAY, GETDATE(), DATEADD(MONTH, 3, "
                + "  ISNULL((SELECT MAX(o.ordered_at) FROM orders o WHERE o.customer_id = c.customer_id AND o.status IN ('COMPLETED', 'DELIVERED')), c.created_at))) AS daysLeft "
                + "FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "WHERE c.is_active = 1 ORDER BY c.customer_id ASC";
        return queryRows(sql);
    }

    public List<Map<String, Object>> listPosProducts() {
        String pSql = "SELECT p.product_id AS id, p.product_code AS maSP, p.product_name AS tenSP, "
                + "ISNULL(b.brand_name, N'DUNA') AS thuongHieu, CAST(p.base_price AS BIGINT) AS giaBan, "
                + "CASE WHEN p.base_price >= 1000000 THEN N'Cao cấp' WHEN p.base_price >= 400000 THEN N'Trung cấp' ELSE N'Tiêu chuẩn' END AS phanCap "
                + "FROM products p LEFT JOIN brands b ON p.brand_id = b.brand_id WHERE p.is_active = 1 ORDER BY p.product_id ASC";
        List<Map<String, Object>> products = queryRows(pSql);

        String vSql = "SELECT pv.product_id, pv.variant_id AS id, pv.variant_id AS idBienThe, "
                + "c.color_name AS mauSac, s.size_code AS trongLuong, pv.sku AS maBienThe, "
                + "ISNULL(pv.barcode, pv.sku) AS barcode, CAST(ISNULL(pv.price, p.base_price) AS BIGINT) AS giaBan, "
                + "ISNULL(SUM(inv.stock_quantity), 0) AS soLuong "
                + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id "
                + "WHERE pv.is_active = 1 "
                + "GROUP BY pv.product_id, pv.variant_id, c.color_name, s.size_code, pv.sku, pv.barcode, pv.price, p.base_price";
        List<Map<String, Object>> allVars = queryRows(vSql);

        Map<Integer, List<Map<String, Object>>> byProd = new LinkedHashMap<>();
        for (Map<String, Object> v : allVars) {
            int pid = ((Number) v.get("product_id")).intValue();
            v.put("mau", v.get("mauSac"));
            v.put("tl", v.get("trongLuong"));
            v.put("size", v.get("trongLuong"));
            v.put("gia", v.get("giaBan"));
            v.put("sl", v.get("soLuong"));
            v.put("idBT", v.get("idBienThe"));
            byProd.computeIfAbsent(pid, k -> new ArrayList<>()).add(v);
        }
        for (Map<String, Object> p : products) {
            int pid = ((Number) p.get("id")).intValue();
            List<Map<String, Object>> vars = byProd.getOrDefault(pid, new ArrayList<>());
            Set<String> colors = new LinkedHashSet<>();
            Set<String> sizes = new LinkedHashSet<>();
            int stockSum = 0;
            for (Map<String, Object> v : vars) {
                colors.add(String.valueOf(v.get("mauSac")));
                sizes.add(String.valueOf(v.get("trongLuong")));
                stockSum += ((Number) v.get("soLuong")).intValue();
            }
            p.put("soLuong", stockSum);
            p.put("mauSacList", new ArrayList<>(colors));
            p.put("trongLuongList", new ArrayList<>(sizes));
            p.put("variantsJson", JsonUtil.toJson(vars));
        }
        return products;
    }

    public Map<String, Object> scanByBarcodeOrSku(String code) {
        String q = code == null ? "" : code.trim();
        if (q.isEmpty()) {
            return null;
        }
        String sql = "SELECT TOP 1 p.product_id AS id, pv.variant_id AS idBT, p.product_code AS maSP, "
                + "p.product_name AS tenSP, CAST(ISNULL(pv.price, p.base_price) AS BIGINT) AS giaBan, "
                + "ISNULL(SUM(inv.stock_quantity), 0) AS soLuong, c.color_name AS mauSac, s.size_code AS trongLuong "
                + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id "
                + "WHERE pv.is_active = 1 AND p.is_active = 1 AND (pv.barcode = ? OR pv.sku = ? OR p.product_code = ?) "
                + "GROUP BY p.product_id, pv.variant_id, p.product_code, p.product_name, pv.price, p.base_price, c.color_name, s.size_code, pv.barcode, pv.sku "
                + "ORDER BY CASE WHEN pv.barcode = ? THEN 0 WHEN pv.sku = ? THEN 1 ELSE 2 END, pv.variant_id ASC";
        Map<String, Object> row = queryFirstRow(sql, q, q, q, q, q);
        if (row != null) {
            row.put("found", Boolean.TRUE);
            row.put("idSanPham", row.get("id"));
            row.put("idBienThe", row.get("idBT"));
            row.put("size", row.get("trongLuong"));
        }
        return row;
    }

    public double getVatRate() {
        Map<String, Object> row = queryFirstRow("SELECT TOP 1 setting_value FROM system_settings WHERE setting_key = 'VAT_RATE'");
        if (row != null && row.get("setting_value") != null) {
            try {
                return Double.parseDouble(String.valueOf(row.get("setting_value")));
            } catch (NumberFormatException ignored) {
            }
        }
        return 8.0;
    }

    public int updateVatRate(double newVat) {
        return executeUpdate("UPDATE system_settings SET setting_value = ?, updated_at = GETDATE() WHERE setting_key = 'VAT_RATE'",
                String.valueOf((int) Math.round(newVat)));
    }

    public Map<String, Object> getHoldOrderInfo(long orderId) {
        Map<String, Object> h = queryFirstRow(
                "SELECT o.order_id AS id, o.order_code AS maDH, ISNULL(o.customer_id, 0) AS idKH, "
                        + "o.receiver_name AS tenKH, ISNULL(o.receiver_phone, '') AS sdtKH, ISNULL(o.note, '') AS ghiChu "
                        + "FROM orders o WHERE o.order_id = ? AND o.payment_status = 'UNPAID'", orderId);
        if (h == null) {
            return null;
        }
        List<Map<String, Object>> items = queryRows(
                "SELECT pv.product_id AS idSP, oi.product_name AS tenSP, CAST(oi.unit_price AS BIGINT) AS donGia, "
                        + "oi.quantity AS soLuong, ISNULL(inv.stock_quantity, 10) AS tonKho "
                        + "FROM order_items oi JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                        + "LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE oi.order_id = ?", orderId);
        h.put("items", items);
        return h;
    }

    public long saveClassicPosOrder(long holdId, Integer customerId, int staffUserId, boolean isPaid,
                                    String payMethod, String voucherCode, double discountAmount,
                                    double taxAmount, String note, String[] spIds, String[] qtys,
                                    String[] prices, String[] colors, String[] sizes) {
        return saveClassicPosOrder(holdId, customerId, staffUserId, isPaid, payMethod,
                voucherCode, discountAmount, 0, taxAmount, note, spIds, qtys, prices, colors, sizes);
    }

    public long saveClassicPosOrder(long holdId, Integer customerId, int staffUserId, boolean isPaid,
                                    String payMethod, String voucherCode, double vipDiscountAmount,
                                    double voucherDiscountInput, double taxAmount, String note,
                                    String[] spIds, String[] qtys, String[] prices, String[] colors, String[] sizes) {
        if (spIds == null || spIds.length == 0 || qtys == null || prices == null) {
            return -1L;
        }
        int itemCount = Math.min(spIds.length, Math.min(qtys.length, prices.length));
        double subtotal = 0;
        List<int[]> itemVars = new ArrayList<>();
        List<String[]> itemMeta = new ArrayList<>();
        List<Integer> itemQtys = new ArrayList<>();
        List<Double> itemPrices = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            if (spIds[i] == null || qtys[i] == null || prices[i] == null) {
                continue;
            }
            int spId;
            int qty;
            double price;
            try {
                spId = Integer.parseInt(spIds[i].trim());
                qty = Integer.parseInt(qtys[i].trim());
                price = Double.parseDouble(prices[i].trim().replace(",", ""));
            } catch (NumberFormatException ex) {
                continue;
            }
            if (spId <= 0 || qty <= 0 || price < 0) {
                continue;
            }
            String col = (colors != null && i < colors.length && colors[i] != null) ? colors[i].trim() : "";
            String sz = (sizes != null && i < sizes.length && sizes[i] != null) ? sizes[i].trim() : "";

            Map<String, Object> vRow = queryFirstRow(
                    "SELECT TOP 1 pv.variant_id, pv.sku, p.product_name "
                            + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                            + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                            + "WHERE pv.product_id = ? AND (? = '' OR c.color_name = ?) AND (? = '' OR s.size_code = ?) "
                            + "ORDER BY pv.variant_id ASC",
                    spId, col, col, sz, sz);
            if (vRow == null) {
                vRow = queryFirstRow(
                        "SELECT TOP 1 pv.variant_id, pv.sku, p.product_name FROM product_variants pv JOIN products p ON pv.product_id = p.product_id WHERE pv.product_id = ?",
                        spId);
            }
            if (vRow == null) {
                continue;
            }
            int variantId = ((Number) vRow.get("variant_id")).intValue();
            String label = String.valueOf(vRow.get("product_name"))
                    + (!col.isEmpty() || !sz.isEmpty() ? " (" + col + (!col.isEmpty() && !sz.isEmpty() ? " - " : "") + sz + ")" : "");
            subtotal += price * qty;
            itemVars.add(new int[]{spId, variantId});
            itemMeta.add(new String[]{label, String.valueOf(vRow.get("sku"))});
            itemQtys.add(qty);
            itemPrices.add(price);
        }
        if (itemVars.isEmpty()) {
            return -1L;
        }

        String custName = "Khách vãng lai";
        String custPhone = "0000000000";
        Integer customerUserId = null;
        if (customerId != null && customerId > 0) {
            Map<String, Object> cRow = queryFirstRow("SELECT user_id, full_name, ISNULL(phone, '') AS phone FROM customers WHERE customer_id = ?", customerId);
            if (cRow != null) {
                custName = String.valueOf(cRow.get("full_name"));
                custPhone = String.valueOf(cRow.get("phone"));
                if (cRow.get("user_id") instanceof Number) {
                    customerUserId = ((Number) cRow.get("user_id")).intValue();
                }
            }
        } else {
            customerId = null;
        }

        // Xac thuc lai voucher dong bo truc tiep voi bang vouchers (Quan ly Voucher)
        Integer validVoucherId = null;
        String validVoucherCode = null;
        double appliedVoucherDisc = 0;
        if (isPaid && voucherCode != null && !voucherCode.trim().isEmpty()) {
            PromotionVoucherDAO voucherDAO = new PromotionVoucherDAO();
            Map<String, Object> vCheck = voucherDAO.validateVoucherDetailed(voucherCode.trim(), subtotal, customerUserId);
            if (Boolean.TRUE.equals(vCheck.get("valid")) && vCheck.get("voucherId") instanceof Number) {
                validVoucherId = ((Number) vCheck.get("voucherId")).intValue();
                validVoucherCode = String.valueOf(vCheck.get("code"));
                double serverDisc = ((Number) vCheck.get("amount")).doubleValue();
                appliedVoucherDisc = Math.min(serverDisc, Math.max(0, subtotal - vipDiscountAmount));
            }
        } else if (!isPaid && voucherDiscountInput > 0) {
            appliedVoucherDisc = voucherDiscountInput;
        }

        double totalDiscount = Math.min(subtotal, Math.max(0, vipDiscountAmount) + Math.max(0, appliedVoucherDisc));
        double vatPct = getVatRate();
        double taxableBase = Math.max(0, subtotal - totalDiscount);
        double finalTax = Math.round(taxableBase * vatPct / 100.0);
        double totalAmount = Math.max(0, taxableBase + finalTax);

        StringBuilder fullNote = new StringBuilder(note == null || note.trim().isEmpty() ? "Bán hàng tại quầy" : note.trim());
        if (vipDiscountAmount > 0 && !fullNote.toString().toLowerCase().contains("giảm giá:")) {
            fullNote.append(" | Giảm giá: -").append(String.format(java.util.Locale.GERMAN, "%,d", Math.round(vipDiscountAmount))).append(" VNĐ");
        }
        if (validVoucherCode != null && appliedVoucherDisc > 0 && !fullNote.toString().toLowerCase().contains("voucher")) {
            fullNote.append(" | Voucher (").append(validVoucherCode).append("): -")
                    .append(String.format(java.util.Locale.GERMAN, "%,d", Math.round(appliedVoucherDisc))).append(" VNĐ");
        }
        if (finalTax > 0 && !fullNote.toString().contains("VAT")) {
            fullNote.append(" | VAT (").append((int) Math.round(vatPct)).append("%): +")
                    .append(String.format(java.util.Locale.GERMAN, "%,d", Math.round(finalTax))).append(" VNĐ");
        }
        String finalNote = fullNote.toString();

        if (holdId > 0) {
            executeUpdate("DELETE FROM order_items WHERE order_id = ?", holdId);
            executeUpdate(
                    "UPDATE orders SET customer_id = ?, status = ?, payment_status = ?, subtotal = ?, discount_amount = ?, "
                            + "tax_amount = ?, total_amount = ?, voucher_id = ?, note = ?, updated_at = GETDATE() WHERE order_id = ?",
                    customerId, isPaid ? "COMPLETED" : "PENDING", isPaid ? "PAID" : "UNPAID",
                    subtotal, totalDiscount, finalTax, totalAmount, validVoucherId, finalNote, holdId);
        } else {
            String orderCode = "POS" + uc131_generateNextOrderCode().substring(2);
            holdId = executeInsertGetId(
                    "INSERT INTO orders (order_code, customer_id, store_id, channel, status, payment_status, handled_by, "
                            + "receiver_name, receiver_phone, shipping_address, subtotal, discount_amount, tax_amount, total_amount, voucher_id, note) "
                            + "VALUES (?, ?, (SELECT TOP 1 store_id FROM stores ORDER BY store_id), 'POS', ?, ?, ?, ?, ?, N'Tại cửa hàng DUNA', ?, ?, ?, ?, ?, ?)",
                    orderCode, customerId, isPaid ? "COMPLETED" : "PENDING", isPaid ? "PAID" : "UNPAID",
                    staffUserId, custName, custPhone, subtotal, totalDiscount, finalTax, totalAmount, validVoucherId, finalNote);
        }

        Integer whId = queryOne("SELECT TOP 1 warehouse_id FROM warehouses ORDER BY warehouse_id", rs -> rs.getInt(1));
        for (int i = 0; i < itemVars.size(); i++) {
            int variantId = itemVars.get(i)[1];
            int qty = itemQtys.get(i);
            double price = itemPrices.get(i);
            executeUpdate("INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    holdId, variantId, itemMeta.get(i)[0], itemMeta.get(i)[1], qty, price, qty * price);
            if (isPaid && whId != null) {
                executeUpdate("UPDATE inventories SET stock_quantity = CASE WHEN stock_quantity >= ? THEN stock_quantity - ? ELSE 0 END, updated_at = GETDATE() WHERE variant_id = ? AND warehouse_id = ?",
                        qty, qty, variantId, whId);
                executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id) VALUES (?, ?, 'SALE', ?, 'ORDER', ?)",
                        variantId, whId, -qty, holdId);
                executeUpdate("UPDATE products SET sold_count = sold_count + ? WHERE product_id = ?", qty, itemVars.get(i)[0]);
            }
        }
        if (isPaid) {
            if (validVoucherId != null) {
                executeUpdate("UPDATE vouchers SET used_count = used_count + 1 WHERE voucher_id = ?", validVoucherId);
                executeUpdate("INSERT INTO voucher_usages (voucher_id, user_id, order_id, discount_amount) VALUES (?, ?, ?, ?)",
                        validVoucherId, customerUserId != null ? customerUserId : staffUserId, holdId, appliedVoucherDisc);
            }
            if (customerId != null) {
                Map<String, Object> oCodeRow = queryFirstRow("SELECT order_code FROM orders WHERE order_id = ?", holdId);
                String finalCode = oCodeRow != null ? String.valueOf(oCodeRow.get("order_code")) : ("#" + holdId);
                syncCustomerSpendTierAndLoyalty(holdId, customerId, totalAmount, finalCode);
            }
            String methodCode = (payMethod != null && payMethod.toLowerCase().contains("chuyển")) ? "VNPAY" : "COD";
            executeUpdate("INSERT INTO payments (order_id, method_id, amount, status, paid_at) "
                    + "SELECT ?, method_id, ?, 'SUCCESS', GETDATE() FROM payment_methods WHERE method_code = ?",
                    holdId, totalAmount, methodCode);
        }
        return holdId;
    }

    public List<Map<String, Object>> listClassicOrders(String search, String trangThai, String payMethod,
                                                       String fromDate, String toDate, int offset, int limit) {
        String kw = likePattern(search);
        String st = trangThai == null ? "" : trangThai.trim();
        String pm = payMethod == null ? "" : payMethod.trim();
        String fd = RequestUtil.sanitizeDate(fromDate, "2000-01-01");
        String td = RequestUtil.sanitizeDate(toDate, "2099-12-31") + " 23:59:59";
        String sql = "SELECT o.order_id AS id, o.order_code AS maDH, "
                + "CONVERT(VARCHAR(16), o.ordered_at, 120) AS ngayTao, "
                + "ISNULL(o.receiver_name, N'Khách vãng lai') AS tenKH, "
                + "ISNULL(u.full_name, N'Admin') AS tenNV, "
                + "CASE WHEN EXISTS (SELECT 1 FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE p.order_id = o.order_id AND m.is_online = 1) THEN N'Chuyển khoản' ELSE N'Tiền mặt' END AS thanhToan, "
                + "CASE WHEN o.status = 'CANCELLED' THEN N'Đã hủy' WHEN o.payment_status = 'PAID' OR o.status IN ('COMPLETED', 'DELIVERED') THEN N'Đã thanh toán' ELSE N'Chưa thanh toán' END AS trangThai, "
                + "CAST(o.total_amount AS BIGINT) AS tongTien, ISNULL(o.note, '') AS ghiChu "
                + "FROM orders o LEFT JOIN users u ON o.handled_by = u.user_id "
                + "WHERE (o.order_code LIKE ? OR ISNULL(o.receiver_name, '') LIKE ? OR ISNULL(o.receiver_phone, '') LIKE ?) "
                + "AND o.ordered_at BETWEEN ? AND ? "
                + "AND (? = '' OR ? = 'ALL' "
                + "  OR (? = N'Đã thanh toán' AND (o.payment_status = 'PAID' OR o.status IN ('COMPLETED', 'DELIVERED')) AND o.status <> 'CANCELLED') "
                + "  OR (? = N'Chưa thanh toán' AND o.payment_status = 'UNPAID' AND o.status <> 'CANCELLED') "
                + "  OR (? = N'Đã hủy' AND o.status = 'CANCELLED')) "
                + "AND (? = '' OR ? = 'ALL' "
                + "  OR (? = N'Chuyển khoản' AND EXISTS (SELECT 1 FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE p.order_id = o.order_id AND m.is_online = 1)) "
                + "  OR (? = N'Tiền mặt' AND NOT EXISTS (SELECT 1 FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE p.order_id = o.order_id AND m.is_online = 1))) "
                + "ORDER BY o.order_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Map<String, Object>> orders = queryRows(sql, kw, kw, kw, fd, td, st, st, st, st, st, pm, pm, pm, pm, Math.max(0, offset), Math.max(1, limit));
        for (Map<String, Object> dh : orders) {
            long oid = ((Number) dh.get("id")).longValue();
            List<Map<String, Object>> items = queryRows(
                    "SELECT oi.order_item_id AS idChiTiet, pv.product_id AS idSP, oi.product_name AS tenSP, "
                            + "oi.quantity AS soLuong, CAST(oi.unit_price AS BIGINT) AS donGia, CAST(oi.line_total AS BIGINT) AS thanhTien "
                            + "FROM order_items oi JOIN product_variants pv ON oi.variant_id = pv.variant_id WHERE oi.order_id = ?", oid);
            dh.put("chiTiet", items);
        }
        return orders;
    }

    public int countClassicOrders(String search, String trangThai, String payMethod, String fromDate, String toDate) {
        String kw = likePattern(search);
        String st = trangThai == null ? "" : trangThai.trim();
        String pm = payMethod == null ? "" : payMethod.trim();
        String fd = RequestUtil.sanitizeDate(fromDate, "2000-01-01");
        String td = RequestUtil.sanitizeDate(toDate, "2099-12-31") + " 23:59:59";
        String sql = "SELECT COUNT(*) FROM orders o "
                + "WHERE (o.order_code LIKE ? OR ISNULL(o.receiver_name, '') LIKE ? OR ISNULL(o.receiver_phone, '') LIKE ?) "
                + "AND o.ordered_at BETWEEN ? AND ? "
                + "AND (? = '' OR ? = 'ALL' "
                + "  OR (? = N'Đã thanh toán' AND (o.payment_status = 'PAID' OR o.status IN ('COMPLETED', 'DELIVERED')) AND o.status <> 'CANCELLED') "
                + "  OR (? = N'Chưa thanh toán' AND o.payment_status = 'UNPAID' AND o.status <> 'CANCELLED') "
                + "  OR (? = N'Đã hủy' AND o.status = 'CANCELLED')) "
                + "AND (? = '' OR ? = 'ALL' "
                + "  OR (? = N'Chuyển khoản' AND EXISTS (SELECT 1 FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE p.order_id = o.order_id AND m.is_online = 1)) "
                + "  OR (? = N'Tiền mặt' AND NOT EXISTS (SELECT 1 FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE p.order_id = o.order_id AND m.is_online = 1)))";
        return queryInt(sql, kw, kw, kw, fd, td, st, st, st, st, st, pm, pm, pm, pm);
    }

    public Map<String, Object> getClassicOrderDetailJson(long orderId) {
        Map<String, Object> row = queryFirstRow(
                "SELECT o.order_code AS maDH, CONVERT(VARCHAR(16), o.ordered_at, 120) AS ngayTao, "
                        + "ISNULL(o.receiver_name, N'Khách vãng lai') AS tenKH, ISNULL(o.receiver_phone, '-') AS sdtKH, "
                        + "ISNULL(o.shipping_address, N'Tại cửa hàng DUNA') AS diaChiKH, "
                        + "CASE WHEN EXISTS (SELECT 1 FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE p.order_id = o.order_id AND m.is_online = 1) THEN N'Chuyển khoản' ELSE N'Tiền mặt' END AS thanhToan, "
                        + "CASE WHEN o.status = 'CANCELLED' THEN N'Đã hủy' WHEN o.payment_status = 'PAID' OR o.status IN ('COMPLETED', 'DELIVERED') THEN N'Đã thanh toán' ELSE N'Chưa thanh toán' END AS trangThai, "
                        + "ISNULL(u.full_name, N'Thu ngân DUNA') AS tenNV, ISNULL(o.note, '') AS ghiChu, "
                        + "ISNULL(v.voucher_code, '') AS voucherCode, ISNULL(v.voucher_name, '') AS voucherName, "
                        + "ISNULL((SELECT TOP 1 CAST(vu.discount_amount AS BIGINT) FROM voucher_usages vu WHERE vu.order_id = o.order_id), 0) AS voucherDiscount, "
                        + "CAST(o.discount_amount AS BIGINT) AS tongGiamGia, "
                        + "CAST(o.tax_amount AS BIGINT) AS tienVAT, "
                        + "CAST(o.total_amount AS BIGINT) AS tongTien "
                        + "FROM orders o LEFT JOIN users u ON o.handled_by = u.user_id "
                        + "LEFT JOIN vouchers v ON o.voucher_id = v.voucher_id WHERE o.order_id = ?", orderId);
        if (row != null) {
            List<Map<String, Object>> itemRows = queryRows(
                    "SELECT CASE WHEN oi.product_name LIKE '%(%' THEN oi.product_name "
                            + "ELSE oi.product_name + N' (' + ISNULL(c.color_name, N'Tiêu chuẩn') + N' / ' + ISNULL(s.size_code, 'L') + N')' END AS tenSP, "
                            + "oi.quantity AS soLuong, CAST(oi.unit_price AS BIGINT) AS donGia, CAST(oi.line_total AS BIGINT) AS thanhTien "
                            + "FROM order_items oi "
                            + "LEFT JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                            + "LEFT JOIN colors c ON pv.color_id = c.color_id "
                            + "LEFT JOIN sizes s ON pv.size_id = s.size_id "
                            + "WHERE oi.order_id = ?", orderId);
            row.put("chiTiet", itemRows);
            row.put("items", itemRows);
        }
        return row;
    }

    public int exchangeOrderItem(long orderId, long orderItemId, int newVariantId, int qty) {
        Map<String, Object> oldItem = queryFirstRow(
                "SELECT variant_id, quantity FROM order_items WHERE order_item_id = ? AND order_id = ?",
                orderItemId, orderId);
        if (oldItem == null || newVariantId <= 0) return 0;
        int oldVariantId = ((Number) oldItem.get("variant_id")).intValue();
        if (oldVariantId == newVariantId) return 0;
        int itemQty = Math.max(1, ((Number) oldItem.get("quantity")).intValue());
        int q = qty > 0 ? Math.min(qty, itemQty) : itemQty;

        Map<String, Object> nv = queryFirstRow(
                "SELECT p.product_name + ' (' + c.color_name + ' - ' + s.size_code + ')' AS lbl, pv.sku, ISNULL(pv.price, p.base_price) AS price "
                        + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                        + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                        + "WHERE pv.variant_id = ? AND pv.is_active = 1",
                newVariantId);
        if (nv == null) return 0;

        Integer whId = queryOne(
                "SELECT TOP 1 warehouse_id FROM inventories WHERE variant_id = ? AND stock_quantity >= ? ORDER BY stock_quantity DESC, warehouse_id ASC",
                rs -> rs.getInt(1), newVariantId, q);
        if (whId == null) {
            return -1;
        }
        int deducted = executeUpdate(
                "UPDATE inventories SET stock_quantity = stock_quantity - ?, updated_at = GETDATE() "
                        + "WHERE variant_id = ? AND warehouse_id = ? AND stock_quantity >= ?",
                q, newVariantId, whId, q);
        if (deducted <= 0) {
            return -1;
        }
        int restored = executeUpdate(
                "UPDATE inventories SET stock_quantity = stock_quantity + ?, updated_at = GETDATE() "
                        + "WHERE variant_id = ? AND warehouse_id = ?",
                q, oldVariantId, whId);
        if (restored == 0) {
            executeUpdate("INSERT INTO inventories (variant_id, warehouse_id, stock_quantity) VALUES (?, ?, ?)",
                    oldVariantId, whId, q);
        }
        executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id, reason) "
                + "VALUES (?, ?, 'RETURN', ?, 'ORDER', ?, N'Hoàn kho khi đổi size/màu')", oldVariantId, whId, q, orderId);
        executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id, reason) "
                + "VALUES (?, ?, 'SALE', ?, 'ORDER', ?, N'Xuất kho biến thể mới khi đổi size/màu')", newVariantId, whId, -q, orderId);

        double newPrice = ((Number) nv.get("price")).doubleValue();
        executeUpdate("UPDATE order_items SET variant_id = ?, product_name = ?, sku = ?, unit_price = ?, line_total = quantity * ? WHERE order_item_id = ?",
                newVariantId, nv.get("lbl"), nv.get("sku"), newPrice, newPrice, orderItemId);

        // Tinh lai tong tien don hang sau khi doi bien the (co the chenh lech gia)
        double newSubtotal = queryDouble("SELECT ISNULL(SUM(line_total), 0) FROM order_items WHERE order_id = ?", orderId);
        Map<String, Object> ordRow = queryFirstRow("SELECT ISNULL(discount_amount, 0) AS disc, ISNULL(shipping_fee, 0) AS ship FROM orders WHERE order_id = ?", orderId);
        double disc = ordRow != null && ordRow.get("disc") instanceof Number ? ((Number) ordRow.get("disc")).doubleValue() : 0;
        double ship = ordRow != null && ordRow.get("ship") instanceof Number ? ((Number) ordRow.get("ship")).doubleValue() : 0;
        double vatPct = getVatRate();
        double taxable = Math.max(0, newSubtotal - disc);
        double newTax = Math.round(taxable * vatPct / 100.0);
        double newTotal = Math.max(0, taxable + newTax + ship);
        executeUpdate("UPDATE orders SET subtotal = ?, tax_amount = ?, total_amount = ?, updated_at = GETDATE() WHERE order_id = ?",
                newSubtotal, newTax, newTotal, orderId);

        return executeUpdate(
                "INSERT INTO return_requests (return_code, order_id, request_type, reason, status) VALUES (?, ?, 'EXCHANGE', N'Đổi size/màu tại cửa hàng', 'COMPLETED')",
                "EX-" + System.currentTimeMillis(), orderId);
    }

    public Map<String, Object> getClassicOrderKpiCounts() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("stat_completedOrders", queryInt("SELECT COUNT(*) FROM orders WHERE (payment_status = 'PAID' OR status IN ('COMPLETED','DELIVERED')) AND status <> 'CANCELLED'"));
        out.put("stat_cancelledOrders", queryInt("SELECT COUNT(*) FROM orders WHERE status = 'CANCELLED'"));
        out.put("stat_totalOrders", queryInt("SELECT COUNT(*) FROM orders"));
        return out;
    }

    public Map<String, Object> buildClassicReportData(int year, int top5Month, int top5Year, int soldMonth, int soldYear) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("stat_completedOrders", queryInt("SELECT COUNT(*) FROM orders WHERE (payment_status = 'PAID' OR status IN ('COMPLETED','DELIVERED')) AND status <> 'CANCELLED'"));
        out.put("stat_cancelledOrders", queryInt("SELECT COUNT(*) FROM orders WHERE status = 'CANCELLED'"));
        out.put("stat_totalOrders", queryInt("SELECT COUNT(*) FROM orders"));

        long revToday = (long) queryDouble("SELECT ISNULL(SUM(total_amount), 0) FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE) AND status <> 'CANCELLED'");
        long costToday = (long) queryDouble("SELECT ISNULL(SUM(oi.quantity * p.cost_price), 0) FROM order_items oi JOIN orders o ON oi.order_id = o.order_id JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id WHERE CAST(o.ordered_at AS DATE) = CAST(GETDATE() AS DATE) AND o.status <> 'CANCELLED'");
        out.put("stat_totalRevenueToday", revToday);
        out.put("stat_grossProfitToday", Math.max(0L, revToday - costToday));

        long revYear = (long) queryDouble("SELECT ISNULL(SUM(total_amount), 0) FROM orders WHERE YEAR(ordered_at) = ? AND status <> 'CANCELLED'", year);
        long costYear = (long) queryDouble("SELECT ISNULL(SUM(oi.quantity * p.cost_price), 0) FROM order_items oi JOIN orders o ON oi.order_id = o.order_id JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id WHERE YEAR(o.ordered_at) = ? AND o.status <> 'CANCELLED'", year);
        long profitYear = Math.max(0L, revYear - costYear);
        double margin = revYear > 0 ? Math.round((profitYear * 1000.0) / revYear) / 10.0 : 0.0;

        out.put("stat_totalRevenueYear", revYear);
        out.put("stat_grossProfitYear", profitYear);
        out.put("stat_profitMarginYear", margin);
        out.put("stat_totalRevenueAllTime", (long) queryDouble("SELECT ISNULL(SUM(total_amount), 0) FROM orders WHERE status <> 'CANCELLED'"));
        out.put("stat_totalSoldYear", queryInt("SELECT ISNULL(SUM(oi.quantity), 0) FROM order_items oi JOIN orders o ON oi.order_id = o.order_id WHERE YEAR(o.ordered_at) = ? AND o.status <> 'CANCELLED'", year));
        out.put("stat_totalSold", queryInt("SELECT ISNULL(SUM(sold_count), 0) FROM products"));
        out.put("stat_totalInventory", queryInt("SELECT ISNULL(SUM(stock_quantity), 0) FROM inventories"));

        long[] mRev = new long[12];
        long[] mCost = new long[12];
        long[] mProfit = new long[12];
        int[] mSold = new int[12];
        List<Map<String, Object>> mRows = queryRows(
                "SELECT MONTH(o.ordered_at) AS m, ISNULL(SUM(o.total_amount), 0) AS rev "
                        + "FROM orders o WHERE YEAR(o.ordered_at) = ? AND o.status <> 'CANCELLED' GROUP BY MONTH(o.ordered_at)", year);
        for (Map<String, Object> mr : mRows) {
            int m = ((Number) mr.get("m")).intValue();
            if (m >= 1 && m <= 12) {
                mRev[m - 1] = ((Number) mr.get("rev")).longValue();
            }
        }
        List<Map<String, Object>> cRows = queryRows(
                "SELECT MONTH(o.ordered_at) AS m, ISNULL(SUM(oi.quantity * p.cost_price), 0) AS cogs "
                        + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id "
                        + "JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                        + "JOIN products p ON pv.product_id = p.product_id "
                        + "WHERE YEAR(o.ordered_at) = ? AND o.status <> 'CANCELLED' GROUP BY MONTH(o.ordered_at)", year);
        for (Map<String, Object> cr : cRows) {
            int m = ((Number) cr.get("m")).intValue();
            if (m >= 1 && m <= 12) {
                mCost[m - 1] = ((Number) cr.get("cogs")).longValue();
            }
        }
        for (int i = 0; i < 12; i++) {
            mProfit[i] = Math.max(0L, mRev[i] - mCost[i]);
        }
        List<Map<String, Object>> sRows = queryRows(
                "SELECT MONTH(o.ordered_at) AS m, ISNULL(SUM(oi.quantity), 0) AS qty "
                        + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id WHERE YEAR(o.ordered_at) = ? AND o.status <> 'CANCELLED' GROUP BY MONTH(o.ordered_at)", year);
        for (Map<String, Object> sr : sRows) {
            int m = ((Number) sr.get("m")).intValue();
            if (m >= 1 && m <= 12) mSold[m - 1] = ((Number) sr.get("qty")).intValue();
        }
        List<Long> revList = new ArrayList<>();
        List<Long> profList = new ArrayList<>();
        List<Integer> soldList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            revList.add(mRev[i]);
            profList.add(mProfit[i]);
            soldList.add(mSold[i]);
        }
        out.put("stat_monthlyRevenue", JsonUtil.toJson(revList));
        out.put("stat_monthlyProfit", JsonUtil.toJson(profList));
        out.put("stat_monthlySold", JsonUtil.toJson(soldList));

        out.put("topProducts", queryRows(
                "SELECT TOP 10 ROW_NUMBER() OVER (ORDER BY SUM(oi.quantity) DESC, SUM(oi.line_total) DESC) AS stt, "
                        + "p.product_code AS maSP, p.product_name AS tenSP, "
                        + "? AS thang, ? AS nam, "
                        + "SUM(oi.quantity) AS daBan, CAST(SUM(oi.line_total) AS BIGINT) AS doanhThu "
                        + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id "
                        + "JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                        + "JOIN products p ON pv.product_id = p.product_id "
                        + "WHERE o.status <> 'CANCELLED' AND MONTH(o.ordered_at) = ? AND YEAR(o.ordered_at) = ? "
                        + "GROUP BY p.product_id, p.product_code, p.product_name "
                        + "ORDER BY SUM(oi.quantity) DESC, SUM(oi.line_total) DESC",
                top5Month, top5Year, top5Month, top5Year));
        out.put("categoryStock", queryRows(
                "SELECT c.category_name AS danhMuc, COUNT(DISTINCT p.product_id) AS soLuong, "
                        + "ISNULL(SUM(inv.stock_quantity), 0) AS soLuongTon "
                        + "FROM categories c LEFT JOIN products p ON c.category_id = p.category_id "
                        + "LEFT JOIN product_variants pv ON p.product_id = pv.product_id "
                        + "LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id "
                        + "GROUP BY c.category_name ORDER BY soLuongTon DESC"));
        out.put("soldProducts", queryRows(
                "SELECT TOP 50 ROW_NUMBER() OVER (ORDER BY SUM(oi.quantity) DESC, MAX(o.ordered_at) DESC) AS stt, "
                        + "p.product_code AS maSP, p.product_name AS tenSP, "
                        + "c.category_name AS danhMuc, c.category_name AS phanLoai, "
                        + "SUM(oi.quantity) AS soLuong, CAST(SUM(oi.line_total) AS BIGINT) AS thanhTien, "
                        + "? AS thang, ? AS nam, CONVERT(VARCHAR(16), MAX(o.ordered_at), 120) AS thoiGian "
                        + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id "
                        + "JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                        + "JOIN products p ON pv.product_id = p.product_id "
                        + "JOIN categories c ON p.category_id = c.category_id "
                        + "WHERE o.status <> 'CANCELLED' AND MONTH(o.ordered_at) = ? AND YEAR(o.ordered_at) = ? "
                        + "GROUP BY p.product_code, p.product_name, c.category_name "
                        + "ORDER BY SUM(oi.quantity) DESC, MAX(o.ordered_at) DESC",
                soldMonth, soldYear, soldMonth, soldYear));
        return out;
    }
}
