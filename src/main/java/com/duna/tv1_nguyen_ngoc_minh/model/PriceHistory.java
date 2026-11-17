package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang price_histories trong CSDL DUNA_MensWear.
 */
public class PriceHistory {
    private int priceHistoryId;
    private int productId;
    private double oldPrice;
    private double newPrice;
    private int changedBy;
    private String reason;
    private String changedAt;

    public PriceHistory() {
    }

    public int getPriceHistoryId() {
        return priceHistoryId;
    }

    public void setPriceHistoryId(int priceHistoryId) {
        this.priceHistoryId = priceHistoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(String changedAt) {
        this.changedAt = changedAt;
    }
}
