package com.duna.common.util;

import javax.servlet.http.HttpServletRequest;

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
        String digits = phone.replaceAll("[^0-9+]", "");
        if (digits.startsWith("+84")) {
            return "0" + digits.substring(3);
        }
        if (digits.startsWith("84") && digits.length() >= 11) {
            return "0" + digits.substring(2);
        }
        return digits;
    }
}
