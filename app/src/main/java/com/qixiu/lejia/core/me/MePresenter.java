package com.qixiu.lejia.core.me;

import android.content.Context;
import android.support.annotation.NonNull;

import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.NoReadMessage;
import com.qixiu.lejia.beans.resp.MeResp;
import com.qixiu.lejia.core.me.sign.MyElectictPayActivity;
import com.qixiu.lejia.core.me.sign.MyElectricPayFirstActivity;
import com.qixiu.lejia.mvp.AbsCallPresenter;
import com.qixiu.lejia.mvp.BaseView;

/**
 * Created by Long on 2018/4/26
 */
class MePresenter extends AbsCallPresenter implements MeContract.Presenter {

    private MeContract.View mView;
    private Context context;
    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (MeContract.View) view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadMeInfo(boolean checkProfileFull) {
        final String token = LoginStatus.getToken();
        call = AppApi.get().meInfo(token);
        call.enqueue(new RequestCallback<MeResp>() {
            @Override
            protected void onSuccess(MeResp resp) {
                mView.showMeResp(resp, checkProfileFull);
            }

            @Override
            protected void onFailure(ResponseError error) {
                mView.showLoadFail();
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadMeNoRead() {
        final String token = LoginStatus.getToken();
        call = AppApi.get().getNoReadMessage(token);
        call.enqueue(new RequestCallback<NoReadMessage>() {
            @Override
            protected void onSuccess(NoReadMessage resp) {
                mView.showMeNoReadResp(resp);
            }

            @Override
            protected void onFailure(ResponseError error) {

            }
        });

    }

    @Override
    public void test() {
//         String fileName="TBS测试.docx";
//         String fileUrl="https://raw.githubusercontent.com/yangxch/Resources/master/test.docx";//远程文档地址
//        FilePreviewActivity.actionStart(context,fileUrl,fileName,FilePreviewActivity.class);
        MyElectictPayActivity.start(context,MyElectricPayFirstActivity.class);
    }

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    @Override
    public void startSigned() {
        mView.startSigned();
    }

    @Override
    public void startAppointment() {
        mView.startAppointment();
    }

    @Override
    public void startCredit() {
        mView.startCredit();
    }

    @Override
    public void startMessage() {
        mView.startMessage();
    }

    @Override
    public void startMeProfile() {
        mView.startMeProfile();
    }

    @Override
    public void startRoomie() {
        mView.startRoomie();
    }

    @Override
    public void checkVersion() {
        mView.checkVersion();
    }

}
