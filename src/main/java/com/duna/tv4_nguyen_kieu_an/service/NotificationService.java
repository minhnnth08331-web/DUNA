package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.tv4_nguyen_kieu_an.dao.NotificationDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M13 - Mau thong bao, Thong bao & Chien dich Email (UC247 - UC256).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class NotificationService {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public List<Map<String, Object>> listTemplates(String channel, String kw) {
        return notificationDAO.uc247_listTemplates(channel, kw);
    }

    public int addTemplate(String code, String channel, String subject, String body) {
        return notificationDAO.uc248_addTemplate(code.toUpperCase(), channel, subject, body);
    }

    public int updateTemplate(int id, String subject, String body) {
        return notificationDAO.uc249_updateTemplate(id, subject, body);
    }

    public int notifyOrderStatus(long orderId, int templateId, String title) {
        return notificationDAO.uc250_notifyOrderStatus(orderId, templateId, title);
    }

    public List<Map<String, Object>> listUnread(int userId, String kw) {
        return notificationDAO.uc251_listUnreadNotifications(userId, kw);
    }

    public int markRead(int userId, long notificationId) {
        return notificationDAO.uc252_markNotificationRead(userId, notificationId);
    }

    public int purgeOldNotifications() {
        return notificationDAO.uc253_purgeOldReadNotifications();
    }

    public int createEmailCampaign(String name, int templateId, int targetGroupId, int createdBy) {
        return notificationDAO.uc254_createEmailCampaign(name, templateId, targetGroupId, createdBy);
    }

    public int scheduleAndSendCampaign(int campaignId, String scheduledAt) {
        return notificationDAO.uc255_scheduleAndSendCampaign(campaignId, scheduledAt);
    }

    public List<Map<String, Object>> listFailedMessages(String kw) {
        return notificationDAO.uc256_listFailedMessages(kw);
    }
}
