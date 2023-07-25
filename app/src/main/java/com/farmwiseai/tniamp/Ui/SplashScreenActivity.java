package com.farmwiseai.tniamp.Ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.mainView.MobileValidationActivity;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.LogIdRequest;
import com.farmwiseai.tniamp.utils.LogIdResponse;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private String serial_no, versionName, dept_id;
    LogIdResponse logCheckResponse;
    CommonFunction commonFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Boolean checkIsLogin = SharedPrefsUtils.getBoolean(SplashScreenActivity.this, SharedPrefsUtils.PREF_KEY.LOGIN_SESSION);
        commonFunction = new CommonFunction(SplashScreenActivity.this);

        try {
            versionName = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "versionName: "+versionName);

        serial_no = SharedPrefsUtils.getString(SplashScreenActivity.this, SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN);
        dept_id = SharedPrefsUtils.getString(SplashScreenActivity.this, SharedPrefsUtils.PREF_KEY.USER_DETAILS);

        if(commonFunction.isNetworkAvailable()){

//            if (serial_no != null && dept_id != null) {
//                checkLogId(serial_no, versionName, dept_id);
//            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (checkIsLogin != null & checkIsLogin) {
                    Intent i = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, MobileValidationActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }

    private void checkLogId(String serial_no, String version_name, String dept_id) {
        try {
            Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
            Call<LogIdResponse> userDataCall = null;

            LogIdRequest logIdRequest = new LogIdRequest();
            logIdRequest.setSerial_no(serial_no);
            logIdRequest.setVersion_name(version_name);
            logIdRequest.setDept_id(dept_id);
            userDataCall = call.getLogCheckResponse(logIdRequest);
            userDataCall.enqueue(new Callback<LogIdResponse>() {
                @Override
                public void onResponse(Call<LogIdResponse> call, Response<LogIdResponse> response) {
                    if (response.body() != null) {
                        logCheckResponse = response.body();
                        Log.i(TAG, "onBody: " + response.code());


                    } else {
                        Log.i(TAG, "onResponse: " + "Server error.!");
                    }

                }

                @Override
                public void onFailure(Call<LogIdResponse> call, Throwable t) {
                    Log.i(TAG, "onResponse: " + t.getMessage());
                }
            });


        } catch (Exception e) {
            Log.i(TAG, "onResponse: " + e.getMessage());
        }
    }
}