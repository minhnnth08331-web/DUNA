package com.duna.thanhvien2_ducanh.repository;

import com.duna.common.entities.BienTheSanPham;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 26: Quét mã vạch sản phẩm trực tiếp qua Camera WebRTC tại quầy thu ngân
 */
public class UC26_QuetBarcodeCameraRepo {
    private final UC25_QuetBarcodeUSBRepo uc25 = new UC25_QuetBarcodeUSBRepo();

    public BienTheSanPham handleWebRTCScan(String scannedBarcode) {
        return uc25.findByBarcodeUSB(scannedBarcode);
    }
}
