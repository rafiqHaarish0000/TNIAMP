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

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner tankStageSpinner, Spinner stageSpinner, EditText wauTxt,
                                   LinearLayout interventioNameLyt,EditText memTxt) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        getAllComponentData = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
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
                    positionValue = String.valueOf(getAllComponentData.get(i).getID());
                    String names = getAllComponentData.get(i).getName();
                    if (names.equals("Model Village")) {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, stageSpinner, wauTxt,memTxt);
                        tankStageSpinner.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                    } else {
                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, stageSpinner, wauTxt,memTxt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        tankStageSpinner.setVisibility(View.GONE);
                        wauTxt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner thirdSpinner, Spinner fourthSpinner, EditText wauTxt,EditText memTxt) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
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
                    Log.i(TAG, "posvalue2: " + positionValue2);

                    if(names.equalsIgnoreCase("Formation of WUA")){
                        thirdSpinner.setVisibility(View.GONE);
                        fourthSpinner.setVisibility(View.GONE);
                        wauTxt.setVisibility(View.VISIBLE);
                        memTxt.setVisibility(View.VISIBLE);

                    }else if(names.equalsIgnoreCase("Capacity building")){
                        thirdSpinner.setVisibility(View.GONE);
                        fourthSpinner.setVisibility(View.GONE);
                        memTxt.setVisibility(View.VISIBLE);
                        wauTxt.setVisibility(View.GONE);
                    }else if(names.equalsIgnoreCase("Shutters")){
                        thirdSpinner.setVisibility(View.GONE);
                        fourthSpinner.setVisibility(View.GONE);
                    }else if(names.equalsIgnoreCase("CCWM")){
                        stagesDropDown(positionValue2, fourthSpinner);
                        thirdSpinner.setVisibility(View.GONE);
                        fourthSpinner.setVisibility(View.VISIBLE);
                    }
                    else{
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        fourthSpinner.setVisibility(View.GONE);
                        tankStageComponent(positionValue2, thirdSpinner, fourthSpinner);
                    }
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

    public void tankStageComponent(CharSequence stagePosVal, Spinner tankStageSpinner, Spinner stageSpinner) {
        commonFunction = new CommonFunction(activity);
        tankStageList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
        adapters = new ComponentAdapter(context, tankStageList);
        adapters.getFilter().filter(String.valueOf(stagePosVal));
        tankStageSpinner.setAdapter(adapters);

        tankStageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    stageSpinner.setVisibility(View.VISIBLE);
                    positionValue2 = String.valueOf(tankStageList.get(i).getID());

                    String names = tankStageList.get(i).getName();
                    if (names.equalsIgnoreCase("Marking Boundary Line") ||
                            names.equalsIgnoreCase("Earthwork") ||
                            names.equalsIgnoreCase("Foundation") ||
                            names.equalsIgnoreCase("Erection of Boundary Stone") ||
                            names.equalsIgnoreCase("Completion")||
                            names.equalsIgnoreCase("Sectioning - Profile Formation")
                            ||names.equalsIgnoreCase("Formwork of Wall")||
                            names.equalsIgnoreCase("Concrete Work")||
                            names.equalsIgnoreCase("Finishing")||
                            names.equalsIgnoreCase("Casting Concrete Blocks")||
                            names.equalsIgnoreCase("Fixing Concrete Blocks")||
                            names.equalsIgnoreCase("Starting")||
                            names.equalsIgnoreCase("Removing Weeds Scrub Jungle")||
                            names.equalsIgnoreCase("Profile Formation")||
                            names.equalsIgnoreCase("Raft Foundation")||
                            names.equalsIgnoreCase("Revetment")||
                            names.equalsIgnoreCase("Well stening")||
                            names.equalsIgnoreCase("Erection of perforated")||
                            names.equalsIgnoreCase("Parapet wall")||
                            names.equalsIgnoreCase("Steel grill")||
                            names.equalsIgnoreCase("Horizontal")) {
                        stageSpinner.setVisibility(View.GONE);
                    } else {
                        stagesDropDown(positionValue2, stageSpinner);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void stagesDropDown(CharSequence stagePosVal, Spinner stageSpinner) {

        commonFunction = new CommonFunction(activity);
        stagesList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
        adapters = new ComponentAdapter(context, stagesList);
        adapters.getFilter().filter(stagePosVal);
        stageSpinner.setAdapter(adapters);

    }


}

