package com.example.astroweather2.utils;

import com.astrocalculator.AstroDateTime;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateManager {
    public static Calendar convertToCalendar(AstroDateTime date) {
        return new GregorianCalendar(
                date.getYear(),
                date.getMonth() - 1,
                date.getYear(),
                date.getHour(),
                date.getMinute(),
                date.getSecond()
        );
    }

    public static AstroDateTime convertToAstroDate(Calendar calendar) {
        return new AstroDateTime(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                2,//calendar.get(Calendar.ZONE_OFFSET)/1000/3600,
                false
        );
    }

    public static String AstroDateToString(AstroDateTime date) {
        return new DateFormatSymbols().getShortMonths()[date.getMonth()-1] + " " + date.getDay() + ", " + date.getYear();
    }

    public static String AstroTimeToString(AstroDateTime date) {
        return timeValue(date.getHour()) + ":" + timeValue(date.getMinute()) + ":" + timeValue(date.getSecond());
    }

    private static String timeValue(int val) {
        if(val < 10)
            return "0" + val;
        else
            return "" + val;
    }
}