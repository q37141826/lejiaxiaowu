package com.qixiu.lejia.base;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class BaseWhiteStateBarActivity extends BaseMultiStateAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


}
