package com.qixiu.thirdparty.wx;

import android.content.Context;

import com.qixiu.thirdparty.SdkConstants;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Long on 2018/5/3
 */
public class WXLoginHelper {

    /**
     * 注册到微信
     */
    public static void registerToWX(Context context){
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, SdkConstants.WX_APP_ID);
        wxapi.registerApp(SdkConstants.WX_APP_ID);
    }

}
