package com.farmwiseai.tniamp.utils;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.farmwiseai.tniamp.R;


public class BaseActivity extends AppCompatActivity {
    // initialize DatabindingPOJO Class
    public DatabindingPOJO mObjdatabindingPOJO;

    public ValidationUtils validationUtils_obj;
    public OfflineDataSyncFile offlineDataSyncFile;
    public CommonFunction obj_commonfunction;

    private ConnectivityReceiver MyReceiver = null;
    protected int[] colors;
    protected int goldDark, goldMed, gold, goldLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //   CommonFunction.darkenStatusBar(this, R.color.white);
        mObjdatabindingPOJO = new DatabindingPOJO();

        validationUtils_obj = new ValidationUtils(BaseActivity.this);
        obj_commonfunction = new CommonFunction(BaseActivity.this);
        offlineDataSyncFile = new OfflineDataSyncFile(BaseActivity.this);

        MyReceiver = new ConnectivityReceiver();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        broadcastIntent();


        final Resources res = getResources();
        goldDark = res.getColor(R.color.gold_dark);
        goldMed = res.getColor(R.color.gold_med);
        gold = res.getColor(R.color.gold);
        goldLight = res.getColor(R.color.gold_light);
        colors = new int[]{goldDark, goldMed, gold, goldLight};

    }

    private void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyReceiver);
    }
}
