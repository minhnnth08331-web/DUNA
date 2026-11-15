package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 28: Kiểm tra & Cảnh báo tồn kho thời gian thực khi đưa hàng vào giỏ
 */
public class UC28_CanhBaoTonKhoRepo {
    private final connectService db = new connectService();

    public boolean checkInventoryRealtime(int idBienThe, int requestedQuantity) {
        String sql = "SELECT SoLuong FROM BienTheSanPham WHERE ID_BienThe = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBienThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoLuong") >= requestedQuantity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
