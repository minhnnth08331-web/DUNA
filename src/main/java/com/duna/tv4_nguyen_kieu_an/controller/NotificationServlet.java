package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M13 - Mau thong bao, Thong bao in-app & Email Campaign (UC247 - UC256).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications"})
public class NotificationServlet extends BaseServlet {
    private final NotificationService service = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC247");
        String keyword = RequestUtil.getString(req, "keyword", "");
        String channel = RequestUtil.getString(req, "channel", "");

        List<Map<String, Object>> rows;
        if ("UC251".equalsIgnoreCase(uc)) {
            rows = service.listUnread(RequestUtil.getInt(req, "userId", 0), keyword);
        } else if ("UC256".equalsIgnoreCase(uc)) {
            rows = service.listFailedMessages(keyword);
        } else {
            rows = service.listTemplates(channel, keyword);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Thông báo & Chiến dịch Email (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv4_nguyen_kieu_an/notifications.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("addTemplate".equals(action)) {
            affected = service.addTemplate(RequestUtil.getString(req, "templateCode", ""),
                    RequestUtil.getString(req, "channel", "EMAIL"),
                    RequestUtil.getString(req, "subject", ""),
                    RequestUtil.getString(req, "body", ""));
        } else if ("markRead".equals(action)) {
            affected = service.markRead(getSessionUserId(req, 1), RequestUtil.getLong(req, "notificationId", 0L));
        } else if ("purgeOld".equals(action)) {
            affected = service.purgeOldNotifications();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/notifications?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
