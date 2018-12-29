package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/16
 */
public class ImageInfo {

    @SerializedName("rg_id")
    private String id;

    @SerializedName("rg_url")
    private String src;

    public ImageInfo(String src) {
        this.src = src;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
