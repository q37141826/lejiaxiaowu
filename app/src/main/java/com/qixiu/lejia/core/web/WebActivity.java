package com.qixiu.lejia.core.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWebActivity;
import com.qixiu.lejia.beans.ImagesPreview;
import com.qixiu.lejia.beans.JsParam;
import com.qixiu.lejia.beans.ShareInfo;
import com.qixiu.lejia.beans.WXPayInfo;
import com.qixiu.lejia.common.Events;
import com.qixiu.lejia.common.SharePanel;
import com.qixiu.lejia.core.me.points.MyPrizeAct;
import com.qixiu.lejia.core.preview.ImagesPreviewAct;
import com.qixiu.lejia.core.service.rent.RentAct;
import com.qixiu.lejia.core.service.we.WaterAndElectricityAct;
import com.qixiu.lejia.core.sign.AuthenticationAct;
import com.qixiu.lejia.core.sign.CorporateSignPayAct;
import com.qixiu.lejia.core.sign.OfflineAffirmAct;
import com.qixiu.lejia.core.sign.PaySuccessAct;
import com.qixiu.lejia.core.sign.PersonalSignPayAct;
import com.qixiu.lejia.core.sign.UserType;
import com.qixiu.lejia.utils.IntentUtils;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.lejia.utils.TextDrawableUtils;
import com.qixiu.lejia.utils.ToastUtil;
import com.qixiu.lejia.utils.ToastUtils;
import com.qixiu.tbslib.DownLoadFileUtils;
import com.qixiu.thirdparty.AliPay;
import com.qixiu.thirdparty.SDKFactory;
import com.qixiu.thirdparty.wx.WXPayEvent;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import okhttp3.Call;

/**
 * Created by Long on 2018/4/28
 */
public class WebActivity extends BaseWebActivity implements AliPay.AliPayResultCallback, Observer {
    OKHttpRequestModel okHttpRequestModel;
    public static final int ONE = 1, TWO = 2;
    private int period = ONE;

    private static final String TAG = "WebActivity";

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_URL = "URL";
    private static final String IS_SIGN = "IS_SIGN";
    private static final String IS_SHARE = "IS_SHARE";
    private static final String CONTENT = "CONTENT";
    private static final String IMAGE = "IMAGE";
    private static final String KEY_MENU_RES = "MENU_RES";

    private String mUrl;

    private AliPay mAliPay;

    private String mSignedId;
    private boolean isSign = false;
    private boolean isShare = false;
    private String title;
    private String content;
    private String image;


    public Context getContext() {
        return this;
    }

    public static void start(Context context, String title, String url) {
//        Intent starter = new Intent(context, WebActivity.class);
//        starter.putExtra(KEY_TITLE, title);
//        starter.putExtra(KEY_URL, url);
//        context.startActivity(starter);
        start(context, title, url, false);
    }

    public static void start(Context context, String title, String url, boolean isSign) {
//        Intent starter = new Intent(context, WebActivity.class);
//        starter.putExtra(KEY_TITLE, title);
//        starter.putExtra(KEY_URL, url);
//        starter.putExtra(IS_SIGN, isSign);
        start(context, title, url, isSign, false, "", "");
    }

    public static void start(Context context, String title, String url, boolean isSign, boolean isShare, String content, String image) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(KEY_TITLE, title);
        starter.putExtra(KEY_URL, url);
        starter.putExtra(IS_SIGN, isSign);
        starter.putExtra(IS_SHARE, isShare);
        starter.putExtra(CONTENT, content);
        starter.putExtra(IMAGE, image);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        WXPayEvent.getInstance().addObserver(this);

        //JsBridge
        getWebView().addJavascriptInterface(new JsBridge(), "JsInterface");

        title = getIntent().getStringExtra(KEY_TITLE);
        setTitle(title);
        isSign = getIntent().getBooleanExtra(IS_SIGN, false);
        isShare = getIntent().getBooleanExtra(IS_SHARE, false);//在onPrepareOptionsMenu这个方法中进行是否展示分享选项
        mUrl = getIntent().getStringExtra(KEY_URL);
        content = getIntent().getStringExtra(CONTENT);
        image = getIntent().getStringExtra(IMAGE);
        if (isSign) {
            String baseUrl = "file://" + mUrl;
            getWebView().loadDataWithBaseURL(baseUrl, mUrl, "text/html", "utf-8", null);
        } else {
            load(mUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (title != null) {
            if (title.contains("签约详情")) {
                TextView textViewOtherRight = findViewById(R.id.textViewOtherRight);
                textViewOtherRight.setVisibility(View.VISIBLE);
                textViewOtherRight.setText("");
                TextDrawableUtils.setLeftImage(getContext(), textViewOtherRight, R.mipmap.my_gj2x);
                textViewOtherRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转拨号界面
                        startDial(content);
                    }
                });
            } else {
                getMenuInflater().inflate(R.menu.menu_refresh, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            //刷新页面
            getWebView().reload();
        }
        if (item.getItemId() == R.id.share) {
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setShareUrl(mUrl);
            shareInfo.setTitle(title);
            shareInfo.setShareImageUrl(image);
            shareInfo.setContent(content);
            //显示分享面板
            SharePanel.newInstance(shareInfo).show(getSupportFragmentManager());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void doAfterSetContent() {
        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //6.0以上使用黑色状态栏图标
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        WXPayEvent.getInstance().deleteObserver(this);
        super.onDestroy();
    }


    //---------------------------------------------------------------------------

    private class JsBridge {

        /**
         * 跳转页面
         *
         * @param json 页面参数
         */
        @JavascriptInterface
        public void pushCommonPage(String json) {
            //todo这里切换一起还是二期
            if (json.contains("合同")) {
                DocumentBean bean = new Gson().fromJson(json, DocumentBean.class);
                Map<String, String> map = new HashMap<>();
                map.put("sd_id", bean.getSd_id());
//                map.put("sd_id", "12106");
                okHttpRequestModel = new OKHttpRequestModel(new OKHttpUIUpdataListener() {
                    @Override
                    public void onSuccess(Object data, int i) {
                        if (data instanceof BaseBean) {
                            BaseBean bean = (BaseBean) data;
                            String url = bean.getO().toString();
                            url.replace("\\", "");
                            String fileName = "查看合同";
//                            FilePreviewActivity.actionStart(getContext(), url, fileName, FilePreviewActivity.class);//这个方法打开DOC文件
                            DownLoadFileUtils.InitFile.callFile(url, new DownLoadFileUtils.FileCallBack() {
                                @Override
                                public void callBack(String path) {
                                    Intent intent = PdfHttpDownloader.getPdfFileIntent(path);
                                    getContext().startActivity(intent);//这个方法选项少一些
//                                    DownLoadFileUtils.openFiles(path,getContext());
//                                    PdfFilePrefviewActivity.start(getContext(),url);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onFailure(C_CodeBean c_codeBean) {
                        ToastUtil.toast(c_codeBean.getM());
                    }
                });
                okHttpRequestModel.okhHttpPost("http://hoperf.whtkl.cn//Home/Life/downloadContract", map, new BaseBean());
            } else {
                period = ONE;
                WebActivity.this.startActivity(json);
            }
        }


        @JavascriptInterface
        public void pushCommonPage1(String json) {
            //todo这里切换一起还是二期
            period = TWO;
            WebActivity.this.startActivity(json);
        }


        //频繁点击会造成订单生成错误
        long payTime = 0;

        @JavascriptInterface
        public void goPay(String json) {
            if (System.currentTimeMillis() - payTime <= 1000) {
                return;
            }
            payTime = System.currentTimeMillis();
            Logger.d(TAG, "pay: " + json);
            try {
                JSONObject obj = new JSONObject(json);
                int status = obj.getInt("c");
                String msg = obj.getString("m");
                String signedId = null;
                if (obj.has("sd_id")) {
                    signedId = obj.getString("sd_id");
                }
                if (status != 1) {
                    showToast(msg);
                    return;
                }
                //判断支付类型
                if (msg.contains("微信")) {
                    //微信支付
                    JSONObject o = obj.getJSONObject("o");
                    WXPayInfo wxPayInfo = WXPayInfo.formJson(o);
                    if (wxPayInfo == null) {
                        showToast("服务器返回微信支付信息错误!");
                        return;
                    }
                    PayReq payReq = WXPayInfo.toPayReq(wxPayInfo);
                    WebActivity.this.startWxPay(payReq, signedId);
                } else {
                    String aliPaySign = obj.getString("o");
                    if (TextUtils.isEmpty(aliPaySign)) {
                        showToast("服务器返回支付宝支付信息错误!");
                        return;
                    }
                    WebActivity.this.startAliPay(aliPaySign, signedId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * Js调用拨号
         *
         * @param JSON 电话号码
         */
        @JavascriptInterface
        public void phoneNumber(String JSON) {
            Logger.d(TAG, "phoneNumber: " + JSON);
            String number = null;
            String sd_id = null;
            try {
                JSONObject obj = new JSONObject(JSON);
                number = obj.getString("phoneNum");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject obj = new JSONObject(JSON);
                sd_id = obj.getString("sd_id");
            } catch (Exception e) {

            }
            if (!TextUtils.isEmpty(sd_id)) {
                String finalSd_id = sd_id;
                okHttpRequestModel = new OKHttpRequestModel(new OKHttpUIUpdataListener() {
                    @Override
                    public void onSuccess(Object data, int i) {
                        if (data instanceof MaintainBean) {
                            MaintainBean bean = (MaintainBean) data;
                            PersonalSignPayAct.start(WebActivity.this, bean.getO().getSd_endtime(), finalSd_id);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onFailure(C_CodeBean c_codeBean) {

                    }
                });
                Map<String, String> map = new HashMap<>();
                map.put("uid", LoginStatus.getToken());
                okHttpRequestModel.okhHttpPost(BuildConfig.BASE_URL + "/Home/Sign/Renewal", map, new MaintainBean());
                return;
            }
            if (TextUtils.isEmpty(number)) {
                showToast("暂无联系方式");
                return;
            }
            WebActivity.this.startDial(number);
        }

        @JavascriptInterface
        public void contractImmediately(String json) {
            WebActivity.this.startSign(json);
        }

        @JavascriptInterface
        public void imageBtn(String json) {
            WebActivity.this.imagesPreview(json);
        }

        @JavascriptInterface
        public void pushBtn(String json) {
            Logger.d(TAG, "pushBtn: " + json);
        }

        @JavascriptInterface
        public void popBtn(String json) {
            Logger.d(TAG, "popBtn: " + json);
            finish();
        }

        @JavascriptInterface
        public void enlistBtn(String json) {
            Logger.d(TAG, "enlistBtn: " + json);
            MyPrizeAct.start(WebActivity.this);
        }

    }


    //---------------------------------------------------------------------------


    private void startSign(String json) {
        Logger.d(TAG, "contractImmediately: 立即签约---" + json);
        AuthenticationAct.start(this, UserType.PERSONAL, "");
    }

    //页面跳转
    private void startActivity(String json) {
        Logger.d(TAG, "startActivity: 传参 == " + json);
        JsParam param = JsParam.of(json);
        if (param == null) {
            showToast("服务器返回数据错误");
            return;
        }

        Logger.d(TAG, "startActivity: param == " + param.toString());

        String url = ApiConstants.buildUrl(param.getLink(), null);
        URLParser urlParser = URLParser.fromURL(url);
        Map<String, String> map = null;
        try {
            map = urlParser.compile()
                    .toMap();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String title = param.getTitle();

        if ("签约详情".equals(title)) {
            //获取签约状态type
            // type==3 待支付状态,跳转去支付页面
            if (map != null && map.containsKey("type")) {
                String type = map.get("type");
                if ("3".equals(type)) {
                    handleSignedDetail(map);
                } else {
                    Phonebean phonebean=new Gson().fromJson(json,Phonebean.class);
                    start(this, title, url, isSign, false, phonebean.getSubPhone(), "");
                }
            }
        } else if ("房租".equals(title)) {
            RentAct.start(this);
        } else if ("水电费".equals(title)) {
            WaterAndElectricityAct.start(this);
        } else {
            if (period == TWO) {
                url = url.replace("#", "ljhouse.app0.001");
                start(this, title, url.replace("http://h.", "http://lj."));//二期的要这样替换一下  旧的就不用了
            } else {
                start(this, title, url);//二期的要这样替换一下  旧的就不用了
            }
        }
//ljhouse.app0.001  url把#替换一下
    }

    //签约详情
    private void handleSignedDetail(Map<String, String> map) {
        //用户类型 0普通 1企业
        int userType = 0;
        if (map.containsKey("user_enterprise")) {
            userType = Integer.parseInt(map.get("user_enterprise"));
        }
        if (userType == 0) {
            OfflineAffirmAct.start(this);
        } else {
            CorporateSignPayAct.start(this);
        }
    }

    //图片预览
    private void imagesPreview(String json) {
        Logger.d(TAG, "imageBtn: " + json);
        //图片浏览
        ImagesPreview imagesPreview = ImagesPreview.of(json);
        if (imagesPreview != null) {
            ImagesPreviewAct.start(this, imagesPreview.getImages(),
                    imagesPreview.getStartPos(), null);
        }
    }

    private void startDial(String number) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("是否前往拨号界面?")
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        IntentUtils.startDial(this, number))
                .setNegativeButton(android.R.string.cancel, null)
                .show());
    }


    //----------------------------pay----------------------------

    private void startWxPay(PayReq payReq, String signedId) {
        mSignedId = signedId;
        boolean result = payReq.checkArgs();
        if (!result) {
            showToast("服务器返回数据错误");
            return;
        }
        SDKFactory.createWXApi(this)
                .sendReq(payReq);
    }

    private void startAliPay(String aliPaySign, String signedId) {
        mSignedId = signedId;
        if (mAliPay == null) {
            mAliPay = new AliPay();
            mAliPay.setCallback(this);
        }
        mAliPay.pay(this, aliPaySign);
    }

    @Override
    public void onPayFailure() {
        ToastUtils.showShort(this, R.string.pay_failure);
    }

    @Override
    public void onPaySuccess() {
        ToastUtils.showShort(this, R.string.pay_success);
        handlePaySuccess();
    }

    @Override
    public void onPayCancel() {
        ToastUtils.showShort(this, R.string.pay_cancel);
    }

    @Override
    public void update(Observable o, Object arg) {
        int payResult = (int) arg;
        switch (payResult) {
            case WXPayEvent.PayResult.SUCCESS:
                handlePaySuccess();
                break;
            case WXPayEvent.PayResult.CANCEL:
                onPayCancel();
                break;
            case WXPayEvent.PayResult.ERROR:
                onPayFailure();
                break;
        }
    }

    private void handlePaySuccess() {
        if (mSignedId != null) {
            PaySuccessAct.start(this, mSignedId);
            finish();
        } else {
            EventBus.getDefault().post(new Events.H5PaySuccessEvent());
            finish();
        }
    }

    @Subscribe
    public void paySuccessEvent(Events.H5PaySuccessEvent event) {
        String url = getWebView().getUrl();
        Logger.d(TAG, "paySuccessEvent: " + url);
        if (url.contains(ApiConstants.RENT) || url.contains(ApiConstants.HYDROELECTRIC)) {
            getWebView().reload();
        }
    }

    private static void showToast(String message) {
        ToastUtils.showShort(AppContext.getContext(), message);
    }

    //------------------------------pay----------------------------

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            if (isShare) {
                menu.findItem(R.id.share).setVisible(true);
            } else {
                menu.findItem(R.id.share).setVisible(false);
            }
        } catch (Exception e) {

        }
        return super.onPrepareOptionsMenu(menu);
    }


}
