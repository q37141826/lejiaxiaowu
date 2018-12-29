package com.qixiu.lejia.core.shop;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.RoomDetail;

/**
 * Created by Long on 2018/6/1
 */
public class RoomConfigItem extends BindableItem {

    private RoomDetail.RoomConfig config;

    RoomConfigItem(RoomDetail.RoomConfig config) {
        this.config = config;
    }

    public RoomDetail.RoomConfig getConfig() {
        return config;
    }

    @Override
    public int getViewType() {
        return R.layout.item_room_config;
    }

    @Override
    public int getSpanSize(int spanSize) {
        return 1;
    }
}
