package com.tyranotyrano.steadyhard.contract;

import android.content.Context;

import com.tyranotyrano.steadyhard.contract.adapter.ContentByProjectAdapterContract;
import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentByProjectDataSource;

/**
 * Created by cyj on 2017-12-08.
 */

public interface ContentByProjectContract {
    // ContentByProjectActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void showContentByProjectLayout();
        void refreshSteadyProjectAndToolbar(int currentDays, String lastDate, int status);
        Context getActivityContext();
    }

    // ContentByProjectActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<ContentByProjectContract.View> {
        // model 관련 처리
        void setContentByProjectRepository(ContentByProjectDataSource contentByProjectDataSource);

        void setContentByProjectAdapterView(ContentByProjectAdapterContract.View adapterView);
        void setContentByProjectAdapterModel(ContentByProjectAdapterContract.Model adapterModel);
        void setContentByProjectAdapterOnItemClickListener();
        void getContentByProject(int projectNo);
        void deleteSteadyContent(int position, SteadyProject steadyProject);
    }
}
