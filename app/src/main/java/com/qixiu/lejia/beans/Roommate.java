package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17 0017
 */
public class Roommate {

    @SerializedName("rm_id")
    private String id;

    @SerializedName("rm_name")
    private String name;

    @SerializedName("rm_mate_tel")
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
