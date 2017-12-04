package com.tyranotyrano.steadyhard.model.remote.datasource;

/**
 * Created by cyj on 2017-12-04.
 */

public interface ProfileManagerDataSource {
    boolean deleteUser(String deletePassword);
    String uploadNewProfileImage(String imagePath);
    boolean deletedNewProfileImage(String deleteFileName);
    boolean updateNewProfile(String newProfileImagePath, String newNickname, String originalPassword, String newPassword);
}
