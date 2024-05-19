package com.jamesou.dailycost.db;

public class ReceiptBean {
    String item_amount;

    public String getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(String item_amount) {
        this.item_amount = item_amount;
    }

    public String getItem_amount_unit() {
        return item_amount_unit;
    }

    public void setItem_amount_unit(String item_amount_unit) {
        this.item_amount_unit = item_amount_unit;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    String item_amount_unit;
    String item_name;
    String item_qty;

    @Override
    public String toString() {
        return "ReceiptBean{" +
                "item_amount='" + item_amount + '\'' +
                ", item_amount_unit='" + item_amount_unit + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_qty='" + item_qty + '\'' +
                '}';
    }
}
