package com.qixiu.lejia.core.me.points;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.mine.points.PointsBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by my on 2018/7/26.
 */

public class PointsDetailsAdapter extends RecyclerBaseAdapter {
    public static final int REDUCE = 1;
    public static final int ADD = 2;
    private int state = ADD;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REDUCE, ADD})
    public @interface State {
        int REDUCE = 1;
        int ADD = 2;
    }

    public void setState(@State int state) {
        this.state = state;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_point;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new PointsDetailsHolder(itemView, context, this);
    }

    public class PointsDetailsHolder extends RecyclerBaseHolder {

        TextView textview_way_item,
                textView_point_time,
                textView_point;

        public PointsDetailsHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            textview_way_item = itemView.findViewById(R.id.textview_way_item);
            textView_point_time = itemView.findViewById(R.id.textView_point_time);
            textView_point = itemView.findViewById(R.id.textView_point);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof PointsBean.OBean.AddBean) {
                PointsBean.OBean.AddBean bean = (PointsBean.OBean.AddBean) mData;
                textview_way_item.setText(bean.getUi_title());
                textView_point_time.setText(bean.getUi_createtime());
                textView_point.setText("+"+bean.getUi_integral());
                textView_point.setTextColor(mContext.getResources().getColor(R.color.color_points));
            }
            if (mData instanceof PointsBean.OBean.ReduceBean) {
                PointsBean.OBean.ReduceBean bean = (PointsBean.OBean.ReduceBean) mData;
                textview_way_item.setText(bean.getUi_title());
                textView_point_time.setText(bean.getUi_createtime());
                textView_point.setText("-"+bean.getUi_integral());
                textView_point.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        }
    }
}
