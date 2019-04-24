package com.qixiu.lejia.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qixiu.lejia.core.login.LoginActivity;
import com.qixiu.lejia.core.me.profile.CompleteProfileAct;
import com.qixiu.lejia.prefs.Prefs;
import com.qixiu.lejia.prefs.PrefsKeys;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Long on 2018/4/26
 */
public final class LoginStatus {

    private static LoginStatus instance = new LoginStatus();

    /*是否已登录*/
    private volatile boolean logged;
    private String token;

    private LoginStatus() {
        logged = Prefs.contains(PrefsKeys.KEY_TOKEN);
    }

    public static boolean isLogged() {
        return instance.logged;
    }

    @Nullable
    public static String getToken() {
        if (instance.token == null) {
            instance.token = Prefs.getString(PrefsKeys.KEY_TOKEN);
        }
        return instance.token;//todo 测试的时候改变一下这个地方
//        return 4060 + "";
    }

    /*登录成功*/
    public static void logged(String phone, @NonNull String token) {
        instance.logged = true;
        instance.token = token;
        //保存uid
        Prefs.put(PrefsKeys.KEY_TOKEN, token);
        //保存手机号
        Prefs.put(PrefsKeys.KEY_PHONE, phone);
        //
        Prefs.put(PrefsKeys.KEY_NAME, phone);

        //发布登录成功事件
        LoginEvent.getInstance().loginSuccess();

        //友盟账号统计
        MobclickAgent.onProfileSignIn(phone);
    }

    /*退出登录*/
    public static void logout() {
        instance.logged = false;
        instance.token = null;

        //删除保存的uid
        Prefs.remove(PrefsKeys.KEY_TOKEN);
        //删除保存的phone
        Prefs.remove(PrefsKeys.KEY_PHONE);

        //发布退出登录事件
        LoginEvent.getInstance().logout();

        //友盟账号统计(登出)
        MobclickAgent.onProfileSignOff();

    }

    //验证登录，没有登录跳转登录界面
    public static boolean verifiedLogin(Context context) {
        if (!isLogged()) {
            LoginActivity.start(context);
            return false;
        }
        return true;
    }

    //验证登录，没有登录跳转登录界面
    public static void verifiedIdentified(Context context) {
        if (!"1".equals(Prefs.getString(PrefsKeys.IS_IDENTIFYED))) {
            CompleteProfileAct.start(context);
        }
    }

    public static boolean isVerified() {
        if (!"1".equals(Prefs.getString(PrefsKeys.IS_IDENTIFYED))) {
            return false;
        }
        return true;
    }

}
