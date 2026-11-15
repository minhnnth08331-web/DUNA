package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 47: Thống kê Doanh thu, Tổng giá vốn & Lợi nhuận gộp theo Tháng/Năm
 */
public class UC47_ThongKeLoiNhuanThangNamRepo {
    private final connectService db = new connectService();

    public Map<String, Long> getDoanhThuThangNam(int month, int year) {
        Map<String, Long> stats = new HashMap<>();
        String sql = "SELECT ISNULL(SUM(dh.TongTien), 0) AS DoanhThu, " +
                     "ISNULL(SUM(ct.SoLuong * sp.GiaNhap), 0) AS GiaVon " +
                     "FROM DonHang dh " +
                     "INNER JOIN TrangThaiDonHang tt ON dh.TrangThaiDH_ID = tt.TrangThaiDH_ID " +
                     "LEFT JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                     "LEFT JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                     "WHERE tt.TenTrangThai = N'Đã thanh toán' AND MONTH(dh.NgayTao) = ? AND YEAR(dh.NgayTao) = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long dt = rs.getLong("DoanhThu");
                    long gv = rs.getLong("GiaVon");
                    stats.put("doanhThu", dt);
                    stats.put("giaVon", gv);
                    stats.put("loiNhuanGop", dt - gv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}
