package com.tyranotyrano.steadyhard.contract;

import android.content.Context;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.JoinDataSource;

/**
 * Created by cyj on 2017-11-30.
 */

public interface JoinContract {
    // JoinActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void selectPictureByGallery();
        void checkedEmailDuplication();
        void joinedNewUser();
        void showProfileSettings();
        void setUserProfileImagePath(String userProfileImagePath);
        void setDefaultProfileImage();
        void finishJoinActivity(String message);
        Context getActivityContext();
    }

    // JoinActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<JoinContract.View> {
        // model 관련 처리
        void setJoinRepository(JoinDataSource joinDataSource);

        void checkEmailDuplication(String email);
        void createNewUser(String email, String passwordFirst, String passwordSecond);
        void selectPictureByGallery();
        void uploadProfileImage(String profileImagePath);
        void saveProfileInfo(String userProfileImagePath , String nickname, String email);
    }
}
