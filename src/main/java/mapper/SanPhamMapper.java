package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import model.SanPham;

/**
 * Mapper ánh xạ dữ liệu Sản phẩm (SanPham Entity Mapping)
 */
public class SanPhamMapper implements RowMapper<SanPham> {

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
    public SanPham mapRow(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();

        if (hasColumn(rs, "ID_SanPham")) {
            sp.setId(rs.getInt("ID_SanPham"));
        } else if (hasColumn(rs, "id")) {
            sp.setId(rs.getInt("id"));
        }

        if (hasColumn(rs, "MaSP")) sp.setMaSP(rs.getString("MaSP"));
        if (hasColumn(rs, "TenSP")) sp.setTenSP(rs.getString("TenSP"));

        if (hasColumn(rs, "ID_DanhMuc")) sp.setIdDanhMuc(rs.getInt("ID_DanhMuc"));
        if (hasColumn(rs, "TenDanhMuc")) {
            sp.setDanhMuc(rs.getString("TenDanhMuc"));
        } else if (hasColumn(rs, "danhMuc")) {
            sp.setDanhMuc(rs.getString("danhMuc"));
        }

        if (hasColumn(rs, "ThuongHieu")) sp.setThuongHieu(rs.getString("ThuongHieu"));
        if (hasColumn(rs, "GiaBan")) sp.setGiaBan(rs.getLong("GiaBan"));
        if (hasColumn(rs, "GiaNhap")) sp.setGiaNhap(rs.getLong("GiaNhap"));

        if (hasColumn(rs, "TongSoLuong")) {
            sp.setSoLuong(rs.getInt("TongSoLuong"));
        } else if (hasColumn(rs, "SoLuong")) {
            sp.setSoLuong(rs.getInt("SoLuong"));
        }

        if (hasColumn(rs, "TrangThai")) sp.setTrangThai(rs.getString("TrangThai"));
        if (hasColumn(rs, "ChatLieu")) sp.setChatLieu(rs.getString("ChatLieu"));
        if (hasColumn(rs, "FormDang")) sp.setFormDang(rs.getString("FormDang"));
        if (hasColumn(rs, "HinhAnh")) sp.setHinhAnh(rs.getString("HinhAnh"));
        if (hasColumn(rs, "MoTa")) sp.setMoTa(rs.getString("MoTa"));

        String ntStr = "", ncnStr = "";
        try {
            if (hasColumn(rs, "NgayTao")) {
                Timestamp ts1 = rs.getTimestamp("NgayTao");
                if (ts1 != null) ntStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts1);
            }
            if (hasColumn(rs, "NgayCapNhat")) {
                Timestamp ts2 = rs.getTimestamp("NgayCapNhat");
                if (ts2 != null) ncnStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts2);
            }
        } catch (Exception exDate) {
            try {
                if (hasColumn(rs, "NgayTao")) ntStr = rs.getString("NgayTao");
                if (hasColumn(rs, "NgayCapNhat")) ncnStr = rs.getString("NgayCapNhat");
            } catch (Exception ignored) {}
        }
        sp.setNgayTao(ntStr != null ? ntStr : "");
        sp.setNgayCapNhat(ncnStr != null ? ncnStr : "");

        return sp;
    }

    @Override
    public Map<String, Object> toMap(SanPham entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

