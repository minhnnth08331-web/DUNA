package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.service.SystemConfigService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M17 - Cau hinh he thong & Cong viec chay ngam (UC297 - UC300).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "SystemConfigServlet", urlPatterns = {"/system"})
public class SystemConfigServlet extends BaseServlet {
    private final SystemConfigService service = new SystemConfigService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC297");
        String keyword = RequestUtil.getString(req, "keyword", "");
        List<Map<String, Object>> rows = "UC299".equalsIgnoreCase(uc)
                ? service.listScheduledJobs(keyword)
                : service.listSettings(keyword);
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Cau hinh He thong & Job ngam (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/system.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("updateSetting".equals(action)) {
            affected = service.updateSetting(RequestUtil.getString(req, "settingKey", ""),
                    RequestUtil.getString(req, "settingValue", ""));
        } else if ("updateJob".equals(action)) {
            affected = service.updateScheduledJob(RequestUtil.getString(req, "jobName", ""),
                    "1".equals(req.getParameter("isEnabled")),
                    RequestUtil.getString(req, "cronExpression", "0 0 * * * *"));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/system?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
