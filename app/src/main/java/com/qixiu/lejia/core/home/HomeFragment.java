package com.qixiu.lejia.core.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qixiu.adapter.BindableItem;
import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.Event;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.home.MessageBean;
import com.qixiu.lejia.beans.home.story.StoryBean;
import com.qixiu.lejia.core.home.home_event.SignWebActivity;
import com.qixiu.lejia.core.me.message.MessageListActivity;
import com.qixiu.lejia.core.shop.ShopDetailAct;
import com.qixiu.lejia.core.shop.ShopMapDisplayAct;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.items.AbcSpaceItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     首页
 * </pre>
 */
public class HomeFragment extends Fragment implements HomeContract.View {

    private static final String TAG = "HomeFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private HomeContract.Presenter mPresenter;

    private BindableRecyclerAdapter mAdapter;

    protected Call call;

    private TextView noRead;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new HomePresenter();
        mPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noRead = view.findViewById(R.id.no_read);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(() -> {
            //refresh
            mPresenter.get();
            getNoReadMessage();
        });

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //RecyclerView 无数据时，SwipeRefreshLayout无法下拉刷新
        List<BindableItem> items = Collections.singletonList(AbcSpaceItem.newInstance());
        mAdapter = new BindableRecyclerAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.message)
                .setOnClickListener(v -> {
                    if (LoginStatus.verifiedLogin(getContext())) {
//                        String url = ApiConstants.buildUrl(ApiConstants.MESSAGE, null);
//                        WebActivity.start(getContext(), getString(R.string.me_message), url);
                        MessageListActivity.start(getContext());
                    }
                });
        getNoReadMessage();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRefreshLayout.setRefreshing(true);
        mPresenter.get();

    }

    @Override
    public void onResume() {
        super.onResume();
        BannerController.start(mRecyclerView);
        getNoReadMessage();
    }

    @Override
    public void onPause() {
        BannerController.stop(mRecyclerView);
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            BannerController.stop(mRecyclerView);
        } else {
            BannerController.start(mRecyclerView);
        }
    }

    //-------------------------------------------------------------------

    @Override
    public void showLoadFail() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showItems(@NonNull List<BindableItem> items) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.onReset(items);
        mAdapter.setItemActionHandler(new HomeItemActionHandler(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public List<BindableItem> getItems() {
        return mAdapter.getItems();
    }

    @Override
    public void startNearby() {
        ShopMapDisplayAct.start(getContext());
    }

    //乐家客故事更多
    @Override
    public void startStory() {
        HomeStoryAct.start(getContext());
//        HomeStoryActivity.start(getContext());
    }

    @Override
    public void startShopDetail(Shop shop) {
        ShopDetailAct.start(getContext(), shop.getId(), shop.getImage());
    }

    @Override
    public void startStoryDetail(StoryBean.ListBean storyBean) {
        if (storyBean instanceof StoryBean.ListBean) {
            StoryBean.ListBean bean = (StoryBean.ListBean) storyBean;
            if(TextUtils.isEmpty(bean.getLink())){
                Intent intent = new Intent(getActivity(), SignWebActivity.class);
                intent.putExtra("data",bean);
                SignWebActivity.start(getContext(), intent);
                //                WebActivity.start(getContext(), bean.getT+itle(), BuildConfig.BASE_URL + storyWebUrl + bean.getId());
            }else {
                WebActivity.start(getContext(),bean.getTitle(),bean.getLink());
            }
        }
    }

    @Override
    public void startEventDetail(Event event) {
        int type = event.getType();
        if (type == 0) return;
        if (type == 1) {
            //内链
//            Map<String, String> params = new HashMap<>();
//            params.put("ay_id", event.getId());
//            String url = ApiConstants.buildUrl(ApiConstants.EVENT, params);
//            url="http://lj.lejiaxiaowu.com/ljhouse.app0.001/tale.html?gid="+event.getId();
//            WebActivity.start(getContext(), event.getTitle(), url);
            Intent intent = new Intent(getActivity(), SignWebActivity.class);
            intent.putExtra("data",event);
            SignWebActivity.start(getContext(), intent);
        } else if (type == 2) {
            //外链
            WebActivity.start(getContext(), event.getTitle(), event.getLink(),false,true,event.getIntro(),event.getImage());
        }
    }

    @Override
    public void startHouseExplain(String image) {
        GuaranteeExplainAct.start(getContext(), 0, image);
    }

    @Override
    public void startFurnitureExplain(String image) {
        GuaranteeExplainAct.start(getContext(), 1, image);
    }

    @Override
    public void startSocialExplain(String image) {
        GuaranteeExplainAct.start(getContext(), 2, image);
    }

    @SuppressWarnings("unchecked")
    private void getNoReadMessage() {
        if(!LoginStatus.isLogged()){
            return;
        }
      String  url=  BuildConfig.BASE_URL+"/Home/UserCenter/noticestatus";
        OKHttpRequestModel okHttpRequestModel=new OKHttpRequestModel(new OKHttpUIUpdataListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data, int i) {
                if(data instanceof MessageBean){
                    MessageBean messageBean= (MessageBean) data;
                    if(messageBean.getO().getNoread()==0){
                        noRead.setVisibility(View.GONE);
                    }else {
                        noRead.setVisibility(View.VISIBLE);
                        noRead.setText((messageBean.getO().getNoread()+"").replace(".0",""));
                    }
                }

            }

            @Override
            public void onError(okhttp3.Call call, Exception e, int i) {

            }

            @Override
            public void onFailure(C_CodeBean c_codeBean) {

            }
        });
        Map<String,String>  map=new HashMap<>();
        map.put("uid",LoginStatus.getToken());
        okHttpRequestModel.okhHttpPost(url,map,new MessageBean());
//        call = AppApi.get().getNoReadMessage(LoginStatus.getToken());
//        call.enqueue(new RequestCallback<NotReadMessage>() {
//            @Override
//            protected void onSuccess(NotReadMessage resp) {
//                int isReaf = resp.getO();
//                if (isReaf == 0) {
//                    noRead.setVisibility(View.VISIBLE);
//                } else {
//                    noRead.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            protected void onFailure(ResponseError error) {
//                super.onFailure(error);
//            }
//        });
    }

}
