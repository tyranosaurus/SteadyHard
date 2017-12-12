package com.tyranotyrano.steadyhard.contract;

import android.content.Context;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;

/**
 * Created by cyj on 2017-11-29.
 */

public interface ProfileContract {
    // ProfileFragment 관련 View 처리
    interface View extends BaseView {
        void clearCookie();
        void clearUserInfo();
        void callLoginActivity();
        void turnOffAutoLogin();
        void showSnackBar(String message);
        void drawSteadyProjectPieChart(int success, int ongoing, int fail);
        Context getActivityContext();
        void refreshProfileFragment();
    }

    // ProfileFragment 관련 View 처리
    interface Presenter extends BasePresenter<ProfileContract.View> {
        // Model 관련 처리
        void setProfileRepository(ProfileDataSource profileDataSource);

        void clearSessionToken(String token);
        void getSteadyProjectStatusCount();
    }
}
