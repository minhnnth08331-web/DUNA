package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang voucher_usages trong CSDL DUNA_MensWear.
 */
public class VoucherUsage {
    private int usageId;
    private int voucherId;
    private int userId;
    private long orderId;
    private double discountAmount;
    private String usedAt;

    public VoucherUsage() {
    }

    public int getUsageId() {
        return usageId;
    }

    public void setUsageId(int usageId) {
        this.usageId = usageId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(String usedAt) {
        this.usedAt = usedAt;
    }
}
