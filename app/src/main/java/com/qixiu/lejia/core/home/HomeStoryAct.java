package com.qixiu.lejia.core.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.adapter.myrecycler.OnRecyclerItemListener;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.home.story.StoryBean;
import com.qixiu.lejia.beans.home.story.StoryFatherBean;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


public class HomeStoryAct extends BaseWhiteStateBarActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, XRecyclerView.LoadingListener, OnRecyclerItemListener, OKHttpUIUpdataListener {
    public static String storyWebUrl = "ljhouse.app0.001/story-detail.html?gid=";
    private XRecyclerView recycler_story;
    private HomeStoryAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OKHttpRequestModel  okHttpRequestModel;
    private int pageNo=1,pageSize=10	;

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeStoryAct.class);
        context.startActivity(starter);
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_home_story, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        okHttpRequestModel=new OKHttpRequestModel(this);
        switchToContentState();
        recycler_story = view.findViewById(R.id.recycler_story);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        XrecyclerViewUtil.setXrecyclerView(recycler_story, this, this, false, 1, null);
        adapter = new HomeStoryAdapter();
        recycler_story.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);
        initData();

    }

    private void initData() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo=1;
                requstData();
            }
        });
        requstData();
        setTitle("更多乐家客故事");
    }

    private void requstData() {
        Map<String,String> map=new HashMap<>();
        map.put("pageNo",pageNo+"");
        map.put("pageSize",pageSize+"");
        okHttpRequestModel.okhHttpPost(BuildConfig.BASE_URL+"Home/Life/storylist",map,new StoryFatherBean());

//        call = AppApi.get().storylist();
//        call.enqueue(new RequestCallback<StoryBean>() {
//            @Override
//            protected void onSuccess(StoryBean datas) {
//                adapter.refreshData(datas.getList());
//                mSwipeRefreshLayout.setRefreshing(false);
//                recycler_story.loadMoreComplete();
//                switchToContentState();
//            }
//
//            @Override
//            protected void onFailure(ResponseError error) {
//                super.onFailure(error);
//                switchToContentState();
//                mSwipeRefreshLayout.setRefreshing(false);
//                recycler_story.loadMoreComplete();
//            }
//        });
    }


    @Override
    protected void onReload() {
        //切换到显示的切面
        switchToContentState();
    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {

    }

    //下拉刷新
    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        pageNo++;
        requstData();
    }

    @Override
    public void onItemClick(View v, Object data) {
        if (data instanceof StoryBean.ListBean) {
            StoryBean.ListBean bean = (StoryBean.ListBean) data;
            WebActivity.start(this, bean.getTitle(), BuildConfig.BASE_URL + storyWebUrl + bean.getId());
        }
    }

    @Override
    public void onSuccess(Object data, int i) {
        if(data instanceof StoryFatherBean ){
            StoryFatherBean bean= (StoryFatherBean) data;
            if(pageNo==1){
                adapter.refreshData(bean.getO().getList());
            }else {
                adapter.addDatas(bean.getO().getList());
            }
        }
        switchToContentState();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToContentState();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        mSwipeRefreshLayout.setRefreshing(false);
        switchToContentState();
    }
}
