package com.qixiu.lejia.core.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.common.PayUtils;
import com.qixiu.lejia.utils.ToastUtils;

/**
 * Created by Long on 2018/6/5
 * <pre>
 *     Base生活服务支付界面
 * </pre>
 */
public abstract class BaseServicePayAct extends BaseWhiteStateBarActivity implements PayUtils.PayResultCallback {

    private PayUtils mPayUtils;

    //选择的支付方式
    @PayUtils.PayWay
    protected int mSelectedPayWay;

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

//    public void setPayListenner( PayUtils.PayResultCallback callback){
//        if(mPayUtils!=null){
//            mPayUtils.setPayResultCallback(callback);
//        }
//    }


    @Override
    public void onPaySuccess() {
        ToastUtils.showShort(this, PayUtils.PAY_SUCCESS);
        //返回支付成功
        setResult(RESULT_OK);
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


    /**
     * 开始支付
     *
     * @param payScene   缴费类型1是房租,2是水费,3是电费
     * @param roomId     房间ID
     * @param money      支付金额
     * @param rentBillId 房租账单ID(房租支付时传)
     * @param weBillId   水电账单ID(水电支付时传)
     */
    protected void startPay(int payScene, @NonNull String roomId, @NonNull String money,
                            @Nullable String rentBillId, @Nullable String weBillId) {
        mPayUtils.startPay(mSelectedPayWay, payScene, roomId, money, -1, -1,
                null, null, rentBillId, weBillId);
    }


    /**
     * 显示选择支付方式Dialog
     *
     * @param view 展示支付方式的View
     */
    protected void showPayWaysDialog(TextView view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_pay_way)
                .setItems(R.array.pay_ways, (dialog, which) -> {
                    mSelectedPayWay = which == 0 ? PayUtils.PayWay.ALI : PayUtils.PayWay.WX;
                    if (which == 0) {
                        view.setText(R.string.alipay);
                    } else {
                        view.setText(R.string.wx);
                    }
                }).show();
    }

}
