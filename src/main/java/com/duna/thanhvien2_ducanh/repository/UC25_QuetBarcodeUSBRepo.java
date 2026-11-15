package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.BienTheSanPham;
import com.duna.common.util.BarcodeUtil;
import java.sql.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 25: Tích hợp súng bắn mã vạch Barcode USB quét hàng siêu tốc tại quầy
 */
public class UC25_QuetBarcodeUSBRepo {
    private final connectService db = new connectService();

    public BienTheSanPham findByBarcodeUSB(String barcode) {
        if (!BarcodeUtil.isValidBarcode(barcode)) return null;
        String sql = "SELECT * FROM BienTheSanPham WHERE Barcode = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, barcode.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BienTheSanPham bt = new BienTheSanPham();
                    bt.setIdBienThe(rs.getInt("ID_BienThe"));
                    bt.setIdSanPham(rs.getInt("ID_SanPham"));
                    bt.setMauSac(rs.getString("MauSac"));
                    bt.setTrongLuong(rs.getString("TrongLuong"));
                    bt.setGiaBan(rs.getLong("GiaBan"));
                    bt.setSoLuong(rs.getInt("SoLuong"));
                    bt.setBarcode(rs.getString("Barcode"));
                    return bt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
