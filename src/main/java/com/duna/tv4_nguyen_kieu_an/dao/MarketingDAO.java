package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M12 - Banner, Blog phoi do & Ban tin Newsletter (UC237 - UC246).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class MarketingDAO extends BaseDAO {

    // UC237: Xem danh sach banner theo vi tri hien thi tren website
    public List<Map<String, Object>> uc237_listBanners(String keyword) {
        String sql = "SELECT banner_id, title, image_url, link_url, banner_position, display_order, start_date, end_date, is_active "
                + "FROM banners WHERE title LIKE ? ORDER BY banner_position, display_order";
        return queryRows(sql, likePattern(keyword));
    }

    // UC238: Them moi banner quang cao kem anh va lien ket
    public int uc238_addBanner(String title, String imageUrl, String linkUrl, String position,
                               int displayOrder, String startDate, String endDate) {
        return executeUpdate("INSERT INTO banners (title, image_url, link_url, banner_position, display_order, start_date, end_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)", title, imageUrl, linkUrl, position, displayOrder, startDate, endDate);
    }

    // UC239: Cap nhat thoi gian hien thi va thu tu banner
    public int uc239_updateBanner(int bannerId, int displayOrder, String endDate) {
        return executeUpdate("UPDATE banners SET display_order = ?, end_date = ? WHERE banner_id = ?",
                displayOrder, endDate, bannerId);
    }

    // UC240: Tu dong an banner het han hien thi
    public int uc240_hideExpiredBanners() {
        return executeUpdate("UPDATE banners SET is_active = 0 WHERE is_active = 1 AND end_date < GETDATE()");
    }

    // UC241: Xem danh sach bai viet phoi do theo chuyen muc
    public List<Map<String, Object>> uc241_listPublishedBlogPosts(String keyword) {
        String sql = "SELECT p.post_id, p.title, p.slug, c.category_name, p.status, p.published_at, p.view_count "
                + "FROM blog_posts p LEFT JOIN blog_categories c ON p.blog_category_id = c.blog_category_id "
                + "WHERE p.status = 'PUBLISHED' AND p.title LIKE ? ORDER BY p.published_at DESC";
        return queryRows(sql, likePattern(keyword));
    }

    // UC242: Soan bai viet moi o trang thai nhap
    public int uc242_createDraftPost(Integer categoryId, String title, String slug, String summary,
                                     String content, String thumbUrl, int authorId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM blog_posts WHERE slug = ?) "
                + "INSERT INTO blog_posts (blog_category_id, title, slug, summary, content, thumbnail_url, author_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, slug, categoryId, title, slug, summary, content, thumbUrl, authorId);
    }

    // UC243: Xuat ban bai viet len website
    public int uc243_publishPost(int postId) {
        return executeUpdate("UPDATE blog_posts SET status = 'PUBLISHED', published_at = GETDATE(), updated_at = GETDATE() "
                + "WHERE post_id = ? AND status = 'DRAFT'", postId);
    }

    // UC244: Xem bai viet theo duong dan va tang luot xem
    public Map<String, Object> uc244_viewPostBySlug(String slug) {
        executeUpdate("UPDATE blog_posts SET view_count = view_count + 1 WHERE slug = ?", slug);
        return queryFirstRow("SELECT post_id, title, summary, content, view_count, published_at FROM blog_posts WHERE slug = ? AND status = 'PUBLISHED'", slug);
    }

    // UC245: Them moi chuyen muc bai viet
    public int uc245_addBlogCategory(String name, String slug) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM blog_categories WHERE slug = ?) INSERT INTO blog_categories (category_name, slug) VALUES (?, ?)";
        return executeUpdate(sql, slug, name, slug);
    }

    // UC246: Dang ky nhan ban tin khuyen mai qua email
    public int uc246_subscribeNewsletter(String email) {
        executeUpdate("IF NOT EXISTS (SELECT 1 FROM newsletter_subscribers WHERE email = ?) INSERT INTO newsletter_subscribers (email) VALUES (?)",
                email, email);
        return executeUpdate("UPDATE newsletter_subscribers SET is_active = 1 WHERE email = ?", email);
    }
}
