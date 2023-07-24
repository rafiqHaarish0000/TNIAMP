package com.farmwiseai.tniamp.utils;

import static android.content.ContentValues.TAG;

import android.app.Activity;
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
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.AnimalResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.FishResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.HortiResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.MarkResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.SecondImageResponse;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.TNAU_Response;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.WRDResponse;
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

    public static void onlineDataTnauUpload(TNAU_Request request, String tnau_request, String lineDeptId) {

        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<TNAU_Response> userDataCall = call.getTnauResponse(request);
        userDataCall.enqueue(new Callback<TNAU_Response>() {
            @Override
            public void onResponse(Call<TNAU_Response> call, Response<TNAU_Response> response) {
                if (response.body() != null) {
                    try {
                        String txt_id = String.valueOf(response.body().getResponseMessage().getTnauLandDeptId());
                        Log.i(TAG, "txt_value: " + txt_id.toString());
                        uploadSecondImagetest(txt_id, tnau_request, lineDeptId);
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
        //  return callbackstring;
    }

    public static void onlineDataAEDUpload(AEDRequest request, String aed_request, String lineDeptId) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AEDResponse> userDataCall = null;
        userDataCall = call.getAEDResponse(request);
        userDataCall.enqueue(new Callback<AEDResponse>() {
            @Override
            public void onResponse(Call<AEDResponse> call, Response<AEDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getAedLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImagetest(txt_id, aed_request, lineDeptId);
                }
            }

            @Override
            public void onFailure(Call<AEDResponse> call, Throwable t) {

            }
        });

    }

    public static void onlineDataAgriUpload(Agri_Request request, String agriImageReq, String lineDeptId) {
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
                        uploadSecondImagetest(txt_id, agriImageReq, lineDeptId);

                    } catch (Exception e) {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<AgriResponse> call, Throwable t) {

            }
        });

    }

    public static void onlineDataHortiUpload(HortiRequest request, String HortiImageReq, String lineDeptId) {
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
                        uploadSecondImagetest(txt_id, HortiImageReq, lineDeptId);
                    } catch (Exception e) {

                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<HortiResponse> call, Throwable t) {

            }
        });

    }

    public static void onlineDataMarketingUpload(MarkRequest request, String markImgReq, String lineDeptId) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<MarkResponse> userDataCall = null;
        userDataCall = call.getMarkResponse(request);
        userDataCall.enqueue(new Callback<MarkResponse>() {
            @Override
            public void onResponse(Call<MarkResponse> call, Response<MarkResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getMarketinglanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImagetest(txt_id, markImgReq, lineDeptId);

                } else {

                }
            }

            @Override
            public void onFailure(Call<MarkResponse> call, Throwable t) {


            }
        });

    }

    public static void onlineDataWrdUpload(WRDRequest request, String wrdImgReq, String lineDeptId) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<WRDResponse> userDataCall = null;
        userDataCall = call.getWRDResponse(request);
        userDataCall.enqueue(new Callback<WRDResponse>() {
            @Override
            public void onResponse(Call<WRDResponse> call, Response<WRDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getWrdLandDeptId());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImagetest(txt_id, wrdImgReq, lineDeptId);

                } else {

                }
            }

            @Override
            public void onFailure(Call<WRDResponse> call, Throwable t) {


            }
        });

    }

    public static void onlineDataAnimalUpload(AnimalRequest animalRequest, String animImageReq, String lineDeptId) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<AnimalResponse> userDataCall = null;
        userDataCall = call.getAnimalResponse(animalRequest);
        userDataCall.enqueue(new Callback<AnimalResponse>() {
            @Override
            public void onResponse(Call<AnimalResponse> call, Response<AnimalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getAnimallanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImagetest(txt_id, animImageReq, lineDeptId);

                } else {


                }
            }

            @Override
            public void onFailure(Call<AnimalResponse> call, Throwable t) {

            }
        });

    }

    public static void onlineDataFisheriesUpload(FishRequest request, String fishImageReq, String lineDeptId) {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<FishResponse> userDataCall = null;
        userDataCall = call.getFishRespone(request);
        userDataCall.enqueue(new Callback<FishResponse>() {
            @Override
            public void onResponse(Call<FishResponse> call, Response<FishResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String txt_id = String.valueOf(response.body().getResponseMessage().getFisherylanddeptid());
                    Log.i(TAG, "txt_value: " + txt_id.toString());
                    uploadSecondImagetest(txt_id, fishImageReq, lineDeptId);

                } else {

                }
            }

            @Override
            public void onFailure(Call<FishResponse> call, Throwable t) {

            }
        });

    }

    public static void uploadSecondImage(String txt_id, String secondImage) {

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
        //  return returnvalue;
    }

    public static void uploadSecondImagetest(String txt_id, String secondImage, String lineDeptId) {

        SecondImageRequest request = new SecondImageRequest();
        //  request.setDepartment_id(SharedPrefsUtils.getString(mContext, SharedPrefsUtils.PREF_KEY.USER_DETAILS));
        request.setDepartment_id(lineDeptId);
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
                        mCommonFunction.mLoadCustomToast((Activity) mContext, successMessage);
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
        // return returnvalue;
    }

    public static String offLineCount(String deptId) {
        String count = String.valueOf(0);
        if (deptId.equalsIgnoreCase("1")) {
            ArrayList<TNAU_Request> offlineRequest = SharedPrefsUtils.getArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("2")) {
            ArrayList<Agri_Request> offlineRequest = SharedPrefsUtils.getAgriArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("3")) {
            ArrayList<HortiRequest> offlineRequest = SharedPrefsUtils.getHortiArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("4")) {
            ArrayList<AEDRequest> offlineRequest = SharedPrefsUtils.getAEDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("5")) {
            ArrayList<AnimalRequest> offlineRequest = SharedPrefsUtils.getARDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("6")) {
            ArrayList<WRDRequest> offlineRequest = SharedPrefsUtils.getWrdArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("7")) {
            ArrayList<MarkRequest> offlineRequest = SharedPrefsUtils.getMarkArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else if (deptId.equalsIgnoreCase("8")) {
            ArrayList<FishRequest> offlineRequest = SharedPrefsUtils.getFishArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            }
        } else {
            ArrayList<TNAU_Request> offlineRequest = SharedPrefsUtils.getArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
            ArrayList<Agri_Request> offlineRequest2 = SharedPrefsUtils.getAgriArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);
            ArrayList<HortiRequest> offlineRequest3 = SharedPrefsUtils.getHortiArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);
            ArrayList<AEDRequest> offlineRequest4 = SharedPrefsUtils.getAEDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
            ArrayList<AnimalRequest> offlineRequest5 = SharedPrefsUtils.getARDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);
            ArrayList<WRDRequest> offlineRequest6 = SharedPrefsUtils.getWrdArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
            ArrayList<MarkRequest> offlineRequest7 = SharedPrefsUtils.getMarkArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
            ArrayList<FishRequest> offlineRequest8 = SharedPrefsUtils.getFishArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);

            if (offlineRequest != null && offlineRequest.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest2 != null && offlineRequest2.size() > 0) {
                int allCount = offlineRequest2.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest3 != null && offlineRequest3.size() > 0) {
                int allCount = offlineRequest3.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest4 != null && offlineRequest4.size() > 0) {
                int allCount = offlineRequest4.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest5 != null && offlineRequest5.size() > 0) {
                int allCount = offlineRequest5.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest6 != null && offlineRequest6.size() > 0) {
                int allCount = offlineRequest.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest7 != null && offlineRequest7.size() > 0) {
                int allCount = offlineRequest7.size();
                count = String.valueOf(allCount);
            } else if (offlineRequest8 != null && offlineRequest8.size() > 0) {
                int allCount = offlineRequest8.size();
                count = String.valueOf(allCount);
            }
        }
       /* if (deptId == "9") {
            int allCount = SharedPrefsUtils.getArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA).size() +
                    SharedPrefsUtils.getAgriArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI).size() +
                    SharedPrefsUtils.getHortiArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI).size()
                    + SharedPrefsUtils.getARDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI).size()
                    + SharedPrefsUtils.getARDArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED).size()
                    + SharedPrefsUtils.getMarkArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING).size()
                    + SharedPrefsUtils.getFishArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH).size()
                    + SharedPrefsUtils.getWrdArrayList(mContext, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD).size();
            if (allCount < 10) {
                count = String.valueOf(10 - allCount);
            }
        }*/
        return count;
    }


}
