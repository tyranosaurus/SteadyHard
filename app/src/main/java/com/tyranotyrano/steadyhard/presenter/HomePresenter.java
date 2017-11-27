package com.tyranotyrano.steadyhard.presenter;

import com.tyranotyrano.steadyhard.contract.HomeContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;

/**
 * Created by cyj on 2017-11-24.
 */

public class HomePresenter implements HomeContract.Presenter, SteadyProjectAdapterContract.OnItemClickListener {
    HomeContract.View mView = null;
    SteadyProjectAdapterContract.View adapterView = null;
    SteadyProjectAdapterContract.Model adapterModel = null;


    public void attachView(HomeContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setSteadyProjectAdapterView(SteadyProjectAdapterContract.View adapterView) {
        if ( adapterView != null ) {
            this.adapterView = adapterView;
        }
    }

    @Override
    public void setSteadyProjectAdapterModel(SteadyProjectAdapterContract.Model adapterModel) {
        if ( adapterModel != null) {
            this.adapterModel = adapterModel;
        }
    }

    @Override
    public void setSteadyProjectAdapterOnItemClickListener() {
        this.adapterView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        SteadyProject item = adapterModel.getItem(position);

        // 임시로 넣은 것 : 삭제할 것
        mView.showSnackBar(item.getProjectTitle());
    }
}
