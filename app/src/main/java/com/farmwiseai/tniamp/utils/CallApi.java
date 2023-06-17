package com.farmwiseai.tniamp.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Retrofit.Request.ListOfTNAU;
import com.farmwiseai.tniamp.utils.adapters.CustomAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApi {
    private Context context;
    private List<ListOfTNAU> getAllListOfTNAU;
    private CustomAdapter adapters;
    private CharSequence positionValue;

    public CallApi(Context context, List<ListOfTNAU> getAllListOfTNAU, CustomAdapter adapters, CharSequence positionValue) {
        this.context = context;
        this.getAllListOfTNAU = getAllListOfTNAU;
        this.adapters = adapters;
        this.positionValue = positionValue;
    }

    //first spinner phrase;

    public void firstSpinnerPhrase(Spinner firstSpinner) {

        try {
            Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
            Call<List<ListOfTNAU>> userDataCall = null;
            userDataCall = call.getAllData();
            userDataCall.enqueue(new Callback<List<ListOfTNAU>>() {
                @Override
                public void onResponse(Call<List<ListOfTNAU>> call, Response<List<ListOfTNAU>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAllListOfTNAU = response.body();

                        adapters = new CustomAdapter(context, getAllListOfTNAU);
                        positionValue = "0";
                        adapters.getFilter().filter(positionValue);
                        firstSpinner.setAdapter(adapters);

                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ListOfTNAU>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });

        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            Log.d(TAG, "apiForAllListOfTNAU: " + arrayIndexOutOfBoundsException);
        }
    }

    //second spinner phrase;
    public void secondSpinnerPhrase(int posVal, Spinner secondSpinner) {
        try {
            Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
            Call<List<ListOfTNAU>> userDataCall = null;
            userDataCall = call.getAllData();
            userDataCall.enqueue(new Callback<List<ListOfTNAU>>() {
                @Override
                public void onResponse(Call<List<ListOfTNAU>> call, Response<List<ListOfTNAU>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        getAllListOfTNAU = response.body();

                        adapters = new CustomAdapter(context, getAllListOfTNAU);

                        Log.d(TAG, "onItemSelected: " + getAllListOfTNAU.get(posVal).getID());

                        //get id position for second filters
                        positionValue = String.valueOf(getAllListOfTNAU.get(posVal).getID());
                        adapters.getFilter().filter(positionValue);
                        secondSpinner.setAdapter(adapters);

                    } else {
                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ListOfTNAU>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });

        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            Log.d(TAG, "apiForAllListOfTNAU: " + arrayIndexOutOfBoundsException);
        }
    }

//    public void thirdSpinnerPhrase(int posVal, Spinner thirdSpinner) {
//        try {
//            Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
//            Call<List<ListOfTNAU>> userDataCall = null;
//            userDataCall = call.getAllData();
//            userDataCall.enqueue(new Callback<List<ListOfTNAU>>() {
//                @Override
//                public void onResponse(Call<List<ListOfTNAU>> call, Response<List<ListOfTNAU>> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        getAllListOfTNAU = response.body();
//
//                        adapters = new CustomAdapter(context, getAllListOfTNAU);
//                        adapters.getIdFilter().filter(String.valueOf(posVal));
//                        thirdSpinner.setAdapter(adapters);
//
//                    } else {
//                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<ListOfTNAU>> call, Throwable t) {
//                    Log.d(TAG, "onFailure: " + t);
//                }
//            });
//
//        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
//            Log.d(TAG, "apiForAllListOfTNAU: " + arrayIndexOutOfBoundsException);
//        }
//    }

}

