package com.qixiu.lejia.core.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginEvent;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseLoadIndicatorFrag;
import com.qixiu.lejia.beans.NoReadMessage;
import com.qixiu.lejia.beans.home.MessageBean;
import com.qixiu.lejia.beans.resp.MeResp;
import com.qixiu.lejia.core.login.LoginActivity;
import com.qixiu.lejia.core.me.message.MessageListActivity;
import com.qixiu.lejia.core.me.points.PointsAct;
import com.qixiu.lejia.core.me.profile.ProfileEvent;
import com.qixiu.lejia.core.me.profile.UserProfileAct;
import com.qixiu.lejia.core.me.roommate.RoommatesAct;
import com.qixiu.lejia.core.version.NewVersionDialog;
import com.qixiu.lejia.core.version.Version;
import com.qixiu.lejia.core.version.VersionCheckContract;
import com.qixiu.lejia.core.version.VersionCheckPresenter;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.service.DownloadService;
import com.qixiu.lejia.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     我的
 * </pre>
 */
public class MeFragment extends BaseLoadIndicatorFrag
        implements MeContract.View, Observer, VersionCheckContract.View {

    private final Handler mHandler = new Handler();

    private MeViewHolder mViewHolder;
    private MeContract.Presenter mPresenter;
    private VersionCheckContract.Presenter mVersionPresenter;
    private MeResp meBean;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginEvent.getInstance().addObserver(this);
        ProfileEvent.getInstance().addObserver(this);
        mPresenter = new MePresenter();
        mPresenter.onAttach(this);
        ((MePresenter) mPresenter).setContext(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewHolder = new MeViewHolder(view, mPresenter);

    }


    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        //判断是否登录，是-->加载数据
        if (LoginStatus.isLogged()) {
            mPresenter.loadMeInfo(true);
            mPresenter.loadMeNoRead();
        }
    }

    @Override
    public void onDestroy() {
        LoginEvent.getInstance().deleteObserver(this);
        ProfileEvent.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    /**
     * 登录状态更新
     *
     * @param o   {@link LoginEvent}
     * @param arg 1:登录成功 -1:退出登录
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LoginEvent) {
            int status = (int) arg;
            if (status == LoginEvent.Event.LOGIN) {
                //登录成功
                mPresenter.loadMeInfo(true);
                mPresenter.loadMeNoRead();
            } else {
                //退出登录
                mViewHolder.logout();
            }
        } else if (o instanceof ProfileEvent) {
            //个人资料更新,重新加载
            mPresenter.loadMeInfo(false);
            mPresenter.loadMeNoRead();
        }
    }

    @Override
    public void showLoadFail() {
        //加载失败
        Toast.makeText(getContext(), ResponseError.CONNECT.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMeResp(MeResp resp, boolean checkProfileFull) {
        mViewHolder.update(resp);
        if (checkProfileFull) {
            if (resp.getProfile().getDataFullStatus() == 0) {
                //未完善个人信息
                mHandler.postDelayed(this::showTipDialog, 400);
            }
        }
        meBean = resp;
    }

    @Override
    public void showMeNoReadResp(NoReadMessage resp) {
//        mViewHolder.updateNoReadMessage(resp);
    }

    @Override
    public void test() {
        mPresenter.test();
    }

    @Override
    public void startLogin() {
        LoginActivity.start(getContext());
    }

    @Override
    public void startSigned() {
        String url = ApiConstants.buildUrl(ApiConstants.SIGNED, null);
        WebActivity.start(getContext(), getString(R.string.me_sign), url);
    }

    @Override
    public void startAppointment() {
        String url = ApiConstants.buildUrl(ApiConstants.APPOINTMENT, null);
        WebActivity.start(getContext(), getString(R.string.me_appointment), url);
    }

    @Override
    public void startCredit() {
        PointsAct.start(getContext());
    }

    @Override
    public void startMessage() {
//        String url = ApiConstants.buildUrl(ApiConstants.MESSAGE, null);
//        WebActivity.start(getContext(), getString(R.string.me_message), url);
        MessageListActivity.start(getContext());
    }

    @Override
    public void startMeProfile() {
        Intent intent = new Intent(getActivity(), UserProfileAct.class);
        intent.putExtra("data", meBean.getProfile());
        UserProfileAct.start(getContext(), intent);
    }

    @Override
    public void startRoomie() {
        RoommatesAct.start(getContext());
    }

    @Override
    public void checkVersion() {
        if (DownloadService.isRunning) {
            ToastUtils.showShort(getContext(), "新版本正在下载中...");
            return;
        }
        if (mVersionPresenter == null) {
            mVersionPresenter = new VersionCheckPresenter();
            mVersionPresenter.onAttach(this);
        }
        mVersionPresenter.onCheck(BuildConfig.VERSION_CODE, this);
    }

    @Override
    public void showNoNewVersion() {
        ToastUtils.showShort(getContext(), "现版本已是最新版本");
    }

    @Override
    public void showNewVersion(@NonNull Version version) {
        NewVersionDialog.newInstance(version)
                .show(getChildFragmentManager(), null);
    }


    private boolean displayedTip = false;

    private void showTipDialog() {
        if (displayedTip) {
            return;
        }
        displayedTip = true;
        Intent intent = new Intent(getActivity(), UserProfileAct.class);
        intent.putExtra("data", meBean.getProfile());
        new AlertDialog.Builder(getActivity())
                .setMessage("尊敬的用户，在个人中心完善个人资料可以获得信用积分哟~")
                .setPositiveButton("立即完善", (dialog, which) -> {
                    UserProfileAct.start(getContext(), intent);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        getNoReadMessage();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getNoReadMessage();
    }

    private void getNoReadMessage() {
        if (!LoginStatus.isLogged()) {
            return;
        }
        String url = BuildConfig.BASE_URL + "/Home/UserCenter/noticestatus";
        OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(new OKHttpUIUpdataListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data, int i) {
                mViewHolder.updateNoReadMessage(data);
            }

            @Override
            public void onError(okhttp3.Call call, Exception e, int i) {

            }

            @Override
            public void onFailure(C_CodeBean c_codeBean) {

            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());
        okHttpRequestModel.okhHttpPost(url, map, new MessageBean());
    }
}
