package com.qixiu.lejia.beans;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;
import com.qixiu.lejia.utils.DatetimeConstants;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Long on 2018/5/2
 */
public class CorporateSignFifthInfo {

    @SerializedName("sd_starttime")
    private String startDate;

    @SerializedName("sd_time")
    private String lease;

    @SerializedName("sd_endtime")
    private String endtDate;

    @SerializedName("sd_sign_type")
    private String signMode;

    @SerializedName("sd_first_pay")
    private String firstPay;

    @SerializedName("sd_surplus_pay")
    private String monthlyPay;

    /**
     * 返回格式化后的租期开始时间
     *
     * @return 格式化的租期开始时间
     */
    public String getStartDateFmt() {
        String empty = "";
        if (TextUtils.isEmpty(startDate)) return empty;
        try {
            Date date = java.text.DateFormat.getInstance().parse(startDate);
            return DateFormat.format(DatetimeConstants.YTD_CN, date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return empty;
        }
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLease() {
        return lease;
    }

    public void setLease(String lease) {
        this.lease = lease;
    }

    public String getSignMode() {
        return signMode;
    }

    public void setSignMode(String signMode) {
        this.signMode = signMode;
    }

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

    public String getEndtDate() {
        return endtDate;
    }

    public void setEndtDate(String endtDate) {
        this.endtDate = endtDate;
    }
}
