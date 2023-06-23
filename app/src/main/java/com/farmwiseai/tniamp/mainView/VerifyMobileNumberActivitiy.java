package com.farmwiseai.tniamp.mainView;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.ValidateOTP;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.ActivityVerifyMobileNumberActivitiyBinding;
import com.farmwiseai.tniamp.utils.BaseActivity;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyMobileNumberActivitiy extends BaseActivity {
    ActivityVerifyMobileNumberActivitiyBinding mBinding;
String otpValue,phoneNumber;
List<ValidateOTP> validateOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(VerifyMobileNumberActivitiy.this, R.layout.activity_verify_mobile_number_activitiy);
        Bundle extras = getIntent().getExtras();
        otpValue= extras.getString("otp");
        phoneNumber= extras.getString("phone");
        mBinding.digitCodeValue.setText(otpValue);
        setContentView(mBinding.getRoot());
              mBinding.confirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUtils_obj.isOTPNumber(mBinding.digitCodeValue.getText().toString())) {

                    mBinding.digitCodeValue.setError("otp is not found");

                }else{
                    validateOTP(phoneNumber,mBinding.digitCodeValue.getText().toString());

                }
            }
        });

        mBinding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUtils_obj.isEmptyEditText(mBinding.digitCodeValue.getText().toString())) {

                    mLoadCustomToast(VerifyMobileNumberActivitiy.this,"Please Enter Valid Mobile Number");

                }else{
                    ValidateOTP();
                }
            }
        });





    }

    private void validateOTP(String phoneNumber , String otpValue) {
        try {
            Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
            Call<List<ValidateOTP>> userDataCall = null;
            userDataCall = call.validateOTP(phoneNumber,otpValue);
            userDataCall.enqueue(new Callback<List<ValidateOTP>>() {
                @Override
                public void onResponse(Call<List<ValidateOTP>> call, Response<List<ValidateOTP>> response) {
                    if (response.body() != null) {
                        validateOTP = response.body();
                        Log.i(TAG, "onBody: " + response.code());
                        SharedPrefsUtils.putBoolean(VerifyMobileNumberActivitiy.this,SharedPrefsUtils.PREF_KEY.LOGIN_SESSION,true);
                        SharedPrefsUtils.putString(VerifyMobileNumberActivitiy.this,SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN,validateOTP.get(0).getSerialNo().toString());
                        Intent i = new Intent(VerifyMobileNumberActivitiy.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else {
                        mLoadCustomToast(getParent(), "InValid OTP ");

                    }
                }

                @Override
                public void onFailure(Call<List<ValidateOTP>> call, Throwable t) {
                    mLoadCustomToast(getParent(), "InValid OTP");

                }
            });
/*
            userDataCall.enqueue(new Callback<List<ValidateOTP>>() {
                @Override
                public void onResponse(Call<List<ValidateOTP>> call, Response<<List<ValidateOTP>> response) {
                    if (response.body() != null) {
                        validateOTP = response.body();
                        Log.i(TAG, "onBody: " + response.code());
                        SharedPrefsUtils.putBoolean(VerifyMobileNumberActivitiy.this,SharedPrefsUtils.PREF_KEY.LOGIN_SESSION,true);
                        SharedPrefsUtils.putString(VerifyMobileNumberActivitiy.this,SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN,validateOTP.getSerialNo().toString());
                        Intent i = new Intent(VerifyMobileNumberActivitiy.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else {
                        mLoadCustomToast(getParent(), "InValid OTP ");

                    }
                }

                @Override
                public void onFailure(Call<<List<ValidateOTP>> call, Throwable t) {
                    mLoadCustomToast(getParent(), "InValid OTP");

                }
            });
*/

        } catch (Exception e) {
            mLoadCustomToast(getParent(), "Exception Caught");
        }
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
    private void ValidateOTP(){

    }


}