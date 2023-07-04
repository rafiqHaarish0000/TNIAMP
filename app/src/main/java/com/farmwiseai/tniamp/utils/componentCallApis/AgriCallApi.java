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

import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;

import java.util.List;

public class AgriCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList, sub_componentList;
    private ComponentAdapter adapters, componentAdapter;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    public LookUpDataClass lookUpDataClass;

    public AgriCallApi(Activity activity, Context context, List<ComponentData> getAllComponentData, ComponentAdapter adapters, ComponentAdapter componentAdapter, CharSequence positionValue, BackPressListener backPressListener) {
        this.context = context;
        this.getAllComponentData = getAllComponentData;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.componentAdapter = componentAdapter;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }


    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, EditText datePicker, LinearLayout hideLyt, LinearLayout trainingLyt, LinearLayout seedLyt,
                                   LinearLayout interventioNameLyt) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        getAllComponentData = FetchDeptLookup.readDataFromFile(context, "agrilookup.json");
        adapters = new ComponentAdapter(context, getAllComponentData);
        positionValue = "0";
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subComponentSpinner.setVisibility(View.VISIBLE);
                try {
                    lookUpDataClass.setIntervention1(String.valueOf(getAllComponentData.get(i).getID()));
                    lookUpDataClass.setComponentValue(getAllComponentData.get(i).getName());
                    positionValue = String.valueOf(getAllComponentData.get(i).getID());
                    String names = getAllComponentData.get(i).getName();

                    if (names.equalsIgnoreCase("Others")) {
                        interventioNameLyt.setVisibility(View.VISIBLE);
                        subComponentSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.VISIBLE);
                        seedLyt.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.GONE);

                    } else if (names.equalsIgnoreCase("Model Village")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.GONE);
                        seedLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                    } else if (names.equals("Seed Village Group")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
                        subComponentSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        seedLyt.setVisibility(View.VISIBLE);
                        interventioNameLyt.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Farmers Field School")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
                        stageSpinner.setVisibility(View.GONE);
                        seedLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("IPM village-Vermicompost")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.GONE);
                        seedLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("Cono Weeding")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.GONE);
                        seedLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);

                    } else {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
                        stageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.VISIBLE);
                        trainingLyt.setVisibility(View.GONE);
                        seedLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        Log.i(TAG, "itemSelected: " + String.valueOf(getAllComponentData.get(i).getID()));
                        //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    //second spinner phrase;
    public void subComponenetDropDown(String posVal, Spinner secondSpinner, Spinner thirdSpinner, EditText editText) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "agrilookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String names = sub_componentList.get(i).getName();

                thirdSpinner.setVisibility(View.VISIBLE);

                try {
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    lookUpDataClass.setSubComponentValue(sub_componentList.get(i).getName());
                    Log.i(TAG, "posvalue2: " + positionValue2);
                    if (names.contains("Sowing")) {
                        editText.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Installation")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Milky")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("First") || names.contains("Field")) {
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Harvest")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("SWIKC") || names.contains("Water walk") || names.contains("PRA Excercise") || names.contains("SWIC Centre") || names.contains("CCMG") || names.contains("Farmers Discussion") || names.contains("Village Vision") || names.contains("Entry Point Activity") || names.contains("Awareness Meeting") || names.contains("Water budgetting wall painting") || names.contains("Village vision wall painting")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                    }
                    stagesDropDown(positionValue2, thirdSpinner, editText);
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void stagesDropDown(CharSequence stagePosVal, Spinner thirdSpinner, EditText editText) {
        commonFunction = new CommonFunction(activity);
        stagesList = FetchDeptLookup.readDataFromFile(context, "agrilookup.json");
        adapters = new ComponentAdapter(context, stagesList);
        adapters.getFilter().filter(stagePosVal);
        thirdSpinner.setAdapter(adapters);
        thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Log.i(TAG, "names: " + stagesList.get(i).getName());
                    String names = stagesList.get(i).getName();
                    if (names.contains("Sowing")) {
                        editText.setVisibility(View.VISIBLE);
                    } else if (names.contains("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
                    lookUpDataClass.setIntervention4(stagesList.get(i).getName());
                    backPressListener.onSelectedInputs(lookUpDataClass);
                } catch (Exception e) {

                }
//                                    positionValue2 = String.valueOf(getAllComponentData.get(Integer.parseInt(stagePosVal)).getID());
//                                    Log.i(TAG, "posvalue2: " + positionValue);
//                                    stagesDropDown(positionValue2, thirdSpinner,editText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}

/*
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

public class AgriCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList, sub_componentList;
    private ComponentAdapter adapters, componentAdapter;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    public LookUpDataClass lookUpDataClass;

    public AgriCallApi(Activity activity, Context context, List<ComponentData> getAllComponentData, ComponentAdapter adapters, ComponentAdapter componentAdapter, CharSequence positionValue, BackPressListener backPressListener) {
        this.context = context;
        this.getAllComponentData = getAllComponentData;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.componentAdapter = componentAdapter;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }


    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, EditText datePicker, LinearLayout hideLyt, LinearLayout trainingLyt, LinearLayout seedLyt,
                                   LinearLayout interventioNameLyt) {

        commonFunction = new CommonFunction(activity);


        if (commonFunction.isNetworkAvailable() == true) {
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
                                    subComponentSpinner.setVisibility(View.VISIBLE);

                                    try {
                                        positionValue = String.valueOf(getAllComponentData.get(i).getID());
                                        String names = getAllComponentData.get(i).getName();
                                        if (names.equalsIgnoreCase("Others")) {
                                            subComponentSpinner.setVisibility(View.GONE);
                                            stageSpinner.setVisibility(View.GONE);
                                            hideLyt.setVisibility(View.VISIBLE);
                                            seedLyt.setVisibility(View.GONE);
                                            trainingLyt.setVisibility(View.GONE);
                                            interventioNameLyt.setVisibility(View.VISIBLE);
                                          //  subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);

                                        }
                                        else if (names.equals("Model Village")) {
                                            subComponentSpinner.setVisibility(View.VISIBLE);
                                            stageSpinner.setVisibility(View.GONE);
                                            hideLyt.setVisibility(View.VISIBLE);
                                            seedLyt.setVisibility(View.GONE);
                                            trainingLyt.setVisibility(View.GONE);
                                            interventioNameLyt.setVisibility(View.GONE);
                                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);

                                        } else if (names.equals("Farmers Field School")) {
                                            subComponenetDropDown(String.valueOf(positionValue),subComponentSpinner,stageSpinner,datePicker);
                                            subComponentSpinner.setVisibility(View.VISIBLE);
                                            stageSpinner.setVisibility(View.GONE);
                                            interventioNameLyt.setVisibility(View.GONE);
                                            trainingLyt.setVisibility(View.GONE);
                                        }
                                        else if (names.equals("Seed_Village_Group")) {
                                            subComponenetDropDown(String.valueOf(positionValue),subComponentSpinner,stageSpinner,datePicker);
                                            subComponentSpinner.setVisibility(View.GONE);
                                            stageSpinner.setVisibility(View.GONE);
                                            seedLyt.setVisibility(View.VISIBLE);
                                            interventioNameLyt.setVisibility(View.GONE);
                                            trainingLyt.setVisibility(View.GONE);
                                        }
                                        else if (names.equals("IPM")) {
                                            subComponentSpinner.setVisibility(View.GONE);
                                            stageSpinner.setVisibility(View.GONE);
                                            hideLyt.setVisibility(View.GONE);
                                            seedLyt.setVisibility(View.GONE);
                                            interventioNameLyt.setVisibility(View.GONE);
                                            trainingLyt.setVisibility(View.VISIBLE);
                                        } else if (names.contains("Cono Weeding")) {
                                            subComponenetDropDown(String.valueOf(positionValue),subComponentSpinner,stageSpinner,datePicker);
                                            stageSpinner.setVisibility(View.GONE);
                                        } else {
                                            subComponentSpinner.setVisibility(View.VISIBLE);
                                            stageSpinner.setVisibility(View.GONE);
                                            datePicker.setVisibility(View.GONE);
                                            hideLyt.setVisibility(View.VISIBLE);
                                            trainingLyt.setVisibility(View.GONE);
                                            seedLyt.setVisibility(View.GONE);
                                            interventioNameLyt.setVisibility(View.GONE);
                                            Log.i(TAG, "itemSelected: " + String.valueOf(getAllComponentData.get(i).getID()));
                                            //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker);
                                        }
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
    public void subComponenetDropDown(String posVal, Spinner secondSpinner, Spinner thirdSpinner, EditText editText) {

        commonFunction = new CommonFunction(activity);
        if (commonFunction.isNetworkAvailable() == true) {
            try {
                commonFunction.showProgress();
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getAgriComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            sub_componentList = response.body();

                            adapters = new ComponentAdapter(context, sub_componentList);
//                            Log.d(TAG, "onItemSelected: " + getAllComponentData.get(posVal).getID());

                            //get id position for second filters
//                            positionValue = String.valueOf(getAllComponentData.get(posVal).getID());

                            adapters.getFilter().filter(posVal);
                            secondSpinner.setAdapter(adapters);
commonFunction.hideProgress();
                            secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    String names = sub_componentList.get(i).getName();
                                    thirdSpinner.setVisibility(View.VISIBLE);

                                    try {
                                        positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                                        Log.i(TAG, "posvalue2: " + positionValue2);
                                        if (names.contains("Sowing")) {
                                            editText.setVisibility(View.VISIBLE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (names.contains("Planting")) {
                                            editText.setVisibility(View.VISIBLE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (names.contains("Installation")) {
                                            editText.setVisibility(View.GONE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (names.contains("Milky")) {
                                            editText.setVisibility(View.GONE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (names.contains("First") || names.contains("Field")) {
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else if (names.contains("Harvest")) {
                                            editText.setVisibility(View.GONE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        }else if (names.contains("CCWM")||names.contains("SWIKC")||names.contains("Water walk")||names.contains("PRA Excercise")||names.contains("SWIC Centre")||names.contains("CCMG")||names.contains("Farmers Discussion")||names.contains("Village Vision")||names.contains("Entry Point Activity")||names.contains("Awareness Meeting")) {
                                            editText.setVisibility(View.GONE);
                                            thirdSpinner.setVisibility(View.GONE);
                                        } else {
                                            editText.setVisibility(View.GONE);
                                            thirdSpinner.setVisibility(View.VISIBLE);
                                        }
                                        stagesDropDown(positionValue2, thirdSpinner, editText);
                                        lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
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
                commonFunction.showProgress();
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<List<ComponentData>> userDataCall = null;
                userDataCall = call.getAgriComponents();
                userDataCall.enqueue(new Callback<List<ComponentData>>() {
                    @Override
                    public void onResponse(Call<List<ComponentData>> call, Response<List<ComponentData>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            stagesList = response.body();
                            componentAdapter = new ComponentAdapter(context, stagesList);
                            componentAdapter.getFilter().filter(stagePosVal);
                            thirdSpinner.setAdapter(componentAdapter);

commonFunction.hideProgress();
                            thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        Log.i(TAG, "names: " + stagesList.get(i).getName());
                                        String names = stagesList.get(i).getName();
                                        if (names.contains("Sowing")) {
                                            editText.setVisibility(View.VISIBLE);
                                        } else if (names.contains("Planting")) {
                                            editText.setVisibility(View.VISIBLE);
                                        } else {
                                            editText.setVisibility(View.GONE);
                                        }
                                        lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
                                        backPressListener.onSelectedInputs(lookUpDataClass);
                                    } catch (Exception e) {

                                    }
//                                    positionValue2 = String.valueOf(getAllComponentData.get(Integer.parseInt(stagePosVal)).getID());
//                                    Log.i(TAG, "posvalue2: " + positionValue);
//                                    stagesDropDown(positionValue2, thirdSpinner,editText);
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

*/
