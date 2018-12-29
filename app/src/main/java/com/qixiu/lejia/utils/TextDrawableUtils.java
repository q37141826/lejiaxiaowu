package com.qixiu.lejia.utils;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

/**
 * Created by my on 2018/12/28.
 */

public class TextDrawableUtils {
    public  static  void setLeftImage(Context context, TextView textView, int resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        } else {
            textView.setCompoundDrawables(context.getResources().getDrawable(resource), null, null, null);
        }

    }
}
