package com.duna.thanhvien3_bach.repository;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 35: Tự động áp dụng chiết khấu hạng VIP vào giỏ hàng POS
 */
public class UC35_ApDungChietKhauVIPRepo {

    public long tinhGiamGiaHangVIP(long subtotal, String hangVIP) {
        if (hangVIP == null) return 0;
        double rate = 0.0;
        if ("Bạc".equalsIgnoreCase(hangVIP)) rate = 0.02;
        else if ("Vàng".equalsIgnoreCase(hangVIP)) rate = 0.05;
        else if ("VIP".equalsIgnoreCase(hangVIP) || "Kim Cương".equalsIgnoreCase(hangVIP)) rate = 0.10;
        return Math.round(subtotal * rate);
    }
}
