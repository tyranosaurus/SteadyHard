package com.tyranotyrano.steadyhard.presenter;

import com.tyranotyrano.steadyhard.contract.ContentContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyContentAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;

/**
 * Created by cyj on 2017-11-24.
 */

public class ContentPresenter implements ContentContract.Presenter, SteadyContentAdapterContract.OnItemClickListener {
    ContentContract.View mView = null;
    SteadyContentAdapterContract.View adapterView = null;
    SteadyContentAdapterContract.Model adapterModel = null;

    @Override
    public void attachView(ContentContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setSteadyContentAdapterView(SteadyContentAdapterContract.View adapterView) {
        if ( adapterView != null ) {
            this.adapterView = adapterView;
        }
    }

    @Override
    public void setSteadyContentAdapterModel(SteadyContentAdapterContract.Model adapterModel) {
        if ( adapterModel != null ) {
            this.adapterModel = adapterModel;
        }
    }

    @Override
    public void setSteadyContentAdapterOnItemClickListener() {
        adapterView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        SteadyContent item = adapterModel.getItem(position);

        // 임시로 넣은 것 : 삭제할 것
        mView.showSnackBar(item.getContentText());
    }
}
