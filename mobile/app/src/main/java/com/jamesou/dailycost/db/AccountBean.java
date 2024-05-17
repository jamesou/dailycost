package com.jamesou.dailycost.db;


public class AccountBean {
    int id;
    String categoryName;
    int sImageId;
    String comment;
    float money;
    String time;
    int year;
    int month;
    int day;
    // income:1 ， expense:0
    int kind;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public AccountBean(int id, String categoryName, int sImageId, String comment, float money, String time, int year, int month, int day, int kind) {
        this.id = id;
        this.categoryName = categoryName;
        this.sImageId = sImageId;
        this.comment = comment;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }

    public AccountBean() {
    }

    @Override
    public String toString() {
        return "AccountBean," + id +
                "," + categoryName +
                "," + sImageId +
                "," + comment +
                "," + money +
                "," + time +
                "," + year +
                "," + month +
                "," + day +
                "," + kind;
    }
}
