package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.SanPham;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 08: Cập nhật thông tin chi tiết, hình ảnh & Mô tả giặt ủi sản phẩm
 */
public class UC08_CapNhatSanPhamRepo {
    private final connectService db = new connectService();

    public boolean capNhatSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET TenSP = ?, ID_DanhMuc = ?, GiaBan = ?, GiaNhap = ?, " +
                     "ChatLieu = ?, FormDang = ?, HinhAnh = ?, MoTa = ?, NgayCapNhat = GETDATE() WHERE ID_SanPham = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getIdDanhMuc());
            ps.setLong(3, sp.getGiaBan());
            ps.setLong(4, sp.getGiaNhap());
            ps.setString(5, sp.getChatLieu());
            ps.setString(6, sp.getFormDang());
            ps.setString(7, sp.getHinhAnh());
            ps.setString(8, sp.getMoTa());
            ps.setInt(9, sp.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
