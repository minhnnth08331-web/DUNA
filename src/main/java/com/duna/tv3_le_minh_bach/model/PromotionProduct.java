package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang promotion_products trong CSDL DUNA_MensWear.
 */
public class PromotionProduct {
    private int promotionProductId;
    private int promotionId;
    private int productId;

    public PromotionProduct() {
    }

    public int getPromotionProductId() {
        return promotionProductId;
    }

    public void setPromotionProductId(int promotionProductId) {
        this.promotionProductId = promotionProductId;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
