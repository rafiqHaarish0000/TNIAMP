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
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.MarkRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.MarkResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentMarketingBinding;
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
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
import com.farmwiseai.tniamp.utils.componentCallApis.MarketingCallApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketingFragment extends Fragment implements View.OnClickListener, BackPressListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3; // stages
    public BackPressListener backPressListener;
    public String lat;
    public String lon;
    public EditText wauText, memberTxt;
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageName = null;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stageValue = null;
    public String stageLastValue = null;
    public String cropNameVal;
    public String seasonNameVal;
    public String catNameVal;
    FragmentMarketingBinding marketingBinding;
    //    public String seasonNameVal = null;
    ArrayList<MarkRequest> offlineMarkRequest = new ArrayList<>();
    ArrayList<String>offlineMarkImageRequest;
    DatePickerDialog picker;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, lengthValue, lsPointValue, sliceNumberValue, near_tank, remarks, dateField;
    private List<ComponentData> componentDropDown;
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private GPSTracker gpsTracker;
    private List<VillageData> villageDataList;
    private List<String> interventionList, category1, categoryExpo, traningList, cropList, seasonList;
    private VillageAdaapter villageAdaapter;
    private CharSequence myString = "0";
    private CharSequence posValue = "0";
    private ComponentAdapter adapter, adapter2;
    private SubBasinAdapter subAdapter;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private Spinner subBasinSpinner, districtSpinner,
            blockSpinner, componentSpinner,
            sub_componentSpinner, cropSpinner, seasonSpinner, categorySpinner1,
            categorySpinnerExpos, villageSpinner, interventionSpinner, trainingSpinner, stageSpinner;
    private EditText datePicker;
    private MarketingCallApi marketingCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList;
    private LinearLayout layout1, layout2, layoutTrain, layoutExpo, otherLyt, newReqLayout, businessLyt;
    private String villageValue, firstImageBase64, secondImageBase64, interventionTypeVal,
            cropVal, seasonVal, trainingVal, catogoryVal, categoryExpoVal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cropNameVal = null;
        seasonNameVal = null;
        catNameVal = null;

        mCommonFunction = new CommonFunction(getActivity());
        // Inflate the layout for this fragment
        marketingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_marketing, container, false);
        marketingBinding.popBackImage.setOnClickListener(this);
        marketingBinding.submissionBtn.setOnClickListener(this);
        marketingBinding.image1.setOnClickListener(this);
        marketingBinding.image2.setOnClickListener(this);
        marketingBinding.dateFrom.setOnClickListener(this);
        marketingBinding.dateFrom1.setOnClickListener(this);
        marketingBinding.dateTo.setOnClickListener(this);
        marketingBinding.dateTo2.setOnClickListener(this);
        marketingBinding.incorporationDateTxt.setOnClickListener(this);
        marketingBinding.inNumberDate.setOnClickListener(this);
        marketingBinding.DOCompletion.setOnClickListener(this);
        marketingBinding.durationVisit.setOnClickListener(this);
        marketingBinding.dateOfRealease.setOnClickListener(this);


        remarks = marketingBinding.remarksTxt.getText().toString().trim();


        componentSpinner = marketingBinding.componentTxt;
        sub_componentSpinner = marketingBinding.subComponentsTxt;
        stageSpinner = marketingBinding.stageTxt;
        otherLyt = marketingBinding.othersLayout;
        layout1 = marketingBinding.layout1;
        layout2 = marketingBinding.layout2;
        layoutTrain = marketingBinding.trainingLyt;
        layoutExpo = marketingBinding.exposureVistlyt;
        newReqLayout = marketingBinding.businessPlanLyt;


        backPressListener = this;

        marketingCallApi = new MarketingCallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        marketingCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stageSpinner, layout1, layout2, layoutTrain, layoutExpo, otherLyt, newReqLayout);
        offlineMarkRequest = SharedPrefsUtils.getMarkArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);

        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);

        setAllDropDownData();

        return marketingBinding.getRoot();
    }

    private boolean fieldValidation(String category,
                                    String remarks, String date) {

        remarks = marketingBinding.remarksTxt.getText().toString().trim();
        String traningDatefrom = marketingBinding.dateFrom1.getText().toString().trim();
        String traningDateTo = marketingBinding.dateTo2.getText().toString().trim();
       /* if (componentValue != null) {
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
            mCommonFunction.mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        }
        //other layout
        else if (otherLyt.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (marketingBinding.inerventionNameTxt.getText().toString().trim().length() == 0) {
                marketingBinding.inerventionNameTxt.setError("field empty");
                return false;
            }
        }
        //layout 1
        else if (layout1.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (marketingBinding.numberFPC.getText().toString().trim().isEmpty()) {
                marketingBinding.numberFPC.setError("field empty");
                return false;
            } else if (marketingBinding.inNumberDate.getText().toString().trim().isEmpty()
                    || marketingBinding.inNumberDate.getText().toString().trim().length() == 0) {
                marketingBinding.inNumberDate.setError("field empty");
                return false;
            } else if (marketingBinding.noVillage.getText().toString().trim().isEmpty()) {
                marketingBinding.noVillage.setError("field empty");
                return false;
            } else if (marketingBinding.noOfFigs.getText().toString().trim().isEmpty()) {
                marketingBinding.noOfFigs.setError("field empty");
                return false;
            } else if (marketingBinding.noOfMembers.getText().toString().trim().isEmpty()) {
                marketingBinding.noOfMembers.setError("field empty");
                return false;
            } else if (marketingBinding.maleOthers.getText().toString().trim().isEmpty()) {
                marketingBinding.maleOthers.setError("field empty");
                return false;
            } else if (marketingBinding.maleNo.getText().toString().trim().isEmpty()) {
                marketingBinding.maleNo.setError("field empty");
                return false;
            } else if (marketingBinding.femaleOthers.getText().toString().trim().isEmpty()) {
                marketingBinding.femaleOthers.setError("field empty");
                return false;
            } else if (marketingBinding.femaleNo.getText().toString().trim().isEmpty()) {
                marketingBinding.maleNo.setError("field empty");
                return false;
            } else if (cropNameVal == null || seasonNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (marketingBinding.consultingAgency.getText().toString().trim().isEmpty()) {
                marketingBinding.consultingAgency.setError("field empty");
                return false;
            } else if (marketingBinding.incorporationNumber.getText().toString().trim().isEmpty()) {
                marketingBinding.incorporationNumber.setError("field empty");
                return false;
            } else if (marketingBinding.incorporationDateTxt.getText().toString().trim().isEmpty()) {
                marketingBinding.incorporationDateTxt.setError("field empty");
                return false;
            } else if (marketingBinding.locationConsultancyTxt.getText().toString().trim().isEmpty()) {
                marketingBinding.locationConsultancyTxt.setError("field empty");
                return false;
            } else if (marketingBinding.nameOfCeo.getText().toString().trim().isEmpty()) {
                marketingBinding.nameOfCeo.setError("field empty");
                return false;
            } else if (marketingBinding.mobileNumber.getText().toString().trim().isEmpty()
                    || marketingBinding.mobileNumber.length() < 10) {
                marketingBinding.mobileNumber.setError("Please enter the valid mobile number");
                return false;
            } else if (!ValidationUtils.isValidMobileNumber(marketingBinding.mobileNumber.getText().toString().trim())) {
                marketingBinding.mobileNumber.setError("Please enter the valid mobile number");
                return false;
            } else if (marketingBinding.emailTxt.getText().toString().trim().isEmpty() || !ValidationUtils.isValidEmail(marketingBinding.emailTxt.getText().toString().trim())) {
                marketingBinding.emailTxt.setError("Invalid Email Id");
                return false;
            } else if (marketingBinding.infrastructureTxt.getText().toString().trim().isEmpty()) {
                marketingBinding.infrastructureTxt.setError("field empty");
                return false;
            }

        }
        //layout 2
        else if (layout2.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (catNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (marketingBinding.nameValue.getText().toString().trim().isEmpty()) {
                marketingBinding.nameValue.setError("field empty");
                return false;
            } else if (marketingBinding.capacity.getText().toString().trim().isEmpty()) {
                marketingBinding.capacity.setError("field empty");
                return false;
            } else if (marketingBinding.DOCompletion.getText().toString().trim().isEmpty() ||
                    marketingBinding.DOCompletion.getText().toString().trim().length() == 0) {
                marketingBinding.DOCompletion.setError("field empty");
                return false;
            } else if (marketingBinding.QantityStoredTXT.getText().toString().trim().isEmpty()) {
                marketingBinding.QantityStoredTXT.setError("field empty");
                return false;
            } else if (marketingBinding.noOfBeneficieries.getText().toString().trim().isEmpty()) {
                marketingBinding.noOfBeneficieries.setError("field empty");
                return false;
            } else if (marketingBinding.govertShare.getText().toString().trim().isEmpty()) {
                marketingBinding.govertShare.setError("field empty");
                return false;
            } else if (marketingBinding.contributionTxt.getText().toString().trim().isEmpty()) {
                marketingBinding.contributionTxt.setError("field empty");
                return false;
            }
        }
        //training layout
        else if (marketingBinding.trainingLyt.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (marketingBinding.venue.getText().toString().trim().isEmpty()) {
                marketingBinding.venue.setError("field empty");
                return false;
            } else if (marketingBinding.durationTrainigTxt.getText().toString().trim().isEmpty() ||
                    marketingBinding.durationTrainigTxt.getText().toString().trim().length() == 0) {
                marketingBinding.durationTrainigTxt.setError("field empty");
                return false;
            } else if (marketingBinding.noOftrainee.getText().toString().trim().isEmpty()) {
                marketingBinding.noOftrainee.setError("field empty");
                return false;
            } else if (marketingBinding.traineeOtherNo.getText().toString().trim().isEmpty()) {
                marketingBinding.traineeOtherNo.setError("field empty");
                return false;
            } else if (marketingBinding.traineeSCSTNO.getText().toString().trim().isEmpty()) {
                marketingBinding.traineeSCSTNO.setError("field empty");
                return false;
            } else if (marketingBinding.traineeOtherNoF.getText().toString().trim().isEmpty()) {
                marketingBinding.traineeOtherNoF.setError("field empty");
                return false;
            } else if (marketingBinding.traineeSCSTNOF.getText().toString().trim().isEmpty()) {
                marketingBinding.traineeSCSTNOF.setError("field empty");
                return false;
            } else if (marketingBinding.dateFrom1.getText().toString().trim().isEmpty()) {
                marketingBinding.dateFrom1.setError("field empty");
                return false;
            } else if (marketingBinding.dateTo2.getText().toString().trim().isEmpty()) {
                marketingBinding.dateTo2.setError("field empty");
                return false;
            }
        }
        //exposure layout
        else if (marketingBinding.exposureVistlyt.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (marketingBinding.nameHq.getText().toString().trim().isEmpty()) {
                marketingBinding.nameHq.setError("field empty");
                return false;
            } else if (marketingBinding.exposeNumber.getText().toString().trim().isEmpty()) {
                marketingBinding.exposeNumber.setError("field empty");
                return false;
            } else if (marketingBinding.durationVisit.getText().toString().trim().isEmpty() ||
                    marketingBinding.durationVisit.getText().toString().trim().length() == 0) {
                marketingBinding.durationVisit.setError("field empty");
                return false;
            } else if (marketingBinding.otherNoE.getText().toString().trim().isEmpty()) {
                marketingBinding.otherNoE.setError("field empty");
                return false;
            } else if (marketingBinding.scStNOE.getText().toString().trim().isEmpty()) {
                marketingBinding.scStNOE.setError("field empty");
                return false;
            } else if (marketingBinding.scStNOEF.getText().toString().trim().isEmpty()) {
                marketingBinding.scStNOEF.setError("field empty");
                return false;
            } else if (marketingBinding.dateFrom.getText().toString().trim().isEmpty()) {
                marketingBinding.dateFrom.setError("field empty");
                return false;
            } else if (marketingBinding.dateTo.getText().toString().trim().isEmpty()) {
                marketingBinding.dateTo.setError("field empty");
                return false;
            }else if (marketingBinding.country.getText().toString().trim().isEmpty()) {
                marketingBinding.country.setError("field empty");
                return false;
            } else if (marketingBinding.places.getText().toString().trim().isEmpty()) {
                marketingBinding.places.setError("field empty");
                return false;
            }
        }
        //newReqLayout
        else if (newReqLayout.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (marketingBinding.sINo.getText().toString().trim().isEmpty()) {
                marketingBinding.sINo.setError("field empty");
                return false;
            } else if (marketingBinding.nameOfbene.getText().toString().trim().isEmpty()) {
                marketingBinding.nameOfbene.setError("field empty");
                return false;
            } else if (marketingBinding.villageName.getText().toString().trim().isEmpty()) {
                marketingBinding.villageName.setError("field empty");
                return false;
            } else if (marketingBinding.nameOfProject.getText().toString().trim().isEmpty()) {
                marketingBinding.nameOfProject.setError("field empty");
                return false;
            } else if (marketingBinding.costOfProject.getText().toString().trim().isEmpty()) {
                marketingBinding.costOfProject.setError("field empty");
                return false;
            } else if (marketingBinding.dateOfRealease.getText().toString().trim().isEmpty()) {
                marketingBinding.dateOfRealease.setError("field empty");
                return false;
            } else if (marketingBinding.totalGrant.getText().toString().trim().isEmpty() ||
                    marketingBinding.totalGrant.getText().toString().trim().length() == 0) {
                marketingBinding.totalGrant.setError("field empty");
                return false;
            }
        } else if (otherLyt.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "businessPlan: " + true);
            if (marketingBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                marketingBinding.inerventionNameTxt.setError("field empty");
                return false;
            }
        }

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_back_image:
                Intent intent = new Intent(getContext(), DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                break;

            case R.id.submission_btn:
                boolean checkValidaiton = fieldValidation(near_tank, remarks, dateField);

                Log.i(TAG, "componentTxt: " + componentSpinner.getSelectedItem());
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //do the code for save all data
                    //  Toast.makeText(context, "Server error.!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.image_1:
                if (checkPermission()) {
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
                if (checkPermission()) {
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
            case R.id.dateFrom:
                dateFieldValidation(marketingBinding.dateFrom);
                break;
            case R.id.dateTo:
                dateFieldValidation(marketingBinding.dateTo);
                break;
            case R.id.dateFrom1:
                dateFieldValidation(marketingBinding.dateFrom1);
                break;
            case R.id.dateTo2:
                dateFieldValidation(marketingBinding.dateTo2);
                break;
            case R.id.incorporationDateTxt:
                dateFieldValidation(marketingBinding.incorporationDateTxt);
                break;
            case R.id.inNumberDate:
                dateFieldValidation(marketingBinding.inNumberDate);
                break;
            case R.id.DOCompletion:
                dateFieldValidation(marketingBinding.DOCompletion);
                break;
            case R.id.durationVisit:
                dateFieldValidation(marketingBinding.durationVisit);
                break;
            case R.id.dateOfRealease:
                dateFieldValidation(marketingBinding.dateOfRealease);
                break;

        }
    }

    private void dateOfSowing_Planting(EditText datePicker) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        long maxTime = cldr.getTimeInMillis();

        // Move day as first day of the month
        cldr.set(Calendar.DAY_OF_MONTH, day);
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

    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = marketingBinding.subBasinTxt;
        districtSpinner = marketingBinding.districtTxt;
        blockSpinner = marketingBinding.blockTxt;
        componentSpinner = marketingBinding.componentTxt;
        sub_componentSpinner = marketingBinding.subComponentsTxt;
        villageSpinner = marketingBinding.villageTxt;
        interventionSpinner = marketingBinding.inverntionTyper;
        categorySpinner1 = marketingBinding.categorySpinner;
        categorySpinnerExpos = marketingBinding.exposureCategory;
        cropSpinner = marketingBinding.cropSpinner;
        seasonSpinner = marketingBinding.seasonSpinner;
        trainingSpinner = marketingBinding.nameOFTrainingSpinner;
        stageSpinner = marketingBinding.stageTxt;


        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        marketingBinding.phase1.setItem(phraseList);


//phase drop down spinner
        marketingBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                subBasinValue = null;
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(marketingBinding.phase1.getSelectedItemPosition());
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

        interventionList = new ArrayList<>();
        interventionList.add("Demo");
        interventionList.add("Sustainability");
        interventionList.add("Adoption");
        marketingBinding.inverntionTyper.setItem(interventionList);
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

        cropList = new ArrayList<>();
        cropList.add("Pulses");
        cropList.add("Oilseeds");
        cropList.add("Minor millets");
        cropList.add("Topioca");
        cropList.add("Coconut");
        cropList.add("Banana");
        cropList.add("Chillies");
        cropList.add("Maize");
        marketingBinding.cropSpinner.setItem(cropList);
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropVal = String.valueOf(cropSpinner.getSelectedItemPosition());
                cropNameVal = cropSpinner.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + cropVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        seasonList = new ArrayList<>();
        seasonList.add("Kharif");
        seasonList.add("Rabi");
        marketingBinding.seasonSpinner.setItem(seasonList);
        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seasonVal = String.valueOf(seasonSpinner.getSelectedItemPosition());
                seasonNameVal = seasonSpinner.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + cropVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryList = new ArrayList<>();
        categoryList.add("RM");
        categoryList.add("PACB");
        categoryList.add("Farmer's farm");
        categoryList.add("Agro Entrepreneur");
        marketingBinding.categorySpinner.setItem(categoryList);
        categorySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catogoryVal = String.valueOf(categorySpinner1.getSelectedItemPosition());
                catNameVal = categorySpinner1.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + cropVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryExpo = new ArrayList<>();
        categoryExpo.add("Official");
        categoryExpo.add("Farmers");
        marketingBinding.exposureCategory.setItem(categoryExpo);
        categorySpinnerExpos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryExpoVal = String.valueOf(categorySpinnerExpos.getSelectedItemPosition());
                Log.i(TAG, "interventionType:" + categoryExpoVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        traningList = new ArrayList<>();
        traningList.add("Interface workshop");
        traningList.add("Officers training");
        traningList.add("Farmers training");
        traningList.add("Facilitation workshop");
        marketingBinding.nameOFTrainingSpinner.setItem(traningList);
        trainingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                trainingVal = String.valueOf(trainingSpinner.getSelectedItemPosition());
                Log.i(TAG, "interventionType:" + trainingVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
            if (resultCode == Activity.RESULT_OK) {
                if (takePicture && valueofPic == 1) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    marketingBinding.image1.setImageBitmap(photo);
                    // BitMap is data structure of image file which store the image in memory
                    firstImageBase64 = getEncodedString(photo);
                } else if (!takePicture && valueofPic == 2) {
                    Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    marketingBinding.image2.setImageBitmap(photo2);
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

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.NO_WRAP);


    }

    private void finalSubmission() {
        getAllData();
    }

    private void getAllData() {

        remarks = marketingBinding.remarksTxt.getText().toString().trim();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);

        MarkRequest request = new MarkRequest();


        request.setCreated_by(SharedPrefsUtils.getString(context, SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));

        request.setCreated_date("2020-05-26 06:51:12");
        request.setDate_from("2019-11-27");
        request.setDate_to("");
        request.setDate_of_completion("");

        request.setIntervention1(intervention1);

        if (marketingBinding.subComponentsTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention2(intervention2);
        } else {
            request.setIntervention2("0");
        }
        if (marketingBinding.stageTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention3(intervention3);
        } else {
            request.setIntervention3("0");
        }

        request.setIntervention4("");


        //layout 1
        if (marketingBinding.layout1.getVisibility() == View.VISIBLE) {
            request.setNof_village(marketingBinding.noVillage.getText().toString());
            request.setOthers_female_no(marketingBinding.femaleOthers.getText().toString());
            request.setOthers_male_no(marketingBinding.maleOthers.getText().toString());
            request.setSc_st_male_no(marketingBinding.maleNo.getText().toString());
            request.setSc_st_female_no(marketingBinding.femaleNo.getText().toString());
            request.setNof_mem(marketingBinding.noOfMembers.getText().toString());
            request.setMobile(marketingBinding.mobileNumber.getText().toString().trim());
            request.setName(marketingBinding.nameValue.getText().toString().trim());
            request.setEmail(marketingBinding.emailTxt.getText().toString().trim());
            request.setCrop(cropVal);
            request.setInfra_avail(marketingBinding.infrastructureTxt.getText().toString());
        } else {
            request.setFpc_name("1");
            request.setNof_village("1");
            request.setOthers_female_no("1");
            request.setOthers_male_no("1");
            request.setSc_st_male_no("1");
            request.setSc_st_female_no("1");
            request.setNof_mem("1");
            request.setMobile("1");
            request.setName("");
            request.setEmail("");
            request.setCrop(cropVal);
            request.setInfra_avail("");
        }

        if (marketingBinding.layout2.getVisibility() == View.VISIBLE) {
            request.setNo_of_beneficeries(marketingBinding.noOfBeneficieries.getText().toString().trim());
            request.setGovt_share(marketingBinding.govertShare.getText().toString());
            request.setCapacity(marketingBinding.capacity.getText().toString());
            request.setGodown_category(catNameVal);
        } else {
            request.setNo_of_beneficeries("0");
            request.setGovt_share("0");
            request.setCapacity("0");
            request.setGodown_category("");
        }


        if (marketingBinding.trainingLyt.getVisibility() == View.VISIBLE) {
            request.setNof_female(marketingBinding.traineeOtherNoF.getText().toString().trim());
            request.setNof_male(marketingBinding.traineeOtherNo.getText().toString().trim());
            request.setFemale_no1(marketingBinding.traineeOtherNoF.getText().toString().trim());
            request.setFemale_no2(marketingBinding.traineeSCSTNOF.getText().toString().trim());
            request.setVenue(marketingBinding.venue.getText().toString().trim());
            request.setNumber_trainees(marketingBinding.noOftrainee.getText().toString());
            request.setTraining_name(trainingVal);
        } else {
            request.setNof_female("1");
            request.setNof_male("1");
            request.setFemale_no1("1");
            request.setFemale_no2("1");
            request.setVenue("1");
            request.setNumber_trainees("1");
            request.setTraining_name("null");
        }

        if (marketingBinding.exposureVistlyt.getVisibility() == View.VISIBLE) {
            request.setFpc_name(marketingBinding.nameHq.getText().toString());
            request.setCategory2(categoryExpoVal);

        } else {
            request.setFpc_name("null");
            request.setCategory2("");
        }

        request.setVillage(villageValue);
        request.setLat(lat);
        request.setLon(lon);

        request.setCategory(catogoryVal);
        request.setImage1(firstImageBase64.trim());
        request.setImage2(secondImageBase64.trim());
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lat);
        request.setRemarks(remarks);
        request.setTxn_id("20191127172744");
        request.setDate("");
        request.setStatus("1");

        if (mCommonFunction.isNetworkAvailable() == true) {
            ObjectMapper mapper = new ObjectMapper();
            String json = null;
            try {
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println(json);
            mCommonFunction.showProgress();
            onlineDataUpload(request);

        } else {
            String offlineText = "";
            if (offlineMarkRequest == null) {
                offlineMarkRequest = new ArrayList<>();
                offlineMarkRequest.add(request);
                offlineMarkImageRequest= new ArrayList<>();
                offlineMarkImageRequest.add(secondImageBase64);
                SharedPrefsUtils.saveMarkArrayList(context, offlineMarkRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
                SharedPrefsUtils.saveArrayListMarkImage(context, offlineMarkImageRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_MARKETING);
                offlineText = "Data saved successfully in offline data";

            } else if (offlineMarkRequest.size() < 10) {
                offlineMarkRequest.add(request);
                offlineMarkImageRequest.add(secondImageBase64);
                SharedPrefsUtils.saveMarkArrayList(context, offlineMarkRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
                SharedPrefsUtils.saveArrayListMarkImage(context, offlineMarkImageRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_MARKETING);
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

    private void onlineDataUpload(MarkRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<MarkResponse> userDataCall = null;
        userDataCall = call.getMarkResponse(request);
        userDataCall.enqueue(new Callback<MarkResponse>() {
            @Override
            public void onResponse(Call<MarkResponse> call, Response<MarkResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getMarketinglanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImage(txt_id);

                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                    mCommonFunction.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<MarkResponse> call, Throwable t) {
                mCommonFunction.hideProgress();

            }
        });

    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("7");
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