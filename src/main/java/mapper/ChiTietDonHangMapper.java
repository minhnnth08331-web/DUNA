package mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import model.ChiTietDonHang;

/**
 * Mapper ánh xạ dữ liệu Chi tiết đơn hàng (ChiTietDonHang Entity Mapping)
 */
public class ChiTietDonHangMapper implements RowMapper<ChiTietDonHang> {

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
    public ChiTietDonHang mapRow(ResultSet rs) throws SQLException {
        ChiTietDonHang item = new ChiTietDonHang();

        if (hasColumn(rs, "ID_ChiTiet")) item.setIdChiTiet(rs.getInt("ID_ChiTiet"));
        else if (hasColumn(rs, "idChiTiet")) item.setIdChiTiet(rs.getInt("idChiTiet"));

        if (hasColumn(rs, "ID_DonHang")) item.setIdDonHang(rs.getInt("ID_DonHang"));
        if (hasColumn(rs, "ID_SanPham")) item.setIdSanPham(rs.getInt("ID_SanPham"));
        if (hasColumn(rs, "ID_BienThe")) item.setIdBienThe(rs.getInt("ID_BienThe"));

        if (hasColumn(rs, "MaSP")) item.setMaSP(rs.getString("MaSP"));
        if (hasColumn(rs, "TenSP")) item.setTenSP(rs.getString("TenSP"));
        if (hasColumn(rs, "SoLuong")) item.setSoLuong(rs.getInt("SoLuong"));
        if (hasColumn(rs, "DonGia")) item.setDonGia(rs.getLong("DonGia"));
        if (hasColumn(rs, "ThanhTien")) item.setThanhTien(rs.getLong("ThanhTien"));

        if (hasColumn(rs, "MauSac")) item.setMauSac(rs.getString("MauSac"));
        if (hasColumn(rs, "KichCo")) item.setKichCo(rs.getString("KichCo"));
        else if (hasColumn(rs, "TrongLuong")) item.setKichCo(rs.getString("TrongLuong"));
        else if (hasColumn(rs, "size")) item.setKichCo(rs.getString("size"));

        return item;
    }

    @Override
    public Map<String, Object> toMap(ChiTietDonHang entity) {
        if (entity == null) return null;
        return entity.toMap();
    }
}

