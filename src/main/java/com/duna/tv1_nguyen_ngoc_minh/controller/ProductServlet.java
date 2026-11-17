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
 * Controller M03 - San pham, Bien the SKU/Barcode, Media & Tag (UC064 - UC086).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/ProductServlet", "/variants"})
public class ProductServlet extends BaseServlet {
    private final CatalogProductService productService = new CatalogProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String uc = RequestUtil.getString(req, "uc", "/variants".equals(servletPath) ? "UC076" : "UC064");
        String keyword = RequestUtil.getString(req, "keyword", RequestUtil.getString(req, "search", ""));
        int productId = RequestUtil.getInt(req, "productId", RequestUtil.getInt(req, "id", 1));
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));

        List<Map<String, Object>> rows;
        if ("UC065".equalsIgnoreCase(uc) || !keyword.isEmpty()) {
            rows = productService.searchProducts(keyword);
        } else if ("UC066".equalsIgnoreCase(uc)) {
            int catId = RequestUtil.getInt(req, "categoryId", 0);
            int colId = RequestUtil.getInt(req, "collectionId", 0);
            rows = productService.filterProducts(catId > 0 ? catId : null, colId > 0 ? colId : null,
                    RequestUtil.getDouble(req, "minPrice", 0), RequestUtil.getDouble(req, "maxPrice", 99999999), keyword);
        } else if ("UC074".equalsIgnoreCase(uc)) {
            rows = productService.getTopSelling(10);
        } else if ("UC076".equalsIgnoreCase(uc) || "/variants".equals(servletPath)) {
            rows = productService.listVariants(productId, keyword);
        } else if ("UC081".equalsIgnoreCase(uc)) {
            rows = productService.listMedia(productId);
        } else if ("UC084".equalsIgnoreCase(uc)) {
            rows = productService.listTags(keyword);
        } else {
            rows = productService.listProducts(keyword, (page - 1) * 20, 20);
        }

        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("categories", productService.listCategoryTree());
        req.setAttribute("collections", productService.listCollections(""));
        req.setAttribute("brands", productService.listBrands(""));
        req.setAttribute("colors", productService.listColors());
        req.setAttribute("sizes", productService.listSizes(""));
        req.setAttribute("selectedProductId", productId);
        setResultRows(req, uc, "Quan ly San pham & Bien the (" + uc + ")", rows);
        forwardView(req, resp, "/variants".equals(servletPath) || "UC076".equalsIgnoreCase(uc)
                ? "/WEB-INF/views/tv1_nguyen_ngoc_minh/variants.jsp"
                : "/WEB-INF/views/tv1_nguyen_ngoc_minh/products.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("createProduct".equals(action)) {
            String code = RequestUtil.getString(req, "productCode", "");
            String name = RequestUtil.getString(req, "productName", "");
            String slug = RequestUtil.getString(req, "slug", code.toLowerCase());
            affected = productService.createProduct(code, name, slug,
                    RequestUtil.getInt(req, "categoryId", 1),
                    null, null,
                    RequestUtil.getDouble(req, "basePrice", 350000),
                    RequestUtil.getDouble(req, "costPrice", 170000),
                    RequestUtil.getString(req, "materialInfo", "Cotton"),
                    RequestUtil.getString(req, "formFit", "REGULAR_FIT"),
                    RequestUtil.getString(req, "description", ""),
                    RequestUtil.getString(req, "careInstructions", "Giat nhe"));
        } else if ("updatePrice".equals(action)) {
            affected = productService.updatePrice(
                    RequestUtil.getInt(req, "productId", 0),
                    RequestUtil.getDouble(req, "newPrice", 0),
                    getSessionUserId(req, 1),
                    RequestUtil.getString(req, "reason", "Cap nhat gia"));
        } else if ("toggleStatus".equals(action)) {
            affected = productService.toggleActiveAndFeature(RequestUtil.getInt(req, "productId", 0), true);
        } else if ("addVariant".equals(action)) {
            affected = productService.addUniqueVariant(
                    RequestUtil.getInt(req, "productId", 1),
                    RequestUtil.getInt(req, "colorId", 1),
                    RequestUtil.getInt(req, "sizeId", 2),
                    RequestUtil.getString(req, "sku", ""),
                    RequestUtil.getString(req, "barcode", ""),
                    RequestUtil.getDouble(req, "price", 390000));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/products?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
