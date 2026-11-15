package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 53: Xem lịch sử mua sắm chi tiết & Tổng chi tiêu tích lũy của khách
 */
public class UC53_LichSuMuaSamKhachRepo {
    private final connectService db = new connectService();

    public List<Map<String, Object>> getLichSuMuaSam(int idKhachHang) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT dh.ID_DonHang, dh.MaDonHang, dh.NgayTao, dh.TongTien, tt.TenTrangThai " +
                     "FROM DonHang dh " +
                     "INNER JOIN TrangThaiDonHang tt ON dh.TrangThaiDH_ID = tt.TrangThaiDH_ID " +
                     "WHERE dh.ID_KhachHang = ? ORDER BY dh.ID_DonHang DESC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idKhachHang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("idDonHang", rs.getInt("ID_DonHang"));
                    item.put("maDonHang", rs.getString("MaDonHang"));
                    item.put("ngayTao", rs.getTimestamp("NgayTao"));
                    item.put("thanhTien", rs.getLong("TongTien"));
                    item.put("trangThai", rs.getString("TenTrangThai"));
                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
