package com.qixiu.lejia.core.me.sign;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.utils.PictureCut;
import com.qixiu.lejia.widget.PaletteView;

import org.greenrobot.eventbus.EventBus;

public class SignNameActivity extends BaseWhiteStateBarActivity {

    Bitmap signatuerBitamp;
    PaletteView paletteview;
    private BitmapDrawable signatuerDrawable;



    public void saveSign(View view) {
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                signatuerBitamp = paletteview.buildBitmap();
//                signatuerDrawable = new BitmapDrawable(signatuerBitamp);
                //上传图片
                String path = PictureCut.saveBitmapToSdcard(signatuerBitamp);
                EventBus.getDefault().post(path);
                finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate( R.layout.activity_sign, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {

    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        switchToContentState();
    }
}
