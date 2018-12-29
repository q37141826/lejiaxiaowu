package com.qixiu.lejia.core.home.home_story;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.adapter.BindableItem;
import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.items.AbcSpaceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeStoryActivity extends BaseWhiteStateBarActivity implements XRecyclerView.LoadingListener {
    private BindableRecyclerAdapter mAdapter;
    private SwipeRefreshLayout swip_home_story;
    private RecyclerView recyclerview_home_story;

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeStoryActivity.class);
        context.startActivity(starter);
    }


    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_home_story, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        setTitle("乐家客故事");
        recyclerview_home_story =findViewById(R.id.recyclerview_home_story);
        recyclerview_home_story.setLayoutManager(new LinearLayoutManager(this));
        swip_home_story=findViewById(R.id.swip_home_story);
        List<BindableItem> items = Collections.singletonList(AbcSpaceItem.newInstance());
        mAdapter=new BindableRecyclerAdapter(items);
        recyclerview_home_story.setAdapter(mAdapter);
        getData();
    }

    @Override
    protected void onReload() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public void getData(){
        List<HomeStroryBean> datas=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            HomeStroryBean bean=new HomeStroryBean();
            bean.setContent("测试"+(i+1));
            bean.setImag("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532081625656&di=df11679e24225fbd008745e7880e48ba&imgtype=0&src=http%3A%2F%2Fs9.sinaimg.cn%2Fmw690%2F002b8u0Rgy6QLq1bCw898%26690");
            bean.setTitle("标题"+(i+1));
            datas.add(bean);
        }
        mAdapter.onReset(datas);
        switchToContentState();
    }
}
