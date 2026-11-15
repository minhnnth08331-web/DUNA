package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 58: Cấu hình điều kiện kích hoạt Voucher (Đơn tối thiểu, Hạn dùng, Lượt dùng)
 */
public class UC58_CauHinhDieuKienVoucherRepo {
    private final connectService db = new connectService();

    public boolean cauHinhDieuKienVoucher(int idVoucher, long donToiThieu, int soLuong, String hanDung) {
        String sql = "UPDATE Vouchers SET DonToiThieu = ?, SoLuong = ? WHERE ID_Voucher = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, donToiThieu);
            ps.setInt(2, soLuong);
            ps.setInt(3, idVoucher);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cauHinhDieuKienVoucher(int idVoucher, long donToiThieu, int soLuong) {
        return cauHinhDieuKienVoucher(idVoucher, donToiThieu, soLuong, null);
    }
}
