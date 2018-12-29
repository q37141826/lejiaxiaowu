package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/6/5
 */
public class RentDetail {

    @SerializedName("ud_name")
    private String owner;

    @SerializedName("pa_money")
    private String rent;

    @SerializedName("pa_total_money")
    private String total;

    @SerializedName("pa_service_money")
    private String wuYe;

    @SerializedName("pa_late_money")
    private String lateFee;

    @SerializedName("pa_pay_type")
    private int payWay;

    @SerializedName("pa_pay_time")
    private String datetime;

    @SerializedName("pa_ro_id")
    private String roomId;

    @SerializedName("pa_reduce_money")
    private String pa_reduce_money;

    private String pa_cycle;

    private String pa_endtime;

    private String pa_createtime;

    private String pa_order;

    public String getPa_cycle() {
        return pa_cycle;
    }

    public void setPa_cycle(String pa_cycle) {
        this.pa_cycle = pa_cycle;
    }

    public String getPa_endtime() {
        return pa_endtime;
    }

    public void setPa_endtime(String pa_endtime) {
        this.pa_endtime = pa_endtime;
    }

    public String getPa_createtime() {
        return pa_createtime;
    }

    public void setPa_createtime(String pa_createtime) {
        this.pa_createtime = pa_createtime;
    }

    public String getPa_order() {
        return pa_order;
    }

    public void setPa_order(String pa_order) {
        this.pa_order = pa_order;
    }

    public String getPa_reduce_money() {
        return pa_reduce_money;
    }

    public void setPa_reduce_money(String pa_reduce_money) {
        this.pa_reduce_money = pa_reduce_money;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getWuYe() {
        return wuYe;
    }

    public void setWuYe(String wuYe) {
        this.wuYe = wuYe;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
