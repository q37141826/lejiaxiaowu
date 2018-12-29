package com.qixiu.lejia.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Long on 2018/6/4
 * <pre>
 *     压缩微信分享图片小于32k
 * </pre>
 */
final class WXBitmapTransformation extends BitmapTransformation {


    WXBitmapTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return ThumbnailUtils.extractThumbnail(toTransform, 200, 200);
    }

    @Override
    public String getId() {
        return WXBitmapTransformation.class.getSimpleName();
    }
}
