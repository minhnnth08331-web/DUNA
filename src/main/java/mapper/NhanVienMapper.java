package mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import model.NhanVien;

/**
 * Mapper ánh xạ dữ liệu Nhân viên (NhanVien Entity Mapping)
 */
public class NhanVienMapper implements RowMapper<NhanVien> {

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
    public NhanVien mapRow(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();

        if (hasColumn(rs, "ID_NhanVien")) nv.setId(rs.getInt("ID_NhanVien"));
        else if (hasColumn(rs, "id")) nv.setId(rs.getInt("id"));

        if (hasColumn(rs, "MaNhanVien")) nv.setMaNhanVien(rs.getString("MaNhanVien"));
        if (hasColumn(rs, "HoVaTen")) nv.setHoVaTen(rs.getString("HoVaTen"));
        if (hasColumn(rs, "SoDienThoai")) nv.setSoDienThoai(rs.getString("SoDienThoai"));
        if (hasColumn(rs, "Email")) nv.setEmail(rs.getString("Email"));
        if (hasColumn(rs, "MaSoCCCD")) nv.setMaSoCCCD(rs.getString("MaSoCCCD"));

        if (hasColumn(rs, "NgaySinh")) {
            Date d = rs.getDate("NgaySinh");
            if (d != null) nv.setNgaySinh(new SimpleDateFormat("dd/MM/yyyy").format(d));
        }

        if (hasColumn(rs, "GioiTinh")) nv.setGioiTinh(rs.getString("GioiTinh"));
        if (hasColumn(rs, "DiaChi")) nv.setDiaChi(rs.getString("DiaChi"));
        if (hasColumn(rs, "DiaChiHienTai")) nv.setDiaChiHienTai(rs.getString("DiaChiHienTai"));

        if (hasColumn(rs, "NgayHetHanCCCD")) {
            Date d = rs.getDate("NgayHetHanCCCD");
            if (d != null) nv.setNgayHetHanCCCD(new SimpleDateFormat("dd/MM/yyyy").format(d));
        }

        if (hasColumn(rs, "TrangThaiNV_ID")) nv.setTrangThaiNV_ID(rs.getInt("TrangThaiNV_ID"));
        if (hasColumn(rs, "TenTrangThai")) nv.setTenTrangThai(rs.getString("TenTrangThai"));

        if (hasColumn(rs, "Role_ID")) nv.setRoleId(rs.getInt("Role_ID"));
        if (hasColumn(rs, "Username")) nv.setUsername(rs.getString("Username"));

        return nv;
    }

    @Override
    public Map<String, Object> toMap(NhanVien entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

