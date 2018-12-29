package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/27
 */
public class UserLevel {

    //1-->企业员工 0-->普通用户

    @SerializedName("user_enterprise")
    private int level;

    //1-->已签约 0-->未签约

    @SerializedName("sd_type")
    private int isSign;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }
}
