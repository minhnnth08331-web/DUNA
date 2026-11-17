package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.service.PromotionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M09 - Voucher, Khuyen mai & Flash Sale (UC191 - UC210).
 * Tac gia: Le Minh Bach - TH08442
 */
@WebServlet(name = "PromotionVoucherServlet", urlPatterns = {"/promotions", "/vouchers", "/VoucherServlet"})
public class PromotionVoucherServlet extends BaseServlet {
    private final PromotionService service = new PromotionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC191");
        String keyword = RequestUtil.getString(req, "keyword", "");

        if ("UC196".equalsIgnoreCase(uc)) {
            Map<String, Object> valid = service.validateVoucher(keyword,
                    RequestUtil.getDouble(req, "orderAmount", 500000), getSessionUserId(req, 1));
            if (wantsJson(req)) {
                writeOkJson(resp, uc, valid != null ? "Hop le" : "Khong hop le", valid);
                return;
            }
        }

        List<Map<String, Object>> rows;
        if ("UC198".equalsIgnoreCase(uc)) {
            rows = service.getVoucherAnalytics();
        } else if ("UC199".equalsIgnoreCase(uc)) {
            rows = service.listPromotions(keyword);
        } else if ("UC205".equalsIgnoreCase(uc)) {
            rows = service.listFlashSales(keyword);
        } else if ("UC210".equalsIgnoreCase(uc)) {
            rows = service.listRunningFlashItems();
        } else {
            rows = service.listVouchers(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Voucher, Khuyen mai & Flash Sale (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv3_le_minh_bach/promotions.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("createVoucher".equals(action)) {
            affected = service.createVoucher(RequestUtil.getString(req, "voucherCode", ""),
                    RequestUtil.getString(req, "voucherName", ""),
                    RequestUtil.getString(req, "discountType", "PERCENT"),
                    RequestUtil.getDouble(req, "discountValue", 10),
                    RequestUtil.getDouble(req, "maxDiscountAmount", 100000),
                    RequestUtil.getDouble(req, "minOrderValue", 300000),
                    RequestUtil.getInt(req, "usageLimit", 500),
                    RequestUtil.getInt(req, "perUserLimit", 1),
                    RequestUtil.getString(req, "startDate", "2026-11-01"),
                    RequestUtil.getString(req, "endDate", "2027-03-31"));
        } else if ("toggleVoucher".equals(action)) {
            affected = service.toggleVoucher(RequestUtil.getInt(req, "voucherId", 0));
        } else if ("createPromotion".equals(action)) {
            affected = service.createPromotion(RequestUtil.getString(req, "promotionName", ""),
                    RequestUtil.getString(req, "promoType", "PERCENT"),
                    RequestUtil.getDouble(req, "discountValue", 15),
                    RequestUtil.getString(req, "startDate", "2026-11-01"),
                    RequestUtil.getString(req, "endDate", "2027-01-31"),
                    RequestUtil.getString(req, "bannerUrl", ""));
        } else if ("createFlashSale".equals(action)) {
            affected = service.createFlashSale(RequestUtil.getString(req, "title", ""),
                    RequestUtil.getString(req, "startTime", "2026-11-27 20:00"),
                    RequestUtil.getString(req, "endTime", "2026-11-27 22:00"));
        } else if ("syncFlash".equals(action)) {
            affected = service.syncFlashSaleStatuses();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/promotions?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
