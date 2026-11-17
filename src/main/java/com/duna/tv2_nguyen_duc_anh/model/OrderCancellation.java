package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang order_cancellations trong CSDL DUNA_MensWear.
 */
public class OrderCancellation {
    private int cancellationId;
    private long orderId;
    private String reasonCode;
    private String reasonDetail;
    private int cancelledBy;
    private String cancelledAt;

    public OrderCancellation() {
    }

    public int getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(int cancellationId) {
        this.cancellationId = cancellationId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }

    public int getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(int cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
