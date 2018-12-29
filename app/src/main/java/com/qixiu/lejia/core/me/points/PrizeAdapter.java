package com.qixiu.lejia.core.me.points;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.mine.points.PrizeBean;

/**
 * Created by my on 2018/7/30.
 */

public class PrizeAdapter extends RecyclerBaseAdapter {

    @Override
    public int getLayoutId() {
        return R.layout.item_prize_out;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new PrizeHolder(itemView, context, this);
    }

    public class PrizeHolder extends RecyclerBaseHolder {
        TextView textView_time_prize;
        RecyclerView recycler_item_prize;

        public PrizeHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            textView_time_prize = itemView.findViewById(R.id.textView_time_prize);
            recycler_item_prize = itemView.findViewById(R.id.recycler_item_prize);
        }

        @Override
        public void bindHolder(int position) {
            PrizeInnerAdapter adapter = new PrizeInnerAdapter();
            recycler_item_prize.setLayoutManager(new LinearLayoutManager(mContext));
            recycler_item_prize.setAdapter(adapter);
            if (mData instanceof PrizeBean.OBean) {
                PrizeBean.OBean bean = (PrizeBean.OBean) mData;
                textView_time_prize.setText(bean.getTime());
                adapter.refreshData(bean.getList());
            }
//            adapter.setOnItemClickListener(new OnRecyclerItemListener() {
//                @Override
//                public void onItemClick(View v, Object data) {
//                    WebActivity.start(mContext,"积分大抽奖","http://lj.lejiaxiaowu.com/ljhouse.app0.001/goodaward.html?uid="+ LoginStatus.getToken());
//                }
//            });
        }
    }
}
