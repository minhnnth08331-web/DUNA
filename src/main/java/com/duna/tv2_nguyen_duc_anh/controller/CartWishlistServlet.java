package com.duna.tv2_nguyen_duc_anh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv2_nguyen_duc_anh.service.CartWishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M05 - Gio hang & San pham yeu thich (UC114 - UC125).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "CartWishlistServlet", urlPatterns = {"/cart"})
public class CartWishlistServlet extends BaseServlet {
    private final CartWishlistService service = new CartWishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC114");
        int userId = RequestUtil.getInt(req, "userId", getSessionUserId(req, 1));
        String keyword = RequestUtil.getString(req, "keyword", "");
        List<Map<String, Object>> rows;
        if ("UC123".equalsIgnoreCase(uc)) {
            rows = service.listWishlist(userId, keyword);
        } else if ("UC122".equalsIgnoreCase(uc)) {
            rows = service.listAbandonedCarts(RequestUtil.getDouble(req, "minValue", 300000));
        } else {
            rows = service.getCartItems(userId);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        setResultRows(req, uc, "Giỏ hàng & Yêu thích (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv2_nguyen_duc_anh/cart.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int userId = getSessionUserId(req, 1);
        int affected = 0;
        if ("addCart".equals(action)) {
            affected = service.addToCart(RequestUtil.getInt(req, "cartId", 1),
                    RequestUtil.getInt(req, "variantId", 1), RequestUtil.getInt(req, "quantity", 1));
        } else if ("addWishlist".equals(action)) {
            affected = service.addToWishlist(userId, RequestUtil.getInt(req, "productId", 1));
        } else if ("removeWishlist".equals(action)) {
            affected = service.removeFromWishlist(userId, RequestUtil.getInt(req, "productId", 1));
        } else if ("markAbandoned".equals(action)) {
            affected = service.markAbandonedCarts();
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
