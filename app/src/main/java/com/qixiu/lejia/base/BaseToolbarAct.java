package com.qixiu.lejia.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qixiu.lejia.R;

import java.util.List;

import static com.qixiu.lejia.core.sign.BaseSignPayActivity.DATA;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     公用带标题，返回键的Activity
 * </pre>
 */
@SuppressLint("Registered")
public class BaseToolbarAct extends BaseLoadIndicatorAct {

    private TextView mTitleView;
    private FrameLayout mContainer;
    private TextView textViewRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //必须调用super
        super.setContentView(R.layout.abc_act_toolbar);

        //设置support actionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置禁止显示toolbar自己的标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mTitleView = findViewById(R.id.toolbar_title);
        textViewRight = findViewById(R.id.textViewRight);
        //设置自己的标题
        mTitleView.setText(getTitle());

        mContainer = findViewById(R.id.container);
        //隐藏微信支付
        View view = findViewById(R.id.wx_pay);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public TextView getRightText(){
        return textViewRight;
    }

    @Override
    public void setContentView(int layoutResID) {
        mContainer.addView(getLayoutInflater().inflate(layoutResID, mContainer, false));
    }

    @Override
    public void setContentView(View view) {
        mContainer.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContainer.addView(view, params);
    }

    @Override
    public void setTitle(int titleId) {
        mTitleView.setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public static void start(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void start(Context context, Class activity, Parcelable parcelable) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(DATA,parcelable);
        context.startActivity(intent);
    }

    public static void start(Context context, Class activity, String str) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void start(Context context, Class activity, List<? extends Parcelable> datas) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }
}
