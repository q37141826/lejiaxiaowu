package com.qixiu.lejia.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.thirdparty.SdkConstants;
import com.qixiu.thirdparty.wx.WXPayEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Long on 2018/1/5
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SdkConstants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.d(TAG, "onReq: 微信请求支付");
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.d(TAG, "onResp: 微信支付回调" + resp.errCode);
        WXPayEvent.getInstance().onResp(resp);
        setResult(RESULT_OK);
        finish();
    }

}
