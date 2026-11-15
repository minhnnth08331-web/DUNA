package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.ChiTietDonHang;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 43: Xem chi tiết hóa đơn (Từng món, Size/Màu, Giá bán, Chiết khấu, VAT)
 */
public class UC43_XemChiTietHoaDonRepo {
    private final connectService db = new connectService();

    public List<ChiTietDonHang> getChiTietHoaDon(int idDonHang) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP, bt.MauSac, bt.TrongLuong " +
                     "FROM ChiTietDonHang ct " +
                     "INNER JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                     "LEFT JOIN BienTheSanPham bt ON ct.ID_BienThe = bt.ID_BienThe " +
                     "WHERE ct.ID_DonHang = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietDonHang ct = new ChiTietDonHang();
                    ct.setIdChiTiet(rs.getInt("ID_ChiTiet"));
                    ct.setTenSP(rs.getString("TenSP"));
                    ct.setMauSac(rs.getString("MauSac"));
                    ct.setKichCo(rs.getString("TrongLuong"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getLong("DonGia"));
                    ct.setThanhTien(rs.getLong("ThanhTien"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
