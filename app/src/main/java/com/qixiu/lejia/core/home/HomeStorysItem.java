package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

import java.util.List;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     首页门店展示横向滚动列表
 * </pre>
 */
public class HomeStorysItem extends BindableItem {

    private List<HomeStoryItem> shopItems;

    HomeStorysItem(List<HomeStoryItem> shopItems) {
        this.shopItems = shopItems;
    }

    public List<HomeStoryItem> getStoryItems() {
        return shopItems;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_storys;
    }

}
