package com.qixiu.lejia.api;

/**
 * Created by Long on 2017/12/14
 * <p>响应错误</p>
 */
public final class ResponseError {

    public static final ResponseError CONNECT = of("网络连接失败，请稍后重试。");
    public static final ResponseError TIMEOUT = of("网络连接超时，请稍后重试。");
    public static final ResponseError SERVICE = of("服务器不稳定，请稍后重试。");

    private String errorMessage;

    private ResponseError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static ResponseError of(String errorMessage){
        return new ResponseError(errorMessage);
    }

}
