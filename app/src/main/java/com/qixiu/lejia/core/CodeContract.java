package com.qixiu.lejia.core;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.qixiu.lejia.mvp.CallPresenter;
import com.qixiu.lejia.mvp.LoadIndicatorView;

/**
 * Created by Long on 2018/4/27
 * <pre>
 *     短信验证码相关
 * </pre>
 */
public interface CodeContract {

    interface View extends LoadIndicatorView {

        void showGetCodeSuccess();
    }

    interface Presenter extends CallPresenter {

        /*请求服务器发送短信验证码*/
        void getCode(@NonNull String phone, @IntRange(from = 1, to = 7) int scene);
    }

}
