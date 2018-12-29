package com.qixiu.lejia.base;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     All Activity must extends this.
 * </pre>
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onResume(this);
        Log.e("where",this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenOrentation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //提示，参数在ActivityInfo里面
    public void setScreenOrentation(int screenOrentation) {
        setRequestedOrientation(screenOrentation);
    }

}
