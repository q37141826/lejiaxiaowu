package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RatingBar;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.common.Events;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.utils.IntentUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Long on 2018/5/8
 */
public class PaySuccessAct extends BaseToolbarAct {

    private static final String KEY_SIGNED_ID = "SIGNED_ID";

    private String mSignedId;

    public static void start(Context context, String signedId) {
        Intent starter = new Intent(context, PaySuccessAct.class);
        starter.putExtra(KEY_SIGNED_ID, signedId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignedId = getIntent().getStringExtra(KEY_SIGNED_ID);

        setContentView(R.layout.act_pay_success);

        RatingBar ratingBar = findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            //评分
            score((int) rating);
        });

        //发送签约成功事件
        EventBus.getDefault().post(new Events.SignedSuccessEvent());
    }

    public void startSigned(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("id", mSignedId);
        String url = ApiConstants.buildUrl(ApiConstants.SIGN_DETAIL, params);
        WebActivity.start(this, getString(R.string.sign_detail), url);
        finish();
    }

    @SuppressWarnings("unchecked")
    public void startContact(View view) {
        call = AppApi.get().contactHousekeeper(mSignedId);
        call.enqueue(new RequestCallback<String>(this) {
            @Override
            protected void onSuccess(String contact) {
                IntentUtils.startDial(PaySuccessAct.this, contact);
                finish();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void score(int rating) {
        call = AppApi.get().payScore(LoginStatus.getToken(), mSignedId, rating);
        call.enqueue(new RequestCallback() {
            @Override
            protected void onSuccess(Object o) {
                //ignore
            }
        });
    }


}
