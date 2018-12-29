package com.qixiu.lejia.core.service.complaint;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Complaint;
import com.qixiu.lejia.utils.Lists;

import java.util.List;

/**
 * Created by Long on 2018/5/17
 */
public class ComplaintItem extends BindableItem {

    private Complaint c;

    @Override
    public int getViewType() {
        return R.layout.item_complaint;
    }

    private ComplaintItem(Complaint c) {
        this.c = c;
    }

    public Complaint getC() {
        return c;
    }

    public static List<ComplaintItem> from(List<Complaint> list) {
        List<ComplaintItem> items = Lists.newArrayList();
        for (Complaint c : list) {
            items.add(new ComplaintItem(c));
        }
        return items;
    }

}
