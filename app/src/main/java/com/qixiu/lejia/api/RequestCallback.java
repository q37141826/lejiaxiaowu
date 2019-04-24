package com.qixiu.lejia.api;


import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.qixiu.lejia.mvp.LoadIndicatorView;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Long on 2016/8/12
 * 自定义retrofit2 网络请求回调,方便统一
 */
public abstract class RequestCallback<T> implements Callback<BaseResponse<T>> {

    //Loading指示器
    private LoadIndicatorView mIndicatorView;

    protected RequestCallback() {
    }

    protected RequestCallback(@Nullable LoadIndicatorView loadIndicatorView) {
        mIndicatorView = loadIndicatorView;
        if (mIndicatorView != null) {
            mIndicatorView.showLoadIndicator();
        }
    }

    @Override
    public final void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        Gson gson = new Gson();
        Log.e("data",response.body()!=null? gson.toJson(response.body()):"body_error");
        if (!call.isCanceled()) {
            onComplete();
            if (response.isSuccessful()) {
                BaseResponse<T> body = response.body();
                if (body == null) {
                    onFailure(ResponseError.CONNECT);
                    return;
                }
                onSuccess(body.getBody());
            } else {
                onFailure(ResponseError.SERVICE);
            }
        }
    }

    @Override
    public final void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        t.printStackTrace();
        if (!call.isCanceled()) {
            onComplete();
            if (t instanceof ApiException) {
                //code!=1
                onFailure(ResponseError.of(t.getMessage()));
            } else if (t instanceof SocketTimeoutException) {
                onFailure(ResponseError.TIMEOUT);
            } else {
                onFailure(ResponseError.CONNECT);
            }
        }
    }

    /**
     * 加载失败
     *
     * @param error 错误状态
     */
    protected void onFailure(ResponseError error) {
        if (mIndicatorView != null) {
            mIndicatorView.showErrorMsg(error.getErrorMessage());
        }
    }

    /**
     * 加载成功
     *
     * @param t 数据
     */
    protected abstract void onSuccess(T t);


    /**
     * 加载完成
     */
    protected void onComplete() {
        if (mIndicatorView != null) {
            mIndicatorView.dismissLoadIndicator();
        }
    }

}
