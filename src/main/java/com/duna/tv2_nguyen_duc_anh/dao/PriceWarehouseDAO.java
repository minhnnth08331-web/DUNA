package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M03 & M04 - Lich su gia & Kho hang (UC087 - UC090).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class PriceWarehouseDAO extends BaseDAO {

    // UC087: Xem lich su thay doi gia cua san pham
    public List<Map<String, Object>> uc087_getPriceHistories(int productId, String keyword) {
        String sql = "SELECT h.price_history_id, p.product_name, h.old_price, h.new_price, "
                + "u.full_name AS changed_by_name, h.reason, h.changed_at "
                + "FROM price_histories h JOIN products p ON h.product_id = p.product_id "
                + "LEFT JOIN users u ON h.changed_by = u.user_id "
                + "WHERE (? <= 0 OR h.product_id = ?) AND p.product_name LIKE ? ORDER BY h.changed_at DESC";
        return queryRows(sql, productId, productId, likePattern(keyword));
    }

    // UC088: Xem danh sach kho hang kem tong so luong ton tung kho
    public List<Map<String, Object>> uc088_listWarehousesWithStock(String keyword) {
        String sql = "SELECT w.warehouse_id, w.warehouse_code, w.warehouse_name, w.address, w.phone, w.is_active, "
                + "ISNULL(SUM(i.stock_quantity), 0) AS total_stock "
                + "FROM warehouses w LEFT JOIN inventories i ON w.warehouse_id = i.warehouse_id "
                + "WHERE w.warehouse_name LIKE ? OR w.warehouse_code LIKE ? "
                + "GROUP BY w.warehouse_id, w.warehouse_code, w.warehouse_name, w.address, w.phone, w.is_active";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC089: Them moi kho hang
    public int uc089_createWarehouse(String code, String name, String address, String phone) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM warehouses WHERE warehouse_code = ?) "
                + "INSERT INTO warehouses (warehouse_code, warehouse_name, address, phone) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, address, phone);
    }

    // UC090: Cap nhat thong tin hoac ngung hoat dong kho hang
    public int uc090_updateWarehouse(int warehouseId, String address, boolean active) {
        return executeUpdate("UPDATE warehouses SET address = ?, is_active = ? WHERE warehouse_id = ?",
                address, active ? 1 : 0, warehouseId);
    }
}
