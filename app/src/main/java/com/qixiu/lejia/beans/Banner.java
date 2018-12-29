package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/25
 */
public class Banner {

    @SerializedName("hi_url")
    private String image;

    @SerializedName("hi_link")
    private String link;

    @SerializedName("hi_detailurl")
    private String detailurl;


    public static Banner createFake() {
        Banner banner = new Banner();
        banner.setImage(null);
        banner.setLink(null);
        banner.setDetailurl(null);
        return banner;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }
}
