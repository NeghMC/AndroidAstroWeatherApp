package com.example.astroweather2.utils;

import com.example.astroweather2.datatypes.WeatherData;

import java.util.concurrent.ArrayBlockingQueue;

public class NullableArrayBlockingQueue<T> {
    ArrayBlockingQueue<Wrapper> queue;
    public NullableArrayBlockingQueue(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }

    public boolean add(T o) {
        return queue.add(new Wrapper(o));
    }

    public T take() {
        try {
            return (T)queue.take().unwrap();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    class Wrapper {
        private Object object;
        public Wrapper(Object object) {
            this.object = object;
        }
        public Wrapper() {
            this(null);
        }
        public Object unwrap() {
            return object;
        }
    }
}