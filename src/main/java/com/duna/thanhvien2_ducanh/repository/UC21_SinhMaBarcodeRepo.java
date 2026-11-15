package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.util.BarcodeUtil;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 21: Tự động sinh mã Barcode định danh biến thể (SPxxx-MAU-SIZE)
 */
public class UC21_SinhMaBarcodeRepo {

    public String generateBarcode(String maSP, String mauSac, String kichThuoc) {
        return BarcodeUtil.generateBarcode(maSP, mauSac, kichThuoc);
    }
}
