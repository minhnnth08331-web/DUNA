package com.duna.common.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Lớp tiện ích định dạng tiền tệ và tính toán tài chính - Chuỗi thời trang nam DUNA
 */
public class CurrencyUtil {
    private static final DecimalFormat vndFormat;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("#,##0", symbols);
    }

    /**
     * Định dạng số tiền sang chuỗi VNĐ (Ví dụ: 350.000 đ)
     */
    public static String formatVND(double amount) {
        return vndFormat.format(amount) + " đ";
    }

    /**
     * Tính tiền thối trả lại cho khách tại quầy POS (UC 38)
     */
    public static double calculateChange(double totalAmount, double cashGiven) {
        if (cashGiven < totalAmount) {
            return 0.0;
        }
        return cashGiven - totalAmount;
    }
}
