package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang blog_categories trong CSDL DUNA_MensWear.
 */
public class BlogCategory {
    private int blogCategoryId;
    private String categoryName;
    private String slug;

    public BlogCategory() {
    }

    public int getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(int blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
