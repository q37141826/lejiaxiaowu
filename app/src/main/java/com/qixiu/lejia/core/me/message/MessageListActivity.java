package com.qixiu.lejia.core.me.message;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.adapter.myrecycler.OnRecyclerItemListener;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.mine.MessageListBean;
import com.qixiu.lejia.core.me.message.notice.NoticeActivity;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
/*通知的接口Home/UserCenter/noticeclasslist
*
* */
public class MessageListActivity extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener, OnRecyclerItemListener, View.OnClickListener, XRecyclerView.LoadingListener {
    private XRecyclerView recyclerview_message_list;
    OKHttpRequestModel okHttpRequestModel;
    private SwipeRefreshLayout swip_message_list;
    private MessageListAdapter adapter;
    private Button btn_open_notice;
    public static  final String BILL_NOTICE="账单提醒",SYSTEM_NOTICE="通知提醒",EVENT_NOTICE="活动优惠";
    public static  final String ID="ID",TITLE="TITLE";
    public static void start(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }


    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_message_list, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        btn_open_notice = view.findViewById(R.id.btn_open_notice);
        okHttpRequestModel = new OKHttpRequestModel(this);
        setTitle("消息记录");
        recyclerview_message_list =  view.findViewById(R.id.recyclerview_message_list);
       XrecyclerViewUtil.setXrecyclerView(recyclerview_message_list,this,this,false,1,null);
        swip_message_list = view.findViewById(R.id.swip_message_list);
        swip_message_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        adapter = new MessageListAdapter();
        recyclerview_message_list.setAdapter(adapter);
        load();
        adapter.setOnItemClickListener(this);
        setListenner();
    }

    private void setListenner() {
        btn_open_notice.setOnClickListener(this);

    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken()); //LoginStatus.getToken()
        okHttpRequestModel.okhHttpPost(BuildConfig.BASE_URL + "Home/UserCenter/noticeclasslist", map, new MessageListBean());
    }

    @Override
    public void onSuccess(Object data, int i) {
        switchToContentState();
        if (data instanceof MessageListBean) {
            MessageListBean bean = (MessageListBean) data;
            adapter.refreshData(bean.getO());
        }
        swip_message_list.setRefreshing(false);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToErrorState();
        swip_message_list.setRefreshing(false);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        switchToContentState();
        swip_message_list.setRefreshing(false);

    }

    @Override
    public void onItemClick(View v, Object data) {
        if (data instanceof MessageListBean.OBean) {
            MessageListBean.OBean bean = (MessageListBean.OBean) data;
            Intent intent = new Intent();
            intent.setClass(this,NoticeActivity.class);
            intent.putExtra(ID, bean.getNe_id());
            intent.putExtra(TITLE, bean.getNc_name());
            MessageDetailsActivity.start(this, intent);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        load();
    }
}
