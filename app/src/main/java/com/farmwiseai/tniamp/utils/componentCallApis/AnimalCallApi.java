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
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimalCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> componentList, stagesList, sub_componentList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    public LookUpDataClass lookUpDataClass;

    public AnimalCallApi(Activity activity, Context context, List<ComponentData> componentList,
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

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner,
                                   Spinner stageSpinner, EditText datePicker, EditText noOfCalves,
                                   LinearLayout visLyt, LinearLayout trainLyt,LinearLayout pregnancyLyt,
                                   LinearLayout otherLyt) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        componentList = FetchDeptLookup.readDataFromFile(context, "ahdlookup.json");
        adapters = new ComponentAdapter(context, componentList);
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                subComponentSpinner.setVisibility(View.VISIBLE);
                    positionValue = String.valueOf(componentList.get(i).getID());
                    Log.i(TAG, "onItemSelectedComponent: " + componentList.get(i).getID());
                    try {
                        lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                        positionValue = String.valueOf(componentList.get(i).getID());
                        lookUpDataClass.setComponentValue(componentList.get(i).getName());
                        String names = componentList.get(i).getName();
                        if (names.contains("Model Village")) {
                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker,pregnancyLyt);
                            subComponentSpinner.setVisibility(View.VISIBLE);
                            stageSpinner.setVisibility(View.GONE);
                            visLyt.setVisibility(View.GONE);
                            noOfCalves.setVisibility(View.GONE);
                            trainLyt.setVisibility(View.GONE);
                            otherLyt.setVisibility(View.GONE);
                        } else if (names.equalsIgnoreCase("Dairy Interest Group")) {
                            trainLyt.setVisibility(View.VISIBLE);
                            visLyt.setVisibility(View.GONE);
                            noOfCalves.setVisibility(View.GONE);
                        } else if (names.equalsIgnoreCase("Calf Management") || names.equalsIgnoreCase("Mastitis Management")) {
                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker,pregnancyLyt);
                            noOfCalves.setVisibility(View.VISIBLE);
                            noOfCalves.setHint("No of Calves");
                            trainLyt.setVisibility(View.GONE);
                            visLyt.setVisibility(View.GONE);
                        } else if (names.equalsIgnoreCase(" Infertility Management")||names.equalsIgnoreCase("Artificial Insemination")) {
                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker,pregnancyLyt);
                            trainLyt.setVisibility(View.GONE);
                            noOfCalves.setVisibility(View.VISIBLE);
                            noOfCalves.setHint("No of Cows");
                            visLyt.setVisibility(View.GONE);
                        }else if(names.equalsIgnoreCase("Fodder cultivation")){
                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker,pregnancyLyt);
                            subComponentSpinner.setVisibility(View.VISIBLE);
                            stageSpinner.setVisibility(View.VISIBLE);
                            visLyt.setVisibility(View.VISIBLE);
                            datePicker.setVisibility(View.GONE);
                            trainLyt.setVisibility(View.GONE);
                            pregnancyLyt.setVisibility(View.GONE);
                            noOfCalves.setVisibility(View.GONE);
                            otherLyt.setVisibility(View.GONE);
                        }else if(names.equalsIgnoreCase("Others")){
                            otherLyt.setVisibility(View.VISIBLE);
                            visLyt.setVisibility(View.VISIBLE);
                            subComponentSpinner.setVisibility(View.GONE);
                            stageSpinner.setVisibility(View.GONE);
                            datePicker.setVisibility(View.GONE);
                            trainLyt.setVisibility(View.GONE);
                            noOfCalves.setVisibility(View.GONE);
                            pregnancyLyt.setVisibility(View.GONE);
                        } else {
                            subComponentSpinner.setVisibility(View.VISIBLE);
                            stageSpinner.setVisibility(View.GONE);
                            datePicker.setVisibility(View.GONE);
                            trainLyt.setVisibility(View.VISIBLE);
                            pregnancyLyt.setVisibility(View.GONE);
                            noOfCalves.setVisibility(View.GONE);
                            Log.i(TAG, "itemSelected: " + String.valueOf(componentList.get(i).getID()));
                            //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                            subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker,pregnancyLyt);
                            backPressListener.onSelectedInputs(lookUpDataClass);
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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner thirdSpinner, EditText editText,LinearLayout pregnancyLyt) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "ahdlookup.json");
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
                    if (names.equalsIgnoreCase("Group meeting") || names.equalsIgnoreCase("One day training") ||
                            names.equalsIgnoreCase("One day exposure visit") || names.equalsIgnoreCase("Infertility camp") ||
                            names.equalsIgnoreCase("Estrus Synchronisation") || names.equalsIgnoreCase("Pregnancy verification") ||
                            names.equalsIgnoreCase("Calf born")) {
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Follow up meeting") || names.equalsIgnoreCase("Follow up visit") || names.equalsIgnoreCase("AI")) {
                        thirdSpinner.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("Deworming") || names.equalsIgnoreCase("Salt licks") ||
                            names.equalsIgnoreCase("Treatment") || names.equalsIgnoreCase("Prevention")) {
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Pregnancy diagnosis")) {
                        pregnancyLyt.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                    }else if (names.contains("SWIKC")||names.contains("Water walk")||names.contains("PRA Excercise")||names.contains("SWIC Centre")||names.contains("CCMG")||names.contains("Farmers Discussion")||names.contains("Village Vision")||names.contains("Entry Point Activity")||names.contains("Awareness Meeting")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    }
                    else {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                    }
                    stagesDropDown(positionValue2, thirdSpinner, editText);
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    backPressListener.onSelectedInputs(lookUpDataClass);
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
        stagesList = FetchDeptLookup.readDataFromFile(context, "ahdlookup.json");
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
                    if (names.equalsIgnoreCase("Sowing")) {
                        editText.setVisibility(View.VISIBLE);
                    } else if (names.contains("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                    }
                    backPressListener.onSelectedInputs(lookUpDataClass);
                } catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}

