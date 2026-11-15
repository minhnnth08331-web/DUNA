package com.duna.thanhvien1_minh.repository;

import java.util.*;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 10: Cấu hình Giá niêm yết bán lẻ & Thiết lập tỷ lệ thuế VAT 8%
 */
public class UC10_CauHinhGiaBanVATRepo {

    public Map<String, Object> cauHinhGiaNiemYetVAT(long giaBanChuaThue) {
        Map<String, Object> res = new HashMap<>();
        double vatRate = 0.08;
        long vatAmount = Math.round(giaBanChuaThue * vatRate);
        long giaNiemYet = giaBanChuaThue + vatAmount;
        res.put("giaChuaThue", giaBanChuaThue);
        res.put("thueVAT", vatAmount);
        res.put("giaNiemYet", giaNiemYet);
        return res;
    }
}
