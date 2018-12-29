package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/27
 */
public class Room {

    @SerializedName("sd_ro_id")
    private String id;

    @SerializedName("ro_name")
    private String name;

    @SerializedName("ap_size")
    private String spec;

    @SerializedName("ap_room")
    private String area;

    @SerializedName("ro_number")
    private String number;

    @SerializedName("ro_short_money")
    private String rent;

    @SerializedName("ap_log")
    private String image;

    @SerializedName("sd_id")
    private String signedId;

    @SerializedName("sd_starttime")
    private String sd_starttime;

    public String getSd_starttime() {
        return sd_starttime;
    }

    public void setSd_starttime(String sd_starttime) {
        this.sd_starttime = sd_starttime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSignedId() {
        return signedId;
    }

    public void setSignedId(String signedId) {
        this.signedId = signedId;
    }

}
