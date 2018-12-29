package com.qixiu.lejia.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/24 0024
 * <pre>
 *     统一服务器返回数据样式
 * </pre>
 */
public class BaseResponse<T> {

    //状态码 1-成功 other-失败
    @SerializedName("c")
    private int code;

    //提示消息
    @SerializedName("m")
    private String message;

    //返回数据
    @SerializedName("o")
    private T body;

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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
