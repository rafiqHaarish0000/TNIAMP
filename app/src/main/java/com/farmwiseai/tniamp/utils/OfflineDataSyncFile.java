package com.farmwiseai.tniamp.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AEDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.Agri_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AnimalRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.FishRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.HortiRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.MarkRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.SecondImageRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.WRDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AEDResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AgriResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.TNAU_Response;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineDataSyncFile {
    public static CommonFunction mCommonFunction;
    public static Context mContext;
    public static String callbackstring = "failure";
    static String returnvalue = "";

    public OfflineDataSyncFile(Context mContext) {
        this.mContext = mContext;
    }

    public static String onlineDataTnauUpload(TNAU_Request request, String tnau_request) {
        mCommonFunction.showProgress();
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<TNAU_Response> userDataCall = call.getTnauResponse(request);
        userDataCall.enqueue(new Callback<TNAU_Response>() {
            @Override
            public void onResponse(Call<TNAU_Response> call, Response<TNAU_Response> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getTnauLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        callbackstring = uploadSecondImage(txt_id, tnau_request);
                    } catch (Exception e) {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<TNAU_Response> call, Throwable t) {
                mCommonFunction.hideProgress();
            }
        });
        return callbackstring;
    }

    public static String onlineDataAEDUpload(AEDRequest request, String aed_request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AEDResponse> userDataCall = null;
        userDataCall = call.getAEDResponse(request);
        userDataCall.enqueue(new Callback<AEDResponse>() {
            @Override
            public void onResponse(Call<AEDResponse> call, Response<AEDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getTnauLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    callbackstring = uploadSecondImage(txt_id, aed_request);
                }
            }

            @Override
            public void onFailure(Call<AEDResponse> call, Throwable t) {

            }
        });
        return callbackstring;
    }

    public static String onlineDataAgriUpload(Agri_Request request, String agriImageReq) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AgriResponse> userDataCall = null;
        userDataCall = call.getAgriResponse(request);
        userDataCall.enqueue(new Callback<AgriResponse>() {
            @Override
            public void onResponse(Call<AgriResponse> call, Response<AgriResponse> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getAgriLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        callbackstring = uploadSecondImage(txt_id, agriImageReq);

                    } catch (Exception e) {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<AgriResponse> call, Throwable t) {

            }
        });
        return callbackstring;
    }

  /*  public static String onlineDataHortiUpload(HortiRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<HortiResponse> userDataCall = null;
        userDataCall = call.getHortiResponse(request);
        userDataCall.enqueue(new Callback<HortiResponse>() {
            @Override
            public void onResponse(Call<HortiResponse> call, Response<HortiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getHortiLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        callbackstring = uploadSecondImage(txt_id, tnau_request);
                    } catch (Exception e) {

                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<HortiResponse> call, Throwable t) {

            }
        });
        return callbackstring;
    }

    public static String onlineDataMarketingUpload(MarkRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<MarkResponse> userDataCall = null;
        userDataCall = call.getMarkResponse(request);
        userDataCall.enqueue(new Callback<MarkResponse>() {
            @Override
            public void onResponse(Call<MarkResponse> call, Response<MarkResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getMarketinglanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    callbackstring = uploadSecondImage(txt_id, tnau_request);

                } else {

                }
            }

            @Override
            public void onFailure(Call<MarkResponse> call, Throwable t) {


            }
        });
        return callbackstring;
    }

    public static String onlineDataWrdUpload(WRDRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<WRDResponse> userDataCall = null;
        userDataCall = call.getWRDResponse(request);
        userDataCall.enqueue(new Callback<WRDResponse>() {
            @Override
            public void onResponse(Call<WRDResponse> call, Response<WRDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getWrdLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    callbackstring = uploadSecondImage(txt_id, tnau_request);

                } else {

                }
            }

            @Override
            public void onFailure(Call<WRDResponse> call, Throwable t) {


            }
        });
        return callbackstring;
    }

    public static String onlineDataAnimalUpload(AnimalRequest animalRequest) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AnimalResponse> userDataCall = null;
        userDataCall = call.getAnimalResponse(animalRequest);
        userDataCall.enqueue(new Callback<AnimalResponse>() {
            @Override
            public void onResponse(Call<AnimalResponse> call, Response<AnimalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getAnimallanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    callbackstring = uploadSecondImage(txt_id, tnau_request);

                } else {


                }
            }

            @Override
            public void onFailure(Call<AnimalResponse> call, Throwable t) {

            }
        });
        return callbackstring;
    }

    public static String onlineDataFisheriesUpload(FishRequest request) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<FishResponse> userDataCall = null;
        userDataCall = call.getFishRespone(request);
        userDataCall.enqueue(new Callback<FishResponse>() {
            @Override
            public void onResponse(Call<FishResponse> call, Response<FishResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getFisherylanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    callbackstring = uploadSecondImage(txt_id, tnau_request);

                } else {

                }
            }

            @Override
            public void onFailure(Call<FishResponse> call, Throwable t) {

            }
        });
        return callbackstring;
    }*/

    public static String uploadSecondImage(String txt_id, String secondImage) {

        SecondImageRequest request = new SecondImageRequest();
        //  request.setDepartment_id(SharedPrefsUtils.getString(mContext, SharedPrefsUtils.PREF_KEY.USER_DETAILS));
        request.setDepartment_id("1");
        request.setImg2(secondImage);
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
                        returnvalue = "success";
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        returnvalue = "failure";
                    }
                }
            }

            @Override
            public void onFailure(Call<SecondImageResponse> call, Throwable t) {

            }
        });
        return returnvalue;
    }

    public static String offLineCount(String deptId) {
        String count = String.valueOf(0);
        if (deptId == "1") {
            ArrayList<TNAU_Request> offlineRequest = SharedPrefsUtils.getArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "2") {
            ArrayList<Agri_Request> offlineRequest = SharedPrefsUtils.getAgriArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "3") {
            ArrayList<HortiRequest> offlineRequest = SharedPrefsUtils.getHortiArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "4") {
            ArrayList<AEDRequest> offlineRequest = SharedPrefsUtils.getAEDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "5") {
            ArrayList<AnimalRequest> offlineRequest = SharedPrefsUtils.getARDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "6") {
            ArrayList<WRDRequest> offlineRequest = SharedPrefsUtils.getWrdArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "7") {
            ArrayList<MarkRequest> offlineRequest = SharedPrefsUtils.getMarkArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId == "8") {
            ArrayList<FishRequest> offlineRequest = SharedPrefsUtils.getFishArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else {
            ArrayList<TNAU_Request> offlineRequest = SharedPrefsUtils.getArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        }
       /* if (deptId == "9") {
            int allCount = SharedPrefsUtils.getArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size() +
                    SharedPrefsUtils.getAgriArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size() +
                    SharedPrefsUtils.getAEDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size()
                    + SharedPrefsUtils.getARDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size()
                    + SharedPrefsUtils.getHortiArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size()
                    + SharedPrefsUtils.getMarkArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size()
                    + SharedPrefsUtils.getFishArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size()
                    + SharedPrefsUtils.getWrdArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size();
            if (allCount < 10) {
                count = String.valueOf(10 - allCount);
            }
        }*/
        return count;
    }


}
