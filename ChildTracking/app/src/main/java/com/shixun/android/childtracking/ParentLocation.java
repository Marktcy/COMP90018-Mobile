package com.shixun.android.childtracking;

/**
 * Created by markchunyong on 2017/9/30.
 */

public class ParentLocation {

    @com.google.gson.annotations.SerializedName("longtitude")
    private double longtitude;                                  //Parent set tracking location longtitude
    @com.google.gson.annotations.SerializedName("latitude")
    private double lagitude;                                    //Parent set tracking location latitude
    @com.google.gson.annotations.SerializedName("radius")
    private int radius;                                         //Parent set tracking location radius
    @com.google.gson.annotations.SerializedName("id")
    private String mId;                                         //Parent set tracking location data id

    //Constructor of parent tracked location data
    public ParentLocation(double longtitude, double lagitude, int radius) {
        this.longtitude = longtitude;
        this.lagitude = lagitude;
        this.radius = radius;
    }

    //Set of accessor and mutator
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
}
