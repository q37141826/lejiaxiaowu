package com.qixiu.lejia.core.shop;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Title:
 * Description:解决地图在主scrollview中滑动冲突的问题由于MapView被定义成final class，所以只能在容器中操作了
 * Copyright:武汉企秀网络科技有限公司 Copyright(c)20XX
 * author:xuchi
 * date: 2018/6/21 0021
 * version 1.0
 */
public class MapContainer extends RelativeLayout {
    private NestedScrollView scrollView;

    public MapContainer(Context context) {
        super(context);
    }

    public MapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollView(NestedScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollView.requestDisallowInterceptTouchEvent(false);
        } else {
            scrollView.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}