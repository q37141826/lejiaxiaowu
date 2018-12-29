package com.qixiu.lejia.core.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.ApiConstants;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.ShareInfo;
import com.qixiu.lejia.beans.ShopDetail;
import com.qixiu.lejia.common.ImageBindingAdapters;
import com.qixiu.lejia.common.SharePanel;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.lejia.utils.DensityUtils;
import com.qixiu.lejia.utils.IntentUtils;
import com.qixiu.widget.CircleImageView;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Long on 2018/5/30
 * <pre>
 *     门店详情
 * </pre>
 */
public class ShopDetailAct extends BaseShareCallbackAct implements AMap.InfoWindowAdapter {

    private static final String KEY_SHOP_ID = "SHOP_ID";

    private View mContent;
    private View mBottomBar;
    private RecyclerView mRecyclerView;
    private TextureMapView mapView;            //地图
    private View mLoading;
    private View mReload;

    private TextView mShopNameView;
    private TextView mShopSpecView;
    private TextView mShopIntroView;
    private TextView mManagerNameView;
    private ImageView mManagerAvatar;
    private TextView mManagerIntroView;
    private TextView mManagerPhoneView;

    //门店图片展示帮助类
    private ShopImagesHelper mShopImagesHelper;

    private Call mCall;

    private AMap map;

    //响应数据实体
    private ShopDetail mShopDetailResp;

    private TextView daysTv, hoursTv, minutesTv, secondsTv;
    private long mDay = 10;
    private long mHour = 10;
    private long mMin = 30;
    private long mSecond = 00;// 天 ,小时,分钟,秒

    private boolean isRun = true;

    private MapContainer map_container;

    private NestedScrollView scrollView;

    public static void start(Context context, String shopId, String img) {
        Intent starter = new Intent(context, ShopDetailAct.class);
        starter.putExtra(KEY_SHOP_ID, shopId);
        starter.putExtra("img", img);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.act_shop_detail);

        //init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        map_container = (MapContainer) findViewById(R.id.map_container);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        map_container.setScrollView(scrollView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        mShopImagesHelper = new ShopImagesHelper();
        mShopImagesHelper.onAttach(this);

        //setup map
        setupMap();
        //加载地图
        mapView.onCreate(savedInstanceState);

        //加载数据
        loadShopDetail();

    }

    private void initViews() {
        mContent = findViewById(R.id.content);
        mBottomBar = findViewById(R.id.bottom_bar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mapView = findViewById(R.id.map);
        map = mapView.getMap();
        mLoading = findViewById(R.id.indicator);
        mReload = findViewById(R.id.reload);

        mShopNameView = findViewById(R.id.shop_name);
        mShopSpecView = findViewById(R.id.shop_spec);
        mShopIntroView = findViewById(R.id.shop_intro);
        mManagerAvatar = findViewById(R.id.manager_avatar);
        mManagerNameView = findViewById(R.id.manager_name);
        mManagerIntroView = findViewById(R.id.manager_intro);
        mManagerPhoneView = findViewById(R.id.manager_phone);

        //set recyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addItemDecoration(new RoomDivider());

        //set click listener
        mReload.setOnClickListener(v -> {
            mReload.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            //重新加载数据
            loadShopDetail();
        });

        findViewById(R.id.appointment).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                //预约看房
                        if(LoginStatus.verifiedLogin(getContext())){
                            AppointmentAct.start(getContext(), getIntent().getStringExtra(KEY_SHOP_ID));
                        }
                    }
                });

        findViewById(R.id.sign).setOnClickListener(v -> {
            //立即签约
            SelectRoomDialogFrag.newInstance(mShopDetailResp.getRooms())
                    .show(getSupportFragmentManager(), null);
        });
        mManagerPhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(() -> new AlertDialog.Builder(ShopDetailAct.this)
                        .setTitle("警告")
                        .setMessage("是否前往拨号界面?")
                        .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                IntentUtils.startDial(ShopDetailAct.this, mManagerPhoneView.getText().toString()))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show());
            }
        });
    }

    //地图设置
    private void setupMap() {
        UiSettings settings = map.getUiSettings();
        //隐藏缩放按钮
        settings.setZoomControlsEnabled(false);
        //禁用手势
//        settings.setAllGesturesEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                //分享
                ShareInfo shareInfo = new ShareInfo();
                shareInfo.setTitle(mShopDetailResp.getShopInfo().getName());
                shareInfo.setContent(mShopDetailResp.getShopInfo().getIntro());
                Map<String, String> param = new HashMap<>();
                param.put("st_id", getIntent().getStringExtra(KEY_SHOP_ID));
                String url = ApiConstants.buildUrl(ApiConstants.HOUSE_INTRO, param);
                shareInfo.setShareUrl(url);
                List<ShopDetail.ShopImages> shopImages = mShopDetailResp.getShopImages();
                if (!shopImages.isEmpty()) {
                    List<String> images = shopImages.get(0).getImages();
                    if (!images.isEmpty()) {
                        shareInfo.setShareImageUrl(images.get(0));
                    }
                }

                SharePanel.newInstance(shareInfo).show(getSupportFragmentManager());

                break;
            case R.id.message:
                //消息
                String messageUrl = ApiConstants.buildUrl(ApiConstants.MESSAGE, null);
                WebActivity.start(this, "消息", messageUrl);
                break;
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
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        CallUtil.cancel(mCall);
        mapView.onDestroy();
        super.onDestroy();
    }

    //加载数据
    @SuppressWarnings("unchecked")
    private void loadShopDetail() {
        String shopId = getIntent().getStringExtra(KEY_SHOP_ID);
        mCall = AppApi.get().shopDetail(shopId);
        mCall.enqueue(new RequestCallback<ShopDetail>() {
            @Override
            protected void onSuccess(ShopDetail resp) {
                mContent.setVisibility(View.VISIBLE);
                mBottomBar.setVisibility(View.VISIBLE);
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

    //处理响应数据
    private void handleResp(ShopDetail resp) {
        map.setInfoWindowAdapter(this);//自定义弹出窗体
        //保存响应数据
        mShopDetailResp = resp;

        mShopImagesHelper.onDisplay(resp.getShopImages());

        ShopDetail.ShopInfo shopInfo = resp.getShopInfo();
        mShopNameView.setText(shopInfo.getName());
        String spec = getString(R.string.fmt_shop_spec, shopInfo.getRoomSpecNum(),
                shopInfo.getArea(), shopInfo.getSource());
        mShopSpecView.setText(spec);
        mShopIntroView.setText(shopInfo.getIntro());

        //管家头像
        ImageBindingAdapters.bindImage(mManagerAvatar, shopInfo.getManagerAvatar(), null,
                null, null);
        mManagerNameView.setText(shopInfo.getManager());
        mManagerIntroView.setText(shopInfo.getManagerIntro());
        mManagerPhoneView.setText(shopInfo.getManagerPhone());

        //推荐户型
        List<RecommendRoomItem> items = new ArrayList<>();
        for (int i = 0; i < resp.getRooms().size(); i++) {
            ShopDetail.RecommendRoom room = resp.getRooms().get(i);
            items.add(new RecommendRoomItem(room));
        }
//        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        TimeAdapter adapter = new TimeAdapter(ShopDetailAct.this, items);
//        adapter.setItemActionHandler(o -> {
//            //房间详情
//            RecommendRoomItem item = (RecommendRoomItem) o;
//            RoomDetailAct.start(this, item.getRoom().getId());
//        });
        mRecyclerView.setAdapter(adapter);

        //添加地图标记
        addMapMarker(shopInfo);

    }

    private void addMapMarker(ShopDetail.ShopInfo shopInfo) {
        LatLng latLng = new LatLng(shopInfo.getLat(), shopInfo.getLng());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)
                .draggable(false)
                .setFlat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_1))
                .title(shopInfo.getName());
//                .snippet(shopInfo.getLocation());
        Marker marker = map.addMarker(options);
        Glide.with(this).load(getIntent().getStringExtra("img"))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        //展示图片
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.marker_icon_notitle, null);
                        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.iv_head);
                        imageView.setImageDrawable(resource);
                        Bitmap bitmap = convertViewToBitmap(view);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }
                });
        marker.showInfoWindow();
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoContent = LayoutInflater.from(ShopDetailAct.this).inflate(
                R.layout.item_map_window, null);
        render(marker, infoContent, 2);
        return infoContent;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 对窗口信息赋值
     */
    public void render(Marker marker, View view, int mark) {
//        LinearLayout layout = (LinearLayout) view.findViewById(R.id.window_linearlayout);
//        //设置透明度
//        layout.getBackground().setAlpha(240);
        TextView name = (TextView) view.findViewById(R.id.window_name);
        name.setText(marker.getTitle());
    }


    static class RoomDivider extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = DensityUtils.dip2px(10);
        }
    }

}
