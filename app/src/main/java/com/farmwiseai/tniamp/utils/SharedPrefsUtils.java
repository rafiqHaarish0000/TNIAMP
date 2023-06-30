package com.farmwiseai.tniamp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefsUtils
{

    public static enum PREF_KEY
    {
        SuccessMessage("success_message"),ACCESS_TOKEN("access_token"),LOGIN_SESSION("login_session"),CONFIRM_PASSWORD("confirm_password"),
        KEY_TODAY("key_today"),KEY_OVERDUE("key_overdue"),KEY_FEATURE("key_feature"),USER_DETAILS("user_details"),USER_NAME("user_name"),VIEWPAGERCURRENTPOSTION("viewpagercurrentpostion"),
        MFROM("mfrom"),LEAD_DETAILS("lead_details"),PHARSE_1("spinner_1_value"),COMPONENT("component"),SUB_COMPONENT("sub_component"),STAGE("satges"),
        DATE_OF_SOWING("sowing"),SAVED_OFFLINE_DATA("savedOfflineData"),MODEL_VILLAGE("model_village"),VILLAGE_NAME("village_name"),OFFLINE_DATA("offlineData");

        public final String KEY;

        PREF_KEY(String key)
        {
            this.KEY = key;
        }
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

    public static void putBoolean(Context context, PREF_KEY key, boolean value) {
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


    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);

        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString(key, null);
        return value;
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
    public static void putList(Context context, PREF_KEY key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key.KEY, value);

        // Commit the edits!
        editor.commit();
    }

    public static String getList(Context context, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPref.getString(key.KEY, null);
        return value;
    }
    public static void saveArrayList(Context context,ArrayList<TNAU_Request> list ,PREF_KEY key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public  static ArrayList<TNAU_Request> getArrayList(Context context,PREF_KEY key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<TNAU_Request>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
