package com.qixiu.lejia.core.service.we;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.WePayStatus;
import com.qixiu.lejia.core.service.bill.BillAct;
import com.qixiu.lejia.databinding.ActWeBinding;

/**
 * Created by Long on 2018/5/30
 * <pre>
 *     水电费
 * </pre>
 */
public class WaterAndElectricityAct extends BaseWhiteStateBarActivity {

    private static final int REQ_CODE = 0x233;

    private ActWeBinding mBinding;

    public static void start(Context context) {
        Intent starter = new Intent(context, WaterAndElectricityAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        load();

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        mBinding = ActWeBinding.inflate(inflater);
        //noinspection ConstantConditions
        return mBinding.getRoot();
    }

    @Override
    protected void onContentViewCreated(View view) {
        mBinding.setActionHandler(o -> {
            WePayStatus item = mBinding.getItem();
            if (item == null) {
                return;
            }
            int action = (int) o;
            if (action == 1) {
                //水费详情
                WaterCostDetailAct.start(this, item.getWaterBillId(),
                        item.getWaterPayState(), REQ_CODE);
            } else {
                //电费详情
                ElectricityCostDetailAct.start(this, item.getElectricBillId(),
                        item.getElectricPayState(), REQ_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bill) {
            BillAct.start(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onReload() {
        load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            //房租缴费成功，刷新数据
            load();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().wePayStatus(LoginStatus.getToken());
        call.enqueue(new RequestCallback<WePayStatus>() {
            @Override
            protected void onSuccess(WePayStatus o) {
                switchToContentState();
                mBinding.setItem(o);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

}
