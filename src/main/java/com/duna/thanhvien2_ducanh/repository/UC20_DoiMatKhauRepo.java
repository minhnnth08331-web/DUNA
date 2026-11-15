package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import com.duna.common.util.PasswordUtil;
import java.sql.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 20: Đổi mật khẩu tài khoản người dùng đang đăng nhập (ChangeCredentials)
 */
public class UC20_DoiMatKhauRepo {
    private final connectService db = new connectService();

    public boolean doiMatKhau(String username, String oldPass, String newPass) {
        if (!PasswordUtil.isStrongPassword(newPass)) return false;
        String sql = "UPDATE TaiKhoan SET Password = ? WHERE Username = ? AND Password = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPass);
            ps.setString(2, username);
            ps.setString(3, oldPass);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
