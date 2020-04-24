package com.pkvv.fmb_tracking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class Notice implements Parcelable {
    private String title;
    private String content;
    private @ServerTimestamp Date timestamp;
    private String note_id;

    public Notice(String title, String content, Date timestamp, String note_id) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.note_id = note_id;
    }
    public Notice() {

    }
    protected Notice(Parcel in) {
        title = in.readString();
        content = in.readString();
        note_id = in.readString();

    }
    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }
        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public static Creator<Notice> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(note_id);

    }
}
