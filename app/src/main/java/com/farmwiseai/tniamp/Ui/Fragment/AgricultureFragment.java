package com.farmwiseai.tniamp.Ui.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

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
import com.farmwiseai.tniamp.utils.componentCallApis.AgriCallApi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgricultureFragment extends Fragment implements View.OnClickListener, BackPressListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
    public String intervention1 = null; //component
    public String intervention2 = null; //sub_componenet
    public String intervention3 = null; // stages
    public String intervention4 = null; // stages
    public String farmer_name;
    public String gender;
    public String lat;
    public String lon;
    public String tank_name;
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
    DatePickerDialog picker;
    Agri_Request request;
    ArrayList<Agri_Request> offlineAgriRequest = new ArrayList<>();
    private FragmentAgricultureBinding agricultureBinding;
    private Context context;
    private String farmerName, survey_no, area, near_tank, remarks, dateField, nag, dag, darf, seedra, qop, intName, mobileNumber;
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
    private AgriCallApi agriCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private GPSTracker gpsTracker;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList, interventionList;
    private LinearLayout vis_lyt, trainingLyt, seed_lyt, iNames_lyt;
    private String villageValue, category1, firstImageBase64, secondImageBase64, interventionTypeVal;

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


        nag = null;
        dag = null;
        darf = null;
        seedra = null;
        qop = null;
        agricultureBinding.areaTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        agricultureBinding.popBackImage.setOnClickListener(this);
        agricultureBinding.submissionBtn.setOnClickListener(this);
        agricultureBinding.image1.setOnClickListener(this);
        agricultureBinding.image2.setOnClickListener(this);
        agricultureBinding.dateTxt.setOnClickListener(this);
        agricultureBinding.doaTxt.setOnClickListener(this);
        agricultureBinding.dorfTxt.setOnClickListener(this);
        farmerName = agricultureBinding.farmerTxt.getText().toString();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        dateField = agricultureBinding.dateTxt.getText().toString();
        nag = agricultureBinding.nameOfGroup.getText().toString();
        dag = agricultureBinding.doaTxt.getText().toString();
        darf = agricultureBinding.dorfTxt.getText().toString();
        seedra = agricultureBinding.areaRaisedTxt.getText().toString();
        qop = agricultureBinding.quantityTxt.getText().toString();
        intName = agricultureBinding.inerventionNameTxt.getText().toString();
        mobileNumber = agricultureBinding.mobileNumbertxt.getText().toString();
        componentSpinner = agricultureBinding.componentTxt;
        sub_componentSpinner = agricultureBinding.subComponentsTxt;
        stagesSpinner = agricultureBinding.stagesTxt;
        datePicker = agricultureBinding.dateTxt;
        vis_lyt = agricultureBinding.visibilityLyt;
        seed_lyt = agricultureBinding.seedGroupLyt;
        iNames_lyt = agricultureBinding.othersLayout;
        backPressListener = this;
        offlineAgriRequest = SharedPrefsUtils.getAgriArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);

        agriCallApi = new AgriCallApi(getActivity(), getContext(), componentDropDown, adapter, adapter2, myString, backPressListener);
        agriCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stagesSpinner, datePicker, vis_lyt, trainingLyt, seed_lyt, iNames_lyt);

        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);
        setAllDropDownData();
        return agricultureBinding.getRoot();


    }

    private boolean fieldValidation(String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date,
                                    String nag, String dag, String dafr, String seedra, String qop,
                                    String intName, String mobileNumber) {

        farmerName = agricultureBinding.farmerTxt.getText().toString().trim();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        date = agricultureBinding.dateTxt.getText().toString();
        nag = agricultureBinding.nameOfGroup.getText().toString();
        dag = agricultureBinding.doaTxt.getText().toString();
        dafr = agricultureBinding.dorfTxt.getText().toString();
        seedra = agricultureBinding.areaRaisedTxt.getText().toString();
        qop = agricultureBinding.quantityTxt.getText().toString();
        intName = agricultureBinding.inerventionNameTxt.getText().toString().trim();
        mobileNumber = agricultureBinding.mobileNumbertxt.getText().toString();
        if (componentValue != null) {
            if (componentValue.equalsIgnoreCase("Others"))
                subComponentValue = "Dummy data";
        }

        if (subBasinValue == null || districtValue == null || blockValue == null ||
                villageName == null || componentValue == null
        ) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
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
        } else if (seed_lyt.getVisibility() == View.VISIBLE) {
            if (nag.length() == 0) {
                agricultureBinding.nameOfGroup.setError("field empty");
                return false;
            } else if (dag.length() == 0) {
                {
                    agricultureBinding.doaTxt.setError("field empty");
                    return false;
                }
            } else if (dafr.length() == 0) {
                {
                    agricultureBinding.dorfTxt.setError("field empty");
                    return false;
                }
            } else if (seedra.length() == 0) {
                {
                    agricultureBinding.areaRaisedTxt.setError("field empty");
                    return false;
                }
            } else if (qop.length() == 0) {
                {
                    agricultureBinding.quantityTxt.setError("field empty");
                    return false;
                }
            }

        } else if (vis_lyt.getVisibility() == View.VISIBLE) {
            if (farmerName.length() == 0 || farmerName.isEmpty()) {
                agricultureBinding.farmerTxt.setError("Please enter farmer name");
                return false;
            } else if (survey_no.length() == 0) {
                agricultureBinding.surveyTxt.setError("Please enter survey no");
                return false;
            } else if (area.length() == 0) {
                agricultureBinding.areaTxt.setError("Please enter area");
                return false;
            } else if (Double.valueOf(area) > 2.0) {
                agricultureBinding.areaTxt.setError("Area Should be less than 2(ha)");
                return false;
            } else if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {

                agricultureBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (!ValidationUtils.isValidMobileNumber(mobileNumber)) {
                agricultureBinding.mobileNumbertxt.setError("Please enter the valid mobile number");
                return false;
            } else if (gender == null && genderSpinner.getVisibility() == View.VISIBLE) {

                Toast.makeText(getActivity(), "Please select Gender", Toast.LENGTH_LONG).show();
                return false;

            } else if (category1 == null && categorySpinner.getVisibility() == View.VISIBLE) {
                Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_LONG).show();
                return false;
            } else if (date.isEmpty() && datePicker.getVisibility() == View.VISIBLE) {
                agricultureBinding.dateTxt.setError("Please select the date");
                return false;
            } else if (agricultureBinding.yieldTxt.getText().toString().isEmpty() && agricultureBinding.yieldTxt.getVisibility() == View.VISIBLE) {
                agricultureBinding.yieldTxt.setError("Please enter the yield");
                return false;
            } else if (agricultureBinding.varietyTxt.getText().toString().isEmpty() && agricultureBinding.varietyTxt.getVisibility() == View.VISIBLE) {
                agricultureBinding.varietyTxt.setError("Please enter the variety");
                return false;
            }

        } else if (iNames_lyt.getVisibility() == View.VISIBLE) {
            if (agricultureBinding.inerventionNameTxt.getText().toString().trim().isEmpty() || agricultureBinding.inerventionNameTxt.getText().toString().trim().length() == 0) {
                agricultureBinding.inerventionNameTxt.setError("field empty");
                return false;
            }

            return true;
        }
        return true;
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
                        category1, survey_no, area, near_tank, remarks, dateField, nag, dag, darf, seedra, qop, intName, mobileNumber);
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
                } else {
                    //  showToast(getActivity(), "Validation error");
                }
                break;

            case R.id.image_1:
                if (PermissionUtils.checkPermission(context)) {
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
                dateFieldValidation(agricultureBinding.dateTxt);
                break;
            case R.id.doa_txt:
                dateFieldValidation(agricultureBinding.doaTxt);
                break;
            case R.id.dorf_txt:
                dateFieldValidation(agricultureBinding.dorfTxt);
                break;

        }
    }

    private void finalSubmission() {
        getAllData();
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
        interventionSpinner = agricultureBinding.inverntionTyper;


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
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                Log.i(TAG, "onResponse: " + agricultureBinding.phase1.getSelectedItemPosition());
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(agricultureBinding.phase1.getSelectedItemPosition());
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
                districtDropDown = FetchDeptLookup.readDistrictData(context, "district.json");
                posValue = String.valueOf(sub_basin_DropDown.get(i).getID());
                subBasinValue = sub_basin_DropDown.get(i).getNAME();
                districtAdapter = new DistrictAdapter(getContext(), districtDropDown);
                Log.i(TAG, "districtPos: " + myString);
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
        agricultureBinding.inverntionTyper.setItem(interventionList);
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

    public void showToast(Activity mcontaxt, String message) {
        Toast.makeText(mcontaxt, message, Toast.LENGTH_SHORT).show();
    }

    private void getAllData() {
        request = new Agri_Request();
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
        Log.i(TAG, "dataValue" + dateField);


        request.setVillage(villageValue);
        request.setIntervention1(intervention1);
        request.setIntervention2(intervention2);
        request.setIntervention3(intervention3);
        request.setFarmerName(farmerName);
        request.setGender(gender);
        request.setCategory(category1);
        request.setSurveyNo(survey_no);
        request.setArea(area);
        request.setVariety("");
        request.setImage1(firstImageBase64.trim());
        request.setYield("");
        request.setRemarks(remarks);
        request.setCreatedBy("f55356773fce5b11");
        request.setCreatedDate(dateField);
        request.setLat(lat);
        request.setLon(lon);
        request.setTankName(near_tank);
        request.setTxnDate("Wed Feb 12 2020 12:04:46 GMT+0530 (India Standard Time)");
        request.setPhotoLat(lat);
        request.setPhotoLon(lon);
        request.setTxnId("20200212120446");
        request.setDate("");
        request.setStatus("0");
        request.setInterventionType(interventionTypeVal);
        request.setOtherIntervention(intName);
        request.setGroupName(nag);
        request.setDateCountOpen(dag);
        request.setDateRevolvingFundRelease(darf);
        request.setSeedAreaDecimal(seedra);
        request.setQuantityProcured(qop);

        if (mCommonFunction.isNetworkAvailable() == true) {
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineAgriRequest == null) {
                offlineAgriRequest = new ArrayList<>();
                offlineAgriRequest.add(request);
                SharedPrefsUtils.getAgriArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                offlineText = "Data saved successfully in offline data";

            } else if (offlineAgriRequest.size() < 5) {
                offlineAgriRequest.add(request);
                SharedPrefsUtils.saveAgriArrayList(context, offlineAgriRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
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

    private void onlineDataUpload(Agri_Request request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AgriResponse> userDataCall = null;
        userDataCall = call.getAgriResponse(request);
        userDataCall.enqueue(new Callback<AgriResponse>() {
            @Override
            public void onResponse(Call<AgriResponse> call, Response<AgriResponse> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getAgriLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        uploadSecondImage(txt_id);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "data error.!", Toast.LENGTH_SHORT).show();
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
        intervention4 = lookUpDataClass.getIntervention4();
        componentValue = lookUpDataClass.getComponentValue();
        subComponentValue = lookUpDataClass.getSubComponentValue();

        stageValue = lookUpDataClass.getStageValue();
        stageLastValue = lookUpDataClass.getStagelastvalue();
        if (componentValue.equalsIgnoreCase("Farmers Field School")
                || componentValue.equalsIgnoreCase(" IPM village-Vermicompost") ||
                componentValue.equalsIgnoreCase("Cono Weeding") ||
                componentValue.equalsIgnoreCase("Seed Village Group")) {

            agricultureBinding.varietyTxt.setVisibility(View.GONE);
            agricultureBinding.yieldTxt.setVisibility(View.GONE);
        } else if (componentValue.equalsIgnoreCase("Maize")) {
            agricultureBinding.varietyTxt.setVisibility(View.VISIBLE);
            agricultureBinding.yieldTxt.setVisibility(View.VISIBLE);
        } else if (subComponentValue.equalsIgnoreCase("Green Manure")) {
            agricultureBinding.varietyTxt.setVisibility(View.GONE);
            agricultureBinding.yieldTxt.setVisibility(View.GONE);
        } else if (intervention4.equalsIgnoreCase("Harvest") || subComponentValue.equalsIgnoreCase("Harvest")) {
            agricultureBinding.varietyTxt.setVisibility(View.VISIBLE);
            agricultureBinding.yieldTxt.setVisibility(View.VISIBLE);
        } else {
            agricultureBinding.varietyTxt.setVisibility(View.GONE);
            agricultureBinding.yieldTxt.setVisibility(View.GONE);
        }
        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
    }
}