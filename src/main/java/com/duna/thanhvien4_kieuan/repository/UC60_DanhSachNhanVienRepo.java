package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.NhanVien;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 60: Danh sách nhân viên bán hàng & Quản lý kèm trạng thái hoạt động
 */
public class UC60_DanhSachNhanVienRepo {
    private final connectService db = new connectService();

    public List<NhanVien> getDanhSachNhanVienVaTrangThai() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT nv.*, tt.TenTrangThai, tk.Role_ID FROM NhanVien nv " +
                     "INNER JOIN TrangThaiNhanVien tt ON nv.TrangThaiNV_ID = tt.TrangThaiNV_ID " +
                     "LEFT JOIN TaiKhoan tk ON nv.ID_NhanVien = tk.NhanVienID ORDER BY nv.ID_NhanVien DESC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("ID_NhanVien"));
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setHoVaTen(rs.getString("HoVaTen"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setTenTrangThai(rs.getString("TenTrangThai"));
                nv.setRoleId(rs.getInt("Role_ID"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean capNhatTrangThaiNhanVien(int idNhanVien, int trangThai) {
        String sql = "UPDATE NhanVien SET TrangThaiNV_ID = ? WHERE ID_NhanVien = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, trangThai);
            ps.setInt(2, idNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
