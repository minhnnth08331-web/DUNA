package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import model.Voucher;

/**
 * Mapper ánh xạ dữ liệu Voucher (Voucher Entity Mapping)
 */
public class VoucherMapper implements RowMapper<Voucher> {

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
    public Voucher mapRow(ResultSet rs) throws SQLException {
        Voucher v = new Voucher();

        if (hasColumn(rs, "ID_Voucher")) v.setId(rs.getInt("ID_Voucher"));
        else if (hasColumn(rs, "id")) v.setId(rs.getInt("id"));

        if (hasColumn(rs, "MaVoucher")) v.setMaVoucher(rs.getString("MaVoucher"));
        if (hasColumn(rs, "TenChuongTrinh")) v.setTenChuongTrinh(rs.getString("TenChuongTrinh"));
        if (hasColumn(rs, "GiamGia")) v.setGiamGia(rs.getLong("GiamGia"));
        if (hasColumn(rs, "DonToiThieu")) v.setDonToiThieu(rs.getLong("DonToiThieu"));
        if (hasColumn(rs, "SoLuong")) v.setSoLuong(rs.getInt("SoLuong"));
        if (hasColumn(rs, "DaSuDung")) v.setDaSuDung(rs.getInt("DaSuDung"));
        if (hasColumn(rs, "DonHangSuDung")) v.setDonHangSuDung(rs.getString("DonHangSuDung"));
        if (hasColumn(rs, "TrangThai")) v.setTrangThai(rs.getInt("TrangThai"));

        if (hasColumn(rs, "LoaiVoucher")) v.setLoaiVoucher(rs.getString("LoaiVoucher"));
        else {
            v.setLoaiVoucher((v.getMaVoucher() != null && v.getMaVoucher().toUpperCase().contains("VIP")) ? "VIP" : "Thường");
        }

        if (hasColumn(rs, "NgayTao")) {
            Timestamp ts = rs.getTimestamp("NgayTao");
            if (ts != null) {
                v.setNgayTao(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts));
            }
        }

        return v;
    }

    @Override
    public Map<String, Object> toMap(Voucher entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

