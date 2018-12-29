package com.qixiu.lejia.beans;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Long on 2018/5/10
 */
public class ShareInfo implements Parcelable {

    private String title;

    private String content;

    private String shareUrl;

    private String shareImageUrl;

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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareImageUrl);
    }

    public ShareInfo() {
    }

    protected ShareInfo(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.shareUrl = in.readString();
        this.shareImageUrl = in.readString();
    }

    public static final Parcelable.Creator<ShareInfo> CREATOR = new Parcelable.Creator<ShareInfo>() {
        @Override
        public ShareInfo createFromParcel(Parcel source) {
            return new ShareInfo(source);
        }

        @Override
        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };
}
