package com.qixiu.lejia.core.service.repair;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Repair;
import com.qixiu.lejia.utils.Lists;

import java.util.List;

/**
 * Created by Long on 2018/5/16
 */
public class RepairItem extends BindableItem {

    private Repair repair;

    private RepairItem(Repair repair) {
        this.repair = repair;
    }

    public Repair getRepair() {
        return repair;
    }

    @Override
    public int getViewType() {
        return R.layout.item_repair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepairItem that = (RepairItem) o;

        return repair.equals(that.repair);
    }

    @Override
    public int hashCode() {
        return repair.hashCode();
    }

    public static List<RepairItem> from(List<Repair> list) {
        List<RepairItem> items = Lists.newArrayList();
        for (Repair repair : list) {
            items.add(new RepairItem(repair));
        }
        return items;
    }


}
