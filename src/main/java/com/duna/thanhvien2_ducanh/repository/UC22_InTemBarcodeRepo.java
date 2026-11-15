package com.duna.thanhvien2_ducanh.repository;

import java.util.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 22: Xem trước thẻ bài & In tem mác Barcode DUNA (JsBarcode Engine)
 */
public class UC22_InTemBarcodeRepo {

    public Map<String, String> renderBarcodeLabel(String maBarcode, String tenSP, long giaBan) {
        Map<String, String> label = new HashMap<>();
        label.put("brand", "DUNA MEN'S WEAR");
        label.put("barcode", maBarcode);
        label.put("productName", tenSP);
        label.put("price", String.format("%,d đ", giaBan));
        return label;
    }
}
