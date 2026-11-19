package com.duna.common.uc;

import com.duna.common.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tien ich ho tro thuc thi Use Case & chuan hoa ket qua cho Giao dien / JSON.
 */
public final class UcDispatchSupport {

    private UcDispatchSupport() {
    }

    public static String str(HttpServletRequest req, String name, String def) {
        String v = req.getParameter(name);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }

    public static String safeDate(HttpServletRequest req, String name, String def) {
        return RequestUtil.getSafeDate(req, name, def);
    }

    public static int numInt(HttpServletRequest req, String name, int def) {
        String v = req.getParameter(name);
        if (v == null || v.trim().isEmpty()) return def;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static long numLong(HttpServletRequest req, String name, long def) {
        String v = req.getParameter(name);
        if (v == null || v.trim().isEmpty()) return def;
        try {
            return Long.parseLong(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static double numDouble(HttpServletRequest req, String name, double def) {
        String v = req.getParameter(name);
        if (v == null || v.trim().isEmpty()) return def;
        try {
            return Double.parseDouble(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static boolean bool(HttpServletRequest req, String name, boolean def) {
        String v = req.getParameter(name);
        if (v == null || v.trim().isEmpty()) return def;
        return "true".equalsIgnoreCase(v.trim()) || "1".equals(v.trim()) || "on".equalsIgnoreCase(v.trim());
    }

    public static int sessionUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("userId") instanceof Number) {
            return ((Number) s.getAttribute("userId")).intValue();
        }
        return 1;
    }

    public static List<Map<String, Object>> wrapMap(Map<String, Object> m, String ucCode, String emptyNote) {
        if (m == null || m.isEmpty()) {
            return wrapScalar(ucCode, emptyNote, "Không có dữ liệu phù hợp");
        }
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(m);
        return list;
    }

    public static List<Map<String, Object>> wrapScalar(String ucCode, Object resultValue, String note) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("maUC", ucCode);
        row.put("ketQua", resultValue);
        row.put("trangThai", note);
        return Collections.singletonList(row);
    }

    public static List<Map<String, Object>> wrapAffected(String ucCode, long affected, String okMsg, String skipMsg) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("maUC", ucCode);
        row.put("banGhiAnhHuong", affected);
        row.put("status", affected > 0 ? "COMPLETED" : "SKIPPED");
        row.put("ghiChu", affected > 0 ? okMsg : skipMsg);
        return Collections.singletonList(row);
    }
}
