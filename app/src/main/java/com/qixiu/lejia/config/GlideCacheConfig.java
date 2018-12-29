package com.qixiu.lejia.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Long on 2018/4/24
 * <pre>
 *     Glide图片缓存配置
 * </pre>
 */
public class GlideCacheConfig implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片缓存到App内部储存缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }

}
