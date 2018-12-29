package com.qixiu.lejia.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.qixiu.lejia.utils.Logger;

/**
 * Created by Long on 2018/4/20 0020
 * <pre>
 *     Activity 生命周期回调
 * </pre>
 */
public class AppLifeCircleCallback implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "AppLifeCircleCallback";

    public static AppLifeCircleCallback get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final AppLifeCircleCallback INSTANCE = new AppLifeCircleCallback();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivityCreated(). ");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivityStarted().");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivityResumed().");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivityPaused() ");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivityStopped()");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivitySaveInstanceState().");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.d(TAG, activity.getLocalClassName() + "  ====> onActivityDestroyed().");
    }

}
