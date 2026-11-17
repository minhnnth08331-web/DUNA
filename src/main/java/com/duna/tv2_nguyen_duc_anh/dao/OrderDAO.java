package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

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
        return queryRows(sql, st, st, ch, ch, fromDate, toDate, likePattern(codeKeyword));
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

    // UC138: Hoan tat don hang, tru ton thuc te va cong chi tieu tich luy cho khach
    public int uc138_completeOrder(long orderId) {
        int updated = executeUpdate("UPDATE orders SET status = 'COMPLETED', updated_at = GETDATE() WHERE order_id = ? AND status = 'DELIVERED'", orderId);
        if (updated > 0) {
            executeUpdate("UPDATE customers SET total_spent = total_spent + (SELECT total_amount FROM orders WHERE order_id = ?), "
                    + "total_orders = total_orders + 1 WHERE customer_id = (SELECT customer_id FROM orders WHERE order_id = ?)",
                    orderId, orderId);
            executeUpdate("UPDATE products SET sold_count = sold_count + 1 WHERE product_id IN ("
                    + "SELECT pv.product_id FROM order_items oi JOIN product_variants pv ON oi.variant_id = pv.variant_id WHERE oi.order_id = ?)",
                    orderId);
        }
        return updated;
    }

    // UC139: Huy don hang, ghi ly do va hoan tra ton kho giu cho
    public int uc139_cancelOrder(long orderId, String reasonCode, String reasonDetail, int userId) {
        int updated = executeUpdate("UPDATE orders SET status = 'CANCELLED', updated_at = GETDATE() WHERE order_id = ? AND status IN ('PENDING', 'CONFIRMED')",
                orderId);
        if (updated > 0) {
            executeUpdate("INSERT INTO order_cancellations (order_id, reason_code, reason_detail, cancelled_by) VALUES (?, ?, ?, ?)",
                    orderId, reasonCode, reasonDetail, userId);
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
}
