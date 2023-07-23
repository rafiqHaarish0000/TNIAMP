package com.farmwiseai.tniamp.mainView;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.ActivityRegistrationBinding;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.LatLongPojo;
import com.farmwiseai.tniamp.utils.PermissionUtils;
import com.farmwiseai.tniamp.utils.ValidationUtils;
import com.farmwiseai.tniamp.utils.adapters.BlockAdapter;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DepartmentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DistrictAdapter;
import com.farmwiseai.tniamp.utils.adapters.SubBasinAdapter;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private GPSTracker gpsTracker;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(RegistrationActivity.this, R.layout.activity_registration);
        setContentView(registrationBinding.getRoot());
        mCommonFunction = new CommonFunction(RegistrationActivity.this);
        registrationBinding.submissionBtn.setOnClickListener(this);
        getLocation();
        checkAllDropDowns();
    }
    private void getLocation() {
        if (PermissionUtils.checkPermission(getApplicationContext()) == false) {
            requestPermission();
        }
        gpsTracker = new GPSTracker(this);
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                gpsTracker.getLatitude();
                gpsTracker.getLongitude();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                department = String.valueOf(departmentList.get(i).getID());
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
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void finalSubmission() {
        try {


            if (mCommonFunction.isNetworkAvailable() == true) {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setSerialNo("123po45pos67");
                registerRequest.setLineDept(department);
                registerRequest.setVillage(String.valueOf(villageId));
                registerRequest.setName(registrationBinding.name.getText().toString().trim());
                registerRequest.setMobile(registrationBinding.mobilenumber.getText().toString());
                registerRequest.setEmail(registrationBinding.email.getText().toString());
                registerRequest.setCreatedDate(getDateTime());
                LatLongPojo latLongPojo = new LatLongPojo();
                latLongPojo = PermissionUtils.getLocation(getApplicationContext());

                registerRequest.setLat(latLongPojo.getLat());
                registerRequest.setLon(latLongPojo.getLon());
                registerRequest.setVersion("2");
                registerRequest.setSubbasin(String.valueOf(subBasinId));
                registerRequest.setUserStatus("1");
                mCommonFunction.showProgress();

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
                            Toast.makeText(getApplicationContext(), "Please submit the valid data!", Toast.LENGTH_SHORT).show();
                        }
                        mCommonFunction.hideProgress();
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        mCommonFunction.hideProgress();
                    }
                });


            } else {
                mCommonFunction.mLoadCustomToast(this, "Please check your Internet connectivity!");
            }
        } catch (Exception e) {
//            mCommonFunction.mLoadCustomToast(this, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
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
    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

}