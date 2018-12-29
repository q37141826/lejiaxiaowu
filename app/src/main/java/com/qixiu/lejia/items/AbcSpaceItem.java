package com.qixiu.lejia.items;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/24 0024
 */
public class AbcSpaceItem extends BindableItem {

    public static AbcSpaceItem newInstance() {
        return new AbcSpaceItem();
    }

    @Override
    public int getViewType() {
        return R.layout.abc_item_space;
    }

}
