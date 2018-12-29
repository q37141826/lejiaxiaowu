package com.qixiu.lejia.core.service.repair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.qixiu.lejia.beans.Repair;
import com.qixiu.lejia.utils.DensityUtils;

import java.util.List;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *     报修
 * </pre>
 */
public class RepairsAct extends BaseWhiteStateBarActivity {

    private static final int REQ_CODE = 0xff;

    private RecyclerView            mRecyclerView;
    private BindableRecyclerAdapter mAdapter;

    public static void start(Context context) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, RepairsAct.class);
            context.startActivity(starter);
        }
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
        return inflater.inflate(R.layout.abc_recycler_view, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPadding(0, DensityUtils.dip2px(16), 0, 0);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_offset_start_16));
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
            RepairCreateAct.start(this, REQ_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            switchToLoadingState();
            load();
        }
    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().repairList(LoginStatus.getToken());
        call.enqueue(new RequestCallback<List<Repair>>() {
            @Override
            protected void onSuccess(List<Repair> list) {
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

    private void handleResp(List<Repair> list) {
        List<RepairItem> items = RepairItem.from(list);
        mAdapter = new BindableRecyclerAdapter(items);
        mAdapter.setItemActionHandler(new RepairItemActionHandler() {

            @Override
            public void onItemClick(RepairItem o) {
                RepairDetailAct.start(RepairsAct.this, o.getRepair().getId());
            }

            @Override
            public boolean onItemLongClick(RepairItem repairItem) {
                showAffirmDialog(repairItem);
                return true;
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }


    private void showAffirmDialog(RepairItem item) {
        new AlertDialog.Builder(this)
                .setMessage("确认要删除此条投诉？")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> delRepair(item))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @SuppressWarnings("unchecked")
    private void delRepair(RepairItem item) {
        call = AppApi.get().delRepair(item.getRepair().getId());
        call.enqueue(new RequestCallback(this) {
            @Override
            protected void onSuccess(Object o) {
                mAdapter.remove(item);
            }
        });
    }

}
