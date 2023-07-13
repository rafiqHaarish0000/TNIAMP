package com.farmwiseai.tniamp.mainView;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DepartmentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
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

import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private CommonFunction mCommonFunction;
    ActivityRegistrationBinding registrationBinding;
    public String department = null;
    public String subBasinValue = null;
    public String districtValue = null;
    public String blockValue = null;
    public String villageValue = null;
    private Spinner departmentSpinner,sub_basin,districts,block,village;
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
//        mCommonFunction = new CommonFunction(RegistrationActivity.this);
        registrationBinding.submissionBtn.setOnClickListener(this);
        checkAllDropDowns();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submission_btn:
                if (validation(registrationBinding.name.getText().toString(),
                        registrationBinding.mobilenumber.getText().toString(),
                        registrationBinding.email.getText().toString())) {

                }
                break;
        }
    }

    private boolean validation(String name, String mobileNumber, String emailId) {
        name = registrationBinding.name.getText().toString();
        mobileNumber = registrationBinding.mobilenumber.getText().toString();
        emailId = registrationBinding.email.getText().toString();
//        if (department == null || subBasinValue == null || districtValue == null || blockValue == null ||
//                villageValue == null) {
//                mCommonFunction.mLoadCustomToast(RegistrationActivity.this,"Please enter all mandatory fields");
//            return false;
//        } else

        if (name.length() == 0) {
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

    private void checkAllDropDowns(){
        departmentSpinner = registrationBinding.departmentSp;
        sub_basin = registrationBinding.subBasinTxt;
        districts = registrationBinding.districtTxt;
        block = registrationBinding.blockTxt;
        village = registrationBinding.villageTxt;

        departmentList = FetchDeptLookup.readDepartmentData(RegistrationActivity.this, "departmentlookup.json");
        positionValue = "1";
        adapters = new DepartmentAdapter(RegistrationActivity.this,departmentList);
        adapters.getFilter().filter(positionValue);
        departmentSpinner.setAdapter(adapters);



        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                department = departmentSpinner.getSelectedItem().toString();
//                mCommonFunction.mLoadCustomToast(RegistrationActivity.this,department);
                sub_basin_DropDown = FetchDeptLookup.readSubBasin(RegistrationActivity.this, "sub_basin.json");
                subAdapter = new SubBasinAdapter(RegistrationActivity.this, sub_basin_DropDown);
                myString = String.valueOf(1);
                subAdapter.getFilter().filter(myString);
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void finalSubmission() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}