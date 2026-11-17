package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang review_replies trong CSDL DUNA_MensWear.
 */
public class ReviewReply {
    private int replyId;
    private int reviewId;
    private int userId;
    private String content;
    private String createdAt;

    public ReviewReply() {
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
