package com.pkvv.fmb_tracking.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class DriverLocation {

    private Drivers drivers;
    private GeoPoint geo_point;
    private @ServerTimestamp Date timestamp;

    public DriverLocation(Drivers drivers, GeoPoint geo_point, Date timestamp) {
        this.drivers = drivers;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    public DriverLocation() {

    }

    public Drivers getDrivers() {
        return drivers;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
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
        return "UserLocation{" +
                "drivers=" + drivers +
                ", geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                '}';
    }

}
