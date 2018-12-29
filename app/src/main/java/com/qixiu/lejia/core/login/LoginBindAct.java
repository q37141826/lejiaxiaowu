package com.qixiu.lejia.core.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.base.BaseLoadIndicatorAct;
import com.qixiu.lejia.beans.Token;
import com.qixiu.lejia.common.Validator;
import com.qixiu.lejia.core.CodeContract;
import com.qixiu.lejia.core.CodePresenter;
import com.qixiu.lejia.mvp.PresenterUtil;
import com.qixiu.lejia.utils.DeviceUtils;

/**
 * Created by Long on 2018/4/24
 * <pre>
 *     第三方登录绑定手机号
 * </pre>
 */
public class LoginBindAct extends BaseLoadIndicatorAct implements CodeContract.View {

    private static final String KEY_NICKNAME = "NICKNAME";
    private static final String KEY_AVATAR   = "AVATAR";
    private static final String KEY_TYPE     = "TYPE";
    private static final String KEY_OPENID   = "OPENID";

    static final String KEY_TOKEN = "TOKEN";
    static final String KEY_PHONE = "PHONE";

    private TextView mGetCodeBtn;
    private EditText mPhoneEdit;
    private EditText mCodeEdit;

    private CountDownTimer mCountDownTimer;

    private CodeContract.Presenter mPresenter;

    public static void start(Activity context, String nickname, String avatar, int type,
                             String openid, int requestCode) {
        Intent starter = new Intent(context, LoginBindAct.class);
        starter.putExtra(KEY_NICKNAME, nickname);
        starter.putExtra(KEY_AVATAR, avatar);
        starter.putExtra(KEY_TYPE, type);
        starter.putExtra(KEY_OPENID, openid);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.act_login_bind);

        //init views
        mGetCodeBtn = findViewById(R.id.btn_code);
        mPhoneEdit = findViewById(R.id.edit_phone);
        mCodeEdit = findViewById(R.id.edit_code);

        mGetCodeBtn.setOnClickListener(v -> {
            String phone = mPhoneEdit.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            mPresenter.getCode(phone, 2);
        });

        mPresenter = new CodePresenter();
        mPresenter.onAttach(this);

    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
        PresenterUtil.destroy(mPresenter);
        super.onDestroy();
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


    @SuppressWarnings("unchecked")
    public void bindPhone(View view) {
        String phone = mPhoneEdit.getText().toString().trim();
        String code = mCodeEdit.getText().toString().trim();

        if (!Validator.validatePhoneNumber(this, phone)) {
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        String nickname = intent.getStringExtra(KEY_NICKNAME);
        String avatar = intent.getStringExtra(KEY_AVATAR);
        int type = intent.getIntExtra(KEY_TYPE, 1);
        String openid = intent.getStringExtra(KEY_OPENID);

        //绑定手机
        String id = DeviceUtils.getUniqueId(AppContext.getContext());
        call = AppApi.get().loginBind(phone, code, nickname, avatar, type, openid, id);
        call.enqueue(new RequestCallback<Token>(this) {
            @Override
            protected void onSuccess(Token token) {
                Toast.makeText(LoginBindAct.this, "绑定手机成功", Toast.LENGTH_SHORT).show();

                //返回数据
                Intent intent = new Intent();
                intent.putExtra(KEY_PHONE,phone);
                intent.putExtra(KEY_TOKEN, token.getUid());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


}
