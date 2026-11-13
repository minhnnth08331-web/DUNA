package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import model.DonHang;

/**
 * Mapper ánh xạ dữ liệu Đơn hàng (DonHang Entity Mapping)
 */
public class DonHangMapper implements RowMapper<DonHang> {

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            for (int x = 1; x <= count; x++) {
                if (columnName.equalsIgnoreCase(rsmd.getColumnLabel(x)) ||
                    columnName.equalsIgnoreCase(rsmd.getColumnName(x))) {
                    return true;
                }
            }
        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public DonHang mapRow(ResultSet rs) throws SQLException {
        DonHang dh = new DonHang();

        if (hasColumn(rs, "ID_DonHang")) dh.setId(rs.getInt("ID_DonHang"));
        else if (hasColumn(rs, "id")) dh.setId(rs.getInt("id"));

        if (hasColumn(rs, "MaDonHang")) dh.setMaDonHang(rs.getString("MaDonHang"));

        if (hasColumn(rs, "ID_KhachHang")) {
            int idKH = rs.getInt("ID_KhachHang");
            if (!rs.wasNull()) dh.setIdKhachHang(idKH);
        }

        if (hasColumn(rs, "TenKhachHang")) dh.setTenKhachHang(rs.getString("TenKhachHang"));
        else if (hasColumn(rs, "HoVaTen")) dh.setTenKhachHang(rs.getString("HoVaTen"));

        if (hasColumn(rs, "SDT")) dh.setSdtKhachHang(rs.getString("SDT"));
        else if (hasColumn(rs, "SoDienThoai")) dh.setSdtKhachHang(rs.getString("SoDienThoai"));

        if (hasColumn(rs, "ID_NhanVien")) {
            int idNV = rs.getInt("ID_NhanVien");
            if (!rs.wasNull()) dh.setIdNhanVien(idNV);
        }

        if (hasColumn(rs, "TenNhanVien")) dh.setTenNhanVien(rs.getString("TenNhanVien"));

        if (hasColumn(rs, "TongTien")) dh.setTongTien(rs.getLong("TongTien"));
        if (hasColumn(rs, "GiamGia")) dh.setGiamGia(rs.getLong("GiamGia"));
        if (hasColumn(rs, "ThanhTien")) dh.setThanhTien(rs.getLong("ThanhTien"));

        if (hasColumn(rs, "TrangThaiDH_ID")) dh.setTrangThaiDH_ID(rs.getInt("TrangThaiDH_ID"));
        if (hasColumn(rs, "TenTrangThai")) dh.setTenTrangThai(rs.getString("TenTrangThai"));

        if (hasColumn(rs, "PhuongThucTT")) dh.setPhuongThucTT(rs.getString("PhuongThucTT"));
        if (hasColumn(rs, "GhiChu")) dh.setGhiChu(rs.getString("GhiChu"));
        if (hasColumn(rs, "LoaiDonHang")) dh.setLoaiDonHang(rs.getString("LoaiDonHang"));

        if (hasColumn(rs, "NgayTao")) {
            Timestamp ts = rs.getTimestamp("NgayTao");
            if (ts != null) {
                dh.setNgayTao(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts));
            }
        }

        if (hasColumn(rs, "TongSoLuong")) {
            dh.setSoLuongSP(rs.getInt("TongSoLuong"));
        } else if (hasColumn(rs, "SoLuongSP")) {
            dh.setSoLuongSP(rs.getInt("SoLuongSP"));
        }

        return dh;
    }

    @Override
    public Map<String, Object> toMap(DonHang entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

