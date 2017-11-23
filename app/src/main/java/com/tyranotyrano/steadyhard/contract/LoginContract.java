package com.tyranotyrano.steadyhard.contract;

import android.content.Intent;

/**
 * Created by cyj on 2017-11-24.
 */

public interface LoginContract {
    // LoginActivity 관련 View 처리
   interface View extends BaseView {
        void callActivity(Intent intent);
    }

    // LoginActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<LoginContract.View> {
        void callActivity(Intent intent);
    }
}
