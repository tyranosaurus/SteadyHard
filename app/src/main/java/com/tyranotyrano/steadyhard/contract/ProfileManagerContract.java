package com.tyranotyrano.steadyhard.contract;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;

/**
 * Created by cyj on 2017-12-04.
 */

public interface ProfileManagerContract {
    // ProfileManagerActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void clearSharedPreferencesData();
        void callLoginActivity();
        void selectPictureByGallery();
        void setOriginalProfileImage();
        void setNewProfileImagePath(String newProfileImagePath);
        void updatedNewProfileInfo();
        Context getActivityContext();
    }

    // ProfileManagerActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<ProfileManagerContract.View> {
        // model 관련 처리
        void setProfileManagerRepository(ProfileManagerDataSource profileManagerDataSource);

        AlertDialog.Builder buildAlertDialog(AlertDialog.Builder builder, EditText edittextInputPassword);
        void selectPictureByGallery();
        void uploadNewProfileImage(String projectImagePath);
        void deleteNewProfileImage(String deleteFileName);
        void updateNewProfile(String newProfileImagePath, String newNickname, String originalPassword, String newPasswordFirst, String newPasswordSecond);
    }
}
