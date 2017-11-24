package com.tyranotyrano.steadyhard.presenter;

import com.tyranotyrano.steadyhard.contract.LoginContract;

/**
 * Created by cyj on 2017-11-24.
 */

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View mView = null;

    @Override
    public void attachView(LoginContract.View view) {
        if ( view != null ) {
            mView = view;
        }
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void callActivity(int callCode) {
        mView.callActivity(callCode);
    }
}
