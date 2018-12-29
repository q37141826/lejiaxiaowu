package com.qixiu.lejia.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public final class ToastUtils {


    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void showShort(@NonNull Context context, @StringRes int strRes) {
        show(context, context.getResources().getString(strRes), Toast.LENGTH_SHORT);
    }

    public static void showShort(@NonNull Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(@NonNull Context context, String message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    public static void showLong(@NonNull Context context, @StringRes int strRes) {
        show(context, context.getResources().getString(strRes), Toast.LENGTH_LONG);
    }

    private static void show(@NonNull Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

}
