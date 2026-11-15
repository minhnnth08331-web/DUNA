package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 64: Xuất toàn bộ báo cáo tài chính & Biểu đồ ra file Excel (POI)
 */
public class UC64_XuatBaoCaoTaiChinhExcelRepo {
    private final connectService db = new connectService();

    public List<Map<String, Object>> getFinancialReportData() {
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT CONVERT(date, dh.NgayTao) AS Ngay, " +
                     "COUNT(DISTINCT dh.ID_DonHang) AS TongDon, " +
                     "SUM(dh.TongTien) AS DoanhThu, " +
                     "SUM(ct.SoLuong * sp.GiaNhap) AS GiaVon " +
                     "FROM DonHang dh " +
                     "INNER JOIN TrangThaiDonHang tt ON dh.TrangThaiDH_ID = tt.TrangThaiDH_ID " +
                     "LEFT JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                     "LEFT JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                     "WHERE tt.TenTrangThai = N'Đã thanh toán' " +
                     "GROUP BY CONVERT(date, dh.NgayTao) ORDER BY Ngay DESC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ngay", rs.getString("Ngay"));
                row.put("tongDon", rs.getInt("TongDon"));
                long dt = rs.getLong("DoanhThu");
                long gv = rs.getLong("GiaVon");
                row.put("doanhThu", dt);
                row.put("giaVon", gv);
                row.put("loiNhuan", dt - gv);
                data.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
