package com.qixiu.thirdparty.wx;

import java.util.Observable;

/**
 * Created by Long on 2018/5/3
 * <pre>
 *     微信登录事件
 * </pre>
 */
public class WXLoginEvent extends Observable {

    private static final WXLoginEvent ourInstance = new WXLoginEvent();

    public static WXLoginEvent getInstance() {
        return ourInstance;
    }

    private WXLoginEvent() {
    }

    /**
     * 微信登录授权成功
     *
     * @param code 微信授权临时票据，可获得access_token
     */
    public void authSuccess(String code) {
        setChanged();
        notifyObservers(code);
    }

    /**
     * 微信登录授权失败
     */
    public void authFailure() {
        setChanged();
        notifyObservers();
    }

}
