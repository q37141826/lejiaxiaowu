package com.qixiu.lejia.core.service.rent;

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
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.RentDetail;
import com.qixiu.lejia.core.service.BaseServicePayAct;
import com.qixiu.lejia.databinding.ActRentDetailPaidBinding;
import com.qixiu.lejia.databinding.ActRentDetailUnpaidBinding;
import com.qixiu.widget.MultiStateLayout;

/**
 * Created by Long on 2018/5/30
 */
public class RentDetailAct extends BaseServicePayAct {

    private static final String KEY_BILL_ID = "BILL_ID";
    private static final String KEY_PAY_STATUS = "PAY_STATUS";

    private ViewDataBinding mBinding;

    //房租账单ID
    private String mExtraBillId;
    private int mExtraPayStatus;
    //折扣
    RelativeLayout relativeLayout_discount;
    private TextView textViewGotoPay;
    private RelativeLayout relativeLateFee;

    public static void start(Activity context, @Nullable String billId, int payStatus, int requestCode) {
        Intent starter = new Intent(context, RentDetailAct.class);
        starter.putExtra(KEY_BILL_ID, billId);
        starter.putExtra(KEY_PAY_STATUS, payStatus);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mExtraBillId = getIntent().getStringExtra(KEY_BILL_ID);
        mExtraPayStatus = getIntent().getIntExtra(KEY_PAY_STATUS, 0);
        super.onCreate(savedInstanceState);
        if (mExtraBillId == null || TextUtils.isEmpty(mExtraBillId)) {
            switchToEmptyState();
            return;
        }
        loadRentDetail();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        View empty = inflater.inflate(R.layout.abc_bill_empty, null, false);
        multiStateLayout.setViewForState(empty, MultiStateLayout.VIEW_STATE_EMPTY);

        if (mExtraPayStatus == 1) {
            //已缴费
            mBinding = ActRentDetailPaidBinding.inflate(inflater);
            View rootView = mBinding.getRoot();
            relativeLayout_discount = rootView.findViewById(R.id.relative_discount);
            relativeLateFee = rootView.findViewById(R.id.relativeLateFee);
        } else {
            //未缴费
            mBinding = ActRentDetailUnpaidBinding.inflate(inflater);
            View rootView = mBinding.getRoot();
            relativeLayout_discount = rootView.findViewById(R.id.relative_discount);
            relativeLateFee = rootView.findViewById(R.id.relativeLateFee);


            //noinspection ConstantConditions
            textViewGotoPay = rootView.findViewById(R.id.textViewGotoPay);
            mBinding.setVariable(BR.actionHandler, (ItemActionHandler<Object>) o -> {
                int scene = (int) o;
                if (scene == 0) {
                    //选择支付方式
                    ActRentDetailUnpaidBinding b = (ActRentDetailUnpaidBinding) mBinding;
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
        loadRentDetail();
    }

    private void doPay() {
        textViewGotoPay.setEnabled(false);
        ActRentDetailUnpaidBinding binding = (ActRentDetailUnpaidBinding) mBinding;
        RentDetail item = binding.getItem();
        if (item == null) {
            return;
        }
        startPay(1, item.getRoomId(), item.getTotal(), mExtraBillId, null);
    }


    @SuppressWarnings("unchecked")
    private void loadRentDetail() {
        call = AppApi.get().rentBillDetail(mExtraBillId, LoginStatus.getToken());
        call.enqueue(new RequestCallback<RentDetail>() {
            @Override
            protected void onSuccess(RentDetail rentDetail) {
                switchToContentState();
                mBinding.setVariable(BR.item, rentDetail);
                if (TextUtils.isEmpty(rentDetail.getPa_reduce_money())) {
                    relativeLayout_discount.setVisibility(View.GONE);
                } else {
                    relativeLayout_discount.setVisibility(View.VISIBLE);
                }
                if(TextUtils.isEmpty(rentDetail.getLateFee())||"0".equals(rentDetail.getLateFee())||"0.00".equals(rentDetail.getLateFee())){
                    relativeLateFee.setVisibility(View.GONE);
                }else {
                    relativeLateFee.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });

    }


    @Override
    public void onPayCancel() {
        super.onPayCancel();
        textViewGotoPay.setEnabled(true);

    }

    @Override
    public void onPaySuccess() {
        super.onPaySuccess();
        textViewGotoPay.setEnabled(true);

    }

    @Override
    public void onPayFailure() {
        super.onPayFailure();
        textViewGotoPay.setEnabled(true);
    }
}
