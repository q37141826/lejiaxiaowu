package com.qixiu.lejia.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/25
 * <pre>
 *     活动
 * </pre>
 */
public class Event implements Parcelable {

    @SerializedName("ay_id")
    private String id;

    @SerializedName("ay_title")
    private String title;

    @SerializedName("ay_content")
    private String intro;

    @SerializedName("ay_starttime")
    private String startDate;

    @SerializedName("ay_endtime")
    private String endDate;

    @SerializedName("ay_link")
    private String link;

    @SerializedName("ay_url")
    private String image;

    @SerializedName("ay_type")
    private int type;

    @SerializedName("ay_post_content")
    private String ay_post_content;

    public String getAy_post_content() {
        return ay_post_content;
    }

    public void setAy_post_content(String ay_post_content) {
        this.ay_post_content = ay_post_content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.intro);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.link);
        dest.writeString(this.image);
        dest.writeInt(this.type);
        dest.writeString(this.ay_post_content);
    }

    public Event() {
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.intro = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.link = in.readString();
        this.image = in.readString();
        this.type = in.readInt();
        this.ay_post_content = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
