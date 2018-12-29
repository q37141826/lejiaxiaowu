package com.qixiu.lejia.core.version;

import android.support.annotation.NonNull;

import com.qixiu.lejia.mvp.BasePresenter;
import com.qixiu.lejia.mvp.BaseView;
import com.qixiu.lejia.mvp.LoadIndicatorView;


/**
 * Created by Long on 2017/3/25
 * 新版本检查
 */
public interface VersionCheckContract {

    interface View extends BaseView {

        void showNoNewVersion();

        void showNewVersion(@NonNull Version version);

    }

    interface Presenter extends BasePresenter {

        void onCheck(int versionCode);

        void onCheck(int versionCode, @NonNull LoadIndicatorView view);

    }

}
