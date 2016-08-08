package cn.crepusculo.subway_ticket_android.util;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import cn.crepusculo.subway_ticket_android.application.MyApplication;

/**
 * The SharedPreferencesUtils class
 * This is a util class.
 * <p/>
 * It is use to get the instance of
 * SharedPreferences more convenient.
 *
 * @author wafer
 * @since 16/5/29 01:02
 */

public class SharedPreferencesUtils {

    private static final String FILENAME_PREFIX = "cn.crepusculo.subway_ticket_android.";


    private SharedPreferencesUtils() {
        // Required empty
    }


    public static SharedPreferences getPreferences(String filename) {

        String name = FILENAME_PREFIX + filename;
        return MyApplication.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void putBoolean(SharedPreferences preferences, String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public static void putInt(SharedPreferences preferences, String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public static void putString(SharedPreferences preferences, String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public static void putFloat(SharedPreferences preferences, String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public static void putLong(SharedPreferences preferences, String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public static void putStringSet(SharedPreferences preferences, String key, Set<String> value) {
        preferences.edit().putStringSet(key, value).apply();
    }
}
