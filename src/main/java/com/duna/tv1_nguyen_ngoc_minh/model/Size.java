package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang sizes trong CSDL DUNA_MensWear.
 */
public class Size {
    private int sizeId;
    private String sizeCode;
    private String categoryType;
    private int displayOrder;

    public Size() {
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
