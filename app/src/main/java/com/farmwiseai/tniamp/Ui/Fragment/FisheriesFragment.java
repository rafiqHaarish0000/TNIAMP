package com.farmwiseai.tniamp.Ui.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
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

import com.anurag.multiselectionspinner.MultiSpinner;
import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.FishRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.FishResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentFisheriesBinding;
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
import com.farmwiseai.tniamp.utils.componentCallApis.FishCallApi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FisheriesFragment extends Fragment implements View.OnClickListener, BackPressListener {
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
    public EditText wauText, memberTxt;
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageName = null;
    public String componentValue = null;
    public String subComponentValue = null;
    public String stageValue = null;
    public String stageLastValue = null;
    public String lessNameVal;
    public String benNameVal;
    public String benNameVal1;
    public String benNameValfinal;
    public String genderNameVal;
    public String catNameVal;
    public String speciesNameVal;
    FragmentFisheriesBinding fisheriesBinding;
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"Catla", "Rohu", "Mrigal", "Common carp", "Grass carp", "GIF Tilapia"};
    ArrayList<FishRequest> offlineMarkRequest = new ArrayList<>();
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, lengthValue, lsPointValue, sliceNumberValue, near_tank, remarks, dateField;
    private List<ComponentData> componentDropDown;
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private List<VillageData> villageDataList;
    private List<String> phraseList, interventionList, multiAdapterList, genderList, categoryList, lesseeList, speciesList, beneList,beneSecondlist;
    private VillageAdaapter villageAdaapter;
    private CharSequence myString = "0";
    private CharSequence posValue = "0";
    private ComponentAdapter adapter, adapter2;
    private SubBasinAdapter subAdapter;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private Spinner subBasinSpinner, districtSpinner,
            blockSpinner, componentSpinner,
            sub_componentSpinner, stageSpinner, beneficarySpinner, beneficaryFinal, beneficarySpinner1, specicesSpinner1,
            categorySpinner, villageSpinner, interventionSpinner, specicesSpinner2, lesseeSpinner, genderSpinner;
    private MultiSpinner multiSpinner1, multiSpinner2, multiSpinner3;
    private EditText datePicker;
    private FishCallApi fishCallApi;
    private boolean takePicture;
    private int valueofPic = 0;
    private int valueofPicCount = 0;
    private CommonFunction mCommonFunction;
    private LinearLayout layout1, layout2, layout3, layout4, layout5, otherLyt, linFishTankInfo;
    private String villageValue, firstImageBase64, secondImageBase64, interventionTypeVal,
            genderVal, catVal, lesseVal, benVal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lessNameVal = null;
        benNameVal = null;
        benNameVal1 = null;
        benNameValfinal = null;
        genderNameVal = null;
        catNameVal = null;
        speciesNameVal = null;

        mCommonFunction = new CommonFunction(getActivity());
        // Inflate the layout for this fragment
        fisheriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fisheries, container, false);
        fisheriesBinding.popBackImage.setOnClickListener(this);
        fisheriesBinding.submissionBtn.setOnClickListener(this);
        fisheriesBinding.image1.setOnClickListener(this);
        fisheriesBinding.image2.setOnClickListener(this);
        remarks = fisheriesBinding.remarksTxt.getText().toString().trim();
        componentSpinner = fisheriesBinding.componentTxt;
        sub_componentSpinner = fisheriesBinding.subComponentsTxt;
        stageSpinner = fisheriesBinding.stagesTxt;
        beneficaryFinal = fisheriesBinding.beneFinal;
        otherLyt = fisheriesBinding.othersLayout;
        linFishTankInfo = fisheriesBinding.linFishTankInfo;
        layout1 = fisheriesBinding.layout1;
        layout2 = fisheriesBinding.layout2;
        layout3 = fisheriesBinding.layout3;
        layout4 = fisheriesBinding.layout4;
        layout5 = fisheriesBinding.layout5;

        backPressListener = this;
        fishCallApi = new FishCallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        fishCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stageSpinner, layout1, layout2, layout3, layout4, layout5, otherLyt, beneficaryFinal, linFishTankInfo);

        offlineMarkRequest = SharedPrefsUtils.getFishArrayList(context, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);

        LatLongPojo latLongPojo = new LatLongPojo();
        latLongPojo = PermissionUtils.getLocation(getContext());
        lat = latLongPojo.getLat();
        lon = latLongPojo.getLon();
        Log.i("data", lat + "," + lon);


        setAllDropDownData();
        return fisheriesBinding.getRoot();
    }

    private boolean fieldValidation(String category,
                                    String remarks, String date) {


        remarks = fisheriesBinding.remarksTxt.getText().toString().trim();

        Log.i(TAG, "modelVillage: " + componentValue);

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
        }

        else if (valueofPicCount == 0 || valueofPicCount < 2) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
            return false;
        } else if (layout1.getVisibility() == View.VISIBLE) {
            if (lessNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (fisheriesBinding.speciesStockedTxt.getText().toString().trim().length() == 0) {
                fisheriesBinding.speciesStockedTxt.setError("Do not empty field");
                return false;
            }

        } else if (layout2.getVisibility() == View.VISIBLE) {
            if (benNameVal == null || speciesNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (fisheriesBinding.feedQuality.getText().toString().trim().isEmpty()) {
                fisheriesBinding.feedQuality.setError("Do not empty field");
                return false;
            }

        } else if (layout3.getVisibility() == View.VISIBLE) {
            if (benNameVal1 == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (fisheriesBinding.speciesStockedTxt1.getText().toString().trim().length() == 0) {
                fisheriesBinding.speciesStockedTxt1.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.feedQuality1.getText().toString().trim().isEmpty()) {
                fisheriesBinding.feedQuality1.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.num.getText().toString().trim().isEmpty()) {
                fisheriesBinding.num.setError("Do not empty field");
                return false;
            }

        } else if (layout4.getVisibility() == View.VISIBLE) {
            if (genderNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (catNameVal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            } else if (fisheriesBinding.farmPond.getText().length() == 0) {
                fisheriesBinding.farmPond.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.beneName.getText().length() == 0) {
                fisheriesBinding.beneName.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.surveyVal.getText().length() == 0) {
                fisheriesBinding.surveyVal.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.mobileVal.getText().length() == 0 ||
                    fisheriesBinding.mobileVal.length() < 10) {
                fisheriesBinding.mobileVal.setError("Please enter the valid mobile number");
                return false;
            } else if (!ValidationUtils.isValidMobileNumber(fisheriesBinding.mobileVal.getText().toString().trim())) {
                fisheriesBinding.mobileVal.setError("Please enter the valid mobile number");
                return false;
            } else if (fisheriesBinding.feedQuality2.getText().length() == 0) {
                fisheriesBinding.feedQuality2.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.numbOfSeeds.getText().length() == 0) {
                fisheriesBinding.numbOfSeeds.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.numbOfSeedsHarvest.getText().length() == 0) {
                fisheriesBinding.numbOfSeedsHarvest.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.quantityOfFishHar.getText().length() == 0) {
                fisheriesBinding.quantityOfFishHar.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.speciesStockedTxt2.getText().length() == 0) {
                fisheriesBinding.speciesStockedTxt2.setError("Do not empty field");
                return false;
            }

        }
        //layout 5
        else if (fisheriesBinding.layout5.getVisibility() == View.VISIBLE) {

            if (fisheriesBinding.volumeOfWater.getText().length() == 0) {
                fisheriesBinding.volumeOfWater.setError("Do not empty field");
                return false;
            } /*else if (fisheriesBinding.removeSeeds.getText().toString().trim().isEmpty()) {
                fisheriesBinding.removeSeeds.setError("Do not empty field");
                return false;
            }*/ else if (fisheriesBinding.farmPond1.getText().toString().trim().isEmpty()) {
                fisheriesBinding.farmPond1.setError("Do not empty field");
                return false;
            }else if (otherLyt.getVisibility() == View.VISIBLE) {
                if (fisheriesBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                    fisheriesBinding.inerventionNameTxt.setError("field empty");
                    return false;
                }
            }

        } else if (fisheriesBinding.linFishTankInfo.getVisibility() == View.VISIBLE) {
            if (fisheriesBinding.nodalTXt.getText().toString().trim().length() == 0) {
                fisheriesBinding.nodalTXt.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.nameofTankTXT.getText().toString().trim().length() == 0) {
                fisheriesBinding.nameofTankTXT.setError("Do not empty field");
                return false;
            } else if (fisheriesBinding.waterTxt.getText().toString().trim().isEmpty()) {
                fisheriesBinding.waterTxt.setError("Do not empty filed");
                return false;
            } else if (fisheriesBinding.seedStockTXt.getText().toString().trim().length() == 0) {
                fisheriesBinding.seedStockTXt.setError("Do not empty field");
                return false;
            }else if (otherLyt.getVisibility() == View.VISIBLE) {
                if (fisheriesBinding.inerventionNameTxt.getText().toString().trim().isEmpty()) {
                    fisheriesBinding.inerventionNameTxt.setError("field empty");
                    return false;
                }
            }

        }
        //beneficary value
        else if (beneficaryFinal.getVisibility() == View.VISIBLE) {
            if (benNameValfinal == null) {
                mCommonFunction.mLoadCustomToast(getActivity(), "Please Enter All Mandatory Fiellds.!");
                return false;
            }

        }

        //other layout

        return true;
    }

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
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //do the code for save all data
                    //    Toast.makeText(context, "Validation Error!", Toast.LENGTH_SHORT).show();
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

    private void setAllDropDownData() {

        //binding all the spinner field without component dropdowns
        subBasinSpinner = fisheriesBinding.subBasinTxt;
        districtSpinner = fisheriesBinding.districtTxt;
        blockSpinner = fisheriesBinding.blockTxt;
        componentSpinner = fisheriesBinding.componentTxt;
        sub_componentSpinner = fisheriesBinding.subComponentsTxt;
        villageSpinner = fisheriesBinding.villageTxt;
        interventionSpinner = fisheriesBinding.inverntionTyper;
        lesseeSpinner = fisheriesBinding.lessee;
        beneficarySpinner = fisheriesBinding.bene1;
        beneficarySpinner1 = fisheriesBinding.bene2;
        categorySpinner = fisheriesBinding.categorySpinner;
        genderSpinner = fisheriesBinding.genderTxt;

        categoryList = new ArrayList<>();
        categoryList.add("SC");
        categoryList.add("ST");
        categoryList.add("Others");

        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");

        lesseeList = new ArrayList<>();
        lesseeList.add("FCS");
        lesseeList.add("Private");
        lesseeList.add("Not leased");

        speciesList = new ArrayList<>();
        speciesList.add("GIFT");
        speciesList.add("Pangassius");
        speciesList.add("Others");

        beneList = new ArrayList<>();
        beneList.add("FCS");
        beneList.add("SHG");
        beneList.add("Others");

        beneSecondlist = new ArrayList<>();
        beneSecondlist.add("FCS");
        beneSecondlist.add("WUA");
        beneSecondlist.add("Others");

        //phase data
        phraseList = new ArrayList<>();
        phraseList.add("Choose phase");
        phraseList.add("Phase 1");
        phraseList.add("Phase 2");
        phraseList.add("Phase 3");
        phraseList.add("Phase 4");
        fisheriesBinding.phase1.setItem(phraseList);


//phase drop down spinner
        fisheriesBinding.phase1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                subBasinValue = null;
                Log.i(TAG, "onPhraseSelected: " + phraseList.get(position));
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(context, "sub_basin.json");
                subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                myString = String.valueOf(fisheriesBinding.phase1.getSelectedItemPosition());
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
        fisheriesBinding.inverntionTyper.setItem(interventionList);
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

        fisheriesBinding.speciesStockedTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMessageForSpicies(getContext(), "Species Stocked", selectedLanguage, langArray, langList);
            }
        });
        fisheriesBinding.speciesStockedTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMessageForSpicies(getContext(), "Species Stocked", selectedLanguage, langArray, langList);
            }
        });
        fisheriesBinding.speciesStockedTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMessageForSpicies(getContext(), "Species Stocked", selectedLanguage, langArray, langList);
            }
        });

        fisheriesBinding.lessee.setItem(lesseeList);
        lesseeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lesseVal = String.valueOf(lesseeSpinner.getSelectedItemPosition());
                lessNameVal = lesseeSpinner.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fisheriesBinding.bene1.setItem(beneSecondlist);
        beneficarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                benVal = String.valueOf(beneficarySpinner.getSelectedItemPosition());
                benNameVal = beneficarySpinner.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fisheriesBinding.bene2.setItem(beneSecondlist);
        beneficarySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                benVal = String.valueOf(beneficarySpinner1.getSelectedItemPosition());
                benNameVal1 = beneficarySpinner1.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fisheriesBinding.beneFinal.setItem(beneList);
        beneficaryFinal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                benVal = String.valueOf(beneficaryFinal.getSelectedItemPosition());
                benNameValfinal = beneficaryFinal.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fisheriesBinding.speciesSpinner1.setItem(speciesList);
        fisheriesBinding.speciesSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                benVal = String.valueOf(beneficaryFinal.getSelectedItemPosition());
                speciesNameVal = fisheriesBinding.speciesSpinner1.getSelectedItem().toString();
//                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fisheriesBinding.categorySpinner.setItem(categoryList);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catVal = String.valueOf(categorySpinner.getSelectedItemPosition());
                catNameVal = categorySpinner.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fisheriesBinding.genderTxt.setItem(genderList);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderVal = String.valueOf(genderSpinner.getSelectedItemPosition());
                genderNameVal = genderSpinner.getSelectedItem().toString();
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void alertMessageForSpicies(Context context, String title, boolean[] selectedLanguage, String[] items,
                                        ArrayList<Integer> values) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set title
        builder.setTitle(title);

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setMultiChoiceItems(items, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    values.add(i);
                    // Sort array list
                    Collections.sort(values);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    values.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < values.size(); j++) {
                    // concat array value
                    stringBuilder.append(items[values.get(j)]);
                    // check condition
                    if (j != values.size() - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ");
                    }
                }
                // set text on textView
                fisheriesBinding.speciesStockedTxt.setText(stringBuilder.toString());
                fisheriesBinding.speciesStockedTxt1.setText(stringBuilder.toString());
                fisheriesBinding.speciesStockedTxt2.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        // show dialog
        builder.show();
    }


    private boolean checkPermission() {
        // Permission is not granted
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
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
                    fisheriesBinding.image1.setImageBitmap(photo);
                    // BitMap is data structure of image file which store the image in memory
                    firstImageBase64 = getEncodedString(photo);
                } else if (!takePicture && valueofPic == 2) {
                    Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    fisheriesBinding.image2.setImageBitmap(photo2);
                    // BitMap is data structure of image file which store the image in memory
                    secondImageBase64 = getEncodedString(photo2);
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

    private void getAllData() {

        remarks = fisheriesBinding.remarksTxt.getText().toString().trim();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);

        FishRequest request = new FishRequest();
        request.setBeneficiary("");
        request.setBeneficiary_name(benVal);
        request.setCreated_by("f55356773fce5b11");
        request.setCreated_date("2020-02-12 11:02:02");
        request.setFeed_qty(fisheriesBinding.speciesStockedTxt.getText().toString().trim());
        request.setHarvested("");
        request.setImage1(firstImageBase64.trim());
        request.setIntervention1(intervention1);
        request.setIntervention2(intervention2);
        request.setIntervention3(intervention3);
        request.setLessee(lesseVal);
        request.setLat(lat);
        request.setLon(lon);
        request.setNo_of_stocks_req("");
        request.setNodal_officer(fisheriesBinding.nodalTXt.toString());
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setPond_constructed_by("");
        request.setQty_fish_harvested("");
        request.setRemarks(remarks);
        request.setSeed_no("");
        request.setSpecies_stoked("");
        request.setTank_name(fisheriesBinding.nameofTankTXT.getText().toString().trim());
        request.setTxn_date("WedFeb12202012:04:46GMT+0530(IndiaStandardTime)");
        request.setTxn_id("20200212120446");
        request.setVillage(villageValue);
        request.setWater_spread_area("");
        request.setCategory("");
        request.setGender(genderVal);
        request.setMobile("");
        request.setSurvey_no("");
        request.setStatus("0");


        if (mCommonFunction.isNetworkAvailable()) {
            onlineDataUpload(request);
        } else {
            String offlineText = "";
            if (offlineMarkRequest == null) {
                offlineMarkRequest = new ArrayList<>();
                offlineMarkRequest.add(request);
                SharedPrefsUtils.saveFishArrayList(context, offlineMarkRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                offlineText = "Data saved successfully in offline data";

            } else if (offlineMarkRequest.size() < 5) {
                offlineMarkRequest.add(request);
                SharedPrefsUtils.saveFishArrayList(context, offlineMarkRequest, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
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

    private void onlineDataUpload(FishRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<FishResponse> userDataCall = null;
        userDataCall = call.getFishRespone(request);
        userDataCall.enqueue(new Callback<FishResponse>() {
            @Override
            public void onResponse(Call<FishResponse> call, Response<FishResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getFisherylanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id);
                    uploadSecondImage(txt_id);

                } else {
                    Toast.makeText(getContext(), "data getting error.!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<FishResponse> call, Throwable t) {

            }
        });

    }

    private void uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("8");
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
        subComponentValue = lookUpDataClass.getSubComponentValue();
        stageValue = lookUpDataClass.getStageValue();
        stageLastValue = lookUpDataClass.getStagelastvalue();
//        Log.i(TAG, "modelVillage: "+componentValue);
        Log.i(TAG, "getComponentData: " + intervention1 + intervention2 + intervention3);
    }
}