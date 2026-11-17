package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang order_notes trong CSDL DUNA_MensWear.
 */
public class OrderNote {
    private int orderNoteId;
    private long orderId;
    private int userId;
    private String noteContent;
    private boolean isInternal;
    private String createdAt;

    public OrderNote() {
    }

    public int getOrderNoteId() {
        return orderNoteId;
    }

    public void setOrderNoteId(int orderNoteId) {
        this.orderNoteId = orderNoteId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setIsInternal(boolean isInternal) {
        this.isInternal = isInternal;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
