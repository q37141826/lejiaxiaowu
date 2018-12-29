package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.home.story.StoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     首页横向滚动门店列表条目
 * </pre>
 */
public class HomeStoryItem extends BindableItem {

    private StoryBean.ListBean storyBean;

    private HomeStoryItem(StoryBean.ListBean storyBean) {
        this.storyBean = storyBean;
    }

    public StoryBean.ListBean getStoryBean() {
        return storyBean;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_story;
    }

    public static List<HomeStoryItem> of(List<StoryBean.ListBean> storyBeans) {
        List<HomeStoryItem> list = new ArrayList<>();
        for (StoryBean.ListBean storyBean : storyBeans) {
            list.add(new HomeStoryItem(storyBean));
        }
        return list;
    }

}
