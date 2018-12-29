package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Long on 2018/5/8
 * <pre>
 *     微信支付数据
 * </pre>
 */
public class WXPayInfo {

    @SerializedName("appid")
    private String appId;

    @SerializedName("partnerid")
    private String partnerId;

    @SerializedName("prepayid")
    private String prepayId;

    @SerializedName("package")
    private String packageValue;

    @SerializedName("noncestr")
    private String nonceStr;

    @SerializedName("timestamp")
    private String timeStamp;

    @SerializedName("sign")
    private String sign;


    public static PayReq toPayReq(WXPayInfo info) {
        PayReq payReq = new PayReq();
        payReq.appId = info.appId;
        payReq.partnerId = info.partnerId;
        payReq.prepayId = info.prepayId;
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = info.nonceStr;
        payReq.timeStamp = info.timeStamp;
        payReq.sign = info.sign;
        return payReq;
    }

    public static WXPayInfo formJson(JSONObject object) {
        WXPayInfo info = new WXPayInfo();
        try {
            info.setAppId(object.getString("appid"));
            info.setPartnerId(object.getString("partnerid"));
            info.setPrepayId(object.getString("prepayid"));
            info.setPackageValue(object.getString("package"));
            info.setNonceStr(object.getString("noncestr"));
            info.setTimeStamp(object.getString("timestamp"));
            info.setSign(object.getString("sign"));
            return info;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
