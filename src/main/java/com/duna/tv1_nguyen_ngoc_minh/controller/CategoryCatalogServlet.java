package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.service.CatalogProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M03 - Danh muc, Bo suu tap, Thuong hieu, Mau sac, Size & Tu van size (UC044 - UC063).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "CategoryCatalogServlet", urlPatterns = {"/categories"})
public class CategoryCatalogServlet extends BaseServlet {
    private final CatalogProductService catalogService = new CatalogProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC044");
        String keyword = RequestUtil.getString(req, "keyword", "");
        int catId = RequestUtil.getInt(req, "categoryId", 1);

        if ("UC062".equalsIgnoreCase(uc)) {
            Map<String, Object> rec = catalogService.recommendSize(catId,
                    RequestUtil.getString(req, "formFit", "SLIM_FIT"),
                    RequestUtil.getDouble(req, "heightCm", 172),
                    RequestUtil.getDouble(req, "weightKg", 68));
            if (wantsJson(req)) {
                writeOkJson(resp, uc, "Tu van size", rec);
                return;
            }
            req.setAttribute("recommendedSize", rec);
        }

        List<Map<String, Object>> rows;
        if ("UC048".equalsIgnoreCase(uc)) {
            rows = catalogService.listCollections(keyword);
        } else if ("UC052".equalsIgnoreCase(uc)) {
            rows = catalogService.listBrands(keyword);
        } else if ("UC054".equalsIgnoreCase(uc)) {
            rows = catalogService.listColors();
        } else if ("UC057".equalsIgnoreCase(uc)) {
            rows = catalogService.listSizes(keyword);
        } else if ("UC060".equalsIgnoreCase(uc)) {
            rows = catalogService.getSizeChart(catId);
        } else {
            rows = catalogService.listCategoryTree();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("categories", catalogService.listCategoryTree());
        req.setAttribute("colors", catalogService.listColors());
        req.setAttribute("sizes", catalogService.listSizes(""));
        setResultRows(req, uc, "Danh mục, Thuộc tính & Tư vấn Size (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/categories.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("createCategory".equals(action)) {
            int pId = RequestUtil.getInt(req, "parentId", 0);
            affected = catalogService.createCategory(
                    RequestUtil.getString(req, "categoryName", ""),
                    RequestUtil.getString(req, "slug", ""),
                    pId > 0 ? pId : null,
                    RequestUtil.getString(req, "description", ""),
                    RequestUtil.getInt(req, "displayOrder", 0));
        } else if ("createColor".equals(action)) {
            affected = catalogService.createColor(RequestUtil.getString(req, "colorName", ""),
                    RequestUtil.getString(req, "hexCode", "#0F1E36"), RequestUtil.getString(req, "colorGroup", "BLUE"));
        } else if ("createSize".equals(action)) {
            affected = catalogService.createSize(RequestUtil.getString(req, "sizeCode", ""),
                    RequestUtil.getString(req, "categoryType", "SHIRT"), RequestUtil.getInt(req, "displayOrder", 1));
        } else if ("deleteCategory".equals(action)) {
            affected = catalogService.deleteCategory(RequestUtil.getInt(req, "categoryId", 0));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/categories?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
