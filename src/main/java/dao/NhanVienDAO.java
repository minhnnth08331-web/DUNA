package dao;

import connectService.connectService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mapper.NhanVienMapper;
import model.NhanVien;

/**
 * Data Access Object cho Nhân viên & Tài khoản - DUNA Men's Wear
 */
public class NhanVienDAO {
    private final connectService db = new connectService();
    private final NhanVienMapper nhanVienMapper = new NhanVienMapper();

    /**
     * Lấy danh sách nhân viên theo từ khóa tìm kiếm và trạng thái
     */
    public List<NhanVien> search(String query, String status) {
        List<NhanVien> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT n.*, tt.TenTrangThai, tk.Username, tk.Role_ID FROM NhanVien n " +
            "INNER JOIN TrangThaiNhanVien tt ON n.TrangThaiNV_ID = tt.TrangThaiNV_ID " +
            "LEFT JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID " +
            "WHERE 1=1 "
        );

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (n.HoVaTen LIKE ? OR n.MaNhanVien LIKE ? OR tk.Username LIKE ? OR n.SoDienThoai LIKE ? OR n.Email LIKE ?) ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND n.TrangThaiNV_ID = ? ");
        }

        sql.append("ORDER BY CASE WHEN (tk.Role_ID = 1 OR tk.Username = 'admin' OR n.HoVaTen LIKE '%Admin%' OR (tk.Role_ID IS NULL AND n.MaNhanVien LIKE 'QL%')) AND ISNULL(tk.Role_ID, 0) != 2 THEN 0 ELSE 1 END ASC, n.MaNhanVien ASC, n.ID_NhanVien ASC ");

        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (query != null && !query.trim().isEmpty()) {
                String pattern = "%" + query.trim() + "%";
                for (int i = 0; i < 5; i++) {
                    ps.setString(idx++, pattern);
                }
            }
            if (status != null && !status.trim().isEmpty()) {
                try {
                    ps.setInt(idx++, Integer.parseInt(status.trim()));
                } catch (NumberFormatException ignored) {}
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(nhanVienMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy nhân viên theo ID
     */
    public NhanVien getById(int id) {
        String sql = "SELECT n.*, tt.TenTrangThai, tk.Username, tk.Role_ID FROM NhanVien n " +
                     "INNER JOIN TrangThaiNhanVien tt ON n.TrangThaiNV_ID = tt.TrangThaiNV_ID " +
                     "LEFT JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID " +
                     "WHERE n.ID_NhanVien = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return nhanVienMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy nhân viên theo Username
     */
    public NhanVien getByUsername(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        String sql = "SELECT n.*, tt.TenTrangThai, tk.Username, tk.Role_ID FROM NhanVien n " +
                     "INNER JOIN TrangThaiNhanVien tt ON n.TrangThaiNV_ID = tt.TrangThaiNV_ID " +
                     "INNER JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID " +
                     "WHERE tk.Username = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return nhanVienMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

