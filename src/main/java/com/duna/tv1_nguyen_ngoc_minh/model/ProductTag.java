package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang product_tags trong CSDL DUNA_MensWear.
 */
public class ProductTag {
    private int tagId;
    private String tagName;
    private String slug;

    public ProductTag() {
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
