package com.qixiu.lejia.core.service.repair;

import com.qixiu.adapter.ItemActionHandler;

/**
 * Created by Long on 2018/5/24
 */
public interface RepairItemActionHandler extends ItemActionHandler<RepairItem> {

    boolean onItemLongClick(RepairItem repairItem);

}
