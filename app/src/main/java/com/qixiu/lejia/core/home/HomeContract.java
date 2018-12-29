package com.qixiu.lejia.core.home;

import android.support.annotation.NonNull;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.beans.Event;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.home.story.StoryBean;
import com.qixiu.lejia.mvp.BaseView;
import com.qixiu.lejia.mvp.CallPresenter;

import java.util.List;

/**
 * Created by Long on 2018/4/25
 */
interface HomeContract {

    interface View extends BaseView {

        void showLoadFail();

        void showItems(@NonNull List<BindableItem> items);
        List<BindableItem> getItems();
        void startNearby();

        void startStory();

        void startShopDetail(Shop shop);

        void startStoryDetail(StoryBean.ListBean storyBean);

        void startEventDetail(Event event);

        void startHouseExplain(String image);

        void startFurnitureExplain(String image);

        void startSocialExplain(String image);
    }

    interface Presenter extends CallPresenter {

        void get();

    }


}
