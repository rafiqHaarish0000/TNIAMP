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

public class WRDCallAPi {
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList, sub_componentList, tankStageList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    public LookUpDataClass lookUpDataClass;

    public WRDCallAPi(Activity activity, Context context, List<ComponentData> getAllComponentData, ComponentAdapter adapters, CharSequence positionValue, BackPressListener backPressListener) {
        this.context = context;
        this.getAllComponentData = getAllComponentData;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }


    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner tankStageSpinner,Spinner stageSpinner ,EditText datePicker,
                                   LinearLayout interventioNameLyt) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        getAllComponentData = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
        adapters = new ComponentAdapter(context, getAllComponentData);
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subComponentSpinner.setVisibility(View.VISIBLE);
                try {
                    positionValue = String.valueOf(getAllComponentData.get(i).getID());
                    String names = getAllComponentData.get(i).getName();
                    if (names.equalsIgnoreCase("Other")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        tankStageSpinner.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.VISIBLE);
//                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, datePicker);

                    } else if (names.equals("Model Village")) {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        tankStageSpinner.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, stageSpinner,datePicker);
                    } else {
                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, stageSpinner,datePicker);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        tankStageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention1(String.valueOf(getAllComponentData.get(i).getID()));


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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner thirdSpinner,Spinner fourthSpinner ,EditText editText) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String names = sub_componentList.get(i).getName();
                System.out.println(names);
                thirdSpinner.setVisibility(View.VISIBLE);
                try {
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    Log.i(TAG, "posvalue2: " + positionValue2);
                    if (names.equalsIgnoreCase("Tank Bund")) {
                        System.out.println(positionValue2);
                        thirdSpinner.setVisibility(View.VISIBLE);
                    } else {
                        thirdSpinner.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    tankStageComponent(positionValue2, thirdSpinner, fourthSpinner,editText);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void tankStageComponent(CharSequence stagePosVal, Spinner tankStageSpinner, Spinner stageSpinner, EditText editText) {
        commonFunction = new CommonFunction(activity);
//        tankStageList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");

        adapters = new ComponentAdapter(context, sub_componentList);
//        commonFunction.showProgress();
        adapters.getFilter().filter(String.valueOf(stagePosVal));
        tankStageSpinner.setAdapter(adapters);
        commonFunction.dismiss();
        tankStageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    stageSpinner.setVisibility(View.VISIBLE);
                    Log.i(TAG, "names: " + stagesList.get(i).getName());
                    positionValue2 = String.valueOf(tankStageList.get(i).getID());
                    stagesDropDown(positionValue2, stageSpinner, editText);
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
        stagesList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
        adapters = new ComponentAdapter(context, stagesList);
        adapters.getFilter().filter(String.valueOf(stagePosVal));
        stageSpinner.setAdapter(adapters);

//        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                try {
//                    Log.i(TAG, "names: " + stagesList.get(i).getName());
//                    String names = stagesList.get(i).getName();
////                    lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
////                    backPressListener.onSelectedInputs(lookUpDataClass);
//                    if (names.contains("Sowing")) {
//                        editText.setVisibility(View.VISIBLE);
//                    } else if (names.contains("Planting")) {
//                        editText.setVisibility(View.VISIBLE);
//                    } else {
//                        editText.setVisibility(View.GONE);
//                    }
//
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }


}

