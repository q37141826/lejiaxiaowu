package com.qixiu.lejia.core.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.utils.ToastUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Long on 2018/6/1
 */
@SuppressLint("Registered")
public class BaseShareCallbackAct extends BaseActivity implements IUiListener {


    @Override
    public void onComplete(Object o) {
        ToastUtils.showShort(this, R.string.share_success);
    }

    @Override
    public void onError(UiError uiError) {
        ToastUtils.showShort(this, R.string.share_failure);
    }

    @Override
    public void onCancel() {
        ToastUtils.showShort(this, R.string.share_cancel);
    }
    public Context getContext(){
        return this;
    }
    public Activity getActivity(){
        return this;
    }
}
