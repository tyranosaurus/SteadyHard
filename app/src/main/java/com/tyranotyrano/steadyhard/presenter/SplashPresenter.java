package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.SplashContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.SplashDataSource;
import com.tyranotyrano.steadyhard.view.SplashActivity;

/**
 * Created by cyj on 2017-11-28.
 */

public class SplashPresenter implements SplashContract.Presenter {
    SplashContract.View mView = null;
    SplashDataSource mRepository = null;

    public void attachView(SplashContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setSplashRepository(SplashDataSource splashDataSource) {
        if ( splashDataSource != null ) {
            this.mRepository = splashDataSource;
        }
    }

    @Override
    public void startAutoLogin(String token) {
        new AutoLoginTask().execute(token);
    }

    @Override
    public void callActivity(int callCode) {
        mView.callActivity(callCode);
    }

    public class AutoLoginTask extends AsyncTask<String, Integer, String> {
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
        protected String doInBackground(String... params) {
            String token = params[0];

            String cookie = mRepository.startAutoLogin(token);

            return cookie;
        }

        @Override
        protected void onPostExecute(String cookie) {
            super.onPostExecute(cookie);
            progressDialog.dismiss();

            if ( cookie != null ) {
                mView.setCookieSharedpreferences(cookie);
                // 자동 로그인 성공시 : 메인 액티비티 호출
                mView.callActivity(SplashActivity.CALL_CODE_ACTIVITY_MAIN);
            } else {
                // 자동 로그인 실패시 : 로그인 액티비티 호출
                mView.callActivity(SplashActivity.CALL_CODE_ACTIVITY_LOGIN);
            }
        }
    }
}
