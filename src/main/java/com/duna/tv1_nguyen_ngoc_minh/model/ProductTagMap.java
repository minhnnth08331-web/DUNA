package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang product_tag_map trong CSDL DUNA_MensWear.
 */
public class ProductTagMap {
    private int productTagId;
    private int productId;
    private int tagId;

    public ProductTagMap() {
    }

    public int getProductTagId() {
        return productTagId;
    }

    public void setProductTagId(int productTagId) {
        this.productTagId = productTagId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
