package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/14
 */
public class SignedRoom {

    @SerializedName("st_name")
    private String name;

    @SerializedName("ro_number")
    private String roomNum;

    @SerializedName("ro_id")
    private int roomId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }


    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
