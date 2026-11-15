package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 18: Ghi nhớ trạng thái phiên đăng nhập qua Cookie an toàn (Remember Me)
 */
public class UC18_GhiNhoCookieRepo {
    private final connectService db = new connectService();

    public boolean isValidRememberMeToken(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        String sql = "SELECT COUNT(*) FROM TaiKhoan tk JOIN NhanVien nv ON tk.NhanVienID = nv.ID_NhanVien WHERE tk.Username = ? AND nv.TrangThaiNV_ID = 1";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
