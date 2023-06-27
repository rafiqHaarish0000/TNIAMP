package com.farmwiseai.tniamp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.farmwiseai.tniamp.mainView.GPSTracker;

public class PermissionUtils {
    private static final int PERMISSION_REQUEST_CODE = 200;
    public PermissionUtils() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(final Activity activity, final String permission) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission);
        final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    public static void setShouldShowStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }

    public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }

    public static LatLongPojo getLocation(Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        String value="";
         double latitude = 0, longitude=0;
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new LatLongPojo(String.valueOf(latitude),String.valueOf(longitude));

    }
    public static boolean checkPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }
    public static void requestPermission(Activity context) {

        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }
}
