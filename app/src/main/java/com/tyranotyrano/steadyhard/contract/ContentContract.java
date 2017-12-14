package com.tyranotyrano.steadyhard.contract;

import android.content.Context;

import com.tyranotyrano.steadyhard.contract.adapter.SteadyContentAdapterContract;
import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentDataSource;

/**
 * Created by cyj on 2017-11-24.
 */

public interface ContentContract {
    // ContentFragment 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void showSteadyContentsLayout();
        Context getActivityContext();
        void refreshContentFragment();
    }

    // ContentFragment 관련 Presenter 처리
    interface Presenter extends BasePresenter<ContentContract.View> {
        // model 관련 처리
        void setContentRepository(ContentDataSource contentDataSource);

        void setSteadyContentAdapterView(SteadyContentAdapterContract.View adapterView);
        void setSteadyContentAdapterModel(SteadyContentAdapterContract.Model adapterModel);
        void setSteadyContentAdapterOnItemClickListener();
        void getContents();
        void refreshSteadyContents();
    }
}
