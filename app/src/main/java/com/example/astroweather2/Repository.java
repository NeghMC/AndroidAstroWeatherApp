package com.example.astroweather2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.util.Log;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.astroweather2.datatypes.GeoLocation;
import com.example.astroweather2.datatypes.WeatherData;
import com.example.astroweather2.room.ApplicationDatabase;
import com.example.astroweather2.room.LocationDao;
import com.example.astroweather2.utils.DateManager;
import com.example.astroweather2.utils.NullableArrayBlockingQueue;
import com.example.astroweather2.utils.ResultCallback;
import com.example.astroweather2.utils.ResultCallbackWithHandler;
import com.example.astroweather2.webservice.WeatherApiService;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Repository {

    private LocationDao locationDao;
    private Context application;
    public static final String TAG = "reposytory";

    public static final String CURRENT_WEATHER_FILE_NAME = "CurrentWeather";

    public Repository(Context context) {
        locationDao = ApplicationDatabase.getInstance().locationDao(); // now we can use DAO
        this.application = context;
    }

    public void refreshWeatherData(final int id, final int unit, final ResultCallbackWithHandler<WeatherData> resultCallback) {
        new Thread(new Runnable() {
            NullableArrayBlockingQueue<Object> dataQueue = new NullableArrayBlockingQueue<>(6);

            @Override
            public void run() {
                GeoLocation location = ApplicationDatabase.getInstance().locationDao().getById(id); // get locaton
                if(location == null) {
                    resultCallback.notifyFailture("There is no location with id "+ id + " in database");
                    return;
                }

                WeatherApiService.getWeatherData(location.latitude, location.longitude, unit, new ResultCallback<WeatherData>() {
                    @Override
                    public void onSuccess(WeatherData result, String warning) {
                        dataQueue.add(result);
                    }
                    @Override
                    public void onFailture(String msg) {
                        Log.d(TAG, "onFailture: " + msg);
                        dataQueue.add(null);
                    }
                });

                WeatherData weatherData = (WeatherData)dataQueue.take();
                if(weatherData == null) {
                    weatherData = loadWeatherFromPersistentStorage();
                    if(weatherData == null)
                        resultCallback.notifyFailture("Can not connect to the server. Also there is no saved data in hard drive");
                    else
                        resultCallback.notifySuccess(weatherData, "Can not connect to the server");
                    return;
                }

                WeatherApiService.getWeatherIcon(weatherData.current.conditionIconUrl, new ResultCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap result, String warning) {
                        dataQueue.add(result);
                    }

                    @Override
                    public void onFailture(String msg) {
                        dataQueue.add(null);
                    }
                });

                WeatherApiService.getWeatherIcon(weatherData.forecatsDays[0].iconUrl, new ResultCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap result, String warning) {
                        dataQueue.add(result);
                    }

                    @Override
                    public void onFailture(String msg) {
                        dataQueue.add(null);
                    }
                });

                WeatherApiService.getWeatherIcon(weatherData.forecatsDays[1].iconUrl, new ResultCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap result, String warning) {
                        dataQueue.add(result);
                    }

                    @Override
                    public void onFailture(String msg) {
                        dataQueue.add(null);
                    }
                });

                WeatherApiService.getWeatherIcon(weatherData.forecatsDays[2].iconUrl, new ResultCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap result, String warning) {
                        dataQueue.add(result);
                    }

                    @Override
                    public void onFailture(String msg) {
                        dataQueue.add(null);
                    }
                });

                Bitmap icon = (Bitmap)dataQueue.take();
                if(icon == null) {
                    resultCallback.notifyFailture("Could not download icon");
                } else {
                    weatherData.current.icon = icon;
                    for (int i = 0; i < 3; i++) {
                        icon = (Bitmap)dataQueue.take();
                        weatherData.forecatsDays[i].icon = icon;
                    }
                    resultCallback.notifySuccess(weatherData, null);
                    saveWeatherToPersistentStorage(weatherData);
                }

            }
        }).start();
    }

    private WeatherData loadWeatherFromPersistentStorage() {
        return WeatherData.loadFromFile(application, CURRENT_WEATHER_FILE_NAME);
    }

    private void saveWeatherToPersistentStorage(WeatherData weatherData) {
        weatherData.saveToFile(application, CURRENT_WEATHER_FILE_NAME);
    }

    public void refreshAstronomicData(final int id, final ResultCallbackWithHandler<AstroCalculator> resultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GeoLocation location = ApplicationDatabase.getInstance().locationDao().getById(id);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getDefault());
                AstroCalculator astroCalculator = new AstroCalculator(
                        DateManager.convertToAstroDate(calendar),
                        new AstroCalculator.Location(
                                location.latitude,
                                location.longitude
                        )
                );
                resultCallback.notifySuccess(astroCalculator, null);
            }
        }).start();
    }
}


