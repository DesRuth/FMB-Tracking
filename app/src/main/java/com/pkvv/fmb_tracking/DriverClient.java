package com.pkvv.fmb_tracking;

import android.app.Application;

import com.pkvv.fmb_tracking.models.Drivers;

public class DriverClient extends Application {

    private Drivers drivers = null;

    public Drivers getDrivers() {
        return drivers;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
    }

}
