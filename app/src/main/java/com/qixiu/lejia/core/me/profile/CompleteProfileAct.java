package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.RealProfile;
import com.qixiu.lejia.core.CodeContract;
import com.qixiu.lejia.core.CodePresenter;
import com.qixiu.lejia.databinding.ActCompleteProfileBinding;
import com.qixiu.lejia.prefs.Prefs;
import com.qixiu.lejia.prefs.PrefsKeys;
import com.qixiu.lejia.utils.RegexUtils;

/**
 * Created by Long on 2018/5/8
 * <pre>
 *
 * </pre>
 */
public class CompleteProfileAct extends BaseToolbarAct implements CodeContract.View {

    private ActCompleteProfileBinding mBinding;

    private CodeContract.Presenter mPresenter;
    private CountDownTimer mCountDownTimer;

    public static void start(Context context) {
        Intent starter = new Intent(context, CompleteProfileAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mBinding = ActCompleteProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mPresenter = new CodePresenter();
        mPresenter.onAttach(this);

        mBinding.sex.setOnClickListener(v -> {
            //choose sex
            showChooseSexDialog();
        });

        mBinding.btnCode.setOnClickListener(v -> {
            String phone = mBinding.editPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                showToast(R.string.sign_hint_phone);
                return;
            }
            mPresenter.getCode(phone, 4);
        });

        mBinding.next.setOnClickListener(v -> {
            //verifyInput
            verifyInput();
        });


        //加载真实信息
        loadRealProfile();

    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mPresenter.onDestroy();
        super.onDestroy();
    }

    private void showChooseSexDialog() {
        final String[] sexList = getResources().getStringArray(R.array.sex_list);
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_choose_sex)
                .setItems(sexList, (v, index) -> {
                    mBinding.sex.setText(sexList[index]);
                })
                .show();
    }

    //验证输入
    private void verifyInput() {
        //验证姓名
        String name = mBinding.editName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast(R.string.sign_hint_name);
            return;
        }

        //验证手机号
        String phone = mBinding.editPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast(R.string.sign_hint_phone);
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(R.string.sign_hint_invalid_phone);
            return;
        }

        //验证身份证号
        String id = mBinding.editId.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            showToast(R.string.sign_hint_id);
            return;
        }
        if (!RegexUtils.isIDCard18(id)) {
            showToast(R.string.sign_hint_invalid_id);
            return;
        }

        //验证验证码
        String code = mBinding.editCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast(R.string.sign_hint_code);
            return;
        }

        //所有验证通过

        upload(name, phone, id, code);

        //所有验证通过

        upload(name, phone, id, code);

    }

    @Override
    public void showGetCodeSuccess() {
        mBinding.btnCode.setEnabled(false);
        //开始倒计时
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.btnCode.setText(getString(R.string.login_countdown, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mBinding.btnCode.setEnabled(true);
                mBinding.btnCode.setText(R.string.login_get_code);
            }
        };
        mCountDownTimer.start();
    }

    private void upload(String name, String phone, String id, String code) {
        String sex = mBinding.sex.getText().toString();
        call = AppApi.get().signFirstStep(LoginStatus.getToken(), name, phone, sex, id, code);
        //noinspection unchecked
        call.enqueue(new RequestCallback<String>(this) {
            @Override
            protected void onSuccess(String o) {
                Prefs.put(PrefsKeys.IS_IDENTIFYED,"1");
                finish();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadRealProfile() {
        call = AppApi.get().realProfile(LoginStatus.getToken());
        call.enqueue(new RequestCallback<RealProfile>() {
            @Override
            protected void onSuccess(RealProfile profile) {
                mBinding.editName.setText(profile.getRealName());
                mBinding.editPhone.setText(profile.getPhone());
                mBinding.sex.setText(profile.getSex());
                mBinding.editId.setText(profile.getId());
                if ("1".equals(profile.getIdentified())) {
                    mBinding.editName.setEnabled(false);
                    mBinding.editPhone.setEnabled(false);
                    mBinding.sex.setEnabled(false);
                    mBinding.editId.setEnabled(false);
                    mBinding.editCode.setVisibility(View.GONE);
                    mBinding.btnCode.setVisibility(View.GONE);
                    mBinding.next.setVisibility(View.GONE);
                }

            }

        });
    }

    private void showToast(int s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("实名认证");
    }
}
