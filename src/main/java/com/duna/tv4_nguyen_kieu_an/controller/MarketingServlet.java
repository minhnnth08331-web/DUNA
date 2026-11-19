package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.service.MarketingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M12 - Banner, Blog phoi do & Ban tin Newsletter (UC237 - UC246).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "MarketingServlet", urlPatterns = {"/marketing"})
public class MarketingServlet extends BaseServlet {
    private final MarketingService service = new MarketingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC237");
        String keyword = RequestUtil.getString(req, "keyword", "");
        List<Map<String, Object>> rows = "UC241".equalsIgnoreCase(uc)
                ? service.listBlogPosts(keyword)
                : service.listBanners(keyword);
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Nội dung & Marketing Web (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/marketing.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("addBanner".equals(action)) {
            affected = service.addBanner(RequestUtil.getString(req, "title", ""),
                    RequestUtil.getString(req, "imageUrl", "/img/duna-logo.png"),
                    RequestUtil.getString(req, "linkUrl", "/products"),
                    RequestUtil.getString(req, "bannerPosition", "HOME_HERO"),
                    RequestUtil.getInt(req, "displayOrder", 1),
                    RequestUtil.getSafeDate(req, "startDate", "2026-11-01"),
                    RequestUtil.getSafeDate(req, "endDate", "2027-03-31"));
        } else if ("hideExpired".equals(action)) {
            affected = service.hideExpiredBanners();
        } else if ("subscribe".equals(action)) {
            affected = service.subscribeNewsletter(RequestUtil.getString(req, "email", ""));
        } else if ("createPost".equals(action)) {
            affected = service.createDraftPost(null, RequestUtil.getString(req, "title", ""),
                    RequestUtil.getString(req, "slug", "post-" + System.currentTimeMillis()),
                    RequestUtil.getString(req, "summary", ""),
                    RequestUtil.getString(req, "content", ""), "/img/duna-logo.png", getSessionUserId(req, 1));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/marketing?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
