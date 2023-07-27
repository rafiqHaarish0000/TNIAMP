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

public class AnimalCallApi {
    public LookUpDataClass lookUpDataClass;
    private Activity activity;
    private Context context;
    private List<ComponentData> componentList, stagesList, sub_componentList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    private String compName = null, subCompName = null, stageName = null, stageLastName = null;

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
                                   LinearLayout visLyt, LinearLayout trainLyt, LinearLayout pregnancyLyt,
                                   LinearLayout otherLyt,EditText variety,EditText yeild) {

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
                    compName = componentList.get(i).getName();
                    subCompName = null;
                    stageName = null;
                    lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    positionValue = String.valueOf(componentList.get(i).getID());
                    String names = componentList.get(i).getName();
                    if (names.contains("Model Village")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker, pregnancyLyt,variety,yeild);
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
                        otherLyt.setVisibility(View.GONE);
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker, pregnancyLyt,variety,yeild);
                        subComponentSpinner.setVisibility(View.VISIBLE);

                    } else if (names.equalsIgnoreCase("Calf Management") || names.equalsIgnoreCase("Mastitis Management")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker, pregnancyLyt,variety,yeild);
                        noOfCalves.setVisibility(View.VISIBLE);
                        noOfCalves.setHint("No of Calves");
                        trainLyt.setVisibility(View.GONE);
                        visLyt.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Infertility Management") || names.equalsIgnoreCase("Artificial Insemination")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker, pregnancyLyt,variety,yeild);
                        trainLyt.setVisibility(View.GONE);
                        noOfCalves.setVisibility(View.VISIBLE);
                        noOfCalves.setHint("No of Cows");
                        visLyt.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Fodder cultivation")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker, pregnancyLyt,variety,yeild);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.VISIBLE);
                        visLyt.setVisibility(View.VISIBLE);
                        datePicker.setVisibility(View.GONE);
                        trainLyt.setVisibility(View.GONE);
                        pregnancyLyt.setVisibility(View.GONE);
                        noOfCalves.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Others")) {
                        otherLyt.setVisibility(View.VISIBLE);
                        visLyt.setVisibility(View.GONE);
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
                        otherLyt.setVisibility(View.GONE);
                        Log.i(TAG, "itemSelected: " + String.valueOf(componentList.get(i).getID()));
                        //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, stageSpinner, datePicker, pregnancyLyt,variety,yeild);
                    }
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

    //second spinner phrase;
    public void subComponenetDropDown(CharSequence posVal,
                                      Spinner secondSpinner, Spinner thirdSpinner, EditText editText,
                                      LinearLayout pregnancyLyt,EditText variety,EditText yeild) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "ahdlookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String names = sub_componentList.get(i).getName();
                subCompName = sub_componentList.get(i).getName();
                stageName = null;
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
                        pregnancyLyt.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("SWIKC")
                            || names.equalsIgnoreCase("Water walk") ||
                            names.equalsIgnoreCase("PRA Excercise") ||
                            names.equalsIgnoreCase("Initial convergence") ||
                            names.equalsIgnoreCase("CCMG") ||
                            names.equalsIgnoreCase("Farmers Discussion (DSS)") ||
                            names.equalsIgnoreCase("Village Vision") ||
                            names.equalsIgnoreCase("Entry Level Activity") ||
                            names.equalsIgnoreCase("Awareness Meeting")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                    }
                    stagesDropDown(positionValue2, thirdSpinner, editText,variety,yeild);
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
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

    public void stagesDropDown(CharSequence stagePosVal, Spinner thirdSpinner, EditText editText,EditText variety,EditText yeild) {
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
                    stageName = stagesList.get(i).getName();
                    String names = stagesList.get(i).getName();
                    lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    if (names.equalsIgnoreCase("Sowing")) {
                        editText.setVisibility(View.VISIBLE);
                    } else if (names.contains("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                    }
                    if(compName.equalsIgnoreCase("Fodder cultivation")){
                        if(names.equalsIgnoreCase("Vegetative")){
                            variety.setVisibility(View.VISIBLE);
                            yeild.setVisibility(View.VISIBLE);
                        }else{
                            variety.setVisibility(View.GONE);
                            yeild.setVisibility(View.GONE);
                        }
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

