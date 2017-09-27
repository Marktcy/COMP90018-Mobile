package com.shixun.android.childtracking;

/**
 * Created by shixunliu on 26/9/17.
 */

public class ParentLocation {

    @com.google.gson.annotations.SerializedName("longtitude")
    private double longtitude;
    @com.google.gson.annotations.SerializedName("latitude")
    private double lagitude;
    @com.google.gson.annotations.SerializedName("radius")
    private int radius;
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    public ParentLocation(double longtitude, double lagitude, int radius) {
        this.longtitude = longtitude;
        this.lagitude = lagitude;
        this.radius = radius;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLagitude() {
        return lagitude;
    }

    public void setLagitude(double lagitude) {
        this.lagitude = lagitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ParentLocation && ((ParentLocation) o).mId == mId;
    }
}
