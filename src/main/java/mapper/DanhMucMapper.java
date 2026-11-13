package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import model.DanhMuc;

/**
 * Mapper ánh xạ dữ liệu Danh mục (DanhMuc Entity Mapping)
 */
public class DanhMucMapper implements RowMapper<DanhMuc> {

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
    public DanhMuc mapRow(ResultSet rs) throws SQLException {
        DanhMuc dm = new DanhMuc();
        if (hasColumn(rs, "ID_DanhMuc")) dm.setIdDanhMuc(rs.getInt("ID_DanhMuc"));
        else if (hasColumn(rs, "id")) dm.setIdDanhMuc(rs.getInt("id"));

        if (hasColumn(rs, "TenDanhMuc")) dm.setTenDanhMuc(rs.getString("TenDanhMuc"));
        if (hasColumn(rs, "MoTa")) dm.setMoTa(rs.getString("MoTa"));

        return dm;
    }

    @Override
    public Map<String, Object> toMap(DanhMuc entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

