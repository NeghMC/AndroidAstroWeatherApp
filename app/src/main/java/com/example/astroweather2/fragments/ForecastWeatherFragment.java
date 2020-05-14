package com.example.astroweather2.fragments;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.astroweather2.ApplicationViewModel;
import com.example.astroweather2.Consts;
import com.example.astroweather2.R;
import com.example.astroweather2.databinding.FragmentForecastBinding;
import com.example.astroweather2.datatypes.WeatherData;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.Date;

public class ForecastWeatherFragment extends Fragment {
    private FragmentForecastBinding mBind;
    private ApplicationViewModel appData;

    public ForecastWeatherFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false);
        return mBind.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appData = new ViewModelProvider(getActivity()).get(ApplicationViewModel.class);
        appData.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(WeatherData weatherData) {
                DecimalFormat decimalFormat = new DecimalFormat("0");

                mBind.first.day.setText(weatherData.forecatsDays[0].date);
                mBind.first.tempMax.setText(weatherData.forecatsDays[0].maxTemp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.first.tempMin.setText(weatherData.forecatsDays[0].minTemp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.first.humidity.setText(decimalFormat.format(weatherData.forecatsDays[0].humidity) + "%");
                mBind.first.image.setImageBitmap(weatherData.forecatsDays[0].icon);

                mBind.second.day.setText(weatherData.forecatsDays[1].date);
                mBind.second.tempMax.setText(weatherData.forecatsDays[1].maxTemp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.second.tempMin.setText(weatherData.forecatsDays[1].minTemp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.second.humidity.setText(decimalFormat.format(weatherData.forecatsDays[1].humidity) + "%");
                mBind.second.image.setImageBitmap(weatherData.forecatsDays[1].icon);

                mBind.third.day.setText(weatherData.forecatsDays[2].date);
                mBind.third.tempMax.setText(weatherData.forecatsDays[2].maxTemp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.third.tempMin.setText(weatherData.forecatsDays[2].minTemp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.third.humidity.setText(decimalFormat.format(weatherData.forecatsDays[2].humidity) + "%");
                mBind.third.image.setImageBitmap(weatherData.forecatsDays[2].icon);
            }
        });
    }
}
