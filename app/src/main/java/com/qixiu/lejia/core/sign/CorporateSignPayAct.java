package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.CorporateSignFifthInfo;
import com.qixiu.lejia.beans.resp.CorporateSignResp;
import com.qixiu.lejia.common.PayUtils;
import com.qixiu.lejia.databinding.ActCorporateSignPayBinding;
import com.qixiu.lejia.utils.DatetimeConstants;

import java.util.Date;

/**
 * Created by Long on 2018/4/28
 * <pre>
 *     企业用户签约支付页
 * </pre>
 */
public class CorporateSignPayAct extends BaseSignPayActivity {

    private ActCorporateSignPayBinding mBinding;

    private String mRoomId;
    //签约ID
    private String mSignedId;

    public static void start(Context context) {
        Intent starter = new Intent(context, CorporateSignPayAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActCorporateSignPayBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //租期开始时间
        mBinding.startDate.setText(DateFormat.format(DatetimeConstants.YTD_CN, new Date()));

        //缴费明细点击
        mBinding.payDetails.setOnClickListener(v -> {
            if (mRoomId != null) {
                PayDetailsDialog.newInstance(1, mRoomId, -1, -1,"")//todo 这里在那时填写为1，然后看看测试怎么测试的
                        .show(getSupportFragmentManager());
            }
        });

        mBinding.pay.setOnClickListener(v -> {
            mBinding.pay.setEnabled(false);
            int checkedRadioButtonId = mBinding.payWays.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.wx_pay) {
                startPay(PayUtils.PayWay.WX);
            } else {
                startPay(PayUtils.PayWay.ALI);
            }
        });

        load();

    }

    @Override
    protected String getSignedId() {
        return mSignedId;
    }

    private void startPay(@PayUtils.PayWay int payWay) {
        if (mRoomId == null) {
            return;
        }
        CorporateSignFifthInfo info = mBinding.getInfo();
        if (info == null) {
            return;
        }
        super.startPay(payWay, mRoomId, info.getFirstPay(), -1, -1, info.getFirstPay(),
                info.getMonthlyPay());
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().corporateSignFifthStep(LoginStatus.getToken());
        call.enqueue(new RequestCallback<CorporateSignResp>() {
            @Override
            protected void onSuccess(CorporateSignResp resp) {
                handleResp(resp);
            }

            @Override
            protected void onFailure(ResponseError error) {
                Toast.makeText(CorporateSignPayAct.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResp(CorporateSignResp resp) {
        mBinding.setRoom(resp.getRoom());
        mBinding.setInfo(resp.getCorporateSignFifthInfo());

        mRoomId = resp.getRoom().getId();
        mSignedId = resp.getRoom().getSignedId();

    }

    @Override
    public void onPayFailure() {
        super.onPayFailure();
        mBinding.pay.setEnabled(true);
    }

    @Override
    public void onPaySuccess() {
        super.onPaySuccess();
        mBinding.pay.setEnabled(true);
    }

    @Override
    public void onPayCancel() {
        super.onPayCancel();
        mBinding.pay.setEnabled(true);
    }
}
