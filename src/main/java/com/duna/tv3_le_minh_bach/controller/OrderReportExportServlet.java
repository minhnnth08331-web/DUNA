package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.ExcelExportUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.service.PaymentRefundService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M06 - Xuat Excel doi soat don hang (UC158).
 * Tac gia: Le Minh Bach - TH08442
 */
@WebServlet(name = "OrderReportExportServlet", urlPatterns = {"/order-export"})
public class OrderReportExportServlet extends BaseServlet {
    private final PaymentRefundService service = new PaymentRefundService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fromDate = RequestUtil.getSafeDate(req, "fromDate", "2026-01-01");
        String toDate = RequestUtil.getSafeDate(req, "toDate", "2027-12-31");
        List<Map<String, Object>> rows = service.getOrdersForExport(fromDate, toDate);
        if (wantsJson(req)) {
            writeOkJson(resp, "UC158", "Du lieu xuat Excel", rows);
            return;
        }
        ExcelExportUtil.writeExcel(resp, "DUNA_Orders_Export.xlsx", "DonHang", rows);
    }
}
