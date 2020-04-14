package com.pkvv.fmb_tracking.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Drivers implements Parcelable{

    private String email;
    private String user_id;
    private String username;
    private String avatar;

    public Drivers(String email, String user_id, String username, String avatar) {
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
    }

    public Drivers() {

    }

    protected Drivers(Parcel in) {
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        avatar = in.readString();
    }

    public static final Creator<Drivers> CREATOR = new Creator<Drivers>() {
        @Override
        public Drivers createFromParcel(Parcel in) { return new Drivers(in);
        }

        @Override
        public Drivers[] newArray(int size) { return new Drivers[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static Creator<Drivers> getCREATOR() {
        return CREATOR;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(avatar);
    }
}

