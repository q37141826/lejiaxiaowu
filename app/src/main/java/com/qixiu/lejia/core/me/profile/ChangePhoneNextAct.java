package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.common.Validator;
import com.qixiu.lejia.core.CodeContract;
import com.qixiu.lejia.core.CodePresenter;
import com.qixiu.lejia.databinding.ActChangePhoneNextBinding;
import com.qixiu.lejia.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class ChangePhoneNextAct extends BaseToolbarAct implements CodeContract.View, OKHttpUIUpdataListener<BaseBean> {
    private String url = BuildConfig.BASE_URL + "Home/Login/replacetel";
    private ActChangePhoneNextBinding mBinding;
    private CountDownTimer mCountDownTimer;
    private CodeContract.Presenter mPresenter;
    protected Call call;
    public static final String USER_TEL = "USER_TEL";
    private String userTel;
    private EditText edit_phone;
    private OKHttpRequestModel okHttpRequestModel;

    public static void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActChangePhoneNextBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new CodePresenter();
        mPresenter.onAttach(this);
        okHttpRequestModel = new OKHttpRequestModel(this);
        //获取验证码
        mBinding.btnCode.setOnClickListener(v -> {
            String phone = mBinding.editPhone.getText().toString().trim();
            if (!Validator.validatePhoneNumber(this, phone)) {
                return;
            }

            mPresenter.getCode(phone, 7);
        });

//        //确认
        mBinding.bind.setOnClickListener(v -> {
                    String phone = mBinding.editPhone.getText().toString().trim();
                    String code = mBinding.editCode.getText().toString().trim();
                    if (!Validator.validatePhoneNumber(ChangePhoneNextAct.this, phone)) {
                        return;
                    }
                    if (!Validator.validateSMSCode(ChangePhoneNextAct.this, code)) {
                        return;
                    }
                    Map<String, String> map = new HashMap();
                    map.put("user_tel", userTel);
                    map.put("repleace_tel", phone);
                    map.put("code", code);
                    map.put("type", 7 + "");
                    okHttpRequestModel.okhHttpPost(url, map, new BaseBean());
                }
        );
//
        userTel = getIntent().getStringExtra(USER_TEL);
    }


    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mPresenter.onDestroy();
        super.onDestroy();
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


    private void showToast(int s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccess(BaseBean data, int i) {
        if (data.getUrl().equals(url)) {
            ToastUtil.toast(data.getM());
            EventBus.getDefault().post(new ChangePhoneAct.ChangePhoneNotice());
            finish();
        }
    }

    @Override
    public void onError(okhttp3.Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast(c_codeBean.getM());
    }
}
