package com.qixiu.lejia.core.home;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qixiu.adapter.BindableViewHolder;
import com.qixiu.lejia.databinding.ItemHomeGuaranteeBinding;
import com.qixiu.lejia.databinding.ItemHomeHeaderBinding;
import com.qixiu.widget.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/5/15
 */
class BannerController {

    static void start(@NonNull RecyclerView recyclerView) {
        List<BannerView> views = getBannerView(recyclerView);
        if (views == null) {
            return;
        }
        for (BannerView view : views) {
            if (view != null) {
                execute(view, true);
            }
        }
    }

    static void stop(@NonNull RecyclerView recyclerView) {
        List<BannerView> views = getBannerView(recyclerView);
        if (views == null) {
            return;
        }
        for (BannerView view : views) {
            if (view != null) {
                execute(view, false);
            }
        }
    }

    private static List<BannerView> getBannerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return null;
        }

        List<BannerView> views = new ArrayList<>();
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        /*顶部轮播图*/
        View topView = layoutManager.findViewByPosition(0);
        if (topView != null) {
            BindableViewHolder holder = (BindableViewHolder) recyclerView.getChildViewHolder(topView);
            ViewDataBinding binding = holder.getBinding();
            if (binding instanceof ItemHomeHeaderBinding) {
                ItemHomeHeaderBinding headerBinding = (ItemHomeHeaderBinding) binding;
                views.add(headerBinding.banner);
            }
        }

        /*中间轮播图*/
        View view = layoutManager.findViewByPosition(2);
        if (view != null) {
            BindableViewHolder holder = (BindableViewHolder) recyclerView.getChildViewHolder(view);
            ViewDataBinding binding = holder.getBinding();
            if (binding instanceof ItemHomeGuaranteeBinding) {
                ItemHomeGuaranteeBinding guaranteeBinding = (ItemHomeGuaranteeBinding) binding;
                views.add(guaranteeBinding.banner);
            }
        }

        return views;
    }

    private static void execute(BannerView view, boolean starting) {
        if (starting) view.onStartLoop();
        else view.onStopLoop();
    }

}
