package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.ExcelExportUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.dao.PromotionVoucherDAO;
import com.duna.tv3_le_minh_bach.service.PromotionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M09 - Voucher, Khuyen mai & Flash Sale (UC191 - UC210) & Giao dien Quan ly Voucher.
 * Tac gia: Le Minh Bach - TH08442
 */
@WebServlet(name = "PromotionVoucherServlet", urlPatterns = {"/promotions", "/vouchers", "/VoucherServlet"})
public class PromotionVoucherServlet extends BaseServlet {
    private final PromotionService service = new PromotionService();
    private final PromotionVoucherDAO voucherDAO = new PromotionVoucherDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String uc = req.getParameter("uc");
        String action = RequestUtil.getString(req, "action", "");

        if ("toggleStatus".equalsIgnoreCase(action)) {
            int id = RequestUtil.getInt(req, "id", 0);
            if (id > 0) {
                service.toggleVoucher(id);
            }
            resp.sendRedirect(req.getContextPath() + "/VoucherServlet?success=status");
            return;
        }

        // Classic UI view (/VoucherServlet or /vouchers without uc/json)
        if (("/VoucherServlet".equals(servletPath) || "/vouchers".equals(servletPath))
                && (uc == null || uc.isEmpty()) && !wantsJson(req)) {
            String search = RequestUtil.getString(req, "search", "");
            String giamGia = RequestUtil.getString(req, "giamGia", "");
            String trangThai = RequestUtil.getString(req, "trangThai", "");
            List<Map<String, Object>> vouchers = voucherDAO.listClassicVouchers(search, giamGia, trangThai);

            if ("excel".equalsIgnoreCase(req.getParameter("export"))) {
                ExcelExportUtil.writeExcel(resp, "duna_vouchers.xlsx", "Vouchers", vouchers);
                return;
            }

            Map<String, Object> stats = voucherDAO.getClassicVoucherStats();
            req.setAttribute("vouchers", vouchers);
            req.setAttribute("search", search);
            req.setAttribute("giamGia", giamGia);
            req.setAttribute("trangThai", trangThai);
            req.setAttribute("stat_total", stats.get("stat_total"));
            req.setAttribute("stat_available", stats.get("stat_available"));
            req.setAttribute("stat_used", stats.get("stat_used"));
            req.setAttribute("stat_discount", stats.get("stat_discount"));
            forwardView(req, resp, "/quan-ly-voucher.jsp");
            return;
        }

        String resolvedUc = (uc == null || uc.isEmpty()) ? "UC191" : uc;
        String keyword = RequestUtil.getString(req, "keyword", "");

        if ("UC196".equalsIgnoreCase(resolvedUc)) {
            Map<String, Object> valid = service.validateVoucher(keyword,
                    RequestUtil.getDouble(req, "orderAmount", 500000), getSessionUserId(req, 1));
            if (wantsJson(req)) {
                writeOkJson(resp, resolvedUc, valid != null ? "Hop le" : "Khong hop le", valid);
                return;
            }
        }

        List<Map<String, Object>> rows;
        if ("UC198".equalsIgnoreCase(resolvedUc)) {
            rows = service.getVoucherAnalytics();
        } else if ("UC199".equalsIgnoreCase(resolvedUc)) {
            rows = service.listPromotions(keyword);
        } else if ("UC205".equalsIgnoreCase(resolvedUc)) {
            rows = service.listFlashSales(keyword);
        } else if ("UC210".equalsIgnoreCase(resolvedUc)) {
            rows = service.listRunningFlashItems();
        } else {
            rows = service.listVouchers(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, resolvedUc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, resolvedUc, "Voucher, Khuyến mãi & Flash Sale (" + resolvedUc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv3_le_minh_bach/promotions.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String action = RequestUtil.getString(req, "action", "");
        String servletPath = req.getServletPath();
        int affected = 0;

        if ("addCustom".equals(action)) {
            String vipNumber = RequestUtil.getString(req, "vipNumber", "");
            String tenCT = RequestUtil.getString(req, "tenChuongTrinh", "Voucher VIP DUNA");
            double giamGia = RequestUtil.getDouble(req, "giamGia", 100000);
            double donToiThieu = RequestUtil.getDouble(req, "donToiThieu", 500000);
            double giamToiDa = RequestUtil.getDouble(req, "giamToiDa", 0);
            int soLuong = RequestUtil.getInt(req, "soLuong", 1);
            affected = voucherDAO.createClassicVipVoucher("VIP" + vipNumber, tenCT, giamGia, donToiThieu, giamToiDa, soLuong);
            resp.sendRedirect(req.getContextPath() + "/VoucherServlet?" + (affected > 0 ? "success=added" : "error=exist"));
            return;
        } else if ("generateBatch".equals(action)) {
            String prefix = RequestUtil.getString(req, "batchPrefix", "YN100");
            String campaign = RequestUtil.getString(req, "batchTen", "Lô voucher ưu đãi DUNA");
            double amount = RequestUtil.getDouble(req, "batchAmount", 100000);
            double minOrder = RequestUtil.getDouble(req, "batchMinOrder", 0);
            double maxDiscount = RequestUtil.getDouble(req, "batchMaxDiscount", 0);
            int qty = RequestUtil.getInt(req, "batchQty", 10);
            affected = voucherDAO.generateClassicBatchVouchers(prefix, campaign, amount, minOrder, maxDiscount, qty);
            resp.sendRedirect(req.getContextPath() + "/VoucherServlet?success=batch&count=" + affected);
            return;
        } else if ("createVoucher".equals(action)) {
            affected = service.createVoucher(RequestUtil.getString(req, "voucherCode", ""),
                    RequestUtil.getString(req, "voucherName", ""),
                    RequestUtil.getString(req, "discountType", "PERCENT"),
                    RequestUtil.getDouble(req, "discountValue", 10),
                    RequestUtil.getDouble(req, "maxDiscountAmount", 100000),
                    RequestUtil.getDouble(req, "minOrderValue", 300000),
                    RequestUtil.getInt(req, "usageLimit", 500),
                    RequestUtil.getInt(req, "perUserLimit", 1),
                    RequestUtil.getSafeDate(req, "startDate", "2026-11-01"),
                    RequestUtil.getSafeDate(req, "endDate", "2027-03-31"));
        } else if ("toggleVoucher".equals(action)) {
            affected = service.toggleVoucher(RequestUtil.getInt(req, "voucherId", 0));
        } else if ("createPromotion".equals(action)) {
            affected = service.createPromotion(RequestUtil.getString(req, "promotionName", ""),
                    RequestUtil.getString(req, "promoType", "PERCENT"),
                    RequestUtil.getDouble(req, "discountValue", 15),
                    RequestUtil.getSafeDate(req, "startDate", "2026-11-01"),
                    RequestUtil.getSafeDate(req, "endDate", "2027-01-31"),
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
            String target = "/VoucherServlet".equals(servletPath) ? "/VoucherServlet" : "/promotions";
            resp.sendRedirect(req.getContextPath() + target + "?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
