package com.qixiu.lejia.core.service;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/23
 */
class ServiceItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    ServiceItemDecoration(Context context) {
        space = (int) (context.getResources().getDisplayMetrics().density * 10);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        if (holder.getItemViewType() == R.layout.item_service_recommend) {
            outRect.top = space;
        }
    }
}
