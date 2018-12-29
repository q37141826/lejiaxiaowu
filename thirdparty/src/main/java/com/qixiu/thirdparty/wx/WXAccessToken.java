package com.qixiu.thirdparty.wx;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/3
 */
@Keep
public class WXAccessToken {

    private int errcode;

    private String errmsg;

    @SerializedName("openid")
    private String openId;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


}
