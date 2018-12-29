package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/5/17
 */
public class Complaint {

    @SerializedName("ad_id")
    private String id;
    @SerializedName("ud_url")
    private String avatar;
    @SerializedName("ct_title")
    private String tag;
    @SerializedName("ad_content")
    private String desc;
    @SerializedName("ad_createtime")
    private String date;
    @SerializedName("ad_read_type")
    private int readed;              //是否有新回答


    @SerializedName("ad_reply_content")
    private String replyContent;
    @SerializedName("ad_updatetime")
    private String replyTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }
}
