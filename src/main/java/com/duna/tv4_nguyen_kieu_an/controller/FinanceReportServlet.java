package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.ExcelExportUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.service.FinanceReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M16 - Chi phi van hanh, Doanh thu, Loi nhuan & Dashboard dieu hanh (UC279 - UC296).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "FinanceReportServlet", urlPatterns = {"/finance"})
public class FinanceReportServlet extends BaseServlet {
    private final FinanceReportService financeService = new FinanceReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC296");
        String keyword = RequestUtil.getString(req, "keyword", "");
        String from = RequestUtil.getSafeDate(req, "fromDate", "2026-01-01");
        String to = RequestUtil.getSafeDate(req, "toDate", "2027-12-31");

        if ("export".equalsIgnoreCase(RequestUtil.getString(req, "action", ""))) {
            ExcelExportUtil.writeExcel(resp, "DUNA_Financial_Report.xlsx", "BaoCaoTaiChinh", financeService.grossProfitByProduct());
            return;
        }
        if ("UC296".equalsIgnoreCase(uc) && wantsJson(req)) {
            writeOkJson(resp, uc, "Dashboard dieu hanh", financeService.getDashboard());
            return;
        }

        List<Map<String, Object>> rows;
        if ("UC279".equalsIgnoreCase(uc)) {
            rows = financeService.listExpenses(from, to, keyword);
        } else if ("UC285".equalsIgnoreCase(uc)) {
            rows = financeService.listDailySummaries(from, to);
        } else if ("UC286".equalsIgnoreCase(uc)) {
            rows = financeService.monthlyRevenue(RequestUtil.getInt(req, "year", 2026));
        } else if ("UC287".equalsIgnoreCase(uc)) {
            rows = financeService.revenueByCategory();
        } else if ("UC288".equalsIgnoreCase(uc)) {
            rows = financeService.revenueByChannelAndStore();
        } else if ("UC290".equalsIgnoreCase(uc)) {
            rows = financeService.grossProfitByProduct();
        } else if ("UC291".equalsIgnoreCase(uc)) {
            rows = financeService.inventoryValuation();
        } else {
            rows = financeService.topVipCustomers();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("dashboard", financeService.getDashboard());
        req.setAttribute("netProfit", financeService.monthlyNetProfit(from, to));
        setResultRows(req, uc, "Tài chính & Báo cáo thống kê (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/finance.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("addExpense".equals(action)) {
            affected = financeService.addExpense(RequestUtil.getInt(req, "categoryId", 1),
                    null, RequestUtil.getDouble(req, "amount", 0),
                    RequestUtil.getSafeDate(req, "expenseDate", "2026-11-17"),
                    RequestUtil.getString(req, "description", ""), getSessionUserId(req, 1));
        } else if ("summarizeYesterday".equals(action)) {
            affected = financeService.summarizeYesterday();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/finance?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
