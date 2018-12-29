package com.qixiu.lejia.core.service.complaint;

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
import com.qixiu.lejia.beans.Complaint;
import com.qixiu.lejia.utils.DensityUtils;

import java.util.List;

/**
 * Created by Long on 2018/5/17
 * <pre>
 *     投诉
 * </pre>
 */
public class ComplaintsAct extends BaseWhiteStateBarActivity {

    private static final int REQ_CODE = 0xFF;

    private RecyclerView mRecyclerView;

    public static void start(Context context) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, ComplaintsAct.class);
            context.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setPadding(0, DensityUtils.dip2px(16), 0, 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_offset_start_80));
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complaint, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create) {
            ComplaintCreateAct.start(this, REQ_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            //新建投诉成功，刷新页面
            switchToLoadingState();
            load();
        }
    }

    @Override
    protected void onReload() {
        load();
    }


    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().complaintList(LoginStatus.getToken());
        call.enqueue(new RequestCallback<List<Complaint>>() {
            @Override
            protected void onSuccess(List<Complaint> list) {
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

    private void handleResp(List<Complaint> list) {
        List<ComplaintItem> items = ComplaintItem.from(list);
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        adapter.setItemActionHandler(o -> {
            ComplaintItem item = (ComplaintItem) o;
            Complaint c = item.getC();
            ComplaintDetailAct.start(this, c.getId(), c.getTag());
            //投诉已读状态
            //0->新增投诉状态
            //1->后台已回答，用户未读
            //2->后台已回答，用户已读
            if (c.getReaded() == 1) {
                //只有在后台已回答情况下，设置用户已读
                item.getC().setReaded(2);
            }
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        mRecyclerView.setAdapter(adapter);

    }

}
