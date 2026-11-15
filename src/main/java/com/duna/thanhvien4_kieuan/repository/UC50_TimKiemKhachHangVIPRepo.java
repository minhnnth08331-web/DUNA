package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 50: Tìm kiếm khách hàng theo Tên, Số điện thoại và Lọc theo Hạng VIP
 */
public class UC50_TimKiemKhachHangVIPRepo {
    private final connectService db = new connectService();

    public List<KhachHang> searchKhachHang(String query, String hangVIP) {
        List<KhachHang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT ID_KhachHang, MaKH, HoTen, SoDienThoai, HangKhachHang, TrangThai FROM KhachHang WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (HoTen LIKE ? OR SoDienThoai LIKE ? OR MaKH LIKE ?) ");
            String kw = "%" + query.trim() + "%";
            params.add(kw); params.add(kw); params.add(kw);
        }
        if (hangVIP != null && !hangVIP.trim().isEmpty() && !"all".equalsIgnoreCase(hangVIP)) {
            sql.append("AND HangKhachHang = ? ");
            params.add(hangVIP.trim());
        }
        sql.append("ORDER BY ID_KhachHang DESC");

        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setId(rs.getInt("ID_KhachHang"));
                    kh.setMaKhachHang(rs.getString("MaKH") != null ? rs.getString("MaKH") : "KH" + kh.getId());
                    kh.setHoVaTen(rs.getString("HoTen") != null ? rs.getString("HoTen") : "");
                    kh.setSoDienThoai(rs.getString("SoDienThoai"));
                    kh.setHangKhachHang(rs.getString("HangKhachHang"));
                    list.add(kh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
