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
 * Controller M03 (Gia) & M04 (Kho hang, Ton kho & Nhat ky) (UC087 - UC099).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "WarehouseInventoryServlet", urlPatterns = {"/inventory", "/warehouses"})
public class WarehouseInventoryServlet extends BaseServlet {
    private final InventoryProcurementService service = new InventoryProcurementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String uc = RequestUtil.getString(req, "uc", "/warehouses".equals(servletPath) ? "UC088" : "UC091");
        String keyword = RequestUtil.getString(req, "keyword", "");
        int warehouseId = RequestUtil.getInt(req, "warehouseId", 0);
        int productId = RequestUtil.getInt(req, "productId", 0);

        List<Map<String, Object>> rows;
        if ("UC087".equalsIgnoreCase(uc)) {
            rows = service.listPriceHistories(productId, keyword);
        } else if ("UC088".equalsIgnoreCase(uc) || "/warehouses".equals(servletPath)) {
            rows = service.listWarehouses(keyword);
        } else if ("UC092".equalsIgnoreCase(uc) || !keyword.isEmpty()) {
            rows = service.searchInventoryBySku(keyword);
        } else if ("UC093".equalsIgnoreCase(uc)) {
            rows = service.listLowStock();
        } else if ("UC097".equalsIgnoreCase(uc)) {
            rows = service.listInventoryLogs(keyword);
        } else {
            rows = service.listInventories(warehouseId);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("warehouses", service.listWarehouses(""));
        req.setAttribute("lowStockCount", service.listLowStock().size());
        setResultRows(req, uc, "Quản lý Kho & Tồn kho (" + uc + ")", rows);
        forwardView(req, resp, "/warehouses".equals(servletPath)
                ? "/WEB-INF/views/tv2_nguyen_duc_anh/warehouses.jsp"
                : "/WEB-INF/views/tv2_nguyen_duc_anh/inventory.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("createWarehouse".equals(action)) {
            affected = service.createWarehouse(RequestUtil.getString(req, "warehouseCode", ""),
                    RequestUtil.getString(req, "warehouseName", ""),
                    RequestUtil.getString(req, "address", ""),
                    RequestUtil.getString(req, "phone", ""));
        } else if ("adjustStock".equals(action)) {
            affected = service.adjustStock(RequestUtil.getInt(req, "variantId", 1),
                    RequestUtil.getInt(req, "warehouseId", 1),
                    RequestUtil.getInt(req, "newQty", 10),
                    RequestUtil.getInt(req, "delta", 0),
                    RequestUtil.getString(req, "reason", "Kiem ke"),
                    getSessionUserId(req, 1));
        } else if ("updateThreshold".equals(action)) {
            affected = service.updateThreshold(RequestUtil.getInt(req, "variantId", 1),
                    RequestUtil.getInt(req, "warehouseId", 1),
                    RequestUtil.getInt(req, "threshold", 5));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/inventory?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
