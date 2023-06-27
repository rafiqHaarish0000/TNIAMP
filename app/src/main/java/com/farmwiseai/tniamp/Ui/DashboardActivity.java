package com.farmwiseai.tniamp.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Ui.Fragment.AEDFragment;
import com.farmwiseai.tniamp.Ui.Fragment.AboutFragment;
import com.farmwiseai.tniamp.Ui.Fragment.AgricultureFragment;
import com.farmwiseai.tniamp.Ui.Fragment.AnimalFragment;
import com.farmwiseai.tniamp.Ui.Fragment.FisheriesFragment;
import com.farmwiseai.tniamp.Ui.Fragment.HorticultureFragment;
import com.farmwiseai.tniamp.Ui.Fragment.MarketingFragment;
import com.farmwiseai.tniamp.Ui.Fragment.TNAUFragment;
import com.farmwiseai.tniamp.Ui.Fragment.WRDFragment;
import com.farmwiseai.tniamp.databinding.ActivityDashboardBinding;
import com.farmwiseai.tniamp.mainView.MobileValidationActivity;
import com.farmwiseai.tniamp.mainView.VerifyMobileNumberActivitiy;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDashboardBinding binding;
    CommonFunction mCommonFunction;
    String username,lineDeptId;
    int countData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        setContentView(binding.getRoot());
         username=  SharedPrefsUtils.getString(DashboardActivity.this,SharedPrefsUtils.PREF_KEY.USER_NAME);
        lineDeptId=  SharedPrefsUtils.getString(DashboardActivity.this,SharedPrefsUtils.PREF_KEY.USER_DETAILS);
lineDeptId="1";
        binding.txtUserName.setText("Welcome "+username);
        mCommonFunction = new CommonFunction(DashboardActivity.this);
        showDept(lineDeptId);
        binding.naviTnau.setOnClickListener(this);
        binding.naviAgri.setOnClickListener(this);
        binding.naviHorti.setOnClickListener(this);
        binding.naviAed.setOnClickListener(this);
        binding.naviAnimal.setOnClickListener(this);
        binding.naviWrd.setOnClickListener(this);
        binding.naviMarketing.setOnClickListener(this);
        binding.navFish.setOnClickListener(this);
        binding.aboutImage.setOnClickListener(this);
        binding.logoutIcon.setOnClickListener(this);


    }

    private void showDept(String lineDeptId) {
        if(lineDeptId=="1")
        {
            binding.naviTnau.setEnabled(true);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);


        } else if(lineDeptId=="2")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(true);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);


        }else if(lineDeptId=="3")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(true);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);


        }
        else if(lineDeptId=="4")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(true);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);


        }
        else if(lineDeptId=="5")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(true);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);


        }
        else if(lineDeptId=="6")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(true);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);


        }
        else if(lineDeptId=="7")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(true);
            binding.navFish.setEnabled(false);


        }else if(lineDeptId=="8")
        {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(true);

        }else
        {
            binding.naviTnau.setEnabled(true);
            binding.naviAgri.setEnabled(true);
            binding.naviHorti.setEnabled(true);
            binding.naviAed.setEnabled(true);
            binding.naviAnimal.setEnabled(true);
            binding.naviWrd.setEnabled(true);
            binding.naviMarketing.setEnabled(true);
            binding.navFish.setEnabled(true);
            binding.aboutImage.setEnabled(true);
            binding.logoutIcon.setEnabled(true);

        }

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
            case R.id.navi_aed:
                setAddFragment(new AEDFragment());
                break;
            case R.id.navi_horti:
                setAddFragment(new HorticultureFragment());
                break;
            case R.id.navi_animal:
                setAddFragment(new AnimalFragment());
                break;
            case R.id.navi_wrd:
                setAddFragment(new WRDFragment());
                break;
            case R.id.navi_marketing:
                setAddFragment(new MarketingFragment());
                break;
            case R.id.nav_fish:
                setAddFragment(new FisheriesFragment());
                break;
            case R.id.about_image:
                setAddFragment(new AboutFragment());
                break;
            case R.id.logout_icon:
                showMessageOKCancel("Are you sure want to Logout.?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPrefsUtils.clearAllPrefs(DashboardActivity.this);
                        mCommonFunction.navigation(DashboardActivity.this, MobileValidationActivity.class);
                        dialogInterface.dismiss();
                    }
                });
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashboardActivity.this)
                .setMessage(message)
                .setPositiveButton("Yes", okListener)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                    }
                })
                .setTitle("Logout")
                .create()
                .show();
    }

    private void checkOfflineDataPresent() {
        if (mCommonFunction.isNetworkAvailable() == true) {
            //data should send to api and notification should be cleared accordingly
            String checkDataIsPresentOrNot = SharedPrefsUtils.getString(DashboardActivity.this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
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
            String checkDataIsPresentOrNot = SharedPrefsUtils.getString(DashboardActivity.this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
            if (checkDataIsPresentOrNot.length() != 0) {
                binding.notificationBadge.setVisibility(View.VISIBLE);
            }
        }
    }


}