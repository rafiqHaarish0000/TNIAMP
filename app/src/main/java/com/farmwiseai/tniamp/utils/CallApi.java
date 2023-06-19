package com.farmwiseai.tniamp.utils;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private Activity activity;
    private Context context;
    private List<ListOfTNAU> getAllListOfTNAU, stagesList;
    private CustomAdapter adapters, customAdapter;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;

    public CallApi(Activity activity,Context context, List<ListOfTNAU> getAllListOfTNAU, CustomAdapter adapters, CustomAdapter customAdapter, CharSequence positionValue) {
        this.context = context;
        this.getAllListOfTNAU = getAllListOfTNAU;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.customAdapter = customAdapter;
        this.activity = activity;
    }


    //first spinner phrase;

    public void firstSpinnerPhrase(Spinner firstSpinner, Spinner secondSpinner, Spinner thirdSpinner,EditText text) {

        commonFunction = new CommonFunction(activity);


        if(commonFunction.isNetworkAvailable() == true){
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

                            //position handling

                            firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    secondSpinner.setVisibility(View.VISIBLE);
                                    thirdSpinner.setVisibility(View.GONE);
                                    Log.i(TAG, "itemSelected: " + String.valueOf(getAllListOfTNAU.get(i).getID()));
                                    //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                                    secondSpinnerPhrase(i, secondSpinner, thirdSpinner,text);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


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
        }else{
            Toast.makeText(context,"Connection lost,Please check your internet connectivity",Toast.LENGTH_LONG).show();
        }




    }

    //second spinner phrase;
    public void secondSpinnerPhrase(int posVal, Spinner secondSpinner, Spinner thirdSpinner,EditText editText) {

        commonFunction = new CommonFunction(activity);
        if(commonFunction.isNetworkAvailable()==true){
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

                            secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    thirdSpinner.setVisibility(View.VISIBLE);

                                    //save data for offline
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SUB_COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                                    positionValue2 = String.valueOf(getAllListOfTNAU.get(posVal).getID());
                                    Log.i(TAG, "posvalue2: " + positionValue);
                                    thirdSpinnerPhrase(i, thirdSpinner,editText);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


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
        }else{
            Toast.makeText(context,"Connection lost,Please check your internet connectivity",Toast.LENGTH_LONG).show();
        }

    }

    public void thirdSpinnerPhrase(int posVal, Spinner thirdSpinner,EditText editText) {
        commonFunction = new CommonFunction(activity);
        if(commonFunction.isNetworkAvailable()==true){
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ListOfTNAU>> userDataCall = null;
                userDataCall = call.getAllData();
                userDataCall.enqueue(new Callback<List<ListOfTNAU>>() {
                    @Override
                    public void onResponse(Call<List<ListOfTNAU>> call, Response<List<ListOfTNAU>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            stagesList = response.body();
                            customAdapter = new CustomAdapter(context, stagesList);
                            customAdapter.getFilter().filter(positionValue2);
                            thirdSpinner.setAdapter(customAdapter);

                            thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.i(TAG, "names: " + stagesList.get(i).getName());

                                    //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.STAGE,String.valueOf(stagesList.get(i).getName()));

                                    if (stagesList.get(i).getName().contains("Sowing")) {
                                        editText.setVisibility(View.VISIBLE);
                                    }else{
                                        editText.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

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
        }else{
            Toast.makeText(context,"Connection lost,Please check your internet connectivity",Toast.LENGTH_LONG).show();
        }

    }


}

