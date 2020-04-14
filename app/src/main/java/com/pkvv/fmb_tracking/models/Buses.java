package com.pkvv.fmb_tracking.models;

public class Buses {
    private Drivers drivers;
    String BusNo ;

    public Buses(Drivers drivers, String busNo) {
        this.drivers = drivers;
        BusNo = busNo;
    }
    public Buses(){

    }

    public Drivers getDrivers() {
        return drivers;
    }

    public String getBusNo() {
        return BusNo;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    @Override
    public String toString() {
        return "Buses{" +
                "drivers=" + drivers +
                ", BusNo='" + BusNo + '\'' +
                '}';
    }
}
