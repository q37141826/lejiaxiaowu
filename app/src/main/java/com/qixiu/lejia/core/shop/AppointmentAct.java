package com.qixiu.lejia.core.shop;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.RealProfile;
import com.qixiu.lejia.common.Validator;
import com.qixiu.lejia.core.CodeContract;
import com.qixiu.lejia.core.CodePresenter;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.lejia.utils.ToastUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Long on 2018/5/30
 * <pre>
 *     预约看房
 * </pre>
 */
public class AppointmentAct extends BaseToolbarAct implements CodeContract.View {

    private static final String TAG = "AppointmentAct";

    private static final String KEY_SHOP_ID = "SHOP_ID";

    private EditText mNameEdit;
    private EditText mPhoneEdit;
    private EditText mCodeEdit;
    private EditText mMessageEdit;
    private TextView mSelectDateView;
    private TextView mGetCodeBtn;

    //选择的预约时间
    private String mSelectedDate;

    //倒计时
    private CountDownTimer mCountDownTimer;

    //获取验证码业务
    private CodeContract.Presenter mPresenter;

    public static void start(Context context, String shopId) {
        Intent starter = new Intent(context, AppointmentAct.class);
        starter.putExtra(KEY_SHOP_ID, shopId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_appointment);

        mNameEdit = findViewById(R.id.edit_name);

        mPhoneEdit = findViewById(R.id.edit_phone);
        mCodeEdit = findViewById(R.id.edit_code);
        mMessageEdit = findViewById(R.id.edit_message);
        mSelectDateView = findViewById(R.id.date);
        mGetCodeBtn = findViewById(R.id.btn_code);
        //加载真实信息
        loadRealProfile();

        mSelectDateView.setOnClickListener(v -> showDataPickDialog());
        mGetCodeBtn.setOnClickListener(v -> {
            //获取验证码
            if (mPresenter == null) {
                mPresenter = new CodePresenter();
                mPresenter.onAttach(this);
            }
            String phone = mPhoneEdit.getText().toString();
            if (!Validator.validatePhoneNumber(this, phone)) {
                return;
            }
            mPresenter.getCode(phone, 3);
        });
    }

    //再看看
    public void look(View view) {
        finish();
    }

    //提交
    @SuppressWarnings("unchecked")
    public void submit(View view) {
        String name = mNameEdit.getText().toString();
        String phone = mPhoneEdit.getText().toString();
        String code = mCodeEdit.getText().toString();
        String message = mMessageEdit.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(this, R.string.sign_hint_name);
            return;
        }

        if (!Validator.validatePhoneNumber(this, phone)) {
            return;
        }

        if (!Validator.validateSMSCode(this, code)) {
            return;
        }

        if (TextUtils.isEmpty(mSelectedDate)) {
            ToastUtils.showShort(this, "请选择预约时间");
            return;
        }

        //提交
        String shopId = getIntent().getStringExtra(KEY_SHOP_ID);
        call = AppApi.get().submitAppointment(LoginStatus.getToken(), shopId, name, phone,
                code, mSelectedDate, message);
        call.enqueue(new RequestCallback(this) {
            @Override
            protected void onSuccess(Object o) {
                ToastUtils.showShort(AppointmentAct.this, "预约成功");
                finish();
            }
        });

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
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = null;
        super.onDestroy();
    }


    private void showDataPickDialog() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            //判断选择的时间是否小于当前时间
            Calendar futureCal = Calendar.getInstance();
            futureCal.set(year, month, dayOfMonth);
            if (futureCal.compareTo(cal) <= 0) {
                //选择的日期小于或等于当前日期，弹出提醒
                ToastUtils.showShort(this, "请选择正确的预约日期！");
                return;
            }

            String dateFmt = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            Logger.d(TAG, "showDataPickDialog: " + dateFmt);
            mSelectDateView.setText(dateFmt);
            mSelectedDate = dateFmt;
        }, y, m, d).show();
    }


    @SuppressWarnings("unchecked")
    private void loadRealProfile() {
        call = AppApi.get().realProfile(LoginStatus.getToken());
        call.enqueue(new RequestCallback<RealProfile>() {
            @Override
            protected void onSuccess(RealProfile profile) {
                mNameEdit.setText(profile.getRealName());
                mPhoneEdit.setText(profile.getPhone());
            }
        });
    }


}
