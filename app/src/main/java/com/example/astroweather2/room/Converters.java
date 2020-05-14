package com.example.astroweather2.room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Bitmap bitmapFromArray(byte[] value) {
        return value == null ? null : BitmapFactory.decodeByteArray(value, 0, value.length);
    }

    @TypeConverter
    public static byte[] bitmapToArray(Bitmap value) {
        if(value == null)
            return null;
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
    }
}
