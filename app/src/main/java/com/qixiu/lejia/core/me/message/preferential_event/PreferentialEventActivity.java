package com.qixiu.lejia.core.me.message.preferential_event;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

public class PreferentialEventActivity extends BaseWhiteStateBarActivity implements XRecyclerView.LoadingListener {
    private SwipeRefreshLayout swip_PreferentialEvent;
    private XRecyclerView xrecycler_PreferentialEvent;
    PreferentialAdapter adapter;

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_preferential_event, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        setTitle("优惠活动");
        initView(view);
    }

    private void initView(View view) {
        swip_PreferentialEvent=view.findViewById(R.id.swip_PreferentialEvent);
        xrecycler_PreferentialEvent=view.findViewById(R.id.xrecycler_PreferentialEvent);
        XrecyclerViewUtil.setXrecyclerView(xrecycler_PreferentialEvent,this,this,false,1,null);

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
}
