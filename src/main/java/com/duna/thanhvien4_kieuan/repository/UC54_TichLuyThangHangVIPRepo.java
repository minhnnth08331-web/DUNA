package com.duna.thanhvien4_kieuan.repository;

import com.duna.common.connection.connectService;
import java.sql.*;

/**
 * MEMBER 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * UC 54: Tự động tích lũy chi tiêu và thăng hạng thành viên VIP
 */
public class UC54_TichLuyThangHangVIPRepo {
    private final connectService db = new connectService();

    public String tichLuyChiTieuVaThangHang(int idKhachHang, long soTienDonHang) {
        String updateSql = "UPDATE KhachHang SET TongTienChiTieu = TongTienChiTieu + ? WHERE ID_KhachHang = ?";
        String selectSql = "SELECT TongTienChiTieu FROM KhachHang WHERE ID_KhachHang = ?";
        String rankSql = "UPDATE KhachHang SET HangKhachHang = ? WHERE ID_KhachHang = ?";
        String newRank = "Thường";
        try (Connection con = db.myConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psUp = con.prepareStatement(updateSql);
                 PreparedStatement psSel = con.prepareStatement(selectSql)) {
                psUp.setLong(1, soTienDonHang);
                psUp.setInt(2, idKhachHang);
                psUp.executeUpdate();

                psSel.setInt(1, idKhachHang);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        long totalSpent = rs.getLong(1);
                        if (totalSpent >= 10000000L) newRank = "VIP";
                        else if (totalSpent >= 5000000L) newRank = "Vàng";
                        else if (totalSpent >= 2000000L) newRank = "Bạc";
                    }
                }
                try (PreparedStatement psRank = con.prepareStatement(rankSql)) {
                    psRank.setString(1, newRank);
                    psRank.setInt(2, idKhachHang);
                    psRank.executeUpdate();
                }
                con.commit();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newRank;
    }
}
