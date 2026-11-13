package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import model.BienTheSanPham;

/**
 * Mapper ánh xạ dữ liệu Biến thể sản phẩm (BienTheSanPham Entity Mapping)
 */
public class BienTheSanPhamMapper implements RowMapper<BienTheSanPham> {

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
    public BienTheSanPham mapRow(ResultSet rs) throws SQLException {
        BienTheSanPham bt = new BienTheSanPham();

        if (hasColumn(rs, "ID_BienThe")) bt.setIdBienThe(rs.getInt("ID_BienThe"));
        else if (hasColumn(rs, "id")) bt.setIdBienThe(rs.getInt("id"));

        if (hasColumn(rs, "ID_SanPham")) bt.setIdSanPham(rs.getInt("ID_SanPham"));
        if (hasColumn(rs, "MauSac")) bt.setMauSac(rs.getString("MauSac"));

        if (hasColumn(rs, "TrongLuong")) bt.setTrongLuong(rs.getString("TrongLuong"));
        else if (hasColumn(rs, "size")) bt.setTrongLuong(rs.getString("size"));
        else if (hasColumn(rs, "KichCo")) bt.setTrongLuong(rs.getString("KichCo"));

        if (hasColumn(rs, "GiaNhap")) bt.setGiaNhap(rs.getLong("GiaNhap"));
        if (hasColumn(rs, "GiaBan")) bt.setGiaBan(rs.getLong("GiaBan"));
        if (hasColumn(rs, "SoLuong")) bt.setSoLuong(rs.getInt("SoLuong"));
        if (hasColumn(rs, "Barcode")) bt.setBarcode(rs.getString("Barcode"));
        if (hasColumn(rs, "MaBienThe")) bt.setMaBienThe(rs.getString("MaBienThe"));

        return bt;
    }

    @Override
    public Map<String, Object> toMap(BienTheSanPham entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

