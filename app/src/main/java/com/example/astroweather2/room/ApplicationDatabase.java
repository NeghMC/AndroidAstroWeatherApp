package com.example.astroweather2.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.astroweather2.datatypes.GeoLocation;
import com.example.astroweather2.datatypes.WeatherData;

@Database(entities = {GeoLocation.class}, version = 6)
@TypeConverters(Converters.class)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase instance;

    public abstract LocationDao locationDao();

    public static synchronized void initialise(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ApplicationDatabase.class, "location_database")
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)
                    .build();
        }
    }

    public static ApplicationDatabase getInstance() {
        return instance;
    }
}