package com.qixiu.lejia.core.service.complaint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.Complaint;
import com.qixiu.lejia.databinding.ActComplaintDetailBinding;

/**
 * Created by Long on 2018/5/17
 */
public class ComplaintDetailAct extends BaseWhiteStateBarActivity {

    private static final String KEY_ID    = "ID";
    private static final String KEY_TITLE = "TITLE";

    private ActComplaintDetailBinding mBinding;

    public static void start(Context context, String id, String title) {
        Intent starter = new Intent(context, ComplaintDetailAct.class);
        starter.putExtra(KEY_ID, id);
        starter.putExtra(KEY_TITLE, title);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(KEY_TITLE);
        setTitle(title);
        loadComplaintDetail();
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        mBinding = ActComplaintDetailBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    protected void onContentViewCreated(View view) {
    }

    @Override
    protected void onReload() {
        loadComplaintDetail();
    }

    @SuppressWarnings("unchecked")
    private void loadComplaintDetail() {
        String id = getIntent().getStringExtra(KEY_ID);
        call = AppApi.get().complaintDetail(id);
        call.enqueue(new RequestCallback<Complaint>() {
            @Override
            protected void onSuccess(Complaint o) {
                switchToContentState();
                mBinding.setItem(o);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

}
