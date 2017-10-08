package com.shixun.android.childtracking;

/**
 * Created by markchunyong on 2017/9/30.
 */

// objects used for sending location info of child to back end
public class ChildLocation {

    @com.google.gson.annotations.SerializedName("longtitude")
    private double mLongtitude;                                 //Children location longtitude
    @com.google.gson.annotations.SerializedName("id")
    private String mId;                                         //Children location data ID
    @com.google.gson.annotations.SerializedName("latitude")
    private double mLatitude;                                   //Children location latitude

    //Constructor
    public ChildLocation() {}

    //Set of accessor and mutator
    public double getLongtitude() {
        return mLongtitude;
    }

    public void setLongtitude(double longtitude) {
        mLongtitude = longtitude;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }
}
