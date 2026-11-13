package dao;

import connectService.connectService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mapper.KhachHangMapper;
import model.KhachHang;

/**
 * Data Access Object cho Khách hàng - DUNA Men's Wear
 */
public class KhachHangDAO {
    private final connectService db = new connectService();
    private final KhachHangMapper khachHangMapper = new KhachHangMapper();

    /**
     * Lấy toàn bộ danh sách khách hàng kèm tổng chi tiêu và tổng số sản phẩm đã mua
     */
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT k.ID_KhachHang, k.MaKhachHang, k.HoVaTen, k.SoDienThoai, k.Email, k.DiaChi, k.GioiTinh, " +
                     "k.HangKhachHang, " +
                     "ISNULL(Spend.TongTien, 0) AS TongTienChiTieu, " +
                     "ISNULL(TotalQty.TongSP, 0) AS TongSPMua, " +
                     "k.TrangThai, k.NgayBatDauChuKy, k.NgayTao " +
                     "FROM KhachHang k " +
                     "LEFT JOIN ( " +
                     "    SELECT dh.ID_KhachHang, SUM(dh.ThanhTien) AS TongTien " +
                     "    FROM DonHang dh " +
                     "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                     "    GROUP BY dh.ID_KhachHang " +
                     ") Spend ON k.ID_KhachHang = Spend.ID_KhachHang " +
                     "LEFT JOIN ( " +
                     "    SELECT dh.ID_KhachHang, SUM(ct.SoLuong) AS TongSP " +
                     "    FROM DonHang dh " +
                     "    JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                     "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                     "    GROUP BY dh.ID_KhachHang " +
                     ") TotalQty ON k.ID_KhachHang = TotalQty.ID_KhachHang " +
                     "ORDER BY k.ID_KhachHang DESC";

        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(khachHangMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách dạng Map
     */
    public List<Map<String, Object>> getAllAsMap() {
        List<KhachHang> list = getAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (KhachHang kh : list) {
            mapList.add(khachHangMapper.toMap(kh));
        }
        return mapList;
    }

    /**
     * Tìm khách hàng theo số điện thoại
     */
    public KhachHang getByPhone(String phone) {
        String sql = "SELECT k.ID_KhachHang, k.MaKhachHang, k.HoVaTen, k.SoDienThoai, k.Email, k.DiaChi, k.GioiTinh, " +
                     "k.HangKhachHang, " +
                     "ISNULL(Spend.TongTien, 0) AS TongTienChiTieu, " +
                     "ISNULL(TotalQty.TongSP, 0) AS TongSPMua, " +
                     "k.TrangThai, k.NgayBatDauChuKy, k.NgayTao " +
                     "FROM KhachHang k " +
                     "LEFT JOIN ( " +
                     "    SELECT dh.ID_KhachHang, SUM(dh.ThanhTien) AS TongTien " +
                     "    FROM DonHang dh " +
                     "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                     "    GROUP BY dh.ID_KhachHang " +
                     ") Spend ON k.ID_KhachHang = Spend.ID_KhachHang " +
                     "LEFT JOIN ( " +
                     "    SELECT dh.ID_KhachHang, SUM(ct.SoLuong) AS TongSP " +
                     "    FROM DonHang dh " +
                     "    JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                     "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                     "    GROUP BY dh.ID_KhachHang " +
                     ") TotalQty ON k.ID_KhachHang = TotalQty.ID_KhachHang " +
                     "WHERE k.SoDienThoai = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return khachHangMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy khách hàng theo ID
     */
    public KhachHang getById(int id) {
        String sql = "SELECT k.ID_KhachHang, k.MaKhachHang, k.HoVaTen, k.SoDienThoai, k.Email, k.DiaChi, k.GioiTinh, " +
                     "k.HangKhachHang, " +
                     "ISNULL(Spend.TongTien, 0) AS TongTienChiTieu, " +
                     "ISNULL(TotalQty.TongSP, 0) AS TongSPMua, " +
                     "k.TrangThai, k.NgayBatDauChuKy, k.NgayTao " +
                     "FROM KhachHang k " +
                     "LEFT JOIN ( " +
                     "    SELECT dh.ID_KhachHang, SUM(dh.ThanhTien) AS TongTien " +
                     "    FROM DonHang dh " +
                     "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                     "    GROUP BY dh.ID_KhachHang " +
                     ") Spend ON k.ID_KhachHang = Spend.ID_KhachHang " +
                     "LEFT JOIN ( " +
                     "    SELECT dh.ID_KhachHang, SUM(ct.SoLuong) AS TongSP " +
                     "    FROM DonHang dh " +
                     "    JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                     "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                     "    GROUP BY dh.ID_KhachHang " +
                     ") TotalQty ON k.ID_KhachHang = TotalQty.ID_KhachHang " +
                     "WHERE k.ID_KhachHang = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return khachHangMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

