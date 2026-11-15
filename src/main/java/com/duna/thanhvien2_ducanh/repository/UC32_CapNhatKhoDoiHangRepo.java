package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 32: Cập nhật kho hàng tự động khi đổi hàng (Cộng kho cũ, Trừ kho mới)
 */
public class UC32_CapNhatKhoDoiHangRepo {
    private final connectService db = new connectService();

    public boolean capNhatKhoKhiDoiHang(int idBienTheCu, int idBienTheMoi, int soLuongDoi) {
        String sqlCongCu = "UPDATE BienTheSanPham SET SoLuong = SoLuong + ? WHERE ID_BienThe = ?";
        String sqlTruMoi = "UPDATE BienTheSanPham SET SoLuong = SoLuong - ? WHERE ID_BienThe = ?";
        try (Connection con = db.myConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psCong = con.prepareStatement(sqlCongCu);
                 PreparedStatement psTru = con.prepareStatement(sqlTruMoi)) {
                psCong.setInt(1, soLuongDoi);
                psCong.setInt(2, idBienTheCu);
                psCong.executeUpdate();

                psTru.setInt(1, soLuongDoi);
                psTru.setInt(2, idBienTheMoi);
                psTru.executeUpdate();

                con.commit();
                return true;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
