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
public class HomeShopsItem extends BindableItem {

    private List<HomeShopItem> shopItems;

    HomeShopsItem(List<HomeShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public List<HomeShopItem> getShopItems() {
        return shopItems;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_shops;
    }

}
