package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 55: Chính sách chu kỳ Reset hạng khách hàng sau 3 tháng cuốn chiếu (Job ngầm)
 */
public class UC55_ResetHang3ThangJobRepo {
    private final connectService db = new connectService();

    public int jobResetHang3ThangCuonChieu() {
        String sql = "UPDATE KhachHang SET HangKhachHang = N'Thường', TongTienChiTieu = 0, NgayBatDauChuKy = GETDATE() " +
                     "WHERE DATEDIFF(day, NgayBatDauChuKy, GETDATE()) > 90 AND HangKhachHang <> N'Thường'";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
