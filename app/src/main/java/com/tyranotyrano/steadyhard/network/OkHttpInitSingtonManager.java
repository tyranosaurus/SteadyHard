package com.tyranotyrano.steadyhard.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by cyj on 2017-11-27.
 */

public class OkHttpInitSingtonManager {
    private static OkHttpClient okHttpClient;

    static {
        // 최대 10초 동안 연결
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private OkHttpInitSingtonManager() {
        // Singleton Pattern
    }

    public static OkHttpClient getOkHttpClient(){
        if ( okHttpClient != null ) {
            return okHttpClient;
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}
