package com.qixiu.lejia.core.me.roommate;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Roommate;
import com.qixiu.lejia.utils.Lists;

import java.util.List;

/**
 * Created by Long on 2018/5/16
 */
public class RoommateItem extends BindableItem {

    private Roommate roommate;

    private RoommateItem(Roommate roommate) {
        this.roommate = roommate;
    }

    public Roommate getRoommate() {
        return roommate;
    }

    @Override
    public int getViewType() {
        return R.layout.item_roommate;
    }

    public static List<RoommateItem> from(List<Roommate> list) {
        List<RoommateItem> items = Lists.newArrayList();
        for (Roommate roommate : list) {
            items.add(new RoommateItem(roommate));
        }
        return items;
    }


}
