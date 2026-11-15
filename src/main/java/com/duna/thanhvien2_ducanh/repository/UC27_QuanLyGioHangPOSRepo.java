package com.duna.thanhvien2_ducanh.repository;

import java.util.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 27: Quản lý giỏ hàng POS (Thêm món, chỉnh số lượng, xóa món)
 */
public class UC27_QuanLyGioHangPOSRepo {

    public List<Map<String, Object>> capNhatGioHang(List<Map<String, Object>> cart, int idBienThe, int newQuantity) {
        if (cart == null) cart = new ArrayList<>();
        if (newQuantity <= 0) {
            cart.removeIf(item -> ((Integer) item.get("idBienThe")) == idBienThe);
        } else {
            for (Map<String, Object> item : cart) {
                if (((Integer) item.get("idBienThe")) == idBienThe) {
                    item.put("soLuong", newQuantity);
                    long donGia = (Long) item.get("donGia");
                    item.put("thanhTien", donGia * newQuantity);
                    return cart;
                }
            }
        }
        return cart;
    }

    public List<Map<String, Object>> themVaoGioHang(List<Map<String, Object>> cart, int idBienThe, int soLuong, String tenSP, long donGia, String barcode) {
        if (cart == null) cart = new ArrayList<>();
        for (Map<String, Object> item : cart) {
            if (((Integer) item.get("idBienThe")) == idBienThe) {
                int currentQty = (Integer) item.get("soLuong");
                int newQty = currentQty + soLuong;
                item.put("soLuong", newQty);
                item.put("thanhTien", donGia * newQty);
                return cart;
            }
        }
        Map<String, Object> newItem = new HashMap<>();
        newItem.put("idBienThe", idBienThe);
        newItem.put("tenSP", tenSP);
        newItem.put("donGia", donGia);
        newItem.put("soLuong", soLuong);
        newItem.put("thanhTien", donGia * soLuong);
        newItem.put("barcode", barcode);
        cart.add(newItem);
        return cart;
    }

    public List<Map<String, Object>> xoaKhoiGioHang(List<Map<String, Object>> cart, int idBienThe) {
        if (cart != null) {
            cart.removeIf(item -> ((Integer) item.get("idBienThe")) == idBienThe);
        }
        return cart;
    }

    public List<Map<String, Object>> xoaToanBoGioHang(List<Map<String, Object>> cart) {
        if (cart != null) {
            cart.clear();
        }
        return cart;
    }
}
