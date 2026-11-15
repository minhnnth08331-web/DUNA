package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 49: Xem danh sách khách hàng & Phân hạng thành viên (Thường, Bạc, Vàng, VIP)
 */
public class UC49_DanhSachKhachHangVIPRepo {
    private final connectService db = new connectService();

    public List<KhachHang> getDanhSachKhachHangVaHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY TongTienChiTieu DESC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getInt("ID_KhachHang"));
                String ma = null;
                try { ma = rs.getString("MaKH"); } catch (Exception e) {
                    try { ma = rs.getString("MaKhachHang"); } catch (Exception ignored) {}
                }
                kh.setMaKhachHang(ma != null ? ma : "KH" + kh.getId());

                String ten = null;
                try { ten = rs.getString("HoTen"); } catch (Exception e) {
                    try { ten = rs.getString("HoVaTen"); } catch (Exception ignored) {}
                }
                kh.setHoVaTen(ten != null ? ten : "");

                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setHangKhachHang(rs.getString("HangKhachHang"));
                try {
                    kh.setTongTienChiTieu(rs.getLong("TongTienChiTieu"));
                } catch (Exception ignored) {}
                kh.setTrangThai(rs.getString("TrangThai"));
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
