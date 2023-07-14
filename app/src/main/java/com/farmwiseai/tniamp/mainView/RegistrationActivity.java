package com.farmwiseai.tniamp.mainView;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DepartmentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.RegisterRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.RegisterResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.databinding.ActivityRegistrationBinding;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.ValidationUtils;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DepartmentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private CommonFunction mCommonFunction;
    ActivityRegistrationBinding registrationBinding;
    public String department = null;
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageValue = null;

    private Integer departmentId, subBasinId, districtId, blockId, villageId;
    private Spinner departmentSpinner, sub_basin, districts, block, village;
    private List<DepartmentData> departmentList;
    private List<Sub_Basin_Data> sub_basin_DropDown;
    private List<DistrictData> districtDropDown;
    private List<BlockData> blockDropDown;
    private List<VillageData> villageDataList;
    private SubBasinAdapter subAdapter;
    private DepartmentAdapter adapters;
    private DistrictAdapter districtAdapter;
    private BlockAdapter blockAdapter;
    private VillageAdaapter villageAdaapter;
    private CharSequence positionValue;
    private CharSequence myString = "0";
    private CharSequence posValue = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(RegistrationActivity.this, R.layout.activity_registration);
        setContentView(registrationBinding.getRoot());
        mCommonFunction = new CommonFunction(RegistrationActivity.this);
        registrationBinding.submissionBtn.setOnClickListener(this);
        checkAllDropDowns();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submission_btn:
                if (validation(registrationBinding.name.getText().toString(),
                        registrationBinding.mobilenumber.getText().toString(),
                        registrationBinding.email.getText().toString())) {
                    finalSubmission();
                } else {
                    mCommonFunction.mLoadCustomToast(this, "Validation error,Please check the empty fields.!");
                }
                break;
        }
    }

    private boolean validation(String name, String mobileNumber, String emailId) {
        name = registrationBinding.name.getText().toString();
        mobileNumber = registrationBinding.mobilenumber.getText().toString();
        emailId = registrationBinding.email.getText().toString();
        if (department == null || subBasinValue == null || districtValue == null || blockValue == null ||
                villageValue == null) {
            mCommonFunction.mLoadCustomToast(RegistrationActivity.this, "Please enter all mandatory fields");
        } else if (name.length() == 0) {
            registrationBinding.name.setError("Invalid name");
            return false;
        } else if (!ValidationUtils.isValidMobileNumber(mobileNumber) || mobileNumber.length() == 0 || mobileNumber.length() < 10) {
            registrationBinding.mobilenumber.setError("Invalid mobile number");
            return false;
        } else if (!ValidationUtils.isValidEmail(emailId) || emailId.length() == 0) {
            registrationBinding.email.setError("Invalid Email ID");
            return false;
        }
        return true;
    }

    private void checkAllDropDowns() {
        departmentSpinner = registrationBinding.departmentSp;
        sub_basin = registrationBinding.subBasinTxt;
        districts = registrationBinding.districtTxt;
        block = registrationBinding.blockTxt;
        village = registrationBinding.villageTxt;

        departmentList = FetchDeptLookup.readDepartmentData(RegistrationActivity.this, "departmentlookup.json");
        positionValue = "1";
        adapters = new DepartmentAdapter(RegistrationActivity.this, departmentList);
        adapters.getFilter().filter(positionValue);
        departmentSpinner.setAdapter(adapters);


        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                department = departmentSpinner.getSelectedItem().toString();
//                mCommonFunction.mLoadCustomToast(RegistrationActivity.this,department);
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(RegistrationActivity.this, "sub_basin.json");
                subAdapter = new SubBasinAdapter(RegistrationActivity.this, sub_basin_DropDown);
              /*  department = departmentList.get(i).getNAME();
                myString = String.valueOf(1);
                subAdapter.getFilter().filter(myString);*/
                sub_basin.setAdapter(subAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sub_basin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtDropDown = FetchDeptLookup.readDistrictData(RegistrationActivity.this, "district.json");
                posValue = String.valueOf(sub_basin_DropDown.get(i).getID());
                subBasinId = sub_basin_DropDown.get(i).getID();
                subBasinValue = sub_basin_DropDown.get(i).getNAME();
                districtAdapter = new DistrictAdapter(RegistrationActivity.this, districtDropDown);
                Log.i(TAG, "subBasinPos: " + posValue);
                districtAdapter.getFilter().filter(posValue);
                districts.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                blockDropDown = FetchDeptLookup.readBlockData(RegistrationActivity.this, "block.json");
                posValue = String.valueOf(districtDropDown.get(i).getID());
                districtValue = districtDropDown.get(i).getNAME();
                Log.i(TAG, "posValue: " + posValue);
                blockAdapter = new BlockAdapter(RegistrationActivity.this, blockDropDown);
                blockAdapter.getFilter().filter(posValue);
                block.setAdapter(blockAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                villageDataList = FetchDeptLookup.readVillageData(RegistrationActivity.this, "village.json");
                posValue = String.valueOf(blockDropDown.get(i).getID());
                blockValue = blockDropDown.get(i).getNAME();
                villageAdaapter = new VillageAdaapter(RegistrationActivity.this, villageDataList);
                villageAdaapter.getFilter().filter(posValue);
                village.setAdapter(villageAdaapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                villageId = villageDataList.get(i).getID();
                villageValue = villageDataList.get(i).getNAME();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void finalSubmission() {
        try {
            if (mCommonFunction.isNetworkAvailable() == true) {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setSERIAL_NO("");
                registerRequest.setLINE_DEPT(1);
                registerRequest.setVILLAGE(villageId);
                registerRequest.setNAME(registrationBinding.name.getText().toString());
                registerRequest.setMOBILE(registrationBinding.mobilenumber.getText().toString());
                registerRequest.setEMAIL(registrationBinding.email.getText().toString());
                registerRequest.setCREATED_DATE("");
                registerRequest.setLat("");
                registerRequest.setLon("");
                registerRequest.setVersion("");
                registerRequest.setSubbasin(subBasinId);
                registerRequest.setUSER_STATUS(2);

                Interface_Api api = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<RegisterResponse> registerResponseCall = null;
                registerResponseCall = api.getRegisterResponse(registerRequest);

                registerResponseCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Intent i = new Intent(RegistrationActivity.this, MobileValidationActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("phone", registrationBinding.mobilenumber.getText().toString());
                            i.putExtras(extras);
                            startActivity(i);
                        } else {
                            mCommonFunction.mLoadCustomToast(RegistrationActivity.this, "Server error,Please try again later.!");
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        mCommonFunction.mLoadCustomToast(RegistrationActivity.this, "502 Bad Gateway.!");
                    }
                });


            } else {
                mCommonFunction.mLoadCustomToast(this, "Internet connection lost, Please connect your network available.!");
            }
        } catch (Exception e) {
//            mCommonFunction.mLoadCustomToast(this, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}