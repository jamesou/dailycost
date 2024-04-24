package com.jamesou.dailycost.db;

/**
 * @Description : cost category
 */

public class CategoryBean {
    /**
     * 类型名称
     */
    int id;
    String categoryName;
    // 未被选中的图片ID
    int imageId;
    // 被选中的图片ID
    int sImageId;
    // 收入-1 ， 支出-0
    int kind;

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public CategoryBean(int id, String categoryName, int imageId, int sImageId , int kind) {
        this.id = id;
        this.categoryName = categoryName;
        this.imageId = imageId;
        this.sImageId = sImageId;
        this.kind = kind;
    }

    public CategoryBean() {
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CategoryBean,"+ id +
                "," + categoryName +
                "," + imageId +
                "," + sImageId +
                "," + kind;
    }
}
