package com.qixiu.lejia.core.home;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Banner;
import com.qixiu.lejia.common.ImageBindingAdapters;
import com.qixiu.lejia.common.Validator;
import com.qixiu.lejia.core.web.WebActivity;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.widget.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/6/1
 */
public class TopBannerAdapter extends BannerAdapter {

    private static final String TAG = "TopBannerAdapter";


    private TopBannerAdapter(List<Banner> banners) {
        super(banners);
    }

    @Override
    public BannerAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        return new PageViewHolder(inflater.inflate(R.layout.page_shop_image, container, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.PageViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        holder.imageView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        Drawable d = ContextCompat.getDrawable(context, R.drawable.holder_top_banner);
        Banner banner = banners.get(position);
        ImageBindingAdapters.bindImage(holder.imageView, banner.getImage(), d, d, d);
    }

    @BindingAdapter("homeTopBanner")
    public static void bindHomeTopBannerPages(BannerView view, List<Banner> banners) {
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
            Logger.d(TAG, "bindPages: 绑定顶部轮播图数据。。。。。");
            TopBannerAdapter pagerAdapter = new TopBannerAdapter(list);
            pagerAdapter.setOnItemClickListener((v, position) -> {
                Banner banner = list.get(position);
                Logger.d(TAG, "bindPages: 顶部轮播图ITEM click" + banner.getLink());
                if (!banner.getLink().equals("#")) {
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


}
