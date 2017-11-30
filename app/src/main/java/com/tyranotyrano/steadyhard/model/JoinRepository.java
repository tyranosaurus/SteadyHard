package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.JoinDataSource;

/**
 * Created by cyj on 2017-11-30.
 */

public class JoinRepository implements JoinDataSource {
    private JoinDataSource mJoinRemoteDataSource = null;

    public JoinRepository(JoinDataSource JoinRemoteDataSource) {
        if ( JoinRemoteDataSource != null ) {
            this.mJoinRemoteDataSource = JoinRemoteDataSource;
        }
    }

    @Override
    public boolean isEmailDuplication(String email) {
        boolean isEmailDuplication = mJoinRemoteDataSource.isEmailDuplication(email);

        return isEmailDuplication;
    }

    @Override
    public boolean createNewUser(String email, String password) {
        boolean isJoinSuccess = mJoinRemoteDataSource.createNewUser(email, password);

        return isJoinSuccess;
    }

    @Override
    public String uploadProfileImage(String imagePath) {
        String userProfileImagePath = mJoinRemoteDataSource.uploadProfileImage(imagePath);

        return userProfileImagePath;
    }

    @Override
    public boolean saveProfileInfo(String userProfileImagePath, String nickname, String email) {
        boolean saveResult = mJoinRemoteDataSource.saveProfileInfo(userProfileImagePath, nickname, email);

        return saveResult;
    }
}
