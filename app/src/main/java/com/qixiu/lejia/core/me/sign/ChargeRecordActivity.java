package com.qixiu.lejia.core.me.sign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ChargeRecordActivity extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener, XRecyclerView.LoadingListener {
    String url = BuildConfig.BASE_URL + "Home/Elemeter/getElemeterOrder";
    SwipeRefreshLayout swipRefresh;
    XRecyclerView recyclerView;
    OKHttpRequestModel okHttpRequestModel;
    int page = 1;
    private ChargeRecordAdapter adapter;

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_charge, null);
    }

    @Override
    protected void onContentViewCreated(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        swipRefresh = view.findViewById(R.id.swipRefresh);
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                requestData();
            }
        });
        okHttpRequestModel = new OKHttpRequestModel(this);
        XrecyclerViewUtil.setXrecyclerView(recyclerView, this, this, false, 1, null);
        adapter = new ChargeRecordAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", LoginStatus.getToken());
        map.put("page", page + "");
        okHttpRequestModel.okhHttpPost(url, map, new BaseBean());
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
        if (data instanceof RecordBean) {
            RecordBean bean = (RecordBean) data;
            adapter.refreshData(bean.getO());
        }
        recyclerView.loadMoreComplete();
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        recyclerView.loadMoreComplete();
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        recyclerView.loadMoreComplete();
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page++;
        requestData();
    }


    public class ChargeRecordAdapter extends RecyclerBaseAdapter {


        @Override
        public int getLayoutId() {
            return R.layout.item_charge_record;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new ChargeRecordHolder(itemView, context, this);
        }

        public class ChargeRecordHolder extends RecyclerBaseHolder {
            TextView textViewTitle, textViewRoomId,
                    textViewPosition, textViewTime,
                    textViewMoney;

            public ChargeRecordHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewRoomId = itemView.findViewById(R.id.textViewRoomId);
                textViewPosition = itemView.findViewById(R.id.textViewPosition);
                textViewTime = itemView.findViewById(R.id.textViewTime);
                textViewMoney = itemView.findViewById(R.id.textViewMoney);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof RecordBean.OBean) {
                    RecordBean.OBean bean = (RecordBean.OBean) mData;
                    textViewTitle.setText(bean.getSt_name());
                    textViewRoomId.setText(bean.getRo_number());
                    textViewPosition.setText(bean.getEquipment_name());
                    textViewTime.setText(bean.getPay_time());
                    textViewMoney.setText(bean.getPay_money());
                }
            }
        }
    }


    public class RecordBean extends BaseBean<List<RecordBean.OBean>> {


        public class OBean {
            /**
             * pay_money : 10.00
             * pay_time : 2019-01-24 13:35:51
             * pay_method : 1
             * st_name : 乐家小屋·第一季
             * ro_number : 测试220
             * equipment_type : elemeter
             * equipment_name : 测试1
             */

            private String pay_money;
            private String pay_time;
            private String pay_method;
            private String st_name;
            private String ro_number;
            private String equipment_type;
            private String equipment_name;

            public String getPay_money() {
                return pay_money;
            }

            public void setPay_money(String pay_money) {
                this.pay_money = pay_money;
            }

            public String getPay_time() {
                return pay_time;
            }

            public void setPay_time(String pay_time) {
                this.pay_time = pay_time;
            }

            public String getPay_method() {
                return pay_method;
            }

            public void setPay_method(String pay_method) {
                this.pay_method = pay_method;
            }

            public String getSt_name() {
                return st_name;
            }

            public void setSt_name(String st_name) {
                this.st_name = st_name;
            }

            public String getRo_number() {
                return ro_number;
            }

            public void setRo_number(String ro_number) {
                this.ro_number = ro_number;
            }

            public String getEquipment_type() {
                return equipment_type;
            }

            public void setEquipment_type(String equipment_type) {
                this.equipment_type = equipment_type;
            }

            public String getEquipment_name() {
                return equipment_name;
            }

            public void setEquipment_name(String equipment_name) {
                this.equipment_name = equipment_name;
            }
        }
    }
}
