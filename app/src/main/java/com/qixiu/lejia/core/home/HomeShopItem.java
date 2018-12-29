package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     首页横向滚动门店列表条目
 * </pre>
 */
public class HomeShopItem extends BindableItem {

    private Shop shop;

    private HomeShopItem(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_shop;
    }

    public static List<HomeShopItem> of(List<Shop>shops){
        List<HomeShopItem> list = new ArrayList<>();
        for (Shop shop : shops) {
            list.add(new HomeShopItem(shop));
        }
        return list;
    }

}
