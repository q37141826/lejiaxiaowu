package com.qixiu.lejia.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.core.me.message.MessageListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class JPushReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.context = context;
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        String keyword = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.e(getClass().toString(), "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " +
                printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(getClass().toString(), "[MyReceiver] 接收Registration Id : " + regId);
//            send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(getClass().toString(), "[MyReceiver] 接收到推送下来的自定义消息: " +
                    bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(getClass().toString(), "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String titile = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            //第一时间收到推送后的自动处理
            try {
                //接收通知只有在启动Activity时才做操作
            } catch (Exception e) {
                Log.e("LOGCAT", e.getMessage());
            }
            Log.e(getClass().toString(), "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            boolean islogin = LoginStatus.verifiedLogin(context);//判断是否登录
            if (islogin) {
                MessageListActivity.start(context);
            }
            LinkedTreeMap<String, String> comment =
                    new Gson().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA),
                            LinkedTreeMap.class);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(getClass().toString(), "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
                    bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected =
                    intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(getClass().toString(),
                    "[MyReceiver]" + intent.getAction() + " connected state change to " +
                            connected);
        } else {
            Log.e(getClass().toString(), "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 打印所有的 intent extra 数据
     */
    private String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.e(getClass().toString(), "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(JPushReceiver.class.toString(), "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 处理消息
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Intent msgIntent = new Intent(JPushAction.MESSAGE_RECEIVED_ACTION);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (null != extraJson && extraJson.length() > 0) {
                    msgIntent.putExtra(JPushAction.KEY_EXTRAS, extras);
                }
            } catch (JSONException e) {

            }

        }
        context.sendBroadcast(msgIntent);


    }

    public static class JPushAction {

        /**
         * 用于接收广播的Action
         */
        public static String MESSAGE_RECEIVED_ACTION;
        public static final String KEY_MESSAGE = "message";
        public static final String KEY_EXTRAS = "extras";

    }

}
