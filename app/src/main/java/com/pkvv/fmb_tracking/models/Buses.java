package com.pkvv.fmb_tracking.models;

import android.os.Parcelable;

import java.io.Serializable;

public class Buses implements Serializable {

    String BusNo  ;
    String user_id;

    public Buses(String busNo, String user_id) {
        BusNo = busNo;
        this.user_id = user_id;
    }
    public Buses(){

    }

    public String getBusNo() {
        return BusNo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return BusNo ;

    }
}
