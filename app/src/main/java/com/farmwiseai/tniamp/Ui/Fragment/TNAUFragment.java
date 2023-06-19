package com.farmwiseai.tniamp.Ui.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.Request.ListOfTNAU;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentTNAUBinding;
import com.farmwiseai.tniamp.utils.CallApi;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;
import com.farmwiseai.tniamp.utils.adapters.CustomAdapter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TNAUFragment extends Fragment implements View.OnClickListener {
    FragmentTNAUBinding tnauBinding;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, farmerName, category, survey_no, area, near_tank, remarks, dateField;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    List<ListOfTNAU> spinnerPos1;
    CharSequence myString = "0";
    CustomAdapter adapter, adapter2;
    Spinner firstSpinner, secondSpinner, thirdSpinner;
    EditText datePicker;
    private CallApi callApi;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic;
    CommonFunction mCommonFunction;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCommonFunction = new CommonFunction(getActivity());

        tnauBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_t_n_a_u, container, false);


        tnauBinding.popBackImage.setOnClickListener(this);
        tnauBinding.submissionBtn.setOnClickListener(this);
        tnauBinding.image1.setOnClickListener(this);
        tnauBinding.image2.setOnClickListener(this);
        tnauBinding.dateTxt.setOnClickListener(this);


        farmerName = tnauBinding.farmerTxt.getText().toString();
        survey_no = tnauBinding.surveyTxt.getText().toString();
        area = tnauBinding.areaTxt.getText().toString();
        near_tank = tnauBinding.tankTxt.getText().toString();
        remarks = tnauBinding.remarksTxt.getText().toString();
        dateField = tnauBinding.dateTxt.getText().toString();


        firstSpinner = tnauBinding.componentTxt;
        secondSpinner = tnauBinding.subComponentsTxt;
        thirdSpinner = tnauBinding.stagesTxt;
        datePicker = tnauBinding.dateTxt;

        callApi = new CallApi(getActivity(), getContext(), spinnerPos1, adapter, adapter2, myString);
        callApi.firstSpinnerPhrase(firstSpinner, secondSpinner, thirdSpinner, datePicker);



        return tnauBinding.getRoot();

    }

    private boolean fieldValidation(String phases, String sub_basin,
                                    String district, String village, String component, String sub_components, String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date) {

        if (farmerName.length() == 0) {
            tnauBinding.farmerTxt.setError("farmer name not found");
            return false;
        } else if (survey_no.length() == 0) {
            tnauBinding.surveyTxt.setError("Sub basin not found");
            return false;
        } else if (area.length() == 0) {
            tnauBinding.areaTxt.setError("Sub basin not found");
            return false;
        } else if (near_tank.length() == 0) {
            tnauBinding.tankTxt.setError("Sub basin not found");
            return false;
        } else if (remarks.length() == 0) {
            tnauBinding.remarksTxt.setError("Sub basin not found");
            return false;
        } else if (date.length() == 0) {
            tnauBinding.dateTxt.setError("Sub basin not found");
            return false;
        }


        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        switch (view.getId()) {
            case R.id.pop_back_image:
                Intent intent = new Intent(context, DashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.submission_btn:
                boolean checkValidaiton = fieldValidation(phases, sub_basin, district, village, component, sub_components, farmerName,
                        category, survey_no, area, near_tank, remarks, dateField);
                if (!checkValidaiton) {
                    Toast.makeText(context, "Data not found.!", Toast.LENGTH_SHORT).show();
                } else {
                    //do the code for save all data
                    Toast.makeText(context, "Data saved successfully.!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_1:
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
                break;

            case R.id.image_2:
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
                break;

            case R.id.date_txt:
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        tnauBinding.dateTxt.setText(dateFormat.format(myCalendar.getTime()));
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
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
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            if (takePicture && valueofPic == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                tnauBinding.image1.setImageBitmap(photo);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo);
            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                tnauBinding.image2.setImageBitmap(photo2);
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

    private void finalSubmission(){

        if(mCommonFunction.isNetworkAvailable() == true){
            //data should saved in post api

        }else{
            String offlineText = "Data saved successfully in offline data";
            showMessageOKCancel(offlineText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA,offlineText);
                    mCommonFunction.navigation(getActivity(),DashboardActivity.class);
                }
            });
        }
    }


}