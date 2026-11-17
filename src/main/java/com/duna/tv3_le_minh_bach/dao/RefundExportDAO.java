package com.duna.tv3_le_minh_bach.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M06 - Hoan tien, Doi size, Job don hang & Xuat Excel don hang (UC152 - UC158).
 * Tac gia: Le Minh Bach - TH08442
 */
public class RefundExportDAO extends BaseDAO {

    // UC152: Tao yeu cau hoan tien sau khi nhan hang tra
    public long uc152_createRefund(long orderId, Integer returnId, Long paymentId, double amount, String method) {
        String sql = "INSERT INTO refunds (order_id, return_id, payment_id, amount, refund_method) VALUES (?, ?, ?, ?, ?)";
        return executeInsertGetId(sql, orderId, returnId, paymentId, amount, method);
    }

    // UC153: Hoan tat hoan tien va cap nhat trang thai thanh toan don hang
    public int uc153_completeRefund(int refundId) {
        int updated = executeUpdate("UPDATE refunds SET status = 'COMPLETED', processed_at = GETDATE() WHERE refund_id = ?", refundId);
        if (updated > 0) {
            executeUpdate("UPDATE orders SET payment_status = 'REFUNDED', updated_at = GETDATE() "
                    + "WHERE order_id = (SELECT order_id FROM refunds WHERE refund_id = ?)", refundId);
            executeUpdate("UPDATE return_requests SET status = 'REFUNDED', updated_at = GETDATE() "
                    + "WHERE return_id = (SELECT return_id FROM refunds WHERE refund_id = ?)", refundId);
        }
        return updated;
    }

    // UC154: Xu ly doi hang sang size khac: tru kho size moi va cong kho size cu
    public int uc154_processSizeExchange(int returnId, int warehouseId) {
        List<Map<String, Object>> items = queryRows(
                "SELECT oi.variant_id AS old_variant, ri.exchange_variant_id AS new_variant, ri.quantity "
                        + "FROM return_request_items ri JOIN order_items oi ON ri.order_item_id = oi.order_item_id "
                        + "WHERE ri.return_id = ? AND ri.exchange_variant_id IS NOT NULL",
                returnId);
        for (Map<String, Object> it : items) {
            int oldVar = ((Number) it.get("old_variant")).intValue();
            int newVar = ((Number) it.get("new_variant")).intValue();
            int qty = ((Number) it.get("quantity")).intValue();
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity - ? WHERE variant_id = ? AND warehouse_id = ? AND stock_quantity >= ?",
                    qty, newVar, warehouseId, qty);
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity + ? WHERE variant_id = ? AND warehouse_id = ?",
                    qty, oldVar, warehouseId);
        }
        return executeUpdate("UPDATE return_requests SET status = 'COMPLETED', updated_at = GETDATE() WHERE return_id = ?", returnId);
    }

    // UC155: Tu dong huy don hang qua han chua thanh toan online
    public int uc155_autoCancelUnpaidOnlineOrders() {
        String sql = "UPDATE orders SET status = 'CANCELLED', updated_at = GETDATE() "
                + "WHERE status = 'PENDING' AND payment_status = 'UNPAID' AND ordered_at < DATEADD(HOUR, -24, GETDATE()) "
                + "AND order_id IN (SELECT p.order_id FROM payments p JOIN payment_methods m ON p.method_id = m.method_id WHERE m.is_online = 1)";
        return executeUpdate(sql);
    }

    // UC156: Tu dong hoan tat don hang sau 7 ngay giao thanh cong
    public int uc156_autoCompleteDeliveredOrders() {
        return executeUpdate("UPDATE orders SET status = 'COMPLETED', updated_at = GETDATE() "
                + "WHERE status = 'DELIVERED' AND updated_at < DATEADD(DAY, -7, GETDATE())");
    }

    // UC157: Thong ke so luong va gia tri don hang theo trang thai
    public List<Map<String, Object>> uc157_getOrderStatsByStatus() {
        String sql = "SELECT status, COUNT(*) AS total_orders, ISNULL(SUM(total_amount), 0) AS total_value "
                + "FROM orders GROUP BY status ORDER BY total_orders DESC";
        return queryRows(sql);
    }

    // UC158: Xuat danh sach don hang ra file Excel de doi soat
    public List<Map<String, Object>> uc158_getOrdersForExport(String fromDate, String toDate) {
        String sql = "SELECT o.order_code, o.ordered_at, o.receiver_name, o.receiver_phone, o.channel, "
                + "o.status, o.payment_status, o.subtotal, o.discount_amount, o.shipping_fee, o.total_amount "
                + "FROM orders o WHERE o.ordered_at BETWEEN ? AND ? ORDER BY o.ordered_at DESC";
        return queryRows(sql, fromDate, toDate);
    }
}
