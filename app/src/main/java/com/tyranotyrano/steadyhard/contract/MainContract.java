package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.MainDataSource;

/**
 * Created by cyj on 2017-11-28.
 */

public interface MainContract {
    // MainActivity 관련 View 처리
    interface View extends BaseView {
        void clearCookie();
        void clearUserInfo();
    }

    // MainActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<MainContract.View> {
        // Model 관련 처리
        void setMainRepository(MainDataSource mainDataSource);

        void clearSessionToken(String token);
        void clearUserInfo();
    }
}
