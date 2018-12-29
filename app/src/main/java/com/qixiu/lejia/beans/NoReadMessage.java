package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Title:
 * Description:
 * Copyright:武汉企秀网络科技有限公司 Copyright(c)20XX
 * author:xuchi
 * date: 2018/6/21 0021
 * version 1.0
 */
public class NoReadMessage {

    @SerializedName("ne_read")
    private int noRead;

    public int getNoRead() {
        return noRead;
    }

    public void setNoRead(int noRead) {
        this.noRead = noRead;
    }
}
