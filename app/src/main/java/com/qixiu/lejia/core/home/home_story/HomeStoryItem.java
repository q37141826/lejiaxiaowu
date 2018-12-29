package com.qixiu.lejia.core.home.home_story;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2018/7/20.
 */

public class HomeStoryItem extends BindableItem {
    HomeStroryBean homeStroryBean;

    public HomeStroryBean getHomeStroryBean() {
        return homeStroryBean;
    }

    public HomeStoryItem(HomeStroryBean homeStroryBean) {
        this.homeStroryBean = homeStroryBean;
    }

    @Override
    public int getViewType() {
        return R.layout.item_story;
    }

    public static List<HomeStoryItem> of(List<HomeStroryBean> homeStroryBeans) {
        List<HomeStoryItem> list = new ArrayList<>();
        for (HomeStroryBean homeStroryBean : homeStroryBeans) {
            list.add(new HomeStoryItem(homeStroryBean));
        }
        return list;
    }
}
