package com.duna.tv3_le_minh_bach.service;

import com.duna.tv3_le_minh_bach.dao.PaymentDAO;
import com.duna.tv3_le_minh_bach.dao.RefundExportDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M06 (Hoan tien/Doi size) & M07 (Thanh toan & Doi soat COD) (UC152 - UC170).
 * Tac gia: Le Minh Bach - TH08442
 */
public class PaymentRefundService {
    private final RefundExportDAO refundExportDAO = new RefundExportDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public long createRefund(long orderId, Integer returnId, Long paymentId, double amount, String method) {
        return refundExportDAO.uc152_createRefund(orderId, returnId, paymentId, amount, method);
    }

    public int completeRefund(int refundId) {
        return refundExportDAO.uc153_completeRefund(refundId);
    }

    public int processSizeExchange(int returnId, int warehouseId) {
        return refundExportDAO.uc154_processSizeExchange(returnId, warehouseId);
    }

    public int autoCancelUnpaidOrders() {
        return refundExportDAO.uc155_autoCancelUnpaidOnlineOrders();
    }

    public int autoCompleteDeliveredOrders() {
        return refundExportDAO.uc156_autoCompleteDeliveredOrders();
    }

    public List<Map<String, Object>> getOrderStatsByStatus() {
        return refundExportDAO.uc157_getOrderStatsByStatus();
    }

    public List<Map<String, Object>> getOrdersForExport(String fromDate, String toDate) {
        return refundExportDAO.uc158_getOrdersForExport(fromDate, toDate);
    }

    public List<Map<String, Object>> listPaymentMethods(String keyword) {
        return paymentDAO.uc159_listPaymentMethods(keyword);
    }

    public int addPaymentMethod(String code, String name, boolean online, int order) {
        return paymentDAO.uc160_addPaymentMethod(code.toUpperCase(), name, online, order);
    }

    public int togglePaymentMethod(int methodId) {
        return paymentDAO.uc161_togglePaymentMethod(methodId);
    }

    public long createCodPayment(long orderId, double amount) {
        return paymentDAO.uc162_createCodPayment(orderId, amount);
    }

    public long createOnlinePayment(long orderId, String methodCode, double amount, String txnRef) {
        return paymentDAO.uc163_166_createOnlinePayment(orderId, methodCode.toUpperCase(), amount, txnRef);
    }

    public int handleIpnSuccess(String txnRef, String gateway, String gatewayTxnNo, double amount) {
        return paymentDAO.uc164_handlePaymentIpnSuccess(txnRef, gateway, gatewayTxnNo, amount);
    }

    public int markPaymentFailed(long paymentId, String gateway, String code, double amount) {
        return paymentDAO.uc165_markPaymentFailed(paymentId, gateway, code, amount);
    }

    public List<Map<String, Object>> listTransactions(long orderId, String kw) {
        return paymentDAO.uc167_listPaymentTransactions(orderId, kw);
    }

    public int createCodReconciliation(int carrierId, String from, String to, int createdBy) {
        return paymentDAO.uc168_createCodReconciliation(carrierId, from, to, createdBy);
    }

    public int completeCodReconciliation(int reconciliationId) {
        return paymentDAO.uc169_completeCodReconciliation(reconciliationId);
    }

    public int refundOnlinePayment(long paymentId, String gateway, String refundTxnNo, double amount) {
        return paymentDAO.uc170_refundOnlinePayment(paymentId, gateway, refundTxnNo, amount);
    }
}
