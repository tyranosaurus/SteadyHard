package com.tyranotyrano.steadyhard.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.tyranotyrano.steadyhard.contract.ProfileContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;

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

    public class SessionLogoutTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
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
}
