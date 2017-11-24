package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.adapter.SteadyContentAdapterContract;
import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;

/**
 * Created by cyj on 2017-11-24.
 */

public interface ContentContract {
    // ContentFragment 관련 View 처리
    interface View extends BaseView {
        /** 임시로 넣은 것 : 삭제할 것.*/
        void showSnackBar(String message);
    }

    // ContentFragment 관련 Presenter 처리
    interface Presenter extends BasePresenter<ContentContract.View> {
        void setSteadyContentAdapterView(SteadyContentAdapterContract.View adapterView);
        void setSteadyContentAdapterModel(SteadyContentAdapterContract.Model adapterModel);
        void setSteadyContentAdapterOnItemClickListener();
    }
}
