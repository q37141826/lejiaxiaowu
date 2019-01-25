package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
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
import com.qixiu.lejia.core.me.profile.CompleteProfileAct;
import com.qixiu.lejia.databinding.ActAuthenticationBinding;
import com.qixiu.lejia.utils.RegexUtils;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     第一步 完善信息
 * </pre>
 */
public class AuthenticationAct extends BaseToolbarAct implements CodeContract.View {

    private static final String KEY_USER_TYPE = "USER_TYPE";

    private ActAuthenticationBinding mBinding;

    private CodeContract.Presenter mPresenter;
    private CountDownTimer mCountDownTimer;

    @UserType
    private int mUserType;

    public static void start(Context context, @UserType int userType, String shopId) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, AuthenticationAct.class);
            starter.putExtra(KEY_USER_TYPE, userType);
            starter.putExtra("shopId", shopId);
            context.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //检查用户类型
        checkUserType();

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

        mPresenter = new CodePresenter();
        mPresenter.onAttach(this);


        loadRealProfile();

        //判断是否认证了
        LoginStatus.verifiedIdentified(getContext());
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

//        //验证验证码
//        String code = mBinding.editCode.getText().toString().trim();
//        if (TextUtils.isEmpty(code)) {
//            showToast(R.string.sign_hint_code);
//            return;
//        }

        //所有验证通过
//        upload(name, phone, id, code);
        upload(name, phone, id, "");
    }

    private void upload(String name, String phone, String id, String code) {
        String sex = mBinding.sex.getText().toString();
        call = AppApi.get().signFirstStep(LoginStatus.getToken(), name, phone, sex, id, code);
        //noinspection unchecked
        call.enqueue(new RequestCallback<String>(this) {
            @Override
            protected void onSuccess(String o) {
                finish();
                QualificationsAct.start(AuthenticationAct.this, mUserType, getIntent().getStringExtra("shopId"));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadRealProfile() {
        call = AppApi.get().realProfile(LoginStatus.getToken());
        call.enqueue(new RequestCallback<RealProfile>() {
            @Override
            protected void onSuccess(RealProfile profile) {
                if (TextUtils.isEmpty(profile.getRealName()) || TextUtils.isEmpty(profile.getPhone()) || TextUtils.isEmpty(profile.getId())) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("")
                            .setMessage("请进行实名认证")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                CompleteProfileAct.start(getContext());
                            })
                            .setNegativeButton(android.R.string.cancel, ((dialog, which) -> {
                                finish();
                            }))
                            .show();
                    return;
                }
                mBinding.editName.setText(profile.getRealName());
                mBinding.editPhone.setText(profile.getPhone());
                mBinding.sex.setText(profile.getSex());
                mBinding.editId.setText(profile.getId());
                setEnable();//已经认证的不准改资料
            }
        });
    }

    public void setEnable() {
        if (!TextUtils.isEmpty(mBinding.editId.getText().toString()) && !TextUtils.isEmpty(mBinding.editName.getText().toString()) &&
                !TextUtils.isEmpty(mBinding.editPhone.getText().toString()) && !TextUtils.isEmpty(mBinding.sex.getText().toString())) {
            mBinding.editId.setEnabled(false);
            mBinding.editName.setEnabled(false);
            mBinding.editPhone.setEnabled(false);
            mBinding.editSex.setEnabled(false);
        }
    }


    @Override
    public void showGetCodeSuccess() {
        mBinding.btnCode.setEnabled(false);
        //开始倒计时
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.btnCode.setText(getString(R.string.login_countdown, millisUntilFinished
                        / 1000));
            }

            @Override
            public void onFinish() {
                mBinding.btnCode.setEnabled(true);
                mBinding.btnCode.setText(R.string.login_get_code);
            }
        };
        mCountDownTimer.start();
    }


    private void checkUserType() {
        mUserType = getIntent().getIntExtra(KEY_USER_TYPE, UserType.PERSONAL);
        if (mUserType == UserType.CORPORATE) {
            mBinding.personalSteps.setVisibility(View.GONE);
            mBinding.corporateSteps.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(int s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //是否已经实名认证
        if(!LoginStatus.isVerified()){
            finish();
        }
    }
}
