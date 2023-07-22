package com.farmwiseai.tniamp.Ui.Fragment;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3;
    public String intervention4; // stages// stages
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
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageName = null;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stageValue = null;
    public String stageLastValue = null;
    public String genderNameVal;
    public String catVal;
    public BackPressListener backPressListener;
    FragmentHorticultureBinding horticultureBinding;
    DatePickerDialog picker;
    ArrayList<HortiRequest> offlineHortiRequest = new ArrayList<>();
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, farmerName, category1, survey_no, area, near_tank, remarks, dateField, mobileNumber;
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
            genderSpinner, categorySpinner, villageSpinner, interventionSpinner, cropstagespinner;
    private EditText datePicker;
    private HortiCallApi hortiCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList, interventionList;
    private LinearLayout vis_lyt, trainingLyt, iNames_lyt;
    private GPSTracker gpsTracker;
    private double lati, longi;
    private String villageValue, firstImageBase64, secondImageBase64, interventionTypeVal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        genderNameVal = null;
        catVal = null;

        mCommonFunction = new CommonFunction(getActivity());

        horticultureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_horticulture, container, false);

        horticultureBinding.areaTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        horticultureBinding.popBackImage.setOnClickListener(this);
        horticultureBinding.submissionBtn.setOnClickListener(this);
        horticultureBinding.image1.setOnClickListener(this);
        horticultureBinding.image2.setOnClickListener(this);
        horticultureBinding.dateTxt.setOnClickListener(this);


        farmerName = horticultureBinding.farmerTxt.getText().toString().trim();
        survey_no = horticultureBinding.surveyTxt.getText().toString().trim();
        area = horticultureBinding.areaTxt.getText().toString().trim();
        near_tank = horticultureBinding.tankTxt.getText().toString().trim();
        remarks = horticultureBinding.remarksTxt.getText().toString().trim();
        dateField = horticultureBinding.dateTxt.getText().toString().trim();
        backPressListener = this;
        componentSpinner = horticultureBinding.componentTxt;
        sub_componentSpinner = horticultureBinding.subComponentsTxt;
        stagesSpinner = horticultureBinding.stagesTxt;
        cropstagespinner = horticultureBinding.cropStages;

        datePicker = horticultureBinding.dateTxt;
        vis_lyt = horticultureBinding.visibilityLyt;
        trainingLyt = horticultureBinding.iecLayt;
        intName = horticultureBinding.inerventionNameTxt.getText().toString().trim();
        iNames_lyt = horticultureBinding.inerventionLyt;
        mobileNumber = horticultureBinding.mobileNumbertxt.getText().toString().trim();
        hortiCallApi = new HortiCallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        hortiCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, cropstagespinner, stagesSpinner, datePicker, vis_lyt, trainingLyt, iNames_lyt);

        offlineHortiRequest = SharedPrefsUtils.getHortiArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);

        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);

        setAllDropDownData();


        return horticultureBinding.getRoot();

    }

    private boolean fieldValidation(String farmerName, String category, String gender,
                                    String survey_no, String area, String near_tank, String remarks, String date, String intName, String mobileNumber) {

        farmerName = horticultureBinding.farmerTxt.getText().toString().trim();
        survey_no = horticultureBinding.surveyTxt.getText().toString().trim();
        area = horticultureBinding.areaTxt.getText().toString().trim();
        near_tank = horticultureBinding.tankTxt.getText().toString().trim();
        remarks = horticultureBinding.remarksTxt.getText().toString().trim();
        date = horticultureBinding.dateTxt.getText().toString().trim();
        intName = horticultureBinding.inerventionNameTxt.getText().toString().trim();
        mobileNumber = horticultureBinding.mobileNumbertxt.getText().toString().trim();

      /*  if (componentValue != null) {
            if (componentValue.equalsIgnoreCase("Others"))
                subComponentValue = "Dummy data";
        }*/

        if (subBasinValue == null || districtValue == null || blockValue == null ||
                villageName == null || componentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (sub_componentSpinner.getVisibility() == View.VISIBLE && subComponentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (horticultureBinding.stagesTxt.getVisibility() == View.VISIBLE && stageLastValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (horticultureBinding.cropStages.getVisibility() == View.VISIBLE && stageValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (datePicker.getVisibility() == View.VISIBLE && date.isEmpty()) {
            horticultureBinding.dateTxt.setError("Please select Date");
            return false;
        } else if (valueofPicCount == 0 || valueofPicCount < 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        } else if (vis_lyt.getVisibility() == View.VISIBLE) {
            if (farmerName.length() == 0) {
                horticultureBinding.farmerTxt.setError("Please enter farmer name");
                return false;
            }
            if (survey_no.length() == 0) {
                horticultureBinding.surveyTxt.setError("Please enter survey no");
                return false;
            }
            if (area.length() == 0) {
                horticultureBinding.areaTxt.setError("Please enter area");
                return false;
            } else if (Double.valueOf(area) > 2.0) {
                horticultureBinding.areaTxt.setError("Area Should be less than 2(ha)");
                return false;
            }
            if (mobileNumber.isEmpty() || (mobileNumber.length() < 10)) {
                horticultureBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (ValidationUtils.isValidMobileNumber(mobileNumber) == false) {
                horticultureBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (gender == null && genderSpinner.getVisibility() == View.VISIBLE) {

                Toast.makeText(getActivity(), "Please select Gender", Toast.LENGTH_LONG).show();
                return false;

            } else if (category1 == null && categorySpinner.getVisibility() == View.VISIBLE) {
                Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_LONG).show();
                return false;
            } else if (horticultureBinding.yieldTxt.getText().toString().trim().isEmpty() && horticultureBinding.yieldTxt.getVisibility() == View.VISIBLE) {
                horticultureBinding.yieldTxt.setError("Please enter the yield");
                return false;
            } else if (horticultureBinding.varietyTxt.getText().toString().trim().isEmpty() && horticultureBinding.varietyTxt.getVisibility() == View.VISIBLE) {
                horticultureBinding.varietyTxt.setError("Please enter the variety");
                return false;
            } else if (horticultureBinding.inerventionLyt.getVisibility() == View.VISIBLE) {
                if (horticultureBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                    horticultureBinding.inerventionNameTxt.setError("field empty");
                    return false;
                }

            }
        } else if (trainingLyt.getVisibility() == View.VISIBLE) {
            if (horticultureBinding.noOfParticipants.getText().toString().trim().isEmpty()) {
                horticultureBinding.noOfParticipants.setError("Do not empty field");
                return false;
            } else if (horticultureBinding.maleOthers.getText().toString().trim().isEmpty()) {
                horticultureBinding.maleOthers.setError("Do not empty field");
                return false;
            } else if (horticultureBinding.maleNo.getText().toString().trim().isEmpty()) {
                horticultureBinding.maleNo.setError("Do not empty field");
                return false;
            } else if (horticultureBinding.femaleOthers.getText().toString().trim().isEmpty()) {
                horticultureBinding.femaleOthers.setError("Do not empty field");
                return false;
            } else if (horticultureBinding.femaleNo.getText().toString().trim().isEmpty()) {
                horticultureBinding.femaleNo.setError("Do not empty field");
                return false;
            }
        }


        return true;
    }


    public void showToast(Activity mcontaxt, String message) {
        Toast.makeText(mcontaxt, message, Toast.LENGTH_SHORT).show();
    }


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
                boolean checkValidaiton = fieldValidation(farmerName,
                        category1, gender, survey_no, area, near_tank, remarks, dateField, intName, mobileNumber);
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //  showToast(getActivity(), "Validation error");
                }
                break;

            case R.id.image_1:
                if (PermissionUtils.checkPermission(context)) {
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


            case R.id.date_txt:
                dateFieldValidation(horticultureBinding.dateTxt);
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
        interventionSpinner = horticultureBinding.inverntionTyper;

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
                subBasinValue = null;
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                Log.i(TAG, "onResponse: " + horticultureBinding.phase1.getSelectedItemPosition());
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(horticultureBinding.phase1.getSelectedItemPosition());
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
                villageValue = String.valueOf(villageDataList.get(i).getID());
                villageName = villageDataList.get(i).getNAME();
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
                genderNameVal = genderSpinner.getSelectedItem().toString();
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
                category1 = categorySpinner.getSelectedItem().toString();
                catVal = categorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        interventionList = new ArrayList<>();
        interventionList.add("Demo");
        interventionList.add("Sustainability");
        interventionList.add("Adoption");
        horticultureBinding.inverntionTyper.setItem(interventionList);
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

    private void dateFieldValidation(EditText datePicker) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show();

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
            if (resultCode == Activity.RESULT_OK) {
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
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast toast = Toast.makeText(getContext(), "Canceled, no photo selected.", Toast.LENGTH_LONG);
            toast.show();

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

        getAllData();
    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }


    private void getAllData() {

        farmerName = horticultureBinding.farmerTxt.getText().toString().trim();
        survey_no = horticultureBinding.surveyTxt.getText().toString().trim();
        area = horticultureBinding.areaTxt.getText().toString().trim();
        area = horticultureBinding.areaTxt.getText().toString().trim();
        remarks = horticultureBinding.remarksTxt.getText().toString().trim();
        near_tank = horticultureBinding.tankTxt.getText().toString().trim();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);

        HortiRequest request = new HortiRequest();

        request.setVillage(villageValue);
        request.setIntervention1(intervention1);

        if (horticultureBinding.subComponentsTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention2(intervention2);
        } else {
            request.setIntervention2("0");
        }

        if (horticultureBinding.stagesTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention3(intervention3);
        } else {
            request.setIntervention3("0");
        }

        request.setFarmerName(farmerName);
        request.setGender(gender);
        request.setCategory(category1);
        request.setSurveyNo(survey_no);
        request.setArea(area);

        if (horticultureBinding.varietyTxt.getVisibility() == View.VISIBLE) {
            request.setVariety(horticultureBinding.varietyTxt.getText().toString().trim());
        } else {
            request.setVariety("null");
        }

        request.setImage1(firstImageBase64);

        if (horticultureBinding.yieldTxt.getVisibility() == View.VISIBLE) {
            request.setYield(horticultureBinding.yieldTxt.getText().toString().trim());
        } else {
            request.setYield("null");
        }

        request.setRemarks(remarks);
        request.setCreatedBy(SharedPrefsUtils.getString(context, SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
        request.setCreatedDate("2020-02-12 11:02:02");
        request.setLat(lat);
        request.setLon(lon);
        request.setTankName(near_tank);
        request.setTxnDate(mCommonFunction.getDateTime());
        request.setPhotoLat(lat);
        request.setPhotoLon(lon);
        request.setTxnId("20200212120446");
        request.setStatus("0");
        request.setInterventionType(interventionTypeVal);

        if (horticultureBinding.inerventionLyt.getVisibility() == View.VISIBLE) {
            request.setOtherIntervention(intName);
        } else {
            request.setOtherIntervention("null");
        }

        if (mCommonFunction.isNetworkAvailable() == true) {
            mCommonFunction.showProgress();
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineHortiRequest == null) {
                offlineHortiRequest = new ArrayList<>();
                offlineHortiRequest.add(request);
                SharedPrefsUtils.saveHortiArrayList(context, offlineHortiRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                offlineText = "Data saved successfully in offline data";

            } else if (offlineHortiRequest.size() < 5) {
                offlineHortiRequest.add(request);
                SharedPrefsUtils.saveHortiArrayList(context, offlineHortiRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                offlineText = "Data saved successfully in offline data";

            } else {
                offlineText = "You reached the offline Store Data limit please Sync !";
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

    private void onlineDataUpload(HortiRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<HortiResponse> userDataCall = null;
        userDataCall = call.getHortiResponse(request);
        userDataCall.enqueue(new Callback<HortiResponse>() {
            @Override
            public void onResponse(Call<HortiResponse> call, Response<HortiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getHortiLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        uploadSecondImage(txt_id);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                    mCommonFunction.dismiss();
                }
            }

            @Override
            public void onFailure(Call<HortiResponse> call, Throwable t) {
                mCommonFunction.dismiss();
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
//                        SharedPrefsUtils.putString(getContext(), SharedPrefsUtils.PREF_KEY.SuccessMessage, successMessage);
                        Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();
                        mCommonFunction.navigation(getContext(), DashboardActivity.class);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                }
                mCommonFunction.dismiss();

            }

            @Override
            public void onFailure(Call<SecondImageResponse> call, Throwable t) {
                mCommonFunction.dismiss();

            }
        });

    }

    @Override
    public void onSelectedInputs(LookUpDataClass lookUpDataClass) {
        intervention1 = lookUpDataClass.getIntervention1();
        intervention2 = lookUpDataClass.getIntervention2();
        intervention3 = lookUpDataClass.getIntervention3();
        intervention4 = lookUpDataClass.getIntervention4();
        componentValue = lookUpDataClass.getComponentValue();
        subComponentValue = lookUpDataClass.getSubComponentValue();
        stageValue = lookUpDataClass.getStageValue();
        stageLastValue = lookUpDataClass.getStagelastvalue();
        if (componentValue.equalsIgnoreCase("Micro Irrigation")
                || componentValue.equalsIgnoreCase(" Shade Net")
                || componentValue.equalsIgnoreCase("Mulching")) {
            horticultureBinding.varietyTxt.setVisibility(View.GONE);
            horticultureBinding.yieldTxt.setVisibility(View.GONE);
        } else if (subComponentValue.equalsIgnoreCase("Harvest")) {
            horticultureBinding.varietyTxt.setVisibility(View.VISIBLE);
            horticultureBinding.yieldTxt.setVisibility(View.VISIBLE);
        } else if (subComponentValue.equalsIgnoreCase("Distribution of Inputs")) {
            horticultureBinding.varietyTxt.setVisibility(View.GONE);
            horticultureBinding.yieldTxt.setVisibility(View.GONE);
        } else if (intervention4.equalsIgnoreCase("Harvest") ||
                intervention4.equalsIgnoreCase("1st Harvest") ||
                intervention4.equalsIgnoreCase("Last Harvest")) {
            horticultureBinding.varietyTxt.setVisibility(View.VISIBLE);
            horticultureBinding.yieldTxt.setVisibility(View.VISIBLE);
        } else {
            horticultureBinding.varietyTxt.setVisibility(View.GONE);
            horticultureBinding.yieldTxt.setVisibility(View.GONE);
        }

        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
    }
}