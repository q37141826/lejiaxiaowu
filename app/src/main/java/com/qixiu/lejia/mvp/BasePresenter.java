package com.qixiu.lejia.mvp;

import android.support.annotation.NonNull;

/**
 * Created by Long on 2017/2/20
 */
public interface BasePresenter {

    void onAttach(@NonNull BaseView view);

    void onDestroy();

}
