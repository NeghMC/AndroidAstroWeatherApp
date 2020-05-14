package com.example.astroweather2.utils;

import android.os.Handler;

import androidx.annotation.Nullable;

public interface ResultCallback<T> {
    void onSuccess(T result, @Nullable String warning);
    void onFailture(String msg);
}