package com.example.astroweather2;

import android.app.Activity;
import android.app.Application;
import android.net.sip.SipSession;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astrocalculator.AstroCalculator;
import com.example.astroweather2.datatypes.WeatherData;
import com.example.astroweather2.room.ApplicationDatabase;
import com.example.astroweather2.utils.ResultCallback;
import com.example.astroweather2.utils.ResultCallbackWithHandler;
import com.example.astroweather2.webservice.WebServiceQueue;

import org.json.JSONObject;

public class ApplicationViewModel extends AndroidViewModel { // in contrary to ViewModel, consist of application context

    // repository variable
    private Repository repository;

    private MutableLiveData<WeatherData> weatherDataLiveData = new MutableLiveData<>();
    private MutableLiveData<AstroCalculator> astroCalculatorLiveData = new MutableLiveData<>();

    public ApplicationViewModel(@NonNull Application application) {
        super(application);

        WebServiceQueue.initialise(application);
        ApplicationDatabase.initialise(application);
        repository = new Repository(application);
    }

    /* current weather data */
    public LiveData<WeatherData> getWeatherData() {
        return weatherDataLiveData;
    }

    public LiveData<AstroCalculator> getAstronomicData() {
        return astroCalculatorLiveData;
    }

    public void refreshWeatherData(int id, int unit) {
        repository.refreshWeatherData(id, unit, new ResultCallbackWithHandler<WeatherData>(new Handler()) {
            @Override
            public void onSuccess(WeatherData result, String msg) {
                weatherDataLiveData.setValue(result);
            }

            @Override
            public void onFailture(String msg) {
                Toast.makeText(getApplication(), "error: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshAstronomicData(int id) {
        repository.refreshAstronomicData(id, new ResultCallbackWithHandler<AstroCalculator>(new Handler()) {
            @Override
            protected void onSuccess(AstroCalculator result, String warning) {
                astroCalculatorLiveData.setValue(result);
            }

            @Override
            protected void onFailture(String msg) {
                Toast.makeText(getApplication(), "could not calculate astronomic data: " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
