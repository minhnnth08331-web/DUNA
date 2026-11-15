package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 24: Tìm kiếm nhanh sản phẩm tại quầy bằng Auto-complete siêu tốc
 */
public class UC24_TimKiemAutoCompleteRepo {
    private final connectService db = new connectService();

    public List<Map<String, Object>> searchAutoComplete(String keyword) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT TOP 8 bt.ID_BienThe, sp.TenSP, bt.MauSac, bt.TrongLuong, bt.GiaBan, bt.SoLuong, bt.Barcode " +
                     "FROM BienTheSanPham bt " +
                     "INNER JOIN SanPham sp ON bt.ID_SanPham = sp.ID_SanPham " +
                     "WHERE sp.TenSP LIKE ? OR bt.Barcode LIKE ? OR sp.MaSP LIKE ? " +
                     "ORDER BY bt.SoLuong DESC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("idBienThe", rs.getInt("ID_BienThe"));
                    item.put("tenSP", rs.getString("TenSP"));
                    item.put("mauSac", rs.getString("MauSac"));
                    item.put("size", rs.getString("TrongLuong"));
                    item.put("giaBan", rs.getLong("GiaBan"));
                    item.put("soLuong", rs.getInt("SoLuong"));
                    item.put("barcode", rs.getString("Barcode"));
                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
