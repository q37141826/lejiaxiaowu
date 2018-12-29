package com.qixiu.lejia.core.service;

import com.qixiu.lejia.beans.resp.LifeServiceResp;

/**
 * Created by Long on 2018/5/23
 */
interface LifeServicesView {

    void startRecommendDetail(LifeServiceResp.RecommendService service);

    void startRent();

    void startHydroelectric();

    void startRepair();

    void startQuestion();

    void startComplaint();

}
