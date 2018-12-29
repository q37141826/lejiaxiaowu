package com.qixiu.lejia.core.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.UserLevel;
import com.qixiu.lejia.core.login.LoginActivity;
import com.qixiu.lejia.core.sign.AuthenticationAct;
import com.qixiu.lejia.core.sign.UserType;
import com.qixiu.lejia.utils.ToastUtils;

/**
 * Created by Long on 2018/5/8
 */
public class WelcomeSettledAct extends BaseToolbarAct {

    public static void start(Context context) {
        Intent starter = new Intent(context, WelcomeSettledAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome_settle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @SuppressWarnings("unchecked")
    public void startAuthentication(View view) {
        if (!LoginStatus.isLogged()) {
            LoginActivity.start(this);
            return;
        }
        call = AppApi.get().getUserType(LoginStatus.getToken());
        call.enqueue(new RequestCallback<UserLevel>(this) {
            @Override
            protected void onSuccess(UserLevel level) {
                if (level.getLevel() == 0) {
                    ToastUtils.showShort(WelcomeSettledAct.this, "您不是企业用户");
                    return;
                }
                if (level.getIsSign() == 1) {
                    ToastUtils.showShort(WelcomeSettledAct.this, "您已签约");
                    return;
                }
                AuthenticationAct.start(WelcomeSettledAct.this, UserType.CORPORATE,"");
                finish();
            }
        });
    }
}
