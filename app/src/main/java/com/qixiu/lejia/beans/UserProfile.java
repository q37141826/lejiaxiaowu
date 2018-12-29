package com.qixiu.lejia.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/26
 */
public class UserProfile implements Parcelable {

    @SerializedName("ud_url")
    private String avatar;

    @SerializedName("ud_nickname")
    private String nickName;

    /*资料是否完整 0=未完善 1=已完善 */
    @SerializedName("infostatus")
    private int dataFullStatus;

    @SerializedName("uo_hobby")
    private String hobby;

    @SerializedName("uo_height")
    private String heightAndWeight;

    @SerializedName("uo_education")
    private String education;

    @SerializedName("uo_income")
    private String revenue;

    @SerializedName("uo_marriage")
    private String maritalStatus;

    private String user_tel;

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getDataFullStatus() {
        return dataFullStatus;
    }

    public void setDataFullStatus(int dataFullStatus) {
        this.dataFullStatus = dataFullStatus;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getHeightAndWeight() {
        return heightAndWeight;
    }

    public void setHeightAndWeight(String heightAndWeight) {
        this.heightAndWeight = heightAndWeight;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.nickName);
        dest.writeInt(this.dataFullStatus);
        dest.writeString(this.hobby);
        dest.writeString(this.heightAndWeight);
        dest.writeString(this.education);
        dest.writeString(this.revenue);
        dest.writeString(this.maritalStatus);
        dest.writeString(this.user_tel);
    }

    public UserProfile() {
    }

    protected UserProfile(Parcel in) {
        this.avatar = in.readString();
        this.nickName = in.readString();
        this.dataFullStatus = in.readInt();
        this.hobby = in.readString();
        this.heightAndWeight = in.readString();
        this.education = in.readString();
        this.revenue = in.readString();
        this.maritalStatus = in.readString();
        this.user_tel = in.readString();
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
