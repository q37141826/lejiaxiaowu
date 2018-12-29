package com.qixiu.thirdparty.wx;

import android.support.annotation.IntDef;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;

/**
 * Created by Long on 2018/1/11
 */
public class WXPayEvent extends Observable implements IWXAPIEventHandler {

    private static WXPayEvent instance;

    public static WXPayEvent getInstance() {
        if (instance == null) {
            instance = new WXPayEvent();
        }
        return instance;
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //never call
    }

    @Override
    public void onResp(BaseResp resp) {
        int result;
        if (resp.errCode == 0) {
            result = PayResult.SUCCESS;
        } else if (resp.errCode == -2) {
            result = PayResult.CANCEL;
        } else {
            result = PayResult.ERROR;
        }
        setChanged();
        notifyObservers(result);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PayResult.SUCCESS, PayResult.CANCEL, PayResult.ERROR})
    public @interface PayResult {
        int SUCCESS = 1;
        int CANCEL = 0;
        int ERROR = -1;
    }

}
