package com.farmwiseai.tniamp.Ui.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.Agri_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AgriResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentAgricultureBinding;
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;
import com.farmwiseai.tniamp.utils.componentCallApis.AgriCallApi;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.CustomToast;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgricultureFragment extends Fragment implements View.OnClickListener,BackPressListener {
    private FragmentAgricultureBinding agricultureBinding;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, farmerName, survey_no, area, near_tank, remarks, dateField;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    private List<ComponentData> componentDropDown;
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private List<VillageData> villageDataList;
    private CharSequence myString = "0";
    private CharSequence posValue = "0";
    private ComponentAdapter adapter, adapter2;
    private SubBasinAdapter subAdapter;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private VillageAdaapter villageAdaapter;
    private Spinner subBasinSpinner, districtSpinner, blockSpinner, componentSpinner, sub_componentSpinner, stagesSpinner, genderSpinner, categorySpinner, villageSpinner;
    private EditText datePicker;
    private AgriCallApi agriCallApi;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic;
    private GPSTracker gpsTracker;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList;
    private LinearLayout vis_lyt;
    private double lati, longi;
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3; // stages
    public String farmer_name;
    public String gender;
    public String variety;
    public String yield;
    public String created_by; //serial number
    public String created_date;
    public String lat;
    public String lon;
    public String image1;
    public String tank_name;
    public String txn_date;
    public String photo_lat;
    public String photo_lon;
    public String txn_id;
    public String date;
    public String status;
    public BackPressListener backPressListener;
    private String villageValue, category, firstImageBase64, secondImageBase64;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCommonFunction = new CommonFunction(getActivity());

        agricultureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_agriculture, container, false);


        agricultureBinding.popBackImage.setOnClickListener(this);
        agricultureBinding.submissionBtn.setOnClickListener(this);
        agricultureBinding.image1.setOnClickListener(this);
        agricultureBinding.image2.setOnClickListener(this);
        agricultureBinding.dateTxt.setOnClickListener(this);


        farmerName = agricultureBinding.farmerTxt.getText().toString();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        dateField = agricultureBinding.dateTxt.getText().toString();


        componentSpinner = agricultureBinding.componentTxt;
        sub_componentSpinner = agricultureBinding.subComponentsTxt;
        stagesSpinner = agricultureBinding.stagesTxt;
        datePicker = agricultureBinding.dateTxt;
        vis_lyt = agricultureBinding.visibilityLyt;
        backPressListener = this;

        agriCallApi = new AgriCallApi(getActivity(), getContext(), componentDropDown, adapter, adapter2, myString,backPressListener);
        agriCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stagesSpinner, datePicker, vis_lyt);

        getLocation();
        setAllDropDownData();


        return agricultureBinding.getRoot();


    }

    private boolean fieldValidation(String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date) {

        farmerName = agricultureBinding.farmerTxt.getText().toString();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        date = agricultureBinding.dateTxt.getText().toString();


        if (agricultureBinding.phase1.getSelectedItem() == null
                && subBasinSpinner.getSelectedItem() == null
                && districtSpinner.getSelectedItem() == null
                && blockSpinner.getSelectedItem() == null
                && componentSpinner.getSelectedItem() == null
                && sub_componentSpinner.getSelectedItem() == null
                && stagesSpinner.getSelectedItem() == null
                && genderSpinner.getSelectedItem() == null
                && categorySpinner.getSelectedItem() == null
                && villageSpinner.getSelectedItem() == null) {
            mLoadCustomToast(getActivity(), "Empty field found.!, Please enter all the fields");
        }

        if (valueofPic != 0 && valueofPic != 1 && valueofPic != 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
        }

        if (farmerName.length() == 0) {
            agricultureBinding.farmerTxt.setError("Please enter farmer name");
            return false;
        } else if (survey_no.length() == 0) {
            agricultureBinding.surveyTxt.setError("Please enter survey no");
            return false;
        } else if (area.length() == 0) {
            agricultureBinding.areaTxt.setError("Please enter area");
            return false;
        } else if (near_tank.length() == 0) {
            agricultureBinding.tankTxt.setError("Please enter near by tank name");
            return false;
        } else if (remarks.length() == 0) {
            agricultureBinding.remarksTxt.setError("Remarks not found");
            return false;
        } else if (date.length() == 0) {
            agricultureBinding.dateTxt.setError("Please enter the date");
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.submission_btn:
                boolean checkValidaiton = fieldValidation(farmerName,
                    category, survey_no, area, near_tank, remarks, dateField);
                if (checkValidaiton) {
                    try {
                        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                        } else {
                            //  getLocation(view);
                            gpsTracker = new GPSTracker(getContext());
                            lat = String.valueOf(gpsTracker.getLatitude());
                            lon = String.valueOf(gpsTracker.getLongitude());
                            finalSubmission();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    showToast(getActivity(),"Validation error");
                }
                break;

            case R.id.image_1:
                if (checkPermission()) {
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
    private void finalSubmission() {

        if (mCommonFunction.isNetworkAvailable() == true) {
            //data should saved in post api
            // Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
            getAllData();
            //mCommonFunction.navigation(getActivity(), DashboardActivity.class);

        } else {
            String offlineText = "Data saved successfully in offline data";
            showMessageOKCancel(offlineText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA, offlineText);
                    mCommonFunction.navigation(getActivity(), DashboardActivity.class);
                }
            });
        }
    }


    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = agricultureBinding.subBasinTxt;
        districtSpinner = agricultureBinding.districtTxt;
        blockSpinner = agricultureBinding.blockTxt;
        genderSpinner = agricultureBinding.genderTxt;
        categorySpinner = agricultureBinding.categoryTxt;
        componentSpinner = agricultureBinding.componentTxt;
        sub_componentSpinner = agricultureBinding.subComponentsTxt;
        stagesSpinner = agricultureBinding.stagesTxt;
        villageSpinner = agricultureBinding.villageTxt;
        datePicker = agricultureBinding.dateTxt;


        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        agricultureBinding.phase1.setItem(phraseList);


//phase drop down spinner
        agricultureBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (mCommonFunction.isNetworkAvailable() == true) {
                    try {
                        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                        Call<List<Sub_Basin_Data>> userDataCall = null;
                        userDataCall = call.getSub_basinData();
                        userDataCall.enqueue(new Callback<List<Sub_Basin_Data>>() {
                            @Override
                            public void onResponse(Call<List<Sub_Basin_Data>> call, Response<List<Sub_Basin_Data>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    sub_basin_DropDown = response.body();
                                    Log.i(TAG, "onResponse: " + agricultureBinding.phase1.getSelectedItemPosition());
                                    subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                                    myString = String.valueOf(agricultureBinding.phase1.getSelectedItemPosition());
                                    subAdapter.getFilter().filter(myString);
                                    subBasinSpinner.setAdapter(subAdapter);
                                } else {
                                    mLoadCustomToast(getActivity(), getString(R.string.server_error));
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Sub_Basin_Data>> call, Throwable t) {

                            }
                        });

                    } catch (Exception e) {
                        mLoadCustomToast(getActivity(), e.toString());
                    }

                } else {
                    mLoadCustomToast(getActivity(), getString(R.string.network_error));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //district dropdown spinner
        subBasinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mCommonFunction.isNetworkAvailable() == true) {

                    try {
                        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                        Call<List<DistrictData>> userDataCall = null;
                        userDataCall = call.getDistrictData();
                        userDataCall.enqueue(new Callback<List<DistrictData>>() {
                            @Override
                            public void onResponse(Call<List<DistrictData>> call, Response<List<DistrictData>> response) {
                                if (response.isSuccessful() && response.body() != null) {

                                    districtDropDown = response.body();
                                    posValue = String.valueOf(sub_basin_DropDown.get(i).getID());
                                    Log.i(TAG, "posValue: " + posValue);
                                    districtAdapter = new DistrictAdapter(getContext(), districtDropDown);
                                    Log.i(TAG, "districtPos: " + myString);
                                    districtAdapter.getFilter().filter(posValue);
                                    districtSpinner.setAdapter(districtAdapter);

                                } else {
                                    mLoadCustomToast(getActivity(), getResources().getString(R.string.server_error));
                                }

                            }

                            @Override
                            public void onFailure(Call<List<DistrictData>> call, Throwable t) {
                                mLoadCustomToast(getActivity(), t.toString());
                            }
                        });

                    } catch (Exception e) {
                        mLoadCustomToast(getActivity(), e.toString());
                    }


                } else {
                    mLoadCustomToast(getActivity(), getString(R.string.network_error));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //blockSpinner
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mCommonFunction.isNetworkAvailable() == true) {
                    try {
                        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                        Call<List<BlockData>> userDataCall = null;
                        userDataCall = call.getBlockData();
                        userDataCall.enqueue(new Callback<List<BlockData>>() {
                            @Override
                            public void onResponse(Call<List<BlockData>> call, Response<List<BlockData>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    blockDropDown = response.body();
                                    posValue = String.valueOf(districtDropDown.get(i).getID());
                                    Log.i(TAG, "posValue: " + posValue);
                                    blockAdapter = new BlockAdapter(getContext(), blockDropDown);
                                    Log.i(TAG, "districtPos: " + myString);
                                    blockAdapter.getFilter().filter(posValue);
                                    blockSpinner.setAdapter(blockAdapter);
                                } else {
                                    mLoadCustomToast(getActivity(), getString(R.string.server_error));
                                }
                            }

                            @Override
                            public void onFailure(Call<List<BlockData>> call, Throwable t) {

                            }
                        });

                    } catch (Exception e) {
                        mLoadCustomToast(getActivity(), getString(R.string.server_error));
                    }
                } else {
                    mLoadCustomToast(getActivity(), getString(R.string.network_error));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mCommonFunction.isNetworkAvailable() == true) {
                    try {
                        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                        Call<List<VillageData>> userDataCall = null;
                        userDataCall = call.getVillageData();
                        userDataCall.enqueue(new Callback<List<VillageData>>() {
                            @Override
                            public void onResponse(Call<List<VillageData>> call, Response<List<VillageData>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    villageDataList = response.body();
                                    posValue = String.valueOf(blockDropDown.get(i).getID());
                                    Log.i(TAG, "posValue: " + posValue);
                                    villageAdaapter = new VillageAdaapter(getContext(), villageDataList);
                                    Log.i(TAG, "districtPos: " + myString);
                                    villageAdaapter.getFilter().filter(posValue);
                                    villageSpinner.setAdapter(villageAdaapter);

                                } else {
                                    mLoadCustomToast(getActivity(), getString(R.string.server_error));
                                }
                            }

                            @Override
                            public void onFailure(Call<List<VillageData>> call, Throwable t) {

                            }
                        });

                    } catch (Exception e) {
                        mLoadCustomToast(getActivity(), getString(R.string.server_error));
                    }
                } else {
                    mLoadCustomToast(getActivity(), getString(R.string.network_error));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onValue: " + villageDataList.get(i).getNAME());
                villageValue = String.valueOf(villageDataList.get(i).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //gender dropdown list
        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        agricultureBinding.genderTxt.setItem(genderList);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = genderSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //category dropdown spinner
        categoryList = new ArrayList<>();
        categoryList.add("SC");
        categoryList.add("ST");
        categoryList.add("Others");
        agricultureBinding.categoryTxt.setItem(categoryList);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        agricultureBinding.dateTxt.setText(dateFormat.format(myCalendar.getTime()));
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
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            if (takePicture && valueofPic == 1) {
                Bitmap photo1 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                agricultureBinding.image1.setImageBitmap(photo1);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo1);
                firstImageBase64 = getEncodedString(photo1);
//                Toast.makeText(getContext(), getEncodedString(photo1), Toast.LENGTH_LONG).show();


            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                agricultureBinding.image2.setImageBitmap(photo2);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo2);
                secondImageBase64 = getEncodedString(photo2);
            }
        }

    }

    private String encodeImage(Bitmap bm) {
        Bitmap immagex = bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);

        byte[] imageArr = os.toByteArray();

        String encodeImage = Base64.encodeToString(imageArr, Base64.NO_WRAP);

        return encodeImage;

    }

    private boolean getLocation() {
        gpsTracker = new GPSTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            lati = gpsTracker.getLatitude();
            longi = gpsTracker.getLongitude();

            Log.i(TAG, "Latitude" + lati + " " + "Longitude" + longi);

        } else {
            gpsTracker.showSettingsAlert();
        }
        return true;
    }

//    private void finalSubmission() {
//
//        if (mCommonFunction.isNetworkAvailable() == true) {
//            try {
//                getAllData();
//                mCommonFunction.navigation(getActivity(), DashboardActivity.class);
//                showToast(getActivity(), SharedPrefsUtils.getString(getContext(), SharedPrefsUtils.PREF_KEY.SuccessMessage));
//            } catch (Exception e) {
//                showToast(getActivity(), e.getMessage());
//            }
//        } else {
//            showToast(getActivity(), getResources().getString(R.string.network_error));
////            String offlineText = "Data saved successfully in offline data";
////            showMessageOKCancel(offlineText, new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialogInterface, int i) {
//////                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA, offlineText);
////                    mCommonFunction.navigation(getActivity(), DashboardActivity.class);
////                }
////            });
//        }
//    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }

    public void showToast(Activity mcontaxt, String message) {
        Toast.makeText(mcontaxt, message, Toast.LENGTH_SHORT).show();
    }

    private void getAllData() {

        farmerName = agricultureBinding.farmerTxt.getText().toString();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        dateField = agricultureBinding.dateTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue"+dateField);

        Agri_Request request = new Agri_Request();
        request.setVillage(villageValue);
        request.setIntervention1(intervention1);
        request.setIntervention2(intervention2);
        request.setIntervention3(intervention3);
        request.setFarmer_name(farmerName);
        request.setGender(gender);
        request.setCategory(category);
        request.setSurvey_no(survey_no);
        request.setArea(area);
        request.setVariety(" ");
        request.setImage1(firstImageBase64);
        request.setYield(" ");
        request.setRemarks(remarks);
        request.setCreated_by("f55356773fce5b11");
        request.setCreated_date(dateField);
        request.setLat(lat);
        request.setLon(lon);
        request.setTank_name(near_tank);
        request.setTxn_date("Wed Feb 12 2020 12:04:46 GMT+0530 (India Standard Time)");
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setTxn_id("20200212120446");
        request.setDate(dateField);
        request.setStatus("0");

        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AgriResponse> userDataCall = null;
        userDataCall = call.getAgriResponse(request);
        userDataCall.enqueue(new Callback<AgriResponse>() {
            @Override
            public void onResponse(Call<AgriResponse> call, Response<AgriResponse> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getTnauLandDeptId());
                        Log.i(TAG, "txt_value: "+txt_id.toString());
                        mCommonFunction.navigation(getActivity(),DashboardActivity.class);
                        uploadSecondImage(txt_id);
//                        List<AgriResponse> agriResponses = new ArrayList<>();
//                        agriResponses.addAll(response.body().getResponse());
                    } catch (Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"data error.!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgriResponse> call, Throwable t) {

            }
        });

    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("2");
        request.setImg2(secondImageBase64.trim());
        request.setID(txt_id);

        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<SecondImageResponse> userDataCall = null;
        userDataCall = call.getSecondImageURL(request);
        userDataCall.enqueue(new Callback<SecondImageResponse>() {
            @Override
            public void onResponse(Call<SecondImageResponse> call, Response<SecondImageResponse> response) {
                if (response.body() != null) {
                    try {
                        String successMessage = response.body().getResponse();
                        Log.i(TAG, "onSuccessMsg"+successMessage);
                        mCommonFunction.navigation(getContext(),DashboardActivity.class);
//                        SharedPrefsUtils.putString(getContext(), SharedPrefsUtils.PREF_KEY.SuccessMessage, successMessage);
                        Toast.makeText(getContext(),successMessage,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"data getting error.!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SecondImageResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onSelectedInputs(LookUpDataClass lookUpDataClass) {
        intervention1 = lookUpDataClass.getIntervention1();
        intervention2 = lookUpDataClass.getIntervention2();
        intervention3 = lookUpDataClass.getIntervention3();
        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
    }
}