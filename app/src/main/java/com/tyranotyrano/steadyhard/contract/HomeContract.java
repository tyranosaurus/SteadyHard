package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.adapter.SteadyProjectAdapterContract;
import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;

/**
 * Created by cyj on 2017-11-24.
 */

public interface HomeContract {
    // HomeFragment 관련 View 처리
    interface View extends BaseView {
        /** 임시로 넣은 것 : 삭제할 것.*/
        void showSnackBar(String message);
    }

    // HomeFragment 관련 Presenter 처리
    interface Presenter extends BasePresenter<HomeContract.View> {
        void setSteadyProjectAdapterView(SteadyProjectAdapterContract.View adapterView);
        void setSteadyProjectAdapterModel(SteadyProjectAdapterContract.Model adapterModel);
        void setSteadyProjectAdapterOnItemClickListener();
    }
}
