package com.qixiu.lejia.core.home;

import android.support.annotation.NonNull;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.beans.home.story.StoryFatherBean;
import com.qixiu.lejia.beans.resp.HomeResp;
import com.qixiu.lejia.mvp.AbsCallPresenter;
import com.qixiu.lejia.mvp.BaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Long on 2018/4/25
 */
class HomePresenter extends AbsCallPresenter implements HomeContract.Presenter {

    private HomeContract.View mView;
    private OKHttpRequestModel okHttpRequestModel;

    @Override
    public void onAttach(@NonNull BaseView view) {
        mView = (HomeContract.View) view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void get() {
        call = AppApi.get().index();
        call.enqueue(new RequestCallback<HomeResp>() {
            @Override
            protected void onSuccess(HomeResp resp) {
                handleResp(resp);
            }

            @Override
            protected void onFailure(ResponseError error) {
                mView.showLoadFail();
            }
        });

    }

    private void handleResp(HomeResp resp) {
        List<BindableItem> items = new ArrayList<>();

        //头部
        items.add(HomeHeaderItem.newInstance(resp.getBannerResp().getTopBanner()));

        //门店展示
        items.add(new HomeShopsItem(HomeShopItem.of(resp.getShops())));

        //品质保障
        items.add(HomeGuaranteeItem.newInstance(resp.getBannerResp().getMiddleBanners(), resp.getGuarantees()));

        //线下活动
        items.add(new HomeEventsItem(HomeEventItem.of(resp.getEvents())));

        //乐家客故事
//         items.add(new HomeStorysItem(HomeStoryItem.of(resp.getStoryBeans())));
//        items.add(new HomeStorysItem(HomeStoryItem.of(test.getStoryBeans())));//暂时隐藏故事部分


        okHttpRequestModel=new OKHttpRequestModel(new OKHttpUIUpdataListener() {
            @Override
            public void onSuccess(Object data, int i) {
                if(data instanceof StoryFatherBean){
                    StoryFatherBean storysbeans= (StoryFatherBean) data;
                    items.add(new HomeStorysItem(HomeStoryItem.of(storysbeans.getO().getList())));
                    //脚部
                    items.add(HomeFooterItem.newInstance());
                    mView.showItems(items);
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onFailure(C_CodeBean c_codeBean) {

            }
        });
        Map<String,String> map=new HashMap<>();
        map.put("pageNo",1+"");
        map.put("pageSize",7+"");
        okHttpRequestModel.okhHttpPost(BuildConfig.BASE_URL+"Home/Life/storylist",map,new StoryFatherBean());


        //乐家客故事
//        call = AppApi.get().storylist();
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                StoryFatherBean storyFatherBean= (StoryFatherBean) response.body();
//                items.add(new HomeStorysItem(HomeStoryItem.of(storyFatherBean.getO().getList())));
//                mView.showItems(items);
//                mView.showLoadFail();
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
        mView.showItems(items);
    }


}
