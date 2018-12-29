package com.qixiu.lejia.items;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.PayDetails;
import com.qixiu.lejia.utils.Lists;

import java.util.List;

/**
 * Created by Long on 2018/4/28 0028
 */
public class PayDetailsCostItem extends BindableItem {

    private PayDetails.Cost cost;

    private PayDetailsCostItem(PayDetails.Cost cost) {
        this.cost = cost;
    }

    public PayDetails.Cost getCost() {
        return cost;
    }

    public static PayDetailsCostItem of(PayDetails.Cost cost) {
        return new PayDetailsCostItem(cost);
    }

    public static List<PayDetailsCostItem>of(List<PayDetails.Cost>costs) {
        List<PayDetailsCostItem> list = Lists.newArrayList();
        for (PayDetails.Cost cost : costs) {
            list.add(new PayDetailsCostItem(cost));
        }
        return list;
    }

    @Override
    public int getViewType() {
        return R.layout.item_pay_details_cost;
    }
}
