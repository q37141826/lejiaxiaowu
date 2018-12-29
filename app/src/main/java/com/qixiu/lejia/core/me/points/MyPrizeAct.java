package com.qixiu.lejia.core.me.points;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.adapter.myrecycler.OnRecyclerItemListener;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.PrizeSection;
import com.qixiu.lejia.beans.mine.points.PrizeBean;
import com.qixiu.lejia.utils.ToastUtil;
import com.qixiu.lejia.utils.XrecyclerViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MyPrizeAct extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener, XRecyclerView.LoadingListener, OnRecyclerItemListener {
    private String url = BuildConfig.BASE_URL + "/Home/Index/getuserdraw";
    private OKHttpRequestModel okHttpRequestModel;
    private XRecyclerView recycler_prize;
    private List<PrizeSection> list = new ArrayList();
    //    private MyPrizeAdapter myPrizeAdapter;
//    private TreeMap<String, List<PrizeDetail>> dataMap;
    private PrizeAdapter adapter;
    private SwipeRefreshLayout swip_refresh;

    public static void start(Context context) {
        Intent starter = new Intent(context, MyPrizeAct.class);
        context.startActivity(starter);
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_prize, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        okHttpRequestModel = new OKHttpRequestModel(this);
        recycler_prize = view.findViewById(R.id.recycler_prize);
        swip_refresh = findViewById(R.id.swip_refresh);
        XrecyclerViewUtil.setXrecyclerView(recycler_prize, this, this, false, 1, null);
//        myPrizeAdapter = new MyPrizeAdapter(R.layout.item_prize, R.layout.head_prize, list);
//        recycler_prize.setAdapter(myPrizeAdapter);
        adapter = new PrizeAdapter();
        recycler_prize.setAdapter(adapter);
        switchToContentState();
        setIntiData();
        swip_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setIntiData();
            }
        });
        adapter.setOnItemClickListener(this);
    }

    private void setIntiData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());// LoginStatus.getToken()//todo 调试完毕之后改回去 2381+""
        okHttpRequestModel.okhHttpPost(url, map, new PrizeBean());
//
//
//
//
//
//        dataMap = new TreeMap<>( new Comparator<String>() {
//            public int compare(String obj1, String obj2) {
//                // 降序排序
//                return obj2.compareTo(obj1);
//            }
//        });
//        List<PrizeDetail> sampleData = test.getSampleData();
//
//        for (PrizeDetail sampleDatum : sampleData) {
//            Log.e("mtag","getAge"+sampleDatum.getAge()+"getName"+sampleDatum.getName()+"getTime"+sampleDatum.getTime());
//
//            String time = sampleDatum.getTime();
//            if (!dataMap.containsKey(time)) {
//                List<PrizeDetail> lista = new ArrayList<>();
//                lista.add(sampleDatum);
//                dataMap.put(time, lista);
//            } else {
//                dataMap.get(time).add(sampleDatum);
//            }
//        }
//        Iterator<Map.Entry<String, List<PrizeDetail>>> it = dataMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, List<PrizeDetail>> entry = it.next();
//            if (!TextUtils.isEmpty(entry.getKey())) {
//                list.add(new PrizeSection(true, entry.getKey().toString()));
//                for (PrizeDetail prizeDetail : entry.getValue()) {
//                    list.add(new PrizeSection(prizeDetail));
//                }
//            }
//        }

    }

    @Override
    protected void onReload() {
        //切换到显示的切面
        switchToContentState();
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof PrizeBean) {
            PrizeBean bean = (PrizeBean) data;
            adapter.refreshData(bean.getO());
        }
        switchToContentState();
        swip_refresh.setRefreshing(false);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToContentState();
        swip_refresh.setRefreshing(false);

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast(c_codeBean.getM());
        switchToContentState();
        swip_refresh.setRefreshing(false);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(View v, Object data) {

    }
}
