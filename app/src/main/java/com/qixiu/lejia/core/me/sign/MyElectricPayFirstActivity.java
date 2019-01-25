package com.qixiu.lejia.core.me.sign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.OnRecyclerItemListener;
import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class MyElectricPayFirstActivity extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener, OnRecyclerItemListener {
    String url = BuildConfig.BASE_URL + "Home/Elemeter/get_user_equipment";
    RecyclerView recyclerview;
    SwipeRefreshLayout swipRefresh;
    OKHttpRequestModel okHttpRequestModel;
    private PayAdapter adapter;

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_electric_pay_first, null);
    }

    @Override
    protected void onContentViewCreated(View view) {
        setTitle("生活缴费");
        recyclerview = view.findViewById(R.id.recyclerview);
        swipRefresh = view.findViewById(R.id.swipRefresh);
        adapter = new PayAdapter();
        recyclerview.setAdapter(adapter);
        okHttpRequestModel = new OKHttpRequestModel(this);
        adapter.setOnItemClickListener(this);
        requestData();
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
        //设置右边
        setRightText();
    }

    private void setRightText() {
        getRightText().setText("充值记录");
        getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargeRecordActivity.start(getContext(), ChargeRecordActivity.class);
            }
        });
        getRightText().setVisibility(View.VISIBLE);
    }

    private void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", LoginStatus.getToken());
        okHttpRequestModel.okhHttpPost(url, map, new PayListBean());
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        switchToContentState();
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof PayListBean) {
            PayListBean bean = (PayListBean) data;
            adapter.refreshData(bean.getO());
        }
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(View v, Object data) {
        if (data instanceof PayListBean.OBean) {
            PayListBean.OBean bean = (PayListBean.OBean) data;
            MyElectictPayActivity.start(getContext(), MyElectictPayActivity.class, bean);

        }
    }


    public class PayAdapter extends RecyclerBaseAdapter {

        @Override
        public int getLayoutId() {
            return R.layout.item_pay_electric;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new PayHolder(itemView, context, this);
        }

        public class PayHolder extends RecyclerBaseHolder {
            TextView textViewProjectName,
                    textViewRoomNum,
                    textViewSite;

            public PayHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
                textViewRoomNum = itemView.findViewById(R.id.textViewRoomNum);
                textViewSite = itemView.findViewById(R.id.textViewSite);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof PayListBean.OBean) {
                    PayListBean.OBean bean = (PayListBean.OBean) mData;
                    textViewProjectName.setText(bean.getStore_name());
                    textViewRoomNum.setText(bean.getRo_number());
                    textViewSite.setText(bean.getRo_number());
                }
            }
        }
    }


}
