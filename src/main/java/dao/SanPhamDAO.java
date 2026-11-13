package dao;

import connectService.connectService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mapper.BienTheSanPhamMapper;
import mapper.SanPhamMapper;
import model.BienTheSanPham;
import model.SanPham;

/**
 * Data Access Object cho Sản phẩm - Chuỗi thời trang nam DUNA
 */
public class SanPhamDAO {
    private final connectService db = new connectService();
    private final SanPhamMapper sanPhamMapper = new SanPhamMapper();
    private final BienTheSanPhamMapper bienTheMapper = new BienTheSanPhamMapper();

    /**
     * Lấy danh sách sản phẩm có tìm kiếm và bộ lọc
     */
    public List<SanPham> search(String search, String cat, String status) {
        List<SanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT sp.ID_SanPham, sp.MaSP, sp.TenSP, dm.TenDanhMuc, " +
            "sp.ThuongHieu, sp.GiaBan, sp.GiaNhap, " +
            "ISNULL((SELECT SUM(bt.SoLuong) FROM BienTheSanPham bt WHERE bt.ID_SanPham = sp.ID_SanPham), sp.TongSoLuong) AS TongSoLuong, " +
            "sp.TrangThai, sp.ChatLieu, sp.FormDang, sp.HinhAnh, sp.MoTa, sp.NgayTao, sp.NgayCapNhat " +
            "FROM SanPham sp " +
            "INNER JOIN DanhMuc dm ON sp.ID_DanhMuc = dm.ID_DanhMuc " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (sp.TenSP LIKE ? OR sp.MaSP LIKE ? OR sp.ChatLieu LIKE ? OR sp.FormDang LIKE ?) ");
            String kw = "%" + search.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (cat != null && !cat.trim().isEmpty()) {
            sql.append("AND dm.TenDanhMuc = ? ");
            params.add(cat.trim());
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND sp.TrangThai = ? ");
            params.add(status.trim());
        }
        sql.append("ORDER BY sp.ID_SanPham DESC");

        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(sanPhamMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách dạng List<Map<String, Object>> để tương thích ngược 100% với JSP
     */
    public List<Map<String, Object>> searchAsMap(String search, String cat, String status) {
        List<SanPham> entities = search(search, cat, status);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (SanPham sp : entities) {
            mapList.add(sanPhamMapper.toMap(sp));
        }
        return mapList;
    }

    /**
     * Lấy thông tin chi tiết một sản phẩm theo ID
     */
    public SanPham getById(int id) {
        String sql = "SELECT sp.ID_SanPham, sp.MaSP, sp.TenSP, dm.TenDanhMuc, sp.ID_DanhMuc, " +
                     "sp.ThuongHieu, sp.GiaBan, sp.GiaNhap, " +
                     "ISNULL((SELECT SUM(bt.SoLuong) FROM BienTheSanPham bt WHERE bt.ID_SanPham = sp.ID_SanPham), sp.TongSoLuong) AS TongSoLuong, " +
                     "sp.TrangThai, sp.ChatLieu, sp.FormDang, sp.HinhAnh, sp.MoTa, sp.NgayTao, sp.NgayCapNhat " +
                     "FROM SanPham sp " +
                     "INNER JOIN DanhMuc dm ON sp.ID_DanhMuc = dm.ID_DanhMuc " +
                     "WHERE sp.ID_SanPham = ?";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = sanPhamMapper.mapRow(rs);
                    sp.setDanhSachBienThe(getVariantsByProductId(id));
                    return sp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách các biến thể (size, màu sắc) của một sản phẩm
     */
    public List<BienTheSanPham> getVariantsByProductId(int productId) {
        List<BienTheSanPham> list = new ArrayList<>();
        String sql = "SELECT ID_BienThe, ID_SanPham, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode, MaBienThe " +
                     "FROM BienTheSanPham WHERE ID_SanPham = ? ORDER BY ID_BienThe";
        try (Connection conn = db.myConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(bienTheMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

