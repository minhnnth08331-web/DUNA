package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang loyalty_transactions trong CSDL DUNA_MensWear.
 */
public class LoyaltyTransaction {
    private int loyaltyTxnId;
    private int accountId;
    private long orderId;
    private int points;
    private String txnType;
    private String description;
    private String expiresAt;
    private String createdAt;

    public LoyaltyTransaction() {
    }

    public int getLoyaltyTxnId() {
        return loyaltyTxnId;
    }

    public void setLoyaltyTxnId(int loyaltyTxnId) {
        this.loyaltyTxnId = loyaltyTxnId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
