package com.qixiu.lejia.core.service.bill;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.BillHistory;

/**
 * Created by Long on 2018/5/30 0030
 */
public class BillHistoryTitleItem extends BindableItem {

    private BillHistory bill;

    BillHistoryTitleItem(BillHistory bill) {
        this.bill = bill;
    }

    public BillHistory getBill() {
        return bill;
    }

    @Override
    public int getViewType() {
        return R.layout.item_bill_title;
    }
}
