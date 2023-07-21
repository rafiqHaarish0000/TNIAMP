package com.farmwiseai.tniamp.mainView;

import static android.content.ContentValues.TAG;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.GenerateOTP;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.databinding.ActivityMobileValidationBinding;
import com.farmwiseai.tniamp.utils.BaseActivity;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.adapters.VillageAdaapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileValidationActivity extends BaseActivity {
    ActivityMobileValidationBinding binding;
    GenerateOTP generateOTP;
    CommonFunction commonFunction;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MobileValidationActivity.this, R.layout.activity_mobile_validation);
        commonFunction = new CommonFunction(MobileValidationActivity.this);
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            phoneNumber = extras.getString("phone");
            binding.mobileValues.setText(phoneNumber);
        }


//        if (binding.mobileValues.getText().toString().isEmpty()) {
//
//        } else if (binding.mobileValues.getText().toString().contains(phoneNumber)) {
//
//        } else {
//            binding.mobileValues.setText(phoneNumber);
//        }

        binding.mVerifyMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUtils_obj.isEmptyEditText(binding.mobileValues.getText().toString())
                        || !validationUtils_obj.isValidMobile(binding.mobileValues.getText().toString())) {

                    binding.mobileValues.setError("Enter Valid MobileNumber");

//                    mLoadCustomToast(MobileValidationActivity.this,"Please enter the correct mobile number");

                } else {
                    try {
                        commonFunction.showProgress();
                        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                        Call<GenerateOTP> userDataCall = null;
                        userDataCall = call.generateOTP(binding.mobileValues.getText().toString(), "Your TNIAMP OTP :  ", "4");
                        userDataCall.enqueue(new Callback<GenerateOTP>() {
                            @Override
                            public void onResponse(Call<GenerateOTP> call, Response<GenerateOTP> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    generateOTP = response.body();
                                    Log.i(TAG, "onBody: " + response.code());
                                    if (generateOTP.getResponse().equalsIgnoreCase("success")) {
                                        mLoadCustomToast(getParent(), "OTP send to mobile Number");
//                                        commonFunction.showProgress();
                                        Intent i = new Intent(MobileValidationActivity.this, VerifyMobileNumberActivitiy.class);
                                        Bundle extras = new Bundle();
                                        //   extras.putString("otp", generateOTP.getResponseMessage().getOtpDataId().toString());
                                        extras.putString("phone", binding.mobileValues.getText().toString());
                                        i.putExtras(extras);
                                        startActivity(i);
                                    }
                                } else {
                                    mLoadCustomToast(getParent(), "MobileNumber Not registered");
                                    Intent i = new Intent(MobileValidationActivity.this, RegistrationActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                commonFunction.dismiss();
                            }

                            @Override
                            public void onFailure(Call<GenerateOTP> call, Throwable t) {
                                mLoadCustomToast(getParent(), "Please check the internet connection.!");
commonFunction.dismiss();
                            }
                        });

                    } catch (Exception e) {
                        mLoadCustomToast(getParent(), "Exception Caught");
                    }

                }
            }
        });
    }
}