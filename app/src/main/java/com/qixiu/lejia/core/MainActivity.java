package com.qixiu.lejia.core;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.beans.RealProfile;
import com.qixiu.lejia.common.Events;
import com.qixiu.lejia.core.version.NewVersionDialog;
import com.qixiu.lejia.core.version.Version;
import com.qixiu.lejia.core.version.VersionCheckContract;
import com.qixiu.lejia.core.version.VersionCheckPresenter;
import com.qixiu.lejia.prefs.Prefs;
import com.qixiu.lejia.prefs.PrefsKeys;
import com.qixiu.widget.BottomNavigationView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;

public class MainActivity extends BaseActivity implements VersionCheckContract.View {

    private static final String TAG = "MainActivity";

    private final Handler mHandler = new Handler();

    /*导航*/
    private MainNavigator mNavigator;
    /*版本更新*/
    private VersionCheckContract.Presenter mVersionPresenter;

    public static final String KEY_USER_TYPE = "USER_TYPE";

    private int mUserType;

    private BottomNavigationView bottomBar;

    private final Runnable CHECK_VERSION = () -> {
        mVersionPresenter.onCheck(BuildConfig.VERSION_CODE);
    };


    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //6.0以上使用黑色状态栏图标
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //6.0以下
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);

        mNavigator = new MainNavigator(getSupportFragmentManager());

        //初始化底部导航
        setupBottomBar();

        mNavigator.onCreate(savedInstanceState);

        //版本更新
        mVersionPresenter = new VersionCheckPresenter();
        mVersionPresenter.onAttach(this);
        mHandler.postDelayed(CHECK_VERSION, 2000);

        //每次启动，友盟统计活跃账号
        if (LoginStatus.isLogged()) {
            String phone = Prefs.getString(PrefsKeys.KEY_PHONE);
            if (phone != null) {
                MobclickAgent.onProfileSignIn(phone);
            }
        }
    }

    @Subscribe
    public void corporateHomePage(Events.CorporateHomePageEvent event) {
        mUserType = 2;
        checkUserType();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        mNavigator.onDestroy();
        mHandler.removeCallbacks(CHECK_VERSION);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setupBottomBar() {
        bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setOnCheckedChangeListener(checkedIndex -> {
            //切换fragment
            mNavigator.show(checkedIndex);
            if (checkedIndex > 1) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    getWindow().setStatusBarColor(Color.WHITE);
                } else {
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
        });
    }

    @Override
    public void showNoNewVersion() {
    }

    @Override
    public void showNewVersion(@NonNull Version version) {
        NewVersionDialog.newInstance(version)
                .show(getSupportFragmentManager(), null);
    }

    private void checkUserType() {
        if (mUserType == 2) {
            new AlertDialog.Builder(this)
                    .setTitle("企业用户")
                    .setMessage("您为企业用户，点击右上角企业员工办理入住")
                    .setPositiveButton("确定", null)
                    .show();
            bottomBar.checked(1);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //动态权限申请
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            //保存是否实名认证状态
            saveIdentifyState();
        }
    }

    private void saveIdentifyState() {
        //保存是否实名认证状态
        Call call = AppApi.get().realProfile(LoginStatus.getToken());
        call.enqueue(new RequestCallback<RealProfile>() {
            @Override
            protected void onSuccess(RealProfile profile) {
                    Prefs.put(PrefsKeys.IS_IDENTIFYED, profile.getIdentified());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveIdentifyState();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，无法预览合同等文件", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
