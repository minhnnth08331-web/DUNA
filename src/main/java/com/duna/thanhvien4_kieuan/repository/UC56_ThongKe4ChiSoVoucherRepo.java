package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 56: Bảng tổng quan 4 chỉ số thống kê Voucher (Phát hành, Còn lại, Đã dùng)
 */
public class UC56_ThongKe4ChiSoVoucherRepo {
    private final connectService db = new connectService();

    public Map<String, Integer> getThongKeVoucher() {
        Map<String, Integer> kpi = new HashMap<>();
        String sql = "SELECT COUNT(*) AS TongVoucher, " +
                     "ISNULL(SUM(SoLuong), 0) AS TongPhatHanh, " +
                     "ISNULL(SUM(DaSuDung), 0) AS TongDaDung, " +
                     "ISNULL(SUM(CASE WHEN TrangThai = 1 THEN 1 ELSE 0 END), 0) AS DangHoatDong " +
                     "FROM Vouchers";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                kpi.put("tongPhatHanh", rs.getInt("TongPhatHanh"));
                kpi.put("tongDaDung", rs.getInt("TongDaDung"));
                kpi.put("conLai", rs.getInt("TongPhatHanh") - rs.getInt("TongDaDung"));
                kpi.put("dangHoatDong", rs.getInt("DangHoatDong"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kpi;
    }
}
