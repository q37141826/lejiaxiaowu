package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/6/6
 * <pre>
 *  {
 *      "water_type": "0",
 *      "water_id": "0",
 *      "water_read": "0",
 *      "electric_type": "1",
 *      "electric_id": "1",
 *      "electric_read": "0"
 * }
 * </pre>
 */
public class WePayStatus {

    @SerializedName("ro_number")
    private String roomNum;

    @SerializedName("water_type")
    private int    waterPayState;
    @SerializedName("water_id")
    private String waterBillId;
    @SerializedName("water_read")
    private int    waterRead;

    @SerializedName("electric_type")
    private int    electricPayState;
    @SerializedName("electric_id")
    private String electricBillId;
    @SerializedName("electric_read")
    private int    electricRead;


    public boolean showWaterBadge() {
        return waterPayState == 2 && waterRead == 0;
    }

    public boolean showElectricBadge() {
        return electricPayState == 2 && electricRead == 0;
    }


    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public int getWaterPayState() {
        return waterPayState;
    }

    public void setWaterPayState(int waterPayState) {
        this.waterPayState = waterPayState;
    }

    public String getWaterBillId() {
        return waterBillId;
    }

    public void setWaterBillId(String waterBillId) {
        this.waterBillId = waterBillId;
    }

    public int getWaterRead() {
        return waterRead;
    }

    public void setWaterRead(int waterRead) {
        this.waterRead = waterRead;
    }

    public int getElectricPayState() {
        return electricPayState;
    }

    public void setElectricPayState(int electricPayState) {
        this.electricPayState = electricPayState;
    }

    public String getElectricBillId() {
        return electricBillId;
    }

    public void setElectricBillId(String electricBillId) {
        this.electricBillId = electricBillId;
    }

    public int getElectricRead() {
        return electricRead;
    }

    public void setElectricRead(int electricRead) {
        this.electricRead = electricRead;
    }

}
