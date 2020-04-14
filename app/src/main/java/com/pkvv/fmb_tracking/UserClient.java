package com.pkvv.fmb_tracking;

import android.app.Application;

import com.pkvv.fmb_tracking.models.User;

public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}