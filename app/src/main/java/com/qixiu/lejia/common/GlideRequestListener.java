package com.qixiu.lejia.common;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Long on 2018/6/7
 */
public abstract class GlideRequestListener<T, R> implements RequestListener<T, R> {

    @Override
    public final boolean onResourceReady(R resource, T model, Target<R> target,
                                         boolean isFromMemoryCache, boolean isFirstResource) {
        onSuccess(resource);
        return true;
    }

    @Override
    public final boolean onException(Exception e, T model, Target<R> target, boolean isFirstResource) {
        onFailure(e, model);
        return true;
    }

    protected void onSuccess(R resource) {
    }

    protected void onFailure(Exception e, T model) {
    }

}
