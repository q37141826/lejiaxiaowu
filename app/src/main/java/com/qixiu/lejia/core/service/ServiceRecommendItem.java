package com.qixiu.lejia.core.service;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.resp.LifeServiceResp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by Long on 2018/4/23
 */
public class ServiceRecommendItem extends BindableItem {

    private LifeServiceResp.RecommendService service;

    private ServiceRecommendItem(LifeServiceResp.RecommendService service) {
        this.service = service;
    }

    public LifeServiceResp.RecommendService getService() {
        return service;
    }

    @Override
    public int getViewType() {
        return R.layout.item_service_recommend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceRecommendItem that = (ServiceRecommendItem) o;

        return service.equals(that.service);
    }

    @Override
    public int hashCode() {
        return service.hashCode();
    }

    public static List<ServiceRecommendItem> of(@NonNull List<LifeServiceResp.RecommendService> services) {
        List<ServiceRecommendItem> list = new ArrayList<>();
        for (LifeServiceResp.RecommendService service : services) {
            list.add(new ServiceRecommendItem(service));
        }
        return list;
    }

}
