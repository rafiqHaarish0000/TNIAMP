package com.farmwiseai.tniamp.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Ui.Fragment.AgricultureFragment;
import com.farmwiseai.tniamp.Ui.Fragment.TNAUFragment;
import com.farmwiseai.tniamp.databinding.ActivityDashboardBinding;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDashboardBinding binding;
    CommonFunction mCommonFunction;
    int countData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        setContentView(binding.getRoot());

        mCommonFunction = new CommonFunction(DashboardActivity.this);
binding.naviAgri.setOnClickListener(this);
        binding.naviTnau.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setAddFragment(new TNAUFragment());
            }
        });
    }



    private void setAddFragment(Fragment addFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_navigation, addFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navi_tnau:
                setAddFragment(new TNAUFragment());
                break;
            case R.id.navi_agri:
                setAddFragment(new AgricultureFragment());
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashboardActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    private void checkOfflineDataPresent() {
        if (mCommonFunction.isNetworkAvailable() == true) {
            //data should send to api and notification should be cleared accordingly
            String checkDataIsPresentOrNot = SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
            if (checkDataIsPresentOrNot.length() != 0) {
                showMessageOKCancel("Please update the offline data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //fetch all data to backend api and clear sharedpref
                        binding.notificationBadge.setVisibility(View.VISIBLE);
                        SharedPrefsUtils.clearAllPrefs(DashboardActivity.this);
                        dialogInterface.dismiss();
                    }
                });
            } else {
                binding.notificationBadge.setVisibility(View.GONE);
            }
        } else {
            String checkDataIsPresentOrNot = SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
            if (checkDataIsPresentOrNot.length() != 0) {
                binding.notificationBadge.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
         finish();
        super.onBackPressed();
    }
}