package com.qixiu.lejia.items;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/27
 */
public class AbcLoadingItem extends BindableItem {


    public static AbcLoadingItem newInstance() {
        return new AbcLoadingItem();
    }

    @Override
    public int getViewType() {
        return R.layout.item_loading;
    }

}
