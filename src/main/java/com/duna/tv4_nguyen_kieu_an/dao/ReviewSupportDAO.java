package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M11 - Danh gia san pham, Ticket ho tro & FAQ (UC221 - UC236).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class ReviewSupportDAO extends BaseDAO {

    // UC221: Xem danh gia da duyet cua san pham kem anh khach hang
    public List<Map<String, Object>> uc221_listApprovedReviews(int productId) {
        String sql = "SELECT r.review_id, p.product_name, u.full_name, r.rating, r.title, r.content, "
                + "r.fit_feedback, r.created_at, rm.media_url "
                + "FROM product_reviews r JOIN products p ON r.product_id = p.product_id "
                + "JOIN users u ON r.user_id = u.user_id "
                + "LEFT JOIN review_media rm ON r.review_id = rm.review_id "
                + "WHERE (? <= 0 OR r.product_id = ?) AND r.status = 'APPROVED' ORDER BY r.created_at DESC";
        return queryRows(sql, productId, productId);
    }

    // UC222: Gui danh gia san pham sau khi mua hang thanh cong
    public long uc222_submitReview(int productId, int userId, Long orderItemId, int rating,
                                   String title, String content, String fitFeedback) {
        String sql = "INSERT INTO product_reviews (product_id, user_id, order_item_id, rating, title, content, fit_feedback) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeInsertGetId(sql, productId, userId, orderItemId, rating, title, content, fitFeedback);
    }

    // UC223: Tai anh thuc te cua khach len danh gia
    public int uc223_addReviewMedia(int reviewId, String mediaUrl) {
        return executeUpdate("INSERT INTO review_media (review_id, media_url, media_type) VALUES (?, ?, 'IMAGE')",
                reviewId, mediaUrl);
    }

    // UC224: Xem danh sach danh gia cho kiem duyet
    public List<Map<String, Object>> uc224_listPendingReviews(String keyword) {
        String sql = "SELECT r.review_id, p.product_name, u.full_name, r.rating, r.content, r.status, r.created_at "
                + "FROM product_reviews r JOIN products p ON r.product_id = p.product_id "
                + "JOIN users u ON r.user_id = u.user_id WHERE r.status = 'PENDING' AND p.product_name LIKE ? ORDER BY r.created_at ASC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC225-UC226: Duyet hoac tu choi danh gia
    public int uc225_226_moderateReview(int reviewId, String newStatus) {
        return executeUpdate("UPDATE product_reviews SET status = ? WHERE review_id = ? AND status = 'PENDING'",
                newStatus, reviewId);
    }

    // UC227: Shop phan hoi danh gia cua khach hang
    public int uc227_replyReview(int reviewId, int userId, String content) {
        return executeUpdate("INSERT INTO review_replies (review_id, user_id, content) VALUES (?, ?, ?)",
                reviewId, userId, content);
    }

    // UC228: Thong ke diem danh gia trung binh va phan hoi ve size cua san pham
    public List<Map<String, Object>> uc228_getReviewSummaryByProduct() {
        String sql = "SELECT r.product_id, p.product_name, COUNT(*) AS total_reviews, "
                + "AVG(CAST(r.rating AS DECIMAL(3,2))) AS avg_rating, "
                + "SUM(CASE WHEN r.fit_feedback = 'TOO_SMALL' THEN 1 ELSE 0 END) AS too_small, "
                + "SUM(CASE WHEN r.fit_feedback = 'TRUE_TO_SIZE' THEN 1 ELSE 0 END) AS true_to_size, "
                + "SUM(CASE WHEN r.fit_feedback = 'TOO_LARGE' THEN 1 ELSE 0 END) AS too_large "
                + "FROM product_reviews r JOIN products p ON r.product_id = p.product_id "
                + "WHERE r.status = 'APPROVED' GROUP BY r.product_id, p.product_name";
        return queryRows(sql);
    }

    // UC229: Khach hang tao yeu cau ho tro gan voi don hang
    public long uc229_createSupportTicket(String ticketCode, Integer customerId, Long orderId,
                                          Integer categoryId, String subject, String priority,
                                          int senderUserId, String firstMessage) {
        long ticketId = executeInsertGetId(
                "INSERT INTO support_tickets (ticket_code, customer_id, order_id, ticket_category_id, subject, priority) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                ticketCode, customerId, orderId, categoryId, subject, priority);
        if (ticketId > 0) {
            executeUpdate("INSERT INTO support_ticket_messages (ticket_id, sender_user_id, message_content, is_staff) VALUES (?, ?, ?, 0)",
                    ticketId, senderUserId, firstMessage);
        }
        return ticketId;
    }

    // UC230: Xem danh sach yeu cau ho tro theo trang thai va muc uu tien
    public List<Map<String, Object>> uc230_listTickets(String codeKeyword) {
        String sql = "SELECT t.ticket_id, t.ticket_code, c.full_name, tc.category_name, t.subject, "
                + "t.priority, t.status, u.full_name AS assigned_name, t.created_at "
                + "FROM support_tickets t LEFT JOIN customers c ON t.customer_id = c.customer_id "
                + "LEFT JOIN ticket_categories tc ON t.ticket_category_id = tc.ticket_category_id "
                + "LEFT JOIN users u ON t.assigned_to = u.user_id "
                + "WHERE t.status IN ('OPEN', 'IN_PROGRESS') AND t.ticket_code LIKE ? "
                + "ORDER BY t.priority DESC, t.created_at ASC";
        return queryRows(sql, likePattern(codeKeyword));
    }

    // UC231: Phan cong nhan vien cham soc xu ly yeu cau ho tro
    public int uc231_assignTicket(int ticketId, int staffUserId) {
        return executeUpdate("UPDATE support_tickets SET assigned_to = ?, status = 'IN_PROGRESS', updated_at = GETDATE() WHERE ticket_id = ? AND status = 'OPEN'",
                staffUserId, ticketId);
    }

    // UC232: Gui tin nhan phan hoi trong yeu cau ho tro
    public int uc232_addTicketMessage(int ticketId, int senderUserId, String content, boolean isStaff) {
        return executeUpdate("INSERT INTO support_ticket_messages (ticket_id, sender_user_id, message_content, is_staff) VALUES (?, ?, ?, ?)",
                ticketId, senderUserId, content, isStaff ? 1 : 0);
    }

    // UC233: Xem toan bo hoi thoai cua mot yeu cau ho tro
    public List<Map<String, Object>> uc233_listTicketMessages(int ticketId, String keyword) {
        String sql = "SELECT m.message_id, u.full_name, m.message_content, m.is_staff, m.attachment_url, m.created_at "
                + "FROM support_ticket_messages m LEFT JOIN users u ON m.sender_user_id = u.user_id "
                + "WHERE m.ticket_id = ? AND m.message_content LIKE ? ORDER BY m.created_at ASC";
        return queryRows(sql, ticketId, likePattern(keyword));
    }

    // UC234: Dong yeu cau ho tro sau khi giai quyet xong
    public int uc234_closeTicket(int ticketId) {
        return executeUpdate("UPDATE support_tickets SET status = 'CLOSED', updated_at = GETDATE() WHERE ticket_id = ? AND status IN ('IN_PROGRESS', 'RESOLVED')",
                ticketId);
    }

    // UC235: Xem danh sach cau hoi thuong gap theo nhom
    public List<Map<String, Object>> uc235_listFaqs(String keyword) {
        String sql = "SELECT faq_id, faq_group, question, answer, display_order FROM faqs "
                + "WHERE is_active = 1 AND question LIKE ? ORDER BY faq_group, display_order";
        return queryRows(sql, likePattern(keyword));
    }

    // UC236: Them moi cau hoi thuong gap
    public int uc236_addFaq(String group, String question, String answer, int displayOrder) {
        return executeUpdate("INSERT INTO faqs (faq_group, question, answer, display_order) VALUES (?, ?, ?, ?)",
                group, question, answer, displayOrder);
    }
}
