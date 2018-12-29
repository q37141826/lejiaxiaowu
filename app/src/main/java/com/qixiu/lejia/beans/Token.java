package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/24
 */
public class Token {

    @SerializedName("uid")
    private String uid;

    private String phone;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
