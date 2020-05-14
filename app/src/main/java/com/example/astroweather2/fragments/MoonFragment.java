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
import com.example.astroweather2.databinding.FragmentMoonBinding;
import com.example.astroweather2.room.ApplicationDatabase;
import com.example.astroweather2.utils.DateManager;

import java.text.DecimalFormat;

public class MoonFragment extends Fragment {
    private FragmentMoonBinding mBind;
    private ApplicationViewModel appData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_moon, container, false);
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
                mBind.moonrise.setText(
                        DateManager.AstroTimeToString(astroCalculator.getMoonInfo().getMoonrise())
                );
                mBind.moonset.setText(
                        DateManager.AstroTimeToString(astroCalculator.getMoonInfo().getMoonset())
                );
                mBind.newMoon.setText(
                        DateManager.AstroDateToString(astroCalculator.getMoonInfo().getNextNewMoon())
                );
                mBind.fullMoon.setText(
                        DateManager.AstroDateToString(astroCalculator.getMoonInfo().getNextFullMoon())
                );
                mBind.phase.setText(
                        new String(decimalFormat.format(astroCalculator.getMoonInfo().getIllumination() * 100) + '\u0025')
                );
                mBind.syndodic.setText(
                        new String(decimalFormat.format(astroCalculator.getMoonInfo().getAge()/365*29.66))
                );
            }
        });
    }
}
