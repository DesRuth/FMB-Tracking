package com.pkvv.fmb_tracking.models;

public class DriverKey {
    String DriverKey;

    public DriverKey(String driverKey) {
        DriverKey = driverKey;
    }
    public DriverKey(){

    }

    public String getDriverKey() {
        return DriverKey;
    }

    public void setDriverKey(String driverKey) {
        DriverKey = driverKey;
    }

    @Override
    public String toString() {
        return "DriverKey{" +
                "DriverKey='" + DriverKey + '\'' +
                '}';
    }
}
