package com.farmwiseai.tniamp.Ui.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AEDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AEDResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentAEDBinding;
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.CustomToast;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.LatLongPojo;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.PermissionUtils;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;
import com.farmwiseai.tniamp.utils.ValidationUtils;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;
import com.farmwiseai.tniamp.utils.componentCallApis.AEDCallApi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AEDFragment extends Fragment implements View.OnClickListener, BackPressListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
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
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageName = null;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stageValue = null;
    public String stageLastValue = null;
    public BackPressListener backPressListener;
    ArrayList<AEDRequest> offlineAedRequest = new ArrayList<>();
    ArrayList<String> offlineImageAedRequest = new ArrayList<>();
    private Context context;
    private FragmentAEDBinding aedBinding;
    private String farmerName, category1, survey_no, area, near_tank, remarks, dateField, village, interventionName, mobileNumber;
    private List<ComponentData> componentDropDown;
    private List<Sub_Basin_Data> sub_basin_DropDown = new ArrayList<>();
    private List<DistrictData> districtDropDown = new ArrayList<>();
    private List<BlockData> blockDropDown = new ArrayList<>();
    private List<VillageData> villageDataList = new ArrayList<>();
    private CharSequence myString = "0";
    private CharSequence posValue = "0";
    private ComponentAdapter adapter, adapter2;
    private SubBasinAdapter subAdapter;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private VillageAdaapter villageAdaapter;
    private Spinner subBasinSpinner, districtSpinner, blockSpinner, componentSpinner,
            sub_componentSpinner, stageSpinner, genderSpinner, categorySpinner, villageSpinner, interventionSpinner;
    private EditText datePicker;
    private AEDCallApi aedCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList, interventionList;
    private GPSTracker gpsTracker;
    private LinearLayout hideLyt, otherLayt, vis_lyt, iNames_lyt;
    private double lati, longi;
    private String villageValue, firstImageBase64, secondImageBase64, interventionTypeVal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCommonFunction = new CommonFunction(getActivity());
        context = getContext();
        aedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_a_e_d, container, false);
        aedBinding.popBackImage.setOnClickListener(this);
        aedBinding.submissionBtn.setOnClickListener(this);
        aedBinding.image1.setOnClickListener(this);
        aedBinding.image2.setOnClickListener(this);
        farmerName = aedBinding.farmerTxt.getText().toString();
        survey_no = aedBinding.surveyTxt.getText().toString();
        area = aedBinding.areaTxt.getText().toString();
        near_tank = aedBinding.tankTxt.getText().toString();
        remarks = aedBinding.remarksTxt.getText().toString();
        componentSpinner = aedBinding.componentTxt;
        sub_componentSpinner = aedBinding.subComponentsTxt;
        stageSpinner = aedBinding.stageTxt;
        hideLyt = aedBinding.visibilityLyt;
        otherLayt = aedBinding.othersLayout;
        backPressListener = this;
        SharedPrefsUtils.putInt(context, SharedPrefsUtils.PREF_KEY.BACK_PRESSED, 0);

        mobileNumber = aedBinding.mobileNumbertxt.getText().toString();
        offlineAedRequest = SharedPrefsUtils.getAEDArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
        offlineImageAedRequest = SharedPrefsUtils.getArrayListAedImage(context, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);
        aedCallApi = new AEDCallApi(getActivity(), getContext(), componentDropDown, adapter, adapter2, myString, backPressListener);
        aedCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stageSpinner, hideLyt, otherLayt);
        aedBinding.areaTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);
        setAllDataValues();
        return aedBinding.getRoot();
    }

    private void setAllDataValues() {
        subBasinSpinner = aedBinding.subBasinTxt;
        districtSpinner = aedBinding.districtTxt;
        blockSpinner = aedBinding.blockTxt;
        genderSpinner = aedBinding.genderTxt;
        categorySpinner = aedBinding.categoryTxt;
        villageSpinner = aedBinding.villageTxt;
        interventionSpinner = aedBinding.inverntionTyper;
 //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        aedBinding.phase1.setItem(phraseList);


        //phase drop down spinner
        aedBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                subBasinValue = null;
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));

                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                Log.i(TAG, "onResponse: " + aedBinding.phase1.getSelectedItemPosition());
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(aedBinding.phase1.getSelectedItemPosition());
                subAdapter.getFilter().filter(myString);
                subBasinSpinner.setAdapter(subAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //district dropdown spinner
        subBasinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtValue = null;
                districtDropDown = FetchDeptLookup.readDistrictData(context, "district.json");
                posValue = String.valueOf(sub_basin_DropDown.get(i).getID());
                subBasinValue = sub_basin_DropDown.get(i).getNAME();

                districtAdapter = new DistrictAdapter(getContext(), districtDropDown);
                districtAdapter.getFilter().filter(posValue);
                districtSpinner.setAdapter(districtAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //blockSpinner
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                blockValue = null;
                blockDropDown = FetchDeptLookup.readBlockData(context, "block.json");
                posValue = String.valueOf(districtDropDown.get(i).getID());
                districtValue = districtDropDown.get(i).getNAME();

                Log.i(TAG, "posValue: " + posValue);
                blockAdapter = new BlockAdapter(getContext(), blockDropDown);
                blockAdapter.getFilter().filter(posValue);
                blockSpinner.setAdapter(blockAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                villageName = null;
                villageDataList = FetchDeptLookup.readVillageData(context, "village.json");
                posValue = String.valueOf(blockDropDown.get(i).getID());
                blockValue = blockDropDown.get(i).getNAME();

                villageAdaapter = new VillageAdaapter(getContext(), villageDataList);
                villageAdaapter.getFilter().filter(posValue);
                villageSpinner.setAdapter(villageAdaapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onValue: " + villageDataList.get(i).getNAME());
                village = String.valueOf(villageDataList.get(i).getID());
                villageValue = String.valueOf(villageDataList.get(i).getID());

                villageName = villageDataList.get(i).getNAME();
//                SharedPrefsUtils.putString(getContext(), SharedPrefsUtils.PREF_KEY.VILLAGE_NAME, villageDataList.get(i).getNAME());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //gender dropdown list
        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        aedBinding.genderTxt.setItem(genderList);

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
        aedBinding.categoryTxt.setItem(categoryList);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category1 = categorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        interventionList = new ArrayList<>();
        interventionList.add("Demo");
        interventionList.add("Sustainability");
        interventionList.add("Adoption");
        aedBinding.inverntionTyper.setItem(interventionList);
        interventionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                interventionTypeVal = String.valueOf(interventionSpinner.getSelectedItemPosition());
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    // validation for all mandatory fields
    private boolean fieldValidation(String farmerName, String category, String gender1,
                                    String survey_no, String area, String near_tank, String remarks, String date, String intName, String mobileNumber) {

        farmerName = aedBinding.farmerTxt.getText().toString().trim();
        survey_no = aedBinding.surveyTxt.getText().toString();
        area = aedBinding.areaTxt.getText().toString();
        near_tank = aedBinding.tankTxt.getText().toString();
        remarks = aedBinding.remarksTxt.getText().toString();
        intName = aedBinding.inerventionNameTxt.getText().toString().trim();
        mobileNumber = aedBinding.mobileNumbertxt.getText().toString().trim();

    /*    if (componentValue != null) {
            if (componentValue.equalsIgnoreCase("Others"))
                subComponentValue = "Dummy data";
        }*/
        if (subBasinValue == null || districtValue == null || blockValue == null ||
                villageName == null || componentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
            return false;
        } else if (sub_componentSpinner.getVisibility() == View.VISIBLE && subComponentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (stageSpinner.getVisibility() == View.VISIBLE && stageValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (valueofPicCount == 0 || valueofPicCount < 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        }

        if (aedBinding.visibilityLyt.getVisibility() == View.VISIBLE) {
            if (farmerName.length() == 0) {
                aedBinding.farmerTxt.setError("Please enter farmer name");
                return false;
            } /*else if (date.length() == 0) {
                aedBinding.dateTxt.setError("Please enter the date");
                return false;
            }*/ else if (survey_no.length() == 0) {
                aedBinding.surveyTxt.setError("Please enter survey no");
                return false;
            } else if (area.length() == 0) {
                aedBinding.areaTxt.setError("Please enter area");
                return false;

            } else if (Double.parseDouble(area) > 2.0) {
                aedBinding.areaTxt.setError("Area Should be less than 2(ha)");
                return false;
            } else if (mobileNumber.isEmpty() || (mobileNumber.length() < 10)) {
                aedBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;

            } else if (ValidationUtils.isValidMobileNumber(mobileNumber) == false) {
                aedBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (gender == null && genderSpinner.getVisibility() == View.VISIBLE) {
                Toast.makeText(getActivity(), "Please select Gender", Toast.LENGTH_LONG).show();
                return false;
            } else if (category1 == null && categorySpinner.getVisibility() == View.VISIBLE) {
                Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_LONG).show();
                return false;
            } else if (otherLayt.getVisibility() == View.VISIBLE) {
                if (intName.length() == 0) {
                    aedBinding.inerventionNameTxt.setError("field empty");
                    return false;
                }
            }

        } else if (aedBinding.othersLayout.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (aedBinding.inerventionNameTxt.getText().toString().isEmpty()) {
                aedBinding.inerventionNameTxt.setError("field empty");
                return false;
            }
        }

        return true;
    }


    // click event for finalSubmission button and others
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.pop_back_image:
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                break;

            case R.id.submission_btn:
                Log.i(TAG, "componentTxt: " + componentSpinner.getSelectedItem());
                boolean checkValidaiton = fieldValidation(farmerName,
                        category1, gender, survey_no, area, near_tank, remarks, dateField, interventionName, mobileNumber);
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //do the code for save all data
                    //  Toast.makeText(context, "Server error.!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.image_1:
                if (PermissionUtils.checkPermission(context)) {
                    aedBinding.image1.setSelected(true);
                    Log.i(TAG, "onClick: " + "granded.!");

                    valueofPic = 1;
                    valueofPicCount++;
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
                    aedBinding.image2.setSelected(true);
                    Log.i(TAG, "onClick: " + "granded.!");
                    valueofPic = 2;
                    valueofPicCount++;
                    takePicture = false;
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                } else {
                    PermissionUtils.requestPermission(getActivity());
                }
                break;

        }
    }


    // alert pop up
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
        if (requestCode == pic_id && resultCode == Activity.RESULT_OK) {
            if (takePicture && valueofPic == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                aedBinding.image1.setImageBitmap(photo);
                // BitMap is data structure of image file which store the image in memory
                firstImageBase64 = getEncodedString(photo);
            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                aedBinding.image2.setImageBitmap(photo2);
                // BitMap is data structure of image file which store the image in memory
                secondImageBase64 = getEncodedString(photo2);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast toast = Toast.makeText(getContext(), "Canceled, no photo selected.", Toast.LENGTH_LONG);
            toast.show();

        }

    }

    //save the image in base64 format for fetch in backend data
    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.NO_WRAP);


    }

    // final submission button validation for online and save data for offline data through database..
    private void finalSubmission() {
        getAllData();

    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }


    private void getAllData() {

        farmerName = aedBinding.farmerTxt.getText().toString();
        survey_no = aedBinding.surveyTxt.getText().toString();
        area = aedBinding.areaTxt.getText().toString();
        area = aedBinding.areaTxt.getText().toString();
        remarks = aedBinding.remarksTxt.getText().toString();
        near_tank = aedBinding.tankTxt.getText().toString();
        interventionName = aedBinding.inerventionNameTxt.getText().toString();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);

        AEDRequest request = new AEDRequest();
        request.setVillage(village);
        request.setIntervention1(intervention1);

        if (aedBinding.subComponentsTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention2(intervention2);
        } else {
            request.setIntervention2("0");
        }

        if (aedBinding.stageTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention3(intervention3);
        } else {
            request.setIntervention3("0");
        }
        request.setFarmer_name(farmerName);
        request.setGender(gender);
        request.setCategory(category1);
        request.setSurvey_no(survey_no);
        request.setArea(area);
        request.setImage1(firstImageBase64.trim());
        request.setRemarks(remarks);
        request.setCreated_by(SharedPrefsUtils.getString(context, SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
        request.setCreated_date(dateField);
        request.setLat(lat);
        request.setLon(lon);
        request.setTank_name(near_tank);
        request.setTxn_date(mCommonFunction.getDateTime());
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setTxn_id("20200212120446");
        request.setDate(dateField);
        request.setStatus("1");
        request.setIntervention_type(interventionTypeVal);

        if (aedBinding.othersLayout.getVisibility() == View.VISIBLE) {
            request.setOther_intervention(interventionName);
        } else {
            request.setOther_intervention("null");
        }

        if (mCommonFunction.isNetworkAvailable() == true) {
            mCommonFunction.showProgress();
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineAedRequest == null) {
                offlineAedRequest = new ArrayList<>();
                offlineAedRequest.add(request);
                offlineImageAedRequest = new ArrayList<>();
                offlineImageAedRequest.add(secondImageBase64);
                SharedPrefsUtils.saveAEDArrayList(context, offlineAedRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
                SharedPrefsUtils.saveArrayListAedImage(context, offlineImageAedRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);

                offlineText = "Data saved successfully in offline data";

            } else if (offlineAedRequest.size() < 10) {
                offlineAedRequest.add(request);
                offlineImageAedRequest.add(secondImageBase64);
                SharedPrefsUtils.saveAEDArrayList(context, offlineAedRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
                SharedPrefsUtils.saveArrayListAedImage(context, offlineImageAedRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);
                offlineText = "Data saved successfully in offline data";

            } else {
                offlineText = "You’ve reached the offline Data Limit,Please Sync!";
            }
            showMessageOKCancel(offlineText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA, offlineText);
                    mCommonFunction.navigation(getActivity(), DashboardActivity.class);
                }
            });
        }


    }

    private void onlineDataUpload(AEDRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AEDResponse> userDataCall = null;
        userDataCall = call.getAEDResponse(request);
        userDataCall.enqueue(new Callback<AEDResponse>() {
            @Override
            public void onResponse(Call<AEDResponse> call, Response<AEDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getAedLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImage(txt_id);
                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                    mCommonFunction.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<AEDResponse> call, Throwable t) {
                mCommonFunction.hideProgress();
            }
        });

    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("4");
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
                        Log.i(TAG, "onSuccessMsg" + successMessage);
//                        SharedPrefsUtils.putString(getContext(), SharedPrefsUtils.PREF_KEY.SuccessMessage, successMessage);
                        Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();
                        mCommonFunction.navigation(getContext(), DashboardActivity.class);


                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                }
                mCommonFunction.hideProgress();

            }

            @Override
            public void onFailure(Call<SecondImageResponse> call, Throwable t) {
                mCommonFunction.hideProgress();

            }
        });

    }

    @Override
    public void onSelectedInputs(LookUpDataClass lookUpDataClass) {
        intervention1 = lookUpDataClass.getIntervention1();
        intervention2 = lookUpDataClass.getIntervention2();
        intervention3 = lookUpDataClass.getIntervention3();
        componentValue = lookUpDataClass.getComponentValue();
        subComponentValue = lookUpDataClass.getSubComponentValue();
        stageValue = lookUpDataClass.getStageValue();
        stageLastValue = lookUpDataClass.getStagelastvalue();
        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
    }
}