package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M14 - Cua hang, Ca ban hang POS & Ket tien mat (UC257 - UC266).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class StorePosDAO extends BaseDAO {

    // UC257: Xem danh sach cua hang va kho lien ket
    public List<Map<String, Object>> uc257_listStores(String keyword) {
        String sql = "SELECT s.store_id, s.store_code, s.store_name, s.address, p.province_name, "
                + "w.warehouse_name, s.open_time, s.close_time, s.is_active "
                + "FROM stores s LEFT JOIN provinces p ON s.province_id = p.province_id "
                + "LEFT JOIN warehouses w ON s.warehouse_id = w.warehouse_id "
                + "WHERE s.store_name LIKE ? OR s.store_code LIKE ?";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC258: Them moi cua hang ban le
    public int uc258_addStore(String code, String name, String address, Integer provinceId, String phone,
                              String openTime, String closeTime, Integer warehouseId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM stores WHERE store_code = ?) "
                + "INSERT INTO stores (store_code, store_name, address, province_id, phone, open_time, close_time, warehouse_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, address, provinceId, phone, openTime, closeTime, warehouseId);
    }

    // UC259: Cap nhat gio mo cua va kho xuat hang cua cua hang
    public int uc259_updateStoreHoursAndWarehouse(int storeId, String openTime, String closeTime, int warehouseId) {
        return executeUpdate("UPDATE stores SET open_time = ?, close_time = ?, warehouse_id = ? WHERE store_id = ?",
                openTime, closeTime, warehouseId, storeId);
    }

    // UC260: Mo ca ban hang tai quay va nhap tien dau ca
    public long uc260_openPosSession(int storeId, int cashierId, double openingCash) {
        Map<String, Object> existing = queryFirstRow(
                "SELECT pos_session_id FROM pos_sessions WHERE cashier_id = ? AND status = 'OPEN'", cashierId);
        if (existing != null) {
            return ((Number) existing.get("pos_session_id")).longValue();
        }
        return executeInsertGetId("INSERT INTO pos_sessions (store_id, cashier_id, opening_cash) VALUES (?, ?, ?)",
                storeId, cashierId, openingCash);
    }

    // UC261: Tra cuu san pham tai quay bang quet ma vach
    public List<Map<String, Object>> uc261_scanBarcodeAtPos(String barcodeOrSku, int warehouseId) {
        String sql = "SELECT pv.variant_id, p.product_code, p.product_name, c.color_name, s.size_code, "
                + "pv.sku, pv.barcode, ISNULL(pv.price, p.base_price) AS price, inv.stock_quantity "
                + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "JOIN inventories inv ON pv.variant_id = inv.variant_id "
                + "WHERE (pv.barcode LIKE ? OR pv.sku LIKE ? OR p.product_name LIKE ?) "
                + "AND (? <= 0 OR inv.warehouse_id = ?)";
        String pat = likePattern(barcodeOrSku);
        return queryRows(sql, pat, pat, pat, warehouseId, warehouseId);
    }

    // UC262: Ban hang tai quay: tao don POS, tru ton kho va ghi nhan tien mat
    public long uc262_createPosOrder(String orderCode, Integer customerId, int storeId, int warehouseId,
                                     int cashierId, int posSessionId, int variantId, int qty) {
        Map<String, Object> v = queryFirstRow(
                "SELECT p.product_name, pv.sku, ISNULL(pv.price, p.base_price) AS price "
                        + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id WHERE pv.variant_id = ?",
                variantId);
        if (v == null) {
            return 0L;
        }
        String prodName = String.valueOf(v.get("product_name"));
        String sku = String.valueOf(v.get("sku"));
        double price = ((Number) v.get("price")).doubleValue();
        double total = price * qty;

        long orderId = executeInsertGetId(
                "INSERT INTO orders (order_code, customer_id, store_id, channel, status, payment_status, handled_by, subtotal, total_amount) "
                        + "VALUES (?, ?, ?, 'POS', 'COMPLETED', 'PAID', ?, ?, ?)",
                orderCode, customerId, storeId, cashierId, total, total);
        if (orderId > 0) {
            executeUpdate("INSERT INTO order_items (order_id, variant_id, product_name, sku, quantity, unit_price, line_total) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)", orderId, variantId, prodName, sku, qty, price, total);
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity - ?, updated_at = GETDATE() "
                    + "WHERE variant_id = ? AND warehouse_id = ? AND stock_quantity >= ?", qty, variantId, warehouseId, qty);
            executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id) "
                    + "VALUES (?, ?, 'SALE', ?, 'ORDER', ?)", variantId, warehouseId, -qty, orderId);
            executeUpdate("INSERT INTO cash_drawer_transactions (pos_session_id, txn_type, amount, order_id) VALUES (?, 'SALE', ?, ?)",
                    posSessionId, total, orderId);
        }
        return orderId;
    }

    // UC263: Ghi nhan thu chi phat sinh trong ket tien mat
    public int uc263_recordCashDrawerTxn(int posSessionId, String txnType, double amount, String note) {
        String normType = txnType == null ? "CASH_OUT" : txnType.trim().toUpperCase();
        if ("IN".equals(normType)) {
            normType = "CASH_IN";
        } else if ("OUT".equals(normType)) {
            normType = "CASH_OUT";
        }
        if (!"SALE".equals(normType) && !"CASH_IN".equals(normType) && !"CASH_OUT".equals(normType) && !"REFUND".equals(normType)) {
            normType = "CASH_OUT";
        }
        return executeUpdate("INSERT INTO cash_drawer_transactions (pos_session_id, txn_type, amount, note) VALUES (?, ?, ?, ?)",
                posSessionId, normType, amount, note);
    }

    // UC264: Dong ca ban hang va doi chieu tien mat thuc te
    public int uc264_closePosSession(int posSessionId, double closingCash) {
        String expSql = "SELECT ps.opening_cash + ISNULL(SUM(CASE WHEN d.txn_type IN ('SALE', 'CASH_IN') THEN d.amount "
                + "WHEN d.txn_type IN ('CASH_OUT', 'REFUND') THEN -d.amount ELSE 0 END), 0) AS expected_cash "
                + "FROM pos_sessions ps LEFT JOIN cash_drawer_transactions d ON ps.pos_session_id = d.pos_session_id "
                + "WHERE ps.pos_session_id = ? GROUP BY ps.opening_cash";
        double expected = queryDouble(expSql, posSessionId);
        return executeUpdate("UPDATE pos_sessions SET closing_cash = ?, expected_cash = ?, status = 'CLOSED', closed_at = GETDATE() "
                + "WHERE pos_session_id = ? AND status = 'OPEN'", closingCash, expected, posSessionId);
    }

    // UC265: Bao cao doanh thu theo ca ban hang
    public List<Map<String, Object>> uc265_reportByPosSession(int storeId) {
        String sql = "SELECT ps.pos_session_id, u.full_name AS cashier, ps.status, ps.opened_at, ps.closed_at, "
                + "ISNULL(SUM(CASE WHEN d.txn_type = 'SALE' THEN d.amount ELSE 0 END), 0) AS total_sales, "
                + "ISNULL(SUM(CASE WHEN d.txn_type = 'REFUND' THEN d.amount ELSE 0 END), 0) AS total_refund "
                + "FROM pos_sessions ps JOIN users u ON ps.cashier_id = u.user_id "
                + "LEFT JOIN cash_drawer_transactions d ON ps.pos_session_id = d.pos_session_id "
                + "WHERE (? <= 0 OR ps.store_id = ?) GROUP BY ps.pos_session_id, u.full_name, ps.status, ps.opened_at, ps.closed_at "
                + "ORDER BY ps.pos_session_id DESC";
        return queryRows(sql, storeId, storeId);
    }

    // UC266: Xem lich su ca ban hang theo thu ngan
    public List<Map<String, Object>> uc266_listSessionsByCashier(int cashierId, String statusFilter) {
        String sql = "SELECT pos_session_id, store_id, opening_cash, closing_cash, expected_cash, "
                + "(ISNULL(closing_cash, 0) - ISNULL(expected_cash, 0)) AS difference, status, opened_at, closed_at "
                + "FROM pos_sessions WHERE (? <= 0 OR cashier_id = ?) AND status LIKE ? ORDER BY opened_at DESC";
        return queryRows(sql, cashierId, cashierId, likePattern(statusFilter));
    }
}
