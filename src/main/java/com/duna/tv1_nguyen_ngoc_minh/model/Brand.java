package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang brands trong CSDL DUNA_MensWear.
 */
public class Brand {
    private int brandId;
    private String brandName;
    private String logoUrl;
    private String description;

    public Brand() {
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
