package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 14: Cập nhật Giá bán riêng & Số lượng tồn kho theo từng biến thể
 */
public class UC14_CapNhatGiaTonBienTheRepo {
    private final connectService db = new connectService();

    public boolean capNhatGiaVaTonKhoBienThe(int idBienThe, long giaBanMoi, int soLuongMoi) {
        String sql = "UPDATE BienTheSanPham SET GiaBan = ?, SoLuong = ? WHERE ID_BienThe = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, giaBanMoi);
            ps.setInt(2, soLuongMoi);
            ps.setInt(3, idBienThe);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
