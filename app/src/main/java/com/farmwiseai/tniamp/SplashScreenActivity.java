package com.farmwiseai.tniamp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.mainView.MobileValidationActivity;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Boolean checkIsLogin=  SharedPrefsUtils.getBoolean(SplashScreenActivity.this,SharedPrefsUtils.PREF_KEY.LOGIN_SESSION);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if(checkIsLogin!=null& checkIsLogin) {
                    Intent i = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashScreenActivity.this, MobileValidationActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }
}