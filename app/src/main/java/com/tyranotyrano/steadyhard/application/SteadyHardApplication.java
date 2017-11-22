package com.tyranotyrano.steadyhard.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by cyj on 2017-11-22.
 */

public class SteadyHardApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getTestContext() {

        if ( mContext == null ) {
            Log.e("CONTEXT_ERROR", "Test application Context is null");

            return null;
        }

        return mContext;
    }
}
