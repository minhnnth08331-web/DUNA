package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.service.LoyaltyEarnService;
import com.duna.tv4_nguyen_kieu_an.service.RewardSupportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M10 - Tich diem thuong & Doi qua (UC211 - UC220).
 * Tac gia: Le Minh Bach - TH08442 (phoi hop TV4 UC219-UC220)
 */
@WebServlet(name = "LoyaltyServlet", urlPatterns = {"/loyalty"})
public class LoyaltyServlet extends BaseServlet {
    private final LoyaltyEarnService earnService = new LoyaltyEarnService();
    private final RewardSupportService rewardService = new RewardSupportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC216");
        int customerId = RequestUtil.getInt(req, "customerId", 0);
        String keyword = RequestUtil.getString(req, "keyword", "");

        if ("UC212".equalsIgnoreCase(uc)) {
            Map<String, Object> bal = earnService.getBalance(customerId > 0 ? customerId : 1);
            if (wantsJson(req)) {
                writeOkJson(resp, uc, "So du diem", bal);
                return;
            }
            req.setAttribute("balance", bal);
        }

        List<Map<String, Object>> rows = "UC218".equalsIgnoreCase(uc)
                ? earnService.listRewards(keyword)
                : earnService.listTransactions(customerId, keyword);
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("rewards", earnService.listRewards(""));
        setResultRows(req, uc, "Tích điểm & Đổi quà (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv3_le_minh_bach/loyalty.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("adjust".equals(action)) {
            affected = earnService.adjustPoints(RequestUtil.getInt(req, "accountId", 1),
                    RequestUtil.getInt(req, "delta", 10), RequestUtil.getString(req, "reason", "Dieu chinh"));
        } else if ("addReward".equals(action)) {
            int vid = RequestUtil.getInt(req, "voucherId", 0);
            affected = rewardService.addReward(RequestUtil.getString(req, "rewardName", ""),
                    RequestUtil.getInt(req, "pointsRequired", 200), vid > 0 ? vid : null,
                    RequestUtil.getInt(req, "stockQuantity", 100));
        } else if ("redeem".equals(action)) {
            affected = rewardService.redeemReward(RequestUtil.getInt(req, "accountId", 1),
                    RequestUtil.getInt(req, "rewardId", 1), RequestUtil.getInt(req, "pointsCost", 200));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/loyalty?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
