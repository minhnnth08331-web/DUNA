package com.duna.thanhvien3_bach.repository;

import java.text.SimpleDateFormat;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 41: In hóa đơn thanh toán nhiệt khổ K80/K57 tại quầy thu ngân
 */
public class UC41_InHoaDonNhietK80Repo {

    public String taoNoiDungHoaDonK80(int idDonHang) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         DUNA MEN'S WEAR POS            \n");
        sb.append("      Thời Trang Nam Tầm Trung         \n");
        sb.append("========================================\n");
        sb.append("Mã HĐ: HD").append(idDonHang).append("\n");
        sb.append("Ngày: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date())).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Cảm ơn Quý khách & Hẹn gặp lại!\n");
        return sb.toString();
    }
}
