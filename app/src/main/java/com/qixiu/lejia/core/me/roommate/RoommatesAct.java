package com.qixiu.lejia.core.me.roommate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.Roommate;
import com.qixiu.lejia.utils.DensityUtils;
import com.qixiu.lejia.utils.ToastUtils;
import com.qixiu.widget.MultiStateLayout;

import java.util.List;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *     室友
 * </pre>
 */
public class RoommatesAct extends BaseWhiteStateBarActivity implements RoommateCreateDialog.OnAddRoommateCallback {

    private RecyclerView mRecyclerView;

    public static void start(Context context) {
        Intent starter = new Intent(context, RoommatesAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressLint("InflateParams")
        View emptyView = getLayoutInflater().inflate(R.layout.empty_roommate, null, false);
        multiStateLayout.setViewForState(emptyView, MultiStateLayout.VIEW_STATE_EMPTY);

        getRoommateList();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.abc_recycler_view, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        mRecyclerView = (RecyclerView) view;
        //set padding top 16dp
        mRecyclerView.setPadding(0, DensityUtils.dip2px(16), 0, 0);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create) {
            RoommateCreateDialog.newInstance().show(getSupportFragmentManager());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onReload() {
        getRoommateList();
    }

    @Override
    public void onAddRoommateSuccess() {
        getRoommateList();
    }

    @SuppressWarnings("unchecked")
    private void getRoommateList() {
        call = AppApi.get().roommateList(LoginStatus.getToken());
        call.enqueue(new RequestCallback<List<Roommate>>() {
            @Override
            protected void onSuccess(List<Roommate> list) {
                if (list.isEmpty()) {
                    switchToEmptyState();
                    return;
                }
                switchToContentState();
                handleResp(list);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

    private void handleResp(List<Roommate> list) {
        List<RoommateItem> items = RoommateItem.from(list);
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        adapter.setItemActionHandler(new OnRoommateLongClickListener() {
            @Override
            public boolean onItemLongClick(RoommateItem item) {
                showAffirmDialog(item);
                return true;
            }

            @Override
            public void onItemClick(RoommateItem item) {
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void showAffirmDialog(RoommateItem item) {
        new AlertDialog.Builder(this)
                .setMessage("是否确认删除此室友？")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    delRoommate(item.getRoommate().getId());
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @SuppressWarnings("unchecked")
    private void delRoommate(String id) {
        call = AppApi.get().delRoommate(id);
        call.enqueue(new RequestCallback(this) {
            @Override
            protected void onSuccess(Object o) {
                ToastUtils.showShort(RoommatesAct.this, "删除成功");
                switchToLoadingState();
                getRoommateList();
            }
        });
    }

}
