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

public class HortiCallApi {
    private Activity activity;
    private Context context;
    private List<ComponentData> componentList, stagesList, sub_componentList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    private LookUpDataClass lookUpDataClass;

    public HortiCallApi(Activity activity, Context context, List<ComponentData> componentList,
                        ComponentAdapter adapters, CharSequence positionValue,BackPressListener backPressListener) {
        this.context = context;
        this.componentList = componentList;
        this.adapters = adapters;
        this.positionValue = positionValue;
        this.activity = activity;
        this.backPressListener = backPressListener;
        lookUpDataClass = new LookUpDataClass();
    }


    //first spinner phrase;

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, EditText datePicker, LinearLayout hideLyt, LinearLayout trainingLyt,LinearLayout interventioNameLyt) {

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
                    lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                    positionValue = String.valueOf(componentList.get(i).getID());
                    Log.i(TAG, "onItemSelectedComponent: " + componentList.get(i).getID());
                    String names = componentList.get(i).getName();
                    if (names.equalsIgnoreCase("Others")) {
                        interventioNameLyt.setVisibility(View.VISIBLE);
                        subComponentSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.VISIBLE);
                        trainingLyt.setVisibility(View.GONE);

                    } else if (names.equalsIgnoreCase("Model Village")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner, datePicker,trainingLyt);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                    }else {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.VISIBLE);
                        hideLyt.setVisibility(View.VISIBLE);
                        trainingLyt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        Log.i(TAG, "itemSelected: " + String.valueOf(componentList.get(i).getID()));
                     
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner, datePicker,trainingLyt);
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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner thirdSpinner, EditText editText,LinearLayout hideLayout) {

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
                try {
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    if (names.contains("Training")) {
                        hideLayout.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Exposure")) {
                        hideLayout.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                    }else if(names.contains("Vegetables")){
                        hideLayout.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                    }else if(names.contains("Vegetables")){
                        hideLayout.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                    }
                    else{
                        hideLayout.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    }
                    Log.i(TAG, "posvalue2: " + positionValue2);
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    stagesDropDown(positionValue2, thirdSpinner, editText);
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
        stagesList = FetchDeptLookup.readDataFromFile(context, "hortilookup.json");
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

       

    }


}

