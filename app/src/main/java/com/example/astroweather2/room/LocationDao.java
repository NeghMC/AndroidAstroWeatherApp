package com.example.astroweather2.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.astroweather2.datatypes.GeoLocation;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface LocationDao { // sql operations
    @Insert
    void insert(GeoLocation location);

    @Update
    void update(GeoLocation location);

    @Delete
    void delete(GeoLocation location);

    @Query("DELETE FROM GeoLocation")
    void deleteAll();

    @Query("SELECT * FROM Geolocation WHERE Geolocation.id=:id")
    GeoLocation getById(int id);

    @Query("SELECT * FROM GeoLocation")
    List<GeoLocation> getAll();
}

