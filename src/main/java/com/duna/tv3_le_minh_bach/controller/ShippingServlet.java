package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv3_le_minh_bach.service.ShippingGeoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M08 - Dia ly, Don vi van chuyen, Bang phi & Van don (UC171 - UC190).
 * Tac gia: Le Minh Bach - TH08442
 */
@WebServlet(name = "ShippingServlet", urlPatterns = {"/shipping"})
public class ShippingServlet extends BaseServlet {
    private final ShippingGeoService service = new ShippingGeoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC171");
        String keyword = RequestUtil.getString(req, "keyword", "");
        int provinceId = RequestUtil.getInt(req, "provinceId", 0);

        List<Map<String, Object>> rows;
        if ("UC174".equalsIgnoreCase(uc) || "UC175".equalsIgnoreCase(uc)) {
            rows = provinceId > 0 ? service.listWards(provinceId, keyword) : service.searchWards(keyword);
        } else if ("UC178".equalsIgnoreCase(uc)) {
            rows = service.listZones(keyword);
        } else if ("UC180".equalsIgnoreCase(uc)) {
            rows = service.listCarriers(keyword);
        } else if ("UC182".equalsIgnoreCase(uc)) {
            rows = service.listMethods(keyword);
        } else if ("UC184".equalsIgnoreCase(uc)) {
            rows = service.listFees();
        } else if ("UC189".equalsIgnoreCase(uc)) {
            rows = service.trackShipment(keyword);
        } else if ("UC190".equalsIgnoreCase(uc)) {
            rows = service.listDelayedOrFailed(keyword);
        } else {
            rows = service.listProvinces(keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("zones", service.listZones(""));
        req.setAttribute("carriers", service.listCarriers(""));
        setResultRows(req, uc, "Dia ly & Van chuyen (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv3_le_minh_bach/shipping.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("addProvince".equals(action)) {
            int zoneId = RequestUtil.getInt(req, "zoneId", 0);
            affected = service.addProvince(RequestUtil.getString(req, "provinceCode", ""),
                    RequestUtil.getString(req, "provinceName", ""), zoneId > 0 ? zoneId : null);
        } else if ("addZone".equals(action)) {
            affected = service.addZone(RequestUtil.getString(req, "zoneName", ""),
                    RequestUtil.getString(req, "description", ""));
        } else if ("addCarrier".equals(action)) {
            affected = service.addCarrier(RequestUtil.getString(req, "carrierCode", ""),
                    RequestUtil.getString(req, "carrierName", ""),
                    RequestUtil.getString(req, "apiEndpoint", ""),
                    RequestUtil.getString(req, "phone", ""));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/shipping?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
