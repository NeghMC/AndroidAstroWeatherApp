package com.example.astroweather2.webservice;

import android.graphics.Bitmap;
import android.icu.util.Freezable;
import android.webkit.GeolocationPermissions;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.astroweather2.datatypes.GeoLocation;
import com.example.astroweather2.datatypes.WeatherData;
import com.example.astroweather2.utils.ResultCallback;
import com.example.astroweather2.utils.ResultCallbackWithHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherApiService {
    public static final String API_KEY = "368561c7651f493a8f2130733201005";

    private static String getCityListUrl(String sequence) {
        return "http://api.weatherapi.com/v1/search.json?key=" + API_KEY + "&q=" + sequence;
    }

    private static String getWeatherUrl(double latitude, double longitude) {
        return "http://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + latitude + "," + longitude + "&days=3";
    }

    public static void getSearchList(String sequence, final ResultCallback<ArrayList<GeoLocation>> callback) {
        WebServiceQueue.getInstance().sendRequest(
                new JsonArrayRequest(Request.Method.GET, getCityListUrl(sequence), null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<GeoLocation> locations = new ArrayList<>(response.length());
                                for(int iterator = 0; iterator < response.length(); ++iterator)
                                    locations.add(new GeoLocation().toGeoLocation(response.optJSONObject(iterator)));
                                callback.onSuccess(locations, null);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callback.onFailture(error.getMessage());
                            }
                        }
                )
        );

        /*
        [
           {
              "id":2801268,
              "name":"London, City of London, Greater London, United Kingdom",
              "region":"City of London, Greater London",
              "country":"United Kingdom",
              "lat":51.52,
              "lon":-0.11,
              "url":"london-city-of-london-greater-london-united-kingdom"
           }
        ]
         */

    }

    public static void getWeatherData(double latitude, double longitude, final int unit, final ResultCallback<WeatherData> resultCallback) {
        WebServiceQueue.getInstance().sendRequest(new JsonObjectRequest(Request.Method.GET, getWeatherUrl(latitude, longitude), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        WeatherData weatherData = new WeatherData().parseFromJson(response, unit);
                        if(weatherData == null)
                            resultCallback.onFailture("Weather is null");
                        resultCallback.onSuccess(weatherData, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        resultCallback.onFailture(error.getMessage());
                    }
                }));
    }

    public static void getWeatherIcon(String url, final ResultCallback<Bitmap> resultCallback) {
        WebServiceQueue.getInstance().sendRequest(new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                    resultCallback.onSuccess(response, null);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    resultCallback.onFailture(error.getMessage());
            }
        }));
    }
}
