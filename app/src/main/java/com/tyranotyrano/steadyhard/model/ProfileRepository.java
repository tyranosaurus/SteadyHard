package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.ProfileRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;

/**
 * Created by cyj on 2017-11-29.
 */

public class ProfileRepository implements ProfileDataSource {
    private ProfileDataSource mProfileRemoteDataSource = null;

    public ProfileRepository(ProfileRemoteDataSource profileRemoteDataSource) {
        this.mProfileRemoteDataSource = profileRemoteDataSource;
    }

    @Override
    public boolean clearSessionToken(String token) {
        boolean isSessionLogout = mProfileRemoteDataSource.clearSessionToken(token);

        return isSessionLogout;
    }
}
