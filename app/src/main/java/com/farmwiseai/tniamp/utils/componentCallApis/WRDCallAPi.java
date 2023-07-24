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
    public LookUpDataClass lookUpDataClass;
    private Activity activity;
    private Context context;
    private List<ComponentData> getAllComponentData, stagesList, sub_componentList, tankStageList;
    private ComponentAdapter adapters;
    private CharSequence positionValue;
    private CharSequence positionValue2;
    private CommonFunction commonFunction;
    private BackPressListener backPressListener;
    private String compName = null, subCompName = null, stageName = null, stageLastName = null, intervention3 = null;

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
                                   LinearLayout interventioNameLyt, EditText memTxt, LinearLayout linTankInfo) {

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
                    compName = getAllComponentData.get(i).getName();
                    subCompName = null;
                    stageName = null;
                    stageLastName = null;
                    lookUpDataClass.setIntervention1(String.valueOf(getAllComponentData.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    lookUpDataClass.setStagelastvalue(stageLastName);
                    positionValue = String.valueOf(getAllComponentData.get(i).getID());
                    String names = getAllComponentData.get(i).getName();
                    if (names.equals("Model Village")) {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, stageSpinner, wauTxt, memTxt, interventioNameLyt);
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        linTankInfo.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Others")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.VISIBLE);
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        linTankInfo.setVisibility(View.VISIBLE);
                    } else {
                        subComponenetDropDown(positionValue, subComponentSpinner, tankStageSpinner, stageSpinner, wauTxt, memTxt, interventioNameLyt);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        interventioNameLyt.setVisibility(View.GONE);
                        linTankInfo.setVisibility(View.VISIBLE);
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
    public void subComponenetDropDown(CharSequence posVal, Spinner subComponentSpinner, Spinner tankStageSpinner, Spinner stageSpinner, EditText wauTxt, EditText memTxt, LinearLayout linIntervention) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "wrdlookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        subComponentSpinner.setAdapter(adapters);
        // tankStageSpinner.setVisibility(View.VISIBLE);
        subComponentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //   tankStageSpinner.setVisibility(View.VISIBLE);
                try {

                    Log.i(TAG, "posvalue2: " + positionValue2);
                    subCompName = sub_componentList.get(i).getName();
                    stageName = null;
                    stageLastName = null;
                    String names = sub_componentList.get(i).getName();
                    positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                    if (names.equalsIgnoreCase("Others")) {
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        linIntervention.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("Formation of WUA")) {
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        wauTxt.setVisibility(View.VISIBLE);
                        memTxt.setVisibility(View.VISIBLE);
                        linIntervention.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Capacity building")) {
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        memTxt.setVisibility(View.VISIBLE);
                        wauTxt.setVisibility(View.GONE);
                        linIntervention.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Shutters")
                            || (names.equalsIgnoreCase("others")) |
                            (names.equalsIgnoreCase("Revetment"))) {
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        linIntervention.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("CCWM")) {
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.VISIBLE);
                        linIntervention.setVisibility(View.GONE);
                        stagesDropDown(positionValue2, stageSpinner);

                    } else if (names.equalsIgnoreCase("Initial Convergence")
                            || names.equalsIgnoreCase("Awareness Meeting")
                            || names.equalsIgnoreCase("Entry Level Activity")
                            || names.equalsIgnoreCase("Farmers Discussion (DSS)")) {
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        tankStageSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        linIntervention.setVisibility(View.GONE);
                    } else {
                        wauTxt.setVisibility(View.GONE);
                        memTxt.setVisibility(View.GONE);
                        tankStageSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.GONE);
                        linIntervention.setVisibility(View.GONE);
                        tankStageComponent(positionValue2, tankStageSpinner, stageSpinner);
                    }
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    if (subCompName.equalsIgnoreCase("Others")) {
                        lookUpDataClass.setStageValue("dummy value");
                        lookUpDataClass.setStagelastvalue("dummy value");
                    } else {
                        lookUpDataClass.setStageValue(stageName);
                        lookUpDataClass.setStagelastvalue(stageLastName);
                    }
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
                    stageName = tankStageList.get(i).getName();
                    intervention3 = String.valueOf(tankStageList.get(i).getID());
                    stageLastName = null;
                    lookUpDataClass.setIntervention3(intervention3);
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    lookUpDataClass.setStagelastvalue(stageLastName);
                    positionValue2 = String.valueOf(tankStageList.get(i).getID());
                    String names = tankStageList.get(i).getName();
                    if (subCompName.equalsIgnoreCase("Tank Bund")) {
                        if (names.equalsIgnoreCase("Strengthening of Bund") ||
                                names.equalsIgnoreCase("Model Sectioning of Bund")) {
                            stagesDropDown(String.valueOf(tankStageList.get(i).getID()), stageSpinner);
                            stageSpinner.setVisibility(View.VISIBLE);
                        } else if (names.equalsIgnoreCase("Jungle Clearance") ||
                                names.equalsIgnoreCase("Revetment")) {
                            stageSpinner.setVisibility(View.GONE);
                        }
                    } else if (compName.equalsIgnoreCase("Canal") || compName.equalsIgnoreCase("Channel")) {
                        if (subCompName.equalsIgnoreCase("Desilting")) {
                            stageSpinner.setVisibility(View.GONE);
                        }
                    } else if (names.equalsIgnoreCase("Marking Boundary Line") ||
                            names.equalsIgnoreCase("Earthwork") ||
                            names.equalsIgnoreCase("Removing Weeds - Scrub Jungle") ||
                            names.equalsIgnoreCase("Removing the Slit") ||
                            names.equalsIgnoreCase("Foundation") ||
                            names.equalsIgnoreCase("Erection of Boundary Stone") ||
                            names.equalsIgnoreCase("Completion") ||
                            names.equalsIgnoreCase("Sectioning - Profile Formation") ||
                            names.equalsIgnoreCase("Formwork for Wall") ||
                            names.equalsIgnoreCase("Concrete Work") ||
                            names.equalsIgnoreCase("Finishing") ||
                            names.equalsIgnoreCase("Casting Concrete Blocks") ||
                            names.equalsIgnoreCase("Fixing Concrete Blocks") ||
                            names.equalsIgnoreCase("Starting") ||
                            names.equalsIgnoreCase("Profile Formation") ||
                            names.equalsIgnoreCase("Raft Foundation") ||
                            names.equalsIgnoreCase("Revetment") ||
                            names.equalsIgnoreCase("Well stening") ||
                            names.equalsIgnoreCase("Erection of perforated") ||
                            names.equalsIgnoreCase("Parapet wall") ||
                            names.equalsIgnoreCase("Steel grill") ||
                            names.equalsIgnoreCase("Horizontal") ||
                            names.equalsIgnoreCase("Jungle Clearance") ||
                            names.equalsIgnoreCase("Strengthening of Bund") ||
                            names.equalsIgnoreCase("Model Sectioning of Bund") ||
                            names.equalsIgnoreCase("Formwork for Cut-off wall") ||
                            names.equalsIgnoreCase("Formwork") ||
                            names.equalsIgnoreCase("Formwork for BodyWall") ||
                            names.equalsIgnoreCase("Well stening concrete work") ||
                            names.equalsIgnoreCase("Erection of perforated PVC pipe") ||
                            names.equalsIgnoreCase("Initial") ||
                            names.equalsIgnoreCase("Syphon") ||
                            names.equalsIgnoreCase("Aquaduct") ||
                            names.equalsIgnoreCase("Culvert") ||
                            names.equalsIgnoreCase("Drop") ||
                            names.equalsIgnoreCase("Regulator") ||
                            names.equalsIgnoreCase("Under tunnel") ||
                            names.equalsIgnoreCase("Steel grill cover") ||
                            names.equalsIgnoreCase("Before") ||
                            names.equalsIgnoreCase("After") ||
                            names.equalsIgnoreCase("Jungle Clearance") ||
                            names.equalsIgnoreCase("Horizontal Shaft") ||
                            names.equalsIgnoreCase("benching") ||
                            names.equalsIgnoreCase("layer 1") ||
                            names.equalsIgnoreCase("layer 2") ||
                            names.equalsIgnoreCase("layer 3")) {
                        stageSpinner.setVisibility(View.GONE);
                    } else {
                        stagesDropDown(String.valueOf(tankStageList.get(i).getID()), stageSpinner);
                        stageSpinner.setVisibility(View.VISIBLE);
                    }

                    backPressListener.onSelectedInputs(lookUpDataClass);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
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
        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    stageLastName = stagesList.get(i).getName();
                    // stageLastName = null;

                    //   lookUpDataClass.setIntervention3(String.valueOf(tankStageList.get(i).getID()));
                    lookUpDataClass.setIntervention4(String.valueOf(stagesList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStagelastvalue(stageLastName);
                    if (stageName == null) {
                        lookUpDataClass.setStageValue("stageName");
                        lookUpDataClass.setIntervention3("1");

                    } else {
                        lookUpDataClass.setStageValue(stageName);
                        lookUpDataClass.setIntervention3(intervention3);

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


}

