package com.farmwiseai;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.databinding.ActivityTestBinding;
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.utils.componentCallApis.TNAU_CallApi;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity  {
    ActivityTestBinding testBinding;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testBinding = DataBindingUtil.setContentView(TestActivity.this, R.layout.activity_test);
        setContentView(testBinding.getRoot());

        testBinding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testBinding.notificationBadgeCount.setVisibility(View.VISIBLE);
                count = count+1;
                Log.i(TAG, "onClickNotificationBadger "+true);
                if(count == 1){
                    testBinding.notificationBadgeCount.setVisibility(View.VISIBLE);
                    testBinding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.active_notification_icon));
                }else {
                    testBinding.notificationBadgeCount.setVisibility(View.GONE);
                    testBinding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.notification));
                    count = 0;
                }
            }
        });

        testBinding.parenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testBinding.notificationBadgeCount.setVisibility(View.GONE);
                testBinding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.notification));
            }
        });

    }
}