package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 40: Xác nhận thanh toán Chuyển khoản QR tức thì trên Mobile (QR-Mobile)
 */
public class UC40_XacNhanThanhToanQRRepo {
    private final connectService db = new connectService();

    public boolean xacNhanChuyenKhoanQRTucThi(int idDonHang) {
        String sql = "UPDATE DonHang SET TrangThaiDH_ID = 2, PhuongThucTT = N'Chuyển khoản VietQR' WHERE ID_DonHang = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDonHang);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
