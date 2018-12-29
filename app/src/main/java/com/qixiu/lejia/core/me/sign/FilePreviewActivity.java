package com.qixiu.lejia.core.me.sign;

import android.os.Bundle;

import com.qixiu.tbslib.FileDisplayActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class FilePreviewActivity extends FileDisplayActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void goRight() {
        super.goRight();
//        SignNameActivity.start(this,SignNameActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void  getResult(String path){

    }
}
