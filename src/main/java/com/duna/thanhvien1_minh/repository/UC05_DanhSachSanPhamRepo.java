package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.SanPham;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 05: Danh sách sản phẩm thời trang & Bộ lọc danh mục, trạng thái
 */
public class UC05_DanhSachSanPhamRepo {
    private final connectService db = new connectService();

    public List<SanPham> getDanhSachSanPham(String keyword, String cat, String status) {
        List<SanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT sp.*, dm.TenDanhMuc, " +
            "ISNULL((SELECT SUM(bt.SoLuong) FROM BienTheSanPham bt WHERE bt.ID_SanPham = sp.ID_SanPham), sp.TongSoLuong) AS TongSL " +
            "FROM SanPham sp " +
            "INNER JOIN DanhMuc dm ON sp.ID_DanhMuc = dm.ID_DanhMuc " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (sp.TenSP LIKE ? OR sp.MaSP LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw); params.add(kw);
        }
        if (cat != null && !cat.trim().isEmpty() && !"all".equalsIgnoreCase(cat)) {
            sql.append("AND dm.TenDanhMuc = ? ");
            params.add(cat.trim());
        }
        if (status != null && !status.trim().isEmpty() && !"all".equalsIgnoreCase(status)) {
            sql.append("AND sp.TrangThai = ? ");
            params.add(status.trim());
        }
        sql.append("ORDER BY sp.ID_SanPham DESC");

        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setId(rs.getInt("ID_SanPham"));
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("TenSP"));
                    sp.setDanhMuc(rs.getString("TenDanhMuc"));
                    sp.setThuongHieu(rs.getString("ThuongHieu"));
                    sp.setGiaBan(rs.getLong("GiaBan"));
                    sp.setGiaNhap(rs.getLong("GiaNhap"));
                    sp.setSoLuong(rs.getInt("TongSL"));
                    sp.setTrangThai(rs.getString("TrangThai"));
                    sp.setChatLieu(rs.getString("ChatLieu"));
                    sp.setFormDang(rs.getString("FormDang"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
