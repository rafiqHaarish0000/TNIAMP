package com.farmwiseai.tniamp.Ui.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.HortiRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.HortiResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentHorticultureBinding;
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.CustomToast;
import com.farmwiseai.tniamp.utils.LatLongPojo;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.PermissionUtils;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;
import com.farmwiseai.tniamp.utils.componentCallApis.HortiCallApi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HorticultureFragment extends Fragment implements View.OnClickListener, BackPressListener {
    FragmentHorticultureBinding horticultureBinding;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, farmerName, category, survey_no, area, near_tank, remarks, dateField;
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
    private Spinner subBasinSpinner, districtSpinner,
            blockSpinner, componentSpinner,
            sub_componentSpinner, stagesSpinner,
            genderSpinner, categorySpinner, villageSpinner, interventionSpinner;
    private EditText datePicker;
    private HortiCallApi hortiCallApi;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList;
    private LinearLayout vis_lyt, trainingLyt, iNames_lyt;
    private GPSTracker gpsTracker;
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
    public String status, intName;
    public BackPressListener backPressListener;
    private String villageValue, firstImageBase64, secondImageBase64;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCommonFunction = new CommonFunction(getActivity());

        horticultureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_horticulture, container, false);


        horticultureBinding.popBackImage.setOnClickListener(this);
        horticultureBinding.submissionBtn.setOnClickListener(this);
        horticultureBinding.image1.setOnClickListener(this);
        horticultureBinding.image2.setOnClickListener(this);
        horticultureBinding.dateTxt.setOnClickListener(this);


        farmerName = horticultureBinding.farmerTxt.getText().toString();
        survey_no = horticultureBinding.surveyTxt.getText().toString();
        area = horticultureBinding.areaTxt.getText().toString();
        near_tank = horticultureBinding.tankTxt.getText().toString();
        remarks = horticultureBinding.remarksTxt.getText().toString();
        dateField = horticultureBinding.dateTxt.getText().toString();
        backPressListener = this;
        componentSpinner = horticultureBinding.componentTxt;
        sub_componentSpinner = horticultureBinding.subComponentsTxt;
        stagesSpinner = horticultureBinding.stagesTxt;
        datePicker = horticultureBinding.dateTxt;
        vis_lyt = horticultureBinding.visibilityLyt;
        trainingLyt = horticultureBinding.iecLayt;
        intName = horticultureBinding.inerventionNameTxt.getText().toString();
        iNames_lyt = horticultureBinding.inerventionLyt;
        hortiCallApi = new HortiCallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        hortiCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stagesSpinner, datePicker, vis_lyt, trainingLyt);
        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);

        setAllDropDownData();


        return horticultureBinding.getRoot();

    }

    private boolean fieldValidation(String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date, String intName) {

        farmerName = horticultureBinding.farmerTxt.getText().toString();
        survey_no = horticultureBinding.surveyTxt.getText().toString();
        area = horticultureBinding.areaTxt.getText().toString();
        near_tank = horticultureBinding.tankTxt.getText().toString();
        remarks = horticultureBinding.remarksTxt.getText().toString();
        date = horticultureBinding.dateTxt.getText().toString();
        intName = horticultureBinding.inerventionNameTxt.getText().toString();


        if (horticultureBinding.phase1.getSelectedItem() == null
                && subBasinSpinner.getSelectedItem() == null
                && districtSpinner.getSelectedItem() == null
                && blockSpinner.getSelectedItem() == null
                && componentSpinner.getSelectedItem() == null
                && sub_componentSpinner.getSelectedItem() == null
                && stagesSpinner.getSelectedItem() == null
                && genderSpinner.getSelectedItem() == null
                && categorySpinner.getSelectedItem() == null
                && villageSpinner.getSelectedItem() == null
                && interventionSpinner.getSelectedItem() == null) {
            mLoadCustomToast(getActivity(), "Empty field found.!, Please enter all the fields");
        }

        if (valueofPic != 0 && valueofPic != 1 && valueofPic != 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
        }


        if (farmerName.length() == 0 && horticultureBinding.farmerTxt.getVisibility() == View.VISIBLE) {
            horticultureBinding.farmerTxt.setError("Please enter farmer name");
            return false;
        } else if (survey_no.length() == 0 && horticultureBinding.surveyTxt.getVisibility() == View.VISIBLE) {
            horticultureBinding.surveyTxt.setError("Please enter survey no");
            return false;
        } else if (area.length() == 0 && horticultureBinding.areaTxt.getVisibility() == View.VISIBLE) {
            horticultureBinding.areaTxt.setError("Please enter area");
            return false;
        } /*else if (near_tank.length() == 0 && horticultureBinding.tankTxt.getVisibility() == View.VISIBLE) {
            horticultureBinding.tankTxt.setError("Please enter near by tank name");
            return false;
        }*/ else if (remarks.length() == 0 && horticultureBinding.remarksTxt.getVisibility() == View.VISIBLE) {
            horticultureBinding.remarksTxt.setError("Remarks not found");
            return false;
        } else if (date.length() == 0 && horticultureBinding.dateTxt.getVisibility() == View.VISIBLE) {
            horticultureBinding.dateTxt.setError("Please enter the date");
            return false;
        }
    /*    if (farmerName.length() == 0) {
            horticultureBinding.farmerTxt.setError("Please enter farmer name");
            return false;
        } else if (survey_no.length() == 0) {
            horticultureBinding.surveyTxt.setError("Please enter survey no");
            return false;
        } else if (area.length() == 0) {
            horticultureBinding.areaTxt.setError("Please enter area");
            return false;
        } *//*else if (near_tank.length() == 0) {
            horticultureBinding.tankTxt.setError("Please enter near by tank name");
            return false;
        }*//*
        else if (remarks.length() == 0) {
            horticultureBinding.remarksTxt.setError("Remarks not found");
            return false;
        } *//*else if (date.length() == 0) {
            horticultureBinding.dateTxt.setError("Please enter the date");
            return false;
        }*/


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
                        category, survey_no, area, near_tank, remarks, dateField, intName);
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //do the code for save all data
                    Toast.makeText(context, "Data saved successfully.!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.image_1:
                if (PermissionUtils.checkPermission(context)) {
                    Log.i(TAG, "onClick: " + "granded.!");
                    valueofPic = 1;
                    takePicture = true;
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                } else {
                    PermissionUtils.requestPermission(getActivity());
                }
                break;

            case R.id.image_2:
                if (PermissionUtils.checkPermission(context)) {
                    Log.i(TAG, "onClick: " + "granded.!");
                    valueofPic = 2;
                    takePicture = false;
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                } else {
                    PermissionUtils.requestPermission(getActivity());
                }
                break;

            case R.id.date_txt:
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

        }
    }


    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = horticultureBinding.subBasinTxt;
        districtSpinner = horticultureBinding.districtTxt;
        blockSpinner = horticultureBinding.blockTxt;
        genderSpinner = horticultureBinding.genderTxt;
        categorySpinner = horticultureBinding.categoryTxt;
        componentSpinner = horticultureBinding.componentTxt;
        sub_componentSpinner = horticultureBinding.subComponentsTxt;
        stagesSpinner = horticultureBinding.stagesTxt;
        villageSpinner = horticultureBinding.villageTxt;
        datePicker = horticultureBinding.dateTxt;


        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        horticultureBinding.phase1.setItem(phraseList);


//phase drop down spinner
        horticultureBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));
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
                                    Log.i(TAG, "onResponse: " + horticultureBinding.phase1.getSelectedItemPosition());
                                    subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                                    myString = String.valueOf(horticultureBinding.phase1.getSelectedItemPosition());
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
        horticultureBinding.genderTxt.setItem(genderList);

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
        horticultureBinding.categoryTxt.setItem(categoryList);

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
        horticultureBinding.dateTxt.setText(dateFormat.format(myCalendar.getTime()));
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
                horticultureBinding.image1.setImageBitmap(photo);
                // BitMap is data structure of image file which store the image in memory
                firstImageBase64 = getEncodedString(photo);
            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                horticultureBinding.image2.setImageBitmap(photo2);
                // BitMap is data structure of image file which store the image in memory
                secondImageBase64 = getEncodedString(photo2);
            }
        }

    }

    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.NO_WRAP);


    }

    private void finalSubmission() {

        if (mCommonFunction.isNetworkAvailable() == true) {
            //data should saved in post api
            getAllData();

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

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }




    private void getAllData() {

        farmerName = horticultureBinding.farmerTxt.getText().toString();
        survey_no = horticultureBinding.surveyTxt.getText().toString();
        area = horticultureBinding.areaTxt.getText().toString();
        area = horticultureBinding.areaTxt.getText().toString();
        remarks = horticultureBinding.remarksTxt.getText().toString();
        near_tank = horticultureBinding.tankTxt.getText().toString();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);

        HortiRequest request = new HortiRequest();
        request.setVillage(villageValue);
        request.setIntervention1(intervention1);
        request.setIntervention2(intervention2);
        request.setIntervention3(intervention3);
        request.setFarmerName(farmerName);
        request.setGender(gender);
        request.setCategory(category);
        request.setSurveyNo(survey_no);
        request.setArea(area);
        request.setVariety(" ");
        request.setImage1(firstImageBase64);
        request.setYield(" ");
        request.setRemarks(remarks);
        request.setCreatedBy("f55356773fce5b11");
        request.setCreatedDate("2020-02-12 11:02:02");
        request.setLat(lat);
        request.setLon(lon);
        request.setTankName(near_tank);
        request.setTxnDate("Wed Feb 12 2020 12:04:46 GMT+0530 (India Standard Time)");
        request.setPhotoLat(lat);
        request.setPhotoLon(lon);
        request.setTxnId("20200212120446");
        request.setStatus("0");


        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<HortiResponse> userDataCall = null;
        userDataCall = call.getHortiResponse(request);
        userDataCall.enqueue(new Callback<HortiResponse>() {
            @Override
            public void onResponse(Call<HortiResponse> call, Response<HortiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getHortiLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    mCommonFunction.navigation(getActivity(), DashboardActivity.class);
                    uploadSecondImage(txt_id);
//                        List<AgriResponse> agriResponses = new ArrayList<>();
//                        agriResponses.addAll(response.body().getResponse());
                } else {

                }
            }

            @Override
            public void onFailure(Call<HortiResponse> call, Throwable t) {

            }
        });

    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("3");
        request.setImg2(secondImageBase64);
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
                        Log.i(TAG, "onSuccessMsg" + successMessage);
                        mCommonFunction.navigation(getContext(), DashboardActivity.class);
//                        SharedPrefsUtils.putString(getContext(), SharedPrefsUtils.PREF_KEY.SuccessMessage, successMessage);
                        Toast.makeText(getContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "data getting error.!", Toast.LENGTH_SHORT).show();
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