package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;

/**
 * Created by cyj on 2017-12-04.
 */

public class ProfileManagerRepository implements ProfileManagerDataSource {

    private ProfileManagerDataSource mProfileManagerRemoteDataSource;

    public ProfileManagerRepository(ProfileManagerDataSource profileManagerRemoteDataSource) {
        if ( profileManagerRemoteDataSource != null ) {
            this.mProfileManagerRemoteDataSource = profileManagerRemoteDataSource;
        }
    }

    @Override
    public boolean deleteUser(String deletePassword) {
        boolean deleteResult = mProfileManagerRemoteDataSource.deleteUser(deletePassword);

        return deleteResult;
    }
}
