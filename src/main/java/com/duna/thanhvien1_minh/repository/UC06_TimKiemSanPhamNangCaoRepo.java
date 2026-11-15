package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.SanPham;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 06: Tìm kiếm nâng cao sản phẩm theo Mã SP, Tên, Chất liệu, Form dáng
 */
public class UC06_TimKiemSanPhamNangCaoRepo {
    private final connectService db = new connectService();

    public List<SanPham> searchAdvanced(String maSP, String ten, String chatLieu, String formDang) {
        List<SanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT sp.*, dm.TenDanhMuc FROM SanPham sp INNER JOIN DanhMuc dm ON sp.ID_DanhMuc = dm.ID_DanhMuc WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (maSP != null && !maSP.trim().isEmpty()) {
            sql.append("AND sp.MaSP LIKE ? ");
            params.add("%" + maSP.trim() + "%");
        }
        if (ten != null && !ten.trim().isEmpty()) {
            sql.append("AND sp.TenSP LIKE ? ");
            params.add("%" + ten.trim() + "%");
        }
        if (chatLieu != null && !chatLieu.trim().isEmpty()) {
            sql.append("AND sp.ChatLieu = ? ");
            params.add(chatLieu.trim());
        }
        if (formDang != null && !formDang.trim().isEmpty()) {
            sql.append("AND sp.FormDang = ? ");
            params.add(formDang.trim());
        }
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setId(rs.getInt("ID_SanPham"));
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("TenSP"));
                    sp.setDanhMuc(rs.getString("TenDanhMuc"));
                    sp.setGiaBan(rs.getLong("GiaBan"));
                    sp.setChatLieu(rs.getString("ChatLieu"));
                    sp.setFormDang(rs.getString("FormDang"));
                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
