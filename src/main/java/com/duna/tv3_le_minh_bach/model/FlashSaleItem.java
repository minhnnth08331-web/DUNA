package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang flash_sale_items trong CSDL DUNA_MensWear.
 */
public class FlashSaleItem {
    private int flashItemId;
    private int flashSaleId;
    private int variantId;
    private double salePrice;
    private int quantityLimit;
    private int soldQuantity;

    public FlashSaleItem() {
    }

    public int getFlashItemId() {
        return flashItemId;
    }

    public void setFlashItemId(int flashItemId) {
        this.flashItemId = flashItemId;
    }

    public int getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(int flashSaleId) {
        this.flashSaleId = flashSaleId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getQuantityLimit() {
        return quantityLimit;
    }

    public void setQuantityLimit(int quantityLimit) {
        this.quantityLimit = quantityLimit;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
