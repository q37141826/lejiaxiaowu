package com.qixiu.lejia.core.service;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/23
 */
public class ServiceFooterItem extends BindableItem {

    public static ServiceFooterItem newInstance() {
        return new ServiceFooterItem();
    }

    @Override
    public int getViewType() {
        return R.layout.include_lejia;
    }

}
