package com.qixiu.lejia.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/24
 */
public class ResponseStatus {

    //状态码 1-成功 other-失败
    @SerializedName("c")
    private int code;

    //提示消息
    @SerializedName("m")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return code == 1;
    }

}
