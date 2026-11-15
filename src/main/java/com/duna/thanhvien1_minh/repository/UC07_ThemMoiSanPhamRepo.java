package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.SanPham;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 07: Thêm mới sản phẩm thời trang nam (Giao diện CSS Grid 2 cột)
 */
public class UC07_ThemMoiSanPhamRepo {
    private final connectService db = new connectService();

    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham (MaSP, TenSP, ID_DanhMuc, ThuongHieu, GiaBan, GiaNhap, TongSoLuong, TrangThai, ChatLieu, FormDang, HinhAnh, MoTa, NgayTao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setInt(3, sp.getIdDanhMuc());
            ps.setString(4, sp.getThuongHieu() != null ? sp.getThuongHieu() : "DUNA");
            ps.setLong(5, sp.getGiaBan());
            ps.setLong(6, sp.getGiaNhap());
            ps.setInt(7, sp.getSoLuong());
            ps.setString(8, sp.getTrangThai() != null ? sp.getTrangThai() : "Còn hàng");
            ps.setString(9, sp.getChatLieu());
            ps.setString(10, sp.getFormDang());
            ps.setString(11, sp.getHinhAnh());
            ps.setString(12, sp.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
