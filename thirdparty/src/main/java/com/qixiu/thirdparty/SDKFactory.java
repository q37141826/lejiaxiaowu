package com.qixiu.thirdparty;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * Created by Long on 2018/5/8
 */
public class SDKFactory {

    public static IWXAPI createWXApi(Context context) {
        return WXAPIFactory.createWXAPI(context, SdkConstants.WX_APP_ID, false);
    }

    public static Tencent createTencent(Context context) {
        return Tencent.createInstance(SdkConstants.QQ_APP_ID, context);
    }

}
