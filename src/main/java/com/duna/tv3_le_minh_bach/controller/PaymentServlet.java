package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.service.PaymentRefundService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M06 (Hoan tien/Doi size) & M07 (Thanh toan, VNPay/MoMo & Doi soat COD) (UC152 - UC170).
 * Tac gia: Le Minh Bach - TH08442
 */
@WebServlet(name = "PaymentServlet", urlPatterns = {"/payments", "/refunds"})
public class PaymentServlet extends BaseServlet {
    private final PaymentRefundService service = new PaymentRefundService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String uc = RequestUtil.getString(req, "uc", "/refunds".equals(servletPath) ? "UC157" : "UC159");
        String keyword = RequestUtil.getString(req, "keyword", "");
        long orderId = RequestUtil.getLong(req, "orderId", 0L);

        List<Map<String, Object>> rows;
        if ("UC157".equalsIgnoreCase(uc)) {
            rows = service.getOrderStatsByStatus();
        } else if ("UC167".equalsIgnoreCase(uc)) {
            rows = service.listTransactions(orderId, keyword);
        } else {
            rows = service.listPaymentMethods(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("paymentMethods", service.listPaymentMethods(""));
        setResultRows(req, uc, "Thanh toán, Hoàn tiền & Đối soát COD (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv3_le_minh_bach/payments.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        long res = 0;
        if ("addMethod".equals(action)) {
            res = service.addPaymentMethod(RequestUtil.getString(req, "methodCode", ""),
                    RequestUtil.getString(req, "methodName", ""),
                    "1".equals(req.getParameter("isOnline")),
                    RequestUtil.getInt(req, "displayOrder", 4));
        } else if ("toggleMethod".equals(action)) {
            res = service.togglePaymentMethod(RequestUtil.getInt(req, "methodId", 0));
        } else if ("createRefund".equals(action)) {
            res = service.createRefund(RequestUtil.getLong(req, "orderId", 1),
                    null, null, RequestUtil.getDouble(req, "amount", 0),
                    RequestUtil.getString(req, "refundMethod", "BANK_TRANSFER"));
        } else if ("completeRefund".equals(action)) {
            res = service.completeRefund(RequestUtil.getInt(req, "refundId", 0));
        } else if ("autoCancel".equals(action)) {
            res = service.autoCancelUnpaidOrders();
        } else if ("autoComplete".equals(action)) {
            res = service.autoCompleteDeliveredOrders();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", res);
        } else {
            resp.sendRedirect(req.getContextPath() + "/payments?msg=" + (res > 0 ? "ok" : "skip"));
        }
    }
}
