package com.qixiu.lejia.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.qixiu.lejia.R;
import com.qixiu.widget.MultiStateLayout;

import static com.qixiu.lejia.core.sign.BaseSignPayActivity.DATA;


/**
 * Created by Long on 2017/11/10
 * <p>
 * 显示多种状态的Activity(加载中，加载错误，空数据)
 * </p>
 */
@SuppressLint("Registered")
public abstract class BaseMultiStateAct extends BaseToolbarAct {

    protected MultiStateLayout multiStateLayout;

    private final View.OnClickListener onClickListener = v -> {
        //切换到加载中，重新加载
        switchToLoadingState();
        onReload();
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abc_multi_state_layout);
        multiStateLayout = findViewById(R.id.multi_state_layout);

        //of content view
        final View contentView = onCreateContentView(getLayoutInflater());
        multiStateLayout.setViewForState(contentView, MultiStateLayout.VIEW_STATE_CONTENT);

        //subclass impl
        onContentViewCreated(contentView);

        //set reload click listener
        multiStateLayout.setOnReloadClickListener(onClickListener);
    }

    /**
     * 切换到加载中视图
     *
     * @see MultiStateLayout#setViewState(int)
     */
    protected void switchToLoadingState() {
        if (multiStateLayout.getViewState() != MultiStateLayout.VIEW_STATE_LOADING) {
            multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_LOADING);
        }
    }

    /**
     * 切换到加载错误视图
     *
     * @see MultiStateLayout#setViewState(int)
     */
    protected void switchToErrorState() {
        if (multiStateLayout.getViewState() != MultiStateLayout.VIEW_STATE_ERROR) {
            multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_ERROR);
        }
    }

    /**
     * 切换到内容视图
     *
     * @see MultiStateLayout#setViewState(int)
     */
    protected void switchToContentState() {
        if (multiStateLayout.getViewState() != MultiStateLayout.VIEW_STATE_CONTENT) {
            multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_CONTENT);
        }
    }

    /**
     * 切换到空数据视图
     *
     * @see MultiStateLayout#setViewState(int)
     */
    protected void switchToEmptyState() {
        if (multiStateLayout.getViewState() != MultiStateLayout.VIEW_STATE_EMPTY) {
            multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_EMPTY);
        }
    }

    /**
     * 创建真正需要显示的内容视图
     *
     * @param inflater LayoutInflater
     * @return View
     */
    @NonNull
    protected abstract View onCreateContentView(LayoutInflater inflater);

    /**
     * 内容视图创建完成
     *
     * @param view 内容视图
     */
    protected abstract void onContentViewCreated(View view);


    /**
     * load fail reload
     */
    protected abstract void onReload();

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }

    public static void start(Context context, Parcelable parcelable, Activity activity) {
        Intent intent = new Intent(context, activity.getClass());
        intent.putExtra(DATA, parcelable);
        context.startActivity(intent);
    }

    public static void start(Context context, String data, Activity activity) {
        Intent intent = new Intent(context, activity.getClass());
        intent.putExtra(DATA, data);
        context.startActivity(intent);
    }
}
