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
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TNAU_CallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> componentList, stagesList, sub_componentList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    public LookUpDataClass lookUpDataClass;

    public TNAU_CallApi(Activity activity, Context context, List<ComponentData> componentList,
                        ComponentAdapter adapters, CharSequence positionValue, BackPressListener backPressListener) {
        this.context = context;
        this.componentList = componentList;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }

    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, EditText datePicker, LinearLayout hideLyt) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";

        if (commonFunction.isNetworkAvailable() == true) {
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getTNAUComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            componentList = response.body();

                            adapters = new ComponentAdapter(context, componentList);
                            positionValue = "0";
                            adapters.getFilter().filter(positionValue);
                            componentSpinner.setAdapter(adapters);

                            //position handling

                            componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                                        positionValue = String.valueOf(componentList.get(i).getID());
                                        Log.i(TAG, "onItemSelectedComponent: " + componentList.get(i).getID());

                                        subComponentSpinner.setVisibility(View.VISIBLE);
                                        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                                        Call<List<ComponentData>> userDataCall = null;
                                        userDataCall = call.getTNAUComponents();
                                        userDataCall.enqueue(new Callback<List<ComponentData>>() {
                                            @Override
                                            public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    //subComponentSpinner.setVisibility(View.VISIBLE);
                                                    if (componentList.get(i).getName().equals("Model Village")) {
                                                        hideLyt.setVisibility(View.GONE);
                                                        subComponentSpinner.setVisibility(View.GONE);
                                                        stageSpinner.setVisibility(View.GONE);
                                                        datePicker.setVisibility(View.GONE);
                                                    } else {
                                                        subComponentSpinner.setVisibility(View.VISIBLE);
                                                        stageSpinner.setVisibility(View.VISIBLE);
                                                        hideLyt.setVisibility(View.VISIBLE);
                                                        Log.i(TAG, "itemSelected: " + String.valueOf(componentList.get(i).getID()));
                                                        //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                                                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner, datePicker);
                                                    }
                                                } else {

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<ComponentData>> call, Throwable t) {

                                            }
                                        });

//                                        subComponentSpinner.setVisibility(View.VISIBLE);
//                                        if (getAllComponentData.get(i).getName().equals("Model Village")) {
//                                            hideLyt.setVisibility(View.GONE);
//                                        }
//                                        else {
//                                            subComponentSpinner.setVisibility(View.VISIBLE);
//                                            stageSpinner.setVisibility(View.VISIBLE);
//                                            hideLyt.setVisibility(View.VISIBLE);
//                                            Log.i(TAG, "itemSelected: " + String.valueOf(getAllComponentData.get(i).getID()));
//                                            //save data for offline data..
////                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));
//
//                                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
//                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
        } else {
            Toast.makeText(context, "Connection lost,Please check your internet connectivity", Toast.LENGTH_LONG).show();
        }


    }

    //second spinner phrase;
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner thirdSpinner, EditText editText) {

        commonFunction = new CommonFunction(activity);
        if (commonFunction.isNetworkAvailable() == true) {
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getTNAUComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            sub_componentList = response.body();
                            adapters = new ComponentAdapter(context, sub_componentList);
                            adapters.getFilter().filter(String.valueOf(posVal));
                            secondSpinner.setAdapter(adapters);

                            secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    String names = sub_componentList.get(i).getName();
                                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));

                                    try {

                                        if (names.contains("Sowing")) {
                                            editText.setVisibility(View.VISIBLE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (names.contains("Planting")) {
                                            editText.setVisibility(View.VISIBLE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (secondSpinner.getSelectedItem() == null && secondSpinner.getVisibility() == View.GONE) {
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else {
                                            thirdSpinner.setVisibility(View.VISIBLE);
                                        }
                                        positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                                        Log.i(TAG, "posvalue2: " + positionValue2);
                                        stagesDropDown(positionValue2, thirdSpinner, editText);
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
        } else {
            Toast.makeText(context, "Connection lost,Please check your internet connectivity", Toast.LENGTH_LONG).show();
        }

    }

    public void stagesDropDown(CharSequence stagePosVal, Spinner thirdSpinner, EditText editText) {
        commonFunction = new CommonFunction(activity);
        if (commonFunction.isNetworkAvailable() == true) {
            try {
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getTNAUComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            stagesList = response.body();
                            adapters = new ComponentAdapter(context, stagesList);
                            adapters.getFilter().filter(stagePosVal);
                            thirdSpinner.setAdapter(adapters);


                            thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        Log.i(TAG, "names: " + stagesList.get(i).getName());

                                        String names = stagesList.get(i).getName();
                                        lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
                                        backPressListener.onSelectedInputs(lookUpDataClass);
                                        if (names.contains("Sowing")) {
                                            editText.setVisibility(View.VISIBLE);
                                        } else if (names.contains("Planting")) {
                                            editText.setVisibility(View.VISIBLE);
                                        } else {
                                            editText.setVisibility(View.GONE);
                                        }
                                    } catch (Exception e) {

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
        } else {
            Toast.makeText(context, "Connection lost,Please check your internet connectivity", Toast.LENGTH_LONG).show();
        }

    }


}

