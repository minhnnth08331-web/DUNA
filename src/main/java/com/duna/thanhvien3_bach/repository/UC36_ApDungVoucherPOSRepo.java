package com.duna.thanhvien3_bach.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 36: Tra cứu, kiểm tra điều kiện & Áp dụng mã Voucher tại quầy
 */
public class UC36_ApDungVoucherPOSRepo {
    private final connectService db = new connectService();

    public long kiemTraVaApDungVoucher(String maVoucher, long subtotal) {
        String sql = "SELECT * FROM Vouchers WHERE MaVoucher = ? AND TrangThai = 1";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVoucher != null ? maVoucher.trim() : "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long donToiThieu = rs.getLong("DonToiThieu");
                    if (subtotal >= donToiThieu) {
                        return rs.getLong("GiamGia");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
