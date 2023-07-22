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

public class TNAU_CallApi {
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

    public TNAU_CallApi(Activity activity, Context context, List<ComponentData> componentList,
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

    public void ComponentDropDowns(Spinner componentSpinner, Spinner subComponentSpinner, Spinner stageSpinner, EditText datePicker, LinearLayout hideLyt,
                                   EditText variety, EditText yield) {

        commonFunction = new CommonFunction(activity);
        positionValue = "0";
        componentList = FetchDeptLookup.readDataFromFile(context, "lookup.json");
        adapters = new ComponentAdapter(context, componentList);
        positionValue = "0";
        adapters.getFilter().filter(positionValue);
        componentSpinner.setAdapter(adapters);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Log.i(TAG, "onItemSelectedComponent: " + componentList.get(i).getID());
                    positionValue = String.valueOf(componentList.get(i).getID());
                    subComponentSpinner.setVisibility(View.VISIBLE);
                    compName = componentList.get(i).getName();
                    subCompName = null;
                    stageName = null;
                    String names = componentList.get(i).getName();
                    //subComponentSpinner.setVisibility(View.VISIBLE);
                    if (names.equals("Model Village")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner, datePicker, variety, yield);
                        hideLyt.setVisibility(View.GONE);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("GHG emission")) {
                        subComponentSpinner.setVisibility(View.GONE);
                        stageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } /*else if(
                    names.equalsIgnoreCase("Pesticide free Village") ||
                            names.equalsIgnoreCase("Area Expansion") ||
                            names.equalsIgnoreCase("Model Village") ||
                            names.equalsIgnoreCase("High Density Planting") ||
                            names.equalsIgnoreCase("TNAU-special intervention"))
                    {
                        stageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);

                    }*/ else if (names.contains("Red gram promotion ")) {
                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner, datePicker, variety, yield);
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else {
                        subComponentSpinner.setVisibility(View.VISIBLE);
                        stageSpinner.setVisibility(View.GONE);
                        hideLyt.setVisibility(View.VISIBLE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                        Log.i(TAG, "itemSelected: " + String.valueOf(componentList.get(i).getID()));
                        //save data for offline data..
//                                    SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.COMPONENT,String.valueOf(getAllListOfTNAU.get(i).getName()));

                        subComponenetDropDown(positionValue, subComponentSpinner, stageSpinner, datePicker, variety, yield);

                    }
                    lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    // lookUpDataClass.setStagelastvalue(subCompName);
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
    public void subComponenetDropDown(CharSequence posVal, Spinner secondSpinner, Spinner thirdSpinner, EditText editText,
                                      EditText variety, EditText yield) {

        commonFunction = new CommonFunction(activity);
        sub_componentList = FetchDeptLookup.readDataFromFile(context, "lookup.json");
        adapters = new ComponentAdapter(context, sub_componentList);
        adapters.getFilter().filter(String.valueOf(posVal));
        secondSpinner.setAdapter(adapters);
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionValue2 = String.valueOf(sub_componentList.get(i).getID());
                String names = sub_componentList.get(i).getName();
                subCompName = sub_componentList.get(i).getName();
                stageName = null;
                //  thirdSpinner.setVisibility(View.VISIBLE);
                try {
                    if (names.contains("Sowing")) {
                        editText.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Installation")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Milky")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("First") || names.contains("Field")) {
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Harvest") || names.equalsIgnoreCase("Harvest of Pulse") ||
                            names.equalsIgnoreCase("Harvest of Rice")) {
                        variety.setVisibility(View.VISIBLE);
                        yield.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                    } else if (names.contains("Group formation")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Meetings")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Foliar Spray")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("CCWM")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.equalsIgnoreCase("Initial Convergence") || names.equalsIgnoreCase("Awareness Meeting") || names.equalsIgnoreCase("Entry Level Activity") || names.equalsIgnoreCase("Farmers Discussion (DSS)")) {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.GONE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else {
                        editText.setVisibility(View.GONE);
                        thirdSpinner.setVisibility(View.VISIBLE);
                        variety.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    }
                    stagesDropDown(positionValue2, thirdSpinner, editText, variety, yield);
                    lookUpDataClass.setIntervention2(String.valueOf(sub_componentList.get(i).getID()));
                    lookUpDataClass.setIntervention1(String.valueOf(componentList.get(i).getID()));
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
                    //   lookUpDataClass.setSubComponentValue(sub_componentList.get(i).getName());
                    backPressListener.onSelectedInputs(lookUpDataClass);
                    Log.i(TAG, "posvalue2: " + positionValue2);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void stagesDropDown(CharSequence stagePosVal, Spinner thirdSpinner, EditText editText, EditText varitey, EditText yield) {
        commonFunction = new CommonFunction(activity);
        stagesList = FetchDeptLookup.readDataFromFile(context, "lookup.json");
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
                    if (names.contains("Sowing")) {
                        editText.setVisibility(View.VISIBLE);
                        varitey.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    } else if (names.contains("Planting")) {
                        editText.setVisibility(View.VISIBLE);
                    } else if (names.equalsIgnoreCase("Harvest") ||
                            names.equalsIgnoreCase("Harvest of Pulse") ||
                            names.equalsIgnoreCase("Harvest of Rice") ||
                            names.equalsIgnoreCase("1st Harvest") ||
                            names.equalsIgnoreCase("Last Harvest")
                    ) {
                        editText.setVisibility(View.GONE);
                        varitey.setVisibility(View.VISIBLE);
                        yield.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                        varitey.setVisibility(View.GONE);
                        yield.setVisibility(View.GONE);
                    }
                    lookUpDataClass.setIntervention3(String.valueOf(stagesList.get(i).getID()));
                    lookUpDataClass.setIntervention4(stagesList.get(i).getName());
                    lookUpDataClass.setComponentValue(compName);
                    lookUpDataClass.setSubComponentValue(subCompName);
                    lookUpDataClass.setStageValue(stageName);
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