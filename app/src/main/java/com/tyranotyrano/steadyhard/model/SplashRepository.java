package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.SplashDataSource;

/**
 * Created by cyj on 2017-11-28.
 */

public class SplashRepository implements SplashDataSource {

    private SplashDataSource mSplashRemoteDataSource = null;

    public SplashRepository(SplashDataSource splashRemoteDataSource) {
        if ( splashRemoteDataSource != null ) {
            this.mSplashRemoteDataSource = splashRemoteDataSource;
        }
    }

    @Override
    public String startAutoLogin(String token) {
        String cookie = mSplashRemoteDataSource.startAutoLogin(token);

        return cookie;
    }
}
