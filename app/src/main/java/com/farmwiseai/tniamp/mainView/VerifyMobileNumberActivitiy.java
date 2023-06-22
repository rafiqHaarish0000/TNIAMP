package com.farmwiseai.tniamp.mainView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.ActivityVerifyMobileNumberActivitiyBinding;
import com.farmwiseai.tniamp.utils.BaseActivity;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

public class VerifyMobileNumberActivitiy extends BaseActivity {
    ActivityVerifyMobileNumberActivitiyBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(VerifyMobileNumberActivitiy.this, R.layout.activity_verify_mobile_number_activitiy);
        setContentView(mBinding.getRoot());


        mBinding.confirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUtils_obj.isOTPNumber(mBinding.digitCodeValue.getText().toString())) {

                    mBinding.digitCodeValue.setError("otp is not found");

                }else{
                    SharedPrefsUtils.putBoolean(VerifyMobileNumberActivitiy.this,SharedPrefsUtils.PREF_KEY.LOGIN_SESSION,true);
                    Intent i = new Intent(VerifyMobileNumberActivitiy.this, DashboardActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        mBinding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUtils_obj.isEmptyEditText(mBinding.digitCodeValue.getText().toString())) {

                    mLoadCustomToast(VerifyMobileNumberActivitiy.this,"Please Enter Valid Mobile Number");

                }else{
                    generateOtp();
                }
            }
        });





    }

    private void twoDesignValidation() {
        Bundle bundle = getIntent().getExtras();
        boolean bool_value = bundle.getBoolean("get_bool");

//        if (!bool_value) {
//            Intent i = new Intent(VerifyMobileNumberActivitiy.this, ChangePasswordActivity.class);
//            i.putExtra("message", "Change Password");
//            i.putExtra("message_1", "Change");
//            startActivity(i);
//        } else if (bool_value) {
//            Intent i = new Intent(VerifyMobileNumberActivitiy.this, SignupActivity.class);
//            startActivity(i);
//        }
    }

    // get otp digit code for mobile number
    private void generateOtp(){

    }


}