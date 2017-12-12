package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ProfileContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;

import java.util.List;
import java.util.Map;

/**
 * Created by cyj on 2017-11-29.
 */

public class ProfilePresenter implements ProfileContract.Presenter {
    ProfileContract.View mView = null;
    ProfileDataSource mRepository = null;

    public void attachView(ProfileContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setProfileRepository(ProfileDataSource profileDataSource) {
        if ( profileDataSource != null ) {
            this.mRepository = profileDataSource;
        }
    }

    @Override
    public void clearSessionToken(String token) {
        new SessionLogoutTask().execute(token);
    }

    @Override
    public void getSteadyProjectStatusCount() {
        new ProjectStatusGetCountTask().execute();
    }

    public class SessionLogoutTask extends AsyncTask<String, Integer, Boolean> {
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
        protected Boolean doInBackground(String... params) {
            String token = params[0];
            boolean isSessionLogout = mRepository.clearSessionToken(token);

            return isSessionLogout;
        }

        @Override
        protected void onPostExecute(Boolean isSessionLogout) {
            super.onPostExecute(isSessionLogout);
            progressDialog.dismiss();

            if ( isSessionLogout ) {
                mView.clearCookie();
                mView.clearUserInfo();
                mView.callLoginActivity();
                mView.turnOffAutoLogin();
            } else {
                // SessionLogout에 실패했을 경우
                Log.e("clearSessionToken()","There is no Token value for Session Logout.");
            }
        }
    }

    public class ProjectStatusGetCountTask extends AsyncTask<Void, Integer, Map<String, Object>> {
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
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String, Object> map = mRepository.getSteadyProjectStatusCount();
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

            if ( map != null ) {
                // 성공
                int success = (int)((List)map.get("projectStatusCount")).get(0);
                int ongoing = (int)((List)map.get("projectStatusCount")).get(1);
                int fail = (int)((List)map.get("projectStatusCount")).get(2);

                mView.drawSteadyProjectPieChart(success, ongoing, fail);
            } else {
                // 실패
                String message = "파이차트 정보를 가져오는데 실패했습니다.";
                mView.showSnackBar(message);
            }
        }
    }
}
