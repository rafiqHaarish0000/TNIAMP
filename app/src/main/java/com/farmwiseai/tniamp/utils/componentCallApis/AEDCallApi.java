package com.farmwiseai.tniamp.utils.componentCallApis;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.utils.BackPressListener;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.FetchDeptLookup;
import com.farmwiseai.tniamp.utils.LookUpDataClass;
import com.farmwiseai.tniamp.utils.adapters.ComponentAdapter;

import java.util.List;

public class AEDCallApi {
    public LookUpDataClass lookUpDataClass;
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, sub_componentList, stagesList;
    private ComponentAdapter adapters, componentAdapter;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    private String compName = null, subCompName = null, stageName = null, stageLastName = null;

    public AEDCallApi(Activity activity, Context context, List<ComponentData> getAllComponentData,
                      ComponentAdapter adapters, ComponentAdapter componentAdapter, CharSequence positionValue,
                      BackPressListener backPressListener) {
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

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner thirdSpinner, LinearLayout hideLyt, LinearLayout otherLyt) {

        commonFunction = new CommonFunction(activity);
        getAllComponentData = FetchDeptLookup.readDataFromFile(context, "aedlookup.json");
        adapters = new ComponentAdapter(context, getAllComponentData);
        positionValue = "0";
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    compName = getAllComponentData.get(i).getName();
                    subCompName = null;
                    stageName = null;
                    lookUpDataClass.setIntervention1(String.valueOf(getAllComponentData.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    positionValue = String.valueOf(getAllComponentData.get(i).getID());
                    String name = getAllComponentData.get(i).getName();
                    if (name.equalsIgnoreCase("Model Village")) {
                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, thirdSpinner);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        hideLyt.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                    } else if (name.equalsIgnoreCase("Others")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.VISIBLE);
                        hideLyt.setVisibility(View.VISIBLE);

                    } else if (name.equalsIgnoreCase("Impact of sustainability")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.VISIBLE);

                    } else {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        hideLyt.setVisibility(View.VISIBLE);
                        otherLyt.setVisibility(View.GONE);

                        Log.i(TAG, "itemSelected: " + String.valueOf(getAllComponentData.get(i).getID()));
                        //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                        subComponenetDropDown(String.valueOf(positionValue), subComponentSpinner, thirdSpinner);
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
    public void subComponenetDropDown(String posVal, Spinner secondSpinner, Spinner thirdSpinner) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "aedlookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(posVal);
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String names = sub_componentList.get(i).getName();
                try {
                    subCompName = sub_componentList.get(i).getName();
                    stageName = null;
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    lookUpDataClass.setSubComponentValue(sub_componentList.get(i).getName());
                    Log.i(TAG, "posvalue2: " + positionValue2);
                    if (names.equalsIgnoreCase("CCWM")) {
                        thirdSpinner.setVisibility(View.VISIBLE);
                        stagesDropDown(String.valueOf(sub_componentList.get(i).getID()), thirdSpinner);

                    } else {
                        thirdSpinner.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    backPressListener.onSelectedInputs(lookUpDataClass);
//                                        stagesDropDown(positionValue2, thirdSpinner, editText);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void stagesDropDown(CharSequence stagePosVal, Spinner thirdSpinner) {
        commonFunction = new CommonFunction(activity);
        stagesList = FetchDeptLookup.readDataFromFile(context, "aedlookup.json");
        adapters = new ComponentAdapter(context, stagesList);
        adapters.getFilter().filter(stagePosVal);
        thirdSpinner.setAdapter(adapters);
        thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Log.i(TAG, "names: " + stagesList.get(i).getName());
                    String names = stagesList.get(i).getName();
                    stageName = stagesList.get(i).getName();
                    lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
                    lookUpDataClass.setIntervention4(stagesList.get(i).getName());
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    backPressListener.onSelectedInputs(lookUpDataClass);
//                                        if (names.contains("Sowing")) {
//                                            editText.setVisibility(View.VISIBLE);
//                                        } else if (names.contains("Planting")) {
//                                            editText.setVisibility(View.VISIBLE);
//                                        } else {
//                                            editText.setVisibility(View.GONE);
//                                        }
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

