package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M04 - Ton kho, Giu cho & Nhat ky xuat nhap ton (UC091 - UC099).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class InventoryDAO extends BaseDAO {

    // UC091: Xem ton kho theo tung bien the size mau va tung kho
    public List<Map<String, Object>> uc091_listVariantInventories(int warehouseId) {
        String sql = "SELECT TOP 200 inv.inventory_id, w.warehouse_name, p.product_code, p.product_name, "
                + "c.color_name, s.size_code, pv.sku, pv.barcode, inv.stock_quantity, inv.reserved_quantity, "
                + "(inv.stock_quantity - inv.reserved_quantity) AS available_qty, inv.low_stock_threshold "
                + "FROM inventories inv JOIN warehouses w ON inv.warehouse_id = w.warehouse_id "
                + "JOIN product_variants pv ON inv.variant_id = pv.variant_id "
                + "JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id "
                + "JOIN sizes s ON pv.size_id = s.size_id "
                + "WHERE (? <= 0 OR inv.warehouse_id = ?) ORDER BY inv.inventory_id DESC";
        return queryRows(sql, warehouseId, warehouseId);
    }

    // UC092: Tra cuu ton kho theo SKU hoac ma vach
    public List<Map<String, Object>> uc092_findInventoryBySkuOrBarcode(String query) {
        String sql = "SELECT pv.variant_id, pv.sku, pv.barcode, p.product_name, w.warehouse_name, "
                + "inv.stock_quantity, inv.reserved_quantity, (inv.stock_quantity - inv.reserved_quantity) AS available_qty "
                + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id "
                + "JOIN inventories inv ON pv.variant_id = inv.variant_id "
                + "JOIN warehouses w ON inv.warehouse_id = w.warehouse_id "
                + "WHERE pv.sku LIKE ? OR pv.barcode = ?";
        return queryRows(sql, likePattern(query), query == null ? "" : query.trim());
    }

    // UC093: Canh bao ton kho duoi nguong an toan
    public List<Map<String, Object>> uc093_listLowStockAlerts() {
        String sql = "SELECT p.product_code, p.product_name, pv.sku, w.warehouse_name, "
                + "inv.stock_quantity, inv.low_stock_threshold "
                + "FROM inventories inv JOIN product_variants pv ON inv.variant_id = pv.variant_id "
                + "JOIN products p ON pv.product_id = p.product_id "
                + "JOIN warehouses w ON inv.warehouse_id = w.warehouse_id "
                + "WHERE inv.stock_quantity <= inv.low_stock_threshold ORDER BY inv.stock_quantity ASC";
        return queryRows(sql);
    }

    // UC094: Cap nhat nguong canh bao het hang cho bien the
    public int uc094_updateLowStockThreshold(int variantId, int warehouseId, int threshold) {
        return executeUpdate(
                "UPDATE inventories SET low_stock_threshold = ?, updated_at = GETDATE() WHERE variant_id = ? AND warehouse_id = ?",
                threshold, variantId, warehouseId);
    }

    // UC095: Giu cho ton kho khi khach dat hang online
    public int uc095_reserveStock(int variantId, int warehouseId, int qty) {
        String sql = "UPDATE inventories SET reserved_quantity = reserved_quantity + ?, updated_at = GETDATE() "
                + "WHERE variant_id = ? AND warehouse_id = ? AND (stock_quantity - reserved_quantity) >= ?";
        return executeUpdate(sql, qty, variantId, warehouseId, qty);
    }

    // UC096: Giai phong so luong giu cho khi don hang bi huy
    public int uc096_releaseReservedStock(int variantId, int warehouseId, int qty) {
        String sql = "UPDATE inventories SET reserved_quantity = CASE WHEN reserved_quantity >= ? THEN reserved_quantity - ? ELSE 0 END, "
                + "updated_at = GETDATE() WHERE variant_id = ? AND warehouse_id = ?";
        return executeUpdate(sql, qty, qty, variantId, warehouseId);
    }

    // UC097: Xem nhat ky xuat nhap ton kho chi tiet
    public List<Map<String, Object>> uc097_listInventoryLogs(String skuKeyword) {
        String sql = "SELECT TOP 150 l.log_id, pv.sku, w.warehouse_name, l.change_type, l.quantity_changed, "
                + "l.reason, u.full_name, l.created_at "
                + "FROM inventory_logs l JOIN product_variants pv ON l.variant_id = pv.variant_id "
                + "LEFT JOIN warehouses w ON l.warehouse_id = w.warehouse_id "
                + "LEFT JOIN users u ON l.created_by = u.user_id "
                + "WHERE pv.sku LIKE ? ORDER BY l.log_id DESC";
        return queryRows(sql, likePattern(skuKeyword));
    }

    // UC098: Loc nhat ky ton kho theo loai thay doi va khoang ngay
    public List<Map<String, Object>> uc098_filterLogsByTypeAndDate(String changeType, String fromDate, String toDate) {
        String sql = "SELECT log_id, variant_id, warehouse_id, change_type, quantity_changed, reason, created_at "
                + "FROM inventory_logs WHERE change_type LIKE ? AND created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        return queryRows(sql, likePattern(changeType), fromDate, toDate);
    }

    // UC099: Dieu chinh ton kho thu cong kem ly do va ghi nhat ky
    public int uc099_adjustStockManually(int variantId, int warehouseId, int newQty, int delta, String reason, int userId) {
        int updated = executeUpdate(
                "UPDATE inventories SET stock_quantity = ?, updated_at = GETDATE() WHERE variant_id = ? AND warehouse_id = ?",
                newQty, variantId, warehouseId);
        if (updated > 0) {
            executeUpdate(
                    "INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reason, created_by) "
                            + "VALUES (?, ?, 'ADJUSTMENT', ?, ?, ?)",
                    variantId, warehouseId, delta, reason, userId);
        }
        return updated;
    }
}
