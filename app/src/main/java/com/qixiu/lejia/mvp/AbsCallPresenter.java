package com.qixiu.lejia.mvp;

import android.support.annotation.CallSuper;

import retrofit2.Call;

/**
 * Created by Long on 2017/3/25
 */
public abstract class AbsCallPresenter implements CallPresenter {

    protected Call call;

    @CallSuper
    @Override
    public void onCancelLoad() {
        CallUtil.cancel(call);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        onCancelLoad();
    }

}
