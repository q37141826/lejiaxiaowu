package com.qixiu.lejia.core.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseLoadIndicatorAct;
import com.qixiu.lejia.common.Validator;
import com.qixiu.lejia.mvp.PresenterUtil;
import com.qixiu.thirdparty.wx.WXLoginHelper;
import com.qixiu.thirdparty.wx.WXUserInfo;
import com.tencent.tauth.Tencent;

/**
 * Created by Long on 2018/4/24
 * <pre>
 *     登录
 * </pre>
 */
public class LoginActivity extends BaseLoadIndicatorAct implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    private static final int REQ_CODE = 0XFF;

    private TextView mGetCodeBtn;
    private EditText mPhoneEdit;
    private EditText mCodeEdit;

    private LoginContract.Presenter           mPresenter;
    private LoginContract.ThirdLoginPresenter mThirdLoginPresenter;

    //倒计时
    private CountDownTimer mCountDownTimer;


    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.act_login);

        //init views
        mGetCodeBtn = findViewById(R.id.btn_code);
        mPhoneEdit = findViewById(R.id.edit_phone);
        mCodeEdit = findViewById(R.id.edit_code);

        mGetCodeBtn.setOnClickListener(v -> {
            String phone = mPhoneEdit.getText().toString().trim();
            if (Validator.validatePhoneNumber(this, phone)) {
                mPresenter.getCode(phone);
            }
        });

        //init presenter
        mPresenter = new LoginPresenter();
        mPresenter.onAttach(this);

        //注册到微信
        WXLoginHelper.registerToWX(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            //绑定手机成功
            String token = data.getStringExtra(LoginBindAct.KEY_TOKEN);
            String phone = data.getStringExtra(LoginBindAct.KEY_PHONE);
            showLoginSuccess(phone, token);
            return;
        }
        //Tencent回调，必须设置
        checkThirdLoginPresenter();
        Tencent.onActivityResultData(requestCode, resultCode, data,
                mThirdLoginPresenter.getTencentCallback());
    }

    //登录
    public void login(View view) {
        String phone = mPhoneEdit.getText().toString().trim();
        String code = mCodeEdit.getText().toString().trim();

        if (!Validator.validatePhoneNumber(this, phone)) {
            return;
        }

        if (!Validator.validateSMSCode(this, code)) {
            return;
        }

        mPresenter.login(phone, code);

    }

    //SdkConstants login
    public void loginWithQQ(View view) {
        //LoginBindAct.start(this);
        showLoadIndicator();
        checkThirdLoginPresenter();
        mThirdLoginPresenter.loginWithQQ(this);
    }


    //WX login
    public void loginWithWx(View view) {
        checkThirdLoginPresenter();
        mThirdLoginPresenter.loginWithWX(this);
    }

    @Override
    public void showGetCodeSuccess() {
        mGetCodeBtn.setEnabled(false);
        //开始倒计时
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mGetCodeBtn.setText(getString(R.string.login_countdown, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mGetCodeBtn.setEnabled(true);
                mGetCodeBtn.setText(R.string.login_get_code);
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void showLoginSuccess(String phone, @NonNull String token) {
        LoginStatus.logged(phone, token);
        //退出
        finish();

    }


    @Override
    public void showThirdLoginCancel() {
        Toast.makeText(this, "登录取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTencentLoginSuccess(String nickname, String avatar, String openId) {
        mPresenter.thirdLogin(nickname, avatar, 1, openId);
    }

    @Override
    public void startLoginBind(String nickname, String avatar, int type, String openid) {
        LoginBindAct.start(this, nickname, avatar, type, openid, REQ_CODE);
    }

    @Override
    public void showWXUninstalled() {
        Toast.makeText(this, R.string.wx_uninstalled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWXLoginSuccess(WXUserInfo info) {
        mPresenter.thirdLogin(info.getNickname(), info.getAvatar(), 2, info.getUnionid());
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
        PresenterUtil.destroy(mPresenter);
        PresenterUtil.destroy(mThirdLoginPresenter);
        super.onDestroy();
    }

    private void checkThirdLoginPresenter() {
        if (mThirdLoginPresenter == null) {
            mThirdLoginPresenter = new ThirdLoginPresenterImpl();
            mThirdLoginPresenter.onAttach(this);
        }
    }

}
