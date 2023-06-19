package com.farmwiseai.tniamp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefsUtils
{

    public static enum PREF_KEY
    {
        UID("uid"),ACCESS_TOKEN("access_token"),LOGIN_SESSION("login_session"),CONFIRM_PASSWORD("confirm_password"),
        KEY_TODAY("key_today"),KEY_OVERDUE("key_overdue"),KEY_FEATURE("key_feature"),USER_DETAILS("user_details"),VIEWPAGERCURRENTPOSTION("viewpagercurrentpostion"),
        MFROM("mfrom"),LEAD_DETAILS("lead_details"),PHARSE_1("spinner_1_value"),COMPONENT("component"),SUB_COMPONENT("sub_component"),STAGE("satges"),
        DATE_OF_SOWING("sowing"),SAVED_OFFLINE_DATA("savedOfflineData");

        public final String KEY;

        PREF_KEY(String key)
        {
            this.KEY = key;
        }
    }
    public static void putInt(PREF_KEY key, int value) {
        putInt(SharePerferenceApplication.getInstance(), key, value);
    }

    public static int getInt(PREF_KEY key) {
        return getInt(SharePerferenceApplication.getInstance(), key);
    }

    public static void putLong(PREF_KEY key, long value) {
        putLong(SharePerferenceApplication.getInstance(), key, value);
    }

    public static long getLong(PREF_KEY key) {
        return getLong(SharePerferenceApplication.getInstance(), key);
    }

    public static void putString(PREF_KEY key, String value) {
        putString(SharePerferenceApplication.getInstance(), key, value);
    }

    public static String getString(PREF_KEY key) {
        return getString(SharePerferenceApplication.getInstance(), key);
    }

    public static boolean putBoolean(PREF_KEY key, boolean value) {
        putBoolean(SharePerferenceApplication.getInstance(), key, value);
        return value;
    }

    public static boolean getBoolean(PREF_KEY key) {
        return getBoolean(SharePerferenceApplication.getInstance(), key);
    }

    private static void putInt(Context context, PREF_KEY key, int value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key.KEY, value);

        // Commit the edits!
        editor.commit();
    }

    private static int getInt(Context context, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        int value = sharedPref.getInt(key.KEY, 0);
        return value;
    }

    private static void putLong(Context context, PREF_KEY key, long value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key.KEY, value);

        // Commit the edits!
        editor.commit();
    }

    private static long getLong(Context context, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        long value = sharedPref.getLong(key.KEY, 0);
        return value;
    }

    public static void putString(Context context, PREF_KEY key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key.KEY, value);

        // Commit the edits!
        editor.commit();
    }

    public static String getString(Context context, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString(key.KEY, null);
        return value;
    }

    private static void putBoolean(Context context, PREF_KEY key, boolean value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key.KEY, value);

        // Commit the edits!
        editor.commit();
    }

    public static boolean getBoolean(Context context, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean value = sharedPref.getBoolean(key.KEY, false);
        return value;
    }

    public static void putString(String key, String value) {
        putString(SharePerferenceApplication.getInstance(), key, value);
    }

    private static void putString(Context context, String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);

        editor.commit();
    }

    private static String getString(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString(key, null);
        return value;
    }

    public static String getString(String key) {
        return getString(SharePerferenceApplication.getInstance(), key);
    }


    public static void clearAllPrefs(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public static void clearStringPrefs(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().remove(key).commit();
    }

}
