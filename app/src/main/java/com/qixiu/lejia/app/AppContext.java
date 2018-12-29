package com.qixiu.lejia.app;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.thirdparty.SdkConstants;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

public class AppContext extends MultiDexApplication {

    private static AppContext sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;

        //Log开关
        Logger.enable(BuildConfig.DEBUG);

        //注册activity生命周期回调
        registerActivityLifecycleCallbacks(AppLifeCircleCallback.get());
        //极光推送
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);


        /*
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(this, SdkConstants.UMENG_KEY, "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE, null);
        /*
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }

    /**
     * 获取全局 Application
     *
     * @return Global Application
     */
    public static Application getApplication() {
        return sAppContext;
    }

    /**
     * 获取全局Context
     *
     * @return Global context
     */
    public static Context getContext() {
        return sAppContext;
    }

}
