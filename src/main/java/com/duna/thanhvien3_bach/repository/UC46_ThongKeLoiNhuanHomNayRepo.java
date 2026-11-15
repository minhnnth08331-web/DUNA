package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 46: Thống kê Doanh thu thuần, Tổng giá vốn & Lợi nhuận gộp Hôm nay
 */
public class UC46_ThongKeLoiNhuanHomNayRepo {
    private final connectService db = new connectService();

    public Map<String, Long> getDoanhThuHomNay() {
        Map<String, Long> stats = new HashMap<>();
        String sql = "SELECT ISNULL(SUM(dh.TongTien), 0) AS DoanhThu, " +
                     "ISNULL(SUM(ct.SoLuong * ISNULL(ct.GiaNhapLucBan, ISNULL(sp.GiaNhap, 0))), 0) AS GiaVon " +
                     "FROM DonHang dh " +
                     "LEFT JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                     "LEFT JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                     "WHERE dh.TrangThaiDH_ID = 1 AND CONVERT(date, dh.NgayTao) = CONVERT(date, GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                long dt = rs.getLong("DoanhThu");
                long gv = rs.getLong("GiaVon");
                stats.put("doanhThu", dt);
                stats.put("giaVon", gv);
                stats.put("loiNhuanGop", dt - gv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}
