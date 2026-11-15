package com.duna.thanhvien1_minh.repository;

import com.duna.common.connection.connectService;
import java.sql.*;
import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 02: Quản lý Thuộc tính Chất liệu vải may mặc (Cotton, Kaki, Denim)
 */
public class UC02_QuanLyChatLieuRepo {
    private final connectService db = new connectService();

    public List<String> getChatLieuList() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT ChatLieu FROM SanPham WHERE ChatLieu IS NOT NULL AND ChatLieu <> ''";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("ChatLieu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            list.addAll(Arrays.asList("Cotton 100%", "Kaki", "Denim", "Linen", "Modal"));
        }
        return list;
    }

    public boolean themChatLieu(String chatLieu) {
        return chatLieu != null && !chatLieu.trim().isEmpty();
    }

    public boolean xoaChatLieu(String chatLieu) {
        if (chatLieu == null || chatLieu.trim().isEmpty()) return false;
        String sql = "UPDATE SanPham SET ChatLieu = NULL WHERE ChatLieu = ?";
        try (Connection con = db.myConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, chatLieu);
            return ps.executeUpdate() >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
