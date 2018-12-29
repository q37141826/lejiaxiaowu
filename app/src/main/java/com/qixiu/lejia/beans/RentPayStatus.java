package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/6/4
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
public class RentPayStatus {

    @SerializedName("ro_number")
    private String roomNum;

    //账单ID
    @SerializedName("pa_id")
    private String billId;

    //房租账单状态    2为欠费,1为已缴费,0为未生成账单
    @SerializedName("pa_type")
    private int payStatus;

    //阅读状态,1是已读,0是未读
    @SerializedName("pa_read")
    private int read;


    //是否显示红点
    public boolean showBadge() {
        return payStatus == 2 && read == 0;
    }


    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
