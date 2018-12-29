package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/4/23
 */
public class HomeEventItem extends BindableItem {

    private Event event;

    private HomeEventItem(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_event;
    }

    public static List<HomeEventItem> of(List<Event> events) {
        List<HomeEventItem> list = new ArrayList<>();
        for (Event event : events) {
            list.add(new HomeEventItem(event));
        }
        return list;
    }

}
