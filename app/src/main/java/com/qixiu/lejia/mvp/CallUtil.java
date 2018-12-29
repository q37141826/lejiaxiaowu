package com.qixiu.lejia.mvp;

import android.support.annotation.Nullable;

import retrofit2.Call;

/**
 * Created by Long on 2017/3/25
 */
public final class CallUtil {

    private CallUtil() {
    }

    public static void cancel(@Nullable Call call) {
        if (call != null && !call.isCanceled() && call.isExecuted()) {
            call.cancel();
        }
    }

}
