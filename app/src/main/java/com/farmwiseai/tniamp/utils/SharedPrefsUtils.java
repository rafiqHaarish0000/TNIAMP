package com.farmwiseai.tniamp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AEDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.Agri_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AnimalRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.FishRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.HortiRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.MarkRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.WRDRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefsUtils {

    public static enum PREF_KEY {
        ACCESS_TOKEN("access_token"), LOGIN_SESSION("login_session"),
        USER_DETAILS("user_details"), USER_NAME("user_name"),
        ONLINE_DATA_COUNT("notifyCount"),
        SAVED_OFFLINE_DATA("savedOfflineData"), OFFLINE_DATA("offlineData"),
        OFFLINE_DATA_AGRI("offlineDataAgri"), SAVED_OFFLINE_DATA_AGRI("savedOfflineDataAgri"),
        OFFLINE_DATA_AED("offlineDataAed"), SAVED_OFFLINE_DATA_AED("savedOfflineDataAed"),
        OFFLINE_DATA_ANI("offlineDataAni"), SAVED_OFFLINE_DATA_ANI("savedOfflineDataAni"),
        OFFLINE_DATA_HORTI("offlineDataHorti"), SAVED_OFFLINE_DATA_HORTI("savedOfflineDataHorti"),
        OFFLINE_DATA_WRD("offlineDataWrd"), SAVED_OFFLINE_DATA_WRD("savedOfflineDataWrd"),
        OFFLINE_DATA_MARKETING("offlineDataMarketing"), SAVED_OFFLINE_DATA_MARKETING("savedOfflineDataMarketing"),
        OFFLINE_DATA_FISH("offlineDataFish"), SAVED_OFFLINE_DATA_FISH("savedOfflineDataFish");


        public final String KEY;

        PREF_KEY(String key) {
            this.KEY = key;
        }
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

    public static void clearStringPrefs(Context context, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().remove(key.KEY).apply();
    }


    public static void saveArrayList(Context context, ArrayList<TNAU_Request> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<TNAU_Request> getArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<TNAU_Request>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveAgriArrayList(Context context, ArrayList<Agri_Request> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<Agri_Request> getAgriArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<Agri_Request>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveHortiArrayList(Context context, ArrayList<HortiRequest> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<HortiRequest> getHortiArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<HortiRequest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static ArrayList<WRDRequest> getWrdArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<WRDRequest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveWRDArrayList(Context context, ArrayList<WRDRequest> list, PREF_KEY key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }


    public static ArrayList<MarkRequest> getMarkArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<MarkRequest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveMarkArrayList(Context context, ArrayList<MarkRequest> list, PREF_KEY key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<FishRequest> getFishArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<FishRequest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveFishArrayList(Context context, ArrayList<FishRequest> list, PREF_KEY key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }


    public static void saveAEDArrayList(Context context, ArrayList<AEDRequest> list, PREF_KEY key) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<AEDRequest> getAEDArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<AEDRequest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public static void saveARDArrayList(Context context, ArrayList<AnimalRequest> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<AnimalRequest> getARDArrayList(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<AnimalRequest>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static ArrayList<String> getArrayListImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListagriImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListagriImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListHortiImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListHortiImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListAedImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListAedImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListAhdImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListAhdImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListMarkImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListMarkImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListwrdImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListWrdImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayListFishImage(Context context, PREF_KEY key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key.KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveArrayListFishImage(Context context, ArrayList<String> list, PREF_KEY key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key.KEY, json);
        editor.apply();

    }
}

