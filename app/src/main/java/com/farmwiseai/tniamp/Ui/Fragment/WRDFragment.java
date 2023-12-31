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
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.WRDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.WRDResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentWRDFRagmentBinding;
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.CustomToast;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.LatLongPojo;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.PermissionUtils;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;
import com.farmwiseai.tniamp.utils.componentCallApis.WRDCallAPi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WRDFragment extends Fragment implements View.OnClickListener, BackPressListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    final Calendar myCalendar = Calendar.getInstance();
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3; // stages
    public String intervention4; // stages
    public BackPressListener backPressListener;
    public String lat;
    public String lon;
    private String remarksData;
    public EditText wauText, memberTxt;
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageName = null;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stageValue = null;
    public String stageLastValue = null;
    DatePickerDialog picker;
    ArrayList<WRDRequest> offlineWRDRequest = new ArrayList<>();
    ArrayList<String> offlineWRDImageRequest = new ArrayList<>();

    private FragmentWRDFRagmentBinding wrdfragmentBinding;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, lengthValue, lsPointValue, sliceNumberValue, near_tank, remarks, dateField;
    private List<ComponentData> componentDropDown;
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private List<VillageData> villageDataList;
    private List<String> interventionList;
    private VillageAdaapter villageAdaapter;
    private CharSequence myString = "0";
    private CharSequence posValue = "0";
    private ComponentAdapter adapter, adapter2;
    private SubBasinAdapter subAdapter;
    private GPSTracker gpsTracker;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private Spinner subBasinSpinner, districtSpinner,
            blockSpinner, componentSpinner,
            sub_componentSpinner, tankStageSpinner, stageSpinner, villageSpinner, interventionSpinner;
    private EditText datePicker;
    private WRDCallAPi wrdCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList;
    private LinearLayout vis_lyt, iNames_lyt, linTankInfo;
    private String villageValue, firstImageBase64, secondImageBase64, interventionTypeVal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCommonFunction = new CommonFunction(getActivity());

        wrdfragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_w_r_d_f_ragment, container, false);

        wrdfragmentBinding.lengthTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        wrdfragmentBinding.lsPoint.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //  wrdfragmentBinding.sliceNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        wrdfragmentBinding.popBackImage.setOnClickListener(this);
        wrdfragmentBinding.submissionBtn.setOnClickListener(this);
        wrdfragmentBinding.image1.setOnClickListener(this);
        wrdfragmentBinding.image2.setOnClickListener(this);


        lengthValue = wrdfragmentBinding.lengthTxt.getText().toString().trim();
        lsPointValue = wrdfragmentBinding.lsPoint.getText().toString().trim();
        sliceNumberValue = wrdfragmentBinding.sliceNumber.getText().toString().trim();
        remarks = wrdfragmentBinding.remarksTxt.getText().toString().trim();


        componentSpinner = wrdfragmentBinding.componentTxt;
        sub_componentSpinner = wrdfragmentBinding.subComponentsTxt;
        tankStageSpinner = wrdfragmentBinding.taskStages;
        stageSpinner = wrdfragmentBinding.stagesTxt;
        iNames_lyt = wrdfragmentBinding.othersLayout;
        linTankInfo = wrdfragmentBinding.linTankDetails;
        wauText = wrdfragmentBinding.nameOfWAU;
        memberTxt = wrdfragmentBinding.noOfMembers;

        backPressListener = this;

        wrdCallApi = new WRDCallAPi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        wrdCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, tankStageSpinner, stageSpinner, wauText, iNames_lyt, memberTxt, linTankInfo);

        offlineWRDRequest = SharedPrefsUtils.getWrdArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
        offlineWRDImageRequest = SharedPrefsUtils.getArrayListwrdImage(context, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);
        SharedPrefsUtils.putInt(context, SharedPrefsUtils.PREF_KEY.BACK_PRESSED, 1);

        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);
        setAllDropDownData();


        return wrdfragmentBinding.getRoot();
    }

    private boolean fieldValidation(String lengthNumberTxt, String category,
                                    String lsPointTxt, String sliceNumberTxt, String date) {

        lengthNumberTxt = wrdfragmentBinding.lengthTxt.getText().toString().trim();
        lsPointTxt = wrdfragmentBinding.lsPoint.getText().toString().trim();
        sliceNumberTxt = wrdfragmentBinding.sliceNumber.getText().toString().trim();

        String noOfMembers = wrdfragmentBinding.noOfMembers.getText().toString().trim();
        String nameOfWAU = wrdfragmentBinding.nameOfWAU.getText().toString().trim();

      /*  if (componentValue != null) {
            if (componentValue.equalsIgnoreCase("Others"))
                subComponentValue = "Dummy data";
        }
*/


        if (subBasinValue == null || districtValue == null || blockValue == null ||
                villageName == null || componentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (sub_componentSpinner.getVisibility() == View.VISIBLE && subComponentValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (wrdfragmentBinding.taskStages.getVisibility() == View.VISIBLE && stageValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (wrdfragmentBinding.stagesTxt.getVisibility() == View.VISIBLE && stageLastValue == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fields.!");
            return false;
        } else if (valueofPicCount == 0 || valueofPicCount < 2) {
            mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        } else if (wrdfragmentBinding.linTankDetails.getVisibility() == View.VISIBLE) {
            if (lengthNumberTxt.length() == 0) {
                wrdfragmentBinding.lengthTxt.setError("Please enter length number");
                return false;
            } else if (lsPointTxt.length() == 0) {
                wrdfragmentBinding.lsPoint.setError("Please enter LS point number");
                return false;
            } else if (sliceNumberTxt.length() == 0) {
                wrdfragmentBinding.sliceNumber.setError("Please enter sluice number");
                return false;
            } else if (wrdfragmentBinding.noOfMembers.getVisibility() == View.VISIBLE && noOfMembers.length() == 0) {
                wrdfragmentBinding.noOfMembers.setError("Do not empty field");
                return false;

            } else if (wrdfragmentBinding.nameOfWAU.getVisibility() == View.VISIBLE && nameOfWAU.length() == 0) {
                wrdfragmentBinding.nameOfWAU.setError("Do not empty field");
                return false;

            } else if (iNames_lyt.getVisibility() == View.VISIBLE) {
                if (wrdfragmentBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                    wrdfragmentBinding.inerventionNameTxt.setError("field empty");
                    return false;
                }
            }
        } else if (iNames_lyt.getVisibility() == View.VISIBLE) {
            if (wrdfragmentBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                wrdfragmentBinding.inerventionNameTxt.setError("field empty");
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
                boolean checkValidaiton = fieldValidation(lengthValue, lsPointValue, sliceNumberValue, near_tank, dateField);
                checkTestData();
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //do the code for save all data
                    // Toast.makeText(context, "Server error.!", Toast.LENGTH_SHORT).show();
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
                    requestPermission();
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
                    requestPermission();
                }
                break;

        }
    }

    private void checkTestData() {
        wrdfragmentBinding.checkValues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = wrdfragmentBinding.subBasinTxt;
        districtSpinner = wrdfragmentBinding.districtTxt;
        blockSpinner = wrdfragmentBinding.blockTxt;
        componentSpinner = wrdfragmentBinding.componentTxt;
        sub_componentSpinner = wrdfragmentBinding.subComponentsTxt;
        tankStageSpinner = wrdfragmentBinding.stagesTxt;
        villageSpinner = wrdfragmentBinding.villageTxt;
        interventionSpinner = wrdfragmentBinding.inverntionTyper;


        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        wrdfragmentBinding.phase1.setItem(phraseList);


//phase drop down spinner
        wrdfragmentBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                subBasinValue = null;
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(wrdfragmentBinding.phase1.getSelectedItemPosition());
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
        wrdfragmentBinding.inverntionTyper.setItem(interventionList);
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
                    wrdfragmentBinding.image1.setImageBitmap(photo);
                    // BitMap is data structure of image file which store the image in memory
                    Log.i(TAG, "base: " + getEncodedString(photo));
                    firstImageBase64 = getEncodedString(photo);
                } else if (!takePicture && valueofPic == 2) {
                    Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    wrdfragmentBinding.image2.setImageBitmap(photo2);
                    secondImageBase64 = getEncodedString(photo2);
                    // BitMap is data structure of image file which store the image in memory
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast = Toast.makeText(getContext(), "Canceled, no photo selected.", Toast.LENGTH_LONG);
                toast.show();

            }
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

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }

    private void getAllData() {

        remarks = wrdfragmentBinding.remarksTxt.getText().toString().trim();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);
        WRDRequest request = new WRDRequest();
        request.setCreated_by(SharedPrefsUtils.getString(context, SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
        request.setCreated_date(dateField);
        request.setImage1(firstImageBase64.trim());
        request.setIntervention1(intervention1);
        if (wrdfragmentBinding.subComponentsTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention2(intervention2);
        } else {
            request.setIntervention2("0");
        }
        if (wrdfragmentBinding.taskStages.getVisibility() == View.VISIBLE) {
            request.setIntervention3(intervention3);
        } else {
            request.setIntervention3("0");
        }
        if (wrdfragmentBinding.stagesTxt.getVisibility() == View.VISIBLE) {
            request.setIntervention4(intervention4);
        } else {
            request.setIntervention4("0");
        }
        request.setLat(lat);
        if (wrdfragmentBinding.linTankDetails.getVisibility() == View.VISIBLE) {
            request.setLength(wrdfragmentBinding.lengthTxt.getText().toString().trim());
            request.setLs_point(wrdfragmentBinding.lsPoint.getText().toString().trim());
            request.setSluice(wrdfragmentBinding.sliceNumber.getText().toString().trim());
        } else {
            request.setLength("0");
            request.setLs_point("0");
            request.setSluice("0");
        }

        request.setLon(lon);

//        request.setRemarks(remarks);

        request.setTxn_date(mCommonFunction.getDateTime());
        request.setTxn_id("20200212120446");
        request.setVillage(villageValue);

        if (wrdfragmentBinding.nameOfWAU.getVisibility() == View.VISIBLE) {
            request.setWua_name(wrdfragmentBinding.nameOfWAU.getText().toString().trim());
        } else {
            request.setWua_name("");
        }

        if (wrdfragmentBinding.noOfMembers.getVisibility() == View.VISIBLE) {
            request.setNof_mem(wrdfragmentBinding.noOfMembers.getText().toString().trim());
        } else {
            request.setNof_mem("");
        }

        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setStatus("1");

        if(!remarksData.isEmpty()){
            request.setRemarks(remarksData);
        }else{
            request.setRemarks(remarks);
        }
        
        request.setIntervention_type("3");

        if (wrdfragmentBinding.othersLayout.getVisibility() == View.VISIBLE) {
            request.setOther_intervention(wrdfragmentBinding.inerventionNameTxt.getText().toString().trim());
        } else {
            request.setOther_intervention("null");
        }


        if (mCommonFunction.isNetworkAvailable() == true) {
            mCommonFunction.showProgress();
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineWRDRequest == null) {
                offlineWRDRequest = new ArrayList<>();
                offlineWRDRequest.add(request);
                offlineWRDImageRequest = new ArrayList<>();
                offlineWRDImageRequest.add(secondImageBase64);
                SharedPrefsUtils.saveArrayListWrdImage(context, offlineWRDImageRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);
                SharedPrefsUtils.saveWRDArrayList(context, offlineWRDRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
                offlineText = "Data saved successfully in offline data";

            } else if (offlineWRDRequest.size() < 10) {
                offlineWRDRequest.add(request);
                offlineWRDImageRequest.add(secondImageBase64);
                SharedPrefsUtils.saveArrayListWrdImage(context, offlineWRDImageRequest, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);
                SharedPrefsUtils.saveWRDArrayList(context, offlineWRDRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
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

    private void onlineDataUpload(WRDRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<WRDResponse> userDataCall = null;
        userDataCall = call.getWRDResponse(request);
        userDataCall.enqueue(new Callback<WRDResponse>() {
            @Override
            public void onResponse(Call<WRDResponse> call, Response<WRDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getWrdLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImage(txt_id);
                } else {
                    Toast.makeText(getContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                    mCommonFunction.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<WRDResponse> call, Throwable t) {
                mCommonFunction.hideProgress();

            }
        });

    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("6");
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
        intervention4 = lookUpDataClass.getIntervention4();
        componentValue = lookUpDataClass.getComponentValue();
        subComponentValue = lookUpDataClass.getSubComponentValue();
        stageValue = lookUpDataClass.getStageValue();
        stageLastValue = lookUpDataClass.getStagelastvalue();
        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
    }
}