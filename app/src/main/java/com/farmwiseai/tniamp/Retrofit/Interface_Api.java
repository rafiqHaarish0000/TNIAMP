package com.farmwiseai.tniamp.Retrofit;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.GenerateOTP;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.TNAU_Response;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;
import com.farmwiseai.tniamp.Retrofit.DataClass.ValidateOTP;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Interface_Api
{

//lookUps and static get response

    @GET("lookup?type=inv_tnau")
    Call<List<ComponentData>> getTNAUComponents();
    @GET("lookup?type=inv_agri")
    Call<List<ComponentData>> getAgriComponents();
    @GET("lookup?type=inv_horti")
    Call<List<ComponentData>> getHortiComponents();
    @GET("lookup?type=inv_aed")
    Call<List<ComponentData>> getAEDComponets();

    @GET("lookup?type=sub_basin")
    Call<List<Sub_Basin_Data>> getSub_basinData();
    @GET("lookup?type=district")
    Call<List<DistrictData>> getDistrictData();
    @GET("lookup?type=block")
    Call<List<BlockData>> getBlockData();
    @GET("lookup?type=village_det")
    Call<List<VillageData>> getVillageData();

    @GET("otp/generate?")
    Call<GenerateOTP> generateOTP(@Query("phone") String phone_number, @Query("message") String message, @Query("digits") String digits);
    @GET("otp/validate?")
    Call<List<ValidateOTP>> validateOTP(@Query("phone") String phone_number, @Query("otp") String otp);

    // request and response data for all the departments
    @POST("/api/tnau")
    Call<List<TNAU_Response>> mTnauAllListItems(@Body TNAU_Request tnau_request);






}

