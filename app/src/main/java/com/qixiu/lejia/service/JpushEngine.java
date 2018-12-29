package com.qixiu.lejia.service;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.qixiu.lejia.app.AppContext.getContext;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class JpushEngine {


    /**
     * 初始化JPush
     *
     * @param context
     */
    public static void initJPush(Context context) {
        JPushInterface.init(context);
        String machineCode = getMachineCode();
        if (machineCode != null)
            setAlias(context, machineCode);
        Log.e("machineCode", machineCode);
    }

    /**
     * 设置别名，针对别名推送消息
     *
     * @param context
     * @param alias   别名
     */
    public static void setAlias(Context context, String alias) {
        JPushInterface.setAlias(context, alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d(getClass().toString(), i + "---" + s);
            }
        });
    }

    public static String getMachineCode() {

        final TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String uniqueId = deviceUuid.toString();
        return tmDevice;
    }


}
