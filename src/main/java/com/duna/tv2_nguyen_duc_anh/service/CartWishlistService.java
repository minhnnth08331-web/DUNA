package com.duna.tv2_nguyen_duc_anh.service;

import com.duna.tv2_nguyen_duc_anh.dao.CartWishlistDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M05 - Gio hang & Yeu thich (UC114 - UC125).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class CartWishlistService {
    private final CartWishlistDAO cartWishlistDAO = new CartWishlistDAO();

    public List<Map<String, Object>> getCartItems(int userId) {
        return cartWishlistDAO.uc114_getCartItems(userId);
    }

    public int addToCart(int cartId, int variantId, int qty) {
        return cartWishlistDAO.uc115_addToCart(cartId, variantId, qty);
    }

    public int updateCartItem(int cartId, int variantId, int qty) {
        return cartWishlistDAO.uc116_updateCartItemQuantity(cartId, variantId, qty);
    }

    public int removeCartItem(int cartId, int variantId) {
        return cartWishlistDAO.uc117_removeCartItem(cartId, variantId);
    }

    public int applyVoucher(int cartId, String voucherCode) {
        return cartWishlistDAO.uc118_applyVoucherToCart(cartId, voucherCode);
    }

    public Map<String, Object> getSummary(int cartId) {
        return cartWishlistDAO.uc119_getCartSummary(cartId);
    }

    public int mergeGuestCart(int userCartId, String sessionToken) {
        return cartWishlistDAO.uc120_mergeGuestCart(userCartId, sessionToken);
    }

    public int markAbandonedCarts() {
        return cartWishlistDAO.uc121_markAbandonedCarts();
    }

    public List<Map<String, Object>> listAbandonedCarts(double minVal) {
        return cartWishlistDAO.uc122_listAbandonedCarts(minVal);
    }

    public List<Map<String, Object>> listWishlist(int userId, String kw) {
        return cartWishlistDAO.uc123_listWishlist(userId, kw);
    }

    public int addToWishlist(int userId, int productId) {
        return cartWishlistDAO.uc124_addToWishlist(userId, productId);
    }

    public int removeFromWishlist(int userId, int productId) {
        return cartWishlistDAO.uc125_removeFromWishlist(userId, productId);
    }
}
