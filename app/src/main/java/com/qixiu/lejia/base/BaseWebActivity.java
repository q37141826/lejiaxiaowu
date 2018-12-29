package com.qixiu.lejia.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.utils.Logger;


/**
 * Created by Long on 2017/1/5
 */
@SuppressLint("Registered")
public class BaseWebActivity extends BaseLoadIndicatorAct {

    private static final String TAG = "BaseWebActivity";

    private TextView titleView;
    private ProgressBar progressBar;

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doAfterSetContent();

        setContentView(R.layout.abc_web_act);

        //设置support actionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置禁止显示toolbar自己的标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //设置标题
        titleView = findViewById(R.id.title);
        titleView.setSelected(true);
        titleView.setText(getTitle());

        progressBar = findViewById(R.id.progress);

        webView = findViewById(R.id.web_view);

        setupWebView();

    }


    @SuppressLint("SetJavaScriptEnabled")
    protected void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);

        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(webViewClient);

    }


    @Override
    public void setTitle(@StringRes int resID) {
        titleView.setText(resID);
    }

    protected void setTitle(String title) {
        titleView.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    protected void doAfterSetContent() {
    }

    protected WebView getWebView() {
        return webView;
    }

    /**
     * 加载网页
     *
     * @param url url
     */
    protected void load(String url) {
        webView.loadUrl(url);
    }


    //-----------------------------WebChromeClient-------------------------------------------

    protected void onProgressChanged(WebView view, int newProgress) {
        progressBar.setProgress(newProgress);
        progressBar.setVisibility(newProgress >= 70 ? View.GONE : View.VISIBLE);

    }

    protected boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        result.cancel();
        new AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
        return true;
    }

    //---------------------------------WebChromeClient---------------------------------------


    //---------------------------------WebViewClient---------------------------------------

    protected boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.d(TAG, "shouldOverrideUrlLoading: " + url);
        if (url.startsWith("http")) {
            view.loadUrl(url);
        }
        return true;
    }


    protected void onReceivedHttpError(WebView view, WebResourceRequest request,
                                       WebResourceResponse errorResponse) {
        Logger.d(TAG, "onReceivedHttpError: " + request.getUrl().toString());
    }


    protected void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Logger.d(TAG, "onReceivedError: " + "errorCode====" + errorCode + "--------failingUrl====" + failingUrl +
                "--------description ====" + description);

        /*String htmlData = "<html><body><div align=\"center\" >This is the description for the load fail : "
                + description + "</div></body></html>";
        view.loadData(htmlData, "text/html", "utf-8");*/

    }


    protected void onPageStarted(WebView view, String url, Bitmap favicon) {
        Logger.d(TAG, "onPageStarted: " + url);
    }


    protected void onPageFinished(WebView view, String url) {
        Logger.d(TAG, "onPageFinished: " + url);
        //refreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }


    //---------------------------------WebViewClient---------------------------------------


    private final WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            BaseWebActivity.this.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return BaseWebActivity.this.onJsAlert(view, url, message, result);
        }

    };

    private final WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return BaseWebActivity.this.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                        WebResourceResponse errorResponse) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BaseWebActivity.this.onReceivedHttpError(view, request, errorResponse);
            }
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            BaseWebActivity.this.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            BaseWebActivity.this.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            BaseWebActivity.this.onPageFinished(view, url);
        }

    };


}
