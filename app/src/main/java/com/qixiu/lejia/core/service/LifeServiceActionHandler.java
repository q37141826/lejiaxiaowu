package com.qixiu.lejia.core.service;

import com.qixiu.adapter.ItemActionHandler;
import com.qixiu.lejia.beans.resp.LifeServiceResp;

/**
 * Created by Long on 2018/5/4
 */
public class LifeServiceActionHandler implements ItemActionHandler {


    private LifeServicesView mView;

    LifeServiceActionHandler(LifeServicesView view) {
        mView = view;
    }

    @Override
    public void onItemClick(Object o) {
        if (o instanceof ServiceRecommendItem) {
            ServiceRecommendItem item = (ServiceRecommendItem) o;
            LifeServiceResp.RecommendService service = item.getService();
            mView.startRecommendDetail(service);
        }
    }

    /**
     * 跳转房租界面
     */
    public void startRent() {
        mView.startRent();
    }

    /**
     * 跳转水电界面
     */
    public void startHydroelectric() {
        mView.startHydroelectric();
    }

    /**
     * 跳转维修界面
     */
    public void startRepair() {
        mView.startRepair();
    }

    /**
     * 跳转客户提问界面
     */
    public void startQuestion() {
        mView.startQuestion();
    }

    /**
     * 跳转投诉建议界面
     */
    public void startComplaint() {
        mView.startComplaint();
    }

}
