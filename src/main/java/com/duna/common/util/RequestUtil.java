package com.duna.common.util;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Tien ich doc & kiem tra tham so HTTP an toan (tranh crash 500 NumberFormatException).
 */
public final class RequestUtil {

    private RequestUtil() {
    }

    public static String getString(HttpServletRequest req, String name, String defaultVal) {
        String val = req.getParameter(name);
        if (val == null) {
            return defaultVal;
        }
        String trimmed = val.trim();
        return trimmed.isEmpty() ? defaultVal : trimmed;
    }

    public static int getInt(HttpServletRequest req, String name, int defaultVal) {
        String raw = req.getParameter(name);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static long getLong(HttpServletRequest req, String name, long defaultVal) {
        String raw = req.getParameter(name);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultVal;
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static double getDouble(HttpServletRequest req, String name, double defaultVal) {
        String raw = req.getParameter(name);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultVal;
        }
        try {
            return Double.parseDouble(raw.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static String normalizePhone(String phone) {
        if (phone == null) {
            return "";
        }
        String cleaned = phone.trim().replaceAll("[\\s.\\-()]", "");
        if (cleaned.startsWith("+84")) {
            String rest = cleaned.substring(3);
            return rest.matches("[0-9]+") ? "0" + rest : cleaned;
        }
        if (cleaned.startsWith("84") && cleaned.length() == 11 && cleaned.matches("[0-9]+")) {
            return "0" + cleaned.substring(2);
        }
        return cleaned;
    }

    /**
     * Kiem tra SDT Viet Nam hop le: bat dau bang so 0, gom dung 10 chu so (dau so 03, 05, 07, 08, 09).
     */
    public static boolean isValidVnPhone(String phone) {
        String norm = normalizePhone(phone);
        return norm.matches("^0(3|5|7|8|9)[0-9]{8}$");
    }

    /**
     * Kiem tra va chuan hoa chuoi ngay dinh dang yyyy-MM-dd (chong loi SQL Conversion failed / HTTP 500).
     */
    public static String sanitizeDate(String raw, String defaultVal) {
        if (raw == null) {
            return defaultVal;
        }
        String trimmed = raw.trim();
        if (!trimmed.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return defaultVal;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(trimmed);
            return trimmed;
        } catch (ParseException e) {
            return defaultVal;
        }
    }

    public static String getSafeDate(HttpServletRequest req, String name, String defaultVal) {
        return sanitizeDate(req.getParameter(name), defaultVal);
    }
}
