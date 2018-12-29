package com.qixiu.lejia.core.me.message.notice;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.adapter.myrecycler.OnRecyclerItemListener;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.mine.notice.BillNoticeBean;
import com.qixiu.lejia.beans.mine.notice.DiscountBean;
import com.qixiu.lejia.beans.mine.notice.SystemNoticeBean;
import com.qixiu.lejia.core.me.message.MessageListActivity;
import com.qixiu.lejia.core.service.rent.RentAct;
import com.qixiu.lejia.core.service.repair.RepairsAct;
import com.qixiu.lejia.core.service.we.WaterAndElectricityAct;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.qixiu.lejia.core.me.message.MessageListActivity.BILL_NOTICE;
import static com.qixiu.lejia.core.me.message.MessageListActivity.SYSTEM_NOTICE;

public class NoticeActivity extends BaseWhiteStateBarActivity implements XRecyclerView.LoadingListener, OKHttpUIUpdataListener, OnRecyclerItemListener {
    SwipeRefreshLayout swip_notice;
    XRecyclerView xrecycler_notice;
    NoticeAdapter adapter;
    //
    private String systemNoticeUrl = BuildConfig.BASE_URL + "/Home/UserCenter/informlist";
    private String billNoticeUrl = BuildConfig.BASE_URL + "/Home/UserCenter/billlist";
    private String eventNoticeUrl = BuildConfig.BASE_URL + "/Home/UserCenter/discount";
    private String id;
    private String title;
    private String url;
    private int pageNo = 1, pageSize = 10;
    private String nc_id = 9 + "";

    private OKHttpRequestModel okHttpRequestModel;
    BaseBean bean = new BaseBean();

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_details, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        initView(view);
        initListenner();
        initData();
        adapter.setOnItemClickListener(this);
    }

    private void initData() {
        Map<String, String> map = new HashMap();
        map.put("uid",LoginStatus.getToken()+"");
        map.put("nc_id", nc_id);
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        okHttpRequestModel.okhHttpPost(url, map, bean);

    }

    private void initListenner() {
        swip_notice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                initData();
            }
        });
    }

    private void initView(View view) {
        okHttpRequestModel = new OKHttpRequestModel(this);
        id = getIntent().getStringExtra(MessageListActivity.ID);
        title = getIntent().getStringExtra(MessageListActivity.TITLE);
        if (title.contains(SYSTEM_NOTICE)) {
            url = systemNoticeUrl;
            bean = new SystemNoticeBean();
            nc_id = 9 + "";
            setTitle("通知");
        } else if (title.contains(BILL_NOTICE)) {
            url = billNoticeUrl;
            nc_id = 8 + "";
            bean = new BillNoticeBean();
            setTitle("账单提醒");
        } else {
            bean = new DiscountBean();
            nc_id = 10 + "";
            url = eventNoticeUrl;
            setTitle("优惠活动");
        }
        xrecycler_notice = view.findViewById(R.id.xrecycler_notice);
        XrecyclerViewUtil.setXrecyclerView(xrecycler_notice, this, this, false, 1, null);
        swip_notice = view.findViewById(R.id.swip_notice);
        adapter = new NoticeAdapter();
        xrecycler_notice.setAdapter(adapter);
    }

    @Override
    protected void onReload() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        pageNo++;
        initData();
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof SystemNoticeBean) {
            SystemNoticeBean bean = (SystemNoticeBean) data;
            if (pageNo == 1) {
                adapter.refreshData(bean.getO().getList());
            } else {
                adapter.addDatas(bean.getO().getList());
            }

        }
        if (data instanceof BillNoticeBean) {
            BillNoticeBean bean = (BillNoticeBean) data;
            if (pageNo == 1) {
                adapter.refreshData(bean.getO().getList());
            } else {
                adapter.addDatas(bean.getO().getList());
            }
        }
        if (data instanceof DiscountBean) {
            DiscountBean bean = (DiscountBean) data;
            if (pageNo == 1) {
                adapter.refreshData(bean.getO().getList());
            } else {
                adapter.addDatas(bean.getO().getList());
            }
        }
        swip_notice.setRefreshing(false);
        xrecycler_notice.loadMoreComplete();
        switchToContentState();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToContentState();
        swip_notice.setRefreshing(false);
        xrecycler_notice.loadMoreComplete();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        switchToContentState();
        swip_notice.setRefreshing(false);
        xrecycler_notice.loadMoreComplete();
    }

    @Override
    public void onItemClick(View v, Object data) {
        if (data instanceof BillNoticeBean.OBean.ListBean) {
            BillNoticeBean.OBean.ListBean listBean = (BillNoticeBean.OBean.ListBean) data;
            if (listBean.getClassName().equals(RentAct.class.getSimpleName())) {
                RentAct.start(this);
            } else {
                WaterAndElectricityAct.start(this);
            }
        }
        if (data instanceof SystemNoticeBean.OBean.ListBean) {
            SystemNoticeBean.OBean.ListBean listBean = (SystemNoticeBean.OBean.ListBean) data;
            String systemNoticeKey[] = {"1", "2", "3", "4"};
            if (systemNoticeKey[0].equals(listBean.getType())) {
                startSigned();
            } else if (systemNoticeKey[1].equals(listBean.getType())) {
                startAppointment();
            } else {
                RepairsAct.start(this);
            }
        }

    }


    public void startSigned() {
        String url = ApiConstants.buildUrl(ApiConstants.SIGNED, null);
        WebActivity.start(this, getString(R.string.me_sign), url);
    }

    public void startAppointment() {
        String url = ApiConstants.buildUrl(ApiConstants.APPOINTMENT, null);
        WebActivity.start(this, getString(R.string.me_appointment), url);
    }
}
