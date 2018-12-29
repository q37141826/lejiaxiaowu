package com.qixiu.lejia.items;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/5/15 0015
 */
public class AbcDividerItem  extends BindableItem {

    public static AbcDividerItem newInstance() {
        return new AbcDividerItem();
    }

    @Override
    public int getViewType() {
        return R.layout.abc_item_divider;
    }
}
