package com.example.astroweather2.webservice;

import android.app.Application;
import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WebServiceQueue {
    private RequestQueue queue;
    private static WebServiceQueue instance;

    private WebServiceQueue(Application appContext) {
        queue = Volley.newRequestQueue(appContext);
    }

    public static synchronized void initialise(Application appContext) {
        if(instance == null) {
            instance = new WebServiceQueue(appContext);
        }
    }

    public static synchronized WebServiceQueue getInstance() {
        return instance;
    }

    public void sendRequest(Request req) {
        queue.add(req);
    }
}
