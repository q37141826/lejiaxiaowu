package com.qixiu.lejia.core.service;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qixiu.adapter.BindableItem;
import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginEvent;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.SignedRoom;
import com.qixiu.lejia.beans.resp.LifeServiceResp;
import com.qixiu.lejia.common.Events;
import com.qixiu.lejia.common.ImageBindingAdapters;
import com.qixiu.lejia.core.service.complaint.ComplaintsAct;
import com.qixiu.lejia.core.service.qa.QuestionsAct;
import com.qixiu.lejia.core.service.rent.RentAct;
import com.qixiu.lejia.core.service.repair.RepairsAct;
import com.qixiu.lejia.core.service.we.WaterAndElectricityAct;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.items.AbcDividerItem;
import com.qixiu.lejia.items.AbcSpaceItem;
import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.lejia.utils.DensityUtils;
import com.qixiu.lejia.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     生活服务
 * </pre>
 */
public class LifeServicesFragment extends Fragment implements Observer, LifeServicesView {

    private static final String TAG = "LifeServicesFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mSignedRoom;

    private BindableRecyclerAdapter mAdapter;

    private Call mCall;

    private List<BindableItem> mItems = new ArrayList<>();
    private ServiceHeaderItem mHeaderItem = ServiceHeaderItem.newInstance();

    public static LifeServicesFragment newInstance() {
        return new LifeServicesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginEvent.getInstance().addObserver(this);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_services, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setProgressViewOffset(true,
                DensityUtils.dip2px(18), DensityUtils.dip2px(72));
        //refresh
        mRefreshLayout.setOnRefreshListener(() -> {
//            load();
//            if (LoginStatus.isLogged()) {
//                loadSignedRoom();
//            }
            mRefreshLayout.setRefreshing(false);
        });
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new ServiceItemDecoration(getContext()));

        mSignedRoom = view.findViewById(R.id.location);
        if (!LoginStatus.isLogged()) {
            mSignedRoom.setText(R.string.service_not_settled);
        } else {
            loadSignedRoom();
        }

        view.findViewById(R.id.user_type)
                .setOnClickListener(v -> WelcomeSettledAct.start(getContext()));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
        //加载数据
        load();

    }

    private void setView() {
        //头部
        mItems.add(ServiceHeaderItem.newInstance());
        //推荐标题
        mItems.add(ServiceRecommendTitleItem.newInstance());
        //间隔
        mItems.add(AbcSpaceItem.newInstance());
        //脚部
        mItems.add(ServiceFooterItem.newInstance());

        mAdapter = new BindableRecyclerAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        CallUtil.cancel(mCall);
        LoginEvent.getInstance().deleteObserver(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 加载数据
     */
    @SuppressWarnings("unchecked")
    private void load() {
        mCall = AppApi.get().lifeService();
        mCall.enqueue(new RequestCallback<LifeServiceResp>() {
            @Override
            protected void onSuccess(LifeServiceResp resp) {
                handleResp(resp);
            }

            @Override
            protected void onComplete() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void handleResp(LifeServiceResp resp) {

        //清除所有
        mItems.clear();

        //顶部图片
        String image = resp.getTopBanner().getImage();
        mHeaderItem.setBanner(ImageBindingAdapters.jointImageUrl(image));
        mItems.add(mHeaderItem);

        //推荐服务
        List<LifeServiceResp.RecommendService> services = resp.getRecommendServices();
        List<ServiceRecommendItem> newItems = ServiceRecommendItem.of(services);

        //间隔
        mItems.add(AbcDividerItem.newInstance());
        //为你推荐标题
        mItems.add(ServiceRecommendTitleItem.newInstance());
        if (newItems.size() > 0) {
            mItems.addAll(newItems);
            mItems.add(AbcSpaceItem.newInstance());
        }
        mItems.add(ServiceFooterItem.newInstance());
        //reset adapter
        mAdapter.setItemActionHandler(new LifeServiceActionHandler(this));
        mAdapter.onReset(mItems);
    }

    /**
     * 获取用户已签约的房间名称
     */
    @SuppressWarnings("unchecked")
    private void loadSignedRoom() {
        mCall = AppApi.get().signedRoom(LoginStatus.getToken());
        mCall.enqueue(new RequestCallback<SignedRoom>() {
            @Override
            protected void onSuccess(SignedRoom room) {
                mSignedRoom.setText(room.getName());
            }

            @Override
            protected void onFailure(ResponseError error) {
                super.onFailure(error);
                mSignedRoom.setText(R.string.service_not_settled);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        int event = (int) arg;
        if (event == LoginEvent.Event.LOGIN) {
            loadSignedRoom();
        } else {
            mSignedRoom.setText(R.string.service_not_settled);
        }
    }

    @Override
    public void startRecommendDetail(LifeServiceResp.RecommendService service) {
        String url = null;
        int type = service.getType();
        if (type == 2) {
            //外链
            url = service.getLink();
        } else if (type == 1) {
            //内链
            if (service.getTitle().contains("乐购")) {
                //区别对待
                url = BuildConfig.BASE_H5_URL2 + "/lgindex.html?lgid=" + 1;
            } else {
//                Map<String, String> params = new HashMap<>();
//                params.put("rd_id", service.getId());
//                url = ApiConstants.buildUrl(ApiConstants.RECOMMEND, params);
                url = BuildConfig.BASE_H5_URL2 + "/lgindex.html?lgid=" + 4;
            }
        }

        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort(getContext(), "链接未设置");
            return;
        }

        WebActivity.start(getContext(), service.getTitle(), url);
    }

    @Override
    public void startRent() {
        if (LoginStatus.verifiedLogin(getContext()) && isSigned()) {
            RentAct.start(getContext());
        }
    }

    @Override
    public void startHydroelectric() {
        if (LoginStatus.verifiedLogin(getContext()) && isSigned()) {
            WaterAndElectricityAct.start(getContext());
        }
    }

    @Override
    public void startRepair() {
        if (LoginStatus.verifiedLogin(getContext()) && isSigned()) {
            RepairsAct.start(getContext());
        }
    }

    @Override
    public void startQuestion() {
        QuestionsAct.start(getContext());
    }

    @Override
    public void startComplaint() {
        ComplaintsAct.start(getContext());
    }

    @Subscribe
    public void signedSuccess(Events.SignedSuccessEvent event) {
        loadSignedRoom();
    }

    private boolean isSigned() {
        String s = mSignedRoom.getText().toString();
        String s1 = getString(R.string.service_not_settled);
        if (s.equals(s1)) {
            ToastUtils.showShort(getContext(), "入住房间之后才能查看~");
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        //6.0以上使用黑色状态栏图标
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(Color.WHITE);
        } else {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
