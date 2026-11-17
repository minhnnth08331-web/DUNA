package com.duna.tv2_nguyen_duc_anh.service;

import com.duna.tv2_nguyen_duc_anh.dao.OrderDAO;
import com.duna.tv2_nguyen_duc_anh.dao.ReturnRequestDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M06 - Don hang, Hoa don & Yeu cau doi tra (UC126 - UC151).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class OrderReturnService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();

    public List<Map<String, Object>> listOrders(String keyword, int offset, int limit) {
        return orderDAO.uc126_listOrdersPaged(keyword, offset, limit);
    }

    public List<Map<String, Object>> searchOrders(String keyword) {
        return orderDAO.uc127_searchOrders(keyword);
    }

    public List<Map<String, Object>> filterOrders(String status, String channel, String from, String to, String kw) {
        return orderDAO.uc128_filterOrders(status, channel, from, to, kw);
    }

    public Map<String, Object> getOrderHeader(long orderId) {
        return orderDAO.uc129_getOrderHeader(orderId);
    }

    public List<Map<String, Object>> getOrderItems(long orderId) {
        return orderDAO.uc129_getOrderItems(orderId);
    }

    public List<Map<String, Object>> listCustomerOrders(int customerId, String kw) {
        return orderDAO.uc130_listCustomerOrders(customerId, kw);
    }

    public String nextOrderCode() {
        return orderDAO.uc131_generateNextOrderCode();
    }

    public long placeOrderFromCart(int customerId, int userId, String name, String phone, String addr,
                                   double subtotal, double shipFee, int cartId) {
        return orderDAO.uc132_placeOrderFromCart(nextOrderCode(), customerId, userId, name, phone, addr, subtotal, shipFee, cartId);
    }

    public long createPhoneOrder(int customerId, int staffId, String name, String phone, String addr,
                                 double subtotal, double shipFee, String note) {
        return orderDAO.uc133_createPhoneOrder(nextOrderCode(), customerId, staffId, name, phone, addr, subtotal, shipFee, note);
    }

    public int confirmOrder(long orderId, int staffId, String note) {
        return orderDAO.uc134_136_transitionOrderStatus(orderId, "PENDING", "CONFIRMED", staffId, note);
    }

    public int packOrder(long orderId, int staffId) {
        return orderDAO.uc134_136_transitionOrderStatus(orderId, "CONFIRMED", "PACKING", staffId, "Dang dong goi");
    }

    public int shipOrder(long orderId, int staffId) {
        return orderDAO.uc134_136_transitionOrderStatus(orderId, "PACKING", "SHIPPING", staffId, "Da giao DVVC");
    }

    public int deliverOrder(long orderId, int staffId) {
        return orderDAO.uc137_markDelivered(orderId, staffId);
    }

    public int completeOrder(long orderId) {
        return orderDAO.uc138_completeOrder(orderId);
    }

    public int cancelOrder(long orderId, String reasonCode, String reasonDetail, int userId) {
        return orderDAO.uc139_cancelOrder(orderId, reasonCode, reasonDetail, userId);
    }

    public int addNote(long orderId, int userId, String content, boolean internal) {
        return orderDAO.uc140_addOrderNote(orderId, userId, content, internal);
    }

    public List<Map<String, Object>> listNotes(long orderId, String kw) {
        return orderDAO.uc141_listOrderNotes(orderId, kw);
    }

    public int updateShippingAddress(long orderId, String name, String phone, String addr) {
        return orderDAO.uc142_updateShippingAddress(orderId, name, phone, addr);
    }

    public List<Map<String, Object>> listStatusHistory(long orderId, String kw) {
        return orderDAO.uc143_listStatusHistory(orderId, kw);
    }

    public int issueInvoice(String invNo, long orderId, String buyer, String company, String tax, String addr) {
        return orderDAO.uc144_issueInvoice(invNo, orderId, buyer, company, tax, addr);
    }

    public List<Map<String, Object>> findInvoices(String query) {
        return orderDAO.uc145_findInvoices(query);
    }

    public int cancelInvoice(int invoiceId) {
        return orderDAO.uc146_cancelInvoice(invoiceId);
    }

    public long createReturnRequest(String code, long orderId, int customerId, String type, String reason,
                                    long orderItemId, int qty, Integer exchangeVarId, String note) {
        return returnDAO.uc147_createReturnRequest(code, orderId, customerId, type, reason, orderItemId, qty, exchangeVarId, note);
    }

    public List<Map<String, Object>> listReturns(String status, String kw) {
        return returnDAO.uc148_listReturnRequests(status, kw);
    }

    public int approveReturn(int returnId, int staffId) {
        return returnDAO.uc149_approveReturn(returnId, staffId);
    }

    public int rejectReturn(int returnId, int staffId, String note) {
        return returnDAO.uc150_rejectReturn(returnId, staffId, note);
    }

    public int receiveReturn(int returnId, int whId, int varId, int qty) {
        return returnDAO.uc151_receiveReturnToStock(returnId, whId, varId, qty);
    }
}
