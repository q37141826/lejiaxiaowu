package com.qixiu.lejia.core.service.bill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.adapter.BindableItem;
import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.BillHistory;
import com.qixiu.lejia.core.service.rent.RentDetailAct;
import com.qixiu.lejia.core.service.we.ElectricityCostDetailAct;
import com.qixiu.lejia.core.service.we.WaterCostDetailAct;
import com.qixiu.lejia.widget.BaseVerticalDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/5/30
 */
public class BillAct extends BaseWhiteStateBarActivity {

    private RecyclerView mRecyclerView;

    public static void start(Context context) {
        Intent starter = new Intent(context, BillAct.class);
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
        return inflater.inflate(R.layout.abc_recycler_view, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemDivider divider = new ItemDivider(this);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_offset_start_48));
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        call = AppApi.get().bill(LoginStatus.getToken());
        call.enqueue(new RequestCallback<List<BillHistory>>() {
            @Override
            protected void onSuccess(List<BillHistory> resp) {
                if (resp == null || resp.isEmpty()) {
                    switchToEmptyState();
                    return;
                }
                switchToContentState();
                handleResp(resp);
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }

    private void handleResp(List<BillHistory> resp) {
        List<BindableItem> items = new ArrayList<>();
        for (BillHistory bill : resp) {
            items.add(new BillHistoryTitleItem(bill));
            List<BillHistory.Expenditure> expenditures = bill.getExpenditures();
            if (expenditures != null) {
//                for (BillHistory.Expenditure expenditure : expenditures) {
//                    items.add(new BillHistoryItem(expenditure));
//                }
                //UI有变更，需要变化背景
                for (int i = 0; i < expenditures.size(); i++) {
                    //如果是第一个，那么背景设置为白色，圆角再上，如果是最后一个圆角在下
                    if (i == 0) {
                        expenditures.get(i).setPosition(1);
                    } else if (i == expenditures.size() - 1) {
                        expenditures.get(i).setPosition(-1);
                    } else {
                        expenditures.get(i).setPosition(0);
                    }
                    items.add(new BillHistoryItem(expenditures.get(i)));
                }
            }
        }
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        adapter.setItemClickListener(new BindableRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (items.get(position) instanceof BillHistoryItem) {
                    BillHistoryItem billHistoryItem = (BillHistoryItem) items.get(position);
                    if (billHistoryItem.getEx().getType() == 1) {
                        RentDetailAct.start(BillAct.this, billHistoryItem.getEx().getPayId(), 1, 1024);
                    } else if (billHistoryItem.getEx().getType() == 2) {
                        WaterCostDetailAct.start(BillAct.this, billHistoryItem.getEx().getBillId(), 1, 1024);
                    } else if (billHistoryItem.getEx().getType() == 3) {
                        ElectricityCostDetailAct.start(BillAct.this, billHistoryItem.getEx().getBillId(), 1, 1024);
                    }
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private static class ItemDivider extends BaseVerticalDivider {

        ItemDivider(Context context) {
            super(context);
        }

        @Override
        protected boolean isNeedDraw(int itemViewType, int position) {
            return itemViewType == R.layout.item_bill_history;
        }

    }

}
