package com.duna.thanhvien2_ducanh.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 29: Quản lý nhiều đơn hàng chờ cùng lúc tại quầy (Pending Order Tabs)
 */
public class UC29_QuanLyDonHangChoRepo {
    private static final Map<String, List<Map<String, Object>>> pendingTabs = new ConcurrentHashMap<>();

    public Map<String, Object> createPendingTab(String tabId, List<Map<String, Object>> cart) {
        Map<String, Object> tab = new HashMap<>();
        tab.put("tabId", tabId);
        tab.put("cart", cart);
        tab.put("createdAt", new java.util.Date());
        if (tabId != null && cart != null) {
            pendingTabs.put(tabId, new ArrayList<>(cart));
        }
        return tab;
    }

    public List<Map<String, Object>> getPendingTab(String tabId) {
        return pendingTabs.get(tabId);
    }

    public boolean removePendingTab(String tabId) {
        return pendingTabs.remove(tabId) != null;
    }

    public Map<String, List<Map<String, Object>>> getAllPendingTabs() {
        return new HashMap<>(pendingTabs);
    }
}
