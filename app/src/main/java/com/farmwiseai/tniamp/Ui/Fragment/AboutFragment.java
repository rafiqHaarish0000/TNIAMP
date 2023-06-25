package com.farmwiseai.tniamp.Ui.Fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Ui.DashboardActivity;
import com.farmwiseai.tniamp.databinding.FragmentAboutBinding;
import com.farmwiseai.tniamp.utils.CommonFunction;

public class AboutFragment extends Fragment {
FragmentAboutBinding aboutBinding;
CommonFunction commonFunction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        commonFunction = new CommonFunction(getActivity());
        aboutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        aboutBinding.popBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonFunction.navigation(getContext(), DashboardActivity.class);
            }
        });
        return aboutBinding.getRoot();
    }
}