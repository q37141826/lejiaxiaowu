package com.qixiu.lejia.core.shop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.base.BaseActivity;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.ShopLocation;
import com.qixiu.lejia.core.home.HomeItemDecorations;
import com.qixiu.lejia.core.home.HomeShopItem;
import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.widget.CircleImageView;
import com.qixiu.widget.MultiStateLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;

/**
 * Created by Long on 2018/5/31
 * <pre>
 *     门店地图展示
 * </pre>
 */
public class ShopMapDisplayAct extends BaseActivity implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {

    private static final String TAG = "ShopMapDisplayAct";

    private static final LatLng WUHAN = new LatLng(30.592935, 114.305215);

    private MultiStateLayout multiStateLayout;
    private RecyclerView     mRecyclerView;
    private MapView          mapView;
    private AMap             map;

    private Call mCall;

    private ArrayList<Marker> markers = new ArrayList<Marker>();

    private int moveWidth = 180;

    private int width;

    private List<ShopLocation> shopList = new ArrayList<ShopLocation>();

    public static void start(Context context) {
        Intent starter = new Intent(context, ShopMapDisplayAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置标记可以让内容在状态栏上显示
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_shop_map_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        mRecyclerView.addItemDecoration(HomeItemDecorations.shopsDivider(10).create(mRecyclerView));

        multiStateLayout = findViewById(R.id.multi_state_layout);
        multiStateLayout.setOnReloadClickListener(v -> loadShops());
        mapView = findViewById(R.id.map);
        map = mapView.getMap();
        //隐藏地图缩放按钮
        map.getUiSettings().setZoomControlsEnabled(false);

        //init map
        mapView.onCreate(savedInstanceState);

        //load data
        loadShops();

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        CallUtil.cancel(mCall);
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private void loadShops() {
        mCall = AppApi.get().allShops();
        mCall.enqueue(new RequestCallback<List<ShopLocation>>() {
            @Override
            protected void onSuccess(List<ShopLocation> list) {
                multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_CONTENT);
                shopList = list;
                handleResp(list);
            }

            @Override
            protected void onFailure(ResponseError error) {
                multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_ERROR);
            }
        });

    }

    private void handleResp(List<ShopLocation> list) {
        map.setOnMarkerClickListener(this);
        map.setInfoWindowAdapter(this);//自定义弹出窗体
        if (list.isEmpty()) {
            multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_EMPTY);
            return;
        }
        List<Shop> shops = new ArrayList<>();
        for (ShopLocation shopLocation : list) {
            shops.add(shopLocation.getShop());
        }
        List<HomeShopItem> shopItems = HomeShopItem.of(shops);
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(shopItems);
        adapter.setItemActionHandler(o -> {
            Shop shop = (Shop) o;
            ShopDetailAct.start(this, shop.getId(), shop.getImage());
        });
        mRecyclerView.setAdapter(adapter);

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        width = dm.widthPixels;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int adapterNowPos = l.findFirstVisibleItemPosition();
                    int adapterLastPos = l.findFirstCompletelyVisibleItemPosition();
                    int position = 0;
                    position = adapterNowPos;
                    if (adapterLastPos > 0) {
                        position = adapterLastPos;
                    }
//                    final int positionFin = position;
                    Marker marker = markers.get(position);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));
                    moveWidth = (width - mRecyclerView.getChildAt(0).getWidth()) / 2 - 10;
                    ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, moveWidth);
                    showMarker(position);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int adapterNowPos = l.findFirstVisibleItemPosition();
                int adapterLastPos = l.findFirstCompletelyVisibleItemPosition();
                int position = 0;
                position = adapterNowPos;
                if (adapterLastPos > 0) {
                    position = adapterLastPos;
                }
                Marker marker = markers.get(position);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));
            }
        });
        //地图标记数据
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(WUHAN, 11));
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final int position = i;
            ShopLocation location = list.get(position);
            Shop shop = location.getShop();
            MarkerOptions ops = new MarkerOptions();
            ops.position(new LatLng(location.getLat(), location.getLng()))
                    .draggable(false)
                    .setFlat(true)
                    .icon(BitmapDescriptorFactory.fromResource(getRandomMarkerIcon()))
                    .title(shop.getTitle());
//                    .snippet(shop.getAddress());
//            markerOptions.add(ops);
            Marker marker = map.addMarker(ops);
//            marker.showInfoWindow();
            markers.add(marker);
        }

        //添加标记到地图上
//        markers = map.addMarkers(markerOptions, false);
//        Logger.d(TAG, "handleResp: markers size = " + markers.size());
        //移动到第一个坐标
        Marker marker = markers.get(0);
        //显示标题
//        marker.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));

        for (int i = 0; i < list.size(); i++) {
            final int position = i;
            ShopLocation location = list.get(position);
            Shop shop = location.getShop();
            Glide.with(this).load(shop.getImage())
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            //展示图片
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.marker_icon_black, null);
                            if (position == 0) {
                                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.marker_icon, null);
                            }
                            CircleImageView imageView = (CircleImageView) view.findViewById(R.id.iv_head);
                            TextView windowName = (TextView) view.findViewById(R.id.window_name);
                            windowName.setText(shop.getTitle());
                            imageView.setImageDrawable(resource);
                            Bitmap bitmap = convertViewToBitmap(view);
                            markers.get(position).setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        }
                    });
        }
    }

    private void showMarker(int showPostion) {
        for (int i = 0; i < shopList.size(); i++) {
            final int position = i;
            ShopLocation location = shopList.get(position);
            Shop shop = location.getShop();
            Glide.with(this).load(shop.getImage())
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            //展示图片
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.marker_icon_black, null);
                            if (position == showPostion) {
                                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.marker_icon, null);
                            }
                            CircleImageView imageView = (CircleImageView) view.findViewById(R.id.iv_head);
                            TextView windowName = (TextView) view.findViewById(R.id.window_name);
                            windowName.setText(shop.getTitle());
                            imageView.setImageDrawable(resource);
                            Bitmap bitmap = convertViewToBitmap(view);
                            markers.get(position).setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        }
                    });
        }
    }


    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


    private static final int[] MARKER_RES = {R.drawable.ic_marker_1, R.drawable.ic_marker_2,
            R.drawable.ic_marker_3};

    private int getRandomMarkerIcon() {
        return MARKER_RES[new Random().nextInt(2)];
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));
        for (int i = 0; i < markers.size(); i++) {
            if (marker.equals(markers.get(i))) {
                final int position = i;
                moveWidth = (width - mRecyclerView.getChildAt(0).getWidth()) / 2 - 10;
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, moveWidth);
                showMarker(i);
            }
        }
        return true;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        View infoContent = LayoutInflater.from(ShopMapDisplayAct.this).inflate(
                R.layout.item_map_window, null);
        render(marker, infoContent, 2);
        return infoContent;
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

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
