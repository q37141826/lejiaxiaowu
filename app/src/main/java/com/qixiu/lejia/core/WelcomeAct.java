package com.qixiu.lejia.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.prefs.Prefs;
import com.qixiu.lejia.prefs.PrefsKeys;
import com.qixiu.lejia.service.JpushEngine;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Long on 2018/5/10
 */
public class WelcomeAct extends BaseActivity {
    String perimission[] = {Manifest.permission.READ_PHONE_STATE};
    private final Handler mHandler = new Handler();
    private final Runnable delayTask = () -> {
        //判断是否是第一次运行
        boolean firstRun = Prefs.getBoolean(PrefsKeys.KEY_FIRST_RUN, true);
        if (firstRun) {
            //启动引导页
            GuideAct.start(this);
        } else {
            MainActivity.start(WelcomeAct.this);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (!hasPermission(perimission)) {
            hasRequse(1, perimission);
            return;
        } else {
            JpushEngine.initJPush(this);
            JPushInterface.setDebugMode(true);
            mHandler.postDelayed(delayTask, 1000);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    //检查权限
    public boolean hasPermission(String... permission) {
        for (String permissiom : permission) {
            if (ActivityCompat.checkSelfPermission(this, permissiom) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //添加权限
    public void hasRequse(int code, String... permission) {
        ActivityCompat.requestPermissions(this, permission, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!hasPermission(perimission)) {
            hasRequse(1, perimission);
            finish();
            return;
        } else {
            JpushEngine.initJPush(this);
            JPushInterface.setDebugMode(true);
            mHandler.postDelayed(delayTask, 1000);
        }
    }
}
