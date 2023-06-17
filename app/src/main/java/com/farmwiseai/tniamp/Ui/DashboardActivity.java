package com.farmwiseai.tniamp.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Ui.Fragment.TNAUFragment;
import com.farmwiseai.tniamp.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        setContentView(binding.getRoot());


        binding.naviTnau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddFragment(new TNAUFragment());
            }
        });
    }



    private void setAddFragment(Fragment addFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_navigation,addFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navi_tnau:
                setAddFragment(new TNAUFragment());
                break;
        }
    }
}