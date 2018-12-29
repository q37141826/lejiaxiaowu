package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseMultiStateAct;
import com.qixiu.lejia.beans.UserProfile;
import com.qixiu.lejia.databinding.ActOtherUserProfileBinding;

/**
 * Created by Long on 2018/5/4
 * <pre>
 *     其他资料
 * </pre>
 */
public class OtherUserProfileAct extends BaseMultiStateAct {

    private ActOtherUserProfileBinding mBinding;

    public static void start(Context context) {
        Intent starter = new Intent(context, OtherUserProfileAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load();
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        mBinding = ActOtherUserProfileBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    protected void onContentViewCreated(View view) {

    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().userOtherProfile(LoginStatus.getToken());
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
        mBinding.hobby.setSecondaryText(profile.getHobby());
        mBinding.heightAndWeight.setSecondaryText(profile.getHeightAndWeight());
        mBinding.education.setSecondaryText(profile.getEducation());
        mBinding.revenue.setSecondaryText(profile.getRevenue());
        mBinding.maritalStatus.setSecondaryText(profile.getMaritalStatus());
    }

}
