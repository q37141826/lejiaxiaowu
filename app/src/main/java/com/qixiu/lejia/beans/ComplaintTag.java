package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17 0017
 */
public class ComplaintTag {

    @SerializedName("ct_id")
    private String id;

    @SerializedName("ct_title")
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
