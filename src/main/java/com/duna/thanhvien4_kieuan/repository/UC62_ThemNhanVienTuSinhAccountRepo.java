package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.NhanVien;
import java.sql.*;
import java.util.Random;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 62: Thêm mới nhân viên, kiểm tra trùng CCCD & Tự sinh tài khoản (QLxxx/NVxxx)
 */
public class UC62_ThemNhanVienTuSinhAccountRepo {
    private final connectService db = new connectService();

    public boolean themNhanVienKiemTraCCCD(NhanVien nv, boolean isQuanLy) {
        String checkSql = "SELECT COUNT(*) FROM NhanVien WHERE MaSoCCCD = ?";
        String insertSql = "INSERT INTO NhanVien (MaNhanVien, HoVaTen, SoDienThoai, Email, MaSoCCCD, DiaChi, TrangThaiNV_ID, NgayTao) " +
                           "VALUES (?, ?, ?, ?, ?, ?, 1, GETDATE())";
        try (Connection con = db.myConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {
            checkPs.setString(1, nv.getMaSoCCCD());
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                String prefix = isQuanLy ? "QL" : "NV";
                String maNV = prefix + (100 + new Random().nextInt(900));
                insertPs.setString(1, maNV);
                insertPs.setString(2, nv.getHoVaTen());
                insertPs.setString(3, nv.getSoDienThoai());
                insertPs.setString(4, nv.getEmail());
                insertPs.setString(5, nv.getMaSoCCCD());
                insertPs.setString(6, nv.getDiaChi());
                return insertPs.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
