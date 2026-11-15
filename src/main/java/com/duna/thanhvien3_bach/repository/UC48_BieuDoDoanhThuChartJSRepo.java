package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 48: Biểu đồ trực quan so sánh Doanh thu & Lợi nhuận qua Chart.js
 */
public class UC48_BieuDoDoanhThuChartJSRepo {
    private final connectService db = new connectService();

    public List<Map<String, Object>> getChartData(int days) {
        List<Map<String, Object>> points = new ArrayList<>();
        String sql = "SELECT TOP (?) CONVERT(date, dh.NgayTao) AS Ngay, " +
                     "SUM(dh.TongTien) AS DoanhThu " +
                     "FROM DonHang dh " +
                     "INNER JOIN TrangThaiDonHang tt ON dh.TrangThaiDH_ID = tt.TrangThaiDH_ID " +
                     "WHERE tt.TenTrangThai = N'Đã thanh toán' " +
                     "GROUP BY CONVERT(date, dh.NgayTao) ORDER BY Ngay ASC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, days > 0 ? days : 7);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> pt = new HashMap<>();
                    pt.put("ngay", rs.getString("Ngay"));
                    pt.put("doanhThu", rs.getLong("DoanhThu"));
                    points.add(pt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }
}
