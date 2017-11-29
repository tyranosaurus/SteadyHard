package com.tyranotyrano.steadyhard.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;

/**
 * Created by cyj on 2017-11-22.
 */

public class SteadyHardApplication extends Application {
    private static Context mContext;
    private static String mCookie = "";

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getSteadyHardContext() {

        if ( mContext == null ) {
            Log.e("CONTEXT_ERROR", "SteadyHard application Context is null");

            return null;
        }

        return mContext;
    }

    public static void setCookie(String cookie) {
        if ( cookie != null ) {
            mCookie = cookie;
        }
    }

    public static String getCookie() {
        return mCookie;
    }

    public static void clearCookie() {
        mCookie = "";
    }

    /** Glide 사용시 OOM 처리 */
    @Override public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }
}
