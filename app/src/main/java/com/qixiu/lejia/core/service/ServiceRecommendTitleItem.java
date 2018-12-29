package com.qixiu.lejia.core.service;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/23
 */
public class ServiceRecommendTitleItem  extends BindableItem {

    public static ServiceRecommendTitleItem newInstance() {
        return new ServiceRecommendTitleItem();
    }

    @Override
    public int getViewType() {
        return R.layout.item_service_recommend_title;
    }
}
