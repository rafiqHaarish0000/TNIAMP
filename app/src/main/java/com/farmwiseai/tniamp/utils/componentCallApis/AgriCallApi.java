package com.farmwiseai.tniamp.utils.componentCallApis;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgriCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList;
    private ComponentAdapter adapters, componentAdapter;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;

    public AgriCallApi(Activity activity, Context context, List<ComponentData> getAllComponentData, ComponentAdapter adapters, ComponentAdapter componentAdapter, CharSequence positionValue) {
        this.context = context;
        this.getAllComponentData = getAllComponentData;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.componentAdapter = componentAdapter;
        this.activity = activity;
    }

    //first spinner phrase;

    public void ComponentValues(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, EditText datePicker, LinearLayout hideLayout) {

        commonFunction = new CommonFunction(activity);


        if(commonFunction.isNetworkAvailable() == true){
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getAgriComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            getAllComponentData = response.body();

                            adapters = new ComponentAdapter(context, getAllComponentData);
                            positionValue = "0";
                            adapters.getFilter().filter(positionValue);
                            componentSpinner.setAdapter(adapters);

                            //position handling

                            componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                    if(getAllComponentData.get(i).getName().equals("IPM village-Vermicompost") &&
//                                            getAllComponentData.get(i).getName().equals("Cono Weeding")){
//                                        subComponentSpinner.setVisibility(View.VISIBLE);
//                                        stageSpinner.setVisibility(View.GONE);
//                                        datePicker.setVisibility(View.GONE);
//                                    }
//                                    else

                                    if(getAllComponentData.get(i).getName().equals("Model Village")){
                                        hideLayout.setVisibility(View.GONE);
                                    }else{
                                        subComponentSpinner.setVisibility(View.VISIBLE);
                                        stageSpinner.setVisibility(View.GONE);
                                        datePicker.setVisibility(View.GONE);
                                        hideLayout.setVisibility(View.VISIBLE);
                                        Log.i(TAG, "itemSelected: " + String.valueOf(getAllComponentData.get(i).getID()));
                                        //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                                        secondSpinnerPhrase(i, subComponentSpinner, stageSpinner,datePicker);
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
                    public void onFailure(Call<List<ComponentData>> call, Throwable t) {
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
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getTNAUComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            getAllComponentData = response.body();

                            adapters = new ComponentAdapter(context, getAllComponentData);


                            Log.d(TAG, "onItemSelected: " + getAllComponentData.get(posVal).getID());

                            //get id position for second filters
                            positionValue = String.valueOf(getAllComponentData.get(posVal).getID());
                            adapters.getFilter().filter(positionValue);
                            secondSpinner.setAdapter(adapters);

                            secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    thirdSpinner.setVisibility(View.VISIBLE);

                                    //save data for offline
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.SUB_COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                                    positionValue2 = String.valueOf(getAllComponentData.get(posVal).getID());
                                    Log.i(TAG, "posvalue2: " + positionValue);
                                    stagesValues(i, thirdSpinner,editText);
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
                    public void onFailure(Call<List<ComponentData>> call, Throwable t) {
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

    public void stagesValues(int posVal, Spinner stageSpinner, EditText datePicker) {
        commonFunction = new CommonFunction(activity);
        if(commonFunction.isNetworkAvailable()==true){
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getAgriComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            stagesList = response.body();
                            componentAdapter = new ComponentAdapter(context, stagesList);
                            componentAdapter.getFilter().filter(positionValue2);
                            stageSpinner.setAdapter(componentAdapter);

                            stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.i(TAG, "names: " + stagesList.get(i).getName());

                                    if (stagesList.get(i).getName().contains("Sowing") && stagesList.get(i).getName().contains("Planting") ) {
                                        datePicker.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        datePicker.setVisibility(View.GONE);
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
                    public void onFailure(Call<List<ComponentData>> call, Throwable t) {
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

