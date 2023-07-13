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

public class HortiCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> componentList, cropList,stageList,sub_componentList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    private LookUpDataClass lookUpDataClass;

    public HortiCallApi(Activity activity, Context context, List<ComponentData> componentList,
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

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner cropSpinner, Spinner stageSpinner,
                                   EditText datePicker, LinearLayout visLyt, LinearLayout trainingLyt, LinearLayout interventioNameLyt) {

        commonFunction = new CommonFunction(activity);

        componentList = FetchDeptLookup.readDataFromFile(context, "hortilookup.json");
        adapters = new ComponentAdapter(context, componentList);
        positionValue = "0";
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subComponentSpinner.setVisibility(View.VISIBLE);

                try {

                    positionValue = String.valueOf(componentList.get(i).getID());


                    Log.i(TAG, "onItemSelectedComponent: " + componentList.get(i).getID());
                    String names = componentList.get(i).getName();
                    if (names.equalsIgnoreCase("Others")) {
                        interventioNameLyt.setVisibility(View.VISIBLE);
                        subComponentSpinner.setVisibility(View.GONE);
                        cropSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        visLyt.setVisibility(View.VISIBLE);
                        trainingLyt.setVisibility(View.GONE);

                    } else if (names.equalsIgnoreCase("Model Village")) {
                        subComponenetDropDown(positionValue, subComponentSpinner,cropSpinner,stageSpinner ,datePicker, trainingLyt);
                        stageSpinner.setVisibility(View.GONE);
                        cropSpinner.setVisibility(View.GONE);
                        visLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.GONE);
                    }else if(names.equalsIgnoreCase("Crop Diversification")){
                        subComponenetDropDown(positionValue, subComponentSpinner,cropSpinner,stageSpinner,datePicker, trainingLyt);
                        cropSpinner.setVisibility(View.VISIBLE);
                        visLyt.setVisibility(View.VISIBLE);
                        trainingLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                    }else if(names.equalsIgnoreCase("IEC/CB")){
                        subComponenetDropDown(positionValue, subComponentSpinner,cropSpinner,stageSpinner,datePicker, trainingLyt);
                        cropSpinner.setVisibility(View.GONE);
                        visLyt.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.VISIBLE);
                        interventioNameLyt.setVisibility(View.GONE);
                    }
                    else {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        Log.i(TAG, "itemSelected: " + String.valueOf(componentList.get(i).getID()));
                        cropSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        trainingLyt.setVisibility(View.GONE);
                        visLyt.setVisibility(View.VISIBLE);
                        interventioNameLyt.setVisibility(View.GONE);
                        subComponenetDropDown(positionValue, subComponentSpinner,cropSpinner,stageSpinner ,datePicker, trainingLyt);
                    }
                    lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                    lookUpDataClass.setComponentValue(componentList.get(i).getName());
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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner cropSpinner,Spinner stageSpinner ,EditText editText, LinearLayout hideLayout) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "hortilookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String names = sub_componentList.get(i).getName();
                lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                lookUpDataClass.setSubComponentValue(sub_componentList.get(i).getName());

                try {
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    if (names.equalsIgnoreCase("Training")) {
                        hideLayout.setVisibility(View.VISIBLE);
                        cropSpinner.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Exposure")) {
                        hideLayout.setVisibility(View.VISIBLE);
                        cropSpinner.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Vegetables")
                            || names.equalsIgnoreCase("Fruits")
                            || names.equalsIgnoreCase("Spices")
                            || names.equalsIgnoreCase("Flowers")
                            || names.equalsIgnoreCase("Plantation Crops")) {
                        hideLayout.setVisibility(View.GONE);
                        cropSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("SWIKC")
                            || names.equalsIgnoreCase("Water walk") ||
                            names.equalsIgnoreCase("PRA Excercise") ||
                            names.equalsIgnoreCase("SWIC Centre") ||
                            names.equalsIgnoreCase("CCMG") ||
                            names.equalsIgnoreCase("Farmers Discussion") ||
                            names.equalsIgnoreCase("Village Vision") ||
                            names.equalsIgnoreCase("Entry Point Activity") ||
                            names.equalsIgnoreCase("Awareness Meeting")) {
                        hideLayout.setVisibility(View.GONE);
                        editText.setVisibility(View.GONE);
                        cropSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                    }else if(names.equalsIgnoreCase("CCWM")){
                        stageSpinner.setVisibility(View.VISIBLE);
                        hideLayout.setVisibility(View.GONE);
                        cropSpinner.setVisibility(View.GONE);
                        stagesDropDown(String.valueOf(sub_componentList.get(i).getID()),stageSpinner,editText);

                    }
                    else {
                        hideLayout.setVisibility(View.GONE);
                        cropSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        cropStageDropDown(String.valueOf(sub_componentList.get(i).getID()),cropSpinner,stageSpinner,editText);
                    }
                    Log.i(TAG, "posvalue2: " + positionValue2);
                    cropStageDropDown(String.valueOf(sub_componentList.get(i).getID()),cropSpinner,stageSpinner,editText);
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    lookUpDataClass.setSubComponentValue(sub_componentList.get(i).getName());
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


    public void cropStageDropDown(CharSequence stagePosVal, Spinner cropSpinner,Spinner stageSpinner,EditText editText) {

        commonFunction = new CommonFunction(activity);
        cropList = FetchDeptLookup.readDataFromFile(context, "hortilookup.json");
        adapters = new ComponentAdapter(context, cropList);
        adapters.getFilter().filter(stagePosVal);
        cropSpinner.setAdapter(adapters);
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Log.i(TAG, "names: " + cropList.get(i).getName());
                    String names = cropList.get(i).getName();
                    stagesDropDown(String.valueOf(cropList.get(i).getID()),stageSpinner,editText);
                    lookUpDataClass.setIntervention3(String.valueOf(cropList.get(i).getID()));
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void stagesDropDown(CharSequence stagePosVal, Spinner stageSpinner, EditText editText) {
        commonFunction = new CommonFunction(activity);
        stageList = FetchDeptLookup.readDataFromFile(context, "hortilookup.json");
        adapters = new ComponentAdapter(context, stageList);
        adapters.getFilter().filter(stagePosVal);
        stageSpinner.setAdapter(adapters);
        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String names = stageList.get(i).getName();
                    if (names.equalsIgnoreCase("Date of Sowing")||
                            names.equalsIgnoreCase("Date of Planting")
                            ||names.equalsIgnoreCase("Date of Transplanting")) {
                        editText.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention3(String.valueOf(cropList.get(i).getID()));
                    lookUpDataClass.setIntervention4(stageList.get(i).getName());
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

