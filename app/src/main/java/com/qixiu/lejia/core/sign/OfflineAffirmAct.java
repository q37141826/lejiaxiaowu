package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.Room;
import com.qixiu.lejia.databinding.ActOfflineAffirmBinding;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     第四步 线下确认
 * </pre>
 */
public class OfflineAffirmAct extends BaseToolbarAct {

    private ActOfflineAffirmBinding mBinding;

    public static void start(Context context) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, OfflineAffirmAct.class);
            context.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActOfflineAffirmBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        mBinding.stepView.go(3);
        mBinding.next.setOnClickListener(v -> {
            if (mBinding.getRoom() == null) {
                load();
            }else {
                PersonalSignPayAct.start(this);
                finish();
            }
        });

        load();

    }

    private void load() {
        call = AppApi.get().signFourStep(LoginStatus.getToken());
        //noinspection unchecked
        call.enqueue(new RequestCallback<Room>() {
            @Override
            protected void onSuccess(Room o) {
                handleResp(o);
            }

            @Override
            protected void onFailure(ResponseError error) {
                if(error.getErrorMessage().equals("暂未分房")){
                    //未分房
                    //noinspection ConstantConditions
                    mBinding.undistributedRoom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onComplete() {
                mBinding.indicator.setVisibility(View.GONE);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void handleResp(Room room) {
        if (room != null) {
            mBinding.undistributedRoom.setVisibility(View.GONE);
            mBinding.distributedRoom.getRoot().setVisibility(View.VISIBLE);
            mBinding.next.setBackground(getDrawable(R.drawable.button_default));
            mBinding.setRoom(room);
        }
    }


}
