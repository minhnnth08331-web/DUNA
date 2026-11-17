package com.duna.common.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet co so cho tat ca Controller: ho tro JSON API va JSP forward an toan.
 */
public abstract class BaseServlet extends HttpServlet {

    protected boolean wantsJson(HttpServletRequest req) {
        return "json".equalsIgnoreCase(req.getParameter("format"))
                || "1".equals(req.getParameter("ajax"))
                || (req.getHeader("Accept") != null && req.getHeader("Accept").contains("application/json"));
    }

    protected void writeJson(HttpServletResponse resp, int statusCode, Object payload) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.write(JsonUtil.toJson(payload));
        }
    }

    protected void writeOkJson(HttpServletResponse resp, String ucCode, String message, Object data) throws IOException {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("ok", Boolean.TRUE);
        res.put("uc", ucCode);
        res.put("message", message);
        res.put("data", data);
        writeJson(resp, HttpServletResponse.SC_OK, res);
    }

    protected void writeErrJson(HttpServletResponse resp, int statusCode, String message) throws IOException {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("ok", Boolean.FALSE);
        res.put("message", message);
        writeJson(resp, statusCode, res);
    }

    protected void forwardView(HttpServletRequest req, HttpServletResponse resp, String jspPath)
            throws ServletException, IOException {
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }

    protected void setResultRows(HttpServletRequest req, String ucCode, String title, List<Map<String, Object>> rows) {
        req.setAttribute("activeUc", ucCode);
        req.setAttribute("resultTitle", title);
        req.setAttribute("resultRows", rows);
    }

    protected int getSessionUserId(HttpServletRequest req, int fallback) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return fallback;
        }
        Object v = session.getAttribute("userId");
        return (v instanceof Number) ? ((Number) v).intValue() : fallback;
    }

    protected String getSessionRole(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            return "";
        }
        return String.valueOf(session.getAttribute("role"));
    }
}
