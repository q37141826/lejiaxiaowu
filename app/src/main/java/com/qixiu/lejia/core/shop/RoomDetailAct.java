package com.qixiu.lejia.core.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.RoomDetail;
import com.qixiu.lejia.beans.ShareInfo;
import com.qixiu.lejia.beans.ShopDetail;
import com.qixiu.lejia.beans.UserLevel;
import com.qixiu.lejia.common.Events;
import com.qixiu.lejia.common.SharePanel;
import com.qixiu.lejia.core.MainActivity;
import com.qixiu.lejia.core.login.LoginActivity;
import com.qixiu.lejia.core.sign.AuthenticationAct;
import com.qixiu.lejia.core.sign.UserType;
import com.qixiu.lejia.mvp.CallUtil;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Long on 2018/6/1
 * <pre>
 *     房间详情
 * </pre>
 */
public class RoomDetailAct extends BaseShareCallbackAct {

    private static final String KEY_ROOM_ID = "ROOM_ID";

    private View         mContent;
    private View         mSignBtn;
    private RecyclerView mRecyclerView;
    private View         mLoading;
    private View         mReload;

    private TextView mRoomNameView;
    private TextView mRoomSpecView;
    private TextView mRoomIntroView;
    private TextView mRoomRentView;

    //门店图片展示帮助类
    private ShopImagesHelper mShopImagesHelper;

    //网络请求
    private Call       mCall;
    //响应数据
    private RoomDetail mRoomDetail;

    public static void start(Context context, String roomId) {
        Intent starter = new Intent(context, RoomDetailAct.class);
        starter.putExtra(KEY_ROOM_ID, roomId);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.act_room_detail);

        //init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        mShopImagesHelper = new ShopImagesHelper();
        mShopImagesHelper.onAttach(this);

        //加载数据
        loadRoomDetail();

    }

    private void initViews() {
        mContent = findViewById(R.id.content);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLoading = findViewById(R.id.indicator);
        mReload = findViewById(R.id.reload);
        mSignBtn = findViewById(R.id.sign);

        mRoomNameView = findViewById(R.id.room_name);
        mRoomSpecView = findViewById(R.id.room_spec);
        mRoomIntroView = findViewById(R.id.room_intro);
        mRoomRentView = findViewById(R.id.room_rent);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        mSignBtn.setOnClickListener(v -> startAuthentication());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            //分享
            ShareInfo shareInfo = new ShareInfo();
            RoomDetail.RoomInfo roomInfo = mRoomDetail.getRoomInfo();
            shareInfo.setTitle(roomInfo.getShopName() + roomInfo.getName());
            shareInfo.setContent(roomInfo.getIntro());
            Map<String, String> param = new HashMap<>();
            param.put("ap_id", getIntent().getStringExtra(KEY_ROOM_ID));
            String url = ApiConstants.buildUrl(ApiConstants.HOUSE_DETAIL, param);
            shareInfo.setShareUrl(url);

            List<ShopDetail.ShopImages> shopImages = mRoomDetail.getRoomImages();
            if (!shopImages.isEmpty()) {
                List<String> images = shopImages.get(0).getImages();
                if (!images.isEmpty()) {
                    shareInfo.setShareImageUrl(images.get(0));
                }
            }
            //显示分享面板
            SharePanel.newInstance(shareInfo).show(getSupportFragmentManager());

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        CallUtil.cancel(mCall);
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private void loadRoomDetail() {
        String roomId = getIntent().getStringExtra(KEY_ROOM_ID);
        mCall = AppApi.get().roomDetail(roomId);
        mCall.enqueue(new RequestCallback<RoomDetail>() {
            @Override
            protected void onSuccess(RoomDetail resp) {
                mContent.setVisibility(View.VISIBLE);
                mSignBtn.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
                handleResp(resp);
            }

            @Override
            protected void onFailure(ResponseError error) {
                mLoading.setVisibility(View.GONE);
                mReload.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleResp(RoomDetail resp) {
        mRoomDetail = resp;

        mShopImagesHelper.onDisplay(resp.getRoomImages());

        RoomDetail.RoomInfo roomInfo = resp.getRoomInfo();
        String roomTitle = getString(R.string.fmt_room_title, roomInfo.getShopName(), roomInfo.getStyle());
        mRoomNameView.setText(roomTitle);

        String spec = getString(R.string.fmt_room_spec3, roomInfo.getName(), roomInfo.getArea());
        mRoomSpecView.setText(spec);

        String priceRange = getString(R.string.fmt_price_range, roomInfo.getLowestPrice(),
                roomInfo.getHighestPrice());
        mRoomRentView.setText(priceRange);

        mRoomIntroView.setText(roomInfo.getIntro());

        //房屋配置
        List<RoomConfigItem> items = new ArrayList<>();
        for (RoomDetail.RoomConfig config : resp.getConfigs()) {
            items.add(new RoomConfigItem(config));
        }
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        mRecyclerView.setAdapter(adapter);

    }

    @SuppressWarnings("unchecked")
    public void startAuthentication() {
        if (!LoginStatus.isLogged()) {
            LoginActivity.start(this);
            return;
        }
        mCall = AppApi.get().getUserType(LoginStatus.getToken());
        mCall.enqueue(new RequestCallback<UserLevel>() {
            @Override
            protected void onSuccess(UserLevel level) {
                if (level.getLevel() == 0) {
                    AuthenticationAct.start(RoomDetailAct.this, UserType.PERSONAL,mRoomDetail.getRoomInfo().getShopId());
                    return;
                }
                if (LoginStatus.verifiedLogin(RoomDetailAct.this)) {
                    Intent starter = new Intent(RoomDetailAct.this, MainActivity.class);
                    RoomDetailAct.this.startActivity(starter);
                    EventBus.getDefault().post(new  Events.CorporateHomePageEvent());
                }
                finish();
            }
        });
    }


}
