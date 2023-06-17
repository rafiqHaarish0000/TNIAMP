package com.farmwiseai.tniamp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.farmwiseai.tniamp.databinding.ActivityNoInternetBinding;
import com.farmwiseai.tniamp.utils.CommonFunction;


public class ActivityNoInternet extends AppCompatActivity {

    ActivityNoInternetBinding mActivityNointernetbinding;
    CommonFunction obj_commonfunction;
    boolean mDoubleBackToExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityNointernetbinding = DataBindingUtil.setContentView(this, R.layout.activity_no_internet);
        obj_commonfunction = new CommonFunction(ActivityNoInternet.this);

        mActivityNointernetbinding.mLinearLayoutTryagein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obj_commonfunction.isNetworkAvailable() == true) {

                  /* if(SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.NOINTERNETACTIVTYCLASS).contentEquals("Dashboard"))
                   {*/
                    obj_commonfunction.navigation(ActivityNoInternet.this, SplashScreenActivity.class);
                    finish();
                    //   }


                } else {

                    mLoadCustomToast(getResources().getString(R.string.try_Again));
                }

            }
        });

    }


    private void mLoadCustomToast(String message) {
        Toast.makeText(ActivityNoInternet.this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isConnected;
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExit) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExit = true;
        Toast.makeText(ActivityNoInternet.this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> mDoubleBackToExit = false, 2000);
    }
}