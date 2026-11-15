package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.DonHang;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 42: Danh sách toàn bộ hóa đơn bán hàng & Lọc theo khoảng ngày
 */
public class UC42_DanhSachHoaDonRepo {
    private final connectService db = new connectService();
    private final UC43_XemChiTietHoaDonRepo uc43 = new UC43_XemChiTietHoaDonRepo();

    public List<DonHang> getDonHangTheoNgay(String fromDate, String toDate) {
        List<DonHang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT dh.*, tt.TenTrangThai, kh.HoTen AS TenKhachHang " +
                     "FROM DonHang dh " +
                     "INNER JOIN TrangThaiDonHang tt ON dh.TrangThaiDH_ID = tt.TrangThaiDH_ID " +
                     "LEFT JOIN KhachHang kh ON dh.ID_KhachHang = kh.ID_KhachHang WHERE 1=1 ");
        List<String> params = new ArrayList<>();
        if (fromDate != null && !fromDate.trim().isEmpty() && toDate != null && !toDate.trim().isEmpty()) {
            sql.append("AND CONVERT(date, dh.NgayTao) BETWEEN ? AND ? ");
            params.add(fromDate.trim());
            params.add(toDate.trim());
        }
        sql.append("ORDER BY dh.ID_DonHang DESC");

        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setId(rs.getInt("ID_DonHang"));
                    dh.setMaDonHang(rs.getString("MaDonHang"));
                    String tenKh = rs.getString("TenKhachHang");
                    if (tenKh == null || tenKh.trim().isEmpty()) tenKh = rs.getString("TenNguoiNhan");
                    dh.setTenKhachHang(tenKh != null ? tenKh : "Khách lẻ tại quầy");
                    dh.setTenNhanVien(rs.getString("TenNhanVien"));

                    long tt = 0;
                    try {
                        tt = rs.getLong("TongTien");
                    } catch (Exception ex) {
                        try { tt = rs.getLong("ThanhTien"); } catch (Exception ignored) {}
                    }
                    dh.setTongTien(tt);
                    dh.setThanhTien(tt);
                    dh.setPhuongThucTT(rs.getString("PhuongThucThanhToan"));
                    dh.setGhiChu(rs.getString("GhiChu"));
                    dh.setTenTrangThai(rs.getString("TenTrangThai"));
                    dh.setNgayTao(rs.getString("NgayTao"));

                    dh.setDanhSachChiTiet(uc43.getChiTietHoaDon(dh.getId()));
                    list.add(dh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
