package com.tyranotyrano.steadyhard.model.remote.datasource;

/**
 * Created by cyj on 2017-11-30.
 */

public interface JoinDataSource {
    boolean isEmailDuplication(String email);
    boolean createNewUser(String email, String password);
    String uploadProfileImage(String imagePath);
    boolean saveProfileInfo(String userProfileImagePath, String nickname, String email);
}
