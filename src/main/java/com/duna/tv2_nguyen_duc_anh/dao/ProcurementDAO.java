package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M04 - Nha cung cap, Don nhap PO, Phieu nhap, Dieu chuyen & Kiem ke (UC100 - UC113).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class ProcurementDAO extends BaseDAO {

    // UC100: Xem danh sach nha cung cap va xuong may
    public List<Map<String, Object>> uc100_listSuppliers(String keyword) {
        String sql = "SELECT supplier_id, supplier_code, supplier_name, contact_person, phone, email, payment_terms, is_active "
                + "FROM suppliers WHERE supplier_name LIKE ? OR supplier_code LIKE ? ORDER BY supplier_name";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC101: Them moi nha cung cap kem ma so thue
    public int uc101_createSupplier(String code, String name, String contact, String phone, String email,
                                    String address, String taxCode, String paymentTerms) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM suppliers WHERE supplier_code = ?) "
                + "INSERT INTO suppliers (supplier_code, supplier_name, contact_person, phone, email, address, tax_code, payment_terms) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, contact, phone, email, address, taxCode, paymentTerms);
    }

    // UC102: Cap nhat thong tin lien he va dieu khoan thanh toan nha cung cap
    public int uc102_updateSupplier(int supplierId, String phone, String paymentTerms) {
        return executeUpdate("UPDATE suppliers SET phone = ?, payment_terms = ?, updated_at = GETDATE() WHERE supplier_id = ?",
                phone, paymentTerms, supplierId);
    }

    // UC103: Ngung hop tac nha cung cap neu khong co PO dang mo
    public int uc103_deactivateSupplierIfIdle(int supplierId) {
        String sql = "UPDATE suppliers SET is_active = 0, updated_at = GETDATE() "
                + "WHERE supplier_id = ? AND NOT EXISTS (SELECT 1 FROM purchase_orders WHERE supplier_id = ? AND status IN ('DRAFT', 'CONFIRMED', 'PARTIAL'))";
        return executeUpdate(sql, supplierId, supplierId);
    }

    // UC104: Tao don dat hang nha cung cap kem chi tiet bien the
    public long uc104_createPurchaseOrder(String poCode, int supplierId, int warehouseId, String expectedDate,
                                          String note, int userId, int variantId, int qty, double unitCost) {
        long poId = executeInsertGetId(
                "INSERT INTO purchase_orders (po_code, supplier_id, warehouse_id, status, expected_date, note, created_by) "
                        + "VALUES (?, ?, ?, 'DRAFT', ?, ?, ?)",
                poCode, supplierId, warehouseId, expectedDate, note, userId);
        if (poId > 0) {
            executeUpdate("INSERT INTO purchase_order_items (po_id, variant_id, quantity, unit_cost) VALUES (?, ?, ?, ?)",
                    poId, variantId, qty, unitCost);
            executeUpdate("UPDATE purchase_orders SET total_amount = (SELECT SUM(quantity * unit_cost) FROM purchase_order_items WHERE po_id = ?) WHERE po_id = ?",
                    poId, poId);
        }
        return poId;
    }

    // UC105: Xem danh sach don dat hang nha cung cap theo trang thai
    public List<Map<String, Object>> uc105_listPurchaseOrders(String status, String poKeyword) {
        String sql = "SELECT po.po_id, po.po_code, s.supplier_name, w.warehouse_name, po.status, po.total_amount, po.expected_date "
                + "FROM purchase_orders po JOIN suppliers s ON po.supplier_id = s.supplier_id "
                + "JOIN warehouses w ON po.warehouse_id = w.warehouse_id "
                + "WHERE (? = '' OR po.status = ?) AND po.po_code LIKE ? ORDER BY po.po_id DESC";
        String st = status == null ? "" : status.trim();
        return queryRows(sql, st, st, likePattern(poKeyword));
    }

    // UC106: Xac nhan don dat hang gui nha cung cap
    public int uc106_confirmPurchaseOrder(int poId) {
        return executeUpdate("UPDATE purchase_orders SET status = 'CONFIRMED', updated_at = GETDATE() WHERE po_id = ? AND status = 'DRAFT'",
                poId);
    }

    // UC107: Huy don dat hang nha cung cap chua nhan hang
    public int uc107_cancelPurchaseOrder(int poId) {
        String sql = "UPDATE purchase_orders SET status = 'CANCELLED', updated_at = GETDATE() "
                + "WHERE po_id = ? AND status IN ('DRAFT', 'CONFIRMED') AND NOT EXISTS (SELECT 1 FROM goods_receipts WHERE po_id = ?)";
        return executeUpdate(sql, poId, poId);
    }

    // UC108: Lap phieu nhap kho theo don dat hang va cong ton kho
    public long uc108_createGoodsReceiptAndRestock(String receiptCode, int poId, int warehouseId, int userId,
                                                   int variantId, int qty, double unitCost, String note) {
        long receiptId = executeInsertGetId(
                "INSERT INTO goods_receipts (receipt_code, po_id, warehouse_id, received_by, note) VALUES (?, ?, ?, ?, ?)",
                receiptCode, poId, warehouseId, userId, note);
        if (receiptId > 0) {
            executeUpdate("INSERT INTO goods_receipt_items (receipt_id, variant_id, quantity, unit_cost) VALUES (?, ?, ?, ?)",
                    receiptId, variantId, qty, unitCost);
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity + ?, updated_at = GETDATE() WHERE variant_id = ? AND warehouse_id = ?",
                    qty, variantId, warehouseId);
            executeUpdate(
                    "INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id, reason, created_by) "
                            + "VALUES (?, ?, 'RESTOCK', ?, 'GOODS_RECEIPT', ?, ?, ?)",
                    variantId, warehouseId, qty, receiptId, note, userId);
        }
        return receiptId;
    }

    // UC109: Cap nhat so luong da nhan va trang thai don dat hang
    public int uc109_updatePurchaseOrderReceiptProgress(int poId, int variantId, int receivedQty) {
        executeUpdate("UPDATE purchase_order_items SET received_quantity = received_quantity + ? WHERE po_id = ? AND variant_id = ?",
                receivedQty, poId, variantId);
        String sql = "UPDATE purchase_orders SET status = CASE "
                + "WHEN (SELECT ISNULL(SUM(quantity - received_quantity), 0) FROM purchase_order_items WHERE po_id = ?) <= 0 THEN 'RECEIVED' "
                + "ELSE 'PARTIAL' END, updated_at = GETDATE() WHERE po_id = ?";
        return executeUpdate(sql, poId, poId);
    }

    // UC110: Tao phieu dieu chuyen hang giua cac kho
    public long uc110_createStockTransfer(String code, int fromWh, int toWh, int userId, int variantId, int qty) {
        long transferId = executeInsertGetId(
                "INSERT INTO stock_transfers (transfer_code, from_warehouse_id, to_warehouse_id, created_by) VALUES (?, ?, ?, ?)",
                code, fromWh, toWh, userId);
        if (transferId > 0) {
            executeUpdate("INSERT INTO stock_transfer_items (transfer_id, variant_id, quantity) VALUES (?, ?, ?)",
                    transferId, variantId, qty);
        }
        return transferId;
    }

    // UC111: Hoan tat dieu chuyen kho va cap nhat ton hai kho
    public int uc111_completeStockTransfer(int transferId) {
        List<Map<String, Object>> items = queryRows("SELECT variant_id, quantity FROM stock_transfer_items WHERE transfer_id = ?", transferId);
        Map<String, Object> header = queryFirstRow("SELECT from_warehouse_id, to_warehouse_id FROM stock_transfers WHERE transfer_id = ?", transferId);
        if (header == null) {
            return 0;
        }
        int fromWh = ((Number) header.get("from_warehouse_id")).intValue();
        int toWh = ((Number) header.get("to_warehouse_id")).intValue();
        for (Map<String, Object> it : items) {
            int vId = ((Number) it.get("variant_id")).intValue();
            int qty = ((Number) it.get("quantity")).intValue();
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity - ? WHERE variant_id = ? AND warehouse_id = ? AND stock_quantity >= ?",
                    qty, vId, fromWh, qty);
            executeUpdate("UPDATE inventories SET stock_quantity = stock_quantity + ? WHERE variant_id = ? AND warehouse_id = ?",
                    qty, vId, toWh);
            executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id) VALUES (?, ?, 'TRANSFER_OUT', ?, 'STOCK_TRANSFER', ?)",
                    vId, fromWh, -qty, transferId);
            executeUpdate("INSERT INTO inventory_logs (variant_id, warehouse_id, change_type, quantity_changed, reference_type, reference_id) VALUES (?, ?, 'TRANSFER_IN', ?, 'STOCK_TRANSFER', ?)",
                    vId, toWh, qty, transferId);
        }
        return executeUpdate("UPDATE stock_transfers SET status = 'COMPLETED', completed_at = GETDATE() WHERE transfer_id = ?", transferId);
    }

    // UC112: Mo phieu kiem ke va ghi nhan so luong dem thuc te
    public long uc112_createStocktake(String code, int warehouseId, int userId) {
        long stId = executeInsertGetId(
                "INSERT INTO stocktakes (stocktake_code, warehouse_id, created_by) VALUES (?, ?, ?)",
                code, warehouseId, userId);
        if (stId > 0) {
            executeUpdate("INSERT INTO stocktake_items (stocktake_id, variant_id, system_quantity, counted_quantity) "
                    + "SELECT ?, variant_id, stock_quantity, stock_quantity FROM inventories WHERE warehouse_id = ?",
                    stId, warehouseId);
        }
        return stId;
    }

    // UC113: Chot kiem ke, doi chieu chenh lech va dieu chinh ton kho
    public int uc113_completeStocktake(int stocktakeId, int warehouseId) {
        executeUpdate("UPDATE inv SET stock_quantity = si.counted_quantity, updated_at = GETDATE() "
                + "FROM inventories inv JOIN stocktake_items si ON inv.variant_id = si.variant_id "
                + "WHERE si.stocktake_id = ? AND inv.warehouse_id = ?", stocktakeId, warehouseId);
        return executeUpdate("UPDATE stocktakes SET status = 'COMPLETED', completed_at = GETDATE() WHERE stocktake_id = ?", stocktakeId);
    }
}
