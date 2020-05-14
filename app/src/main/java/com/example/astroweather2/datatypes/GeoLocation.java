package com.example.astroweather2.datatypes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class GeoLocation { // sql table

    @PrimaryKey(autoGenerate = false)
    public int id;

    public String country, region, name;
    public double latitude, longitude;

    public GeoLocation(int id, String country, String region, String name, double latitude, double longitude) {
        this.id = id;
        this.country = country;
        this.region = region;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public GeoLocation() {}

    public GeoLocation toGeoLocation(JSONObject jLocation) {
        this.id = jLocation.optInt("id");
                this.country = jLocation.optString("country");
                this.region = jLocation.optString("region");
                this.name = jLocation.optString("name");
                this.latitude = jLocation.optDouble("lat");
                this.longitude = jLocation.optDouble("lon");
                return this;
    }
}


