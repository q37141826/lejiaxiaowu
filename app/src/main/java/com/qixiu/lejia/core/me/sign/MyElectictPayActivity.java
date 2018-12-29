package com.qixiu.lejia.core.me.sign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.core.service.bill.BillAct;

import java.util.ArrayList;
import java.util.List;

public class MyElectictPayActivity extends BaseWhiteStateBarActivity {
    ElectricPayAdapter adapter;
    RecyclerView recyclerview;
    Button btnCharge;

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_electict_pay, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        adapter = new ElectricPayAdapter();
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        btnCharge = view.findViewById(R.id.btnCharge);
        setTitle("生活缴费");
        getRightText().setVisibility(View.VISIBLE);
        getRightText().setText("充值记录");
        getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillAct.start(getContext());
            }
        });
        List<ElecPayBean.OBean> datas = new ArrayList<>();
        ElecPayBean.OBean
                bean = new ElecPayBean.OBean();
        bean.setName("缴费单位");
        bean.setValue("电力公司");
        datas.add(bean);
        bean = new ElecPayBean.OBean();
        bean.setName("缴费户号");
        bean.setValue("wkcshy-20-2-22-4");
        datas.add(bean);
        bean = new ElecPayBean.OBean();
        bean.setName("户名");
        bean.setValue("真实姓名");
        datas.add(bean);
                bean = new ElecPayBean.OBean();
        bean.setName("当前可用余额");
        bean.setValue("0");
        datas.add(bean);
        bean = new ElecPayBean.OBean();
        bean.setName("当前欠费金额");
        bean.setValue("0");
        datas.add(bean);
        adapter.refreshData(datas);
    }


    @Override
    protected void onReload() {

    }


    public class ElectricPayAdapter extends RecyclerBaseAdapter {

        @Override
        public int getLayoutId() {
            return R.layout.item_electicpay;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new ElectricPayHolder(itemView, context, this);
        }

        public class ElectricPayHolder extends RecyclerBaseHolder {
           TextView textViewName,
                    textViewValue;
            public ElectricPayHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewName=itemView.findViewById(R.id.textViewName);
                textViewValue=itemView.findViewById(R.id.textViewValue);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof ElecPayBean.OBean){
                    ElecPayBean.OBean bean= (ElecPayBean.OBean) mData;
                    textViewName.setText(bean.getName());
                    textViewValue.setText(bean.getValue());
                }

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        switchToContentState();
    }
}
