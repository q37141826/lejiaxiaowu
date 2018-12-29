package com.qixiu.lejia.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.lejia.utils.ToastUtils;
import com.qixiu.thirdparty.SdkConstants;
import com.qixiu.thirdparty.wx.WXLoginEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信分享/登录回调
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SdkConstants.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        Logger.d(TAG, "onResp: baseresp.getTypeText = " + resp.getType());
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_USER_CANCEL:
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (resp.getType() == SdkConstants.WX_TYPE_LOGIN) {
                    //登录失败
                    handleLoginFail();
                } else {
                    //分享失败
                    handleShareFail();
                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == SdkConstants.WX_TYPE_LOGIN) {
                    handleLoginSuccess(((SendAuth.Resp) resp).code);
                } else {
                    handleShareSuccess();
                }
                break;
        }
        //退出
        finish();
    }

    private void handleLoginFail() {
        ToastUtils.showShort(this, R.string.login_failure);
        WXLoginEvent.getInstance().authFailure();
    }

    private void handleLoginSuccess(String code) {
        WXLoginEvent.getInstance().authSuccess(code);
    }

    private void handleShareFail() {
        ToastUtils.showShort(this, R.string.share_failure);
    }

    private void handleShareSuccess() {
        ToastUtils.showShort(this, R.string.share_success);
    }

}