package com.pkvv.fmb_tracking.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class DriverCurrentLocation {
    private String uid;
    private GeoPoint geo_point;
    private @ServerTimestamp Date timestamp;

    public DriverCurrentLocation(String uid, GeoPoint geo_point, Date timestamp) {
        this.uid = uid;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }
    public DriverCurrentLocation(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DriverCurrentLocation{" +
                "uid='" + uid + '\'' +
                ", geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                '}';
    }
}
