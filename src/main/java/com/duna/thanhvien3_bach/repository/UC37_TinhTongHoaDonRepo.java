package com.duna.thanhvien3_bach.repository;

import java.util.*;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 37: Tự động tính toán tổng hóa đơn (Tiền hàng, giảm VIP, Voucher, VAT)
 */
public class UC37_TinhTongHoaDonRepo {

    public Map<String, Long> tinhToanTongHoaDon(long subtotal, long vipDiscount, long voucherDiscount, boolean includeVAT) {
        long tongGiam = vipDiscount + voucherDiscount;
        long afterDiscount = Math.max(0, subtotal - tongGiam);
        long vat = includeVAT ? Math.round(afterDiscount * 0.08) : 0;
        long total = afterDiscount + vat;

        Map<String, Long> res = new HashMap<>();
        res.put("tienHang", subtotal);
        res.put("giamVIP", vipDiscount);
        res.put("giamVoucher", voucherDiscount);
        res.put("thueVAT", vat);
        res.put("tongThanhToan", total);
        return res;
    }
}
