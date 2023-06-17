package com.farmwiseai.tniamp.mainView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.databinding.ActivityUserValidationBinding;
import com.farmwiseai.tniamp.utils.BaseActivity;

public class UserValidationActivity extends BaseActivity {
    ActivityUserValidationBinding mBinding;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(UserValidationActivity.this, R.layout.activity_user_validation);
        setContentView(mBinding.getRoot());


        if (ContextCompat.checkSelfPermission(UserValidationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserValidationActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        }



        mBinding.nextSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalidation();
            }
        });

        mBinding.popBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserValidationActivity.this, MobileValidationActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
        }
    }

    private void getvalidation() {


        if (!validationUtils_obj.isEmptyEditText(mBinding.departmentValue.getText().toString())) {
            mBinding.departmentValue.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.subBasinValue.getText().toString())) {
            mBinding.subBasinValue.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.districtValue.getText().toString())) {
            mBinding.districtValue.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.blockValue.getText().toString())) {
            mBinding.blockValue.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.villageValue.getText().toString())) {
            mBinding.villageValue.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.nameValue.getText().toString())) {
            mBinding.nameValue.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.mobileNumber.getText().toString())) {
            mBinding.mobileNumber.setError("please enter your name");
        }
        if (!validationUtils_obj.isEmptyEditText(mBinding.emailValue.getText().toString())) {
            mBinding.emailValue.setError("please enter your name");
        }


    }
}