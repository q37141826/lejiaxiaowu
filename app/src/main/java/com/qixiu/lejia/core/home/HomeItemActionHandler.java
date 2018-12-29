package com.qixiu.lejia.core.home;

import com.qixiu.adapter.ItemActionHandler;
import com.qixiu.lejia.beans.Event;
import com.qixiu.lejia.beans.Guarantee;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.home.story.StoryBean;

import java.util.List;

/**
 * Created by Long on 2018/5/4
 */
public class HomeItemActionHandler implements ItemActionHandler {

    private static final String TAG = "HomeItemActionHandler";

    private HomeContract.View mView;

    HomeItemActionHandler(HomeContract.View view) {
        this.mView = view;
    }

    @Override
    public void onItemClick(Object o) {
        if (o instanceof Shop) {
            //门店展示ITEM点击
            mView.startShopDetail((Shop) o);
            return;
        }
        //乐家客故事ITEM点击
        if (o instanceof StoryBean.ListBean){
            mView.startStoryDetail((StoryBean.ListBean) o);
            return;
        }
    }

    public void startNearby() {
        mView.startNearby();
    }

    public void startStory() {
        mView.startStory();
    }

    public void startEventDetail(Event event) {
        mView.startEventDetail(event);
    }

    public void onStartHouseExplain(HomeGuaranteeItem item) {
        if (checkGuaranteeNotNull(item)) {
            mView.startHouseExplain(item.getGuarantees().get(0).getImage());
        }
    }

    public void onStartFurnitureExplain(HomeGuaranteeItem item) {
        if (checkGuaranteeNotNull(item)) {
            mView.startFurnitureExplain(item.getGuarantees().get(1).getImage());
        }
    }

    public void onStartSocialExplain(HomeGuaranteeItem item) {
        if (checkGuaranteeNotNull(item)) {
            mView.startSocialExplain(item.getGuarantees().get(2).getImage());
        }
    }

    private boolean checkGuaranteeNotNull(HomeGuaranteeItem item) {
        List<Guarantee> guarantees = item.getGuarantees();
        return guarantees != null && guarantees.size() >= 3;
    }

}
