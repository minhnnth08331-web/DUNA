package com.duna.tv2_nguyen_duc_anh.service;

import com.duna.tv2_nguyen_duc_anh.dao.InventoryDAO;
import com.duna.tv2_nguyen_duc_anh.dao.PriceWarehouseDAO;
import com.duna.tv2_nguyen_duc_anh.dao.ProcurementDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M03 (Gia) & M04 (Kho, Nhap hang, Dieu chuyen, Kiem ke) (UC087 - UC113).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class InventoryProcurementService {
    private final PriceWarehouseDAO priceWarehouseDAO = new PriceWarehouseDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final ProcurementDAO procurementDAO = new ProcurementDAO();

    public List<Map<String, Object>> listPriceHistories(int productId, String kw) {
        return priceWarehouseDAO.uc087_getPriceHistories(productId, kw);
    }

    public List<Map<String, Object>> listWarehouses(String kw) {
        return priceWarehouseDAO.uc088_listWarehousesWithStock(kw);
    }

    public int createWarehouse(String code, String name, String addr, String phone) {
        return priceWarehouseDAO.uc089_createWarehouse(code, name, addr, phone);
    }

    public int updateWarehouse(int id, String addr, boolean active) {
        return priceWarehouseDAO.uc090_updateWarehouse(id, addr, active);
    }

    public List<Map<String, Object>> listInventories(int warehouseId) {
        return inventoryDAO.uc091_listVariantInventories(warehouseId);
    }

    public List<Map<String, Object>> searchInventoryBySku(String query) {
        return inventoryDAO.uc092_findInventoryBySkuOrBarcode(query);
    }

    public List<Map<String, Object>> listLowStock() {
        return inventoryDAO.uc093_listLowStockAlerts();
    }

    public int updateThreshold(int variantId, int whId, int threshold) {
        return inventoryDAO.uc094_updateLowStockThreshold(variantId, whId, threshold);
    }

    public int reserveStock(int variantId, int whId, int qty) {
        return inventoryDAO.uc095_reserveStock(variantId, whId, qty);
    }

    public int releaseStock(int variantId, int whId, int qty) {
        return inventoryDAO.uc096_releaseReservedStock(variantId, whId, qty);
    }

    public List<Map<String, Object>> listInventoryLogs(String skuKw) {
        return inventoryDAO.uc097_listInventoryLogs(skuKw);
    }

    public List<Map<String, Object>> filterLogs(String type, String from, String to) {
        return inventoryDAO.uc098_filterLogsByTypeAndDate(type, from, to);
    }

    public int adjustStock(int variantId, int whId, int newQty, int delta, String reason, int userId) {
        return inventoryDAO.uc099_adjustStockManually(variantId, whId, newQty, delta, reason, userId);
    }

    public List<Map<String, Object>> listSuppliers(String kw) {
        return procurementDAO.uc100_listSuppliers(kw);
    }

    public int createSupplier(String code, String name, String contact, String phone, String email, String addr, String tax, String terms) {
        return procurementDAO.uc101_createSupplier(code, name, contact, phone, email, addr, tax, terms);
    }

    public int updateSupplier(int id, String phone, String terms) {
        return procurementDAO.uc102_updateSupplier(id, phone, terms);
    }

    public int deactivateSupplier(int id) {
        return procurementDAO.uc103_deactivateSupplierIfIdle(id);
    }

    public long createPurchaseOrder(String code, int supId, int whId, String expDate, String note, int userId, int varId, int qty, double cost) {
        return procurementDAO.uc104_createPurchaseOrder(code, supId, whId, expDate, note, userId, varId, qty, cost);
    }

    public List<Map<String, Object>> listPurchaseOrders(String status, String poKw) {
        return procurementDAO.uc105_listPurchaseOrders(status, poKw);
    }

    public int confirmPurchaseOrder(int poId) {
        return procurementDAO.uc106_confirmPurchaseOrder(poId);
    }

    public int cancelPurchaseOrder(int poId) {
        return procurementDAO.uc107_cancelPurchaseOrder(poId);
    }

    public long receiveGoodsAndRestock(String code, int poId, int whId, int userId, int varId, int qty, double cost, String note) {
        long rid = procurementDAO.uc108_createGoodsReceiptAndRestock(code, poId, whId, userId, varId, qty, cost, note);
        if (rid > 0) {
            procurementDAO.uc109_updatePurchaseOrderReceiptProgress(poId, varId, qty);
        }
        return rid;
    }

    public long createTransfer(String code, int fromWh, int toWh, int userId, int varId, int qty) {
        return procurementDAO.uc110_createStockTransfer(code, fromWh, toWh, userId, varId, qty);
    }

    public int completeTransfer(int transferId) {
        return procurementDAO.uc111_completeStockTransfer(transferId);
    }

    public long createStocktake(String code, int whId, int userId) {
        return procurementDAO.uc112_createStocktake(code, whId, userId);
    }

    public int completeStocktake(int stocktakeId, int whId) {
        return procurementDAO.uc113_completeStocktake(stocktakeId, whId);
    }
}
