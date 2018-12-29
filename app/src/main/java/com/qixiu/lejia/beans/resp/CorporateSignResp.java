package com.qixiu.lejia.beans.resp;

import com.google.gson.annotations.SerializedName;
import com.qixiu.lejia.beans.CorporateSignFifthInfo;
import com.qixiu.lejia.beans.Room;

/**
 * Created by Long on 2018/5/2
 */
public class CorporateSignResp {

    @SerializedName("detail")
    private CorporateSignFifthInfo corporateSignFifthInfo;

    @SerializedName("roomdetail")
    private Room room;

    public CorporateSignFifthInfo getCorporateSignFifthInfo() {
        return corporateSignFifthInfo;
    }

    public void setCorporateSignFifthInfo(CorporateSignFifthInfo corporateSignFifthInfo) {
        this.corporateSignFifthInfo = corporateSignFifthInfo;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
