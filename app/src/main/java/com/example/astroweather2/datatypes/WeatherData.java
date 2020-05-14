package com.example.astroweather2.datatypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.astroweather2.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.WatchEvent;

/*
public class WeatherData implements Serializable {
    public Current current = new Current();
    public Location location = new Location();

    public class Current {
        public int lastUpdatedEpoch;
        public String lastUpdated;
        public double tempC;
        public double tempF;
        public int isDay;
        public String condition;
        public String conditionIcon;
        public int conditionCode;
        public double windMph;
        public double windKph;
        public int windDegree;
        public String windDir;
        public double pressureMb;
        public double pressureIn;
        public double precipMm;
        public double precipIn;
        public int humidity;
        public int cloud;
        public double feelslikeC;
        public double feelslikeF;
        public double visKm;
        public double visMiles;
        public double uv;
        public double gustMph;
        public double gustKph;
        public Bitmap icon;
    }

    public class Location {
        public String name;
        public String region;
        public String country;
        public double lat;
        public double lon;
        public String tzId;
        public int localtimeEpoch;
        public String localtime;
    }

    public WeatherData parseFromJson(JSONObject jWeather) {
        JSONObject jCurrent = jWeather.optJSONObject("current");
        assert jCurrent != null : "Could not parse 'current' json object";
        current.lastUpdated = jCurrent.optString("last_updated");
        current.lastUpdatedEpoch = jCurrent.optInt("last_updated_epoch");
        current.tempC = jCurrent.optDouble("temp_c");
        current.tempF = jCurrent.optDouble("temp_c");
        current.feelslikeC = jCurrent.optDouble("feelslike_c");
        current.feelslikeF = jCurrent.optDouble("feelslike_f");

        JSONObject condition = jCurrent.optJSONObject("condition");
        assert condition != null : "Could not parse 'condition' json object";
        current.condition = condition.optString("text");
        current.conditionIcon = condition.optString("icon");
        current.conditionCode = condition.optInt("code");

        current.windMph = jCurrent.optDouble("wind_mph");
        current.windKph= jCurrent.optDouble("wind_kph");
        current.windDegree = jCurrent.optInt("wind_degree");
        current.windDir = jCurrent.optString("wind_dir");
        current.pressureMb = jCurrent.optDouble("pressure_mb");
        current.pressureIn = jCurrent.optDouble("pressure_in");
        current.humidity = jCurrent.optInt("humidity");
        current.cloud = jCurrent.optInt("cloud");
        current.isDay = jCurrent.optInt("is_day");
        current.uv = jCurrent.optDouble("uv");
        current.gustMph = jCurrent.optDouble("gust_mph");
        current.gustKph = jCurrent.optDouble("gust_kph");
        current.precipMm = jCurrent.optDouble("precip_mm");
        current.precipIn = jCurrent.optDouble("precip_in");
        current.visKm = jCurrent.optDouble("vis_km");
        current.visMiles = jCurrent.optDouble("vis_miles");

        JSONObject jLocation = jWeather.optJSONObject("location");
        assert jLocation != null : "Could not parse 'loaction' json object";
        location.country = jLocation.optString("country");
        location.region = jLocation.optString("region");
        location.name = jLocation.optString("name");
        location.localtime = jLocation.optString("localtime");
        location.localtimeEpoch = jLocation.optInt("localtime_epoch");
        location.lat = jLocation.optDouble("lat");
        location.lon = jLocation.optDouble("lon");
        location.tzId = jLocation.optString("tz_id");

        return this;
    }

    public void saveToFile(Context context, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WeatherData loadFromFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            WeatherData weatherData = (WeatherData) is.readObject();
            is.close();
            fis.close();
            return weatherData;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
 */

public class WeatherData implements Serializable {
    private static final long serialVersionUID = -1055581976002586621L;

    public Current current = new Current();
    public Location location = new Location();
    public ForecatsDay[] forecatsDays = new ForecatsDay[3];
    public int unit;
    
    public class Current implements Serializable {
        public int lastUpdatedEpoch;
        public String lastUpdated;
        public double temp;
        public int isDay;
        public String condition;
        public String conditionIconUrl;
        public int conditionCode;
        public double wind;
        public String windDir;
        public double pressure;
        public double precip;
        public int humidity;
        public int cloud;
        public double feelslike;
        public double vis;
        public double uv;
        public double gust;
        public transient Bitmap icon;

        /* custom serialization */
        public final void writeObject(ObjectOutputStream outS) throws IOException {
            outS.defaultWriteObject();
            icon.compress(Bitmap.CompressFormat.PNG, 100, outS);
        }

        public final void readObject(ObjectInputStream inS) throws IOException, ClassNotFoundException {
            icon = BitmapFactory.decodeStream(inS);
            inS.defaultReadObject();
        }
        /* -------------------- */
    }
    
    public class Location implements Serializable {
        public String name;
        public String region;
        public String country;
        public double lat;
        public double lon;
        public String tzId;
        public int localtimeEpoch;
        public String localtime;
    }

    public class ForecatsDay implements Serializable {
        public String date;
        public int dateEpoch;
        public double maxTemp;
        public double minTemp;
        public double humidity;
        public String iconUrl;
        public transient Bitmap icon;

        public final void writeObject(ObjectOutputStream outS) throws IOException {
            outS.defaultWriteObject();
            icon.compress(Bitmap.CompressFormat.PNG, 100, outS);
        }

        public final void readObject(ObjectInputStream inS) throws IOException, ClassNotFoundException {
            inS.defaultReadObject();
            icon = BitmapFactory.decodeStream(inS);
        }
    }

    public WeatherData parseFromJson(JSONObject jWeather, int unit) {
        try {
            JSONObject jCurrent = jWeather.getJSONObject("current");
            current.lastUpdated = jCurrent.getString("last_updated");
            current.lastUpdatedEpoch = jCurrent.getInt("last_updated_epoch");
            current.windDir = jCurrent.getString("wind_dir");
            current.humidity = jCurrent.getInt("humidity");
            current.cloud = jCurrent.getInt("cloud");
            current.isDay = jCurrent.getInt("is_day");
            current.uv = jCurrent.getDouble("uv");
            JSONObject condition = jCurrent.getJSONObject("condition");
            current.condition = condition.getString("text");
            current.conditionIconUrl = "http:" + condition.getString("icon");
            current.conditionCode = condition.getInt("code");

            if(unit == Consts.UNIT_METRICS) {
                current.temp = jCurrent.getDouble("temp_c");
                current.feelslike = jCurrent.getDouble("feelslike_c");
                current.pressure = jCurrent.getDouble("pressure_mb");
                current.gust = jCurrent.getDouble("gust_kph");
                current.precip = jCurrent.getDouble("precip_mm");
                current.wind= jCurrent.getDouble("wind_kph");
                current.vis = jCurrent.getDouble("vis_km");
            } else {
                current.temp = jCurrent.getDouble("temp_c");
                current.feelslike = jCurrent.getDouble("feelslike_f");
                current.wind = jCurrent.getDouble("wind_mph");
                current.pressure = jCurrent.getDouble("pressure_in");
                current.gust = jCurrent.getDouble("gust_mph");
                current.precip = jCurrent.getDouble("precip_in");
                current.vis = jCurrent.getDouble("vis_miles");
            }

            JSONObject jLocation = jWeather.getJSONObject("location");
            location.country = jLocation.getString("country");
            location.region = jLocation.getString("region");
            location.name = jLocation.getString("name");
            location.localtime = jLocation.getString("localtime");
            location.localtimeEpoch = jLocation.getInt("localtime_epoch");
            location.lat = jLocation.getDouble("lat");
            location.lon = jLocation.getDouble("lon");
            location.tzId = jLocation.getString("tz_id");

            JSONArray jForecasts = jWeather.getJSONObject("forecast").getJSONArray("forecastday");
            for(int i = 0; i < forecatsDays.length; ++i) {
                JSONObject jForecast = jForecasts.getJSONObject(i);
                forecatsDays[i] = new ForecatsDay();
                forecatsDays[i].date = jForecast.getString("date");
                forecatsDays[i].dateEpoch = jForecast.getInt("date_epoch");
                JSONObject jDay = jForecast.getJSONObject("day");
                forecatsDays[i].humidity = jDay.getInt("avghumidity");
                if (unit == Consts.UNIT_METRICS) {
                    forecatsDays[i].maxTemp = jDay.getDouble("maxtemp_c");
                    forecatsDays[i].minTemp = jDay.getDouble("mintemp_c");
                } else {
                    forecatsDays[i].maxTemp = jDay.getDouble("maxtemp_f");
                    forecatsDays[i].minTemp = jDay.getDouble("mintemp_f");
                }
                forecatsDays[i].iconUrl = "http:" + jDay.getJSONObject("condition").getString("icon");
            }
            this.unit = unit;
            return this;

        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveToFile(Context context, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WeatherData loadFromFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            WeatherData weatherData = (WeatherData) is.readObject();
            is.close();
            fis.close();
            return weatherData;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

/*
"forecast":{
      "forecastday":[
         {
            "date":"2020-05-12",
            "date_epoch":1589241600,
            "day":{
               "maxtemp_c":14.4,
               "maxtemp_f":57.9,
               "mintemp_c":6.2,
               "mintemp_f":43.2,
               "avgtemp_c":10.1,
               "avgtemp_f":50.2,
               "maxwind_mph":14.5,
               "maxwind_kph":23.4,
               "totalprecip_mm":0.0,
               "totalprecip_in":0.0,
               "avgvis_km":10.0,
               "avgvis_miles":6.0,
               "avghumidity":47.0,
               "daily_will_it_rain":0,
               "daily_chance_of_rain":"0",
               "daily_will_it_snow":0,
               "daily_chance_of_snow":"0",
               "condition":{
                  "text":"Overcast",
                  "icon":"//cdn.weatherapi.com/weather/64x64/day/122.png",
                  "code":1009
               },
               "uv":5.8
            },
            "astro":{
               "sunrise":"06:12 AM",
               "sunset":"09:23 PM",
               "moonrise":"02:05 AM",
               "moonset":"10:24 AM"
            }
         },
         {
            "date":"2020-05-13",
            "date_epoch":1589328000,
            "day":{
               "maxtemp_c":15.9,
               "maxtemp_f":60.6,
               "mintemp_c":7.5,
               "mintemp_f":45.5,
               "avgtemp_c":11.4,
               "avgtemp_f":52.6,
               "maxwind_mph":16.8,
               "maxwind_kph":27.0,
               "totalprecip_mm":0.0,
               "totalprecip_in":0.0,
               "avgvis_km":10.0,
               "avgvis_miles":6.0,
               "avghumidity":52.0,
               "daily_will_it_rain":0,
               "daily_chance_of_rain":"0",
               "daily_will_it_snow":0,
               "daily_chance_of_snow":"0",
               "condition":{
                  "text":"Partly cloudy",
                  "icon":"//cdn.weatherapi.com/weather/64x64/day/116.png",
                  "code":1003
               },
               "uv":6.2
            },
            "astro":{
               "sunrise":"06:11 AM",
               "sunset":"09:24 PM",
               "moonrise":"02:48 AM",
               "moonset":"11:30 AM"
            }
         },
         {
            "date":"2020-05-14",
            "date_epoch":1589414400,
            "day":{
               "maxtemp_c":11.1,
               "maxtemp_f":52.0,
               "mintemp_c":5.1,
               "mintemp_f":41.2,
               "avgtemp_c":8.8,
               "avgtemp_f":47.9,
               "maxwind_mph":17.0,
               "maxwind_kph":27.4,
               "totalprecip_mm":0.0,
               "totalprecip_in":0.0,
               "avgvis_km":10.0,
               "avgvis_miles":6.0,
               "avghumidity":52.0,
               "daily_will_it_rain":0,
               "daily_chance_of_rain":"0",
               "daily_will_it_snow":0,
               "daily_chance_of_snow":"0",
               "condition":{
                  "text":"Overcast",
                  "icon":"//cdn.weatherapi.com/weather/64x64/day/122.png",
                  "code":1009
               },
               "uv":2.6
            },
 */