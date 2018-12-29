package com.qixiu.lejia.core.me.roommate;

import com.qixiu.adapter.ItemActionHandler;

/**
 * Created by Long on 2018/5/17 0017
 */
public interface OnRoommateLongClickListener extends ItemActionHandler<RoommateItem> {

    boolean onItemLongClick(RoommateItem item);

}
