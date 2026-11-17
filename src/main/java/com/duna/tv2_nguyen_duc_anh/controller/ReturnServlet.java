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
 * Controller M06 - Yeu cau doi tra hang (UC147 - UC151).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "ReturnServlet", urlPatterns = {"/returns"})
public class ReturnServlet extends BaseServlet {
    private final OrderReturnService service = new OrderReturnService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC148");
        List<Map<String, Object>> rows = service.listReturns(RequestUtil.getString(req, "status", ""),
                RequestUtil.getString(req, "keyword", ""));
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Quan ly Doi tra hang (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv2_nguyen_duc_anh/returns.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int returnId = RequestUtil.getInt(req, "returnId", 0);
        int userId = getSessionUserId(req, 1);
        int affected = 0;
        if ("approve".equals(action)) {
            affected = service.approveReturn(returnId, userId);
        } else if ("reject".equals(action)) {
            affected = service.rejectReturn(returnId, userId, RequestUtil.getString(req, "reason", "Tu choi doi tra"));
        } else if ("receive".equals(action)) {
            affected = service.receiveReturn(returnId, RequestUtil.getInt(req, "warehouseId", 1),
                    RequestUtil.getInt(req, "variantId", 1), RequestUtil.getInt(req, "quantity", 1));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/returns?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
