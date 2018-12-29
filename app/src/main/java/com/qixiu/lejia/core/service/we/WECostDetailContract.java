package com.qixiu.lejia.core.service.we;

import android.support.annotation.NonNull;

import com.qixiu.lejia.beans.WECostDetail;
import com.qixiu.lejia.mvp.BasePresenter;

/**
 * Created by Long on 2018/6/6
 */
interface WECostDetailContract {

    interface View {

        void onLoadFailure();

        void onLoadSuccess(WECostDetail resp);
    }

    interface Presenter extends BasePresenter{

        void onLoad(@NonNull String billId);

    }


}
