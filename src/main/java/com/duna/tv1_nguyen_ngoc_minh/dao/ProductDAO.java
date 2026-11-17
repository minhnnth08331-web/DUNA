package com.duna.tv1_nguyen_ngoc_minh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M03 - San pham, Bien the SKU/Barcode, Media & Tag (UC064 - UC086).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class ProductDAO extends BaseDAO {

    // UC064: Xem danh sach san pham phan trang va sap xep moi nhat
    public List<Map<String, Object>> uc064_listProductsPaged(String keyword, int offset, int limit) {
        String sql = "SELECT p.product_id, p.product_code, p.product_name, c.category_name, "
                + "ISNULL(b.brand_name, N'DUNA') AS brand_name, p.base_price, p.cost_price, p.form_fit, "
                + "p.is_featured, p.is_active, p.sold_count "
                + "FROM products p JOIN categories c ON p.category_id = c.category_id "
                + "LEFT JOIN brands b ON p.brand_id = b.brand_id "
                + "WHERE p.product_name LIKE ? OR p.product_code LIKE ? "
                + "ORDER BY p.product_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat, Math.max(0, offset), Math.max(1, limit));
    }

    // UC065: Tim kiem san pham theo ma san pham hoac ten
    public List<Map<String, Object>> uc065_searchProducts(String keyword) {
        String sql = "SELECT p.product_id, p.product_code, p.product_name, c.category_name, p.base_price, p.cost_price, p.form_fit, p.is_active "
                + "FROM products p JOIN categories c ON p.category_id = c.category_id "
                + "WHERE p.product_code LIKE ? OR p.product_name LIKE ? ORDER BY p.product_id DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC066: Loc san pham theo danh muc, bo suu tap va khoang gia
    public List<Map<String, Object>> uc066_filterProducts(Integer categoryId, Integer collectionId,
                                                          double minPrice, double maxPrice, String keyword) {
        String sql = "SELECT p.product_id, p.product_code, p.product_name, p.base_price, p.cost_price, c.category_name, p.is_active "
                + "FROM products p JOIN categories c ON p.category_id = c.category_id "
                + "WHERE (? IS NULL OR p.category_id = ?) AND (? IS NULL OR p.collection_id = ?) "
                + "AND p.base_price BETWEEN ? AND ? AND p.is_active = 1 AND p.product_name LIKE ? "
                + "ORDER BY p.product_id DESC";
        return queryRows(sql, categoryId, categoryId, collectionId, collectionId, minPrice, maxPrice, likePattern(keyword));
    }

    // UC067: Xem chi tiet san pham kem danh muc, thuong hieu va bo suu tap
    public Map<String, Object> uc067_getProductDetail(int productId) {
        String sql = "SELECT p.*, c.category_name, b.brand_name, col.collection_name "
                + "FROM products p JOIN categories c ON p.category_id = c.category_id "
                + "LEFT JOIN brands b ON p.brand_id = b.brand_id "
                + "LEFT JOIN collections col ON p.collection_id = col.collection_id WHERE p.product_id = ?";
        return queryFirstRow(sql, productId);
    }

    // UC068: Them moi san pham thoi trang nam voi ma san pham duy nhat
    public int uc068_createProduct(String code, String name, String slug, int categoryId,
                                   Integer collectionId, Integer brandId, double basePrice,
                                   double costPrice, String material, String formFit, String desc, String care) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM products WHERE product_code = ?) "
                + "INSERT INTO products (product_code, product_name, slug, category_id, collection_id, brand_id, "
                + "base_price, cost_price, material_info, form_fit, description, care_instructions) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, slug, categoryId, collectionId, brandId,
                basePrice, costPrice, material, formFit, desc, care);
    }

    // UC069: Cap nhat thong tin, chat lieu, form dang va huong dan giat ui
    public int uc069_updateProductInfo(int productId, String materialInfo, String formFit, String careInstructions) {
        String sql = "UPDATE products SET material_info = ?, form_fit = ?, care_instructions = ?, updated_at = GETDATE() WHERE product_id = ?";
        return executeUpdate(sql, materialInfo, formFit, careInstructions, productId);
    }

    // UC070: Cap nhat gia ban niem yet va luu lich su thay doi gia
    public int uc070_updatePriceWithHistory(int productId, double newPrice, int changedByUserId, String reason) {
        String logSql = "INSERT INTO price_histories (product_id, old_price, new_price, changed_by, reason) "
                + "SELECT product_id, base_price, ?, ?, ? FROM products WHERE product_id = ?";
        executeUpdate(logSql, newPrice, changedByUserId, reason, productId);
        return executeUpdate("UPDATE products SET base_price = ?, updated_at = GETDATE() WHERE product_id = ?", newPrice, productId);
    }

    // UC071: Bat tat trang thai kinh doanh va danh dau san pham noi bat
    public int uc071_toggleActiveAndFeature(int productId, boolean featured) {
        String sql = "UPDATE products SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END, "
                + "is_featured = ?, updated_at = GETDATE() WHERE product_id = ?";
        return executeUpdate(sql, featured ? 1 : 0, productId);
    }

    // UC072: Ngung kinh doanh san pham (xoa mem) va an toan bo bien the
    public int uc072_softDeleteProduct(int productId) {
        int rows = executeUpdate("UPDATE products SET is_active = 0, updated_at = GETDATE() WHERE product_id = ?", productId);
        executeUpdate("UPDATE product_variants SET is_active = 0 WHERE product_id = ?", productId);
        return rows;
    }

    // UC073: Xoa vinh vien san pham chua tao bien the
    public int uc073_deleteProductIfNoVariants(int productId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = ?) DELETE FROM products WHERE product_id = ?";
        return executeUpdate(sql, productId, productId);
    }

    // UC074: Xem top san pham ban chay theo so luong da ban
    public List<Map<String, Object>> uc074_getTopSellingProducts(int topN) {
        String sql = "SELECT TOP (?) product_id, product_code, product_name, sold_count, base_price "
                + "FROM products WHERE is_active = 1 ORDER BY sold_count DESC";
        return queryRows(sql, Math.max(1, topN));
    }

    // UC075: Tang luot xem san pham khi khach mo trang chi tiet
    public int uc075_incrementViewCount(int productId) {
        return executeUpdate("UPDATE products SET view_count = view_count + 1 WHERE product_id = ?", productId);
    }

    // UC076: Xem danh sach bien the mau va size kem ton kho
    public List<Map<String, Object>> uc076_listVariantsWithStock(int productId, String skuKeyword) {
        String sql = "SELECT pv.variant_id, pv.sku, pv.barcode, c.color_name, s.size_code, "
                + "ISNULL(pv.price, p.base_price) AS sell_price, ISNULL(SUM(inv.stock_quantity), 0) AS stock_qty "
                + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id "
                + "WHERE pv.product_id = ? AND pv.sku LIKE ? "
                + "GROUP BY pv.variant_id, pv.sku, pv.barcode, c.color_name, s.size_code, pv.price, p.base_price";
        return queryRows(sql, productId, likePattern(skuKeyword));
    }

    // UC077: Them moi bien the san pham rang buoc duy nhat mau - size
    public int uc077_addUniqueVariant(int productId, int colorId, int sizeId, String sku, String barcode, double price) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = ? AND color_id = ? AND size_id = ?) "
                + "INSERT INTO product_variants (product_id, color_id, size_id, sku, barcode, price) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, productId, colorId, sizeId, productId, colorId, sizeId, sku, barcode, price);
    }

    // UC078: Tu dong sinh ma SKU va barcode cho bien the moi
    public String uc078_generateSkuAndBackfillBarcodes(int productId, int colorId, int sizeId) {
        executeUpdate("UPDATE product_variants SET barcode = '8936' + RIGHT('00000000' + CAST(variant_id AS VARCHAR(8)), 8) WHERE barcode IS NULL");
        Map<String, Object> row = queryFirstRow(
                "SELECT p.product_code + '-' + UPPER(LEFT(c.color_group, 3)) + '-' + s.size_code AS generated_sku "
                        + "FROM products p, colors c, sizes s WHERE p.product_id = ? AND c.color_id = ? AND s.size_id = ?",
                productId, colorId, sizeId);
        return row == null ? "" : String.valueOf(row.get("generated_sku"));
    }

    // UC079: Cap nhat gia ban rieng cho bien the
    public int uc079_updateVariantPrice(int variantId, double price) {
        return executeUpdate("UPDATE product_variants SET price = ? WHERE variant_id = ?", price, variantId);
    }

    // UC080: Xoa bien the chua co ton kho va chua phat sinh don hang
    public int uc080_deleteVariantIfUnused(int variantId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM inventories WHERE variant_id = ? AND stock_quantity > 0) "
                + "AND NOT EXISTS (SELECT 1 FROM order_items WHERE variant_id = ?) "
                + "BEGIN DELETE FROM inventories WHERE variant_id = ?; DELETE FROM product_variants WHERE variant_id = ?; END";
        return executeUpdate(sql, variantId, variantId, variantId, variantId);
    }

    // UC081: Xem thu vien anh va video cua san pham theo mau sac
    public List<Map<String, Object>> uc081_listProductMedia(int productId) {
        String sql = "SELECT pm.media_id, pm.media_type, pm.media_url, pm.is_primary, c.color_name "
                + "FROM product_media pm LEFT JOIN colors c ON pm.color_id = c.color_id "
                + "WHERE pm.product_id = ? ORDER BY pm.is_primary DESC, pm.display_order";
        return queryRows(sql, productId);
    }

    // UC082: Tai len anh san pham gan voi tung mau sac
    public int uc082_addProductMedia(int productId, Integer colorId, String mediaUrl, boolean primary, int displayOrder) {
        String sql = "INSERT INTO product_media (product_id, color_id, media_type, media_url, is_primary, display_order) "
                + "VALUES (?, ?, 'IMAGE', ?, ?, ?)";
        return executeUpdate(sql, productId, colorId, mediaUrl, primary ? 1 : 0, displayOrder);
    }

    // UC083: Dat anh dai dien chinh hien thi tren website
    public int uc083_setPrimaryMedia(int productId, int mediaId) {
        executeUpdate("UPDATE product_media SET is_primary = 0 WHERE product_id = ?", productId);
        return executeUpdate("UPDATE product_media SET is_primary = 1 WHERE media_id = ? AND product_id = ?", mediaId, productId);
    }

    // UC084: Xem danh sach the san pham kem so san pham duoc gan
    public List<Map<String, Object>> uc084_listProductTags(String keyword) {
        String sql = "SELECT t.tag_id, t.tag_name, t.slug, COUNT(m.product_id) AS total_products "
                + "FROM product_tags t LEFT JOIN product_tag_map m ON t.tag_id = m.tag_id "
                + "WHERE t.tag_name LIKE ? GROUP BY t.tag_id, t.tag_name, t.slug ORDER BY total_products DESC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC085: Gan the cho san pham
    public int uc085_assignTagToProduct(int productId, int tagId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM product_tag_map WHERE product_id = ? AND tag_id = ?) "
                + "INSERT INTO product_tag_map (product_id, tag_id) VALUES (?, ?)";
        return executeUpdate(sql, productId, tagId, productId, tagId);
    }

    // UC086: Go the khoi san pham
    public int uc086_removeTagFromProduct(int productId, int tagId) {
        return executeUpdate("DELETE FROM product_tag_map WHERE product_id = ? AND tag_id = ?", productId, tagId);
    }

    // --- Adapter cho giao dien quan-ly-san-pham.jsp (DUNA_MensWear 100 bang) ---
    public List<Map<String, Object>> listClassicProducts(String search, String cat, String status, int offset, int limit) {
        String kw = likePattern(search);
        String c = cat == null ? "" : cat.trim();
        String st = status == null ? "" : status.trim();
        String sql = "SELECT p.product_id AS id, p.product_code AS maSP, p.product_name AS tenSP, "
                + "cat.category_name AS danhMuc, ISNULL(b.brand_name, N'DUNA Basic') AS thuongHieu, "
                + "ISNULL(p.material_info, N'Cotton') AS chatLieu, ISNULL(p.form_fit, 'REGULAR_FIT') AS formDang, "
                + "CAST(p.base_price AS BIGINT) AS giaBan, CAST(p.cost_price AS BIGINT) AS giaNhap, "
                + "ISNULL((SELECT SUM(inv.stock_quantity) FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = p.product_id), 0) AS soLuong, "
                + "ISNULL((SELECT TOP 1 pm.media_url FROM product_media pm WHERE pm.product_id = p.product_id ORDER BY pm.is_primary DESC, pm.media_id ASC), 'img/duna-logo.png') AS hinhAnh, "
                + "ISNULL(p.description, N'') AS moTa, "
                + "CASE WHEN p.is_active = 0 THEN N'Ngừng bán' "
                + "WHEN ISNULL((SELECT SUM(inv.stock_quantity) FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = p.product_id), 0) <= 0 THEN N'Hết hàng' "
                + "ELSE N'Còn hàng' END AS trangThai, "
                + "CONVERT(VARCHAR(10), p.created_at, 120) AS ngayTao "
                + "FROM products p JOIN categories cat ON p.category_id = cat.category_id "
                + "LEFT JOIN brands b ON p.brand_id = b.brand_id "
                + "WHERE (p.product_code LIKE ? OR p.product_name LIKE ? OR ISNULL(p.material_info, '') LIKE ?) "
                + "AND (? = '' OR cat.category_name = ?) "
                + "AND (? = '' "
                + "  OR (? = N'Ngừng bán' AND p.is_active = 0) "
                + "  OR (? = N'Hết hàng' AND p.is_active = 1 AND ISNULL((SELECT SUM(inv.stock_quantity) FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = p.product_id), 0) <= 0) "
                + "  OR (? = N'Còn hàng' AND p.is_active = 1 AND ISNULL((SELECT SUM(inv.stock_quantity) FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = p.product_id), 0) > 0)) "
                + "ORDER BY p.product_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return queryRows(sql, kw, kw, kw, c, c, st, st, st, st, Math.max(0, offset), Math.max(1, limit));
    }

    public int countClassicProducts(String search, String cat, String status) {
        String kw = likePattern(search);
        String c = cat == null ? "" : cat.trim();
        String st = status == null ? "" : status.trim();
        String sql = "SELECT COUNT(*) FROM products p JOIN categories cat ON p.category_id = cat.category_id "
                + "WHERE (p.product_code LIKE ? OR p.product_name LIKE ? OR ISNULL(p.material_info, '') LIKE ?) "
                + "AND (? = '' OR cat.category_name = ?) "
                + "AND (? = '' "
                + "  OR (? = N'Ngừng bán' AND p.is_active = 0) "
                + "  OR (? = N'Hết hàng' AND p.is_active = 1 AND ISNULL((SELECT SUM(inv.stock_quantity) FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = p.product_id), 0) <= 0) "
                + "  OR (? = N'Còn hàng' AND p.is_active = 1 AND ISNULL((SELECT SUM(inv.stock_quantity) FROM product_variants pv JOIN inventories inv ON pv.variant_id = inv.variant_id WHERE pv.product_id = p.product_id), 0) > 0))";
        return queryInt(sql, kw, kw, kw, c, c, st, st, st, st);
    }

    public List<Map<String, Object>> listClassicVariants(int productId) {
        String sql = "SELECT pv.variant_id AS id, pv.variant_id AS idBienThe, c.color_name AS mauSac, "
                + "s.size_code AS trongLuong, s.size_code AS size, pv.sku AS maBienThe, "
                + "ISNULL(NULLIF(pv.barcode, ''), pv.sku) AS barcode, "
                + "CAST(ISNULL(pv.price, p.base_price) AS BIGINT) AS giaBan, "
                + "ISNULL(SUM(inv.stock_quantity), 0) AS soLuong "
                + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "LEFT JOIN inventories inv ON pv.variant_id = inv.variant_id "
                + "WHERE pv.product_id = ? AND pv.is_active = 1 "
                + "GROUP BY pv.variant_id, c.color_name, s.size_code, pv.sku, pv.barcode, pv.price, p.base_price "
                + "ORDER BY c.color_name, s.size_code";
        return queryRows(sql, productId);
    }

    public int saveClassicProduct(String maSP, String tenSP, String catName, String brandName,
                                  double giaBan, double giaNhap, String chatLieu, String formDang,
                                  String hinhAnh, String moTa, boolean active) {
        Integer catId = queryOne("SELECT TOP 1 category_id FROM categories WHERE category_name = ?", rs -> rs.getInt(1), catName);
        if (catId == null) catId = queryOne("SELECT TOP 1 category_id FROM categories ORDER BY category_id", rs -> rs.getInt(1));
        Integer brandId = queryOne("SELECT TOP 1 brand_id FROM brands WHERE brand_name = ?", rs -> rs.getInt(1), brandName);
        if (brandId == null) brandId = queryOne("SELECT TOP 1 brand_id FROM brands ORDER BY brand_id", rs -> rs.getInt(1));
        String safeCode = (maSP == null || maSP.trim().isEmpty()) ? ("SP" + (System.currentTimeMillis() % 100000)) : maSP.trim().toUpperCase();
        String safeForm = ("SLIM_FIT".equalsIgnoreCase(formDang) || "OVERSIZE".equalsIgnoreCase(formDang)) ? formDang.toUpperCase() : "REGULAR_FIT";
        String slug = safeCode.toLowerCase() + "-" + (System.currentTimeMillis() % 10000);
        long pid = executeInsertGetId(
                "INSERT INTO products (product_code, product_name, slug, category_id, brand_id, base_price, cost_price, material_info, form_fit, description, is_active) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                safeCode, tenSP, slug, catId, brandId, giaBan, giaNhap, chatLieu, safeForm, moTa, active ? 1 : 0);
        if (pid > 0 && hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            executeUpdate("INSERT INTO product_media (product_id, media_type, media_url, is_primary, display_order) VALUES (?, 'IMAGE', ?, 1, 1)",
                    pid, hinhAnh.trim());
        }
        return pid > 0 ? (int) pid : 0;
    }

    public int updateClassicProduct(int id, String tenSP, String catName, String brandName,
                                    double giaBan, double giaNhap, String chatLieu, String formDang,
                                    String hinhAnh, String moTa, boolean active) {
        Integer catId = queryOne("SELECT TOP 1 category_id FROM categories WHERE category_name = ?", rs -> rs.getInt(1), catName);
        Integer brandId = queryOne("SELECT TOP 1 brand_id FROM brands WHERE brand_name = ?", rs -> rs.getInt(1), brandName);
        String safeForm = ("SLIM_FIT".equalsIgnoreCase(formDang) || "OVERSIZE".equalsIgnoreCase(formDang)) ? formDang.toUpperCase() : "REGULAR_FIT";
        int updated = executeUpdate(
                "UPDATE products SET product_name = ?, category_id = ISNULL(?, category_id), brand_id = ISNULL(?, brand_id), "
                        + "base_price = ?, cost_price = ?, material_info = ?, form_fit = ?, description = ?, is_active = ?, updated_at = GETDATE() "
                        + "WHERE product_id = ?",
                tenSP, catId, brandId, giaBan, giaNhap, chatLieu, safeForm, moTa, active ? 1 : 0, id);
        if (updated > 0 && hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            executeUpdate("UPDATE product_media SET is_primary = 0 WHERE product_id = ?", id);
            executeUpdate("INSERT INTO product_media (product_id, media_type, media_url, is_primary, display_order) VALUES (?, 'IMAGE', ?, 1, 1)",
                    id, hinhAnh.trim());
        }
        return updated;
    }

    public int addClassicVariant(int productId, String colorName, String sizeCode, double giaBan, int soLuong, String barcode) {
        Integer colorId = queryOne("SELECT TOP 1 color_id FROM colors WHERE color_name = ?", rs -> rs.getInt(1), colorName);
        if (colorId == null) {
            colorId = (int) executeInsertGetId("INSERT INTO colors (color_name, hex_code, color_group) VALUES (?, '#1E293B', 'OTHER')", colorName);
        }
        Integer sizeId = queryOne("SELECT TOP 1 size_id FROM sizes WHERE size_code = ?", rs -> rs.getInt(1), sizeCode);
        if (sizeId == null) {
            sizeId = (int) executeInsertGetId("INSERT INTO sizes (size_code, category_type, display_order) VALUES (?, 'SHIRT', 99)", sizeCode);
        }
        String sku = "SP" + productId + "-" + colorId + "-" + sizeCode.toUpperCase();
        String bc = (barcode == null || barcode.trim().isEmpty()) ? ("8936" + System.currentTimeMillis() % 100000000L) : barcode.trim();
        long vid = executeInsertGetId(
                "IF NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = ? AND color_id = ? AND size_id = ?) "
                        + "INSERT INTO product_variants (product_id, color_id, size_id, sku, barcode, price) VALUES (?, ?, ?, ?, ?, ?)",
                productId, colorId, sizeId, productId, colorId, sizeId, sku, bc, giaBan);
        if (vid > 0) {
            executeUpdate("INSERT INTO inventories (variant_id, warehouse_id, stock_quantity) VALUES (?, (SELECT TOP 1 warehouse_id FROM warehouses ORDER BY warehouse_id), ?)",
                    vid, Math.max(0, soLuong));
            return 1;
        }
        return 0;
    }

    public int editClassicVariant(int variantId, double giaBan, int soLuong, String barcode) {
        executeUpdate("UPDATE product_variants SET price = ?, barcode = CASE WHEN ? <> '' THEN ? ELSE barcode END WHERE variant_id = ?",
                giaBan, barcode == null ? "" : barcode, barcode == null ? "" : barcode, variantId);
        return executeUpdate("UPDATE inventories SET stock_quantity = ?, updated_at = GETDATE() WHERE variant_id = ?",
                Math.max(0, soLuong), variantId);
    }

    public int deleteClassicVariant(int variantId) {
        int refCount = queryInt(
                "SELECT (SELECT COUNT(*) FROM order_items WHERE variant_id = ?) "
                        + "+ (SELECT COUNT(*) FROM cart_items WHERE variant_id = ?) "
                        + "+ (SELECT COUNT(*) FROM purchase_order_items WHERE variant_id = ?) "
                        + "+ (SELECT COUNT(*) FROM stocktake_items WHERE variant_id = ?) "
                        + "+ (SELECT COUNT(*) FROM flash_sale_items WHERE variant_id = ?)",
                variantId, variantId, variantId, variantId, variantId);
        if (refCount == 0) {
            executeUpdate("DELETE FROM inventory_logs WHERE variant_id = ?", variantId);
            executeUpdate("DELETE FROM inventories WHERE variant_id = ?", variantId);
            return executeUpdate("DELETE FROM product_variants WHERE variant_id = ?", variantId);
        }
        executeUpdate("UPDATE inventories SET stock_quantity = 0, updated_at = GETDATE() WHERE variant_id = ?", variantId);
        return executeUpdate("UPDATE product_variants SET is_active = 0 WHERE variant_id = ?", variantId);
    }
}
