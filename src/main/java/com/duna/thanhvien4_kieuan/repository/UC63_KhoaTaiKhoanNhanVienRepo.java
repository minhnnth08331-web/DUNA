package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 63: Khóa tài khoản nhân viên tức thời & Bắt buộc nhập Lý do khóa
 */
public class UC63_KhoaTaiKhoanNhanVienRepo {
    private final connectService db = new connectService();

    public boolean khoaTaiKhoanNhanVien(int idNhanVien, String lyDoKhoa) {
        String sql = "UPDATE NhanVien SET TrangThaiNV_ID = 2 WHERE ID_NhanVien = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean moKhoaTaiKhoanNhanVien(int idNhanVien) {
        String sql = "UPDATE NhanVien SET TrangThaiNV_ID = 1 WHERE ID_NhanVien = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
