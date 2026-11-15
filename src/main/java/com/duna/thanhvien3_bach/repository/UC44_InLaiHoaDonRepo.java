package com.duna.thanhvien3_bach.repository;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 44: In lại hóa đơn bán hàng cho khách khi có khiếu nại (Re-print)
 */
public class UC44_InLaiHoaDonRepo {
    private final UC41_InHoaDonNhietK80Repo uc41 = new UC41_InHoaDonNhietK80Repo();

    public String reprintHoaDon(int idDonHang) {
        return "[BẢN IN LẠI - REPRINT]\n" + uc41.taoNoiDungHoaDonK80(idDonHang);
    }
}
