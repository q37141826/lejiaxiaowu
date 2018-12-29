package com.qixiu.lejia.core.login;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.qixiu.lejia.mvp.BasePresenter;
import com.qixiu.lejia.mvp.CallPresenter;
import com.qixiu.lejia.mvp.LoadIndicatorView;
import com.qixiu.thirdparty.wx.WXUserInfo;
import com.tencent.tauth.IUiListener;

/**
 * Created by Long on 2018/4/24
 */
interface LoginContract {

    interface View extends LoadIndicatorView {

        void showGetCodeSuccess();

        void showLoginSuccess(String phone, @NonNull String token);

        void showThirdLoginCancel();

        void showTencentLoginSuccess(String nickname, String avatar, String openId);

        void startLoginBind(String nickname, String avatar, int type, String openid);

        void showWXUninstalled();

        void showWXLoginSuccess(WXUserInfo info);
    }

    interface Presenter extends CallPresenter {

        void getCode(@NonNull String phone);

        void login(@NonNull String phone, @NonNull String code);

        /**
         * 第三方登录
         *
         * @param nickname 昵称
         * @param avatar   头像地址
         * @param type     第三方标识 1-QQ 2-微信
         * @param openid   第三方返回唯一标识
         */
        void thirdLogin(String nickname, String avatar, int type, String openid);
    }

    interface ThirdLoginPresenter extends BasePresenter {
        void loginWithQQ(@NonNull Activity activity);

        IUiListener getTencentCallback();

        void loginWithWX(@NonNull Activity activity);
    }

}
