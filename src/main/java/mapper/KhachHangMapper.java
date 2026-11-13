package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import model.KhachHang;

/**
 * Mapper ánh xạ dữ liệu Khách hàng (KhachHang Entity Mapping)
 */
public class KhachHangMapper implements RowMapper<KhachHang> {

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
    public KhachHang mapRow(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();

        if (hasColumn(rs, "ID_KhachHang")) kh.setId(rs.getInt("ID_KhachHang"));
        else if (hasColumn(rs, "id")) kh.setId(rs.getInt("id"));

        if (hasColumn(rs, "MaKhachHang")) kh.setMaKhachHang(rs.getString("MaKhachHang"));
        if (hasColumn(rs, "HoVaTen")) kh.setHoVaTen(rs.getString("HoVaTen"));
        if (hasColumn(rs, "SoDienThoai")) kh.setSoDienThoai(rs.getString("SoDienThoai"));
        if (hasColumn(rs, "Email")) kh.setEmail(rs.getString("Email"));
        if (hasColumn(rs, "DiaChi")) kh.setDiaChi(rs.getString("DiaChi"));
        if (hasColumn(rs, "GioiTinh")) kh.setGioiTinh(rs.getString("GioiTinh"));

        String rank = "Thường";
        if (hasColumn(rs, "HangKhachHang")) {
            String r = rs.getString("HangKhachHang");
            if (r != null && !r.trim().isEmpty()) rank = r.trim();
        }
        kh.setHangKhachHang(rank);

        // Chiết khấu theo hạng
        int discount = 0;
        if ("VIP".equalsIgnoreCase(rank)) discount = 7;
        else if ("Vàng".equalsIgnoreCase(rank)) discount = 4;
        else if ("Bạc".equalsIgnoreCase(rank)) discount = 2;
        kh.setPhanTramGiam(discount);

        if (hasColumn(rs, "TongTienChiTieu")) kh.setTongTienChiTieu(rs.getLong("TongTienChiTieu"));
        if (hasColumn(rs, "TongSPMua")) kh.setTongSPMua(rs.getInt("TongSPMua"));
        if (hasColumn(rs, "TrangThai")) kh.setTrangThai(rs.getString("TrangThai"));

        if (hasColumn(rs, "NgayBatDauChuKy")) {
            Timestamp ts = rs.getTimestamp("NgayBatDauChuKy");
            if (ts != null) kh.setNgayBatDauChuKy(new SimpleDateFormat("dd/MM/yyyy").format(ts));
        }

        if (hasColumn(rs, "hanGiuHang")) {
            kh.setHanGiuHang(rs.getString("hanGiuHang"));
        }

        if (hasColumn(rs, "NgayTao")) {
            Timestamp ts = rs.getTimestamp("NgayTao");
            if (ts != null) kh.setNgayTao(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts));
        }

        return kh;
    }

    @Override
    public Map<String, Object> toMap(KhachHang entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

