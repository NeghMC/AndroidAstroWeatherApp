package com.example.astroweather2.utils;

import android.os.Handler;

public abstract class ResultCallbackWithHandler<T> {
    private Handler handler;
    public ResultCallbackWithHandler(Handler handler) {
        this.handler = handler;
    }
    public ResultCallbackWithHandler() {
        this(null);
    }
    public Handler getHandler() {
        return handler;
    }
    protected abstract void onSuccess(T result, String warning);
    protected abstract void onFailture(String msg);
    public void notifySuccess(final T result, final String warning) {
        if(handler == null)
            onSuccess(result, warning);
        else
            handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(result, warning);
            }
        });
    }
    public void notifyFailture(final String msg) {
        if(handler == null)
            onFailture(msg);
        else
            handler.post(new Runnable() {
            @Override
            public void run() {
                onFailture(msg);
            }
        });
    }
}
