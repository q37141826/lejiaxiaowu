package com.qixiu.lejia.core.service.we;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiu.adapter.ItemActionHandler;
import com.qixiu.lejia.BR;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.WECostDetail;
import com.qixiu.lejia.core.service.BaseServicePayAct;
import com.qixiu.lejia.databinding.ActElectricityPaidBinding;
import com.qixiu.lejia.databinding.ActElectricityUnpaidBinding;
import com.qixiu.widget.MultiStateLayout;

/**
 * Created by Long on 2018/6/6
 */
public class ElectricityCostDetailAct extends BaseServicePayAct implements WECostDetailContract.View {

    private static final String KEY_BILL_ID = "BILL_ID";
    private static final String KEY_PAY_STATUS = "PAY_STATUS";

    private ViewDataBinding mBinding;

    private String mExtraBillId;
    private int mExtraPayStatus;

    private WECostDetailContract.Presenter mPresenter;
    private RelativeLayout relativeLayout_discount;
    private TextView btn;
    private RelativeLayout relativeLateFee;

    public static void start(Activity context, @Nullable String billId, int payStatus, int requestCode) {
        Intent starter = new Intent(context, ElectricityCostDetailAct.class);
        starter.putExtra(KEY_BILL_ID, billId);
        starter.putExtra(KEY_PAY_STATUS, payStatus);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mExtraBillId = getIntent().getStringExtra(KEY_BILL_ID);
        mExtraPayStatus = getIntent().getIntExtra(KEY_PAY_STATUS, 0);
        super.onCreate(savedInstanceState);

        if (mExtraPayStatus == 0 || (mExtraBillId == null || TextUtils.isEmpty(mExtraBillId))) {
            switchToEmptyState();
            return;
        }

        mPresenter = new WECostDetailPresenter();
        mPresenter.onAttach(this);
        mPresenter.onLoad(mExtraBillId);

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        View empty = inflater.inflate(R.layout.abc_bill_empty, null, false);
        multiStateLayout.setViewForState(empty, MultiStateLayout.VIEW_STATE_EMPTY);

        if (mExtraPayStatus == 1) {
            mBinding = ActElectricityPaidBinding.inflate(inflater);
            View rootView = mBinding.getRoot();
            relativeLayout_discount = rootView.findViewById(R.id.relative_discount);
            relativeLateFee = rootView.findViewById(R.id.relativeLateFee);
        } else {
            mBinding = ActElectricityUnpaidBinding.inflate(inflater);
            View rootView = mBinding.getRoot();
            relativeLayout_discount = rootView.findViewById(R.id.relative_discount);
            relativeLateFee = rootView.findViewById(R.id.relativeLateFee);

            //noinspection ConstantConditions
            mBinding.setVariable(BR.actionHandler, (ItemActionHandler<Object>) o -> {
                int scene = (int) o;
                if (scene == 0) {
                    //选择支付方式
                    ActElectricityUnpaidBinding b = (ActElectricityUnpaidBinding) mBinding;
                    showPayWaysDialog(b.payWays);
                } else {
                    //立即支付
                    doPay();
                }
            });
        }
        //noinspection ConstantConditions
        return mBinding.getRoot();
    }

    @Override
    protected void onContentViewCreated(View view) {
    }

    @Override
    protected void onReload() {
        mPresenter.onLoad(mExtraBillId);
    }

    @Override
    public void onLoadFailure() {
        switchToErrorState();
    }

    @Override
    public void onLoadSuccess(WECostDetail resp) {
        switchToContentState();
        mBinding.setVariable(BR.resp, resp);
        if (TextUtils.isEmpty(resp.getHy_reduce_money())) {
            relativeLayout_discount.setVisibility(View.GONE);
        } else {
            relativeLayout_discount.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(resp.getLateFee())||"0".equals(resp.getLateFee())){
            relativeLateFee.setVisibility(View.GONE);
        }else {
            relativeLateFee.setVisibility(View.VISIBLE);
        }
    }

    private void doPay() {
        btn = (TextView) mBinding.getRoot().findViewById(R.id.btn_pay);
        btn.setEnabled(false);
        ActElectricityUnpaidBinding binding = (ActElectricityUnpaidBinding) mBinding;
        WECostDetail item = binding.getResp();
        if (item == null) {
            return;
        }
        startPay(3, item.getRoomId(), item.getSumAll(), null, mExtraBillId);
    }


    @Override
    public void onPayCancel() {
        super.onPayCancel();
        btn.setEnabled(true);
    }

    @Override
    public void onPayFailure() {
        super.onPayFailure();
        btn.setEnabled(true);
    }

    @Override
    public void onPaySuccess() {
        super.onPaySuccess();
        btn.setEnabled(true);
    }
}
