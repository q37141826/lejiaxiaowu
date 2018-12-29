package com.qixiu.lejia.core.service.we;

import android.support.annotation.NonNull;

import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.WECostDetail;
import com.qixiu.lejia.mvp.AbsCallPresenter;
import com.qixiu.lejia.mvp.BaseView;

/**
 * Created by Long on 2018/6/6 0006
 */
class WECostDetailPresenter extends AbsCallPresenter implements WECostDetailContract.Presenter {

    private WECostDetailContract.View mView;

    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (WECostDetailContract.View) view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onLoad(@NonNull String billId) {
        call = AppApi.get().wePayDetail(LoginStatus.getToken(), billId);
        call.enqueue(new RequestCallback<WECostDetail>() {
            @Override
            protected void onSuccess(WECostDetail resp) {
                mView.onLoadSuccess(resp);
            }

            @Override
            protected void onFailure(ResponseError error) {
                mView.onLoadFailure();
            }
        });
    }

}
