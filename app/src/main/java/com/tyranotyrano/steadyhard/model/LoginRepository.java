package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.LoginRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;

/**
 * Created by cyj on 2017-11-26.
 */

public class LoginRepository implements LoginDataSource {

    private LoginDataSource mLoginRemoteDataSource = null;

    public LoginRepository(LoginRemoteDataSource loginRemoteDataSource) {
        if ( loginRemoteDataSource != null ) {
            this.mLoginRemoteDataSource = loginRemoteDataSource;
        }
    }

    @Override
    public boolean checkLogin(String email, String password) {
        boolean isLogin = mLoginRemoteDataSource.checkLogin(email, password);

        return isLogin;
    }
}
