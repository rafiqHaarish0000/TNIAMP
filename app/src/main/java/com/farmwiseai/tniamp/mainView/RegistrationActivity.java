package com.farmwiseai.tniamp.mainView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DepartmentData;
import com.farmwiseai.tniamp.databinding.ActivityRegistrationBinding;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.ValidationUtils;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;
import com.farmwiseai.tniamp.utils.adapters.DepartmentAdapter;

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
    private DepartmentAdapter adapters;
    private CharSequence positionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(RegistrationActivity.this, R.layout.activity_registration);
        setContentView(registrationBinding.getRoot());
        mCommonFunction = new CommonFunction(RegistrationActivity.this);
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