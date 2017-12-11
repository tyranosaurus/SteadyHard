package com.tyranotyrano.steadyhard.contract;

import android.support.v7.app.AlertDialog;

import com.tyranotyrano.steadyhard.contract.adapter.SteadyProjectAdapterContract;
import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.HomeDataSource;

/**
 * Created by cyj on 2017-11-24.
 */

public interface HomeContract {
    // HomeFragment 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void showSteadyProjectsLayout();
        void callContentByProjectActivity(SteadyProject steadyProject, int position);
    }

    // HomeFragment 관련 Presenter 처리
    interface Presenter extends BasePresenter<HomeContract.View> {
        // Model 관련 처리
        void setHomeRepository(HomeDataSource homeDataSource);

        void setSteadyProjectAdapterView(SteadyProjectAdapterContract.View adapterView);
        void setSteadyProjectAdapterModel(SteadyProjectAdapterContract.Model adapterModel);
        void setSteadyProjectAdapterOnItemClickListener();
        void getSteadyProjects();
        AlertDialog.Builder buildDeleteAlertDialog(AlertDialog.Builder builder, int position);
    }
}
