package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang goods_receipt_items trong CSDL DUNA_MensWear.
 */
public class GoodsReceiptItem {
    private int receiptItemId;
    private int receiptId;
    private int variantId;
    private int quantity;
    private double unitCost;

    public GoodsReceiptItem() {
    }

    public int getReceiptItemId() {
        return receiptItemId;
    }

    public void setReceiptItemId(int receiptItemId) {
        this.receiptItemId = receiptItemId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }
}
