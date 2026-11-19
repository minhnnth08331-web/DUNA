package com.duna.tv1_nguyen_ngoc_minh.service;

import com.duna.common.util.BarcodeUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.CatalogDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.ProductDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M03 - Danh muc, Bo suu tap, Thuong hieu, Mau sac, Size & San pham (UC044 - UC086).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class CatalogProductService {
    private final CatalogDAO catalogDAO = new CatalogDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public List<Map<String, Object>> listCategoryTree() {
        return catalogDAO.uc044_listCategoryTree();
    }

    public int createCategory(String name, String slug, Integer parentId, String desc, int order) {
        return catalogDAO.uc045_createCategory(name, slug, parentId, desc, order);
    }

    public int updateCategory(int id, String name, String desc, String imgUrl) {
        return catalogDAO.uc046_updateCategory(id, name, desc, imgUrl);
    }

    public int deleteCategory(int id) {
        return catalogDAO.uc047_deleteCategoryIfUnused(id);
    }

    public List<Map<String, Object>> listCollections(String keyword) {
        return catalogDAO.uc048_listCollections(keyword);
    }

    public int createCollection(String name, String season, String banner, String desc, String start, String end) {
        return catalogDAO.uc049_createCollection(name, season, banner, desc, start, end);
    }

    public int updateCollection(int id, String endDate, String banner) {
        return catalogDAO.uc050_updateCollection(id, endDate, banner);
    }

    public int deleteCollection(int id) {
        return catalogDAO.uc051_deleteCollection(id);
    }

    public List<Map<String, Object>> listBrands(String keyword) {
        return catalogDAO.uc052_listBrands(keyword);
    }

    public int createBrand(String name, String logo, String desc) {
        return catalogDAO.uc053_createBrand(name, logo, desc);
    }

    public List<Map<String, Object>> listColors() {
        return catalogDAO.uc054_listColors();
    }

    public int createColor(String name, String hex, String group) {
        return catalogDAO.uc055_createColor(name, hex, group);
    }

    public int deleteColor(int colorId) {
        return catalogDAO.uc056_deleteColorIfUnused(colorId);
    }

    public List<Map<String, Object>> listSizes(String keyword) {
        return catalogDAO.uc057_listSizes(keyword);
    }

    public int createSize(String code, String categoryType, int order) {
        return catalogDAO.uc058_createSize(code, categoryType, order);
    }

    public int deleteSize(int sizeId) {
        return catalogDAO.uc059_deleteSizeIfUnused(sizeId);
    }

    public List<Map<String, Object>> getSizeChart(int categoryId) {
        return catalogDAO.uc060_getSizeChart(categoryId);
    }

    public int updateSizeChart(int chartId, double minH, double maxH, double minChest, double maxChest) {
        return catalogDAO.uc061_updateSizeChart(chartId, minH, maxH, minChest, maxChest);
    }

    public Map<String, Object> recommendSize(int categoryId, String formFit, double h, double w) {
        return catalogDAO.uc062_recommendSize(categoryId, formFit, h, w);
    }

    public int addSizeRule(int categoryId, String formFit, double hMin, double hMax, double wMin, double wMax, int sizeId) {
        return catalogDAO.uc063_addSizeRecommendationRule(categoryId, formFit, hMin, hMax, wMin, wMax, sizeId);
    }

    public List<Map<String, Object>> listProducts(String keyword, int offset, int limit) {
        return productDAO.uc064_listProductsPaged(keyword, offset, limit);
    }

    public int countProducts(String keyword) {
        return productDAO.uc064_countProducts(keyword);
    }

    public List<Map<String, Object>> searchProducts(String keyword) {
        return productDAO.uc065_searchProducts(keyword);
    }

    public List<Map<String, Object>> filterProducts(Integer catId, Integer colId, double minP, double maxP, String kw) {
        return productDAO.uc066_filterProducts(catId, colId, minP, maxP, kw);
    }

    public Map<String, Object> getProductDetail(int productId) {
        productDAO.uc075_incrementViewCount(productId);
        return productDAO.uc067_getProductDetail(productId);
    }

    public int createProduct(String code, String name, String slug, int catId, Integer colId, Integer brandId,
                             double basePrice, double costPrice, String material, String formFit, String desc, String care) {
        return productDAO.uc068_createProduct(code, name, slug, catId, colId, brandId, basePrice, costPrice, material, formFit, desc, care);
    }

    public int updateProductInfo(int productId, String material, String formFit, String care) {
        return productDAO.uc069_updateProductInfo(productId, material, formFit, care);
    }

    public int updatePrice(int productId, double newPrice, int userId, String reason) {
        return productDAO.uc070_updatePriceWithHistory(productId, newPrice, userId, reason);
    }

    public int toggleActiveAndFeature(int productId, boolean featured) {
        return productDAO.uc071_toggleActiveAndFeature(productId, featured);
    }

    public int softDeleteProduct(int productId) {
        return productDAO.uc072_softDeleteProduct(productId);
    }

    public int deleteProductIfNoVariants(int productId) {
        return productDAO.uc073_deleteProductIfNoVariants(productId);
    }

    public List<Map<String, Object>> getTopSelling(int topN) {
        return productDAO.uc074_getTopSellingProducts(topN);
    }

    public List<Map<String, Object>> listVariants(int productId, String skuKw) {
        return productDAO.uc076_listVariantsWithStock(productId, skuKw);
    }

    public int addUniqueVariant(int productId, int colorId, int sizeId, String sku, String barcode, double price) {
        Map<String, Object> prod = productDAO.uc067_getProductDetail(productId);
        String pCode = (prod != null && prod.get("product_code") != null)
                ? String.valueOf(prod.get("product_code")).trim().toUpperCase()
                : String.format("SP%03d", productId);
        String finalSku = (sku == null || sku.trim().isEmpty())
                ? BarcodeUtil.generateBarcode(pCode, "C" + colorId, "S" + sizeId)
                : sku.trim().toUpperCase();
        String finalBarcode = (barcode == null || barcode.trim().isEmpty())
                ? "8936" + String.format("%04d%02d%02d", productId, colorId, sizeId)
                : barcode.trim();
        return productDAO.uc077_addUniqueVariant(productId, colorId, sizeId, finalSku, finalBarcode, price);
    }

    public String generateSkuAndBackfill(int productId, int colorId, int sizeId) {
        return productDAO.uc078_generateSkuAndBackfillBarcodes(productId, colorId, sizeId);
    }

    public int updateVariantPrice(int variantId, double price) {
        return productDAO.uc079_updateVariantPrice(variantId, price);
    }

    public int deleteVariant(int variantId) {
        return productDAO.uc080_deleteVariantIfUnused(variantId);
    }

    public List<Map<String, Object>> listMedia(int productId) {
        return productDAO.uc081_listProductMedia(productId);
    }

    public int addMedia(int productId, Integer colorId, String url, boolean primary, int order) {
        return productDAO.uc082_addProductMedia(productId, colorId, url, primary, order);
    }

    public int setPrimaryMedia(int productId, int mediaId) {
        return productDAO.uc083_setPrimaryMedia(productId, mediaId);
    }

    public List<Map<String, Object>> listTags(String kw) {
        return productDAO.uc084_listProductTags(kw);
    }

    public int assignTag(int productId, int tagId) {
        return productDAO.uc085_assignTagToProduct(productId, tagId);
    }

    public int removeTag(int productId, int tagId) {
        return productDAO.uc086_removeTagFromProduct(productId, tagId);
    }
}
