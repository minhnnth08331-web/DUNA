package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.BienTheSanPham;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 12: Xem chi tiết danh sách Biến thể Màu sắc & Kích cỡ (modalVariants)
 */
public class UC12_DanhSachBienTheRepo {
    private final connectService db = new connectService();

    public List<BienTheSanPham> getVariantsByProductId(int productId) {
        List<BienTheSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM BienTheSanPham WHERE ID_SanPham = ? ORDER BY MauSac, TrongLuong";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BienTheSanPham bt = new BienTheSanPham();
                    bt.setIdBienThe(rs.getInt("ID_BienThe"));
                    bt.setIdSanPham(rs.getInt("ID_SanPham"));
                    bt.setMauSac(rs.getString("MauSac"));
                    bt.setTrongLuong(rs.getString("TrongLuong"));
                    bt.setGiaBan(rs.getLong("GiaBan"));
                    bt.setGiaNhap(rs.getLong("GiaNhap"));
                    bt.setSoLuong(rs.getInt("SoLuong"));
                    bt.setBarcode(rs.getString("Barcode"));
                    bt.setMaBienThe(rs.getString("MaBienThe"));
                    list.add(bt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
