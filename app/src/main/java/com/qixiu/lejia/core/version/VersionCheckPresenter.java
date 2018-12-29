package com.qixiu.lejia.core.version;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.mvp.AbsCallPresenter;
import com.qixiu.lejia.mvp.BaseView;
import com.qixiu.lejia.mvp.LoadIndicatorView;


/**
 * Created by Long on 2017/3/25
 * 版本检查
 */
public class VersionCheckPresenter extends AbsCallPresenter
        implements VersionCheckContract.Presenter {

    private VersionCheckContract.View mView;

    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (VersionCheckContract.View) view;
    }

    @Override
    public void onCheck(int versionCode) {
        check(versionCode, null);
    }

    @Override
    public void onCheck(int versionCode, @NonNull LoadIndicatorView view) {
        check(versionCode, view);
    }

    @SuppressWarnings("unchecked")
    private void check(int versionCode, @Nullable LoadIndicatorView view) {
        call = AppApi.get().checkVersion();
        call.enqueue(new RequestCallback<Version>(view) {
            @Override
            protected void onSuccess(Version version) {
                //判断是否有新版本
                if (version == null || version.getVersionCode() == 0 ||
                        version.getVersionCode() <= versionCode) {
                    //没有新版本
                    mView.showNoNewVersion();
                } else {
                    mView.showNewVersion(version);
                }
            }
        });
    }

}
