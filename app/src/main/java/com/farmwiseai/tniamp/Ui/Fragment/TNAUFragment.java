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
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.TNAU_Response;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentTNAUBinding;
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
import com.farmwiseai.tniamp.utils.componentCallApis.TNAU_CallApi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TNAUFragment extends Fragment implements View.OnClickListener, BackPressListener {
    public static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3; // stages
    public String intervention4; // stages
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
    public String villageValue = null;
    private String remarksData;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stagevalue = null;
    public String stagelastvalue = null;
    public BackPressListener backPressListener;
    ArrayList<TNAU_Request> offlineRequest = new ArrayList<>();
    ArrayList<String> offlineImageRequest = new ArrayList<>();
    TNAU_Request request;
    DatePickerDialog picker;
    String regex = "^[6-9][0-9]{9}$";
    EditText varity, yeild;
    private FragmentTNAUBinding tnauBinding;
    private Context context;
    private String farmerName, category1, survey_no, area, near_tank, remarks, dateField, village, mobileNumber;
    private List<ComponentData> componentDropDown;
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private List<VillageData> villageDataList;
    private List<String> interventionList;
    private CharSequence myString = "0";
    private CharSequence posValue = "0";
    private ComponentAdapter adapter;
    private SubBasinAdapter subAdapter;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private VillageAdaapter villageAdaapter;
    private Spinner subBasinSpinner, districtSpinner, blockSpinner,
            componentSpinner, sub_componentSpinner, stagesSpinner,
            genderSpinner, categorySpinner, villageSpinner, interventionSpinner;
    private EditText datePicker;
    private TNAU_CallApi TNAUCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList;
    private GPSTracker gpsTracker;
    private LinearLayout hideLyt;
    private String firstImageBase64, secondImageBase64, interventionTypeVal;

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

        gender = null;
        category1 = null;
        tnauBinding.areaTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tnauBinding.popBackImage.setOnClickListener(this);
        tnauBinding.submissionBtn.setOnClickListener(this);
        tnauBinding.image1.setOnClickListener(this);
        tnauBinding.image2.setOnClickListener(this);
        tnauBinding.dateTxt.setOnClickListener(this);
        SharedPrefsUtils.putInt(context, SharedPrefsUtils.PREF_KEY.BACK_PRESSED, 1);

        offlineRequest = SharedPrefsUtils.getArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
        offlineImageRequest = SharedPrefsUtils.getArrayListImage(context, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);

        farmerName = tnauBinding.farmerTxt.getText().toString().trim();
        survey_no = tnauBinding.surveyTxt.getText().toString().trim();
        area = tnauBinding.areaTxt.getText().toString().trim();
        near_tank = tnauBinding.tankTxt.getText().toString().trim();
        remarks = tnauBinding.remarksTxt.getText().toString().trim();
        dateField = tnauBinding.dateTxt.getText().toString().trim();
        backPressListener = this;
        /*
        below component spinner is vary for all the department so please refer the callApi class
        component spinner visible the other two dropdowns according to the data
        date picker functionalities will only shown while user choose to sowing their crops
         */

        componentSpinner = tnauBinding.componentTxt;
        sub_componentSpinner = tnauBinding.subComponentsTxt;
        stagesSpinner = tnauBinding.stagesTxt;
        datePicker = tnauBinding.dateTxt;
        hideLyt = tnauBinding.visibilityLyt;
        varity = tnauBinding.varietyTxt;
        yeild = tnauBinding.yieldTxt;

        TNAUCallApi = new TNAU_CallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        TNAUCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stagesSpinner, datePicker, hideLyt, varity, yeild);
      /*  LookUpDataClass lookUpDataClass = new LookUpDataClass();
        Log.i(TAG, "onSelectedInputs: "+lookUpDataClass.getIntervention1());
      */
        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);

        setAllDropDownData();

//        if (lookUpDataClass.getIntervention3() != null &&
//                lookUpDataClass.getIntervention3().equalsIgnoreCase("Harvest")) {
//            tnauBinding.varietyTxt.setVisibility(View.VISIBLE);
//            tnauBinding.yieldTxt.setVisibility(View.VISIBLE);
//        } else {
//            tnauBinding.varietyTxt.setVisibility(View.GONE);
//            tnauBinding.yieldTxt.setVisibility(View.GONE);
//        }

        return tnauBinding.getRoot();

    }

    private void setSpinnerError(Spinner spinner, String errorMessage) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null) {
            spinner.requestFocus();
            mCommonFunction.mLoadCustomToast(getActivity(), errorMessage);
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    // validation for all mandatory fields
    private boolean fieldValidation(String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date, String mobileNumber) {

        farmerName = tnauBinding.farmerTxt.getText().toString().trim();
        survey_no = tnauBinding.surveyTxt.getText().toString().trim();
        area = tnauBinding.areaTxt.getText().toString().trim();
        near_tank = tnauBinding.tankTxt.getText().toString().trim();
        remarks = tnauBinding.remarksTxt.getText().toString().trim();
        date = tnauBinding.dateTxt.getText().toString().trim();
        mobileNumber = tnauBinding.mobileNumbertxt.getText().toString().trim();
        //date = "11-09-2023";
       /* if (componentValue != null) {
            if (componentValue.equalsIgnoreCase("Others"))
              //  subComponentValue = "Dummy data";
        }
        if (subComponentValue != null) {
            if (componentValue.equalsIgnoreCase("Others")) {
                stagevalue = "Dummy data";
                stagelastvalue = "Dummy data";
            }
        }*/
        /*if (stagevalue == null) {
            stagevalue = "Dummy data";
            stagelastvalue = "Dummy data";
        }
        if (stagelastvalue == null) {

            stagelastvalue = "Dummy data";
        }*/

        if (subBasinValue == null || districtValue == null || blockValue == null ||
                villageValue == null || componentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (sub_componentSpinner.getVisibility() == View.VISIBLE && subComponentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (stagesSpinner.getVisibility() == View.VISIBLE && stagevalue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (valueofPicCount == 0 || valueofPicCount < 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        } else if (valueofPic == 1) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        } else if (tnauBinding.visibilityLyt.getVisibility() == View.VISIBLE) {
            if (survey_no.length() == 0) {
                tnauBinding.surveyTxt.setError("Please enter survey no");
                return false;
            } else if (area.length() == 0) {
                tnauBinding.areaTxt.setError("Please enter area");
                return false;
            } else if (Double.valueOf(area) > 2.0) {
                tnauBinding.areaTxt.setError("Area Should be less than 2(ha)");
                return false;
            } else if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {

                tnauBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (!ValidationUtils.isValidMobileNumber(mobileNumber)) {
                tnauBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (gender == null && genderSpinner.getVisibility() == View.VISIBLE) {

                Toast.makeText(getActivity(), "Please select Gender", Toast.LENGTH_LONG).show();
                return false;

            } else if (category1 == null && categorySpinner.getVisibility() == View.VISIBLE) {
                Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_LONG).show();
                return false;
            } else if (farmerName.isEmpty() || farmerName.length() == 0) {
                tnauBinding.farmerTxt.setError("Please Enter Farmer Name");
                return false;
            } else if (date.isEmpty() && datePicker.getVisibility() == View.VISIBLE) {
                tnauBinding.dateTxt.setError("Please select the date");
                return false;
            } else if (tnauBinding.yieldTxt.getText().toString().trim().isEmpty() && tnauBinding.yieldTxt.getVisibility() == View.VISIBLE) {
                tnauBinding.yieldTxt.setError("Please enter the yield");
                return false;
            } else if (tnauBinding.varietyTxt.getText().toString().trim().isEmpty() && tnauBinding.varietyTxt.getVisibility() == View.VISIBLE) {
                tnauBinding.varietyTxt.setError("Please enter the variety");
                return false;
            }

        }
        return true;
    }


    // click event for finalSubmission button and others
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
                        category1, survey_no, area, near_tank, remarks, dateField, mobileNumber);

                Log.i(TAG, "componentTxt: " + componentSpinner.getSelectedItem());
                checkTestData();
                if (checkValidaiton) {

                    finalSubmission();

                } else {
                    //do the code for save all data
                    //Toast.makeText(context, "Server error.!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.image_1:
                if (PermissionUtils.checkPermission(context)) {
                    tnauBinding.image1.setSelected(true);
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
                    tnauBinding.image2.setSelected(true);
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
                dateFieldValidation(tnauBinding.dateTxt);
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
                        datePicker.setError(null);
                    }
                }, year, month, day);
        picker.getDatePicker().setMaxDate(maxTime);
        picker.getDatePicker().setMinDate(minTime);
        picker.show();


    }


    // here are all the spinner dropdown except components
    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = tnauBinding.subBasinTxt;
        districtSpinner = tnauBinding.districtTxt;
        blockSpinner = tnauBinding.blockTxt;
        genderSpinner = tnauBinding.genderTxt;
        categorySpinner = tnauBinding.categoryTxt;
        villageSpinner = tnauBinding.villageTxt;
        interventionSpinner = tnauBinding.inverntionTyper;


        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        tnauBinding.phase1.setItem(phraseList);


        //phase drop down spinner
        tnauBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                subBasinValue = null;
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(tnauBinding.phase1.getSelectedItemPosition());
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
                Log.i(TAG, "subBasinPos: " + posValue);
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
//                if(districtDropDown.get(i).getNAME().length() != 0){
//                    districtValue = districtDropDown.get(i).getNAME();
//                }else{
//                    districtValue = null;
//                }

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
                villageValue = null;
                villageDataList = FetchDeptLookup.readVillageData(context, "village.json");
                posValue = String.valueOf(blockDropDown.get(i).getID());
                blockValue = blockDropDown.get(i).getNAME();
                villageAdaapter = new VillageAdaapter(getContext(), villageDataList);
                villageAdaapter.getFilter().filter(posValue);
                villageSpinner.setAdapter(villageAdaapter);
               /* if (mCommonFunction.isNetworkAvailable() == true) {
mCommonFunction.showProgress();
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
                                    villageAdaapter = new VillageAdaapter(getContext(), villageDataList);
                                    villageAdaapter.getFilter().filter(posValue);
                                    villageSpinner.setAdapter(villageAdaapter);
mCommonFunction.hideProgress();
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
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onValue: " + villageDataList.get(i).getNAME());
                villageValue = villageDataList.get(i).getNAME();
                village = String.valueOf(villageDataList.get(i).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //gender dropdown list
        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        tnauBinding.genderTxt.setItem(genderList);

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
        tnauBinding.categoryTxt.setItem(categoryList);

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
        tnauBinding.inverntionTyper.setItem(interventionList);

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

    private void checkTestData() {
        tnauBinding.checkValues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    remarksData = "Test Data";
                } else {
                    remarksData = "";
                    Log.i(TAG, "onCheckedChanged: " + remarks);
                }
            }
        });
    }

    //calender updates
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        tnauBinding.dateTxt.setText(dateFormat.format(myCalendar.getTime()));
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

        if (requestCode == pic_id) {
            if (resultCode == Activity.RESULT_OK) {
                if (takePicture && valueofPic == 1) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    tnauBinding.image1.setImageBitmap(photo);
                    // BitMap is data structure of image file which store the image in memory
                    Log.i(TAG, "base: " + getEncodedString(photo));
                    firstImageBase64 = getEncodedString(photo);
                } else if (!takePicture && valueofPic == 2) {
                    Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    tnauBinding.image2.setImageBitmap(photo2);
                    secondImageBase64 = getEncodedString(photo2);
                    // BitMap is data structure of image file which store the image in memory
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast = Toast.makeText(getContext(), "Canceled, no photo selected.", Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }

    //save the image in base64 format for fetch in backend data
    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();
        String convertedData = Base64.encodeToString(imageArr, Base64.NO_WRAP);
        Log.i("data", convertedData);
        return convertedData;


    }


    // final submission button validation for online and save data for offline data through database..
    private void finalSubmission() {


        //data should saved in post api
        // Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
        getAllData();

    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }


    @Override
    public void onSelectedInputs(LookUpDataClass lookUpDataClass) {
        intervention1 = lookUpDataClass.getIntervention1();
        intervention2 = lookUpDataClass.getIntervention2();
        intervention3 = lookUpDataClass.getIntervention3();
        intervention4 = lookUpDataClass.getIntervention4();

        componentValue = lookUpDataClass.getComponentValue();
        subComponentValue = lookUpDataClass.getSubComponentValue();
        stagevalue = lookUpDataClass.getStageValue();
        stagelastvalue = lookUpDataClass.getStagelastvalue();


//        if (intervention4.equalsIgnoreCase("Harvest") ||
//                intervention4.equalsIgnoreCase("Harvest of Pulse") ||
//                intervention4.equalsIgnoreCase("Harvest of Rice") ||
//                intervention4.equalsIgnoreCase("1st Harvest") ||
//                intervention4.equalsIgnoreCase("Last Harvest") ||
//                subComponentValue.equalsIgnoreCase("Harvest")) {
//            tnauBinding.varietyTxt.setVisibility(View.VISIBLE);
//            tnauBinding.yieldTxt.setVisibility(View.VISIBLE);
//        }

        Log.i(TAG, "getAllComponentValue: " + intervention1 + intervention2 + intervention3);

    }

    private void getAllData() {
        request = new TNAU_Request();
        Log.i(TAG, "letLATLONG: " + lat + lon);
        Log.i(TAG, "base64: " + firstImageBase64.trim());
        Log.i(TAG, "base64: " + secondImageBase64);

        farmerName = tnauBinding.farmerTxt.getText().toString().trim();
        survey_no = tnauBinding.surveyTxt.getText().toString().trim();
        area = tnauBinding.areaTxt.getText().toString().trim();
        area = tnauBinding.areaTxt.getText().toString().trim();
        remarks = tnauBinding.remarksTxt.getText().toString().trim();
        dateField = tnauBinding.dateTxt.getText().toString().trim();
        near_tank = tnauBinding.tankTxt.getText().toString().trim();


        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);


        request.setVillage(village);
        request.setIntervention1(intervention1);
        request.setIntervention2(intervention2);
        request.setIntervention3(intervention3);
        request.setFarmer_name(farmerName);
        request.setGender(gender);
        request.setCategory(category1);
        request.setSurvey_no(survey_no);
        request.setArea(area);
        if (tnauBinding.varietyTxt.getVisibility() == View.VISIBLE) {
            request.setVariety(tnauBinding.varietyTxt.getText().toString().trim());
        } else {
            request.setVariety("null");
        }
        request.setImage1(firstImageBase64.trim());
        if (tnauBinding.yieldTxt.getVisibility() == View.VISIBLE) {
            request.setYield(tnauBinding.yieldTxt.getText().toString().trim());
        } else {
            request.setYield("null");
        }

//        request.setRemarks(remarks);
        request.setCreated_by(SharedPrefsUtils.getString(context, SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));

        request.setCreated_date(dateField);
        request.setLat(lat);
        request.setLon(lon);
        request.setTank_name(near_tank);
        request.setTxn_date(mCommonFunction.getDateTime());
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setTxn_id("20200212120446");
        request.setDate("");

        if(!remarksData.isEmpty()){
            request.setRemarks(remarksData);
        }else{
            request.setRemarks(remarks);
        }

        request.setStatus("1");
        if (mCommonFunction.isNetworkAvailable() == true) {
            mCommonFunction.showProgress();
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineRequest == null) {
                offlineRequest = new ArrayList<>();
                offlineImageRequest = new ArrayList<>();
                offlineRequest.add(request);
                offlineImageRequest.add(secondImageBase64);
                SharedPrefsUtils.saveArrayList(context, offlineRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                SharedPrefsUtils.saveArrayListImage(context, offlineImageRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);

                offlineText = "Data saved successfully in offline data";

            } else if (offlineRequest.size() < 10) {
                offlineRequest.add(request);
                offlineImageRequest.add(secondImageBase64);
                SharedPrefsUtils.saveArrayList(context, offlineRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                SharedPrefsUtils.saveArrayListImage(context, offlineImageRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);

                offlineText = "Data saved successfully in offline data";

            } else {
                offlineText = "You’ve reached the offline Data Limit,Please Sync!";
            }
            showMessageOKCancel(offlineText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA, offlineText);
                    mCommonFunction.navigation(getActivity(), DashboardActivity.class,offlineRequest.size());
                }
            });
        }


    }

    private void onlineDataUpload(TNAU_Request request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<TNAU_Response> userDataCall = call.getTnauResponse(request);
        userDataCall.enqueue(new Callback<TNAU_Response>() {
            @Override
            public void onResponse(Call<TNAU_Response> call, Response<TNAU_Response> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getTnauLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        //    mCommonFunction.navigation(getActivity(),DashboardActivity.class);
                        uploadSecondImage(txt_id);

//                        List<AgriResponse> agriResponses = new ArrayList<>();
//                        agriResponses.addAll(response.body().getResponse());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                    mCommonFunction.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<TNAU_Response> call, Throwable t) {
                mCommonFunction.hideProgress();

            }
        });
    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("1");
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
                        Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();

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
}