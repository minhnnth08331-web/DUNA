package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang expense_categories trong CSDL DUNA_MensWear.
 */
public class ExpenseCategory {
    private int expenseCategoryId;
    private String categoryName;

    public ExpenseCategory() {
    }

    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
