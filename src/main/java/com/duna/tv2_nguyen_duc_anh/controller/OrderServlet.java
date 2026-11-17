package com.duna.tv2_nguyen_duc_anh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv2_nguyen_duc_anh.service.OrderReturnService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M06 - Quan ly Don hang & Hoa don ban hang (UC126 - UC146).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/OrderServlet"})
public class OrderServlet extends BaseServlet {
    private final OrderReturnService service = new OrderReturnService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String view = RequestUtil.getString(req, "view", "");
        if ("sell".equalsIgnoreCase(view)) {
            resp.sendRedirect(req.getContextPath() + "/pos");
            return;
        }
        if ("report".equalsIgnoreCase(view)) {
            resp.sendRedirect(req.getContextPath() + "/finance");
            return;
        }
        String uc = RequestUtil.getString(req, "uc", "UC126");
        String keyword = RequestUtil.getString(req, "keyword", "");
        long orderId = RequestUtil.getLong(req, "orderId", 1L);
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));

        List<Map<String, Object>> rows;
        if ("UC127".equalsIgnoreCase(uc) || !keyword.isEmpty()) {
            rows = service.searchOrders(keyword);
        } else if ("UC128".equalsIgnoreCase(uc)) {
            rows = service.filterOrders(RequestUtil.getString(req, "status", ""),
                    RequestUtil.getString(req, "channel", ""), "2026-01-01", "2027-12-31", keyword);
        } else if ("UC130".equalsIgnoreCase(uc)) {
            rows = service.listCustomerOrders(RequestUtil.getInt(req, "customerId", 1), keyword);
        } else if ("UC141".equalsIgnoreCase(uc)) {
            rows = service.listNotes(orderId, keyword);
        } else if ("UC143".equalsIgnoreCase(uc)) {
            rows = service.listStatusHistory(orderId, keyword);
        } else if ("UC145".equalsIgnoreCase(uc)) {
            rows = service.findInvoices(keyword);
        } else {
            rows = service.listOrders(keyword, (page - 1) * 20, 20);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Quan ly Don hang & Hoa don (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv2_nguyen_duc_anh/orders.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        long orderId = RequestUtil.getLong(req, "orderId", 0L);
        int userId = getSessionUserId(req, 1);
        long res = 0;
        if ("createPhoneOrder".equals(action)) {
            res = service.createPhoneOrder(RequestUtil.getInt(req, "customerId", 1), userId,
                    RequestUtil.getString(req, "receiverName", ""), RequestUtil.getString(req, "receiverPhone", ""),
                    RequestUtil.getString(req, "shippingAddress", ""), RequestUtil.getDouble(req, "subtotal", 390000),
                    RequestUtil.getDouble(req, "shippingFee", 30000), RequestUtil.getString(req, "note", ""));
        } else if ("confirm".equals(action)) {
            res = service.confirmOrder(orderId, userId, "Da xac nhan");
        } else if ("pack".equals(action)) {
            res = service.packOrder(orderId, userId);
        } else if ("ship".equals(action)) {
            res = service.shipOrder(orderId, userId);
        } else if ("deliver".equals(action)) {
            res = service.deliverOrder(orderId, userId);
        } else if ("complete".equals(action)) {
            res = service.completeOrder(orderId);
        } else if ("cancel".equals(action)) {
            res = service.cancelOrder(orderId, "STAFF_CANCEL", RequestUtil.getString(req, "reason", "Huy don"), userId);
        } else if ("addNote".equals(action)) {
            res = service.addNote(orderId, userId, RequestUtil.getString(req, "noteContent", ""), true);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", res);
        } else {
            resp.sendRedirect(req.getContextPath() + "/orders?msg=" + (res > 0 ? "ok" : "skip"));
        }
    }
}
