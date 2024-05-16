package com.jamesou.dailycost.bean;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Bill {
    private int billId;
    private int userId;
    private int categoryId;
    private float billAmount;
    private Date billTime;
    private String billRemark;
    private Category category;

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(float billAmount) {
        this.billAmount = billAmount;
    }

    public Date getBillTime() {
        return billTime;
    }

    public void setBillTime(Date billTime) {
        this.billTime = billTime;
    }

    public String getBillRemark() {
        return billRemark;
    }

    public void setBillRemark(String billRemark) {
        this.billRemark = billRemark;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", billAmount=" + billAmount +
                ", billTime=" + billTime +
                ", billRemark='" + billRemark + '\'' +
                ", category=" + category +
                '}';
    }
}
