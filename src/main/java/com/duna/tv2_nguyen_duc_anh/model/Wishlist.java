package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang wishlists trong CSDL DUNA_MensWear.
 */
public class Wishlist {
    private int wishlistId;
    private int userId;
    private int productId;
    private String createdAt;

    public Wishlist() {
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
