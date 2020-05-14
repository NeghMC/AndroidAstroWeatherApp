package com.example.astroweather2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.astrocalculator.AstroCalculator;
import com.example.astroweather2.ApplicationViewModel;
import com.example.astroweather2.R;
import com.example.astroweather2.databinding.FragmentSunBinding;
import com.example.astroweather2.utils.DateManager;

import java.text.DecimalFormat;

public class SunFragment extends Fragment {
    FragmentSunBinding mBind;
    ApplicationViewModel appData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_sun, container, false);
        return mBind.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appData = new ViewModelProvider(getActivity()).get(ApplicationViewModel.class);
        appData.getAstronomicData().observe(getViewLifecycleOwner(), new Observer<AstroCalculator>() {
            @Override
            public void onChanged(AstroCalculator astroCalculator) {
                DecimalFormat decimalFormat = new DecimalFormat("0.##");

                mBind.civilDawn.setText(
                        DateManager.AstroTimeToString(astroCalculator.getSunInfo().getTwilightEvening())
                );
                mBind.sunriseAzimuth.setText(
                        new String(decimalFormat.format(astroCalculator.getSunInfo().getAzimuthRise()) + "\u00B0")
                );
                mBind.sunriseTime.setText(
                        DateManager.AstroTimeToString(astroCalculator.getSunInfo().getSunrise())
                );
                mBind.sunsetAzimuth.setText(
                        new String(decimalFormat.format(astroCalculator.getSunInfo().getAzimuthSet()) + "\u00B0")
                );
                mBind.sunsetTime.setText(
                        DateManager.AstroTimeToString(astroCalculator.getSunInfo().getSunset())
                );
                mBind.twilightSun.setText(
                        DateManager.AstroTimeToString(astroCalculator.getSunInfo().getTwilightMorning())
                );
            }
        });
    }
}
