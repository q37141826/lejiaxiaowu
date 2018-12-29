package com.qixiu.lejia.app;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;

/**
 * Created by Long on 2018/4/24
 * <pre>
 *     登录事件发布
 * </pre>
 */
public class LoginEvent extends Observable {

    private static LoginEvent instance = new LoginEvent();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Event.LOGIN, Event.LOGOUT})
    public @interface Event {
        int LOGIN  = 1;  //登录
        int LOGOUT = 2;  //登出
    }


    public static LoginEvent getInstance() {
        return instance;
    }

    /**
     * 发布登录成功事件通知
     */
    public void loginSuccess() {
        setChanged();
        notifyObservers(Event.LOGIN);
    }

    /**
     * 发布退出登录事件通知
     */
    public void logout() {
        setChanged();
        notifyObservers(Event.LOGOUT);
    }
}
