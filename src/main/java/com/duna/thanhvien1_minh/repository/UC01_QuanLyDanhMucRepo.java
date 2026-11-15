package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.DanhMuc;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 01: Quản lý Danh mục thời trang nam (Áo sơ mi, Polo, Quần âu, Blazer)
 */
public class UC01_QuanLyDanhMucRepo {
    private final connectService db = new connectService();
    private final UC04_KiemTraKhoaNgoaiDanhMucRepo uc04 = new UC04_KiemTraKhoaNgoaiDanhMucRepo();

    public List<DanhMuc> getDanhSachDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhMuc ORDER BY ID_DanhMuc ASC";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DanhMuc dm = new DanhMuc();
                dm.setIdDanhMuc(rs.getInt("ID_DanhMuc"));
                dm.setTenDanhMuc(rs.getString("TenDanhMuc"));
                try {
                    dm.setMoTa(rs.getString("MoTa"));
                } catch (Exception e) {
                    dm.setMoTa("");
                }
                list.add(dm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themDanhMuc(DanhMuc dm) {
        String sql = "INSERT INTO DanhMuc (TenDanhMuc) VALUES (?)";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dm.getTenDanhMuc());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatDanhMuc(DanhMuc dm) {
        String sql = "UPDATE DanhMuc SET TenDanhMuc = ? WHERE ID_DanhMuc = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dm.getTenDanhMuc());
            ps.setInt(2, dm.getIdDanhMuc());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaDanhMuc(int idDanhMuc) {
        if (uc04.checkDanhMucForeignKey(idDanhMuc)) {
            return false;
        }
        String sql = "DELETE FROM DanhMuc WHERE ID_DanhMuc = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDanhMuc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
