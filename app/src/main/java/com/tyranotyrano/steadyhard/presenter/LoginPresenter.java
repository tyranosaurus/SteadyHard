package com.tyranotyrano.steadyhard.presenter;

import android.content.Intent;

import com.tyranotyrano.steadyhard.contract.LoginContract;

/**
 * Created by cyj on 2017-11-24.
 */

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View mView = null;

    @Override
    public void setView(LoginContract.View view) {
        if ( view != null ) {
            mView = view;
        }
    }

    @Override
    public void callActivity(Intent intent) {
        mView.callActivity(intent);
    }
}
