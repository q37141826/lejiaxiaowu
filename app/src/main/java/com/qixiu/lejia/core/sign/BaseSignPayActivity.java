package com.qixiu.lejia.core.sign;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.common.PayUtils;
import com.qixiu.lejia.utils.ToastUtils;

/**
 * Created by Long on 2018/5/8
 * <pre>
 *     Base签约支付界面
 * </pre>
 */
@SuppressLint("Registered")
public abstract class BaseSignPayActivity extends BaseToolbarAct implements PayUtils.PayResultCallback {
    public static final String DATA="DATA";
    private PayUtils mPayUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPayUtils = new PayUtils();
        mPayUtils.setPayResultCallback(this);
        mPayUtils.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        mPayUtils.onDestroy();
        mPayUtils = null;
        super.onDestroy();
    }

    /**
     * 开始支付
     *
     * @param payWay 支付方式
     * @param roomId 房间ID
     * @param money  金额
     */
    @SuppressWarnings("unchecked")
    protected void startPay(@PayUtils.PayWay int payWay, String roomId, String money, int lease, int periods,
                            String firstPay, String monthlyPay) {
        mPayUtils.startPay(payWay, 1, roomId, money, lease, periods, firstPay, monthlyPay,
                null, null);
    }


    @Override
    public void onPaySuccess() {
        PaySuccessAct.start(this, getSignedId());
        finish();
    }

    @Override
    public void onPayFailure() {
        ToastUtils.showShort(this, PayUtils.PAY_FAILURE);
    }

    @Override
    public void onPayCancel() {
        ToastUtils.showShort(this, PayUtils.PAY_CANCEL);
    }


    protected abstract String getSignedId();
}
