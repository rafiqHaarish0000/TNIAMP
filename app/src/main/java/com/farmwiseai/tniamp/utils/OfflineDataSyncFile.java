package com.farmwiseai.tniamp.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AEDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AEDResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineDataSyncFile {
    private CommonFunction mCommonFunction;
 public  static String callbackstring="failure";
    public static String onlineDataAEDUpload(AEDRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AEDResponse> userDataCall = null;
        userDataCall = call.getAEDResponse(request);
        userDataCall.enqueue(new Callback<AEDResponse>() {
            @Override
            public void onResponse(Call<AEDResponse> call, Response<AEDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getTnauLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                  callbackstring=  uploadSecondImage(txt_id);
                }
            }

            @Override
            public void onFailure(Call<AEDResponse> call, Throwable t) {

            }
        });
return callbackstring;
    }
    static String returnvalue="";
    public static String uploadSecondImage(String txt_id) {

        SecondImageRequest request = new SecondImageRequest();
        request.setDepartment_id("4");
        request.setImg2("secondImageBase64.trim()");
        request.setID(txt_id);
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<SecondImageResponse> userDataCall = null;
        userDataCall = call.getSecondImageURL(request);
        userDataCall.enqueue(new Callback<SecondImageResponse>() {
            @Override
            public void onResponse(Call<SecondImageResponse> call, Response<SecondImageResponse> response) {
                if (response.body() != null) {
                    try {
                        String successMessage = response.body().getResponse();
                        Log.i(TAG, "onSuccessMsg" + successMessage);
                        returnvalue="success";
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        returnvalue="failure";
                    }
                }
            }

            @Override
            public void onFailure(Call<SecondImageResponse> call, Throwable t) {

            }
        });
return returnvalue;
    }

}
