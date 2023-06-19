package com.farmwiseai.tniamp.Retrofit;
import com.farmwiseai.tniamp.Retrofit.Request.ListOfTNAU;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Interface_Api
{

    /*
    * #dev_api = http://172.16.40.246:3000
#dev_api = http://vilfresh-admin.pptssolutions.com
dev_api = http://172.16.40.207:3000
    * */

    @GET("lookup?type=inv_tnau")
    Call<List<ListOfTNAU>> getAllData();





}

