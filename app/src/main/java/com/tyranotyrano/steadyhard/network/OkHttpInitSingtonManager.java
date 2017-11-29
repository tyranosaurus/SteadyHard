package com.tyranotyrano.steadyhard.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpInitSingtonManager {
    private static OkHttpClient okHttpClient = null;

    static {
        // 최대 20초 동안 연결
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
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
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
}
