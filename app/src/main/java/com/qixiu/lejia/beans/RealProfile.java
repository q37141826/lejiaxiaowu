package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/10
 */
public class RealProfile {

    @SerializedName("ud_name")
    private String realName;

    @SerializedName("user_tel")
    private String phone;

    @SerializedName("ud_sex")
    private String sex;

    @SerializedName("ud_identity")
    private String id;

    private String identified;//0表示没有认证，1表示认证过

    public String getIdentified() {
        return identified;
    }

    public void setIdentified(String identified) {
        this.identified = identified;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
