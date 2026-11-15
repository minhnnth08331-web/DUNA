package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 09: Quản lý Giá vốn nhập hàng (COGS) để tính toán biên lợi nhuận gộp
 */
public class UC09_QuanLyGiaVonCOGSRepo {
    private final connectService db = new connectService();

    public Map<String, Object> tinhLoiNhuanGop(int idSanPham) {
        Map<String, Object> res = new HashMap<>();
        String sql = "SELECT GiaBan, GiaNhap, (GiaBan - GiaNhap) AS LoiNhuanGop, " +
                     "CASE WHEN GiaBan > 0 THEN ROUND(((GiaBan - GiaNhap) * 100.0 / GiaBan), 2) ELSE 0 END AS TyLeLoiNhuan " +
                     "FROM SanPham WHERE ID_SanPham = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSanPham);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    res.put("giaBan", rs.getLong("GiaBan"));
                    res.put("giaNhap", rs.getLong("GiaNhap"));
                    res.put("loiNhuanGop", rs.getLong("LoiNhuanGop"));
                    res.put("tyLeLoiNhuan", rs.getDouble("TyLeLoiNhuan"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean capNhatGiaNhap(int idSanPham, long giaNhapMoi) {
        String sql = "UPDATE SanPham SET GiaNhap = ? WHERE ID_SanPham = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, giaNhapMoi);
            ps.setInt(2, idSanPham);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
