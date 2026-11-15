package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 59: Tạo lô Voucher tự động hàng loạt (Batch Generation) cho sự kiện
 */
public class UC59_TaoLoVoucherHangLoatRepo {
    private final connectService db = new connectService();

    public int generateBatchVouchers(String prefix, int quantity, long giamGia, long donToiThieu) {
        String sql = "INSERT INTO Vouchers (MaVoucher, TenChuongTrinh, GiamGia, DonToiThieu, SoLuong, DaSuDung, TrangThai, NgayTao) " +
                     "VALUES (?, ?, ?, ?, 1, 0, 1, GETDATE())";
        int count = 0;
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 1; i <= quantity; i++) {
                String code = String.format("%s%04d", prefix != null ? prefix : "EVT", i);
                ps.setString(1, code);
                ps.setString(2, "Sự kiện thời trang DUNA (" + code + ")");
                ps.setLong(3, giamGia);
                ps.setLong(4, donToiThieu);
                ps.addBatch();
            }
            int[] res = ps.executeBatch();
            count = res.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
