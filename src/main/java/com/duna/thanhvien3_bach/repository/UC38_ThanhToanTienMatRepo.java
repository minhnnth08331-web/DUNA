package com.duna.thanhvien3_bach.repository;

import com.duna.common.util.CurrencyUtil;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 38: Tính tiền thối tự động khi khách thanh toán bằng Tiền mặt
 */
public class UC38_ThanhToanTienMatRepo {

    public double tinhTienThoi(double totalAmount, double cashGiven) {
        return CurrencyUtil.calculateChange(totalAmount, cashGiven);
    }
}
