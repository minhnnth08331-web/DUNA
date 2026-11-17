package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang purchase_order_items trong CSDL DUNA_MensWear.
 */
public class PurchaseOrderItem {
    private int poItemId;
    private int poId;
    private int variantId;
    private int quantity;
    private double unitCost;
    private int receivedQuantity;

    public PurchaseOrderItem() {
    }

    public int getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(int poItemId) {
        this.poItemId = poItemId;
    }

    public int getPoId() {
        return poId;
    }

    public void setPoId(int poId) {
        this.poId = poId;
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

    public int getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }
}
