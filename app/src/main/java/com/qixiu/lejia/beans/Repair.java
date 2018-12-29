package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17
 */
public class Repair {

    @SerializedName("ri_id")
    private String id;

    @SerializedName("ri_title")
    private String title;

    @SerializedName("ri_createtime")
    private String time;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repair repair = (Repair) o;

        return id.equals(repair.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
