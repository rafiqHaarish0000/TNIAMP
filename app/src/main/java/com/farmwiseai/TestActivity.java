package com.farmwiseai;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Retrofit.Request.ListOfTNAU;
import com.farmwiseai.tniamp.databinding.ActivityTestBinding;
import com.farmwiseai.tniamp.utils.CallApi;
import com.farmwiseai.tniamp.utils.adapters.CustomAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding testBinding;
    List<ListOfTNAU> spinnerPos1, spinnerPos2, spinnerPos3;
    CharSequence myString = "0";
    CustomAdapter adapter, adapter2, adapter3;
    Spinner firstSpinner, secondSpinner, thirdSpinner;
    private CallApi callApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testBinding = DataBindingUtil.setContentView(TestActivity.this, R.layout.activity_test);
        setContentView(testBinding.getRoot());

        firstSpinner = testBinding.phase1;
        secondSpinner = testBinding.phase2;
        thirdSpinner = testBinding.phase3;

        callApi = new CallApi(TestActivity.this, spinnerPos1, adapter, myString);
        callApi.firstSpinnerPhrase(firstSpinner);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                secondSpinner.setVisibility(View.VISIBLE);
                callApi = new CallApi(TestActivity.this, spinnerPos2, adapter2, myString);

                Log.d(TAG, "posValue: " + spinnerPos2.get(position).getID());

                callApi.secondSpinnerPhrase(position, secondSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                thirdSpinner.setVisibility(View.VISIBLE);
                callApi = new CallApi(TestActivity.this, spinnerPos2, adapter2, myString);

                Log.d(TAG, "posValue: " + spinnerPos2.get(position).getID());

                callApi.secondSpinnerPhrase(position, secondSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


}