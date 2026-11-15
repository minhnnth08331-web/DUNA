package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.NhanVien;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 61: Xem chi tiết hồ sơ nhân sự đầy đủ (CCCD 12 số, Quê quán, Địa chỉ)
 */
public class UC61_XemChiTietHoSoNhanSuRepo {
    private final connectService db = new connectService();

    public NhanVien getChiTietHoSoCCCD(int idNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE ID_NhanVien = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setId(rs.getInt("ID_NhanVien"));
                    nv.setMaNhanVien(rs.getString("MaNhanVien"));
                    nv.setHoVaTen(rs.getString("HoVaTen"));
                    nv.setMaSoCCCD(rs.getString("MaSoCCCD"));
                    nv.setDiaChi(rs.getString("DiaChi"));
                    nv.setDiaChiHienTai(rs.getString("DiaChiHienTai"));
                    return nv;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
