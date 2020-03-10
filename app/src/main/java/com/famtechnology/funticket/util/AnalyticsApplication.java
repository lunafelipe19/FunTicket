package com.famtechnology.funticket.util;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.google.android.gms.analytics.Tracker;

/**
 * Created by ailtonramos on 03/08/2017.<br>
 *  A custom classe to start analytics component on that application
 */
public class AnalyticsApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
