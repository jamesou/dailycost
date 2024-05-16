package com.keepfool.bill.bean;

import org.springframework.stereotype.Component;

@Component
public class Category {
    private int categoryId;
    private String categoryName;
    private String categoryIcon;
    private int categoryState;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public int getCategoryState() {
        return categoryState;
    }

    public void setCategoryState(int categoryState) {
        this.categoryState = categoryState;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryIcon='" + categoryIcon + '\'' +
                ", categoryState=" + categoryState +
                '}';
    }
}
