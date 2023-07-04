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

public class MarketingCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList, sub_componentList, tankStageList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    public LookUpDataClass lookUpDataClass;

    public MarketingCallApi(Activity activity, Context context, List<ComponentData> getAllComponentData, ComponentAdapter adapters, CharSequence positionValue, BackPressListener backPressListener) {
        this.context = context;
        this.getAllComponentData = getAllComponentData;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }


    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, LinearLayout layoutComp1,
                                   LinearLayout layoutComp2, LinearLayout layoutTraining, LinearLayout layoutExpo,
                                   LinearLayout otherLyt,LinearLayout newReqLayt) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        getAllComponentData = FetchDeptLookup.readDataFromFile(context, "marklookup.json");
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
                    if (names.equalsIgnoreCase("Model Village")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, layoutTraining, layoutExpo,newReqLayt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        otherLyt.setVisibility(View.GONE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layoutTraining.setVisibility(View.GONE);
                        layoutExpo.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        newReqLayt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Others")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layoutTraining.setVisibility(View.GONE);
                        layoutExpo.setVisibility(View.GONE);
                        newReqLayt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Farmer Producer Company")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, layoutTraining, layoutExpo,newReqLayt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp1.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutTraining.setVisibility(View.GONE);
                        layoutExpo.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        newReqLayt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Construction of Storage Godowns")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, layoutTraining, layoutExpo,newReqLayt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.VISIBLE);
                        layoutComp1.setVisibility(View.GONE);
                        layoutTraining.setVisibility(View.GONE);
                        layoutExpo.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        newReqLayt.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Institutional training & Exposure visit")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, layoutTraining, layoutExpo,newReqLayt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        layoutComp2.setVisibility(View.GONE);
                        otherLyt.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layoutTraining.setVisibility(View.GONE);
                        layoutExpo.setVisibility(View.GONE);
                        newReqLayt.setVisibility(View.GONE);
                    } else {
                        subComponenetDropDown(positionValue, subComponentSpinner, layoutTraining, layoutExpo,newReqLayt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        otherLyt.setVisibility(View.GONE);
                        layoutComp1.setVisibility(View.GONE);
                        layoutComp2.setVisibility(View.GONE);
                        layoutTraining.setVisibility(View.GONE);
                        layoutExpo.setVisibility(View.GONE);
                        newReqLayt.setVisibility(View.GONE);
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
                                      LinearLayout layoutTraining, LinearLayout layoutExposure,
                                      LinearLayout newReqlayt) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "marklookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String names = sub_componentList.get(i).getName();
                try {
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());

                    Log.i(TAG, "posvalue2: " + positionValue2);

                    if (names.equalsIgnoreCase("Training")) {
                        layoutTraining.setVisibility(View.VISIBLE);
                        layoutExposure.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Exposure visit")) {
                        layoutTraining.setVisibility(View.GONE);
                        layoutExposure.setVisibility(View.VISIBLE);
                    }else if(names.equalsIgnoreCase("Business Plan Development")
                            ||names.equalsIgnoreCase("Matching Grant")){
                        layoutTraining.setVisibility(View.GONE);
                        layoutExposure.setVisibility(View.GONE);
                        newReqlayt.setVisibility(View.VISIBLE);
                    }
                    else {
                        layoutTraining.setVisibility(View.GONE);
                        layoutExposure.setVisibility(View.GONE);
                        newReqlayt.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    lookUpDataClass.setSubComponentValue(sub_componentList.get(i).getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}

