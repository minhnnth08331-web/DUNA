package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.tv4_nguyen_kieu_an.dao.LoyaltyRewardDAO;
import com.duna.tv4_nguyen_kieu_an.dao.ReviewSupportDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M10 (Qua) & M11 (Danh gia & Ticket ho tro) (UC219 - UC236).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class RewardSupportService {
    private final LoyaltyRewardDAO rewardDAO = new LoyaltyRewardDAO();
    private final ReviewSupportDAO reviewSupportDAO = new ReviewSupportDAO();

    public int addReward(String name, int pts, Integer voucherId, int stock) {
        return rewardDAO.uc219_addReward(name, pts, voucherId, stock);
    }

    public int redeemReward(int accountId, int rewardId, int cost) {
        return rewardDAO.uc220_redeemReward(accountId, rewardId, cost);
    }

    public List<Map<String, Object>> listApprovedReviews(int productId) {
        return reviewSupportDAO.uc221_listApprovedReviews(productId);
    }

    public long submitReview(int productId, int userId, Long orderItemId, int rating, String title, String content, String fit) {
        return reviewSupportDAO.uc222_submitReview(productId, userId, orderItemId, rating, title, content, fit);
    }

    public int addReviewMedia(int reviewId, String url) {
        return reviewSupportDAO.uc223_addReviewMedia(reviewId, url);
    }

    public List<Map<String, Object>> listPendingReviews(String kw) {
        return reviewSupportDAO.uc224_listPendingReviews(kw);
    }

    public int approveReview(int reviewId) {
        return reviewSupportDAO.uc225_226_moderateReview(reviewId, "APPROVED");
    }

    public int rejectReview(int reviewId) {
        return reviewSupportDAO.uc225_226_moderateReview(reviewId, "REJECTED");
    }

    public int replyReview(int reviewId, int userId, String content) {
        return reviewSupportDAO.uc227_replyReview(reviewId, userId, content);
    }

    public List<Map<String, Object>> getReviewSummary() {
        return reviewSupportDAO.uc228_getReviewSummaryByProduct();
    }

    public long createTicket(String code, Integer custId, Long ordId, Integer catId, String subj, String prio, int uid, String msg) {
        return reviewSupportDAO.uc229_createSupportTicket(code, custId, ordId, catId, subj, prio, uid, msg);
    }

    public List<Map<String, Object>> listTickets(String kw) {
        return reviewSupportDAO.uc230_listTickets(kw);
    }

    public int assignTicket(int ticketId, int staffId) {
        return reviewSupportDAO.uc231_assignTicket(ticketId, staffId);
    }

    public int addTicketMessage(int ticketId, int uid, String msg, boolean isStaff) {
        return reviewSupportDAO.uc232_addTicketMessage(ticketId, uid, msg, isStaff);
    }

    public List<Map<String, Object>> listTicketMessages(int ticketId, String kw) {
        return reviewSupportDAO.uc233_listTicketMessages(ticketId, kw);
    }

    public int closeTicket(int ticketId) {
        return reviewSupportDAO.uc234_closeTicket(ticketId);
    }

    public List<Map<String, Object>> listFaqs(String kw) {
        return reviewSupportDAO.uc235_listFaqs(kw);
    }

    public int addFaq(String group, String q, String a, int order) {
        return reviewSupportDAO.uc236_addFaq(group, q, a, order);
    }
}
