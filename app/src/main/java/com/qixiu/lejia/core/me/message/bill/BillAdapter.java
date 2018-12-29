package com.qixiu.lejia.core.me.message.bill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;

/**
 * Created by my on 2018/7/23.
 */

public class BillAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_notice;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new BillHolder(itemView, context, this);
    }

    public class BillHolder extends RecyclerBaseHolder {

        public BillHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
        }

        @Override
        public void bindHolder(int position) {

        }
    }
}
