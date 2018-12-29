package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.resp.MeResp;
import com.qixiu.lejia.core.CodeContract;
import com.qixiu.lejia.core.CodePresenter;
import com.qixiu.lejia.databinding.ActChangePhoneBinding;
import com.qixiu.lejia.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.qixiu.lejia.core.me.profile.ChangePhoneNextAct.USER_TEL;

public class ChangePhoneAct extends BaseToolbarAct implements CodeContract.View, OKHttpUIUpdataListener<BaseBean> {
    private String url = BuildConfig.BASE_URL + "Home/Login/replacetelone";


    private ActChangePhoneBinding mBinding;
    private CountDownTimer mCountDownTimer;
    private CodeContract.Presenter mPresenter;
    protected Call call;
    private String phone;
    private OKHttpRequestModel okHttpRequestModel;
    private String str;

    public static void start(Context context) {
        Intent starter = new Intent(context, ChangePhoneAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mBinding = ActChangePhoneBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPresenter = new CodePresenter();
        mPresenter.onAttach(this);
        loadRealProfile();//加载当前用户的号码
        okHttpRequestModel = new OKHttpRequestModel(this);
        //获取验证码
        mBinding.btnCode.setOnClickListener(v -> {

            if (TextUtils.isEmpty(phone)) {
                showToast(R.string.sign_code);
                return;
            }
            mPresenter.getCode(phone, 4);
        });

        //下一步
        mBinding.next.setOnClickListener(v -> {
            str = getString(R.string.chang_phone);
            Map<String, String> map = new HashMap();
            map.put("user_tel", mBinding.textPhone.getText().toString().trim().replace(str, ""));
            map.put("code", mBinding.editCode.getText().toString().trim());
            map.put("type", 4 + "");
            okHttpRequestModel.okhHttpPost(url, map, new BaseBean());

        });

    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private void loadRealProfile() {
//        call = AppApi.get().realProfile(LoginStatus.getToken());
        call = AppApi.get().meInfo(LoginStatus.getToken());
        call.enqueue(new RequestCallback<MeResp>() {

            @Override
            protected void onSuccess(MeResp profile) {
                phone = profile.getProfile().getUser_tel();
                if (!TextUtils.isEmpty(phone)) {
                    mPresenter.getCode(phone, 4);
                }
                String s = getResources().getString(R.string.chang_phone) + profile.getProfile().getUser_tel();
                mBinding.textPhone.setText(s);
            }
        });


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
            Intent intent = new Intent(ChangePhoneAct.this, ChangePhoneNextAct.class);
            intent.putExtra(USER_TEL, mBinding.textPhone.getText().toString().trim().replace(str, ""));
            ChangePhoneNextAct.start(this, intent);
        }
    }

    @Override
    public void onError(okhttp3.Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast(c_codeBean.getM());
    }


    public static class ChangePhoneNotice{

    }

    @Subscribe
    public void onEvent(ChangePhoneNotice event) {
        finish();
    }
}
