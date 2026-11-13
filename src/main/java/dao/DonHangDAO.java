package dao;

import connectService.connectService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mapper.ChiTietDonHangMapper;
import mapper.DonHangMapper;
import model.ChiTietDonHang;
import model.DonHang;

/**
 * Data Access Object cho Đơn hàng / Hóa đơn - DUNA Men's Wear POS
 */
public class DonHangDAO {
    private final connectService db = new connectService();
    private final DonHangMapper donHangMapper = new DonHangMapper();
    private final ChiTietDonHangMapper chiTietMapper = new ChiTietDonHangMapper();

    /**
     * Lấy danh sách hóa đơn theo trạng thái và tìm kiếm
     */
    public List<DonHang> getAll(String search, String trangThai) {
        List<DonHang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT dh.ID_DonHang, dh.MaDonHang, dh.ID_KhachHang, " +
            "ISNULL(kh.HoVaTen, N'Khách lẻ tại quầy') AS TenKhachHang, " +
            "ISNULL(kh.SoDienThoai, '') AS SDT, " +
            "dh.ID_NhanVien, nv.HoVaTen AS TenNhanVien, dh.NgayTao, " +
            "dh.TongTien, dh.GiamGia, dh.ThanhTien, " +
            "dh.TrangThaiDH_ID, ttdh.TenTrangThai, dh.PhuongThucTT, dh.GhiChu, " +
            "(SELECT ISNULL(SUM(ct.SoLuong), 0) FROM ChiTietDonHang ct WHERE ct.ID_DonHang = dh.ID_DonHang) AS TongSoLuong " +
            "FROM DonHang dh " +
            "JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
            "LEFT JOIN KhachHang kh ON dh.ID_KhachHang = kh.ID_KhachHang " +
            "LEFT JOIN NhanVien nv ON dh.ID_NhanVien = nv.ID_NhanVien " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (dh.MaDonHang LIKE ? OR kh.HoVaTen LIKE ? OR kh.SoDienThoai LIKE ?) ");
            String kw = "%" + search.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            sql.append(" AND ttdh.TenTrangThai = ? ");
            params.add(trangThai.trim());
        }
        sql.append(" ORDER BY dh.ID_DonHang DESC");

        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(donHangMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy chi tiết đơn hàng theo ID
     */
    public DonHang getById(int id) {
        String sql = "SELECT dh.ID_DonHang, dh.MaDonHang, dh.ID_KhachHang, " +
                     "ISNULL(kh.HoVaTen, N'Khách lẻ tại quầy') AS TenKhachHang, " +
                     "ISNULL(kh.SoDienThoai, '') AS SDT, " +
                     "dh.ID_NhanVien, nv.HoVaTen AS TenNhanVien, dh.NgayTao, " +
                     "dh.TongTien, dh.GiamGia, dh.ThanhTien, " +
                     "dh.TrangThaiDH_ID, ttdh.TenTrangThai, dh.PhuongThucTT, dh.GhiChu, " +
                     "(SELECT ISNULL(SUM(ct.SoLuong), 0) FROM ChiTietDonHang ct WHERE ct.ID_DonHang = dh.ID_DonHang) AS TongSoLuong " +
                     "FROM DonHang dh " +
                     "JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                     "LEFT JOIN KhachHang kh ON dh.ID_KhachHang = kh.ID_KhachHang " +
                     "LEFT JOIN NhanVien nv ON dh.ID_NhanVien = nv.ID_NhanVien " +
                     "WHERE dh.ID_DonHang = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonHang dh = donHangMapper.mapRow(rs);
                    dh.setDanhSachChiTiet(getOrderItems(id));
                    return dh;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách các mặt hàng trong đơn hàng
     */
    public List<ChiTietDonHang> getOrderItems(int orderId) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT ct.ID_ChiTiet, ct.ID_DonHang, ct.ID_SanPham, ct.ID_BienThe, " +
                     "sp.MaSP, ct.TenSP, ct.SoLuong, ct.DonGia, ct.ThanhTien, " +
                     "bt.MauSac, bt.TrongLuong AS KichCo " +
                     "FROM ChiTietDonHang ct " +
                     "LEFT JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                     "LEFT JOIN BienTheSanPham bt ON ct.ID_BienThe = bt.ID_BienThe " +
                     "WHERE ct.ID_DonHang = ? ORDER BY ct.ID_ChiTiet ASC";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(chiTietMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

