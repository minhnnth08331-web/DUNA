package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang stocktake_items trong CSDL DUNA_MensWear.
 */
public class StocktakeItem {
    private int stocktakeItemId;
    private int stocktakeId;
    private int variantId;
    private int systemQuantity;
    private int countedQuantity;
    private String note;

    public StocktakeItem() {
    }

    public int getStocktakeItemId() {
        return stocktakeItemId;
    }

    public void setStocktakeItemId(int stocktakeItemId) {
        this.stocktakeItemId = stocktakeItemId;
    }

    public int getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(int stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getSystemQuantity() {
        return systemQuantity;
    }

    public void setSystemQuantity(int systemQuantity) {
        this.systemQuantity = systemQuantity;
    }

    public int getCountedQuantity() {
        return countedQuantity;
    }

    public void setCountedQuantity(int countedQuantity) {
        this.countedQuantity = countedQuantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
