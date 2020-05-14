package com.example.astroweather2.fragments;

import android.annotation.SuppressLint;
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

import com.example.astroweather2.ApplicationViewModel;
import com.example.astroweather2.Consts;
import com.example.astroweather2.R;
import com.example.astroweather2.datatypes.WeatherData;
import com.example.astroweather2.databinding.FragmentCurrentWeatherBinding;

public class CurrentWeatherFragment extends Fragment {
    private FragmentCurrentWeatherBinding mBind;
    private ApplicationViewModel appData;

    public CurrentWeatherFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_current_weather, container, false);
        return mBind.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appData = new ViewModelProvider(getActivity()).get(ApplicationViewModel.class);
        appData.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherData>() {
            String noInfo = "No info";
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(WeatherData weatherData) {
                if(weatherData == null) return;
                if(weatherData.current.icon != null)
                    mBind.weatherImg.setImageBitmap(weatherData.current.icon);
                mBind.cityName.setText(weatherData.location.name == null ? noInfo : weatherData.location.name);
                mBind.location.setText(weatherData.location.lat + "N " + weatherData.location.lon + "E");
                mBind.description.setText(weatherData.current.condition == null ? noInfo : weatherData.current.condition);
                mBind.temperature.setText(weatherData.current.temp + (weatherData.unit == Consts.UNIT_METRICS ?  "\u2103" : "\u2109"));
                mBind.humidity.setText(weatherData.current.humidity + "%");
                mBind.pressure.setText(weatherData.current.pressure + (weatherData.unit == Consts.UNIT_METRICS ? " hPa" : " in"));
                mBind.wind.setText(weatherData.current.windDir + " " + weatherData.current.wind + (weatherData.unit == Consts.UNIT_METRICS ? "Kph" : "Mph"));
            }
        });
    }
}
