package com.duna.tv2_nguyen_duc_anh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv2_nguyen_duc_anh.service.InventoryProcurementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M04 - Nha cung cap, Nhap hang PO, Dieu chuyen & Kiem ke (UC100 - UC113).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "ProcurementServlet", urlPatterns = {"/procurement"})
public class ProcurementServlet extends BaseServlet {
    private final InventoryProcurementService service = new InventoryProcurementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC100");
        String keyword = RequestUtil.getString(req, "keyword", "");
        String status = RequestUtil.getString(req, "status", "");
        List<Map<String, Object>> rows = "UC105".equalsIgnoreCase(uc)
                ? service.listPurchaseOrders(status, keyword)
                : service.listSuppliers(keyword);
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("suppliers", service.listSuppliers(""));
        req.setAttribute("warehouses", service.listWarehouses(""));
        setResultRows(req, uc, "Nha cung cap & Nhap hang (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv2_nguyen_duc_anh/procurement.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        long res = 0;
        if ("createSupplier".equals(action)) {
            res = service.createSupplier(RequestUtil.getString(req, "supplierCode", ""),
                    RequestUtil.getString(req, "supplierName", ""),
                    RequestUtil.getString(req, "contactPerson", ""),
                    RequestUtil.getString(req, "phone", ""),
                    RequestUtil.getString(req, "email", ""),
                    RequestUtil.getString(req, "address", ""),
                    RequestUtil.getString(req, "taxCode", ""),
                    RequestUtil.getString(req, "paymentTerms", "30 ngay"));
        } else if ("createPO".equals(action)) {
            res = service.createPurchaseOrder(RequestUtil.getString(req, "poCode", "PO-" + System.currentTimeMillis()),
                    RequestUtil.getInt(req, "supplierId", 1),
                    RequestUtil.getInt(req, "warehouseId", 1),
                    RequestUtil.getString(req, "expectedDate", "2026-12-30"),
                    RequestUtil.getString(req, "note", "Nhap hang DUNA"),
                    getSessionUserId(req, 1),
                    RequestUtil.getInt(req, "variantId", 1),
                    RequestUtil.getInt(req, "quantity", 50),
                    RequestUtil.getDouble(req, "unitCost", 190000));
        } else if ("confirmPO".equals(action)) {
            res = service.confirmPurchaseOrder(RequestUtil.getInt(req, "poId", 0));
        } else if ("createStocktake".equals(action)) {
            res = service.createStocktake("KK-" + System.currentTimeMillis(),
                    RequestUtil.getInt(req, "warehouseId", 1), getSessionUserId(req, 1));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", res);
        } else {
            resp.sendRedirect(req.getContextPath() + "/procurement?msg=" + (res > 0 ? "ok" : "skip"));
        }
    }
}
