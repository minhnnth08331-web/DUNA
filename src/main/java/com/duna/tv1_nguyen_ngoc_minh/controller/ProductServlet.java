package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.uc.Tv1UcDispatcher;
import com.duna.common.uc.UcRegistry;
import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.ProductDAO;
import com.duna.tv1_nguyen_ngoc_minh.service.CatalogProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller M03 - Quan ly San pham, Bien the SKU/Barcode & Ma tran Size/Mau (UC064 - UC086).
 * Tuong thich 100% giao dien quan-ly-san-pham.jsp va API JSON 300 UC.
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/ProductServlet", "/variants"})
public class ProductServlet extends BaseServlet {
    private final CatalogProductService productService = new CatalogProductService();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        String action = RequestUtil.getString(req, "action", "");
        String uc = RequestUtil.getString(req, "uc", "");

        if ("1".equals(req.getParameter("ajaxDetailAll"))) {
            int pid = RequestUtil.getInt(req, "id", RequestUtil.getInt(req, "productId", 1));
            Map<String, Object> prod = productService.getProductDetail(pid);
            List<Map<String, Object>> variants = productDAO.listClassicVariants(pid);
            List<Map<String, Object>> specs = new ArrayList<>();
            specs.add(spec("Chất liệu vải", prod != null && prod.get("material_info") != null ? String.valueOf(prod.get("material_info")) : "Cotton cao cấp DUNA"));
            specs.add(spec("Form dáng", prod != null && prod.get("form_fit") != null ? String.valueOf(prod.get("form_fit")) : "REGULAR_FIT"));
            specs.add(spec("Thương hiệu", prod != null && prod.get("brand_name") != null ? String.valueOf(prod.get("brand_name")) : "DUNA Men's Wear"));
            specs.add(spec("Hướng dẫn giặt ủi", prod != null && prod.get("care_instructions") != null ? String.valueOf(prod.get("care_instructions")) : "Giặt máy nhẹ, ủi nhiệt độ vừa"));
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("variants", variants);
            payload.put("specs", specs);
            writeJson(resp, 200, payload);
            return;
        }

        if ("uc12".equalsIgnoreCase(action)) {
            int pid = RequestUtil.getInt(req, "id", RequestUtil.getInt(req, "productId", 1));
            List<Map<String, Object>> variants = productDAO.listClassicVariants(pid);
            writeJson(resp, 200, variants);
            return;
        }

        if ("uc16".equalsIgnoreCase(action)) {
            double h = RequestUtil.getDouble(req, "height", 170);
            double w = RequestUtil.getDouble(req, "weight", 65);
            String size = (h >= 178 || w >= 78) ? "XL" : ((h >= 172 || w >= 68) ? "L" : ((h >= 165 || w >= 58) ? "M" : "S"));
            Map<String, Object> res = new LinkedHashMap<>();
            res.put("height", h);
            res.put("weight", w);
            res.put("recommendedSize", size);
            writeJson(resp, 200, res);
            return;
        }

        if ("/variants".equals(servletPath) || "UC076".equalsIgnoreCase(uc)) {
            int productId = RequestUtil.getInt(req, "productId", RequestUtil.getInt(req, "id", 1));
            String kw = RequestUtil.getString(req, "keyword", "");
            List<Map<String, Object>> rows = productService.listVariants(productId, kw);
            if (wantsJson(req)) {
                writeOkJson(resp, "UC076", "Thanh cong", rows);
                return;
            }
            req.setAttribute("colors", productService.listColors());
            req.setAttribute("sizes", productService.listSizes(""));
            req.setAttribute("selectedProductId", productId);
            setResultRows(req, "UC076", "Biến thể SKU & Barcode (UC076)", rows);
            forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/variants.jsp");
            return;
        }

        String search = RequestUtil.getString(req, "search", RequestUtil.getString(req, "keyword", ""));
        if ("/products".equals(servletPath)) {
            String activeUc = uc.isEmpty() ? "UC064" : uc.toUpperCase();
            UcRegistry.UcMeta meta = UcRegistry.findByCode(activeUc);
            List<Map<String, Object>> rows = Tv1UcDispatcher.execute(activeUc, req);
            if (wantsJson(req)) {
                writeOkJson(resp, activeUc, "Thanh cong", rows);
                return;
            }
            req.setAttribute("categories", productService.listCategoryTree());
            req.setAttribute("selectedProductId", RequestUtil.getInt(req, "productId", 1));
            setResultRows(req, activeUc, meta != null ? activeUc + " — " + meta.getTitle() : "Nghiệp vụ Sản phẩm M03", rows);
            forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/products.jsp");
            return;
        }

        String cat = RequestUtil.getString(req, "cat", "");
        String status = RequestUtil.getString(req, "status", "");
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));
        int pageSize = 20;

        List<Map<String, Object>> listSP = productDAO.listClassicProducts(search, cat, status, (page - 1) * pageSize, pageSize);
        int total = productDAO.countClassicProducts(search, cat, status);
        int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));

        if (wantsJson(req)) {
            writeOkJson(resp, "UC064", "Thanh cong", listSP);
            return;
        }
        req.setAttribute("listSP", listSP);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("p_search", search);
        req.setAttribute("p_cat", cat);
        req.setAttribute("p_status", status);
        forwardView(req, resp, "/quan-ly-san-pham.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        boolean jsonOnly = false;

        if ("createProduct".equals(action)) {
            String code = RequestUtil.getString(req, "productCode", "SP-NEW").toUpperCase();
            String name = RequestUtil.getString(req, "productName", "Sản phẩm mới DUNA");
            String slug = code.toLowerCase() + "-" + (System.currentTimeMillis() % 10000L);
            int catId = RequestUtil.getInt(req, "categoryId", 1);
            double baseP = RequestUtil.getDouble(req, "basePrice", 390000);
            double costP = RequestUtil.getDouble(req, "costPrice", 185000);
            String fit = RequestUtil.getString(req, "formFit", "REGULAR_FIT");
            affected = productService.createProduct(code, name, slug, catId, null, 1, baseP, costP, "Cotton", fit, "", "");
        } else if ("add".equals(action)) {
            boolean active = !"Ngừng bán".equals(RequestUtil.getString(req, "trangThai", "Còn hàng"));
            double giaBan = RequestUtil.getDouble(req, "giaBan", 390000);
            int newPid = productDAO.saveClassicProduct(
                    RequestUtil.getString(req, "maSP", RequestUtil.getString(req, "productCode", "")),
                    RequestUtil.getString(req, "tenSP", RequestUtil.getString(req, "productName", "")),
                    RequestUtil.getString(req, "cat", "Áo Sơ Mi Nam"),
                    RequestUtil.getString(req, "thuongHieu", "DUNA Basic"),
                    giaBan,
                    RequestUtil.getDouble(req, "giaNhap", 190000),
                    RequestUtil.getString(req, "chatLieu", "Cotton"),
                    RequestUtil.getString(req, "formDang", "REGULAR_FIT"),
                    RequestUtil.getString(req, "hinhAnh", "img/duna-logo.png"),
                    RequestUtil.getString(req, "moTa", ""),
                    active);
            if (newPid > 0) {
                String defMau = RequestUtil.getString(req, "mauSac", RequestUtil.getString(req, "defMauSac", "Trắng"));
                String defSize = RequestUtil.getString(req, "trongLuong", RequestUtil.getString(req, "defTrongLuong", "L"));
                int defQty = RequestUtil.getInt(req, "soLuong", 20);
                productDAO.addClassicVariant(newPid, defMau, defSize, giaBan, defQty, "");
            }
            affected = newPid > 0 ? 1 : 0;
        } else if ("edit".equals(action)) {
            boolean active = !"Ngừng bán".equals(RequestUtil.getString(req, "trangThai", "Còn hàng"));
            affected = productDAO.updateClassicProduct(
                    RequestUtil.getInt(req, "id", 0),
                    RequestUtil.getString(req, "tenSP", ""),
                    RequestUtil.getString(req, "cat", "Áo Sơ Mi Nam"),
                    RequestUtil.getString(req, "thuongHieu", "DUNA Basic"),
                    RequestUtil.getDouble(req, "giaBan", 390000),
                    RequestUtil.getDouble(req, "giaNhap", 190000),
                    RequestUtil.getString(req, "chatLieu", "Cotton"),
                    RequestUtil.getString(req, "formDang", "REGULAR_FIT"),
                    RequestUtil.getString(req, "hinhAnh", ""),
                    RequestUtil.getString(req, "moTa", ""),
                    active);
        } else if ("delete".equals(action)) {
            affected = productService.softDeleteProduct(RequestUtil.getInt(req, "id", 0));
        } else if ("addVariant".equals(action)) {
            jsonOnly = wantsJson(req) || "1".equals(req.getParameter("ajax"));
            int pid = RequestUtil.getInt(req, "idSP", RequestUtil.getInt(req, "productId", 1));
            String color = RequestUtil.getString(req, "mauSac", "Đen");
            String size = RequestUtil.getString(req, "trongLuong", "M");
            affected = productDAO.addClassicVariant(pid, color, size,
                    RequestUtil.getDouble(req, "giaBan", 390000),
                    RequestUtil.getInt(req, "soLuong", 10), "");
        } else if ("editVariant".equals(action)) {
            jsonOnly = wantsJson(req) || "1".equals(req.getParameter("ajax"));
            int vid = RequestUtil.getInt(req, "idBT", RequestUtil.getInt(req, "id", 0));
            affected = productDAO.editClassicVariant(vid,
                    RequestUtil.getDouble(req, "giaBan", 390000),
                    RequestUtil.getInt(req, "soLuong", 0), "");
        } else if ("deleteVariant".equals(action)) {
            jsonOnly = wantsJson(req) || "1".equals(req.getParameter("ajax"));
            int vid = RequestUtil.getInt(req, "idBT", RequestUtil.getInt(req, "id", 0));
            affected = productDAO.deleteClassicVariant(vid);
        }

        if (jsonOnly || wantsJson(req)) {
            Map<String, Object> out = new LinkedHashMap<>();
            out.put("success", affected > 0);
            out.put("ok", affected > 0);
            out.put("affected", affected);
            writeJson(resp, 200, out);
        } else {
            String target = "/products".equals(req.getServletPath()) ? "/products?success=1" : "/ProductServlet?success=1";
            resp.sendRedirect(req.getContextPath() + target);
        }
    }

    private Map<String, Object> spec(String k, String v) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ten", k);
        m.put("giaTri", v);
        return m;
    }
}
