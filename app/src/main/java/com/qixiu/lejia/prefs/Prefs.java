package com.qixiu.lejia.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.qixiu.lejia.app.AppContext;

import java.util.Set;

/**
 * Created by Long on 2017/11/10
 * <p>首选项</p>
 *
 * @see SharedPreferences
 */
public class Prefs {

    private static final String PREFS_FILE_NAME = "prefs";

    private SharedPreferences prefs;

    private Prefs() {
        prefs = AppContext.getContext().getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static Prefs get() {
        return SingletonHolder.instance;
    }

    public static void put(String key, Object value) {
        SharedPreferences.Editor edit = get().prefs.edit();
        if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        }
        edit.apply();
    }

    public static void putStringSet(String key, Set<String> values) {
        SharedPreferences.Editor edit = get().prefs.edit();
        edit.putStringSet(key, values);
        edit.apply();
    }

    public static int getInt(String key) {
        return get().prefs.getInt(key, 0);
    }

    public static long getLong(String key) {
        return get().prefs.getLong(key, 0);
    }

    public static float getFloat(String key) {
        return get().prefs.getFloat(key, 0f);
    }

    public static boolean getBoolean(String key) {
        return get().prefs.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return get().prefs.getBoolean(key, defValue);
    }

    public static String getString(String key) {
        return get().prefs.getString(key, null);
    }

    public static Set<String> getStringSet(String key) {
        return get().prefs.getStringSet(key, null);
    }

    /*是否存在key*/
    public static boolean contains(String key) {
        return get().prefs.contains(key);
    }

    /*删除key*/
    public static void remove(String key) {
        get().prefs.edit().remove(key).apply();
    }

    /*注册事件监听*/
    public static void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        get().prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    /*注销事件监听*/
    public static void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        get().prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }


    private static class SingletonHolder {
        private static Prefs instance = new Prefs();
    }

}
