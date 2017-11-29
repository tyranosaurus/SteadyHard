package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.MainRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.MainDataSource;

/**
 * Created by cyj on 2017-11-28.
 */

public class MainRepository implements MainDataSource {

    private MainDataSource mMainRemoteDataSource = null;

    public MainRepository(MainRemoteDataSource mainRemoteDataSource) {
        if ( mainRemoteDataSource != null ) {
            this.mMainRemoteDataSource = mainRemoteDataSource;
        }
    }

    @Override
    public boolean clearSessionToken(String token) {
        boolean isSessionLogout = mMainRemoteDataSource.clearSessionToken(token);

        return isSessionLogout;
    }
}
