package com.qixiu.lejia.core.me.points;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.PointsHistory;
import com.qixiu.lejia.utils.Lists;

import java.util.List;

/**
 * Created by Long on 2018/5/16
 */
public class PointsDetailItem extends BindableItem {

    private PointsHistory p;

    private PointsDetailItem(PointsHistory p) {
        this.p = p;
    }

    public PointsHistory getP() {
        return p;
    }


    @Override
    public int getViewType() {
        return R.layout.item_points_detail;
    }


    public static List<PointsDetailItem> from(List<PointsHistory> list) {
        List<PointsDetailItem> items = Lists.newArrayList();
        for (PointsHistory p : list) {
            items.add(new PointsDetailItem(p));
        }
        return items;
    }



}
