package com.tyranotyrano.steadyhard.contract;

import android.content.Context;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.SplashDataSource;

/**
 * Created by cyj on 2017-11-28.
 */

public interface SplashContract {
    // SplashActivity 관련 View 처리
    interface View extends BaseView {
        void callActivity(int callCode);
        void setCookieSharedpreferences(String cookie);
        Context getActivityContext();
    }

    // SplashActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<SplashContract.View> {
        // model 관련 처리
        void setSplashRepository(SplashDataSource splashDataSource);

        void startAutoLogin(String token);
        void callActivity(int callCode);
    }
}
