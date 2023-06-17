package com.farmwiseai.tniamp.mainView;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.databinding.ActivityMobileValidationBinding;
import com.farmwiseai.tniamp.utils.BaseActivity;

public class MobileValidationActivity extends BaseActivity {
ActivityMobileValidationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MobileValidationActivity.this, R.layout.activity_mobile_validation);
        setContentView(binding.getRoot());

        binding.mVerifyMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUtils_obj.isEmptyEditText(binding.mobileValues.getText().toString())
                        || !validationUtils_obj.isValidMobile(binding.mobileValues.getText().toString())) {

                    binding.mobileValues.setError("Mobile number not found");

//                    mLoadCustomToast(MobileValidationActivity.this,"Please enter the correct mobile number");

                }else{
                    Intent i = new Intent(MobileValidationActivity.this,VerifyMobileNumberActivitiy.class);
                    startActivity(i);
                }
            }
        });
    }
}