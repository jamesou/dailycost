package com.jamesou.dailycost.db;

public class FloatWrapper {
    public float getTotalMoeny() {
        return totalMoeny;
    }

    public void setTotalMoeny(float totalMoeny) {
        this.totalMoeny = totalMoeny;
    }

    private float totalMoeny;

    public FloatWrapper(int totalMoeny) {
        this.totalMoeny = totalMoeny;
    }
}
