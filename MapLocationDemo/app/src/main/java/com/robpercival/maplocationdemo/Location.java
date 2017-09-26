package com.robpercival.maplocationdemo;

/**
 * Created by shixunliu on 25/9/17.
 */

public class Location {

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("longtitude")
    private double mLongtitude;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("latitude")
    private double mLatitude;

    /**
     * ToDoItem constructor
     */
    public Location() {

    }

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

    @Override
    public boolean equals(Object o) {
        return o instanceof Location && ((Location) o).mId == mId;
    }
}