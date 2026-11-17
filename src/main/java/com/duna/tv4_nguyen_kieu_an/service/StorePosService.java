package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.tv4_nguyen_kieu_an.dao.StorePosDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M14 - Cua hang, Ca POS & Giao dich ket tien (UC257 - UC266).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class StorePosService {
    private final StorePosDAO posDAO = new StorePosDAO();

    public List<Map<String, Object>> listStores(String kw) {
        return posDAO.uc257_listStores(kw);
    }

    public int addStore(String code, String name, String addr, Integer provId, String phone, String open, String close, Integer whId) {
        return posDAO.uc258_addStore(code.toUpperCase(), name, addr, provId, phone, open, close, whId);
    }

    public int updateStore(int storeId, String open, String close, int whId) {
        return posDAO.uc259_updateStoreHoursAndWarehouse(storeId, open, close, whId);
    }

    public long openSession(int storeId, int cashierId, double openingCash) {
        return posDAO.uc260_openPosSession(storeId, cashierId, openingCash);
    }

    public List<Map<String, Object>> scanBarcode(String query, int whId) {
        return posDAO.uc261_scanBarcodeAtPos(query, whId);
    }

    public long createPosOrder(String orderCode, Integer custId, int storeId, int whId, int cashierId, int sessionId, int variantId, int qty) {
        return posDAO.uc262_createPosOrder(orderCode, custId, storeId, whId, cashierId, sessionId, variantId, qty);
    }

    public int recordCashTxn(int sessionId, String txnType, double amount, String note) {
        return posDAO.uc263_recordCashDrawerTxn(sessionId, txnType, amount, note);
    }

    public int closeSession(int sessionId, double closingCash) {
        return posDAO.uc264_closePosSession(sessionId, closingCash);
    }

    public List<Map<String, Object>> reportBySession(int storeId) {
        return posDAO.uc265_reportByPosSession(storeId);
    }

    public List<Map<String, Object>> listCashierSessions(int cashierId, String statusKw) {
        return posDAO.uc266_listSessionsByCashier(cashierId, statusKw);
    }
}
