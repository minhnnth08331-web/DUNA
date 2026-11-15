package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 52: Cập nhật thông tin khách hàng, ngày sinh và địa chỉ liên lạc
 */
public class UC52_CapNhatKhachHangRepo {
    private final connectService db = new connectService();

    public boolean capNhatThongTinKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET HoTen = ?, SoDienThoai = ?, Email = ?, DiaChi = ?, GioiTinh = ? WHERE ID_KhachHang = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getHoVaTen());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getGioiTinh());
            ps.setInt(6, kh.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
