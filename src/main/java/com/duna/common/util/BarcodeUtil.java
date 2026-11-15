package com.duna.common.util;

/**
 * Lớp tiện ích sinh mã Barcode và định danh biến thể sản phẩm - DUNA Men's Wear
 * Phục vụ UC 21, 22, 25, 26
 */
public class BarcodeUtil {

    /**
     * Tự động sinh mã vạch Barcode chuẩn định danh biến thể (UC 21)
     * Cấu trúc: SP{id}-{MAU}-{SIZE} (Ví dụ: SP101-DEN-L)
     */
    public static String generateBarcode(String maSP, String mauSac, String kichThuoc) {
        String cleanMa = (maSP != null ? maSP.trim().toUpperCase() : "SP000");
        String cleanMau = (mauSac != null ? mauSac.trim().toUpperCase().replaceAll("\\s+", "") : "DEF");
        String cleanSize = (kichThuoc != null ? kichThuoc.trim().toUpperCase() : "FREESIZE");
        return cleanMa + "-" + cleanMau + "-" + cleanSize;
    }

    /**
     * Kiểm tra tính hợp lệ của mã vạch Barcode
     */
    public static boolean isValidBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return false;
        }
        return barcode.trim().matches("^[A-Za-z0-9\\-_]{4,30}$");
    }
}
