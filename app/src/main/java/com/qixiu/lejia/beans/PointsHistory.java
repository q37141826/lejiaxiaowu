package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17
 */
public class PointsHistory {

    @SerializedName("ui_title")
    private String way;

    @SerializedName("ui_createtime")
    private String time;

    @SerializedName("ui_integral")
    private String points;

    // 1-加 0-减
    @SerializedName("ui_type")
    private int type;

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
