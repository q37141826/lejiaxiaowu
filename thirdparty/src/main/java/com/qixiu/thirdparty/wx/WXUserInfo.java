package com.qixiu.thirdparty.wx;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/3
 */
@Keep
public class WXUserInfo {

    private int errcode;

    private String errmsg;

    private String nickname;

    @SerializedName("headimgurl")
    private String avatar;

    private String openid;

    private String unionid;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
