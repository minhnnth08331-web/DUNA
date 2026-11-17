package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.tv4_nguyen_kieu_an.dao.MarketingDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M12 - Banner, Blog phoi do & Ban tin (UC237 - UC246).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class MarketingService {
    private final MarketingDAO marketingDAO = new MarketingDAO();

    public List<Map<String, Object>> listBanners(String kw) {
        return marketingDAO.uc237_listBanners(kw);
    }

    public int addBanner(String title, String img, String link, String pos, int order, String start, String end) {
        return marketingDAO.uc238_addBanner(title, img, link, pos, order, start, end);
    }

    public int updateBanner(int id, int order, String endDate) {
        return marketingDAO.uc239_updateBanner(id, order, endDate);
    }

    public int hideExpiredBanners() {
        return marketingDAO.uc240_hideExpiredBanners();
    }

    public List<Map<String, Object>> listBlogPosts(String kw) {
        return marketingDAO.uc241_listPublishedBlogPosts(kw);
    }

    public int createDraftPost(Integer catId, String title, String slug, String summary, String content, String thumb, int authorId) {
        return marketingDAO.uc242_createDraftPost(catId, title, slug, summary, content, thumb, authorId);
    }

    public int publishPost(int postId) {
        return marketingDAO.uc243_publishPost(postId);
    }

    public Map<String, Object> viewPost(String slug) {
        return marketingDAO.uc244_viewPostBySlug(slug);
    }

    public int addBlogCategory(String name, String slug) {
        return marketingDAO.uc245_addBlogCategory(name, slug);
    }

    public int subscribeNewsletter(String email) {
        return marketingDAO.uc246_subscribeNewsletter(email);
    }
}
