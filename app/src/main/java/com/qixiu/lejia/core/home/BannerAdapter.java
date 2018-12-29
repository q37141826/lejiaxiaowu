package com.qixiu.lejia.core.home;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qixiu.adapter.RecyclerPagerAdapter;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Banner;
import com.qixiu.lejia.common.ImageBindingAdapters;
import com.qixiu.lejia.common.Validator;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.widget.BannerView;
import com.qixiu.widget.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/4/23
 */
public class BannerAdapter extends RecyclerPagerAdapter<BannerAdapter.PageViewHolder> {

    private static final String TAG = "BannerAdapter";

    List<Banner> banners;

    OnItemClickListener onItemClickListener;

    BannerAdapter(List<Banner> banners) {
        this.banners = banners;
    }

    @BindingAdapter("pages")
    public static void bindPages(BannerView view, List<Banner> banners) {
        List<Banner> list = new ArrayList<>();
        if (banners == null || banners.isEmpty()) {
            //添加一个假数据
            list.add(Banner.createFake());
        } else {
            list.addAll(banners);
        }
        ViewPager pager = view.getViewPager();
        if (pager == null) {
            return;
        }
        PagerAdapter adapter = pager.getAdapter();
        if (adapter == null) {
            Logger.d(TAG, "bindPages: 绑定轮播图数据。。。。。");
            BannerAdapter pagerAdapter = newInstance(list);
            pagerAdapter.setOnItemClickListener((v, position) -> {
                Logger.d(TAG, "bindPages: 轮播图ITEM click" + position);
                Banner banner = list.get(position);
                if (banner.getLink() != null && !banner.getLink().equals("#")) {
                    if (Validator.validateUrl(banner.getLink())) {
                        WebActivity.start(view.getContext(), "详情", banner.getLink());
                    } else {
                        if (Validator.validateUrl(banner.getDetailurl())) {
//                    WebActivity.start(view.getContext(), "详情", banner.getImage());
                            GuaranteeExplainAct.start(view.getContext(), 0, banner.getDetailurl());
                        }
                    }
                }
            });
            view.setAdapter(pagerAdapter);
        }

    }

    private static BannerAdapter newInstance(List<Banner> banners) {
        return new BannerAdapter(banners);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        return new PageViewHolder(inflater.inflate(R.layout.banner_item, container, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        holder.imageView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        Drawable d = ContextCompat.getDrawable(context, R.drawable.holder_middle_banner);
        Banner banner = banners.get(position);
        ImageBindingAdapters.bindImage(holder.imageView, banner.getImage(), d, d, d);
    }

    static class PageViewHolder extends RecyclerPagerAdapter.ViewHolder {

        ImageView imageView;

        PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }


}
