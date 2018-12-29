package com.qixiu.lejia.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

/**
 * px dp sp 转换工具
 */
public class DensityUtils {

    private static final float density = Resources.getSystem().getDisplayMetrics().density;

    private static final float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;

    public static int dip2px(float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / scaledDensity + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * scaledDensity + 0.5f);
    }

}
