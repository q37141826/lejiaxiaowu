package com.qixiu.lejia.core.version;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2017/3/25
 * APK版本
 */
@Keep
public class Version implements Parcelable {

    @SerializedName("name")
    private int versionCode;

    @SerializedName("apklink")
    private String apkLink;

    @SerializedName("type")
    private int updateType;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getApkLink() {
        return apkLink;
    }

    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.versionCode);
        dest.writeString(this.apkLink);
        dest.writeInt(this.updateType);
    }

    public Version() {
    }

    protected Version(Parcel in) {
        this.versionCode = in.readInt();
        this.apkLink = in.readString();
        this.updateType = in.readInt();
    }

    public static final Parcelable.Creator<Version> CREATOR = new Parcelable.Creator<Version>() {
        @Override
        public Version createFromParcel(Parcel source) {
            return new Version(source);
        }

        @Override
        public Version[] newArray(int size) {
            return new Version[size];
        }
    };

}
