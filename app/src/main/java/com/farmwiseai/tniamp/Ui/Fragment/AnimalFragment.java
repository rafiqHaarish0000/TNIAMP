package com.farmwiseai.tniamp.Ui.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
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

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AnimalRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AnimalResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.MarkResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentAnimalBinding;
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
import com.farmwiseai.tniamp.utils.componentCallApis.AnimalCallApi;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimalFragment extends Fragment implements View.OnClickListener, BackPressListener {
    FragmentAnimalBinding animalBinding;
    private Context context;
    private String farmerName, survey_no, near_tank, remarks, dateField, area, nag, dag, darf, seedra, qop, nop, mon, moo,
            fon, foo, intName;
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
    private Spinner subBasinSpinner, districtSpinner, blockSpinner, componentSpinner,
            sub_componentSpinner, stagesSpinner, genderSpinner, categorySpinner, villageSpinner, interventionSpinner;
    private EditText datePicker;
    private AnimalRequest request;
    private AnimalCallApi animalCallApi;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private GPSTracker gpsTracker;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList, interventionList;
    private LinearLayout vis_lyt, trainingLyt, pregLyt, iNames_lyt;
    private double lati, longi;
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3;  // stages
    public String intervention4;  // stages
    public String farmer_name;
    public String gender = "";
    public String lat;
    public String lon;
    public String tank_name;
    public String date;
    public String status;
    public String venue;
    public String noFarmer;
    public BackPressListener backPressListener;
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageName = null;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stageValue = null;
    public String stageLastValue = null;
    DatePickerDialog picker;
    private String villageValue, category1 = "", firstImageBase64, secondImageBase64, interventionTypeVal;
    private String genderValue, catNameVal;
    ArrayList<AnimalRequest> offlineARDRequest = new ArrayList<AnimalRequest>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        genderValue = null;
        catNameVal = null;

        mCommonFunction = new CommonFunction(getActivity());

        animalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_animal, container, false);
        animalBinding.popBackImage.setOnClickListener(this);
        animalBinding.submissionBtn.setOnClickListener(this);
        animalBinding.image1.setOnClickListener(this);
        animalBinding.image2.setOnClickListener(this);
        animalBinding.dateTxt.setOnClickListener(this);
        animalBinding.areaTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        farmerName = animalBinding.farmerTxt.getText().toString();
        survey_no = animalBinding.surveyTxt.getText().toString();
        near_tank = animalBinding.tankTxt.getText().toString();
        remarks = animalBinding.remarksTxt.getText().toString();
        dateField = animalBinding.dateTxt.getText().toString();
        venue = animalBinding.venue.getText().toString();
        mon = animalBinding.maleNo.getText().toString();
        moo = animalBinding.scStNO.getText().toString();
        fon = animalBinding.femaleNo.getText().toString();
        foo = animalBinding.fScStNO.getText().toString();

        componentSpinner = animalBinding.componentTxt;
        sub_componentSpinner = animalBinding.subComponentsTxt;
        stagesSpinner = animalBinding.stagesTxt;
        datePicker = animalBinding.dateTxt;
        vis_lyt = animalBinding.visibilityLyt;
        trainingLyt = animalBinding.trainingLayout;
        EditText calves = animalBinding.noOfCalves;
        pregLyt = animalBinding.pregLyt;
        iNames_lyt = animalBinding.othersLayout;

        backPressListener = this;

        offlineARDRequest = SharedPrefsUtils.getARDArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);


        animalCallApi = new AnimalCallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        animalCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stagesSpinner, datePicker, calves, vis_lyt, trainingLyt, pregLyt, iNames_lyt);
        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);
        setAllDropDownData();


        return animalBinding.getRoot();
    }

    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = animalBinding.subBasinTxt;
        districtSpinner = animalBinding.districtTxt;
        blockSpinner = animalBinding.blockTxt;
        genderSpinner = animalBinding.genderTxt;
        categorySpinner = animalBinding.categoryTxt;
        componentSpinner = animalBinding.componentTxt;
        sub_componentSpinner = animalBinding.subComponentsTxt;
        stagesSpinner = animalBinding.stagesTxt;
        villageSpinner = animalBinding.villageTxt;
        datePicker = animalBinding.dateTxt;
        interventionSpinner = animalBinding.inverntionTyper;


        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        animalBinding.phase1.setItem(phraseList);


//phase drop down spinner
        animalBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                subBasinValue = null;
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                Log.i(TAG, "onResponse: " + animalBinding.phase1.getSelectedItemPosition());
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(animalBinding.phase1.getSelectedItemPosition());
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
                villageValue = String.valueOf(villageDataList.get(i).getID());
                villageName = String.valueOf(villageDataList.get(i).getNAME());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //gender dropdown list
        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        animalBinding.genderTxt.setItem(genderList);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = genderSpinner.getSelectedItem().toString();
                genderValue = genderSpinner.getSelectedItem().toString();
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
        animalBinding.categoryTxt.setItem(categoryList);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category1 = categorySpinner.getSelectedItem().toString();
                catNameVal = categorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        interventionList = new ArrayList<>();
        interventionList.add("Demo");
        interventionList.add("Sustainability");
        interventionList.add("Adoption");
        animalBinding.inverntionTyper.setItem(interventionList);

        interventionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                interventionTypeVal = String.valueOf(interventionSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private boolean fieldValidation(String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date,
                                    String nag, String dag, String dafr, String seedra, String qop, String nop,
                                    String moN, String mOO, String foN, String fOO, String intName) {

        farmerName = animalBinding.farmerTxt.getText().toString().trim();
        survey_no = animalBinding.surveyTxt.getText().toString();
        area = animalBinding.areaTxt.getText().toString();
        near_tank = animalBinding.tankTxt.getText().toString();
        remarks = animalBinding.remarksTxt.getText().toString();
        date = animalBinding.dateTxt.getText().toString();
        moN = animalBinding.maleNo.getText().toString();
        mOO = animalBinding.scStNO.getText().toString();
        foN = animalBinding.femaleNo.getText().toString();
        fOO = animalBinding.fScStNO.getText().toString();
        noFarmer = animalBinding.fScStNO.getText().toString();
        venue = animalBinding.fScStNO.getText().toString();

        String mobileNumber = animalBinding.mobileNumber.getText().toString().trim();

       if (componentValue != null) {
            if (componentValue.equalsIgnoreCase("Others"))
                subComponentValue = "Dummy data";
        }

        if (subBasinValue == null || districtValue == null || blockValue == null ||
                villageName == null || componentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
            return false;
        } else if (sub_componentSpinner.getVisibility() == View.VISIBLE && subComponentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (stagesSpinner.getVisibility() == View.VISIBLE && stageValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (valueofPicCount == 0 || valueofPicCount < 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        } else if (vis_lyt.getVisibility() == View.VISIBLE) {
            if (farmerName.length() == 0) {
                animalBinding.farmerTxt.setError("Please enter farmer name");
                return false;
            } else if (survey_no.length() == 0) {
                animalBinding.surveyTxt.setError("Please enter survey no");
                return false;
            } else if (area.length() == 0) {
                animalBinding.areaTxt.setError("Please enter area");
                return false;
            } else if (Double.valueOf(area) > 2.0) {
                animalBinding.areaTxt.setError("Area Should be less than 2(ha)");
                return false;
            }
            if (mobileNumber.isEmpty() || (mobileNumber.length() < 10)) {
                animalBinding.mobileNumber.setError("Please enter the valid mobile number");
                return false;
            } else if (ValidationUtils.isValidMobileNumber(mobileNumber) == false) {
                animalBinding.mobileNumber.setError("Please enter the valid mobile number");
                return false;
            } else if (genderValue == null || catNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            }


        } else if (trainingLyt.getVisibility() == View.VISIBLE) {

            if (animalBinding.noOfFarmers.getText().toString().isEmpty()) {
                animalBinding.noOfFarmers.setError("field empty");
                return false;
            } else if (animalBinding.venue.getText().toString().isEmpty()) {
                animalBinding.venue.setError("field empty");
                return false;
            } else if (animalBinding.otherNo.getText().toString().isEmpty()) {
                {
                    animalBinding.otherNo.setError("field empty");
                    return false;
                }
            } else if (animalBinding.femaleOthers.getText().toString().isEmpty()) {
                {
                    animalBinding.femaleOthers.setError("field empty");
                    return false;
                }
            }
        } else if (animalBinding.noOfCalves.getVisibility() == View.VISIBLE) {
            if (animalBinding.noOfCalves.getText().toString().isEmpty()) {
                animalBinding.noOfCalves.setError("field empty");
                return false;
            }
        } else if (iNames_lyt.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (animalBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                animalBinding.inerventionNameTxt.setError("field empty");
                return false;
            }
        }


        return true;
    }

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
                Intent intent = new Intent(getContext(), DashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.submission_btn:
                boolean checkValidaiton = fieldValidation(farmerName,
                        category1, survey_no, area, near_tank, remarks, dateField, nag, dag, darf, seedra, qop,
                        nop, mon, moo, fon, foo, intName);
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //   mLoadCustomToast(getActivity(), "Server error");
                }
                break;
            case R.id.image_1:
                if (checkPermission()) {

                    valueofPic = 1;
                    valueofPicCount++;
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
                    valueofPicCount++;
                    takePicture = false;
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                } else {
                    requestPermission();
                }
                break;

            case R.id.date_txt:
                dateFieldValidation(animalBinding.dateTxt);
                break;

        }
    }

    private void dateFieldValidation(EditText datePicker) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        long maxTime = cldr.getTimeInMillis();

        // Move day as first day of the month
        cldr.set(Calendar.DAY_OF_MONTH, 1);
        // Move "month" for previous one
        cldr.add(Calendar.MONTH, -1);

        // Min = time after changes
        long minTime = cldr.getTimeInMillis();
        // date picker dialog
        picker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.getDatePicker().setMaxDate(maxTime);
        picker.getDatePicker().setMinDate(minTime);
        picker.show();


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        animalBinding.dateTxt.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void finalSubmission() {
        getAllData();

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    private void getAllData() {
        request = new AnimalRequest();
        farmerName = animalBinding.farmerTxt.getText().toString();
        survey_no = animalBinding.surveyTxt.getText().toString();
        area = animalBinding.areaTxt.getText().toString();
        area = animalBinding.areaTxt.getText().toString();
        remarks = animalBinding.remarksTxt.getText().toString();
        dateField = animalBinding.dateTxt.getText().toString();
        near_tank = animalBinding.tankTxt.getText().toString();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);


        request.setVillage(villageValue);
        request.setIntervention1(intervention1);

        if (animalBinding.subComponentsTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention2(intervention2);
        } else {
            request.setIntervention2("0");
        }

        if (animalBinding.stagesTxt.getVisibility() == View.VISIBLE) {
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
        request.setFish_culture("null");
        request.setCreated_by("f55356773fce5b11");
        request.setCreated_date(dateField);
        request.setLat(lat);
        request.setLon(lon);
        request.setTank_name(near_tank);
        request.setTxn_date(mCommonFunction.getDateTime());
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setTxn_id("20200212120446");
        request.setDate("");
        request.setStatus("0");

        if (animalBinding.noOfCalves.getVisibility() == View.VISIBLE && animalBinding.noOfCalves.getHint().equals("No of Calves")) {
            request.setNo_of_calves(animalBinding.noOfCalves.getText().toString());
        } else {
            request.setNo_of_calves("0");
        }
        if (animalBinding.noOfCalves.getVisibility() == View.VISIBLE && animalBinding.noOfCalves.getHint().equals("No of Cows")) {
            request.setNo_of_calves(animalBinding.noOfCalves.getText().toString());
        } else {
            request.setNo_of_cows("0");
        }


        if (animalBinding.trainingLayout.getVisibility() == View.VISIBLE) {
            request.setNo_of_farmers(animalBinding.noOfFarmers.getText().toString());
            request.setOthers_female_no(animalBinding.femaleOthers.getText().toString());
            request.setOthers_male_no(animalBinding.otherNo.getText().toString());
            request.setSc_st_female_no(animalBinding.fScStNO.getText().toString());
            request.setSc_st_male_no(animalBinding.scStNO.getText().toString());
            request.setVenue(animalBinding.venue.getText().toString());
        } else {
            request.setNo_of_farmers("0");
            request.setOthers_female_no("0");
            request.setOthers_male_no("0");
            request.setSc_st_female_no("0");
            request.setSc_st_male_no("0");
            request.setVenue("null");
        }
        request.setMobile(animalBinding.mobileNumber.getText().toString());
        request.setIntervention_type("3");

        if (animalBinding.othersLayout.getVisibility() == View.VISIBLE) {
            request.setOther_intervention(animalBinding.inerventionNameTxt.getText().toString());
        } else {
            request.setOther_intervention("");
        }


//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        try {
//            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
//            request = new ObjectMapper().readValue(json, AnimalRequest.class);
//            System.out.println(json);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        if (mCommonFunction.isNetworkAvailable()) {
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineARDRequest == null) {
                offlineARDRequest = new ArrayList<>();
                offlineARDRequest.add(request);
                SharedPrefsUtils.saveARDArrayList(context, offlineARDRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                offlineText = "Data saved successfully in offline data";

            } else if (offlineARDRequest.size() < 5) {
                offlineARDRequest.add(request);
                SharedPrefsUtils.saveARDArrayList(context, offlineARDRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
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

    private void onlineDataUpload(AnimalRequest animalRequest) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AnimalResponse> userDataCall = null;
        userDataCall = call.getAnimalResponse(animalRequest);
        userDataCall.enqueue(new Callback<AnimalResponse>() {
            @Override
            public void onResponse(Call<AnimalResponse> call, Response<AnimalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getAnimallanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImage(txt_id);

                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AnimalResponse> call, Throwable t) {

            }
        });
        /*userDataCall.enqueue(new Callback<AnimalResponse>() {
            @Override
            public void onResponse(Call<AnimalResponse> call, Response<AnimalResponse> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getAnimallanddeptid());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        uploadSecondImage(txt_id);
//                        List<AgriResponse> agriResponses = new ArrayList<>();
//                        agriResponses.addAll(response.body().getResponse());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "data error.!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AnimalResponse> call, Throwable t) {

            }
        });*/
    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("5");
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
        componentValue = lookUpDataClass.getComponentValue();
        Log.i(TAG, "otherData: " + componentValue);
        if (componentValue.equalsIgnoreCase("Others")) {
            subComponentValue = "data";
        }
        subComponentValue = lookUpDataClass.getSubComponentValue();
        stageValue = lookUpDataClass.getStageValue();
        stageLastValue = lookUpDataClass.getStagelastvalue();
        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            if (takePicture && valueofPic == 1) {
                Bitmap photo1 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                animalBinding.image1.setImageBitmap(photo1);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo1);
                firstImageBase64 = getEncodedString(photo1);
//                Toast.makeText(getContext(), getEncodedString(photo1), Toast.LENGTH_LONG).show();


            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                animalBinding.image2.setImageBitmap(photo2);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo2);
                secondImageBase64 = getEncodedString(photo2);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast toast = Toast.makeText(getContext(), "Canceled, no photo selected.", Toast.LENGTH_LONG);
            toast.show();

        }

    }

    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);

        byte[] imageArr = os.toByteArray();

        String encodeImage = Base64.encodeToString(imageArr, Base64.NO_WRAP);

        return encodeImage;

    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }
}