package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.UserProfile;
import com.qixiu.lejia.common.ImageBindingAdapters;
import com.qixiu.lejia.common.SingleImagePicker;
import com.qixiu.lejia.core.login.LoginActivity;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.databinding.ActUserProfileBinding;
import com.qixiu.lejia.utils.FileProviderUtil;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.lejia.utils.StreamUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Long on 2018/5/4
 * <pre>
 *     用户资料
 * </pre>
 */
public class UserProfileAct extends BaseWhiteStateBarActivity implements Observer,
        EasyPermissions.PermissionCallbacks {

    private static final String TAG = "UserProfileAct";

    private ActUserProfileBinding mBinding;

    private SingleImagePicker mImagePicker;
    private UserProfile userProfile;

    public static void start(Context context) {
        Intent starter = new Intent(context, UserProfileAct.class);
        context.startActivity(starter);
    }

    public static void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProfileEvent.getInstance().addObserver(this);

        //加载数据
        load();

    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        mBinding = ActUserProfileBinding.inflate(inflater);
        //noinspection ConstantConditions
        return mBinding.getRoot();
    }

    @Override
    protected void onContentViewCreated(View view) {
        EventBus.getDefault().register(this);
        userProfile = getIntent().getParcelableExtra("data");
        //其他资料
        mBinding.other.setOnClickListener(v -> {
            String url = ApiConstants.buildUrl(ApiConstants.OTHER_PROFILE, null);
            WebActivity.start(this, "其他资料", url);
        });

        mBinding.nickname.setOnClickListener(v -> {
            String s = mBinding.nickname.getSecondaryText().toString();
            AlterNicknameAct.start(this, s);
        });

        mBinding.alterAvatar.setOnClickListener(v -> {
            if (mImagePicker == null) {
                mImagePicker = new SingleImagePicker(this);
                mImagePicker.setCallback(uri -> {
                    Logger.d(TAG, "onContentViewCreated: " + uri);
                    updateAvatar(uri);
                });
            }
            mImagePicker.onStart();
        });

        mBinding.logout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("提醒")
                    .setMessage("是否确认退出登录？")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        //退出登录
                        LoginStatus.logout();
                        finish();
                        LoginActivity.start(this);
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });

        mBinding.real.setOnClickListener(v -> CompleteProfileAct.start(this));
        mBinding.qualification.setOnClickListener(v -> CompleteQualificationAct.start(this));
        mBinding.changePhone.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("是否确认更改手机号？")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        ChangePhoneAct.start(this);
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
        //  mBinding.changePhone.setOnClikcL


    }

    @Override
    protected void onReload() {
        load();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ProfileEvent) {
            //reload
            load();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImagePicker != null) {
            mImagePicker.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mImagePicker.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        mImagePicker.onPermissionsDenied(requestCode, perms);
    }

    @Override
    protected void onDestroy() {
        ProfileEvent.getInstance().deleteObserver(this);
        if (mImagePicker != null) {
            mImagePicker.clear();
        }
        mImagePicker = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().userProfile(LoginStatus.getToken());
        call.enqueue(new RequestCallback<UserProfile>() {
            @Override
            protected void onSuccess(UserProfile profile) {
                switchToContentState();
                handleResp(profile);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

    private void handleResp(UserProfile profile) {
        Drawable d = ContextCompat.getDrawable(this, R.drawable.holder_user_avatar);
        ImageBindingAdapters.bindImage(mBinding.avatar, profile.getAvatar(), d, null, d);
        mBinding.nickname.setSecondaryText(profile.getNickName());
        mBinding.changePhone.setSecondaryText(userProfile.getUser_tel());
        if (LoginStatus.isVerified()) {
            mBinding.real.setSecondaryText("已认证");
        } else {
            mBinding.real.setSecondaryText("未认证");
        }
    }


    @SuppressWarnings("unchecked")
    private void updateAvatar(Uri uri) {
        String path = FileProviderUtil.getFilePath(this, uri);
        byte[] bytes = StreamUtils.fileToBytes(new File(path));
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        base64Image += "data:image/jpg;base64,";


        call = AppApi.get().alterAvatar(LoginStatus.getToken(), base64Image);
        call.enqueue(new RequestCallback(this) {
            @Override
            protected void onSuccess(Object o) {
                ProfileEvent.getInstance().profileUpdate();
            }
        });

    }

    @Subscribe
    public void onEvent(ChangePhoneAct.ChangePhoneNotice event) {
        finish();
    }

}
