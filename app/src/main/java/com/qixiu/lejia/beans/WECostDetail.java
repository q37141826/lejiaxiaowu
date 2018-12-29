package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/6/6
 */
public class WECostDetail {

    @SerializedName("hy_total")
    private String sum;     //总金额


    @SerializedName("hy_pay_total")
    private String sumAll;     //实际支付金额

    @SerializedName("username")
    private String owner;   //户主

    @SerializedName("hy_last_time")
    private String startCountDate;
    @SerializedName("hy_now_time")
    private String endCountDate;

    @SerializedName("hy_last_code")
    private String lastCount;
    @SerializedName("hy_now_code")
    private String currentCount;

    @SerializedName("hy_degrees")
    private String owed;

    @SerializedName("hy_late_money")
    private String lateFee; //滞纳金

    @SerializedName("hy_unit_price")
    private String price;   // 水/电单价

    @SerializedName("hy_payaa")
    private int halve;      //平摊

    @SerializedName("hy_pay_type")
    private int payWay;

    @SerializedName("hy_pay_time")
    private String paidDate;

    @SerializedName("hy_ro_id")
    private String roomId;

    @SerializedName("hy_reduce_money")
    private String hy_reduce_money;


    private String hy_cycle;

    private String hy_endtime;

    private String hy_createtime;

    private String hy_order;

    public String getHy_cycle() {
        return hy_cycle;
    }

    public void setHy_cycle(String hy_cycle) {
        this.hy_cycle = hy_cycle;
    }

    public String getHy_endtime() {
        return hy_endtime;
    }

    public void setHy_endtime(String hy_endtime) {
        this.hy_endtime = hy_endtime;
    }

    public String getHy_createtime() {
        return hy_createtime;
    }

    public void setHy_createtime(String hy_createtime) {
        this.hy_createtime = hy_createtime;
    }

    public String getHy_order() {
        return hy_order;
    }

    public void setHy_order(String hy_order) {
        this.hy_order = hy_order;
    }

    public String getHy_reduce_money() {
        return hy_reduce_money;
    }

    public void setHy_reduce_money(String hy_reduce_money) {
        this.hy_reduce_money = hy_reduce_money;
    }

    public String getSumAll() {
        return sumAll;
    }

    public void setSumAll(String sumAll) {
        this.sumAll = sumAll;
    }
    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStartCountDate() {
        return startCountDate;
    }

    public void setStartCountDate(String startCountDate) {
        this.startCountDate = startCountDate;
    }

    public String getEndCountDate() {
        return endCountDate;
    }

    public void setEndCountDate(String endCountDate) {
        this.endCountDate = endCountDate;
    }

    public String getLastCount() {
        return lastCount;
    }

    public void setLastCount(String lastCount) {
        this.lastCount = lastCount;
    }

    public String getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(String currentCount) {
        this.currentCount = currentCount;
    }

    public String getOwed() {
        return owed;
    }

    public void setOwed(String owed) {
        this.owed = owed;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getHalve() {
        return halve;
    }

    public void setHalve(int halve) {
        this.halve = halve;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
