package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/23
 */
public class HomeFooterItem extends BindableItem {


    public static HomeFooterItem newInstance() {
        return new HomeFooterItem();
    }


    @Override
    public int getViewType() {
        return R.layout.include_lejia;
    }



}
