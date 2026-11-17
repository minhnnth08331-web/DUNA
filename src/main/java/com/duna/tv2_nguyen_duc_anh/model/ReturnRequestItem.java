package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang return_request_items trong CSDL DUNA_MensWear.
 */
public class ReturnRequestItem {
    private int returnItemId;
    private int returnId;
    private long orderItemId;
    private int quantity;
    private int exchangeVariantId;
    private String conditionNote;

    public ReturnRequestItem() {
    }

    public int getReturnItemId() {
        return returnItemId;
    }

    public void setReturnItemId(int returnItemId) {
        this.returnItemId = returnItemId;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getExchangeVariantId() {
        return exchangeVariantId;
    }

    public void setExchangeVariantId(int exchangeVariantId) {
        this.exchangeVariantId = exchangeVariantId;
    }

    public String getConditionNote() {
        return conditionNote;
    }

    public void setConditionNote(String conditionNote) {
        this.conditionNote = conditionNote;
    }
}
