package com.qixiu.lejia.core.me.points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.PointSection;
import com.qixiu.lejia.beans.mine.points.PointsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 积分明细修改版
 */
public class PointsDetailNewAct extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener {
    private String url = BuildConfig.BASE_URL + "Home/Repair/get_integral_info";
    private RecyclerView recyclerViewAdd;
    private List<PointSection> mDatas = new ArrayList();
    private OKHttpRequestModel okHttpRequestModel;
    private RecyclerView recycler_view_reduce;
    private PointsDetailsAdapter adapterAdd, adapterReduce;
    private LinearLayout linearLayout_add, linearLayout_reduce;


    public static void start(Context context) {
        Intent starter = new Intent(context, PointsDetailNewAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_newpoints_detail, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        linearLayout_add=findViewById(R.id.linearLayout_add);
        linearLayout_reduce=findViewById(R.id.linearLayout_reduce);
        okHttpRequestModel = new OKHttpRequestModel(this);
        recyclerViewAdd = view.findViewById(R.id.recycler_view_add);
        recycler_view_reduce = view.findViewById(R.id.recycler_view_reduce);
        recycler_view_reduce.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdd.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_offset_start_16));
        recyclerViewAdd.addItemDecoration(divider);
        adapterAdd = new PointsDetailsAdapter();
        adapterAdd.setState(PointsDetailsAdapter.ADD);

        adapterReduce = new PointsDetailsAdapter();
        adapterReduce.setState(PointsDetailsAdapter.REDUCE);
        recyclerViewAdd.setAdapter(adapterAdd);
        recycler_view_reduce.setAdapter(adapterReduce);

    }

    @Override
    protected void onReload() {
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
//        call = AppApi.get().pointsDetail(LoginStatus.getToken());
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());//
        okHttpRequestModel.okhHttpPost(url, map, new PointsBean());


//        call.enqueue(new RequestCallback<List<PointsHistory>>() {
//            @Override
//            protected void onSuccess(List<PointsHistory> list) {
//                if (list.isEmpty()) {
//                    switchToEmptyState();
//                    return;
//                }
//                switchToContentState();
//                getData(list);
////                getData(test.getTextData());
//                MyPointsDetailNewAdapter myPointsDetailNewAdapter = new MyPointsDetailNewAdapter(R.layout.item_point,
//                        R.layout.item_point_herder, mDatas);
//
//               BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(PointsDetailItem.from(list));
//                recyclerViewAdd.setAdapter(adapter);
////                recyclerViewAdd.setAdapter(myPointsDetailNewAdapter);
//            }
//
//            @Override
//            protected void onFailure(ResponseError error) {
//                switchToErrorState();
//            }
//        });
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof PointsBean) {
            PointsBean bean = (PointsBean) data;
            adapterReduce.refreshData(bean.getO().getReduce());
            adapterAdd.refreshData(bean.getO().getAdd());
            if(bean.getO().getAdd().size()==0){
                linearLayout_add.setVisibility(View.GONE);
            }else {
                linearLayout_add.setVisibility(View.VISIBLE);
            }
            if(bean.getO().getReduce().size()==0){
                linearLayout_reduce.setVisibility(View.GONE);
            }else {
                linearLayout_reduce.setVisibility(View.VISIBLE);
            }
        }
        switchToContentState();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToContentState();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        switchToContentState();
    }

//    private void getData(List<PointsHistory> list) {
//        TreeMap<String, List<PointsHistory>> mDataMap = new TreeMap<>( new Comparator<String>() {
//            public int compare(String obj1, String obj2) {
//                // 降序排序
//                return obj2.compareTo(obj1);
//            }
//        });
//        for (PointsHistory pointsHistory : list) {
//            String type = pointsHistory.getType() + "";
//            if (!mDataMap.containsKey(type)) {
//                List<PointsHistory> mList = new ArrayList<>();
//                mList.add(pointsHistory);
//                mDataMap.put(type, mList);
//            } else {
//                mDataMap.get(type).add(pointsHistory);
//            }
//        }
//        Iterator<Map.Entry<String, List<PointsHistory>>> iterator = mDataMap.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, List<PointsHistory>> entry = iterator.next();
//            if (!TextUtils.isEmpty(entry.getKey())) {
//                if (entry.getKey().equals("1")) {
//                    mDatas.add(new PointSection(true, getResources().getString(R.string.got_get_points)));
//                } else {
//                    mDatas.add(new PointSection(true, getResources().getString(R.string.got_down_points)));
//                }
//                for (PointsHistory pointsHistory : entry.getValue()) {
//                    mDatas.add(new PointSection(pointsHistory));
//                    Log.d("mtag", "pointsHistory: "+pointsHistory.getWay());
//                }
//            }
//        }
//
//    }


}
