package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17 0017
 */
public class PointsResp {

    @SerializedName("ud_name")
    private String name;

    @SerializedName("user_integral")
    private String points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
