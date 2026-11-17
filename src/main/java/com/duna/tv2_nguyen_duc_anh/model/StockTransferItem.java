package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang stock_transfer_items trong CSDL DUNA_MensWear.
 */
public class StockTransferItem {
    private int transferItemId;
    private int transferId;
    private int variantId;
    private int quantity;

    public StockTransferItem() {
    }

    public int getTransferItemId() {
        return transferItemId;
    }

    public void setTransferItemId(int transferItemId) {
        this.transferItemId = transferItemId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
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
}
