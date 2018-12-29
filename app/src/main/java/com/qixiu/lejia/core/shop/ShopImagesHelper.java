package com.qixiu.lejia.core.shop;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.ShopDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Long on 2018/5/31
 */
class ShopImagesHelper {

    private ViewPager    mViewPager;
    private RecyclerView mRecyclerView;
    /**
     * 幻灯片计时器
     */
    private Timer        mTimer;
//    /**
//     * 店铺图片集合
//     */
//    private List<ShopDetail.ShopImages> images;
    /**
     * 轮播图指针的位置
     */
    private int position = 0;

    void onAttach(Activity host) {
        mViewPager = host.findViewById(R.id.pager);

        mRecyclerView = host.findViewById(R.id.images);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(host, LinearLayoutManager.HORIZONTAL, false));

    }

    void onDisplay(@NonNull List<ShopDetail.ShopImages> images) {

//        this.images = images;

        //设置图片
        if (images.size() > 0) {
            ShopImageAdapter imageAdapter = new ShopImageAdapter(images.get(0).getImages());
            mViewPager.setAdapter(imageAdapter);
//        refresh(mViewPager, images.get(0).getImages().size());

            //设置指示器
            List<ShopCateImageItem> items = new ArrayList<>();
            for (ShopDetail.ShopImages image : images) {
                items.add(new ShopCateImageItem(image));
            }
            //选择第一个
            items.get(0).setChecked(true);
            BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
            adapter.setItemActionHandler(o -> {
                //切换图片显示
                for (ShopCateImageItem item : items) {
                    item.setChecked(false);
                }
                ShopCateImageItem item = (ShopCateImageItem) o;
                item.setChecked(true);

                //获取点击的item的pos
                position = items.indexOf(item);

                //adapter#notifyDataSetChanged() don't work
                ShopImageAdapter newImageAdapter = new ShopImageAdapter(images.get(position).getImages());
                mViewPager.setAdapter(newImageAdapter);

            });
            mRecyclerView.setAdapter(adapter);

            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    int i = 0;
                    if (mViewPager.getCurrentItem() > images.get(position).getImages().size()) {
                        i = 0;
                    } else {
                        i = mViewPager.getCurrentItem();
                    }
                    i++;
                    if (i > images.get(position).getImages().size() - 1) {
                        if (images.size() == 1) {
                            i = 0;
                            mViewPager.setCurrentItem(i);
                        }
                        if (images.size() > 1) {
                            if (position > images.size() - 1 || position == images.size() - 1) {
                                position = 0;
                            } else {
                                position++;
                            }
                            for (ShopCateImageItem item : items) {
                                item.setChecked(false);
                            }
                            ShopCateImageItem item = (ShopCateImageItem) adapter.getItem(position);
                            item.setChecked(true);
                            LinearLayoutManager l = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                            int adapterFisrtPos = l.findFirstCompletelyVisibleItemPosition();
                            int adapterLastPos = l.findLastCompletelyVisibleItemPosition();
                            if (position > adapterLastPos || position < adapterFisrtPos) {
                                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 150);
                            }
                            //获取点击的item的pos
//                            int index = items.indexOf(item);

                            //adapter#notifyDataSetChanged() don't work
                            ShopImageAdapter newImageAdapter = new ShopImageAdapter(images.get(position).getImages());
                            mViewPager.setAdapter(newImageAdapter);
                        }
                    } else {
                        mViewPager.setCurrentItem(i);
                    }
                }
            };
            class MyTask extends TimerTask {
                public void run() {
                    Message msg = Message.obtain();
                    handler.sendMessage(msg);
                }
            }

            mTimer = null;
            if (mTimer == null) { // 保证只有一个 定时任务
                mTimer = new Timer(true);
                mTimer.schedule(new MyTask(), 3000, 3000);
            }
        }
    }
}
