package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M06 - Yeu cau doi tra hang & nhap lai kho (UC147 - UC151).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class ReturnRequestDAO extends BaseDAO {

    // UC147: Khach tao yeu cau doi tra hang kem san pham can doi tra
    public long uc147_createReturnRequest(String returnCode, long orderId, int customerId, String type,
                                          String reason, long orderItemId, int qty, Integer exchangeVariantId, String note) {
        long returnId = executeInsertGetId(
                "INSERT INTO return_requests (return_code, order_id, customer_id, request_type, reason) VALUES (?, ?, ?, ?, ?)",
                returnCode, orderId, customerId, type, reason);
        if (returnId > 0) {
            executeUpdate(
                    "INSERT INTO return_request_items (return_id, order_item_id, quantity, exchange_variant_id, condition_note) "
                            + "VALUES (?, ?, ?, ?, ?)",
                    returnId, orderItemId, qty, exchangeVariantId, note);
        }
        return returnId;
    }

    // UC148: Xem danh sach yeu cau doi tra dang cho xu ly
    public List<Map<String, Object>> uc148_listReturnRequests(String status, String codeKeyword) {
        String sql = "SELECT r.return_id, r.return_code, o.order_code, c.full_name, r.request_type, r.reason, r.status, r.created_at "
                + "FROM return_requests r JOIN orders o ON r.order_id = o.order_id "
                + "LEFT JOIN customers c ON r.customer_id = c.customer_id "
                + "WHERE (? = '' OR r.status = ?) AND r.return_code LIKE ? ORDER BY r.created_at DESC";
        String st = status == null ? "" : status.trim();
        return queryRows(sql, st, st, likePattern(codeKeyword));
    }

    // UC149: Duyet yeu cau doi tra hang cua khach
    public int uc149_approveReturn(int returnId, int staffUserId) {
        return executeUpdate("UPDATE return_requests SET status = 'APPROVED', handled_by = ?, updated_at = GETDATE() WHERE return_id = ? AND status = 'PENDING'",
                staffUserId, returnId);
    }

    // UC150: Tu choi yeu cau doi tra va ghi chu ly do
    public int uc150_rejectReturn(int returnId, int staffUserId, String reasonNote) {
        int updated = executeUpdate("UPDATE return_requests SET status = 'REJECTED', handled_by = ?, updated_at = GETDATE() WHERE return_id = ? AND status = 'PENDING'",
                staffUserId, returnId);
        if (updated > 0) {
            executeUpdate("INSERT INTO order_notes (order_id, user_id, note_content) SELECT order_id, ?, ? FROM return_requests WHERE return_id = ?",
                    staffUserId, reasonNote, returnId);
        }
        return updated;
    }

    // UC151: Nhan hang tra ve va nhap lai kho
    public int uc151_receiveReturnToStock(int returnId, int warehouseId, int variantId, int qty) {
        int updated = executeUpdate("UPDATE return_requests SET status = 'RECEIVED', updated_at = GETDATE() WHERE return_id = ? AND status = 'APPROVED'",
                returnId);
        if (updated > 0) {
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity + ?, updated_at = GETDATE() WHERE variant_id = ? AND warehouse_id = ?",
                    qty, variantId, warehouseId);
            executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id, reason) "
                    + "VALUES (?, ?, 'RETURN', ?, 'RETURN_REQUEST', ?, N'Khách trả hàng còn nguyên tem')",
                    variantId, warehouseId, qty, returnId);
        }
        return updated;
    }
}
