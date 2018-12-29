package com.qixiu.lejia.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.qixiu.lejia.R;
import com.qixiu.lejia.utils.RegexUtils;
import com.qixiu.lejia.utils.ToastUtils;

/**
 * Created by Long on 2018/5/10
 * <pre>
 *     各种输入验证
 * </pre>
 */
public class Validator {

    public static boolean validateUrl(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }

    /**
     * 验证手机号
     */
    public static boolean validatePhoneNumber(@NonNull Context context, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort(context, R.string.login_hint_phone);
            return false;
        }
        if (!RegexUtils.isMobileExact(phoneNum)) {
            ToastUtils.showShort(context, R.string.validate_phone_format_error);
            return false;
        }
        return true;
    }

    /**
     * 验证短信验证码(4-6位)
     */
    public static boolean validateSMSCode(@NonNull Context context, String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort(context, R.string.login_hint_code);
            return false;
        }
        int codeLength = code.length();
        if (codeLength > 6 || codeLength < 4) {
            ToastUtils.showShort(context, R.string.validate_code_length_error);
            return false;
        }
        return true;
    }


}
