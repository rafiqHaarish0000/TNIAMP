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
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity implements LocationListener {
    ActivityTestBinding testBinding;
    List<ComponentData> spinnerPos1;
    CharSequence myString = "0";
    ComponentAdapter adapter, adapter2;
    Spinner firstSpinner, secondSpinner, thirdSpinner;
    EditText datePicker;
    List<String> multiAdapterList;
    private TNAU_CallApi TNAUCallApi;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic;
    private GPSTracker gpsTracker;
    double lat, longi;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testBinding = DataBindingUtil.setContentView(TestActivity.this, R.layout.activity_test);
        setContentView(testBinding.getRoot());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        firstSpinner = testBinding.phase1;
        secondSpinner = testBinding.phase2;
        thirdSpinner = testBinding.phase3;
        datePicker = testBinding.sowingDatepicker;

        multiAdapterList = new ArrayList<>();
        multiAdapterList.add("One");
        multiAdapterList.add("Two");
        multiAdapterList.add("Three");
        multiAdapterList.add("Four");
        multiAdapterList.add("Five");
        testBinding.spinnerMultiSpinner.initMultiSpinner(this, testBinding.spinnerMultiSpinner);
        testBinding.spinnerMultiSpinner.setBackground(getResources().getDrawable(R.drawable.edit_text_background));
        testBinding.spinnerMultiSpinner.setPadding(20, 20, 20, 20);
        testBinding.spinnerMultiSpinner.setAdapterWithOutImage(this, multiAdapterList, new MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener() {
            @Override
            public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
                for (int i = 0; i < chosenItems.size(); i++) {
                    Log.e("chosenItems", chosenItems.get(i));
                }
            }
        });


        List<String> phraseList;
        phraseList = new ArrayList<>();
        phraseList.add("one");
        phraseList.add("two");
        testBinding.phase1.setItem(phraseList);

//        String pharse = testBinding.phase1.getSelectedItem();
        testBinding.clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testBinding.phase1 != null && testBinding.phase1.getSelectedItem() != null) {
                    Log.i(TAG, "spinnerValidate: " + "Success.!");
                } else {
                    Log.i(TAG, "spinnerValidate: " + "Please enter the empty field");
                }
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        testBinding.sowingDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TestActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        testBinding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testBinding.image1.setSelected(true);
                if (checkPermission()) {
                    Log.i(TAG, "onClick: " + "granded.!");
                    valueofPic = 1;
                    takePicture = true;
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                } else {
                    requestPermission();
                }
            }
        });

        testBinding.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Log.i(TAG, "onClick: " + "granded.!");
                    valueofPic = 2;
                    takePicture = false;
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                } else {
                    requestPermission();
                }
            }
        });


        testBinding.getLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                gpsTracker = new GPSTracker(TestActivity.this);
//                try {
//                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//                        ActivityCompat.requestPermissions(TestActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//                    }else{
//                        lat = gpsTracker.getLatitude();
//                        longi = gpsTracker.getLongitude();
//                        testBinding.latValue.setText(String.valueOf(lat));
//                        testBinding.longValue.setText(String.valueOf(longi));
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                try {
                    if (ActivityCompat.checkSelfPermission(TestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(TestActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    onLocationChanged(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//        TNAUCallApi = new TNAU_CallApi(TestActivity.this,TestActivity.this, spinnerPos1, adapter, adapter2, myString, backPressListener);
//        TNAUCallApi.firstSpinnerPhrase(firstSpinner, secondSpinner, thirdSpinner, datePicker);


    }

    private void setSpinnerError(Spinner spinner, String errorMessage){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null) {
            spinner.requestFocus();
            Toast.makeText(TestActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }


    private void getLocation(View view) {
        gpsTracker = new GPSTracker(TestActivity.this);
        if (gpsTracker.canGetLocation()) {

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        testBinding.sowingDatepicker.setText(dateFormat.format(myCalendar.getTime()));
    }
    //take pic and save it image view;


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(TestActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            if (takePicture && valueofPic == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                testBinding.image1.setImageBitmap(photo);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo);
            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                testBinding.image2.setImageBitmap(photo2);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo2);
            }


        }

    }

    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.URL_SAFE);


    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Log.i(TAG, "onLocationChanged: " + lat + lon);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}