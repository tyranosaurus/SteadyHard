package com.tyranotyrano.steadyhard.presenter;

import android.os.AsyncTask;

import com.tyranotyrano.steadyhard.contract.ContentByProjectContract;
import com.tyranotyrano.steadyhard.contract.adapter.ContentByProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentByProjectDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cyj on 2017-12-08.
 */

public class ContentByProjectPresenter implements ContentByProjectContract.Presenter, ContentByProjectAdapterContract.OnContentByProjectClickListener {
    ContentByProjectContract.View mView = null;
    ContentByProjectDataSource mRepository = null;

    ContentByProjectAdapterContract.View mAdapterView = null;
    ContentByProjectAdapterContract.Model mAdapterModel = null;

    public void attachView(ContentByProjectContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setContentByProjectRepository(ContentByProjectDataSource contentByProjectDataSource) {
        if ( contentByProjectDataSource != null ) {
            this.mRepository = contentByProjectDataSource;
        }
    }

    @Override
    public void setContentByProjectAdapterView(ContentByProjectAdapterContract.View adapterView) {
        if ( adapterView != null ) {
            this.mAdapterView = adapterView;
        }
    }

    @Override
    public void setContentByProjectAdapterModel(ContentByProjectAdapterContract.Model adapterModel) {
        if ( adapterModel != null ) {
            this.mAdapterModel = adapterModel;
        }
    }

    @Override
    public void setContentByProjectAdapterOnItemClickListener() {
        mAdapterView.setOnContentByProjectClickListener(this);
    }

    @Override
    public void onContentByProjectClick(int position) {
        SteadyContent item = mAdapterModel.getSteadyContentItem(position);

        // 임시
        mView.showSnackBar(item.getNo()+"");
    }

    @Override
    public void getContentByProject(int projectNo) {
        new ContentByProjectGetTask().execute(projectNo);
    }

    public class ContentByProjectGetTask extends AsyncTask<Integer, Integer, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Map<String, Object> doInBackground(Integer... params) {
            int projectNo = params[0];

            Map<String, Object> map = mRepository.getContentsByProjectNo(projectNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);

            if ( map != null ) {
                if ( (boolean)map.get("result") ) {
                    // 데이터 제대로 잘 가지고 온 경우
                    List<SteadyContent> steadyContentList = new ArrayList<>();

                    if ( map.get("steadyContentList") instanceof List) {
                        steadyContentList.addAll((List<SteadyContent>) map.get("steadyContentList"));
                    }

                    mAdapterModel.clearAdapter();

                    for ( SteadyContent item : steadyContentList ) {
                        mAdapterModel.addItem(item);
                    }

                    mAdapterView.notifyAdapter();

                    mView.showContentByProjectLayout();
                } else {
                    // 데이터 제대로 못 가지고 온 경우
                    String message = "콘텐츠를 가져오는데 실패했습니다. 잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                // 통신 실패
                String message = "인터넷 연결이 원활하지 않습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
