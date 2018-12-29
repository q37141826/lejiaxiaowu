package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/5/17 0017
 */
public class RepairDetail {

    @SerializedName("rep_name")
    private String serviceProject;

    @SerializedName("ri_user_name")
    private String name;

    @SerializedName("ri_tel")
    private String phone;

    @SerializedName("ri_addres")
    private String address;

    @SerializedName("ri_content")
    private String desc;

    @SerializedName("ri_createtime")
    private String submitTime;

    @SerializedName("ri_handle_time")
    private String allotTime;

    @SerializedName("ri_endtime")
    private String doneTime;

    @SerializedName("ri_state")
    private int status;

    @SerializedName("img")
    private List<ImageInfo> images;


    public String getServiceProject() {
        return serviceProject;
    }

    public void setServiceProject(String serviceProject) {
        this.serviceProject = serviceProject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getAllotTime() {
        return allotTime;
    }

    public void setAllotTime(String allotTime) {
        this.allotTime = allotTime;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    public void setImages(List<ImageInfo> images) {
        this.images = images;
    }
}
