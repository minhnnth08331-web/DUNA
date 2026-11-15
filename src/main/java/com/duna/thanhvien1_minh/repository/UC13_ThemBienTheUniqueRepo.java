package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.BienTheSanPham;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 13: Thêm mới biến thể Size/Màu kèm ràng buộc duy nhất (Unique DB)
 */
public class UC13_ThemBienTheUniqueRepo {
    private final connectService db = new connectService();

    public boolean themBienTheUnique(BienTheSanPham bt) {
        String checkSql = "SELECT COUNT(*) FROM BienTheSanPham WHERE ID_SanPham = ? AND MauSac = ? AND TrongLuong = ?";
        String insertSql = "INSERT INTO BienTheSanPham (ID_SanPham, MaBienThe, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode, NgayTao) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {
            checkPs.setInt(1, bt.getIdSanPham());
            checkPs.setString(2, bt.getMauSac());
            checkPs.setString(3, bt.getTrongLuong());
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                insertPs.setInt(1, bt.getIdSanPham());
                insertPs.setString(2, bt.getMaBienThe());
                insertPs.setString(3, bt.getMauSac());
                insertPs.setString(4, bt.getTrongLuong());
                insertPs.setLong(5, bt.getGiaNhap());
                insertPs.setLong(6, bt.getGiaBan());
                insertPs.setInt(7, bt.getSoLuong());
                insertPs.setString(8, bt.getBarcode());
                return insertPs.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
