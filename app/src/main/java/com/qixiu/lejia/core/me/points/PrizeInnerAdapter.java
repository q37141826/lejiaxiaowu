package com.qixiu.lejia.core.me.points;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.mine.points.PrizeBean;

/**
 * Created by my on 2018/7/26.
 */

public class PrizeInnerAdapter extends RecyclerBaseAdapter {

    @Override
    public int getLayoutId() {
        return R.layout.item_prize;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new PrizeInnerHolder(itemView, context, this);
    }

    public class PrizeInnerHolder extends RecyclerBaseHolder {
        ImageView image_prize, iamgeView_is_Received;
        TextView textView_prize_title_text,
                textView_prize_numble_immage;

        public PrizeInnerHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            image_prize = itemView.findViewById(R.id.image_prize);
            iamgeView_is_Received = itemView.findViewById(R.id.iamgeView_is_Received);
            textView_prize_title_text = itemView.findViewById(R.id.textView_prize_title_text);
            textView_prize_numble_immage = itemView.findViewById(R.id.textView_prize_numble_immage);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof PrizeBean.OBean.ListBean) {
                PrizeBean.OBean.ListBean bean = (PrizeBean.OBean.ListBean) mData;
                Glide.with(mContext).load(bean.getDr_url()).into(image_prize);
                if (bean.getDi_status().equals("0")) {
                    iamgeView_is_Received.setVisibility(View.GONE);
                } else {
                    iamgeView_is_Received.setVisibility(View.VISIBLE);
                }
                textView_prize_title_text.setText(bean.getDr_name());
                textView_prize_numble_immage.setText(bean.getDi_code());
            }
        }
    }

}
