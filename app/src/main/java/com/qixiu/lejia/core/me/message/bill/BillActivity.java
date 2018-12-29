package com.qixiu.lejia.core.me.message.bill;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

public class BillActivity extends BaseWhiteStateBarActivity implements XRecyclerView.LoadingListener {
    private SwipeRefreshLayout swip_bill;
    private XRecyclerView xrecycler_bill;

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_bill, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        setTitle("账单提醒");
        initView(view);
    }

    private void initView(View view) {
        swip_bill = view.findViewById(R.id.swip_bill);
        xrecycler_bill = view. findViewById(R.id.xrecycler_bill);
        XrecyclerViewUtil.setXrecyclerView(xrecycler_bill,this,this,false,1,null);

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
