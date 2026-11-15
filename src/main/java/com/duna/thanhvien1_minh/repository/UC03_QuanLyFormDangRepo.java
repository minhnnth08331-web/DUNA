package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 03: Quản lý Thuộc tính Form dáng trang phục (Slimfit, Regular, Oversize)
 */
public class UC03_QuanLyFormDangRepo {
    private final connectService db = new connectService();

    public List<String> getFormDangList() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT FormDang FROM SanPham WHERE FormDang IS NOT NULL AND FormDang <> ''";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("FormDang"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            list.addAll(Arrays.asList("Slimfit", "Regular Fit", "Oversize", "Classic"));
        }
        return list;
    }

    public boolean themFormDang(String formDang) {
        return formDang != null && !formDang.trim().isEmpty();
    }

    public boolean xoaFormDang(String formDang) {
        if (formDang == null || formDang.trim().isEmpty()) return false;
        String sql = "UPDATE SanPham SET FormDang = NULL WHERE FormDang = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, formDang);
            return ps.executeUpdate() >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
