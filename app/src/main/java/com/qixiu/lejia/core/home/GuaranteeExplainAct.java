package com.qixiu.lejia.core.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseToolbarAct;

/**
 * Created by Long on 2018/6/12
 */
public class GuaranteeExplainAct extends BaseToolbarAct {

    private static final String KEY_TYPE  = "TYPE";
    private static final String KEY_IMAGE = "IMAGE";

    public static void start(Context context, int type, @NonNull String image) {
        Intent starter = new Intent(context, GuaranteeExplainAct.class);
        starter.putExtra(KEY_TYPE, type);
        starter.putExtra(KEY_IMAGE, image);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_guarantee_explain);

        int type = getIntent().getIntExtra(KEY_TYPE, 0);
        if (type == 0) {
            setTitle(R.string.guarantee);
        } else if (type == 1) {
            setTitle(R.string.guarantee);
        } else {
            setTitle(R.string.guarantee);
        }

        String image = getIntent().getStringExtra(KEY_IMAGE);

        WebView webView = findViewById(R.id.image);

        int screenWidthDp = px2dp(this, getScreenWidth(this));

        String html = "<html>" +
                "<body>" +
                "   <style type=\"text/css\">  " +
                "  body{  " +
                "   margin:0px;  " +
                "  }   " +
                " </style>  " +
                "<img id=\"img\" src=\"" + image + "\" width=\"" + screenWidthDp + "\"/>" +
                "<script>" +
                " function getsize(){" +
                "  var img = document.getElementById(\"img\");" +
                "  javascript:stub.getNaturalSize(img.naturalWidth,img.naturalHeight);" +
                " }" +
                "</script>" +
                "</body>" +
                "</html>";

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:getsize()");
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void getNaturalSize(int imgWidth, int imgHeight) {
            }
        }, "stub");

        webView.loadData(html, "text/html", "utf-8");
    }

    // 获取屏幕宽度
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    // 将px转换成dp值
    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
