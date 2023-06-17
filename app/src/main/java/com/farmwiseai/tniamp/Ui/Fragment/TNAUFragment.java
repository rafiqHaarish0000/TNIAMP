package com.farmwiseai.tniamp.Ui.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.farmwiseai.TestActivity;
import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
import com.farmwiseai.tniamp.Retrofit.Request.ListOfTNAU;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentTNAUBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TNAUFragment extends Fragment implements View.OnClickListener {
    FragmentTNAUBinding tnauBinding;
    private Context context;
    private String phases, sub_basin, district, block, village, component, sub_components, farmerName, category, survey_no, area, near_tank, remarks, date;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tnauBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_t_n_a_u, container, false);


        tnauBinding.popBackImage.setOnClickListener(this);
        tnauBinding.submissionBtn.setOnClickListener(this);
        tnauBinding.takePic.setOnClickListener(this);

        phases = tnauBinding.phaseTxt.getText().toString();
        sub_basin = tnauBinding.subBasinTxt.getText().toString();
        district = tnauBinding.districtTxt.getText().toString();
        village = tnauBinding.villageTxt.getText().toString();
        component = tnauBinding.componentTxt.getText().toString();
        sub_components = tnauBinding.subComponentsTxt.getText().toString();
        farmerName = tnauBinding.farmerTxt.getText().toString();
        category = tnauBinding.categoryTxt.getText().toString();
        survey_no = tnauBinding.surveyTxt.getText().toString();
        area = tnauBinding.areaTxt.getText().toString();
        near_tank = tnauBinding.tankTxt.getText().toString();
        remarks = tnauBinding.remarksTxt.getText().toString();
        date = tnauBinding.dateTxt.getText().toString();

        return tnauBinding.getRoot();

    }

    private boolean fieldValidation(String phases, String sub_basin,
                                    String district, String village, String component, String sub_components, String farmerName, String category,
                                    String survey_no, String area, String near_tank, String remarks, String date) {


        if (phases.length() == 0) {
            tnauBinding.phaseTxt.setError("Phases not found");
            return false;
        } else if (sub_basin.length() == 0) {
            tnauBinding.subBasinTxt.setError("Sub basin not found");
            return false;
        } else if (district.length() == 0) {
            tnauBinding.districtTxt.setError("Sub basin not found");
            return false;
        } else if (village.length() == 0) {
            tnauBinding.villageTxt.setError("Sub basin not found");
            return false;
        } else if (component.length() == 0) {
            tnauBinding.componentTxt.setError("Sub basin not found");
            return false;
        } else if (sub_components.length() == 0) {
            tnauBinding.subComponentsTxt.setError("Sub basin not found");
            return false;
        } else if (farmerName.length() == 0) {
            tnauBinding.farmerTxt.setError("Sub basin not found");
            return false;
        } else if (category.length() == 0) {
            tnauBinding.categoryTxt.setError("Sub basin not found");
            return false;
        } else if (survey_no.length() == 0) {
            tnauBinding.surveyTxt.setError("Sub basin not found");
            return false;
        } else if (area.length() == 0) {
            tnauBinding.areaTxt.setError("Sub basin not found");
            return false;
        } else if (near_tank.length() == 0) {
            tnauBinding.tankTxt.setError("Sub basin not found");
            return false;
        } else if (remarks.length() == 0) {
            tnauBinding.remarksTxt.setError("Sub basin not found");
            return false;
        } else if (date.length() == 0) {
            tnauBinding.dateTxt.setError("Sub basin not found");
            return false;
        }


        return true;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            tnauBinding.pic1.setImageBitmap(photo);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_back_image:
                Intent intent = new Intent(context, DashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.submission_btn:
                boolean checkValidaiton = fieldValidation(phases, sub_basin, district, village, component, sub_components, farmerName,
                        category, survey_no, area, near_tank, remarks, date);
                if (!checkValidaiton) {
                    Toast.makeText(context, "Data not found.!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Data saved successfully.!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.take_pic:
                Intent i = new Intent(getActivity(), TestActivity.class);
                startActivity(i);
                break;
        }
    }


    private void getTnauList() {
        Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
        Call<List<ListOfTNAU>> userDataCall = call.getAllData();

        userDataCall.enqueue(new Callback<List<ListOfTNAU>>() {
            @Override
            public void onResponse(Call<List<ListOfTNAU>> call, Response<List<ListOfTNAU>> response) {
                if (response.body() != null) {
                    response.body();
                    Log.i(TAG, "onResponse: " + response.body());
                } else {
                    Log.i(TAG, "onResponse: " + "no data found..!");
                }
            }

            @Override
            public void onFailure(Call<List<ListOfTNAU>> call, Throwable t) {

            }
        });
    }

}