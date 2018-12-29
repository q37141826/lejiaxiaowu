package com.qixiu.lejia.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.lejia.R;
import com.qixiu.widget.MultiStateLayout;


/**
 * Created by Long on 2016/10/21
 * 显示多种状态的Fragment(加载中，加载错误，空数据)
 */
public abstract class BaseMultiStateFrag extends BaseLoadIndicatorFrag {

    protected MultiStateLayout multiStateLayout;

    private final View.OnClickListener onClickListener = v -> {
        //切换到加载中，重新加载
        switchToLoadingState();
        onReload();
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        multiStateLayout = (MultiStateLayout) inflater
                .inflate(R.layout.abc_multi_state_layout, container, false);

        //of content view
        final View contentView = onCreateContentView(inflater);
        multiStateLayout.setViewForState(contentView, MultiStateLayout.VIEW_STATE_CONTENT);
        return multiStateLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View contentView = multiStateLayout.getView(MultiStateLayout.VIEW_STATE_CONTENT);

        //subclass impl
        onContentViewCreated(contentView);

        //set reload click listener
        multiStateLayout.setOnReloadClickListener(onClickListener);

    }


    /**
     * 切换到加载中视图
     * <br/>
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
     * <br/>
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
     * <br/>
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
     * <br/>
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


}
