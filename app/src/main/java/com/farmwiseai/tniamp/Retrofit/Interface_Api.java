package com.farmwiseai.tniamp.Retrofit;

import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.GenerateOTP;
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
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.ValidateOTP;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Interface_Api {

//lookUps and static get response

    @GET("lookup?type=inv_tnau")
    Call<List<ComponentData>> getTNAUComponents();

    @GET("lookup?type=inv_agri")
    Call<List<ComponentData>> getAgriComponents();

    @GET("lookup?type=inv_horti")
    Call<List<ComponentData>> getHortiComponents();

    @GET("lookup?type=inv_aed")
    Call<List<ComponentData>> getAEDComponets();

    @GET("lookup?type=inv_wrd")
    Call<List<ComponentData>> getWRDComponents();

    @GET("lookup?type=inv_husb")
    Call<List<ComponentData>> getAnimalComponents();

    @GET("lookup?type=sub_basin")
    Call<List<Sub_Basin_Data>> getSub_basinData();

    @GET("lookup?type=district")
    Call<List<DistrictData>> getDistrictData();

    @GET("lookup?type=block")
    Call<List<BlockData>> getBlockData();

    @GET("lookup?type=village_det")
    Call<List<VillageData>> getVillageData();

    //image2 url
    @POST("image2")
    Call<SecondImageResponse> getSecondImageURL(@Body SecondImageRequest secondImageRequest);

    @GET("otp/generate?")
    Call<GenerateOTP> generateOTP(@Query("phone") String phone_number, @Query("message") String message, @Query("digits") String digits);

    @GET("otp/validate?")
    Call<ValidateOTP> validateOTP(@Query("phone") String phone_number, @Query("otp") String otp);

    // request and response data for all the departments
    @POST("tnau")
    Call<TNAU_Response> getTnauResponse(@Body TNAU_Request tnau_request);

    @POST("agri")
    Call<AgriResponse> getAgriResponse(@Body Agri_Request agri_request);

    @POST("aed")
    Call<AEDResponse> getAEDResponse(@Body AEDRequest tnau_request);

    @POST("horti")
    Call<HortiResponse> getHortiResponse(@Body HortiRequest tnau_request);

    @POST("wrd")
    Call<WRDResponse> getWRDResponse(@Body WRDRequest tnau_request);

    @POST("animalhusbandary")
    Call<AnimalResponse> getAnimalResponse(@Body AnimalRequest animalRequest);

    @POST("marketing")
    Call<MarkResponse> getMarkResponse(@Body MarkRequest markRequest);

    @POST("fishery")
    Call<FishResponse> getFishRespone(@Body FishRequest fishRequest);

}

