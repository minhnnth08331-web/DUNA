package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 15: Xóa biến thể sản phẩm & Tự động đồng bộ số lượng tồn kho cha
 */
public class UC15_XoaBienTheDongBoKhoRepo {
    private final connectService db = new connectService();

    public boolean xoaBienTheVaDongBoKho(int idBienThe, int idSanPham) {
        String delSql = "DELETE FROM BienTheSanPham WHERE ID_BienThe = ?";
        String syncSql = "UPDATE SanPham SET TongSoLuong = ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE ID_SanPham = ?), 0) WHERE ID_SanPham = ?";
        try (Connection con = db.myConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psDel = con.prepareStatement(delSql);
                 PreparedStatement psSync = con.prepareStatement(syncSql)) {
                psDel.setInt(1, idBienThe);
                psDel.executeUpdate();

                psSync.setInt(1, idSanPham);
                psSync.setInt(2, idSanPham);
                psSync.executeUpdate();

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
