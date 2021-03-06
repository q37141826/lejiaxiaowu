package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/28
 * <pre>
 *     租金
 * </pre>
 */
public class Rent {

    @SerializedName("sd_first_pay")
    private String firstPay;

    @SerializedName("sd_surplus_pay")
    private String monthlyPay;

    public String getFirstPay() {
        return firstPay;
    }

    public void setFirstPay(String firstPay) {
        this.firstPay = firstPay;
    }

    public String getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(String monthlyPay) {
        this.monthlyPay = monthlyPay;
    }
}
