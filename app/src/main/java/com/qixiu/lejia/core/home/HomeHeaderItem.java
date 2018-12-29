package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Banner;

import java.util.List;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     首页头部
 * </pre>
 */
public class HomeHeaderItem extends BindableItem {

    private List<Banner> banners;

    private HomeHeaderItem(List<Banner> banner) {
        this.banners = banner;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public static HomeHeaderItem newInstance(List<Banner> banner) {
        return new HomeHeaderItem(banner);
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_header;
    }


}
