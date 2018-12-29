package com.qixiu.lejia.common;

import android.app.Activity;
import android.support.annotation.IntDef;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.WXPayInfo;
import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.lejia.mvp.LoadIndicatorView;
import com.qixiu.lejia.utils.ToastUtils;
import com.qixiu.thirdparty.AliPay;
import com.qixiu.thirdparty.SDKFactory;
import com.qixiu.thirdparty.wx.WXPayEvent;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;

/**
 * Created by Long on 2018/6/5
 */
public class PayUtils implements AliPay.AliPayResultCallback, Observer {

    public static final int PAY_CANCEL  = R.string.pay_cancel;
    public static final int PAY_FAILURE = R.string.pay_failure;
    public static final int PAY_SUCCESS = R.string.pay_success;

    private Activity          mHostAct;
    private LoadIndicatorView mIndicatorView;

    //支付宝支付工具
    private AliPay mAliPay;

    //网络请求支付数据
    private Call mCall;

    //转交支付结果到调用方
    private PayResultCallback mCallback;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PayWay.WX, PayWay.ALI})
    public @interface PayWay {

        int WX  = 1;
        int ALI = 2;
    }

    public void onCreate(Activity host) {
        mHostAct = host;
        if (host instanceof LoadIndicatorView) {
            mIndicatorView = (LoadIndicatorView) host;
        }
        WXPayEvent.getInstance().addObserver(this);
    }

    public void onDestroy() {
        CallUtil.cancel(mCall);
        WXPayEvent.getInstance().deleteObserver(this);
    }

    public void setPayResultCallback(PayResultCallback callback) {
        mCallback = callback;
    }

    /**
     * 支付
     *
     * @param payWay     支付方式1是微信,2是支付宝
     * @param payScene   缴费类型1是房租,2是水费,3是电费
     * @param money      支付金额
     * @param roomId     用户缴费的房间id
     * @param lease      租期,签约时传入
     * @param periods    付款方式,1是押一付一,2是押一付三,3是半年付,4是全年付
     * @param firstPay   首次支付金额
     * @param monthlyPay 其余每次支付金额
     * @param rentBillId 房租账单id,如果用户是在房租账单页面支付,传入该id
     * @param weBillId   水电费账单id,如果用户是在水电费页面支付,传入该id
     */
    @SuppressWarnings("unchecked")
    public void startPay(@PayWay int payWay, int payScene, String roomId, String money, int lease, int periods,
                         String firstPay, String monthlyPay, String rentBillId, String weBillId) {
        if (payWay == PayWay.WX) {
            mCall = AppApi.get().wxPay(LoginStatus.getToken(), 1, money, payScene,
                    roomId, lease, periods, firstPay, monthlyPay, rentBillId, weBillId);
        } else {
            mCall = AppApi.get().aliPay(LoginStatus.getToken(), 2, money, payScene,
                    roomId, lease, periods, firstPay, monthlyPay, rentBillId, weBillId);
        }

        mCall.enqueue(new RequestCallback(mIndicatorView) {
            @Override
            protected void onSuccess(Object o) {
                if (o instanceof WXPayInfo) {
                    WXPayInfo info = (WXPayInfo) o;
                    startWXPay(WXPayInfo.toPayReq(info));
                } else if (o instanceof String) {
                    startAliPay((String) o);
                }
            }

            @Override
            protected void onFailure(ResponseError error) {
                super.onFailure(error);
                onPayFailure();
            }
        });
    }


    private void startWXPay(PayReq payReq) {
        boolean result = payReq.checkArgs();
        if (!result) {
            ToastUtils.showShort(mHostAct, "服务器返回数据错误");
            return;
        }
        SDKFactory.createWXApi(mHostAct)
                .sendReq(payReq);
    }

    private void startAliPay(String sign) {
        if (mAliPay == null) {
            mAliPay = new AliPay();
            mAliPay.setCallback(this);
        }
        mAliPay.pay(mHostAct, sign);
    }

    @Override
    public void onPayFailure() {
        if (mCallback != null) {
            mCallback.onPayFailure();
        }
    }

    @Override
    public void onPaySuccess() {
        if (mCallback != null) {
            mCallback.onPaySuccess();
        }
    }

    @Override
    public void onPayCancel() {
        if (mCallback != null) {
            mCallback.onPayCancel();
        }
    }

    /**
     * 微信支付回调
     *
     * @param o   微信支付被观察者
     * @param arg 支付状态 {@link WXPayEvent.PayResult}
     */
    @Override
    public void update(Observable o, Object arg) {
        int payResult = (int) arg;
        switch (payResult) {
            case WXPayEvent.PayResult.SUCCESS:
                onPaySuccess();
                break;
            case WXPayEvent.PayResult.CANCEL:
                onPayCancel();
                break;
            case WXPayEvent.PayResult.ERROR:
                onPayFailure();
                break;
        }
    }

    public interface PayResultCallback {
        void onPaySuccess();

        void onPayFailure();

        void onPayCancel();
    }


    /**
     * 获取支付方式文字表达
     *
     * @param payWay 1-微信 2-支付宝 3-线下
     * @return 文字表达式
     */
    public static String getPayWay(int payWay) {
        if (payWay <= 0) {
            return "";
        }
        int strRes = 0;
        switch (payWay) {
            case 2:
                strRes = R.string.wx;
                break;
            case 1:
                strRes = R.string.alipay;
                break;
            case 3:
                strRes = R.string.offline_pay;
                break;
        }
        return AppContext.getContext().getString(strRes);
    }

}
