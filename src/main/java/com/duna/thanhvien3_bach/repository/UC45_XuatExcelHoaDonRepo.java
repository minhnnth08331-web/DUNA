package com.duna.thanhvien3_bach.repository;

import com.duna.common.entities.DonHang;
import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 45: Xuất dữ liệu danh sách hóa đơn bán hàng ra file Excel (POI)
 */
public class UC45_XuatExcelHoaDonRepo {
    private final UC42_DanhSachHoaDonRepo uc42 = new UC42_DanhSachHoaDonRepo();

    public List<Map<String, Object>> exportOrdersData(String fromDate, String toDate) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<DonHang> orders = uc42.getDonHangTheoNgay(fromDate, toDate);
        for (DonHang d : orders) {
            Map<String, Object> row = new HashMap<>();
            row.put("maDH", d.getMaDonHang());
            row.put("thanhTien", d.getThanhTien());
            row.put("trangThai", d.getTenTrangThai());
            list.add(row);
        }
        return list;
    }
}
