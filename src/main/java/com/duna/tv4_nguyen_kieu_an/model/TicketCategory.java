package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang ticket_categories trong CSDL DUNA_MensWear.
 */
public class TicketCategory {
    private int ticketCategoryId;
    private String categoryName;

    public TicketCategory() {
    }

    public int getTicketCategoryId() {
        return ticketCategoryId;
    }

    public void setTicketCategoryId(int ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
