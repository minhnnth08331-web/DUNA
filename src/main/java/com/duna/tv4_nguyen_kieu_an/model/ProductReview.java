package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang product_reviews trong CSDL DUNA_MensWear.
 */
public class ProductReview {
    private int reviewId;
    private int productId;
    private int userId;
    private long orderItemId;
    private int rating;
    private String title;
    private String content;
    private String fitFeedback;
    private String status;
    private String createdAt;

    public ProductReview() {
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFitFeedback() {
        return fitFeedback;
    }

    public void setFitFeedback(String fitFeedback) {
        this.fitFeedback = fitFeedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
