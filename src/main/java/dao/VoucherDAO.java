package dao;

import connectService.connectService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mapper.VoucherMapper;
import model.Voucher;

/**
 * Data Access Object cho Khuyến mãi / Voucher - DUNA Men's Wear
 */
public class VoucherDAO {
    private final connectService db = new connectService();
    private final VoucherMapper voucherMapper = new VoucherMapper();

    /**
     * Lấy tất cả voucher theo bộ lọc
     */
    public List<Voucher> search(String search, String giamGia, String trangThai) {
        List<Voucher> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT ID_Voucher, MaVoucher, ISNULL(TenChuongTrinh, N'Voucher tri ân khách hàng DUNA Men') AS TenChuongTrinh, " +
            "GiamGia, ISNULL(DonToiThieu, GiamGia * 5) AS DonToiThieu, SoLuong, DaSuDung, " +
            "ISNULL(DonHangSuDung, '') AS DonHangSuDung, ISNULL(TrangThai, 1) AS TrangThai, NgayTao " +
            "FROM Vouchers WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (MaVoucher LIKE ? OR TenChuongTrinh LIKE ? OR DonHangSuDung LIKE ?) ");
            String kw = "%" + search.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (giamGia != null && !giamGia.trim().isEmpty()) {
            try {
                long val = Long.parseLong(giamGia.trim());
                sql.append(" AND GiamGia = ? ");
                params.add(val);
            } catch (NumberFormatException ignored) {}
        }
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            try {
                int st = Integer.parseInt(trangThai.trim());
                sql.append(" AND ISNULL(TrangThai, 1) = ? ");
                params.add(st);
            } catch (NumberFormatException ignored) {}
        }
        sql.append(" ORDER BY ID_Voucher DESC");

        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(voucherMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách dạng Map
     */
    public List<Map<String, Object>> searchAsMap(String search, String giamGia, String trangThai) {
        List<Voucher> entities = search(search, giamGia, trangThai);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Voucher v : entities) {
            mapList.add(voucherMapper.toMap(v));
        }
        return mapList;
    }

    /**
     * Tìm voucher theo mã
     */
    public Voucher getByCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;
        String sql = "SELECT ID_Voucher, MaVoucher, ISNULL(TenChuongTrinh, N'Voucher tri ân khách hàng DUNA Men') AS TenChuongTrinh, " +
                     "GiamGia, ISNULL(DonToiThieu, GiamGia * 5) AS DonToiThieu, SoLuong, DaSuDung, " +
                     "ISNULL(DonHangSuDung, '') AS DonHangSuDung, ISNULL(TrangThai, 1) AS TrangThai, NgayTao " +
                     "FROM Vouchers WHERE UPPER(MaVoucher) = UPPER(?)";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return voucherMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Đổi trạng thái kích hoạt của voucher
     */
    public boolean toggleStatus(int id) {
        String sql = "UPDATE Vouchers SET TrangThai = CASE WHEN ISNULL(TrangThai, 1) = 1 THEN 0 ELSE 1 END WHERE ID_Voucher = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

