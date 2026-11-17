package com.duna.tv2_nguyen_duc_anh.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M05 - Gio hang & San pham yeu thich (UC114 - UC125).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
public class CartWishlistDAO extends BaseDAO {

    // UC114: Xem gio hang hien tai kem hinh anh, mau sac va size
    public List<Map<String, Object>> uc114_getCartItems(int userId) {
        String sql = "SELECT ci.cart_item_id, ci.cart_id, ci.variant_id, p.product_name, c.color_name, s.size_code, "
                + "pv.sku, ci.quantity, ci.unit_price, (ci.quantity * ci.unit_price) AS line_total "
                + "FROM cart_items ci JOIN carts ct ON ci.cart_id = ct.cart_id "
                + "JOIN product_variants pv ON ci.variant_id = pv.variant_id "
                + "JOIN products p ON pv.product_id = p.product_id "
                + "JOIN colors c ON pv.color_id = c.color_id JOIN sizes s ON pv.size_id = s.size_id "
                + "WHERE ct.user_id = ? AND ct.status = 'ACTIVE'";
        return queryRows(sql, userId);
    }

    // UC115: Them san pham vao gio hang va cong don so luong
    public int uc115_addToCart(int cartId, int variantId, int qty) {
        int updated = executeUpdate("UPDATE cart_items SET quantity = quantity + ? WHERE cart_id = ? AND variant_id = ?",
                qty, cartId, variantId);
        if (updated == 0) {
            return executeUpdate(
                    "INSERT INTO cart_items (cart_id, variant_id, quantity, unit_price) "
                            + "SELECT ?, pv.variant_id, ?, ISNULL(pv.price, p.base_price) "
                            + "FROM product_variants pv JOIN products p ON pv.product_id = p.product_id WHERE pv.variant_id = ?",
                    cartId, qty, variantId);
        }
        return updated;
    }

    // UC116: Cap nhat so luong san pham trong gio co kiem tra ton kho
    public int uc116_updateCartItemQuantity(int cartId, int variantId, int qty) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND variant_id = ? "
                + "AND EXISTS (SELECT 1 FROM inventories WHERE variant_id = ? AND (stock_quantity - reserved_quantity) >= ?)";
        return executeUpdate(sql, qty, cartId, variantId, variantId, qty);
    }

    // UC117: Xoa mot san pham khoi gio hang
    public int uc117_removeCartItem(int cartId, int variantId) {
        return executeUpdate("DELETE FROM cart_items WHERE cart_id = ? AND variant_id = ?", cartId, variantId);
    }

    // UC118: Ap dung ma voucher vao gio hang
    public int uc118_applyVoucherToCart(int cartId, String voucherCode) {
        String sql = "UPDATE carts SET voucher_id = ("
                + "SELECT TOP 1 v.voucher_id FROM vouchers v WHERE v.voucher_code = ? AND v.is_active = 1 "
                + "AND GETDATE() BETWEEN v.start_date AND v.end_date AND (v.usage_limit IS NULL OR v.used_count < v.usage_limit)"
                + "), updated_at = GETDATE() WHERE cart_id = ?";
        return executeUpdate(sql, voucherCode, cartId);
    }

    // UC119: Tinh tong tien tam tinh va so luong san pham cua gio hang
    public Map<String, Object> uc119_getCartSummary(int cartId) {
        String sql = "SELECT ISNULL(SUM(quantity), 0) AS total_items, ISNULL(SUM(quantity * unit_price), 0) AS subtotal "
                + "FROM cart_items WHERE cart_id = ?";
        return queryFirstRow(sql, cartId);
    }

    // UC120: Hop nhat gio hang khach vang lai vao gio tai khoan
    public int uc120_mergeGuestCart(int userCartId, String sessionToken) {
        Map<String, Object> guest = queryFirstRow(
                "SELECT cart_id FROM carts WHERE session_token = ? AND status = 'ACTIVE'", sessionToken);
        if (guest == null) {
            return 0;
        }
        int guestCartId = ((Number) guest.get("cart_id")).intValue();
        executeUpdate("UPDATE cart_items SET cart_id = ? WHERE cart_id = ? AND variant_id NOT IN (SELECT variant_id FROM cart_items WHERE cart_id = ?)",
                userCartId, guestCartId, userCartId);
        return executeUpdate("UPDATE carts SET status = 'CONVERTED', updated_at = GETDATE() WHERE cart_id = ?", guestCartId);
    }

    // UC121: Danh dau gio hang bi bo quen sau 24 gio
    public int uc121_markAbandonedCarts() {
        return executeUpdate("UPDATE carts SET status = 'ABANDONED' WHERE status = 'ACTIVE' AND updated_at < DATEADD(HOUR, -24, GETDATE())");
    }

    // UC122: Lay danh sach gio hang bo quen de gui email nhac mua
    public List<Map<String, Object>> uc122_listAbandonedCarts(double minValue) {
        String sql = "SELECT u.email, u.full_name, ct.cart_id, SUM(ci.quantity * ci.unit_price) AS cart_value "
                + "FROM carts ct JOIN users u ON ct.user_id = u.user_id "
                + "JOIN cart_items ci ON ct.cart_id = ci.cart_id WHERE ct.status = 'ABANDONED' "
                + "GROUP BY u.email, u.full_name, ct.cart_id HAVING SUM(ci.quantity * ci.unit_price) > ?";
        return queryRows(sql, minValue);
    }

    // UC123: Xem danh sach san pham yeu thich cua khach hang
    public List<Map<String, Object>> uc123_listWishlist(int userId, String keyword) {
        String sql = "SELECT w.wishlist_id, p.product_id, p.product_code, p.product_name, p.base_price, w.created_at "
                + "FROM wishlists w JOIN products p ON w.product_id = p.product_id "
                + "WHERE w.user_id = ? AND p.product_name LIKE ? ORDER BY w.created_at DESC";
        return queryRows(sql, userId, likePattern(keyword));
    }

    // UC124: Them san pham vao danh sach yeu thich
    public int uc124_addToWishlist(int userId, int productId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM wishlists WHERE user_id = ? AND product_id = ?) "
                + "INSERT INTO wishlists (user_id, product_id) VALUES (?, ?)";
        return executeUpdate(sql, userId, productId, userId, productId);
    }

    // UC125: Bo san pham khoi danh sach yeu thich
    public int uc125_removeFromWishlist(int userId, int productId) {
        return executeUpdate("DELETE FROM wishlists WHERE user_id = ? AND product_id = ?", userId, productId);
    }
}
