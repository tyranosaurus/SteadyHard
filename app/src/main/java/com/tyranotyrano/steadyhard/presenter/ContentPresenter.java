package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ContentContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyContentAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentDataSource;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cyj on 2017-11-24.
 */

public class ContentPresenter implements ContentContract.Presenter, SteadyContentAdapterContract.OnSteadyContentClickListener {
    ContentContract.View mView = null;
    ContentDataSource mRepository = null;

    SteadyContentAdapterContract.View mAdapterView = null;
    SteadyContentAdapterContract.Model mAdapterModel = null;

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
    public void setContentRepository(ContentDataSource contentDataSource) {
        if ( contentDataSource != null ) {
            this.mRepository = contentDataSource;
        }
    }

    @Override
    public void setSteadyContentAdapterView(SteadyContentAdapterContract.View adapterView) {
        if ( adapterView != null ) {
            this.mAdapterView = adapterView;
        }
    }

    @Override
    public void setSteadyContentAdapterModel(SteadyContentAdapterContract.Model adapterModel) {
        if ( adapterModel != null ) {
            this.mAdapterModel = adapterModel;
        }
    }

    @Override
    public void setSteadyContentAdapterOnItemClickListener() {
        mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void onSteadyContentClick(int position) {
        SteadyContent item = mAdapterModel.getSteadyContentItem(position);
    }

    @Override
    public void getContents() {
        new SteadyContentsGetTask().execute(MainActivity.user.getNo());
    }

    public class SteadyContentsGetTask extends AsyncTask<Integer, Integer, Map<String, Object>> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected Map<String, Object> doInBackground(Integer... params) {
            int userNo = params[0];

            Map<String, Object> map = mRepository.getSteadyContentsByUserNo(userNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

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

                    mView.showSteadyContentsLayout();
                } else {
                    // 데이터 제대로 못 가지고 온 경우
                    String message = "오늘의 꾸준함을 가져오는데 실패했습니다. 잠시후 다시 시도해주세요.";
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
