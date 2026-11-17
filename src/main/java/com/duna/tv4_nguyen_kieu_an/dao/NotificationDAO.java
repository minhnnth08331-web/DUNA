package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M13 - Mau thong bao, Thong bao in-app, Chien dich Email & Nhat ky (UC247 - UC256).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class NotificationDAO extends BaseDAO {

    // UC247: Xem danh sach mau thong bao theo kenh gui
    public List<Map<String, Object>> uc247_listTemplates(String channel, String codeKeyword) {
        String sql = "SELECT template_id, template_code, channel, subject, is_active "
                + "FROM notification_templates WHERE (? = '' OR channel = ?) AND template_code LIKE ? ORDER BY template_code";
        String ch = channel == null ? "" : channel.trim();
        return queryRows(sql, ch, ch, likePattern(codeKeyword));
    }

    // UC248: Them moi mau email xac nhan don hang
    public int uc248_addTemplate(String code, String channel, String subject, String body) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM notification_templates WHERE template_code = ?) "
                + "INSERT INTO notification_templates (template_code, channel, subject, body) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, code, code, channel, subject, body);
    }

    // UC249: Cap nhat noi dung mau thong bao
    public int uc249_updateTemplate(int templateId, String subject, String body) {
        return executeUpdate("UPDATE notification_templates SET subject = ?, body = ? WHERE template_id = ?",
                subject, body, templateId);
    }

    // UC250: Tao thong bao trong ung dung khi don hang doi trang thai
    public int uc250_notifyOrderStatus(long orderId, int templateId, String title) {
        String sql = "INSERT INTO notifications (user_id, template_id, title, content) "
                + "SELECT o.user_id, ?, ?, N'Đơn ' + o.order_code + N' đã chuyển sang trạng thái ' + o.status "
                + "FROM orders o WHERE o.order_id = ? AND o.user_id IS NOT NULL";
        return executeUpdate(sql, templateId, title, orderId);
    }

    // UC251: Xem danh sach thong bao chua doc cua nguoi dung
    public List<Map<String, Object>> uc251_listUnreadNotifications(int userId, String keyword) {
        String sql = "SELECT notification_id, title, content, is_read, created_at FROM notifications "
                + "WHERE (? <= 0 OR user_id = ?) AND is_read = 0 AND title LIKE ? ORDER BY created_at DESC";
        return queryRows(sql, userId, userId, likePattern(keyword));
    }

    // UC252: Danh dau thong bao da doc
    public int uc252_markNotificationRead(int userId, long notificationId) {
        return executeUpdate("UPDATE notifications SET is_read = 1 WHERE user_id = ? AND notification_id = ?",
                userId, notificationId);
    }

    // UC253: Tu dong xoa thong bao da doc cu hon 90 ngay
    public int uc253_purgeOldReadNotifications() {
        return executeUpdate("DELETE FROM notifications WHERE is_read = 1 AND created_at < DATEADD(DAY, -90, GETDATE())");
    }

    // UC254: Tao chien dich email marketing gui theo nhom khach hang
    public int uc254_createEmailCampaign(String name, int templateId, int targetGroupId, int createdBy) {
        String sql = "INSERT INTO email_campaigns (campaign_name, template_id, target_group_id, total_recipients, created_by) "
                + "VALUES (?, ?, ?, (SELECT COUNT(*) FROM customers WHERE group_id = ? AND email IS NOT NULL), ?)";
        return executeUpdate(sql, name, templateId, targetGroupId, targetGroupId, createdBy);
    }

    // UC255: Len lich gui chien dich email va cap nhat trang thai
    public int uc255_scheduleAndSendCampaign(int campaignId, String scheduledAt) {
        executeUpdate("UPDATE email_campaigns SET status = 'SCHEDULED', scheduled_at = ? WHERE campaign_id = ? AND status = 'DRAFT'",
                scheduledAt, campaignId);
        return executeUpdate("UPDATE email_campaigns SET status = 'SENT', sent_at = GETDATE() WHERE campaign_id = ? AND status = 'SCHEDULED' AND scheduled_at <= GETDATE()",
                campaignId);
    }

    // UC256: Xem nhat ky gui email va SMS that bai de gui lai
    public List<Map<String, Object>> uc256_listFailedMessages(String recipientKeyword) {
        String sql = "SELECT message_log_id, channel, recipient, subject, error_message, created_at "
                + "FROM message_logs WHERE status = 'FAILED' AND created_at >= DATEADD(DAY, -7, GETDATE()) "
                + "AND recipient LIKE ? ORDER BY created_at DESC";
        return queryRows(sql, likePattern(recipientKeyword));
    }
}
