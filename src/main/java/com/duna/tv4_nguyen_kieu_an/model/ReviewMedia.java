package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang review_media trong CSDL DUNA_MensWear.
 */
public class ReviewMedia {
    private int reviewMediaId;
    private int reviewId;
    private String mediaUrl;
    private String mediaType;

    public ReviewMedia() {
    }

    public int getReviewMediaId() {
        return reviewMediaId;
    }

    public void setReviewMediaId(int reviewMediaId) {
        this.reviewMediaId = reviewMediaId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
