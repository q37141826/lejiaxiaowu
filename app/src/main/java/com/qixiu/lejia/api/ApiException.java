package com.qixiu.lejia.api;

/**
 * Created by Long on 2018/4/24
 * <pre>
 *     服务器返回code不等于1的错误
 * </pre>
 */
public final class ApiException extends RuntimeException {

    private ApiException(String message) {
        super(message);
    }

    public static ApiException of(String message){
        return new ApiException(message);
    }

}
