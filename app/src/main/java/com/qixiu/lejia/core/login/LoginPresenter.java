package com.qixiu.lejia.core.login;

import android.support.annotation.NonNull;

import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.beans.Token;
import com.qixiu.lejia.mvp.AbsCallPresenter;
import com.qixiu.lejia.mvp.BaseView;
import com.qixiu.lejia.service.JpushEngine;
import com.qixiu.lejia.utils.DeviceUtils;

/**
 * Created by Long on 2018/4/24
 */
@SuppressWarnings("unchecked")
class LoginPresenter extends AbsCallPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (LoginContract.View) view;
    }

    @Override
    public void getCode(@NonNull String phone) {
        call = AppApi.get().getCode(phone, 1);
        call.enqueue(new RequestCallback<Object>(mView) {
            @Override
            protected void onSuccess(Object o) {
                mView.showGetCodeSuccess();
            }
        });
    }

    @Override
    public void login(@NonNull String phone, @NonNull String code) {
        call = AppApi.get().login(phone, code,1+"", JpushEngine.getMachineCode());
        call.enqueue(new RequestCallback<Token>(mView) {
            @Override
            protected void onSuccess(Token token) {
                mView.showLoginSuccess(phone, token.getUid());
            }
        });
    }

    @Override
    public void thirdLogin(String nickname, String avatar, int type, String openid) {
        String id = DeviceUtils.getUniqueId(AppContext.getContext());
        call = AppApi.get().thirdLogin(nickname, avatar, openid, type, id);
        call.enqueue(new RequestCallback<Token>(mView) {
            @Override
            protected void onSuccess(Token token) {
                mView.showLoginSuccess(token.getPhone(), token.getUid());
            }

            @Override
            protected void onFailure(ResponseError error) {
                if (error.getErrorMessage().equals("请绑定手机")) {
                    mView.startLoginBind(nickname, avatar, type, openid);
                } else {
                    super.onFailure(error);
                }
            }
        });
    }

}
