package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv2_nguyen_duc_anh.service.OrderReturnService;
import com.duna.tv4_nguyen_kieu_an.service.StorePosService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M14 - Cua hang, Ca POS & Ban hang tai quay (UC257 - UC266).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "StorePosServlet", urlPatterns = {"/pos", "/stores"})
public class StorePosServlet extends BaseServlet {
    private final StorePosService posService = new StorePosService();
    private final OrderReturnService orderService = new OrderReturnService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String uc = RequestUtil.getString(req, "uc", "/stores".equals(servletPath) ? "UC257" : "UC261");
        String keyword = RequestUtil.getString(req, "keyword", "");
        int storeId = RequestUtil.getInt(req, "storeId", 0);
        int warehouseId = RequestUtil.getInt(req, "warehouseId", 1);

        List<Map<String, Object>> rows;
        if ("UC257".equalsIgnoreCase(uc) || "/stores".equals(servletPath)) {
            rows = posService.listStores(keyword);
        } else if ("UC265".equalsIgnoreCase(uc)) {
            rows = posService.reportBySession(storeId);
        } else if ("UC266".equalsIgnoreCase(uc)) {
            rows = posService.listCashierSessions(RequestUtil.getInt(req, "cashierId", 0), keyword);
        } else {
            rows = posService.scanBarcode(keyword, warehouseId);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("stores", posService.listStores(""));
        req.setAttribute("sessions", posService.listCashierSessions(0, ""));
        setResultRows(req, uc, "Ban hang POS & Cua hang (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/pos.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int userId = getSessionUserId(req, 1);
        long res = 0;
        if ("addStore".equals(action)) {
            res = posService.addStore(RequestUtil.getString(req, "storeCode", ""),
                    RequestUtil.getString(req, "storeName", ""),
                    RequestUtil.getString(req, "address", ""),
                    null, RequestUtil.getString(req, "phone", ""), "08:30", "21:30", 1);
        } else if ("openSession".equals(action)) {
            res = posService.openSession(RequestUtil.getInt(req, "storeId", 1), userId,
                    RequestUtil.getDouble(req, "openingCash", 2000000));
        } else if ("createPosOrder".equals(action)) {
            int custId = RequestUtil.getInt(req, "customerId", 0);
            String code = "POS" + orderService.nextOrderCode().substring(2);
            res = posService.createPosOrder(code, custId > 0 ? custId : null,
                    RequestUtil.getInt(req, "storeId", 1),
                    RequestUtil.getInt(req, "warehouseId", 1),
                    userId, RequestUtil.getInt(req, "sessionId", 1),
                    RequestUtil.getInt(req, "variantId", 1),
                    RequestUtil.getInt(req, "quantity", 1));
        } else if ("closeSession".equals(action)) {
            res = posService.closeSession(RequestUtil.getInt(req, "sessionId", 1),
                    RequestUtil.getDouble(req, "closingCash", 0));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", res);
        } else {
            resp.sendRedirect(req.getContextPath() + "/pos?msg=" + (res > 0 ? "ok" : "skip"));
        }
    }
}
