package com.duna.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Lớp tiện ích xử lý thời gian và chu kỳ hội viên 3 tháng - DUNA Men's Wear
 * Phục vụ UC 42, 46, 47, 55, 58
 */
public class DateUtil {
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATETIME_PATTERN).format(date);
    }

    /**
     * Tính toán ngày hết hạn giữ hạng theo chu kỳ 3 tháng cuốn chiếu (UC 55)
     */
    public static Date calculate3MonthExpiry(Date fromDate) {
        if (fromDate == null) fromDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }

    /**
     * Kiểm tra khách hàng có còn trong chu kỳ 3 tháng hợp lệ không
     */
    public static boolean isWithin3MonthCycle(Date lastPurchaseDate) {
        if (lastPurchaseDate == null) return false;
        Date expiry = calculate3MonthExpiry(lastPurchaseDate);
        return new Date().before(expiry);
    }
}
