package com.qixiu.lejia.core.me.points;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.PrizeDetail;
import com.qixiu.lejia.beans.PrizeSection;

import java.util.List;

class MyPrizeAdapter extends BaseSectionQuickAdapter<PrizeSection, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public MyPrizeAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }


    @Override
    protected void convertHead(BaseViewHolder helper, PrizeSection item) {
        helper.setText(R.id.head_time, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, PrizeSection item) {
        PrizeDetail detail = item.t;
//        helper.setText(R.id.prize_title_text, detail.getName());
//        helper.setText(R.id.prize_numble_immage, detail.getAge());
    }
}
