package com.farmwiseai.tniamp.Ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.Toast;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.BaseApi;
import com.farmwiseai.tniamp.Retrofit.DataClass.GetUserCountData;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AEDRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.Agri_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.AnimalRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.FishRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.HortiRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.MarkRequest;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.TNAU_Request;
import com.farmwiseai.tniamp.Retrofit.DataClass.RequestData.WRDRequest;
import com.farmwiseai.tniamp.Retrofit.Interface_Api;
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
import com.farmwiseai.tniamp.mainView.GPSTracker;
import com.farmwiseai.tniamp.mainView.MobileValidationActivity;
import com.farmwiseai.tniamp.utils.BaseActivity;
import com.farmwiseai.tniamp.utils.CommonFunction;
import com.farmwiseai.tniamp.utils.OfflineDataSyncFile;
import com.farmwiseai.tniamp.utils.PermissionUtils;
import com.farmwiseai.tniamp.utils.SharedPrefsUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {
    ActivityDashboardBinding binding;
    CommonFunction mCommonFunction;
    String username, lineDeptId;
    int countData;
    private GPSTracker gpsTracker;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProgressDialog progressDialog;

    GetUserCountData getUserCountData;
    String notifiCount;
    Bundle extras;
    int count = 0;
    int doubleBackToExitPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        setContentView(binding.getRoot());
        username = SharedPrefsUtils.getString(DashboardActivity.this, SharedPrefsUtils.PREF_KEY.USER_NAME);
        lineDeptId = SharedPrefsUtils.getString(DashboardActivity.this, SharedPrefsUtils.PREF_KEY.USER_DETAILS);
        //  lineDeptId = "2";
        binding.txtUserName.setText("Welcome " + username);
        mCommonFunction = new CommonFunction(DashboardActivity.this);
        showDept(lineDeptId);

        if(!PermissionUtils.isLocationEnabled(DashboardActivity.this)){
            showMessageOKLocationPermission("Please Enable Location Services In Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent location_Intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(location_Intent);
                    finish();
                }
            });
        }

        getLocation();
        SharedPrefsUtils.putInt(getApplicationContext(), SharedPrefsUtils.PREF_KEY.BACK_PRESSED, 0);
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
        binding.imageView4.setOnClickListener(this);
        binding.frameLayout.setOnClickListener(this);
        extras = getIntent().getExtras();
        if (extras != null) {
            binding.countValText.setText(extras.getString("count"));
        }
        this.runOnUiThread(new Runnable() {
            public void run() {

                //  syncOfflineData();
                if (mCommonFunction.isNetworkAvailable()) {
                    String count = OfflineDataSyncFile.offLineCount(lineDeptId);
                    if (Integer.parseInt(count) > 0) {
                        OfflineDataSynProgressDialog dataSynProgressDialog = new OfflineDataSynProgressDialog(DashboardActivity.this);
                        dataSynProgressDialog.execute();

                    }
                }
            }
        });
    }


    private void getUserCount() {

        if (!mCommonFunction.isNetworkAvailable()) {
            binding.sentTxt.setText(SharedPrefsUtils.getString(getApplicationContext(), SharedPrefsUtils.PREF_KEY.ONLINE_DATA_COUNT));
            binding.countValText.setText("No of Offline Data: " + OfflineDataSyncFile.offLineCount(lineDeptId));

        } else {
            try {
                mCommonFunction.showProgress();
                binding.countValText.setText("No of Offline Data: " + OfflineDataSyncFile.offLineCount(lineDeptId));
                Interface_Api call = BaseApi.getUrlApiCall().create(Interface_Api.class);
                Call<GetUserCountData> userDataCall = null;
                userDataCall = call.getUserCount(SharedPrefsUtils.getString(getApplicationContext(), SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN), SharedPrefsUtils.getString(getApplicationContext(), SharedPrefsUtils.PREF_KEY.USER_DETAILS));
                userDataCall.enqueue(new Callback<GetUserCountData>() {
                    @Override
                    public void onResponse(Call<GetUserCountData> call, Response<GetUserCountData> response) {
                        if (response.body() != null) {
                            getUserCountData = response.body();
                            Log.i(TAG, "onBody: " + response.code());
                            String responsemsg = getUserCountData.getResponseMessage().getGeoTagCount().toString();
                            if (responsemsg != null) {
                                notifiCount = getUserCountData.getResponseMessage().getGeoTagCount().toString();
                                String msg = ("No of data submit: " + notifiCount);
                                SharedPrefsUtils.putString(getApplicationContext(), SharedPrefsUtils.PREF_KEY.ONLINE_DATA_COUNT, msg);
                                binding.sentTxt.setText("No of data submit: " + notifiCount);
                                //  mLoadCustomToast(getParent(), notifiCount);

                            } else {
                                mLoadCustomToast(getParent(), getUserCountData.getResponseMessage().getResponse().toString());
                            }

                        }
                        mCommonFunction.hideProgress();
                    }

                    @Override
                    public void onFailure(Call<GetUserCountData> call, Throwable t) {
                        mCommonFunction.hideProgress();
                    }
                });


            } catch (Exception e) {
                mLoadCustomToast(getParent(), "Exception Caught");
            }
        }
    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void syncOfflineData() {
        if (mCommonFunction.isNetworkAvailable()) {
            String count = OfflineDataSyncFile.offLineCount(lineDeptId);
            // String count = OfflineDataSyncFile.offLineCount(SharedPrefsUtils.getString(getApplicationContext(), SharedPrefsUtils.PREF_KEY.USER_DETAILS));
            if (Integer.parseInt(count) > 0) {
                //  syncOfflineLogic();
            }
        }
    }

    private void clearDataSync(String lineDeptId) {
        switch (lineDeptId) {
            case "1":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                break;
            case "2":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AGRI);
                break;
            case "3":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);
                break;
            case "4":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_ANI);
                break;
            case "5":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_HORTI);
                break;
            case "6":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);
                break;
            case "7":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_MARKETING);
                break;
            case "8":
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_FISH);
                break;

            default:
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AGRI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_ANI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_HORTI);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_MARKETING);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);
                SharedPrefsUtils.clearStringPrefs(this, SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_FISH);
                break;
        }
    }


    @SuppressLint("NewApi")
    private void showDept(String lineDeptId) {
        if (lineDeptId.equalsIgnoreCase("1")) {
            binding.naviTnau.setEnabled(true);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);
            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
        } else if (lineDeptId.equalsIgnoreCase("2")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(true);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);
            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
        } else if (lineDeptId.equalsIgnoreCase("3")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(true);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);

            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));

        } else if (lineDeptId.equalsIgnoreCase("4")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(true);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);

            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));


        } else if (lineDeptId.equalsIgnoreCase("5")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(true);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);

            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));

        } else if (lineDeptId.equalsIgnoreCase("6")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(true);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(false);

            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));


        } else if (lineDeptId.equalsIgnoreCase("7")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(true);
            binding.navFish.setEnabled(false);

            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));

        } else if (lineDeptId.equalsIgnoreCase("8")) {
            binding.naviTnau.setEnabled(false);
            binding.naviAgri.setEnabled(false);
            binding.naviHorti.setEnabled(false);
            binding.naviAed.setEnabled(false);
            binding.naviAnimal.setEnabled(false);
            binding.naviWrd.setEnabled(false);
            binding.naviMarketing.setEnabled(false);
            binding.navFish.setEnabled(true);
            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.transparent));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));

        } else {
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
            binding.naviTnau.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviAgri.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviHorti.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviAed.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviAnimal.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviWrd.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.naviMarketing.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));
            binding.navFish.setBackgroundTintList(this.getResources().getColorStateList(R.color.white));

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
                        Intent intent = new Intent(DashboardActivity.this, MobileValidationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                });
                break;
            //notification count onclick events
            case R.id.imageView4:
                getUserCount();
                binding.notificationBadgeCount.setVisibility(View.VISIBLE);
                count = count + 1;
                Log.i(TAG, "onClickNotificationBadger " + true);
                if (count == 1) {
                    binding.notificationBadgeCount.setVisibility(View.VISIBLE);
                    binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.active_notification_icon));
                } else {
                    binding.notificationBadgeCount.setVisibility(View.GONE);
                    binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.notification));
                    count = 0;
                }
                break;
            case R.id.frameLayout:
                binding.notificationBadgeCount.setVisibility(View.GONE);
                binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.notification));
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashboardActivity.this).setMessage(message).setPositiveButton("Yes", okListener).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialogInterface.cancel();
                    }
                }).setTitle("TNIAMP").create().show();
    }

    private void showMessageOKLocationPermission(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashboardActivity.this).setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setTitle("Allow Permission").create().show();
    }


    private void getLocation() {
        if (PermissionUtils.checkPermission(getApplicationContext()) == false) {
            PermissionUtils.requestPermission(this);
        }
        gpsTracker = new GPSTracker(this);
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                gpsTracker.getLatitude();
                gpsTracker.getLongitude();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermission();
                                    }
                                }
                            });
                        }
                    }
                }
                break;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onBackPressed() {
        doubleBackToExitPressed = SharedPrefsUtils.getInt(getApplicationContext(), SharedPrefsUtils.PREF_KEY.BACK_PRESSED);
        if (doubleBackToExitPressed == 1) {
            doubleBackToExitPressed++;
            SharedPrefsUtils.putInt(getApplicationContext(), SharedPrefsUtils.PREF_KEY.BACK_PRESSED, doubleBackToExitPressed);
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            finish();
            super.onBackPressed();
        }
    }

    //offline data sync progress dialog
    class OfflineDataSynProgressDialog extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public OfflineDataSynProgressDialog(DashboardActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait until data sync...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                mLoadCustomToast(getParent(), " Data Synced successfully");
                clearDataSync(lineDeptId);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
          /*  try {

            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            String current = "failure";
            try {
                if (lineDeptId.equalsIgnoreCase("1")) {
                    ArrayList<TNAU_Request> tnauRequests = SharedPrefsUtils.getArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                    ArrayList<String> tnauImageReq = SharedPrefsUtils.getArrayListImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);

                    if (tnauRequests != null && tnauRequests.size() > 0) {
                        for (int i = 0; i < tnauRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataTnauUpload(tnauRequests.get(i), tnauImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId.equalsIgnoreCase("2")) {
                    ArrayList<Agri_Request> agri_requests = SharedPrefsUtils.getAgriArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);
                    ArrayList<String> agriImageReq = SharedPrefsUtils.getArrayListagriImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AGRI);

                    if (agri_requests != null && agri_requests.size() > 0) {
                        for (int i = 0; i < agri_requests.size(); i++) {
                            OfflineDataSyncFile.onlineDataAgriUpload(agri_requests.get(i), agriImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId.equalsIgnoreCase("3")) {
                    ArrayList<HortiRequest> aedRequests = SharedPrefsUtils.getHortiArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListHortiImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_HORTI);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataHortiUpload(aedRequests.get(i), aedImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId.equalsIgnoreCase("4")) {
                    ArrayList<AEDRequest> aedRequests = SharedPrefsUtils.getAEDArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListagriImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataAEDUpload(aedRequests.get(i), aedImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId == "5") {
                    ArrayList<AnimalRequest> aedRequests = SharedPrefsUtils.getARDArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListAhdImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_ANI);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataAnimalUpload(aedRequests.get(i), aedImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId == "6") {
                    ArrayList<WRDRequest> aedRequests = SharedPrefsUtils.getWrdArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListwrdImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataWrdUpload(aedRequests.get(i), aedImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId == "7") {
                    ArrayList<MarkRequest> aedRequests = SharedPrefsUtils.getMarkArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListMarkImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_MARKETING);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataMarketingUpload(aedRequests.get(i), aedImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else if (lineDeptId == "8") {
                    ArrayList<FishRequest> aedRequests = SharedPrefsUtils.getFishArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListFishImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_FISH);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataFisheriesUpload(aedRequests.get(i), aedImageReq.get(i), lineDeptId);
                        }
                        // return the data to onPostExecute method

                    }
                } else {
                    ArrayList<TNAU_Request> tnauRequests = SharedPrefsUtils.getArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA);
                    ArrayList<String> tnauImageReq = SharedPrefsUtils.getArrayListImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA);
                    if (tnauRequests != null && tnauRequests.size() > 0) {
                        for (int i = 0; i < tnauRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataTnauUpload(tnauRequests.get(i), tnauImageReq.get(i), "1");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<Agri_Request> agri_requests = SharedPrefsUtils.getAgriArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AGRI);
                    ArrayList<String> agriImageReq = SharedPrefsUtils.getArrayListagriImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AGRI);

                    if (agri_requests != null && agri_requests.size() > 0) {
                        for (int i = 0; i < agri_requests.size(); i++) {
                            OfflineDataSyncFile.onlineDataAgriUpload(agri_requests.get(i), agriImageReq.get(i), "2");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<HortiRequest> hortiRequests = SharedPrefsUtils.getHortiArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_HORTI);
                    ArrayList<String> hortiImageReq = SharedPrefsUtils.getArrayListHortiImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_HORTI);

                    if (hortiRequests != null && hortiRequests.size() > 0) {
                        for (int i = 0; i < hortiRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataHortiUpload(hortiRequests.get(i), hortiImageReq.get(i), "3");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<AEDRequest> aedRequests = SharedPrefsUtils.getAEDArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_AED);
                    ArrayList<String> aedImageReq = SharedPrefsUtils.getArrayListagriImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_AED);

                    if (aedRequests != null && aedRequests.size() > 0) {
                        for (int i = 0; i < aedRequests.size(); i++) {
                            OfflineDataSyncFile.onlineDataAEDUpload(aedRequests.get(i), aedImageReq.get(i), "4");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<AnimalRequest> aedRequests1 = SharedPrefsUtils.getARDArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_ANI);
                    ArrayList<String> aedImageReq1 = SharedPrefsUtils.getArrayListAhdImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_ANI);

                    if (aedRequests1 != null && aedRequests1.size() > 0) {
                        for (int i = 0; i < aedRequests1.size(); i++) {
                            OfflineDataSyncFile.onlineDataAnimalUpload(aedRequests1.get(i), aedImageReq1.get(i), "5");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<WRDRequest> aedRequests2 = SharedPrefsUtils.getWrdArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_WRD);
                    ArrayList<String> aedImageReq2 = SharedPrefsUtils.getArrayListwrdImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_WRD);

                    if (aedRequests2 != null && aedRequests2.size() > 0) {
                        for (int i = 0; i < aedRequests2.size(); i++) {
                            OfflineDataSyncFile.onlineDataWrdUpload(aedRequests2.get(i), aedImageReq2.get(i), "6");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<MarkRequest> aedRequests3 = SharedPrefsUtils.getMarkArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_MARKETING);
                    ArrayList<String> aedImageReq3 = SharedPrefsUtils.getArrayListMarkImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_MARKETING);

                    if (aedRequests3 != null && aedRequests3.size() > 0) {
                        for (int i = 0; i < aedRequests3.size(); i++) {
                            OfflineDataSyncFile.onlineDataMarketingUpload(aedRequests3.get(i), aedImageReq3.get(i), "7");
                        }
                        // return the data to onPostExecute method

                    }
                    ArrayList<FishRequest> aedRequests4 = SharedPrefsUtils.getFishArrayList(getApplicationContext(), SharedPrefsUtils.PREF_KEY.OFFLINE_DATA_FISH);
                    ArrayList<String> aedImageReq4 = SharedPrefsUtils.getArrayListFishImage(getApplicationContext(), SharedPrefsUtils.PREF_KEY.SAVED_OFFLINE_DATA_FISH);

                    if (aedRequests4 != null && aedRequests4.size() > 0) {
                        for (int i = 0; i < aedRequests4.size(); i++) {
                            OfflineDataSyncFile.onlineDataFisheriesUpload(aedRequests4.get(i), aedImageReq4.get(i), "8");
                        }
                        // return the data to onPostExecute method

                    }
                }
                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
                //   return "Exception: " + e.getMessage();
            }
            return null;
        }
    }
}
