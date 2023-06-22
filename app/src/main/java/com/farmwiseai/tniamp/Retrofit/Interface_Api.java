package com.farmwiseai.tniamp.Retrofit;
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.DistrictData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.ResponseData.TNAU_Response;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Interface_Api
{

    /*
    * #dev_api = http://172.16.40.246:3000
#dev_api = http://vilfresh-admin.pptssolutions.com
dev_api = http://172.16.40.207:3000
    * */
//lookUps
    @GET("lookup?type=inv_tnau")
    Call<List<ComponentData>> getTNAUComponents();
    @GET("lookup?type=inv_agri")
    Call<List<ComponentData>> getAgriComponents();
    @GET("lookup?type=inv_horti")
    Call<List<ComponentData>> getHortiComponents();

    @GET("lookup?type=sub_basin")
    Call<List<Sub_Basin_Data>> getSub_basinData();
    @GET("lookup?type=district")
    Call<List<DistrictData>> getDistrictData();
    @GET("lookup?type=block")
    Call<List<BlockData>> getBlockData();

    @POST("/api/tnau")
    Call<List<TNAU_Response>> mTnauAllListItems(@Body TNAU_Request tnau_request);
//    @GET("lookup?type=sub_basin")
//    Call<List<Sub_Basin_Data>> getVillageData();





}

