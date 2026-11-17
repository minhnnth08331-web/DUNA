package com.duna.tv3_le_minh_bach.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M07 - Thanh toan, Cong thanh toan & Doi soat COD (UC159 - UC170).
 * Tac gia: Le Minh Bach - TH08442
 */
public class PaymentDAO extends BaseDAO {

    // UC159: Xem danh sach phuong thuc thanh toan
    public List<Map<String, Object>> uc159_listPaymentMethods(String keyword) {
        String sql = "SELECT method_id, method_code, method_name, is_online, display_order, is_active "
                + "FROM payment_methods WHERE method_name LIKE ? OR method_code LIKE ? ORDER BY display_order";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC160: Them moi phuong thuc thanh toan
    public int uc160_addPaymentMethod(String code, String name, boolean online, int displayOrder) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_code = ?) "
                + "INSERT INTO payment_methods (method_code, method_name, is_online, display_order) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, online ? 1 : 0, displayOrder);
    }

    // UC161: Bat tat phuong thuc thanh toan tren trang dat hang
    public int uc161_togglePaymentMethod(int methodId) {
        return executeUpdate("UPDATE payment_methods SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END WHERE method_id = ?",
                methodId);
    }

    // UC162: Tao ban ghi thanh toan khi khach chon thanh toan COD
    public long uc162_createCodPayment(long orderId, double amount) {
        return executeInsertGetId(
                "INSERT INTO payments (order_id, method_id, amount, status) "
                        + "SELECT ?, method_id, ?, 'PENDING' FROM payment_methods WHERE method_code = 'COD'",
                orderId, amount);
    }

    // UC163 & UC166: Tao yeu cau thanh toan online (VNPay / MoMo) va luu giao dich cho xu ly
    public long uc163_166_createOnlinePayment(long orderId, String methodCode, double amount, String txnRef) {
        long paymentId = executeInsertGetId(
                "INSERT INTO payments (order_id, method_id, amount, status, transaction_ref) "
                        + "SELECT ?, method_id, ?, 'PENDING', ? FROM payment_methods WHERE method_code = ?",
                orderId, amount, txnRef, methodCode);
        if (paymentId > 0) {
            executeUpdate("INSERT INTO payment_transactions (payment_id, gateway, amount, response_code) VALUES (?, ?, ?, NULL)",
                    paymentId, methodCode, amount);
        }
        return paymentId;
    }

    // UC164: Xu ly ket qua thanh toan VNPay thanh cong (IPN callback)
    public int uc164_handlePaymentIpnSuccess(String txnRef, String gateway, String gatewayTxnNo, double amount) {
        Map<String, Object> pay = queryFirstRow(
                "SELECT payment_id, order_id FROM payments WHERE transaction_ref = ? AND status = 'PENDING'", txnRef);
        if (pay == null) {
            return 0;
        }
        long paymentId = ((Number) pay.get("payment_id")).longValue();
        long orderId = ((Number) pay.get("order_id")).longValue();
        executeUpdate("UPDATE payments SET status = 'SUCCESS', paid_at = GETDATE() WHERE payment_id = ?", paymentId);
        executeUpdate("UPDATE orders SET payment_status = 'PAID', status = 'CONFIRMED', updated_at = GETDATE() WHERE order_id = ?", orderId);
        return executeUpdate(
                "INSERT INTO payment_transactions (payment_id, gateway, gateway_txn_no, response_code, amount, raw_response) "
                        + "VALUES (?, ?, ?, '00', ?, N'{\"responseCode\":\"00\"}')",
                paymentId, gateway, gatewayTxnNo, amount);
    }

    // UC165: Xu ly thanh toan that bai hoac het han thanh toan online
    public int uc165_markPaymentFailed(long paymentId, String gateway, String responseCode, double amount) {
        executeUpdate("UPDATE payments SET status = 'FAILED' WHERE payment_id = ?", paymentId);
        return executeUpdate("INSERT INTO payment_transactions (payment_id, gateway, response_code, amount) VALUES (?, ?, ?, ?)",
                paymentId, gateway, responseCode, amount);
    }

    // UC167: Xem lich su cac giao dich thanh toan cua mot don hang
    public List<Map<String, Object>> uc167_listPaymentTransactions(long orderId, String methodKeyword) {
        String sql = "SELECT p.payment_id, m.method_name, p.amount, p.status, t.gateway, t.gateway_txn_no, t.response_code, t.created_at "
                + "FROM payments p JOIN payment_methods m ON p.method_id = m.method_id "
                + "LEFT JOIN payment_transactions t ON p.payment_id = t.payment_id "
                + "WHERE (? <= 0 OR p.order_id = ?) AND m.method_name LIKE ? ORDER BY p.payment_id DESC";
        return queryRows(sql, orderId, orderId, likePattern(methodKeyword));
    }

    // UC168: Tao phien doi soat COD voi don vi van chuyen theo ky
    public int uc168_createCodReconciliation(int carrierId, String periodFrom, String periodTo, int createdBy) {
        String sql = "INSERT INTO cod_reconciliations (carrier_id, period_from, period_to, total_cod, total_fee, net_amount, created_by) "
                + "SELECT carrier_id, ?, ?, ISNULL(SUM(cod_amount), 0), ISNULL(SUM(shipping_fee), 0), "
                + "ISNULL(SUM(cod_amount), 0) - ISNULL(SUM(shipping_fee), 0), ? "
                + "FROM shipments WHERE carrier_id = ? AND status = 'DELIVERED' AND delivered_at BETWEEN ? AND ? GROUP BY carrier_id";
        return executeUpdate(sql, periodFrom, periodTo, createdBy, carrierId, periodFrom, periodTo);
    }

    // UC169: Xac nhan hoan tat doi soat COD va ghi nhan tien ve
    public int uc169_completeCodReconciliation(int reconciliationId) {
        return executeUpdate("UPDATE cod_reconciliations SET status = 'RECONCILED', reconciled_at = GETDATE() "
                + "WHERE reconciliation_id = ? AND status = 'PENDING'", reconciliationId);
    }

    // UC170: Hoan tien qua cong thanh toan cho don da thanh toan online
    public int uc170_refundOnlinePayment(long paymentId, String gateway, String refundTxnNo, double amount) {
        int updated = executeUpdate("UPDATE payments SET status = 'REFUNDED' WHERE payment_id = ? AND status = 'SUCCESS'", paymentId);
        if (updated > 0) {
            executeUpdate("INSERT INTO payment_transactions (payment_id, gateway, gateway_txn_no, response_code, amount) "
                    + "VALUES (?, ?, ?, '00', ?)", paymentId, gateway, refundTxnNo, amount);
        }
        return updated;
    }
}
