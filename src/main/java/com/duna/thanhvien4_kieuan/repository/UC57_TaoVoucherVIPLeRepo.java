package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.Voucher;
import java.sql.*;
import java.util.Random;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 57: Tạo Voucher VIP lẻ với mã ngẫu nhiên tự sinh (Giảm % hoặc Tiền mặt)
 */
public class UC57_TaoVoucherVIPLeRepo {
    private final connectService db = new connectService();

    public boolean taoVoucherVIPLe(Voucher v) {
        String randomCode = "DUNA" + (1000 + new Random().nextInt(9000));
        String sql = "INSERT INTO Vouchers (MaVoucher, TenChuongTrinh, GiamGia, DonToiThieu, SoLuong, DaSuDung, TrangThai, NgayTao) " +
                     "VALUES (?, ?, ?, ?, ?, 0, 1, GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getMaVoucher() != null ? v.getMaVoucher() : randomCode);
            ps.setString(2, v.getTenChuongTrinh());
            ps.setLong(3, v.getGiamGia());
            ps.setLong(4, v.getDonToiThieu());
            ps.setInt(5, v.getSoLuong() > 0 ? v.getSoLuong() : 1);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
