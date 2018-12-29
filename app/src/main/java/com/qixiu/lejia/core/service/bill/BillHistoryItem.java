package com.qixiu.lejia.core.service.bill;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.BillHistory;

/**
 * Created by Long on 2018/5/30
 */
public class BillHistoryItem extends BindableItem {
    private static final String TAG = "BillHistoryItem";

    private BillHistory.Expenditure ex;

    BillHistoryItem(BillHistory.Expenditure expenditure) {
        ex = expenditure;
    }

    public BillHistory.Expenditure getEx() {
        return ex;
    }

    @Override
    public int getViewType() {
        return R.layout.item_bill_history;
    }

    public static int getBackRes(BillHistory.Expenditure ex){
        if(ex.getPosition()==1){
            return R.drawable.shape_white_top_cornor;
        }else if(ex.getPosition()==-1){
            return R.drawable.shape_white_bottom_cornor;
        }else {
            return R.drawable.shape_white;
        }
    }

    public static int bindIcon(BillHistory.Expenditure ex) {
        int type = ex.getType();
        if (type <= 0) {
            return 0;
        }
        switch (type) {
            case 1:
                return R.drawable.ic_rent;
            case 2:
                return R.drawable.ic_water;
            case 3:
                return R.drawable.ic_power;
        }
        return 0;
    }

}
