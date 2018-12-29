package com.qixiu.lejia.common;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.utils.Logger;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     图片加载相关
 * </pre>
 */
@Keep
public final class ImageBindingAdapters {

    private static final String TAG = "ImageBindingAdapters";

    public static <T> void loadImage(ImageView view, T src) {
        if (src == null) {
            return;
        }
        Glide.with(view.getContext())
                .load(src)
                .into(view);
    }

    public static String jointImageUrl(String src) {
        if (TextUtils.isEmpty(src)) {
            return "";
        }
        if (!src.startsWith("http")) {
            src = src.substring(1, src.length());
            src = BuildConfig.BASE_URL + src;
        }
        return src;
    }


    /**
     * 绑定加载图片
     *
     * @param view        要绑定的view
     * @param src         图片资源
     * @param defHolder   当图片资源为空时，要显示的默认图
     * @param placeHolder 加载时的占位图
     * @param errorHolder 加载错误后显示的图片
     */
    @BindingAdapter(value = {"bindImage", "defHolder", "placeHolder", "errorHolder"}, requireAll = false)
    public static void bindImage(ImageView view, String src, Drawable defHolder,
                                 Drawable placeHolder, Drawable errorHolder) {
        if (TextUtils.isEmpty(src)) {
            Logger.d(TAG, "bindImage: src == null");
            if (defHolder != null) {
                view.setImageDrawable(defHolder);
            }
            return;
        }
        src = jointImageUrl(src);
        Logger.d(TAG, "bindImage: " + src);
        Glide.with(view.getContext())
                .load(src)
                .asBitmap()
                .placeholder(placeHolder)
                .error(errorHolder)
                .into(view);
    }

    @BindingAdapter("bindDrawableRes")
    public static void bindDrawableRes(ImageView view, int res) {
        if (res <= 0) {
            return;
        }
        view.setImageResource(res);
    }

    @BindingAdapter("visibleGone")
    public static void visibleGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
