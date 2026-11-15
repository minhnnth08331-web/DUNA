package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 11: Quản lý trạng thái kinh doanh sản phẩm (Còn hàng / Ngừng bán)
 */
public class UC11_TrangThaiKinhDoanhRepo {
    private final connectService db = new connectService();

    public boolean toggleTrangThaiKinhDoanh(int idSanPham) {
        String sql = "UPDATE SanPham SET TrangThai = CASE WHEN TrangThai = N'Còn hàng' THEN N'Ngừng bán' ELSE N'Còn hàng' END WHERE ID_SanPham = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSanPham);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTrangThai(int idSanPham, String trangThai) {
        String sql = "UPDATE SanPham SET TrangThai = ? WHERE ID_SanPham = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, idSanPham);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
