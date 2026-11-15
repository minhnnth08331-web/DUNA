package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import java.sql.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 34: Thêm nhanh khách hàng mới trực tiếp tại quầy thu ngân (modalAddKH)
 */
public class UC34_ThemNhanhKhachHangPOSRepo {
    private final connectService db = new connectService();

    public KhachHang quickAddCustomer(String hoTen, String sdt) {
        String maKH = "KH" + (System.currentTimeMillis() % 100000);
        String sql = "INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, HangKhachHang, TongTienChiTieu, TrangThai, NgayTao) " +
                     "VALUES (?, ?, ?, N'Thường', 0, N'Hoạt động', GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, maKH);
            ps.setString(2, hoTen != null ? hoTen.trim() : "Khách lẻ");
            ps.setString(3, sdt != null ? sdt.trim() : "");
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setId(gk.getInt(1));
                    kh.setMaKhachHang(maKH);
                    kh.setHoVaTen(hoTen);
                    kh.setSoDienThoai(sdt);
                    kh.setHangKhachHang("Thường");
                    return kh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
