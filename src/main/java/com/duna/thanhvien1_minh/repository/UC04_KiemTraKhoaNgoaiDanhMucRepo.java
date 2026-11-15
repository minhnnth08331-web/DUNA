package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 04: Kiểm tra ràng buộc toàn vẹn khóa ngoại khi thao tác Danh mục
 */
public class UC04_KiemTraKhoaNgoaiDanhMucRepo {
    private final connectService db = new connectService();

    public boolean checkDanhMucForeignKey(int idDanhMuc) {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE ID_DanhMuc = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDanhMuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
