package com.duna.thanhvien1_minh.repository;

import com.duna.common.util.SizeGuideUtil;

/**
 * MEMBER 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * UC 16: Bảng tra cứu tư vấn Size thông minh theo Chiều cao - Cân nặng
 */
public class UC16_TuVanSizeThongMinhRepo {

    public String tuVanSize(int chieuCaoCm, int canNangKg) {
        return SizeGuideUtil.recommendSize(chieuCaoCm, canNangKg);
    }
}
