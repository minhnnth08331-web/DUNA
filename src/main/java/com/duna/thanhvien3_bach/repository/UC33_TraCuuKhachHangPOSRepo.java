package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import java.sql.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 33: Tra cứu khách hàng thành viên bằng SĐT (+84) tại màn hình POS
 */
public class UC33_TraCuuKhachHangPOSRepo {
    private final connectService db = new connectService();

    public KhachHang searchCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return null;
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ? OR SoDienThoai = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pClean = phone.trim().replace("+84", "0");
            ps.setString(1, phone.trim());
            ps.setString(2, pClean);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    return kh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
