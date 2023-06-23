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
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentAgricultureBinding;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;
import com.farmwiseai.tniamp.utils.componentCallApis.AgriCallApi;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.CustomToast;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgricultureFragment extends Fragment implements View.OnClickListener  {
    private FragmentAgricultureBinding agricultureBinding;
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
    private Spinner subBasinSpinner, districtSpinner, blockSpinner, componentSpinner, sub_componentSpinner, stagesSpinner, genderSpinner, categorySpinner, villageSpinner;
    private EditText datePicker;
    private AgriCallApi agriCallApi;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean takePicture;
    private int valueofPic;
    private CommonFunction mCommonFunction;
    private List<String> phraseList, genderList, categoryList;
    private LinearLayout vis_lyt;
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


        agricultureBinding.popBackImage.setOnClickListener(this);
        agricultureBinding.submissionBtn.setOnClickListener(this);
        agricultureBinding.image1.setOnClickListener(this);
        agricultureBinding.image2.setOnClickListener(this);
        agricultureBinding.dateTxt.setOnClickListener(this);


        farmerName = agricultureBinding.farmerTxt.getText().toString();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        dateField = agricultureBinding.dateTxt.getText().toString();


        componentSpinner = agricultureBinding.componentTxt;
        sub_componentSpinner = agricultureBinding.subComponentsTxt;
        stagesSpinner = agricultureBinding.stagesTxt;
        datePicker = agricultureBinding.dateTxt;
        vis_lyt = agricultureBinding.visibilityLyt;

        agriCallApi = new AgriCallApi(getActivity(), getContext(), componentDropDown, adapter, adapter2, myString);
        agriCallApi.ComponentDropDowns(componentSpinner, sub_componentSpinner, stagesSpinner, datePicker,vis_lyt);

        setAllDropDownData();


        return agricultureBinding.getRoot();

        
    }

    private boolean fieldValidation(String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date) {

        farmerName = agricultureBinding.farmerTxt.getText().toString();
        survey_no = agricultureBinding.surveyTxt.getText().toString();
        area = agricultureBinding.areaTxt.getText().toString();
        near_tank = agricultureBinding.tankTxt.getText().toString();
        remarks = agricultureBinding.remarksTxt.getText().toString();
        date = agricultureBinding.dateTxt.getText().toString();


        if (agricultureBinding.phase1.getSelectedItem() == null
                && subBasinSpinner.getSelectedItem() == null
                && districtSpinner.getSelectedItem() == null
                && blockSpinner.getSelectedItem() == null
                && componentSpinner.getSelectedItem() == null
                && sub_componentSpinner.getSelectedItem() == null
                && stagesSpinner.getSelectedItem() == null
                && genderSpinner.getSelectedItem() == null
                && categorySpinner.getSelectedItem() == null
                && villageSpinner.getSelectedItem() == null) {
            mLoadCustomToast(getActivity(),"Empty field found.!, Please enter all the fields");
        }

        if(valueofPic != 0 && valueofPic != 1 && valueofPic != 2){
            mLoadCustomToast(getActivity(),"Image is empty, Please take 2 photos");
        }

        if (farmerName.length() == 0) {
            agricultureBinding.farmerTxt.setError("Please enter farmer name");
            return false;
        } else if (survey_no.length() == 0) {
            agricultureBinding.surveyTxt.setError("Please enter survey no");
            return false;
        } else if (area.length() == 0) {
            agricultureBinding.areaTxt.setError("Please enter area");
            return false;
        } else if (near_tank.length() == 0) {
            agricultureBinding.tankTxt.setError("Please enter near by tank name");
            return false;
        } else if (remarks.length() == 0) {
            agricultureBinding.remarksTxt.setError("Remarks not found");
            return false;
        } else if (date.length() == 0) {
            agricultureBinding.dateTxt.setError("Please enter the date");
            return false;
        }


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
                startActivity(intent);
                break;

            case R.id.submission_btn:
                boolean checkValidaiton = fieldValidation(farmerName,
                        category, survey_no, area, near_tank, remarks, dateField);
                if (checkValidaiton) {
                    finalSubmission();
                } else {
                    //do the code for save all data
                    Toast.makeText(context, "Data saved successfully.!", Toast.LENGTH_SHORT).show();
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

            case R.id.date_txt:
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

        }
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
                                    Log.i(TAG, "onResponse: " + agricultureBinding.phase1.getSelectedItemPosition());
                                    subAdapter = new SubBasinAdapter(getContext(), sub_basin_DropDown);
                                    myString = String.valueOf(agricultureBinding.phase1.getSelectedItemPosition());
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
                Log.i(TAG, "onValue: " + villageDataList.get(i).getNAME());
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
        agricultureBinding.genderTxt.setItem(genderList);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        agricultureBinding.dateTxt.setText(dateFormat.format(myCalendar.getTime()));
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
            if (takePicture && valueofPic == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                agricultureBinding.image1.setImageBitmap(photo);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo);
            } else if (!takePicture && valueofPic == 2) {
                Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                agricultureBinding.image2.setImageBitmap(photo2);
                // BitMap is data structure of image file which store the image in memory
                getEncodedString(photo2);
            }
        }

    }

    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

  /* or use below if you want 32 bit images

   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.URL_SAFE);

    }

    private void finalSubmission() {

        if (mCommonFunction.isNetworkAvailable() == true) {
            //data should saved in post api
            Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
            mCommonFunction.navigation(getActivity(), DashboardActivity.class);

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

}