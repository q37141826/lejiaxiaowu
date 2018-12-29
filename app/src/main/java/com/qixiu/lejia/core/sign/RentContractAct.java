package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.UserLevel;
import com.qixiu.lejia.databinding.ActRentContractBinding;
import com.qixiu.lejia.utils.Timer;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     第三部 租期合约
 * </pre>
 */
public class RentContractAct extends BaseToolbarAct {

    private static final String KEY_USER_TYPE = "USER_TYPE";

    private ActRentContractBinding mBinding;

    public static void start(Context context, @UserType int userType,String shopId) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, RentContractAct.class);
            starter.putExtra(KEY_USER_TYPE, userType);
            starter.putExtra("shopId", shopId);
            context.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActRentContractBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //检查用户类型
        checkUserType();
        mBinding.next.setOnClickListener(v -> {
            boolean checked = mBinding.checkbox.isChecked();
            if (!checked) {
                Toast.makeText(this, R.string.sign_hint_agree_contract, Toast.LENGTH_SHORT).show();
                return;
            }
            getUserLevel();
        });

        //设置10s后才能点击这个按钮
        mBinding.next.setEnabled(false);
        mBinding.next.setBackgroundColor(getResources().getColor(R.color.grey_500));
        Timer timer=new Timer(10*1000,1000);
        timer.setTextView(mBinding.next);
        timer.setEnableBack(R.drawable.button_default);
        timer.setUnableBack(R.drawable.btn_bg_grey);
        timer.start();
    }

    private void getUserLevel() {
        call = AppApi.get().userLevel(LoginStatus.getToken(),getIntent().getStringExtra("shopId"));
        //noinspection unchecked
        call.enqueue(new RequestCallback<UserLevel>(this) {
            @Override
            protected void onSuccess(UserLevel level) {
                if (level.getLevel() == 1) {
                    CorporateSignPayAct.start(RentContractAct.this);
                } else {
                    OfflineAffirmAct.start(RentContractAct.this);
                }
                finish();
            }
        });
    }

    private void checkUserType() {
        int userType = getIntent().getIntExtra(KEY_USER_TYPE, UserType.PERSONAL);
        if (userType == UserType.CORPORATE) {
            mBinding.personalSteps.setVisibility(View.GONE);
            mBinding.corporateSteps.setVisibility(View.VISIBLE);
        }
    }



}
