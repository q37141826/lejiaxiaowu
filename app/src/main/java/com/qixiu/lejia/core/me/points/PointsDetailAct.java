package com.qixiu.lejia.core.me.points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseMultiStateAct;
import com.qixiu.lejia.beans.PointsHistory;

import java.util.List;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *     积分明细
 * </pre>
 */
public class PointsDetailAct extends BaseMultiStateAct {

    private RecyclerView mRecyclerView;

    public static void start(Context context) {
        Intent starter = new Intent(context, PointsDetailAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_points_detail, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_offset_start_16));
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().pointsDetail(LoginStatus.getToken());
        call.enqueue(new RequestCallback<List<PointsHistory>>() {
            @Override
            protected void onSuccess(List<PointsHistory> list) {
                if (list.isEmpty()) {
                    switchToEmptyState();
                    return;
                }
                switchToContentState();

                BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(PointsDetailItem.from(list));
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }


}
