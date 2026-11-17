package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.uc.Tv1UcDispatcher;
import com.duna.common.uc.Tv2UcDispatcher;
import com.duna.common.uc.Tv3UcDispatcher;
import com.duna.common.uc.Tv4UcDispatcher;
import com.duna.common.uc.UcDispatchSupport;
import com.duna.common.uc.UcRegistry.UcMeta;
import com.duna.common.uc.UcRegistry;
import com.duna.common.util.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller dieu phoi ban do 300 Use Case & thuc thi truc tiep qua /uc-run.
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "UcWorkbenchServlet", urlPatterns = {"/uc-hub", "/uc-run"})
public class UcWorkbenchServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        if ("/uc-run".equals(path)) {
            executeUc(req, resp);
            return;
        }
        prepareHubAttributes(req);
        forwardView(req, resp, "/WEB-INF/views/common/uc-hub.jsp");
    }

    private void prepareHubAttributes(HttpServletRequest req) {
        String mod = UcDispatchSupport.str(req, "module", "");
        String owner = UcDispatchSupport.str(req, "owner", "");
        String kw = UcDispatchSupport.str(req, "keyword", "");
        List<UcMeta> filtered = UcRegistry.filter(mod, owner, kw);
        req.setAttribute("ucList", filtered);
        req.setAttribute("selectedModule", mod.toUpperCase());
        req.setAttribute("selectedOwner", owner.toUpperCase());
        req.setAttribute("keyword", kw);
        req.setAttribute("totalUcCount", UcRegistry.findAll().size());
    }

    private void executeUc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ucCode = UcDispatchSupport.str(req, "uc", "").toUpperCase();
        UcMeta meta = UcRegistry.findByCode(ucCode);
        if (meta == null) {
            if (wantsJson(req)) {
                Map<String, Object> err = new LinkedHashMap<>();
                err.put("ok", false);
                err.put("message", "Không tìm thấy Use Case: " + ucCode);
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, err);
                return;
            }
            req.setAttribute("flashError", "Không tìm thấy mã Use Case: " + ucCode);
            prepareHubAttributes(req);
            forwardView(req, resp, "/WEB-INF/views/common/uc-hub.jsp");
            return;
        }

        List<Map<String, Object>> rows;
        try {
            int num = Integer.parseInt(meta.getCode().substring(2));
            if (num <= 86) {
                rows = Tv1UcDispatcher.execute(meta.getCode(), req);
            } else if (num <= 151) {
                rows = Tv2UcDispatcher.execute(meta.getCode(), req);
            } else if (num <= 218) {
                rows = Tv3UcDispatcher.execute(meta.getCode(), req);
            } else {
                rows = Tv4UcDispatcher.execute(meta.getCode(), req);
            }
        } catch (Exception ex) {
            rows = UcDispatchSupport.wrapScalar(meta.getCode(), "ERROR", "Lỗi thực thi: " + ex.getMessage());
            req.setAttribute("flashError", "Lỗi thực thi " + meta.getCode() + ": " + ex.getMessage());
        }

        if (wantsJson(req)) {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("ok", true);
            payload.put("uc", meta.getCode());
            payload.put("module", meta.getModule());
            payload.put("title", meta.getTitle());
            payload.put("count", rows == null ? 0 : rows.size());
            payload.put("rows", rows);
            writeJson(resp, HttpServletResponse.SC_OK, payload);
            return;
        }

        setResultRows(req, meta.getCode(), meta.getCode() + " — " + meta.getTitle(), rows);
        req.setAttribute("executedMeta", meta);
        if (req.getAttribute("flashError") == null && "WRITE".equalsIgnoreCase(meta.getMode()) && rows != null && !rows.isEmpty()) {
            Object note = rows.get(0).get("ghiChu");
            Object status = rows.get(0).get("status");
            if ("SKIPPED".equals(status)) {
                req.setAttribute("flashError", meta.getCode() + ": " + (note != null ? note : "Không có bản ghi nào bị ảnh hưởng"));
            } else {
                req.setAttribute("flashSuccess", meta.getCode() + " thành công: " + (note != null ? note : meta.getTitle()));
            }
        }

        boolean fromHub = "hub".equalsIgnoreCase(req.getParameter("from"));
        if (fromHub || "/users".equals(meta.getRoute())) {
            prepareHubAttributes(req);
            forwardView(req, resp, "/WEB-INF/views/common/uc-hub.jsp");
            return;
        }

        req.setAttribute("moduleUcs", UcRegistry.findByRoute(meta.getRoute()));
        req.setAttribute("moduleRoute", meta.getRoute());
        forwardView(req, resp, meta.getJspView());
    }
}
