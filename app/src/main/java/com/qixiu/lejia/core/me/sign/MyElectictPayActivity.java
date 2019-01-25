package com.qixiu.lejia.core.me.sign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.core.service.BaseServicePayAct;
import com.qixiu.lejia.core.service.bill.BillAct;
import com.qixiu.lejia.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.qixiu.lejia.core.sign.BaseSignPayActivity.DATA;

public class MyElectictPayActivity extends BaseServicePayAct implements OKHttpUIUpdataListener {
    String url = BuildConfig.BASE_URL + "Home/Elemeter/get_elemeter_info";
    OKHttpRequestModel okHttpRequestModel;


    ElectricPayAdapter adapter;
    RecyclerView recyclerview;
    Button btnCharge;
    private View contentView;
    EditText edittextMoney;
    private ImageView imageViewWeichatSelect;
    private ImageView imageViewAliSelect;
    private PayListBean.OBean bean;

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_electict_pay, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        bean = getIntent().getParcelableExtra(DATA);

        Map<String, String> map = new HashMap<>();
        map.put("equipment_uuid", bean.getEquipment_uuid());
        map.put("yd_home_id", bean.getYd_home_id());
        map.put("store_id", bean.getStore_id());
        map.put("room_id", bean.getRoom_id());
        map.put("user_id", LoginStatus.getToken());
        okHttpRequestModel = new OKHttpRequestModel(this);
        okHttpRequestModel.okhHttpPost(url, map, new PayDetailsBean());


        adapter = new ElectricPayAdapter();
        recyclerview = view.findViewById(R.id.recyclerview);
        edittextMoney = view.findViewById(R.id.edittextMoney);
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


    }


    @Override
    protected void onReload() {

    }

    //去支付
    public void gopay(View view) {
        if (TextUtils.isEmpty(edittextMoney.getText().toString())) {
            ToastUtil.toast("请输入金额");
            return;
        }
        showPop(edittextMoney.getText().toString());
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof PayDetailsBean) {
            PayDetailsBean payDetailsBean = (PayDetailsBean) data;

            List<ElecPayInnerBean.OBean> datas = new ArrayList<>();
            ElecPayInnerBean.OBean
                    bean = new ElecPayInnerBean.OBean();
            bean.setName("门店名称");
            bean.setValue(payDetailsBean.getO().getStore_name());
            datas.add(bean);
            bean = new ElecPayInnerBean.OBean();
            bean.setName("房号");
            bean.setValue(payDetailsBean.getO().getRo_number());
            datas.add(bean);
            bean = new ElecPayInnerBean.OBean();
            bean.setName("剩余电量");
            bean.setValue(payDetailsBean.getO().getLeft() + "");
            datas.add(bean);
            bean = new ElecPayInnerBean.OBean();
            bean.setName("当前可用余额");
            bean.setValue("0");
            datas.add(bean);
            bean = new ElecPayInnerBean.OBean();
            bean.setName("当前欠费金额");
            bean.setValue("0");
            datas.add(bean);
            adapter.refreshData(datas);
        }


    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

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
                textViewName = itemView.findViewById(R.id.textViewName);
                textViewValue = itemView.findViewById(R.id.textViewValue);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof ElecPayInnerBean.OBean) {
                    ElecPayInnerBean.OBean bean = (ElecPayInnerBean.OBean) mData;
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


    //
    public void showPop(String money) {
        //根据字数计算每个条目的长度
        contentView = View.inflate(getContext(), R.layout.pop_pay, null);
        PopupWindow popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setClippingEnabled(false);
        RelativeLayout relative_weichat_pop = contentView.findViewById(R.id.relative_weichat_pop);
        RelativeLayout relative_alipay_pop = contentView.findViewById(R.id.relative_alipay_pop);
        imageViewWeichatSelect = contentView.findViewById(R.id.imageViewWeichatSelect);
        imageViewAliSelect = contentView.findViewById(R.id.imageViewAliSelect);
        Button btnPaySoon = contentView.findViewById(R.id.btnPaySoon);
        TextView textViewClosePop = contentView.findViewById(R.id.textViewClosePop);
        textViewClosePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        relative_alipay_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayState(1);
            }
        });
        relative_alipay_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayState(2);
            }
        });
    }

    public void setPayState(int payState) {
        if (payState == 1) {
            imageViewWeichatSelect.setImageResource(R.mipmap.no_selected2x);
            imageViewAliSelect.setImageResource(R.mipmap.select2x);
        } else {
            imageViewWeichatSelect.setImageResource(R.mipmap.select2x);
            imageViewAliSelect.setImageResource(R.mipmap.no_selected2x);
        }
    }

}
