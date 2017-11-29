package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.User;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;

/**
 * Created by cyj on 2017-11-24.
 */

public interface LoginContract {
    // LoginActivity 관련 View 처리
    interface View extends BaseView {
        void callActivity(int callCode);
        void showSnackBar(String message);
        void setLoginSharedpreferences(User user);
    }

    // LoginActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<LoginContract.View> {
        // model 관련 처리
        void setLoginRepository(LoginDataSource loginDataSource);

        // 로그인 Email, Password를 Model에 보내 체크
        void checkLogin(String email, String password, int callCode);
        // Activity 호출
        void callActivity(int callCode);
    }
}
