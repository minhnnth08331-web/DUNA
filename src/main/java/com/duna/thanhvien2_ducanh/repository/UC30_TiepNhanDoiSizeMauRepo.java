package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 30: Tiếp nhận & Mở giao diện Đổi Size / Đổi Màu sản phẩm (modalExchange)
 */
public class UC30_TiepNhanDoiSizeMauRepo {
    private final connectService db = new connectService();

    public Map<String, Object> openExchangeModal(int idChiTietDonHang) {
        Map<String, Object> data = new HashMap<>();
        String sql = "SELECT ct.*, sp.TenSP, sp.ID_SanPham, bt.MauSac, bt.TrongLuong " +
                     "FROM ChiTietDonHang ct " +
                     "INNER JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                     "LEFT JOIN BienTheSanPham bt ON ct.ID_BienThe = bt.ID_BienThe " +
                     "WHERE ct.ID_ChiTiet = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idChiTietDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data.put("idChiTiet", rs.getInt("ID_ChiTiet"));
                    data.put("tenSP", rs.getString("TenSP"));
                    data.put("mauSacCu", rs.getString("MauSac"));
                    data.put("sizeCu", rs.getString("TrongLuong"));
                    data.put("donGia", rs.getLong("DonGia"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
