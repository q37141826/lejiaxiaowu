package com.qixiu.lejia.api.request;


import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public interface OKHttpUIUpdataListener<T extends Object> {
    void onSuccess(T data, int i);
    void onError(Call call, Exception e, int i);
    void onFailure(C_CodeBean c_codeBean);

}
