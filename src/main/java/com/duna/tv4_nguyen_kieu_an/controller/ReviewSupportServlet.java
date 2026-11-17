package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.service.RewardSupportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M11 - Danh gia san pham, Ticket ho tro & FAQ (UC221 - UC236).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "ReviewSupportServlet", urlPatterns = {"/reviews", "/support"})
public class ReviewSupportServlet extends BaseServlet {
    private final RewardSupportService service = new RewardSupportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        boolean isSupport = "/support".equals(servletPath);
        String uc = RequestUtil.getString(req, "uc", isSupport ? "UC230" : "UC224");
        String keyword = RequestUtil.getString(req, "keyword", "");
        int productId = RequestUtil.getInt(req, "productId", 0);
        int ticketId = RequestUtil.getInt(req, "ticketId", 1);

        List<Map<String, Object>> rows;
        if ("UC221".equalsIgnoreCase(uc)) {
            rows = service.listApprovedReviews(productId);
        } else if ("UC228".equalsIgnoreCase(uc)) {
            rows = service.getReviewSummary();
        } else if ("UC230".equalsIgnoreCase(uc) || (isSupport && !"UC233".equalsIgnoreCase(uc) && !"UC235".equalsIgnoreCase(uc))) {
            rows = service.listTickets(keyword);
        } else if ("UC233".equalsIgnoreCase(uc)) {
            rows = service.listTicketMessages(ticketId, keyword);
        } else if ("UC235".equalsIgnoreCase(uc)) {
            rows = service.listFaqs(keyword);
        } else {
            rows = service.listPendingReviews(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, (isSupport ? "Ho tro khach hang" : "Danh gia san pham") + " (" + uc + ")", rows);
        forwardView(req, resp, isSupport
                ? "/WEB-INF/views/tv4_nguyen_kieu_an/support.jsp"
                : "/WEB-INF/views/tv4_nguyen_kieu_an/reviews.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int userId = getSessionUserId(req, 1);
        long res = 0;
        if ("approveReview".equals(action)) {
            res = service.approveReview(RequestUtil.getInt(req, "reviewId", 0));
        } else if ("rejectReview".equals(action)) {
            res = service.rejectReview(RequestUtil.getInt(req, "reviewId", 0));
        } else if ("createTicket".equals(action)) {
            res = service.createTicket("TK-" + System.currentTimeMillis(),
                    null, null, null,
                    RequestUtil.getString(req, "subject", "Ho tro don hang"),
                    RequestUtil.getString(req, "priority", "NORMAL"),
                    userId, RequestUtil.getString(req, "message", ""));
        } else if ("closeTicket".equals(action)) {
            res = service.closeTicket(RequestUtil.getInt(req, "ticketId", 0));
        } else if ("addFaq".equals(action)) {
            res = service.addFaq(RequestUtil.getString(req, "faqGroup", "Chung"),
                    RequestUtil.getString(req, "question", ""),
                    RequestUtil.getString(req, "answer", ""), 1);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", res);
        } else {
            resp.sendRedirect(req.getContextPath() + req.getServletPath() + "?msg=" + (res > 0 ? "ok" : "skip"));
        }
    }
}
