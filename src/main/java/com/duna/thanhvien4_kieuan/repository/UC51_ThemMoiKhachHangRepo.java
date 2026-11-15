package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 51: Thêm mới hồ sơ khách hàng chuẩn hóa đầu số (+84)
 */
public class UC51_ThemMoiKhachHangRepo {
    private final connectService db = new connectService();

    public boolean themKhachHangChuanHoa(KhachHang kh) {
        String rawPhone = kh.getSoDienThoai() != null ? kh.getSoDienThoai().trim() : "";
        String cleanPhone = rawPhone.replace("+84", "0").replaceAll("[^0-9]", "");
        String sql = "INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, Email, DiaChi, GioiTinh, HangKhachHang, TongTienChiTieu, TrangThai, NgayBatDauChuKy, NgayTao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, N'Thường', 0, N'Hoạt động', GETDATE(), GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "KH" + (System.currentTimeMillis() % 100000));
            ps.setString(2, kh.getHoVaTen());
            ps.setString(3, cleanPhone);
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getGioiTinh() != null ? kh.getGioiTinh() : "Nam");
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
