package com.qixiu.lejia.core.login;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiException;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.mvp.BaseView;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.thirdparty.SdkConstants;
import com.qixiu.thirdparty.wx.WXAccessToken;
import com.qixiu.thirdparty.wx.WXLoginEvent;
import com.qixiu.thirdparty.wx.WXUserInfo;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Long on 2018/5/2
 * <pre>
 *     第三方登录
 * </pre>
 */
class ThirdLoginPresenterImpl implements LoginContract.ThirdLoginPresenter, Observer {

    private static final String TAG = "ThirdLoginPresenterImpl";

    private final Gson gson = new GsonBuilder().create();
    private CompositeDisposable disposables = new CompositeDisposable();

    private LoginContract.View mView;

    private Tencent mTencent;
    private IWXAPI wxApi;


    /**
     * Tencent登录回调
     * <p>
     * 登录回调数据如下：
     * <pre>
     * {
     *    "ret": 0,
     *    "openid": "75ED26DEDF4A14B3EB20684F1388F1AC",
     *    "access_token": "1F4CF219CC62EC7437C206B0620C5C3B",
     *    "pay_token": "23D3832C0B37FA2D8176E47714581987",
     *    "expires_in": 7776000,
     *    "pf": "desktop_m_qq-10000144-android-2002-",
     *    "pfkey": "1ae10dd6cde31f8e5ff49a8601e650d4",
     *    "msg": "",
     *    "login_cost": 376,
     *    "query_authority_cost": 107,
     *    "authority_cost": 93581,
     *    "expires_time": 1533023379233
     * }
     * </pre>
     * <p>
     * 用户资料回调如下:
     * <pre>
     *
     * {
     *    "ret": 0,
     *    "msg": "",
     *    "is_lost": 0,
     *    "nickname": "zzzlong",
     *    "gender": "男",
     *    "province": "湖北",
     *    "city": "武汉",
     *    "year": "1991",
     *    "figureurl": "http://qzapp.qlogo.cn/qzapp/1106861790/75ED26DEDF4A14B3EB20684F1388F1AC/30",
     *    "figureurl_1": "http://qzapp.qlogo.cn/qzapp/1106861790/75ED26DEDF4A14B3EB20684F1388F1AC/50",
     *    "figureurl_2": "http://qzapp.qlogo.cn/qzapp/1106861790/75ED26DEDF4A14B3EB20684F1388F1AC/100",
     *    "figureurl_qq_1": "http://thirdqq.qlogo.cn/qqapp/1106861790/75ED26DEDF4A14B3EB20684F1388F1AC/40",
     *    "figureurl_qq_2": "http://thirdqq.qlogo.cn/qqapp/1106861790/75ED26DEDF4A14B3EB20684F1388F1AC/100",
     *    "is_yellow_vip": "0",
     *    "vip": "0",
     *    "yellow_vip_level": "0",
     *    "level": "0",
     *    "is_yellow_year_vip": "0"
     * }
     *
     * </pre>
     */
    private final IUiListener mTencentCallback = new IUiListener() {
        @Override
        public void onComplete(Object response) {

            mView.dismissLoadIndicator();

            Logger.d(TAG, "onComplete: " + response.toString());
            JSONObject json = (JSONObject) response;
            if (json.has("access_token")) {
                //登录回调
                Logger.d(TAG, "onComplete: 登录回调");
                mTencent.setOpenId(json.optString("openid"));
                mTencent.setAccessToken(json.optString("access_token"),
                        json.optString("expires_in"));

                //获取用户资料
                getTencentUserInfo();

            } else {
                //获取用户资料回调
                Logger.d(TAG, "onComplete: 获取用户资料回调");

                String nickname = json.optString("nickname");
                String avatar = json.optString("figureurl_2");

                mView.showTencentLoginSuccess(nickname, avatar, mTencent.getOpenId());

            }

        }

        @Override
        public void onError(UiError error) {
            Logger.d(TAG, "onError: ");
            mView.dismissLoadIndicator();
            mView.showErrorMsg(error.errorMessage);
        }

        @Override
        public void onCancel() {
            Logger.d(TAG, "onPayCancel: ");
            mView.dismissLoadIndicator();
            mView.showThirdLoginCancel();
        }
    };

    ThirdLoginPresenterImpl() {
        //注册微信登录事件观察者
        WXLoginEvent.getInstance().addObserver(this);
    }

    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (LoginContract.View) view;
    }

    @Override
    public void onDestroy() {
        mView = null;
        mTencent = null;
        wxApi = null;
        //注销微信登录事件观察者
        WXLoginEvent.getInstance().deleteObserver(this);
        disposables.clear();
    }

    @Override
    public void loginWithQQ(@NonNull Activity activity) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(SdkConstants.QQ_APP_ID, AppContext.getContext());
        }
        mTencent.login(activity, "all", mTencentCallback);
    }

    @Override
    public IUiListener getTencentCallback() {
        return mTencentCallback;
    }

    @Override
    public void loginWithWX(@NonNull Activity context) {
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(context, SdkConstants.WX_APP_ID);
        }
        //判断用户是否安装了微信
        if (!wxApi.isWXAppInstalled()) {
            mView.showWXUninstalled();
            return;
        }
        //构造登录请求
        SendAuth.Req req = new SendAuth.Req();
        req.scope = context.getString(R.string.wx_scope);
        req.state = context.getString(R.string.wx_state);
        //发送请求
        wxApi.sendReq(req);

    }

    //获取QQ用户资料
    private void getTencentUserInfo() {
        UserInfo userInfo = new UserInfo(AppContext.getContext(), mTencent.getQQToken());
        userInfo.getUserInfo(mTencentCallback);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            Logger.d(TAG, "update: 微信登录失败。。。");
            return;
        }
        String code = (String) arg;
        //授权登录成功，获取access_token
        Logger.d(TAG, "update: 微信登录成功  code = " + code);

        mView.showLoadIndicator();

        //获取用户资料
        //step1 获取access_token
        io.reactivex.Observable<String> observable =
                AppApi.get().getWXAccessToken(SdkConstants.WX_APP_ID, SdkConstants.WX_APP_SECRET, code);
        Disposable disposable = observable
                .map(s -> gson.fromJson(s, WXAccessToken.class))
                .flatMap((Function<WXAccessToken, ObservableSource<String>>) accessToken -> {
                    if (accessToken.getErrcode() > 0) {
                        throw ApiException.of(accessToken.getErrmsg());
                    }
                    //step2 获取用户资料
                    return AppApi.get().getWXUserInfo(accessToken.getAccessToken(), accessToken.getOpenId());
                })
                .map(s -> gson.fromJson(s, WXUserInfo.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(info -> {
                    //请求成功
                    mView.dismissLoadIndicator();
                    mView.showWXLoginSuccess(info);
                }, t -> {
                    //失败
                    mView.dismissLoadIndicator();
                    t.printStackTrace();
                });

        disposables.add(disposable);
    }

}
