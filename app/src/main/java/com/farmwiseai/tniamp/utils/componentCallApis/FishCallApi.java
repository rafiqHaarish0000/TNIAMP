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

public class FishCallApi {
    public LookUpDataClass lookUpDataClass;
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList, sub_componentList, tankStageList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    private String compName = null, subCompName = null, stageName = null, stageLastName = null;

    public FishCallApi(Activity activity, Context context, List<ComponentData> getAllComponentData, ComponentAdapter adapters, CharSequence positionValue, BackPressListener backPressListener) {
        this.context = context;
        this.getAllComponentData = getAllComponentData;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }


    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, LinearLayout layoutComp1,
                                   LinearLayout layoutComp2, LinearLayout layout3, LinearLayout layout4, LinearLayout layout5,LinearLayout layout6,
                                   LinearLayout otherLyt, Spinner beneficarySpinner, LinearLayout linFishTankInfo,EditText seedHarvest,
                                   EditText quantityHarvest,EditText quantityOfHarvesIrrigation,EditText qOHarvestL2) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        getAllComponentData = FetchDeptLookup.readDataFromFile(context, "fishlookup.json");
        adapters = new ComponentAdapter(context, getAllComponentData);
        positionValue = "0";
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subComponentSpinner.setVisibility(View.VISIBLE);
                try {
                    compName = getAllComponentData.get(i).getName();
                    subCompName = null;
                    stageName = null;
                    lookUpDataClass.setIntervention1(String.valueOf(getAllComponentData.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    backPressListener.onSelectedInputs(lookUpDataClass);
                    positionValue = String.valueOf(getAllComponentData.get(i).getID());
                    String names = getAllComponentData.get(i).getName();
                    if (names.equalsIgnoreCase("Model Village")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Others")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("Fish culture in irrigation tanks")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp1.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.VISIBLE);

                    } else if (names.equalsIgnoreCase("Cage farming of fishes")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.VISIBLE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.VISIBLE);

                    } else if (names.equalsIgnoreCase("Fish seed rearing in cages")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.VISIBLE);

                    } else if (names.equalsIgnoreCase("Aquaculture in farm ponds")
                            || names.equalsIgnoreCase("Earthern fish seed rearing and culture farm")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.VISIBLE);

                    } else if (names.equalsIgnoreCase("Fish Kiosk")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        layout6.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.VISIBLE);
                        linFishTankInfo.setVisibility(View.VISIBLE);

                    } else if (names.equalsIgnoreCase("Fish culture in Modular tanks")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        layout5.setVisibility(View.VISIBLE);
                        layout6.setVisibility(View.VISIBLE);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.GONE);
                    } else {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner,
                                layout5,layout6,seedHarvest,quantityHarvest,quantityOfHarvesIrrigation,qOHarvestL2);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        otherLyt.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layoutComp2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        layout5.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        beneficarySpinner.setVisibility(View.GONE);
                        linFishTankInfo.setVisibility(View.VISIBLE);

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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner,
                                      Spinner stageSpinner,
                                      LinearLayout layout5, LinearLayout layout6,
                                      EditText seedHarvest,EditText quantityHarvest,EditText quantityOfHarvestIrrigation,
                                      EditText qoHarvestL2) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "fishlookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String names = sub_componentList.get(i).getName();
                try {
                    subCompName = sub_componentList.get(i).getName();
                    stageName = null;
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    Log.i(TAG, "posvalue2: " + positionValue2);

                    if(compName.equalsIgnoreCase("Fish culture in Modular tanks")){
                        if(names.equalsIgnoreCase("Stocking")){
                            layout5.setVisibility(View.VISIBLE);
                            layout6.setVisibility(View.GONE);
                            seedHarvest.setVisibility(View.GONE);
                            quantityHarvest.setVisibility(View.GONE);
                        } else if (names.equalsIgnoreCase("Harvest")) {
                            seedHarvest.setVisibility(View.VISIBLE);
                            quantityHarvest.setVisibility(View.VISIBLE);
                            layout5.setVisibility(View.VISIBLE);
                            layout6.setVisibility(View.VISIBLE);
                        }else{
                            layout6.setVisibility(View.GONE);
                            seedHarvest.setVisibility(View.VISIBLE);
                            quantityHarvest.setVisibility(View.VISIBLE);
                        }
                    }

                    if(compName.equalsIgnoreCase("Aquaculture in farm ponds") ||
                            compName.equalsIgnoreCase("Earthern fish seed rearing and culture farm")){
                        if(names.equalsIgnoreCase("Stocking")){
                            seedHarvest.setVisibility(View.GONE);
                            quantityHarvest.setVisibility(View.GONE);
                        } else if (names.equalsIgnoreCase("Harvest")) {
                            seedHarvest.setVisibility(View.VISIBLE);
                            quantityHarvest.setVisibility(View.VISIBLE);
                        }else{
                            seedHarvest.setVisibility(View.VISIBLE);
                            quantityHarvest.setVisibility(View.VISIBLE);
                        }
                    }

                    if (names.equalsIgnoreCase("Long Seasonal tanks") ||
                            names.equalsIgnoreCase("Short Seasonal tanks")) {
                        stageSpinner.setVisibility(View.VISIBLE);
                        stagesDropDown(positionValue2, stageSpinner,quantityOfHarvestIrrigation);
                    } else if (names.equalsIgnoreCase("CCWM")) {
                        stageSpinner.setVisibility(View.VISIBLE);
                        stagesDropDown(positionValue2, stageSpinner,quantityOfHarvestIrrigation);
                    } else if (names.contains("Harvest")) {
                        stageSpinner.setVisibility(View.GONE);
                    } else {
                        stageSpinner.setVisibility(View.GONE);
                    }

                    if(compName.equalsIgnoreCase("Cage farming of fishes")){
                        if(names.equalsIgnoreCase("Harvest")){
                            qoHarvestL2.setVisibility(View.VISIBLE);
                        }else{
                            qoHarvestL2.setVisibility(View.GONE);
                        }
                    }

                    stagesDropDown(positionValue2, stageSpinner,quantityOfHarvestIrrigation);
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

    public void stagesDropDown(CharSequence stagePosVal, Spinner thirdSpinner,EditText quantityOfHarvestIrrigation) {
        commonFunction = new CommonFunction(activity);
        stagesList = FetchDeptLookup.readDataFromFile(context, "fishlookup.json");
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
                    if(compName.equalsIgnoreCase("Fish culture in irrigation tanks")){
                        if(names.equalsIgnoreCase("Harvest")){
                            quantityOfHarvestIrrigation.setVisibility(View.VISIBLE);
                        } else if (names.equalsIgnoreCase("Stocking")) {
                            quantityOfHarvestIrrigation.setVisibility(View.GONE);
                        } else {
                            quantityOfHarvestIrrigation.setVisibility(View.GONE);
                        }
                    }
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
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


    }


}

