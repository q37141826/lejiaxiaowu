package com.qixiu.lejia.core;

import android.support.annotation.NonNull;

import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.mvp.AbsCallPresenter;
import com.qixiu.lejia.mvp.BaseView;

/**
 * Created by Long on 2018/4/27
 */
public class CodePresenter extends AbsCallPresenter implements CodeContract.Presenter {

    private CodeContract.View mView;

    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (CodeContract.View) view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getCode(@NonNull String phone, int scene) {
        call = AppApi.get().getCode(phone, scene);
        call.enqueue(new RequestCallback<Object>(mView) {
            @Override
            protected void onSuccess(Object o) {
                mView.showGetCodeSuccess();
            }
        });
    }

}
