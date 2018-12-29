package com.qixiu.lejia.core.me.points;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.PointSection;
import com.qixiu.lejia.beans.PointsHistory;

import java.util.List;

public class MyPointsDetailNewAdapter extends BaseSectionQuickAdapter<PointSection, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public MyPointsDetailNewAdapter(int layoutResId, int sectionHeadResId, List<PointSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, PointSection item) {
        helper.setText(R.id.point_title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointSection item) {
        PointsHistory pointsHistory = item.t;
        TextView point = helper.getView(R.id.textView_point);
        helper.setText(R.id.textview_way_item, pointsHistory.getWay());
        helper.setText(R.id.textView_point_time, pointsHistory.getTime());
        if (pointsHistory.getType() == 0) {
            point.setTextColor(mContext.getResources().getColor(R.color.red));
            String downPoint = "-" + pointsHistory.getPoints();
            point.setText(downPoint);
        } else {
            point.setTextColor(mContext.getResources().getColor(R.color.color_points));
            String upPoint = pointsHistory.getPoints();
            point.setText(upPoint);
        }


    }
}
