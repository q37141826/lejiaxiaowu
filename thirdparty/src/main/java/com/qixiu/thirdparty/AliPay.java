package com.qixiu.thirdparty;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by Long on 2018/1/5
 */
public class AliPay implements Runnable {

    private static final String TAG = "AliPay";

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private AliPayResultCallback callback;

    private Activity mActivity;
    private String   mOrder;

    public void setCallback(AliPayResultCallback callback) {
        this.callback = callback;
    }

    public void pay(Activity activity, String order) {
        mActivity = activity;
        mOrder = order;
        new Thread(this).start();
    }

    @Override
    public void run() {
        PayTask payTask = new PayTask(mActivity);
        Map<String, String> map = payTask.payV2(mOrder, false);
        PayResult payResult = new PayResult(map);
        Log.d(TAG, "run: " + payResult.toString());
        /*
         * 9000     订单支付成功
         * 8000     正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
         * 4000     订单支付失败
         * 6001    用户中途取消
         * 6002     网络连接出错
         * 6004     支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
         * 其它      其它支付错误
         */
        if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
            //支付成功，运行在子线程
            if (callback != null) {
                mHandler.post(() -> callback.onPaySuccess());
            }
        } else if ("6001".equals(payResult.getResultStatus())) {
            if (callback != null) {
                mHandler.post(() -> callback.onPayCancel());
            }
        } else {
            //支付失败
            if (callback != null) {
                mHandler.post(() -> callback.onPayFailure());
            }
        }
    }

    public interface AliPayResultCallback {

        void onPayFailure();

        void onPayCancel();

        void onPaySuccess();
    }


    private static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

}
