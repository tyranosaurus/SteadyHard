package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;

/**
 * Created by cyj on 2017-11-24.
 */

public interface LoginContract {
    // LoginActivity 관련 View 처리
   interface View extends BaseView {
        void callActivity(int callCode);
    }

    // LoginActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<LoginContract.View> {
        void callActivity(int callCode);
    }
}
