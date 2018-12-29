package com.qixiu.lejia.core.me;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.UserProfile;
import com.qixiu.lejia.beans.home.MessageBean;
import com.qixiu.lejia.beans.resp.MeResp;
import com.qixiu.lejia.common.ImageBindingAdapters;
import com.qixiu.widget.LineControllerView;

/**
 * Created by Long on 2018/4/26
 */
class MeViewHolder implements View.OnClickListener {

    private View                 root;
    private MeContract.Presenter mPresenter;

    /*views*/
    private ImageView mAvatar;
    private TextView  mNickname;

    private TextView mSignedCount;                  //签约数
    private View     mRentTag;                          //房租未缴纳标签
    private View     mHydroelectricTag;                 //水电未缴纳标签

    private LineControllerView mAppointment;        //预约
    private LineControllerView mCredit;             //信用


    private LineControllerView mAppVersion;         //版本
    private LineControllerView test;         //测试

    private TextView noRead;


    MeViewHolder(View root, MeContract.Presenter presenter) {
        this.root = root;
        this.mPresenter = presenter;

        findViews();

    }

    private void findViews() {
        mAvatar = root.findViewById(R.id.avatar);
        mNickname = root.findViewById(R.id.nickname);
        noRead = root.findViewById(R.id.no_read);

        View signed = root.findViewById(R.id.signed);
        mSignedCount = root.findViewById(R.id.signed_count);
        mRentTag = root.findViewById(R.id.rent_tag);
        mHydroelectricTag = root.findViewById(R.id.hydroelectric_tag);
        mAppointment = root.findViewById(R.id.appointment);
        mCredit = root.findViewById(R.id.credit);
        test=root.findViewById(R.id.test);
        mAppVersion = root.findViewById(R.id.version);

        mAvatar.setOnClickListener(this);
        signed.setOnClickListener(this);
        mAppointment.setOnClickListener(this);
        mCredit.setOnClickListener(this);
        mAppVersion.setOnClickListener(this);
        test.setOnClickListener(this);
        mAppVersion.setSecondaryText(BuildConfig.VERSION_NAME);

        root.findViewById(R.id.message).setOnClickListener(this);
        root.findViewById(R.id.profile).setOnClickListener(this);
        root.findViewById(R.id.chum).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.version) {
            //版本
            mPresenter.checkVersion();
            return;
        }

        if (!LoginStatus.isLogged()) {
            mPresenter.startLogin();
            return;
        }

        switch (v.getId()) {
            case R.id.avatar:
                //个人资料
                mPresenter.startMeProfile();
                break;
            case R.id.signed:
                //我的签约
                mPresenter.startSigned();
                break;
            case R.id.appointment:
                //我的预约
                mPresenter.startAppointment();
                break;
            case R.id.credit:
                //信用积分
                mPresenter.startCredit();
                break;
            case R.id.message:
                //消息
                mPresenter.startMessage();
                break;
            case R.id.profile:
                //个人资料
                mPresenter.startMeProfile();
                break;
            case R.id.chum:
                //室友
                mPresenter.startRoomie();
                break;
            case R.id.test:
                //室友
                mPresenter.test();
                break;
        }
    }

    //退出登录，清除所有数据显示
    void logout() {
        mAvatar.setImageResource(R.drawable.ic_avatar_holder);
        mNickname.setText(R.string.me_please_login);

        mSignedCount.setText("");
        mRentTag.setVisibility(View.GONE);
        mHydroelectricTag.setVisibility(View.GONE);

        mAppointment.setSecondaryText("");

    }

    void updateNoReadMessage(BaseBean resp) {
        if(resp instanceof MessageBean){
            MessageBean bean= (MessageBean) resp;
            double isReaf = bean.getO().getNoread();
            if (isReaf == 0)
            {
                noRead.setVisibility(View.GONE);
            }
            else {
                noRead.setVisibility(View.VISIBLE);
                noRead.setText((isReaf+"").replace(".0",""));
            }
        }

    }

    void update(MeResp resp) {
        //设置头像
        UserProfile profile = resp.getProfile();
        if (!TextUtils.isEmpty(profile.getAvatar())) {
            Context context = mAvatar.getContext();
            Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_avatar_holder);
            ImageBindingAdapters.bindImage(mAvatar, profile.getAvatar(), d, d, d);
        }

        //设置头像
        if (TextUtils.isEmpty(profile.getNickName())) {
            mNickname.setText(R.string.me_nickname_unset);
        } else {
            mNickname.setText(profile.getNickName());
        }

        //签约状态
        if (Integer.valueOf(resp.getSignedCount()) > 0) {
            mSignedCount.setText(resp.getSignedCount());
        }

        //房租未缴纳标签
        mRentTag.setVisibility(resp.getRentPaymentStatus().getStatus() == 1 ? View.GONE : View.VISIBLE);
        //水电未缴纳标签
        mHydroelectricTag.setVisibility(resp.getHydroelectricPaymentStatus().getStatus() == 1 ? View.GONE : View.VISIBLE);


        //预约数
        if (Integer.valueOf(resp.getAppointmentCount()) > 0) {
            mAppointment.setSecondaryText(resp.getAppointmentCount());
        }

    }
}
