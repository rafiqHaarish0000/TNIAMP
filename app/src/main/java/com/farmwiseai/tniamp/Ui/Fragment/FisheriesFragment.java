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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
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
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FisheriesFragment extends Fragment implements View.OnClickListener, BackPressListener {
    FragmentFisheriesBinding fisheriesBinding;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, lengthValue, lsPointValue, sliceNumberValue, near_tank, remarks, dateField;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int pic_id = 123;
    private List<ComponentData> componentDropDown;
    public String intervention1; //component
    public String intervention2; //sub_componenet
    public String intervention3; // stages
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private List<VillageData> villageDataList;
    private List<String> phraseList, interventionList, multiAdapterList, genderList, categoryList, lesseeList, speciesList, beneList;
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
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic;
    private CommonFunction mCommonFunction;
    private LinearLayout layout1, layout2, layout3, layout4, otherLyt;
    public BackPressListener backPressListener;
    private String villageValue, firstImageBase64, secondImageBase64, interventionTypeVal,
            genderVal, catVal, lesseVal, benVal;
    public String lat;
    public String lon;
    public EditText wauText, memberTxt;
    ArrayList<FishRequest> offlineMarkRequest = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCommonFunction = new CommonFunction(getActivity());
        // Inflate the layout for this fragment
        fisheriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fisheries, container, false);
        fisheriesBinding.popBackImage.setOnClickListener(this);
        fisheriesBinding.submissionBtn.setOnClickListener(this);
        fisheriesBinding.image1.setOnClickListener(this);
        fisheriesBinding.image2.setOnClickListener(this);


        remarks = fisheriesBinding.remarksTxt.getText().toString();


        componentSpinner = fisheriesBinding.componentTxt;
        sub_componentSpinner = fisheriesBinding.subComponentsTxt;
        stageSpinner = fisheriesBinding.stagesTxt;
        beneficaryFinal = fisheriesBinding.beneFinal;

        otherLyt = fisheriesBinding.othersLayout;
        layout1 = fisheriesBinding.layout1;
        layout2 = fisheriesBinding.layout2;
        layout3 = fisheriesBinding.layout3;
        layout4 = fisheriesBinding.layout4;


        backPressListener = this;

        fishCallApi = new FishCallApi(getActivity(), getContext(), componentDropDown, adapter, myString, backPressListener);
        fishCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stageSpinner, layout1, layout2, layout3, layout4, otherLyt, beneficaryFinal);

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

        remarks = fisheriesBinding.remarksTxt.getText().toString();


        if (fisheriesBinding.phase1.getSelectedItem() == null
                && subBasinSpinner.getSelectedItem() == null
                && districtSpinner.getSelectedItem() == null
                && blockSpinner.getSelectedItem() == null
                && componentSpinner.getSelectedItem() == null
                && sub_componentSpinner.getSelectedItem() == null
                && villageSpinner.getSelectedItem() == null
                && interventionSpinner.getSelectedItem() == null) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Empty field found.!, Please enter all the fields");
        }

        if (valueofPic != 0 && valueofPic != 1 && valueofPic != 2) {
            mCommonFunction.mLoadCustomToast(getActivity(), "Image is empty, Please take 2 photos");
        }

//        if (lengthNumberTxt.length() == 0) {
//            marketingBinding.lengthTxt.setError("Please enter farmer name");
//            return false;
//        } else if (lsPointTxt.length() == 0) {
//            marketingBinding.lsPoint.setError("Please enter survey no");
//            return false;
//        } else if (sliceNumberTxt.length() == 0) {
//            marketingBinding.sliceNumber.setError("Please enter area");
//            return false;
//        }


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
                    Toast.makeText(context, "Validation Error!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.image_1:
                if (checkPermission()) {
                    Log.i(TAG, "onClick: " + "granded.!");
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
                    Log.i(TAG, "onClick: " + "granded.!");
                    valueofPic = 2;
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
        multiSpinner1 = fisheriesBinding.speciesMulti1;
        multiSpinner2 = fisheriesBinding.speciesMulti2;
        multiSpinner3 = fisheriesBinding.speciesMulti3;
        lesseeSpinner = fisheriesBinding.lessee;
        beneficarySpinner = fisheriesBinding.bene1;
        beneficarySpinner1 = fisheriesBinding.bene2;
        categorySpinner = fisheriesBinding.categorySpinner;
        genderSpinner = fisheriesBinding.genderTxt;

        //multiSpinnerAdapters
        multiAdapterList = new ArrayList<>();
        multiAdapterList.add("Cata");
        multiAdapterList.add("Rahu");
        multiAdapterList.add("Mrigal");
        multiAdapterList.add("Common carp");
        multiAdapterList.add("Grass carp");

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
        beneList.add("WUA");
        beneList.add("Others");

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
                districtDropDown = FetchDeptLookup.readDistrictData(context, "district.json");
                posValue = String.valueOf(sub_basin_DropDown.get(i).getID());
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
                blockDropDown = FetchDeptLookup.readBlockData(context, "block.json");
                posValue = String.valueOf(districtDropDown.get(i).getID());
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

        fisheriesBinding.speciesMulti1.initMultiSpinner(getContext(), fisheriesBinding.speciesMulti1);
        fisheriesBinding.speciesMulti2.initMultiSpinner(getContext(), fisheriesBinding.speciesMulti2);
        fisheriesBinding.speciesMulti3.initMultiSpinner(getContext(), fisheriesBinding.speciesMulti3);

        fisheriesBinding.speciesMulti1.setBackground(getResources().getDrawable(R.drawable.edit_text_background));
        fisheriesBinding.speciesMulti1.setPadding(20, 20, 20, 20);
        fisheriesBinding.speciesMulti1.setAdapterWithOutImage(getContext(), multiAdapterList, new MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener() {
            @Override
            public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
                for (int i = 0; i < chosenItems.size(); i++) {
                    Log.e("chosenItems", chosenItems.get(i));
                }
            }
        });
        fisheriesBinding.speciesMulti2.setBackground(getResources().getDrawable(R.drawable.edit_text_background));
        fisheriesBinding.speciesMulti2.setPadding(20, 20, 20, 20);
        fisheriesBinding.speciesMulti2.setAdapterWithOutImage(getContext(), multiAdapterList, new MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener() {
            @Override
            public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
                for (int i = 0; i < chosenItems.size(); i++) {
                    Log.e("chosenItems", chosenItems.get(i));
                }
            }
        });
        fisheriesBinding.speciesMulti3.setBackground(getResources().getDrawable(R.drawable.edit_text_background));
        fisheriesBinding.speciesMulti3.setPadding(20, 20, 20, 20);
        fisheriesBinding.speciesMulti3.setAdapterWithOutImage(getContext(), multiAdapterList, new MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener() {
            @Override
            public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
                for (int i = 0; i < chosenItems.size(); i++) {
                    Log.e("chosenItems", chosenItems.get(i));
                }
            }
        });

        fisheriesBinding.lessee.setItem(lesseeList);
        lesseeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lesseVal = String.valueOf(lesseeSpinner.getSelectedItemPosition());
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fisheriesBinding.bene1.setItem(beneList);
        beneficarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                benVal = String.valueOf(beneficarySpinner.getSelectedItemPosition());
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fisheriesBinding.bene2.setItem(beneList);
        beneficarySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                benVal = String.valueOf(beneficarySpinner1.getSelectedItemPosition());
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
                Log.i(TAG, "interventionType:" + interventionTypeVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fisheriesBinding.speciesSpinner1.setItem(speciesList);
        beneficaryFinal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                benVal = String.valueOf(beneficaryFinal.getSelectedItemPosition());
                Log.i(TAG, "interventionType:" + interventionTypeVal);
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

        remarks = fisheriesBinding.remarksTxt.getText().toString();

        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateField = dateFormat.format(myCalendar.getTime());
        Log.i(TAG, "dataValue" + dateField);

        FishRequest request = new FishRequest();
        request.setBeneficiary("");
        request.setBeneficiary_name(benVal);
        request.setCreated_by("f55356773fce5b11");
        request.setCreated_date("2020-02-12 11:02:02");
        request.setFeed_qty("");
        request.setHarvested("");
        request.setImage1(firstImageBase64.trim());
        request.setIntervention1(intervention1);
        request.setIntervention2(intervention2);
        request.setIntervention3(intervention3);
        request.setLessee(lesseVal);
        request.setLat(lat);
        request.setLon(lon);
        request.setNo_of_stocks_req("");
        request.setNodal_officer("");
        request.setPhoto_lat(lat);
        request.setPhoto_lon(lon);
        request.setPond_constructed_by("");
        request.setQty_fish_harvested("");
        request.setRemarks(remarks);
        request.setSeed_no("");
        request.setSpecies_stoked("");
        request.setTank_name(fisheriesBinding.nameofTankTXT.getText().toString());
        request.setTxn_date("WedFeb12202012:04:46GMT+0530(IndiaStandardTime)");
        request.setTxn_id("20200212120446");
        request.setVillage(villageValue);
        request.setWater_spread_area("");
        request.setCategory("");
        request.setGender(genderVal);
        request.setMobile("");
        request.setSurvey_no("");
        request.setStatus("0");


        if (mCommonFunction.isNetworkAvailable() == true) {
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
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    mCommonFunction.navigation(getActivity(), DashboardActivity.class);
                    uploadSecondImage(txt_id);

                } else {

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