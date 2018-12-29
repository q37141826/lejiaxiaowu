package com.qixiu.lejia.core.shop;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.ShopDetail;

/**
 * Created by Long on 2018/5/30
 */
public class RecommendRoomItem extends BindableItem {

    private ShopDetail.RecommendRoom room;

    RecommendRoomItem(ShopDetail.RecommendRoom room) {
        this.room = room;
    }

    public ShopDetail.RecommendRoom getRoom() {
        return room;
    }

    @Override
    public int getViewType() {
        return R.layout.item_recommend_room;
    }


}
