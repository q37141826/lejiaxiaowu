package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

import java.util.List;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     首页线下活动条目
 * </pre>
 */
public class HomeEventsItem extends BindableItem {

    private List<HomeEventItem> items;

    HomeEventsItem(List<HomeEventItem> items) {
        this.items = items;
    }

    public List<HomeEventItem> getItems() {
        return items;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_events;
    }

}
