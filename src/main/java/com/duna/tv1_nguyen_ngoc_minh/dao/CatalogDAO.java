package com.duna.tv1_nguyen_ngoc_minh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M03 - Danh muc, Bo suu tap, Thuong hieu, Mau sac, Size & Tu van size (UC044 - UC063).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class CatalogDAO extends BaseDAO {

    // UC044: Xem cay danh muc san pham da cap (cha - con)
    public List<Map<String, Object>> uc044_listCategoryTree() {
        String sql = "SELECT c.category_id, c.category_name, c.slug, c.parent_id, p.category_name AS parent_name, "
                + "c.description, c.display_order, c.is_active "
                + "FROM categories c LEFT JOIN categories p ON c.parent_id = p.category_id "
                + "ORDER BY ISNULL(c.parent_id, c.category_id), c.parent_id, c.display_order";
        return queryRows(sql);
    }

    // UC045: Them moi danh muc san pham kem slug chuan SEO
    public int uc045_createCategory(String name, String slug, Integer parentId, String description, int displayOrder) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM categories WHERE slug = ?) "
                + "INSERT INTO categories (category_name, slug, parent_id, description, display_order) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql, slug, name, slug, parentId, description, displayOrder);
    }

    // UC046: Cap nhat ten, mo ta va anh dai dien danh muc
    public int uc046_updateCategory(int categoryId, String name, String description, String imageUrl) {
        String sql = "UPDATE categories SET category_name = ?, description = ?, image_url = ? WHERE category_id = ?";
        return executeUpdate(sql, name, description, imageUrl, categoryId);
    }

    // UC047: Xoa danh muc va kiem tra san pham, danh muc con
    public int uc047_deleteCategoryIfUnused(int categoryId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM products WHERE category_id = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM categories WHERE parent_id = ?) "
                + "DELETE FROM categories WHERE category_id = ?";
        return executeUpdate(sql, categoryId, categoryId, categoryId);
    }

    // UC048: Xem danh sach bo suu tap thoi trang theo mua
    public List<Map<String, Object>> uc048_listCollections(String keyword) {
        String sql = "SELECT collection_id, collection_name, season, banner_image_url, start_date, end_date, is_active "
                + "FROM collections WHERE collection_name LIKE ? ORDER BY start_date DESC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC049: Them moi bo suu tap Xuan He / Thu Dong kem banner
    public int uc049_createCollection(String name, String season, String bannerUrl, String desc, String startDate, String endDate) {
        String sql = "INSERT INTO collections (collection_name, season, banner_image_url, description, start_date, end_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, name, season, bannerUrl, desc, startDate, endDate);
    }

    // UC050: Cap nhat thoi gian ap dung va banner bo suu tap
    public int uc050_updateCollection(int collectionId, String endDate, String bannerUrl) {
        String sql = "UPDATE collections SET end_date = ?, banner_image_url = ? WHERE collection_id = ?";
        return executeUpdate(sql, endDate, bannerUrl, collectionId);
    }

    // UC051: Xoa bo suu tap va go lien ket san pham
    public int uc051_deleteCollection(int collectionId) {
        executeUpdate("UPDATE products SET collection_id = NULL WHERE collection_id = ?", collectionId);
        return executeUpdate("DELETE FROM collections WHERE collection_id = ?", collectionId);
    }

    // UC052: Xem danh sach thuong hieu kem so luong san pham
    public List<Map<String, Object>> uc052_listBrands(String keyword) {
        String sql = "SELECT b.brand_id, b.brand_name, b.description, COUNT(p.product_id) AS total_products "
                + "FROM brands b LEFT JOIN products p ON b.brand_id = p.brand_id "
                + "WHERE b.brand_name LIKE ? GROUP BY b.brand_id, b.brand_name, b.description ORDER BY total_products DESC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC053: Them moi thuong hieu hoac dong san pham
    public int uc053_createBrand(String brandName, String logoUrl, String description) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM brands WHERE brand_name = ?) "
                + "INSERT INTO brands (brand_name, logo_url, description) VALUES (?, ?, ?)";
        return executeUpdate(sql, brandName, brandName, logoUrl, description);
    }

    // UC054: Xem bang mau sac chuan kem ma hex
    public List<Map<String, Object>> uc054_listColors() {
        String sql = "SELECT color_id, color_name, hex_code, color_group FROM colors ORDER BY color_group, color_name";
        return queryRows(sql);
    }

    // UC055: Them moi mau sac va ma hex truc quan
    public int uc055_createColor(String colorName, String hexCode, String colorGroup) {
        String sql = "INSERT INTO colors (color_name, hex_code, color_group) VALUES (?, ?, ?)";
        return executeUpdate(sql, colorName, hexCode, colorGroup);
    }

    // UC056: Xoa mau sac va kiem tra bien the dang su dung
    public int uc056_deleteColorIfUnused(int colorId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM product_variants WHERE color_id = ?) DELETE FROM colors WHERE color_id = ?";
        return executeUpdate(sql, colorId, colorId);
    }

    // UC057: Xem danh sach kich co theo loai trang phuc
    public List<Map<String, Object>> uc057_listSizes(String keyword) {
        String sql = "SELECT size_id, size_code, category_type, display_order FROM sizes "
                + "WHERE size_code LIKE ? ORDER BY category_type, display_order ASC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC058: Them moi kich co
    public int uc058_createSize(String sizeCode, String categoryType, int displayOrder) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM sizes WHERE size_code = ? AND category_type = ?) "
                + "INSERT INTO sizes (size_code, category_type, display_order) VALUES (?, ?, ?)";
        return executeUpdate(sql, sizeCode, categoryType, sizeCode, categoryType, displayOrder);
    }

    // UC059: Xoa kich co va kiem tra rang buoc voi bien the san pham
    public int uc059_deleteSizeIfUnused(int sizeId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM product_variants WHERE size_id = ?) DELETE FROM sizes WHERE size_id = ?";
        return executeUpdate(sql, sizeId, sizeId);
    }

    // UC060: Xem bang thong so size chuan theo danh muc
    public List<Map<String, Object>> uc060_getSizeChart(int categoryId) {
        String sql = "SELECT d.chart_detail_id, c.category_name, s.size_code, d.min_height_cm, d.max_height_cm, "
                + "d.min_weight_kg, d.max_weight_kg, d.min_chest_cm, d.max_chest_cm "
                + "FROM size_chart_details d JOIN sizes s ON d.size_id = s.size_id "
                + "JOIN categories c ON d.category_id = c.category_id "
                + "WHERE d.category_id = ? ORDER BY s.display_order";
        return queryRows(sql, categoryId);
    }

    // UC061: Cap nhat khoang chieu cao, can nang, vong nguc trong bang size
    public int uc061_updateSizeChart(int chartDetailId, double minH, double maxH, double minChest, double maxChest) {
        String sql = "UPDATE size_chart_details SET min_height_cm = ?, max_height_cm = ?, min_chest_cm = ?, max_chest_cm = ? "
                + "WHERE chart_detail_id = ?";
        return executeUpdate(sql, minH, maxH, minChest, maxChest, chartDetailId);
    }

    // UC062: Tu van size tu dong theo chieu cao va can nang khach nhap
    public Map<String, Object> uc062_recommendSize(int categoryId, String formFit, double heightCm, double weightKg) {
        String sql = "SELECT TOP 1 s.size_id, s.size_code, r.form_fit FROM size_recommendation_rules r "
                + "JOIN sizes s ON r.recommended_size_id = s.size_id "
                + "WHERE r.category_id = ? AND r.form_fit = ? AND ? BETWEEN r.height_min AND r.height_max "
                + "AND ? BETWEEN r.weight_min AND r.weight_max";
        return queryFirstRow(sql, categoryId, formFit, heightCm, weightKg);
    }

    // UC063: Them moi quy tac tu van size cho form dang Slim fit / Oversize
    public int uc063_addSizeRecommendationRule(int categoryId, String formFit, double hMin, double hMax,
                                               double wMin, double wMax, int recommendedSizeId) {
        String sql = "INSERT INTO size_recommendation_rules (category_id, form_fit, height_min, height_max, weight_min, weight_max, recommended_size_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, categoryId, formFit, hMin, hMax, wMin, wMax, recommendedSizeId);
    }
}
